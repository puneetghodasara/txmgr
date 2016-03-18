package main;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import me.puneetghodasara.txmgr.core.listener.RuleListener;

@ImportResource({ "spring.xml" })
// @PropertySource("classpath:/config.properties")
@EnableWebMvc
@SpringBootApplication
public class AppConfig extends SpringBootServletInitializer {
	@Resource
	ApplicationContext context;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AppConfig.class, args);

		System.out.println(" --- start ---");

	}
	
	@Bean
	public RuleListener ruleListener(){
		return new RuleListener();
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AppConfig.class);
	}

}
