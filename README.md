# MathsMate SA

Monorepo containing:
- Spring Boot backend with MongoDB
- React frontend (Vite)
- Docker Compose support

Current status:
- Backend clean compile passes
- Frontend production build passes

## Quick Start

### Prerequisites
1. Java 21
2. Node.js 20+
3. MongoDB (local) or MongoDB Atlas

### Local Run (Recommended During Development)

Open two terminals.

#### Terminal 1 - Backend
From `backend`:

- Windows PowerShell:
	- `cd C:\Users\Siyabonga\mathsmate-sa\backend`
	- Optional Atlas connection:
		- `$env:MONGODB_URI="mongodb+srv://<username>:<password>@<cluster-host>/<db-name>?retryWrites=true&w=majority&appName=Cluster0"`
	- `./mvnw.cmd spring-boot:run`

- macOS/Linux:
	- `cd backend`
	- Optional Atlas connection:
		- `export MONGODB_URI="mongodb+srv://<username>:<password>@<cluster-host>/<db-name>?retryWrites=true&w=majority&appName=Cluster0"`
	- `./mvnw spring-boot:run`

#### Terminal 2 - Frontend
From `frontend`:

- Windows PowerShell (execution-policy safe):
	- `cd C:\Users\Siyabonga\mathsmate-sa\frontend`
	- `npm.cmd install`
	- `npm.cmd run dev`

- macOS/Linux:
	- `cd frontend`
	- `npm install`
	- `npm run dev`

### App URLs
- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`
- Example health endpoint: `http://localhost:8080/api/problems/health`

The frontend proxies `/api/*` to `http://localhost:8080`.

## Docker Run

If Docker Desktop is installed and running:

1. `docker compose up --build`
2. Open `http://localhost:5173`

To stop containers:

- `docker compose down`

## Build Commands

### Backend
- Windows: `mvnw.cmd clean compile`
- macOS/Linux: `./mvnw clean compile`

### Frontend
- Windows PowerShell: `npm.cmd run build`
- macOS/Linux: `npm run build`

## API Overview

### Problems
- `GET /api/problems` list all problems
- `GET /api/problems/{id}` get by id
- `POST /api/problems/solve` solve text problem
- `POST /api/problems/solve/image` solve uploaded image (OCR)
- `GET /api/problems/search?q=...` search by keyword
- `GET /api/problems/similar?q=...` find similar problems
- `DELETE /api/problems/{id}` delete problem

### Users
- `GET /api/users` list users
- `GET /api/users/{id}` get user by id
- `POST /api/users` create user
- `PUT /api/users/{id}` update user
- `DELETE /api/users/{id}` delete user

### Curriculum
- `GET /api/curriculum` list curriculum topics
- `GET /api/curriculum?grade=Grade 10` filter by grade
- `GET /api/curriculum/{topic}` get topic
- `GET /api/curriculum/search?q=...` search topics

## Notes

- MongoDB URI is configurable via `MONGODB_URI` (see `backend/src/main/resources/application.properties`).
- OCR is configurable via `backend/src/main/resources/application.properties`.
- CAPS curriculum seed data loads automatically on startup if the collection is empty.
