I. **How to run the application:**

1. gradlew build
2. docker-compose up

Docker-compose is going to run Zookeeper, Kafka and Spring-boot application with embedded H2 database.

By default application is being run with parameter showcase.mode=true, which means that application is started in showcase mode.
To run application in standard mode user can modify Dockerfile and change showcase.mode=true to showcase.mode=false, rebuilding
docker-compose is required by executing:
docker-compose up --build

II. **Endpoints**

Application exposes Swagger UI. It can be reached by visiting http://localhost:8110/swagger-ui.html
From Swagger UI it is possible to execute endpoints:
- /stock-prices - which returns list of stock prices by given pagination
- /stock-prices/{stockName} - which returns a stock price by its name

III. **Requirements**

1. Stock price table.

Given data is stored in table STOCK_PRICE. Data is inserted to the table when the application is starting by script data.sql

2. Randomize stock prices

If the showcase.mode is set to true, than the scheduler StockPricesUpdateScheduler is going to send randomized prices to Kafka topic. Scheduler is invoked using cron parameter - showcase.cron. By default it is set to execute scheduler every 5 seconds.
Than the listener is saving received data to the database.

3. Data persistance

Data is persisted in embedded H2 database.

4. REST API 

Please take a look at point II. **Endpoints**

IV. **Tech stack**

1. Java 11
2. Spring Boot, Spring Data
3. Embedded H2 database 
4. JPA - Hibernate
5. gradle
6. docker, docker-compose
7. JUnit5, mockito, JAssert
8. Swagger (Springfox lib)
9. Apache Kafka
10. MapStruct
11. Lombok
