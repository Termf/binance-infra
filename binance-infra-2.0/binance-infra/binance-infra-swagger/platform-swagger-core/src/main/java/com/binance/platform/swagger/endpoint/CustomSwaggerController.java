package com.binance.platform.swagger.endpoint;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import com.binance.platform.swagger.model.TestPlanData;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.binance.platform.swagger.callrecord.ApiCallRecordManager;
import com.binance.platform.swagger.model.ApiCallRecord;
import com.binance.platform.swagger.model.ApiCallRecordIdentity;
import com.binance.platform.swagger.model.ApiCallStatics;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import springfox.documentation.annotations.ApiIgnore;

import static java.util.stream.Collectors.toList;
import static springfox.documentation.spring.web.paths.Paths.splitCamelCase;

/**
 * @author rony.gu
 * @date 2020/4/22
 */
@RestController
@ApiIgnore
public class CustomSwaggerController {

    private static final String DEFAULT_API_CALL_RECORDS_URL = "/v2/api-docs/records";
    private static final String DEFAULT_API_CALL_STATICS_URL = "/v2/api-docs/statics";
    private static final String DEFAULT_API_TEST_PLAN_DATA_URL = "/v2/api-docs/test-plan-data";
    private static final String DEFAULT_API_SAVE_CALL_RECORD_URL = "/v2/api-docs/save-record";
    private static final String DEFAULT_API_DELETE_CALL_RECORD_URL = "/v2/api-docs/delete-record";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSwaggerController.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ApiCallRecordManager apiCallRecordManager;

    private final ConfigurableApplicationContext context;

    public CustomSwaggerController(ConfigurableApplicationContext context) {
        this.context = context;
    }


    /**
     * 获取一些统计信息
     */
    @GetMapping(DEFAULT_API_CALL_STATICS_URL)
    @ResponseBody
    public ResponseEntity<Json> getApiCallStatics() throws JsonProcessingException {
        ApiCallStatics apiCallStatics = new ApiCallStatics();
        try {
            Map<ApiCallRecordIdentity, String> allMethods = getAllApis();
            apiCallStatics.setTotalApiCnt(allMethods.keySet().size());
            if (apiCallRecordManager != null) {
                Long testPlanCnt = apiCallRecordManager.getTestPlanApiCnt();
                if (testPlanCnt != null) {
                    apiCallStatics.setTestPlanApiCnt(testPlanCnt);
                } else {
                    Long existingCount = apiCallRecordManager.countExistingKeys(Lists.newArrayList(allMethods.keySet()));
                    apiCallRecordManager.setTestPlanApiCnt(existingCount);
                    apiCallStatics.setTestPlanApiCnt(existingCount);
                }
            }
        } catch (Exception e) {
            // ingore
        }
        return new ResponseEntity<>(new Json(objectMapper.writeValueAsString(apiCallStatics)), HttpStatus.OK);
    }

    private Map<ApiCallRecordIdentity, String> getAllApis() {
        Map<String, Object> beanNames = context.getBeansWithAnnotation(RestController.class);
        Map<ApiCallRecordIdentity, String> allMethods = Maps.newHashMap();
        for (Object controller : beanNames.values()) {
            Map<ApiCallRecordIdentity, String> restAnnotationMethods = getRestAnnotationMethods(controller.getClass());
            if (restAnnotationMethods != null) {
                allMethods.putAll(restAnnotationMethods);
            }
        }
        return allMethods;
    }

    /**
     * 下载所有testPlan计划数据
     */
    @GetMapping(DEFAULT_API_TEST_PLAN_DATA_URL)
    @ResponseBody
    public ResponseEntity<Json> getAllTestPlanData() throws JsonProcessingException {
        List<TestPlanData> testPlanDataList = Lists.newArrayList();
        try {
            Map<ApiCallRecordIdentity, String> allApis = getAllApis();
            allApis.forEach((apiCallRecordIdentity, path) -> {
                List<ApiCallRecord> allApiCallRecords = apiCallRecordManager.find(apiCallRecordIdentity);
                List<ApiCallRecord> testPlanApiCallRecords = allApiCallRecords.stream()
                        .filter(apiCallRecord -> apiCallRecord.isTestPlan() != null && apiCallRecord.isTestPlan()).collect(toList());
                if (!CollectionUtils.isEmpty(testPlanApiCallRecords)) {
                    TestPlanData testPlanData = new TestPlanData();
                    testPlanData.setPath(path);
                    testPlanData.setTestPlanData(testPlanApiCallRecords);
                    testPlanDataList.add(testPlanData);
                }
            });
        } catch (Exception e) {
            // ignore
        }
        return new ResponseEntity<>(new Json(objectMapper.writeValueAsString(testPlanDataList)), HttpStatus.OK);
    }

