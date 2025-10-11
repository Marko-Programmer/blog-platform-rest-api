
# BlogPlatform API

## Overview
**BlogPlatform API** is a Spring Boot RESTful application for managing blog posts and users.  
It allows creating, reading, updating, and deleting posts, managing users, and handling authentication with JWT.

This project is **primarily a demonstration** of technologies and my best practices in Java backend development rather than a complex business logic system.

Key features include:  

- CRUD operations for posts and users.  
- JWT-based authentication and authorization.  
- Role-based access control (Admin vs User).  
- DTO-based API responses for safe and clean data transfer.  
- Global exception handling with meaningful HTTP status codes.  
- Unit and integration tests covering controllers, services, and repositories.

<br><br>

## Technologies & Tools

**Programming Languages:** Java, SQL  
**Frameworks & Libraries:** Spring Boot, Spring MVC, Spring Data JPA, Spring Security, Spring Testing, Hibernate, Jakarta Validation  
**Databases:** MySQL (configurable), H2 (for testing)  
**Web Development:** REST API  
**Other Tools & Technologies:** Git, Maven, JWT, Mockito, JUnit 5

<br><br>

## Features

### User Management
- Register new users and authenticate with JWT.  
- View current logged-in user information.  
- Admin can view all users and delete them.  
- Role-based security: `ROLE_USER` vs `ROLE_ADMIN`.

### Post Management
- Create, update, and delete blog posts.  
- Fetch single posts or lists of posts.  
- Posts are associated with authors.  
- Only authors or admins can update or delete a post.  

### Security
- JWT authentication and authorization.  
- Role-based access control on sensitive endpoints.  
- Passwords are securely hashed with BCrypt.  

### Testing & Validation
- DTO validation using Jakarta Validation (`@NotBlank`, `@Email`, etc.).  
- Comprehensive unit and integration tests using Mockito and JUnit 5.  
- Mocked security context for testing authenticated requests.

<br><br>

## API Endpoints

| Method | Path | Description | Parameters / Body |
|--------|------|-------------|-----------------|
| POST   | /auth/register | Register a new user | JSON UserCreateDTO |
| POST   | /auth/login | Authenticate user and return JWT | JSON LoginRequestDTO |
| GET    | /users/me | Get current logged-in user | - |
| GET    | /admin/users | Get all users (Admin only) | - |
| DELETE | /admin/users/{id} | Delete user by id (Admin only) | Path: user id |
| GET    | /posts | Get list of posts | - |
| GET    | /posts/{id} | Get post by id | Path: post id |
| POST   | /posts | Create a post | JSON PostCreateDTO |
| PUT    | /posts/{id} | Update a post | JSON PostCreateDTO |
| DELETE | /posts/{id} | Delete a post | Path: post id |

<br><br>

## Architecture & Design

- **DTOs:** Ensure API responses are clean and secure.  
- **Validation:** Input validation via `@Valid` and Jakarta Validation annotations.  
- **Error Handling:** Global `@ControllerAdvice` handles exceptions with proper HTTP status codes (400, 401, 403, 404).  
- **Repository Layer:** Spring Data JPA repositories with standard CRUD operations.  
- **Security:** JWT + Spring Security for authentication, role-based authorization, and secure password storage.  
- **Testing:** MockMvc for controller tests, Mockito for service layer, H2 in-memory database for repository tests.  

<br><br>

## Example Usage

**Register a new user:** 
POST /auth/register
{
  "username": "marko",
  "email": "marko@post.com",
  "password": "password123" 
} 

**Login and receive JWT:**
 
POST /auth/login
{
  "username": "marko",
  "password": "password123"
} 

**Create a post:**
 
POST /posts 
{
  "title": "My First Post",
  "content": "This is the content of my first post"
} 
 

<br><br>

## Why This Project Stands Out

* Demonstrates **modern Java backend stack**: Spring Boot, MVC, JPA, Security, Testing, JWT.
* **Layered architecture**: Controller → Service → Repository.
* **DTO-based API** ensures clean, safe responses.
* **Role-based access control** for secure endpoints.
* **Unit and integration tests** covering main functionality.
* **Professional error handling** with clear HTTP status codes. 
<br><br>

## Notes
 
* The business logic is intentionally simple to showcase usage of technologies such as Spring Boot, JWT, DTOs, validation, exception handling, and testing. 
