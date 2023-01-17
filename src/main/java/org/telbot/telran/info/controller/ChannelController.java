package org.telbot.telran.info.controller;

//1. Make as rest Controller
//2. Create methods
//3. Write annotation etc.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.service.ChannelService;

import java.util.List;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping
    public List<Channel> channelList (){
        return channelService.list();
    }

    @GetMapping ("/{id}")
    public Channel getChannelById(@PathVariable(name = "id") int id){
        return channelService.findById(id);
    }

    @PostMapping("/{name}")
    public Channel createChannel(@PathVariable(name = "name") String name){
        return channelService.create(name);
    }

    @PutMapping
    public Channel updateChannel (@RequestBody Channel channel){
        return channelService.update(channel);
    }

    @DeleteMapping("/{id}")
    public void deleteChannel (@PathVariable(name = "id") int id){
        channelService.deleteById(id);
    }



}
