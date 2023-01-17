package org.telbot.telran.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelService channelService;
    // добавлять сюда - нормально
    // инжектим всегда именно сервис, а не репозитории или контроллеры
    // тк в сервисах все проверки и т.п.


    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(int id) {
        if(userRepository.findById(id).isPresent()){
        return userRepository.findById(id).get();
        } else {
            //add logger
            System.out.println("User not found");
            return null;
        }
    }

    @Override
    public User createUser(String userName) {
        User user = new User();
        user.setName(userName);
        User userEntity = userRepository.save(user);
        createUserDir(userEntity.getId());
        return userEntity;
    }

    @Override
    public void deleteUserById(int id) {
        User user = getUser(id);
        if (user!=null) {
            deleteUserDir(id);
            userRepository.delete(user);
        }
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    private void createUserDir(int userId){
        String dirName = "src/main/reports/reports_user_"+userId;
        try {
            Path directory = Files.createDirectory(Paths.get(dirName));
        } catch (IOException e) {
            e.printStackTrace();
            //add logger
        }
    }

    private void deleteUserDir(int userId){
        String dirName = "src/main/reports/reports_user_"+userId;
        try {
            Files.deleteIfExists(Paths.get(dirName));
        } catch (IOException e) {
            e.printStackTrace();
            //add logger
        }
    }

}
