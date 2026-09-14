package com.aita.gitanalytics.dao;

import com.aita.gitanalytics.dto.CommitLogDTO;
import com.aita.gitanalytics.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GitCommitDAO {

    public void insertCommitLog(CommitLogDTO commit) throws SQLException {
        String sql = "INSERT INTO commit_logs (group_id, user_id, commit_hash, commit_message, additions, deletions, total_churn, commit_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commit.getGroupId());
            if (commit.getUserId() > 0) {
                ps.setInt(2, commit.getUserId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, commit.getCommitHash());
            ps.setString(4, commit.getCommitMessage());
            ps.setInt(5, commit.getAdditions());
            ps.setInt(6, commit.getDeletions());
            ps.setInt(7, commit.getAdditions() + commit.getDeletions());
            ps.setTimestamp(8, new Timestamp(commit.getCommitDate().getTime()));
            
            ps.executeUpdate();
        }
    }

    public List<CommitLogDTO> getCommitsByGroup(int groupId) throws SQLException {
        List<CommitLogDTO> list = new ArrayList<>();
        String sql = "SELECT c.*, u.full_name FROM commit_logs c " +
                     "LEFT JOIN users u ON c.user_id = u.user_id " +
                     "WHERE c.group_id = ? ORDER BY c.commit_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CommitLogDTO dto = new CommitLogDTO();
                    dto.setCommitId(rs.getInt("commit_id"));
                    dto.setGroupId(rs.getInt("group_id"));
                    dto.setUserId(rs.getInt("user_id"));
                    dto.setCommitHash(rs.getString("commit_hash"));
                    dto.setCommitMessage(rs.getString("commit_message"));
                    dto.setAdditions(rs.getInt("additions"));
                    dto.setDeletions(rs.getInt("deletions"));
                    dto.setTotalChurn(rs.getInt("total_churn"));
                    dto.setCommitDate(rs.getTimestamp("commit_date"));
                    dto.setAuthorName(rs.getString("full_name"));
                    list.add(dto);
                }
            }
        }
        return list;
    }

    public List<CommitLogDTO> getCommitsByUser(int groupId, int userId) throws SQLException {
        List<CommitLogDTO> list = new ArrayList<>();
        String sql = "SELECT c.*, u.full_name FROM commit_logs c " +
                     "LEFT JOIN users u ON c.user_id = u.user_id " +
                     "WHERE c.group_id = ? AND c.user_id = ? ORDER BY c.commit_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CommitLogDTO dto = new CommitLogDTO();
                    dto.setCommitId(rs.getInt("commit_id"));
                    dto.setGroupId(rs.getInt("group_id"));
                    dto.setUserId(rs.getInt("user_id"));
                    dto.setCommitHash(rs.getString("commit_hash"));
                    dto.setCommitMessage(rs.getString("commit_message"));
                    dto.setAdditions(rs.getInt("additions"));
                    dto.setDeletions(rs.getInt("deletions"));
                    dto.setTotalChurn(rs.getInt("total_churn"));
                    dto.setCommitDate(rs.getTimestamp("commit_date"));
                    dto.setAuthorName(rs.getString("full_name"));
                    list.add(dto);
                }
            }
        }
        return list;
    }

    public void clearCommitsByGroup(int groupId) throws SQLException {
        String sql = "DELETE FROM commit_logs WHERE group_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.executeUpdate();
        }
    }
}
