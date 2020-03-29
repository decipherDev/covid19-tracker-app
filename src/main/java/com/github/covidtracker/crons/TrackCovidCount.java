package com.github.covidtracker.crons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.covidtracker.service.CoronaCountService;



@Component
public class TrackCovidCount {
	private final CoronaCountService coronaCount;
	private static final Logger LOG = LoggerFactory.getLogger(TrackCovidCount.class);
	
	@Autowired
	public TrackCovidCount(CoronaCountService coronaCount) {
		this.coronaCount=coronaCount;
	}
	
	@Scheduled(fixedDelay = 3600000)
	public void scheduledTrackingAtFixedDelay() {
		LOG.info("Started Collecting Data");
		
		coronaCount.retrieveCountFromSource();
		
		LOG.info("Data collection Ended");
	}
}
