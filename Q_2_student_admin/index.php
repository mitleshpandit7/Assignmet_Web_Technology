<?php
require_once "db.php";

$stmt = $pdo->query("SELECT * FROM students ORDER BY id DESC");
$students = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Management Dashboard</title>

    <!-- Font Awesome Icons -->
   <link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

    <link rel="stylesheet" href="style.css">
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            padding: 30px;
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            color: #1e293b;
        }

        .container {
            max-width: 1100px;
            margin: 20px auto;
            padding: 25px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
            margin-bottom: 25px;
        }

        h1 {
            margin: 0;
            font-size: 26px;
        }

        .add-btn {
            background: #2563eb;
            color: white;
            padding: 11px 16px;
            border-radius: 7px;
            text-decoration: none;
        }

        .add-btn:hover {
            background: #1d4ed8;
        }

        .table-wrapper {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 650px;
        }

        th, td {
            padding: 14px;
            text-align: left;
            border-bottom: 1px solid #e2e8f0;
        }

        th {
            background: #f8fafc;
            color: #475569;
        }

        tr:hover td {
            background: #f8fafc;
        }

        .actions {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .actions form {
            margin: 0;
        }

        .action-btn {
            display: inline-flex;
            justify-content: center;
            align-items: center;
            width: 36px;
            height: 36px;
            border: none;
            border-radius: 7px;
            text-decoration: none;
            cursor: pointer;
            font-size: 15px;
            transition: 0.2s;
        }

        .view-btn {
            background: #dbeafe;
            color: #2563eb;
        }

        .edit-btn {
            background: #dcfce7;
            color: #15803d;
        }

        .delete-btn {
            background: #fee2e2;
            color: #dc2626;
        }

        .action-btn:hover {
            opacity: 0.7;
            transform: translateY(-1px);
        }

        .empty {
            text-align: center;
            padding: 30px;
            color: #64748b;
        }

        @media (max-width: 600px) {
            body {
                padding: 12px;
            }

            .container {
                padding: 15px;
            }

            h1 {
                font-size: 21px;
            }
        }
    </style>
</head>

<body>
<div class="container">

    <div class="header">
        <h1>Student Management</h1>

        <a href="add_student.php" class="add-btn">
            <i class="fa-solid fa-plus"></i> Add Student
        </a>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Roll Number</th>
                    <th>Email</th>
                    <th>Course</th>
                    <th>Action </th>
                </tr>
            </thead>

            <tbody>
            <?php if (count($students) > 0): ?>

                <?php foreach ($students as $row): ?>
                    <tr>
                        <td><?= htmlspecialchars((string)$row['id']) ?></td>
                        <td><?= htmlspecialchars($row['name']) ?></td>
                        <td><?= htmlspecialchars($row['roll_number']) ?></td>
                        <td><?= htmlspecialchars($row['email'] ?? '') ?></td>
                        <td><?= htmlspecialchars($row['course']) ?></td>

                        <td>
                            <div class="actions">

                                <!-- View -->
                                <a href="view_student.php?id=<?= (int)$row['id'] ?>"
                                   class="action-btn view-btn"
                                   title="View Student"
                                   aria-label="View Student">
                                    <i class="fa-solid fa-eye"></i>
                                </a>

                                <!-- Edit -->
                                <a href="edit_student.php?id=<?= (int)$row['id'] ?>"
                                   class="action-btn edit-btn"
                                   title="Edit Student"
                                   aria-label="Edit Student">
                                    <i class="fa-solid fa-pen-to-square"></i>
                                </a>

                                <!-- Delete -->
                                <form action="delete_student.php"
                                      method="POST"
                                      onsubmit="return confirm('Are you sure you want to delete this student?');">

                                    <input type="hidden"
                                           name="id"
                                           value="<?= (int)$row['id'] ?>">

                                    <button type="submit"
                                            class="action-btn delete-btn"
                                            title="Delete Student"
                                            aria-label="Delete Student">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </form>

                            </div>
                        </td>
                    </tr>
                <?php endforeach; ?>

            <?php else: ?>
                <tr>
                    <td colspan="6" class="empty">
                        No students found. Click "Add Student" to add one.
                    </td>
                </tr>
            <?php endif; ?>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
