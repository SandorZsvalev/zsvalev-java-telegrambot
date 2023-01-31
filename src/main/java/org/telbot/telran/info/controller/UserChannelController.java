package org.telbot.telran.info.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.exceptions.NoUserChannelException;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.service.UserChannelService;

@RestController
@RequestMapping("/user_channel")
public class UserChannelController {

    @Autowired
    UserChannelService userChannelService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> listChannelForUser(@PathVariable(name = "userId") int userid) {
        try {
            return new ResponseEntity<>(userChannelService.listActiveChannelByUserID(userid), HttpStatus.OK);
        } catch (NoChannelFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // subscribe User to Channel
    @PostMapping("/{userId},{channelId}")
    public ResponseEntity<?> createUserChannel(@PathVariable(name = "userId") int userId, @PathVariable(name = "channelId") int channelId) {
        try {
            return new ResponseEntity<>(userChannelService.createUserChannel(userId, channelId), HttpStatus.OK);
        } catch (NoUserFoundException | NoChannelFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // unsubscribe User
    @DeleteMapping("/{userId},{channelId}")
    public ResponseEntity<?> deleteUserChannel(@PathVariable(name = "userId") int userId, @PathVariable(name = "channelId") int channelId) {
        try {
            userChannelService.deleteUserChannel(userId, channelId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoUserChannelException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // unsubscribe User
    @PutMapping("/{userId},{channelId}")
    public ResponseEntity<?> unsubscribeUserChannel(@PathVariable(name = "userId") int userId, @PathVariable(name = "channelId") int channelId) {
        try {
            return new ResponseEntity<>(userChannelService.unsubscribeUserFromChannel(userId, channelId), HttpStatus.OK);
        } catch (NoUserChannelException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
