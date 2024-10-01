package userregistration.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

import userregistration.dao.UserCrud;
import userregistration.dto.User;

/*
 * Controller acts like UI - console/web page. 
 * Scanner - used to communicate with end user.
 * Scanner must be in controller only according to project standards.
 * From controller itself execution starts and main method is mandatory.
 * MENU DRIVEN PROJECT
 */


	public class UserController {
		
		public static Scanner sc = new Scanner(System.in);
		public static UserCrud uc = new UserCrud();
		
		public static void main(String[] args) throws SQLException {
			 System.out.println(".....WELCOME .....");
		        // Creating user table
		        uc.createUserTable();

		        boolean isRunning = true;
		        do {
		            System.out.println("\n1. Registration \n2. Login \n3. Exit");
		            int choice = sc.nextInt();
		            sc.nextLine();  // Consume the leftover newline after reading the integer input

		            switch (choice) {
		                case 1:
		                    userRegistration();  // save user data
		                    break;
		                case 2:
		                    login();  // Login and go to post-login operations
		                    break;
		                case 3:
		                    System.out.println("Exiting...");
		                    isRunning = false;
		                    break;
		                default:
		                    System.out.println("Invalid option. Please select again.");
		                    break;
		            }
		        } while (isRunning);

		        System.out.println("....Thank you....");
		    }
			
			
				     
				
					
	//USER INPUTS USING SCANNER FOR CRUD
		
	//SAVE - TAKING INPUT FROM USER TO SAVE A NEW USER INTO DATABASE
		public static void userRegistration() {
			//Loop for exception handling
		    boolean validInput = false;  // To control the loop
		    String email = "", name = "", gender = "", location = "", password = "", dob = "";
		    int age = 0;
		    long contact = 0;
		    Date dateOfBirth = null;

		    while (!validInput) {  // Loop until valid input is received
		        try {
		            System.out.println("Please enter your details....");

		            // Email validation
		            System.out.println("Enter your Email:");
		            email = sc.nextLine();
		            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
		                throw new IllegalArgumentException("Invalid email format. Please enter a valid email.");
		            }

		            // Check if the email already exists in the database
		            if (uc.isEmailExists(email)) {
		                throw new IllegalArgumentException("Email already exists. Please enter a different email.");
		            }

		            System.out.println("Enter your Fullname:");
		            name = sc.nextLine();

		            System.out.println("Enter your Gender:");
		            gender = sc.next();  // Reads until the first space or newline
		            sc.nextLine(); // Consume newline

		            System.out.println("Enter your Location:");
		            location = sc.nextLine();

		            System.out.println("Enter Password:");
		            password = sc.nextLine();

		            // Age validation
		            System.out.println("Enter your Age:");
		            age = sc.nextInt();
		            if (age <= 0) {
		                throw new IllegalArgumentException("Age must be a positive integer.");
		            }
		            sc.nextLine();  // Clear the buffer

		            // Date of Birth validation
		            System.out.println("Enter your DOB (YYYY-MM-DD):");
		            dob = sc.next();
		            try {
		                dateOfBirth = Date.valueOf(dob);  // SQL Date format validation
		            } catch (IllegalArgumentException e) {
		                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
		            }
		            sc.nextLine();  // Clear the buffer

		            // Phone number validation
		            System.out.println("Enter your Phone number:");
		            contact = sc.nextLong();
		            if (String.valueOf(contact).length() != 10) {
		                throw new IllegalArgumentException("Phone number must be 10 digits.");
		            }

		            // Check if the phone number already exists in the database
		            if (uc.isPhoneExists(contact)) {
		                throw new IllegalArgumentException("Phone number already exists. Please enter a different phone number.");
		            }
		            sc.nextLine();  // Clear the buffer

		            // If all inputs are valid, set validInput to true to exit the loop
		            validInput = true;

		        } catch (IllegalArgumentException e) {
		            // Display the friendly message and restart input
		            System.out.println("\nError: " + e.getMessage());
		            System.out.println("Please re-enter your details correctly from the beginning.\n");

		        } catch (Exception e) {
		            // Catch any other unexpected errors
		            System.out.println("An unexpected error occurred: " + e.getMessage());
		            System.out.println("Please try again.");
		            sc.nextLine();  // Clear the buffer for unexpected errors
		        }
		    }

		    // Create User object after valid inputs
		    User user = new User(email, name, gender, location, password, age, dateOfBirth, contact);
		    try {
		        int numberOfRowsCreated = uc.saveUser(user);
		        if (numberOfRowsCreated > 0) {
		            System.out.println("\n" + user.getName() + ", your registration is successful!");
		        }
		    } catch (SQLException e) {
		        // Friendly message for SQL error without stack trace
		        System.out.println("\nAn error occurred while saving your data. Please try again later.");
		    } catch (Exception e) {
		        // Catch any other unexpected errors with a friendly message
		        System.out.println("\nSomething went wrong. Please try again.");
		    }
		}
		    
		  //FETCH - TAKING INPUT FROM USER TO FETCH HIS/HER DETAILS FROM DATABASE
		    
		public static void login() throws SQLException {
	        System.out.println("Enter your email: ");
	        String email = sc.nextLine();

	        System.out.println("Enter your password: ");
	        String password = sc.nextLine();

	        User user = new User();
	        user.setEmail(email);
	        user.setPassword(password);

	        User loggedInUser = UserCrud.fetchUser(user);

	        if (loggedInUser != null) {
	            System.out.println("Login successful!");
	            showOperationsMenu(loggedInUser);  // Go directly to post-login operations
	        } else {
	            System.out.println("Invalid email or password. Try again.");
	        }
	    }

	    // Display menu options after user logs in
	    private static void showOperationsMenu(User user) throws SQLException {
	        boolean continueOperations = true;

	        while (continueOperations) {
	            System.out.println("\nChoose an operation: ");
	            System.out.println("1. Fetch User Details");
	            System.out.println("2. Update User Details");
	            System.out.println("3. Delete User");
	            System.out.println("4. Logout");

	            int choice = sc.nextInt();
	            sc.nextLine();  // Consume newline

	            switch (choice) {
	                case 1:
	                    fetchUserDetails(user);
	                    break;
	                case 2:
	                    updateUser(user);
	                    break;
	                case 3:
	                    deleteUser(user);
	                    continueOperations = false;  // End after delete
	                    break;
	                case 4:
	                    System.out.println("Logging out...");
	                    continueOperations = false;
	                    break;
	                default:
	                    System.out.println("Invalid choice. Try again.");
	                    break;
	            }
	        }
	    }

		    // Fetch user details
		    private static void fetchUserDetails(User user) throws SQLException {
		        System.out.println("\nFetching user details...");
		        User fetchedUser = UserCrud.fetchUser(user);  // Retrieve from DAO
		        
		        if (fetchedUser != null) {
		            System.out.println("User Details:");
		            System.out.println("Email: " + fetchedUser.getEmail());
		            System.out.println("Name: " + fetchedUser.getName());
		            System.out.println("Gender: " + fetchedUser.getGender());
		            System.out.println("Location: " + fetchedUser.getLocation());
		            System.out.println("Phone: " + fetchedUser.getContact());
		            System.out.println("Age:" + fetchedUser.getAge());
		            System.out.println("DOB:" + fetchedUser.getDob());
		            
		        } else {
		            System.out.println("No details found for the provided user.");
		        }
		    }
		    
		//UPDATE - TAKING INPUT FROM USER TO UPDATE HIS/HER DETAILS IN DATABASE

		    // Update user details
		    public static void updateUser(User user) throws SQLException {
		        System.out.println("Enter which field to update: \n 1. Email \n 2. Name \n 3. Password \n 4. Phone Number \n 5. Age \n 6. Gender \n 7. Location \n 8. Date of Birth");
		        int choice = sc.nextInt();
		        sc.nextLine();  // Clear the buffer

		        boolean isValid = false;  // To control loop for validation
		        while (!isValid) {
		            try {
		                switch (choice) {
		                    case 1:  // Email
		                        System.out.println("Enter new Email:");
		                        String email = sc.nextLine();
		                        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
		                            throw new IllegalArgumentException("Invalid email format. Please enter a valid email.");
		                        }
		                        uc.updateUser(user, choice, email);
		                        isValid = true;
		                        break;
		                    case 2:  // Name
		                        System.out.println("Enter new Name:");
		                        String name = sc.nextLine();
		                        uc.updateUser(user, choice, name);
		                        isValid = true;
		                        break;
		                    case 3:  // Password
		                        System.out.println("Enter new Password:");
		                        String password = sc.nextLine();
		                        uc.updateUser(user, choice, password);
		                        isValid = true;
		                        break;
		                    case 4:  // Phone Number
		                        System.out.println("Enter new Phone Number:");
		                        long phone = sc.nextLong();
		                        if (String.valueOf(phone).length() != 10) {
		                            throw new IllegalArgumentException("Phone number must be 10 digits.");
		                        }
		                        uc.updateUser(user, choice, phone);
		                        isValid = true;
		                        break;
		                    case 5:  // Age
		                        System.out.println("Enter new Age:");
		                        int age = sc.nextInt();
		                        if (age <= 0) {
		                            throw new IllegalArgumentException("Age must be a positive integer.");
		                        }
		                        uc.updateUser(user, choice, age);
		                        isValid = true;
		                        break;
		                    case 6:  // Gender
		                        System.out.println("Enter new Gender:");
		                        String gender = sc.nextLine();
		                        uc.updateUser(user, choice, gender);
		                        isValid = true;
		                        break;
		                    case 7:  // Location
		                        System.out.println("Enter new Location:");
		                        String location = sc.nextLine();
		                        uc.updateUser(user, choice, location);
		                        isValid = true;
		                        break;
		                    case 8:  // Date of Birth
		                        System.out.println("Enter new DOB (YYYY-MM-DD):");
		                        String dob = sc.nextLine();
		                        Date dateOfBirth = Date.valueOf(dob);
		                        uc.updateUser(user, choice, dateOfBirth);
		                        isValid = true;
		                        break;
		                    default:
		                        System.out.println("Invalid choice. Please select a valid option.");
		                        choice = sc.nextInt();
		                        sc.nextLine();  // Clear buffer
		                        break;
		                }
		            } catch (IllegalArgumentException e) {
		                System.out.println("Error: " + e.getMessage());
		            }
		        }
		        
		        if (isValid) {
		            System.out.println("User details updated successfully.");
		        } else {
		            System.out.println("Update failed. Please try again.");
		        }
		    }

		    // Delete user
		    private static void deleteUser(User user) throws SQLException {
		        System.out.println("\nAre you sure you want to delete your account? (yes/no)");
		        String confirm = sc.nextLine();

		        if (confirm.equalsIgnoreCase("yes")) {
		            int rowsDeleted = uc.deleteUser(user.getEmail());  // DAO method to delete
		            if (rowsDeleted > 0) {
		                System.out.println("User deleted successfully.");
		            } else {
		                System.out.println("User deletion failed.");
		            }
		        } else {
		            System.out.println("Deletion cancelled.");
		        }
		    }
		    
		}

		

		

		

	
	

			
	


