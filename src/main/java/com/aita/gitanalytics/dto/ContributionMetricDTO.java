package com.aita.gitanalytics.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class ContributionMetricDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int metricId;
    private int groupId;
    private int userId;
    private int totalCommits;
    private int totalAdditions;
    private int totalDeletions;
    private int activeDays;
    private double iciScore;
    private boolean isFreeriderFlagged;
    private Timestamp calculatedAt;

    // Additional UI/Join fields
    private String fullName;
    private String githubUsername;
    private String roleInGroup;

    public ContributionMetricDTO() {
    }

    public ContributionMetricDTO(int metricId, int groupId, int userId, int totalCommits, int totalAdditions, int totalDeletions, int activeDays, double iciScore, boolean isFreeriderFlagged, Timestamp calculatedAt) {
        this.metricId = metricId;
        this.groupId = groupId;
        this.userId = userId;
        this.totalCommits = totalCommits;
        this.totalAdditions = totalAdditions;
        this.totalDeletions = totalDeletions;
        this.activeDays = activeDays;
        this.iciScore = iciScore;
        this.isFreeriderFlagged = isFreeriderFlagged;
        this.calculatedAt = calculatedAt;
    }

    public int getMetricId() {
        return metricId;
    }

    public void setMetricId(int metricId) {
        this.metricId = metricId;
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

    public int getTotalCommits() {
        return totalCommits;
    }

    public void setTotalCommits(int totalCommits) {
        this.totalCommits = totalCommits;
    }

    public int getTotalAdditions() {
        return totalAdditions;
    }

    public void setTotalAdditions(int totalAdditions) {
        this.totalAdditions = totalAdditions;
    }

    public int getTotalDeletions() {
        return totalDeletions;
    }

    public void setTotalDeletions(int totalDeletions) {
        this.totalDeletions = totalDeletions;
    }

    public int getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(int activeDays) {
        this.activeDays = activeDays;
    }

    public double getIciScore() {
        return iciScore;
    }

    public void setIciScore(double iciScore) {
        this.iciScore = iciScore;
    }

    public boolean isFreeriderFlagged() {
        return isFreeriderFlagged;
    }

    public void setFreeriderFlagged(boolean freeriderFlagged) {
        isFreeriderFlagged = freeriderFlagged;
    }

    public Timestamp getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Timestamp calculatedAt) {
        this.calculatedAt = calculatedAt;
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

    public String getRoleInGroup() {
        return roleInGroup;
    }

    public void setRoleInGroup(String roleInGroup) {
        this.roleInGroup = roleInGroup;
    }
}
