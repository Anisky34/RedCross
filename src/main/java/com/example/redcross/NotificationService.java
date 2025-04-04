package com.example.redcross;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VolunteerRepository volunteerRepository;
    public void sendAdminNotification(String subject, String message) {
        List<Volunteer> officers = volunteerRepository.findByRole("OFFICER");
        for (Volunteer officer: officers) {
            sendEmail(officer.getEmail(), subject, message);
        }
    }
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Notification sent to: " + to);
    }
}
