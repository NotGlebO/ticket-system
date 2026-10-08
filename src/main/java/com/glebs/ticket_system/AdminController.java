package com.glebs.ticket_system;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(
        UserRepository userRepository,
        TicketRepository ticketRepository,
        PasswordEncoder passwordEncoder) {

    this.userRepository = userRepository;
    this.ticketRepository = ticketRepository;
    this.passwordEncoder = passwordEncoder;
}

    @GetMapping
    public String adminPage(Model model) {

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", Role.values());

        // Передаём все тикеты в Admin Panel
        model.addAttribute("tickets", ticketRepository.findAll());

        return "admin";
    }

    @PostMapping("/users")
    public String createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Role role) {

        User user = new User(
                username,
                passwordEncoder.encode(password),
                role
        );

        userRepository.save(user);

        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return "redirect:/admin";
    }

    @PostMapping("/tickets/delete-selected")
    public String deleteSelectedTickets(
            @RequestParam(name = "ticketIds", required = false)
            List<Long> ticketIds) {

        if (ticketIds != null && !ticketIds.isEmpty()) {

            List<Ticket> tickets =
                    ticketRepository.findAllById(ticketIds);

            for (Ticket ticket : tickets) {
                ticketRepository.delete(ticket);
            }
        }

        return "redirect:/admin";
    }
}