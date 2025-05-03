package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "parts")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private Double price;
    
    @Column(name = "post_date")
    private LocalDate postDate = LocalDate.now();
    
    @ManyToOne
    @JoinColumn(name = "posted_by", referencedColumnName = "id")
    private User postedBy;

    // Default constructor
    public Part() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    // toString() method for better logging/debugging
    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", postDate=" + postDate +
                ", postedBy=" + (postedBy != null ? postedBy.getId() : null) +
                '}';
    }

    // equals() and hashCode() methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Part)) return false;
        Part part = (Part) o;
        return id != null && id.equals(part.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}