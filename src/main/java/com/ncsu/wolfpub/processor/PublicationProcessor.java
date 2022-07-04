package com.ncsu.wolfpub.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ncsu.wolfpub.constants.FieldConstants;
import com.ncsu.wolfpub.dao.PublicationDAO;
import com.ncsu.wolfpub.dao.TransactionsDAO;
import com.ncsu.wolfpub.dao.WolfDAOFactory;
import com.ncsu.wolfpub.util.InputUtil;
import com.ncsu.wolfpub.util.OutputPrintUtil;

public class PublicationProcessor extends FieldConstants {

	public static void printMenu() {
		System.out.println("\t-------------Production of a book edition or an issue of a publication-------------");
		System.out.println("\t1. Enter a new book edition of a publication");
		System.out.println("\t2. Enter a new issue of a publication");
		System.out.println("\t3. Update a book edition");
		System.out.println("\t4. Delete a book edition");
		System.out.println("\t5. Update a Issue");
		System.out.println("\t6. Delete a Issue");
		System.out.println("\t7. Enter Article");
		System.out.println("\t8. Enter Chapter");
		System.out.println("\t9. Update Article");
		System.out.println("\t10. Update Chapter");
		System.out.println("\t11. Delete Article");
		System.out.println("\t12. Delete Chapter");
		System.out.println("\t13. Update text of an Article");
		System.out.println("\t14. Find books by field");
		System.out.println("\t15. Find articles by field");
		System.out.println("\t16. Find issues by field");
		System.out.println("\t17. Enter Payment to staff member");
		System.out.println("\t18. View Payments claimed by addressee");
		System.out.println("\t0. Back to main menu");
	}

