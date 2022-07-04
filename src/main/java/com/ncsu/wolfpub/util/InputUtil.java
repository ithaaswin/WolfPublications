package com.ncsu.wolfpub.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtil {

	// Using Scanner for Getting Input from User Create the console object Enter data using BufferReader
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public static int getInteger(String fieldName) {
		System.out.print("\t\t\t" + fieldName + " : ");
		try {
			String input = reader.readLine();
			int result = Integer.parseInt(input.trim());
			return result;
		} catch(Exception e) {
			System.out.println("\t\t\tPlease enter a valid integer");
			return getInteger(fieldName);
		}
		
	}
	
	public static float getFloat(String fieldName) {
		System.out.print("\t\t\t" + fieldName + " : ");
		try {
			String input = reader.readLine();
			float result = Float.parseFloat(input.trim());
			return result;
		} catch(Exception e) {
			System.out.println("\t\t\tPlease enter a valid number	");
			return getFloat(fieldName);
		}
		
	}
	
	public static String getString(String fieldName) {
		System.out.print("\t\t\t" + fieldName + " : ");
		String input;
		try {
			input = reader.readLine();
			return input;
		} catch (IOException e) {
			System.out.println("\t\t\tPlease enter a valid string	");
			return getString(fieldName);
		}
		
		
	}
	
	public static Date getDate(String fieldName) {
		System.out.print("\t\t\t" + fieldName + " (in MM/dd/yyyy format) : ");
		try {
			String input = reader.readLine();
			Date date=new SimpleDateFormat("MM/dd/yyyy").parse(input);
			return date;
		} catch(Exception e) {
			System.out.println("\t\t\tPlease enter a valid Date	");
			return getDate(fieldName);
		}
		
	}

	public static String getPhoneNumber(String fieldName) {
		 Pattern pattern =  Pattern.compile("^\\d{10}$");
		  System.out.print("\t\t\t" + fieldName + " : ");
			String input;
			try {
				input = reader.readLine();
				Matcher matcher = pattern.matcher(input);
				if(!matcher.matches()) {
					System.out.println("\t\t\tPlease enter a valid 10 digit Phone Number");
					return getPhoneNumber(fieldName);
				}
				return input;
			} catch (IOException e) {
				System.out.println("\t\t\tPlease enter a valid 10 digit Phone Number");
				return getString(fieldName);
			}
	}
	  

}
