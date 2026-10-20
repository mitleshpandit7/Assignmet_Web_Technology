<?php
require "db.php";

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    die("Invalid request. Delete using the form on the student list.");
}

$id = filter_input(INPUT_POST, "id", FILTER_VALIDATE_INT);
if (!$id) die("Invalid student ID.");

$stmt = $pdo->prepare("DELETE FROM students WHERE id = ?");
$stmt->execute([$id]);

header("Location: index.php");
exit;
