Project Description – Payment Microservices System

Payment Microservices System is a distributed backend application built using Spring Boot and Spring Cloud that simulates a secure digital payment platform similar to Paytm or Stripe. The system follows a microservices architecture where each service is responsible for a specific domain such as authentication, wallet management, payments, and notifications.

The application uses API Gateway for centralized authentication and authorization, where all incoming requests are validated using JWT tokens. Role-based access control is implemented so that admin and user operations are handled securely across services.

The platform includes multiple independent services registered with Eureka Service Discovery, allowing dynamic service communication. Configuration for all services is managed centrally using Spring Cloud Config Server with GitHub-based configuration storage, making configuration management scalable and maintainable.

Users can register and log in, create wallets, check balances, transfer funds to other users, and receive email notifications after successful transactions. Admin users can manage system-level operations such as crediting wallets.

The system also implements Feign clients for inter-service communication, ensuring seamless interactions between payment, wallet, and notification services.

This project demonstrates key enterprise backend architecture concepts, including:

Microservices architecture

API Gateway pattern

JWT-based authentication and role-based authorization

Service discovery using Eureka

Centralized configuration using Spring Cloud Config Server

Inter-service communication with OpenFeign

Transaction handling between wallet and payment services

Email notifications using Spring Mail

The project reflects how modern fintech platforms structure scalable backend systems using Spring Boot, Spring Cloud, and distributed system design principles.
