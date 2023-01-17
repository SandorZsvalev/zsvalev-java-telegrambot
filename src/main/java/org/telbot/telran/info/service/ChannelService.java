package org.telbot.telran.info.service;

import org.telbot.telran.info.model.Channel;

import java.util.List;

public interface ChannelService {

    Channel create (String name,long channelTlgId);
    Channel create (String name);
    List<Channel> list();
    Channel update(Channel channel);
    void deleteById (int id);
    Channel findById(int id);

    Channel findByChannelTlgId(long channelTlgId);

    String getChannelNameByChannelTlgId(long channelTlgId);

}
