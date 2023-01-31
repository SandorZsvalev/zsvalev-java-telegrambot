package org.telbot.telran.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.exceptions.NoUserChannelException;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.User;
import org.telbot.telran.info.model.UserChannel;
import org.telbot.telran.info.repository.UserChannelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserChannelServiceImpl implements UserChannelService {

    private Logger log = LoggerFactory.getLogger(UserChannelServiceImpl.class);
    @Autowired
    private UserChannelRepository userChannelRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;

    @Override
    public List<UserChannel> list() {
        return userChannelRepository.findAll();
    }

    @Override
    public UserChannel getUserChannel(int id) throws NoUserChannelException {
        if (userChannelRepository.findById(id).isPresent()) {
            return userChannelRepository.findById(id).get();
        } else {
            log.error("UserChannel not found");
            throw new NoUserChannelException(id);
        }
    }

    @Override
    public UserChannel createUserChannel(int userId, int channelId) throws NoUserFoundException, NoChannelFoundException {
        User user = userService.getUser(userId);
        Channel channel = channelService.findById(channelId);
        UserChannel userChannel = ifUserAlreadySubscribe(userId, channelId);
        userChannel.setUserId(user.getId());
        userChannel.setChannelId(channel.getId());
        userChannel.setActive(true);
        return userChannelRepository.save(userChannel);
    }

    @Override
    public UserChannel unsubscribeUserFromChannel(int userId, int channelId) throws NoUserChannelException {
        if (userChannelRepository.findByUserIdAndChannelId(userId, channelId).isPresent()) {
            UserChannel foundUserChannel = userChannelRepository.findByUserIdAndChannelId(userId, channelId).get();
            foundUserChannel.setActive(false);
            return userChannelRepository.save(foundUserChannel);
        } else {
            throw new NoUserChannelException(userId, channelId);
        }
    }

    @Override
    public void deleteUserChannel(int userId, int channelId) throws NoUserChannelException {
        if (userChannelRepository.findByUserIdAndChannelId(userId, channelId).isPresent()) {
            UserChannel entityToDelete = userChannelRepository.findByUserIdAndChannelId(userId, channelId).get();
            userChannelRepository.delete(entityToDelete);
        } else {
            throw new NoUserChannelException(userId, channelId);
        }
    }

    @Override
    public List<UserChannel> listUserChannelForUser(int userId) {
        return userChannelRepository.findByUserId(userId);
    }

    @Override
    //лист активных подписок каналов по айди юзера
    public List<Channel> listActiveChannelByUserID(int userId) throws NoChannelFoundException {
        List<UserChannel> byUserId = userChannelRepository.findByUserIdAndActiveTrue(userId);
        List<Integer> channelsId = byUserId.stream().map(UserChannel::getChannelId).collect(Collectors.toList());
        return channelService.findChannelsAllByIds(channelsId);
    }

    @Override
    public List<UserChannel> listAllActiveUserChannel() {
        return userChannelRepository.findAllByActiveTrue();
    }

    private UserChannel ifUserAlreadySubscribe(int userId, int channelId) {
        List<UserChannel> byUserId = userChannelRepository.findByUserId(userId);
        if (!byUserId.isEmpty()) {
            return byUserId.stream().filter(userChannel -> userChannel.getChannelId() == channelId)
                    .findAny()
                    .orElse(new UserChannel());
        } else {
            return new UserChannel();
        }
    }
}
