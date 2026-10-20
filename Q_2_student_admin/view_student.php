<?php
require "db.php";

$id = filter_input(INPUT_GET, "id", FILTER_VALIDATE_INT);
if (!$id) die("Invalid student ID.");

$stmt = $pdo->prepare("SELECT * FROM students WHERE id = ?");
$stmt->execute([$id]);
$s = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$s) die("Student not found.");

$stmt = $pdo->prepare(
    "SELECT * FROM student_files WHERE student_id = ? ORDER BY id DESC"
);
$stmt->execute([$id]);
$files = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<div class="panel">
    <h1>Student Details</h1>

    <p><strong>Name:</strong> <?= e($s['name']) ?></p>
    <p><strong>Roll Number:</strong> <?= e($s['roll_number']) ?></p>
    <p><strong>Email:</strong> <?= e($s['email']) ?></p>
    <p><strong>Course:</strong> <?= e($s['course']) ?></p>

    <a class="btn" href="edit_student.php?id=<?= e($id) ?>">Edit Details</a>
    <a class="btn btn-green" href="upload.php?id=<?= e($id) ?>">Upload File</a>
    <a class="btn btn-grey" href="index.php">Back</a>
</div>

<div class="panel">
    <h1>Uploaded Files</h1>

    <div class="table-wrap">
        <table>
            <tr>
                <th>Filename</th>
                <th>File Type</th>
                <th>Upload Date</th>
                <th>Action</th>
            </tr>

            <?php foreach ($files as $f): ?>
            <tr>
                <td><?= e($f['original_name']) ?></td>
                <td><?= e($f['file_type']) ?></td>
                <td><?= e($f['upload_date']) ?></td>
                <td>
                    <a href="<?= e($f['file_path']) ?>"
                       target="_blank" rel="noopener">Open</a>
                </td>
            </tr>
            <?php endforeach; ?>

            <?php if (!$files): ?>
            <tr><td colspan="4">No files uploaded.</td></tr>
            <?php endif; ?>
        </table>
    </div>
</div>

</main>
<footer>Student Administration System</footer>
</body>
</html>
