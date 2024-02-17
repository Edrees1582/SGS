package org.example.system_interfaces;

import java.io.*;
import java.util.Scanner;

public class InstructorInterface implements UserInterface, Serializable {
    @Override
    public void displayInterface(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String instructorId) {
        try {
            System.out.println("Student Grading System (INSTRUCTOR):");
            Scanner scanner = new Scanner(System.in);
            
            int option;

            do {
                System.out.println("1. Enroll student");
                System.out.println("2. Un-enroll student");
                System.out.println("3. View course students");
                System.out.println("4. Add grade");
                System.out.println("5. View grade");
                System.out.println("6. Update grade");
                System.out.println("7. Delete grade");
                System.out.println("8. Exit");

                System.out.print("Enter option: ");
                option = scanner.nextInt();

                dataOutputStream.writeInt(option);
                dataOutputStream.flush();

                switch (option) {
                    case 1:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter student id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 2:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter student id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 3:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 4:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter student id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter grade: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 5:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter student id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 6:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter student id: ");
                        dataOutputStream.writeUTF(scanner.next());

                        dataOutputStream.flush();

                        System.out.println("Current grade: ");
                        System.out.println(dataInputStream.readUTF());

                        System.out.print("Enter new grade: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 7:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter student id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 8:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println(("Invalid option"));
                }

                dataOutputStream.flush();

                System.out.println("Client: " + dataInputStream.readUTF());
            } while (option != 8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
