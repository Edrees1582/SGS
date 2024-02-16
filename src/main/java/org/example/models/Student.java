package org.example.models;

import org.example.system_interfaces.StudentInterface;
import org.example.users.User;
import org.example.users.UserType;

public class Student extends User {
    public Student(String id, String password, String name) {
        super(id, password, name, UserType.STUDENT, new StudentInterface());
    }
}
