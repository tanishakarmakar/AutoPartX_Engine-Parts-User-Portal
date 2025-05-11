
### AutoPartX – Engine Parts User Portal 🚗🔧

AutoPartX is a full-stack web application that streamlines the buying, selling, and reviewing of engine parts online. Built with modern Java technologies, it combines robust backend functionality with a responsive user interface to enhance the user and admin experience.


## 🧩 Features

- 🛒 Browse, search, and view detailed information about engine parts
- 📝 User registration, login, and profile management with Spring Security
- ✍️ Add and view part reviews
- 📦 Admin dashboard for managing products and users
- 🔔 Real-time notifications for events like new reviews or part listings
- 📊 Seasonal statistics to monitor product trends

---

## ⚙️ Tech Stack

- **Backend:** Spring Boot, Spring Security, Spring Data JPA, Hibernate  
- **Frontend:** Thymeleaf, HTML/CSS, JavaScript  
- **Database:** PostgreSQL  
- **Build Tool:** Gradle  

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Gradle (or use the included Gradle Wrapper)
- PostgreSQL (installed and running)
- Git

---

### 📥 Clone the Repository

```bash
git clone https://github.com/tanishakarmakar/AutoPartX_Engine-Parts-User-Portal.git
cd AutoPartX_Engine-Parts-User-Portal
````

---

### 🛠️ Configure the Database

1. Ensure PostgreSQL is installed and the server is running.
2. Create a new database:

```sql
CREATE DATABASE engine_parts_portal;
```

3. Open the file `src/main/resources/application.properties` and update the following lines with your PostgreSQL credentials:

```properties
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```

---

### 📦 Build and Run the App

Using Gradle Wrapper:

```bash
./gradlew bootRun
```

Or, if Gradle is installed globally:

```bash
gradle bootRun
```

---

### 🔗 Access the Application

Visit the following URL in your browser:

```
http://localhost:8080/
```

---

## 📁 Project Structure

```
AutoPartX_Engine-Parts-User-Portal/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/demo/
│       ├── resources/
│       │   ├── static/
│       │   ├── templates/
│       │   └── application.properties
├── build.gradle
├── settings.gradle
```

---

## 🔐 Security

* Passwords are securely hashed using BCrypt
* Role-based access for users and administrators
* Notifications can only be viewed by the associated user

---

## 📊 Analytics and Notifications

* Real-time alerts for part listings and reviews
* Seasonal listing statistics (Spring, Summer, Fall, Winter)

---

## 💡 Future Improvements

* Email notification system
* Advanced search and filter functionality
* RESTful APIs for mobile app integration

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

