package com.javaworld.osgi.spring;


import com.javaworld.sample.osgi.spring.contact.Contact;
import com.javaworld.sample.osgi.spring.contact.ContactDAO;

public class HelloWorld {
	ContactDAO contactDAO;
	
	public ContactDAO getContactDAO() {
		return contactDAO;
	}

	public void setContactDAO(ContactDAO contactDAO) {
		this.contactDAO = contactDAO;
	}

	public void start() throws Exception {
		System.out.println("Hello Spring World!! ");
		System.out.println("Result of ContactDAO.getContactList()" + contactDAO.getContactList()  );
		System.out.println("Contact with contactId " + contactDAO.getContact(1));
		Contact insertContact = new Contact();
		insertContact.setContactId(4);
		insertContact.setFirstName("Anil");
		insertContact.setLastName("Kumbale");
		contactDAO.updateContact(insertContact);
		System.out.println("Result of ContactDAO.getContactList()" + contactDAO.getContactList()  );
	}
	
	public void stop() throws Exception {
		System.out.println("Goodbye Spring World!!");
	}
}
