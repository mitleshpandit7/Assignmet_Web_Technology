<?php
require "db.php";
$error = "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $name = trim($_POST["name"] ?? "");
    $roll = trim($_POST["roll_number"] ?? "");
    $email = trim($_POST["email"] ?? "");
    $course = trim($_POST["course"] ?? "");

    if ($name === "" || $roll === "" || $course === "") {
        $error = "Name, roll number and course are required.";
    } elseif ($email !== "" && !filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $error = "Enter a valid email address.";
    } else {
        try {
            $stmt = $pdo->prepare(
                "INSERT INTO students (name, roll_number, email, course)
                 VALUES (?, ?, ?, ?)"
            );

            $stmt->execute([$name, $roll, $email, $course]);

            header("Location: view_student.php?id=" . $pdo->lastInsertId());
            exit;
        } catch (PDOException $e) {
            $error = $e->getCode() == "23000"
                ? "This roll number already exists."
                : "Unable to save student.";
        }
    }
}
?>

<div class="panel">
    <h1>Add Student</h1>

    <?php if ($error): ?>
        <p class="error"><?= e($error) ?></p>
    <?php endif; ?>

    <form method="post">
        <label>Student Name *</label>
        <input name="name" required value="<?= e($_POST['name'] ?? '') ?>">

        <label>Roll Number *</label>
        <input name="roll_number" required
               value="<?= e($_POST['roll_number'] ?? '') ?>">

        <label>Email</label>
        <input type="email" name="email"
               value="<?= e($_POST['email'] ?? '') ?>">

        <label>Course *</label>
        <input name="course" required
               value="<?= e($_POST['course'] ?? '') ?>">

        <button type="submit">Save Student</button>
        <a href="index.php" class="btn btn-grey">Cancel</a>
    </form>
</div>

</main>
<footer>Student Administration System</footer>
</body>
</html>
