package com.example.demo.repository;
import com.example.demo.model.User;
import com.example.demo.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndSeenFalse(User user);
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    long countByUserAndSeenFalse(User user); // <-- Add this line

    @Modifying
@Query("UPDATE Notification n SET n.seen = true WHERE n.user = :user AND n.seen = false")
void markAllAsSeenForUser(@Param("user") User user);
}