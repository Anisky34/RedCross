package com.example.redcross;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hour_requests")
public class HourRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @Column(nullable = false)
    private int hoursReqeusted;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Volunteer approvedBy;

    @Column
    private LocalDateTime responseDate;

    public HourRequest() {

    }

    public HourRequest(Volunteer volunteer, int hoursReqeusted) {
        this.volunteer = volunteer;
        this.hoursReqeusted = hoursReqeusted;
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
    public int getHoursReqeusted() {
        return hoursReqeusted;
    }
    public void setHoursReqeusted(int hoursReqeusted) {
        this.hoursReqeusted = hoursReqeusted;
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
    public void setStatus(String status)
    {
        this.status = status;
    }
    public Volunteer getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(Volunteer approvedBy) {
        this.approvedBy = approvedBy;
    }
    public LocalDateTime getResponseDate() {
        return responseDate;
    }
    public void setResponseDate(LocalDateTime requestDate) {
        this.responseDate = responseDate;
    }
}
