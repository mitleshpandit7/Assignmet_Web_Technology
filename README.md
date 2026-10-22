# 🎓 Student Registration and Document Management System

A web-based application for managing student registration, student records, profile images, and academic documents. It combines a validated registration form, a PHP/MySQL admin panel, and a Java Servlet/JSP student profile portal with session-protected file access.

![PHP](https://img.shields.io/badge/PHP-777BB4?logo=php&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?logo=css3&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/Tomcat-F8DC75?logo=apachetomcat&logoColor=black)

---

## 📑 Table of Contents

- [Objectives](#-objectives)
- [Technologies Used](#-technologies-used)
- [Features](#-features)
- [Project Structure](#-project-structure)
- [Database Design](#-database-design)
- [Installation and Setup](#-installation-and-setup)
- [Application Workflow](#-application-workflow)
- [Security and Validation](#-security-and-validation)
- [Testing](#-testing)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Objectives

- Create a responsive student registration form.
- Validate user input using JavaScript.
- Perform CRUD operations using PHP and MySQL.
- Upload and manage student profile images and documents.
- Store file metadata in the MySQL database.
- Retrieve student details using Java Servlets and JDBC.
- Display student profiles dynamically using JSP.
- Restrict access to authenticated users using session management.

## 🛠 Technologies Used

| Technology | Purpose |
|---|---|
| HTML5 | Registration forms and page structure |
| CSS3 | Styling and responsive design |
| JavaScript | Client-side form validation |
| PHP | Student record and file management |
| MySQL | Student records and file metadata |
| Java Servlets | Student data retrieval and file streaming |
| JSP | Dynamic student profile display |
| JDBC | Database connectivity |
| Apache Tomcat | Running the Java Servlet/JSP application |
| Apache HTTP Server | Running the PHP application (commonly via XAMPP) |

## ✨ Features

### Module 1: Student Registration
- Fields for name, roll number, email, course, and password.
- Profile or document file upload.
- Required-field, email format, and password strength validation.
- Responsive layout with styled inputs, buttons, and error messages.

### Module 2: Admin Panel (PHP)
- **Create** new student records.
- **Read** student information.
- **Update** existing student records.
- **Delete** student records.
- Upload profile images, ID proofs, and certificates.
- Validate file type and maximum file size.
- Generate unique filenames to prevent overwriting.
- Save file paths, types, upload dates, and student IDs in MySQL.
- Display uploaded documents on the student details page.
- Handle database errors and invalid uploads gracefully.

### Module 3: Student Profile Portal (Servlets + JSP)
- Connect to MySQL using JDBC.
- Retrieve student records and associated file metadata.
- Stream stored images and documents through a Servlet.
- Display images using HTML `<img>` tags.
- List multiple students using JSP loops.
- Use conditionals to show only available files.
- Protect pages and file access with login sessions.

## 📁 Project Structure

```text
student-management-system/
│
├── registration/
│   ├── index.html
│   ├── style.css
│   └── validation.js
│
├── php-admin/
│   ├── db.php
│   ├── login.php
│   ├── logout.php
│   ├── dashboard.php
│   ├── add_student.php
│   ├── edit_student.php
│   ├── delete_student.php
│   ├── view_student.php
│   └── upload.php
│
├── java-portal/
│   ├── src/main/java/
│   │   └── com/college/
│   │       ├── DBConnection.java
│   │       ├── StudentServlet.java
│   │       ├── FileServlet.java
│   │       └── LoginServlet.java
│   │
│   ├── src/main/webapp/
│   │   ├── login.jsp
│   │   ├── students.jsp
│   │   └── WEB-INF/
│   │       └── web.xml
│   │
│   └── pom.xml
│
├── uploads/
│   ├── profiles/
│   └── documents/
│
├── database/
│   └── schema.sql
│
└── README.md
```

> **Note:** This is a suggested structure. The PHP and Java applications may be deployed separately. Keep uploaded files outside publicly accessible directories where possible.

## 🗄 Database Design

Create a MySQL database named `student_db`.

### `students` table

```sql
CREATE DATABASE student_db;
USE student_db;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    course VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### `student_files` table

```sql
CREATE TABLE student_files (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL UNIQUE,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON DELETE CASCADE
);
```

`students` stores student information, while `student_files` stores metadata for each uploaded image or document. One student can have multiple files.

## 🚀 Installation and Setup

### Prerequisites

- [XAMPP](https://www.apachefriends.org/) (Apache, PHP, MySQL)
- JDK compatible with your Servlet/JSP project
- Apache Tomcat compatible with your Java version
- Maven (if the Java project uses it)
- A code editor such as VS Code or IntelliJ IDEA

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/student-management-system.git
cd student-management-system
```

### 2. Set up the database

1. Start **Apache** and **MySQL** in XAMPP.
2. Open `http://localhost/phpmyadmin`.
3. Create the `student_db` database, or import `database/schema.sql`.
4. Confirm both tables are created.

### 3. Configure the PHP application

1. Copy the `registration` and `php-admin` folders into XAMPP's `htdocs` directory (or configure a virtual host).
2. Update `db.php` with your local database credentials.
3. Configure the upload directory and permitted file types.
4. Open the pages in your browser:

```text
http://localhost/registration/index.html
http://localhost/php-admin/login.php
```

### 4. Configure the Java application

1. Add the MySQL JDBC driver to your project.
2. Update `DBConnection.java` with the database URL and credentials.
3. Configure the upload directory used by `FileServlet`.
4. Build the project:

```bash
mvn clean package
```

5. Deploy the generated WAR file to Apache Tomcat.
6. Start Tomcat and open the app:

```text
http://localhost:8080/java-portal/
```

> Actual URLs depend on your project and deployment configuration.

## 🔄 Application Workflow

1. A student enters their details in the registration form.
2. JavaScript validates required fields, email format, and password strength.
3. The server validates the submitted information again.
4. PHP stores student information in MySQL and processes uploaded files.
5. File metadata is linked to the student through `student_id`.
6. An authenticated administrator views, edits, or deletes student records.
7. The Java Servlet retrieves student information using JDBC.
8. JSP displays student profiles and their available files.
9. `FileServlet` streams requested images or documents after checking authorization.

## 🔒 Security and Validation

- Validate all user input on both the client and server.
- Use prepared statements for SQL queries.
- Hash passwords with PHP `password_hash()` or an appropriate Java hashing library.
- Validate uploaded file extensions, MIME types, and actual file contents.
- Enforce maximum upload size limits.
- Generate unique server-side filenames to prevent overwriting.
- Store uploads outside the public web root where possible.
- Prevent executable files from being uploaded.
- Use session authentication and authorization checks.
- Protect state-changing requests against CSRF attacks.
- Escape output to help prevent cross-site scripting (XSS).
- Restrict document downloads to authorized users.
- Never commit passwords, database credentials, or private student documents to version control.

> ⚠️ **Important:** JavaScript validation alone is not sufficient. PHP and Java endpoints must enforce their own validation and access-control rules.

## 🧪 Testing

| Test Case | Expected Result |
|---|---|
| Submit empty registration form | Required-field errors appear |
| Enter invalid email | Email validation fails |
| Enter a weak password | Password strength error appears |
| Add a student with valid details | Student record is saved |
| Add a duplicate roll number | Duplicate record is rejected |
| Upload an unsupported file | Upload is rejected |
| Upload an oversized file | Upload is rejected |
| Edit student information | Updated information is displayed |
| Delete a student | Record is removed according to the configured policy |
| View student profile | Student details and permitted files appear |
| Request a protected file without login | Access is denied |
| Upload two files with the same original filename | Both files are stored without overwriting |

## 🔮 Future Enhancements

- [ ] Search and filter student records
- [ ] Pagination for large student lists
- [ ] Email verification and password reset
- [ ] Role-based access for administrators, faculty, and students
- [ ] Audit logs for file uploads and record changes
- [ ] Cloud object storage for large document collections
- [ ] Automated database backups
- [ ] Docker deployment and cloud hosting

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request.

## 📄 License

This project is licensed under the [MIT License](LICENSE). *(Add a `LICENSE` file, or replace this section with your preferred license.)*

## 📌 Conclusion

The Student Registration and Document Management System combines front-end form validation, PHP-based CRUD operations, MySQL data storage, secure file handling, and Java Servlet/JSP integration. It provides a foundation for managing student records and academic documents efficiently while emphasizing security, maintainability, and responsive design.
