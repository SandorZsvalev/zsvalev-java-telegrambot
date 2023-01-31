package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telbot.telran.info.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByToSendTrue();

    List<Event> findAllByToSendTrueAndUserId(int userId);

    @Query("SELECT max(id) from Event")
    int findLastId();

    List<Event> findAllEventByIdGreaterThan(int from);
}
