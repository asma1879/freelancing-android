# Freelancing Management System (Android + SQLite)

A full-featured mobile freelancing management platform built with **Android** for the frontend and **SQLite** for local data storage.

## FEATURES

- User registration & login (Client / Freelancer / Admin)
- Profile management (bio, skills, profile image)
- Post & manage jobs
- Browse available jobs
- Place and manage bids
- Job deliveries with submission links
- In-app payments using wallet balance
- Withdrawals with payment method and code
- Wishlist for favorite jobs
- Dashboard with analytics
- Review and rating system

## TECH STACK

- Android: Java/Kotlin, Material Design
- Database: SQLite
- IDE: Android Studio
- Build Tools: Gradle

## PROJECT STRUCTURE

Freelance/
├── .gradle/                  # Gradle system files
├── .idea/                    # IDE configuration files (Android Studio)
├── app/                      # Main Android application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/freelance/
│   │   │   │   ├── activities/      # Activity classes
│   │   │   │   ├── adapters/        # RecyclerView / List adapters
│   │   │   │   ├── database/        # SQLite helper classes (FreelanceDbHelper)
│   │   │   │   └── models/          # Data model classes
│   │   │   ├── res/                  # Layouts, drawables, values
│   │   │   └── AndroidManifest.xml   # App manifest
│   │   └── test/                     # Unit tests
│   └── build.gradle.kts               # Module Gradle build file
├── build/                             # Build output
├── gradle/                             # Gradle wrapper configuration
├── gradlew                             # Gradle wrapper script (Unix)
├── gradlew.bat                         # Gradle wrapper script (Windows)
├── local.properties                    # Local properties (SDK path)
├── settings.gradle.kts                 # Project settings
├── .gitignore                          # Git ignore rules
└── README.md                           # Project documentation

## DATABASE STRUCTURE (SQLite)

Tables and columns:

1. **users**
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - name TEXT
   - email TEXT UNIQUE
   - password TEXT
   - role TEXT
   - bio TEXT
   - skills TEXT
   - profile_image TEXT
   - wallet_balance REAL DEFAULT 0.0

2. **jobs**
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - client_id INTEGER
   - title TEXT
   - description TEXT
   - budget REAL
   - deadline TEXT
   - skills TEXT

3. **bids**
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - job_id INTEGER
   - freelancer_id INTEGER
   - amount REAL
   - message TEXT
   - status TEXT DEFAULT 'Pending'

4. **job_deliveries**
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - job_id INTEGER
   - freelancer_id INTEGER
   - client_id INTEGER
   - delivery_message TEXT
   - submission_link TEXT
   - timestamp TEXT

5. **payments**
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - job_id INTEGER
   - freelancer_id INTEGER
   - client_id INTEGER
   - amount REAL
   - payment_date TEXT
   - status TEXT DEFAULT 'Pending'

6. **withdrawals**
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - freelancer_id INTEGER
   - amount REAL
   - withdraw_date TEXT
   - payment_method TEXT
   - payment_code TEXT

7. **wishlist**
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - freelancer_id INTEGER
   - job_id INTEGER
   - UNIQUE(freelancer_id, job_id)

---

## HOW TO RUN

### 1. Prerequisites
- Android Studio installed
- Minimum SDK version: 21
- Gradle 7+
- Emulator or Android device

### 2. Open Project
1. Launch Android Studio.
2. Click **Open an existing project**.
3. Select the `FreelanceManagementSystem` folder.

### 3. Build & Run
1. Let Gradle sync and build the project.
2. Run on an emulator or physical device.
3. The app uses **SQLite** as local storage automatically.

### 4. Usage
- Register a new user (client/freelancer/admin).  
- Clients can post jobs.  
- Freelancers can browse jobs, place bids, and manage their wishlist.  
- Clients can approve bids, view job deliveries, and process payments.  
- Freelancers can withdraw funds with payment method and code.  
- Users can leave reviews and rate completed jobs.  


