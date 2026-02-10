# 🎓 Quine Institute - College Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue?style=for-the-badge&logo=postgresql)
![JWT](https://img.shields.io/badge/JWT-Auth-black?style=for-the-badge&logo=jsonwebtokens)

**A powerful, secure backend system for modern educational institutions**

[Features](#-features) • [Installation](#-installation--setup) • [API Documentation](#-api-documentation) • [Tech Stack](#-tech-stack)

</div>

---

## 📋 Table of Contents

- [About](#-about)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Environment Configuration](#-environment-configuration)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Security](#-security)
- [Contributing](#-contributing)
- [Download Documentation](#-download-documentation)

---

## 🌟 About

**Quine Institute Backend** is a comprehensive college management system built with Spring Boot 4.0 and PostgreSQL. It handles everything from student admissions to exam results, with robust JWT-based authentication and role-based access control.

This system is designed to streamline administrative tasks and provide a seamless experience for students, faculty, and administrators.

---

## ✨ Features

### 🔐 Authentication & Security
- JWT-based authentication with secure token generation
- Role-based access control (STUDENT, FACULTY, ADMIN)
- Password encryption with BCrypt
- Protected endpoints with Spring Security

### 👨‍🎓 Student Management
- Student registration and admission workflow
- Profile management with role-based access
- Fee tracking and payment verification
- Mentor assignment

### 📚 Academic Features
- Program and semester management
- Subject allocation
- Exam result management
- Notice board system with archive

### 👥 Staff Management
- Employee/Faculty registration
- Department management
- Mentor-student mapping

### 💳 Payment System
- Fee status tracking
- Payment verification
- Automated fee calculation

---

## 🛠 Tech Stack

### Backend Framework
- **Java 21** - Latest LTS version
- **Spring Boot 4.0.0** - Core framework
- **Spring Web MVC** - RESTful API development
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication & Authorization

### Database
- **PostgreSQL** - Primary database
- **Hibernate** - ORM with auto DDL

### Security & Authentication
- **JWT (JSON Web Tokens)** - Token-based auth
- **BCrypt** - Password hashing

### Build Tool
- **Maven** - Dependency management

### Libraries
- **Lombok** - Boilerplate code reduction
- **JJWT** - JWT implementation

---

## 📦 Prerequisites

Before running this project, ensure you have:

- ☕ **Java Development Kit (JDK) 21** or higher
- 🐘 **PostgreSQL 12+** installed and running
- 📦 **Maven 3.8+** (or use the included Maven wrapper)
- 🔧 **Git** for version control
- 💻 Any IDE (IntelliJ IDEA recommended, VS Code, Eclipse)

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/quine-institute.git
cd quine-institute/backend
```

### Step 2: Configure PostgreSQL Database

1. **Create a new database:**

```sql
CREATE DATABASE quine_db;
```

2. **Update database credentials** (see [Environment Configuration](#-environment-configuration))

### Step 3: Configure Application Properties

1. Navigate to `src/main/resources/`
2. Copy `application.properties.example` to `application.properties`
3. Update the following properties:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/quine_db
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password

# JWT Secret Key (Generate a strong secret)
application.security.jwt.secret-key=your-super-secret-jwt-key-here

# Spring Security Credentials
spring.security.user.name=admin
spring.security.user.password=admin123
```

> ⚠️ **Security Warning:** Never commit `application.properties` to version control! Use environment variables in production.

### Step 4: Build the Project

Using Maven wrapper (recommended):

```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

Or using system Maven:

```bash
mvn clean install
```

### Step 5: Run the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

### Step 6: Verify Installation

Test the health of your API:

```bash
curl http://localhost:8080/api/v1/programs
```

---

## ⚙️ Environment Configuration

### application.properties.example

Create this file as a template for your configuration:

```properties
# Application Name
spring.application.name=quine-backend

# Database Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Security
spring.security.user.name=admin
spring.security.user.password=secure_password

# JWT Configuration
application.security.jwt.secret-key=your-256-bit-secret-key-here
```

### .gitignore Configuration

Ensure your `.gitignore` includes:

```
application.properties
*.log
target/
.idea/
*.iml
```

---

## 📖 API Documentation

Base URL: `http://localhost:8080/api/v1`

### 🔐 Authentication Endpoints

#### 1. User Login

**POST** `/auth/login`

**Request Body:**
```json
{
  "username": "2024BCA001",
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "Login Successful!",
  "role": "STUDENT",
  "username": "2024BCA001",
  "name": "Harsh Sharma",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Error Response:**
```json
{
  "message": "User nahi mila! ID check karo."
}
```

---

### 🎓 Admission Endpoints

#### 1. Apply for Admission (Public - No Auth Required)

**POST** `/applications/apply`

**Request Body:**
```json
{
  "name": "Rahul Kumar",
  "email": "rahul@example.com",
  "mobileNumber": "9876543210",
  "fatherName": "Suresh Kumar",
  "motherName": "Priya Kumar",
  "desiredProgram": {
    "id": 1
  }
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Rahul Kumar",
  "email": "rahul@example.com",
  "mobileNumber": "9876543210",
  "fatherName": "Suresh Kumar",
  "motherName": "Priya Kumar",
  "status": "PENDING",
  "applicationDate": "2025-02-10T14:30:00",
  "desiredProgram": {
    "id": 1,
    "name": "BCA"
  }
}
```

#### 2. Get Pending Applications (ADMIN Only)

**GET** `/applications/pending`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Rahul Kumar",
    "email": "rahul@example.com",
    "status": "PENDING",
    "applicationDate": "2025-02-10T14:30:00"
  }
]
```

#### 3. Approve Application (ADMIN Only)

**PUT** `/applications/approve/{applicationId}`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "id": 1,
  "studentId": "2025BCA001",
  "name": "Rahul Kumar",
  "email": "rahul@example.com",
  "mobileNumber": "9876543210",
  "semester": {
    "id": 1,
    "semesterNumber": 1
  },
  "assignedSection": "A",
  "joiningDate": "2025-02-10T14:35:00",
  "totalFees": 50000.0,
  "feesPaid": 0.0
}
```

---

### 👨‍🎓 Student Endpoints

#### 1. Add Student (ADMIN Only)

**POST** `/students`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "studentId": "2025BCA001",
  "name": "Harsh Sharma",
  "email": "harsh@example.com",
  "mobileNumber": "9876543210",
  "fatherName": "Rajesh Sharma",
  "motherName": "Sunita Sharma",
  "semester": {
    "id": 1
  },
  "assignedSection": "A"
}
```

**Response:**
```json
{
  "id": 1,
  "studentId": "2025BCA001",
  "name": "Harsh Sharma",
  "email": "harsh@example.com",
  "mobileNumber": "9876543210",
  "fatherName": "Rajesh Sharma",
  "motherName": "Sunita Sharma",
  "semester": {
    "id": 1,
    "semesterNumber": 1
  },
  "assignedSection": "A",
  "joiningDate": "2025-02-10T10:00:00",
  "totalFees": 50000.0,
  "feesPaid": 0.0
}
```

#### 2. Get All Students (ADMIN, FACULTY)

**GET** `/students`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
[
  {
    "id": 1,
    "studentId": "2025BCA001",
    "name": "Harsh Sharma",
    "email": "harsh@example.com",
    "semester": {
      "id": 1,
      "semesterNumber": 1
    },
    "assignedSection": "A"
  }
]
```

#### 3. Get Student by ID (All authenticated users)

**GET** `/students/{studentId}`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "id": 1,
  "studentId": "2025BCA001",
  "name": "Harsh Sharma",
  "email": "harsh@example.com",
  "mobileNumber": "9876543210",
  "fatherName": "Rajesh Sharma",
  "motherName": "Sunita Sharma",
  "semester": {
    "id": 1,
    "semesterNumber": 1
  },
  "assignedSection": "A",
  "totalFees": 50000.0,
  "feesPaid": 15000.0
}
```

**Error (if student tries to access other's profile):**
```json
{
  "message": "Access Denied: Aap sirf apni profile dekh sakte hain."
}
```

---

### 👔 Employee Endpoints

#### 1. Add Employee (ADMIN Only)

**POST** `/employees`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "employeeId": "EMP001",
  "name": "Dr. Rajesh Kumar",
  "email": "rajesh@quineinstitute.edu",
  "department": "COMPUTER_SCIENCE",
  "position": "Professor",
  "mobileNumber": "9876543210"
}
```

**Response:**
```json
{
  "id": 1,
  "employeeId": "EMP001",
  "name": "Dr. Rajesh Kumar",
  "email": "rajesh@quineinstitute.edu",
  "department": "COMPUTER_SCIENCE",
  "position": "Professor",
  "mobileNumber": "9876543210",
  "joiningDate": "2025-02-10T10:00:00"
}
```

#### 2. Get All Employees

**GET** `/employees`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
[
  {
    "id": 1,
    "employeeId": "EMP001",
    "name": "Dr. Rajesh Kumar",
    "department": "COMPUTER_SCIENCE",
    "position": "Professor"
  }
]
```

#### 3. Get Employee by ID

**GET** `/employees/{id}`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "id": 1,
  "employeeId": "EMP001",
  "name": "Dr. Rajesh Kumar",
  "email": "rajesh@quineinstitute.edu",
  "department": "COMPUTER_SCIENCE",
  "position": "Professor",
  "mobileNumber": "9876543210"
}
```

---

### 📚 Program Endpoints

#### 1. Create Program (ADMIN Only)

**POST** `/programs`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "name": "BCA",
  "code": "BCA",
  "duration": 3,
  "totalSemesters": 6
}
```

**Response:**
```json
{
  "id": 1,
  "name": "BCA",
  "code": "BCA",
  "duration": 3,
  "totalSemesters": 6
}
```

#### 2. Get All Programs

**GET** `/programs`

**Response:**
```json
[
  {
    "id": 1,
    "name": "BCA",
    "code": "BCA",
    "duration": 3,
    "totalSemesters": 6
  },
  {
    "id": 2,
    "name": "MCA",
    "code": "MCA",
    "duration": 2,
    "totalSemesters": 4
  }
]
```

---

### 📅 Semester Endpoints

#### 1. Create Semester (ADMIN Only)

**POST** `/semesters`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "semesterNumber": 1,
  "program": {
    "id": 1
  }
}
```

**Response:**
```json
{
  "id": 1,
  "semesterNumber": 1,
  "program": {
    "id": 1,
    "name": "BCA",
    "code": "BCA"
  }
}
```

#### 2. Get All Semesters

**GET** `/semesters`

**Response:**
```json
[
  {
    "id": 1,
    "semesterNumber": 1,
    "program": {
      "id": 1,
      "name": "BCA"
    }
  }
]
```

#### 3. Get Semester by ID

**GET** `/semesters/{id}`

**Response:**
```json
{
  "id": 1,
  "semesterNumber": 1,
  "program": {
    "id": 1,
    "name": "BCA",
    "totalSemesters": 6
  }
}
```

---

### 📖 Subject Endpoints

#### 1. Create Subject (ADMIN Only)

**POST** `/subjects`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "name": "Data Structures",
  "code": "CS201",
  "credits": 4,
  "program": {
    "id": 1
  },
  "semester": {
    "id": 1
  }
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Data Structures",
  "code": "CS201",
  "credits": 4,
  "program": {
    "id": 1,
    "name": "BCA"
  },
  "semester": {
    "id": 1,
    "semesterNumber": 1
  }
}
```

#### 2. Get All Subjects

**GET** `/subjects`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Data Structures",
    "code": "CS201",
    "credits": 4
  }
]
```

#### 3. Get Subjects by Program

**GET** `/subjects/program/{programId}`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Data Structures",
    "code": "CS201",
    "semester": {
      "semesterNumber": 1
    }
  }
]
```

