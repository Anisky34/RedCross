package com.example.redcross;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import  java.time.LocalDateTime;

@Entity
@Table(name = "help_requests")

public class HelpRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "handled_by")
    private Volunteer handledBy;

    @Column
    private String response;

    @Column
    private LocalDateTime responseDate;

    @Column
    private String message;


    public HelpRequest() {

    }

    public HelpRequest(Volunteer volunteer, String message) {
        this.volunteer = volunteer;
        this.message = message;
        this.requestDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Volunteer getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(Volunteer handledBy) {
        this.handledBy = handledBy;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}

