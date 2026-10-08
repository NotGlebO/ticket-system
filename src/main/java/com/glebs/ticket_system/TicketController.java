package com.glebs.ticket_system;

import java.security.Principal;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TicketController {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMessageRepository ticketMessageRepository;

    public TicketController(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            TicketMessageRepository ticketMessageRepository) {

        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.ticketMessageRepository = ticketMessageRepository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/tickets/new")
    public String createTicketPage() {
        return "create-ticket";
    }

    @PostMapping("/tickets")
    public String createTicket(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String title,
            @RequestParam String description,
            Principal principal) {

        Ticket ticket = new Ticket(
                name,
                email,
                title,
                description
        );

        // Если пользователь вошёл в аккаунт
        if (principal != null) {

            User user = userRepository
                    .findByUsername(principal.getName())
                    .orElse(null);

            ticket.setUser(user);
        }
        ticket.setTicketNumber(generateTicketNumber());
        ticketRepository.save(ticket);

        return "redirect:/";
    }

    @GetMapping("/tickets")
    public String showTickets(Model model) {

        model.addAttribute(
                "tickets",
                ticketRepository.findAll()
        );

        return "tickets";
    }
    @GetMapping("/my-tickets")
    public String showMyTickets(
            Principal principal,
            Model model) {

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        model.addAttribute(
                "tickets",
                ticketRepository.findByUser(user)
        );

        return "my-tickets";
    }
    private String generateTicketNumber() {

        String ticketNumber;

        do {
            ticketNumber = String.valueOf(
                    ThreadLocalRandom.current()
                            .nextInt(100000, 1000000)
            );

        } while (ticketRepository.existsByTicketNumber(ticketNumber));

        return ticketNumber;
    }

    @PostMapping("/tickets/{id}/take")
    public String takeTicket(
            @PathVariable Long id,
            Principal principal) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow();

        User currentUser = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        // Взять можно только свободный тикет
        if (ticket.getAssignedTo() == null) {
            ticket.setAssignedTo(currentUser);
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            ticketRepository.save(ticket);

            TicketMessage systemMessage = new TicketMessage(
                    currentUser.getUsername() + " took the ticket",
                    ticket,
                    currentUser,
                    true
            );

            ticketMessageRepository.save(systemMessage);
        }

        return "redirect:/tickets";
    }

    @PostMapping("/tickets/{id}/release")
    public String releaseTicket(
            @PathVariable Long id,
            Principal principal) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow();

        User currentUser = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        // Отпустить может только тот, кто его взял
        if (ticket.getAssignedTo() != null
                && ticket.getAssignedTo().getUsername()
                        .equals(principal.getName())) {

            ticket.setAssignedTo(null);
            ticket.setStatus(TicketStatus.OPEN);
            ticketRepository.save(ticket);

            TicketMessage systemMessage = new TicketMessage(
                    currentUser.getUsername() + " released the ticket",
                    ticket,
                    currentUser,
                    true
            );

            ticketMessageRepository.save(systemMessage);
        }

        return "redirect:/tickets";
    }

    @PostMapping("/tickets/{id}/complete")
    public String completeTicket(
            @PathVariable Long id,
            Principal principal) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow();

        User currentUser = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        // Завершить может только сотрудник,
        // который сейчас работает с тикетом
        if (ticket.getAssignedTo() != null
                && ticket.getAssignedTo().getUsername()
                        .equals(principal.getName())) {

            ticket.setStatus(TicketStatus.COMPLETED);
            ticketRepository.save(ticket);

            TicketMessage systemMessage = new TicketMessage(
                    currentUser.getUsername() + " closed the ticket",
                    ticket,
                    currentUser,
                    true
            );

            ticketMessageRepository.save(systemMessage);
        }

        return "redirect:/tickets";
    }

    @GetMapping("/tickets/{id}")
    public String showTicket(
            @PathVariable Long id,
            Principal principal,
            Model model) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow();

        User currentUser = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        // Обычный USER может смотреть только свой тикет
        if (currentUser.getRole() == Role.USER) {

            if (ticket.getUser() == null ||
                    !ticket.getUser().getId().equals(currentUser.getId())) {

                return "redirect:/access-denied";
            }
        }

        model.addAttribute("ticket", ticket);

        model.addAttribute(
                "messages",
                ticketMessageRepository
                        .findByTicketOrderByCreatedAtAsc(ticket)
        );

        return "ticket-details";
    }

    @PostMapping("/tickets/{id}/messages")
    public String sendMessage(
            @PathVariable Long id,
            @RequestParam String message,
            Principal principal) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow();

        User currentUser = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        // USER может писать только в свой тикет
        if (currentUser.getRole() == Role.USER) {

            if (ticket.getUser() == null ||
                    !ticket.getUser().getId().equals(currentUser.getId())) {

                return "redirect:/access-denied";
            }
        }

        // Не сохраняем пустое сообщение
        if (message != null && !message.trim().isEmpty()) {

            TicketMessage ticketMessage =
                    new TicketMessage(
                            message.trim(),
                            ticket,
                            currentUser
                    );

            ticketMessageRepository.save(ticketMessage);
        }

        return "redirect:/tickets/" + id;
    }
}