package org.telbot.telran.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.model.Channel;
import org.telbot.telran.info.model.UserChannel;
import org.telbot.telran.info.repository.ChannelRepository;
import org.telbot.telran.info.repository.UserChannelRepository;

import java.util.List;

@Service
public class UserChannelServiceImpl implements UserChannelService {

    @Autowired
    UserChannelRepository userChannelRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Override
    public List<UserChannel> list() {
        return userChannelRepository.findAll();
    }

    @Override
    public UserChannel getUserChannel(int id) {
        if(userChannelRepository.findById(id).isPresent()){
            return userChannelRepository.findById(id).get();
        } else {
            //add logger
            System.out.println("UserChannel not found");
            return null;
        }
    }

    @Override
    public UserChannel createUserChannel(int userId, int ChannelId) {
        UserChannel userChannel = new UserChannel();
        userChannel.setUserId(userId);
        userChannel.setChannelId(ChannelId);
        return userChannelRepository.save(userChannel);
    }

    @Override
    public void deleteUserChannel(int userId, int channelId) {
        UserChannel entityToDelete = userChannelRepository.findByUserIdAndChannelId(userId, channelId);
        userChannelRepository.delete(entityToDelete);
    }

    @Override
    public List<UserChannel> listChannelForUser(int userId) {
      return userChannelRepository.findByUserId(userId);
    }


}
