-- =============================================================================
-- AITA System - Group 5: Git Analytics Teamwork Assessor (FE-L-03)
-- Database DDL & Seed Data Script (Tuần 1 - Tuần 3)
-- Compatible with PostgreSQL & H2 Database
-- =============================================================================

DROP TABLE IF EXISTS contribution_assessments CASCADE;
DROP TABLE IF EXISTS author_metrics CASCADE;
DROP TABLE IF EXISTS commit_logs CASCADE;
DROP TABLE IF EXISTS group_members CASCADE;
DROP TABLE IF EXISTS "groups" CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 1. Table Users
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT', -- 'LECTURER', 'STUDENT', 'ADMIN'
    github_username VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Table Courses
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    semester VARCHAR(20) NOT NULL
);

-- 3. Table Groups
CREATE TABLE "groups" (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    group_name VARCHAR(50) NOT NULL,
    git_repo_url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Table Group Members
CREATE TABLE group_members (
    group_id INT NOT NULL REFERENCES "groups"(group_id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    role_in_group VARCHAR(20) DEFAULT 'MEMBER', -- 'LEADER', 'MEMBER'
    PRIMARY KEY (group_id, user_id)
);

-- 5. Table Commit Logs
CREATE TABLE commit_logs (
    commit_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL REFERENCES "groups"(group_id) ON DELETE CASCADE,
    user_id INT REFERENCES users(user_id) ON DELETE SET NULL,
    commit_hash VARCHAR(40) NOT NULL,
    commit_message TEXT NOT NULL,
    additions INT NOT NULL DEFAULT 0,
    deletions INT NOT NULL DEFAULT 0,
    total_churn INT NOT NULL DEFAULT 0,
    commit_date TIMESTAMP NOT NULL
);

-- 6. Table Author Metrics (ICI Calculated Scores)
CREATE TABLE author_metrics (
    metric_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL REFERENCES "groups"(group_id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    total_commits INT NOT NULL DEFAULT 0,
    total_additions INT NOT NULL DEFAULT 0,
    total_deletions INT NOT NULL DEFAULT 0,
    active_days INT NOT NULL DEFAULT 1,
    ici_score DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    is_freerider_flagged BOOLEAN NOT NULL DEFAULT FALSE,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Table Contribution Assessments (Teacher Overrides & Final Evaluation)
CREATE TABLE contribution_assessments (
    assessment_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL REFERENCES groups(group_id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    final_score DOUBLE PRECISION NOT NULL DEFAULT 10.0,
    is_freerider_flagged BOOLEAN NOT NULL DEFAULT FALSE,
    teacher_notes TEXT,
    assessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for Query Performance Optimization
CREATE INDEX idx_commit_logs_group_user ON commit_logs(group_id, user_id);
CREATE INDEX idx_author_metrics_group ON author_metrics(group_id);
CREATE INDEX idx_users_github ON users(github_username);

-- Seed Data (Password for all users: 'password123' -> SHA-256: ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f)
INSERT INTO users (username, password_hash, full_name, email, role, github_username) VALUES
('teacher_john', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Dr. John Doe', 'john.doe@fe.edu.vn', 'LECTURER', 'johndoe_lecturer'),
('student_alice', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Alice Nguyen', 'alice.n@fe.edu.vn', 'STUDENT', 'alicenguyen_dev'),
('student_bob', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Bob Tran', 'bob.t@fe.edu.vn', 'STUDENT', 'bobtran_dev'),
('student_charlie', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Charlie Le', 'charlie.l@fe.edu.vn', 'STUDENT', 'charliele_lazy');

INSERT INTO courses (course_code, course_name, semester) VALUES 
('PRJ301', 'Java Web Application Development', 'Fall 2026');

INSERT INTO "groups" (course_id, group_name, git_repo_url) VALUES 
(1, 'Group 5 - Git Analytics', 'https://github.com/prj301-group5/aita-git-analytics');

INSERT INTO group_members (group_id, user_id, role_in_group) VALUES 
(1, 2, 'LEADER'),
(1, 3, 'MEMBER'),
(1, 4, 'MEMBER');

-- Sample Commits for Alice (Leader - High Contribution)
INSERT INTO commit_logs (group_id, user_id, commit_hash, commit_message, additions, deletions, total_churn, commit_date) VALUES
(1, 2, 'a1b2c3d4e5f67890123456789012345678901234', 'feat(gitanalytics): initial project structure setup', 450, 10, 460, '2026-09-14 09:00:00'),
(1, 2, 'b2c3d4e5f678901234567890123456789012345a', 'feat(auth): implement JWT Authentication Filter & DAO', 890, 120, 1010, '2026-09-15 14:30:00'),
(1, 2, 'c3d4e5f678901234567890123456789012345a6b', 'feat(service): implement ICI calculation algorithm', 1200, 350, 1550, '2026-09-17 11:15:00'),
(1, 2, 'd4e5f678901234567890123456789012345a6b7c', 'feat(ui): design Lecturer Analytics Dashboard JSP', 910, 720, 1630, '2026-09-20 16:45:00');

-- Sample Commits for Bob (Member - Active Contribution)
INSERT INTO commit_logs (group_id, user_id, commit_hash, commit_message, additions, deletions, total_churn, commit_date) VALUES
(1, 3, 'e5f678901234567890123456789012345a6b7c8d', 'feat(db): write SQL DDL scripts and ERD design', 650, 50, 700, '2026-09-14 10:30:00'),
(1, 3, 'f678901234567890123456789012345a6b7c8d9e', 'feat(dao): add GitCommitDAO PreparedStatement methods', 850, 250, 1100, '2026-09-16 13:00:00'),
(1, 3, '78901234567890123456789012345a6b7c8d9e0f', 'fix(auth): resolve token validation null check', 600, 500, 1100, '2026-09-19 18:20:00');

-- Sample Commits for Charlie (Free-rider - Minimal Contribution)
INSERT INTO commit_logs (group_id, user_id, commit_hash, commit_message, additions, deletions, total_churn, commit_date) VALUES
(1, 4, '8901234567890123456789012345a6b7c8d9e0f1', 'docs: update readme text file', 50, 10, 60, '2026-09-14 11:00:00');

-- Seed Initial ICI Metrics
INSERT INTO author_metrics (group_id, user_id, total_commits, total_additions, total_deletions, active_days, ici_score, is_freerider_flagged) VALUES
(1, 2, 4, 3450, 1200, 4, 52.4, FALSE),
(1, 3, 3, 2100, 800, 3, 38.1, FALSE),
(1, 4, 1, 50, 10, 1, 9.5, TRUE);
