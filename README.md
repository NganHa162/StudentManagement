# Student Management System

A comprehensive web-based Student Management System built with Spring Boot, PostgreSQL, and Thymeleaf templates.

## Features

### For Administrators
- Manage students, teachers, and courses
- Create and assign courses to teachers
- Enroll students in courses
- View system-wide statistics

### For Teachers
- View assigned courses and enrolled students
- Create and manage assignments
- Track assignment submissions and completion status
- Grade students (score, max score, letter grade)
- View course analytics

### For Students
- View enrolled courses
- Access course assignments
- Track assignment deadlines and status
- View grades and feedback
- Dashboard with upcoming assignments

## System Requirements

- **Java**: JDK 17 or higher
- **Maven**: 3.6+ (or use included Maven Wrapper)
- **PostgreSQL**: 15+
- **Port Requirements**:
  - PostgreSQL: 5433
  - Application: 8081

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd StudentManagement
```

### 2. Set Up PostgreSQL Database

#### Option A: Using Docker (Recommended)

```bash
# Start PostgreSQL container
docker-compose up -d

# Verify container is running
docker-compose ps

# View logs if needed
docker-compose logs -f postgres
```

#### Option B: Manual PostgreSQL Installation

1. Install PostgreSQL 15+ on your system
2. Ensure PostgreSQL is running on port 5433
3. Create the database:

```bash
createdb -U postgres studentdb
```

### 3. Initialize Database

Run the initialization script to create tables and insert sample data:

```bash
# Using Docker
docker exec -i studentmanagement-postgres psql -U postgres -d studentdb < init.sql

# OR using local PostgreSQL
psql -h localhost -p 5433 -U postgres -d studentdb -f init.sql
```

**Important**: You MUST run this script before starting the application!

### 4. Configure Database Connection (Optional)

Default configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/studentdb?options=-c%20timezone=UTC
spring.datasource.username=postgres
spring.datasource.password=postgres
server.port=8081
```

Update these values if your PostgreSQL setup differs.

### 5. Build the Application

#### Using Maven Wrapper (Recommended)

**Windows:**
```bash
mvnw.cmd clean install
```

**Linux/Mac:**
```bash
./mvnw clean install
```

#### Using System Maven

```bash
mvn clean install
```

### 6. Run the Application

#### Option A: Using Maven Wrapper

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

#### Option B: Using System Maven

```bash
mvn spring-boot:run
```

#### Option C: Run JAR File

```bash
java -jar target/StudentManagement-0.0.1-SNAPSHOT.jar
```

#### Option D: Run from IDE

1. Open project in IntelliJ IDEA / Eclipse / VS Code
2. Locate `StudentManagementApplication.java`
3. Right-click → Run

### 7. Access the Application

Once started, access the application at:

- **Application URL**: http://localhost:8081
- **Login Page**: http://localhost:8081/login

## Default Login Credentials

The application supports login with **both username and email**.

### Admin Account
- **Email/Username**: `admin@example.com` OR `admin`
- **Password**: `admin123`

### Teacher Accounts
- **Teacher 1**:
  - Email/Username: `teacher1@example.com` OR `teacher11`
  - Password: `teacher123`
- **Teacher 2**:
  - Email/Username: `teacher2@example.com` OR `teacher21`
  - Password: `teacher123`

### Student Accounts
- **Student 1**:
  - Email/Username: `student1@example.com` OR `student11`
  - Password: `student123`
- **Student 2**:
  - Email/Username: `student2@example.com` OR `student21`
  - Password: `student123`
- **Student 3**:
  - Email/Username: `student3@example.com` OR `student31`
  - Password: `student123`

## Application Structure

```
StudentManagement/
├── src/
│   ├── main/
│   │   ├── java/org/example/studentmanagement/
│   │   │   ├── controller/      # MVC Controllers
│   │   │   ├── service/         # Business Logic Layer
│   │   │   ├── dao/             # Data Access Objects (JDBC)
│   │   │   ├── entity/          # Entity/Model Classes
│   │   │   ├── security/        # Spring Security Configuration
│   │   │   └── config/          # Application Configuration
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/       # Thymeleaf HTML Templates
│   │       └── static/          # CSS, JS, Images
│   └── test/                    # Unit and Integration Tests
├── init.sql                     # Database initialization script
├── docker-compose.yaml          # Docker configuration
├── pom.xml                      # Maven dependencies
└── README.md                    # This file
```

## Key Features Details

### Assignment Management
- Teachers can create assignments with titles, descriptions, due dates, and max scores
- Automatic calculation of days remaining until due date
- Track completion status for each student
- Mark assignments as complete/incomplete

