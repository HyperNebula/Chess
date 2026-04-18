# Full-Stack Java Chess Application

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL/SQLite](https://img.shields.io/badge/Database-SQL-blue?style=for-the-badge)

A full-stack, client-server chess application built in Java. This project supports multiple concurrent users, chess move validation, user authentication, and persistent data storage. 

## Key Features

* **Complete Game Logic:** Implements all standard chess rules, including complex moves like pawn promotion.
* **Client-Server Architecture:** Communicates via HTTP APIs and WebSockets to synchronize game state across multiple clients.
* **User Authentication:** Secure user registration, login, and token-based session management.
* **Database Persistence:** Game states, user profiles, and session tokens are stored persistently using a relational SQL database.

## Software Architecture

This application was built utilizing a layered architecture to ensure separation of concerns:

* **Client:** User interface and game rendering.
* **Server Handlers:** Intercepts HTTP requests/WebSockets, parses JSON data, and routes them to the appropriate service.
* **Services:** Contains the core business logic (e.g., verifying user credentials, updating game states).
* **Data Access Objects (DAOs):** Handles all direct interactions with the SQL database, keeping SQL queries isolated from the other logic.

## Tech Stack

* **Language:** Java
* **Database:** MySQL
* **Data Serialization:** Google Gson (JSON)
