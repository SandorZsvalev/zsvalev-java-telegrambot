package org.telbot.telran.info.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUserTest() {
        String name = "Name";
        String role = "user";
        User entity = userService.createUser(name);
        assertTrue(userRepository.findById(entity.getId()).isPresent());
        assertEquals(name, userRepository.findById(entity.getId()).get().getName());
        assertEquals(role, userRepository.findById(entity.getId()).get().getRole());
    }

    @Test
    void getListOfAllUserTest() {
        User firstUser = new User();
        firstUser.setName("First");
        firstUser.setRole("user");
        User secondUser = new User();
        secondUser.setName("First");
        secondUser.setRole("user");
        List<User> userList = new ArrayList<>();
        userList.add(firstUser);
        userList.add(secondUser);
        userRepository.saveAll(userList);
        Assertions.assertEquals(userList.get(0).getName(), userService.list().get(0).getName());
    }

    @Test
    void getUserByIdExceptionTest() {
        NoUserFoundException thrown = Assertions.assertThrows(NoUserFoundException.class, () -> {
            userService.getUser(100);
        });
        assertEquals("No such user found", thrown.getMessage());

    }

    @Test
    void getUserByIdTest() throws NoUserFoundException {
        User userForSave = new User();
        userForSave.setName("Name");
        userForSave.setRole("user");
        User entity = userRepository.save(userForSave);
        User user = userService.getUser(entity.getId());
        assertEquals(entity.getName(), user.getName());

    }


    @Test
    void deleteUserById() {
        NoUserFoundException thrown = Assertions.assertThrows(NoUserFoundException.class, () -> {
            User userForSave = new User();
            userForSave.setName("Name");
            userForSave.setRole("user");
            User entity = userRepository.save(userForSave);
            userService.deleteUserById(entity.getId());
            userService.getUser(entity.getId());
        });
        assertEquals("No such user found", thrown.getMessage());
    }

    @Test
    void updateUser() throws NoUserFoundException {
        User userForSave = new User();
        userForSave.setName("Name");
        userForSave.setRole("user");
        User entity = userRepository.save(userForSave);
        assertTrue(userRepository.findById(entity.getId()).isPresent());
        User userForUpdate = userRepository.findById(entity.getId()).get();
        userForUpdate.setName("Second Name");
        userForUpdate.setRole("admin");
        User updatedUser = userService.updateUser(userForUpdate);
        assertEquals(entity.getId(), updatedUser.getId());
        assertEquals(userForUpdate.getName(), updatedUser.getName());
        assertEquals(userForUpdate.getRole(), updatedUser.getRole());
    }

    @Test
    void updateUserWithoutIdException() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            User userForSave = new User();
            userForSave.setName("Name");
            userForSave.setRole("user");
            userService.updateUser(userForSave);
        });
        assertEquals("There are only user with id can be updated", thrown.getMessage());
    }

    @Test
    void updateUserNoUserFoundException() {
        NoUserFoundException thrown = Assertions.assertThrows(NoUserFoundException.class, () -> {
            User userForSave = new User();
            userForSave.setName("Name");
            userForSave.setRole("user");
            userForSave.setId(100);
            userService.updateUser(userForSave);
        });
        assertEquals("No such user found", thrown.getMessage());
    }

    @Test
    void changeRoleWithWrongRoleException() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            User userForSave = new User();
            userForSave.setName("Name");
            userForSave.setRole("user");
            User entity = userRepository.save(userForSave);
            assertTrue(userRepository.findById(entity.getId()).isPresent());
            User userForChangeRole = userRepository.findById(entity.getId()).get();
            String roleNew = "kingOfGalactic";
            userService.changeRole(userForChangeRole, roleNew);
        });
        assertEquals("Wrong role", thrown.getMessage());
    }

    @Test
    void changeRoleToUser() throws NoUserFoundException {
        User userForSave = new User();
        userForSave.setName("Name");
        userForSave.setRole("admin");
        User entity = userRepository.save(userForSave);
        assertTrue(userRepository.findById(entity.getId()).isPresent());
        User userForChangeRole = userRepository.findById(entity.getId()).get();
        String roleNew = "USER";
        User updatedRoleUser = userService.changeRole(userForChangeRole, roleNew);
        assertEquals(roleNew, updatedRoleUser.getRole());
    }

    @Test
    void changeRoleToAdmin() throws NoUserFoundException {
        User userForSave = new User();
        userForSave.setName("Name");
        userForSave.setRole("user");
        User entity = userRepository.save(userForSave);
        assertTrue(userRepository.findById(entity.getId()).isPresent());
        User userForChangeRole = userRepository.findById(entity.getId()).get();
        String roleNew = "ADMINISTRATOR";
        User updatedRoleUser = userService.changeRole(userForChangeRole, roleNew);
        assertEquals(roleNew, updatedRoleUser.getRole());
    }
}