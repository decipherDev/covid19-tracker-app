package com.github.covidtracker.commons;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CovidTrackerUtility {
	private static final  ObjectMapper mapper = new ObjectMapper();
	
	private CovidTrackerUtility() {
		//Utility class
	}
	
	public static Object map(Object map1, Class<?> map2) {
		try {
			return mapper.readValue(mapper.writeValueAsString(map1), map2) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}	
	
	
}
