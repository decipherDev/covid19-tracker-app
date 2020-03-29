package com.github.covidtracker.beans;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Config {
	  MOHFW ("exturl.mohfw")
	, SLACK_GROUP ("exturl.slackGroup")
	;
	
	private static final Logger LOG = LoggerFactory.getLogger(Config.class);
	
	static {
		Properties property = new Properties();
		try {
			property.load(Config.class.getClassLoader().getResourceAsStream("application.properties"));
			for (Config config : Config.values()) {
				config.url = property.getProperty(config.getName());
			}
		} catch (IOException e) {
			LOG.error("Error while reading application.propety file", e);
		}
	}
	
	private String name;
	private String url;
	
	
	private Config(String name) {
		this.name =  name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getUrl() {
		return this.url;
	}
}
