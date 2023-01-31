package org.telbot.telran.info.exceptions;

public class NoUserFoundException extends Exception{

    public int userId;

    public NoUserFoundException(int userId) {
        super(BotException.NO_USER_FOUND_MESSAGE);
        this.userId = userId;
    }
}