### Grade Management
- Teachers can assign grades with:
  - Numeric score (e.g., 95.5)
  - Max score (e.g., 100)
  - Letter grade (A, B, C, D, F)
  - Feedback comments
- Students can view their grades per course
- Automatic percentage calculation

### Dashboard Features
- **Teacher Dashboard**: View all assigned courses, quick access to course management
- **Student Dashboard**: View enrolled courses and upcoming assignments
- Course cards with hover effects and visual appeal

### Security
- Role-based access control (ADMIN, TEACHER, STUDENT)
- Login with username OR email
- Password encryption support (BCrypt)
- CSRF protection enabled

## Useful Commands

### Docker Commands

```bash
# Start database
docker-compose up -d

# Stop database
docker-compose down

# Stop and remove all data
docker-compose down -v

# View logs
docker-compose logs -f postgres

# Access PostgreSQL shell
docker exec -it studentmanagement-postgres psql -U postgres -d studentdb
```

### Maven Commands

```bash
# Clean build
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Package without tests
mvn clean package -DskipTests

# Clean project
mvn clean
```

### Database Commands

```bash
# Connect to database
psql -h localhost -p 5433 -U postgres -d studentdb

# Run init script
psql -h localhost -p 5433 -U postgres -d studentdb -f init.sql

# Backup database
pg_dump -h localhost -p 5433 -U postgres studentdb > backup.sql

# Restore database
psql -h localhost -p 5433 -U postgres -d studentdb < backup.sql
```

## Common Issues and Solutions

### Issue: Cannot connect to database

**Symptoms**:
```
Connection refused: localhost:5433
```

**Solutions**:
1. Check if PostgreSQL is running:
   ```bash
   docker-compose ps
   ```
2. Verify port 5433 is not in use:
   ```bash
   # Windows
   netstat -ano | findstr :5433

   # Linux/Mac
   lsof -i :5433
   ```
3. Check `application.properties` configuration

### Issue: Port 8081 already in use

**Solution**: Change port in `application.properties`:
```properties
server.port=8082
```

### Issue: Timezone error

**Symptoms**:
```
FATAL: invalid value for parameter "TimeZone"
```

**Solution**: The application is configured to use UTC timezone. Ensure the JDBC URL includes:
```
?options=-c%20timezone=UTC
```

### Issue: Login fails with correct credentials

**Possible causes**:
1. Database not initialized - run `init.sql`
2. Email/username mismatch - try both email and username
3. Check if users exist in database:
   ```sql
   SELECT * FROM students;
   SELECT * FROM teachers;
   SELECT * FROM admins;
   ```

### Issue: 404 or 500 errors

**Solutions**:
1. Check application logs for stack traces
2. Verify all database tables exist
3. Ensure foreign key relationships are intact
4. Clear browser cache and cookies

## Development

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TeacherControllerTest

# Run with coverage
mvn test jacoco:report
```

### Building for Production

```bash
# Create production JAR
mvn clean package -DskipTests

# JAR will be created at:
# target/StudentManagement-0.0.1-SNAPSHOT.jar

# Run production JAR
java -jar target/StudentManagement-0.0.1-SNAPSHOT.jar
```

### Code Style

The project follows standard Java conventions:
- Package naming: lowercase
- Class naming: PascalCase
- Method naming: camelCase
- Constants: UPPER_SNAKE_CASE

## Technology Stack

- **Backend**: Spring Boot 3.x
- **Security**: Spring Security 6.x
- **Database**: PostgreSQL 15+
- **Data Access**: JDBC with DAO pattern
- **Template Engine**: Thymeleaf
- **Frontend**: HTML, CSS, JavaScript
- **Build Tool**: Maven
- **Java Version**: 17

## Database Schema

### Core Tables
- `admins` - Administrator accounts
- `teachers` - Teacher accounts
- `students` - Student accounts
- `courses` - Course information
- `student_course_details` - Student-Course enrollment (junction table)
- `assignments` - Course assignments
- `assignment_details` - Assignment submission tracking
- `grade_details` - Student grades

### Key Relationships
- Teacher → Courses (one-to-many)
- Course → Students (many-to-many via student_course_details)
- Course → Assignments (one-to-many)
- Assignment → Assignment Details (one-to-many)
- Student + Course → Grade Details (one-to-many)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write/update tests
5. Submit a pull request

## License

This project is for educational purposes.

## Support

For issues or questions:
1. Check this README
2. Review application logs
3. Check PostgreSQL logs: `docker-compose logs -f postgres`
4. Verify database connection and data integrity
