package org.telbot.telran.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.repository.ChannelRepository;

import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    ChannelRepository channelRepository;

    @Override
    public Channel create(String name, long channelTlgId) {
        Channel channel = new Channel();
        channel.setChannelName(name);
        channel.setChannelTlgId(channelTlgId);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(String name) {
        long channelTlgId = 0;
        return create(name,channelTlgId);
    }

    @Override
    public List<Channel> list() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(Channel channel) {
        return channelRepository.save(channel);
    }

    @Override
    public void deleteById(int id) {
        Channel channel = findById(id);
        if (channel!=null) {
            channelRepository.delete(channel);
        }
    }

    @Override
    public Channel findById(int id) {
        if (channelRepository.findById(id).isPresent()){
            return channelRepository.findById(id).get();
        } else {
            //add logger
            System.out.println("not found");
            return null;
        }
    }

    @Override
    public Channel findByChannelTlgId(long channelTlgId) {
        if (channelRepository.findByChannelTlgId(channelTlgId).isPresent()){
            return channelRepository.findByChannelTlgId(channelTlgId).get();
        } else {
            System.out.println("channel not found");
            //add logger
            return null;
        }
    }

    @Override
    public String getChannelNameByChannelTlgId(long channelTlgId) {
        Channel foundChannel = findByChannelTlgId(channelTlgId);
        return foundChannel.getChannelName();
    }
}
