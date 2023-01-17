package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telbot.telran.info.model.UserChannel;

import java.util.List;

public interface UserChannelRepository extends JpaRepository<UserChannel,Integer> {

     List<UserChannel> findByUserId(int userId);

     UserChannel findByUserIdAndChannelId(int userId, int channelId);

}
