package com.aita.gitanalytics.controller;

import com.aita.gitanalytics.dto.ContributionMetricDTO;
import com.aita.gitanalytics.service.GitParserService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/analytics/metrics", "/api/analytics/sync"})
public class GitAnalyticsServlet extends HttpServlet {

    private final GitParserService gitParserService = new GitParserService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String groupIdParam = req.getParameter("groupId");
        int groupId = (groupIdParam != null && !groupIdParam.isEmpty()) ? Integer.parseInt(groupIdParam) : 1;

        try {
            List<ContributionMetricDTO> metrics = gitParserService.syncAndCalculateGroupMetrics(groupId);

            Map<String, Object> result = new HashMap<>();
            result.put("groupId", groupId);
            result.put("metrics", metrics);
            result.put("status", "SUCCESS");

            resp.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch metrics: " + e.getMessage());
            resp.getWriter().write(gson.toJson(errorMap));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String groupIdParam = req.getParameter("groupId");
        int groupId = (groupIdParam != null && !groupIdParam.isEmpty()) ? Integer.parseInt(groupIdParam) : 1;

        try {
            List<ContributionMetricDTO> updatedMetrics = gitParserService.syncAndCalculateGroupMetrics(groupId);

            Map<String, Object> result = new HashMap<>();
            result.put("message", "Git repository metrics synchronized successfully.");
            result.put("metrics", updatedMetrics);
            result.put("status", "SUCCESS");

            resp.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Git sync failed: " + e.getMessage());
            resp.getWriter().write(gson.toJson(errorMap));
        }
    }
}
