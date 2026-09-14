package com.aita.gitanalytics.dao;

import com.aita.gitanalytics.dto.CourseDTO;
import com.aita.gitanalytics.dto.GroupDTO;
import com.aita.gitanalytics.dto.GroupMemberDTO;
import com.aita.gitanalytics.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

    public GroupDTO getGroupById(int groupId) throws SQLException {
        String sql = "SELECT * FROM \"groups\" WHERE group_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGroup(rs);
                }
            }
        }
        return null;
    }

    public List<GroupDTO> getAllGroups() throws SQLException {
        List<GroupDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM \"groups\" ORDER BY group_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToGroup(rs));
            }
        }
        return list;
    }

    public CourseDTO getCourseByGroupId(int groupId) throws SQLException {
        String sql = "SELECT c.* FROM courses c " +
                     "JOIN \"groups\" g ON c.course_id = g.course_id " +
                     "WHERE g.group_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CourseDTO course = new CourseDTO();
                    course.setCourseId(rs.getInt("course_id"));
                    course.setCourseCode(rs.getString("course_code"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setSemester(rs.getString("semester"));
                    return course;
                }
            }
        }
        return null;
    }

    public List<GroupMemberDTO> getGroupMembers(int groupId) throws SQLException {
        List<GroupMemberDTO> list = new ArrayList<>();
        String sql = "SELECT gm.group_id, gm.user_id, gm.role_in_group, u.full_name, u.github_username " +
                     "FROM group_members gm " +
                     "JOIN users u ON gm.user_id = u.user_id " +
                     "WHERE gm.group_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GroupMemberDTO dto = new GroupMemberDTO();
                    dto.setGroupId(rs.getInt("group_id"));
                    dto.setUserId(rs.getInt("user_id"));
                    dto.setRoleInGroup(rs.getString("role_in_group"));
                    dto.setFullName(rs.getString("full_name"));
                    dto.setGithubUsername(rs.getString("github_username"));
                    list.add(dto);
                }
            }
        }
        return list;
    }

    public GroupDTO getGroupByUserId(int userId) throws SQLException {
        String sql = "SELECT g.* FROM \"groups\" g " +
                     "JOIN group_members gm ON g.group_id = gm.group_id " +
                     "WHERE gm.user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGroup(rs);
                }
            }
        }
        return null;
    }

    private GroupDTO mapResultSetToGroup(ResultSet rs) throws SQLException {
        GroupDTO group = new GroupDTO();
        group.setGroupId(rs.getInt("group_id"));
        group.setCourseId(rs.getInt("course_id"));
        group.setGroupName(rs.getString("group_name"));
        group.setGitRepoUrl(rs.getString("git_repo_url"));
        group.setCreatedAt(rs.getTimestamp("created_at"));
        return group;
    }
}
