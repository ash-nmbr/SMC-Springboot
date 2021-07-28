package com.example.StockMarketCharting.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserModel")
//@NamedNativeQuery(name = "UserModel.findByName", query = "SELECT * FROM UserModel WHERE name = ?")
public class UserModel {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private String password;
	private String email;
	private boolean Confirmed;
	private boolean Admin;
	private String Role;
	private String mobileNumber;

	
	public UserModel() {
		
		super();
	}
	public UserModel(String name, String password, String email, Boolean admin) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
		this.Admin = admin;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public boolean isConfirmed() {
		return Confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		Confirmed = confirmed;
	}
	public boolean isAdmin() {
		return Admin;
	}
	public void setAdmin(boolean admin) {
		Admin = admin;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	
	
}
