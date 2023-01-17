package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telbot.telran.info.model.TelegramChannelPost;

import java.util.List;

public interface TelegramChannelPostRepository extends JpaRepository<TelegramChannelPost,Integer> {

    List<TelegramChannelPost> findAllPostsByChannelTlgIdAndDateGreaterThan(long channelTlgId, int StartDate);
    List<TelegramChannelPost> findAllPostsByChannelTlgIdAndMessageIdGreaterThan(long channelTlgId, int StartId);
    TelegramChannelPost findPostByChannelTlgIdAndMessageId(long channelTlgId, int messageId);
    List<TelegramChannelPost> findAllByMessageIdGreaterThan(int StartId);
}
