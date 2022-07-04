package com.ncsu.wolfpub.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ncsu.wolfpub.constants.FieldConstants;
import com.ncsu.wolfpub.dao.DistributorDAO;
import com.ncsu.wolfpub.dao.TransactionsDAO;
import com.ncsu.wolfpub.dao.WolfDAOFactory;
import com.ncsu.wolfpub.util.InputUtil;
import com.ncsu.wolfpub.util.OutputPrintUtil;

public class DistributorProcessor extends FieldConstants {

	public static void printMenu() {
		System.out.println("\t-------------Distribution-------------");
		System.out.println("\t1. Enter new distributor");
		System.out.println("\t2. Update distributor information");
		System.out.println("\t3. Delete a distributor");
		System.out.println("\t4. Place an order by Distributor");
		System.out.println("\t5. Generate bill for order");
		System.out.println("\t6. Receive payment and update outstanding balance for distributor");
		System.out.println("\t7. Enter payment made for shipping an order");
		System.out.println("\t8. View distributors");
		System.out.println("\t0. Back to main menu");
	}

	/**
	 * Process the Sub Menu 3
	 * @param option
	 */
	public static void processTask(int option) {
		
		WolfDAOFactory factory = new WolfDAOFactory();
		DistributorDAO dao = factory.getDistributorDAO();
		TransactionsDAO tDao = factory.getTransactionsDAO();
		
		if(option == 1) {
			int did = InputUtil.getInteger("DID");
			int ein = InputUtil.getInteger("EIN");
			String dname = InputUtil.getString("Distributor Name");
			float outstandingBalance = InputUtil.getFloat("OutStandingBalance");
			String type = InputUtil.getString("Distributor Type");
			String address = InputUtil.getString("Address");
			String city = InputUtil.getString("City");
			String location = InputUtil.getString("Location");
			String poc = InputUtil.getString("Point of Contact");
			String phoneNumber = InputUtil.getPhoneNumber("Phone Number");
			dao.createDistributor(did, ein, dname, outstandingBalance, type, address, city, location, poc, phoneNumber);
		} else if(option == 2) {
			String fieldName = InputUtil.getString("FieldName");
			while(DID.equalsIgnoreCase(fieldName)) {
				System.out.println("\tYou cannot update DID..!!");
				fieldName = InputUtil.getString("FieldName");
			}
			String fieldValue = InputUtil.getString("Value");
			int did = InputUtil.getInteger("DID");
			dao.updateDistributorInfo(fieldName, fieldValue, did);
			
		} else if(option == 3) {
			
			int did = InputUtil.getInteger("DID");
			dao.deleteDistributor(did);
			
		} else if(option == 4) {
			
			int orderId = InputUtil.getInteger("OrderId");
			int did = InputUtil.getInteger("Distributor ID");
			Date orderDate = InputUtil.getDate("Date of Order");
			Date deliveryDate = InputUtil.getDate("Delivery Date");
			
			
			
			System.out.println("\n\t\t\t---OrderDetails----");
			List<int[]> orders = new ArrayList<int[]>();
			String nextChoice = "y";
			while("Y".equalsIgnoreCase(nextChoice)) {
				System.out.println();
				int pId  = InputUtil.getInteger("PID");
				int noOfCopies  = InputUtil.getInteger("Number of Copies");
				int[] order = new int[] {pId, noOfCopies};
				orders.add(order);
				nextChoice = InputUtil.getString("Do you want to add more items(y/n)");
			}
			System.out.println();
			float shippingCost = InputUtil.getFloat("Shipping Cost");
			
			dao.placeOrderAndUpdateDistributor(orderId, did, orderDate, deliveryDate, shippingCost, orders);
			
		} else if(option == 5) {
			int orderId = InputUtil.getInteger("Order ID");
			System.out.println("---------------------------------------------------------------------------");
			Map<String, String> orderSummary = dao.getOrderSummary(orderId);
			List<String> otherHeaders = new ArrayList<>(Arrays.asList(ORDER_ID, DID, DISTRIBUTOR_NAME, ORDER_DATE, DELIVERY_DATE));
			OutputPrintUtil.printResults(orderSummary, otherHeaders);
			
			List<Map<String, String>> result = dao.getOrderDetails(orderId);
			List<String> headers = new ArrayList<>(Arrays.asList(PID, TITLE, NUMBER_OF_COPIES, PURCHASE_COST, TOTAL));
			OutputPrintUtil.printResults(result, headers);
			
			otherHeaders = new ArrayList<>(Arrays.asList(PRICE_OF_ORDER, SHIPPING_COST));
			OutputPrintUtil.printResults(orderSummary, otherHeaders);
			System.out.println("---------------------------------------------------------------------------");
		
		} else if(option == 6) {
			int tid = InputUtil.getInteger("TID");
			int did = InputUtil.getInteger("DID");
			float amount = InputUtil.getFloat("Amount");
			String debitcredit = "Credit";
			String paymentMode = InputUtil.getString("Payment Mode");
			String transactionType = "Distributor";
			Date date = InputUtil.getDate("Transaction Date");
			
			tDao.createPaymentByDistributor(tid, date, amount, debitcredit, paymentMode, transactionType, did);
			
			
		} else if(option == 7) {
			int tid = InputUtil.getInteger("TID");
			int orderId = InputUtil.getInteger("Order ID");
			String debitcredit = "Debit";
			String paymentMode = InputUtil.getString("Payment Mode");
			String transactionType = "Shipping Cost";
			Date date = InputUtil.getDate("Transaction Date");
			
			float amount = dao.getShippingCost(orderId);
			
			tDao.createPaymentForShippingCost(tid, date, amount, debitcredit, paymentMode, transactionType, orderId);
			
		} else if(option == 8) {
			List<Map<String, String>> results = new ArrayList<Map<String, String>>();
			List<String> headers = new ArrayList<>(Arrays.asList(DID, EIN, DISTRIBUTOR_NAME, TYPE, ADDRESS, CITY,
					LOCATION, POINT_OF_CONTACT, PHONE_NUMBER, OUT_STANDING_BALANCE));
			int choice = printSelecDistributorMenu();
			if(choice == 1) {
				results = dao.getAllDistributors();
			} else if(choice == 2) {
				int from = InputUtil.getInteger("From");
				int to = InputUtil.getInteger("To");
				results = dao.getAllDistributorsByIntField(PID, from, to);
			} else if(choice == 3) {
				int from = InputUtil.getInteger("From");
				int to = InputUtil.getInteger("To");
				results = dao.getAllDistributorsByIntField(EIN, from, to);
			} else if(choice == 4) {
				String value = InputUtil.getString("Value");
				results = dao.getAllDistributorsByStirngField(DISTRIBUTOR_NAME, value);
			} else if(choice == 5) {
				String value = InputUtil.getString("Value");
				results = dao.getAllDistributorsByStirngField(TYPE, value);
			} else if(choice == 6) {
				String value = InputUtil.getString("Value");
				results = dao.getAllDistributorsByStirngField(CITY, value);
			} else if(choice == 7) {
				String value = InputUtil.getString("Value");
				results = dao.getAllDistributorsByStirngField(LOCATION, value);
			}
			OutputPrintUtil.printResults(results, headers);
		} else if(option == 0) {
			//Nothing to do
		} else {
			System.out.println("\tUser entered a invalid option, Returning to main menu...!!");
		}
	}
	
	private static int printSelecDistributorMenu() {

		System.out.println("\t\t1. Select All");
		System.out.println("\t\t2. Based on PID");
		System.out.println("\t\t3. Based on EIN");
		System.out.println("\t\t4. Based on Name");
		System.out.println("\t\t5. Based on Type");
		System.out.println("\t\t6. Based on City");
		System.out.println("\t\t7. Based on Location");

		return InputUtil.getInteger("Select your choice : ");

	}
}
