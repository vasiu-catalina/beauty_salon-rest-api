# Beauty Salon Spring Boot REST API

REST API for managing a beauty salon: clients, employees, services/products, appointments, and reviews. Includes authentication/authorization, OpenAPI docs, and a health endpoint.

**Database Diagram**
![Database Diagram](assets/images/database-diagramm.svg)

**Public Health Endpoint**
- `GET /api/v1/health` → `{ "status": "UP", "timestamp": "..." }`

**OpenAPI Docs**
- `http://localhost:8081/swagger-ui.html` (default)

**Default Port**
- `8081` (configurable via `SERVER_PORT`)

## Quick Start
1. Clone:
```bash
git clone <repo-url>
cd beauty_salon-rest-api
```
2. Configure environment:
```bash
cp .env.example .env
```
3. Run locally:
```bash
./run.sh --run
```

## Configuration (.env)
`application.properties` reads environment variables with defaults. Adjust `.env` as needed:
```env
SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/beauty_salon
SPRING_DATASOURCE_USERNAME=cata
SPRING_DATASOURCE_PASSWORD=cata
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

SERVER_PORT=8081
```

## Running with Docker
Build and run using the helper script:
```bash
./run-docker.sh --build --run
```

Default port mapping: `8081:8081`.
`run-docker.sh` automatically loads `.env` and adds `host.docker.internal` support on Linux.

## Scripts
**run.sh**
- `--compile` → `mvn compile`
- `--test` → `mvn test`
- `--build` → `mvn clean package`
- `--run` → `mvn spring-boot:run`
- `--coverage` → `mvn test` + JaCoCo report

**run-docker.sh**
- `--build` → build Docker image
- `--run` → run container
- `--name <name>` → image name override
- `--port <port>` → host port override

## Coverage
JaCoCo report:
- `target/site/jacoco/index.html`

Run:
```bash
./run.sh --coverage
```

## Project Structure (High Level)
- `src/main/java/.../controller` REST controllers
- `src/main/java/.../service` business logic
- `src/main/java/.../repository` Spring Data JPA repositories
- `src/main/java/.../entity` JPA entities
- `src/main/java/.../security` auth, filters, config
- `src/main/resources/application.properties` configuration (env-driven)
- `src/test/java/...` unit tests

## Notes
- `.env` is ignored by git.
- If running MySQL on the host from Docker, keep `SPRING_DATASOURCE_URL` pointing to `host.docker.internal`.
