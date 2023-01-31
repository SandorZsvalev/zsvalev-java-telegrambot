package org.telbot.telran.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.repository.UserRepository;
import org.telbot.telran.info.roles.Roles;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(int id) throws NoUserFoundException {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            log.error("User not found");
            throw new NoUserFoundException(id);
        }
    }

    @Override
    public User createUser(String userName) {
        if (userName.isEmpty() || (userName.isBlank())) {
            log.error("Empty user name");
            throw new IllegalArgumentException("Empty user name");
        }
        User user = new User();
        user.setName(userName);
        user.setRole(Roles.USER.getName());
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) throws NoUserFoundException {
        User user = getUser(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public User updateUser(User user) throws NoUserFoundException, IllegalArgumentException {
        if (user.getId() == 0) {
            log.error("No user id included");
            throw new IllegalArgumentException("There are only user with id can be updated");
        }
        User oldVersion = userRepository.findById(user.getId())
                .orElseThrow(() -> new NoUserFoundException(user.getId()));
        oldVersion.setName(user.getName());
        oldVersion.setRole(user.getRole());
        return userRepository.save(oldVersion);
    }

    public User changeRole(User user, String role) throws NoUserFoundException, IllegalArgumentException {
        try {
            Roles.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wrong role");
        }
        user.setRole(role);
        return updateUser(user);
    }
}
