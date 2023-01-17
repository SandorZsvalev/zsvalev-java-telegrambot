package org.telbot.telran.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.TelegramChannelPost;

@Service
public class TelegramBotServiceImpl implements TelegramBotService {

    @Autowired
    ChannelService channelService;

    @Override
    public boolean checkIfChannelIsPresent(long channelTlgId) {
        return channelService.findByChannelTlgId(channelTlgId) != null;

    }

    @Override
    public Channel addNewChannel(String name, long channelTlgId) {
        return channelService.create(name,channelTlgId);
    }

}
