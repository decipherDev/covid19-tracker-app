package com.decipherDev.covidtracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TrackCovidCount {
	private final CoronaCountServiceImpl coronaCount;
	private static final Logger LOG = LoggerFactory.getLogger(TrackCovidCount.class);
	
	@Autowired
	public TrackCovidCount(CoronaCountServiceImpl coronaCount) {
		this.coronaCount=coronaCount;
	}
	
	@Scheduled(fixedDelay = 3600000)
	public void scheduledTrackingAtFixedDelay() {
		LOG.info("Started Collecting Data");
		
		coronaCount.retrieveCountFromSource();
		
		LOG.info("Data collection Ended");
	}
}
