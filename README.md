# 🏍️ MotoGP Stats API

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A comprehensive REST API for MotoGP racing data, built with Spring Boot. This API provides detailed information about teams, riders, and tracks with full CRUD operations, filtering, pagination, and interactive Swagger documentation.

## 🚀 Live Demo

👉 **[Swagger UI - Interactive API Documentation](https://motogp-stats-api.onrender.com/swagger-ui/index.html)**

Explore and test all API endpoints directly in your browser!

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Database Schema](#-database-schema)
- [API Endpoints](#-api-endpoints)
- [Getting Started](#-getting-started)
- [Testing](#-testing)
- [Project Structure](#-project-structure)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)
- [Author](#-author)

## ✨ Features

### Teams Management
- Full CRUD operations for MotoGP teams
- Filter teams by country and manufacturer
- Pagination and sorting support
- Includes rider information in team responses

### Riders Management
- Complete rider profiles with team associations
- Filter by nationality, team, and search by name
- Unique race number validation
- Career statistics (wins, podiums, pole positions, world titles)

### Tracks Management
- Circuit information with lap records
- Filter by country and city
- Sort by track length
- Race schedule integration

### Database & API Features
- Interactive Swagger/OpenAPI documentation
- Database version control with Flyway migrations
- Comprehensive test suite (Unit + Integration)
- PostgreSQL database with proper normalization

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.x |
| **Database** | PostgreSQL 15+ |
| **ORM** | Spring Data JPA / Hibernate |
| **Migrations** | Flyway |
| **Testing** | JUnit 5, Mockito, AssertJ |
| **API Docs** | Swagger UI / OpenAPI |
| **Build Tool** | Maven |
| **Deployment** | Render.com |

## 📊 Database Schema

![Database Schema](diagrams/schema.png)

The database is fully normalized with the following key relationships:
- **Teams → Riders**: One-to-Many (current team)
- **Teams → RiderSeasonTeam**: Season-specific rider assignments
- **Tracks → Races**: Track usage across seasons
- **Riders → RaceResults**: Race participation and performance

## 🔧 API Endpoints

### Teams
```
GET    /teams              - Get all teams (with filtering & pagination)
GET    /teams/{id}         - Get team by ID
GET    /teams/name         - Get team by name
GET    /teams/search       - Search teams by name
GET    /teams/countries    - Get all unique countries
GET    /teams/manufacturers - Get all unique manufacturers
GET    /teams/exists       - Check if team exists
POST   /teams              - Create a new team
PUT    /teams/{id}         - Update an existing team
DELETE /teams/{id}         - Delete a team
```

### Riders
```
GET    /riders              - Get all riders (with filtering & pagination)
GET    /riders/{id}         - Get rider by ID
GET    /riders/name         - Get rider by full name
GET    /riders/search       - Search riders by name
GET    /riders/nationalities - Get all unique nationalities
GET    /riders/exists       - Check if rider exists
POST   /riders              - Create a new rider
PUT    /riders/{id}         - Update an existing rider
DELETE /riders/{id}         - Delete a rider
```

### Tracks
```
GET    /tracks              - Get all tracks (with filtering & pagination)
GET    /tracks/{id}         - Get track by ID
GET    /tracks/circuit      - Get track by circuit name
GET    /tracks/country      - Get tracks by country
GET    /tracks/length-desc  - Get tracks sorted by length (descending)
POST   /tracks              - Create a new track
PUT    /tracks/{id}         - Update an existing track
DELETE /tracks/{id}         - Delete a track
```

## 🚦 Getting Started

### Prerequisites
- Java 17+
- PostgreSQL 15+
- Maven 3.8+

### Local Development Setup

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/motogp-stats-api.git
cd motogp-stats-api
```

2. **Create PostgreSQL database**
```bash
createdb motogp_api_db
```

3. **Update database credentials**
   Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/motogp_api_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. **Run the application**
```bash
mvn spring-boot:run
```

5. **Access Swagger UI**
   Open your browser: `http://localhost:8080/swagger-ui.html`

### Using Docker Compose

Alternatively, run everything with Docker:

```bash
# Start PostgreSQL container
docker-compose up -d

# Run the application
mvn spring-boot:run
```

## 🧪 Testing

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=TeamControllerTest
mvn test -Dtest=RiderRepositoryTest
```

### Test Coverage
- **Repository Layer**: JPA repository methods
- **Service Layer**: Business logic and exception handling
- **Controller Layer**: REST endpoints and validation
- **Integration Tests**: Full HTTP request/response cycles

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/pglazowski/motogpstatsapi/
│   │       ├── controllers/     # REST API endpoints
│   │       ├── services/        # Business logic layer
│   │       ├── repositories/    # JPA data access
│   │       ├── models/          # JPA entities
│   │       ├── dto/             # Data Transfer Objects
│   │       └── exceptions/      # Custom exception handlers
│   └── resources/
│       ├── db/migration/        # Flyway migration scripts
│       ├── application.properties
│       └── application-prod.properties
└── test/                        # Unit & integration tests
```

## 🔮 Future Enhancements

- [ ] Seasons and Races management
- [ ] Championship standings calculation
- [ ] Race results and points tracking
- [ ] Rider statistics and career history
- [ ] Team performance analytics
- [ ] Docker containerization
- [ ] CI/CD pipeline with GitHub Actions

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Your Name**
- GitHub: [@p-glazowski](https://github.com/p-glazowski)

## 🙏 Acknowledgments

- [MotoGP](https://www.motogp.com/) for the inspiration
- [Spring Boot](https://spring.io/projects/spring-boot) community
- [Render](https://render.com/) for free hosting

---

⭐️ If you find this project helpful, please give it a star on GitHub! ⭐️

---

**Live Demo:** [Swagger UI](https://motogp-stats-api.onrender.com/swagger-ui/index.html)
