# 🛒 ShopSphere E-Commerce API

ShopSphere is a secure RESTful E-Commerce backend application built using Spring Boot. It provides APIs for user authentication, product and category management, shopping carts, wishlists, and order placement.

The application uses JWT-based authentication and role-based authorization. It is containerized using Docker and can be run together with MySQL using Docker Compose.

---

## 🚀 Features

- User registration and login
- JWT-based authentication
- Role-based authorization
- Product management
- Category management
- Shopping cart management
- Wishlist management
- Order placement and order history
- Product search and filtering
- Stock validation during order placement
- Automatic stock reduction after placing an order
- Swagger/OpenAPI documentation
- Global exception handling
- Request validation
- MySQL database integration
- Docker support
- Docker Compose support

---

## 🛠️ Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- Hibernate
- Jakarta Validation

### Database

- MySQL 8

### Security

- JWT Authentication
- BCrypt Password Encryption
- Role-Based Authorization

### API Documentation

- Swagger UI
- OpenAPI

### DevOps

- Docker
- Docker Compose

### Testing

- JUnit 5
- Spring Boot Test
- MockMvc
- H2 Database
- Testcontainers

---

# 📁 Project Structure

```text
ecommerce
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.shopsphere.ecommerce
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       │   ├── Auth
│   │   │       │   ├── Product
│   │   │       │   ├── Category
│   │   │       │   ├── Cart
│   │   │       │   ├── Order
│   │   │       │   └── WishList
│   │   │       ├── dto
│   │   │       ├── entity
│   │   │       ├── exception
│   │   │       ├── mapper
│   │   │       ├── repository
│   │   │       └── service
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .gitignore
├── .env.example
├── pom.xml
└── README.md
```

---

# ⚙️ Prerequisites

Before running the application locally, make sure you have installed:

- Java 17 or higher
- Maven
- MySQL
- Docker and Docker Compose (for containerized setup)
- Git

---

# 🔐 Environment Variables

The application uses environment variables for sensitive configuration.

Create a `.env` file in the project root.

```env
DB_URL=jdbc:mysql://localhost:3306/shopsphere
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_jwt_secret
```

A sample configuration is available in:

```text
.env.example
```

> ⚠️ Never commit your actual `.env` file to GitHub.

---

# 💻 Running the Application Locally

## 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/shopsphere-ecommerce.git
```

Move into the project directory:

```bash
cd shopsphere-ecommerce
```

---

## 2. Configure environment variables

Create a `.env` file:

```env
DB_URL=jdbc:mysql://localhost:3306/shopsphere
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_jwt_secret
```

---

## 3. Create the MySQL database

Open MySQL and run:

```sql
CREATE DATABASE shopsphere;
```

---

## 4. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

Or run the `EcommerceApplication` class directly from your IDE.

The application will start at:

```text
http://localhost:8080
```

---

# 🐳 Running with Docker

The project includes Docker support for both the Spring Boot application and MySQL.

## 1. Start the containers

From the project root:

```bash
docker compose up --build
```

This will:

1. Build the Spring Boot application.
2. Create the application Docker image.
3. Start a MySQL container.
4. Start the ShopSphere application container.
5. Connect the application to MySQL through the Docker network.

---

## 2. Run in detached mode

```bash
docker compose up --build -d
```

---

## 3. Check running containers

```bash
docker ps
```

Expected containers:

```text
shopsphere-app
shopsphere-mysql
```

---

## 4. Stop containers

```bash
docker compose down
```

---

## 5. Stop containers and remove database data

```bash
docker compose down -v
```

> ⚠️ This command removes the MySQL Docker volume and deletes the data stored inside the Docker database.

---

# 🏗️ Docker Architecture

```text
                    Docker Compose
                         │
              ┌──────────┴──────────┐
              │                     │
              ▼                     ▼
     ┌─────────────────┐   ┌─────────────────┐
     │ ShopSphere App  │   │      MySQL      │
     │                 │   │                 │
     │ Spring Boot     │──▶│   Database      │
     │ Port: 8080      │   │   Port: 3306    │
     └─────────────────┘   └─────────────────┘
              │                     │
              └──── Docker Network ─┘
```

Inside Docker, the application connects to MySQL using:

```text
jdbc:mysql://mysql:3306/shopsphere
```

The hostname `mysql` is the Docker Compose service name.

---

# 🔐 Authentication and Authorization

ShopSphere uses JWT-based authentication.

## Authentication Flow

```text
User
 │
 ▼
POST /auth/register
 │
 ▼
User registered
 │
 ▼
POST /auth/login
 │
 ▼
JWT Token generated
 │
 ▼
Client sends:
Authorization: Bearer <JWT_TOKEN>
 │
 ▼
JWT Authentication Filter
 │
 ▼
User authenticated
 │
 ▼
