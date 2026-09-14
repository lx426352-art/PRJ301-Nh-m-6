package com.aita.gitanalytics.dto;

import java.io.Serializable;

public class GroupMemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int groupId;
    private int userId;
    private String roleInGroup; // 'LEADER', 'MEMBER'
    
    // Join fields for convenience
    private String fullName;
    private String githubUsername;

    public GroupMemberDTO() {
    }

    public GroupMemberDTO(int groupId, int userId, String roleInGroup) {
        this.groupId = groupId;
        this.userId = userId;
        this.roleInGroup = roleInGroup;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoleInGroup() {
        return roleInGroup;
    }

    public void setRoleInGroup(String roleInGroup) {
        this.roleInGroup = roleInGroup;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }
}
