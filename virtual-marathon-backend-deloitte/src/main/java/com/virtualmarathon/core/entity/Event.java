package com.virtualmarathon.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @OneToOne(targetEntity = Image.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Lob
    private String description;

    @Column(name = "start_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @Column(name = "end_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    private Double distance;
    private String type;
    private String status;

    @Column(name = "faq")
    @ElementCollection
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "question", column = @Column(name = "Question")),
            @AttributeOverride(name = "answer", column = @Column(name = "Answer"))
    })
    private List<StringPair> faq;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "organizer_name")
    private User organizer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StringPair> getFaq() {
        return faq;
    }

    public void setFaq(List<StringPair> faq) {
        this.faq = faq;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

}
