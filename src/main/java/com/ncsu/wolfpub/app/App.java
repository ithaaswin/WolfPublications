package com.ncsu.wolfpub.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.ncsu.wolfpub.db.connection.DBConnectionClient;
import com.ncsu.wolfpub.processor.AnalyticProcessor;
import com.ncsu.wolfpub.processor.DistributorProcessor;
import com.ncsu.wolfpub.processor.EditorProcessor;
import com.ncsu.wolfpub.processor.PublicationProcessor;

/**
 * This is the main class for this Application. User inputs are read here and
 * re-directed to respective service calls according to his inputs.
 * 
 * @author vamsi
 *
 */
public class App {
	
	public static void main(String[] args) {
		try {
			runApplication();
		} finally {
			terminate();
		}
	}
	
	private static void printMainMenu() {
		System.out.println("\n");
		System.out.println("-------------Tasks and Operations-------------");
		System.out.println("1. Editing and publishing");
		System.out.println("2. Production of a book edition or an issue of a publication");
		System.out.println("3. Distribution");
		System.out.println("4. Reports");
		System.out.println("0. Exit");
	}
	
	private static void runApplication() {
		// Using Scanner for Getting Input from User
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        		
		System.out.println("Welcome to Wolf Publications");
		System.out.println("-----------------------------");
		printMainMenu();
		int mainMenuOption = getUserInput(reader);
		
		while(mainMenuOption != 0) {
			
			if(mainMenuOption == 1) {
				EditorProcessor.printMenu();
				int option = getUserInput_Tab(reader);
				EditorProcessor.processTask(option);
			} else if(mainMenuOption == 2) {
				PublicationProcessor.printMenu();
				int option = getUserInput_Tab(reader);
				PublicationProcessor.processTask(option);
			} else if(mainMenuOption == 3) {
				DistributorProcessor.printMenu();
				int option = getUserInput_Tab(reader);
				DistributorProcessor.processTask(option);
			} else if(mainMenuOption == 4) {
				AnalyticProcessor.printMenu();
				int option = getUserInput_Tab(reader);
				AnalyticProcessor.processTask(option);
			} else {
				System.out.println("Please select a Valid Input (1-4)");	
			}
			printMainMenu();
			mainMenuOption = getUserInput(reader);
		}
		System.out.println("Exiting from Wolf Publications...!!");
	}
	
	private static int getUserInput_Tab(BufferedReader reader) {
		System.out.print("\tSelect your option here : ");
		try {
			String input = reader.readLine();
			int result = Integer.parseInt(input.trim());
			return result;
		} catch(Exception e) {
			System.out.println("\tPlease enter a valid integer");
			return getUserInput_Tab(reader);
		}
	}

	private static int getUserInput(BufferedReader reader) {
		System.out.print("Select your option here : ");
		try {
			String input = reader.readLine();
			int result = Integer.parseInt(input.trim());
			return result;
		} catch(Exception e) {
			System.out.println("Please enter a valid integer");
			return getUserInput_Tab(reader);
		}
	}

	private static void terminate() {
		DBConnectionClient.close();
	}
}
