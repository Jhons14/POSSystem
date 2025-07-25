# Point Of Sale (POS) System

Easy management of your products, orders and business operations.
Flexible architecture allowing you to customize your business model.

## Description
This project consists of three main components:
- **Frontend**: Built with React.js and Vite for fast development and optimal performance
- **Backend**: Java Spring Boot application with JWT authentication and RESTful APIs, built with Gradle
- **Database**: PostgreSQL for reliable data persistence

## Preview
You can see an application's preview here https://possystem.jstevenon.com/ <br/>
*Contact the repository owner for demo credentials*

## Installation

### Prerequisites
- Node.js 18+ and npm
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
   npm install
   ```

3. Configure environment variables:
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. Start the development server:
   ```bash
   npm run dev
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

## Contributing

We welcome contributions! To contribute:

1. **Fork the repository.**
2. **Create a new branch** (`git checkout -b feature/your-feature`).
3. **Make your changes.**
4. **Commit your changes** (`git commit -m 'Add some feature'`).
5. **Push to the branch** (`git push origin feature/your-feature`).
6. **Open a Pull Request.**

Please make sure your code adheres to the coding conventions and standards used in the project.

## License

This project is licensed under the MIT License

## Authors

- **Jhon Orjuela** - _Initial work_ - [Jhon's GitHub](https://github.com/Jhons14)

## Acknowledgments

- This project was inspired by the needs of small retail businesses to have an affordable and efficient POS system.
