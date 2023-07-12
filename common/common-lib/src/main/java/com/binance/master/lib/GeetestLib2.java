package com.binance.master.lib;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * Java SDK
 */
public class GeetestLib2 {

    private static final Logger logger = LoggerFactory.getLogger(GeetestLib2.class);
    protected static final String VER_NAME = "4.0";
    protected static final String sdkLang = "java";
    protected static final String API_URL = "http://api.geetest.com";
    protected static final String REGISTER_URL = "/register.php";
    protected static final String VALIDATE_URL = "/validate.php";
    protected HttpHost HOST = HttpHost.create(API_URL);

    // HttpClient连接池可以通过JVM属性自定义
    private static final String MAX_PER_ROUTE = "geetest_max_per_route";
    private static final String MAX_TOTAL = "geetest_max_total";
    private static final String CONNECTION_TIMEOUT = "geetest_connection_timeout";
    private static final String SOCKET_TIMEOUT = "geetest_socket_timeout";


    protected final String json_format = "1";
    /**
     * 调试开关，是否输出调试日志
     */
    public boolean debugCode = false;
//    /**
//     * 极验验证API服务状态Session Key
//     */
//    public static String gtServerStatusSessionKey = "gt_server_status";
//    /**
//     * 极验验证API服务状态Session Key
//     */
//    public static String gtUseridSessionKey = "gt_userid";

    /**
     * 公钥
     */
    private String captchaId = "";
    /**
     * 私钥
     */
    private String privateKey = "";
    /**
     * 是否开启新的failback
     */
    private boolean newFailback = false;

    private ThreadLocal<String> localResponseStr = new ThreadLocal<>();

    private CloseableHttpClient client;

    private static Map<String, GeetestLib2> instances = new ConcurrentHashMap<>();

    /**
     * release the local response so that it can be reused.
     */
    public void reset() {
        localResponseStr.remove();
    }

    /**
     * main entrance for the GeetestLib.
     *
     * @param captchaId
     * @param privateKey
     * @param newFailback
     * @return
     */
    public static GeetestLib2 getInstance(String captchaId, String privateKey, boolean newFailback) {
        final String key = captchaId + newFailback;
        GeetestLib2 geetestLib = instances.get(key);
        if (geetestLib == null) {
            geetestLib = new GeetestLib2(captchaId, privateKey, newFailback);
            instances.put(key, geetestLib);
        }
        return geetestLib;
    }

    /**
     * 带参数构造函数
     *
     * @param captchaId
     * @param privateKey
     */
    private GeetestLib2(String captchaId, String privateKey, boolean newFailback) {
        this.captchaId = captchaId;
        this.privateKey = privateKey;
        this.newFailback = newFailback;
        initHttpClient();
    }

    private void initHttpClient() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setSocketConfig(HOST, SocketConfig.custom().setSoKeepAlive(true).build());
        connManager.setDefaultMaxPerRoute(getIntProperty(MAX_PER_ROUTE, 20));
        connManager.setMaxTotal(getIntProperty(MAX_TOTAL, 40));

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(getIntProperty(CONNECTION_TIMEOUT, 2000))
                .setSocketTimeout(getIntProperty(SOCKET_TIMEOUT, 2000)).build();

