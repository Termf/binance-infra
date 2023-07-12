package com.binance.platform.swagger.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binance.platform.swagger.callrecord.ApiCallRecordManager;
import com.binance.platform.swagger.callrecord.store.RedisApiCallRecordManager;
import com.binance.platform.swagger.config.Swagger2Config.SwaggerDevCondition;
import com.binance.platform.swagger.endpoint.CustomSwaggerController;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Docket.class })
@Conditional(SwaggerDevCondition.class)
public class Swagger2Config {

	@Configuration
	@EnableSwagger2
	protected class Swagger2Load {
		@Value("${swagger.header.add:true}")
		private boolean needAddHeader;

		@Bean
		@ConditionalOnMissingBean
		public Docket createRestApi() {// 创建文档生成器
			ApiSelectorBuilder asb = new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select();
			asb.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class));
			asb.paths(PathSelectors.any());
			// 添加head参数start
			List<Parameter> headers = new ArrayList<>();
			ParameterBuilder userIdHeader = new ParameterBuilder();
			userIdHeader.name("x-user-id").order(1).description("user id").modelRef(new ModelRef("string"))
					.parameterType("header").required(false);
			headers.add(userIdHeader.build());
			ParameterBuilder grayEnvHeader = new ParameterBuilder();
			grayEnvHeader.name("x-gray-env").order(0).description("env flag").modelRef(new ModelRef("string"))
					.parameterType("header").required(false).defaultValue("normal");
			headers.add(grayEnvHeader.build());
			ParameterBuilder traceIdHeader = new ParameterBuilder();
			traceIdHeader.name("x-trace-id").order(2).description("x-trace-id").modelRef(new ModelRef("string"))
					.parameterType("header").required(false).defaultValue(generateUUID());
			headers.add(traceIdHeader.build());
			// 添加head参数end
			Docket docket = asb.build();
			docket.groupName("all");
			if (needAddHeader) {
				docket.globalOperationParameters(headers);
			}
			return docket;
		}

		private ApiInfo getApiInfo() {
			return new ApiInfoBuilder().title("API").version(this.getClass().getPackage().getImplementationVersion())
					.build();
		}

	}

	@Configuration
	@ConditionalOnBean(type = { "org.springframework.data.redis.core.RedisTemplate" })
	protected class SwaggerApiCallRecord {

		@Autowired
		private Environment env;
		@Autowired
		private ConfigurableApplicationContext context;

		@Bean
		public ApiCallRecordManager apiCallRecordManager() {
			return new RedisApiCallRecordManager(context, env.getProperty("spring.application.name"));
		}

		@Bean
		@ConditionalOnBean(ApiCallRecordManager.class)
		@ConditionalOnMissingBean
		public CustomSwaggerController customSwaggerController(ApiCallRecordManager apiCallRecordManager) {
			CustomSwaggerController customSwaggerController = new CustomSwaggerController(context);
			customSwaggerController.setApiCallRecordManager(apiCallRecordManager);
			return customSwaggerController;
		}

	}

	public static class SwaggerDevCondition implements Condition {
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			Environment environment = context.getEnvironment();
			return isDev(environment) || isQa(environment);
		}

		private static boolean isDev(Environment env) {
			if (env != null) {
				final String active = env.getProperty("spring.profiles.active", String.class);
				if (active != null)
					return StringUtils.equalsIgnoreCase(active, "dev") || StringUtils.endsWithIgnoreCase(active, "dev")
							|| StringUtils.equalsIgnoreCase(active, "local")
							|| StringUtils.endsWithIgnoreCase(active, "local");
			}
			String envProperty = System.getProperty("env", "dev");
			if (StringUtils.equalsIgnoreCase("dev", envProperty)
					|| StringUtils.endsWithIgnoreCase(envProperty, "dev")) {
				return true;
			} else {
				return false;
			}
		}

		private static boolean isQa(Environment env) {
			if (env != null) {
				final String active = env.getProperty("spring.profiles.active", String.class);
				if (active != null)
					return StringUtils.equalsIgnoreCase(active, "qa") || StringUtils.endsWithIgnoreCase(active, "qa");
			}
			String envProperty = System.getProperty("env", "qa");
			if (StringUtils.equalsIgnoreCase("qa", envProperty) || StringUtils.endsWithIgnoreCase(envProperty, "qa")) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Controller
	@ApiIgnore
	@RequestMapping(value = "/docs")
	public class DocumentController {

		@RequestMapping()
		public String apis() {
			return "redirect:/swagger-ui.html";
		}
	}

	private static String generateUUID() {
		String s = UUID.randomUUID().toString();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			builder.append((c == '-') ? "" : c);
		}
		return builder.toString();
	}

}
