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
 * This class contains all database calls related to analytics reports.
 * 
 * @author vamsi
 *
 */
public class AnalyticsDAO extends FieldConstants {

	private static final String GET_TOTAL_EXPENSE = "SELECT SUM(Amount) AS Expense FROM Transactions WHERE TransactionDate BETWEEN ? AND ?  AND DebitCredit=\"Debit\"";
	private static final String GET_TOTAL_REVENUE = "SELECT SUM(Amount) AS Revenue FROM Transactions WHERE TransactionDate BETWEEN ? AND ?  AND DebitCredit=\"Credit\"";

	private static final String GET_STATS_PER_PUB_DIST_MONTH = "SELECT 	Year(O.OrderDate) As Year, Month(O.OrderDate) AS Month, PID, SUM(NumberOfCopies) AS NoOfCopies, O.DID, SUM(H.PurchaseCost) AS PurchaseCost FROM Has H JOIN Orders O ON H.OrderID=O.OrderID WHERE O.OrderDate BETWEEN ? AND ? GROUP BY Year,Month,PID, O.DID ORDER BY 	Year DESC, MONTH DESC";
	private static final String GET_REVENUE_PER_CITY = "SELECT 	City, SUM(Amount) AS Revenue from Transactions T JOIN MadeBy M on T.TID = M.TID JOIN Distributor D on M.DID = D.DID WHERE T.TransactionDate BETWEEN ? AND ? GROUP BY City";
	private static final String GET_REVENUE_PER_LOCATION = "SELECT 	Location, SUM(Amount) AS Revenue from Transactions T JOIN MadeBy M 	on 	T.TID = M.TID JOIN 	Distributor D on M.DID = D.DID WHERE T.TransactionDate BETWEEN ? AND ? GROUP BY Location";
	private static final String GET_REVENUE_PER_DISTRIBUTOR = "SELECT 	M.DID, sum(T.Amount) AS Revenue from Transactions T JOIN MadeBy M on T.TID=M.TID WHERE T.TransactionDate BETWEEN ? AND ? GROUP BY M.DID";
	private static final String GET_PAYMENT_TO_AUTHOR_PER_TIME = "SELECT A.AuthorID,Year(T.TransactionDate) As Year, Month(T.TransactionDate) AS Month, SUM(T.Amount) AS Payment FROM    Transactions T JOIN GetsPaid G  ON  G.TID = T.TID JOIN Author A ON  A.AuthorID = G.ID  WHERE T.TransactionDate BETWEEN ? AND ? GROUP BY Year, Month, A.AuthorID ORDER BY Year DESC,Month Desc";
	private static final String GET_PAYMENT_TO_EDITOR_PER_TIME = "SELECT  E.EditorID,Year(T.TransactionDate) As Year, Month(T.TransactionDate) AS Month, SUM(T.Amount) AS Payment FROM Transactions T JOIN GetsPaid G  ON  G.TID = T.TID JOIN Editor E ON  E.EditorID = G.ID  WHERE T.TransactionDate BETWEEN ? AND ? GROUP BY Year, Month, E.EditorID ORDER BY Year DESC,Month Desc";
	private static final String GET_PAYMENT_PER_WORK_TYPE = "SELECT  G.WorkType, SUM(T.Amount) AS Payment FROM Transactions T JOIN GetsPaid G  ON  T.TID = G.TID  WHERE T.TransactionDate BETWEEN ? AND ? GROUP BY G.WorkType";

	private IDBConnection client = DBConnectionClient.getDBClient();

	/**
	 * Returns the reports of all publications per distributor per month from
	 * FromDate to ToDate specified in inputs.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Map<String, String>> getStatsPerPubDistMonth(Date from, Date to) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STATS_PER_PUB_DIST_MONTH)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put(YEAR, rs.getString(YEAR));
				map.put(MONTH, rs.getString(MONTH));
				map.put(DID, rs.getString(DID));
				map.put(PID, rs.getString(PID));
				map.put(NO_OF_COPIES, rs.getString(NO_OF_COPIES));
				map.put(PURCHASE_COST, rs.getString(PURCHASE_COST));

				result.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Returns the reports of revenues per city from FromDate to ToDate specified in
	 * inputs.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Map<String, String>> getRevenuePerCity(Date from, Date to) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_REVENUE_PER_CITY)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put(CITY, rs.getString(CITY));
				map.put(REVENUE, rs.getString(REVENUE));
				result.add(map);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return result;
	}

	/**
	 * Returns the reports of revenues per location from FromDate to ToDate
	 * specified in inputs
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Map<String, String>> getRevenuePerLocation(Date from, Date to) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_REVENUE_PER_LOCATION)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put(LOCATION, rs.getString(LOCATION));
				map.put(REVENUE, rs.getString(REVENUE));
				result.add(map);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return result;
	}

	/**
	 * Returns the reports of revenues per distributor from FromDate to ToDate
	 * specified in inputs
	 * 
	 * @param from
	 * @param to
	 * @return
	 */

	public List<Map<String, String>> getRevenuePerDistributor(Date from, Date to) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_REVENUE_PER_DISTRIBUTOR)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put(DID, rs.getString(DID));
				map.put(REVENUE, rs.getString(REVENUE));
				result.add(map);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return result;
	}

	/**
	 * Returns the reports of total expenses from FromDate to ToDate specified in
	 * inputs
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public double getTotalExpense(Date from, Date to) {
		Connection connection = client.getConnection();

		double expense = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_EXPENSE)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				expense = rs.getDouble(EXPENSE);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return expense;

	}

	/**
	 * Returns the reports of total revenue from FromDate to ToDate specified in
	 * inputs
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public double getTotalRevenue(Date from, Date to) {
		Connection connection = client.getConnection();

		double revenue = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_REVENUE)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				revenue = rs.getDouble(REVENUE);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return revenue;

	}

	/**
	 * Returns the reports of total payments to authors from FromDate to ToDate
	 * specified in inputs
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Map<String, String>> getPaymentToAuthorPerTime(Date from, Date to) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PAYMENT_TO_AUTHOR_PER_TIME)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put(YEAR, rs.getString(YEAR));
				map.put(MONTH, rs.getString(MONTH));
				map.put(AUTHOR_ID, rs.getString(AUTHOR_ID));
				map.put(PAYMENT, rs.getString(PAYMENT));
				result.add(map);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return result;
	}

	/**
	 * Returns the reports of total payments to editors from FromDate to ToDate
	 * specified in inputs
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Map<String, String>> getPaymentToEditorPerTime(Date from, Date to) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PAYMENT_TO_EDITOR_PER_TIME)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put(YEAR, rs.getString(YEAR));
				map.put(MONTH, rs.getString(MONTH));
				map.put(EDITOR_ID, rs.getString(EDITOR_ID));
				map.put(PAYMENT, rs.getString(PAYMENT));
				result.add(map);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return result;
	}

	/**
	 * Returns the reports of total payments made per work type from FromDate to
	 * ToDate specified in inputs
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Map<String, String>> getPaymentPerWorkType(Date from, Date to) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PAYMENT_PER_WORK_TYPE)) {

			preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put(WORK_TYPE, rs.getString(WORK_TYPE));
				map.put(PAYMENT, rs.getString(PAYMENT));
				result.add(map);
			}
		} catch (SQLException e) {
			// Nothing to do
		}
		return result;
	}

}
