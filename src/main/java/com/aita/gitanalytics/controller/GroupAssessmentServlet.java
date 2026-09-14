package com.aita.gitanalytics.controller;

import com.aita.gitanalytics.dao.ContributionAssessmentDAO;
import com.aita.gitanalytics.dto.ContributionAssessmentDTO;
import com.aita.gitanalytics.dto.UserDTO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/assessment/save"})
public class GroupAssessmentServlet extends HttpServlet {

    private final ContributionAssessmentDAO assessmentDAO = new ContributionAssessmentDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        String role = (currentUser != null) ? currentUser.getRole() : (String) req.getAttribute("authenticatedRole");

        if (role == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized: Please log in as Lecturer\"}");
            return;
        }

        if (!"LECTURER".equalsIgnoreCase(role)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\": \"Forbidden: Only lecturers can record score adjustments\"}");
            return;
        }

        try {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            int userId = Integer.parseInt(req.getParameter("userId"));
            double finalScore = Double.parseDouble(req.getParameter("finalScore"));
            boolean isFreeriderFlagged = Boolean.parseBoolean(req.getParameter("isFreeriderFlagged"));
            String teacherNotes = req.getParameter("teacherNotes");

            ContributionAssessmentDTO dto = new ContributionAssessmentDTO();
            dto.setGroupId(groupId);
            dto.setUserId(userId);
            dto.setFinalScore(finalScore);
            dto.setFreeriderFlagged(isFreeriderFlagged);
            dto.setTeacherNotes(teacherNotes);

            assessmentDAO.saveOrUpdateAssessment(dto);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Assessment saved successfully for user ID " + userId);
            resp.getWriter().write(gson.toJson(result));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to save assessment: " + e.getMessage());
            resp.getWriter().write(gson.toJson(errorMap));
        }
    }
}
