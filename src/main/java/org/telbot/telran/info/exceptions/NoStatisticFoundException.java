package org.telbot.telran.info.exceptions;

public class NoStatisticFoundException extends Exception{

    public NoStatisticFoundException() {
        super(BotException.NO_STATISTIC_FOUND_MESSAGE);
    }
}
