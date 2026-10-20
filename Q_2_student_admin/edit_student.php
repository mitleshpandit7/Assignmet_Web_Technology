<?php
require "db.php";

$id = filter_input(INPUT_GET, "id", FILTER_VALIDATE_INT);
if (!$id) die("Invalid student ID.");

$stmt = $pdo->prepare("SELECT * FROM students WHERE id = ?");
$stmt->execute([$id]);
$s = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$s) die("Student not found.");

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
                "UPDATE students
                 SET name=?, roll_number=?, email=?, course=?
                 WHERE id=?"
            );

            $stmt->execute([$name, $roll, $email, $course, $id]);

            header("Location: view_student.php?id=" . $id);
            exit;
        } catch (PDOException $e) {
            $error = $e->getCode() == "23000"
                ? "Roll number already exists."
                : "Unable to update student.";
        }
    }
}
?>

<div class="panel">
    <h1>Edit Student</h1>

    <?php if ($error): ?>
        <p class="error"><?= e($error) ?></p>
    <?php endif; ?>

    <form method="post">
        <label>Student Name *</label>
        <input name="name" required
               value="<?= e($_POST['name'] ?? $s['name']) ?>">

        <label>Roll Number *</label>
        <input name="roll_number" required
               value="<?= e($_POST['roll_number'] ?? $s['roll_number']) ?>">

        <label>Email</label>
        <input type="email" name="email"
               value="<?= e($_POST['email'] ?? $s['email']) ?>">

        <label>Course *</label>
        <input name="course" required
               value="<?= e($_POST['course'] ?? $s['course']) ?>">

        <button type="submit">Update Student</button>
        <a href="view_student.php?id=<?= e($id) ?>" class="btn btn-grey">Cancel</a>
    </form>
</div>

</main>
<footer>Student Administration System</footer>
</body>
</html>
