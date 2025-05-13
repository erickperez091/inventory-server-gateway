# Server-gateway
This project works as entry point for the rest of the microservices, <br/>
having an exposed port and allowing all the calls to enter through it.

Using a configuration and server discovery approach I allow some pattern API call to be redirected to the according microserivce

## Execution
To execute this project, you might need to use jdk 24 and mvn 3.8.6 (I used those versions)

## Projects Related
- server-gateway
- inventory-products
- inventory-invoices

## Tech Stack
```
    Java 24
    Spring Boot
    Docker
    Maven
```