---

### 📊 Exam Result Endpoints

#### 1. Add Exam Result (FACULTY, ADMIN)

**POST** `/results/add`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "student": {
    "id": 1
  },
  "subject": {
    "id": 1
  },
  "marksObtained": 85,
  "totalMarks": 100,
  "grade": "A"
}
```

**Response:**
```json
{
  "id": 1,
  "student": {
    "id": 1,
    "studentId": "2025BCA001",
    "name": "Harsh Sharma"
  },
  "subject": {
    "id": 1,
    "name": "Data Structures",
    "code": "CS201"
  },
  "marksObtained": 85,
  "totalMarks": 100,
  "grade": "A",
  "percentage": 85.0,
  "examDate": "2025-02-10T10:00:00"
}
```

#### 2. Get Student Results

**GET** `/results/student/{studentId}`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
[
  {
    "id": 1,
    "subject": {
      "name": "Data Structures",
      "code": "CS201"
    },
    "marksObtained": 85,
    "totalMarks": 100,
    "grade": "A",
    "percentage": 85.0
  }
]
```

---

### 👨‍🏫 Mentor Endpoints

#### 1. Assign Mentor (ADMIN Only)

**POST** `/mentors`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "faculty": {
    "id": 1
  },
  "student": {
    "id": 1
  }
}
```

**Response:**
```json
{
  "id": 1,
  "faculty": {
    "id": 1,
    "name": "Dr. Rajesh Kumar",
    "employeeId": "EMP001"
  },
  "student": {
    "id": 1,
    "studentId": "2025BCA001",
    "name": "Harsh Sharma"
  },
  "assignedDate": "2025-02-10T10:00:00"
}
```

#### 2. Find Mentor

**GET** `/mentors/find?studentId={studentId}&facultyId={facultyId}`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "id": 1,
  "faculty": {
    "name": "Dr. Rajesh Kumar"
  },
  "student": {
    "name": "Harsh Sharma"
  },
  "assignedDate": "2025-02-10T10:00:00"
}
```

