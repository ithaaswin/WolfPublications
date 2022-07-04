package com.ncsu.wolfpub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * @author vamsi
 *
 */
public class PublicationDAO extends FieldConstants {

	private static final String FIELD_NAME_TAG = "<FieldName>";

	private IDBConnection client = DBConnectionClient.getDBClient();

	private static final String CREATE_PUBLICATION = "INSERT INTO Publication VALUES(?,?,?,?,?)";
	private static final String UPDATE_PUBLICATION = "UPDATE Publication SET <FieldName> = ?  WHERE PID=?";
	private static final String DELETE_PUBLICATION = "DELETE FROM Publication where PID=?";
	private static final String GET_COST_OF__PUBLICATION = "SELECT CostOfEachPiece FROM Publication where PID=?";

	private static final String GET_PUBLICATION_BY_EID = "SELECT PID, Title, Type, Topics, CostOfEachPiece FROM Publication WHERE PID IN (SELECT PID FROM Edits WHERE EditorID= ?)";

	private static final String GET_ALL_BOOKS = " select B.PID, B.ISBN, P.TITLE, B.Edition, P.Type, P.Topics, B.DateOfCreation, B.PublicationDate, P.CostOfEachPiece, B.TableOfContents from Books B join Publication P ON B.PID=P.PID";
	private static final String GET_BOOK_BY_ISBN = " select B.PID, B.ISBN, P.TITLE, B.Edition, P.Type, P.Topics, B.DateOfCreation, B.PublicationDate, P.CostOfEachPiece, B.TableOfContents from Books B join Publication P ON B.PID=P.PID where B.ISBN=?";
	private static final String GET_BOOK_BY_PID = " select B.PID, B.ISBN, P.TITLE, B.Edition, P.Type, P.Topics, B.DateOfCreation, B.PublicationDate, P.CostOfEachPiece, B.TableOfContents from Books B join Publication P ON B.PID=P.PID WHERE P.PID BETWEEN ?  AND ?";
	private static final String GET_BOOK_BY_ISBN_RANGE = " select B.PID, B.ISBN, P.TITLE, B.Edition, P.Type, P.Topics, B.DateOfCreation, B.PublicationDate, P.CostOfEachPiece, B.TableOfContents from Books B join Publication P ON B.PID=P.PID WHERE B.ISBN BETWEEN ?  AND ?";
	private static final String GET_BOOK_BY_EDITION = " select B.PID, B.ISBN, P.TITLE, B.Edition, P.Type, P.Topics, B.DateOfCreation, B.PublicationDate, P.CostOfEachPiece, B.TableOfContents from Books B join Publication P ON B.PID=P.PID where B.EDITION LIKE ?";
	private static final String GET_BOOK_BY_PUBLICATION_DATE = " select B.PID, B.ISBN, P.TITLE, B.Edition, P.Type, P.Topics, B.DateOfCreation, B.PublicationDate, P.CostOfEachPiece, B.TableOfContents from Books B join Publication P ON B.PID=P.PID WHERE B.PublicationDate BETWEEN ? AND ? ";
	private static final String GET_BOOK_BY_DATE_OF_CREATION = " select B.PID, B.ISBN, P.TITLE, B.Edition, P.Type, P.Topics, B.DateOfCreation, B.PublicationDate, P.CostOfEachPiece, B.TableOfContents from Books B join Publication P ON B.PID=P.PID WHERE B.DateOfCreation BETWEEN ? AND ? ";

	private static final String ASSIGN_EDITOR = "INSERT INTO Edits VALUES(?, ?)";

	private static final String CREATE_ARTICLES = "INSERT INTO Articles VALUES(?,?,?,?,?)";
	private static final String DELETE_ARTICLES = "DELETE FROM Articles WHERE PID=? AND ArtNum=?";
	private static final String UPDATE_ARTICLES = "UPDATE Articles SET <FieldName> = ? WHERE PID=? AND ArtNum=?";
	private static final String UPDATE_ARTICLE_TEXT = "UPDATE Articles SET ArticleText = ? WHERE PID=? AND ArtNum=?";
	
