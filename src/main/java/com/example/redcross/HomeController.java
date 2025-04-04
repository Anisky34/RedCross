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
public class HomeController {
    //Executed when the use accesses the homepage /
    @GetMapping("/")
    //Allows passing data to HTML template
    public String home(Model model) {
        //Adds piece of data called event info and connects it to HTML page that's called event info
        model.addAttribute("eventInfo",
                "Upcoming Red Cross Volunteer Event: March 25, 2025");
        //Stores HTML code to navbar
        model.addAttribute("navbar", "<nav><ul><li><a href='/'>Home</a></li><li><a href='/about'>About Us</a></li><li><a href='/volunteer'>Volunteer Opportunities</a></li><li><a href='/donate'>Donate Blood</a></li><li><a href='/contact'>Contact Info</a></li><li><a href='/login'>Login</a></li></ul></nav>");
        //This will make Spring boot look for a home html file
        return "home";
    }
}