package org.telbot.telran.info.service;

import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.exceptions.NoUserChannelException;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.UserChannel;

import java.util.List;

public interface UserChannelService {

    List<UserChannel> list();

    List<UserChannel> listUserChannelForUser(int userId);

    UserChannel getUserChannel(int id) throws NoUserChannelException;

    UserChannel createUserChannel(int userId, int ChannelId) throws NoUserFoundException, NoChannelFoundException;

    void deleteUserChannel(int userId, int channelId) throws NoUserChannelException;

    List<Channel> listActiveChannelByUserID(int userId) throws NoChannelFoundException;

    UserChannel unsubscribeUserFromChannel(int userID, int channelID) throws NoUserChannelException;

    List<UserChannel> listAllActiveUserChannel();

}
