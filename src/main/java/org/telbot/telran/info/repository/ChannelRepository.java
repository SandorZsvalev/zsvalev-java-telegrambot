package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telbot.telran.info.model.Channel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer> {

    Optional<Channel> findByChannelTlgId(long channelTlgId);

    @Query("SELECT e FROM Channel e WHERE e.id IN :ids")
    List<Channel> findAllByIds(@Param("ids") List<Integer> ids);
}
