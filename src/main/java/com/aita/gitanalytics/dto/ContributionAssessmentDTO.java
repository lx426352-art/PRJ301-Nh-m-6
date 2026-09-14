package com.aita.gitanalytics.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class ContributionAssessmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int assessmentId;
    private int groupId;
    private int userId;
    private double finalScore;
    private boolean isFreeriderFlagged;
    private String teacherNotes;
    private Timestamp assessedAt;

    // Join fields
    private String fullName;
    private String githubUsername;

    public ContributionAssessmentDTO() {
    }

    public ContributionAssessmentDTO(int assessmentId, int groupId, int userId, double finalScore, boolean isFreeriderFlagged, String teacherNotes, Timestamp assessedAt) {
        this.assessmentId = assessmentId;
        this.groupId = groupId;
        this.userId = userId;
        this.finalScore = finalScore;
        this.isFreeriderFlagged = isFreeriderFlagged;
        this.teacherNotes = teacherNotes;
        this.assessedAt = assessedAt;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
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

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public boolean isFreeriderFlagged() {
        return isFreeriderFlagged;
    }

    public void setFreeriderFlagged(boolean freeriderFlagged) {
        isFreeriderFlagged = freeriderFlagged;
    }

    public String getTeacherNotes() {
        return teacherNotes;
    }

    public void setTeacherNotes(String teacherNotes) {
        this.teacherNotes = teacherNotes;
    }

    public Timestamp getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(Timestamp assessedAt) {
        this.assessedAt = assessedAt;
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
