# Point Of Sale (POS) System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.org/)
[![React](https://img.shields.io/badge/React-18+-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5+-blue.svg)](https://www.typescriptlang.org/)
[![Bun](https://img.shields.io/badge/Bun-1.x-black.svg)](https://bun.sh/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

Easy management of your products, orders and business operations.
Flexible architecture allowing you to customize your business model.

> **🚀 Production-Ready**: Enterprise-grade POS system with comprehensive security, monitoring, and deployment capabilities.

## 📋 Table of Contents

- [Description](#-description)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Installation](#-installation)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Docker Development](#-docker-development)
- [Production Deployment](#-production-deployment)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

## 📖 Description

A modern, full-stack Point of Sale system designed for small to medium businesses. Built with enterprise-grade architecture featuring microservices design, comprehensive security, and cloud-ready deployment.

### Architecture Components
- **Frontend**: React 18 + TypeScript + Vite, optimized for performance with modern UI/UX
- **Backend**: Spring Boot 3 with JWT authentication, RESTful APIs, and comprehensive security
- **Database**: PostgreSQL with optimized schemas, indexing, and migration management
- **Infrastructure**: Docker containerization, Nginx reverse proxy, Redis caching

## 🌟 Features

- **🛍️ Product Management**: Complete CRUD operations with category organization and image upload
- **💳 Sales Processing**: Real-time cart management with multiple payment methods
- **👥 Customer Management**: Customer registration, profiles, and purchase history
- **📊 Inventory Tracking**: Stock monitoring with low-stock alerts and automated notifications
- **🔐 Security**: JWT authentication, rate limiting, input validation, and OWASP compliance
- **📱 Responsive Design**: Mobile-first design that works seamlessly across all devices
- **🔌 RESTful API**: Comprehensive API with Swagger documentation for easy integration
- **📈 Analytics**: Real-time sales statistics and business intelligence dashboard
- **🚀 Performance**: Optimized with caching, connection pooling, and database indexing
- **🐳 DevOps Ready**: Docker containerization with CI/CD pipeline support

## 🛠️ Tech Stack

### Frontend
- **React 18** - Modern UI library with hooks and context
- **TypeScript** - Type-safe development experience
- **Vite** - Lightning-fast build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **Bun** - Ultra-fast JavaScript runtime and package manager

### Backend
- **Java 17** - Latest LTS version with modern language features
- **Spring Boot 3** - Enterprise-grade framework with auto-configuration
- **Spring Security** - Comprehensive security framework
- **PostgreSQL** - Robust relational database with ACID compliance
- **Flyway** - Database migration and version control
- **Gradle** - Build automation and dependency management

### Infrastructure & DevOps
- **Docker** - Containerization for consistent deployments
- **Nginx** - High-performance reverse proxy and load balancer
- **Redis** - In-memory caching for improved performance
- **HikariCP** - High-performance database connection pooling

### Testing & Quality
- **Vitest** - Fast unit testing framework for frontend
- **JUnit 5** - Comprehensive testing framework for backend
- **ESLint + Prettier** - Code quality and formatting tools
- **SonarQube Ready** - Code quality analysis integration

## 🌐 Live Demo

You can see the application in action here: [https://possystem.jstevenon.com/](https://possystem.jstevenon.com/)

*Contact the repository owner for demo credentials*

## 📦 Installation

### Prerequisites
- Docker and Docker Compose
- Git

### Quick Start with Docker (Recommended)
```bash
# Clone the repository
git clone https://github.com/Jhons14/POSSystem.git
cd POSSystem

# Copy environment file and configure
cp .env.example .env
# Edit .env with your settings

# Start all services
./scripts/dev-start.sh
```

### Manual Installation
- Bun (latest version)
- Java 17+
- PostgreSQL 12+
- Gradle 8+

### Frontend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/Jhons14/POSSystem.git
   cd POSSystem/frontend
   ```

2. Install dependencies:
   ```bash
   bun install
   ```

3. Configure environment variables:
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. Start the development server:
   ```bash
   bun run dev
   ```
   The frontend will be available at http://localhost:5173

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd ../backend
   ```

2. Configure database connection in `src/main/resources/application.properties`

3. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```
   The API will be available at http://localhost:8080

### Database Setup
1. Create a PostgreSQL database
2. Run the SQL scripts in the `sql/` directory:
   ```bash
   psql -d your_database -f sql/schema.sql
   psql -d your_database -f sql/data.sql
   psql -d your_database -f sql/security.sql
   ```

## Features

- **Product Management**: Add, edit, and organize products by categories
- **Sales Processing**: Complete point-of-sale transactions with cart management
- **Customer Management**: Register and manage customer information
- **Inventory Tracking**: Monitor product stock and availability
- **Secure Authentication**: JWT-based user authentication and authorization
- **Responsive Design**: Works on desktop and mobile devices
- **RESTful API**: Well-documented API endpoints for integration

## Usage

### Access the System

1. **Login**: Navigate to `http://localhost:5173` and authenticate with your credentials

2. **Dashboard**: View sales statistics, inventory status, and business metrics

3. **Sales**: Process transactions by adding items to cart and completing purchases

4. **Products**: Manage your product catalog and categories

5. **Customers**: Register new customers and view customer information

## API Documentation

Once the backend is running, access the Swagger documentation at:
`http://localhost:8080/swagger-ui/index.html`

## Docker Development

### Available Services
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/pos/server/api
- **Database**: localhost:5432 (posdb)
- **Redis**: localhost:6379

### Useful Commands
```bash
# View all service logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Restart a service
docker-compose restart backend

# Stop all services
docker-compose down

# Rebuild and start
docker-compose up --build
```

## Production Deployment

For production deployment:
```bash
# Configure production environment
cp .env.example .env
# Edit .env with production values

# Deploy to production
./scripts/prod-deploy.sh
```

## 🧪 Testing

### Frontend Testing
```bash
cd frontend

# Run all tests
bun run test

# Run tests with coverage
bun run test:coverage

# Run tests in watch mode
bun run test --watch

# Run tests with UI
bun run test:ui
```

### Backend Testing
```bash
cd backend

# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# Run integration tests
./gradlew integrationTest
```

### Code Quality
```bash
# Frontend linting and formatting
bun run lint
bun run format
bun run type-check

# Backend code analysis (if SonarQube is configured)
./gradlew sonarqube
```

## 📊 Monitoring and Health Checks

### Health Endpoints
- **Application Health**: `GET /health`
- **Detailed Health**: `GET /health/detailed`
- **Actuator Endpoints**: `GET /actuator/health`
- **Metrics**: `GET /metrics/database`, `/metrics/cache`, `/metrics/performance`

### Performance Monitoring
- Database connection pool metrics
- Cache hit/miss ratios
- API response times
- Resource utilization monitoring
- Core Web Vitals tracking (frontend)

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Install dependencies**: `bun install` (frontend) and `./gradlew build` (backend)
4. **Make your changes** following the existing code style
5. **Run tests**: Ensure all tests pass before submitting
6. **Run linting**: `bun run lint && bun run format`
7. **Commit changes**: `git commit -m 'Add amazing feature'`
8. **Push to branch**: `git push origin feature/amazing-feature`
9. **Open a Pull Request** with a clear description of your changes

### Development Guidelines
- Follow existing code conventions and patterns
- Write tests for new features
- Update documentation as needed
- Ensure Docker builds work correctly
- Check that all security standards are maintained

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Authors

- **Jhon Orjuela** - *Full Stack Developer & Project Lead* - [GitHub](https://github.com/Jhons14)

## 🙏 Acknowledgments

- Inspired by the needs of small retail businesses for affordable POS solutions
- Built with modern web technologies and best practices
- Designed for scalability and enterprise-grade reliability
- Community feedback and contributions are highly valued

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/Jhons14/POSSystem/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Jhons14/POSSystem/discussions)
- **Email**: Contact repository owner for direct support

---

<div align="center">

**⭐ Star this repo if you find it helpful!**

Made with ❤️ by [Jhon Orjuela](https://github.com/Jhons14)

</div>
