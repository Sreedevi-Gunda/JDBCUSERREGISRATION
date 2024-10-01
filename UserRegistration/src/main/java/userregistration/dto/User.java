package userregistration.dto;

import java.sql.Date;

public class User {
	
	//data members
	private String email,name,gender,location,password;
	private int age;
	private Date dob;
	private long contact;
	
	//getters and setters
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public long getContact() {
		return contact;
	}
	public void setContact(long contact) {
		this.contact = contact;
	}
	
	
	//PARAMETERIZED CONSTRUCTOR TO SAVE USER OBJECT
	public User(String email, String name, String gender, String location, String password, int age, Date dob,
			long contact) {
		super();
		this.email = email;
		this.name = name;
		this.gender = gender;
		this.location = location;
		this.password = password;
		this.age = age;
		this.dob = dob;
		this.contact = contact;
	}
	
	
	//NO ARGUMENTED CONSTRUCTOR TO CREATE USER OBJECT WITH NO ARGUMENTSS
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	

	
	
	
	


}
