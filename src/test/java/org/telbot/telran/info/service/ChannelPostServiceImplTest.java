package org.telbot.telran.info.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.ChannelPost;
import org.telbot.telran.info.repository.ChannelRepository;
import org.telbot.telran.info.repository.ChannelPostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ChannelPostServiceImplTest {

    @Autowired
    ChannelPostService channelPostService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelPostRepository channelPostRepository;

    @BeforeEach
    void setUp() {
        channelPostRepository.deleteAll();
        channelRepository.deleteAll();
    }

    @Test
    void createPostTest() {
        Channel channel = channelService.create("first channel", 1111L);
        ChannelPost newPost = new ChannelPost();
        newPost.setChannelTlgId(channel.getChannelTlgId());
        newPost.setMessageId(1);
        newPost.setText("new message");
        newPost.setDate(2222);
        newPost.setUnread(true);
        newPost.setChannelInsideId(3333);
        ChannelPost savedPost = channelPostService.createPost(
                newPost.getChannelTlgId(),
                newPost.getMessageId(),
                newPost.getText(),
                newPost.getDate());
        assertTrue(channelPostRepository.findById(savedPost.getId()).isPresent());
        ChannelPost entityPost = channelPostRepository.findById(savedPost.getId()).get();
        assertEquals(newPost.getChannelTlgId(), entityPost.getChannelTlgId());
        assertEquals(newPost.getText(), entityPost.getText());
        assertEquals(newPost.getMessageId(), entityPost.getMessageId());
        assertEquals(newPost.isUnread(), entityPost.isUnread());
    }

    @Test
    void findAllUnreadPostTest() throws NoDataFoundException {
        Channel channel = channelService.create("first channel", 1111L);
        ChannelPost newPost = new ChannelPost();
        newPost.setChannelTlgId(channel.getChannelTlgId());
        newPost.setMessageId(1);
        newPost.setText("new message");
        newPost.setDate(1);
        newPost.setUnread(false);
        newPost.setChannelInsideId(1);
        channelPostRepository.save(newPost);
        channelPostService.createPost(1111L, 2, "aaa", 2);
        channelPostService.createPost(1111L, 3, "bbb", 3);
        List<ChannelPost> allUnreadPost = channelPostService.findAllUnreadPost();
        assertEquals(2, allUnreadPost.size());
    }

    @Test
    void findAllUnreadPostNoFoundExceptionTest() {
        NoDataFoundException thrown = Assertions.assertThrows(NoDataFoundException.class, () -> {
            Channel channel = channelService.create("first channel", 1111L);
            ChannelPost newPost = new ChannelPost();
            newPost.setChannelTlgId(channel.getChannelTlgId());
            newPost.setMessageId(1);
            newPost.setText("new message");
            newPost.setDate(1);
            newPost.setUnread(false);
            newPost.setChannelInsideId(1);
            channelPostRepository.save(newPost);
            channelPostService.findAllUnreadPost();
        });
        assertEquals("No data found", thrown.getMessage());
    }

}