package com.aita.gitanalytics.filter;

import com.aita.gitanalytics.util.JWTUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/analytics/*", "/api/assessment/*"})
public class JWTAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Check Authorization header for Bearer Token
        String authHeader = req.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            // Check session attribute fallback
            HttpSession session = req.getSession(false);
            if (session != null) {
                token = (String) session.getAttribute("jwtToken");
            }
        }

        if (token != null && JWTUtil.validateToken(token)) {
            String username = JWTUtil.getUsernameFromToken(token);
            String role = JWTUtil.getRoleFromToken(token);
            Integer userId = JWTUtil.getUserIdFromToken(token);

            req.setAttribute("authenticatedUsername", username);
            req.setAttribute("authenticatedRole", role);
            req.setAttribute("authenticatedUserId", userId);
            
            chain.doFilter(request, response);
            return;
        }

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"error\": \"Unauthorized: Missing or invalid JWT Token\", \"code\": 401}");
    }

    @Override
    public void destroy() {
    }
}
