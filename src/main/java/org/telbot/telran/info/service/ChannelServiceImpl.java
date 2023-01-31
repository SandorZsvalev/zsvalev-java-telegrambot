package org.telbot.telran.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.repository.ChannelRepository;

import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    private Logger log = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public Channel create(String name, Long channelTlgId) throws IllegalArgumentException {
        if (name.isEmpty() || channelTlgId == 0) {
            log.error("create been called with wrong argument: name" + name + ", channelTlgId: " + channelTlgId);
            throw new IllegalArgumentException("Some values is null or empty");
        }
        Channel channel = new Channel();
        channel.setChannelName(name);
        channel.setChannelTlgId(channelTlgId);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(Channel channel) throws IllegalArgumentException {
        String name = channel.getChannelName();
        long channelTlgId = channel.getChannelTlgId();
        return create(name,channelTlgId);
    }

    @Override
    public List<Channel> list() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(Channel channel) throws IllegalArgumentException {
        String name = channel.getChannelName();
        long channelTlgId = channel.getChannelTlgId();
        if (name.isEmpty() || channelTlgId == 0) {
            log.error("create call with wrong argument: name" + name + ", channelTlgId: " + channelTlgId);
            throw new IllegalArgumentException("Some values is null or empty");
        }
        return channelRepository.save(channel);
    }

    @Override
    public void deleteById(int id) throws NoChannelFoundException {
        Channel channel = null;
        try {
            channel = findById(id);
        } catch (NoChannelFoundException e) {
            throw new NoChannelFoundException(id);
        }
        channelRepository.delete(channel);
    }

    @Override
    public Channel findById(int id) throws NoChannelFoundException {
        if (channelRepository.findById(id).isPresent()) {
            return channelRepository.findById(id).get();
        } else {
            log.error("Channel not found with id " + id);
            throw new NoChannelFoundException(id);
        }
    }

    @Override
    public Channel findByChannelTlgId(long channelTlgId) {
        if (channelRepository.findByChannelTlgId(channelTlgId).isPresent()) {
            return channelRepository.findByChannelTlgId(channelTlgId).get();
        } else {
            log.error("Channel not found with channelTlgId " + channelTlgId);
            throw new IllegalArgumentException("Such channel not found");
        }
    }

    @Override
    public List<Channel> findChannelsAllByIds(List<Integer> ids) throws NoChannelFoundException {
        if (channelRepository.findAllByIds(ids).isEmpty()) {
            throw new NoChannelFoundException(ids);
        }
        return channelRepository.findAllByIds(ids);
    }
}
