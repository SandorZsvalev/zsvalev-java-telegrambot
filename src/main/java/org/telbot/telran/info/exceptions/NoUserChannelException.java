package org.telbot.telran.info.exceptions;

public class NoUserChannelException extends Exception {

    int userId;
    long channelId;


    public NoUserChannelException(int userId, long channelId) {
        super(BotException.NO_USER_CHANNEL_MESSAGE);
        this.userId = userId;
        this.channelId = channelId;
    }

    public NoUserChannelException(int userId) {
        super(BotException.NO_USER_CHANNEL_MESSAGE);
        this.userId = userId;
    }
}
