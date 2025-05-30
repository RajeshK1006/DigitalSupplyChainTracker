
#  Digital Supply Chain Tracker

A Spring Boot–based backend application designed to **digitally monitor and manage the movement of items** across a supply chain — from suppliers to transporters to warehouses and retailers. It provides **real-time visibility**, **role-based access**, and **automated alerts** to ensure transparency and minimize delays.

---

##  Objectives

- Track product movement throughout the supply chain.
- Record item status at each checkpoint.
- Enable role-specific access and actions.
- Generate alerts for delivery issues (e.g., delays or damages).
- Provide insightful reports and dashboards for visibility.

---

##  Tech Stack

| Layer         | Technology                       |
|--------------|-----------------------------------|
| Framework    | Spring Boot                       |
| Security     | Spring Security  
| ORM          | Spring Data JPA                   |
| Database     | MySQL                             |
| Build Tool   | Maven                     |
| Utilities    | Lombok, ModelMapper     
| API Testing  | Postman                             |
| API Docs     | Swagger (springdoc-openapi)       |





##  Key Modules

### 1.  User & Role Management
- User registration and login
- Roles: **Admin**, **Supplier**, **Transporter**, **Warehouse Manager**
- Role-based API access and restrictions

### 2.  Item & Shipment Tracking
- Suppliers can register products and create shipments
- Track shipment status: `CREATED`, `IN_TRANSIT`, `DELIVERED`, `DELAYED`

### 3.  Checkpoints & Event Logs
- Transporters update shipment status at each node
- Logs include timestamp, location, and status (`RECEIVED`, `DAMAGED`, etc.)

### 4.  Alerts & Notifications
- Auto-alerts for late deliveries or damaged items
- Alert resolution tracking
- Daily/weekly shipment reports

### 5.  Reports
- View delivery performance per supplier/transport partner
- See real-time inventory status
- Analyze delayed or damaged shipments

---

##  Role-Based Access

| Role            | Capabilities                                         |
|------------------|-----------------------------------------------------|
| Admin            | Full access, user/role mgmt, view all reports       |
| Supplier         | Add items, create and view shipments                |
| Transporter      | Update shipment status, add checkpoints             |
| Warehouse Manager| Confirm delivery, receive goods                     |

---

##  Entity Overview

- **User**: ID, name, email, password, role
- **Item**: ID, name, category, supplierId, createdDate
- **Shipment**: ID, itemId, source, destination, expectedDelivery, currentStatus, assignedTransporter
- **CheckpointLog**: shipmentId, location, status, timestamp
- **Alert**: type (Delay/Damage), message, resolved

---

##  Sample API Endpoints

###  Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

###  User (Admin only)
- `GET /api/users`
- `PUT /api/users/{id}/role`

###  Items
- `POST /api/items`
- `GET /api/items`

###  Shipments
- `POST /api/shipments`
- `PUT /api/shipments/{id}/assign`
- `PUT /api/shipments/{id}/status`

###  Checkpoints
- `POST /api/checkpoints`
- `GET /api/checkpoints/shipment/{id}`

###  Alerts
- `GET /api/alerts`
- `PUT /api/alerts/{id}/resolve`

###  Reports
- `GET /api/reports/delivery-performance`
- `GET /api/reports/delayed-shipments`

---

##  Sample Use Case

1. Supplier registers an item and creates a shipment.
2. Transporter picks up the item and updates status at each checkpoint.
3. If the shipment is delayed, the system generates an alert.
4. Warehouse Manager receives and confirms the delivery.
5. Admin reviews the reports to assess performance and delays.

---

##  Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/supply_tracker
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

```

---

##  Optional Enhancements

| Feature            | Tech                    |
|--------------------|--------------------------|
| Email Alerts       | JavaMailSender           |
| Delay Detection    | Scheduled Jobs (@Scheduled)|
| API Documentation  | Swagger UI               |
| Frontend Dashboard | Angular / React          |

---

##  Project Structure

```
com.supplytracker
├── controller
├── dto
├── entity
├── repository
├── service
├── config
├── security
├── exception
└── SupplyTrackerApplication.java
```

---

##  Suggested Sprints

1. Setup base project
2. Implement item and shipment tracking
3. Add checkpoint and status logging
4. Build alerting system
5. Develop reports and dashboards
6. Final testing + Swagger documentation
