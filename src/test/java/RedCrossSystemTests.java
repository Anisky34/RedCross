package com.example.redcross;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedCrossSystemTests {

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private HelpRequestRepository helpRequestRepository;

    @Mock
    private HourRequestRepository hourRequestRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private VolunteerService volunteerService;

    @InjectMocks
    private LoginController loginController;

    @InjectMocks
    private SignUpController signUpController;

    @InjectMocks
    private VolunteerDashboardController volunteerDashboardController;

    @InjectMocks
    private OfficerDashboardController officerDashboardController;

    private Volunteer testVolunteer;
    private Volunteer testOfficer;
    private HelpRequest testHelpRequest;
    private HourRequest testHourRequest;
    private List<Volunteer> volunteerList;
    private List<HelpRequest> helpRequestList;
    private List<HourRequest> hourRequestList;

    @BeforeEach
    void setUp() {
        // Setup test data
        testVolunteer = new Volunteer("testUser", "password123", "test@example.com", "VOLUNTEER", "Red Cross");
        testVolunteer.setId(1L);
        testVolunteer.setHours(10);

        testOfficer = new Volunteer("officerUser", "password123", "officer@example.com", "OFFICER", "Red Cross");
        testOfficer.setId(2L);

        testHelpRequest = new HelpRequest(testVolunteer, "I need assistance with logging hours");
        testHelpRequest.setId(1L);
        testHelpRequest.setStatus("PENDING");

        testHourRequest = new HourRequest(testVolunteer, 5);
        testHourRequest.setId(1L);

        volunteerList = new ArrayList<>();
        volunteerList.add(testVolunteer);
        volunteerList.add(testOfficer);

        helpRequestList = new ArrayList<>();
        helpRequestList.add(testHelpRequest);

        hourRequestList = new ArrayList<>();
        hourRequestList.add(testHourRequest);
    }

    // User Registration Tests
    @Test
    void testUserRegistration() {
        when(volunteerService.findByEmail("new@example.com")).thenReturn(null);
        String result = signUpController.signup("newUser", "new@example.com", "password", "VOLUNTEER", "Red Cross", model);
        verify(volunteerService).saveVolunteer(any(Volunteer.class));
        assertEquals("redirect:/login", result);
    }

    @Test
    void testUserLogin() {
        when(volunteerService.authenticate_account("test@example.com", "password123")).thenReturn("VOLUNTEER");
        String result = loginController.login("test@example.com", "password123", model, redirectAttributes);
        assertEquals("redirect:/volunteer-dashboard?email=test@example.com", result);
    }

    @Test
    void testAccountDeletion_Success() {
        when(volunteerService.findByEmail("test@example.com")).thenReturn(testVolunteer);
        String result = signUpController.deleteAccount("test@example.com", "password123", model, redirectAttributes);
        verify(volunteerService).deleteVolunteer(testVolunteer);
        verify(redirectAttributes).addFlashAttribute("success", "Your account has been deleted");
        assertEquals("redirect:/signup", result);
    }

    @Test
    void testVolunteerDashboard() {
        when(volunteerService.findByEmail("test@example.com")).thenReturn(testVolunteer);
        when(volunteerService.getVolunteerHelpRequests("test@example.com")).thenReturn(helpRequestList);
        String result = volunteerDashboardController.showDashboard("test@example.com", model);
        verify(model).addAttribute("volunteer", testVolunteer);
        verify(model).addAttribute("helpRequests", helpRequestList);
        assertEquals("volunteer-dashboard", result);
    }

    @Test
    void testOfficerDashboardAccess_Success() {
        // Given
        when(volunteerService.findByEmail("officer@example.com")).thenReturn(testOfficer);
        when(volunteerService.getPendingHourRequests()).thenReturn(hourRequestList);
        when(volunteerService.getActiveHelpRequests()).thenReturn(helpRequestList);
        when(volunteerService.getAllVolunteers()).thenReturn(volunteerList);

        // When
        String result = officerDashboardController.showOfficerDashboard("officer@example.com", model);

        // Then
        verify(model).addAttribute("volunteer", testOfficer);
        verify(model).addAttribute("pendingHours", hourRequestList);
        verify(model).addAttribute("helpRequests", helpRequestList);
        verify(model).addAttribute("volunteers", volunteerList);
        assertEquals("officer-dashboard", result);
    }

    @Test
    void testSubmitHourRequest_Success() {
        String result = volunteerDashboardController.updateHours("test@example.com", 5, redirectAttributes);
        verify(volunteerService).createHourRequest("test@example.com", 5);
        verify(redirectAttributes).addFlashAttribute("success", "Your hour request has been submitted for approval");
        assertEquals("redirect:/volunteer-dashboard?email=test@example.com", result);
    }

    @Test
    void testApproveHourRequest_Success() {
        String result = officerDashboardController.approveHours(1L, "officer@example.com", redirectAttributes);
        verify(volunteerService).approveHourRequest(1L, "officer@example.com");
        verify(redirectAttributes).addFlashAttribute("success", "Hours approved successfully");
        assertEquals("redirect:/officer-dashboard?email=officer@example.com", result);
    }

    // Help Request Tests
    @Test
    void testSubmitHelpRequest_Success() {

        String result = volunteerDashboardController.requestHelp("test@example.com", "I need help", redirectAttributes);
        verify(volunteerService).sendHelpRequest("test@example.com", "I need help");
        verify(redirectAttributes).addFlashAttribute("success", "Your help request has been submitted");
        assertEquals("redirect:/volunteer-dashboard?email=test@example.com", result);
    }

    @Test
    void testRespondToHelpRequest_Success() {
        String result = officerDashboardController.respondToHelpRequest(
                1L, "officer@example.com", "IN_PROGRESS", "We're working on it", redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("success", "Response submitted successfully");
        assertEquals("redirect:/officer-dashboard?email=officer@example.com", result);
    }
}
