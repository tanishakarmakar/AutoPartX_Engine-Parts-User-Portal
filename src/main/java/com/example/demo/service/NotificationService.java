package com.example.demo.service;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final PartRepository partRepository;

    // Add this constructor
    public NotificationService(NotificationRepository notificationRepository, 
                             PartRepository partRepository) {
        this.notificationRepository = notificationRepository;
        this.partRepository = partRepository;
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
public void markAllAsSeen(User user) {
    notificationRepository.markAllAsSeenForUser(user);
}
    
    @Transactional
    public Notification createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setSeen(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
    
    @Transactional(readOnly = true)
    public List<Notification> getUnseenNotifications(User user) {
        return notificationRepository.findByUserAndSeenFalse(user);
    }
    
    @Transactional
public void markAsSeen(Long notificationId, User user) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    
    if (!notification.getUser().equals(user)) {
        throw new AccessDeniedException("Cannot modify another user's notifications");
    }
    
    notification.setSeen(true);
    notificationRepository.save(notification);
}
    
    @Transactional(readOnly = true)
    public Map<String, Long> getSeasonalStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        int currentMonth = LocalDate.now().getMonthValue();
        
        if (currentMonth >= 3 && currentMonth <= 5) {
            stats.put("Spring Listings", partRepository.countByPostDateBetween(
                getFirstDayOfMonth(3), 
                getLastDayOfMonth(5)
            ));
        } else if (currentMonth >= 6 && currentMonth <= 8) {
            stats.put("Summer Listings", partRepository.countByPostDateBetween(
                getFirstDayOfMonth(6),
                getLastDayOfMonth(8)
            ));
        } else if (currentMonth >= 9 && currentMonth <= 11) {
            stats.put("Fall Listings", partRepository.countByPostDateBetween(
                getFirstDayOfMonth(9),
                getLastDayOfMonth(11)
            ));
        } else {
            stats.put("Winter Listings", partRepository.countByPostDateBetween(
                getFirstDayOfMonth(12),
                getLastDayOfMonth(2)
            ));
        }
        
        return stats;
    }
    
    private LocalDate getFirstDayOfMonth(int month) {
        int year = LocalDate.now().getYear();
        if (month == 12) {
            return LocalDate.of(year, month, 1);
        } else if (month <= 2) {
            return LocalDate.of(year - 1, month, 1);
        }
        return LocalDate.of(year, month, 1);
    }
    
    private LocalDate getLastDayOfMonth(int month) {
        int year = LocalDate.now().getYear();
        if (month == 2) {
            return LocalDate.of(year, month, 28);
        } else if (month == 12) {
            return LocalDate.of(year, month, 31);
        } else if (month < 2) {
            return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
        }
        return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
    }

    public long countUnseenNotifications(User user) {
        return notificationRepository.countByUserAndSeenFalse(user);
    }
}