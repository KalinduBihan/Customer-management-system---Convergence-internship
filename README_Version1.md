# Customer Management System - Convergence Internship

A comprehensive Spring Boot JPA-based Customer Management System with complete CRUD operations, relationship management, and RESTful API.

## Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Database Access](#database-access)
- [Troubleshooting](#troubleshooting)

---

## Project Overview

This project implements a complete Customer Management System that allows users to:
- Create and manage customers with personal information
- Manage multiple addresses per customer
- Handle customer relationships (family members)
- Store multiple mobile numbers
- Query customers by various criteria (name, age, city, country)

---

## Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17 | Programming Language |
| **Spring Boot** | 4.0.6 | Framework |
| **Spring Data JPA** | Latest | ORM & Database Access |
| **Hibernate** | Latest | JPA Implementation |
| **H2 Database** | Latest | In-memory Database |
| **Lombok** | Latest | Reduce Boilerplate Code |
| **Maven** | 3.6+ | Build Tool |
| **JUnit 5** | Latest | Testing Framework |
| **Mockito** | Latest | Mocking Library |

---

## Features

### ✅ Core Features

- **Customer Management**
  - Create, Read, Update, Delete customers
  - Unique NIC validation
  - Birth date tracking
  - Multiple mobile numbers per customer

- **Address Management**
  - Multiple addresses per customer
  - City and Country associations
  - Proper cascading on deletion

- **Relationship Management**
  - Self-referencing family member relationships
  - Many-to-Many relationships

- **Search & Filter**
  - Search by name (case-insensitive)
  - Filter by age range
  - Find customers by city or country
  - Get customer by NIC

- **Error Handling**
  - Global exception handler
  - Detailed error messages
  - Field validation errors
  - HTTP status codes

### 🔒 Data Optimization

- **N+1 Query Prevention** using JOIN FETCH
- **Lazy Loading** for performance
- **Batch Processing** configuration
- **Cascading Operations** for data integrity

---

## Prerequisites

Before you begin, ensure you have installed:

- **Java 17 or higher**: [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6 or higher**: [Download Maven](https://maven.apache.org/download.cgi)
- **Git**: [Download Git](https://git-scm.com/downloads)
- **Postman** (for testing APIs): [Download Postman](https://www.postman.com/downloads/)

### Verify Installation

```bash
java -version
mvn -version
git --version
```

---

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/KalinduBihan/Customer-management-system---Convergence-internship.git
cd Customer-management-system---Convergence-internship
```

### 2. Build the Project

```bash
mvn clean install
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXs
```

### 3. Verify Dependencies

```bash
mvn dependency:tree
```

---

## Configuration

### Application Properties

The application is configured in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Development Profile

To use the development profile with verbose logging:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Or set in IDE:
- Program Arguments: `--spring.profiles.active=dev`

---

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using IDE (IntelliJ IDEA)

1. Open project in IntelliJ
2. Right-click on `DemoApplication.java`
3. Select **Run 'DemoApplication'**

### Using IDE (Eclipse)

1. Right-click on project → **Run As** → **Spring Boot App**

### Verify Application Started

Look for this message in console:
```
Started DemoApplication in X.XXX seconds (process running with PID XXXX)
```

---

## API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Authentication

Currently, no authentication is required. All endpoints are public.

---

### Customer Endpoints

#### 1. **Create Customer** (POST)

```
POST /api/v1/customers
```

**Request Body:**
```json
{
  "name": "John Doe",
  "dateOfBirth": "1990-05-15",
  "nic": "991234567V",
  "mobileNumbers": ["0771234567", "0772345678"],
  "addresses": [
    {
      "line1": "123 Main Street",
      "line2": "Apt 4B",
      "cityId": 1,
      "countryId": 1
    }
  ],
  "familyMemberIds": null
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "dateOfBirth": "1990-05-15",
  "nic": "991234567V",
  "mobileNumbers": ["0771234567", "0772345678"],
  "addresses": [
    {
      "id": 1,
      "line1": "123 Main Street",
      "line2": "Apt 4B",
      "city": {"id": 1, "name": "Colombo"},
      "country": {"id": 1, "name": "Sri Lanka"}
    }
  ],
  "familyMembers": null
}
```

---

#### 2. **Get All Customers** (GET)

```
GET /api/v1/customers
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "dateOfBirth": "1990-05-15",
    "nic": "991234567V",
    "mobileNumbers": ["0771234567", "0772345678"],
    "addresses": [...],
    "familyMembers": null
  }
]
```

---

#### 3. **Get Customer by ID** (GET)

```
GET /api/v1/customers/{id}
```

**Example:**
```
GET /api/v1/customers/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "dateOfBirth": "1990-05-15",
  "nic": "991234567V",
  "mobileNumbers": ["0771234567", "0772345678"],
  "addresses": [...],
  "familyMembers": null
}
```

---

#### 4. **Get Customer by NIC** (GET)

```
GET /api/v1/customers/nic/{nic}
```

**Example:**
```
GET /api/v1/customers/nic/991234567V
```

---

#### 5. **Search by Name** (GET)

```
GET /api/v1/customers/search/by-name?name={name}
```

**Example:**
```
GET /api/v1/customers/search/by-name?name=John
```

---

#### 6. **Find by Age Range** (GET)

```
GET /api/v1/customers/search/by-age?startDate={date}&endDate={date}
```

**Example:**
```
GET /api/v1/customers/search/by-age?startDate=1980-01-01&endDate=2000-12-31
```

---

#### 7. **Find by City** (GET)

```
GET /api/v1/customers/search/by-city?cityId={cityId}
```

**Example:**
```
GET /api/v1/customers/search/by-city?cityId=1
```

---

#### 8. **Update Customer** (PUT)

```
PUT /api/v1/customers/{id}
```

**Request Body:** (Same as Create)

**Response (200 OK):** Updated customer details

---

#### 9. **Delete Customer** (DELETE)

```
DELETE /api/v1/customers/{id}
```

**Example:**
```
DELETE /api/v1/customers/1
```

**Response (204 No Content):** No body

---

#### 10. **Add Mobile Number** (POST)

```
POST /api/v1/customers/{id}/mobile-numbers?mobileNumber={number}
```

**Example:**
```
POST /api/v1/customers/1/mobile-numbers?mobileNumber=0771111111
```

---

#### 11. **Remove Mobile Number** (DELETE)

```
DELETE /api/v1/customers/{id}/mobile-numbers?mobileNumber={number}
```

---

### Country Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/countries` | List all countries |
| GET | `/api/v1/countries/{id}` | Get by ID |
| GET | `/api/v1/countries/search?name={name}` | Search by name |
| POST | `/api/v1/countries` | Create country |
| PUT | `/api/v1/countries/{id}` | Update country |
| DELETE | `/api/v1/countries/{id}` | Delete country |

---

### City Endpoints

Same pattern as Country endpoints, use `/api/v1/cities`

---

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=CustomerServiceTest
```

### Run Tests with Coverage

```bash
mvn test jacoco:report
```

View report: `target/site/jacoco/index.html`

### Run Tests with Verbose Output

```bash
mvn test -X
```

---

## Database Access

### H2 Console

Access the H2 database console:

```
URL: http://localhost:8080/h2-console
```

**Login Credentials:**
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (leave empty)

**Useful Queries:**

```sql
-- View all customers
SELECT * FROM customers;

-- View all addresses
SELECT * FROM addresses;

-- View customer with addresses
SELECT c.id, c.name, c.nic, a.line1, a.line2, ct.name as city
FROM customers c
LEFT JOIN addresses a ON c.id = a.customer_id
LEFT JOIN cities ct ON a.city_id = ct.id;

-- Count customers by city
SELECT ct.name, COUNT(DISTINCT c.id) as customer_count
FROM customers c
LEFT JOIN addresses a ON c.id = a.customer_id
LEFT JOIN cities ct ON a.city_id = ct.id
GROUP BY ct.name;
```

---

## Project Structure

```
Customer-management-system/
├── src/main/java/com/example/demo/
│   ├── entity/
│   │   ├── Customer.java
│   │   ├── Address.java
│   │   ├── City.java
│   │   └── Country.java
│   ├── repository/
│   │   ├── CustomerRepository.java
│   │   ├── AddressRepository.java
│   │   ├── CityRepository.java
│   │   └── CountryRepository.java
│   ├── dto/
│   │   ├── CustomerDTO.java
│   │   ├── CustomerCreateUpdateDTO.java
│   │   ├── AddressDTO.java
│   │   ├── CityDTO.java
│   │   └── CountryDTO.java
│   ├── service/
│   │   ├── CustomerService.java
│   │   ├── AddressService.java
│   │   ├── CityService.java
│   │   └── CountryService.java
│   ├── controller/
│   │   ├── CustomerController.java
│   │   ├── AddressController.java
│   │   ├── CityController.java
│   │   └── CountryController.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── CustomerNotFoundException.java
│   │   ├── DuplicateNicException.java
│   │   └── InvalidInputException.java
│   ├── config/
│   │   └── WebConfig.java
│   └── DemoApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── application-dev.properties
├── src/test/java/com/example/demo/
│   ├── service/
│   │   ├── CustomerServiceTest.java
│   │   ├── CityServiceTest.java
│   │   ├── CountryServiceTest.java
│   │   └── AddressServiceTest.java
│   ├── controller/
│   │   └── CustomerControllerTest.java
│   └── repository/
│       └── CustomerRepositoryTest.java
├── pom.xml
└── README.md
```

---

## Troubleshooting

### Issue: Port 8080 Already in Use

**Solution:**

Option 1: Change port in `application.properties`
```properties
server.port=8081
```

Option 2: Kill process using port 8080
```bash
# On Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# On Mac/Linux
lsof -ti:8080 | xargs kill -9
```

---

### Issue: H2 Console Not Loading

**Solution:**

Ensure `spring.h2.console.enabled=true` in properties and restart application.

---

### Issue: Validation Errors on Create

**Solution:**

Check validation rules:
- Name: 2-100 characters
- NIC: 10-20 characters, format: 9 digits + optional V/X
- Mobile: Exactly 10 digits
- Date of Birth: Cannot be in future
- At least one address required

---

### Issue: Duplicate NIC Error (409 Conflict)

**Solution:**

Use a different NIC number. Each NIC must be unique in the system.

---

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a Pull Request

---

## License

This project is licensed under the MIT License - see LICENSE file for details.

---

## Author

**Kalindu Bihan** - Convergence Internship

---

## Support

For issues, questions, or suggestions, please create an issue in the repository.

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-05-01 | Initial release with CRUD operations |
| | | Repository layer with N+1 prevention |
| | | Service layer with business logic |
| | | REST controllers with full API |
| | | Global exception handling |
| | | Comprehensive unit & integration tests |
| | | Complete documentation |

---

## Acknowledgments

- Spring Boot Documentation
- JPA/Hibernate Documentation
- Lombok Project
- Mockito Testing Framework
