package org.telbot.telran.info.service;

import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.model.Channel;

import java.util.List;

public interface ChannelService {

    Channel create(String name, Long channelTlgId) throws IllegalArgumentException;
    Channel create(Channel channel) throws IllegalArgumentException;

    List<Channel> list();

    Channel update(Channel channel);

    void deleteById(int id) throws NoChannelFoundException;

    Channel findById(int id) throws NoChannelFoundException;

    Channel findByChannelTlgId(long channelTlgId);

    List<Channel> findChannelsAllByIds(List<Integer> ids) throws NoChannelFoundException;
}
