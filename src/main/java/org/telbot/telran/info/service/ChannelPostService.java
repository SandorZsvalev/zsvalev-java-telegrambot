package org.telbot.telran.info.service;

import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.model.ChannelPost;

import java.util.List;

public interface ChannelPostService {

    ChannelPost createPost(long channelTlgId, int messageId, String text, int date);

    List<ChannelPost> findAllUnreadPost() throws NoDataFoundException;

    void saveAllPosts(List<ChannelPost> posts);

}
