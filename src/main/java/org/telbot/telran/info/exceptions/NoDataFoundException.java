package org.telbot.telran.info.exceptions;

public class NoDataFoundException extends Exception{

    public NoDataFoundException() {
        super(BotException.NO_DATA_FOUND_MESSAGE);
    }
}