	/**
	 * Process the Sub Menu 2
	 * @param option
	 */
	public static void processTask(int option) {
		WolfDAOFactory factory = new WolfDAOFactory();
		PublicationDAO dao = factory.getPublicationDAO();
		TransactionsDAO tdao = factory.getTransactionsDAO();

		if (option == 1) {
			int pid = InputUtil.getInteger("PID");
			int isbn = InputUtil.getInteger("ISBN");
			String edition = InputUtil.getString("Edition");
			Date publicationDate = InputUtil.getDate("Publication Date");
			Date dateOfCreation = InputUtil.getDate("Date of Creation");
			String tableOfContents = InputUtil.getString("Table of Contents");
			dao.createBook(pid, isbn, edition, publicationDate, dateOfCreation, tableOfContents);
		} else if (option == 2) {
			int pid = InputUtil.getInteger("PID");
			int isbn = InputUtil.getInteger("Issue Number");
			String type = InputUtil.getString("Type");
			Date dateOfIssue = InputUtil.getDate("Date of Issue");
			String periodicity = InputUtil.getString("Periodicity");
			String tableOfContents = InputUtil.getString("Table of Contents");
			dao.createIssue(pid, isbn, type, dateOfIssue, periodicity, tableOfContents);

		} else if (option == 3) {
			String fieldName = InputUtil.getString("FieldName");
			while(PID.equalsIgnoreCase(fieldName)) {
				System.out.println("\tYou cannot update PID..!!");
				fieldName = InputUtil.getString("FieldName");
			}
			String fieldValue = InputUtil.getString("Value");
			int pid = InputUtil.getInteger("PID");
			dao.updateBook(fieldName, fieldValue, pid);
		} else if (option == 4) {
			int pid = InputUtil.getInteger("PID");
			String sure = InputUtil.getString(
					"Are you sure you want to delete. This will delete all the chapters associated with Book (y/n) : ");
			if ("Y".equalsIgnoreCase(sure)) {
				dao.deleteBook(pid);
			}
		} else if (option == 5) {
			String fieldName = InputUtil.getString("FieldName");
			while(PID.equalsIgnoreCase(fieldName)) {
				System.out.println("\tYou cannot update PID..!!");
				fieldName = InputUtil.getString("FieldName");
			}
			String fieldValue = InputUtil.getString("Value");
			int pid = InputUtil.getInteger("PID");
			dao.updateIssue(fieldName, fieldValue, pid);
		} else if (option == 6) {
			int pid = InputUtil.getInteger("PID");
			String sure = InputUtil.getString(
					"Are you sure you want to delete. This will delete all the articles associated with Issue (y/n) : ");
			if ("Y".equalsIgnoreCase(sure)) {
				dao.deleteIssue(pid);
			}
		} else if (option == 7) {
			int pid = InputUtil.getInteger("PID");
			int artNum = InputUtil.getInteger("Article Number");
			String articleName = InputUtil.getString("Article Name");
			String articleText = InputUtil.getString("Article Text");
			Date dateOfCreation = InputUtil.getDate("Date of Creation");
			dao.createArticle(pid, artNum, articleName, articleText, dateOfCreation);
		} else if (option == 8) {
			int pid = InputUtil.getInteger("PID");
			int chapNum = InputUtil.getInteger("Chapter Number");
			String chapterName = InputUtil.getString("Chapter Name");
			String contents = InputUtil.getString("Contents");
			dao.createChapter(pid, chapNum, chapterName, contents);

		} else if (option == 9) {
			String fieldName = InputUtil.getString("FieldName");
			while(PID.equalsIgnoreCase(fieldName) || ARTICLE_NUMBER.equalsIgnoreCase(fieldName)) {
				System.out.println("\tYou cannot update PID and Article Number..!!");
				fieldName = InputUtil.getString("FieldName");
			}
			String fieldValue = InputUtil.getString("Value");
			int pid = InputUtil.getInteger("PID");
			int artNum = InputUtil.getInteger("Article Number");
			dao.updateArticle(fieldName, fieldValue, pid, artNum);
		} else if (option == 10) {
			String fieldName = InputUtil.getString("FieldName");
			while(PID.equalsIgnoreCase(fieldName) || CHAPTER_NUMBER.equalsIgnoreCase(fieldName)) {
				System.out.println("\tYou cannot update PID and Chapter Number..!!");
				fieldName = InputUtil.getString("FieldName");
			}
			String fieldValue = InputUtil.getString("Value");
			int pid = InputUtil.getInteger("PID");
			int chapNum = InputUtil.getInteger("Chapter Number");
			dao.updateChapter(fieldName, fieldValue, pid, chapNum);
		} else if (option == 11) {
			int pid = InputUtil.getInteger("PID");
			int artNum = InputUtil.getInteger("Article Number");
			dao.deleteArticle(pid, artNum);
		} else if (option == 12) {
			int pid = InputUtil.getInteger("PID");
			int chapNum = InputUtil.getInteger("Chapter Number");
			dao.deleteChapter(pid, chapNum);
		} else if (option == 13) {
			int pid = InputUtil.getInteger("PID");
			int artNum = InputUtil.getInteger("Article Number");
			String articleText = InputUtil.getString("Article Text");
			dao.updateTextOfArticle(articleText, pid, artNum);
		} else if (option == 14) {
			int choice = printSelectBookMenu();
			List<Map<String, String>> results = new ArrayList<Map<String, String>>();
			List<String> headers = new ArrayList<>(Arrays.asList(PID, ISBN, TITLE, EDITION, TYPE, TOPICS,
					DATE_OF_CREATION, PUBLICATION_DATE, COST_OF_EACH_PIECE, TABLE_OF_CONTENTS));


			if (choice == 1) {
				results = dao.getAllBooks();
			} else if (choice == 2) {
				int fromValue = InputUtil.getInteger("\tFrom");
				int toValue = InputUtil.getInteger("\tTo");
				results = dao.getBooksByIntegerField(PID, fromValue, toValue);
			} else if (choice == 3) {
				int fromValue = InputUtil.getInteger("\tFrom");
				int toValue = InputUtil.getInteger("\tTo");
				results = dao.getBooksByIntegerField(ISBN, fromValue, toValue);
			} else if (choice == 4) {
				String value = InputUtil.getString("\tValue");
				results = dao.getBooksByEdition(value);
			} else if (choice == 5) {
				Date fromValue = InputUtil.getDate("\tFrom");
				Date toValue = InputUtil.getDate("\tTo");
				results = dao.getBooksByDateField(PUBLICATION_DATE, fromValue, toValue);
			} else if (choice == 6) {
				Date fromValue = InputUtil.getDate("\tFrom");
				Date toValue = InputUtil.getDate("\tTo");
				results = dao.getBooksByDateField(DATE_OF_CREATION, fromValue, toValue);
			}
			OutputPrintUtil.printResults(results, headers);

		} else if (option == 15) {
			int choice = printSelectArticleMenu();
			List<Map<String, String>> results = new ArrayList<Map<String, String>>();
			List<String> headers = new ArrayList<>(
					Arrays.asList(PID, ARTICLE_NUMBER, ARTICLE_NAME, ARTICLE_TEXT, DATE_OF_CREATION));
			if (choice == 1) {
				results = dao.getAllArticles();
			} else if (choice == 2) {
				int fromValue = InputUtil.getInteger("\tFrom");
				int toValue = InputUtil.getInteger("\tTo");
				results = dao.getArticlesByIntField(PID, fromValue, toValue);
			} else if (choice == 3) {
				int fromValue = InputUtil.getInteger("\tFrom");
				int toValue = InputUtil.getInteger("\tTo");
				results = dao.getArticlesByIntField(ARTICLE_NUMBER, fromValue, toValue);
			} else if (choice == 4) {
				String value = InputUtil.getString("\tValue");
				results = dao.getArticlesByArticleName(value);
			} else if (choice == 5) {
				Date fromValue = InputUtil.getDate("\tFrom");
				Date toValue = InputUtil.getDate("\tTo");
				results = dao.getArticlesByDateOfCreation(fromValue, toValue);
			}
			OutputPrintUtil.printResults(results, headers);

		} else if (option == 16) {
			List<Map<String, String>> results = new ArrayList<Map<String, String>>();
			List<String> headers = new ArrayList<>(Arrays.asList(PID, ISSUE_NUMBER, TITLE, TYPE, DATE_OF_ISSUE, TOPICS,
					PERIODICITY, COST_OF_EACH_PIECE, TABLE_OF_CONTENTS));
			int choice = printSelecIssueMenu();
			if (choice == 1) {
				results = dao.getAllIssues();
			} else if (choice == 2) {
				int fromValue = InputUtil.getInteger("\tFrom");
				int toValue = InputUtil.getInteger("\tTo");
				results = dao.getAllIssuesByIntField(PID, fromValue, toValue);
			} else if (choice == 3) {
				int fromValue = InputUtil.getInteger("\tFrom");
				int toValue = InputUtil.getInteger("\tTo");
				results = dao.getAllIssuesByIntField(ISSUE_NUMBER, fromValue, toValue);
			} else if (choice == 4) {
				String value = InputUtil.getString("\tValue");
				results = dao.getAllIssuesByStringField(TYPE, value);
			} else if (choice == 5) {
				Date fromValue = InputUtil.getDate("\tFrom");
				Date toValue = InputUtil.getDate("\tTo");
				results = dao.getAllIssuesByDateOfIssue(fromValue, toValue);
			} else if (choice == 6) {
				String value = InputUtil.getString("\tValue");
				results = dao.getAllIssuesByStringField(PERIODICITY, value);
			}
			OutputPrintUtil.printResults(results, headers);
		} else if (option == 17) {
			int tid = InputUtil.getInteger("TID");
			int id = InputUtil.getInteger("EmployeeId");
			String workType = InputUtil.getString("Work type");
			float amount = InputUtil.getFloat("Amount");
			String debitcredit = "Debit";
			String paymentMode = InputUtil.getString("Payment Mode");
			String transactionType = "Salary";
			Date date = InputUtil.getDate("Transaction Date");

			tdao.createPaymentToStaff(tid, date, amount, debitcredit, paymentMode, transactionType, id, workType);

		} else if (option == 18) {
			int id = InputUtil.getInteger("EmplyeeId");
			Date from = InputUtil.getDate("From");
			Date to = InputUtil.getDate("To");
			List<String> headers = new ArrayList<>(
					Arrays.asList(TID, ID, WORK_TYPE, TRANSACTION_DATE, AMOUNT, PAYMENT_MODE));
			List<Map<String, String>> results = tdao.getPaynmentToStaff(id, from, to);
			OutputPrintUtil.printResults(results, headers);
		} else if(option == 0) {
			//Nothing to do
		} else {
			System.out.println("\tUser entered a invalid option, Returning to main menu...!!");
		}
	}

	private static int printSelecIssueMenu() {

		System.out.println("\t\t1. Select All");
		System.out.println("\t\t2. Based on PID");
		System.out.println("\t\t3. Based on Issue Number");
		System.out.println("\t\t4. Based on Issue Type");
		System.out.println("\t\t5. Based on DateOfIssue");
		System.out.println("\t\t6. Based on Periodicity");

		return InputUtil.getInteger("Select your choice");

	}

	private static int printSelectArticleMenu() {

		System.out.println("\t\t1. Select All");
		System.out.println("\t\t2. Based on PID");
		System.out.println("\t\t3. Based on Article Number");
		System.out.println("\t\t4. Based on Article Name");
		System.out.println("\t\t5. Based on DateOfCreation");

		return InputUtil.getInteger("Select your choice");

	}

	private static int printSelectBookMenu() {

		System.out.println("\t\t1. Select All");
		System.out.println("\t\t2. Based on PID");
		System.out.println("\t\t3. Based on ISBN");
		System.out.println("\t\t4. Based on Edition");
		System.out.println("\t\t5. Based on PublicationDate");
		System.out.println("\t\t6. Based on DateOfCreation");

		return InputUtil.getInteger("Select your choice");

	}
}
