# ExamTPAtlas - Library Management System

`ExamTPAtlas` is a Java-based web application built with Servlets and EclipseLink (JPA) to manage a library system. It supports registering users, adding documents (books and magazines), borrowing documents, and returning them. The application uses a MySQL database for persistence and returns JSON responses via Gson.

## Features
- **User Management**: Add and list users.
- **Document Management**: Add and list books and magazines.
- **Borrowing System**: Borrow and return documents, tracking dates.
- **REST-like API**: Uses HTTP methods (GET, POST, PUT) with JSON responses.

## Project Structure
```
ExamTPAtlas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/adamo/tpexamatlas/
│   │   │       ├── model/         # JPA entity classes (User, Document, etc.)
│   │   │       ├── repository/    # Repositories for data access
│   │   │       ├── servlet/       # Servlets for HTTP endpoints
│   │   │       └── util/          # Utility classes (e.g., JsonUtil)
│   │   └── resources/
│   │       └── META-INF/
│   │           └── persistence.xml  # JPA configuration
│   └── test/
├── scripts/                       # Bash scripts for testing API endpoints
│   ├── add_user.sh
│   ├── add_book.sh
│   ├── add_magazine.sh
│   └── borrow_document.sh
├── pom.xml                        # Maven configuration
└── README.md                      # This file
└── .gitignore                  # Default files to ignore

```

## Prerequisites
- **Java**: JDK 17 or higher
- **Maven**: For building the project
- **Tomcat**: Version 10.x for deployment
- **MySQL**: Local server or access to the provided Aiven database
- **curl**: For running bash scripts (optional)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/Adamo08/ExamTPAtlas.git
cd ExamTPAtlas
```

### 2. Database Configuration
The application supports two database options via `persistence.xml`:
- **Aiven MySQL (Cloud)**: Use the provided Aiven database.
- **Local MySQL**: Set up your own local database.

#### Option 1: Use Aiven MySQL
- The Aiven database is preconfigured in the `jpa-atlas-tp-aiven` persistence unit:
    - URL: `jdbc:mysql://mysql-2427ad87-crudstudent.e.aivencloud.com:16378/defaultdb`
    - User: `avnadmin`
    - Password: `AVNS_PSOOt7s5eHuDEhHL_z5`
- Ensure the following in your servlets and `InitialDataLoader`:
  ```java
  emf = Persistence.createEntityManagerFactory("jpa-atlas-tp-aiven");
  ```
- No additional setup is needed if using this option.

#### Option 2: Use Local MySQL
1. Create a local MySQL database:
   ```bash
   mysql -u root -p
   CREATE DATABASE tp_atlas_exam;
   ```
   Or create it via a GUI tool like phpMyAdmin.


2. Update the `jpa-atlas-tp` persistence unit in `persistence.xml` with your local credentials:
   ```xml
   <property name="eclipselink.jdbc.url" value="jdbc:mysql://localhost:3306/tp_atlas_exam"/>
   <property name="eclipselink.jdbc.user" value="your_username"/>
   <property name="eclipselink.jdbc.password" value="your_password"/>
   ```
3. Change the persistence unit name in all servlets (`UserServlet`, `DocumentServlet`, `BorrowingServlet`) and `InitialDataLoader` (if present):
   ```java
   emf = Persistence.createEntityManagerFactory("jpa-atlas-tp");
   ```


### 3. Build the Project
Use Maven to clean and package the project, generating a WAR file in the `target/` directory.

### 4. Deploy to Tomcat
Move the generated WAR file to Tomcat’s `webapps/` directory, then start Tomcat to deploy the application.

### 5. Verify Deployment
Check the Tomcat logs to ensure the deployment was successful, then access the application in your browser or test the API.


## API Endpoints
- **Users**:
    - `GET /users/` - List all users
    - `GET /users/{id}` - Get user by ID
    - `POST /users/` - Add a user (`name`, `mail`)
- **Documents**:
    - `GET /documents/` - List all documents
    - `GET /documents/{id}` - Get document by ID
    - `POST /documents/` - Add a book (`type=book`, `title`, `author`, `isbn`, `datePubl`) or magazine (`type=magazine`, `title`, `publisher`, `issueNumber`, `dateIssue`)
- **Borrowings**:
    - `GET /borrowings/` - List active borrowings
    - `POST /borrowings/` - Borrow a document (`userId`, `documentId`)
    - `PUT /borrowings/` - Return a document (`borrowingId`)

## Bash Scripts
The `scripts/` directory contains bash scripts to send POST requests to the API. They are useful for testing and automation.

### Prerequisites
- Install `curl`: `sudo apt install curl` (on Ubuntu/Debian)
- Make scripts executable:
  ```bash
  chmod +x scripts/*.sh
  ```

### Running the Scripts
Run from the project root. Ensure Tomcat is running and the database is set up.

- **Add a User**:
  ```bash
  ./scripts/add_user.sh "Alice Johnson" "alice.johnson@example.com"
  ```
    - Output: `HTTP Status: 200` with user JSON.

- **Add a Book**:
  ```bash
  ./scripts/add_book.sh "To Kill a Mockingbird" "Harper Lee" "978-0446310789" "1960-07-11"
  ```
    - Output: `HTTP Status: 200` with book JSON.

- **Add a Magazine**:
  ```bash
  ./scripts/add_magazine.sh "Scientific American" "Springer Nature" "Vol 328 No 4" "2025-04-01"
  ```
    - Output: `HTTP Status: 200` with magazine JSON.

- **Borrow a Document**:
  ```bash
  ./scripts/borrow_document.sh 1 1
  ```
    - Output: `HTTP Status: 200` with borrowing JSON (requires existing user and document).

### Notes
- **BASE_URL**: Scripts use `http://localhost:9090/ExamTPAtlas_war_exploded`. Update in each script if your port or context differs.
- **Debugging**: Scripts print HTTP status and response. Check `catalina.out` if errors occur.
- **Order**: Run `add_user.sh` and `add_book.sh`/`add_magazine.sh` before `borrow_document.sh` to ensure IDs exist.
