/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class Quickchat {
 
   public static void main(String[] args) {


        Scanner input = new Scanner(System.in);

        // ---------------- REGISTRATION ----------------

        String username;

        while (true) {
            System.out.println("Enter your username:");
            username = input.nextLine();

            if (username.contains("_") && username.length() <= 5) {
                break;
            }

            System.out.println(
                "Username is not correctly formatted."
            );
        }

        System.out.println("Enter your first name:");
        String firstName = input.nextLine();

        System.out.println("Enter your last name:");
        String lastName = input.nextLine();

        String password;

        while (true) {
            System.out.println("Enter your password:");
            password = input.nextLine();

            boolean hasCap = false;
            boolean hasNum = false;
            boolean hasSpec = false;

            if (password.length() >= 8) {

                for (char c : password.toCharArray()) {

                    if (Character.isUpperCase(c)) {
                        hasCap = true;
                    }

                    if (Character.isDigit(c)) {
                        hasNum = true;
                    }

                    if (!Character.isLetterOrDigit(c)) {
                        hasSpec = true;
                    }
                }

                if (hasCap && hasNum && hasSpec) {
                    break;
                }
            }

            System.out.println(
                "Password is not correctly formatted. "
                + "Use at least 8 characters, an uppercase letter, "
                + "a number, and a special character."
            );
        }

        String cellNumber;

        while (true) {
            System.out.println("Enter your cell phone number:");
            cellNumber = input.nextLine();

            if (cellNumber.matches("\\+27\\d{9}")) {
                break;
            }

            System.out.println(
                "Cell phone number incorrectly formatted or does not "
                + "contain the international code. Please try again."
            );
        }

        // Create the login object
        Login user = new Login(username, password, cellNumber);

        String registrationResult = user.registerUser();
        System.out.println(registrationResult);

        // FIX: Accept the registration message with or without a full stop
        if (registrationResult == null
                || !registrationResult.trim().startsWith(
                    "User has been registered successfully")) {

            System.out.println(
                "Registration failed. Please try again."
            );

            input.close();
            return;
        }

        // ---------------- LOGIN ----------------

        System.out.println("Enter your username to login:");
        String loginUser = input.nextLine();

        System.out.println("Enter your password to login:");
        String loginPass = input.nextLine();

        boolean loginStatus = user.loginUser(loginUser, loginPass);

        System.out.println(
            user.returnLoginStatus(loginStatus, firstName, lastName)
        );

        // Only successfully logged-in users may continue
        if (!loginStatus) {
            System.out.println(
                "Username or password incorrect. Please try again."
            );

            input.close();
            return;
        }

        // ---------------- WELCOME ----------------

        System.out.println("Welcome to QuickChat.");

        // Ask for the message limit once at the start
        int messageLimit = readNumber(
            input,
            "How many messages would you like to enter?",
            1,
            Integer.MAX_VALUE
        );

        int messagesEntered = 0;
        int totalMessagesSent = 0;
        boolean running = true;

        // ---------------- MAIN MENU ----------------

        while (running) {

            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");

            int option = readNumber(
                input,
                "Choose an option:",
                1,
                3
            );

            switch (option) {

                case 1:

                    // Check the message limit
                    if (messagesEntered >= messageLimit) {
                        System.out.println(
                            "You have reached your message limit of "
                            + messageLimit + " messages."
                        );
                        break;
                    }

                    // Allow only the number of messages selected
                    while (messagesEntered < messageLimit) {

                        System.out.println(
                            "\nEntering message "
                            + (messagesEntered + 1)
                            + " of "
                            + messageLimit
                        );

                        System.out.println("Enter recipient number:");
                        String recipient = input.nextLine();

                        System.out.println("Enter your message:");
                        String messageText = input.nextLine();

                        // Count each message entered
                        messagesEntered++;

                        Message msg = new Message(
                            recipient,
                            messageText
                        );

                        // Validate message length and content
                        String validation = msg.sentMessage();

                        if (!msg.checkRecipientCell()) {
                            System.out.println(
                                "Invalid recipient number. "
                                + "Use the format +27123456789."
                            );
                            continue;
                        }

                        if (!msg.checkMessageID()) {
                            System.out.println("Invalid message ID.");
                            continue;
                        }

                        if (!validation.equals("Message ready to send")) {
                            System.out.println(validation);
                            continue;
                        }

                        // Display message details
                        System.out.println(msg.printMessage());

                        System.out.println("\n1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message");

                        int choice = readNumber(
                            input,
                            "Choose what to do with the message:",
                            1,
                            3
                        );

                        if (choice == 1) {

                            totalMessagesSent++;

                            System.out.println(
                                "Message successfully sent."
                            );

                        } else if (choice == 2) {

                            System.out.println(
                                "Message disregarded."
                            );

                        } else if (choice == 3) {

                            msg.storeMessage();

                        }
                    }

                    if (messagesEntered >= messageLimit) {
                        System.out.println(
                            "\nYou have reached your selected limit of "
                            + messageLimit + " messages."
                        );
                    }

                    break;

                case 2:

                    // Recently sent messages feature is in development
                    System.out.println("Coming Soon.");
                    break;

                case 3:

                    System.out.println("Application Closed.");
                    running = false;
                    break;

                default:
                    // The numeric input helper prevents this case
                    break;
            }
        }

        System.out.println(
            "Total messages sent: " + totalMessagesSent
        );

        input.close();
    }

    // Read and validate numeric input
    private static int readNumber(
            Scanner input,
            String prompt,
            int min,
            int max) {

        while (true) {

            System.out.println(prompt);
            String value = input.nextLine().trim();

            try {
                int number = Integer.parseInt(value);

                if (number >= min && number <= max) {
                    return number;
                }

            } catch (NumberFormatException e) {
                // Display the error below and request another input
            }

            System.out.println(
                "Invalid input. Please enter a number between "
                + min + " and " + max + "."
            );
        }
    }
}
    

