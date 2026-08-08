# Agent Guide — POS System (`pos_sys`)

This guide gives an AI agent (or any new developer) everything needed to work safely and correctly in this repository.

## 1. Project Overview

A **Point of Sale (POS) System** built as a **Spring Boot REST API**.

- **Package root:** `com.example.pos_sys`
- **Main class:** `src/main/java/com/example/pos_sys/PosSysApplication.java`
- **Build tool:** Maven (`mvnw` / `mvnw.cmd` wrapper — do not install Maven separately)
- **Java version:** 21
- **Spring Boot version:** 3.5.5

The project is **early-stage / in progress**. Many folders exist as placeholders (models, mappers, repositories, enums, DTOs) and are not yet wired up. Do not assume features exist — verify before using.

## 2. Tech Stack

| Concern | Technology |
|---|---|
| Web framework | Spring Boot Web (MVC / REST) |
| Database access | Spring JDBC (`JdbcTemplate`) + raw SQL |
| Database | MySQL (`jdbc:mysql://localhost:3306/db_restaurant2-5`) |
| Validation | `spring-boot-starter-validation` (Jakarta Bean Validation) |
| Boilerplate | Lombok (`@Data`, etc.) |
| API docs | springdoc-openapi (Swagger UI) |
| Frontend demo | Static `index.html` (vanilla JS `fetch`) |
| Tests | JUnit 5 + `spring-boot-starter-test` |

> **Note:** There is **no Spring Data JPA dependency** in `pom.xml`. Data access currently uses `JdbcTemplate` with raw SQL. The `spring.jpa.*` settings in `application.properties` are leftovers and have no effect. Do not assume JPA entities/repositories exist.

## 3. Directory Layout

```
pos_sys/
├── pom.xml                          # dependencies + build config
├── mvnw / mvnw.cmd                  # Maven wrapper
├── config/
│   └── WebConfig.java               # CORS configuration
├── index.html                       # static frontend demo for the Cashier API
├── src/
│   ├── main/
│   │   ├── java/com/example/pos_sys/
│   │   │   ├── PosSysApplication.java        # entry point
│   │   │   ├── config/                       # (empty except config/ folder at root)
│   │   │   ├── controllers/CashierController.java
│   │   │   ├── dtos/products/
│   │   │   │   ├── ProductRequestDTO.java
│   │   │   │   └── ProductResponseDTO.java
│   │   │   ├── enums/                        # empty placeholder
│   │   │   ├── mappers/ProductMapper.java    # empty placeholder
│   │   │   ├── models/Product.java           # empty placeholder
│   │   │   └── repositories/                 # empty placeholder
│   │   └── resources/
│   │       ├── application.properties        # datasource + JPA settings
│   │       ├── static/                       # empty
│   │       └── templates/                    # empty
│   └── test/java/com/example/pos_sys/
│       └── PosSysApplicationTests.java       # context-load smoke test
```

> The CORS config (`WebConfig.java`) lives in the repo-root `config/` folder but uses package `com.example.pos_sys.config`. It is correctly picked up by component scanning only because `com.example.pos_sys.config` is a subpackage of `com.example.pos_sys`.

## 4. How to Run

**Prerequisites:** JDK 21, MySQL running, database `db_restaurant2-5` created (see `application.properties` for credentials — username `root`, empty password).

```powershell
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

- App starts on **port 8080** (default; no `server.port` override in `application.properties`).
- Swagger UI: `http://localhost:8080/swagger-ui.html` (springdoc-openapi).

**Run tests:**

```powershell
mvnw.cmd test
```

**Build a runnable jar:**

```powershell
mvnw.cmd clean package
java -jar target/pos_sys-0.0.1-SNAPSHOT.jar
```

## 5. Database Configuration

File: `src/main/resources/application.properties`

```properties
spring.application.name=pos_sys
spring.datasource.url=jdbc:mysql://localhost:3306/db_restaurant2-5
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

- The table used by `CashierController` is **`tb_cashiers`** with columns `fullname`, `phone`, `username` (and an `id`).
- The `spring.jpa.*` properties are currently inert (no JPA on the classpath).

## 6. Existing API Endpoints

Base path: `http://localhost:8080`

### Cashier (`CashierController`)

