package com.github.covidtracker.beans;

import lombok.Data;

@Data
public class StateWiseDistribution {
	private String name;
	private int indianNational;
	private int foreignNational;
	private int cured;
	private int death;
	
	public String toStringInMarkDownFormat() {
		int totalCount = indianNational+foreignNational;
		StringBuilder sb = new StringBuilder("| "+name);
	    for(int i=0; i < 20-name.length(); i++) {
	    	sb.append(" ");
	    }
	    sb.append("| "+totalCount);
	    int count  = (int) Math.log10(totalCount) + 1;
	    for(int i=0; i <= 7-count; i++) {
	    	sb.append(" ");
	    }
	    sb.append("|");
	    return sb.toString();
	}
	
	public static StateWiseDistribution mapper(String[] rowData) {
		StateWiseDistribution state = new StateWiseDistribution();
		int count=0;
		state.setName(rowData.length > 6 ? rowData[++count]+" "+rowData[++count] : rowData[++count]); 
		state.setIndianNational(Integer.parseInt(rowData[++count].replace("#", "")));
		state.setForeignNational(Integer.parseInt(rowData[++count].replace("#", "")));
		state.setCured(Integer.parseInt(rowData[++count].replace("#", "")));
		state.setDeath(Integer.parseInt(rowData[++count].replace("#", "")));
		
		return state;
	}
}
