package org.example.models;

import org.example.system_interfaces.AdminInterface;
import org.example.users.User;
import org.example.users.UserType;

public class Admin extends User {
    public Admin(String id, String password, String name) {
        super(id, password, name, UserType.ADMIN, new AdminInterface());
    }
}