Protected API accessed
```

---

# 👥 Roles

The application supports role-based authorization.

Example roles:

```text
USER
ADMIN
```

## ADMIN Permissions

Admins can:

- Create products
- Update products
- Delete products

## USER Permissions

Authenticated users can:

- Manage their cart
- Manage their wishlist
- Place orders
- View their orders
- Access protected APIs according to application security rules

---

# 📚 API Documentation

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger provides interactive API documentation and allows APIs to be tested directly from the browser.

---

# 🔗 API Endpoints

## 🔐 Authentication

### Register User

```http
POST /auth/register
```

Request body:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

---

### Login

```http
POST /auth/login
```

Request body:

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

The login response contains a JWT token.

Use the token for protected endpoints:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

---

# 📦 Product APIs

### Get All Products

```http
GET /products
```

Supports pagination and sorting.

Example:

```text
/products?page=0&size=10&sort=name,asc
```

---

### Get Product by ID

```http
GET /products/{id}
```

Example:

```text
/products/1
```

---

### Create Product

```http
POST /products
```

Requires:

```text
ADMIN role
```

---

### Update Product

```http
PUT /products/{id}
```

Requires:

```text
ADMIN role
```

---

### Delete Product

```http
DELETE /products/{id}
```

Requires:

```text
ADMIN role
```

---

### Search by Brand

```http
GET /products/brand/{brand}
```

Example:

```text
/products/brand/Apple
```

---

### Search by Name

```http
GET /products/search/name?keyword=phone
```

---

### Get Available Products

```http
GET /products/available
```

---

### Search by Brand and Availability

```http
GET /products/search?brand=Apple&available=true
```

---

### Get Products Below Price

```http
GET /products/price/less-than/{price}
```

Example:

```text
/products/price/less-than/50000
```

---

### Get Products Above Price

```http
GET /products/price/greater-than/{price}
```

---

### Get Products At or Below Price

```http
GET /products/price/less-than-equal/{price}
```

---

### Get Products At or Above Price

```http
GET /products/price/greater-than-equal/{price}
```

---

### Get Products by Category

```http
GET /products/category/{categoryName}
```

---

### Filter by Category and Price

```http
GET /products/category-price?categoryName=Electronics&price=50000
```

---

### Filter by Category and Brand

```http
GET /products/category-brand?categoryName=Electronics&brand=Apple
```

---

### Get Available Products by Category and Brand

```http
GET /products/category-brand/available?categoryName=Electronics&brand=Apple
```

---

### Get Available Products by Category and Price

```http
GET /products/category-price/available?categoryName=Electronics&price=50000
```

---

### Filter by Category, Brand and Price

```http
GET /products/category-brand-price?categoryName=Electronics&brand=Apple&price=50000
```

---

### Apply All Filters

```http
GET /products/filter?categoryName=Electronics&brand=Apple&price=50000
```

---

# 🗂️ Category APIs

### Create Category

```http
POST /categories
```

Example request:

```json
{
  "name": "Electronics"
}
```

---

### Get All Categories

```http
GET /categories
```

---

# 🛒 Cart APIs

All cart APIs require JWT authentication.

### Add Product to Cart

```http
POST /cart/{productId}?quantity={quantity}
```

Example:

```text
POST /cart/1?quantity=2
```

---

### Get Cart

```http
GET /cart
```

---

### Update Cart Item Quantity

```http
PUT /cart/{productId}?quantity={quantity}
```

Example:

```text
PUT /cart/1?quantity=3
```

---

### Remove Product from Cart

```http
DELETE /cart/{productId}
```

---

### Clear Cart

```http
DELETE /cart
```

---

# ❤️ Wishlist APIs

All wishlist APIs require JWT authentication.

### Add Product to Wishlist

```http
POST /wishlist/{productId}
```

Example:

```text
POST /wishlist/1
```

---

### Get Wishlist

```http
GET /wishlist
```

---

### Remove Product from Wishlist

```http
DELETE /wishlist/{productId}
```

---

# 📦 Order APIs

All order APIs require JWT authentication.

### Place Order

```http
POST /orders
```

The order is created using the products currently available in the user's cart.

During order placement, the application:

1. Retrieves the authenticated user.
2. Retrieves the user's cart.
3. Validates that the cart is not empty.
4. Checks whether each product is available.
5. Checks stock availability.
6. Creates order items.
7. Calculates the total order amount.
8. Reduces product stock.
9. Saves the order.
10. Clears the user's cart.

---

### Get My Orders

```http
GET /orders
```

Returns the order history of the authenticated user.

---

# 🔒 Protected Endpoint Authentication

For protected APIs, add the following header:

```text
Authorization: Bearer YOUR_JWT_TOKEN
```

Example:

```text
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

# 🗄️ Database Configuration

## Local Development

The application connects to:

```text
jdbc:mysql://localhost:3306/shopsphere
```

## Docker Environment

The application connects to:

```text
jdbc:mysql://mysql:3306/shopsphere
```

The difference is:

```text
Local application:
localhost = your computer

Docker application:
mysql = MySQL service/container
```

---

# 🧪 Testing

The project includes support for:

- JUnit 5
- Spring Boot Test
- MockMvc
- H2 Database
- Testcontainers
- MySQL Testcontainers

Run tests using:

```bash
mvn test
```

Build the application without running tests:

```bash
mvn clean package -DskipTests
```

---

# 📦 Build the Application

Create the JAR:

```bash
mvn clean package
```

The generated JAR will be available inside:

```text
target/
```

---

# ⚙️ Configuration

Important application configuration includes:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
```

Hibernate configuration:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# 🔄 Typical Application Flow

```text
Client
  │
  ▼
Spring Boot REST Controller
  │
  ▼
JWT Authentication Filter
  │
  ▼
Spring Security
  │
  ▼
Service Layer
  │
  ▼
Repository Layer
  │
  ▼
Spring Data JPA / Hibernate
  │
  ▼
MySQL Database
```

---

# 🚀 Future Improvements

Possible future improvements include:

- Payment gateway integration
- Email notifications
- Order cancellation
- Product image upload
- Product reviews and ratings
- Admin dashboard
- User profile management
- Refresh tokens
- Redis caching
- CI/CD pipeline using GitHub Actions
- Cloud deployment

---

# 👩‍💻 Author

**Deepika Kolli**

GitHub: https://github.com/Deepikakolli4

LinkedIn: https://www.linkedin.com/in/deepikakolli4/

---

## ⭐ If you like this project

Give the repository a star on GitHub!