        client = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();
    }

    private int getIntProperty(String key, int defaultValue) {
        String prop = System.getProperty(MAX_PER_ROUTE);
        try {
            return Integer.parseInt(prop);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    /**
     * 获取本次验证初始化返回字符串
     *
     * @return 初始化结果
     */
    public String getResponseStr() {
        return localResponseStr.get();
    }

    public String getVersionInfo() {
        return VER_NAME;
    }

    /**
     * 预处理失败后的返回格式串
     *
     * @return
     */
    private String getFailPreProcessRes() {
        Long rnd1 = Math.round(Math.random() * 100);
        Long rnd2 = Math.round(Math.random() * 100);
        String md5Str1 = md5Encode(rnd1 + "");
        String md5Str2 = md5Encode(rnd2 + "");
        String challenge = md5Str1 + md5Str2.substring(0, 2);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("success", 0);
        jsonObject.put("gt", this.captchaId);
        jsonObject.put("challenge", challenge);
        jsonObject.put("new_captcha", this.newFailback);
        return jsonObject.toString();
    }

    /**
     * 预处理成功后的标准串
     */
    private String getSuccessPreProcessRes(String challenge) {
        gtlog("challenge:" + challenge);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", 1);
        jsonObject.put("gt", this.captchaId);
        jsonObject.put("challenge", challenge);
        return jsonObject.toString();
    }

    /**
     * 验证初始化预处理
     *
     * @param userId
     * @return 1表示初始化成功，0表示初始化失败
     */
    public int preProcess(String userId) {
        if (registerChallenge(userId, "", "") != 1) {
            localResponseStr.set(this.getFailPreProcessRes());
            return 0;
        }
        return 1;
    }

    /**
     * 验证初始化预处理
     *
     * @param userId
     * @param clientType
     * @param ipAddress
     * @return
     */
    public int preProcess(String userId, String clientType, String ipAddress) {
        if (registerChallenge(userId, clientType, ipAddress) != 1) {
            localResponseStr.set(this.getFailPreProcessRes());
            return 0;
        }
        return 1;
    }

    /**
     * 用captchaID进行注册，更新challenge
     *
     * @return 1表示注册成功，0表示注册失败
     */
    private int registerChallenge(String userId, String clientType, String ipAddress) {
        try {
            String param = "gt=" + this.captchaId + "&json_format=" + this.json_format;
            if (StringUtils.isNotEmpty(userId)) {
                param = param + "&user_id=" + userId;
            }
            if (StringUtils.isNotEmpty(clientType)) {
                param = param + "&client_type=" + clientType;
            }
            if (StringUtils.isNotEmpty(ipAddress)) {
                param = param + "&ip_address=" + ipAddress;
            }

            String result_str = readContentFromGet(REGISTER_URL, param);
            if (StringUtils.equals(result_str, "fail")) {
                gtlog("gtServer register challenge failed");
                return 0;
            }

            gtlog("result:" + result_str);
            JSONObject jsonObject = JSONObject.parseObject(result_str);
            String return_challenge = jsonObject.getString("challenge");

            gtlog("return_challenge:" + return_challenge);

            if (return_challenge.length() == 32) {
                String responseStr = this.getSuccessPreProcessRes(this.md5Encode(return_challenge + this.privateKey));
                localResponseStr.set(responseStr);
                return 1;
            } else {
                gtlog("gtServer register challenge error");
                return 0;
            }
        } catch (Exception e) {
            gtlog(e.toString());
            gtlog("exception:register api");
        }
        return 0;
    }

    /**
     * 判断一个表单对象值是否为空
     *
     * @param gtObj
     * @return
     */
    protected boolean objIsEmpty(Object gtObj) {
        if (gtObj == null) {
            return true;
        }

        if (gtObj.toString().trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 检查客户端的请求是否合法,三个只要有一个为空，则判断不合法
     *
     * @param challenge
     * @param validate
     * @param seccode
     * @return
     */
    private boolean requestIsLegal(String challenge, String validate, String seccode) {
        if (objIsEmpty(challenge)) {
            return false;
        }
        if (objIsEmpty(validate)) {
            return false;
        }
        if (objIsEmpty(seccode)) {
            return false;
        }
        return true;
    }

    /**
     * 服务正常的情况下使用的验证方式,向gt-server进行二次验证,获取验证结果
     *
     * @param challenge
     * @param validate
     * @param seccode
     * @param userid
     * @return 验证结果, 1表示验证成功0表示验证失败
     */
    public int enhancedValidateRequest(String challenge, String validate, String seccode, String userid) {
        if (!requestIsLegal(challenge, validate, seccode)) {
            return 0;
        }

        gtlog("request legitimate");

        String param = String.format("challenge=%s&validate=%s&seccode=%s&json_format=%s", challenge, validate, seccode,
                this.json_format);

        String response = "";
        try {
            if (validate.length() <= 0) {
                return 0;
            }
            if (!checkResultByPrivate(challenge, validate)) {
                return 0;
            }

            gtlog("checkResultByPrivate");
            response = readContentFromPost(VALIDATE_URL, param);
            gtlog("response: " + response);
        } catch (Exception e) {
            logger.error("read content error", e);
        }

        String return_seccode = "";
        JSONObject return_map = JSONObject.parseObject(response);
        return_seccode = return_map.getString("seccode");
        gtlog("md5: " + md5Encode(return_seccode));

        if (return_seccode.equals(md5Encode(seccode))) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * failback使用的验证方式
     *
     * @param challenge
     * @param validate
     * @param seccode
     * @return 验证结果, 1表示验证成功0表示验证失败
     */
    public int failbackValidateRequest(String challenge, String validate, String seccode) {
        gtlog("in failback validate");
        if (!requestIsLegal(challenge, validate, seccode)) {
            return 0;
        }
        gtlog("request legitimate");
        return 1;
    }

    /**
     * 输出debug信息，需要开启debugCode
     *
     * @param message
     */
    public void gtlog(String message) {
        if (debugCode) {
            logger.info("gtlog: " + message);
        }
    }

    protected boolean checkResultByPrivate(String challenge, String validate) {
        String encodeStr = md5Encode(privateKey + "geetest" + challenge);
        return validate.equals(encodeStr);
    }


    /**
     * 发送GET请求，获取服务器返回结果
     *
     * @param queryPath
     * @param params
     * @return
     * @throws IOException
     */
    private String readContentFromGet(String queryPath, String params) throws IOException {
        HttpGet httpGet = new HttpGet(queryPath + "?" + params);
        CloseableHttpResponse response = client.execute(HOST, httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            return getGtResponse(response);
        } else {
            gtlog(String.format("gtResponseCode:%s", statusCode));
            try {
                gtlog(String.format("gtResponseStr:%s", getGtResponse(response)));
            } catch (Exception e) {
            }
            return "fail";
        }
    }

    private String getGtResponse(CloseableHttpResponse response) throws IOException {
        // 发送数据到服务器并使用Reader读取返回的数据
        StringBuffer sBuffer = new StringBuffer();
        try (InputStream in = response.getEntity().getContent()) {
            byte[] buf = new byte[1024];
            for (int n; (n = in.read(buf)) != -1; ) {
                sBuffer.append(new String(buf, 0, n, "UTF-8"));
            }
        }
        return sBuffer.toString();
    }

    /**
     * 发送POST请求，获取服务器返回结果
     *
     * @param queryPath
     * @param requestBody
     * @return
     * @throws IOException
     */
    private String readContentFromPost(String queryPath, String requestBody) throws IOException {
        gtlog(requestBody);
        HttpPost httpPost = new HttpPost(queryPath);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(new StringEntity(requestBody, "utf-8"));

        CloseableHttpResponse response = client.execute(HOST, httpPost);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            // 发送数据到服务器并使用Reader读取返回的数据
            return getGtResponse(response);
        } else {
            return "fail";
        }
    }

    /**
     * md5 加密
     *
     * @param plainText
     * @return
     * @time 2014年7月10日 下午3:30:01
     */
    private String md5Encode(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            logger.error("invalid algorithm", e);
        }
        return re_md5;
    }

}
