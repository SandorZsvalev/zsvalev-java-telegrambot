package org.telbot.telran.info.service;

import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.TelegramChannelPost;

public interface TelegramBotService {

    boolean checkIfChannelIsPresent(long channelTlgId);
    Channel addNewChannel(String name,long channelTlgId);

}
