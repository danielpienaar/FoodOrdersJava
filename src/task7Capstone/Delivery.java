package task7Capstone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Delivery {

	public static void main(String[] args) {
		// NOTE: Please make sure drivers.txt is correctly formatted, as the given drivers.txt file has some issues with extra spaces and missing load numbers
		
		// Welcome and initialize objects
		JOptionPane.showMessageDialog(null, "Welcome to the Food Quick delivery service.", "Food Quick", JOptionPane.PLAIN_MESSAGE);
		Customer c = null;
		Restaurant r = null;
		// Create new customer object and update order number
		try {
			int orderNo = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the order number:", "Food Quick (Customer)", JOptionPane.INFORMATION_MESSAGE));
			String cName = JOptionPane.showInputDialog(null, "Enter the customer's name:", "Food Quick (Customer)", JOptionPane.INFORMATION_MESSAGE);
			String cContactNo = JOptionPane.showInputDialog(null, "Enter the customer's contact number:", "Food Quick (Customer)", JOptionPane.INFORMATION_MESSAGE);
			String cAddress = JOptionPane.showInputDialog(null, "Enter the customer's address:", "Food Quick (Customer)", JOptionPane.INFORMATION_MESSAGE);
			String cCity = JOptionPane.showInputDialog(null, "Enter the customer's location(city):", "Food Quick (Customer)", JOptionPane.INFORMATION_MESSAGE);
			String cEmail = JOptionPane.showInputDialog(null, "Enter the customer's email:", "Food Quick (Customer)", JOptionPane.INFORMATION_MESSAGE);
			if (cName.equals("") || cContactNo.equals("") || cAddress.equals("") || cCity.equals("") || cEmail.equals("")) {
				JOptionPane.showMessageDialog(null, "Invalid Customer details: Must fill in all details.", "Invalid Customer details", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			c = new Customer(orderNo, cName, cContactNo, cAddress, cCity, cEmail);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid Customer details: Please enter valid input.", "Invalid Customer details", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		// Create new restaurant object
		try {
			String rName = JOptionPane.showInputDialog(null, "Enter the restaurant's name:", "Food Quick (Restaurant)", JOptionPane.INFORMATION_MESSAGE);
			String rCity = JOptionPane.showInputDialog(null, "Enter the restaurant's location(city):", "Food Quick (Restaurant)", JOptionPane.INFORMATION_MESSAGE);
			String rContactNo = JOptionPane.showInputDialog(null, "Enter the restaurant's contact number:", "Food Quick (Restaurant)", JOptionPane.INFORMATION_MESSAGE);
			String rSpecialPrep = JOptionPane.showInputDialog(null, "Enter any special preparation instructions:", "Food Quick (Restaurant)", JOptionPane.INFORMATION_MESSAGE);
			if (rName.equals("") || rCity.equals("") || rContactNo.equals("") || rSpecialPrep.equals("")) {
				JOptionPane.showMessageDialog(null, "Invalid Restaurant details: Must fill in all details.", "Invalid Customer details", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			r = new Restaurant(rName, rCity, rContactNo, rSpecialPrep);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid Restaurant details: Please enter valid input.", "Invalid Customer details", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		// Add orders
		int ordering = 0;
		while (ordering != 1) {
			ordering = JOptionPane.showConfirmDialog(null, "Would you like to add an item to the order?\nItems currently in order: " + r.count, "Food Quick (Add Order Items)", JOptionPane.YES_NO_OPTION);
			if (ordering == 0) {
				try {
					String orderName = JOptionPane.showInputDialog(null, "Enter the menu item's name:", "Food Quick (Add Order Items)", JOptionPane.INFORMATION_MESSAGE);
					int orderNumber = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the amount the customer ordered:", "Food Quick (Add Order Items)", JOptionPane.INFORMATION_MESSAGE));
					double orderPrice = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter the price of the menu item:", "Food Quick (Add Order Items)", JOptionPane.INFORMATION_MESSAGE));
					r.addOrder(orderName, orderNumber, orderPrice);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid order: Please enter valid input.", "Invalid Order", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		// Create invoice at given path, and create/update other necessary files
		String invoicePath = "src/task7Capstone/invoice.txt";
		String driver = getDriver(c.city, r.city);
		try {
			Formatter f = new Formatter(invoicePath);
			if (driver.equals("Sorry! Our drivers are too far away from you to be able to deliver to your location.")) {
				f.format("%s", driver);
			}else {
				f.format("%s%s", c.toString(), "\n\n");
				f.format("%s", "You have ordered the following from Aesop's Pizza in Cape Town:\n\n");
				f.format("%s", r.getOrders() + "\n");
				f.format("%s", "Special Instructions: " + r.specialPrep + "\n\n");
				f.format("%s", "Total: R" + r.total + "\n\n");
				f.format("%s", driver + " is closest to your location so he/she will be delivering your order to you at:\n\n" + c.address + "\n\n");
				f.format("%s", "If you need to contact the restaurant, their number is " + r.contactNo + ".");
				// Create/update orders and locations
				createOrderNums("src/task7Capstone/OrderNums.txt", c.name, c.orderNumber);
				createLocations("src/task7Capstone/Locations.txt", c.name, c.city);
			}
			System.out.println("Invoice successfully created at: " + invoicePath);
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createOrderNums(String path, String newName, int newOrder) {
		// Get all existing orders from file
		ArrayList<Customer> c = new ArrayList<>();
		try {
			File orders = new File(path);
			Scanner sc;
			sc = new Scanner(orders);
			while (sc.hasNextLine()) {
				Scanner scLine = new Scanner(sc.nextLine()).useDelimiter(", ");
				String name = scLine.next();
				String orderNo = scLine.next();
				c.add(new Customer(name, orderNo, true));
				scLine.close();
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("No previous orders, creating new orders file at: " + path);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		// Add new order, arrange alphabetically and write back to file
		Customer newCust = new Customer(newName, ""+newOrder, true);
		c.add(newCust);
		c.sort(new Customer());
		try {
			Formatter f = new Formatter(path);
			for (int i = 0; i < c.size(); i++) {
				f.format("%s, %s", c.get(i).name, newCust.formatOrder(c.get(i).orderNumber));
				if (i != c.size()-1) {
					f.format("%s", "\n");
				}
			}
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void createLocations(String path, String newName, String newLocation) {
		// Create array of locations from drivers file and get existing customer locations
		ArrayList<String> locations = new ArrayList<>();
		ArrayList<Customer> c = new ArrayList<>();
		try {
			// Available Locations
			File drivers = new File("src/task7Capstone/drivers.txt");
			Scanner sc;
			sc = new Scanner(drivers);
			while (sc.hasNextLine()) {
				Scanner scLine = new Scanner(sc.nextLine()).useDelimiter(", ");
				scLine.next();
				String location = scLine.next();
				if (!locations.contains(location)) {
					locations.add(location);
				}
				scLine.close();
			}
			sc.close();
			// Existing customer locations, plus new customer location
			c.add(new Customer(newName, newLocation, false));
			File custLocations = new File(path);
			sc = new Scanner(custLocations);
			while (sc.hasNextLine()) {
				Scanner scLine = new Scanner(sc.nextLine()).useDelimiter(", ");
				try {
					String name = scLine.next();
					String location = scLine.next();
					c.add(new Customer(name, location, false));
				} catch (NoSuchElementException e) {
					// Ignore lines without two items separated by ", "
				}
				scLine.close();
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("No previous orders, creating new locations file at: " + path);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		// Add new customer location and write back to file
		try {
			Formatter f = new Formatter(path);
			for (int i = 0; i < locations.size(); i++) {
				f.format("%s", locations.get(i) + "\n-----\n");
				for (Customer customer : c) {
					if ((customer.city).equals(locations.get(i))) {
						f.format("%s, %s", customer.name, customer.city + "\n");
					}
				}
				if (i != locations.size()-1) {
					f.format("%s", "\n");
				}
			}
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getDriver(String cLocation, String rLocation) {
		// Gets a driver from the file in the given location with the lowest load, and updates the load number
		String driver = "";
		int lowestLoad = 100;
		int count = 0;
		int lineNo = 0;

		try {
			File drivers = new File("src/task7Capstone/drivers.txt");
			Scanner sc = new Scanner(drivers);
			while (sc.hasNextLine()) {
				count++;
				Scanner scLine = new Scanner(sc.nextLine()).useDelimiter(", ");
				String driverName = scLine.next();
				String driverLocation = scLine.next();
				int driverLoad = scLine.nextInt();
				if (driverLocation.equalsIgnoreCase(rLocation) && driverLocation.equalsIgnoreCase(cLocation)) {
					if (driverLoad < lowestLoad) {
						lowestLoad = driverLoad;
						driver = driverName;
						lineNo = count;
					}
				}
				scLine.close();
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Return driver
		if (driver.equals("")) {
			return "Sorry! Our drivers are too far away from you to be able to deliver to your location.";
		} else {
			// Creates updated text file
			StringBuilder updated = new StringBuilder();
			int fileLines = count;
			count = 0;
			try {
				File drivers = new File("src/task7Capstone/drivers.txt");
				Scanner sc = new Scanner(drivers);
				while (sc.hasNextLine()) {
					count++;
					Scanner scLine = new Scanner(sc.nextLine()).useDelimiter(", ");
					if (lineNo == count) {
						updated.append(scLine.next() + ", ");
						updated.append(scLine.next() + ", ");
						if (count != fileLines) {
							int num = scLine.nextInt();
							System.out.println("Load before: " + num);
							num++;
							System.out.println("Load after: " + num);
							updated.append(num + "\n");
						} else {
							int num = scLine.nextInt();
							System.out.println("Load before (no line break): " + num);
							num++;
							System.out.println("Load after (no line break): " + num);
							updated.append(num);
						}
					} else {
						updated.append(scLine.next() + ", ");
						updated.append(scLine.next() + ", ");
						if (count != fileLines) {
							updated.append((scLine.nextInt()) + "\n");
						} else {
							updated.append((scLine.nextInt()));
						}
					}
					scLine.close();
				}
				sc.close();
				drivers.delete();
				Formatter f = new Formatter("src/task7Capstone/drivers.txt");
				f.format("%s", updated.toString());
				f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return driver;
		}
	}

}
