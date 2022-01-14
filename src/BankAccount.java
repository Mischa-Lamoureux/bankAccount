import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;  
import java.util.Date; 

public class BankAccount {
	//Creates a scanner that can be used to take user input
	static Scanner input = new Scanner(System.in);

	//Rounds to 2 decimal places
	static DecimalFormat df = new DecimalFormat("#.00");
	
	//Initialize class variables
	private String accountType, accountUsername, accountPassword, accountFirstName, accountLastName, accountGender, accountAddress;
	private double accountChequingBalance, accountSavingsBalance, masterBalance = 0, accountInterest;
	private boolean accountInterestCollected;
	//A global variable for interest (change here and it changes everywhere)
	static double globalInterest = 0.05;
	
	//Default constructor (for user creating a new account)
	public BankAccount() {
	}
	
	//Chequing constructor
	public BankAccount(String username, String password, String firstName, String lastName, String gender, String address, double balance) {
		accountType = "Chequing";
		accountUsername = username;
		accountPassword = password;
		accountFirstName = firstName;
		accountLastName = lastName;
		accountGender = gender;
		accountAddress = address;
		accountChequingBalance = balance;
		accountInterestCollected = false;
	}
	
	//Saving constructor
	public BankAccount(String username, String password, String firstName, String lastName, String gender, String address, double balance, double interest) {
		accountType = "Savings";
		accountUsername = username;
		accountPassword = password;
		accountFirstName = firstName;
		accountLastName = lastName;
		accountGender = gender;
		accountAddress = address;
		accountSavingsBalance = balance;
		accountInterestCollected = false;
		accountInterest = interest;
	}
	
