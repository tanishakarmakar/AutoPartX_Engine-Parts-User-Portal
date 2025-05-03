package com.example.demo.repository;
import com.example.demo.model.Part;
import com.example.demo.model.User;
import com.example.demo.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// ReviewRepository.java
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPart(Part part);
    List<Review> findByReviewer(User user);
}
