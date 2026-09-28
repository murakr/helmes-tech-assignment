# Test assignment for Helmes

This assignment is created for Helmes as a test for junior Angular developer position. It features a small full-stack application.

## About

A form where users enter their name, pick the sectors they are involved in and agree to the terms. All sectors are stored in the database and the form is built from them. After saving, the form is refilled with the stored data and the user can edit it for the rest of their session.

## Used technologies

- Angular 22.1, TypeScript 6.0
- Java 25, Spring Boot 4.1, Hibernate
- PostgreSQL 18, Liquibase
- Docker Compose, nginx
- Tests: JUnit, Mockito, Testcontainers (backend), Vitest (frontend)

## Starting the project

Requires Docker.

```bash
cp .env.example .env
docker compose up --build
```

Open http://localhost:8081.

### Local development

```bash
docker compose up -d db                  # database only
cd backend && ./mvnw spring-boot:run     # API on :8080
cd frontend && npm ci && npm start       # app on :4200, proxies /api to :8080
```

### Tests

```bash
cd backend && ./mvnw test                # requires Docker (Testcontainers)
cd frontend && npx ng test --watch=false
```

## Database dump

`database/dump.sql` contains the full structure and sample data. To restore it into an empty database:

```bash
docker compose exec -T db sh -c 'createdb -U "$POSTGRES_USER" restored'
docker compose exec -T db sh -c 'psql -U "$POSTGRES_USER" -d restored' < database/dump.sql
```

The schema itself is defined by the Liquibase changesets in `backend/src/main/resources/db/changelog`.

## Some features

- **Database changes are managed with Liquibase.** Anyone who starts the project gets the same tables and sector list automatically, without running any SQL by hand.
- **Sectors are stored as a tree.** Each sector points to its parent and keeps its original ID. The indentation in the form is worked out from that, rather than being typed into the names with spaces like in the original HTML.
- **Input is checked twice.** The form shows mistakes straight away, and the backend checks everything again before saving, since the form can be bypassed.
- **Sectors are chosen with checkboxes instead of a multi-select list.** Picking several sectors from a normal multi-select needs Ctrl or ⌘, which many people don't know, and one wrong click can clear the whole selection.
- **Tests use a real PostgreSQL database** in Docker, so they check the same database the app actually runs on.

---
Richard Murak