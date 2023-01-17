package org.telbot.telran.info.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.Event;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.model.UserChannel;
import org.telbot.telran.info.service.EventService;
import org.telbot.telran.info.service.UserService;

import java.util.List;

//1. Make class as rest controller
//2. Implement methods

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @GetMapping
    public List<User> list(){
        return userService.list();
    }

    @GetMapping("/{id}")
    public User getUserById (@PathVariable(name = "id") int id){
        return userService.getUser(id);
    }

    @PostMapping("/{name}")
    public User createUser(@PathVariable(name = "name") String name){
        return userService.createUser(name);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable(name = "id") int id){
        userService.deleteUserById(id);
    }

    @PutMapping
    public User updateUser (@RequestBody User user){
        return userService.updateUser(user);
    }

    @GetMapping("/get_event/{userId}")
    public Event createEvent (@PathVariable(name = "userId") int userId){
        System.out.println("вызвали удачно");
        System.out.println("получили юзер айди "+userId);
        return eventService.makeNewEvent(userId);
    }
}
