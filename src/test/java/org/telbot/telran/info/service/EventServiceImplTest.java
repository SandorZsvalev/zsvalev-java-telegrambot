package org.telbot.telran.info.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.Event;
import org.telbot.telran.info.model.ChannelPost;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.repository.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class EventServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserChannelRepository userChannelRepository;
    @Autowired
    private UserChannelService userChannelService;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelPostRepository channelPostRepository;
    @Autowired
    private ChannelPostService channelPostService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;

    @BeforeEach
    void setUp() throws NoUserFoundException, NoChannelFoundException {
        User userFirst = userService.createUser("First user");
        User userSecond = userService.createUser("Second user");
        User userThird = userService.createUser("Third user");
        Channel channelFirst = channelService.create("First Channel", 1111111L);
        Channel channelSecond = channelService.create("Second Channel", 2222222L);
        Channel channelThird = channelService.create("Third Channel", 333333L);
        userChannelService.createUserChannel(userFirst.getId(), channelFirst.getId());
        userChannelService.createUserChannel(userFirst.getId(), channelSecond.getId());
        userChannelService.createUserChannel(userSecond.getId(), channelFirst.getId());
        userChannelService.createUserChannel(userSecond.getId(), channelSecond.getId());
        userChannelService.createUserChannel(userThird.getId(), channelThird.getId());
        channelPostService.createPost(channelFirst.getChannelTlgId(), 1, "first channel text one", 1);
        channelPostService.createPost(channelSecond.getChannelTlgId(), 1, "second channel text one", 2);
        channelPostService.createPost(channelFirst.getChannelTlgId(), 1, "first channel text two", 1);
        channelPostService.createPost(channelSecond.getChannelTlgId(), 1, "second channel text two", 2);
        channelPostService.createPost(channelThird.getChannelTlgId(), 1, "third channel text one", 3);
    }

    @AfterEach
    void cleanTables() {
        userRepository.deleteAll();
        channelRepository.deleteAll();
        userChannelRepository.deleteAll();
        channelPostRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    void createEventTest() throws NoDataFoundException {
        List<ChannelPost> allUnreadPost = channelPostService.findAllUnreadPost();
        assertEquals(5, allUnreadPost.size());
        eventService.createEvent();
        List<Event> allEventByToSendTrue = eventRepository.findAllByToSendTrue();
        assertEquals(3, allEventByToSendTrue.size());
        NoDataFoundException thrown = Assertions.assertThrows(NoDataFoundException.class, () -> {
            channelPostService.findAllUnreadPost();
        });
        assertEquals("No data found", thrown.getMessage());
        List<Integer> userIdList = allEventByToSendTrue.stream().map(Event::getUserId).distinct().toList();
        assertEquals(3, userIdList.size());
    }

    @Test
    void findAllNewEventTest() {
        eventService.createEvent();
        List<Event> allNewEvent = eventService.findAllNewEvent();
        assertEquals(3, allNewEvent.size());
    }

    @Test
    void findAllNewEventByUserId() throws NoDataFoundException {
        eventService.createEvent();
        List<User> allUsers = userRepository.findAll();
        List<Event> allNewEventByUserId = eventService.findAllNewEventByUserId(allUsers.get(0).getId());
        assertEquals("First user", allUsers.get(0).getName());
        assertEquals(1, allNewEventByUserId.size());
    }

    @Test
    void findAllNewEventByUserIdNoNewEventsException() throws NoDataFoundException {
        userService.createUser("Fourth user");
        eventService.createEvent();
        List<User> allUsers = userRepository.findAll();
        assertEquals("Fourth user", allUsers.get(3).getName());
        NoDataFoundException thrown = Assertions.assertThrows(NoDataFoundException.class, () -> {
            eventService.findAllNewEventByUserId(allUsers.get(3).getId());
        });
        assertEquals("No data found", thrown.getMessage());
    }

    @Test
    void markListEventsAsRead() {
        eventService.createEvent();
        List<Event> allByToSendTrue = eventRepository.findAllByToSendTrue();
        eventService.markListEventsAsRead(allByToSendTrue);
        List<Event> allByToSendTrueAfterMarked = eventRepository.findAllByToSendTrue();
        assertEquals(3, allByToSendTrue.size());
        assertTrue(allByToSendTrueAfterMarked.isEmpty());
    }

    @Test
    void findLastEventId() {
        eventService.createEvent();
        List<Event> allEvents = eventRepository.findAll();
        int maxId = allEvents.stream().mapToInt(Event::getId).max().getAsInt();
        assertTrue(allEvents.stream().mapToInt(Event::getId).max().isPresent());
        int lastEventId = eventService.findLastEventId();
        assertEquals(maxId, lastEventId);
    }

    @Test
    void findAllEventByIdGreaterThan() {
        eventService.createEvent();
        List<Event> allEventByIdGreaterThan = eventService.findAllEventByIdGreaterThan(0);
        assertEquals(3, allEventByIdGreaterThan.size());
    }
}