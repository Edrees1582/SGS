package org.example.dao;

import org.example.users.User;
import org.example.users.UserType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

public interface UserDao<T> {
    T get(String id);
    List<T> getAll();
    List<User> getStudents();
    List<User> getInstructors();
    void save(DataInputStream dataInputStream, DataOutputStream dataOutputStream, UserType userType);
    void update(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String id, int updateOption);
    void delete(String id);
}
