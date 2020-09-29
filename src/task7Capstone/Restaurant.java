package task7Capstone;

import java.util.ArrayList;

public class Restaurant {
	
	public String name;
	public String city;
	public String contactNo;
	public String specialPrep;
	public double total;
	// Orders
	private ArrayList<String> orderNameArr = new ArrayList<>();
	private ArrayList<Integer> orderNumberArr = new ArrayList<>();
	private ArrayList<Double> orderPriceArr = new ArrayList<>();
	public int count = 0;
	
	public Restaurant(String name, String city, String contactNo, String specialPrep) {
		this.name = name;
		this.city = city;
		this.contactNo = contactNo;
		this.specialPrep = specialPrep;
	}
	
	public void addOrder(String orderName, int orderNumber, double orderPrice) {
		// Adds an order to the order arrays and updates total
		orderNameArr.add(orderName);
		orderNumberArr.add(orderNumber);
		orderPriceArr.add(orderPrice);
		total += (orderPrice * orderNumber);
		count++;
	}
	
	public String getOrders() {
		// Prints out all the customer's orders in the correct format
		String output = "";
		for (int i = 0; i < orderNameArr.size(); i++) {
			double price = orderPriceArr.get(i);
			price = price * 100;
			price = Math.round(price);
			price = price / 100;
			output += orderNumberArr.get(i) + " x " + orderNameArr.get(i) + " (R" + price + ")\n";
		}
		return output;
	}
}
