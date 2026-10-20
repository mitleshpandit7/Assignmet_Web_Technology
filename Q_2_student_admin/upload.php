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
    if (!isset($_FILES["student_file"]) ||
        $_FILES["student_file"]["error"] !== UPLOAD_ERR_OK) {
        $error = "Select a file to upload.";
    } elseif ($_FILES["student_file"]["size"] > 2 * 1024 * 1024) {
        $error = "File size must not exceed 2 MB.";
    } else {
        $tmp = $_FILES["student_file"]["tmp_name"];
        $original = basename($_FILES["student_file"]["name"]);

        $finfo = new finfo(FILEINFO_MIME_TYPE);
        $mime = $finfo->file($tmp);

        $allowed = [
            "image/jpeg" => "jpg",
            "image/png" => "png",
            "application/pdf" => "pdf"
        ];

        if (!isset($allowed[$mime])) {
            $error = "Only JPG, PNG and PDF files are allowed.";
        } else {
            $folder = __DIR__ . "/uploads/";

            if (!is_dir($folder)) {
                mkdir($folder, 0755, true);
            }

            $stored = bin2hex(random_bytes(16)) . "." . $allowed[$mime];
            $fullPath = $folder . $stored;
            $relativePath = "uploads/" . $stored;

            if (move_uploaded_file($tmp, $fullPath)) {
                try {
                    $stmt = $pdo->prepare(
                        "INSERT INTO student_files
                         (student_id, original_name, stored_name, file_path, file_type)
                         VALUES (?, ?, ?, ?, ?)"
                    );

                    $stmt->execute([
                        $id, $original, $stored, $relativePath, $mime
                    ]);

                    header("Location: view_student.php?id=" . $id);
                    exit;
                } catch (PDOException $e) {
                    if (is_file($fullPath)) unlink($fullPath);
                    $error = "Unable to save file information.";
                }
            } else {
                $error = "Upload failed. Check the uploads folder.";
            }
        }
    }
}
?>

<div class="panel">
    <h1>Upload Student File</h1>
    <p>Student: <strong><?= e($s['name']) ?></strong></p>
    <p>Accepted formats: JPG, PNG, PDF. Maximum size: 2 MB.</p>

    <?php if ($error): ?>
        <p class="error"><?= e($error) ?></p>
    <?php endif; ?>

    <form method="post" enctype="multipart/form-data">
        <label>Select File</label>
        <input type="file" name="student_file"
               accept=".jpg,.jpeg,.png,.pdf" required>

        <button type="submit">Upload File</button>
        <a href="view_student.php?id=<?= e($id) ?>"
           class="btn btn-grey">Cancel</a>
    </form>
</div>

</main>
<footer>Student Administration System</footer>
</body>
</html>
