package com.javaworld.sample.osgi.spring.contact;

public class Contact {

	int contactId;
	String firstName;
	String lastName;
	
	public Contact(){
		
	}
	public Contact(int contactId, String firstName, String lastName) {
		super();
		this.contactId = contactId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String toString() {
		StringBuffer contactStr = new StringBuffer("Contact=[ ContactId=");
		contactStr.append(contactId);
		contactStr.append(", First Name="+ firstName);
		contactStr.append(", Last Name="+ lastName);
		contactStr.append("]");
		return contactStr.toString();
	}
	
}
