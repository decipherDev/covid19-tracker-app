package com.github.covidtracker.commons;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.github.covidtracker.beans.StateWiseDistribution;

public class HTMLParserUtil {
	private HTMLParserUtil() {
		//Utility class
	}
	
	public static Object[] parseHTML(String html) {
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByClass("table-responsive");
		
		return rowWiseMapper(elements.get(7)
				                     .select("table")
				                     .get(0)
				                     .select("tbody")
				                     .select("tr")
						     );
	}

	private static Object[] rowWiseMapper(Elements rowWiseRawData) {
		List<StateWiseDistribution> listOfStates = 
				rowWiseRawData.stream()
				              .map(row -> row.text().split(" "))
				              .filter(row -> row.length < 8)
				              .map(StateWiseDistribution::mapper)
				              .collect(Collectors.toList());
		
		String finalCount = rowWiseRawData.get(rowWiseRawData.size()-1).text();
		
	    return new Object[] {finalCount, listOfStates};
	}
}
