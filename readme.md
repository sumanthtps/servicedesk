# ServiceDesk Backend

A Spring Boot backend for a simple issue tracker.

## Requirements
- Java 21
- Maven 3.9+

## Check Java:
```
java -version
```
(should show 21)

## Run locally

run:
```bash
	./mvnw spring-boot:run
```
build:
```bash
	./mvnw clean install
```
test:
```bash
	./mvnw test
```

# Dependencies

## Spring Web (Web)
- Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.
## Spring Boot Actuator (Ops)
- Supports built in (or custom) endpoints that let you monitor and manage your application - such as application health, metrics, sessions, etc.
## Spring Data JPA (SQL)
- Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.
## PostgreSQL Driver (SQL)
- A JDBC and R2DBC driver that allows Java programs to connect to a PostgreSQL database using standard, database independent Java code.