	private static final String GET_ALL_ARTICLES = "SELECT PID, ArtNum, ArticleName, ArticleText, DateOfCreation FROM Articles";
	private static final String GET_ARTICLES_BY_DATE = "SELECT PID, ArtNum, ArticleName, ArticleText, DateOfCreation FROM Articles where DateOfCreation BETWEEN ?  AND ?";
	private static final String GET_ARTICLES_BY_NAME = "SELECT PID, ArtNum, ArticleName, ArticleText, DateOfCreation FROM Articles where ArticleName LIKE ?";
	private static final String GET_ARTICLES_BY_NUMBER = "SELECT PID, ArtNum, ArticleName, ArticleText, DateOfCreation FROM Articles where ArtNum BETWEEN ?  AND ?";
	private static final String GET_ARTICLES_BY_PID = "SELECT PID, ArtNum, ArticleName, ArticleText, DateOfCreation FROM Articles where PID BETWEEN ?  AND ?";

	private static final String CREATE_CHAPTER = "INSERT INTO Chapter VALUES(?,?,?,?)";
	private static final String DELETE_CHAPTER = "DELETE FROM Chapter WHERE PID=? AND ChapNum=?";
	private static final String UPDATE_CHAPTER = "UPDATE Chapter SET <FieldName> = ? WHERE PID=? AND ChapNum=?";

	private static final String CREATE_BOOKS = "INSERT INTO Books VALUES(?,?,?,?,?,?)";
	private static final String DELETE_BOOKS = "DELETE FROM Books WHERE PID=?";
	private static final String UPDATE_BOOKS = "UPDATE Books SET <FieldName> = ? WHERE PID=?";

	private static final String CREATE_ISSUES = "INSERT INTO Issues VALUES(?,?,?,?,?,?)";
	private static final String DELETE_ISSUES = "DELETE FROM Issues WHERE PID=?";
	private static final String UPDATE_ISSUES = "UPDATE Issues SET <FieldName> = ? WHERE PID=?";
	
	private static final String GET_ALL_ISSUES = "select I.PID, I.IssueNumber, P.Title, I.Type, I.DateOfIssue, P.Topics, I.Periodicity, I.TableOfContents, P.CostOfEachPiece from Issues I join Publication P ON I.PID = P.PID";
	
	private static final String GET_ALL_ISSUES_BY_PID = "select I.PID, I.IssueNumber, P.Title, I.Type, I.DateOfIssue, P.Topics, I.Periodicity, I.TableOfContents, P.CostOfEachPiece from Issues I join Publication P ON I.PID = P.PID where P.PID BETWEEN ? AND ?";
	private static final String GET_ALL_ISSUES_BY_ISSUE_NUMBER = "select I.PID, I.IssueNumber, P.Title, I.Type, I.DateOfIssue, P.Topics, I.Periodicity, I.TableOfContents, P.CostOfEachPiece from Issues I join Publication P ON I.PID = P.PID where I.IssueNumber BETWEEN ? AND ?";
	private static final String GET_ALL_ISSUES_BY_TYPE = "select I.PID, I.IssueNumber, P.Title, I.Type, I.DateOfIssue, P.Topics, I.Periodicity, I.TableOfContents, P.CostOfEachPiece from Issues I join Publication P ON I.PID = P.PID where I.Type LIKE ? ";
	private static final String GET_ALL_ISSUES_BY_DOI = "select I.PID, I.IssueNumber, P.Title, I.Type, I.DateOfIssue, P.Topics, I.Periodicity, I.TableOfContents, P.CostOfEachPiece from Issues I join Publication P ON I.PID = P.PID where I.DateOfIssue BETWEEN ? AND ?";
	private static final String GET_ALL_ISSUES_BY_PERIODICITY = "select I.PID, I.IssueNumber, P.Title, I.Type, I.DateOfIssue, P.Topics, I.Periodicity, I.TableOfContents, P.CostOfEachPiece from Issues I join Publication P ON I.PID = P.PID where I.Periodicity LIKE ?";
	
