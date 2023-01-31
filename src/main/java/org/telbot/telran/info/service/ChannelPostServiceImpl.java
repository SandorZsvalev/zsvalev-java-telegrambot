package org.telbot.telran.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.model.ChannelPost;
import org.telbot.telran.info.repository.ChannelPostRepository;

import java.util.List;

@Service
public class ChannelPostServiceImpl implements ChannelPostService {

    private Logger log = LoggerFactory.getLogger(ChannelPostServiceImpl.class);
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelPostRepository channelPostRepository;

    @Override
    public ChannelPost createPost(long channelTlgId, int messageId, String text, int date) {
        int channelInsideId = channelService.findByChannelTlgId(channelTlgId).getId();
        ChannelPost newPost = new ChannelPost();
        newPost.setChannelTlgId(channelTlgId);
        newPost.setMessageId(messageId);
        newPost.setText(text);
        newPost.setDate(date);
        newPost.setUnread(true);
        newPost.setChannelInsideId(channelInsideId);
        return channelPostRepository.save(newPost);
    }

    @Override
    public List<ChannelPost> findAllUnreadPost() throws NoDataFoundException {
        if (channelPostRepository.findAllByUnreadTrue().size() > 0) {
            return channelPostRepository.findAllByUnreadTrue();
        } else {
            log.error("No unread posts found in channel post service");
            throw new NoDataFoundException();
        }
    }

    public void saveAllPosts(List<ChannelPost> posts) {
        channelPostRepository.saveAll(posts);
    }
}
