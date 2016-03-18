package me.puneetghodasara.txmgr.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.ui.web.AccountEndPoint;
import me.puneetghodasara.txmgr.ui.web.StatementEndPoint;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(AccountEndPoint.class);
		register(StatementEndPoint.class);
		register(MultiPartFeature.class);
		packages("me.puneetghodasara.txmgr.ui.web");
	}

}