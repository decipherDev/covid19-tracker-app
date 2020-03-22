package com.decipherDev.covidtracker.beans;

public enum Config {
	  MOHFW ("https://www.mohfw.gov.in/")
	, SLACK_GROUP ("https://hooks.slack.com/services/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")
	;
	
	private String url;
	
	private Config(String url) {
		this.url =  url;
	}
	
	public String getUrl() {
		return this.url;
	}
}
