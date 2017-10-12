package com.qg.AnyWork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
//@EnableWebSocket
public class AnyWorkApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(AnyWorkApplication.class, args);
	}

	// 重写配置方法
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FormHttpMessageConverter converter = new FormHttpMessageConverter();
		MediaType mediaType = new MediaType("application","x-www-form-urlencoded", Charset.forName("UTF-8"));
		converter.setSupportedMediaTypes(Arrays.asList(mediaType));
		converters.add(converter);
		super.configureMessageConverters(converters);
	}
}