	private static final String UPDATE_TABLE_OF_CONTENTS_FOR_BOOK = "update Books set TableOfContents = ? WHERE PID = ?";
	private static final String UPDATE_TABLE_OF_CONTENTS_FOR_ISSUES = "update Issues set TableOfContents = ? WHERE PID = ?";
	
	/*
	 * updates the table of contents of the book
	 * @param table, Pid
	 */
	public void updateTableOfContentsForBook(String table, int Pid) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TABLE_OF_CONTENTS_FOR_BOOK)) {

			preparedStatement.setString(1, table);
			preparedStatement.setInt(2, Pid);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}
	
	
	/*
	 * updates the table of contents of the issue
	 * @param table, Pid
	 */
	public void updateTableOfContentsForIssues(String table, int Pid) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TABLE_OF_CONTENTS_FOR_ISSUES)) {

			preparedStatement.setString(1, table);
			preparedStatement.setInt(2, Pid);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}
	
	/*
	 * gets all issues by DateOFIssue
	 * @param fromValue, toValue
	 */
	public List<Map<String, String>> getAllIssuesByDateOfIssue(Date fromValue, Date toValue) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ISSUES_BY_DOI)) {

			preparedStatement.setDate(1, new java.sql.Date(fromValue.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(toValue.getTime()));

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISSUE_NUMBER, rs.getString(ISSUE_NUMBER));
				map.put(TITLE, rs.getString(TITLE));
				map.put(TYPE, rs.getString(TYPE));
				map.put(DATE_OF_ISSUE, rs.getString(DATE_OF_ISSUE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(PERIODICITY, rs.getString(PERIODICITY));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
		
	}
	
	/*
	 * gets all issues
	 */
	public List<Map<String, String>> getAllIssues() {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ISSUES)) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISSUE_NUMBER, rs.getString(ISSUE_NUMBER));
				map.put(TITLE, rs.getString(TITLE));
				map.put(TYPE, rs.getString(TYPE));
				map.put(DATE_OF_ISSUE, rs.getString(DATE_OF_ISSUE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(PERIODICITY, rs.getString(PERIODICITY));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
		
	}
	
	/*
	 * gets all issues based on the specified field whose datatype is string
	 * @param fieldName, value of the field
	 */
	public List<Map<String, String>> getAllIssuesByStringField(String fieldName, String value) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		String statement = GET_ALL_ISSUES_BY_TYPE;
		if(fieldName.equalsIgnoreCase(ISBN))
			statement = GET_ALL_ISSUES_BY_PERIODICITY;

		try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
			
			String fieldValue = "%" + value + "%";
			preparedStatement.setString(1, fieldValue);
			
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISSUE_NUMBER, rs.getString(ISSUE_NUMBER));
				map.put(TITLE, rs.getString(TITLE));
				map.put(TYPE, rs.getString(TYPE));
				map.put(DATE_OF_ISSUE, rs.getString(DATE_OF_ISSUE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(PERIODICITY, rs.getString(PERIODICITY));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
		
	}
	
	/*
	 * gets all issues based on the specified field whose datatype is string
	 * @param fieldName, fromValue, toValue
	 */
	public List<Map<String, String>> getAllIssuesByIntField(String fieldName, int fromValue, int toValue) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		String statement = GET_ALL_ISSUES_BY_PID;
		if(fieldName.equalsIgnoreCase(ISBN))
			statement = GET_ALL_ISSUES_BY_ISSUE_NUMBER;

		try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
			
			preparedStatement.setInt(1, fromValue);
			preparedStatement.setInt(2, toValue);
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISSUE_NUMBER, rs.getString(ISSUE_NUMBER));
				map.put(TITLE, rs.getString(TITLE));
				map.put(TYPE, rs.getString(TYPE));
				map.put(DATE_OF_ISSUE, rs.getString(DATE_OF_ISSUE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(PERIODICITY, rs.getString(PERIODICITY));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
		
	}
	
	/*
	 * get cost of the Publication
	 * @param pID
	 */
	public float getCostOfPublication(int pID) {
		Connection connection = client.getConnection();

		float price = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COST_OF__PUBLICATION)) {

			preparedStatement.setInt(1, pID);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				price = rs.getInt(COST_OF_EACH_PIECE);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return price;
		
	}
	
	/*
	 * get all articles
	 */
	public List<Map<String, String>> getAllArticles() {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ARTICLES)) {
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ARTICLE_NUMBER, rs.getString(ARTICLE_NUMBER));
				map.put(ARTICLE_NAME, rs.getString(ARTICLE_NAME));
				map.put(ARTICLE_TEXT, rs.getString(ARTICLE_TEXT));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}
	
	/*
	 * get all articles based on articleName
	 * @param value of the articleName
	 */
	public List<Map<String, String>> getArticlesByArticleName(String value) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ARTICLES_BY_NAME)) {
			
			String fieldValue = "%" + value + "%";
			preparedStatement.setString(1, fieldValue);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ARTICLE_NUMBER, rs.getString(ARTICLE_NUMBER));
				map.put(ARTICLE_NAME, rs.getString(ARTICLE_NAME));
				map.put(ARTICLE_TEXT, rs.getString(ARTICLE_TEXT));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}
	
	/*
	 * get all articles based on a field value whose datatype is int
	 * @param fieldName, fromValue, toValue
	 */
	public List<Map<String, String>> getArticlesByIntField(String fieldName, int fromValue, int toValue) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		String statement = GET_ARTICLES_BY_PID;
		if(fieldName.equalsIgnoreCase(ARTICLE_NUMBER))
			statement = GET_ARTICLES_BY_NUMBER;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

			preparedStatement.setInt(1, fromValue);
			preparedStatement.setInt(2, toValue);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ARTICLE_NUMBER, rs.getString(ARTICLE_NUMBER));
				map.put(ARTICLE_NAME, rs.getString(ARTICLE_NAME));
				map.put(ARTICLE_TEXT, rs.getString(ARTICLE_TEXT));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}
	
	/*
	 * get all articles based on date of creation
	 * @param fromValue, toValue
	 */
	public List<Map<String, String>> getArticlesByDateOfCreation(Date fromValue, Date toValue) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ARTICLES_BY_DATE)) {

			preparedStatement.setDate(1, new java.sql.Date(fromValue.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(toValue.getTime()));

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ARTICLE_NUMBER, rs.getString(ARTICLE_NUMBER));
				map.put(ARTICLE_NAME, rs.getString(ARTICLE_NAME));
				map.put(ARTICLE_TEXT, rs.getString(ARTICLE_TEXT));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/*
	 * get books based on date field
	 * @param fieldName, fromValue, toValue
	 */
	public List<Map<String, String>> getBooksByDateField(String fieldName, Date fromValue, Date toValue) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		String statement = GET_BOOK_BY_PUBLICATION_DATE;
		if (fieldName.equalsIgnoreCase(DATE_OF_CREATION))
			statement = GET_BOOK_BY_DATE_OF_CREATION;

		try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

			preparedStatement.setDate(1, new java.sql.Date(fromValue.getTime()));
			preparedStatement.setDate(2, new java.sql.Date(toValue.getTime()));

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISBN, rs.getString(ISBN));
				map.put(TITLE, rs.getString(TITLE));
				map.put(EDITION, rs.getString(EDITION));
				map.put(TYPE, rs.getString(TYPE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));
				map.put(PUBLICATION_DATE, rs.getString(PUBLICATION_DATE));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/*
	 * get all books 
	 */
	public List<Map<String, String>> getAllBooks() {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BOOKS)) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISBN, rs.getString(ISBN));
				map.put(TITLE, rs.getString(TITLE));
				map.put(EDITION, rs.getString(EDITION));
				map.put(TYPE, rs.getString(TYPE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));
				map.put(PUBLICATION_DATE, rs.getString(PUBLICATION_DATE));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/*
	 * get all books based on editon
	 * @param editionName
	 */
	public List<Map<String, String>> getBooksByEdition(String editionName) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_BY_EDITION)) {

			String fieldValue = "%" + editionName + "%";
			preparedStatement.setString(1, fieldValue);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISBN, rs.getString(ISBN));
				map.put(TITLE, rs.getString(TITLE));
				map.put(EDITION, rs.getString(EDITION));
				map.put(TYPE, rs.getString(TYPE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));
				map.put(PUBLICATION_DATE, rs.getString(PUBLICATION_DATE));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/*
	 * get all books based on field whose datatype is int
	 * @param fieldName, fromValue, toValue
	 */
	public List<Map<String, String>> getBooksByIntegerField(String fieldName, int fromValue, int toValue) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		String statement = GET_BOOK_BY_PID;
		if (fieldName.equalsIgnoreCase(ISBN))
			statement = GET_BOOK_BY_ISBN_RANGE;

		try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

			preparedStatement.setInt(1, fromValue);
			preparedStatement.setInt(2, toValue);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISBN, rs.getString(ISBN));
				map.put(TITLE, rs.getString(TITLE));
				map.put(EDITION, rs.getString(EDITION));
				map.put(TYPE, rs.getString(TYPE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));
				map.put(PUBLICATION_DATE, rs.getString(PUBLICATION_DATE));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));
				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/*
	 * delete a publication
	 */
	public void deletePublication(int pId) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PUBLICATION)) {

			preparedStatement.setInt(1, pId);
			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get deleted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/*
	 * update text of an article 
	 */
	public void updateTextOfArticle(String articleText, int pId, int artNum) {

		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ARTICLE_TEXT)) {

			preparedStatement.setInt(2, pId);
			preparedStatement.setInt(3, artNum);

			preparedStatement.setString(1, articleText);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/*
	 * delete an article 
	 * @param pId, artNum
	 */
	public void deleteArticle(int pId, int artNum) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ARTICLES)) {

			preparedStatement.setInt(1, pId);
			preparedStatement.setInt(2, artNum);
			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get deleted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}	

	/*
	 * Delete a chapter
	 * @param pId, chapNum
	 */
	public void deleteChapter(int pId, int chapNum) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CHAPTER)) {

			preparedStatement.setInt(1, pId);
			preparedStatement.setInt(2, chapNum);
			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get deleted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/*
	 * create an article
	 * @param pId, artNum, articleName, articleText, dateOfCreation
	 */
	public void createArticle(int pId, int artNum, String articleName, String articleText, Date dateOfCreation) {
		// private static final String CREATE_ARTICLES = "INSERT INTO Articles
		// VALUES(?,?,?,?,?)";
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ARTICLES)) {

			preparedStatement.setInt(1, pId);
			preparedStatement.setInt(2, artNum);
			preparedStatement.setString(3, articleName);
			preparedStatement.setString(4, articleText);
			preparedStatement.setDate(5, new java.sql.Date(dateOfCreation.getTime()));

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get inserted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/*
	 * create a chapter
	 * @param pId, chapNum, chapterName, contents
	 */
	public void createChapter(int pId, int chapNum, String chapterName, String contents) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CHAPTER)) {

			preparedStatement.setInt(1, pId);
			preparedStatement.setInt(2, chapNum);
			preparedStatement.setString(3, chapterName);
			preparedStatement.setString(4, contents);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get inserted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/*
	 * update an article 
	 * @param fieldName, fieldValue, pId, artNum
	 */
	public void updateArticle(String fieldName, String fieldValue, int pId, int artNum) {

		Connection connection = client.getConnection();
		String sqlStatement = UPDATE_ARTICLES;
		sqlStatement = sqlStatement.replaceAll(FIELD_NAME_TAG, fieldName);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {

			preparedStatement.setInt(2, pId);
			preparedStatement.setInt(3, artNum);

			if (fieldName.equalsIgnoreCase(DATE_OF_CREATION)) {
				Date temp = new SimpleDateFormat("MM/dd/yyyy").parse(fieldValue);
				java.sql.Date date = new java.sql.Date(temp.getTime());
				preparedStatement.setDate(1, date);
			} else {
				preparedStatement.setString(1, fieldValue);
			}

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");

		} catch (SQLException | ParseException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/*
	 * update a chapter 
	 * @param fieldName, fieldValue, pId, chapNum
	 */
	public void updateChapter(String fieldName, String fieldValue, int pId, int chapNum) {
		// private static final String UPDATE_CHAPTER = "UPDATE Chapter SET <FieldName>
		// = ? WHERE PID=? AND ChapNum=?";
		Connection connection = client.getConnection();
		String sqlStatement = UPDATE_CHAPTER;
		sqlStatement = sqlStatement.replaceAll(FIELD_NAME_TAG, fieldName);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {

			preparedStatement.setInt(2, pId);
			preparedStatement.setInt(3, chapNum);

			preparedStatement.setString(1, fieldValue);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/*
	 * create a book entry
	 * @param pid, isbn, edition, publicationDate, dateOfCreation
	 */
	public void createBook(int pid, int isbn, String edition, Date publicationDate, Date dateOfCreation,
			String tableOfContents) {

		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_BOOKS)) {

			preparedStatement.setInt(1, pid);
			preparedStatement.setInt(2, isbn);
			preparedStatement.setString(3, edition);
			preparedStatement.setDate(4, new java.sql.Date(publicationDate.getTime()));
			preparedStatement.setDate(5, new java.sql.Date(dateOfCreation.getTime()));
			preparedStatement.setString(6, tableOfContents);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get inserted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}

	}

	/*
	 * create an issue 
	 * @param pid, issueNumber, type, dateOfIssue,periodicity,tableOfContents
	 */
	public void createIssue(int pid, int issueNumber, String type, Date dateOfIssue, String periodicity,
			String tableOfContents) {

		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ISSUES)) {

			preparedStatement.setInt(1, pid);
			preparedStatement.setInt(2, issueNumber);
			preparedStatement.setString(3, type);
			preparedStatement.setDate(4, new java.sql.Date(dateOfIssue.getTime()));
			preparedStatement.setString(5, periodicity);
			preparedStatement.setString(6, tableOfContents);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get inserted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}

	}

	/**
	 * update book
	 * @param fieldName
	 * @param fieldValue
	 * @param pId
	 */
	public void updateBook(String fieldName, String fieldValue, int pId) {

		Connection connection = client.getConnection();
		String sqlStatement = UPDATE_BOOKS;
		sqlStatement = sqlStatement.replaceAll(FIELD_NAME_TAG, fieldName);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {

			preparedStatement.setInt(2, pId);

			if (fieldName.equalsIgnoreCase(ISBN)) {
				int value = Integer.parseInt(fieldValue);
				preparedStatement.setInt(1, value);
			} else if (fieldName.equalsIgnoreCase(PUBLICATION_DATE) || fieldName.equalsIgnoreCase(DATE_OF_CREATION)) {
				Date temp = new SimpleDateFormat("MM/dd/yyyy").parse(fieldValue);
				java.sql.Date date = new java.sql.Date(temp.getTime());
				preparedStatement.setDate(1, date);
			} else {
				preparedStatement.setString(1, fieldValue);
			}

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");

		} catch (SQLException | ParseException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/**
	 * delete a book
	 * @param pId
	 */
	public void deleteBook(int pId) {

		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOKS)) {

			preparedStatement.setInt(1, pId);
			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get deleted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}

	}

	/**
	 * update an issue
	 * @param fieldName
	 * @param fieldValue
	 * @param pId
	 */
	public void updateIssue(String fieldName, String fieldValue, int pId) {
		Connection connection = client.getConnection();
		String sqlStatement = UPDATE_ISSUES;
		sqlStatement = sqlStatement.replaceAll(FIELD_NAME_TAG, fieldName);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {

			preparedStatement.setInt(2, pId);

			if (fieldName.equalsIgnoreCase(ISSUE_NUMBER)) {
				int value = Integer.parseInt(fieldValue);
				preparedStatement.setInt(1, value);
			} else if (fieldName.equalsIgnoreCase(DATE_OF_ISSUE)) {
				Date temp = new SimpleDateFormat("MM/dd/yyyy").parse(fieldValue);
				java.sql.Date date = new java.sql.Date(temp.getTime());
				preparedStatement.setDate(1, date);
			} else {
				preparedStatement.setString(1, fieldValue);
			}

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get updated");

		} catch (SQLException | ParseException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/**
	 * dalete an issue
	 * @param pId
	 */
	public void deleteIssue(int pId) {
		// private static final String DELETE_ISSUES = "DELETE FROM Issues WHERE PID=?";
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ISSUES)) {

			preparedStatement.setInt(1, pId);
			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get deleted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}

	}

	/**
	 * create a publication
	 * @param pId
	 * @param title
	 * @param type
	 * @param topics
	 * @param costOfEachPiece
	 */
	public void createPublication(int pId, String title, String type, String topics, float costOfEachPiece) {

		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_PUBLICATION)) {

			preparedStatement.setInt(1, pId);
			preparedStatement.setString(2, title);
			preparedStatement.setString(3, type);
			preparedStatement.setString(4, topics);
			preparedStatement.setFloat(5, costOfEachPiece);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get inserted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
	}

	/**
	 * update a publciation
	 * @param fieldName
	 * @param fieldValue
	 * @param pId
	 */
	public void updatePublication(String fieldName, String fieldValue, int pId) {

		Connection connection = client.getConnection();
		String sqlStatement = UPDATE_PUBLICATION;
		sqlStatement = sqlStatement.replaceAll(FIELD_NAME_TAG, fieldName);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {

			preparedStatement.setInt(2, pId);

			if (fieldName.equalsIgnoreCase(COST_OF_EACH_PIECE)) {
				float value = Float.parseFloat(fieldValue);
				preparedStatement.setFloat(1, value);
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
	 * get a publication based on editor
	 * @param editorID
	 * @return
	 */
	public List<Map<String, String>> getPublicationByEditor(int editorID) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PUBLICATION_BY_EID)) {

			preparedStatement.setInt(1, editorID);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(TITLE, rs.getString(TITLE));
				map.put(TOPICS, rs.getString(TOPICS));
				map.put(TYPE, rs.getString(TYPE));
				map.put(COST_OF_EACH_PIECE, rs.getString(COST_OF_EACH_PIECE));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/**
	 * get book based on ISBN
	 * @param isbn
	 * @return
	 */
	public List<Map<String, String>> getBookByISBN(int isbn) {
		Connection connection = client.getConnection();

		List<Map<String, String>> result = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_BY_ISBN)) {

			preparedStatement.setInt(1, isbn);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Map<String, String> map = new HashMap<>();

				map.put(PID, rs.getString(PID));
				map.put(ISBN, rs.getString(ISBN));
				map.put(EDITION, rs.getString(EDITION));
				map.put(PUBLICATION_DATE, rs.getString(PUBLICATION_DATE));
				map.put(DATE_OF_CREATION, rs.getString(DATE_OF_CREATION));
				map.put(TABLE_OF_CONTENTS, rs.getString(TABLE_OF_CONTENTS));

				result.add(map);
			}
		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}
		return result;
	}

	/**
	 * assign an editor a publication
	 * @param editorId
	 * @param pId
	 */
	public void assignEditorToPub(int editorId, int pId) {
		Connection connection = client.getConnection();

		try (PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_EDITOR)) {

			preparedStatement.setInt(1, editorId);
			preparedStatement.setInt(2, pId);

			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row get inserted");

		} catch (SQLException e) {
			System.out.println("Operation Failed. Please enter valid details " + e.getMessage());
		}

	}
}
