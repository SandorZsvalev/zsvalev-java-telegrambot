package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telbot.telran.info.model.ChannelPost;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelPostRepository extends JpaRepository<ChannelPost, Integer> {

    List<ChannelPost> findAllPostsByChannelTlgIdAndDateGreaterThan(long channelTlgId, int StartDate);

    List<ChannelPost> findAllPostsByChannelTlgIdAndMessageIdGreaterThan(long channelTlgId, int StartId);

    Optional<ChannelPost> findPostByChannelTlgIdAndMessageId(long channelTlgId, int messageId);

    List<ChannelPost> findAllByMessageIdGreaterThan(int StartId);

    List<ChannelPost> findAllByUnreadTrue();
}
