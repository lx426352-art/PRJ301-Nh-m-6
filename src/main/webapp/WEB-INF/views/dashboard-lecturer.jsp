<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.aita.gitanalytics.dto.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lecturer Git Analytics Dashboard | AITA PRJ301</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

    <!-- Header Navbar -->
    <header class="navbar">
        <div class="brand">
            ⚡ AITA System <span class="brand-badge">FE-L-03 Assessor</span>
        </div>
        <div class="user-profile">
            <% UserDTO currentUser = (UserDTO) request.getAttribute("currentUser"); %>
            <span>👨‍🏫 <%= currentUser != null ? currentUser.getFullName() : "Dr. John Doe" %></span>
            <span class="role-tag lecturer">LECTURER</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <div class="container">

        <% 
            GroupDTO group = (GroupDTO) request.getAttribute("group");
            CourseDTO course = (CourseDTO) request.getAttribute("course");
            List<ContributionMetricDTO> metrics = (List<ContributionMetricDTO>) request.getAttribute("metrics");
            List<CommitLogDTO> commits = (List<CommitLogDTO>) request.getAttribute("commits");
            Long freeRiderCount = (Long) request.getAttribute("freeRiderCount");
            if (freeRiderCount == null) freeRiderCount = 0L;
        %>

        <!-- Group Information Banner -->
        <div class="glass-card">
            <div class="card-header">
                <div>
                    <h2 class="card-title">
                        📊 Git Analytics Teamwork Assessor Dashboard
                    </h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.3rem;">
                        Course: <strong><%= course != null ? course.getCourseCode() + " - " + course.getCourseName() : "PRJ301" %></strong> (<%= course != null ? course.getSemester() : "Fall 2026" %>)
                        &nbsp;|&nbsp; Group: <strong><%= group != null ? group.getGroupName() : "Group 5" %></strong>
                    </p>
                    <p style="font-size: 0.85rem; margin-top: 0.4rem; font-family: 'JetBrains Mono', monospace;">
                        🔗 Repository: <a href="<%= group != null ? group.getGitRepoUrl() : "#" %>" target="_blank" style="color: var(--accent); text-decoration: none;"><%= group != null ? group.getGitRepoUrl() : "https://github.com/prj301-group5/aita-git-analytics" %></a>
                    </p>
                </div>
                <div>
                    <button id="btnSyncRepo" onclick="syncGitRepository(<%= group != null ? group.getGroupId() : 1 %>)" class="btn btn-primary">
                        🔄 Sync Git Repo
                    </button>
                </div>
            </div>
        </div>

        <!-- Free-rider Warning Alert Banner -->
        <% if (freeRiderCount > 0) { %>
            <div class="alert-banner">
                <div class="alert-icon">🚨</div>
                <div class="alert-content">
                    <h4>Free-rider Alert Triggered!</h4>
                    <p>Detected <strong><%= freeRiderCount %> student(s)</strong> with Individual Contribution Index ($ICI < 15\%$). Please review their contribution details below before finalizing group grades.</p>
                </div>
            </div>
        <% } %>

        <!-- ICI Score Cards Overview Grid -->
        <h3 style="margin-bottom: 1rem; font-weight: 600; font-size: 1.15rem;">Individual Contribution Overview (ICI Scores)</h3>
        <div class="metrics-grid">
            <% if (metrics != null) { 
                for (ContributionMetricDTO m : metrics) { %>
                <div class="metric-card <%= m.isFreeriderFlagged() ? "freerider" : "" %>">
                    <div class="metric-user">
                        <div>
                            <div class="user-name"><%= m.getFullName() %></div>
                            <div class="user-github">@<%= m.getGithubUsername() %> (<%= m.getRoleInGroup() != null ? m.getRoleInGroup() : "MEMBER" %>)</div>
                        </div>
                        <div class="ici-score-display">
                            <div class="ici-value" style="color: <%= m.isFreeriderFlagged() ? "var(--danger)" : "var(--success)" %>;">
                                <%= m.getIciScore() %>%
                            </div>
                            <span class="status-badge <%= m.isFreeriderFlagged() ? "alert" : "ok" %>">
                                <%= m.isFreeriderFlagged() ? "ALERT: FREE-RIDER" : "OK" %>
                            </span>
                        </div>
                    </div>

                    <div class="progress-bar-bg">
                        <div class="progress-bar-fill" style="width: <%= Math.min(100.0, m.getIciScore()) %>%; background: <%= m.isFreeriderFlagged() ? "linear-gradient(90deg, #ef4444, #dc2626)" : "linear-gradient(90deg, #10b981, #06b6d4)" %>;"></div>
                    </div>

                    <div style="display: flex; justify-content: space-between; font-size: 0.82rem; color: var(--text-muted); margin-top: 0.6rem;">
                        <span>Commits: <strong><%= m.getTotalCommits() %></strong></span>
                        <span>Churn: <strong>+<%= m.getTotalAdditions() %> / -<%= m.getTotalDeletions() %></strong></span>
                        <span>Active: <strong><%= m.getActiveDays() %> day(s)</strong></span>
                    </div>
                </div>
            <%  } 
            } %>
        </div>

        <!-- Detailed Breakdown Table -->
        <div class="glass-card">
            <div class="card-header">
                <h3 class="card-title">📈 Detailed Git Churn Breakdown &amp; Evaluation</h3>
            </div>

            <table class="data-table">
                <thead>
                    <tr>
                        <th>Student Name</th>
                        <th>GitHub Handle</th>
                        <th>Total Commits</th>
                        <th>LOC Added</th>
                        <th>LOC Deleted</th>
                        <th>Active Days</th>
                        <th>ICI Index</th>
                        <th>Status Flag</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (metrics != null && !metrics.isEmpty()) {
                        for (ContributionMetricDTO m : metrics) { %>
                        <tr>
                            <td><strong><%= m.getFullName() %></strong></td>
                            <td style="font-family: 'JetBrains Mono', monospace; color: var(--text-muted);">@<%= m.getGithubUsername() %></td>
                            <td><%= m.getTotalCommits() %></td>
                            <td style="color: #6ee7b7;">+<%= m.getTotalAdditions() %></td>
                            <td style="color: #fca5a5;">-<%= m.getTotalDeletions() %></td>
                            <td><%= m.getActiveDays() %> days</td>
                            <td style="font-weight: 700; font-family: 'JetBrains Mono', monospace; font-size: 1.05rem;">
                                <%= m.getIciScore() %>%
                            </td>
                            <td>
                                <span class="status-badge <%= m.isFreeriderFlagged() ? "alert" : "ok" %>">
                                    <%= m.isFreeriderFlagged() ? "Free-rider Flag" : "Normal Contribution" %>
                                </span>
                            </td>
                            <td>
                                <button class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;"
                                        onclick="openAssessmentModal(<%= m.getUserId() %>, '<%= m.getFullName() %>', 10.0, <%= m.isFreeriderFlagged() %>)">
                                    ✏️ Adjust Grade
                                </button>
                            </td>
                        </tr>
                    <%  } 
                    } else { %>
                        <tr>
                            <td colspan="9" style="text-align: center; color: var(--text-muted);">No metrics data available yet. Click "Sync Git Repo" above.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- Recent Commit Log Table -->
        <div class="glass-card">
            <div class="card-header">
                <h3 class="card-title">📝 Synchronized Git Commit Log History</h3>
            </div>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Commit Hash</th>
                        <th>Author</th>
                        <th>Commit Message</th>
                        <th>Churn (+/-)</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (commits != null && !commits.isEmpty()) {
                        for (CommitLogDTO c : commits) { %>
                        <tr>
                            <td style="font-family: 'JetBrains Mono', monospace; font-size: 0.85rem; color: var(--accent);">
                                <%= (c.getCommitHash() != null && c.getCommitHash().length() >= 7) ? c.getCommitHash().substring(0, 7) : (c.getCommitHash() != null ? c.getCommitHash() : "N/A") %>
                            </td>
                            <td><%= c.getAuthorName() != null ? c.getAuthorName() : "Student #" + c.getUserId() %></td>
                            <td><%= c.getCommitMessage() %></td>
                            <td style="font-family: 'JetBrains Mono', monospace; font-size: 0.85rem;">
                                <span style="color: #6ee7b7;">+<%= c.getAdditions() %></span> / 
                                <span style="color: #fca5a5;">-<%= c.getDeletions() %></span>
                            </td>
                            <td style="font-size: 0.85rem; color: var(--text-muted);"><%= c.getCommitDate() %></td>
                        </tr>
                    <%  }
                    } else { %>
                        <tr>
                            <td colspan="5" style="text-align: center; color: var(--text-muted);">No commit history found.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>

    <!-- Modal for Adjusting Student Score -->
    <div id="assessmentModal" class="modal-overlay">
        <div class="modal-card">
            <h3 style="margin-bottom: 1rem;">Adjust Grade &amp; Evaluation Notes</h3>
            <p style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 1.25rem;">
                Student: <strong id="modalStudentName" style="color: var(--text-main);"></strong>
            </p>

            <form onsubmit="submitAssessmentForm(event)">
                <input type="hidden" id="modalUserId">

                <div class="form-group">
                    <label for="modalScore">Override Final Score (0.0 - 10.0)</label>
                    <input type="number" step="0.1" min="0" max="10" id="modalScore" class="form-control" required value="10.0">
                </div>

                <div class="form-group">
                    <label style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer;">
                        <input type="checkbox" id="modalFreerider" style="width: 18px; height: 18px;">
                        <span>Keep Free-rider Red Flag On</span>
                    </label>
                </div>

                <div class="form-group">
                    <label for="modalNotes">Lecturer Evaluation Notes / Q&amp;A Feedback</label>
                    <textarea id="modalNotes" class="form-control" rows="3" placeholder="Enter reason for score adjustment..."></textarea>
                </div>

                <div style="display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;">
                    <button type="button" class="btn btn-secondary" onclick="closeAssessmentModal()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Assessment</button>
                </div>
            </form>
        </div>
    </div>

    <footer>
        AITA System - Module 5: Git Analytics Teamwork Assessor (FE-L-03) &copy; Fall 2026 PRJ301
    </footer>

    <script src="${pageContext.request.contextPath}/js/analytics-chart.js"></script>
</body>
</html>