---

### 📢 Notice Endpoints

#### 1. Create Notice (ADMIN, FACULTY)

**POST** `/notices`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "title": "Mid-Semester Exam Schedule",
  "content": "Mid-semester exams will be conducted from 20th Feb to 25th Feb 2025.",
  "targetAudience": "ALL"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Mid-Semester Exam Schedule",
  "content": "Mid-semester exams will be conducted from 20th Feb to 25th Feb 2025.",
  "targetAudience": "ALL",
  "postedDate": "2025-02-10T10:00:00",
  "isActive": true
}
```

#### 2. Get All Active Notices

**GET** `/notices`

**Response:**
```json
[
  {
    "id": 1,
    "title": "Mid-Semester Exam Schedule",
    "content": "Mid-semester exams will be conducted from 20th Feb to 25th Feb 2025.",
    "postedDate": "2025-02-10T10:00:00",
    "isActive": true
  }
]
```

#### 3. Get Archived Notices

**GET** `/notices/archive`

**Response:**
```json
[
  {
    "id": 1,
    "title": "Previous Notice",
    "archivedDate": "2025-01-15T10:00:00"
  }
]
```

---

### 💳 Payment Endpoints

#### 1. Get Payment Status

**GET** `/payments/status/{studentId}`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "studentId": "2025BCA001",
  "studentName": "Harsh Sharma",
  "totalFees": 50000.0,
  "feesPaid": 15000.0,
  "remainingFees": 35000.0,
  "paymentStatus": "PARTIAL"
}
```

