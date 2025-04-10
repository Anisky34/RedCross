package com.example.redcross;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
@Service
public class VolunteerService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private HelpRequestRepository helpRequestRepository;
    @Autowired
    private HourRequestRepository hourRequestRepository;

    @Autowired
    NotificationService notificationService;
    public List<Volunteer> getHillcrestEvents() {
        return eventRepository.findByType("Hillcrest");
    }
    public List<Volunteer> getRedCrossEvents() {
        return eventRepository.findByType("Red Cross");
    }
    public void sendHelpRequest(String email, String message) {
        Volunteer volunteer = findByEmail(email);
        if (volunteer != null) {
            HelpRequest helpRequest = new HelpRequest(volunteer, message);
            helpRequestRepository.save(helpRequest);
            String subject = "Volunteer Help Request";
            String messageBody = "Volunteer with email " + email + " has requested assistance.\n\nMessage: " + message;

            notificationService.sendAdminNotification(subject, messageBody);
        }
    }
    public Volunteer findByEmail(String email) {
        return volunteerRepository.findByEmail(email);
    }
    public String authenticate_account(String email, String password) {
        Volunteer volunteer = findByEmail(email);
        if (volunteer != null && volunteer.getPassword().equals(password)) {
            return volunteer.getRole();
        }
        return  null;
    }
    public void saveVolunteer(Volunteer volunteer) {
        volunteerRepository.save(volunteer);
    }
    @Transactional
    public void deleteVolunteer(Volunteer volunteer) {
        List<HourRequest> approvedRequests = hourRequestRepository.findByApprovedById(volunteer.getId());
        for (HourRequest request : approvedRequests) {
            request.setApprovedBy(null);
            hourRequestRepository.save(request);
        }
        List<HelpRequest> handledRequests = helpRequestRepository.findByHandledById(volunteer.getId());
        for (HelpRequest request : handledRequests) {
            request.setHandledBy(null);
            helpRequestRepository.save(request);
        }
        hourRequestRepository.deleteByVolunteerId(volunteer.getId());
        helpRequestRepository.deleteByVolunteerId(volunteer.getId());
        volunteerRepository.delete(volunteer);
    }
    public void createHourRequest(String email, int hours) {
        Volunteer volunteer = findByEmail(email);
        if(volunteer != null) {
            HourRequest hourRequest = new HourRequest(volunteer, hours);
            hourRequestRepository.save(hourRequest);

            String subject = "New Hour Approval Request";
            String message = "Volunteer " + volunteer.getUsername() + "has requested" + hours + "hour(s) approval.";
            notificationService.sendAdminNotification(subject, message);
        }
    }
    public List<HourRequest> getPendingHourRequests() {
        return hourRequestRepository.findByStatus("PENDING");
    }
    public void approveHourRequest(Long requestId, String officerEmail) {
        Optional<HourRequest> requestOpt = hourRequestRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            HourRequest request = requestOpt.get();
            Volunteer volunteer = request.getVolunteer();
            Volunteer officer = findByEmail(officerEmail);

            //Updating hours
            volunteer.setHours(volunteer.getHours() + request.getHoursReqeusted());
            volunteerRepository.save(volunteer);

            //Update the request status
            request.setStatus("APPROVED");
            request.setApprovedBy(officer);
            request.setResponseDate(LocalDateTime.now());
            hourRequestRepository.save(request);

            //Letting the Volunteer know
            String subject = "Hours Approved";
            String message = "Your request for " + request.getHoursReqeusted() + "hours has been approved.";
            notificationService.sendEmail(volunteer.getEmail(), subject, message);
        }
    }
    public void rejectHourRequest(Long requestId, String officerEmail) {
        Optional<HourRequest> requestOpt = hourRequestRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            HourRequest request = requestOpt.get();
            Volunteer volunteer = request.getVolunteer();
            Volunteer officer = findByEmail(officerEmail);

            // Update the request status
            request.setStatus("REJECTED");
            request.setApprovedBy(officer);
            request.setResponseDate(LocalDateTime.now());
            hourRequestRepository.save(request);

            // Notify the volunteer
            String subject = "Hours Rejected";
            String message = "Your request for " + request.getHoursReqeusted() + " hour(s) has been rejected.";
            notificationService.sendEmail(volunteer.getEmail(), subject, message);
        }
    }


    public List<HelpRequest> getActiveHelpRequests() {
        return helpRequestRepository.findByStatusNot("RESOLVED");
    }
    public void respondToHelpRequest(Long requestId, String officerEmail, String status, String response) {
        Optional<HelpRequest> requestOpt = helpRequestRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            HelpRequest request = requestOpt.get();
            Volunteer volunteer = request.getVolunteer();
            Volunteer officer = findByEmail(officerEmail);

            // Update the request
            request.setStatus(status);
            request.setHandledBy(officer);
            request.setResponse(response);
            request.setResponseDate(LocalDateTime.now());
            helpRequestRepository.save(request);

            // Notify the volunteer
            String subject = "Update on Your Help Request";
            String message = "Your help request has been updated to status: " + status + "\n\n";
            message += "Response from " + officer.getUsername() + ":\n" + response;
            notificationService.sendEmail(volunteer.getEmail(), subject, message);
        }
    }

    // Method to get all volunteers
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }
    public List<HelpRequest> getVolunteerHelpRequests(String email) {
        return helpRequestRepository.findByVolunteerEmail(email);
    }
}

