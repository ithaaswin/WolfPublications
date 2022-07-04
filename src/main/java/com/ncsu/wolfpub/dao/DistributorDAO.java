package com.ncsu.wolfpub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ncsu.wolfpub.constants.FieldConstants;
import com.ncsu.wolfpub.db.connection.DBConnectionClient;
import com.ncsu.wolfpub.db.connection.IDBConnection;

/**
 * 
 * This class is to make CRUD operations related to distributor, and orders.
 * @author Aswin Itha
 *
 */

public class DistributorDAO extends FieldConstants {


	private static final String FIELD_NAME_TAG = "<FieldName>";

	private static final String CREATE_DISTRIBUTOR = "INSERT INTO Distributor VALUES(?,?,?,?,?,?,?,?,?,?)";
	private static final String DELETE_DISTRIBUTOR = "DELETE FROM Distributor WHERE DID = ?";
	
	private static final String GET_DISTRIBUTOR = "SELECT DID, EIN, DName, OutstandingBalance, Type, Address, City, Location, POC, PhoneNumber from Distributor";
	private static final String GET_DISTRIBUTOR_BY_DID = "SELECT DID, EIN, DName, OutstandingBalance, Type, Address, City, Location, POC, PhoneNumber from Distributor WHERE DID BETWEEN ? AND ?";
	private static final String GET_DISTRIBUTOR_BY_EIN = "SELECT DID, EIN, DName, OutstandingBalance, Type, Address, City, Location, POC, PhoneNumber from Distributor WHERE EIN BETWEEN ? AND ?";
	
	private static final String GET_DISTRIBUTOR_BY_NAME = "SELECT DID, EIN, DName, OutstandingBalance, Type, Address, City, Location, POC, PhoneNumber from Distributor WHERE DName LIKE ?";
	private static final String GET_DISTRIBUTOR_BY_TYPE = "SELECT DID, EIN, DName, OutstandingBalance, Type, Address, City, Location, POC, PhoneNumber from Distributor WHERE Type LIKE ?";
	private static final String GET_DISTRIBUTOR_BY_CITY = "SELECT DID, EIN, DName, OutstandingBalance, Type, Address, City, Location, POC, PhoneNumber from Distributor WHERE City LIKE ?";
	private static final String GET_DISTRIBUTOR_BY_LOCATION = "SELECT DID, EIN, DName, OutstandingBalance, Type, Address, City, Location, POC, PhoneNumber from Distributor WHERE LOCATION LIKE ?";

	private static final String GET_DISTRIBUTOR_COUNT = "SELECT COUNT(*) AS NoOfDistributors FROM Distributor";

	private static final String UPDATE_OUTSTANDING_BALANCE = "UPDATE Distributor SET OutstandingBalance = ? where DID =?";

	private static final String GET_OUTSTANDING_BALANCE = "SELECT OutstandingBalance FROM Distributor where DID =?";
	private static final String UPDATE_DISTRIBUTOR_INFO = "UPDATE Distributor SET <FieldName>=? WHERE DID=?";

	private static final String NO_OF_DISTRIBUTOR = "NoOfDistributors";
	
	
	private static final String CREATE_ORDER = "INSERT INTO Orders VALUES(?,?,?,?,?,?)";
	private static final String ADD_ITEMS_TO_ORDER = "INSERT INTO Has VALUES(?,?,?,?)";
	private static final String GET_SHIPPING_COST_FOR_ODER = "SELECT ShippingCost from Orders WHERE OrderID = ?";

	private static final String GET_ORDER_DETAILS = "select H.PID, P.Title, H.NumberOfCopies, H.PurchaseCost, (H.NumberOfCopies*H.PurchaseCost) as Total from Has H join Publication P ON H.PID = P.PID where H.OrderId=?";
	private static final String GET_ORDER_SUMMARY = "select O.OrderID, O.DID, O.OrderDate, O.PriceOfOrder, O.DeliveryDate, O.ShippingCost, D.DName from Orders O JOIN Distributor D ON O.DID = D.DID where O.OrderID=?";
	
	IDBConnection client = DBConnectionClient.getDBClient();
	
