# AITA System - Group 5: Git Analytics Teamwork Assessor (FE-L-03)

## 📌 Project Overview
Module **Git Analytics Teamwork Assessor (FE-L-03)** của **Nhóm 5** thuộc Hệ sinh thái **AITA (AI-powered Teaching Assistant System)** cho môn học **PRJ301 (Java Web Application Development)**.

Dự án giải quyết bài toán kiệt sức của giảng viên (**Teacher Burnout**) bằng cách tự động thu thập lịch sử Git, tính toán **Chỉ số Đóng góp Cá nhân (Individual Contribution Index - ICI)**, và gắn cờ cảnh báo rực đỏ cho các trường hợp sinh viên **Free-rider** ($ICI < 15\%$).

---

## 🧮 Công thức Thuật toán ICI Score
$$ICI_i = w_1 \cdot C_i + w_2 \cdot L_i + w_3 \cdot T_i + w_4 \cdot F_i$$

Trong đó:
- $w_1 = 0.25$ (Trọng số Commits ratio)
- $w_2 = 0.45$ (Trọng số LOC Churn ratio: $\Delta \text{LOC} = \text{Additions} + 0.5 \times \text{Deletions}$)
- $w_3 = 0.15$ (Trọng số Active Days Time Spread ratio)
- $w_4 = 0.15$ (Trọng số File Ownership ratio)

---

## 📁 Cấu trúc Project (Chuẩn MVC2 Architecture & Maven)
```
PRJ301-Nh-m-6/
├── pom.xml
├── sql/
│   └── schema.sql                  # Database DDL & Seed Data
├── Dockerfile                      # Sandbox Docker Deployment (École 42 Style)
├── docker-compose.yml
├── src/main/java/com/aita/gitanalytics/
│   ├── controller/                 # Servlet Controllers (Auth, Dashboard, Analytics, Assessment, Logout)
│   ├── dao/                        # JDBC Data Access Objects with PreparedStatements
│   ├── dto/                        # Data Transfer Objects
│   ├── filter/                     # EncodingFilter & JWTAuthFilter
│   ├── service/                    # ScoreCalculatorService, GitParserService, JWTAuthService
│   └── util/                       # DBUtil (auto-H2 seed), JWTUtil, PasswordUtil
├── src/main/webapp/
│   ├── login.jsp                   # Glassmorphic Login Page
│   ├── css/main.css                # Dark Mode Glassmorphism Style
│   ├── js/analytics-chart.js       # AJAX & Modal Handlers
│   └── WEB-INF/
│       ├── web.xml
│       └── views/
│           ├── dashboard-lecturer.jsp
│           ├── student-contribution.jsp
│           └── error.jsp
└── src/test/java/com/aita/gitanalytics/
    └── service/ScoreCalculatorServiceTest.java
```

---

## 🚀 Hướng dẫn Chạy & Kiểm thử Dự án

### 1. Chạy với Maven Local / Embedded Tomcat (Khuyên dùng)
```bash
mvn clean compile test
```
Hoặc package thành `.war`:
```bash
mvn clean package
```

### 2. Tài khoản Demo kiểm thử:
- **Giảng viên (Lecturer)**: `teacher_john` / `password123`
- **Sinh viên Nhóm trưởng**: `student_alice` / `password123`
- **Sinh viên Free-rider**: `student_charlie` / `password123`

---

## 🛡️ Peer-Review 1 Checklist (École 42 Style)
- [x] **Sandbox Compliance**: Chạy biệt lập trong Docker Sandbox (`docker-compose up`).
- [x] **MVC2 & Clean Code**: Phân chia Controller, DAO (JDBC PreparedStatement), DTO, Service, View JSP rõ ràng.
- [x] **Peer Defense Ready**: Sẵn sàng bảo vệ ERD, thuật toán ICI và cảnh báo Free-rider.
