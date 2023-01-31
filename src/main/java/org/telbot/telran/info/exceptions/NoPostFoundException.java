package org.telbot.telran.info.exceptions;

public class NoPostFoundException extends Exception{

    long channelTlgId;
    int messageId;

    public NoPostFoundException(long channelTlgId, int messageId) {
        super(BotException.NO_POST_FOUND_MESSAGE);
        this.channelTlgId = channelTlgId;
        this.messageId = messageId;
    }
}
