package com.example.demo.controller;

import com.example.demo.dto.PartDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.model.Part;
import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.PartService;
import com.example.demo.service.ReviewService;
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
    private final ReviewService reviewService;

    public PartController(PartService partService, ReviewService reviewService) {
        this.partService = partService;
        this.reviewService = reviewService;
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
    public String viewPart(@PathVariable Long id, Model model,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        System.out.println("Accessing /parts/" + id); // Log access

        Part part = partService.getPartById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        if (part.getPostedBy() != null) {
            part.getPostedBy().getName(); // Force load
        }

        model.addAttribute("part", part);
        model.addAttribute("review", new com.example.demo.model.Review());

        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }

        return "parts/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, 
                         Model model,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        User currentUser = userDetails.getUser(); // ✅ Get the actual user
        Part part = partService.getPartById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        if (part.getPostedBy() == null || !part.getPostedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own parts");
        }

        // ✅ Map Part to PartDto
        PartDto partDto = new PartDto();
        partDto.setId(part.getId());
        partDto.setTitle(part.getTitle());
        partDto.setDescription(part.getDescription());
        partDto.setPrice(part.getPrice());

        model.addAttribute("part", partDto); // ✅ Bind PartDto instead of Part
        return "parts/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String updatePart(@PathVariable Long id,
                             @Valid @ModelAttribute("part") PartDto partDto,
                             BindingResult result,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "parts/edit";
        }

        User user = userDetails.getUser();
        partService.updatePart(id, partDto, user); // Ensure service handles DTO
        redirectAttributes.addFlashAttribute("success", "Part updated successfully!");
        return "redirect:/parts/" + id;
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deletePart(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        User user = userDetails.getUser();
        partService.deletePart(id, user);
        redirectAttributes.addFlashAttribute("success", "Part deleted successfully!");
        return "redirect:/parts";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{partId}")
    public String showReviewForm(@PathVariable Long partId,
                                 Model model) {
        Part part = partService.getPartById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + partId));

        model.addAttribute("review", new ReviewDto());
        model.addAttribute("part", part);
        model.addAttribute("reviews", reviewService.getReviewsByPart(partId));

        return "parts/detail";
    }
}
