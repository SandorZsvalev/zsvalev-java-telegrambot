package org.telbot.telran.info.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.service.ChannelService;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping
    public ResponseEntity<?> channelList() {
        return new ResponseEntity<>(channelService.list(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChannelById(@PathVariable(name = "id") int id) {
        try {
            return new ResponseEntity<>(channelService.findById(id), HttpStatus.OK);
        } catch (NoChannelFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() + " id: " + e.id);
        }
    }

    @PostMapping
    public ResponseEntity<?> createChannel(@RequestBody Channel channel) {
        try {
            return new ResponseEntity<>(channelService.create(channel), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateChannel(@RequestBody Channel channel) {
        try {
            return new ResponseEntity<>(channelService.update(channel), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChannel(@PathVariable(name = "id") int id) {
        try {
            channelService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoChannelFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
