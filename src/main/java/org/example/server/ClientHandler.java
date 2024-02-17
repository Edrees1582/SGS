package org.example.server;

import org.example.util.Authentication;
import org.example.users.User;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            int chosenUserType = dataInputStream.readInt();

            String userId = dataInputStream.readUTF();
            String userPassword = dataInputStream.readUTF();

            Authentication authentication = new Authentication();

            User user = authentication.authenticateUser(chosenUserType, userId, userPassword);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            if (user != null) {
                dataOutputStream.writeBoolean(true);
                System.out.println(user.getUserType() + ": " + user.getName() + " logged in");
                objectOutputStream.writeObject(user);
                dataOutputStream.flush();
            }
            else {
                dataOutputStream.writeBoolean(false);
                clientSocket.close();
                return;
            }

            switch (chosenUserType) {
                case 1:
                    AdminClientHandler adminClientHandler = new AdminClientHandler(clientSocket);
                    adminClientHandler.runHandler();
                    break;
                case 2:
                    InstructorClientHandler instructorClientHandler = new InstructorClientHandler(clientSocket);
                    instructorClientHandler.runHandler();
                    break;
                case 3:
                    StudentClientHandler studentClientHandler = new StudentClientHandler(clientSocket);
                    studentClientHandler.runHandler();
                    break;
                default:
                    System.out.println("Invalid user type");
            }

            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
