package com.aita.gitanalytics.controller;

import com.aita.gitanalytics.dto.UserDTO;
import com.aita.gitanalytics.service.JWTAuthService;
import com.aita.gitanalytics.util.JWTUtil;
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

@WebServlet(urlPatterns = {"/login", "/api/auth/login"})
public class AuthServlet extends HttpServlet {

    private final JWTAuthService authService = new JWTAuthService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameParam = req.getParameter("username");
        String passwordParam = req.getParameter("password");

        // Handle JSON Body if form parameters are null
        if (usernameParam == null || passwordParam == null) {
            try {
                Map<String, String> body = gson.fromJson(req.getReader(), new com.google.gson.reflect.TypeToken<Map<String, String>>(){}.getType());
                if (body != null) {
                    usernameParam = body.get("username");
                    passwordParam = body.get("password");
                }
            } catch (Exception e) {
                // Keep parameters as null
            }
        }

        if (usernameParam == null || passwordParam == null || usernameParam.trim().isEmpty()) {
            sendError(req, resp, "Username and password are required.");
            return;
        }

        try {
            UserDTO user = authService.authenticateUser(usernameParam, passwordParam);
            if (user != null) {
                String token = JWTUtil.generateToken(user.getUsername(), user.getRole(), user.getUserId());
                
                // Store in session for JSP web browsing
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("jwtToken", token);

                String acceptHeader = req.getHeader("Accept");
                if (acceptHeader != null && acceptHeader.contains("application/json")) {
                    resp.setContentType("application/json;charset=UTF-8");
                    Map<String, Object> jsonMap = new HashMap<>();
                    jsonMap.put("token", token);
                    jsonMap.put("username", user.getUsername());
                    jsonMap.put("fullName", user.getFullName());
                    jsonMap.put("role", user.getRole());
                    jsonMap.put("userId", user.getUserId());
                    resp.getWriter().write(gson.toJson(jsonMap));
                } else {
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
                }
            } else {
                sendError(req, resp, "Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(req, resp, "Server error during authentication: " + e.getMessage());
        }
    }

    private void sendError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        String acceptHeader = req.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json;charset=UTF-8");
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", message);
            resp.getWriter().write(gson.toJson(errorMap));
        } else {
            req.setAttribute("errorMessage", message);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
