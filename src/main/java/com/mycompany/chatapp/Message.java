/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Student
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    private static int counter = 0;

    private static final String FILE_NAME = "message.json";
    private static final Random RANDOM = new Random();

    // Messages stored in JSON format
    private static JSONArray messageList = new JSONArray();

    // Prevent loading the file repeatedly
    private static boolean loadedFromFile = false;

    // Constructor
    public Message(String recipient, String message) {

        loadStoredMessages();

        this.recipient = recipient == null ? "" : recipient.trim();
        this.message = message == null ? "" : message;

        counter++;
        this.messageNumber = counter;

        // Generate a random 10-digit message ID
        long randomID = 1_000_000_000L
                + (long) (RANDOM.nextDouble() * 9_000_000_000L);

        this.messageID = String.valueOf(randomID);
    }

    // Check message ID
    public boolean checkMessageID() {

        return messageID != null
                && messageID.matches("\\d{1,10}");
    }

    // Check recipient cell phone number
    public boolean checkRecipientCell() {

        return recipient != null
                && recipient.matches("\\+27\\d{9}");
    }

    // Check message content and length
    public String sentMessage() {

        if (message.trim().isEmpty()) {
            return "Message cannot be empty.";
        }

        if (message.length() > 250) {

            int excess = message.length() - 250;

            return "Message exceeds 250 characters by "
                    + excess
                    + ", please reduce size.";
        }

        return "Message ready to send";
    }

    // Create message hash
    public String createMessageHash() {

        String trimmedMessage = message.trim();

        String firstWord = "";
        String lastWord = "";

        if (!trimmedMessage.isEmpty()) {

            String[] words = trimmedMessage.split("\\s+");

            firstWord = words[0];
            lastWord = words[words.length - 1];
        }

        messageHash = messageID.substring(0, 2)
                + ":"
                + messageNumber
                + ":"
                + firstWord.toUpperCase(Locale.ROOT)
                + lastWord.toUpperCase(Locale.ROOT);

        return messageHash;
    }

    // Display message details
    public String printMessage() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    // Load previously stored messages from the JSON file
    private static void loadStoredMessages() {

        if (loadedFromFile) {
            return;
        }

        File file = new File(FILE_NAME);

        if (file.exists() && file.length() > 0) {

            try (FileReader reader = new FileReader(file)) {

                JSONParser parser = new JSONParser();
                Object storedData = parser.parse(reader);

                if (storedData instanceof JSONArray) {

                    messageList = (JSONArray) storedData;
                    counter = messageList.size();

                } else {

                    System.out.println(
                        "The stored message file does not contain "
                        + "a JSON array."
                    );
                }

            } catch (IOException | ParseException e) {

                System.out.println(
                    "Error loading stored messages: " + e.getMessage()
                );
            }
        }

        loadedFromFile = true;
    }

    // Store message in the JSON file
    public JSONObject storeMessage() {

        loadStoredMessages();

        JSONObject obj = new JSONObject();

        obj.put("MessageID", messageID);
        obj.put("MessageHash", createMessageHash());
        obj.put("Recipient", recipient);
        obj.put("Message", message);

        messageList.add(obj);

        try (FileWriter writer = new FileWriter(FILE_NAME)) {

            // Write the complete JSON array to the file
            writer.write(messageList.toJSONString());

            System.out.println("Message successfully stored.");

        } catch (IOException e) {

            // Remove it from memory if writing fails
            messageList.remove(obj);

            System.out.println(
                "Error storing message: " + e.getMessage()
            );
        }

        return obj;
    }

    // Read stored messages (available for future development)
    public static void readMessage() {

        loadStoredMessages();

        if (messageList.isEmpty()) {

            System.out.println("No stored messages found.");
            return;
        }

        System.out.println("\nStored Messages:");

        for (Object item : messageList) {

            if (item instanceof JSONObject) {

                JSONObject obj = (JSONObject) item;

                System.out.println("----------------------------");
                System.out.println(
                    "Message ID: " + obj.get("MessageID")
                );
                System.out.println(
                    "Message Hash: " + obj.get("MessageHash")
                );
                System.out.println(
                    "Recipient: " + obj.get("Recipient")
                );
                System.out.println(
                    "Message: " + obj.get("Message")
                );
            }
        }

        System.out.println("----------------------------");
    }
}
    

