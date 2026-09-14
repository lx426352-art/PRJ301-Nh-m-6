package com.aita.gitanalytics.controller;

import com.aita.gitanalytics.dao.AuthorMetricDAO;
import com.aita.gitanalytics.dao.GitCommitDAO;
import com.aita.gitanalytics.dao.GroupDAO;
import com.aita.gitanalytics.dto.*;
import com.aita.gitanalytics.service.GitParserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final GroupDAO groupDAO = new GroupDAO();
    private final AuthorMetricDAO metricDAO = new AuthorMetricDAO();
    private final GitCommitDAO commitDAO = new GitCommitDAO();
    private final GitParserService gitParserService = new GitParserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserDTO user = (UserDTO) session.getAttribute("user");
        int groupId = 1; // Default Group 5 for PRJ301 Demo

        try {
            // Fetch group and course information
            GroupDTO group = groupDAO.getGroupById(groupId);
            CourseDTO course = groupDAO.getCourseByGroupId(groupId);
            
            // Sync & recalculate metrics to ensure latest state
            List<ContributionMetricDTO> metrics = gitParserService.syncAndCalculateGroupMetrics(groupId);
            List<CommitLogDTO> commits = commitDAO.getCommitsByGroup(groupId);

            req.setAttribute("group", group);
            req.setAttribute("course", course);
            req.setAttribute("metrics", metrics);
            req.setAttribute("commits", commits);
            req.setAttribute("currentUser", user);

            // Count free-riders
            long freeRiderCount = metrics.stream().filter(ContributionMetricDTO::isFreeriderFlagged).count();
            req.setAttribute("freeRiderCount", freeRiderCount);

            if ("LECTURER".equalsIgnoreCase(user.getRole())) {
                req.getRequestDispatcher("/WEB-INF/views/dashboard-lecturer.jsp").forward(req, resp);
            } else {
                // Find student's own metric
                ContributionMetricDTO myMetric = metrics.stream()
                        .filter(m -> m.getUserId() == user.getUserId())
                        .findFirst()
                        .orElse(null);
                req.setAttribute("myMetric", myMetric);
                req.getRequestDispatcher("/WEB-INF/views/student-contribution.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
