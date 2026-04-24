# AI-Powered Travel Planning and Budget Recommendation System

A modern AI travel planner built with **Spring Boot**, **Thymeleaf**, **SQLite**, and **OpenAI**.  
The system allows users to register, log in, create trips, generate AI-powered itineraries, view destination visuals, manage budgets in **MUR**, and save travel plans in a local SQLite database.

---

## Project Version

```text
Version: 1.0.0
Project Type: Full-stack Java Web Application
Framework: Spring Boot
Database: SQLite 3
Frontend: Thymeleaf + Bootstrap + Custom CSS
AI Provider: OpenAI API
Currency Focus: MUR - Mauritian Rupee
```

---

## Main Features

- User registration and login
- Secure authentication with Spring Security
- Create, view, edit, delete trips
- Save trips into SQLite database
- AI-generated travel itinerary
- AI-generated budget recommendation
- Destination insights
- Packing guide
- Travel checklist
- Destination image support
- Modern responsive UI
- Tropical traveller-style interface
- Demo data seeding
- MUR-based budget planning
- VS Code friendly setup

---

## Technologies Used

```text
Java 17
Spring Boot
Spring MVC
Spring Security
Spring Data JPA
Thymeleaf
SQLite 3
Hibernate
Bootstrap 5
Bootstrap Icons
OpenAI API
Gradle
VS Code
```

---

## Project Structure

```text
ai-travel-planner/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/aitravelplanner/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── util/
│   │   │
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/css/
│   │       ├── static/js/
│   │       └── application.properties
│   │
├── build.gradle
├── settings.gradle
├── gradlew.bat
├── setup-and-run.ps1
└── README.md
```

---

## Requirements

Install these before running:

```text
Java 17 JDK
VS Code
Internet connection
OpenAI API key optional
```

Recommended Java:

```text
Eclipse Temurin JDK 17
```

Check Java:

```powershell
java -version
javac -version
```

Expected:

```text
java 17.x
javac 17.x
```

---

## Clone / Download Project

If using Git:

```powershell
git clone <your-repository-url>
cd ai-travel-planner
```

If using ZIP:

```text
1. Extract the ZIP file.
2. Open the extracted ai-travel-planner folder in VS Code.
3. Open VS Code terminal inside the project folder.
```

---

## Open Project in VS Code

```powershell
cd C:\Users\noorg\OneDrive\Desktop\travelplannerAI\ai-travel-planner
code .
```

---

## OpenAI API Key Setup

Open:

```text
src/main/resources/application.properties
```

Add your key:

```properties
openai.api.key=YOUR_OPENAI_API_KEY_HERE
```

If you do not add a key, the app can still run with fallback/demo data.

---

## Example application.properties

```properties
spring.application.name=ai-travel-planner

server.port=8080

spring.datasource.url=jdbc:sqlite:travel_planner.db
spring.datasource.driver-class-name=org.sqlite.JDBC

spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

spring.thymeleaf.cache=false

openai.api.key=YOUR_OPENAI_API_KEY_HERE
openai.model=gpt-4.1-mini
openai.base-url=https://api.openai.com/v1

app.seed.enabled=true
```

---

## Run the Project

### Option 1: Using setup script

```powershell
Unblock-File .\setup-and-run.ps1
.\setup-and-run.ps1
```

### Option 2: Using Gradle wrapper directly

```powershell
.\gradlew.bat clean bootRun
```

---

## Open in Browser

```text
http://localhost:8080
```

---

## Demo Login

```text
Email: demo@travelplanner.app
Password: password
```

Admin demo:

```text
Email: admin@travelplanner.app
Password: password
```

---

## SQLite Database

The app creates a SQLite database automatically:

```text
travel_planner.db
```

If demo data is not appearing, stop the app and delete the database:

```powershell
del .\travel_planner.db
```

Then run again:

```powershell
.\gradlew.bat clean bootRun
```

---

## Important Pages

```text
/                Home page
/login           Login page
/register        Register page
/dashboard       User dashboard
/trips           Saved trips
/trips/new       Create trip
/trips/{id}      Trip details
/trips/{id}/edit Edit trip
```

---

## Main Workflow

```text
1. Register or login.
2. Open dashboard.
3. Create a new trip.
4. Enter destination, origin city, dates, budget, style, and interests.
5. Save trip.
6. Open trip details.
7. Click Generate / Regenerate.
8. View AI itinerary, budget, insights, packing list, and checklist.
```

---

## AI Features

The OpenAI integration generates:

```text
Daily itinerary
Budget breakdown in MUR
Destination insights
Packing guide
Travel checklist
Image prompt / destination visual support
```

---

## Example Trip Data

Seeded trips include:

```text
Bali, Indonesia
Santorini, Greece
Zanzibar, Tanzania
Maldives
Dubai, UAE
Paris, France
Tokyo, Japan
Cape Town, South Africa
Mauritius North Coast Escape
Swiss Alps, Switzerland
Rome, Italy
New York, USA
```

---

## Troubleshooting

### Java 17 error

If you get:

