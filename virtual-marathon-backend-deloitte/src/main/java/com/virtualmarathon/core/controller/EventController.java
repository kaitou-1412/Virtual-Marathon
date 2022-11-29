package com.virtualmarathon.core.controller;

import com.virtualmarathon.core.entity.Event;
import com.virtualmarathon.core.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@CrossOrigin("${cors.urls}")
public class EventController {

    @Autowired
    private EventService eventService;

//    @PostConstruct
//    public void initEvent() { eventService.initEvent(); }

    @PostMapping({"/{organizerName}/createEvent/{imageId}"})
    @PreAuthorize("hasRole('organizer')")
    public Event createEvent(@PathVariable("organizerName") String organizerName, @PathVariable("imageId") Long imageId, @RequestBody Event event) {
        return eventService.createNewEvent(organizerName, imageId, event);
    }

    @GetMapping("/readEvents")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public List<Event> readAllEvent(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "status", required = false) String status
            ) {
        return eventService.showAllEvents(type, status);
    }

    @GetMapping("/readEvent/{eventId}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public Event readEvent(@PathVariable("eventId") Integer eventId) {
        return eventService.showEventById(eventId);
    }

    @GetMapping("/readEventsByOrganizerName/{organizerName}")
    @PreAuthorize("hasRole('organizer')")
    public List<Event> readEventsByOrganizerName(@PathVariable("organizerName") String organizerName) {
        return eventService.showEventByOrganizer(organizerName);
    }

    @PutMapping("/{organizerName}/updateEvent/{eventId}/{imageId}")
    @PreAuthorize("hasRole('organizer')")
    public Event updateEvent(@PathVariable("organizerName") String organizerName, @PathVariable("eventId") Integer eventId, @PathVariable("imageId") Long imageId, @RequestBody Event event) {
        return eventService.changeEventDetails(organizerName, eventId, imageId, event);
    }

    @DeleteMapping("/{organizerName}/deleteEvent/{eventId}")
    @PreAuthorize("hasRole('organizer')")
    public void deleteEvent(@PathVariable("organizerName") String organizerName, @PathVariable("eventId") Integer eventId) {
        eventService.removeEvent(organizerName, eventId);
    }

    @DeleteMapping("/deleteAllEvents")
    @PreAuthorize("hasRole('organizer')")
    public void deleteAllEvents() { eventService.removeAllEvents(); }

}
