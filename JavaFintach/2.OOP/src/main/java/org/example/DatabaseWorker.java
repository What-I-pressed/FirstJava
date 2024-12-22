package org.example;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseWorker {
    // private String host = "ep-still-morning-a2sdufws.eu-central-1.aws.neon.tech";
    // private String url = "jdbc:postgresql://"+host+":5432/neondb";
    // private String user = "neondb_owner";
    // private String password = "oh5pxmtGZN0J";

    Connection connection = null;

    public DatabaseWorker() {
        // Initialize the connection
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DbConfig dbConfig = objectMapper.readValue(new File("D:\\Repos\\JavaFintach\\2.OOP\\src\\main\\java\\options.json"), DbConfig.class);
            connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());

            if (connection != null) {
                System.out.println("Connected to the database!");

            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println("Connection failure.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        createContacts();
    }

    public void addContact(String firstName, String lastName, String email, String phoneNumber){
        PreparedStatement preparedStatement = null;
        String insertQuery = "INSERT INTO Contacts (FirstName, LastName, Email, PhoneNumber) VALUES (?, ?, ?, ?)";

        try {

            // Prepare the statement
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, firstName);    
            preparedStatement.setString(2, lastName);     
            preparedStatement.setString(3, email);        
            preparedStatement.setString(4, phoneNumber);  

            // Execute the query
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new contact was inserted successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting contact: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void editContact(String firstName, String lastName, String email, String phoneNumber){
        PreparedStatement preparedStatement = null;

        String updateQuery = "UPDATE Contacts SET FirstName = ?, LastName = ?, PhoneNumber = ? WHERE Email = ?";

        try {
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, firstName);    
            preparedStatement.setString(2, lastName);     
            preparedStatement.setString(3, phoneNumber);  
            preparedStatement.setString(4, email);        
    
            
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Contact updated successfully!");
            } else {
                System.out.println("No contact found with the provided email.");
            }
    
        } catch (SQLException e) {
            System.out.println("Error updating contact: " + e.getMessage());
        } finally {
            // Закриваємо ресурси
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void showContacts(){
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM Contacts";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            System.out.println("List of contacts : ");

            while(resultSet.next()){
                int contactID = resultSet.getInt("ContactID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String email = resultSet.getString("Email");
                String phoneNumber = resultSet.getString("PhoneNumber");
                Timestamp createdAt = resultSet.getTimestamp("CreatedAt");
                
                System.out.printf("\nID: %d\nFirst Name: %s\nLast Name: %s\nEmail: %s\nPhone: %s\nCreated At: %s%n \n",
                        contactID, firstName, lastName, email, phoneNumber, createdAt);
            }
        } catch (Exception e)  {
            System.out.println("Error retrieving contacts: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createContacts() {
        try {
            Path filePath = Path.of(Objects
                    .requireNonNull(getClass().getClassLoader().getResource("contacts_table.sql")).toURI());
            // Read the content of the file into a String
            String sqlScript = Files.readString(filePath);
            //Клас для виконання комнад до БД
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlScript);
            statement.close();
        } catch (IOException e) {
            // Handle potential I/O errors
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (URISyntaxException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Problem query in database: " + e.getMessage());
        }
    }
}