```text
Cannot find a Java installation matching languageVersion=17
```

Install Java 17:

```powershell
winget install EclipseAdoptium.Temurin.17.JDK
```

Restart VS Code and run:

```powershell
java -version
```

---

### Gradle command not found

You do not need system Gradle if the wrapper exists.

Use:

```powershell
.\gradlew.bat bootRun
```

Not:

```powershell
gradle bootRun
```

---

### Whitelabel Error on /trips/new

Usually caused by a Thymeleaf template error.

Check the VS Code terminal stack trace.

Common fix:

```text
Make sure trip-form.html uses ${formAction}
Do not use ${tripRequest.id} if TripRequest has no id field.
```

---

### Generate gives 404

Make sure the button uses POST form:

```html
<form th:action="@{/trips/{id}/generate(id=${trip.id})}" method="post">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
    <button type="submit">Generate</button>
</form>
```

And controller has:

```java
@PostMapping("/{id}/generate")
```

---

### Demo trips not showing

Delete the database:

```powershell
del .\travel_planner.db
```

Run again:

```powershell
.\gradlew.bat clean bootRun
```

---

## Build Project

```powershell
.\gradlew.bat clean build
```

Generated build files will be inside:

```text
build/
```

---

## Run JAR

After build:

```powershell
java -jar build/libs/ai-travel-planner-0.0.1-SNAPSHOT.jar
```

---

## Future Improvements

```text
Admin dashboard
Trip sharing link
PDF itinerary export
Email itinerary to user
Flight search integration
Hotel recommendation API
Weather API integration
Google Maps integration
User profile page
Multi-currency conversion
Real payment simulation
Advanced AI image gallery
```

---

## Project Summary

This project demonstrates a complete Java web application with authentication, database persistence, AI integration, modern frontend design, and real-world travel planning functionality. It is suitable for a university project, portfolio showcase, or as a base for a more advanced tourism platform.

---

## Author

```text
AI Travel Planner Project
Built using Spring Boot, Thymeleaf, SQLite, and OpenAI.
```



# 🌍 AI-Powered Travel Planning and Budget Recommendation System

A **modern, intelligent travel planning platform** built using **Spring Boot**, **Thymeleaf**, **SQLite**, and **OpenAI**.

This system allows users to **create trips, generate AI-powered itineraries, visualize destinations, plan budgets in Mauritian Rupees (MUR)**, and manage their entire travel experience from a clean, premium dashboard.

## Login
![alt text](image.png)

## Navbar
![alt text](image-1.png)

## Homepage
![alt text](image-2.png)
![alt text](image-3.png)
![alt text](image-4.png)

## Footer
![alt text](image-5.png)

## Dashboard
![alt text](image-6.png)
![alt text](image-7.png)

## Trips
![alt text](image-8.png)
![alt text](image-9.png)

## View Trips Cards CRUD
![alt text](image-10.png)
![alt text](image-11.png)

## Edit Trps Card
![alt text](image-12.png)

## Update Trip Card
![alt text](image-13.png)

## Delete Trip Card
![alt text](image-14.png)
![alt text](image-15.png)

## Create New Trips
![alt text](image-16.png)

## About
![alt text](image-17.png)
![alt text](image-18.png)

## Register New Account
![alt text](image-19.png)


## Database DB SQLLite 3
![alt text](image-23.png)
![alt text](image-20.png)


## Backend SpringBoot MVC Files
![alt text](image-21.png)

## Frontend ThemeLeaf Files 
![alt text](image-22.png)

## Author

This project was designed and developed by:

**Abdurrahman Noor-Ul-Haqq Gurib**  
Senior Software Engineer  

**Beebee Amreen Moushaifah Nassmally**  
System Specialist <3  

---

#  Project Overview

This project simulates a **real-world travel booking & planning platform** similar to:

- Booking.com
- Airbnb Experiences
- Google Travel

But enhanced with **AI capabilities**, including:

- Smart itinerary generation
- Budget optimization
- Travel insights
- Packing and checklist automation

---

#  Core Idea

Instead of manually planning trips, users simply:

```text
1. Enter destination + preferences
2. Click Generate
3. AI builds the full travel plan instantly
```

---

#  Version Information

```text
Version: 1.0.0
Type: Full-stack Web Application
Architecture: MVC (Spring Boot)
Database: SQLite 3 (file-based)
AI Engine: OpenAI API
Frontend: Thymeleaf + Bootstrap + Custom CSS
Currency: MUR (Mauritian Rupees)
```

---

# 🧩 Feature Breakdown (DETAILED)

---

##  1. Authentication System

### What it does:
- Allows users to register and log in securely.
- Stores encrypted passwords using **Spring Security + BCrypt**.

### Why it's important:
- Simulates real-world application security.
- Ensures each user sees **only their own trips**.

### Behind the scenes:
```text
User -> Login Form -> Spring Security -> Database Validation -> Session Created
```

---

##  2. Trip Management (CRUD)

### Features:
- Create a new trip
- View all trips
- Edit trip details
- Delete trips
- Update trip status (PLANNED, UPCOMING, COMPLETED)

