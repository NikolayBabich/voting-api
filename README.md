# Restaurant Voting REST API

### Graduation project of [TopJava internship](https://javaops.ru/view/topjava)

Voting system for deciding where to have lunch

* 2 types of users: admins and regular users
* Admins can input a restaurant and its lunch menu for the day (usually 2-5 items, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for the restaurant they want to have lunch at
* Only one vote counts per user
* If user votes again on the same day:
  - if before 11:00, we assume that he has changed his mind
  - if after 11:00, then it's too late, the vote cannot be changed

Each restaurant provides a new menu each day.

****
### Application stack:
Spring Boot 2.x, Spring MVC, Spring Security, Spring Data JPA, Hibernate, Caffeine Cache, H2 database, JUnit 5, AssertJ, Mockito, Swagger/OpenAPI 3.0

****
### Generated [OpenAPI documentation](http://localhost:8080/swagger-ui.html) by Swagger