	/**
	 * This method is to get all distributors by any string field like name, type, city, location.
	 * 
	 * @param fieldName fieldName
	 * @param value value
	 * @return returns list of all distributors
	 */
	public List<Map<String, String>> getAllDistributorsByStirngField(String fieldName, String value) {
		
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		String statement = GET_DISTRIBUTOR_BY_NAME;
		if(TYPE.equalsIgnoreCase(fieldName)) {
			statement = GET_DISTRIBUTOR_BY_TYPE;
		} else if(CITY.equalsIgnoreCase(fieldName)) {
			statement = GET_DISTRIBUTOR_BY_CITY;
		} else if(LOCATION.equalsIgnoreCase(fieldName)) {
			statement = GET_DISTRIBUTOR_BY_LOCATION;
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

			String fieldValue = "%" + value + "%";
			preparedStatement.setString(1, fieldValue);
			
			ResultSet rs = preparedStatement.executeQuery();

			getDistributorDetailsFromRs(result, rs);
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}
	
	/**
	 *  This method is to get all distributors based on any integer fields by DID, EIN.
	 * @param fieldName field name
	 * @param from range from
	 * @param to range to 
	 * @return list of all distributors
	 */
	public List<Map<String, String>> getAllDistributorsByIntField(String fieldName, int from, int to) {
		
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		String statement = GET_DISTRIBUTOR_BY_DID;
		if(EIN.equalsIgnoreCase(fieldName)) {
			statement = GET_DISTRIBUTOR_BY_EIN;
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

			preparedStatement.setInt(1,  from);
			preparedStatement.setInt(2,  to);
			ResultSet rs = preparedStatement.executeQuery();

			getDistributorDetailsFromRs(result, rs);
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/**
	 * This method is to get all distributors
	 * @return list of all distributors
	 */
	public List<Map<String, String>> getAllDistributors() {
		
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();


		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_DISTRIBUTOR)) {


			ResultSet rs = preparedStatement.executeQuery();

			getDistributorDetailsFromRs(result, rs);
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/**
	 * This method is to convert from Result set to Distributor details
	 * @param result 
	 * @param rs
	 * @throws SQLException
	 */
	private void getDistributorDetailsFromRs(List<Map<String, String>> result, ResultSet rs) throws SQLException {
		while (rs.next()) {
			Map<String, String> map = new HashMap<>();

			map.put(DID, rs.getString(DID));
			map.put(EIN, rs.getString(EIN));
			map.put(DISTRIBUTOR_NAME, rs.getString(DISTRIBUTOR_NAME));
			map.put(OUT_STANDING_BALANCE, rs.getString(OUT_STANDING_BALANCE));
			map.put(TYPE, rs.getString(TYPE));
			map.put(ADDRESS, rs.getString(ADDRESS));
			map.put(CITY, rs.getString(CITY));
			map.put(LOCATION, rs.getString(LOCATION));
			map.put(POINT_OF_CONTACT, rs.getString(POINT_OF_CONTACT));
			map.put(PHONE_NUMBER, rs.getString(PHONE_NUMBER));
			result.add(map);
		}
	}
	
	/**
	 * This method is to get all order summary
	 * @param orderId orderId for summary required
	 * @return
	 */
	public Map<String, String> getOrderSummary(int orderId) {
		Connection connection = client.getConnection();

		Map<String, String> result = new HashMap<>();


		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_SUMMARY)) {

			preparedStatement.setInt(1, orderId);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {

				result.put(ORDER_ID, rs.getString(ORDER_ID));
				result.put(DID, rs.getString(DID));
				result.put(ORDER_DATE, rs.getString(ORDER_DATE));
				result.put(PRICE_OF_ORDER, rs.getString(PRICE_OF_ORDER));
				result.put(DELIVERY_DATE, rs.getString(DELIVERY_DATE));
				result.put(SHIPPING_COST, rs.getString(SHIPPING_COST));
				result.put(DISTRIBUTOR_NAME, rs.getString(DISTRIBUTOR_NAME));

			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
		
	}
	
	/**
	 * This method is to get order details
	 * @param orderId get orderid for which order details are required
	 * @return
	 */
	public List<Map<String, String>> getOrderDetails(int orderId) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();


		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_DETAILS)) {

			preparedStatement.setInt(1, orderId);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(TITLE, rs.getString(TITLE));
				map.put(NUMBER_OF_COPIES, rs.getString(NUMBER_OF_COPIES));
				map.put(PURCHASE_COST, rs.getString(PURCHASE_COST));
				map.put(TOTAL, rs.getString(TOTAL));
				
				result.add(map);
			}
			
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
		
		
	}
	
