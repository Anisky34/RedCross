package com.example.redcross;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class OfficerDashboardController {
    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/officer-dashboard")
    public String showOfficerDashboard(@RequestParam String email, Model model) {
        Volunteer officer = volunteerService.findByEmail(email);
        if(officer == null || !officer.getRole().equals("OFFICER")) {
            return "redirect:/login";
        }
        model.addAttribute("volunteer", officer);
        model.addAttribute("pendingHours", volunteerService.getPendingHourRequests());
        model.addAttribute("helpRequests", volunteerService.getActiveHelpRequests());
        model.addAttribute("volunteers", volunteerService.getAllVolunteers());

        return "officer-dashboard";
    }
    @PostMapping("/approve-hours")
    public String approveHours(@RequestParam Long requestId, @RequestParam String officerEmail, RedirectAttributes redirectAttributes) {
        try {
            volunteerService.approveHourRequest(requestId, officerEmail);
            redirectAttributes.addFlashAttribute("success", "Hours approved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to approve hours: " + e.getMessage());
        }
        return "redirect:/officer-dashboard?email=" + officerEmail;
    }

    @PostMapping("/reject-hours")
    public String rejectHours(@RequestParam Long requestId, @RequestParam String officerEmail, RedirectAttributes redirectAttributes) {
        try {
            volunteerService.rejectHourRequest(requestId, officerEmail);
            redirectAttributes.addFlashAttribute("success", "Hours rejected");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to reject hours: " + e.getMessage());
        }
        return "redirect:/officer-dashboard?email=" + officerEmail;
    }

    @PostMapping("/respond-help")
    public String respondToHelpRequest(
            @RequestParam Long requestId,
            @RequestParam String officerEmail,
            @RequestParam String status,
            @RequestParam String response,
            RedirectAttributes redirectAttributes) {
        try {
            volunteerService.respondToHelpRequest(requestId, officerEmail, status, response);
            redirectAttributes.addFlashAttribute("success", "Response submitted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit response: " + e.getMessage());
        }
        return "redirect:/officer-dashboard?email=" + officerEmail;
    }
}