### Data stored:
```text
Destination
Origin City
Dates
Travelers
Budget
Currency (MUR)
Travel Style
Interests
Notes
```

### Why it's powerful:
- Full lifecycle management of travel plans
- Personalized user experience

---

##  3. AI Itinerary Generation

### What happens:
When user clicks **Generate**, the system:

```text
1. Sends trip data to OpenAI
2. AI processes context
3. Returns structured travel plan
4. Saves it in database
5. Displays beautifully in UI
```

### Generated Content:
- Daily itinerary
- Budget breakdown
- Destination insights
- Packing list
- Travel checklist

### Example:
```text
Day 1:
- Arrival in Bali
- Hotel check-in
- Sunset beach dinner

Day 2:
- Temple visit
- Spa session
- Night market exploration
```

---

##  4. Budget Planning (MUR-based)

### What it includes:
- Flight estimate
- Hotel costs
- Food expenses
- Activities budget

### Why MUR?
- Project is localized for **Mauritius**
- Real-world relevance for local users

---

##  5. Destination Insights

AI provides:
- Best places to visit
- Cultural tips
- Safety advice
- Seasonal recommendations

---

##  6. Packing Guide

Automatically generated:
```text
- Clothing suggestions
- Travel essentials
- Weather-based items
- Activity-based gear
```

---

##  7. Travel Checklist

Ensures user does not forget:
```text
- Passport
- Tickets
- Insurance
- Currency exchange
```

---

##  8. Destination Image System

### Current behavior:
- Uses:
  - OpenAI-generated images (if API key provided)
  - OR fallback images (Unsplash URLs)

### Why:
- Avoid broken UI if AI key missing
- Always display beautiful visuals

---

##  9. Dashboard System

### Displays:
- Total trips
- Upcoming trips
- Budget summary
- AI usage count

### Design:
- Premium cards
- Clean layout
- Tropical travel theme

---

## 🎨 10. Modern UI/UX Design

### Design philosophy:
- Tropical travel vibe 🌴
- Soft gradients
- Rounded cards
- Glassmorphism effects

### Tech used:
```text
Bootstrap 5
Custom CSS
Responsive grid
Icons (Bootstrap Icons)
```

---

##  11. Responsive Design

Works on:
- Desktop
- Tablet
- Mobile

---

##  12. SQLite Database

### Why SQLite:
- Lightweight
- No installation required
- File-based

### Auto-created:
```text
travel_planner.db
```

---

##  13. Demo Data Seeding

### On first run:
- Preloads trips:
```text
Bali, Indonesia
Santorini, Greece
Zanzibar, Tanzania
Dubai, UAE
Paris, France
Tokyo, Japan
Cape Town, South Africa
Maldives
Swiss Alps
New York
```

---

#  System Architecture

```text
Controller → Service → Repository → Database
         ↓
      Thymeleaf UI
```

---

# 📂 Project Structure

```text
ai-travel-planner/
│
├── controller/        → Handles HTTP requests
├── service/           → Business logic + AI integration
├── repository/        → Database queries
├── entity/            → Database models
├── dto/               → Data transfer objects
├── util/              → Helper classes
│
├── templates/         → HTML pages
├── static/css/        → Styling
├── application.properties
```

---

# ⚙️ Installation Guide

---

## 1️⃣ Install Requirements

```powershell
winget install EclipseAdoptium.Temurin.17.JDK
```

Verify:

```powershell
java -version
```

---

## 2️⃣ Open Project

```powershell
cd ai-travel-planner
code .
```

---

## 3️⃣ Add OpenAI Key

Open:

```text
src/main/resources/application.properties
```

Add:

```properties
openai.api.key=YOUR_API_KEY
```

---

## 4️⃣ Run Project

```powershell
.\gradlew.bat clean bootRun
```

---

## 5️⃣ Open in Browser

```text
http://localhost:8080
```

---

#  Demo Credentials

```text
User:
demo@travelplanner.app
password

Admin:
admin@travelplanner.app
password
```

---

#  Common Errors & Fixes

---

##  Java version error

Fix:

```powershell
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17..."
```

---

##  Trips not showing

Fix:

```powershell
del travel_planner.db
.\gradlew.bat bootRun
```

---

##  Generate button error

Make sure:

```java
@PostMapping("/{id}/generate")
```

---

#  Build Project

```powershell
.\gradlew.bat clean build
```

---

#  Run JAR

```powershell
java -jar build/libs/*.jar
```

---

#  Future Enhancements

```text
Google Maps integration
Flight API
Hotel API
Payment gateway
Trip sharing
PDF export
Email notifications
Weather API
AI chat assistant
```

---

#  Conclusion

This project demonstrates:

- Full-stack Java development
- AI integration
- Secure authentication
- Database design
- Modern UI/UX
- Real-world application architecture

It is suitable for:

```text
Final Year Project
Portfolio
Internship showcase
Production prototype
```

---

#  Author

```text
AI Travel Planner
Built with Spring Boot + OpenAI
```