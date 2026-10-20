# Student Profile Portal (Java Servlets + JSP + MySQL)

A login-protected web application where signed-in users can browse student profiles and view or
download each student's uploaded image and document.

**Stack:** Java 11 · Servlet 4.0 / JSP 2.3 (`javax.servlet`) · JSTL 1.2 · JDBC + MySQL · Maven (WAR) · Apache Tomcat 9
No Spring, no frameworks.

---

## 1. What is in this project

```
student-profile-portal/
├── pom.xml                         Maven build (WAR, Java 11, dependencies)
├── README.md                       this file
├── sql/
│   ├── schema.sql                  creates DB `student_portal`, tables `users` and `students`
│   └── sample_data.sql             sample users + 8 sample students
├── sample-uploads/                 sample images/documents that match the sample data
├── config-examples/
│   └── db.properties.example       copy of the settings file, for reference
└── src/main/
    ├── resources/db.properties     <<< YOUR SETTINGS (DB login + upload folder)
    ├── java/com/studentportal/
    │   ├── servlet/  LoginServlet, LogoutServlet, StudentListServlet, FileServlet
    │   ├── filter/   AuthFilter (login check), SecurityHeadersFilter
    │   ├── dao/      StudentDAO, UserDAO            (all SQL, prepared statements)
    │   ├── model/    Student, User
    │   └── util/     AppConfig, DBUtil, PasswordUtil, SafePath, SessionKeys
    └── webapp/
        ├── index.jsp                       redirects to /students
        ├── css/style.css                   all styling
        ├── images/no-image.svg             placeholder for missing images
        └── WEB-INF/
            ├── web.xml                     sessions, welcome file, error pages
            └── views/ login.jsp, students.jsp, error.jsp
```

### URLs

| URL                                   | Purpose                                   | Login needed |
|---------------------------------------|-------------------------------------------|--------------|
| `/login`                              | login form / login check                  | no           |
| `/students` (optional `?q=text`)      | student list (JSP, loops + conditionals)  | **yes**      |
| `/file?id=3&type=image`               | streams student 3's image                 | **yes**      |
| `/file?id=3&type=document`            | streams the document (view in browser)    | **yes**      |
| `/file?id=3&type=document&download=1` | same, forces "Save as…"                   | **yes**      |
| `/logout` (POST only)                 | invalidates the session                   | –            |

---

## 2. Install the prerequisites

### 2.1 Java 11 (JDK, not just JRE)
1. Install a JDK 11 (Eclipse Temurin / Adoptium, Oracle, Amazon Corretto …). JDK 17 also runs it, but the code is Java 11 compatible.
2. Check: open a terminal and run `java -version` and `javac -version` – both should print 11.x.

### 2.2 MySQL 8
1. Install MySQL Community Server 8.x (and optionally MySQL Workbench).
2. Remember the password you set for the `root` user.
3. Check: `mysql --version`.

### 2.3 Apache Tomcat 9
1. Download **Tomcat 9.0.x** ("Core" zip/tar.gz) from tomcat.apache.org and unzip it somewhere, e.g. `C:\apache-tomcat-9.0.xx`.
2. **Use Tomcat 9, not 10/11.** Tomcat 10+ uses `jakarta.servlet` and will NOT run this project.

### 2.4 Eclipse
Use **Eclipse IDE for Enterprise Java and Web Developers** (it includes Maven/m2e and the server tools).

---

## 3. Create the database

Run the two scripts **in this order**.

**Option A – command line (Command Prompt / Linux / macOS terminal):**
```
cd student-profile-portal
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/sample_data.sql
```
(In Windows PowerShell the `<` operator does not work; use Option B or run `mysql -u root -p` and then
`SOURCE C:/full/path/to/sql/schema.sql;` and `SOURCE C:/full/path/to/sql/sample_data.sql;`.)

**Option B – MySQL Workbench:** File → Open SQL Script → `sql/schema.sql` → run (lightning icon); repeat with `sql/sample_data.sql`.

Verify:
```sql
USE student_portal;
SELECT COUNT(*) FROM students;   -- 8
SELECT username FROM users;      -- admin, teacher
```

Both scripts are safe to re-run (nothing is dropped; `INSERT IGNORE` prevents duplicates).

**Sample logins** (change them for real use):

| Username  | Password       |
|-----------|----------------|
| `admin`   | `Admin@123`    |
| `teacher` | `Teacher@123`  |

---

## 4. Configure the application  (`src/main/resources/db.properties`)

```properties
db.url=jdbc:mysql://localhost:3306/student_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.username=root
db.password=your_mysql_password
upload.dir=C:/student_portal_uploads
session.timeout.minutes=30
```

