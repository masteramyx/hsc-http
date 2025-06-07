# HealthShadow Backend Application

REST API server for the HealthShadow platform, supporting multi-user healthcare applications with role-based access for students and professionals.

## Tech Stack

- **Backend:** Kotlin + Ktor framework
- **Database:** PostgreSQL with SQLDelight for type-safe queries
- **Deployment:** Docker Compose with containerized services
- **Build Tool:** Gradle

## Architecture Overview

### Self-Hosted Deployment Architecture

This application uses a **single-server Docker Compose setup** for self-hosting:

```
Internet
    ↓
Your Home Server
├── Reverse Proxy Container (nginx/traefik)
│   ├── Handles SSL/HTTPS certificates
│   ├── Routes traffic to application
│   └── Serves on ports 80/443
├── Application Container (Ktor)
│   ├── Runs on internal port 8080
│   └── Only accessible through proxy
└── PostgreSQL Container
    ├── Database with persistent volumes
    └── Only accessible to application
```

### Why This Architecture?

**Docker Compose Benefits:**
- **Container Orchestration:** Manages multiple services as one application
- **Container Networking:** Services communicate by name (no IP management)
- **Dependency Management:** Database starts before application
- **Environment Variables:** Shared configuration across containers
- **Volume Management:** Data persistence across container restarts
- **One Command Deployment:** `docker-compose up` starts entire stack

**Reverse Proxy Benefits:**
- **SSL/HTTPS:** Automatic certificate management
- **Security:** Application never directly exposed to internet
- **Port Management:** Public ports (80/443) → Internal app port (8080)
- **Domain Routing:** Routes domain name to application
- **Static File Serving:** Efficient serving of assets
- **Rate Limiting:** Built-in DDoS protection

**Port Flow Example:**
```
User: https://myapp.com (port 443)
  ↓
Server: Port 443 → Proxy Container
  ↓
Proxy: Internal request to app:8080
  ↓
App: Processes request, responds via port 8080
  ↓
Proxy: Returns HTTPS response to user
```

## Database Schema

### User System Design

The application uses a **role-based user system** with relational database design:

#### Tables:

**users** - Base authentication table
- `id` (Primary Key)
- `email` (Unique)
- `password_hash` 
- `user_type` (student, professional, admin)
- `created_at`, `updated_at`, `is_active`

**student** - Student-specific data
- `id` (Primary Key)
- `user_id` (Foreign Key → users.id, CASCADE DELETE)
- `first_name`, `last_name`, `phone`
- `student_id`, `major`, `year_level`, `gpa`
- `created_at`, `updated_at`

**professional** - Healthcare professional data
- `id` (Primary Key)
- `user_id` (Foreign Key → users.id, CASCADE DELETE)
- `first_name`, `last_name`, `phone`
- `license_number`, `specialization`, `years_experience`
- `organization`, `title`, `bio`, `verified`
- `created_at`, `updated_at`

#### Foreign Key Benefits:
- **Data Integrity:** CASCADE DELETE ensures no orphaned records
- **Relationship Enforcement:** Each student/professional must have a user account
- **Query Efficiency:** JOIN operations to get complete user profiles

### Database Connection Architecture

This application uses **HikariCP DataSource** with **SQLDelight 2.0** for database connectivity, following the [recommended approach from SQLDelight documentation](https://sqldelight.github.io/sqldelight/2.0.2/jvm_postgresql/).

**Why HikariCP DataSource?**
- **Connection Pooling:** Efficiently manages database connections
- **Performance:** HikariCP is one of the fastest connection pools available
- **SQLDelight Integration:** Native support via `.asJdbcDriver()` extension
- **Production Ready:** Handles connection lifecycle, timeouts, and recovery

**Configuration:**
```kotlin
// DatabaseFactory uses HikariCP with these settings:
maximumPoolSize = 10
isAutoCommit = false
transactionIsolation = "TRANSACTION_REPEATABLE_READ"
```

This approach provides better performance and reliability compared to direct JDBC connections, especially under load.

## Development Setup

### Prerequisites
- JDK 11 or higher
- Docker and Docker Compose (for database)

### Local Development

#### Setup PostgreSQL Database
```bash
# Start PostgreSQL container for development
docker-compose -f docker-compose.dev.yml up -d

# Check container status
docker-compose -f docker-compose.dev.yml ps

# View PostgreSQL logs (useful for troubleshooting)
docker-compose -f docker-compose.dev.yml logs postgres

# Stop PostgreSQL when done
docker-compose -f docker-compose.dev.yml down
```

**Command Explanation:**
- `-f docker-compose.dev.yml` = Use this specific file (default is `docker-compose.yml`)
- `up -d` = Start containers in background (detached mode)
- `ps` = Show running containers and their status
- `logs postgres` = Show PostgreSQL startup messages and errors
- `down` = Stop and remove all containers

**Database Connection:**
- **Host:** localhost
- **Port:** 5432
- **Database:** healthshadow_dev
- **Username:** hsc_user
- **Password:** hsc_dev_password

#### Build and Run Application
```bash
# Build and test
./gradlew build

# Run locally (development mode)
./gradlew run

# Application runs on http://localhost:8080
```

### Testing the API
```bash
# Test root endpoint
curl http://localhost:8080/

# Get all students  
curl http://localhost:8080/students

# Add a student (currently uses in-memory storage)
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"id":"1","firstName":"John","lastName":"Doe","email":"john@example.com","phone":"555-1234"}'
```

## Deployment

### Production Deployment (Self-Hosted)

**Server Requirements:**
- Linux server (Ubuntu/Debian recommended)
- Docker and Docker Compose installed
- Domain name pointing to server IP
- Ports 80 and 443 accessible from internet

**Deployment Process:**
1. Clone repository to server
2. Configure environment variables
3. Run `docker-compose up -d`
4. SSL certificates automatically provisioned

**Data Persistence:**
- Database data stored in Docker volumes
- Survives container restarts and updates
- Backup strategy: Volume snapshots

## Project History

**Previous State:** Heroku-deployed application with basic student management

**Current Modernization:**
- ✅ Updated dependencies (Kotlin 1.9.25, Ktor 2.3.12, SQLDelight 2.0.2)
- ✅ Migrated from Gradle 7.2 to 8.5
- ✅ Redesigned database schema for multi-user system
- ✅ Fixed localhost binding for browser development
- 🔄 In Progress: Docker containerization for self-hosting
- 📋 Planned: Authentication implementation, SSL setup, monitoring

## Future Development

**Authentication System:**
- Password hashing (bcrypt)
- JWT token management
- Login/logout endpoints
- Session management
- Route protection middleware

**API Enhancements:**
- User registration/authentication endpoints
- Role-based access control
- Professional verification workflow
- Student-professional matching system

**Infrastructure:**
- Automated backups
- Monitoring and logging
- CI/CD pipeline
- Health checks

## Contributing

1. Ensure tests pass: `./gradlew test`
2. Follow existing code conventions
3. Update this README for architectural changes
4. Commit messages should be descriptive and concise

## Configuration

### Environment Variables
- `PORT` - Application port (default: 8080)
- `DATABASE_URL` - PostgreSQL connection string
- `JWT_SECRET` - Secret key for JWT tokens (when implemented)

### Application Configuration
- **Local Development:** `app/src/main/resources/application.conf`
- **Production:** Environment variables override configuration