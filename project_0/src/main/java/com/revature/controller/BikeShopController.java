package com.revature.controller;

import java.util.ArrayList;
import java.util.Scanner;

import com.revature.beans.Bike;
import com.revature.beans.BikeStatus;
import com.revature.beans.Role;
import com.revature.beans.User;
import com.revature.services.BikeService;
import com.revature.services.BikeServiceImpl;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

public class BikeShopController {
	private static Scanner scan;
	private static UserService userServ = new UserServiceImpl();
	private static BikeService bikeServ = new BikeServiceImpl();

	private static User user = new User();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		scan = new Scanner(System.in);

		System.out.println("----Welcome to Kyle's Bike Shop----");

		loginLoop: while (true) {

			System.out.println("Would you like to login or register?");
			String userInput = scan.nextLine();
			if (userInput.toLowerCase().equalsIgnoreCase("login")) {
				user = logInUser();
				break loginLoop;
			} else if (userInput.toLowerCase().equalsIgnoreCase("register")) {
				user = registerUser();
				if (user == null) {
					continue;
				} else
					break loginLoop;
			} else {
				continue;
			}
		}

//if you're entering the mainloop, youre logged in
//mainloop first checks roles. each role has its own commands

		mainLoop: while (true) {
			String userInput;

			switch (user.getRole().getName()) {
			case "customer":
				System.out.println("#");
				userInput = scan.nextLine();

				switch (userInput.toLowerCase()) {
				case "help":
					System.out.println(
							"Commands that customers can use are: \n 1. View my bikes \n 2. View available bikes\n 3. View remaining payments for a bike \n 4. Make an offer for a bike\n");
					break;
				case "1":
					System.out.println("View my bikes");
					break;
				case "2":
					System.out.println("View available bikes");
					System.out.println("ID \t BRAND \t YEAR \t COLOR\n");
					ArrayList<Bike> bikes = bikeServ.listAvailableBikes();
					for (Bike bike : bikes){
						System.out.println(bike.getId() + " \t " + bike.getBrand() + " \t " + bike.getYear() + " \t " + bike.getColor());
					};
					break;
				case "3":
					System.out.println("View remaining payments for a bike");
					break;
				case "4":
					System.out.println("Make an offer for a bike");
					
					System.out.println("Enter id of bike you want to make an offer for: ");
					Integer bikeId = Integer.parseInt(scan.nextLine());
					
					System.out.println("Enter amount you want to offer: ");
					Integer amount = Integer.parseInt(scan.nextLine());
					
					if (userServ.makeOffer(bikeId, user.getId(), amount) > 0) {
						System.out.println("Offer accepted.");
					}
					else {
						System.out.println("Offer unsuccessful.");
					}
					break;
				case "quit":
					System.out.println("goodbye...");
					break mainLoop;
				case "exit":
					System.out.println("goodbye...");
					break mainLoop;
				default:
					System.out.println("command not recognized");
				}
				break;
			case "employee":
				System.out.println("(type help for list of commands");
				System.out.println("#");
				userInput = scan.nextLine();
				switch (userInput.toLowerCase()) {
				case "help":
					System.out.println(
							"Commands that employees can use are: \n 1. Add a bicycle to the shop\n 2. accept or reject a pending offer \n 3. remove a bicycle from the shop \n 4. view all payments");
					break;
				case "quit":
					System.out.println("goodbye...");
					break mainLoop;
				case "exit":
					System.out.println("goodbye...");
					break mainLoop;
				case "1":
					System.out.println("Add a bicycle to the shop");
					Bike newBikeToAdd = new Bike();

					BikeStatus status = new BikeStatus();
					newBikeToAdd.setStatus(status);

					newBikeToAdd.setOwnerID(5);// user with id 5 is system

					System.out.println("Brand: ");
					userInput = scan.nextLine();
					newBikeToAdd.setBrand(userInput);

					System.out.println("Year: ");
					userInput = scan.nextLine();
					newBikeToAdd.setYear(userInput);

					System.out.println("Color: ");
					userInput = scan.nextLine();
					newBikeToAdd.setColor(userInput);

					Integer newId = bikeServ.addBike(newBikeToAdd);

					if (newId == null) {
						System.out.println("Failed to add bike");
						continue;
					} else {
						newBikeToAdd.setId(newId);
						System.out.println("Bike with id " + newId + " was added successfully.");
					}

					break;
				case "2":
					System.out.println("accept or reject a pending offer");
					break;
				case "3":
					System.out.println("remove a bicycle from the shop");
					// todo list all bikes
					System.out.println("Enter ID of bike you want to remove");
					userInput = scan.nextLine();
					Bike bikeToRemove = bikeServ.getBikeById(Integer.parseInt(userInput));
					if (bikeToRemove != null) {
						bikeServ.removeBike(bikeToRemove);
						System.out.println("Bike with id " + userInput + " was removed");
					} else {
						System.out.println("there is no bike with id " + userInput + " or something else occurred");
					}

					break;
				case "4":
					System.out.println("view all payments");

					break;
				default:
					System.out.println("command not recognized");
				}
				break;
			}
		}
	}

	private static User registerUser() {
		while (true) {
			User user = new User();
			System.out.println("Username:");
			user.setName(scan.nextLine());
			System.out.println("Password:");
			user.setPassword(scan.nextLine());
			Role r = new Role(2);
			user.setRole(r);
			System.out.println(user.getName() + " : " + user.getPassword());

			if (userServ.getUserByName(user.getName()) == null) {
				Integer newUserId = userServ.addUser(user);
				user.setId(newUserId);
				return user;
			} else {
				System.out.println("User already exists.");
				return null;
			}
		}
	}

	private static User logInUser() {
		while (true) {
			System.out.println("Username: ");
			String username = scan.nextLine();
			System.out.println("Password: ");
			String password = scan.nextLine();

			User user = userServ.getUserByName(username);
			if (user == null) {
				System.out.print("User not found");
			} else if (user.getPassword().equals(password)) {
				System.out.println("Welcome back!");
				return user;
			} else {
				System.out.print("That password is incorrect. ");
			}
			System.out.println("Do you want to try again?");
			String input = scan.nextLine();
			if ("yes".equalsIgnoreCase(input)) {
				continue;
			} else
				return null;
		}
	}
}