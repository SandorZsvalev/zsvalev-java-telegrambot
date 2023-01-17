package org.telbot.telran.info.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.model.UserChannel;
import org.telbot.telran.info.service.UserChannelService;

import java.util.List;

@RestController
@RequestMapping("/user_channel")
public class UserChannelController {
    // list channels for user
    // subscribe User to Channel
    // unsubscribe User
    //

    @Autowired
    UserChannelService userChannelService;

    @GetMapping("/{userId}")
    public List<UserChannel> listChannelForUser(@PathVariable(name = "userId") int userid){
     return userChannelService.listChannelForUser(userid);
    }

    // subscribe User to Channel
    @PostMapping("/{userId},{channelId}")
    public UserChannel createUserChannel(@PathVariable(name = "userId") int userId, @PathVariable(name = "channelId") int channelId){
        return userChannelService.createUserChannel(userId, channelId);
    }

    // unsubscribe User
    @DeleteMapping("/{userId},{channelId}")
    public void deleteUserChannel (@PathVariable(name = "userId") int userId, @PathVariable(name = "channelId") int channelId){
        userChannelService.deleteUserChannel(userId, channelId);
    }


}
