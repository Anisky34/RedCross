package com.example.redcross;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class DashboardController {
    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @RequestParam String email) {
        Volunteer volunteer = volunteerService.findByEmail(email);

        if (volunteer != null) {
            model.addAttribute("volunteer", volunteer);
            if (volunteer.getRole().equals("OFFICER")) {
                return "redirect:/officer-dashboard?email=" + email;
            } else {
                return "redirect:/volunteer-dashboard?email=" + email;
            }
        } else {
            return "redirect:/login";
        }
    }
}
