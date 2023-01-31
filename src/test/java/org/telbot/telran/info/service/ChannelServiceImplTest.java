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
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.repository.ChannelRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ChannelServiceImplTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelService channelService;

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();
    }

    @Test
    void createChannelTest() {
        String channelName = "Channel name";
        Long channelTlgId = 1111L;
        Channel createdChannel = channelService.create(channelName, channelTlgId);
        assertTrue(channelRepository.findById(createdChannel.getId()).isPresent());
        Channel foundedChannel = channelRepository.findById(createdChannel.getId()).get();
        assertEquals(channelName, foundedChannel.getChannelName());
        assertEquals(channelTlgId, foundedChannel.getChannelTlgId());
    }

    @Test
    void createChannelWithoutNameExceptionTest() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            String channelName = "";
            Long channelTlgId = 1111L;
            channelService.create(channelName, channelTlgId);
        });
        assertEquals("Some values is null or empty", thrown.getMessage());
    }

    @Test
    void createChannelWithoutTlgIdExceptionTest() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            String channelName = "Channel Name";
            Long channelTlgId = 0L;
            channelService.create(channelName, channelTlgId);
        });
        assertEquals("Some values is null or empty", thrown.getMessage());
    }

    @Test
    void updateTest() {
        Channel channel = new Channel();
        String channelName = "Channel name";
        long channelTlgId = 1111L;
        channel.setChannelName(channelName);
        channel.setChannelTlgId(channelTlgId);
        Channel entityChannel = channelRepository.save(channel);
        entityChannel.setChannelName("new name");
        entityChannel.setChannelTlgId(2222L);
        Channel updatedChannel = channelService.update(entityChannel);
        assertEquals(entityChannel.getId(), updatedChannel.getId());
        assertEquals(entityChannel.getChannelName(), updatedChannel.getChannelName());
        assertEquals(entityChannel.getChannelTlgId(), updatedChannel.getChannelTlgId());
    }

    @Test
    void updateWithoutNameExceptionTest() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Channel channel = new Channel();
            String channelName = "Channel name";
            long channelTlgId = 1111L;
            channel.setChannelName(channelName);
            channel.setChannelTlgId(channelTlgId);
            Channel entityChannel = channelRepository.save(channel);
            entityChannel.setChannelName("");
            channelService.update(entityChannel);
        });
        assertEquals("Some values is null or empty", thrown.getMessage());
    }

    @Test
    void updateWithoutTlgIdExceptionTest() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Channel channel = new Channel();
            String channelName = "Channel name";
            long channelTlgId = 1111L;
            channel.setChannelName(channelName);
            channel.setChannelTlgId(channelTlgId);
            Channel entityChannel = channelRepository.save(channel);
            entityChannel.setChannelTlgId(0L);
            channelService.update(entityChannel);
        });
        assertEquals("Some values is null or empty", thrown.getMessage());
    }

    @Test
    void deleteByIdTest() throws NoChannelFoundException {
        Channel channel = new Channel();
        String channelName = "Channel name";
        long channelTlgId = 1111L;
        channel.setChannelName(channelName);
        channel.setChannelTlgId(channelTlgId);
        Channel entityChannel = channelRepository.save(channel);
        channelService.deleteById(entityChannel.getId());
        boolean ifDeleted = (channelRepository.findById(entityChannel.getId()).isEmpty());
        assertTrue(ifDeleted);
    }

    @Test
    void deleteByIdWithEmptyChannelTest() {
        NoChannelFoundException thrown = Assertions.assertThrows(NoChannelFoundException.class, () -> {
            channelService.deleteById(1);
        });
        assertEquals("No such channel found", thrown.getMessage());
    }

    @Test
    void findByIdTest() throws NoChannelFoundException {
        Channel channel = new Channel();
        String channelName = "Channel name";
        long channelTlgId = 1111L;
        channel.setChannelName(channelName);
        channel.setChannelTlgId(channelTlgId);
        Channel entityChannel = channelRepository.save(channel);
        Channel foundedChannel = channelService.findById(entityChannel.getId());
        assertEquals(channelName, foundedChannel.getChannelName());
        assertEquals(channelTlgId, foundedChannel.getChannelTlgId());
    }

    @Test
    void findByIdNotFoundExceptionTest() {
        NoChannelFoundException thrown = Assertions.assertThrows(NoChannelFoundException.class, () -> {
            channelService.findById(1);
        });
        assertEquals("No such channel found", thrown.getMessage());
    }

    @Test
    void findByChannelTlgIdTest() {
        Channel channel = new Channel();
        String channelName = "Channel name";
        long channelTlgId = 1111L;
        channel.setChannelName(channelName);
        channel.setChannelTlgId(channelTlgId);
        Channel entityChannel = channelRepository.save(channel);
        Channel foundedChannel = channelService.findByChannelTlgId(channelTlgId);
        assertEquals(entityChannel.getId(), foundedChannel.getId());
    }

    @Test
    void findByChannelTlgIdNotFoundExceptionTest() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            channelService.findByChannelTlgId(1L);
        });
        assertEquals("Such channel not found", thrown.getMessage());
    }


    @Test
    void findChannelsAllByIdsTest() throws NoChannelFoundException {
        String channelName = "Channel name";
        long channelTlgId = 1111L;
        Channel createdFirstChannel = channelService.create(channelName, channelTlgId);
        String channelSecondName = "Channel second name";
        long channelSecondTlgId = 2222L;
        Channel createdSecondChannel = channelService.create(channelSecondName, channelSecondTlgId);
        List<Channel> foundedChannelsList = channelService.findChannelsAllByIds(Arrays.asList(createdFirstChannel.getId(), createdSecondChannel.getId()));
        assertEquals(2, foundedChannelsList.size());
        assertEquals(createdFirstChannel.getId(), foundedChannelsList.get(0).getId());
        assertEquals(createdSecondChannel.getId(), foundedChannelsList.get(1).getId());
    }

    @Test
    void findChannelsAllByIdsNotFoundChannelsTest() {
        NoChannelFoundException thrown = Assertions.assertThrows(NoChannelFoundException.class, () -> {
            channelService.findChannelsAllByIds(Arrays.asList(1, 2));
        });
        assertEquals("No such channel found", thrown.getMessage());
    }

}