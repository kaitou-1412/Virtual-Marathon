package com.virtualmarathon.core.entity;

import javax.persistence.*;

@Entity
@Table(
        name = "tracking_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"participant_name", "event_id"})
        }
)
public class TrackingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "participant_name")
    private User participant;

    @ManyToOne(targetEntity = Event.class)
    @JoinColumn(name = "event_id")
    private Event event;

    private Double time;
    private Double distance;
    private Double speed;

    @Column(name = "has_completed_event")
    private Boolean hasCompletedEvent;

    private Integer difficulty;
    private Integer satisfaction;
    private Integer ease;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Boolean getHasCompletedEvent() {
        return hasCompletedEvent;
    }

    public void setHasCompletedEvent(Boolean hasCompletedEvent) {
        this.hasCompletedEvent = hasCompletedEvent;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Integer getEase() {
        return ease;
    }

    public void setEase(Integer ease) {
        this.ease = ease;
    }

}
