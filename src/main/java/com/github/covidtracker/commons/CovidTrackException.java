package com.github.covidtracker.commons;

public class CovidTrackException extends RuntimeException {
	private static final long serialVersionUID = 8704302792182414166L;

	public CovidTrackException() {
		super();
	}
	
	public CovidTrackException(String message) {
		super(message);
	}
}
