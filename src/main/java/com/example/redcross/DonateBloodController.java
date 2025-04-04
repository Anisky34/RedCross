package com.example.redcross;
//Package name

//Makes it known that it's a spring controller
import org.springframework.stereotype.Controller;
//Allows me to add data to HTML page
import org.springframework.ui.Model;
//Requests for root URl
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//Declares class
public class DonateBloodController {
    //Executed when the use accesses the homepage /
    @GetMapping("/donate")
    //Allows passing data to HTML template
    public String about(Model model) {
        model.addAttribute("navbar", "<nav><ul><li><a href='/'>Home</a></li><li><a href='/about'>About Us</a></li><li><a href='/volunteer'>Volunteer Opportunities</a></li><li><a href='/donate'>Donate Blood</a></li><li><a href='/contact'>Contact Info</a></li><li><a href='/login'>Login</a></li></ul></nav>");
        //This will make Spring boot look for an about html file
        return "donate";
    }
}