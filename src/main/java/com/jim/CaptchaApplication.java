package com.jim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource({"captcha.xml"})
@PropertySource(value = "application.properties")
public class CaptchaApplication extends WebMvcConfigurerAdapter {
	private static ApplicationContext ctx;

	@Autowired
	private void setApplicationContext(ApplicationContext applicationContext){
		ctx = applicationContext;
	}

	public ApplicationContext getApplicationContext(){
		return ctx;
	}

	public static void main(String[] args) {
		SpringApplication.run(CaptchaApplication.class, args);
	}
}
