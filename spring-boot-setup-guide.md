# Spring Boot Project Setup Guide

A step-by-step walkthrough for creating a Spring Boot REST API using Spring Initializr.

---

## Contents

1. [Prerequisites](#1-prerequisites)
2. [Maven vs Gradle](#2-maven-vs-gradle)
3. [Generate the Project](#3-generate-the-project)
4. [Open the Project](#4-open-the-project)
5. [Add a Controller](#5-add-a-controller)
6. [Run the Application](#6-run-the-application)
7. [Verify](#7-verify)
8. [Common Next Steps](#8-common-next-steps)
9. [Troubleshooting](#9-troubleshooting)

---

## 1. Prerequisites

Check your Java version:

```bash
java -version
```

You need **JDK 17 or 21**. If you don't have one, install [Eclipse Temurin](https://adoptium.net) 21.

> You do **not** need to install Maven or Gradle separately. The generated project ships with a wrapper script (`mvnw` / `gradlew`) that downloads the right version automatically.

---

## 2. Maven vs Gradle

Neither is "the base" — they are two alternatives that do the same job (dependency management + build). Spring Boot supports both equally. The build tool has nothing to do with whether you're building a REST API or anything else.

| | Maven | Gradle |
|---|---|---|
| Config file | `pom.xml` (XML) | `build.gradle` / `.kts` (Groovy/Kotlin) |
| Verbosity | Verbose but predictable | Concise, it's real code |
| Speed | Slower on large builds | Faster (incremental, cache, daemon) |
| Learning curve | Gentle | Steeper |
| Tutorial coverage | Very high | Good |
| Default on start.spring.io | Yes | No |

**Recommendation:** if you're learning Spring, pick **Maven**. When your build breaks, the Stack Overflow answer will almost always be a `pom.xml` snippet.

Pick Gradle if your team already uses it, or you're coming from Android/Kotlin.

Same dependency, both formats:

```xml
<!-- Maven: pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

```groovy
// Gradle: build.gradle
implementation 'org.springframework.boot:spring-boot-starter-web'
```

You can switch later — it means rewriting the build file, not the code.

---

## 3. Generate the Project

Go to **https://start.spring.io** and fill in:

| Field | Value |
|---|---|
| Project | **Maven** |
| Language | **Java** |
| Spring Boot | latest stable (avoid SNAPSHOT / M / RC) |
| Group | `com.example` |
| Artifact | `demo` |
| Name | `demo` |
| Package name | `com.example.demo` |
| Packaging | **Jar** |
| Java | **21** (match your `java -version`) |

### Dependencies

Click **Add Dependencies** and select:

| Starter | Purpose | Needed? |
|---|---|---|
| `Spring Web` | REST APIs / MVC | **Yes** — this is what makes it an API |
| `Spring Boot DevTools` | Auto-restart on save | Recommended |
| `Lombok` | Cuts getter/setter boilerplate | Optional |
| `Validation` | `@Valid` on request bodies | Optional |
| `Spring Data JPA` | Database access | Only if persisting data |
| `H2 Database` | In-memory DB for local dev | Pairs with JPA |
| `PostgreSQL Driver` / `MySQL Driver` | Real DB driver | Production use |

Click **Generate** (or press `Ctrl+Enter`). A `demo.zip` downloads.

> **Alternative:** IntelliJ IDEA has this built in — `File → New → Project → Spring Boot`. Same Initializr, but it opens the project for you.

---

## 4. Open the Project

Unzip the archive, then in your IDE choose **Open** and point at the folder containing `pom.xml` (not Import). Let it index and download dependencies once.

### Project structure

```
demo/
├── mvnw, mvnw.cmd              ← wrapper; use instead of installing Maven
├── pom.xml                     ← dependencies live here
└── src/
    ├── main/
    │   ├── java/com/example/demo/
    │   │   └── DemoApplication.java
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       └── templates/
    └── test/java/com/example/demo/
        └── DemoApplicationTests.java
```

---

## 5. Add a Controller

Create `src/main/java/com/example/demo/HelloController.java`:

```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring!";
    }
}
```

> **Important:** the controller must live in `com.example.demo` or a **sub**package. Component scanning starts from `DemoApplication`'s package. This is the number-one cause of "why isn't my endpoint working".

---

## 6. Run the Application

```bash
cd demo
./mvnw spring-boot:run          # macOS / Linux
mvnw.cmd spring-boot:run        # Windows
```

Gradle equivalent:

```bash
./gradlew bootRun
```

Or click the ▶ button on `DemoApplication.java` in your IDE.

Watch the log for:

```
Tomcat started on port 8080
```

---

## 7. Verify

```bash
curl http://localhost:8080/hello
```

Expected output:

```
Hello Spring!
```

Or just open **http://localhost:8080/hello** in a browser.

---

## 8. Common Next Steps

### Change the port

`src/main/resources/application.properties`:

```properties
server.port=8081
spring.application.name=demo
```

### Add a dependency later

Edit `pom.xml`, then reload the Maven project in your IDE:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

You are never locked into your initial Initializr picks.

### Build a runnable jar

```bash
./mvnw clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## 9. Troubleshooting

| Symptom | Cause / Fix |
|---|---|
| Port 8080 already in use | Change `server.port`, or kill the process using the port |
| 404 on your endpoint | Controller is outside the main package — see step 5 |
| Lombok getters "not found" | Enable annotation processing in IDE settings |
| `./mvnw: permission denied` | Run `chmod +x mvnw` |
| Dependencies won't resolve | Reload/re-import the Maven project; check network/proxy |
