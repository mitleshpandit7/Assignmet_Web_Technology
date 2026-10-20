package com.studentportal.dao;

import com.studentportal.model.Student;
import com.studentportal.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Database access for the students table (prepared statements only). */
public class StudentDAO {

    private static final String BASE_SELECT =
            "SELECT id, name, email, department, image_path, document_path FROM students";

    /**
     * Lists students ordered by name.
     * @param search optional text; matches name, email or department (null/blank = all students)
     */
    public List<Student> findAll(String search) throws SQLException {
        boolean hasSearch = search != null && !search.trim().isEmpty();
        String sql = BASE_SELECT
                + (hasSearch ? " WHERE name LIKE ? OR email LIKE ? OR department LIKE ?" : "")
                + " ORDER BY name ASC";

        List<Student> result = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (hasSearch) {
                String pattern = "%" + escapeLike(search.trim()) + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        }
        return result;
    }

    /** @return the student, or null if the id does not exist. */
    public Student findById(int id) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(BASE_SELECT + " WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private static Student map(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setDepartment(rs.getString("department"));
        s.setImagePath(rs.getString("image_path"));
        s.setDocumentPath(rs.getString("document_path"));
        return s;
    }

    /** Makes %, _ and \ in user input match literally in a LIKE pattern. */
    private static String escapeLike(String s) {
        return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
