package com.ncsu.wolfpub.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ncsu.wolfpub.constants.FieldConstants;
import com.ncsu.wolfpub.dao.AnalyticsDAO;
import com.ncsu.wolfpub.dao.DistributorDAO;
import com.ncsu.wolfpub.dao.WolfDAOFactory;
import com.ncsu.wolfpub.util.InputUtil;
import com.ncsu.wolfpub.util.OutputPrintUtil;

public class AnalyticProcessor extends FieldConstants {

	public static void printMenu() {
		System.out.println("\t-------------Reports-------------");

		System.out.println("\t1. Publications sold per distributor per month");
		System.out.println("\t2. Publication house Revenue ");
		System.out.println("\t3. Publication house Expense");
		System.out.println("\t4. Publication house Distributor Count");
		System.out.println("\t5. Revenue per City");
		System.out.println("\t6. Revenue per Distributor");
		System.out.println("\t7. Revenue per Location");
		System.out.println("\t8. Payment per time period");
		System.out.println("\t9. Payment per work type");
		System.out.println("\t0. Back to main menu");

	}

	/**
	 * Process the Sub Menu 4
	 * @param option
	 */
	public static void processTask(int option) {
		WolfDAOFactory factory = new WolfDAOFactory();
		AnalyticsDAO dao = factory.getAnalyticsDAO();
		DistributorDAO dDao = factory.getDistributorDAO();

		if (option == 1) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			List<Map<String, String>> results = dao.getStatsPerPubDistMonth(from , to);
			List<String> headers = new ArrayList<>(Arrays.asList(YEAR, MONTH, DID, PID, PURCHASE_COST, NO_OF_COPIES));
			OutputPrintUtil.printResults(results, headers);
		} else if (option == 2) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			double revenue = dao.getTotalRevenue(from , to);
			System.out.println("\nTotal Revenue of Publication House : " + revenue + "/-\n\n");
		} else if (option == 3) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			double expense = dao.getTotalExpense(from , to);
			System.out.println("\nTotal Expenses of Publication House : " + expense + "/-\n\n");
		} else if (option == 4) {
			int count = dDao.getDistributorsCount();
			System.out.println("\nTotal Current No of Distributors : " + count + "\n\n");
		} else if (option == 5) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			List<Map<String, String>> results = dao.getRevenuePerCity(from , to);
			List<String> headers = new ArrayList<>(Arrays.asList(CITY, REVENUE));
			OutputPrintUtil.printResults(results, headers);
		} else if (option == 6) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			List<Map<String, String>> results = dao.getRevenuePerDistributor(from , to);
			List<String> headers = new ArrayList<>(Arrays.asList(DID, REVENUE));
			OutputPrintUtil.printResults(results, headers);
		} else if (option == 7) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			List<Map<String, String>> results = dao.getRevenuePerLocation(from , to);
			List<String> headers = new ArrayList<>(Arrays.asList(LOCATION, REVENUE));
			OutputPrintUtil.printResults(results, headers);
		} else if (option == 8) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			List<Map<String, String>> results = dao.getPaymentToAuthorPerTime(from , to);
			List<String> headers = new ArrayList<>(Arrays.asList(YEAR, MONTH, AUTHOR_ID, PAYMENT));
			OutputPrintUtil.printResults(results, headers);
			System.out.println();
			results = dao.getPaymentToEditorPerTime(from , to);
			headers = new ArrayList<>(Arrays.asList(YEAR, MONTH, EDITOR_ID, PAYMENT));
			OutputPrintUtil.printResults(results, headers);
		} else if (option == 9) {
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			List<Map<String, String>> results = dao.getPaymentPerWorkType(from , to);
			List<String> headers = new ArrayList<>(Arrays.asList(WORK_TYPE, PAYMENT));
			OutputPrintUtil.printResults(results, headers);
		} else if(option == 0) {
			//Nothing to do
		} else {
			System.out.println("\tUser entered a invalid option, Returning to main menu...!!");
		}

	}	
}
