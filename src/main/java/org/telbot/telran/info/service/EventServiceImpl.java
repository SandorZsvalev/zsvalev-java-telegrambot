package org.telbot.telran.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.Event;
import org.telbot.telran.info.model.ChannelPost;
import org.telbot.telran.info.model.UserChannel;
import org.telbot.telran.info.repository.EventRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
    @Autowired
    private UserChannelService userChannelService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelPostService channelPostService;
    @Autowired
    private EventRepository eventRepository;

    @Scheduled(fixedRateString = "${method.schedule_event}")
    @Override
    public void createEvent() {
        //берем список всех пользователей-подписок, которые активны
        List<UserChannel> activeUserChannelList = userChannelService.listAllActiveUserChannel();
        //берем список всех непрочитанных постов
        List<ChannelPost> unreadPosts;
        try {
            unreadPosts = channelPostService.findAllUnreadPost();
        } catch (NoDataFoundException e) {
            log.error("Creating events is failed. No new post found: "+e.getMessage());
            return;
        }

        //Получаем лист без повторов всех айди каналов, в которых есть непрочитанные посты
        List<Integer> allChannelIds = unreadPosts.stream().map(ChannelPost::getChannelInsideId).distinct().toList();
        //Из него делаем лист каналов целиком, в которых есть непрочитанные посты (чтобы извлечь оттуда их названия потом)
        List<Channel> allChannelList = null;
        try {
            allChannelList = channelService.findChannelsAllByIds(allChannelIds);
        } catch (NoChannelFoundException e) {
            log.error("Unable find list Channel by the List Ids" + e.getMessage() + e.ids.toString());
        }

        //делаем мапу - айди канала : имя канала
        Map<Integer, String> channelTitlesMap = allChannelList.stream()
                .collect(Collectors.toMap(Channel::getId, Channel::getChannelName));

        //получаем мапу для каждого юзера - лист с внтуренними айди каналов, на которые он подписан.
        Map<Integer, List<Integer>> mapUserIdChannelId = activeUserChannelList.stream()
                .collect(Collectors.groupingBy(UserChannel::getUserId,
                        Collectors.mapping(UserChannel::getChannelId,
                                Collectors.toList())));

        //получаем мапу для каждого айди канала - со списком сообщений этого канала
        Map<Integer, List<ChannelPost>> mapChannelIdPosts = unreadPosts.stream()
                .collect(Collectors.groupingBy(ChannelPost::getChannelInsideId));

        //для каждой пары - юзер/его список каналов
        mapUserIdChannelId.forEach((userId, channelsIds) -> {
            StringBuilder sb = new StringBuilder();
            //берем юзера и его список каналов и для каждого канала
            channelsIds.forEach(channelId -> {
                //получаем лист сообщений от этого канала (если такой канал есть среди новых публикаций)
                if (mapChannelIdPosts.containsKey(channelId)) {
                    List<ChannelPost> post = mapChannelIdPosts.get(channelId);
                    String channelTitle = channelTitlesMap.get(channelId);
                    String messagesFromChannel = makeMessageFormListOfPosts(post, channelTitle);
                    sb.append(messagesFromChannel);
                }
            });
            if(!sb.isEmpty()) {
                Event event = new Event();
                event.setUserId(userId);
                event.setDate(convertUnixToSDF(System.currentTimeMillis()));
                event.setMessage(sb.toString());
                event.setToSend(true);
                saveEvent(event);
            }
        });
        unreadPosts.forEach(msg -> msg.setUnread(false));
        channelPostService.saveAllPosts(unreadPosts);
    }

    private Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findAllNewEvent() {
        return eventRepository.findAllByToSendTrue();
    }

    @Override
    public List<Event> findAllNewEventByUserId(int userId) throws NoDataFoundException {
        if (eventRepository.findAllByToSendTrueAndUserId(userId).isEmpty()) {
            log.error("No new events found for user " + userId);
            throw new NoDataFoundException();
        }
        return eventRepository.findAllByToSendTrueAndUserId(userId);
    }

    @Override
    public void markListEventsAsRead(List<Event> sentEvents) {
        sentEvents.forEach(event -> event.setToSend(false));
        eventRepository.saveAll(sentEvents);
    }

    private String convertUnixToSDF(int date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date result = new Date(date * 1000L);
        return sdf.format(result);
    }

    private String convertUnixToSDF(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date result = new Date(date);
        return sdf.format(result);
    }

    private String makeMessageFormListOfPosts(List<ChannelPost> post, String channelTitle) {
        StringBuilder sb = new StringBuilder();
        post.forEach(telegramChannelPost -> {
            sb.append("Channel title: ")
                    .append(channelTitle)
                    .append("\n id post: ")
                    .append(telegramChannelPost.getMessageId())
                    .append("\n date of post: ")
                    .append(convertUnixToSDF(telegramChannelPost.getDate()))
                    .append("\n ")
                    .append(telegramChannelPost.getText())
                    .append("\n *** \n");
        });
        return sb.toString();
    }

    @Override
    public int findLastEventId() {
        return eventRepository.findLastId();
    }

    @Override
    public List<Event> findAllEventByIdGreaterThan(int from) {
        return eventRepository.findAllEventByIdGreaterThan(from);
    }
}
