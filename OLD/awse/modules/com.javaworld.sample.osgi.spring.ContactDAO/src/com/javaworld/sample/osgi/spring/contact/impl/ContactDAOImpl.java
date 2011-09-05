package com.javaworld.sample.osgi.spring.contact.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.javaworld.sample.osgi.spring.contact.Contact;
import com.javaworld.sample.osgi.spring.contact.ContactDAO;

public class ContactDAOImpl implements ContactDAO {

	DataSource dataSource;
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Contact getContact(int contactId) {
		System.out.println("Inside ContactDAOImpl.getContact()");
		Contact contact = new Contact();
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt= connection.prepareStatement("SELECT * FROM CONTACT WHERE CONTACTID=?");
			stmt.setInt(1, contactId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			String firstName = rs.getString("FIRSTNAME");
			String lastName = rs.getString("LASTNAME");
			contact.setContactId(contactId);
			contact.setFirstName(firstName);
			contact.setLastName(lastName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contact;
	}

	public List getContactList() {
		System.out.println("Inside ContactDAOImpl.getContactList()");
		List contactList = new ArrayList();
		try {
			Connection connection = dataSource.getConnection();

			Statement stmt= connection.createStatement();
			ResultSet rs =stmt.executeQuery("SELECT * FROM CONTACT");
			while(rs.next()){
				int contactId = rs.getInt("CONTACTID");
				String firstName = rs.getString("FIRSTNAME");
				String lastName = rs.getString("LASTNAME");
				Contact contact = new Contact(contactId,firstName,lastName);
				contactList.add(contact);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}


	public void deleteContact(int contactId) {
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM CONTACT WHERE CONTACTID=?");
			stmt.setInt(1, contactId);
			stmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}


	public void insertContact(Contact contact) {
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO CONTACT VALUES (?,?,?,?)"); 
			stmt.setInt(1, contact.getContactId());
			stmt.setString(2, contact.getFirstName());
			stmt.setString(3, contact.getLastName());
			stmt.setString(4, "");
			stmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}


	public void updateContact(Contact contact) {
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement("Update contact set FIRSTNAME=?, LASTNAME=? where contactid=?"); 
			stmt.setInt(3, contact.getContactId());
			stmt.setString(1, contact.getFirstName());
			stmt.setString(2, contact.getLastName());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
	}

}
