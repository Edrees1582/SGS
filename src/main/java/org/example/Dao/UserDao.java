package org.example.Dao;

import org.example.users.UserType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

public interface UserDao<T> {
    T get(String id, UserType userType);
    List<T> getAll(UserType userType);
    void save(DataInputStream dataInputStream, DataOutputStream dataOutputStream, UserType userType);
    void update(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String id, UserType userType, int updateOption);
    void delete(String id, UserType userType);
}
