<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | AITA Git Analytics Teamwork Assessor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body style="justify-content: center; align-items: center;">

    <div class="glass-card" style="width: 100%; max-width: 440px; padding: 2.5rem;">
        <div style="text-align: center; margin-bottom: 2rem;">
            <div class="brand" style="justify-content: center; font-size: 1.8rem; margin-bottom: 0.5rem;">
                ⚡ AITA SYSTEM
            </div>
            <span class="brand-badge">Git Analytics Module (FE-L-03)</span>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.75rem;">
                PRJ301 - Individual Contribution Index &amp; Free-rider Assessor
            </p>
        </div>

        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div style="background: rgba(239,68,68,0.2); border: 1px solid rgba(239,68,68,0.4); color: #fca5a5; padding: 0.75rem 1rem; border-radius: 8px; font-size: 0.88rem; margin-bottom: 1.5rem;">
                ⚠️ <%= errorMessage %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Username or Email</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="e.g. teacher_john or student_alice" required value="teacher_john">
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Enter password" required value="password123">
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; justify-content: center; padding: 0.8rem; font-size: 1rem; margin-top: 1rem;">
                🔑 Sign In
            </button>
        </form>

        <div style="margin-top: 2rem; padding-top: 1.25rem; border-top: 1px solid var(--border-color); font-size: 0.8rem; color: var(--text-muted); text-align: center;">
            <p><strong>Demo Test Credentials:</strong></p>
            <p style="margin-top: 0.3rem;">Lecturer: <code>teacher_john</code> / <code>password123</code></p>
            <p style="margin-top: 0.2rem;">Student Leader: <code>student_alice</code> / <code>password123</code></p>
            <p style="margin-top: 0.2rem;">Free-rider Student: <code>student_charlie</code> / <code>password123</code></p>
        </div>
    </div>

</body>
</html>
