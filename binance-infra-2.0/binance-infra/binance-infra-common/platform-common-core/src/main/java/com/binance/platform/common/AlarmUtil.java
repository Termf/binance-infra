package com.binance.platform.common;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.binance.master.enums.LanguageEnum;
import com.binance.master.enums.TerminalEnum;
import com.binance.master.models.APIRequest;
import com.binance.master.utils.Assert;
import com.binance.master.utils.StringUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 发送告警工具类
 *
 * @author Li Fenggang Date: 2020/11/23 8:39 上午
 */
public class AlarmUtil {
    private static final Logger log = LoggerFactory.getLogger(AlarmUtil.class);

    /**
     * 告警服务ID
     */
    private static final String SERVICE_TELEGRAM_ALARM = "TELEGRAM-ALARM";
    /**
     * API路径 - {@value}
     */
    private static final String API_PATH_SEND_TEXT = "/sendtext";

    /**
     * 告警发送线程池
     */
    private static ExecutorService alarmSendPool =
        new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100),
            new ThreadFactoryBuilder().setNameFormat("alarm-sendlog-pool-%d").build(),
            new ThreadPoolExecutor.DiscardPolicy());

    private static DiscoveryClient discoveryClient;
    private static RestTemplate restTemplate;

    protected static void init(ApplicationContext applicationContext, DiscoveryClient discoveryClient,
        RestTemplate restTemplate) {
        Assert.notNull(applicationContext, "ApplicationContext can't be null");
        Assert.notNull(discoveryClient, "DiscoveryClient can't be null");
        Assert.notNull(restTemplate, "RestTemplate can't be null");
        AlarmUtil.discoveryClient = discoveryClient;
        AlarmUtil.restTemplate = restTemplate;
        log.info("AlarmUtil initialized...");
    }

    /**
     * 发送Teams和TG告警消息
     *
     * @param teamsId
     *            teams group ID
     * @param tgId
     *            telegram ID
     * @param message
     *            消息体，teams支持MarkDown语法
     */
    public static void alarm(String teamsId, String tgId, String message) {
        AlarmRequest alarmRequest = new AlarmRequest(message).setRoomId(teamsId).setChatId(tgId);
        send(alarmRequest);
    }

    /**
     * 发送告警消息到Treams的默认告警群
     *
     * @param message
     *            支持MarkDown语法
     */
    public static void alarmTeams(String message) {
        AlarmRequest alarmRequest = new AlarmRequest(message);
        send(alarmRequest);
    }

    /**
     * 发送TEAMS告警消息
     *
     * @param teamsId
     *            teams group ID
     * @param message
     *            消息体。支持MarkDown语法
     */
    public static void alarmTeams(String teamsId, String message) {
        AlarmRequest alarmRequest = new AlarmRequest(message).setRoomId(teamsId);
        send(alarmRequest);
    }

    /**
     * 发送TG告警消息
     *
     * @param tgId
     *            telegram ID
     * @param message
     *            消息体
     */
    public static void alarmTg(String tgId, String message) {
        AlarmRequest alarmRequest = new AlarmRequest(message).setChatId(tgId);
        send(alarmRequest);
    }

    private static void send(AlarmRequest alarmRequest) {
        if (StringUtils.isBlank(alarmRequest.getText())) {
            return;
        }
        if (discoveryClient == null || restTemplate == null) {
            return;
        }
        alarmSendPool.submit(() -> doSend(alarmRequest));
    }

    private static void doSend(AlarmRequest alarmRequest) {
        try {
            HttpEntity<APIRequest<AlarmRequest>> restHttpEntity = createRestHttpEntity(alarmRequest);
            List<ServiceInstance> instances = discoveryClient.getInstances(SERVICE_TELEGRAM_ALARM);
            if (instances.isEmpty()) {
                return;
            }
            int index = new Random().nextInt(instances.size());
            ServiceInstance instance = instances.get(index);
            String baseUri = instance.getUri().toString();
            String requestUri = baseUri + API_PATH_SEND_TEXT;
            restTemplate.postForEntity(requestUri, restHttpEntity, Void.class);
        } catch (Throwable e) {
            log.warn("Send alarm failed.", e);
        }
    }

    private static HttpEntity<APIRequest<AlarmRequest>> createRestHttpEntity(AlarmRequest alarmRequest) {
        // message
        APIRequest<AlarmRequest> request = new APIRequest<>();
        request.setLanguage(LanguageEnum.EN_US);
        request.setTerminal(TerminalEnum.WEB);
        request.setTrackingChain(TrackingUtils.getTrace());
        request.setBody(alarmRequest);

        // header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<APIRequest<AlarmRequest>> httpEntity = new HttpEntity<>(request, headers);
        return httpEntity;
    }

    private static class AlarmRequest {
        /**
         * TG标记值 - 不发送TG消息
         */
        public final static String TG_SEND_IGNORE_FLAG = "-1";
        /**
         * Teams默认告警group id - (10S慢API及2S慢SQL告警)
         */
        // public static final String TEAMS_DEFAULT_GROUP_ID =
        // "Y2lzY29zcGFyazovL3VzL1JPT00vNjk2NjYzNzAtZjExMC0xMWVhLWJmZTQtMmQ2NTAzZDczYjI0";

        public static final String TEAMS_DEFAULT_GROUP_ID =
            "Y2lzY29zcGFyazovL3VzL1JPT00vMzI5OGVkMTAtMTM0Yi0xMWViLWIyZjEtMmI4ZjBjOGFjNjVl";
        /**
         * 消息最大字符数。（The maximum message length is 7439 bytes）
         */
        public static final int MESSAGE_MAX_LENGTH = 2000;

        /** teams使用 */
        private String roomId = TEAMS_DEFAULT_GROUP_ID;
        /** teams消息是否为MarkDown格式。开启 */
        private Boolean teamsMarkDownFlag = false;
        private String appName;
        /** teams和TG都会用 **/
        private String groupName;
        /** 消息正文 **/
        private String text;

        /** TG使用 **/
        private String chatId = TG_SEND_IGNORE_FLAG;
        /**
         * tg参数
         */
        private Map<String, Object> parameters;

        public AlarmRequest(String text) {
            this.text = text;
            appName = StringUtils.EMPTY;
        }

        public String getRoomId() {
            return roomId;
        }

        public AlarmRequest setRoomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Boolean getTeamsMarkDownFlag() {
            return teamsMarkDownFlag;
        }

        public AlarmRequest setTeamsMarkDownFlag(Boolean teamsMarkDownFlag) {
            this.teamsMarkDownFlag = teamsMarkDownFlag;
            return this;
        }

        public String getAppName() {
            return appName;
        }

        public AlarmRequest setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public String getGroupName() {
            return groupName;
        }

        public AlarmRequest setGroupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public String getText() {
            return text;
        }

        public AlarmRequest setText(String text) {
            if (text != null) {
                this.text = text.length() > MESSAGE_MAX_LENGTH ? text.substring(0, MESSAGE_MAX_LENGTH) : text;
            }
            return this;
        }

        public String getChatId() {
            return chatId;
        }

        public AlarmRequest setChatId(String chatId) {
            this.chatId = chatId;
            return this;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public AlarmRequest setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        @Override
        public String toString() {
            return "AlarmRequest{" + "roomId='" + roomId + '\'' + ", teamsMarkDownFlag=" + teamsMarkDownFlag
                + ", appName='" + appName + '\'' + ", groupName='" + groupName + '\'' + ", text='" + text + '\''
                + ", chatId='" + chatId + '\'' + ", parameters=" + parameters + '}';
        }
    }
}
