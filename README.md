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
├── Traefik Reverse Proxy Container
│   ├── Automatic SSL/HTTPS with Let's Encrypt
│   ├── Routes traffic to application containers
│   ├── Service discovery via Docker labels*
│   └── Serves on ports 80/443

*Service Discovery: Traefik automatically finds containers by reading their Docker labels, eliminating the need for manual configuration files
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

## Traefik Reverse Proxy

### What is Traefik?

Traefik is a modern reverse proxy that automatically discovers and routes traffic to your Docker containers. Unlike traditional proxies (nginx, Apache), Traefik requires **zero manual configuration** - it reads Docker labels to understand how to route traffic.

### Why Traefik for Self-Hosting?

**Automatic Configuration:**
- No editing config files when adding/removing services
- Containers declare their own routing rules via labels
- Dynamic updates without proxy restarts

**Built-in SSL/HTTPS:**
- Automatic Let's Encrypt certificate provisioning
- Certificate renewal handled automatically
- HTTPS redirects configured by default

**Docker Integration:**
- Reads Docker socket to discover containers
- Understands Docker networks and service names
- Works seamlessly with Docker Compose

### How It Works in This Project

**Container Labels Define Routing:**
```yaml
app:
  labels:
    - "traefik.enable=true"                                    # Make this container discoverable
    - "traefik.http.routers.hsc-app.rule=Host(`myapp.com`)"   # Route myapp.com to this container
    - "traefik.http.routers.hsc-app.entrypoints=websecure"    # Use HTTPS (port 443)
    - "traefik.http.routers.hsc-app.tls.certresolver=letsencrypt" # Get SSL cert from Let's Encrypt
    - "traefik.http.services.hsc-app.loadbalancer.server.port=8080" # Container listens on port 8080
```

**Traffic Flow:**
```
User: https://myapp.com
  ↓
Traefik: Reads Host header, finds matching container label
  ↓ 
Traefik: Routes to hsc-app container on port 8080
  ↓
App: Processes request, returns response
  ↓
Traefik: Returns HTTPS response with auto-managed SSL certificate
```

### Traefik Configuration Files

The project requires one static configuration file to enable Docker discovery and SSL:

**`traefik/traefik.yml`** - Main configuration
- Enables Docker provider for service discovery
- Configures Let's Encrypt for automatic SSL certificates
- Sets up entrypoints (HTTP port 80, HTTPS port 443)

### Important Commands

```bash
# Start full stack with Traefik
docker-compose up -d

# View Traefik dashboard (shows discovered services)
open http://localhost:8081

# Check Traefik logs (useful for SSL certificate issues)
docker logs hsc-traefik

# Test SSL certificate provisioning
curl -v https://yourdomain.com

# Force SSL certificate renewal (if needed)
docker exec hsc-traefik rm /acme/acme.json
docker restart hsc-traefik
```

### Environment Variables

**Required for Production:**
- `DOMAIN=yourdomain.com` - Your actual domain name
- `POSTGRES_PASSWORD=secure_password` - Database password

**Local Development:**
- Uses `localhost` defaults for testing
- SSL disabled for local development

### SSL Certificate Storage

Traefik stores Let's Encrypt certificates in a Docker volume:
```yaml
volumes:
  traefik-acme:  # Persistent storage for SSL certificates
```

**Important:** This volume persists certificates across container restarts. Losing this volume means re-requesting certificates from Let's Encrypt (rate limited).

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

### Container Management

**Rebuild and Restart Containers:**
```bash
# Most common - rebuild image and recreate all containers
docker-compose -f docker-compose.dev.yml up -d --build

# Alternative: rebuild image first, then recreate
docker build -t hsc-http . && docker-compose -f docker-compose.dev.yml up -d --force-recreate

# Force recreate without rebuilding image (use existing image)
docker-compose -f docker-compose.dev.yml up -d --force-recreate

# Recreate specific service only
docker-compose -f docker-compose.dev.yml up -d --force-recreate app

# Nuclear option - stop, remove everything, rebuild, restart
docker-compose -f docker-compose.dev.yml down
docker build -t hsc-http .
docker-compose -f docker-compose.dev.yml up -d
```

