<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.aita.gitanalytics.dto.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Git Contribution View | AITA PRJ301</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

    <header class="navbar">
        <div class="brand">
            ⚡ AITA System <span class="brand-badge">Student View</span>
        </div>
        <div class="user-profile">
            <% UserDTO currentUser = (UserDTO) request.getAttribute("currentUser"); %>
            <span>🎓 <%= currentUser != null ? currentUser.getFullName() : "Student" %></span>
            <span class="role-tag student">STUDENT</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <div class="container">

        <% 
            GroupDTO group = (GroupDTO) request.getAttribute("group");
            CourseDTO course = (CourseDTO) request.getAttribute("course");
            ContributionMetricDTO myMetric = (ContributionMetricDTO) request.getAttribute("myMetric");
            List<CommitLogDTO> commits = (List<CommitLogDTO>) request.getAttribute("commits");
        %>

        <div class="glass-card">
            <div class="card-header">
                <div>
                    <h2 class="card-title">
                        🙋‍♂️ My Git Contribution Report
                    </h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.3rem;">
                        Group: <strong><%= group != null ? group.getGroupName() : "Group 5" %></strong> (<%= course != null ? course.getCourseCode() : "PRJ301" %>)
                    </p>
                </div>
            </div>

            <% if (myMetric != null) { %>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1.25rem; margin-top: 1rem;">
                    <div style="background: rgba(15,23,42,0.6); padding: 1.25rem; border-radius: 12px; border: 1px solid var(--border-color);">
                        <div style="font-size: 0.85rem; color: var(--text-muted);">ICI Contribution Index</div>
                        <div style="font-size: 2rem; font-weight: 700; color: <%= myMetric.isFreeriderFlagged() ? "var(--danger)" : "var(--success)" %>; font-family: 'JetBrains Mono', monospace; margin-top: 0.3rem;">
                            <%= myMetric.getIciScore() %>%
                        </div>
                        <div style="margin-top: 0.4rem;">
                            <span class="status-badge <%= myMetric.isFreeriderFlagged() ? "alert" : "ok" %>">
                                <%= myMetric.isFreeriderFlagged() ? "⚠️ Free-rider Alert (< 15%)" : "✅ Good Contribution" %>
                            </span>
                        </div>
                    </div>

                    <div style="background: rgba(15,23,42,0.6); padding: 1.25rem; border-radius: 12px; border: 1px solid var(--border-color);">
                        <div style="font-size: 0.85rem; color: var(--text-muted);">Total Valid Commits</div>
                        <div style="font-size: 2rem; font-weight: 700; color: var(--text-main); font-family: 'JetBrains Mono', monospace; margin-top: 0.3rem;">
                            <%= myMetric.getTotalCommits() %>
                        </div>
                    </div>

                    <div style="background: rgba(15,23,42,0.6); padding: 1.25rem; border-radius: 12px; border: 1px solid var(--border-color);">
                        <div style="font-size: 0.85rem; color: var(--text-muted);">Lines of Code (LOC) Churn</div>
                        <div style="font-size: 1.5rem; font-weight: 700; font-family: 'JetBrains Mono', monospace; margin-top: 0.3rem;">
                            <span style="color: #6ee7b7;">+<%= myMetric.getTotalAdditions() %></span> / 
                            <span style="color: #fca5a5;">-<%= myMetric.getTotalDeletions() %></span>
                        </div>
                    </div>

                    <div style="background: rgba(15,23,42,0.6); padding: 1.25rem; border-radius: 12px; border: 1px solid var(--border-color);">
                        <div style="font-size: 0.85rem; color: var(--text-muted);">Active Days Spread</div>
                        <div style="font-size: 2rem; font-weight: 700; color: var(--accent); font-family: 'JetBrains Mono', monospace; margin-top: 0.3rem;">
                            <%= myMetric.getActiveDays() %> days
                        </div>
                    </div>
                </div>
            <% } else { %>
                <p style="color: var(--text-muted); margin-top: 1rem;">No individual contribution metric record found for your account yet.</p>
            <% } %>
        </div>

        <!-- Recent Commit Log Table -->
        <div class="glass-card">
            <h3 class="card-title" style="margin-bottom: 1rem;">📜 Group Recent Commits</h3>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Commit Hash</th>
                        <th>Author</th>
                        <th>Message</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (commits != null && !commits.isEmpty()) {
                        for (CommitLogDTO c : commits) { %>
                        <tr>
                            <td style="font-family: 'JetBrains Mono', monospace; color: var(--accent);">
                                <%= (c.getCommitHash() != null && c.getCommitHash().length() >= 7) ? c.getCommitHash().substring(0, 7) : (c.getCommitHash() != null ? c.getCommitHash() : "N/A") %>
                            </td>
                            <td><%= c.getAuthorName() != null ? c.getAuthorName() : "Student #" + c.getUserId() %></td>
                            <td><%= c.getCommitMessage() != null ? c.getCommitMessage() : "" %></td>
                            <td style="color: var(--text-muted); font-size: 0.85rem;"><%= c.getCommitDate() != null ? c.getCommitDate() : "" %></td>
                        </tr>
                    <%  }
                    } %>
                </tbody>
            </table>
        </div>

    </div>

    <footer>
        AITA System - Module 5: Git Analytics Teamwork Assessor (FE-L-03) &copy; Fall 2026 PRJ301
    </footer>

</body>
</html>
