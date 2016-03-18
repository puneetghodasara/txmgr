package me.puneetghodasara.txmgr.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.ui.web.AccountEndPoint;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(AccountEndPoint.class);
	}

}