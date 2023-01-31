package org.telbot.telran.info.service;

import org.telbot.telran.info.model.Channel;

public interface TelegramBotService {

    boolean checkIfChannelIsPresent(long channelTlgId);

    Channel addNewChannel(String name, long channelTlgId);

}
