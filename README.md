#  Student Enrollment App
This is an Android application that allows students to register, log in, select subjects, view their enrollment summary, and log out. The app uses SQLite for local database management and Java for implementation.

##  Features
-Student Registration: Students can register with their name, email, and password.
-Student Login: Registered students can log in using their email and password.
-Subject Selection: Students can select subjects from a predefined list with a credit limit (max 24 credits).
-Enrollment Summary: After subject selection, students can view a summary of their enrolled subjects and total credits.
-Logout: Students can log out of the app, which clears their session data and redirects them to the login screen.

##  Project Structure
- ###  Java Code:
  - MainActivity.java
  - DatabaseHelper.java
  - RegisterActivity.java
  - LoginActivity.java
  - EnrollmentActivity.java
  - SubjectSelectionActivity.java
  - EnrollmentSummaryActivity.java

- ###  Layout Files:
  - activity_main.xml
  - activity_register.xml
  - activity_login.xml
  - activity_enrollment.xml
  - activity_subject_selection.xml
  - activity_enrollment_summary.xml

- ###  Database:
  - Tables:
    1. `Students`: `id`, `name`, `email`, `password`
    2. `Subjects`: `id`, `name`, `credits`
    3. `Enrollments`: `id`, `student_id`, `subject_id`

##Database Scheme
### Entity-Relation-Diagram
![mermaid-diagram-2024-12-19-180156](https://github.com/user-attachments/assets/f3f66948-f8c4-4f1e-9156-669e89acc5a4)
