package com.glebs.ticket_system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketMessageRepository
        extends JpaRepository<TicketMessage, Long> {

    List<TicketMessage> findByTicketOrderByCreatedAtAsc(Ticket ticket);
}