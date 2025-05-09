package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributeConfig {

    private final NotificationService notificationService;

    public GlobalModelAttributeConfig(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute
    public void addGlobalAttributes(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userDetails.getUser();
            long unseenCount = notificationService.countUnseenNotifications(user);
            model.addAttribute("unseenCount", unseenCount);
        }
    }
}
