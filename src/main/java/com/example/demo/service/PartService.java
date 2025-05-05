package com.example.demo.service;

import com.example.demo.dto.PartDto;
import com.example.demo.model.Part;
import com.example.demo.model.User;
import com.example.demo.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {
    private final PartRepository partRepository;
    private final NotificationService notificationService;

    public PartService(PartRepository partRepository,
                     NotificationService notificationService) {
        this.partRepository = partRepository;
        this.notificationService = notificationService;
    }
    
    @Transactional
    public Part createPart(PartDto partDto, User postedBy) {
        Part part = new Part();
        part.setTitle(partDto.getTitle());
        part.setDescription(partDto.getDescription());
        part.setPrice(partDto.getPrice());
        part.setPostedBy(postedBy);
        
        Part savedPart = partRepository.save(part);
        
        notificationService.createNotification(
            postedBy, 
            "New part listed: " + part.getTitle()
        );
        
        return savedPart;
    }
    
    public List<Part> searchParts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return partRepository.findAll();
        }
        return partRepository.findByTitleContainingIgnoreCase(keyword);
    }
    
    public List<Part> getPartsBySeason(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1-12");
        }
        return partRepository.findByPostMonth(month);
    }
    
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }
    
    public Optional<Part> getPartById(Long id) {
        return partRepository.findById(id)
        .map(part -> {
            // Initialize necessary relationships
            if (part.getPostedBy() != null) {
                part.getPostedBy().getName(); // Force load user
            }
            return part;
        });
    }
    
    @Transactional
    public void updatePart(Long id, PartDto partDto, User user) {
        Part part = partRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Part not found"));
        
        if (!part.getPostedBy().equals(user)) {
            throw new SecurityException("Only the part owner can update this part");
        }
        
        part.setTitle(partDto.getTitle());
        part.setDescription(partDto.getDescription());
        part.setPrice(partDto.getPrice());
        
        partRepository.save(part);
    }
    
    @Transactional
    public void deletePart(Long id, User user) {
        Part part = partRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Part not found"));
        
        if (!part.getPostedBy().equals(user)) {
            throw new SecurityException("Only the part owner can delete this part");
        }
        
        partRepository.delete(part);
    }
}