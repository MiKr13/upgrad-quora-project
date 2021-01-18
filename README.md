# Quora REST API

This project contains REST API endpoints of various functionalities required for a website (similar to Quora).

## Dependencies

In order to observe the functionality of the endpoints, you will use the Swagger user interface and store the data in the PostgreSQL database.

## Technical specifications

1. The project has to be implemented using Java Persistence API (JPA).

## Basic features

1. Authentication & Authorization

2. Definite standard project structure

3. PostgreSQL used with schema properly implemented

4. Spring, Unit Testing, and Mocking implemented

## Steps to run

1. Build the project using the command `mvn clean install -DskipTests`

2. Update database by activating the profile setup using the commands
   * `cd quora-db`
   * `mvn clean install -Psetup`

3. Modify the database password in following files
   * quora-api\src\main\resources\application.yaml
   * quora-db\src\main\resources\config\localhost.properties

4. While authenticating the user use the format "Basic base64encoded(username:password)"
