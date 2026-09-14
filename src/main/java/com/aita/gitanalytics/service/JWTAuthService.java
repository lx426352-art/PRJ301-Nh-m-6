package com.aita.gitanalytics.service;

import com.aita.gitanalytics.dao.UserDAO;
import com.aita.gitanalytics.dto.UserDTO;
import com.aita.gitanalytics.util.JWTUtil;

import java.sql.SQLException;

public class JWTAuthService {

    private final UserDAO userDAO = new UserDAO();

    public String loginAndGenerateToken(String username, String password) throws SQLException {
        UserDTO user = userDAO.authenticate(username, password);
        if (user != null) {
            return JWTUtil.generateToken(user.getUsername(), user.getRole(), user.getUserId());
        }
        return null;
    }

    public UserDTO authenticateUser(String username, String password) throws SQLException {
        return userDAO.authenticate(username, password);
    }
}
