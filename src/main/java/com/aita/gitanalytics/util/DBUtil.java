package com.aita.gitanalytics.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    // =========================
    // SQL SERVER CONFIGURATION
    // =========================

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=aita_db;"
            + "encrypt=false;"
            + "trustServerCertificate=true";

    private static final String USER = "sa";

    // ĐỔI PASSWORD NÀY thành password sa của SQL Server
    private static final String PASSWORD = "123";

    // =========================
    // LOAD SQL SERVER DRIVER
    // =========================

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Server JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    // =========================
    // GET CONNECTION
    // =========================

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    // =========================
    // CREATE TABLES
    // =========================

    public static void initDatabaseSchema(Connection conn)
            throws SQLException {

        try (Statement stmt = conn.createStatement()) {

            // =========================
            // USERS
            // =========================

            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects "
                    + "WHERE name='users' AND xtype='U') "
                    + "CREATE TABLE users ("
                    + "user_id INT IDENTITY(1,1) PRIMARY KEY, "
                    + "full_name VARCHAR(100) NOT NULL, "
                    + "email VARCHAR(100) UNIQUE NOT NULL, "
                    + "role VARCHAR(20) NOT NULL DEFAULT 'STUDENT', "
                    + "github_username VARCHAR(50) NOT NULL, "
                    + "created_at DATETIME DEFAULT GETDATE()"
                    + ")"
            );

            // =========================
            // COURSES
            // =========================

            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects "
                    + "WHERE name='courses' AND xtype='U') "
                    + "CREATE TABLE courses ("
                    + "course_id INT IDENTITY(1,1) PRIMARY KEY, "
                    + "course_code VARCHAR(20) NOT NULL, "
                    + "course_name VARCHAR(100) NOT NULL, "
                    + "semester VARCHAR(20) NOT NULL"
                    + ")"
            );

            // =========================
            // GROUPS
            // =========================

            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects "
                    + "WHERE name='groups' AND xtype='U') "
                    + "CREATE TABLE groups ("
                    + "group_id INT IDENTITY(1,1) PRIMARY KEY, "
                    + "course_id INT NOT NULL, "
                    + "group_name VARCHAR(50) NOT NULL, "
                    + "git_repo_url VARCHAR(255) NOT NULL, "
                    + "created_at DATETIME DEFAULT GETDATE()"
                    + ")"
            );

            // =========================
            // GROUP MEMBERS
            // =========================

            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects "
                    + "WHERE name='group_members' AND xtype='U') "
                    + "CREATE TABLE group_members ("
                    + "group_id INT NOT NULL, "
                    + "user_id INT NOT NULL, "
                    + "role_in_group VARCHAR(20) DEFAULT 'MEMBER', "
                    + "PRIMARY KEY (group_id, user_id)"
                    + ")"
            );

            // =========================
            // COMMIT LOGS
            // =========================

            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects "
                    + "WHERE name='commit_logs' AND xtype='U') "
                    + "CREATE TABLE commit_logs ("
                    + "commit_id INT IDENTITY(1,1) PRIMARY KEY, "
                    + "group_id INT NOT NULL, "
                    + "user_id INT, "
                    + "commit_hash VARCHAR(40) NOT NULL, "
                    + "commit_message VARCHAR(MAX) NOT NULL, "
                    + "additions INT NOT NULL DEFAULT 0, "
                    + "deletions INT NOT NULL DEFAULT 0, "
                    + "total_churn INT NOT NULL DEFAULT 0, "
                    + "commit_date DATETIME NOT NULL"
                    + ")"
            );

            // =========================
            // AUTHOR METRICS
            // =========================

            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects "
                    + "WHERE name='author_metrics' AND xtype='U') "
                    + "CREATE TABLE author_metrics ("
                    + "metric_id INT IDENTITY(1,1) PRIMARY KEY, "
                    + "group_id INT NOT NULL, "
                    + "user_id INT NOT NULL, "
                    + "total_commits INT NOT NULL DEFAULT 0, "
                    + "total_additions INT NOT NULL DEFAULT 0, "
                    + "total_deletions INT NOT NULL DEFAULT 0, "
                    + "active_days INT NOT NULL DEFAULT 1, "
                    + "ici_score FLOAT NOT NULL DEFAULT 0.0, "
                    + "is_freerider_flagged BIT NOT NULL DEFAULT 0, "
                    + "calculated_at DATETIME DEFAULT GETDATE()"
                    + ")"
            );

            // =========================
            // CONTRIBUTION ASSESSMENTS
            // =========================

            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects "
                    + "WHERE name='contribution_assessments' AND xtype='U') "
                    + "CREATE TABLE contribution_assessments ("
                    + "assessment_id INT IDENTITY(1,1) PRIMARY KEY, "
                    + "group_id INT NOT NULL, "
                    + "user_id INT NOT NULL, "
                    + "final_score FLOAT NOT NULL DEFAULT 10.0, "
                    + "is_freerider_flagged BIT NOT NULL DEFAULT 0, "
                    + "teacher_notes VARCHAR(MAX), "
                    + "assessed_at DATETIME DEFAULT GETDATE()"
                    + ")"
            );
        }
    }
}