package com.decipherDev.covidtracker.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessages {
	private String text;

	@Override
	public String toString() {
		return "{" + "\"" + "text" + "\"" + ":" + "\"" + text + "\"}";
	}
	
}
