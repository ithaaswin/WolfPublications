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
 * This class take care of all DBMS calls related to Transactions, Salary
 * Payments, Shipment costs, Distributor Payments
 * 
 * @author Ishwari
 *
 */
public class TransactionsDAO extends FieldConstants {

	private static final String INSERT_PAYMENT = "INSERT INTO Transactions VALUES(?,?,?,?,?,?)";
	private static final String INSERT_STAFF_PAYMENT = "INSERT INTO GetsPaid VALUES(?,?,?)";

	private static final String INSERT_CREDIT_BY_DISTRIBUTOR = "INSERT INTO MadeBy VALUES(?,?)";
	private static final String INSERT_PAYMENT_FOR_ORDER = "INSERT INTO MadeFor VALUES(?,?)";

	private static final String UPDATE_OUTSTANDING_BALANCE = "UPDATE Distributor SET OutstandingBalance = ? where DID =?";

	private static final String GET_PAYMENTS_TO_STAFF = "select G.TID,G.ID, G.WorkType, T.TransactionDate, T.Amount, T.PaymentMode from GetsPaid G join Transactions T ON T.TID = G.TID where G.ID = ? AND T.TransactionDate BETWEEN ? AND ?";

	private IDBConnection client = DBConnectionClient.getDBClient();

	/**
	 * This method is to record a payment made for shipping cost by publication
	 * house
	 * 
	 * @param tid
	 * @param transactionDate
	 * @param amount
	 * @param debitCredit
	 * @param paymentMode
	 * @param transactionType
	 * @param orderId
	 */
	public void createPaymentForShippingCost(int tid, Date transactionDate, float amount, String debitCredit,
			String paymentMode, String transactionType, int orderId) {

		Connection connection = client.getConnection();

		try {
			connection.setAutoCommit(false);
			PreparedStatement statement1 = connection.prepareStatement(INSERT_PAYMENT);

			statement1.setInt(1, tid);
			statement1.setDate(2, new java.sql.Date(transactionDate.getTime()));
			statement1.setFloat(3, amount);
			statement1.setString(4, debitCredit);
			statement1.setString(5, paymentMode);
			statement1.setString(6, transactionType);

			PreparedStatement statement2 = connection.prepareStatement(INSERT_PAYMENT_FOR_ORDER);
			statement2.setInt(1, tid);
			statement2.setInt(2, orderId);

			statement1.executeUpdate();
			statement2.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details");
			if (connection != null) {
				try {
					connection.rollback();
				} catch (Exception nestedException) {
					// Nothing to do
				}

			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// Nothing to do
			}
		}
	}

	/**
	 * This method is to record a payment made by distributor to publication house
	 * 
	 * @param tid
	 * @param transactionDate
	 * @param amount
	 * @param debitCredit
	 * @param paymentMode
	 * @param transactionType
	 * @param dID
	 */
	public void createPaymentByDistributor(int tid, Date transactionDate, float amount, String debitCredit,
			String paymentMode, String transactionType, int dID) {
		DistributorDAO dDao = new WolfDAOFactory().getDistributorDAO();
		Connection connection = client.getConnection();

		try {
			connection.setAutoCommit(false);
			PreparedStatement statement1 = connection.prepareStatement(INSERT_PAYMENT);

			statement1.setInt(1, tid);
			statement1.setDate(2, new java.sql.Date(transactionDate.getTime()));
			statement1.setFloat(3, amount);
			statement1.setString(4, debitCredit);
			statement1.setString(5, paymentMode);
			statement1.setString(6, transactionType);

			PreparedStatement statement2 = connection.prepareStatement(INSERT_CREDIT_BY_DISTRIBUTOR);
			statement2.setInt(1, tid);
			statement2.setInt(2, dID);

			float balance = dDao.getDistributorOutStandingBalance(dID);
			float outstanding = balance - amount;

			PreparedStatement statement3 = connection.prepareStatement(UPDATE_OUTSTANDING_BALANCE);
			statement3.setFloat(1, outstanding);
			statement3.setInt(2, dID);

			statement1.executeUpdate();
			statement2.executeUpdate();
			statement3.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details");
			if (connection != null) {
				try {
					connection.rollback();
				} catch (Exception nestedException) {
					// Nothing to do
				}

			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// Nothing to do
			}
		}

	}

	/**
	 * This method is to record a payment made to staff member to publication house
	 * as Salary for some other work
	 * 
	 * @param tid
	 * @param transactionDate
	 * @param amount
	 * @param debitCredit
	 * @param paymentMode
	 * @param transactionType
	 * @param ID
	 * @param workType
	 */
	public void createPaymentToStaff(int tid, Date transactionDate, float amount, String debitCredit,
			String paymentMode, String transactionType, int ID, String workType) {

		IDBConnection client = DBConnectionClient.getDBClient();
		Connection connection = client.getConnection();

		try {
			connection.setAutoCommit(false);
			PreparedStatement statement1 = connection.prepareStatement(INSERT_PAYMENT);

			statement1.setInt(1, tid);
			statement1.setDate(2, new java.sql.Date(transactionDate.getTime()));
			statement1.setFloat(3, amount);
			statement1.setString(4, debitCredit);
			statement1.setString(5, paymentMode);
			statement1.setString(6, transactionType);

			PreparedStatement statement2 = connection.prepareStatement(INSERT_STAFF_PAYMENT);
			statement2.setInt(1, tid);
			statement2.setInt(2, ID);
			statement2.setString(3, workType);

			statement1.executeUpdate();
			statement2.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details");
			if (connection != null) {
				try {
					connection.rollback();
				} catch (Exception nestedException) {
					// Nothing to do
				}

			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// Nothing to do
			}
		}
	}

	/**
	 * This method is to get payment details made by publication house for any staff
	 * member
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Map<String, String>> getPaynmentToStaff(int id, Date from, Date to) {

		IDBConnection client = DBConnectionClient.getDBClient();
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PAYMENTS_TO_STAFF)) {

			preparedStatement.setInt(1, id);
			preparedStatement.setDate(2, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(3, new java.sql.Date(to.getTime()));

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(TID, rs.getString(TID));
				map.put(ID, rs.getString(ID));
				map.put(WORK_TYPE, rs.getString(WORK_TYPE));
				map.put(TRANSACTION_DATE, rs.getString(TRANSACTION_DATE));
				map.put(AMOUNT, rs.getString(AMOUNT));
				map.put(PAYMENT_MODE, rs.getString(PAYMENT_MODE));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details");
		}
		return result;

	}

}
