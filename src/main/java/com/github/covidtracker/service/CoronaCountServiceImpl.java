package com.github.covidtracker.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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

import com.github.covidtracker.beans.Config;
import com.github.covidtracker.beans.SlackMessages;
import com.github.covidtracker.beans.StateWiseDistribution;
import com.github.covidtracker.commons.CheckHTTPResponseCodes;
import com.github.covidtracker.commons.CovidTrackerUtility;
import com.github.covidtracker.commons.HTMLParserUtil;
import com.github.covidtracker.commons.MarkDownParserUtil;
import com.github.covidtracker.entities.CovidAffectedStates;
import com.github.covidtracker.repositories.CovidRepository;

import jdk.internal.org.jline.utils.Log;

@Component
public class CoronaCountServiceImpl implements CoronaCountService, SlackNotify {
	private static final Logger LOG = LoggerFactory.getLogger(CoronaCountServiceImpl.class);
	private final RestTemplate template;
	private final CovidRepository covidRepository;
	
	@Autowired
	public CoronaCountServiceImpl(RestTemplate template, CovidRepository covidRepository) {
		this.template = template;
		this.covidRepository = covidRepository;
	}
	
	@SuppressWarnings("unchecked")
	public void retrieveCountFromSource() {
		LOG.info("Retrieving data from source");
		ResponseEntity<String> output = template.exchange(Config.MOHFW.getUrl(), HttpMethod.GET, null, String.class);
		
		CheckHTTPResponseCodes.checkResponseCodeIsOK(output.getStatusCode(), Config.MOHFW.getUrl());
		
		Object[] op = HTMLParserUtil.parseHTML(output.getBody());
		
		persistToDB((List<StateWiseDistribution>) op[1]);
		notifySlack(MarkDownParserUtil.parseInMarkDownFormat((String) op[0], (List<StateWiseDistribution>) op[1]));
	    
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
	
	private void persistToDB(List<StateWiseDistribution> listOfStates) {
		BiFunction<Object, Class<?>, Object> mapSWDToCAS = CovidTrackerUtility::map;
		
		covidRepository.saveAll(
		    listOfStates.stream()
					    .map(st -> (CovidAffectedStates)mapSWDToCAS.apply(st, CovidAffectedStates.class))
					    .collect(Collectors.toList())
		);
	}
	
}
