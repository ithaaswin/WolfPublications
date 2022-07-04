package com.ncsu.wolfpub.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ncsu.wolfpub.constants.FieldConstants;
import com.ncsu.wolfpub.dao.PublicationDAO;
import com.ncsu.wolfpub.dao.WolfDAOFactory;
import com.ncsu.wolfpub.util.InputUtil;
import com.ncsu.wolfpub.util.OutputPrintUtil;

/**
 * 
 * @author vamsi
 *
 */
public class EditorProcessor extends FieldConstants {

	public static void printMenu() {
		System.out.println("\t-------------Editing and publishing-------------");
		System.out.println("\t1. Enter basic information on a new publication");
		System.out.println("\t2. Update publication information");
		System.out.println("\t3. Assign editor(s) to publication");
		System.out.println("\t4. View Editor's Publications");
		System.out.println("\t5. Edit Table of contents for issues");
		System.out.println("\t6. Edit Table of contents for books");
		System.out.println("\t0. Back to main menu");
	}
	
	/**
	 * Process the Sub Menu 1
	 * @param option
	 */
	public static void processTask(int option) {
		WolfDAOFactory factory = new WolfDAOFactory();
		PublicationDAO dao = factory.getPublicationDAO();
		
		if(option == 1) {
			int pid = InputUtil.getInteger("PID");
			String title = InputUtil.getString("Title");
			String type = InputUtil.getString("Type");
			String topics = InputUtil.getString("Topics");
			float costOfEachPiece = InputUtil.getFloat("Cost of each piece");
			
			String bookOrEdition = InputUtil.getString("Book or Issue (B/I)");
			
			if("B".equalsIgnoreCase(bookOrEdition) || "Book".equalsIgnoreCase(bookOrEdition)) {
				int isbn = InputUtil.getInteger("ISBN");
				String edition = InputUtil.getString("Edition");
				Date publicationDate = InputUtil.getDate("Publication Date");
				Date dateOfCreation = InputUtil.getDate("Date of Creation");
				String tableOfContents = InputUtil.getString("Table of Contents");
				
				dao.createPublication(pid, title, type, topics, costOfEachPiece);
				dao.createBook(pid, isbn, edition, publicationDate, dateOfCreation, tableOfContents);
				
			} else if("I".equalsIgnoreCase(bookOrEdition) || "Issue".equalsIgnoreCase(bookOrEdition)) {
				int issueNumber = InputUtil.getInteger("Issue Number");
				String issuetype = InputUtil.getString("Type");
				Date dateOfIssue = InputUtil.getDate("Date of Issue");
				String periodicity = InputUtil.getString("Periodicity");
				String tableOfContents = InputUtil.getString("Table of Contents");
				
				dao.createPublication(pid, title, type, topics, costOfEachPiece);
				dao.createIssue(pid, issueNumber, issuetype, dateOfIssue, periodicity, tableOfContents);
			} else {
				System.out.println("\tUser entered a invalid option, Returning to main menu...!!");
			}
			
		} else if(option == 2) {
			String fieldName = InputUtil.getString("FieldName");
			while(PID.equalsIgnoreCase(fieldName)) {
				System.out.println("\tYou cannot update PID..!!");
				fieldName = InputUtil.getString("FieldName");	
			}
			String fieldValue = InputUtil.getString("Value");
			int pid = InputUtil.getInteger("PID");
			dao.updatePublication(fieldName, fieldValue, pid);
		} else if(option == 3) {
			int pid = InputUtil.getInteger("PID");
			int editorId = InputUtil.getInteger("EditorId");
			dao.assignEditorToPub(editorId, pid);
		} else if(option == 4) {
			int editorId = InputUtil.getInteger("EditorId");
			List<Map<String, String>> result = dao.getPublicationByEditor(editorId);
			List<String> headers = new ArrayList<>(Arrays.asList(PID, TITLE, TOPICS, TYPE, COST_OF_EACH_PIECE));
			OutputPrintUtil.printResults(result, headers);
		} else if(option == 5) {
			int pid = InputUtil.getInteger("PID");
			String tableOfContens = InputUtil.getString("Table Of Contents");
			dao.updateTableOfContentsForIssues(tableOfContens, pid);
		} else if(option == 6) {
			int pid = InputUtil.getInteger("PID");
			String tableOfContens = InputUtil.getString("Table Of Contents");
			dao.updateTableOfContentsForBook(tableOfContens, pid);
		} else if(option == 0) {
			//Nothing to do
		} else {
			System.out.println("\tUser entered a invalid option, Returning to main menu...!!");
		}

	}
}
