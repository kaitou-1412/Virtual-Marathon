package com.virtualmarathon.core.controller;

import com.virtualmarathon.core.entity.TrackingDetails;
import com.virtualmarathon.core.service.TrackingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@CrossOrigin("${cors.urls}")
public class TrackingDetailsController {

    @Autowired
    private TrackingDetailsService trackingDetailsService;

    @PostMapping({"/{participantName}/{eventId}/addNewTrackingDetails"})
    @PreAuthorize("hasRole('participant')")
    public TrackingDetails addNewTrackingDetails(@PathVariable("participantName") String participantName, @PathVariable("eventId") Integer eventId, @RequestBody TrackingDetails trackingDetails) {
        return trackingDetailsService.addNewTrackingDetails(participantName, eventId, trackingDetails);
    }

    @GetMapping("/readTrackingDetails")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public List<TrackingDetails> readAllTrackingDetails() {
        return trackingDetailsService.showAllTrackingDetails();
    }

    @GetMapping("/readTrackingDetails/{trackingDetailsId}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public TrackingDetails readTrackingDetails(@PathVariable("trackingDetailsId") Integer trackingDetailsId) {
        return trackingDetailsService.showTrackingDetailsById(trackingDetailsId);
    }

    @GetMapping("/readTrackingDetailsByParticipantNameAndEventId/{participantName}/{eventId}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public TrackingDetails readTrackingDetailsByParticipantNameAndEventId(
            @PathVariable("participantName") String participantName,
            @PathVariable("eventId") Integer eventId
    ) {
        return trackingDetailsService.showTrackingDetailsByParticipantNameAndEventId(participantName, eventId);
    }

    @GetMapping("/readTrackingDetailsByEventId/{eventId}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public List<TrackingDetails> readTrackingDetailsByEventId(@PathVariable("eventId") Integer eventId) {
        return trackingDetailsService.showTrackingDetailsByEventId(eventId);
    }

    @GetMapping("/readTrackingDetailsByParticipantName/{participantName}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public List<TrackingDetails> readTrackingDetailsByParticipantName(@PathVariable("participantName") String participantName) {
        return trackingDetailsService.showTrackingDetailsByParticipantName(participantName);
    }

    @PutMapping("/{participantName}/{eventId}/updateTrackingDetails")
    @PreAuthorize("hasRole('participant')")
    public TrackingDetails updateTrackingDetails(@PathVariable("participantName") String participantName, @PathVariable("eventId") Integer eventId, @RequestBody TrackingDetails trackingDetails) {
        return trackingDetailsService.changeTrackingDetails(participantName, eventId, trackingDetails);
    }

    @DeleteMapping("/{participantName}/{eventId}/deleteTrackingDetails")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public void deleteTrackingDetails(@PathVariable("participantName") String participantName, @PathVariable("eventId") Integer eventId) {
        trackingDetailsService.removeTrackingDetails(participantName, eventId);
    }

    @DeleteMapping("/deleteAllTrackingDetails")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public void deleteAllTrackingDetails() { trackingDetailsService.removeAllTrackingDetails(); }

}
