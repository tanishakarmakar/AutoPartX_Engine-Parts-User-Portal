package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.NotificationService;
import com.example.demo.service.PartService;
import com.example.demo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                userOptional.ifPresent(user ->
                    model.addAttribute("notifications", notificationService.getUnseenNotifications(user))
                );
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
        return "users/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        return "profile/view";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam("name") String name,
                                @RequestParam("email") String email,
                                RedirectAttributes redirectAttributes) {
        User user = userDetails.getUser();
        user.setName(name);
        user.setEmail(email);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }
}
