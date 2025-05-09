package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.PartRepository;

import java.util.*;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

    @Autowired
    private PartRepository partRepository;

    @GetMapping("/seasonal-parts")
    public ResponseEntity<Map<String, Long>> getSeasonalPartStats() {
        List<Object[]> results = partRepository.getSeasonalPartStats();
        Map<String, Long> response = new LinkedHashMap<>();

        for (Object[] row : results) {
            String season = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            response.put(season, count);
        }

        return ResponseEntity.ok(response);
    }
}

