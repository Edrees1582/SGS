package org.example.users;

import org.example.system_interfaces.UserInterface;

import java.io.*;

public class User implements Serializable {
    private final String id;
    private final String password;
    private final String name;
    private final UserType userType;
    private final UserInterface userInterface;

    public User(String id, String password, String name, UserType userType, UserInterface userInterface) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.userType = userType;
        this.userInterface = userInterface;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void displayInterface(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        userInterface.displayInterface(dataInputStream, dataOutputStream, id);
    }

    @Override
    public String toString() {
        return "+------------+----------------------+------------+\n" +
                String.format("| %-10s | %-20s | %-10s |\n", "ID", "Name", "User type") +
                "+------------+----------------------+------------+\n";
    }
}
