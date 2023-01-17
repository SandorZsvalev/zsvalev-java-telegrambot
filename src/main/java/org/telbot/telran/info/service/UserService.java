package org.telbot.telran.info.service;

//1. Made implements this interface
//2. Create methods


import org.telbot.telran.info.model.User;

import java.util.List;

public interface UserService {

    List<User> list();

    User getUser (int id);

    User createUser(String userName);

    User updateUser(User user);

    void deleteUserById(int id);
}
