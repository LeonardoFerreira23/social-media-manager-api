# Social Media Manager API

![CI](https://github.com/LeonardoFerreira23/social-media-manager-api/actions/workflows/ci.yml/badge.svg)

A REST API to register and read social media posts. Each post has a title, a caption, a date and a flag telling whether it has been published.

## Features

- **Create a post** with title, caption, date and published status
- **List all posts**
- **Filter posts** by published status
- **Future dates are rejected**: a post can only be registered with today's date or a past date
- **Interactive API documentation** with Swagger UI
- **Caching** of read operations, automatically refreshed when a new post is created

## Tech stack

Java 21 · Spring Boot 4 · Spring Data JPA · PostgreSQL · Spring Cache · springdoc-openapi (Swagger) · JUnit 5 + Mockito · JaCoCo · Docker / Docker Compose · Maven

---

## Getting started

### Option 1: Docker (recommended)

The only requirement is [Docker](https://www.docker.com/products/docker-desktop/). No Java installation is needed.

```bash
git clone https://github.com/LeonardoFerreira23/social-media-manager-api.git
cd social-media-manager-api
docker compose up --build
```

This starts two containers:

| Container | Description | Port |
|---|---|---|
| `sm-postgres` | PostgreSQL 18 database | 5432 |
| `social-media-manager` | The API | 8081 |

The API waits for the database to be ready before starting. Once you see `Started SocialMediaManagerApplication` in the logs, the API is available at **http://localhost:8081**.

Useful commands:

| Command | What it does |
|---|---|
| `docker compose up -d --build` | Start in the background |
| `docker compose logs -f app` | Follow the API logs |
| `docker compose down` | Stop everything (data is kept) |
| `docker compose down -v` | Stop everything and **delete** the data |

### Option 2: Run locally

Requirements: **JDK 21+** and Docker (for the database only). Maven is not required, since the project ships with the Maven Wrapper (`mvnw`).

1. Start only the database:

   ```bash
   docker compose up -d postgres
   ```

2. Run the application:

   ```bash
   # Linux / macOS
   ./mvnw spring-boot:run

   # Windows (PowerShell)
   .\mvnw.cmd spring-boot:run
   ```

---

## Using the API

### Swagger UI

Open **http://localhost:8081/swagger-ui.html** to browse the documentation and try every endpoint from the browser ("Try it out").

The raw OpenAPI specification is available at http://localhost:8081/v3/api-docs.

### Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/post` | Create a post |
| `GET` | `/post` | List all posts |
| `GET` | `/post?isPublished=true` | List only published posts |
| `GET` | `/post?isPublished=false` | List only unpublished posts |

Dates always use the **`dd/MM/yyyy`** format.

### Create a post

```bash
curl -X POST http://localhost:8081/post \
  -H "Content-Type: application/json" \
  -d '{"title":"Title 1","caption":"Caption 1","date":"21/12/2023","isPublished":false}'
```

Response `201 Created`:

```json
{
  "id": 1,
  "title": "Title 1",
  "caption": "Caption 1",
  "date": "21/12/2023",
  "isPublished": false
}
```

### List posts

```bash
curl http://localhost:8081/post
curl "http://localhost:8081/post?isPublished=true"
```

Response `200 OK`:

```json
[
  {
    "id": 1,
    "title": "Title 1",
    "caption": "Caption 1",
    "date": "20/12/2023",
    "isPublished": true
  }
]
```

### Windows PowerShell equivalents

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8081/post -ContentType "application/json" -Body '{"title":"Title 1","caption":"Caption 1","date":"21/12/2023","isPublished":false}'

Invoke-RestMethod http://localhost:8081/post
Invoke-RestMethod "http://localhost:8081/post?isPublished=true"
```

### Errors

Invalid requests return `400 Bad Request` with a description of the problem, for example:

- a date in the future
- a missing or blank field
- a date that is not in the `dd/MM/yyyy` format, or malformed JSON

See [TECHNICAL.md](TECHNICAL.md#error-handling) for the full error format.

---

## Running the tests

```bash
# Linux / macOS
./mvnw verify

# Windows (PowerShell)
.\mvnw.cmd verify
```

This runs the unit tests and checks that the service layer has **100% line coverage**. The build fails if coverage drops below that.

The tests do not need a database or Docker to run.

The coverage report is generated at `target/site/jacoco/index.html`.

### Continuous integration

Every push and pull request to `main` triggers a GitHub Actions workflow (`.github/workflows/ci.yml`) that builds the project, runs the tests, enforces the coverage rule and builds the Docker image. The coverage report is attached to each run as the `jacoco-report` artifact.

---

## Documentation

- **README.md** (this file): how to install, run and use the API
- **[TECHNICAL.md](TECHNICAL.md)**: architecture, data model, API contracts, error handling and design decisions