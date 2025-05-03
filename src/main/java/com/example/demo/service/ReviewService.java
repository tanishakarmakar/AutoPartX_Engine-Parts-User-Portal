package com.example.demo.service;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.dto.ReviewDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Part;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PartService partService;
    private final NotificationService notificationService;

    public ReviewService(ReviewRepository reviewRepository,
                       PartService partService,
                       NotificationService notificationService) {
        this.reviewRepository = reviewRepository;
        this.partService = partService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Review createReview(ReviewDto reviewDto, User reviewer, Long partId) {
        Part part = partService.getPartById(partId)
            .orElseThrow(() -> new RuntimeException("Part not found with id: " + partId));
        
        Review review = new Review();
        review.setContent(reviewDto.getContent());
        review.setPart(part);
        review.setReviewer(reviewer);
        review.setReviewDate(LocalDate.now());
        
        Review savedReview = reviewRepository.save(review);
        
        notificationService.createNotification(
            part.getPostedBy(),
            "New review for your part: " + part.getTitle()
        );
        
        return savedReview;
    }

    @Transactional
public Long deleteReview(Long reviewId, User user) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    
    if (!review.getReviewer().equals(user)) {
        throw new AccessDeniedException("You can only delete your own reviews");
    }
    
    Long partId = review.getPart().getId();
    reviewRepository.delete(review);
    return partId;
}

    @Transactional(readOnly = true)
    public List<Review> getReviewsByPart(Long partId) {
        Part part = partService.getPartById(partId)
            .orElseThrow(() -> new RuntimeException("Part not found with id: " + partId));
        return reviewRepository.findByPart(part);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByUser(User reviewer) {
        return reviewRepository.findByReviewer(reviewer);
    }

   
}