#### 2. Verify Payment (ADMIN Only)

**POST** `/payments/verify`

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Request Body:**
```json
{
  "studentId": "2025BCA001",
  "amount": 10000.0,
  "paymentMode": "UPI",
  "transactionId": "TXN123456789"
}
```

**Response:**
```json
{
  "message": "Payment verified successfully",
  "studentId": "2025BCA001",
  "totalPaid": 25000.0,
  "remainingFees": 25000.0,
  "transactionId": "TXN123456789"
}
```

---

## 📂 Project Structure

```
Quine Institute/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/quine/backend/
│   │   │   │   ├── config/
│   │   │   │   │   └── SecurityConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── StudentController.java
│   │   │   │   │   ├── EmployeeController.java
│   │   │   │   │   ├── AdmissionController.java
│   │   │   │   │   ├── ProgramController.java
│   │   │   │   │   ├── SemesterController.java
│   │   │   │   │   ├── SubjectController.java
│   │   │   │   │   ├── ExamResultController.java
│   │   │   │   │   ├── MentorController.java
│   │   │   │   │   ├── NoticeController.java
│   │   │   │   │   └── PaymentController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   └── LoginResponse.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Student.java
│   │   │   │   │   ├── Employee.java
│   │   │   │   │   ├── Application.java
│   │   │   │   │   ├── Program.java
│   │   │   │   │   ├── Semester.java
│   │   │   │   │   ├── Subject.java
│   │   │   │   │   ├── ExamResult.java
│   │   │   │   │   ├── Mentor.java
│   │   │   │   │   ├── Notice.java
│   │   │   │   │   ├── NoticeArchive.java
│   │   │   │   │   ├── Role.java
│   │   │   │   │   └── Department.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── StudentRepository.java
│   │   │   │   │   ├── EmployeeRepository.java
│   │   │   │   │   ├── ApplicationRepository.java
│   │   │   │   │   ├── ProgramRepository.java
│   │   │   │   │   ├── SemesterRepository.java
│   │   │   │   │   ├── SubjectRepository.java
│   │   │   │   │   ├── ExamResultRepository.java
│   │   │   │   │   ├── MentorRepository.java
│   │   │   │   │   ├── NoticeRepository.java
│   │   │   │   │   └── NoticeArchiveRepository.java
│   │   │   │   ├── security/
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   └── JwtService.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   │   ├── StudentService.java
│   │   │   │   │   ├── EmployeeService.java
│   │   │   │   │   ├── ApplicationService.java
│   │   │   │   │   ├── ProgramService.java
│   │   │   │   │   ├── SemesterService.java
│   │   │   │   │   ├── SubjectService.java
│   │   │   │   │   ├── ExamResultService.java
│   │   │   │   │   ├── MentorService.java
│   │   │   │   │   └── NoticeService.java
│   │   │   │   └── BackendApplication.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── application.properties.example
│   │   └── test/
│   ├── target/
│   ├── .gitignore
│   ├── pom.xml
│   └── mvnw
│
└── README.md
```

