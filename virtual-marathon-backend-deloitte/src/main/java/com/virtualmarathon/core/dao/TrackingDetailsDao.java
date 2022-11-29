package com.virtualmarathon.core.dao;

import com.virtualmarathon.core.entity.Event;
import com.virtualmarathon.core.entity.TrackingDetails;
import com.virtualmarathon.core.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingDetailsDao extends JpaRepository<TrackingDetails, Integer> {

    List<TrackingDetails> findByEventOrderByTimeAsc(Event event);

    List<TrackingDetails> findByParticipant(User participant);

    TrackingDetails findByParticipantAndEvent(User participant, Event event);

    void deleteByParticipantAndEvent(User participant, Event event);

}
