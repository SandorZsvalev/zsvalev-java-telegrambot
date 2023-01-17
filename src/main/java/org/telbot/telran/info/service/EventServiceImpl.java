package org.telbot.telran.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    UserChannelService userChannelService;

    @Autowired
    UserScheduleService userScheduleService;

    @Autowired
    ChannelService channelService;

    @Autowired
    TelegramChannelPostService telegramChannelPostService;


    @Override
    public Event makeNewEvent(int userId) {
        System.out.println(" метод заработал");
//        int fromTime = getTimePrevReportForUser(userId);
        int fromTime = 1673795686;
        List<Channel> channels = getListChannelsForUser(userId);
        List<TelegramChannelPost> allPosts = new ArrayList<>();
        for (Channel channel : channels) {
            long channelTlgId = channel.getChannelTlgId();
            List<TelegramChannelPost> postsByChannelTlgIdAndTime =
                    telegramChannelPostService.getPostsByChannelTlgIdAndTime(channelTlgId, fromTime);
            allPosts.addAll(postsByChannelTlgIdAndTime);
        }
        Event event = new Event();
        event.setReport(allPosts);
        writeEventFileForUser(event, userId);
        return event;
    }

    private int getTimePrevReportForUser(int userId) {
        return userScheduleService.getReportTime();
    }

    private List<Channel> getListChannelsForUser(int userId) {
        List<UserChannel> userChannelList = userChannelService.listChannelForUser(userId);
        List<Channel> channels = new ArrayList<>();
        for (UserChannel userChannel : userChannelList) {
            int channelId = userChannel.getChannelId();
            channels.add(channelService.findById(channelId));
        }
        return channels;
    }

    private File writeEventFileForUser(Event event, int userId) {
        List<TelegramChannelPost> report = event.getReport();
        String userDir = "src/main/reports/reports_user_" + userId;
        long unixTime = System.currentTimeMillis();
        String fileName = unixTime + "_" + userId;
        Path pathToFile = Paths.get(userDir + "/" + fileName);
        try {
            Path reportFile = Files.createFile(pathToFile);
            for (TelegramChannelPost post : report) {
                long channelTlgId = post.getChannelTlgId();
                String channelTitle = getChannelTitle(channelTlgId);
                int messageId = post.getMessageId();
                int date = post.getDate();
                String dateOfPublication = convertUnixToSDF(date);
                String text = post.getText();
                String header = "Channel title: " +
                        channelTitle + "\n id post: " +
                        messageId + "\n date of post: " +
                        dateOfPublication + "\n "
                        + text
                        + "\n *** \n";
                Files.writeString(reportFile, header, StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            e.printStackTrace();
            //add logger
        }


        return null;
    }

    private String convertUnixToSDF(int date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date result = new Date(date * 1000L);
        return sdf.format(result);
    }

    private String getChannelTitle(long channelTlgId) {
        return channelService.getChannelNameByChannelTlgId(channelTlgId);
    }

}
