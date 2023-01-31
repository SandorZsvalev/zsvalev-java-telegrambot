package org.telbot.telran.info.service;

import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.model.Event;

import java.util.List;

public interface EventService {

    void createEvent();

    List<Event> findAllNewEvent();

    List<Event> findAllNewEventByUserId(int userId) throws NoDataFoundException;

    void markListEventsAsRead(List<Event> sentEvents);

    int findLastEventId();

    List<Event> findAllEventByIdGreaterThan(int from);
}