<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
$host = "localhost";
$dbname = "student_admin";
$username = "root";
$password = "";

try {
    $pdo = new PDO(
        "mysql:host=$host;dbname=$dbname;charset=utf8mb4",
        $username,
        $password
    );
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Database connection failed. Check your database settings.");
}

function e($value) {
    return htmlspecialchars((string)$value, ENT_QUOTES, "UTF-8");
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Student Administration</title>
<style>
* { box-sizing: border-box; }

body {
    margin: 0;
    font-family: Arial, sans-serif;
    background: #f1f4f9;
    color: #202b40;
}

header {
    background: #172642;
    color: white;
    padding: 22px 6%;
}

header h2 { margin: 0; }

nav { margin-top: 15px; }

nav a {
    color: #e5edff;
    text-decoration: none;
    margin-right: 22px;
    font-size: 14px;
}

nav a:hover { color: #70dfc2; }

main {
    max-width: 1100px;
    margin: 35px auto;
    padding: 0 20px;
}

.panel {
    background: white;
    padding: 28px;
    border-radius: 12px;
    box-shadow: 0 5px 22px rgba(20,35,60,.07);
    margin-bottom: 25px;
}

h1 { margin-top: 0; font-size: 27px; }
p { line-height: 1.6; }

label {
    display: block;
    margin-top: 17px;
    font-weight: bold;
    font-size: 14px;
}

input {
    width: 100%;
    padding: 12px;
    margin-top: 7px;
    border: 1px solid #d5dce7;
    border-radius: 7px;
    font-size: 14px;
}

button, .btn {
    display: inline-block;
    background: #2563eb;
    color: white;
    border: 0;
    border-radius: 7px;
    padding: 11px 16px;
    margin-top: 18px;
    text-decoration: none;
    cursor: pointer;
    font-size: 14px;
}

.btn-green { background: #07866e; }
.btn-red { background: #d9364f; }
.btn-grey { background: #526078; }

.table-wrap { overflow-x: auto; }

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 18px;
}

th, td {
    text-align: left;
    padding: 13px 10px;
    border-bottom: 1px solid #e7ebf2;
}

th { background: #f5f7fb; font-size: 13px; }
td { font-size: 14px; }

.message {
    padding: 12px;
    background: #e4f8ef;
    color: #126543;
    border-radius: 7px;
}

.error {
    padding: 12px;
    background: #fff0f0;
    color: #b42335;
    border-radius: 7px;
}

footer {
    text-align: center;
    padding: 20px;
    color: #7a8497;
    font-size: 13px;
}

@media(max-width:600px) {
    .panel { padding: 18px; }
    header { padding: 20px; }
    nav a { display: inline-block; margin-bottom: 10px; }
}
</style>
</head>
<body>
<header>
    <h2>Student Administration</h2>
    <nav>
        <a href="index.php">Dashboard</a>
        <a href="add_student.php">Add Student</a>
    </nav>
</header>
<main>
