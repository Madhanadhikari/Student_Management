# Student_Management
Student management application with JAVA SWING Mysql

## Features
- Role Based Login
- CRUD operation
## Teacher Login
-  Add,delete,update Attendance
-  Delete,Enter,Update Marks as per Subject

## Student Login
-  View Attendance
-  View Marks

## DB table
- User table
```
create table user(id int auto_increment,username varchar(50),password varchar(50),role varchar(50));
```
-  Attendance table
```
MariaDB [php]> desc attendance;
+------------------+--------------------------+------+-----+---------+----------------+
| Field            | Type                     | Null | Key | Default | Extra          |
+------------------+--------------------------+------+-----+---------+----------------+
| id               | int(11)                  | NO   | PRI | NULL    | auto_increment |
| student_username | varchar(100)             | YES  |     | NULL    |                |
| subject          | varchar(100)             | YES  |     | NULL    |                |
| date             | date                     | YES  |     | NULL    |                |
| status           | enum('Present','Absent') | YES  |     | NULL    |                |
| marked_by        | varchar(100)             | YES  |     | NULL    |                |
+------------------+--------------------------+------+-----+---------+----------------+
```
- Marks
```
MariaDB [php]> desc marks;
+------------------+--------------+------+-----+---------+----------------+
| Field            | Type         | Null | Key | Default | Extra          |
+------------------+--------------+------+-----+---------+----------------+
| id               | int(11)      | NO   | PRI | NULL    | auto_increment |
| student_username | varchar(100) | YES  |     | NULL    |                |
| subject          | varchar(100) | YES  |     | NULL    |                |
| marks            | int(11)      | YES  |     | NULL    |                |
| added_by         | varchar(100) | YES  |     | NULL    |                |
+------------------+--------------+------+-----+---------+----------------+
```
## Screenshots
![image](https://github.com/user-attachments/assets/6d44ac69-31d5-40a1-b689-3ba185e55265)
![image](https://github.com/user-attachments/assets/d763928f-6580-4c89-b00e-b87d9f5e53d9)
![image](https://github.com/user-attachments/assets/60f14759-5cab-4aa8-8623-ad186947ad73)
![image](https://github.com/user-attachments/assets/44c4ba2f-c8ec-410b-974d-9ccaf1704d6d)
![image](https://github.com/user-attachments/assets/dbc5d30c-6b16-46e4-bca4-c402f73c3cfa)


