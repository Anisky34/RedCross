 package com.example.redcross;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignUpController {
    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("navbar", "<nav><ul><li><a href='/'>Home</a></li><li><a href='/about'>About Us</a></li><li><a href='/volunteer'>Volunteer Opportunities</a></li><li><a href='/donate'>Donate Blood</a></li><li><a href='/contact'>Contact Info</a></li><li><a href='/login'>Login</a></li><li><a href='/signup'>Sign Up</a></li></ul></nav>");
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam String role, @RequestParam String type, Model model) {
        if (volunteerService.findByEmail(email) != null) {
            model.addAttribute("error", "Email already been registered.");
            return "signup";
        }


        Volunteer newVolunteer = new Volunteer(username, password, email, role, type);
        volunteerService.saveVolunteer(newVolunteer);
        return "redirect:/login";
    }
    @PostMapping("/delete-account")
    public String deleteAccount(@RequestParam String email, @RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
        Volunteer volunteer = volunteerService.findByEmail(email);
        if (volunteer == null) {
            redirectAttributes.addFlashAttribute("error", "Account does not exist");
            return "redirect:/signup";
        }
        if (!volunteer.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/signup";
        }
        volunteerService.deleteVolunteer(volunteer);
        redirectAttributes.addFlashAttribute("success", "Your account has been deleted");
        return "redirect:/signup";
    }


}



