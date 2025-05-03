package com.example.demo.controller.advisor;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
    
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public String handleAccessDenied(Exception ex, Model model) {
        model.addAttribute("error", "You don't have permission to access this resource");
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("error", "An unexpected error occurred");
        return "error";
    }

  @ExceptionHandler(AccessDeniedException.class)
public String handleAccessDenied(AccessDeniedException ex, 
                               Model model,
                               HttpServletRequest request) {
    model.addAttribute("error", ex.getMessage());
    model.addAttribute("path", request.getRequestURI());
    return "error/403";  // Specific error page for 403 errors
}
}
