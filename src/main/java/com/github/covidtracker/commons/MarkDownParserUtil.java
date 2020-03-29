package com.github.covidtracker.commons;

import java.util.List;

import org.springframework.stereotype.Component;

import com.github.covidtracker.beans.StateWiseDistribution;

@Component
public class MarkDownParserUtil {
	private static final String MRKDWN_CODE_BLOCK = "```";
	private static final String MRKDWN_BOLD = "*";
	private static final String MRKDWN_CODE = "`";
	private static final String MRKDWN_NEW_LINE = "\n";
	
	private MarkDownParserUtil() {
		//Utility class
	}
	
	private static String parseTotalCountInMarkDownFormat(int totalCount, String[] finalCountNumbers) {
		return MRKDWN_BOLD+"Total Count"+MRKDWN_BOLD+": "+MRKDWN_CODE+totalCount+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Indian count"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[7]+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Foreigner count"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[8]+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Cured"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[9]+MRKDWN_CODE
				+MRKDWN_NEW_LINE+" "+MRKDWN_BOLD+"Death"+MRKDWN_BOLD+": "+MRKDWN_CODE+finalCountNumbers[10]+MRKDWN_CODE+MRKDWN_NEW_LINE;
	}
	
	public static String parseInMarkDownFormat(String finalCount, List<StateWiseDistribution> states) {
		String[] finalCountNumbers = finalCount.split(" ");
	    int totalCount = Integer.parseInt(finalCountNumbers[7].replace("#", ""))+Integer.parseInt(finalCountNumbers[8].replace("#", ""));
	    
	    String finalCountMRKDWN = parseTotalCountInMarkDownFormat(totalCount, finalCountNumbers);
		
		StringBuilder string = new StringBuilder("|        state        |  count  |\n");
		                           string.append("|---------------------|---------|\n");
		for (StateWiseDistribution stateWiseDistribution : states) {
			string.append(stateWiseDistribution.toStringInMarkDownFormat()+"\n");
		}
		
		return finalCountMRKDWN + MRKDWN_CODE_BLOCK + string.toString() + MRKDWN_CODE_BLOCK;
	}
}
