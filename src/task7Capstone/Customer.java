package task7Capstone;

import java.util.Comparator;

public class Customer implements Comparator<Customer>{
	
	public int orderNumber;
	public String name;
	public String contactNo;
	public String address;
	public String city;
	public String email;
	
	// Constructor for invoice
	public Customer(int orderNumber, String name, String contactNo, String address, String city, String email) {
		this.orderNumber = orderNumber;
		this.name = name;
		this.contactNo = contactNo;
		this.address = address;
		this.city = city;
		this.email = email;
	}
	
	// Constructor for other text files
	public Customer(String name, String data, boolean type) {
		this.name = name;
		if (type) {
			try {
				orderNumber = Integer.parseInt(data);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			city = data;
		}
	}
	
	public Customer() {
		// Empty constructor to make use of public methods
	}
	
	// Implemented compare method to sort customers alphabetically by name
	@Override
    public int compare(Customer o1, Customer o2) {
        return o1.name.compareToIgnoreCase(o2.name);
    }
	
	public String formatOrder(int orderNo) {
		// Format order number
		String numStr = "" + orderNo;
		
		switch (numStr.length()) {
			case 1: 
				return "000" + numStr;
			case 2:
				return "00" + numStr;
			case 3:
				return "0" + numStr;
			default:
				return numStr;
		}
	}
	
	public String toString() {
		return "Order Number: " + formatOrder(orderNumber)
				+ "\nCustomer: " + name
				+ "\nEmail: " + email
				+ "\nPhone Number: " + contactNo
				+ "\nLocation: " + city;
	}
	
}
