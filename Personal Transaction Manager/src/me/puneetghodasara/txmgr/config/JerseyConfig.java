package me.puneetghodasara.txmgr.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.ui.web.AccountEndPoint;
import me.puneetghodasara.txmgr.ui.web.StatementEndPoint;

@ApplicationPath("/api")
@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(AccountEndPoint.class);
		register(StatementEndPoint.class);
		register(MultiPartFeature.class);
		packages("me.puneetghodasara.txmgr.ui.web");
		property(ServletProperties.FILTER_FORWARD_ON_404, true);
	}

}