| Method | Path | Body | Description | Implemented? |
|---|---|---|---|---|
| `POST` | `/api/cashier` | `{ "fullname", "phone", "username" }` | Insert a cashier into `tb_cashiers` | ✅ Yes (returns `Map.of("Message", "Create Success")`) |
| `GET` | `/api/cashier/cashier` | — | List all cashiers | ✅ Yes (returns `{status, data}`) |
| `POST` | `/api/cashier/cashier` | — | Stub | ⛔ Returns `"Hi"` |
| `PUT` | `/api/cashier/cashier` | — | Stub | ⛔ Returns `"Hi"` |
| `DELETE` | `/api/cashier/cashier` | — | Stub | ⛔ Returns `"Hi"` |

**Design note:** the controller mixes both REST styles — the real create/read use `/api/cashier` and `/api/cashier/cashier`, while the stub update/delete sit on `/api/cashier/cashier`. The comment at the top of the file says the goal is **RESTful** (same endpoint, different HTTP method), so `/cashier/cashier` is likely a mistake to be cleaned up.

## 7. Stubs / Placeholders / In-Progress Work

These exist as scaffolding and are **not functional**:

- `models/Product.java` — empty class (`@Data` only)
- `mappers/ProductMapper.java` — empty class
- `enums/` — empty directory
- `repositories/` — empty directory
- `ProductRequestDTO.java` — has validation annotations; note the **typo `categry_id`** field name
- `ProductResponseDTO.java` — defines fields but has no producer code
- No **service layer** exists anywhere
- No `ProductController`

## 8. Known Issues / Gotchas

1. **`index.html` targets the wrong port for POST** — it fetches from `http://localhost:3306` (MySQL port) and posts to `http://localhost:3306/api/cashier`. GET uses `http://localhost:8080`. These URLs are inconsistent and the POST will fail.
2. **CORS allows only `http://localhost:3306`** — `config/WebConfig.java`. If the frontend moves to 8080, update `allowedOrigins`.
3. **Duplicate `spring-boot-starter-jdbc` dependency** in `pom.xml` (lines ~33 and ~39). Harmless but should be deduplicated.
4. **`spring.jpa.*` config is misleading** — no JPA dependency is present.
5. **`tb_cashiers` table must exist** in MySQL — the app does not create it automatically.
6. **`@RequestMapping("/api/cashier")` + `@GetMapping("/cashier")`** produces the awkward `/api/cashier/cashier` URL.

## 9. Coding Conventions

- Package naming: `com.example.pos_sys.<layer>` (`controllers`, `dtos`, `models`, `mappers`, `repositories`, `config`, `enums`).
- DTO packages: `dtos.<entity>/` (e.g. `dtos/products/`), files named `*RequestDTO` / `*ResponseDTO`.
- Use **Lombok `@Data`** on models and DTOs (getters/setters/toString auto-generated).
- Use **Bean Validation annotations** (`@NotBlank`, `@NotNull`, `@Size`, `@Digits`, `@DecimalMin`) on request DTOs with message attributes.
- REST endpoints: prefer **RESTful style** — one resource path, methods `GET` / `POST` / `PUT` / `DELETE` (see controller comment). Existing stub endpoints are the exception, not the rule.
- Data access pattern currently: constructor-inject `JdbcTemplate` and use parameterized `?` placeholders (never string-concatenate SQL).
- Responses: use `Map<String, Object>` (e.g. `Map.of(...)` / `HashMap`) for simple responses.
- Add Swagger tags via `@Tag(name = "...")` on controllers.
- Keep DB credentials out of the code; they already live only in `application.properties`.

## 10. Testing

- Single smoke test: `PosSysApplicationTests.contextLoads()` — verifies the Spring context starts.
- Run with `mvnw.cmd test`.
- If you add features, add JUnit 5 tests under `src/test/java/com/example/pos_sys/`.

## 11. Tooling / Environment

- IDE: VS Code with Java extensions (`.vscode/settings.json` sets `java.configuration.updateBuildConfiguration: interactive`).
- `.github/modernize/java-upgrade/` contains a one-time upgrade-workflow hook — not part of the app runtime. Leave it alone.
- Java compilation uses annotation processing for Lombok (configured in `pom.xml`). Ensure annotation processing is enabled in the IDE or Lombok code will fail to compile.

## 12. Workflow Notes for Agents

1. **Never assume** a layer/service exists — grep for it first.
2. **Verify compile** after changes: `mvnw.cmd -q compile` (Windows) or `./mvnw -q compile`.
3. **Run tests** before finishing: `mvnw.cmd test`.
4. **Do not commit** unless explicitly asked.
5. When touching SQL, keep using `JdbcTemplate` with `?` placeholders unless asked to migrate to JPA.
6. Keep the `agent_guide.md` up to date if the structure or endpoints change materially.
