package com.example.demo.repository;
import com.example.demo.model.Part;
import com.example.demo.model.User;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findByPostedBy(User user);
    List<Part> findByTitleContainingIgnoreCase(String keyword);
    
    @Query("SELECT p FROM Part p WHERE EXTRACT(MONTH FROM p.postDate) = :month")
    List<Part> findByPostMonth(@Param("month") int month);

    @Query("SELECT COUNT(p) FROM Part p WHERE p.postDate BETWEEN :startDate AND :endDate")
Long countByPostDateBetween(@Param("startDate") LocalDate startDate, 
                           @Param("endDate") LocalDate endDate);
}
