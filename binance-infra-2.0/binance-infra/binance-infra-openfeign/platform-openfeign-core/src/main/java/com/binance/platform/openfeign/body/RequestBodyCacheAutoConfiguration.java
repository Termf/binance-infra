package com.binance.platform.openfeign.body;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.binance.platform.common.FilterOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnWebApplication
public class RequestBodyCacheAutoConfiguration {

	/**
	 * 缓存HttpServletRequest的Filter
	 */
	@Bean
	public RequestBodyCacheFilter requestBodyCacheFilter(ObjectMapper objectMapper,
			ConfigurableApplicationContext context) {
		return new RequestBodyCacheFilter(objectMapper, context);
	}

	@Bean
	public FilterRegistrationBean<RequestBodyCacheFilter> cacheContentFilterBean(
			RequestBodyCacheFilter requestBodyCacheFilter) {
		FilterRegistrationBean<RequestBodyCacheFilter> ret = new FilterRegistrationBean<RequestBodyCacheFilter>();
		ret.setFilter(requestBodyCacheFilter);
		ret.addUrlPatterns("/*");
		ret.setOrder(FilterOrder.FILTERORDER_2);
		return ret;
	}

	/**
	 * 支持将Cookie的灰度值转换为Header头
	 */
	@Bean
	public GrayCookieAndHeaderFilter grayCookieAndHeaderFilter() {
		return new GrayCookieAndHeaderFilter();
	}

	@Bean
	public FilterRegistrationBean<GrayCookieAndHeaderFilter> grayCookieAndHeaderFilterFilterBean(
			GrayCookieAndHeaderFilter grayCookieAndHeaderFilter) {
		FilterRegistrationBean<GrayCookieAndHeaderFilter> ret = new FilterRegistrationBean<GrayCookieAndHeaderFilter>();
		ret.setFilter(grayCookieAndHeaderFilter);
		ret.addUrlPatterns("/*");
		ret.setOrder(FilterOrder.FILTERORDER_3);
		return ret;
	}

	/**
	 * 支持HttpServletRequest放入上下文
	 */
	@Bean
	public RequestContextFilter requestContextFilter() {
		return new RequestContextFilter();
	}

	@Bean
	public FilterRegistrationBean<RequestContextFilter> requestContextFilterBean(
			RequestContextFilter myRequestContextFilter) {
		FilterRegistrationBean<RequestContextFilter> ret = new FilterRegistrationBean<RequestContextFilter>();
		ret.setFilter(myRequestContextFilter);
		ret.addUrlPatterns("/*");
		ret.setOrder(FilterOrder.FILTERORDER_4);
		return ret;
	}

	@Bean
	@ConditionalOnMissingBean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("en", "US"));
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws ServletException {
				return true;
			}
		};
		lci.setParamName("lang");
		return lci;
	}
}
