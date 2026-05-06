# Spring Boot Employee CRUD (MySQL + Docker + Jenkins)

Production-style Spring Boot CRUD REST API for Employee Management.

## Tech Stack

- Java 17
- Spring Boot (Web + Data JPA + Hibernate)
- Maven
- MySQL
- Docker
- Jenkins CI/CD

## Project Structure

```
src/main/java/com/yashwanthmk/employeeservice/
  controller/
  dto/
  entity/
  exception/
  repository/
  service/
    impl/
```

## Run MySQL using Docker

```bash
docker run -d --name mysql-employee \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=employee_db \
  -p 3306:3306 \
  mysql:8.0
```

If you want persistence:

```bash
docker volume create mysql_employee_data
docker run -d --name mysql-employee \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=employee_db \
  -p 3306:3306 \
  -v mysql_employee_data:/var/lib/mysql \
  mysql:8.0
```

## Configure Database

Edit `src/main/resources/application.properties` if needed:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

This app also supports environment variables (useful for Docker/Jenkins):

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Default uses:

- DB: `employee_db`
- User: `root`
- Pass: `root`

## Run Locally (without Docker)

```bash
mvn clean spring-boot:run
```

App runs on `http://localhost:8080`.

## API Endpoints (Employee CRUD)

Base path: `/api/v1/employees`

- **Create Employee**: `POST /api/v1/employees`
- **Get All Employees**: `GET /api/v1/employees`
- **Get Employee By ID**: `GET /api/v1/employees/{id}`
- **Update Employee**: `PUT /api/v1/employees/{id}`
- **Delete Employee**: `DELETE /api/v1/employees/{id}`

### Sample JSON Request Body (Create/Update)

```json
{
  "firstName": "Yashwanth",
  "lastName": "MK",
  "email": "yashwanth@example.com",
  "department": "Engineering",
  "salary": 75000.00
}
```

### Sample cURL

Create:

```bash
curl -X POST "http://localhost:8080/api/v1/employees" \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Yashwanth","lastName":"MK","email":"yashwanth@example.com","department":"Engineering","salary":75000.00}'
```

Get all:

```bash
curl "http://localhost:8080/api/v1/employees"
```

Get by id:

```bash
curl "http://localhost:8080/api/v1/employees/1"
```

Update:

```bash
curl -X PUT "http://localhost:8080/api/v1/employees/1" \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Yashwanth","lastName":"MK","email":"yashwanth@example.com","department":"Platform","salary":80000.00}'
```

Delete:

```bash
curl -X DELETE "http://localhost:8080/api/v1/employees/1"
```

## Build JAR

```bash
mvn -DskipTests clean package
```

The jar will be in `target/`.

## Docker Commands

### Build Docker image

```bash
docker build -t yashwanthmk/springboot-app:latest .
```

### Run container (mapping 8085:8080)

```bash
docker run -d --name springboot-app -p 8085:8080 yashwanthmk/springboot-app:latest
```

If MySQL is running on the host machine, you can override DB settings:

```bash
docker run -d --name springboot-app -p 8085:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/employee_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  -e DB_USERNAME="root" \
  -e DB_PASSWORD="root" \
  yashwanthmk/springboot-app:latest
```

App URL: `http://localhost:8085/api/v1/employees`

### Push image to DockerHub

```bash
docker login
docker push yashwanthmk/springboot-app:latest
```

## Jenkins CI/CD

`Jenkinsfile` pipeline stages:

- Clone Repository
- Maven Build
- Docker Build
- Docker Login
- Docker Tag
- Docker Push
- Deploy Container (`-p 8085:8080`)

### Jenkins Setup Notes

- Jenkins agent must have **Docker** installed and permission to run Docker commands.
- Create Jenkins credentials:
  - **Kind**: Username with password
  - **ID**: `dockerhub-creds`
  - **Username/Password**: your DockerHub credentials

