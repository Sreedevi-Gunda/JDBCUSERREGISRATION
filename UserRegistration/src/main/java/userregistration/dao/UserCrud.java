package userregistration.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.cj.jdbc.Driver;


import userregistration.dto.User;

public class UserCrud {
	
	// To track/validate user object manipulation in the database
	public int count; // Number of rows
	
	// Create the user table in MySQL database
	public void createUserTable() throws SQLException {
		DriverManager.registerDriver(new Driver());
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistrationjdbc?createDatabaseIfNotExist=true", "root", "root");
		Statement s = con.createStatement();
		s.execute("CREATE TABLE IF NOT EXISTS user ("
				+ "Email VARCHAR(45) PRIMARY KEY, "
				+ "User_Name VARCHAR(45) NOT NULL, "
				+ "Gender VARCHAR(45) NOT NULL, "
				+ "Location VARCHAR(45) NOT NULL, "
				+ "Password VARCHAR(45) NOT NULL, "
				+ "Age INT(2) NOT NULL, "
				+ "DOB DATE NOT NULL, "
				+ "Phone_Number BIGINT(10) NOT NULL UNIQUE)");
		s.close();
		con.close();
	}

	// Save user data into the database
	public int saveUser(User user) throws SQLException {
		DriverManager.registerDriver(new Driver());
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistrationjdbc?user=root&password=root");
		PreparedStatement ps = con.prepareStatement("INSERT INTO user (Email, User_Name, Password, Phone_Number, Age, Gender, Location, DOB) VALUES (?,?,?,?,?,?,?,?)");
		ps.setString(1, user.getEmail());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		ps.setLong(4, user.getContact());
		ps.setInt(5, user.getAge());
		ps.setString(6, user.getGender());
		ps.setString(7, user.getLocation());
		ps.setDate(8, user.getDob());
		count = ps.executeUpdate();
		ps.close();
		con.close();
		return count;
	}
	
	//For save validation
	// Check if a user with the provided email already exists
	public boolean isEmailExists(String email) throws SQLException {
		DriverManager.registerDriver(new Driver());
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistrationjdbc?user=root&password=root");
		String query = "SELECT COUNT(*) FROM user WHERE Email = ?";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, email);
		ResultSet rs = ps.executeQuery();
		boolean exists = false;
		if (rs.next()) {
			exists = rs.getInt(1) > 0;  // If count > 0, the email exists
		}
		rs.close();
		ps.close();
		con.close();
		return exists;
	}
	
	//For save validation
	// Check if a user with the provided phone number already exists
	public boolean isPhoneExists(long phoneNumber) throws SQLException {
		DriverManager.registerDriver(new Driver());
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistrationjdbc?user=root&password=root");
		String query = "SELECT COUNT(*) FROM user WHERE Phone_Number = ?";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setLong(1, phoneNumber);
		ResultSet rs = ps.executeQuery();
		boolean exists = false;
		if (rs.next()) {
			exists = rs.getInt(1) > 0;  // If count > 0, the phone number exists
		}
		rs.close();
		ps.close();
		con.close();
		return exists;
	}
	
	
	 // Fetch user from database using email and password
    public static User fetchUser(User user) throws SQLException {
        DriverManager.registerDriver(new Driver());
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistrationjdbc?user=root&password=root");
        
        String query = "SELECT * FROM user WHERE Email = ? AND Password = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());

        ResultSet rs = ps.executeQuery();
        User fetchedUser = null;
        if (rs.next()) {
            fetchedUser = new User();
            fetchedUser.setEmail(rs.getString("Email"));
            fetchedUser.setName(rs.getString("User_Name"));
            fetchedUser.setPassword(rs.getString("Password"));
            fetchedUser.setContact(rs.getLong("Phone_Number"));
            fetchedUser.setGender(rs.getString("Gender"));
            fetchedUser.setAge(rs.getInt("Age"));
            fetchedUser.setLocation(rs.getString("Location"));
            fetchedUser.setDob(rs.getDate("DOB"));
           

        }
 
        rs.close();
        ps.close();
        con.close();

        return fetchedUser;
    }

    // Update user details
    
    public void updateUser(User u, int i, Object obj) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            DriverManager.registerDriver(new Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistrationjdbc?user=root&password=root");

            // Switch case based on which field the user wants to update
            String query = null;

            switch (i) {
                case 1:
                    query = "UPDATE user SET Email = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, (String) obj);
                    break;
                case 2:
                    query = "UPDATE user SET User_Name = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, (String) obj);
                    break;
                case 3:
                    query = "UPDATE user SET Password = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, (String) obj);
                    break;
                case 4:
                    query = "UPDATE user SET Phone_Number = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    ps.setLong(1, (long) obj);
                    break;
                case 5:
                    query = "UPDATE user SET Age = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    ps.setInt(1, (int) obj);
                    break;
                case 6:
                    query = "UPDATE user SET Gender = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, (String) obj);
                    break;
                case 7:
                    query = "UPDATE user SET Location = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, (String) obj); // Changed "living_place" to "Location"
                    break;
                case 8:
                    query = "UPDATE user SET DOB = ? WHERE Email = ? AND Password = ?";
                    ps = con.prepareStatement(query);
                    Date obj1 = Date.valueOf((String) obj);
                    ps.setDate(1, obj1);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid update field option.");
            }

            ps.setString(2, u.getEmail()); // Get email from User object
            ps.setString(3, u.getPassword()); // Get password from User object

            count = ps.executeUpdate(); // Execute the update

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of opening
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }

        // Fetch user details after the update (if required)
        try {
            fetchUser(u);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Delete user from database
    public int deleteUser(String email) throws SQLException {
        DriverManager.registerDriver(new Driver());
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistrationjdbc?user=root&password=root");
        
        String query = "DELETE FROM user WHERE Email = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        
        int rowsDeleted = ps.executeUpdate();
        ps.close();
        con.close();
        return rowsDeleted;
    }
}
	



