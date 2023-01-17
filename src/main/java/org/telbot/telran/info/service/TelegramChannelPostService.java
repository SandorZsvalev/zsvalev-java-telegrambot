package org.telbot.telran.info.service;

import org.telbot.telran.info.model.TelegramChannelPost;

import java.util.List;

public interface TelegramChannelPostService {

    TelegramChannelPost createPost(long channelTlgId, int messageId, String text, int date);
    List<TelegramChannelPost> getPostsByChannelTlgIdAndTime(long channelTlgId, int date);
    List<TelegramChannelPost> getPostsByChannelTlgIdAndMessageId(long channelTlgId, int messageId);
    TelegramChannelPost findPostByChannelTlgIdAndMessageId(long channelTlgId, int messageId);
    void updatePost(long channelTlgId, int messageId, String text, int date);
    void deletePost(long channelTlgId, int messageId);
List<TelegramChannelPost> testing (int messageId);

}
