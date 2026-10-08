package com.glebs.ticket_system;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);
    boolean existsByTicketNumber(String ticketNumber);

}