**When to Use:**
- `--build` - When you've changed application code
- `--force-recreate` - When you've changed docker-compose.yml configuration
- Both - When you've changed both code and configuration

### Testing the API

**Direct Access (Bypasses Traefik):**
```bash
# Test root endpoint
curl http://localhost:8080/

# Get all students  
curl http://localhost:8080/students

# Add a student
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"id":"1","firstName":"John","lastName":"Doe","email":"john@example.com","phone":"555-1234"}'
```

**Through Traefik (Production-like):**
```bash
# Test HTTPS access (ignores SSL certificate warnings)
curl -k -H "Host: hsc-http.localhost" https://localhost/

# Test HTTP redirect (should return 301 redirect)
curl -H "Host: hsc-http.localhost" http://localhost/

# Test API endpoints through Traefik
curl -k -H "Host: hsc-http.localhost" https://localhost/students
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
- ✅ Docker containerization for self-hosting
- ✅ Authentication utilities with bcrypt password hashing
- 🔄 In Progress: Web authentication UI (login/logout pages)
- 📋 Planned: SSL setup, monitoring, comprehensive testing

## Future Development

### Platform Expansion (Multi-Service Architecture)

One of the key advantages of this Traefik-based setup is the ability to host **multiple services** for the HealthShadow platform on the same server using different subdomains. Each service gets its own SSL certificate and container, while sharing the same infrastructure.

**How Multi-Service Hosting Works:**

The HTTP `Host` header identifies which service each request is intended for:
```
User Request: https://api.shadowconnects.com
├── DNS resolves to: 73.43.190.195 (your home IP)
├── Router forwards: Port 443 → Server:443
├── Traefik reads: Host: api.shadowconnects.com
└── Routes to: API container based on Docker labels
```

**Potential Services Architecture:**
```yaml
# Main API (Current)
api.shadowconnects.com → Kotlin/Ktor REST API

# Frontend Application  
app.shadowconnects.com → React/Vue.js SPA

# Admin Dashboard
admin.shadowconnects.com → Admin interface

# Student Portal
student.shadowconnects.com → Student-specific features

# Professional Portal  
professional.shadowconnects.com → Healthcare professional tools

# Documentation/Help
docs.shadowconnects.com → API documentation, user guides

# File Storage
files.shadowconnects.com → Document/image upload service

# Real-time Features
ws.shadowconnects.com → WebSocket server for chat/notifications

# Analytics Dashboard
analytics.shadowconnects.com → Usage metrics, reporting
```

**Implementation Example:**
```yaml
# docker-compose.yml - Multiple services
services:
  # Current API service
  api:
    labels:
      - "traefik.http.routers.api.rule=Host(`api.shadowconnects.com`)"
      
  # Frontend application
  frontend:
    image: nginx:alpine
    labels:
      - "traefik.http.routers.frontend.rule=Host(`app.shadowconnects.com`)"
      
  # Admin dashboard
  admin:
    image: admin-dashboard:latest
    labels:
      - "traefik.http.routers.admin.rule=Host(`admin.shadowconnects.com`)"