---

## 🔒 Security

### Authentication Flow

1. User sends credentials to `/api/v1/auth/login`
2. Backend validates credentials
3. If valid, JWT token is generated and returned
4. Client stores token (localStorage/sessionStorage)
5. For protected routes, client sends token in Authorization header:
   ```
   Authorization: Bearer <jwt_token>
   ```

### Role-Based Access Control

| Role | Permissions |
|------|-------------|
| **STUDENT** | View own profile, View notices, View own results, View fee status |
| **FACULTY** | All STUDENT permissions + Add results, View all students, Create notices |
| **ADMIN** | All permissions + Manage users, Approve admissions, Manage programs |

### Security Best Practices

- ✅ All passwords are encrypted using BCrypt
- ✅ JWT tokens expire after configured time
- ✅ Sensitive data (passwords, tokens) are never logged
- ✅ CORS is configured for secure cross-origin requests
- ✅ SQL injection prevention via JPA/Hibernate
- ✅ Input validation on all endpoints

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java naming conventions
- Write clean, documented code
- Add unit tests for new features
- Update API documentation for new endpoints
- Test thoroughly before submitting PR

---

## 📥 Download Documentation

For detailed API documentation, architecture diagrams, and implementation guides:

**[📄 Download Complete Documentation (PDF)](https://github.com/your-username/quine-institute/releases/download/v1.0/Quine-Institute-Documentation.pdf)**

---

## 📞 Support & Contact

- 🐛 **Issues:** [GitHub Issues](https://github.com/your-username/quine-institute/issues)
- 💬 **Discussions:** [GitHub Discussions](https://github.com/your-username/quine-institute/discussions)
- 📧 **Email:** support@quineinstitute.com

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Spring Boot Team for the amazing framework
- PostgreSQL community
- All contributors and testers

---

<div align="center">

### Made in India 🇮🇳 with ❤️ by [Ryu](https://ryuverse.fun)

**⭐ Star this repo if you found it helpful!**

</div>
