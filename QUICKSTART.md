# Quick Start Guide - View My Course Use Case

## Bước 1: Chuẩn bị Database

### Tạo PostgreSQL database:
```sql
CREATE DATABASE student_management;
```

### Cập nhật thông tin database trong `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/student_management
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## Bước 2: Chạy Application

### Sử dụng Maven:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Hoặc sử dụng IDE:
- Mở project trong IntelliJ IDEA hoặc Eclipse
- Chạy class `StudentManagementApplication.java`

## Bước 3: Test API

Application sẽ chạy tại: `http://localhost:8080`

### 1. Xem tất cả khóa học của sinh viên (Student ID = 1)
```bash
curl http://localhost:8080/api/courses/student/1
```

**Kết quả:**
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
    {
      "courseCode": "CS102",
      "courseName": "Data Structures",
      "teacherName": "Dr. Nguyen Van A",
      "schedule": "Tue-Thu 13:00-15:00",
      "totalCourses": 3
    },
    {
      "courseCode": "MATH201",
      "courseName": "Calculus I",
      "teacherName": "Prof. Tran Thi B",
      "schedule": "Mon-Wed-Fri 7:30-9:00",
      "totalCourses": 3
    }
  ]
}
```

### 2. Lọc khóa học theo keyword "CS"
```bash
curl "http://localhost:8080/api/courses/student/1/filter?keyword=CS"
```

### 3. Sắp xếp khóa học theo tên
```bash
curl "http://localhost:8080/api/courses/student/1/sort?sortOption=name"
```

**Các tùy chọn sắp xếp:**
- `name` - Sắp xếp theo tên khóa học
- `code` - Sắp xếp theo mã khóa học
- `teacher` - Sắp xếp theo tên giảng viên
- `schedule` - Sắp xếp theo lịch học

### 4. Kết hợp lọc và sắp xếp
```bash
curl "http://localhost:8080/api/courses/student/1/view?keyword=CS&sortOption=code"
```

### 5. Xem chi tiết khóa học (Course ID = 1)
```bash
curl http://localhost:8080/api/courses/1
```

## Cấu trúc API Endpoints

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/courses/student/{studentId}` | Xem tất cả khóa học của sinh viên |
| GET | `/api/courses/student/{studentId}/filter?keyword={keyword}` | Lọc khóa học theo từ khóa |
| GET | `/api/courses/student/{studentId}/sort?sortOption={option}` | Sắp xếp khóa học |
| GET | `/api/courses/student/{studentId}/view?keyword={keyword}&sort={sort}` | Kết hợp lọc và sắp xếp |
| GET | `/api/courses/{courseId}` | Chi tiết khóa học |

## Dữ liệu mẫu

### Students (Sinh viên)
- **S001** - Hoang Nguyen Trong (ID: 1)
- **S002** - Minh Pham Van (ID: 2)
- **S003** - Linh Vo Thi (ID: 3)

### Teachers (Giảng viên)
- **T001** - Dr. Nguyen Van A (Computer Science)
- **T002** - Prof. Tran Thi B (Mathematics)
- **T003** - Dr. Le Van C (Physics)

### Courses (Khóa học)
- **CS101** - Introduction to Programming
- **CS102** - Data Structures
- **MATH201** - Calculus I
- **PHYS101** - General Physics
- **CS201** - Algorithms

## Test với Postman

1. Import các endpoint sau vào Postman:
   - Base URL: `http://localhost:8080`
   - Add prefix: `/api/courses`

2. Test từng endpoint theo thứ tự trên

3. Kiểm tra response format và data

## Troubleshooting

### Lỗi kết nối database:
```
Connection refused
```
**Giải pháp:** Kiểm tra PostgreSQL đã chạy và thông tin kết nối trong `application.properties`

### Lỗi "Student not found":
```json
{
  "success": false,
  "message": "Student not found with ID: X"
}
```
**Giải pháp:** Sử dụng student ID từ 1-3 (dữ liệu mẫu)

### Tables không được tạo:
**Giải pháp:** Kiểm tra `spring.jpa.hibernate.ddl-auto=update` trong `application.properties`

## Logs

Xem logs trong console để debug:
```
INFO  - Fetching enrolled courses for student ID: 1
INFO  - Found 3 courses for student S001
INFO  - Filtering courses with keyword: CS
INFO  - Sorting courses by: name
```

## Next Steps

Sau khi test thành công, bạn có thể:
1. Thêm authentication/authorization
2. Tạo frontend UI (React, Angular, Vue)
3. Thêm pagination cho danh sách lớn
4. Implement các use case khác (Grade Student, Manage Assignment, etc.)

---

**Chúc mừng! 🎉** Bạn đã hoàn thành implement use case "View My Course" theo class diagram!

