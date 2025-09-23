# Book-Library Microservices

## Project Overview
This project implements a Spring Boot-based microservices system for books and libraries, consisting of two independent services:
- **book-service**: Responsible for global book CRUD, borrowing/returning, and association with libraries.
- **library-service**: Responsible for library CRUD, collection management, and synchronization with books.

The two services communicate synchronously via RestTemplate to ensure data consistency.

## Main Features
### book-service
- Create books (globally unique)
- Query all books or a single book
- Query which libraries a book is available in
- Borrow/return books (synchronously update library-service)
- Add/remove books to/from libraries (synchronize with library-service)

### library-service
- Create/query/update libraries
- Add/remove books to/from libraries (synchronize with book-service)
- Borrow/return books within a library (synchronize with book-service)

## Example Endpoints & Usage (curl examples)

### Book Service
- Create a book:
  ```cmd
  curl -X POST -H "Content-Type: application/json" -d "{\"isbn\":\"1234567890\",\"title\":\"Spring Boot in Action\"}" http://localhost:8080/books
  ```
- Query all books:
  ```cmd
  curl http://localhost:8080/books
  ```
- Borrow a book:
  ```cmd
  curl -X PUT http://localhost:8080/books/borrow/1234567890/1
  ```
- Return a book:
  ```cmd
  curl -X PUT http://localhost:8080/books/return/1234567890/1
  ```
- Add a book to a library (global create + sync):
  ```cmd
  curl -X PUT -H "Content-Type: application/json" -d "{\"isbn\":\"1234567890\",\"title\":\"Spring Boot in Action\"}" http://localhost:8080/books/1234567890/libraries/1/add
  ```

### Library Service
- Create a library:
  ```cmd
  curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"Central Library\"}" http://localhost:8081/libraries
  ```
- Add a book to a library (global create + sync):
  ```cmd
  curl -X PUT -H "Content-Type: application/json" -d "{\"isbn\":\"1234567890\",\"title\":\"Spring Boot in Action\"}" http://localhost:8081/libraries/1/addBook/1234567890
  ```
- Borrow a book in a library:
  ```cmd
  curl -X PUT http://localhost:8081/libraries/1/books/1234567890/borrow
  ```
- Return a book in a library:
  ```cmd
  curl -X PUT http://localhost:8081/libraries/1/books/1234567890/return
  ```

## Technical Highlights
- Spring Boot 3
- Spring Data JPA
- H2 in-memory database
- RestTemplate microservice communication
- DTO pattern
- Domain events & event sourcing

## How to Run
1. Enter the `book-service` and `library-service` directories separately and run:
   ```cmd
   mvn spring-boot:run
   ```
   Or run the main class of each service in your IDE.
2. Access the endpoints for testing.

## Notes
- The model/Book.java and similar classes in both microservices are independent; do not cross-depend in pom.xml.
- You must start each service separately; do not start all modules at once from the parent/root project.

