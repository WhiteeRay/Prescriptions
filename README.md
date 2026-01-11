# Prescription Service

A Spring Boot REST API service for managing medical prescriptions.

# Project Structure

```
prescription-service/
├── src/main/java/com/example/prescriptions/
│   ├── com.example.prescriptions.PrescriptionServiceApplication.java
│   ├── controller/
│   │   └── PrescriptionController.java
│   ├── service/
│   │   ├── PrescriptionService.java
│   │   └── impl/
│   │       └── PrescriptionServiceImpl.java
│   ├── repository/
│   │   ├── PrescriptionRepository.java
│   │   └── PatientRepository.java
│   ├── entity/
│   │   ├── Prescription.java
│   │   └── Patient.java
│   ├── dto/
│   │   ├── PrescriptionRequestDto.java
│   │   └── PrescriptionResponseDto.java
│   ├── exception/
│   │   ├── NotFoundException.java
│   │   ├── ValidationException.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── ErrorResponse.java
│   ├── event/
│   │   ├── PrescriptionCreatedEvent.java
│   │   └── PrescriptionEventListener.java
│   └── config/
│       └── AsyncConfiguration.java
└── src/test/java/com/example/prescriptions/
    └── service/
        └── PrescriptionServiceTest.java
```



# Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd prescription-service
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Running Tests

```bash
mvn test
```

# API Endpoints

## Core Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/prescriptions` | Create a new prescription |
| GET | `/prescriptions/{id}` | Get prescription by ID |
| GET | `/prescriptions/patient/{patientId}` | Get all prescriptions for a patient |
| PUT | `/prescriptions/{id}` | Update a prescription |
| DELETE | `/prescriptions/{id}` | Delete a prescription |

## Filtering Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/prescriptions/filter/doctor?doctorName={name}` | Filter by doctor name |
| GET | `/prescriptions/filter/date-range?startDate={date}&endDate={date}` | Filter by date range |

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON specification:
```
http://localhost:8080/api-docs
```

# Database

**Connection details:**
- JDBC URL: `jdbc:postgresql://localhost:5432/postgres`
- Username: postgres
- Password: <password>



### Get Prescription by ID

```bash
curl http://localhost:8080/prescriptions/1
```

### Get All Prescriptions for a Patient

```bash
curl http://localhost:8080/prescriptions/patient/1
```

### Delete a Prescription

```bash
curl -X DELETE http://localhost:8080/prescriptions/1
```

###  Filter by Doctor

```bash
curl "http://localhost:8080/prescriptions/filter/doctor?doctorName=Dr.%20Aiym"
```

### Filter by Date Range

```bash
curl "http://localhost:8080/prescriptions/filter/date-range?startDate=2026-01-01&endDate=2026-01-31"
```

## Author

**Aknur Mazhitova**  
Software Engineering student  
Java / Spring Boot developer

 Kazakhstan   
Email: aknurmazhitova13@gmail.com 
GitHub: https://github.com/WhiteeRay
