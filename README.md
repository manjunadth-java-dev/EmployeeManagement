# Employee Management System

## Project Overview

Employee Management System is a RESTful backend application developed using Java 21, Spring Boot, Spring Data JPA, Spring Security, JWT authentication, and MySQL.

The application provides secure employee management operations with role-based access control for ADMIN, HR, and EMPLOYEE users.

## Technologies Used

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA / Hibernate
- Spring Security
- JWT Authentication
- MySQL
- Maven
- JUnit 5
- Mockito
- Postman
- Eclipse / Spring Tool Suite (STS)

## Key Features

- User registration with password encryption
- User login with JWT token generation
- Role-based access control
- Employee creation
- Retrieve all employees
- Retrieve employee by ID
- Update employee details
- Delete employee records
- Input validation
- Unit testing using JUnit 5 and Mockito

## User Roles

| Role | Access |
|---|---|
| ADMIN | Employee CRUD operations, including deletion |
| HR | Employee creation, retrieval, and updating |
| EMPLOYEE | Authenticated access according to configured security permissions |

## REST API Endpoints

### Authentication APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/userInfo/userSaved` | Register a new user |
| POST | `/auth/login` | Authenticate user and generate JWT |

### Employee APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/welcome` | Public welcome endpoint |
| GET | `/getAll` | Retrieve all employees |
| GET | `/getBy/{id}` | Retrieve employee by ID |
| POST | `/posted` | Create an employee |
| PUT | `/updated/{id}` | Update an employee |
| DELETE | `/delete/{id}` | Delete an employee |

## Authentication and Authorization

The application uses Spring Security and JSON Web Tokens (JWT) for authentication.

1. A user registers through the registration API.
2. The password is encrypted using BCrypt.
3. The user logs in using their credentials.
4. The application generates a JWT token.
5. The client includes the token in subsequent protected API requests.
6. Spring Security validates the token and enforces role-based authorization.

Example authorization header:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

## Testing

The application includes automated tests developed using JUnit 5, Mockito, and Spring Boot Test.

Test coverage includes:

- EmployeeServiceTest
- EmployeeControllerTest
- UserInfoServiceTest
- UserInfoControllerTest
- UserDetailServiceTest
- AuthControllerTest
- SecurityTest
- EmployeeManagementApplicationTests

### Latest Test Results

| Metric | Result |
|---|---|
| Total tests | 44 |
| Passed | 44 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 0 |

**Build Status: BUILD SUCCESS**

## Build Instructions

### Prerequisites

- Java 21
- Maven
- MySQL
- Eclipse / STS or another Java IDE

### Configuration

Configure the MySQL datasource in the application's configuration.

Provide the `JWT_SECRET` environment variable with a secure secret suitable for HS256 signing.

Do not commit real database credentials or JWT secrets to GitHub.

### Run Tests

```bash
mvn test
```

### Build Application

```bash
mvn clean package
```

### Run Application

```bash
java -jar target/EmployeeManagement-0.0.1-SNAPSHOT.jar
```

Ensure MySQL is available and all required application configuration is supplied before starting the application.

## Future Enhancements

- Pagination and sorting
- Global exception handling
- Swagger / OpenAPI documentation
- Docker containerization
- CI/CD pipeline integration

## Project Status

Backend implementation, JWT security, automated testing, and Maven packaging completed successfully.