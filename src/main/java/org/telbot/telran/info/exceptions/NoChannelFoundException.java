package org.telbot.telran.info.exceptions;

import java.util.List;

public class NoChannelFoundException extends Exception{

    public int id;
    public List<Integer> ids;

    public NoChannelFoundException(int id) {
        super(BotException.NO_CHANNEL_FOUND_MESSAGE);
        this.id = id;
    }

    public NoChannelFoundException(List<Integer> ids) {
        super(BotException.NO_CHANNEL_FOUND_MESSAGE);
        this.ids = ids;
    }

}
