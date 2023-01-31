package org.telbot.telran.info.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.exceptions.NoUserChannelException;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.model.UserChannel;
import org.telbot.telran.info.repository.ChannelRepository;
import org.telbot.telran.info.repository.UserChannelRepository;
import org.telbot.telran.info.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserChannelServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserChannelRepository userChannelRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserChannelService userChannelService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userChannelRepository.deleteAll();
        channelRepository.deleteAll();
    }

    @Test
    void getUserChannelByIdTest() throws NoUserChannelException {
        UserChannel userChannel = new UserChannel();
        userChannel.setChannelId(1);
        userChannel.setUserId(1);
        UserChannel savedEntity = userChannelRepository.save(userChannel);
        UserChannel foundUserChannel = userChannelService.getUserChannel(savedEntity.getId());
        Assertions.assertEquals(userChannel.getUserId(), foundUserChannel.getUserId());
        Assertions.assertEquals(userChannel.getChannelId(), foundUserChannel.getChannelId());
    }

    @Test
    void getUserChannelByIdNoUserChannelExceptionTest() {
        NoUserChannelException thrown = Assertions.assertThrows(NoUserChannelException.class, () -> {
            userChannelService.getUserChannel(100);
        });
        assertEquals("No such subscription found", thrown.getMessage());

    }

    @Test
    void createUserChannelTest() throws NoUserFoundException, NoChannelFoundException {
        User name = userService.createUser("name");
        Channel channel = channelService.create("Some Name", 1111111L);
        int userId = name.getId();
        int channelId = channel.getId();
        boolean active = true;
        UserChannel createdEntity = userChannelService.createUserChannel(userId, channelId);
        assertTrue(userChannelRepository.findById(createdEntity.getId()).isPresent());
        UserChannel fromDbById = userChannelRepository.findById(createdEntity.getId()).get();
        assertEquals(userId, fromDbById.getUserId());
        assertEquals(channelId, fromDbById.getChannelId());
        assertEquals(active, fromDbById.isActive());

    }

    @Test
    void createUserChannelWithNoUserExceptionTest() {
        NoUserFoundException thrown = Assertions.assertThrows(NoUserFoundException.class, () -> {
            Channel channel = channelService.create("Some Name", 1111111L);
            int userId = 1;
            int channelId = channel.getId();
            userChannelService.createUserChannel(userId, channelId);
        });
        assertEquals("No such user found", thrown.getMessage());
    }

    @Test
    void createUserChannelWithNoChannelExceptionTest() {
        NoChannelFoundException thrown = Assertions.assertThrows(NoChannelFoundException.class, () -> {
            User user = userService.createUser("name");
            int userId = user.getId();
            int channelId = 1;
            userChannelService.createUserChannel(userId, channelId);
        });
        assertEquals("No such channel found", thrown.getMessage());
    }

    @Test
    void unsubscribeUserFromChannelTest() throws NoUserChannelException {
        User user = userService.createUser("name");
        Channel channel = channelService.create("Some Name", 1111111L);
        UserChannel userChannel = new UserChannel();
        userChannel.setUserId(user.getId());
        userChannel.setChannelId(channel.getId());
        userChannel.setActive(true);
        UserChannel createdEntity = userChannelRepository.save(userChannel);
        UserChannel userChannelUnsubscribed = userChannelService.unsubscribeUserFromChannel(createdEntity.getUserId(), createdEntity.getChannelId());
        assertEquals(createdEntity.getId(), userChannelUnsubscribed.getId());
        assertNotEquals(createdEntity.isActive(), userChannelUnsubscribed.isActive());
    }

    @Test
    void unsubscribeUserFromChannelWithNoUserChannelExceptionTest() {
        NoUserChannelException thrown = Assertions.assertThrows(NoUserChannelException.class, () -> {
            User user = userService.createUser("name");
            Channel channel = channelService.create("Some Name", 1111111L);
            userChannelService.unsubscribeUserFromChannel(user.getId(), channel.getId());
        });
        assertEquals("No such subscription found", thrown.getMessage());
    }

    @Test
    void deleteUserChannelTest() throws NoUserChannelException {
        User user = userService.createUser("name");
        Channel channel = channelService.create("Some Name", 1111111L);
        UserChannel userChannel = new UserChannel();
        userChannel.setUserId(user.getId());
        userChannel.setChannelId(channel.getId());
        userChannel.setActive(true);
        UserChannel createdEntity = userChannelRepository.save(userChannel);
        userChannelService.deleteUserChannel(createdEntity.getUserId(), createdEntity.getChannelId());
        boolean ifDeleted = (userChannelRepository.findByUserIdAndChannelId(createdEntity.getUserId(), createdEntity.getChannelId()).isEmpty());
        assertTrue(ifDeleted);
    }

    @Test
    void deleteUserChannelWithNoUserChannelExceptionTest() {
        User user = userService.createUser("name");
        Channel channel = channelService.create("Some Name", 1111111L);
        NoUserChannelException thrown = Assertions.assertThrows(NoUserChannelException.class, () -> {
            userChannelService.deleteUserChannel(user.getId(), channel.getId());
        });
        assertEquals("No such subscription found", thrown.getMessage());
    }

    @Test
    void listUserChannelForUserTest() {
        User user = userService.createUser("name");
        Channel channel = channelService.create("First Name", 1111111L);
        Channel channelTwo = channelService.create("Second Name", 2222222L);
        List<UserChannel> userChannelList = new ArrayList<>();
        UserChannel userChannelOne = new UserChannel();
        userChannelOne.setUserId(user.getId());
        userChannelOne.setChannelId(channel.getId());
        userChannelOne.setActive(true);
        UserChannel createdFirstEntity = userChannelRepository.save(userChannelOne);
        userChannelList.add(createdFirstEntity);
        UserChannel userChannelTwo = new UserChannel();
        userChannelTwo.setUserId(user.getId());
        userChannelTwo.setChannelId(channelTwo.getId());
        userChannelTwo.setActive(true);
        UserChannel createdSecondEntity = userChannelRepository.save(userChannelTwo);
        userChannelList.add(createdSecondEntity);
        List<UserChannel> userChannelListFromDb = userChannelService.listUserChannelForUser(user.getId());
        assertEquals(userChannelList.size(), userChannelListFromDb.size());
        assertEquals(userChannelList.get(0).getChannelId(), userChannelListFromDb.get(0).getChannelId());
        assertEquals(userChannelList.get(1).getChannelId(), userChannelListFromDb.get(1).getChannelId());
    }

    @Test
    void listActiveChannelByUserID() throws NoChannelFoundException {
        User user = userService.createUser("name");
        Channel channel = channelService.create("First Name", 1111111L);
        Channel channelTwo = channelService.create("Second Name", 2222222L);
        UserChannel userChannelOne = new UserChannel();
        userChannelOne.setUserId(user.getId());
        userChannelOne.setChannelId(channel.getId());
        userChannelOne.setActive(true);
        userChannelRepository.save(userChannelOne);
        UserChannel userChannelTwo = new UserChannel();
        userChannelTwo.setUserId(user.getId());
        userChannelTwo.setChannelId(channelTwo.getId());
        userChannelTwo.setActive(false);
        userChannelRepository.save(userChannelTwo);
        List<Channel> listFoundActiveChannels = userChannelService.listActiveChannelByUserID(user.getId());
        assertEquals(1, listFoundActiveChannels.size());
        assertEquals("First Name", listFoundActiveChannels.get(0).getChannelName());
    }

    @Test
    void listAllActiveUserChannelTest() {
        User firstUser = userService.createUser("First user name");
        User secondUser = userService.createUser("Second User name");
        Channel channel = channelService.create("First channel Name", 1111111L);
        Channel channelTwo = channelService.create("Second channel Name", 2222222L);
        UserChannel userChannelOne = new UserChannel();
        userChannelOne.setUserId(firstUser.getId());
        userChannelOne.setChannelId(channel.getId());
        userChannelOne.setActive(true);
        userChannelRepository.save(userChannelOne);
        UserChannel userChannelTwo = new UserChannel();
        userChannelTwo.setUserId(secondUser.getId());
        userChannelTwo.setChannelId(channelTwo.getId());
        userChannelTwo.setActive(false);
        userChannelRepository.save(userChannelTwo);
        UserChannel userChannelThree = new UserChannel();
        userChannelThree.setUserId(secondUser.getId());
        userChannelThree.setChannelId(channel.getId());
        userChannelThree.setActive(true);
        userChannelRepository.save(userChannelThree);
        List<UserChannel> userChannelAllActiveList = userChannelService.listAllActiveUserChannel();
        assertEquals(2, userChannelAllActiveList.size());
    }
}