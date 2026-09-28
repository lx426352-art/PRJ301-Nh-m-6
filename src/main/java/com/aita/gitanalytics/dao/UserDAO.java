package com.aita.gitanalytics.dao;

import com.aita.gitanalytics.dto.UserDTO;
import com.aita.gitanalytics.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // =========================================
    // THÊM USER
    // =========================================
    public int createUser(
            String fullName,
            String email,
            String githubUsername) throws SQLException {

        String sql = "INSERT INTO users "
                + "(full_name, email, github_username) "
                + "VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, githubUsername);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }

        return -1;
    }

    // =========================================
    // XEM FULL DANH SÁCH USER
    // =========================================
    public List<UserDTO> getAllUsers() throws SQLException {

        List<UserDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM users ORDER BY user_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        }

        return list;
    }

    // =========================================
    // TÌM USER THEO ID
    // =========================================
    public UserDTO getUserById(int userId) throws SQLException {

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }

        return null;
    }

    // =========================================
    // LẤY USER THEO GROUP
    // =========================================
    public List<UserDTO> getUsersByGroup(int groupId)
            throws SQLException {

        List<UserDTO> list = new ArrayList<>();

        String sql = "SELECT u.* "
                + "FROM users u "
                + "JOIN group_members gm "
                + "ON u.user_id = gm.user_id "
                + "WHERE gm.group_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(mapResultSetToUser(rs));
                }
            }
        }

        return list;
    }

    // =========================================
    // SỬA USER
    // =========================================
    public boolean updateUser(
            int userId,
            String fullName,
            String email,
            String githubUsername) throws SQLException {

        String sql = "UPDATE users "
                + "SET full_name = ?, "
                + "email = ?, "
                + "github_username = ? "
                + "WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, githubUsername);
            ps.setInt(4, userId);

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================
    // XÓA USER
    // =========================================
    public boolean deleteUser(int userId) throws SQLException {

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================
    // MAP RESULTSET -> USERDTO
    // =========================================
    private UserDTO mapResultSetToUser(ResultSet rs)
            throws SQLException {

        UserDTO user = new UserDTO();

        user.setUserId(
                rs.getInt("user_id")
        );

        user.setFullName(
                rs.getString("full_name")
        );

        user.setEmail(
                rs.getString("email")
        );

        user.setGithubUsername(
                rs.getString("github_username")
        );

        user.setCreatedAt(
                rs.getTimestamp("created_at")
        );

        return user;
    }

    // =========================================
    // AUTHENTICATE
    // =========================================
    public UserDTO authenticate(
            String username,
            String rawPassword) throws SQLException {

        return null;
    }
}
