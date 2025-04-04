package com.example.redcross;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            String role = volunteerService.authenticate_account(email, password);

            if (role == null) {
                model.addAttribute("error", "Invalid email or password");
                return "login";
            }

            // Add email as a parameter for the dashboard
            if (role.equals("OFFICER")) {
                return "redirect:/dashboard?email=" + email;
            } else {
                return "redirect:/dashboard?email=" + email;
            }

        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred during login. Please try again.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully");
        return "redirect:/login";
    }
}