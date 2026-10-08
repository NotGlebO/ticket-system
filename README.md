# IT Support Ticket System

A web-based IT support ticket management application developed using Java, Spring Boot, Thymeleaf, and PostgreSQL.

The project provides a centralized platform for creating, managing, and tracking IT support requests.

## Features

- Ticket creation for registered users and guests
- Unique six-digit ticket numbers
- Role-based access control (USER, IT, ADMIN)
- Personal ticket history for registered users
- Ticket assignment and release by IT staff
- Ticket status management (OPEN, IN_PROGRESS, COMPLETED)
- Ticket messaging between users and support staff
- System messages for ticket assignment, release, and completion
- Administrative panel for managing users and tickets
- Secure authentication using Spring Security
- Password encryption using BCrypt

## Technologies

**Backend**
- Java 21
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

**Frontend**
- HTML
- CSS
- JavaScript
- Thymeleaf

**Database**
- PostgreSQL

**Build Tool**
- Maven

## User Roles

| Role | Permissions |
|---|---|
| Guest | Create support tickets |
| USER | Create tickets, view personal tickets, communicate with support |
| IT | View all tickets, assign and manage tickets, communicate with users |
| ADMIN | All IT permissions, plus user and ticket administration |

## Ticket Workflow

1. A user submits a support request.
2. The system generates a unique ticket number.
3. The ticket receives the OPEN status.
4. An IT employee takes the ticket.
5. The status changes to IN_PROGRESS.
6. The employee can communicate with the user through the ticket chat.
7. The employee can release the ticket or mark it as COMPLETED.
8. Ticket status changes are recorded as system messages.

## Ticket Statuses

- **OPEN** — Ticket is waiting for an IT employee.
- **IN_PROGRESS** — Ticket is assigned to an IT employee.
- **COMPLETED** — Ticket has been resolved.

## Database Configuration

The application uses PostgreSQL.

Database connection settings are configured through environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Running Locally

Requirements:
- Java 21
- Maven
- PostgreSQL

1. Clone the repository.
2. Create a PostgreSQL database.
3. Configure the required database environment variables.
4. Start the application using Maven:

   `./mvnw spring-boot:run`

5. Open `http://localhost:8080` in your browser.

## Project Purpose

This project was created as a personal portfolio project to gain practical experience in backend development.

The main focus is on Java, Spring Boot, relational databases, authentication, authorization, and application architecture.

## Future Improvements

- REST API
- Automated testing
- Ticket search and advanced filtering
- Email notifications
- Docker deployment
- Improved exception handling