```

**Benefits of This Architecture:**
- **Single IP Address:** All services use the same public IP (73.43.190.195)
- **Automatic SSL:** Each subdomain gets its own Let's Encrypt certificate
- **Independent Deployment:** Services can be updated/restarted independently
- **Resource Efficiency:** Shared database, shared reverse proxy
- **Easy Scaling:** Add new services by adding containers + labels
- **Professional Appearance:** Clean subdomain structure

**Future Service Ideas:**
- **Matching Service:** Algorithm to connect students with professionals
- **Scheduling System:** Appointment booking and calendar management  
- **Communication Platform:** Secure messaging between users
- **Content Management:** Educational resources and documentation
- **Payment Processing:** Subscription management for premium features
- **Mobile API Gateway:** Specialized endpoints for mobile applications

This architecture allows the HealthShadow platform to grow organically - start with the core API, then add specialized services as user needs become clear.

### Authentication System
- ✅ Password hashing (bcrypt)
- ✅ Session management utilities
- 🔄 Login/logout web pages
- 📋 Route protection middleware
- 📋 User registration flow

### API Enhancements
- User registration/authentication endpoints
- Role-based access control
- Professional verification workflow
- Student-professional matching system

### Infrastructure
- Automated backups
- Monitoring and logging
- CI/CD pipeline
- Health checks
- Code linting and formatting (ktlint)

## Testing Strategy

This project follows industry-standard testing practices with clear separation of concerns:

**Unit Tests** - Test single components in isolation, no external dependencies
- Pure unit tests: Zero dependencies (e.g., validation logic)
- Unit tests with mocks: Isolated business logic with mocked dependencies

**Integration Tests** - Test components working together with external dependencies
- Repository tests: Use in-memory H2 database for fast, isolated database testing
- End-to-end tests: Full application flow testing

**Current Test Coverage:**
- ✅ Authentication layer: Password validation, auth service, user repository
- 📋 Planned: Route handlers, session management, full auth flow

## Contributing

1. Ensure tests pass: `./gradlew test`
2. Follow existing code conventions
3. Write tests for new functionality (unit tests preferred)
4. Update this README for architectural changes
5. Commit messages should be descriptive and concise

## Configuration

### Environment Variables
- `PORT` - Application port (default: 8080)
- `DATABASE_URL` - PostgreSQL connection string
- `JWT_SECRET` - Secret key for JWT tokens (when implemented)

### Application Configuration
- **Local Development:** `app/src/main/resources/application.conf`
- **Production:** Environment variables override configuration

## Development DB Commands

### Connect to PostgreSQL Database

```bash
# Connect to development database
docker exec -it hsc-postgres psql -U hsc_user -d healthshadow_dev

# Alternative: Connect from host machine (if psql installed locally)
psql -h localhost -p 5432 -U hsc_user -d healthshadow_dev
```

### Common PostgreSQL Commands

**View Tables:**
```sql
-- List all tables
\dt

-- Show table structure
\d users
\d student
\d professional

-- Show all columns with types
\d+ users
```

**Query Tables:**
```sql
-- View all users (shows test logins)
SELECT id, email, user_type, is_active, created_at FROM users;

-- View students with user info
SELECT u.email, u.user_type, s.first_name, s.last_name, s.major 
FROM users u 
JOIN student s ON u.id = s.user_id;

-- View professionals with user info
SELECT u.email, u.user_type, p.first_name, p.last_name, p.specialization 
FROM users u 
JOIN professional p ON u.id = p.user_id;
```

**Update Entries:**
```sql
-- Update user password (use actual bcrypt hash)
UPDATE users SET password_hash = '$2a$10$actual.bcrypt.hash.here' WHERE email = 'student@example.com';

-- Activate/deactivate user
UPDATE users SET is_active = false WHERE email = 'student@example.com';
UPDATE users SET is_active = true WHERE email = 'student@example.com';

-- Update student info
UPDATE student SET gpa = 3.85, year_level = 4 WHERE user_id = 1;

-- Verify professional
UPDATE professional SET verified = true WHERE user_id = 2;
```

**Exit Database:**
```sql
-- Exit psql
\q
```

### Test Login Credentials

The database is initialized with these test accounts (passwords need proper bcrypt hashing):

- **Student:** `student@example.com` 
- **Professional:** `professional@example.com`
- **Admin:** `admin@example.com`

**Note:** Current passwords are placeholder `TODO_IMPLEMENT_PASSWORD_HASHING` - you'll need to update them with actual bcrypt-hashed passwords for testing.