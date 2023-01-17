package org.telbot.telran.info.service;

import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.model.UserChannel;

import java.util.List;

public interface UserChannelService {

    List<UserChannel> list();

    List<UserChannel> listChannelForUser(int userId);

    UserChannel getUserChannel (int id);

    UserChannel createUserChannel(int userId, int ChannelId);

    void deleteUserChannel(int userId, int channelId);

}
