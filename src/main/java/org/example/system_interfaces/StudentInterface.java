package org.example.system_interfaces;

import org.example.models.Course;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class StudentInterface implements UserInterface, Serializable {
    @Override
    public void displayInterface(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String studentId) {
        try {
            System.out.println("Student Grading System (STUDENT):");
            Scanner scanner = new Scanner(System.in);

            int option;

            do {
                System.out.println("1. View enrolled courses");
                System.out.println("2. View course grade");
                System.out.println("3. View grades");
                System.out.println("4. Exit");

                System.out.print("Enter option: ");
                option = scanner.nextInt();

                dataOutputStream.writeInt(option);
                dataOutputStream.flush();

                switch (option) {
                    case 1:
                        System.out.println("Enrolled courses list:");
                        dataOutputStream.writeUTF(studentId);
                        break;
                    case 2:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        dataOutputStream.writeUTF(studentId);
                        break;
                    case 3:
                        dataOutputStream.writeUTF(studentId);
                        dataOutputStream.flush();
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option");
                }

                dataOutputStream.flush();

                System.out.println(dataInputStream.readUTF());
            } while (option != 4);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
