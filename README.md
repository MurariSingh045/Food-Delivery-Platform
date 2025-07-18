# 🍔 Food Delivery Platform - Microservices Project

A scalable and modular **Food Delivery Application** built using **Spring Boot Microservices**, with service discovery, API gateway, JWT-based authentication, and inter-service communication using Feign Clients and Web Clients.

## HIGH-LEVEL-DESIGN

![Architecture](./image/Overall-Aechitecture.jpg)

---

## 🛠 Tech Stack

- **Spring Boot (v3+)**
- **Spring Cloud Gateway**
- **Spring Security (JWT Auth)**
- **Spring Data JPA + MySQL**
- **Eureka Discovery Server**
- **OpenFeign for Inter-Service Calls**
- **Docker (optional for deployment)**

---

## 🧱 Microservices Overview

| Service             | Description                                      | Port  |
|---------------------|--------------------------------------------------|--------|
| `DISCOVERY-SERVICE` | Eureka Server for service registration/discovery | `8761` |
| `API-GATEWAY`       | Single entry point for all requests              | `8080` |
| `AUTH-SERVICE`      | User registration, login, JWT generation         | `8081` |
| `RESTAURANT-SERVICE`| Restaurant & Menu management                     | `8083` |
| `ORDER-SERVICE`     | Order placement, status updates, cancellations   | `8082` |

---

## 🔄 Service Communication Flow

Client → API Gateway → Auth / Restaurant / Order Services → DB
↓
Discovery Service (Eureka)

## ✅ Features

### 🔐 Auth Service
- JWT-based login
- Role-based access (`USER`, `RESTAURANT`, `ADMIN`)

### 🍽️ Restaurant Service
- Add restaurants(only by restaurant owners)
- Add menu items (only by restaurant owners)
- View restaurants(by users , restaurant owners and admin)
- View menu items(users & restaurant owners)
- Delete restaurant & menu items(only by restaurants owners)

### 🛒 Order Service
- Place orders (users)
- View order status(usres , restaurant owners & admin)
- Order status update(only by restaurant owners)
- Cancel orders (user & restaurant)
- Admin can force cancel any order

### 📡 Service Discovery
- All services register with **Eureka**

---

### 🌉 API Gateway
- Acts as the single entry point for all clients
- Forwards requests to respective services (via Eureka)
- Validation  of JWT token
- Performs routing based on service name (e.g., `lb://AUTH-SERVICE`)
- Applies global filters (e.g., authentication, logging)
- Adds authentication headers (`X-User-Id`, `X-User-Role`) to outgoing requests
- Gateway routes requests using logical names (e.g., `lb://ORDER-SERVICE`)

---
## 🧪 Sample Endpoints
```markdown
> **Auth Service**
```http
POST /auth/register  (for registration)
POST /auth/login     (for login)
GET /auth/validate   (Token Validation)

---
```
🏢 Restaurant Service
```http
POST /restaurant/add         (Add restaurant)
GET  /restaurant/{id}        (Get restaurant by ID)
GET  /restaurant/all         (List all restaurants)
GET /restaurant/all          (Get all restaurant by users & owners)
POST /menu/add               (Add menu items)
GET  /item/{id}              (Get menu item by id)

```
📦 Order Service
```http

POST   /order/place-order                 (Place an order (USER role))
PATCH  /order/{id}/status                 (Update order status (RESTAURANT owner only))
PATCH  /order/{id}/cancel                 (Cancel order (USER only, if status = PLACED))
PATCH  /order/{id}/admin-cancel           (Force cancel order (ADMIN only))
GET    /order/restaurant/{id}             (Get all orders of a restaurant (RESTAURANT owner))
GET    /order/user/{id}                   (Get orders of a specific user (USER only))
GET    /order/all?status=PLACED           (Filter orders by status (ADMIN))
GET   /order/admin/stats                  (Admin can have platform stats)

```

🚀 How to Run Locally : 
---
 ## Run below services seperately : 

1.Discovery Service

2.Auth Service

3.Restaurant Service

4.Order Service

5.Api Gateway

Ensure all the prerequisites(MySQL, JAVA etc) are installed into your local system

---

🚀 Getting Started :

1.Clone the Repository
```bash
   git clone https://github.com/murarikumar0/FOOD-DELIVERY-PLATFORM.git

   cd FOOD-DELIVERY-PLATFORM
```
---

## 🛢️ Database Schema
```bash
  1.auth_db_food_deliver

  2.restaurant_db_food_delivery

  3.order_db_food_delivery

```

---

### Let's have a look at the API endpoints and review their responses using Postman

---
Run all Microservices 

*Once all services run check it out on Eureka Discovery Service*

![Discovery System](./image/Eureka.png)

*Opne postman and make request for register user,  below is the response i am getting.*

![SignUp User](./image/User-Registration.png)

*after registering user i am requesting for login and in response got JWT token*

![SignIn User](./image/TokenGeneration.png)

*after that validating the role*

![Validate User](./image/Admin-role.png)

*before moving to promoting a user to restaurant owner ,let's have a look at the database(user_table)*

![User-DB](./image/Users.png)

*now admin will promote a user-role to restaurant-role ,but before that login throgh admin email*
*and put token into header while promoting a user to restaurant role*

![promote to restaurant](./image/Admin-promote-user-to-Restaurant-role.png)

*now a restaurnt-role user can add restaurant*

![add restaurant](./image/restaurantOwner-add-restaurant.png)

*after adding restaurant successfully ,restaurant owner is adding menu-Items in their restaurant*

![add menuItems](./image/RestaurantOwner-add-MenuItem.png)

*if restaurant owner wants then he can delete his menuItems from restaurant , like that updateItems and delete restaurant too.*

![Delete Restaurant](./image/ResturantOwner-deleteItem-ById.png)

*here now after login as  user role i am validing the user throgh token which i got in response while login and  validating the token*

![User Validation](./image/User-role.png)

*now requesting to check the restaurant available*

![Check Restaurants](./image/User-check-all-Restaurant.png)

*checking menu-Items of each restaurant*

![Check MenuItems](./image/User-Check-All-MenuItems.png)

*now time comes to place the order for that must be logged in as user below i am showing how to place order*

![Place-Order](./image/User-place-order.png)

*after placing order users can check their orders(if want)*

![Check the order](./image/user-get-all-order.png)

*here restaurant owner are checking all orders but before that ensure you are logged in as restaurant role*

![resOwner check order](./image/Owner-check-order-of-restaurant.png)

*further restaurant owner will update the status of order but order status should be placed then only can change*

![resOwner check order](./image/Owner-update-status.png)

*if a user wants then he/she can cancell their order but before the order is delivered*

![user cancell order](./image/User-cancel-order.png)

*Now times come to admin functionality a admin can have all order details below admin is getting all orders*

![Admin get all order](./image/admin-check-all-order.png)

*Admin can have overall stats of the platform (like how many users are available, total orders, order status etc) below i am showing you how*

![Admin get stats](./image/admin-check-platform-stats.png)

*if admin wants he can forcefully cancelled the orders*

![Admin cancell order](./image/admin-cancel-order.png)


### 🛢️some glimpse of Database Schemas

*Oder  DB*
![Order DB](./image/order-db.png)



*Order-Status DB*
![Order DB](./image/order-status.png)


## I am working on rest of the services (like DELIVERY SERVICE, PAYMENT SYSTEM) and will add ASAP.

---

## THANK YOU

📄 License
```
This project is licensed under the MIT License.
```

---
🤝 Contributing :

 Contributions are welcome! Open an issue or pull request.

---

👨‍💻 Author

Murari Kumar(Java Full Stack Developer)

---










