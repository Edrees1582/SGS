package org.example.system_interfaces;

import java.io.*;
import java.util.Scanner;

public class AdminInterface implements UserInterface, Serializable {
    @Override
    public void displayInterface(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String userId) {
        try {
            System.out.println("Student Grading System (ADMIN):");
            Scanner scanner = new Scanner(System.in);
            int option;

            do {
                System.out.println("1. Add user");
                System.out.println("2. View user");
                System.out.println("3. View users");
                System.out.println("4. Update user");
                System.out.println("5. Delete user");
                System.out.println("6. Add course");
                System.out.println("7. View course");
                System.out.println("8. View courses");
                System.out.println("9. Update course");
                System.out.println("10. Delete course");
                System.out.println("11. View student courses");
                System.out.println("12. View course students");
                System.out.println("13. Exit");

                System.out.print("Enter option: ");
                option = scanner.nextInt();

                dataOutputStream.writeInt(option);
                dataOutputStream.flush();

                switch (option) {
                    case 1:
                        System.out.print("Enter user type: ");
                        dataOutputStream.writeInt(scanner.nextInt());
                        System.out.print("Enter user id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter user password: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter user name: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 2:
                        System.out.print("Enter user type: ");
                        dataOutputStream.writeInt(scanner.nextInt());
                        System.out.print("Enter user id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 3:
                        System.out.print("Enter user type: ");
                        dataOutputStream.writeInt(scanner.nextInt());
                        break;
                    case 4:
                        System.out.print("Enter user type: ");
                        dataOutputStream.writeInt(scanner.nextInt());
                        System.out.print("Enter user id: ");
                        dataOutputStream.writeUTF(scanner.next());

                        dataOutputStream.flush();

                        System.out.println("Current user: ");
                        System.out.println(dataInputStream.readUTF());

                        System.out.println("What do you want to update?");

                        System.out.println("1. Id");
                        System.out.println("2. Password");
                        System.out.println("3. Name");

                        System.out.print("Enter an option: ");

                        int userUpdateOption = scanner.nextInt();
                        dataOutputStream.writeInt(userUpdateOption);
                        dataOutputStream.flush();

                        switch (userUpdateOption) {
                            case 1:
                                System.out.print("Enter new id: ");
                                dataOutputStream.writeUTF(scanner.next());
                                break;
                            case 2:
                                System.out.print("Enter new password: ");
                                dataOutputStream.writeUTF(scanner.next());
                                break;
                            case 3:
                                System.out.print("Enter new name: ");
                                dataOutputStream.writeUTF(scanner.next());
                                break;
                        }

                        dataOutputStream.flush();
                        break;
                    case 5:
                        System.out.print("Enter user type: ");
                        dataOutputStream.writeInt(scanner.nextInt());
                        System.out.print("Enter user id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 6:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter course title: ");
                        dataOutputStream.writeUTF(scanner.next());
                        System.out.print("Enter instructor id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 7:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 8:
                        System.out.println("Courses list:");
                        break;
                    case 9:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());

                        dataOutputStream.flush();

                        System.out.println("Current course: ");
                        System.out.println(dataInputStream.readUTF());

                        System.out.println("What do you want to update?");

                        System.out.println("1. Id");
                        System.out.println("2. Title");
                        System.out.println("3. Instructor id");

                        System.out.print("Enter an option: ");

                        int courseUpdateOption = scanner.nextInt();
                        dataOutputStream.writeInt(courseUpdateOption);
                        dataOutputStream.flush();

                        switch (courseUpdateOption) {
                            case 1:
                                System.out.print("Enter new id: ");
                                dataOutputStream.writeUTF(scanner.next());
                                break;
                            case 2:
                                System.out.print("Enter new title: ");
                                dataOutputStream.writeUTF(scanner.next());
                                break;
                            case 3:
                                System.out.print("Enter new instructor id: ");
                                dataOutputStream.writeUTF(scanner.next());
                                break;
                        }

                        dataOutputStream.flush();
                        break;
                    case 10:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 11:
                        System.out.print("Enter student id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 12:
                        System.out.print("Enter course id: ");
                        dataOutputStream.writeUTF(scanner.next());
                        break;
                    case 13:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println(("Invalid option"));
                }

                dataOutputStream.flush();

                System.out.println(dataInputStream.readUTF());
            } while (option != 13);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
