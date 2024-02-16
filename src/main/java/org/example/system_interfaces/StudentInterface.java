package org.example.system_interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class StudentInterface implements UserInterface, Serializable {
    @Override
    public void displayInterface(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String studentId) {
        try {
            System.out.println("Student Grading System (STUDENT):");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
