package com.virtualmarathon.core.dao;

import com.virtualmarathon.core.entity.Event;
import com.virtualmarathon.core.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDao extends JpaRepository<Event, Integer> {

    List<Event> findByTypeAndStatusOrderByStartDateDesc(String type, String status, Sort end_date);
    List<Event> findByTypeOrderByStartDateDesc(String type, Sort end_date);
    List<Event> findByStatusOrderByStartDateDesc(String status, Sort end_date);
    List<Event> findAllByOrderByStartDateDesc(Sort end_date);
    List<Event> findByOrganizer(User organizer);

}
