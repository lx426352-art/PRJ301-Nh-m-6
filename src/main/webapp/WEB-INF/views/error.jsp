<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error | AITA System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body style="justify-content: center; align-items: center;">

    <div class="glass-card" style="max-width: 500px; text-align: center; padding: 3rem 2rem;">
        <div style="font-size: 3rem; margin-bottom: 1rem;">⚠️</div>
        <h2 style="color: var(--danger); margin-bottom: 0.75rem;">Application Error</h2>
        <p style="color: var(--text-muted); margin-bottom: 1.5rem;">
            <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "An unexpected error occurred in AITA Git Analytics Module." %>
        </p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Return to Dashboard</a>
    </div>

</body>
</html>
