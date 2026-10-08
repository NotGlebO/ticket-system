package com.glebs.ticket_system;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String ticketNumber;

    @Column(length = 25)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @OneToMany(
        mappedBy = "ticket",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<TicketMessage> messages = new ArrayList<>();


    // Если тикет создал авторизованный пользователь,
    // здесь будет храниться его аккаунт.
    // Для гостя user будет null.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Ticket() {
    }


    public Ticket(
            String name,
            String email,
            String title,
            String description) {

        this.name = name;
        this.email = email;
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = TicketStatus.OPEN;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }
}