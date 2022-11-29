package com.virtualmarathon.core.configuration;

import com.virtualmarathon.core.dao.EventDao;
import com.virtualmarathon.core.entity.Event;
import com.virtualmarathon.core.service.EventService;
import com.virtualmarathon.core.service.TrackingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private TrackingDetailsService trackingDetailsService;

    @Scheduled(cron = "${cron.expression}", zone="${time.zone}")
    public void updateEventStatus() throws MessagingException {
        Date currentDate = new Date();
        List<Event> events = eventService.showAllEvents(null, null);
        for (Event event: events) {
            Date startDate = event.getStartDate();
            Date endDate = event.getEndDate();
            if(currentDate.compareTo(endDate) == 1) {
                event.setStatus("closed");
            } else if (currentDate.compareTo(startDate) == -1) {
                event.setStatus("upcoming");
            } else {
                event.setStatus("open");
            }
            eventDao.save(event);
            trackingDetailsService.sendMailToTopPerformers(event.getId());
        }

    }

}