	/**
	 * This method returns the shipping cost of the order
	 * @param orderId
	 * @return
	 */
	public float getShippingCost(int orderId) {
		Connection connection = client.getConnection();

		float shippingCost = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_SHIPPING_COST_FOR_ODER)) {

			preparedStatement.setInt(1, orderId);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				shippingCost = rs.getInt(SHIPPING_COST);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return shippingCost;
		
	}
	
	/**
	 * This method creates the distributor from the input arguments
	 * @param dID
	 * @param ein
	 * @param dName
	 * @param outstandingBalance
	 * @param type
	 * @param address
	 * @param city
	 * @param location
	 * @param poc
	 * @param phoneNumber
	 */
	public void createDistributor(int dID, int ein, String dName, float outstandingBalance, String type, String address,
			String city, String location, String poc, String phoneNumber) {
		
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_DISTRIBUTOR)) {
			preparedStatement.setInt(1, dID);
			preparedStatement.setInt(2, ein);
			preparedStatement.setString(3, dName);
			preparedStatement.setFloat(4, outstandingBalance);
			preparedStatement.setString(5, type);
			preparedStatement.setString(6, address);
			preparedStatement.setString(7, city);
			preparedStatement.setString(8, location);
			preparedStatement.setString(9, poc);
			preparedStatement.setString(10, phoneNumber);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get inserted");
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/**
	 * This method deletes the distributor giving dId.
	 * @param DID
	 */
	public void deleteDistributor(int DID) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DISTRIBUTOR)) {
			preparedStatement.setInt(1, DID);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row is deleted");
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/**
	 * This method returns the total number of distributors.
	 * @return
	 */
	public int getDistributorsCount() {
		Connection connection = client.getConnection();

		int count = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_DISTRIBUTOR_COUNT)) {

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				count = rs.getInt(NO_OF_DISTRIBUTOR);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return count;
	}

	/**
	 * This method updates the outstanding balance for given DID with given balance.
	 * @param balance
	 * @param dId
	 */
	public void updateOutStandingBalance(float balance, int dId) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_OUTSTANDING_BALANCE)) {
			preparedStatement.setFloat(1, balance);
			preparedStatement.setInt(2, dId);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row is updated");
		} catch (SQLException e) {
			System.out.println("Operation failed. Please enter valid details");
		}

	}

	/**
	 * This method updates the distributor info.
	 * @param fieldName
	 * @param fieldValue
	 * @param dId
	 */
	public void updateDistributorInfo(String fieldName, String fieldValue, int dId) {
		Connection connection = client.getConnection();

		String sqlStatement = UPDATE_DISTRIBUTOR_INFO;
		sqlStatement = sqlStatement.replaceAll(FIELD_NAME_TAG, fieldName);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {

			preparedStatement.setInt(2, dId);

			if (fieldName.equalsIgnoreCase(EIN)) {
				int value = Integer.parseInt(fieldValue);
				preparedStatement.setInt(1, value);
			} else {
				preparedStatement.setString(1, fieldValue);
			}

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}

	}

	/**
	 * This method creates the order, places the order with publications and no of publications 
	 * and updates the fdistributor outstanding balance. 
	 * @param orderId
	 * @param did
	 * @param orderDate
	 * @param deliveryDate
	 * @param shippingCost
	 * @param items
	 */
	public void placeOrderAndUpdateDistributor(int orderId, int did, Date orderDate, Date deliveryDate,
			float shippingCost, List<int[]> items) {
		Connection connection = client.getConnection();
		try {
			connection.setAutoCommit(false);
			
			PreparedStatement statement1 = connection.prepareStatement(CREATE_ORDER);
			statement1.setInt(1, orderId);
			statement1.setInt(2, did);
			statement1.setDate(3, new java.sql.Date(orderDate.getTime()));
			statement1.setDate(5, new java.sql.Date(deliveryDate.getTime()));
			statement1.setFloat(6, shippingCost);
			float orderCost = 0;
			
			PreparedStatement statement2 = connection.prepareStatement(ADD_ITEMS_TO_ORDER); 
			for(int[] item : items) {
				statement2.setInt(1, item[0]);
				statement2.setInt(2, orderId);
				statement2.setInt(3, item[1]);
				float cost = costOfPID(item[0]);
				statement2.setFloat(4, cost);
				orderCost += (item[1] * cost);
				statement2.addBatch();
			}
			
			float distributorBalance = getDistributorOutStandingBalance(did);
			
			PreparedStatement statement3 = connection.prepareStatement(UPDATE_OUTSTANDING_BALANCE);
			float outStandingBalance = distributorBalance+orderCost;
			statement3.setFloat(1, outStandingBalance);
			statement3.setInt(2, did);
			
			statement1.setFloat(4, orderCost);
			
			statement1.executeUpdate();
			statement2.executeBatch();
			statement3.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
			if(connection != null) {
				try{
					connection.rollback(); 
				} catch(Exception nestedException) {
					//Nothing to do 
				}
				
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
					//Nothing to do 
			}
		}
	}



	/**
	 * This method returns the out standing balance of a given distributor.
	 * @param did
	 * @return
	 */
	public float getDistributorOutStandingBalance(int did) {
		Connection connection = client.getConnection();

		float balance = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_OUTSTANDING_BALANCE)) {

			preparedStatement.setInt(1, did);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				balance = rs.getInt(OUT_STANDING_BALANCE);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return balance;
	}

	private float costOfPID(int pId) {
		WolfDAOFactory factory = new WolfDAOFactory();
		return factory.getPublicationDAO().getCostOfPublication(pId);
	}
	
	

}
