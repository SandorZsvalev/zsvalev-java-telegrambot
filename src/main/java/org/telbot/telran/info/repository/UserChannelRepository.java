package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telbot.telran.info.model.UserChannel;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChannelRepository extends JpaRepository<UserChannel, Integer> {

    List<UserChannel> findByUserId(int userId);

    Optional<UserChannel> findByUserIdAndChannelId(int userId, int channelId);

    List<UserChannel> findByUserIdAndActiveTrue(int userId);

    List<UserChannel> findAllByActiveTrue();
}
