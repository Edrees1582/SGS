package org.example.client;

import org.example.users.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            final int PORT = 8328;
            final String HOST = "localhost";
            Socket clientSocket = new Socket(HOST, PORT);

            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter id: ");
            dataOutputStream.writeUTF(scanner.next());
            System.out.print("Enter password: ");
            dataOutputStream.writeUTF(scanner.next());
            dataOutputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            boolean isAuthenticated = dataInputStream.readBoolean();

            if (isAuthenticated) {
                User user = (User) objectInputStream.readObject();
                user.displayInterface(dataInputStream, dataOutputStream);
            }
            else {
                System.out.println("Incorrect credentials");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
