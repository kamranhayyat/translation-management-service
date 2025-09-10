Translation Management Service
Overview
This service is part of a microservices-based architecture for managing translations across multiple languages. It provides CRUD operations for languages and translations, along with search capabilities.
Due to time constraints, some foundational services (such as config, discovery, and API gateway) are assumed to be in place and properly configured, even though they are not included in this codebase.

Architecture Notes
* Config, Discovery, API Gateway These services are not included here, but it is assumed they are available and routing is correctly set up through the API Gateway to the Translation Management Service.
* Authentication Service Not implemented in this repo. Normally, a dedicated authentication service would handle:
    * User registration
    * Issuing access tokens
    * Refresh token management
* Auth Filter In a standard setup, token and credential validation should occur at the API Gateway. For simplicity, a minimalistic authentication mechanism has been implemented inside this service.
* Database An in-memory H2 database is used to simplify setup. In a production environment, replace it with a persistent database (e.g., PostgreSQL, MySQL).

Prerequisites
Before running the service, ensure the following are installed and available:
* Java 17 or higher
* Gradle (or use the included Gradle wrapper ./gradlew)
* Git (to clone the repository)
* An IDE such as IntelliJ IDEA or VS Code (recommended)

Tech Stack
* Spring Boot 3.2
* Spring Data JPA
* H2 Database (in-memory)
* Spring Validation
* Lombok
* Gradle (build tool)

