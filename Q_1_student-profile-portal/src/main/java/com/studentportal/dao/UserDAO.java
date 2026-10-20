package com.studentportal.dao;

import com.studentportal.model.User;
import com.studentportal.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Database access for the users table (prepared statements only). */
public class UserDAO {

    private static final String FIND_BY_USERNAME =
            "SELECT id, username, password_hash, full_name FROM users WHERE username = ?";

    /** @return the user, or null when no such username exists. */
    public User findByUsername(String username) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setFullName(rs.getString("full_name"));
                    return u;
                }
                return null;
            }
        }
    }
}
