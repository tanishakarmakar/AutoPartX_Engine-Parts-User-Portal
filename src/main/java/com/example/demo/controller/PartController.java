package com.example.demo.controller;

import com.example.demo.dto.PartDto;
import com.example.demo.model.Part;
import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.PartService;
import com.example.demo.service.UserService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.AccessDeniedException;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/parts")
public class PartController {
    private final PartService partService;
    private final UserService userService;

    public PartController(PartService partService, UserService userService) {
        this.partService = partService;
        this.userService = userService;
    }

    @GetMapping
    public String listParts(Model model,
                          @RequestParam(required = false) Integer month) {
        if (month != null) {
            model.addAttribute("parts", partService.getPartsBySeason(month));
            model.addAttribute("selectedMonth", month);
        } else {
            model.addAttribute("parts", partService.getAllParts());
        }
        return "parts/list";
    }

    @GetMapping("/search")
    public String searchParts(@RequestParam String keyword, Model model) {
        model.addAttribute("parts", partService.searchParts(keyword));
        model.addAttribute("keyword", keyword);
        return "parts/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("part", new PartDto());
        return "parts/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String addPart(@Valid @ModelAttribute("part") PartDto partDto,
                      BindingResult result,
                      @AuthenticationPrincipal CustomUserDetails userDetails,
                      RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        return "parts/add";
    }

    User user = userDetails.getUser();
    partService.createPart(partDto, user);
    redirectAttributes.addFlashAttribute("success", "Part added successfully!");
    return "redirect:/parts";
}

    @GetMapping("/{id}")
public String viewPart(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {

        System.out.println("Accessing /parts/" + id); // Log access

    Part part = partService.getPartById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

    // Eagerly load the postedBy user
    if (part.getPostedBy() != null) {
        part.getPostedBy().getName(); // Force load
    }

    model.addAttribute("part", part);

    // ✅ Add a new Review object to bind to the form
    model.addAttribute("review", new com.example.demo.model.Review());

    // Optional: add user info to show/hide form in Thymeleaf
    if (user != null) {
        model.addAttribute("user", user);
    }

    return "parts/detail"; // Matches your template filename
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, 
                             Model model,
                             @AuthenticationPrincipal User user) {
        Part part = partService.getPartById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part not found"));
        
        if (!part.getPostedBy().equals(user)) {
            throw new AccessDeniedException("You can only edit your own parts");
        }
        
        model.addAttribute("part", part);
        return "parts/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String updatePart(@PathVariable Long id,
                           @Valid @ModelAttribute("part") PartDto partDto,
                           BindingResult result,
                           @AuthenticationPrincipal User user,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "parts/edit";
        }
        
        partService.updatePart(id, partDto, user);
        redirectAttributes.addFlashAttribute("success", "Part updated successfully!");
        return "redirect:/parts/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deletePart(@PathVariable Long id,
                           @AuthenticationPrincipal User user,
                           RedirectAttributes redirectAttributes) {
        partService.deletePart(id, user);
        redirectAttributes.addFlashAttribute("success", "Part deleted successfully!");
        return "redirect:/parts";
    }
}