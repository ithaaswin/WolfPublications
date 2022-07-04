package com.ncsu.wolfpub.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputPrintUtil {

	public static void printResults(List<Map<String, String>> queryResult, List<String> headers) {
		System.out.println("---------------------------------------------------------------------------");
		List<List<String>> rows = new ArrayList<>();
		for(Map<String, String> row : queryResult) {
			List<String> temp = new ArrayList<>();
			for(String header : headers) temp.add(row.get(header));
			rows.add(temp);
		}
		
		int[] maxLengths = new int[headers.size()];
		for (int i = 0; i < headers.size(); i++) {
			maxLengths[i] = headers.get(i).length();
		}
		
		for (List<String> row : rows) {
			for (int i = 0; i < row.size(); i++) {
				maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
			}
		}

		StringBuilder formatBuilder = new StringBuilder();
		for (int maxLength : maxLengths) {
			formatBuilder.append("%-").append(maxLength + 2).append("s");
		}
		String format = formatBuilder.toString();

		StringBuilder result = new StringBuilder();
		result.append(String.format(format, headers.toArray(new String[0]))).append("\n\n");
		for (List<String> row : rows) {
			result.append(String.format(format, row.toArray(new String[0]))).append("\n");
		}
		System.out.println(result.toString());
		System.out.println("---------------------------------------------------------------------------");
	}

	public static void printResults(Map<String, String> orderSummary, List<String> otherHeaders) {
		for(String otherHeader : otherHeaders) {
			System.out.println(otherHeader + " : " + orderSummary.get(otherHeader));
		}
	}

}
