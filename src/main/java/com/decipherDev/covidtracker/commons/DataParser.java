package com.decipherDev.covidtracker.commons;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.decipherDev.covidtracker.beans.StateWiseDistribution;

@Component
public class DataParser {
	private static final String MRKDWN_CODE_BLOCK = "```";
	private static final String MRKDWN_BOLD = "*";
	private static final String MRKDWN_CODE = "`";
	private static final String MRKDWN_NEW_LINE = "\n";
	
	public String parseHTML(String html) {
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByClass("table-responsive");
		
		return rowWiseMapper(elements.get(7)
				                     .select("table")
				                     .get(0)
				                     .select("tbody")
				                     .select("tr")
						     );
	}

	private String rowWiseMapper(Elements rowWiseRawData) {
		List<StateWiseDistribution> listOfStates = 
				rowWiseRawData.stream()
				              .map(row -> row.text().split(" "))
				              .filter(row -> row.length < 8)
				              .map(StateWiseDistribution::mapper)
				              .collect(Collectors.toList());
		
		String finalCount = rowWiseRawData.get(rowWiseRawData.size()-1).text();
	    String[] finalCountNumbers = finalCount.split(" ");
	    int totalCount = Integer.parseInt(finalCountNumbers[7])+Integer.parseInt(finalCountNumbers[8]);
	    
	    String finalCountMRKDWN = parseTotalCountInMarkDownFormat(totalCount, finalCountNumbers);
				
		return finalCountMRKDWN + MRKDWN_CODE_BLOCK +parseInMarkDownFormat(listOfStates)+ MRKDWN_CODE_BLOCK;
	}
	
	private String parseTotalCountInMarkDownFormat(int totalCount, String[] finalCountNumbers) {
		return MRKDWN_BOLD+"Total Count"+MRKDWN_BOLD+": "+MRKDWN_CODE+totalCount+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Indian count"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[7]+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Foreigner count"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[8]+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Cured"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[9]+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Death"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[10]+MRKDWN_CODE+MRKDWN_NEW_LINE;
	}
	
	private String parseInMarkDownFormat(List<StateWiseDistribution> states) {
		StringBuilder string = new StringBuilder("|        state        |  count  |\n");
		                           string.append("|---------------------|---------|\n");
		for (StateWiseDistribution stateWiseDistribution : states) {
			string.append(stateWiseDistribution.toStringInMarkDownFormat()+"\n");
		}
		
		return string.toString();
	}
}
