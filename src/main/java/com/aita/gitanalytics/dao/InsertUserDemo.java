package com.aita.gitanalytics.dao;

import java.util.Scanner;

public class InsertUserDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        UserDAO userDAO = new UserDAO();

        System.out.println("=== NHẬP THÔNG TIN NGƯỜI DÙNG MỚI ===");
        System.out.print("Nhập Họ và tên: ");
        String fullName = scanner.nextLine();

        System.out.print("Nhập Email: ");
        String email = scanner.nextLine();

        System.out.print("Nhập Github Username: ");
        String githubUsername = scanner.nextLine();

        try {
            int newUserId = userDAO.createUser(fullName, email, githubUsername);
            if (newUserId != -1) {
                System.out.println("\n Thêm user thành công! ID mới tạo: " + newUserId);
            } else {
                System.out.println("\n Thêm user thất bại!");
            }
        } catch (Exception e) {
            System.err.println("\n Thêm user thất bại!");
            System.err.println("Lý do: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}