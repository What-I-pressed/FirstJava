package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        DatabaseWorker dw = new DatabaseWorker();
        dw.createTables();
        int query = -1;
        while(query != 0){
            System.out.println("Enter [1] - to add new contact\n[2] - to edit existing contact\n[3] - to show existing contacts\n[0] - to leave");
            query = Integer.parseInt(in.nextLine());
            switch (query) {
                case 1 -> {
                    System.out.println("First name : ");
                    String firstName = in.nextLine();
                    System.out.println("Last name : ");
                    String lastName = in.nextLine();
                    System.out.println("Email : ");
                    String email = in.nextLine();
                    System.out.println("Phone number : ");
                    String phoneNumber = in.nextLine();
                    dw.addContact(firstName, lastName, email, phoneNumber);
                }
                case 2 -> {
                    System.out.println("First name : ");
                    String firstName = in.nextLine();
                    System.out.println("Last name : ");
                    String lastName = in.nextLine();
                    System.out.println("Email(used as indefication for user therefore cannot be changed) : ");
                    String email = in.nextLine();
                    System.out.println("Phone number : ");
                    String phoneNumber = in.nextLine();
                    dw.editContact(firstName, lastName, email, phoneNumber);
                }
                case 3 -> {
                    dw.showContacts();
                }
            }
        }

    }
}