package com.aita.gitanalytics.dao;

import com.aita.gitanalytics.dto.ContributionMetricDTO;
import com.aita.gitanalytics.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorMetricDAO {

    public void saveOrUpdateMetrics(ContributionMetricDTO metric) throws SQLException {
        String checkSql = "SELECT metric_id FROM author_metrics WHERE group_id = ? AND user_id = ?";
        String updateSql = "UPDATE author_metrics SET total_commits = ?, total_additions = ?, total_deletions = ?, " +
                           "active_days = ?, ici_score = ?, is_freerider_flagged = ?, calculated_at = ? " +
                           "WHERE group_id = ? AND user_id = ?";
        String insertSql = "INSERT INTO author_metrics (group_id, user_id, total_commits, total_additions, total_deletions, active_days, ici_score, is_freerider_flagged, calculated_at) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            boolean exists = false;
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, metric.getGroupId());
                checkPs.setInt(2, metric.getUserId());
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, metric.getTotalCommits());
                    ps.setInt(2, metric.getTotalAdditions());
                    ps.setInt(3, metric.getTotalDeletions());
                    ps.setInt(4, metric.getActiveDays());
                    ps.setDouble(5, metric.getIciScore());
                    ps.setBoolean(6, metric.isFreeriderFlagged());
                    ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                    ps.setInt(8, metric.getGroupId());
                    ps.setInt(9, metric.getUserId());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, metric.getGroupId());
                    ps.setInt(2, metric.getUserId());
                    ps.setInt(3, metric.getTotalCommits());
                    ps.setInt(4, metric.getTotalAdditions());
                    ps.setInt(5, metric.getTotalDeletions());
                    ps.setInt(6, metric.getActiveDays());
                    ps.setDouble(7, metric.getIciScore());
                    ps.setBoolean(8, metric.isFreeriderFlagged());
                    ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                    ps.executeUpdate();
                }
            }
        }
    }

    public List<ContributionMetricDTO> getMetricsByGroup(int groupId) throws SQLException {
        List<ContributionMetricDTO> list = new ArrayList<>();
        String sql = "SELECT m.*, u.full_name, u.github_username, gm.role_in_group FROM author_metrics m " +
                     "JOIN users u ON m.user_id = u.user_id " +
                     "LEFT JOIN group_members gm ON m.group_id = gm.group_id AND m.user_id = gm.user_id " +
                     "WHERE m.group_id = ? ORDER BY m.ici_score DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContributionMetricDTO dto = new ContributionMetricDTO();
                    dto.setMetricId(rs.getInt("metric_id"));
                    dto.setGroupId(rs.getInt("group_id"));
                    dto.setUserId(rs.getInt("user_id"));
                    dto.setTotalCommits(rs.getInt("total_commits"));
                    dto.setTotalAdditions(rs.getInt("total_additions"));
                    dto.setTotalDeletions(rs.getInt("total_deletions"));
                    dto.setActiveDays(rs.getInt("active_days"));
                    dto.setIciScore(rs.getDouble("ici_score"));
                    dto.setFreeriderFlagged(rs.getBoolean("is_freerider_flagged"));
                    dto.setCalculatedAt(rs.getTimestamp("calculated_at"));
                    dto.setFullName(rs.getString("full_name"));
                    dto.setGithubUsername(rs.getString("github_username"));
                    dto.setRoleInGroup(rs.getString("role_in_group"));
                    list.add(dto);
                }
            }
        }
        return list;
    }

    public ContributionMetricDTO getMetricByUser(int groupId, int userId) throws SQLException {
        String sql = "SELECT m.*, u.full_name, u.github_username, gm.role_in_group FROM author_metrics m " +
                     "JOIN users u ON m.user_id = u.user_id " +
                     "LEFT JOIN group_members gm ON m.group_id = gm.group_id AND m.user_id = gm.user_id " +
                     "WHERE m.group_id = ? AND m.user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ContributionMetricDTO dto = new ContributionMetricDTO();
                    dto.setMetricId(rs.getInt("metric_id"));
                    dto.setGroupId(rs.getInt("group_id"));
                    dto.setUserId(rs.getInt("user_id"));
                    dto.setTotalCommits(rs.getInt("total_commits"));
                    dto.setTotalAdditions(rs.getInt("total_additions"));
                    dto.setTotalDeletions(rs.getInt("total_deletions"));
                    dto.setActiveDays(rs.getInt("active_days"));
                    dto.setIciScore(rs.getDouble("ici_score"));
                    dto.setFreeriderFlagged(rs.getBoolean("is_freerider_flagged"));
                    dto.setCalculatedAt(rs.getTimestamp("calculated_at"));
                    dto.setFullName(rs.getString("full_name"));
                    dto.setGithubUsername(rs.getString("github_username"));
                    dto.setRoleInGroup(rs.getString("role_in_group"));
                    return dto;
                }
            }
        }
        return null;
    }
}
