package com.decipherDev.covidtracker;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.remote.server.HttpStatusHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.decipherDev.covidtracker.beans.Config;
import com.decipherDev.covidtracker.beans.SlackMessages;
import com.decipherDev.covidtracker.commons.CheckHTTPResponseCodes;
import com.decipherDev.covidtracker.commons.DataParser;

import jdk.internal.org.jline.utils.Log;

@Component
public class CoronaCountServiceImpl implements CoronaCountService, SlackNotify {
	private static final Logger LOG = LoggerFactory.getLogger(CoronaCountServiceImpl.class);
	private final RestTemplate template;
	private final DataParser parser;
	
	@Autowired
	public CoronaCountServiceImpl(RestTemplate template, DataParser parser) {
		this.template = template;
		this.parser = parser;
	}
	
	public void retrieveCountFromSource() {
		LOG.info("Retrieving data from source");
		ResponseEntity<String> output = template.exchange(Config.MOHFW.getUrl(), HttpMethod.GET, null, String.class);
		
		CheckHTTPResponseCodes.checkResponseCodeIsOK(output.getStatusCode(), Config.MOHFW.getUrl());
		
		notifySlack(parser.parseHTML(output.getBody()));
	    
		LOG.info("Will retry after 100000 milliseconds");
	}
	
	public void notifySlack(String message) {
		try {
			SlackMessages slackMessage = new SlackMessages(message);
			LOG.info("Posting {} message to slack", slackMessage);
			
			RequestEntity<String> slackNotifyRequest = 
					RequestEntity.post(new URI(Config.SLACK_GROUP.getUrl()))
					             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
					             .body(slackMessage.toString());
			
			ResponseEntity<String> response = template.exchange(slackNotifyRequest, String.class);
			
			CheckHTTPResponseCodes.checkResponseCodeIsOK(response.getStatusCode(), Config.SLACK_GROUP.getUrl());
		} catch (Exception e) {
			LOG.error("Error in posting message to slack {0}", e);
		} 
	}
	
}
