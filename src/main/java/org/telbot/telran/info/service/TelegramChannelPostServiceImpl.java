package org.telbot.telran.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.model.TelegramChannelPost;
import org.telbot.telran.info.repository.TelegramChannelPostRepository;

import java.util.List;

@Service
public class TelegramChannelPostServiceImpl implements TelegramChannelPostService {

    @Autowired
    TelegramChannelPostRepository telegramChannelPostRepository;

    @Override
    public TelegramChannelPost createPost(long channelTlgId, int messageId, String text, int date) {
        TelegramChannelPost newPost = new TelegramChannelPost();
        newPost.setChannelTlgId(channelTlgId);
        newPost.setMessageId(messageId);
        newPost.setText(text);
        newPost.setDate(date);
        return telegramChannelPostRepository.save(newPost);
    }

    @Override
    public TelegramChannelPost findPostByChannelTlgIdAndMessageId(long channelTlgId, int messageId) {
        return telegramChannelPostRepository.findPostByChannelTlgIdAndMessageId(channelTlgId,messageId);
    }

    @Override
    public List<TelegramChannelPost> getPostsByChannelTlgIdAndTime(long channelTlgId, int date) {

        return telegramChannelPostRepository.findAllPostsByChannelTlgIdAndDateGreaterThan(channelTlgId,date);
    }


    @Override
    public List<TelegramChannelPost> getPostsByChannelTlgIdAndMessageId(long channelTlgId, int messageId) {

        return telegramChannelPostRepository.findAllPostsByChannelTlgIdAndMessageIdGreaterThan(channelTlgId,messageId);
    }

    @Override
    public void updatePost(long channelTlgId, int messageId, String text, int date) {
        TelegramChannelPost postForUpdate = findPostByChannelTlgIdAndMessageId(channelTlgId,messageId);
        if (postForUpdate!=null) {
            postForUpdate.setText(text);
            telegramChannelPostRepository.save(postForUpdate);
        }
        //add logger
    }

    @Override
    public void deletePost(long channelTlgId, int messageId) {
        TelegramChannelPost postForDelete = findPostByChannelTlgIdAndMessageId(channelTlgId,messageId);
        if (postForDelete!=null) {
            telegramChannelPostRepository.delete(postForDelete);
        }
        // add logger
    }

    public List<TelegramChannelPost> testing(int startId){
        return telegramChannelPostRepository.findAllByMessageIdGreaterThan(startId);
    }
}
