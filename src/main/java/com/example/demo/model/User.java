package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "postedBy", cascade = CascadeType.ALL)
    private List<Part> parts = new ArrayList<>();
    
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    // No-args constructor
    public User() {
    }

    // All-args constructor
    public User(Long id, String name, String email, String password, 
               List<Part> parts, List<Review> reviews, List<Notification> notifications) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.parts = parts;
        this.reviews = reviews;
        this.notifications = notifications;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Safe getters for collections
    public List<Part> getParts() {
        if (parts == null) {
            parts = new ArrayList<>();
        }
        return parts;
    }

    public List<Review> getReviews() {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    public List<Notification> getNotifications() {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        return notifications;
    }

    // Safe setters for collections
    public void setParts(List<Part> parts) {
        if (parts != null) {
            this.parts = parts;
        } else {
            this.parts = new ArrayList<>();
        }
    }

    public void setReviews(List<Review> reviews) {
        if (reviews != null) {
            this.reviews = reviews;
        } else {
            this.reviews = new ArrayList<>();
        }
    }

    public void setNotifications(List<Notification> notifications) {
        if (notifications != null) {
            this.notifications = notifications;
        } else {
            this.notifications = new ArrayList<>();
        }
    }

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id != null && id.equals(user.id);
}

@Override
public int hashCode() {
    return getClass().hashCode();
}

}