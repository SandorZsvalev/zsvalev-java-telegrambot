package org.telbot.telran.info.service;

import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.User;

import java.util.List;

public interface UserService {

    List<User> list();

    User getUser(int id) throws NoUserFoundException;

    User createUser(String userName);

    User updateUser(User user) throws NoUserFoundException;

    void deleteUserById(int id) throws NoUserFoundException;

    User changeRole(User user, String role) throws NoUserFoundException;
}
