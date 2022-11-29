package com.virtualmarathon.core.service;

import com.virtualmarathon.core.dao.EventDao;
import com.virtualmarathon.core.dao.ImageDao;
import com.virtualmarathon.core.dao.UserDao;
import com.virtualmarathon.core.entity.Event;
import com.virtualmarathon.core.entity.Image;
import com.virtualmarathon.core.entity.StringPair;
import com.virtualmarathon.core.entity.User;
import com.virtualmarathon.core.exception.event.EventNotFoundException;
import com.virtualmarathon.core.exception.event.WrongEventOrganizerException;
import com.virtualmarathon.core.exception.event.WrongEventStatusException;
import com.virtualmarathon.core.exception.event.WrongEventTypeException;
import com.virtualmarathon.core.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private UserService userService;

    public Event createNewEvent(String userName, Long imageId, Event event) {
        User organizer = userDao.findById(userName).get();
        Image image = imageDao.findById(imageId).get();
        event.setOrganizer(organizer);
        event.setImage(image);
        return eventDao.save(event);
    }

    public List<Event> showAllEvents(String type, String status) {
        System.out.println(type);
        System.out.println(status);
        if(type == null && status == null) return eventDao.findAllByOrderByStartDateDesc(Sort.by("endDate").ascending());
        else if(status == null) return eventDao.findByTypeOrderByStartDateDesc(type, Sort.by("endDate").ascending());
        else if (type == null) return eventDao.findByStatusOrderByStartDateDesc(status, Sort.by("endDate").ascending());
        return eventDao.findByTypeAndStatusOrderByStartDateDesc(type, status, Sort.by("endDate").ascending());
    }

    public Event showEventById(Integer eventId) {
        Optional<Event> optionalEvent = eventDao.findById(eventId);
        if(optionalEvent.isPresent()) return optionalEvent.get();
        else throw new EventNotFoundException("Event not found for eventId: " + eventId);
    }

    public List<Event> showEventByOrganizer(String organizerName) {
        User organizer;
        Optional<User> optionalUser = userDao.findById(organizerName);
        if(optionalUser.isPresent()) organizer = optionalUser.get();
        else throw new UserNotFoundException("Organizer not found for username: " + organizerName);
        return eventDao.findByOrganizer(organizer);
    }

    public Event changeEventDetails(String userName, Integer eventId, Long imageId, Event updatedEvent) {
        if(eventDao.findById(eventId).isPresent()) {
            Event existingEvent = eventDao.findById(eventId).get();
            Image image = imageDao.findById(imageId).get();
            existingEvent.setTitle(updatedEvent.getTitle());
            existingEvent.setImage(image);
            existingEvent.setDescription(updatedEvent.getDescription());
            existingEvent.setStartDate(updatedEvent.getStartDate());
            existingEvent.setEndDate(updatedEvent.getEndDate());
            existingEvent.setDistance(updatedEvent.getDistance());
            if(!(updatedEvent.getType().equals("running") || updatedEvent.getType().equals("cycling"))) {
                throw new WrongEventTypeException("Wrong Event Type: " + updatedEvent.getType());
            }
            existingEvent.setType(updatedEvent.getType());
            if(!(updatedEvent.getStatus().equals("open") || updatedEvent.getStatus().equals("closed") || updatedEvent.getStatus().equals("upcoming"))) {
                throw new WrongEventStatusException("Wrong Event Status: " + updatedEvent.getStatus());
            }
            existingEvent.setStatus(updatedEvent.getStatus());
            existingEvent.setFaq(updatedEvent.getFaq());
            if(!existingEvent.getOrganizer().getUserName().equals(userName)) {
                throw new WrongEventOrganizerException("Wrong Event Organizer: " + userName);
            }
            return eventDao.save(existingEvent);
        } else {
            throw new EventNotFoundException("Event not found for eventId: " + eventId);
        }
    }

    public void removeEvent(String userName, Integer eventId) {
        if(eventDao.findById(eventId).isPresent()) {
            Event existingEvent = eventDao.findById(eventId).get();
            if(!existingEvent.getOrganizer().getUserName().equals(userName)) {
                throw new WrongEventOrganizerException("Wrong Event Organizer: " + userName);
            }
            eventDao.deleteById(eventId);
        } else {
            throw new EventNotFoundException("Event not found for eventId: " + eventId);
        }
    }

    public void removeAllEvents() {
        eventDao.deleteAll();
    }

    public void initEvent() {

        userService.initRoleAndUser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        User adminUser = userDao.findById("admin123").get();
        User organizer = userDao.findById("organizer123").get();

        List<StringPair> faq = new ArrayList<>();
        StringPair sp1 = new StringPair();
        sp1.setQuestion("What is the registration fees?");
        sp1.setAnswer("Registration is free.");
        StringPair sp2 = new StringPair();
        sp2.setQuestion("What are the support contact details ?");
        sp2.setAnswer("Support Number - +91 9611720505 & Email ID - support@mysamay.in");
        StringPair sp3 = new StringPair();
        sp3.setQuestion("How will be distance tracked?");
        sp3.setAnswer("To be decided.");
        faq.add(sp1);
        faq.add(sp2);
        faq.add(sp3);

        Image image1 = imageDao.findById(Long.valueOf(1)).get();
        Image image2 = imageDao.findById(Long.valueOf(2)).get();
        Image image3 = imageDao.findById(Long.valueOf(3)).get();
        Image image4 = imageDao.findById(Long.valueOf(4)).get();

        Event event1 = new Event();
        event1.setTitle("World wide Marathon Virtual Training Run 1");
        event1.setImage(image1);
        event1.setDescription("This is the World wide Marathon Virtual Training Run 1 to be held Virtually.");
        event1.setStartDate(sdf.parse("23/03/2022", new ParsePosition(0)));
        event1.setEndDate(sdf.parse("25/03/2022", new ParsePosition(0)));
        event1.setDistance(21.0);
        event1.setType("running");
        event1.setStatus("upcoming");
        event1.setFaq(faq);
        event1.setOrganizer(adminUser);

        Event event2 = new Event();
        event2.setTitle("AFLI NDM - LIVE (Charity)");
        event2.setImage(image2);
        event2.setDescription("New Delhi Marathon endorses this noble association with CHILDLINE India Foundation.");
        event2.setStartDate(sdf.parse("20/03/2022", new ParsePosition(0)));
        event2.setEndDate(sdf.parse("31/03/2022", new ParsePosition(0)));
        event2.setDistance(15.0);
        event2.setType("cycling");
        event2.setStatus("open");
        event2.setFaq(faq);
        event2.setOrganizer(organizer);


        Event event3 = new Event();
        event3.setTitle("AFLI NDM - Virtual");
        event3.setImage(image3);
        event3.setDescription("AFLI NDM is an annual running event with a full marathon distance, is also termed as the National Championship.");
        event3.setStartDate(sdf.parse("15/03/2022", new ParsePosition(0)));
        event3.setEndDate(sdf.parse("25/03/2022", new ParsePosition(0)));
        event3.setDistance(17.0);
        event3.setType("running");
        event3.setStatus("open");
        event3.setFaq(faq);
        event3.setOrganizer(adminUser);

        Event event4 = new Event();
        event4.setTitle("Runaissance 2022 - A Community Run");
        event4.setImage(image4);
        event4.setDescription("RUNaissance 2022 - A Community Run to restart (or start) your pursuit of fitness and running.");
        event4.setStartDate(sdf.parse("27/03/2022", new ParsePosition(0)));
        event4.setEndDate(sdf.parse("10/04/2022", new ParsePosition(0)));
        event4.setDistance(2.0);
        event4.setType("cycling");
        event4.setStatus("upcoming");
        event4.setFaq(faq);
        event4.setOrganizer(organizer);

        Event event5 = new Event();
        event5.setTitle("Around The World");
        event5.setImage(image1);
        event5.setDescription("Didn't we all successfully reach the moon and come back, even though virtually?");
        event5.setStartDate(sdf.parse("23/02/2022", new ParsePosition(0)));
        event5.setEndDate(sdf.parse("28/02/2022", new ParsePosition(0)));
        event5.setDistance(7.0);
        event5.setType("running");
        event5.setStatus("closed");
        event5.setFaq(faq);
        event5.setOrganizer(adminUser);

        eventDao.save(event1);
        eventDao.save(event2);
        eventDao.save(event3);
        eventDao.save(event4);
        eventDao.save(event5);

    }

}