1. Set `db.password` (and `db.username` if you do not use `root`).
2. Create the upload folder and put the files in it:
   * Create a folder, e.g. `C:/student_portal_uploads` (Windows) or `/home/you/student_portal_uploads` (Linux/macOS).
   * Copy **everything inside `sample-uploads/`** into it.
   * Set `upload.dir` to that folder. **Always use forward slashes `/`, even on Windows.**
3. Save the file. After any change to `db.properties`, redeploy/restart the app (Eclipse: Servers view → right-click server → *Clean…* or just restart).

> Optional: keep the settings outside the project by starting Tomcat with
> `-Dportal.config=C:/secure/portal.properties` (or the environment variable `PORTAL_CONFIG`).

---

## 5. Run in Eclipse

1. **Configure Tomcat in Eclipse:** Window → Preferences → Server → Runtime Environments → Add… → *Apache Tomcat v9.0* → point to your Tomcat folder and select the JDK 11 as the JRE → Finish.
2. **Import:** File → Import… → Maven → *Existing Maven Projects* → select the `student-profile-portal` folder → Finish. Wait for Maven to download dependencies (needs internet the first time).
3. If you see red errors: right-click project → Maven → *Update Project…* → tick *Force Update* → OK.
4. Check the Java level: Project → Properties → Java Compiler → 11.
5. **Run:** right-click the project → Run As → *Run on Server* → choose Tomcat v9.0 → Finish.
6. Eclipse opens the app in a browser; normally the address is
   `http://localhost:8080/student-profile-portal/` (the exact URL is shown in Eclipse's browser tab).
7. Log in with `admin` / `Admin@123`.

### Alternative: build the WAR from the command line
```
mvn clean package
```
Copy `target/student-profile-portal.war` into Tomcat's `webapps/` folder, start Tomcat (`bin/startup.bat` or `bin/startup.sh`), and open `http://localhost:8080/student-profile-portal/`.

---

## 6. What you should see

* `/` → redirected to the login page.
* After login: a grid of 8 student cards (search box on top).
* Alice–Emily: photo + working **View** / **Download** buttons for the PDF.
* Frank Miller: files are listed in the DB but not on disk → placeholder image; the document link shows a friendly "not found" page (demonstrates missing-file handling).
* Grace Lee: no image/document at all → initials avatar and "No document uploaded".
* Henry Adams: no image, plain-text document that opens in the browser.
* **Log out** returns to the login page; pressing Back or opening `/students` or `/file?...` again requires login.

---

## 7. Which files to edit to customise the project

| I want to…                                   | Edit this                                                                      |
|-----------------------------------------------|--------------------------------------------------------------------------------|
| Change the DB host/user/password              | `src/main/resources/db.properties`                                             |
| Change where files are stored                 | `upload.dir` in `db.properties`                                                |
| Change the session timeout                    | `session.timeout.minutes` in `db.properties` **and** `<session-timeout>` in `WEB-INF/web.xml` |
| Change colours, fonts, layout                 | `src/main/webapp/css/style.css`                                                |
| Change the student page layout / text         | `src/main/webapp/WEB-INF/views/students.jsp`                                   |
| Change the login page                         | `src/main/webapp/WEB-INF/views/login.jsp`                                      |
| Change the error page text                    | `src/main/webapp/WEB-INF/views/error.jsp`                                      |
| Change the "no image" picture                 | `src/main/webapp/images/no-image.svg`                                          |
| Add/rename a student column                   | SQL table, `model/Student.java`, `dao/StudentDAO.java`, `students.jsp`         |
| Allow more file types (e.g. `.xlsx`)          | the `CONTENT_TYPES` map at the top of `servlet/FileServlet.java`               |
| Add more protected URLs                       | `urlPatterns` in `filter/AuthFilter.java`                                      |
| Change the Java/Tomcat/library versions       | `pom.xml`                                                                      |

### Add a student
```sql
USE student_portal;
INSERT INTO students (name, email, department, image_path, document_path)
VALUES ('New Student', 'new.student@example.edu', 'Chemistry', 'new_student.jpg', 'new_student.pdf');
```
Then copy `new_student.jpg` and `new_student.pdf` into the upload folder. Paths are relative to `upload.dir`
(sub-folders are allowed, e.g. `2026/new_student.jpg`). Reload the page – no restart needed.

### Add a login user
Passwords are never stored in plain text. Generate a hash:
```
mvn compile
java -cp target/classes com.studentportal.util.PasswordUtil "MyNewPassword"
```
Copy the printed `pbkdf2$…` line into:
```sql
INSERT INTO users (username, password_hash, full_name)
VALUES ('newuser', 'PASTE_THE_HASH_HERE', 'New User');
```
(In Eclipse you can instead right-click `PasswordUtil.java` → Run As → Java Application after adding the argument in Run Configurations.)

---

## 8. How the requirements are met

| Requirement                                   | Where                                                                       |
|-----------------------------------------------|-----------------------------------------------------------------------------|
| JDBC → MySQL, fetch details + file paths      | `util/DBUtil`, `dao/StudentDAO`                                             |
| Retrieve & stream images/documents            | `servlet/FileServlet` (reads from `upload.dir`, sets Content-Type/Length)   |
| Dynamic HTML/JSP with `<img>` → Servlet       | `students.jsp` → `<img src=".../file?id=…&type=image">`                     |
| Looping + conditional logic                   | `students.jsp`: `c:forEach`, `c:choose/when/otherwise`, `c:if`              |
| Session handling, only logged-in access       | `LoginServlet`, `LogoutServlet`, `filter/AuthFilter`                        |

### Security measures
* **Prepared statements** everywhere (no SQL string concatenation with user input; LIKE wildcards in the search box are escaped).
* **Hashed passwords** (PBKDF2-HMAC-SHA256, per-user salt, constant-time comparison; equal work for unknown usernames).
* **Session fixation protection:** old session invalidated and a new one created at login; HttpOnly cookie; cookie-only tracking.
* **Logout is POST + CSRF token**, then `session.invalidate()`.
* **File access:** the browser only sends a student *id*. The path comes from the database and is validated by `SafePath`
  (must stay inside `upload.dir` after resolving `..`, absolute paths and symlinks; must be an existing regular file).
  Only whitelisted extensions are served (SVG/HTML are deliberately not), with `X-Content-Type-Options: nosniff`.
* **Output escaping:** all database values are printed with `<c:out>` (prevents XSS).
* JSPs live in `WEB-INF/views`, so they cannot be requested directly.
* Error pages never show stack traces.

Not included (out of scope for this assignment): HTTPS setup, login-attempt rate limiting, a connection pool,
and user/student management screens.

---

## 9. Testing status – please read

I built this in an environment **without Maven, Tomcat, MySQL or internet access**, so it could not be run end-to-end.

**Verified:**
* All Java sources compile at Java 11 language level with zero warnings. They were compiled against *hand-written stubs*
  of the Servlet API signatures (the real `javax.servlet-api` jar was not available), so a real Maven compile is still the final check.
* Unit-style checks passed for: path-traversal protection (`../`, absolute paths, backslashes, NUL byte, directories, symlink escape),
  password hashing/verification, loading of config values, and that the password hashes in `sample_data.sql` match the documented sample passwords.
* `pom.xml`, `web.xml`, SVG are well-formed XML. JSTL tags in all JSPs are balanced; every EL property used in `students.jsp`
  exists on `Student`; every URL/JSP path referenced matches a servlet mapping or an existing file.
* Every file named in `sample_data.sql` exists in `sample-uploads/`, except the two that are *intentionally* missing (Frank Miller demo).

**NOT tested (please check on your machine):**
* `mvn clean package` / dependency download.
* Deployment on Tomcat 9 and Eclipse import / "Run on Server".
* Rendering of the JSP pages (JSP compilation, EL, JSTL) and how the CSS looks in a browser.
* Executing the SQL scripts on MySQL, and the JDBC queries against a real database.
* The full login → list → image/document → logout flow, including session expiry.

If something fails, the Tomcat log (`logs/catalina.*.log` or Eclipse's Console view) will show the exact cause.

---

## 10. Troubleshooting

| Symptom                                                  | Likely cause / fix                                                                 |
|----------------------------------------------------------|-------------------------------------------------------------------------------------|
| "Something went wrong" page right after login            | DB not running or wrong `db.url`/user/password. See the Tomcat log.                |
| Log shows `Access denied for user`                       | Wrong `db.username` / `db.password`.                                               |
| Log shows `Unknown database 'student_portal'`            | Run `sql/schema.sql` first.                                                         |
| Log shows `Public Key Retrieval is not allowed`          | Keep `allowPublicKeyRetrieval=true` in `db.url`.                                    |
| Every student shows the placeholder image                | `upload.dir` wrong, or files not copied into it. Use `/` slashes.                   |
| Login always says "Invalid username or password"         | `sample_data.sql` not run, or typo. Sample: `admin` / `Admin@123`.                  |
| `ClassNotFoundException: javax.servlet...` or 404 on all pages with Tomcat 10 | You must use **Tomcat 9**.                         |
| HTTP 500 mentioning JSTL / `c:` taglib                   | Maven did not bundle `jstl-1.2.jar`; run Maven → Update Project, redeploy.         |
| Red X on the project in Eclipse                          | Maven → Update Project (Force Update); make sure the project uses JDK 11.           |
