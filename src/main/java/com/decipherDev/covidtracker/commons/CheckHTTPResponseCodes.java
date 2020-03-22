package com.decipherDev.covidtracker.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class CheckHTTPResponseCodes {
	private static final Logger LOG = LoggerFactory.getLogger(CheckHTTPResponseCodes.class);
	private CheckHTTPResponseCodes() {
		//Utility class
	}
	
	public static void checkResponseCodeIsOK(HttpStatus status, String url) {
		if(status != HttpStatus.OK) {
			LOG.error("Error contacting source {}", url);
			throw new CovidTrackException("Request was not successful");
		}
	}
	
}