    /**
     * 从redis获取某个api的最近调用记录
     *
     * @param identity
     * @return
     */
    @PostMapping(DEFAULT_API_CALL_RECORDS_URL)
    @ResponseBody
    public ResponseEntity<Json> getApiCallRecords(@RequestBody ApiCallRecordIdentity identity) throws JsonProcessingException {
        try {
            if (apiCallRecordManager != null) {
                List<ApiCallRecord> apiCallRecords = apiCallRecordManager.find(identity);
                return new ResponseEntity<>(new Json(objectMapper.writeValueAsString(apiCallRecords)), HttpStatus.OK);
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    @PostMapping(DEFAULT_API_SAVE_CALL_RECORD_URL)
    @ResponseBody
    public void saveApiCallRecord(@RequestBody ApiCallRecord record) {
        try {
            if (record != null) {
                record.setCreateTime(sdf.format(new Date()));
                if (apiCallRecordManager != null) {
                    apiCallRecordManager.saveOfUpdate(record);
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @DeleteMapping(value = DEFAULT_API_DELETE_CALL_RECORD_URL)
    @ResponseBody
    public void deleteApiCallRecord(@RequestBody ApiCallRecordIdentity identity) {
        try {
            if (apiCallRecordManager != null) {
                apiCallRecordManager.delete(identity);
            }
        } catch (Exception e) {
            // ignore
        }
    }


    private Map<ApiCallRecordIdentity, String> getRestAnnotationMethods(Class<?> clazzController) {
        Map<ApiCallRecordIdentity, String> methodUrlMap = new HashMap<>();
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(clazzController, RequestMapping.class);
        ApiIgnore requestMappingApiIgnore = AnnotationUtils.findAnnotation(clazzController, ApiIgnore.class);
        if (requestMappingApiIgnore != null) {
            return Maps.newHashMap();
        }
        String rootPath = "";
        if (requestMapping != null) {
            rootPath = requestMapping.value()[0];
        }
        Method[] methods = clazzController.getMethods();
        if (methods == null || methods.length == 0) {
            methods = clazzController.getSuperclass().getMethods();
        }
        for (Method method : methods) {
            GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
            if (getMapping != null && getMapping.value().length > 0) {
                populateMethodUrl(methodUrlMap, clazzController.getSimpleName(), method, "GET@", rootPath, getMapping.value()[0]);
                continue;
            }
            PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
            if (postMapping != null && postMapping.value().length > 0) {
                populateMethodUrl(methodUrlMap, clazzController.getSimpleName(), method, "POST@", rootPath, postMapping.value()[0]);
                continue;
            }
            PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
            if (putMapping != null && putMapping.value().length > 0) {
                populateMethodUrl(methodUrlMap, clazzController.getSimpleName(), method, "PUT@", rootPath, putMapping.value()[0]);
                continue;
            }
            DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
            if (deleteMapping != null && deleteMapping.value().length > 0) {
                populateMethodUrl(methodUrlMap, clazzController.getSimpleName(), method, "DELETE@", rootPath, deleteMapping.value()[0]);
                continue;
            }
            RequestMapping methodRequestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
            if (methodRequestMapping != null && methodRequestMapping.value().length > 0) {
                String prefix = null;
                if (methodRequestMapping.method() != null && methodRequestMapping.method().length > 0) {
                    prefix = methodRequestMapping.method()[0].name() + "@";
                }
                populateMethodUrl(methodUrlMap, clazzController.getSimpleName(), method, prefix, rootPath, methodRequestMapping.value()[0]);
            }
        }
        return methodUrlMap;
    }


    private void populateMethodUrl(Map<ApiCallRecordIdentity, String> methodUrlMap, String clazzControllerName, Method method, String prefix,
            String rootPath, String value) {
        ApiCallRecordIdentity apiCallRecordIdentity = new ApiCallRecordIdentity();
        apiCallRecordIdentity.setId(UUID.randomUUID().toString());
        apiCallRecordIdentity.setEndpointName(controllerNameAsGroup(clazzControllerName));
        apiCallRecordIdentity.setMethodName(prefix == null ? method.getName() + "Using" : method.getName() + "Using" + prefix.replace("@", ""));
        String resourceName = null;
        try {
            resourceName = getResourceName(prefix, rootPath, value);
        } catch (Exception e) {
            LOGGER.error("getResourceName has failed");
        }
        methodUrlMap.put(apiCallRecordIdentity, resourceName);
    }


    private static String controllerNameAsGroup(String clazzControllerName) {
        if (StringUtils.isNoneBlank(clazzControllerName)) {
            int index = clazzControllerName.indexOf("$");
            clazzControllerName = index != -1 ? clazzControllerName.substring(0, index) : clazzControllerName;
        }
        return splitCamelCase(clazzControllerName, "-").replace("/", "").toLowerCase();
    }

    private static String getResourceName(String prefix, String rootPath, String value) {
        StringBuilder sb = StringUtils.isBlank(prefix) ? new StringBuilder() : new StringBuilder(prefix);
        sb.append(rootPath);
        if (sb.length() == 0) {
            return value;
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '/') {
            sb.append('/');
        }
        if (!StringUtils.isEmpty(value)) {
            if (value.charAt(0) == '/') {
                sb.append(value.substring(1));
            } else {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    private static class Json {
        private final String value;

        public Json(String value) {
            this.value = value;
        }

        @JsonValue
        @JsonRawValue
        public String value() {
            return this.value;
        }
    }

    public void setApiCallRecordManager(ApiCallRecordManager apiCallRecordManager) {
        this.apiCallRecordManager = apiCallRecordManager;
    }

}
