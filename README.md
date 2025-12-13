# Student Management System

Hệ thống quản lý học sinh với Spring Boot và PostgreSQL.

## Yêu cầu hệ thống

- **Java**: JDK 17 hoặc cao hơn
- **Maven**: 3.6+ (hoặc sử dụng Maven Wrapper có sẵn)
- **Docker & Docker Compose**: Để chạy PostgreSQL database
- **PostgreSQL**: 15+ (hoặc sử dụng Docker)

## Cài đặt và chạy project

### Bước 1: Clone project

```bash
git clone <repository-url>
cd StudentManagement
```

### Bước 2: Khởi động PostgreSQL Database

Có 2 cách để chạy database:

#### Cách 1: Sử dụng Docker Compose (Khuyến nghị)

```bash
# Khởi động PostgreSQL container
docker-compose up -d

# Kiểm tra container đang chạy
docker-compose ps

# Xem logs nếu cần
docker-compose logs -f postgres
```

#### Cách 2: Cài đặt PostgreSQL trực tiếp

1. Cài đặt PostgreSQL 15+ trên máy
2. Tạo database:

```sql
CREATE DATABASE studentdb;
```

3. Đảm bảo PostgreSQL đang chạy trên port 5432

### Bước 2.5: Tạo Database Schema (QUAN TRỌNG)

**Bạn PHẢI chạy script này trước khi chạy ứng dụng:**

```bash
# Sử dụng Docker
docker exec -i studentmanagement-postgres psql -U postgres -d studentdb < database/schema.sql

# Hoặc kết nối trực tiếp
psql -h localhost -U postgres -d studentdb -f database/schema.sql
```

**Xem chi tiết**: `database/README.md`

### Bước 3: Cấu hình Database (nếu cần)

File cấu hình: `src/main/resources/application.properties`

Mặc định:

- URL: `jdbc:postgresql://localhost:5432/studentdb`
- Username: `postgres`
- Password: `postgres`

Nếu bạn sử dụng cấu hình khác, hãy cập nhật file `application.properties`.

### Bước 4: Build project

#### Sử dụng Maven Wrapper (Khuyến nghị)

**Windows:**

```bash
mvnw.cmd clean install
```

**Linux/Mac:**

```bash
./mvnw clean install
```

#### Hoặc sử dụng Maven đã cài đặt

```bash
mvn clean install
```

### Bước 5: Chạy ứng dụng

#### Cách 1: Sử dụng Maven Wrapper

**Windows:**

```bash
mvnw.cmd spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

#### Cách 2: Sử dụng Maven

```bash
mvn spring-boot:run
```

#### Cách 3: Chạy JAR file

Sau khi build, chạy file JAR:

```bash
java -jar target/StudentManagement-0.0.1-SNAPSHOT.jar
```

#### Cách 4: Chạy từ IDE

1. Mở project trong IntelliJ IDEA / Eclipse / VS Code
2. Tìm file `StudentManagementApplication.java`
3. Click chuột phải → Run

### Bước 6: Truy cập ứng dụng

Sau khi ứng dụng khởi động thành công, truy cập:

- **URL**: http://localhost:8080
- **Login Page**: http://localhost:8080/login

## Các lệnh hữu ích

### Docker Compose

```bash
# Khởi động database
docker-compose up -d

# Dừng database
docker-compose down

# Dừng và xóa dữ liệu
docker-compose down -v

# Xem logs
docker-compose logs -f postgres

# Kiểm tra trạng thái
docker-compose ps
```

### Maven

```bash
# Build project
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run

# Chạy tests
mvn test

# Clean build
mvn clean
```

## Cấu trúc project

```
StudentManagement/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/studentmanagement/
│   │   │       ├── controller/     # REST Controllers
│   │   │       ├── service/        # Business Logic
│   │   │       ├── dao/           # Data Access Objects
│   │   │       ├── entity/        # Entity Classes
│   │   │       ├── security/      # Security Configuration
│   │   │       └── config/         # Configuration Classes
│   │   └── resources/
│   │       ├── application.properties  # Application Config
│   │       └── templates/          # Thymeleaf Templates
│   └── test/                        # Test Files
├── docker-compose.yaml              # Docker Compose Config
├── pom.xml                          # Maven Dependencies
└── README.md                        # This file
```

## Xử lý lỗi thường gặp

### Lỗi: Cannot connect to database

**Nguyên nhân**: PostgreSQL chưa khởi động hoặc cấu hình sai

**Giải pháp**:

1. Kiểm tra PostgreSQL đang chạy:

```bash
docker-compose ps
```

2. Kiểm tra cấu hình trong `application.properties`

3. Kiểm tra port 5432 có bị chiếm dụng không:

```bash
# Windows
netstat -ano | findstr :5432

# Linux/Mac
lsof -i :5432
```

### Lỗi: Port 8080 already in use

**Giải pháp**: Thay đổi port trong `application.properties`:

```properties
server.port=8081
```

### Lỗi: Java version không đúng

**Nguyên nhân**: Project yêu cầu Java 17

**Giải pháp**:

1. Kiểm tra Java version:

```bash
java -version
```

2. Cài đặt hoặc cập nhật Java 17

## Phát triển

### Chạy tests

```bash
mvn test
```

### Build production JAR

```bash
mvn clean package -DskipTests
```

File JAR sẽ được tạo tại: `target/StudentManagement-0.0.1-SNAPSHOT.jar`

## Tài khoản mặc định

Sau khi chạy `DataInitializer`, các tài khoản mặc định sẽ được tạo. Kiểm tra file `DataInitializer.java` để xem thông tin đăng nhập.

## Hỗ trợ

Nếu gặp vấn đề, vui lòng kiểm tra:

1. Logs của ứng dụng
2. Logs của PostgreSQL container
3. Cấu hình trong `application.properties`
