package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, 
                                UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String listNotifications(Model model,
                                  @AuthenticationPrincipal User user,
                                  RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("notifications", 
                notificationService.getNotificationsForUser(user));
            return "notifications/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to load notifications");
            return "redirect:/";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mark-seen/{id}")
    public String markAsSeen(@PathVariable Long id,
                           @AuthenticationPrincipal User user,
                           RedirectAttributes redirectAttributes) {
        try {
            notificationService.markAsSeen(id, user);
            redirectAttributes.addFlashAttribute("success", "Notification marked as read");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update notification");
        }
        return "redirect:/notifications";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mark-all-seen")
    public String markAllAsSeen(@AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes) {
        try {
            notificationService.markAllAsSeen(user);
            redirectAttributes.addFlashAttribute("success", "All notifications marked as read");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update notifications");
        }
        return "redirect:/notifications";
    }
}