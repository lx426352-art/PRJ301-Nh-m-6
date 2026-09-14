package com.aita.gitanalytics.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class GroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int groupId;
    private int courseId;
    private String groupName;
    private String gitRepoUrl;
    private Timestamp createdAt;

    public GroupDTO() {
    }

    public GroupDTO(int groupId, int courseId, String groupName, String gitRepoUrl, Timestamp createdAt) {
        this.groupId = groupId;
        this.courseId = courseId;
        this.groupName = groupName;
        this.gitRepoUrl = gitRepoUrl;
        this.createdAt = createdAt;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGitRepoUrl() {
        return gitRepoUrl;
    }

    public void setGitRepoUrl(String gitRepoUrl) {
        this.gitRepoUrl = gitRepoUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
