package com.example.redcross;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VolunteerDashboardController {
    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/volunteer-dashboard")
    public String showDashboard(@RequestParam String email, Model model) {
        Volunteer volunteer = volunteerService.findByEmail(email);
        if (volunteer == null) {
            return "redirect:/login";
        }

        List<HelpRequest> helpRequests = volunteerService.getVolunteerHelpRequests(email);

        model.addAttribute("volunteer", volunteer);
        model.addAttribute("helpRequests", helpRequests);
        return "volunteer-dashboard";
    }
    @PostMapping("/update-hours")
    public String updateHours(@RequestParam String email, @RequestParam int hours, RedirectAttributes redirectAttributes) {
        try {
            volunteerService.createHourRequest(email, hours);
            redirectAttributes.addFlashAttribute("success", "Your hour request has been submitted for approval");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit hour request: " + e.getMessage());
        }
        return "redirect:/volunteer-dashboard?email=" + email;
    }
    @GetMapping("/view-events")
    public String viewEvents(Model model) {
        model.addAttribute("hillcrestEvents", volunteerService.getHillcrestEvents());
        model.addAttribute("redCrossEvents", volunteerService.getRedCrossEvents());
        return "events";
    }
    @PostMapping("/request-help")
    public String requestHelp(@RequestParam String email, @RequestParam String message, RedirectAttributes redirectAttributes) {
        try {
            volunteerService.sendHelpRequest(email, message);
            redirectAttributes.addFlashAttribute("success", "Your help request has been submitted");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit help request: " + e.getMessage());
        }
        return "redirect:/volunteer-dashboard?email=" + email;
    }
    @GetMapping("/my-help-requests")
    public String viewMyHelpRequests(@RequestParam String email, Model model) {
        Volunteer volunteer = volunteerService.findByEmail(email);
        if (volunteer == null) {
            return "redirect:/login";
        }
        List<HelpRequest> helpRequests = volunteerService.getVolunteerHelpRequests(email);
        model.addAttribute("volunteer", volunteer);
        model.addAttribute("helpRequests", helpRequests);

        return "my-help-requests";
    }
}
