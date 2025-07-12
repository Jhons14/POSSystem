# Point Of Sale (POS) system

Easy management of your products, orders and bussiness.
You're free to decide your own custom bussines model.

## Description
This proyect is building by front, server and database:
- Front-end uses React.js and Vite technologies.
- The server (backend) uses JAVA Spring, implements security bearer auth token with credencials, and was built on graddle.
- The database uses PostgreSQL.

You can see an application's preview here https://front-market-nu.vercel.app/ 
## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Jhons14/front-market.git
   ```
2. Navega al directorio del proyecto:
   ```bash
   cd front-market
   ```
3. Instala las dependencias necesarias:
   ```bash
   npm install
   ```
4. Configura las variables de entorno:
   ```bash
    cp .env.example .env
   ```
5. Configura las variables de entorno:
   ```bash
    npm run DEV
   ```
The frontend should now be running on http://localhost:5173.

## Usage

### Access the System

1. **Login:**

   - Open your browser and navigate to `http://localhost:5173`.

2. **Dashboard:**

   - After logging in, you will be taken to the dashboard, where you can view sales stats, manage inventory, and more.

3. **Sales:**

   - Navigate to the "Sales" section to process transactions. Add items to the cart, apply discounts, and complete the sale.

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
