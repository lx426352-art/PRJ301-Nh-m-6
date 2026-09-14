package com.aita.gitanalytics.dto;

import java.io.Serializable;
import java.util.Date;

public class CommitLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int commitId;
    private int groupId;
    private int userId;
    private String commitHash;
    private String commitMessage;
    private int additions;
    private int deletions;
    private int totalChurn;
    private Date commitDate;
    
    // Join field
    private String authorName;

    public CommitLogDTO() {
    }

    public CommitLogDTO(int commitId, int groupId, int userId, String commitHash, String commitMessage, int additions, int deletions, Date commitDate) {
        this.commitId = commitId;
        this.groupId = groupId;
        this.userId = userId;
        this.commitHash = commitHash;
        this.commitMessage = commitMessage;
        this.additions = additions;
        this.deletions = deletions;
        this.totalChurn = additions + deletions;
        this.commitDate = commitDate;
    }

    public int getCommitId() {
        return commitId;
    }

    public void setCommitId(int commitId) {
        this.commitId = commitId;
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

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
        this.totalChurn = this.additions + this.deletions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
        this.totalChurn = this.additions + this.deletions;
    }

    public int getTotalChurn() {
        return totalChurn;
    }

    public void setTotalChurn(int totalChurn) {
        this.totalChurn = totalChurn;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
