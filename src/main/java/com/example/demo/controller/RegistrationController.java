package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Add this import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/register")  // Added base path mapping
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "users/register";
    }

    @PostMapping
public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto,
                         RedirectAttributes redirectAttributes) {
    userService.registerUser(registrationDto);
    redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
    return "redirect:/login";
}
}