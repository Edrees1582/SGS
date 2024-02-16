package org.example.models;

import org.example.system_interfaces.InstructorInterface;
import org.example.users.User;
import org.example.users.UserType;

public class Instructor extends User {
    public Instructor(String id, String password, String name) {
        super(id, password, name, UserType.INSTRUCTOR, new InstructorInterface());
    }
}