	//Gets the current date and time and formats it
	//Code taken from: https://www.javatpoint.com/java-get-current-date
	public static void getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    Date date = new Date();
	    System.out.println(formatter.format(date));
	}
	
	//Welcomes the user
	public static int welcomeScreen() {
	    boolean welcomeLoop;
	    welcomeLoop = true;
		while(welcomeLoop) {
			System.out.print("Welcome to the bank!										"); 
			getDate();
		    System.out.println("");
		    System.out.println("1 - Login to an existing account");
		    System.out.println("2 - Create a new account");
		    String loginProcedure = input.nextLine();
		    switch(loginProcedure) {
			case "1": return 1;
			case "2": return 2;
			default: clearScreen();
				System.out.println("You have entered an invalid input. Please try again.");
				System.out.println("");
				welcomeLoop = true;
				break;
		    }
		}
		return 0;
	}
	
	//Clears the screen by 30 lines
	public static void clearScreen() {
		for(int i = 0; i < 50; i++) {
			System.out.println("");
		}
	}
	
	//Create a random 7-digit number
	public static String generateNumber() {
		int accountNumber = (int)((Math.random() * 9000000) + 1000000);
		String returnValue = String.valueOf(accountNumber);
		return returnValue;
	}
	
	//Waits x ms (1000 = 1 second)
	//Method taken from: https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
	public static void wait(int ms) {
	    try {
	        Thread.sleep(ms);
	    }
	    catch(InterruptedException ex) {
	        Thread.currentThread().interrupt();
	    }
	}
	
	//Allows user to deposit money into their account
	public void deposit() {
		boolean depositLoop, whichAccountLoop, depositAgainLoop;
		String stringDeposit, whichAccount = "0";
		depositLoop = true;
		whichAccountLoop = true;
		depositAgainLoop = true;
		while(depositLoop) { //Allows the user to keep making deposits until they say otherwise
			while(whichAccountLoop) { //Loops the selector until the user enters a valid input
				//Determines what kind of account the user currently has
				if(accountType.equals("Chequing")) {
					masterBalance = accountChequingBalance; //"masterBalance" allows me to use one variable in the code below
					whichAccountLoop = false;
				}
				else if(accountType.equals("Savings")) {
					masterBalance = accountSavingsBalance;
					whichAccountLoop = false;
				}
				else if(accountType.equals("Both")) { //Allows the user to pick what account they deposit to
					System.out.println("Which account would you like to deposit to?");
					System.out.println("");
					System.out.println("1 - Deposit to chequing account");
					System.out.println("2 - Deposit to savings account");
					whichAccount = input.nextLine();
					switch(whichAccount) {
					case "1": masterBalance = accountChequingBalance;
						whichAccountLoop = false;
						clearScreen();
					break;
					case "2": masterBalance = accountSavingsBalance;
						whichAccountLoop = false;
						clearScreen();
					break;
					default: clearScreen();
					System.out.println("You have entered an invalid input. Please try again.");
					System.out.println("");
					whichAccountLoop = true;
					break;
					}
				}
			}
			depositAgainLoop = true; //Ensures that the "depositAgainLoop" will be activated
			System.out.println("Deposit -");
			System.out.println("");
			if(accountType.equals("Chequing") || whichAccount.equals("1")) {
				System.out.println("Current chequing balance: $" + df.format(accountChequingBalance));
			}
			else if(accountType.equals("Savings") || whichAccount.equals("2")) {
				System.out.println("Current savings balance: $" + df.format(accountSavingsBalance));
			}
			System.out.println("Please enter the amount of money you wish to deposit.");
			stringDeposit = input.nextLine();
			//Ensures user does not enter any invalid inputs
			for(int i = 0; i < stringDeposit.length(); i++) {
				//Code taken from: https://www.geeksforgeeks.org/how-to-check-if-string-contains-only-digits-in-java/
				if(stringDeposit.charAt(i) >= '0' && stringDeposit.charAt(i) <= '9' || stringDeposit.charAt(i) == '.') {
					depositLoop = false;
				}
				else {
					clearScreen();
					System.out.println("Invalid input. Please enter a positive number.");
					System.out.println("");
					depositLoop = true;
					break;
				}
			}
			if(!depositLoop) {
				double deposit = Double.parseDouble(stringDeposit);
				if(deposit == 0) { //Checks if user is trying to deposit $0
					clearScreen();
					System.out.println("You cannot deposit $0.");
					System.out.println("");
					depositLoop = true;
				}
				//Ensures that the user cannot input extremely large amounts of money (too high of a number gives a compiler error)
				else if((masterBalance + deposit) > 1000000000) {
					clearScreen();
					System.out.println("Your account cannot hold more than 1 billion dollars ($1,000,000,000).");
					System.out.println("");
					depositLoop = true;
				}
				else {
					masterBalance = masterBalance + deposit;
					if(accountType.equals("Chequing") || whichAccount.equals("1")) { //Adds money to their chequing account
						accountChequingBalance = accountChequingBalance + deposit;
					}
					else if(accountType.equals("Savings") || whichAccount.equals("2")) { //Adds money to their savings account
						accountSavingsBalance = accountSavingsBalance + deposit;
					}
					clearScreen();
					System.out.println("You have deposited $" + df.format(deposit) + " to your account.");
					if(accountType.equals("Chequing") || whichAccount.equals("1")) {
						System.out.println("Your new chequing balance is: $" + df.format(accountChequingBalance) + ".");
					}
					else if(accountType.equals("Savings") || whichAccount.equals("2")) {
						System.out.println("Your new savings balance is: $" + df.format(accountSavingsBalance) + ".");
					}
					System.out.println("");
					while(depositAgainLoop) { //Allows the user to deposit more money to their account
						System.out.println("Would you like to deposit more money to your account? (Y/N)");
						String depositAgain = input.nextLine();
						if(depositAgain.equals("Y") || depositAgain.equals("y")) {
							clearScreen();
							depositAgainLoop = false;
							depositLoop = true;
						}
						else if(depositAgain.equals("N") || depositAgain.equals("n")){
							depositAgainLoop = false;
							depositLoop = false;
						}
						else {
							clearScreen();
							System.out.println("Invalid input. Please enter Y/N.");
							System.out.println("");
							depositAgainLoop = true;
						}
					}
				}
			}
		}
	}
	
	//Allows user to withdraw money from their account
	public void withdraw() {
		boolean withdrawLoop, whichAccountLoop, withdrawAgainLoop;
		String whichAccount = "0";
		withdrawLoop = true;
		whichAccountLoop = true;
		withdrawAgainLoop = true;
		while(withdrawLoop) {
			while(whichAccountLoop) {
				if(accountType.equals("Chequing")) {
					masterBalance = accountChequingBalance; //"masterBalance" allows me to use one variable in the code below
					whichAccountLoop = false;
				}
				else if(accountType.equals("Savings")) {
					masterBalance = accountSavingsBalance;
					whichAccountLoop = false;
				}
				else if(accountType.equals("Both")) { //Allows the user to pick which account they withdraw from
					System.out.println("Which account would you like to withdraw from?");
					System.out.println("");
					System.out.println("1 - Withdraw from chequing account");
					System.out.println("2 - Withdraw from savings account");
					whichAccount = input.nextLine();
					switch(whichAccount) {
					case "1": masterBalance = accountChequingBalance;
						clearScreen();
						whichAccountLoop = false;
					break;
					case "2": masterBalance = accountSavingsBalance;
						clearScreen();
						whichAccountLoop = false;
					break;
					default: clearScreen();
					System.out.println("You have entered an invalid input. Please try again.");
					System.out.println("");
					whichAccountLoop = true;
					break;
					}
				}
			}
			//Stops the user from withdrawing money if they have none in their account
			if(masterBalance < 0.01) { //IMPORTANT: Fixes a compiler glitch that can leave the user with a fraction of a cent ($0.001)
				clearScreen();
				masterBalance = 0.00; //Sets the user's balance to $0 if they have a fraction of a cent
				System.out.println("You have no money to withdraw.");
				System.out.println("");
				if(accountType.equals("Chequing") || whichAccount.equals("1")) {
					accountChequingBalance = 0.00;
					System.out.println("Current chequing balance: $" + df.format(accountChequingBalance));
				}
				else if(accountType.equals("Savings") || whichAccount.equals("2")) {
					accountSavingsBalance = 0.00;
					System.out.println("Current savings balance: $" + df.format(accountSavingsBalance));
				}
				System.out.println("");
				System.out.println("Please press any key to return to the menu.");
				input.nextLine();
				break;
			}
			withdrawAgainLoop = true;
			System.out.println("Withdraw -");
			System.out.println("");
			if(accountType.equals("Chequing") || whichAccount.equals("1")) {
				System.out.println("Current chequing balance: $" + df.format(accountChequingBalance));
			}
			else if(accountType.equals("Savings") || whichAccount.equals("2")) {
				System.out.println("Current savings balance: $" + df.format(accountSavingsBalance));
			}
			System.out.println("Please enter the amount of money you wish to withdraw.");
			String stringWithdraw = input.nextLine();
			//Ensures user does not enter any invalid inputs
			for(int i = 0; i < stringWithdraw.length(); i++) {
				//Code taken from: https://www.geeksforgeeks.org/how-to-check-if-string-contains-only-digits-in-java/
				if (stringWithdraw.charAt(i) >= '0' && stringWithdraw.charAt(i) <= '9' || stringWithdraw.charAt(i) == '.') {
					withdrawLoop = false;
				}
				else { //Activate if user enters anything other than a # or "."
					clearScreen();
					System.out.println("Invalid input. Please enter a positive number.");
					System.out.println("");
					withdrawLoop = true;
					break;
				}
			}
			if(!withdrawLoop) {
				double withdraw = Double.parseDouble(stringWithdraw);
				if(withdraw > masterBalance) { //Restricts the user from withdrawing negative amounts of money
					clearScreen();
					System.out.println("You do not have enough money to withdraw $" + df.format(withdraw) + " from your account.");
					System.out.println("");
					withdrawLoop = true;
				}
				else if(withdraw == 0) { //Restricts the user from withdrawing $0
					clearScreen();
					System.out.println("You cannot withdraw $0.");
					System.out.println("");
					withdrawLoop = true;
				}
				else if(withdraw <= masterBalance) {
					masterBalance = masterBalance - withdraw;
					if(accountType.equals("Chequing") || whichAccount.equals("1")) {
						accountChequingBalance = accountChequingBalance - withdraw;
					}
					else if(accountType.equals("Savings") || whichAccount.equals("2")) {
						accountSavingsBalance = accountSavingsBalance - withdraw;
					}
					clearScreen();
					System.out.println("You have withdrawn $" + df.format(withdraw) + " from your account.");
					if(accountType.equals("Chequing") || whichAccount.equals("1")) {
						System.out.println("Your new chequing balance is: $" + df.format(accountChequingBalance) + ".");
					}
					else if(accountType.equals("Savings") || whichAccount.equals("2")) {
						System.out.println("Your new savings balance is: $" + df.format(accountSavingsBalance) + ".");
					}
					System.out.println("");
					//Allows the user to go through withdraw process again
					while(withdrawAgainLoop) {
						System.out.println("Would you like to withdraw more money from your account? (Y/N)");
						String withdrawAgain = input.nextLine();
						if(withdrawAgain.equals("Y") || withdrawAgain.equals("y")) {
							clearScreen();
							withdrawAgainLoop = false;
							withdrawLoop = true;
						}
						else if(withdrawAgain.equals("N") || withdrawAgain.equals("n")) {
							withdrawAgainLoop = false;
							withdrawLoop = false;
						}
						else {
							clearScreen();
							System.out.println("Invalid input. Please enter Y/N.");
							System.out.println("");
							withdrawAgainLoop = true;
						}
					}
				}
			}
		}
	}
	
	//Prints each of the user's account balance
	public void checkBalance() {
		if(accountType.equals("Chequing")) {
			System.out.println("Your current chequing balance is $" + df.format(accountChequingBalance));
		}
		else if(accountType.equals("Savings")) {
			System.out.println("Your current savings balance is $" + df.format(accountSavingsBalance));
		}
		else if(accountType.equals("Both")) {
			System.out.println("Your current chequing balance is $" + df.format(accountChequingBalance));
			System.out.println("Your current savings balance is $" + df.format(accountSavingsBalance));
		}
		System.out.println("");
		System.out.println("Press enter to return.");
		input.nextLine();
	}
	
	//Adds an account depending on what the user already has
	public void addAccount() {
		if(accountType.equals("Chequing")) {
			accountInterest = globalInterest;
			System.out.println("Thanks for creating a savings account with us!");
			System.out.println("");
			System.out.println("This account has a " + globalInterest*100 + "% interest that you are able to collect daily.");
			System.out.println("");
			System.out.println("Press enter to return.");
			input.nextLine();
		}
		else if(accountType.equals("Savings")) {
			System.out.println("Thanks for creating a chequing account with us!");
			System.out.println("");
			System.out.println("Press enter to return.");
			input.nextLine();
		}
	}
	
	//Allows the user to transfer money between their chequing and savings account and vice versa
	public void transfer() {
		boolean transferLoop, whichAccountLoop, transferAgainLoop;
		String whichAccount = "", stringTransferInput;
		double transferFrom = 0, transferTo = 0;
		transferLoop = true;
		whichAccountLoop = true;
		transferAgainLoop = true;
		clearScreen();
		while(transferLoop) {
			transferAgainLoop = true;
			while(whichAccountLoop) {
				System.out.println("Transfer -");
				System.out.println("");
				System.out.println("Current chequing balance: $" + df.format(accountChequingBalance));
				System.out.println("Current savings balance: $" + df.format(accountSavingsBalance));
				System.out.println("");
				System.out.println("What account would you like to transfer to?");
				System.out.println("");
				System.out.println("1 - Transfer from chequing to savings (Chequing -> Savings)");
				System.out.println("2 - Transfer from savings to chequing (Savings -> Chequing)");
				whichAccount = input.nextLine();
				switch(whichAccount) {
				case "1":
					if(accountChequingBalance > 0) {
						transferFrom = accountChequingBalance; //Using "transferFrom" and "transferTo" allows me to lessen the code below
						transferTo = accountSavingsBalance;
						whichAccountLoop = false;
						clearScreen();
					}
					else { //Ensures that the user does not get stuck in an infinite loop of they have $0 in their account
						clearScreen();
						System.out.println("You do not have enough money in that account to transfer!");
						System.out.println("");
						whichAccountLoop = true;
					}
				break;
				case "2":
					if(accountSavingsBalance > 0) {
						transferFrom = accountSavingsBalance;
						transferTo = accountChequingBalance;
						whichAccountLoop = false;
						clearScreen();
					}
					else {
						clearScreen();
						System.out.println("You do not have enough money in that account to transfer!");
						System.out.println("");
						whichAccountLoop = true;
					}
				break;
				default: clearScreen();
					System.out.println("You have entered an invalid input. Please try again.");
					System.out.println("");
					whichAccountLoop = true;
					break;
				}
			}
			if(whichAccount.equals("1")) {
				System.out.println("Please enter the amount of money you wish to transfer from your chequing account to your savings account.");
			}
			else if(whichAccount.equals("2")) {
				System.out.println("Please enter the amount of money you wish to transfer from your savings account to your chequing account.");
			}
			stringTransferInput = input.nextLine();
			for(int i = 0; i < stringTransferInput.length(); i++) {
				//Ensures that the user only enters numbers and decimals
				//Code taken from: https://www.geeksforgeeks.org/how-to-check-if-string-contains-only-digits-in-java/
				if (stringTransferInput.charAt(i) >= '0' && stringTransferInput.charAt(i) <= '9' || stringTransferInput.charAt(i) == '.') {
					transferLoop = false;
				}
				else {
					clearScreen();
					System.out.println("Invalid input. Please enter a positive number.");
					System.out.println("");
					transferLoop = true;
					break;
				}
			}
			if(!transferLoop) {
				double transferInput = Double.parseDouble(stringTransferInput);
				if(transferInput > transferFrom) {
					clearScreen();
					System.out.println("You do not have enough money to transfer $" + df.format(transferInput) + " from your account.");
					System.out.println("");
					transferLoop = true;
				}
				else if(transferInput == 0) {
					clearScreen();
					System.out.println("You cannot transfer $0.");
					System.out.println("");
					transferLoop = true;
				}
				else if(transferInput <= transferFrom) {
					transferFrom -= transferInput;
					transferTo += transferInput;
					if(whichAccount.equals("1")) { //Sets account balances to their proper balances
						accountChequingBalance = transferFrom;
						accountSavingsBalance = transferTo;
					}
					else if(whichAccount.equals("2")) {
						accountChequingBalance = transferTo;
						accountSavingsBalance = transferFrom;
					}
					clearScreen();
					if(whichAccount.equals("1")) {
						System.out.println("You have transfered $" + df.format(transferInput) + " from your chequing account to your savings account.");
					}
					else if(whichAccount.equals("2")) {
						System.out.println("You have transfered $" + df.format(transferInput) + " from your savings account to your chequing account.");
					}
					System.out.println("");
					System.out.println("Your new chequing balance is: $" + df.format(accountChequingBalance));
					System.out.println("Your new savings balance is: $" + df.format(accountSavingsBalance));
					System.out.println("");
					while(transferAgainLoop) { //Allows the user to go through the transfer process again
						System.out.println("Would you like to transfer more money between your accounts? (Y/N)");
						String transferAgain = input.nextLine();
						if(transferAgain.equals("Y") || transferAgain.equals("y")) {
							clearScreen();
							transferAgainLoop = false;
							transferLoop = true;
						}
						else if(transferAgain.equals("N") || transferAgain.equals("n")) {
							transferAgainLoop = false;
							transferLoop = false;
						}
						else {
							clearScreen();
							System.out.println("Invalid input. Please enter Y/N.");
							System.out.println("");
							transferAgainLoop = true;
						}
					}
				}
			}
		}
	}
	
	//Allows the user to collect their savings interest once per session
	public boolean collectInterest() {
		boolean interestDecisionLoop;
		interestDecisionLoop = true;
		while(interestDecisionLoop) {
			System.out.println("Interest -");
			System.out.println("");
			System.out.println("Your current savings balance is : $" + df.format(accountSavingsBalance) + ".");
			System.out.println("Your interest rate is " + accountInterest*100 + "%.");
			System.out.println("If you collect your interest now, you will recieve $" + df.format(accountSavingsBalance * accountInterest) + ".");
			System.out.println("");
			System.out.println("Would you like to collect your interest?");
			System.out.println("");
			System.out.println("1 - Yes! I will collect my $" + df.format(accountSavingsBalance * accountInterest) + " in interest.");
			System.out.println("2 - No. I will collect my interest at a later time.");
			String interestDecision = input.nextLine();
			switch(interestDecision) { //Allows user to pick if they collect their interest or not
			case "1": clearScreen();
				System.out.println("You have collected $" + df.format(accountSavingsBalance * accountInterest) + " in interest!");
				accountSavingsBalance *= (1 + accountInterest) ;
				System.out.println("Your new savings account balance is: $" + df.format(accountSavingsBalance) + ".");
				System.out.println("");
				System.out.println("Press enter to return.");
				input.nextLine();
				return true;
			case "2": clearScreen();
				System.out.println("Your interest has not been collected.");
				System.out.println("");
				System.out.println("You will now return to the main menu.");
				wait(3000);
				return false;
			default: clearScreen();
				System.out.println("You have entered an invalid input. Please try again.");
				System.out.println("");
				interestDecisionLoop = true;
			}
		}
		return false;
	}
	
	//Logs user out of their account and prints their optional receipt 
	public void logout() {
		boolean receiptLoop = true;
		System.out.println("You have been successfully logged out.");
		System.out.println("");
		System.out.print("Thanks for banking with us,");
		if(accountGender.equals("Male")) { //Switches the user's prefix depending on their gender
			System.out.println(" Mr. " + accountLastName + ".");
		}
		else if(accountGender.equals("Female")) {
			System.out.println(" Ms. " + accountLastName + ".");
		}
		else {
			System.out.println(" " + accountFirstName + ".");
		}
		while(receiptLoop) { //Allows the user to pick whether or not they would like a receipt
			System.out.println("");
			System.out.println("Would you like a receipt? (Y/N)");
			String receipt = input.nextLine();
			if(receipt.equals("Y") || receipt.equals("y")) {
				for(int i = 10; i >= 0; i--) { //Keeps the receipt on screen for 10 seconds before exiting to the welcome screen
					clearScreen();
					getDate();
					System.out.println("");
					System.out.println(accountFirstName + " " + accountLastName);
					System.out.println(accountAddress);
					if(accountType.equals("Chequing")) {
						System.out.println("Chequing balance: $" + df.format(accountChequingBalance));
					}
					else if(accountType.equals("Savings")) {
						System.out.println("Savings balance: $" + df.format(accountSavingsBalance));
					}
					else if(accountType.equals("Both")) {
						System.out.println("Chequing balance: $" + df.format(accountChequingBalance));
						System.out.println("Savings balance: $" + df.format(accountSavingsBalance));
					}
					System.out.println("");
					System.out.println("Exiting in: " + i + "...");
					wait(1000);
				}
				receiptLoop = false;
			}
			else if(receipt.equals("N") || receipt.equals("n")) {
				for (int i = 3; i >= 0; i--) { //Exits to the welcome screen after 3 seconds
					clearScreen();
					System.out.println("Exiting in: " + i + "...");
					wait(1000);
				}
				receiptLoop = false;
			}
			else {
				clearScreen();
				System.out.println("Invalid input. Please enter Y/N.");
				System.out.println("");
				receiptLoop = true;
			}
		}
	}
	
	//Performs all major actions (account creation, calling methods, menu screen)
	public static void main(String[] args) {
		boolean logout = true, usernameLogin, passwordLogin, menuScreen, editFirstName, editLastName, editPassword, editGender, editAddress, editAccountType, editInfoLoop, editSelectorLoop, reviewInfoLoop;
		int account = 0;
		String genderSelector, accountSelector;
		
		ArrayList<BankAccount> userAccounts = new ArrayList<BankAccount>(); //Creates a new ArrayList
		
		//Creates 3 preset user accounts
		userAccounts.add(new BankAccount("9274501", "3x1LhbNLKAShpYPA", "John", "Stuart", "Male", "4153 Camden Street, Reno, NV", 6278.48));
		userAccounts.add(new BankAccount("0595485", "YXWNshhv6CUqJHWR", "Charles", "Hernandez", "Other", "2241 Burning Memory Lane, Tupelo, MS", 4392.35, 0.023));
		userAccounts.add(new BankAccount("4430476", "fD193kfs8czLEgEM", "Alice", "Evans", "Female", "4226 Sunset Dr, Brick, NJ", 24873.72));
		
		while(logout) {
			int passwordIncorrect = 0;
			//Sets all local variables = true to ensure everything activates when the user logs in for a second time
			usernameLogin = true;
			passwordLogin = true;
			menuScreen = true;
			editSelectorLoop = true;
			editInfoLoop = true;
			editFirstName = true;
			editLastName = true;
			editPassword = true;
			editGender = true;
			editAddress = true;
			editAccountType = true;
			reviewInfoLoop = true;
			//Call welcome screen
			int loginProcedure = welcomeScreen();
			clearScreen();
			//Allows the user to create a new account
			if(loginProcedure == 2) {
				userAccounts.add(new BankAccount());
				account = userAccounts.size() - 1;
				userAccounts.get(account).accountUsername = generateNumber(); //Generates a random 7-digit username
				userAccounts.get(account).accountInterestCollected = false;
				while(editInfoLoop) {
					while(editFirstName) {
						System.out.println("Please enter your first name.");
						userAccounts.get(account).accountFirstName = input.nextLine();
						break;
					}
					while(editLastName) {
						System.out.println("Please enter your last name.");
						userAccounts.get(account).accountLastName = input.nextLine();
						break;
					}
					while(editPassword) {
						System.out.println("Please enter a secure password.");
						userAccounts.get(account).accountPassword = input.nextLine();
						break;
					}
					while(editGender) {
						System.out.println("Are you male, female, or other?");
						System.out.println("1 - Male");
						System.out.println("2 - Female");
						System.out.println("3 - Other");
						genderSelector = input.nextLine();
						switch(genderSelector) {
						case "1": userAccounts.get(account).accountGender = "Male";
							editGender = false;
						break;
						case "2": userAccounts.get(account).accountGender = "Female";
							editGender = false;
						break;
						case "3": userAccounts.get(account).accountGender = "Other";
							editGender = false;
						break;
						default: clearScreen();
							System.out.println("You have entered an invalid input. Please try again.");
							System.out.println("");
							editGender = true;
						break;
						}
					}
					while(editAddress) {
						System.out.println("Please enter your address.");
						userAccounts.get(account).accountAddress = input.nextLine();
						break;
					}
					while(editAccountType) {
						System.out.println("Would you like to create a chequing or savings account?");
						System.out.println("");
						System.out.println("1 - Chequing");
						System.out.println("2 - Savings");
						accountSelector = input.nextLine();
						switch(accountSelector) {
						case "1": userAccounts.get(account).accountType = "Chequing";
							userAccounts.get(account).accountInterest = 0.00;
							editAccountType = false;
						break;
						case "2": userAccounts.get(account).accountType = "Savings";
							userAccounts.get(account).accountInterest = globalInterest;
							editAccountType = false;
						break;
						default: clearScreen();
							System.out.println("You have entered an invalid input. Please try again.");
							System.out.println("");
							editAccountType = true;
						break;
						}
					}
					reviewInfoLoop = true;
					clearScreen();
					if(editSelectorLoop) {
						System.out.println("Thanks for creating an account with us!");
						System.out.println("");
						System.out.println("Please look over your information on the following screen to make sure it's correct.");
						wait(5000);
						clearScreen();
					}
					else {
						System.out.println("Your information has been edited.");
						System.out.println("");
						System.out.println("Please look over your information on the following screen to make sure it's correct.");
						wait(5000);
						clearScreen();
					}
					//Allows user to verify their imputed information
					while(reviewInfoLoop) {
						System.out.println("Account username: " + userAccounts.get(account).accountUsername);
						System.out.println("Account password: " + userAccounts.get(account).accountPassword);
						System.out.println("Full name: " + userAccounts.get(account).accountFirstName + " " + userAccounts.get(account).accountLastName);
						System.out.println("Gender: " + userAccounts.get(account).accountGender);
						System.out.println("Address: " + userAccounts.get(account).accountAddress);
						System.out.println("Account type: " + userAccounts.get(account).accountType);
						System.out.println("");
						System.out.println("Is this information correct?");
						System.out.println("");
						System.out.println("1 - Yes, my information is correct and I'm ready to finalize my account");
						System.out.println("2 - No, my information is incorrect and I need to edit it");
						String finalizeAccount = input.nextLine();
						clearScreen();
						if(finalizeAccount.equals("1")) { //Saves the user's new account if all of the information is correct
							System.out.println("Your information has been saved.");
							System.out.println("");
							System.out.println("You will be prompted to enter your credentials on the next screen.");
							editInfoLoop = false;
							reviewInfoLoop = false;
							wait(5000);
							clearScreen();
						}
						else if(finalizeAccount.equals("2")) { //Allows the user to edit their specific information
							editSelectorLoop = true;
							while(editSelectorLoop) {
								System.out.println("Which piece of information do you need to edit?");
								System.out.println("");
								System.out.println("1 - First name");
								System.out.println("2 - Last name");
								System.out.println("3 - Password");
								System.out.println("4 - Gender");
								System.out.println("5 - Address");
								System.out.println("6 - Account type");
								String editInfo = input.nextLine();
								editFirstName = false;
								editLastName = false;
								editPassword = false;
								editGender = false;
								editAddress = false;
								editAccountType = false;
								switch(editInfo) { //Activates certain loops depending on what the user would like to edit
								case "1": clearScreen();
									editFirstName = true;
									editInfoLoop = true;
									editSelectorLoop = false;
								break;
								case "2": clearScreen();
									editLastName = true;
									editInfoLoop = true;
									editSelectorLoop = false;
								break;
								case "3": clearScreen();
									editPassword = true;
									editInfoLoop = true;
									editSelectorLoop = false;
								break;
								case "4": clearScreen();
									editGender = true;
									editInfoLoop = true;
									editSelectorLoop = false;
								break;
								case "5": clearScreen();
									editAddress = true;
									editInfoLoop = true;
									editSelectorLoop = false;
								break;
								case "6": clearScreen();
									editAccountType = true;
									editInfoLoop = true;
									editSelectorLoop = false;
								break;
								default: clearScreen();
									System.out.println("You have entered an invalid input. Please try again.");
									System.out.println("");
									editSelectorLoop = true;
								break;
								}
								reviewInfoLoop = false;
							}
						}
						else {
							clearScreen();
							System.out.println("You have entered an invalid input. Please try again.");
							System.out.println("");
							reviewInfoLoop = true;
						}
					}
				}
			}
			//Allows the user to login into their account with their username and password
			while(usernameLogin) {
				System.out.println("Please enter your credentials -");
				System.out.println("");
				System.out.println("Username:");
				String inputUsername = input.nextLine();
				for(account = 0; account < userAccounts.size(); account++) {
					if(inputUsername.equals(userAccounts.get(account).accountUsername)) { //Break to password loop if username is correct
						usernameLogin = false;
						break;
					}
					else { //Makes the user retry their username if their username is incorrect
						usernameLogin = true;
					}
				}
				if(usernameLogin) {
					clearScreen();
					System.out.println("This account does not exist.");
					System.out.println("");
				}
			}
			while(passwordLogin) {
				if(passwordIncorrect >= 5) { //Locks the user out of their account if they enter the wrong password 5 times
					logout = true;
					menuScreen = false;
					usernameLogin = false;
					passwordLogin = false;
					for(int i = 60; i >= 0; i--) {
						clearScreen();
						System.out.println("Your account is disabled.");
						System.out.println("");
						System.out.println("Try again in " + i + " seconds.");
						wait(1000);
					}
					clearScreen();
					break;
				}
				System.out.println("Password:											Attempt " + passwordIncorrect + " of 5.");
				String inputPassword = input.nextLine();
				if(inputPassword.equals(userAccounts.get(account).accountPassword)) {
					break;
				}
				else {
					clearScreen();
					System.out.println("Invalid password.");
					System.out.println("");
					passwordIncorrect++;
					passwordLogin = true;
				}
			}
			while(menuScreen) {
				clearScreen();
				//Changes introduction depending on gender
				if(userAccounts.get(account).accountGender.equals("Male")) {
					System.out.println("");
					System.out.println("Welcome, Mr. " + userAccounts.get(account).accountLastName + ",");
				}
				else if(userAccounts.get(account).accountGender.equals("Female")) {
					System.out.println("");
					System.out.println("Welcome, Ms. " + userAccounts.get(account).accountLastName + ",");
				}
				else {
					System.out.println("");
					System.out.println("Welcome, " + userAccounts.get(account).accountFirstName + ",");
				}
				System.out.println("Address: " + userAccounts.get(account).accountAddress);
				if(userAccounts.get(account).accountType.equals("Chequing")) {
					System.out.println("Your chequing account balance is: $" + df.format(userAccounts.get(account).accountChequingBalance));
				}
				else if(userAccounts.get(account).accountType.equals("Savings")) {
					System.out.println("Your savings account balance is: $" + df.format(userAccounts.get(account).accountSavingsBalance));
				}
				else if(userAccounts.get(account).accountType.equals("Both")) {
					System.out.println("Your chequing account balance is: $" + df.format(userAccounts.get(account).accountChequingBalance));
					System.out.println("Your savings account balance is: $" + df.format(userAccounts.get(account).accountSavingsBalance));
				}
				System.out.println("");
				System.out.println("How may I help you today?"); //Lists all of the functions available to the user
				System.out.println("1 - Deposit money");
				System.out.println("2 - Withdraw money");
				System.out.println("3 - Check balance");
				if(userAccounts.get(account).accountType.equals("Chequing")) {
					System.out.println("4 - Create a savings account");
				}
				else if(userAccounts.get(account).accountType.equals("Savings")) {
					System.out.println("4 - Create a chequing account");
					System.out.println("5 - Collect interest");
				}
				else if(userAccounts.get(account).accountType.equals("Both")) {
					System.out.println("4 - Transfer money");
					System.out.println("5 - Collect interest");
				}
				System.out.println("9 - Logout");
				String menuSelector = input.nextLine();
				switch(menuSelector) {
				case "1": clearScreen(); //Deposit
					userAccounts.get(account).deposit();
				break;
				case "2": clearScreen(); //Withdraw
					userAccounts.get(account).withdraw();
				break;
				case "3": clearScreen(); //Check balance
					userAccounts.get(account).checkBalance();
				break;
				case "4": clearScreen(); //Creates new account/transfer money between accounts
					//Allows the fourth case to have multiple functions depending on the account
					if(userAccounts.get(account).accountType.equals("Chequing")) {
						userAccounts.get(account).addAccount();
						userAccounts.get(account).accountType = "Both";
					}
					else if(userAccounts.get(account).accountType.equals("Savings")) {
						userAccounts.get(account).addAccount();
						userAccounts.get(account).accountType = "Both";
					}
					else if(userAccounts.get(account).accountType.equals("Both")) {
						userAccounts.get(account).transfer();
					}
				break;
				case "5": clearScreen(); //Collect interest
					if(userAccounts.get(account).accountType.equals("Chequing")) { //Ensures that the user cannot enter the transfer method if they don't have both accounts
						System.out.println("You have entered an invalid input. Please try again.");
						wait(2000);
						break;
					}
					else {
						if(!userAccounts.get(account).accountInterestCollected) {
							userAccounts.get(account).accountInterestCollected = userAccounts.get(account).collectInterest();
							break;
						}
						else if(userAccounts.get(account).accountInterestCollected) { //Ensures the user can't collect interest more than once per session
							System.out.println("You have already collected interest today. Please try again in 24 hours.");
							System.out.println("");
							System.out.println("Press enter to return.");
							input.nextLine();
							break;
						}
					}
				case "9": clearScreen(); //Logout
					userAccounts.get(account).logout();
					menuScreen = false;
					clearScreen();
				break;
				default: clearScreen();
					System.out.println("You have entered an invalid input. Please try again.");
					//Wait 2 seconds
					wait(2000);
				break;
				}
			}
		}
	}
}