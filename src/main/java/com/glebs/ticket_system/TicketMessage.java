package com.glebs.ticket_system;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_messages")
public class TicketMessage {
    private boolean systemMessage;
    public boolean isSystemMessage() {
        return systemMessage;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // К какому тикету относится сообщение
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    // Кто написал сообщение
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public TicketMessage() {
    }

    public TicketMessage(String message, Ticket ticket, User user) {
        this.message = message;
        this.ticket = ticket;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.systemMessage = false;
    }

    public TicketMessage(
            String message,
            Ticket ticket,
            User user,
            boolean systemMessage) {

        this.message = message;
        this.ticket = ticket;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.systemMessage = systemMessage;
    }


    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public User getUser() {
        return user;
    }
}