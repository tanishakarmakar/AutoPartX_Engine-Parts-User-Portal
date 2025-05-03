package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import com.example.demo.service.PartService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class HomeController {
    private final PartService partService;
    private final NotificationService notificationService;
    private final UserService userService;

    public HomeController(PartService partService, 
                        NotificationService notificationService,
                        UserService userService) {
        this.partService = partService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        try {
            model.addAttribute("parts", partService.getAllParts());
            
            if (principal != null) {
                Optional<User> userOptional = userService.findByEmail(principal.getName());
                if (userOptional.isPresent()) {
                    model.addAttribute("notifications", 
                        notificationService.getUnseenNotifications(userOptional.get()));
                }
                // Silently continue if user not found
            }
            
            model.addAttribute("seasonalStats", notificationService.getSeasonalStats());
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading page. Please try again later.");
            return "error";
        }
    }

    @GetMapping("/login")
public String login() {
    return "users/login"; // Will render login.html
}




}