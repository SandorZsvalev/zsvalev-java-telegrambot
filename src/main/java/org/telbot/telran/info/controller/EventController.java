package org.telbot.telran.info.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.model.Event;
import org.telbot.telran.info.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> listAllUnsentEventByUserId(@PathVariable(name = "userId") int userid) {
        List<Event> allNewEventByUserId = null;
        try {
            allNewEventByUserId = eventService.findAllNewEventByUserId(userid);
            eventService.markListEventsAsRead(allNewEventByUserId);
            return new ResponseEntity<>(allNewEventByUserId, HttpStatus.OK);
        } catch (NoDataFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
