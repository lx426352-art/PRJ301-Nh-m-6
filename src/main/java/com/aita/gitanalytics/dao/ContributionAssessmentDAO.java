package com.aita.gitanalytics.dao;

import com.aita.gitanalytics.dto.ContributionAssessmentDTO;
import com.aita.gitanalytics.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContributionAssessmentDAO {

    public void saveOrUpdateAssessment(ContributionAssessmentDTO assessment) throws SQLException {
        String checkSql = "SELECT assessment_id FROM contribution_assessments WHERE group_id = ? AND user_id = ?";
        String updateSql = "UPDATE contribution_assessments SET final_score = ?, is_freerider_flagged = ?, " +
                           "teacher_notes = ?, assessed_at = ? WHERE group_id = ? AND user_id = ?";
        String insertSql = "INSERT INTO contribution_assessments (group_id, user_id, final_score, is_freerider_flagged, teacher_notes, assessed_at) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            boolean exists = false;
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, assessment.getGroupId());
                checkPs.setInt(2, assessment.getUserId());
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setDouble(1, assessment.getFinalScore());
                    ps.setBoolean(2, assessment.isFreeriderFlagged());
                    ps.setString(3, assessment.getTeacherNotes());
                    ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    ps.setInt(5, assessment.getGroupId());
                    ps.setInt(6, assessment.getUserId());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, assessment.getGroupId());
                    ps.setInt(2, assessment.getUserId());
                    ps.setDouble(3, assessment.getFinalScore());
                    ps.setBoolean(4, assessment.isFreeriderFlagged());
                    ps.setString(5, assessment.getTeacherNotes());
                    ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    ps.executeUpdate();
                }
            }
        }
    }

    public List<ContributionAssessmentDTO> getAssessmentsByGroup(int groupId) throws SQLException {
        List<ContributionAssessmentDTO> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name, u.github_username FROM contribution_assessments a " +
                     "JOIN users u ON a.user_id = u.user_id " +
                     "WHERE a.group_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContributionAssessmentDTO dto = new ContributionAssessmentDTO();
                    dto.setAssessmentId(rs.getInt("assessment_id"));
                    dto.setGroupId(rs.getInt("group_id"));
                    dto.setUserId(rs.getInt("user_id"));
                    dto.setFinalScore(rs.getDouble("final_score"));
                    dto.setFreeriderFlagged(rs.getBoolean("is_freerider_flagged"));
                    dto.setTeacherNotes(rs.getString("teacher_notes"));
                    dto.setAssessedAt(rs.getTimestamp("assessed_at"));
                    dto.setFullName(rs.getString("full_name"));
                    dto.setGithubUsername(rs.getString("github_username"));
                    list.add(dto);
                }
            }
        }
        return list;
    }
}
