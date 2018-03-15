package com.mainserver.project;

import java.sql.*;
import java.util.Scanner;

public class Phonebook {
    public static String help_msg = "Press: H - for Help, A - for Adding Contact, S - for Searching Contact, E - for Editing Contact, Q - Exit.";
    Scanner scanner = new Scanner(System.in);
    String command = "";
    boolean flag = false;
    public void run() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/phonebook", "root", "root");
            conn.createStatement().executeUpdate("create table IF NOT EXISTS contacts (" +
                    "id int auto_increment primary key," +
                    "name varchar(50)," +
                    "lname varchar(50)," +
                    "phone varchar(50))"
            );
            System.out.println("\n\n***** Welcome to Phonebook *****\n\n");
            System.out.println("[Main Menu] " + help_msg + "\n");
            while(true) {
                if (flag) {
                    System.out.println(help_msg);
                }
                command = scanner.nextLine().trim();
                if (command.trim().length() > 1) {flag = true;}
                if (command.equalsIgnoreCase("H")) {
                    flag = true;
                } else if(command.equalsIgnoreCase("A")) {
                    System.out.println("Type in contact details in format: name, lname, phone\n");
                    while (true) {
                        String data = scanner.nextLine().trim();
                        String[] temp = data.split(",");
                        if (temp.length != 3) {
                            System.out.println("Error, the insertion format should be in the format: name, lname, phone\n");
                            continue;
                        }
                        conn.createStatement().executeUpdate("insert into contacts (name,lname,phone) values" +
                                "('" + temp[0] + "','" + temp[1] + "','" + temp[2] + "')");
                        break;
                    }
                } else if(command.equalsIgnoreCase("S")) {
                    flag = true;
                    System.out.println("Type in the name you are searching for: \n");
                    String data = scanner.nextLine().trim();

                    String q = "select * from contacts where name like '%" + data + "%' or lname like '%" + data + "%' or phone like '%" + data + "%'";
                    PreparedStatement pst = conn.prepareStatement(q);
                    pst.clearParameters();
                    ResultSet rs = pst.executeQuery();
                    System.out.println("********            Results         ********");
                    System.out.println("Id\t\t\tName\t\t\tLast Name\t\t\tPhone number\n");
                    while (rs.next()) {
                        int rowId = rs.getInt(1);
                        String name = rs.getString(2);
                        String lname = rs.getString(3);
                        String phone = rs.getString(4);
                        System.out.printf("%d\t\t\t%s\t\t\t%s\t\t\t%s\n", rowId, name, lname, phone);
                    }

                } else if (command.equalsIgnoreCase("Q")) {
                    conn.close();
                    System.out.println("Goodbye User...");
                    System.exit(0);
                } else {
                    System.out.println("Unknown command! Please try again: ");
                    flag = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
