# View My Course Use Case - Implementation Guide

## Overview
This implementation provides the complete "View My Course" use case for students based on the class diagram. Students can view their enrolled courses, filter by keywords, and sort by various criteria.

## Architecture

### Entities
- **Student**: Student information (studentId, studentCode, fullName, email, dateOfBirth)
- **Teacher**: Teacher information (teacherId, teacherCode, fullName, email, department)
- **Course**: Course information (courseId, courseCode, courseName, schedule, teacher)
- **StudentCourseDetails**: Enrollment relationship (enrollmentId, student, course, enrollmentDate, status)

### Repositories
- **CourseRepository**: Data access for Course entities
  - `findCoursesByStudentId(studentId)`: Get active courses for a student
  - `findCourseWithTeacher(courseId)`: Get course with teacher details

### Services
- **CourseService**: Business logic layer
  - `getEnrolledCourses(studentId)`: Retrieve all enrolled courses
  - `filterCourses(courses, keyword)`: Filter by course code, name, or teacher
  - `sortCourses(courses, option)`: Sort by name, code, teacher, or schedule
  - `convertToViews(courses)`: Convert entities to DTOs
  - `getStudentCourses(studentId, keyword, sortOption)`: Complete workflow

### Controllers
- **CourseController**: REST API endpoints
  - `GET /api/courses/student/{studentId}`: View all courses
  - `GET /api/courses/student/{studentId}/filter?keyword={keyword}`: Filter courses
  - `GET /api/courses/student/{studentId}/sort?option={option}`: Sort courses
  - `GET /api/courses/student/{studentId}/view?keyword={keyword}&sort={sort}`: Combined endpoint
  - `GET /api/courses/{courseId}`: Get course details

### DTOs
- **CourseView**: View model for displaying course information
  - courseCode, courseName, teacherName, schedule, totalCourses
- **ApiResponse**: Standard API response wrapper
  - success, message, data

## Setup Instructions

### 1. Database Configuration
Update `application.properties` with your PostgreSQL credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/student_management
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. Create Database
```sql
CREATE DATABASE student_management;
```

### 3. Run the Application
```bash
./mvnw clean install
./mvnw spring-boot:run
```

The application will:
- Create tables automatically (via `spring.jpa.hibernate.ddl-auto=update`)
- Insert sample data from `data.sql`

## API Endpoints

### View All Courses for a Student
```http
GET http://localhost:8080/api/courses/student/1
```

**Response:**
```json
{
  "success": true,
  "message": "Found 3 course(s)",
  "data": [
    {
      "courseCode": "CS101",
      "courseName": "Introduction to Programming",
      "teacherName": "Dr. Nguyen Van A",
      "schedule": "Mon-Wed 9:00-11:00",
      "totalCourses": 3
    },
    ...
  ]
}
```

### Filter Courses by Keyword
```http
GET http://localhost:8080/api/courses/student/1/filter?keyword=programming
```

**Response:** Returns courses matching "programming" in code, name, or teacher name

### Sort Courses
```http
GET http://localhost:8080/api/courses/student/1/sort?sortOption=name
```

**Sort Options:**
- `name`: Sort by course name
- `code`: Sort by course code
- `teacher`: Sort by teacher name
- `schedule`: Sort by schedule

### Combined Endpoint (Filter + Sort)
```http
GET http://localhost:8080/api/courses/student/1/view?keyword=CS&sortOption=code
```

### Get Course Details
```http
GET http://localhost:8080/api/courses/1
```

## Testing

### Sample Data
The application includes sample data with:
- 3 Teachers (T001, T002, T003)
- 3 Students (S001, S002, S003)
- 5 Courses (CS101, CS102, MATH201, PHYS101, CS201)
- Multiple enrollments with ACTIVE and COMPLETED status

### Test Scenarios

1. **View all courses for student S001 (ID: 1)**
   ```bash
   curl http://localhost:8080/api/courses/student/1
   ```

2. **Filter courses by "CS"**
   ```bash
   curl "http://localhost:8080/api/courses/student/1/filter?keyword=CS"
   ```

3. **Sort courses by name**
   ```bash
   curl "http://localhost:8080/api/courses/student/1/sort?sortOption=name"
   ```

4. **Filter and sort combined**
   ```bash
   curl "http://localhost:8080/api/courses/student/1/view?keyword=CS&sortOption=code"
   ```

## Implementation Details

### Business Logic Flow
1. **Controller** receives HTTP request with studentId, optional keyword, and sortOption
2. **Service** processes the request:
   - Retrieves enrolled courses from repository
   - Filters courses if keyword provided
   - Sorts courses if sortOption provided
   - Converts entities to DTOs
3. **Controller** returns formatted API response

### Key Features
- ✅ RESTful API design
- ✅ Service layer separation
- ✅ DTO pattern for data transfer
- ✅ Exception handling with custom exceptions
- ✅ Logging for debugging
- ✅ Transaction management
- ✅ Sample data for testing
- ✅ Cross-origin support (CORS)

### Status Filtering
Only courses with `status = 'ACTIVE'` are shown in the student's view. Completed or dropped courses are excluded.

## Technologies Used
- Spring Boot 3.5.7
- Spring Data JPA
- PostgreSQL
- Lombok
- Java 17

## Database Schema

```sql
students
  - student_id (PK)
  - student_code (unique)
  - full_name
  - email (unique)
  - date_of_birth

teachers
  - teacher_id (PK)
  - teacher_code (unique)
  - full_name
  - email (unique)
  - department

courses
  - course_id (PK)
  - course_code (unique)
  - course_name
  - schedule
  - teacher_id (FK)

student_course_details
  - enrollment_id (PK)
  - student_id (FK)
  - course_id (FK)
  - enrollment_date
  - status (ACTIVE, COMPLETED, DROPPED)
```

## Error Handling
- `404 NOT FOUND`: Student or course not found
- `500 INTERNAL SERVER ERROR`: Unexpected errors
- All errors return standard ApiResponse format with `success: false` and error message

## Future Enhancements
- Authentication and authorization
- Pagination for large course lists
- Course enrollment functionality
- Grade viewing
- Assignment tracking
- Export to PDF/Excel
- Email notifications

