package com.aita.gitanalytics.dao;

import com.aita.gitanalytics.dto.UserDTO;

import java.util.List;
import java.util.Scanner;

public class InsertUserDemo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in, "UTF-8");
        UserDAO userDAO = new UserDAO();

        while (true) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("          USER MANAGEMENT");
            System.out.println("======================================");
            System.out.println("1. Thêm user");
            System.out.println("2. Xem full danh sách");
            System.out.println("3. Sửa user");
            System.out.println("4. Xóa user");
            System.out.println("5. Thoát");
            System.out.println("======================================");

            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            // =========================================
            // 1. THÊM USER
            // =========================================
            if (choice.equals("1")) {

                System.out.println();
                System.out.println(
                        "=== NHẬP THÔNG TIN NGƯỜI DÙNG MỚI ==="
                );

                System.out.print("Nhập Họ và tên: ");
                String fullName = scanner.nextLine();

                System.out.print("Nhập Email: ");
                String email = scanner.nextLine();

                System.out.print("Nhập Github Username: ");
                String githubUsername = scanner.nextLine();

                try {

                    int newUserId = userDAO.createUser(
                            fullName,
                            email,
                            githubUsername
                    );

                    if (newUserId != -1) {

                        System.out.println();
                        System.out.println(
                                "Thêm user thành công!"
                        );

                        System.out.println(
                                "ID mới tạo: " + newUserId
                        );

                    } else {

                        System.out.println(
                                "Thêm user thất bại!"
                        );
                    }

                } catch (Exception e) {

                    System.out.println();
                    System.out.println(
                            "Thêm user thất bại!"
                    );

                    System.out.println(
                            "Lý do: " + e.getMessage()
                    );
                }
            }

            // =========================================
            // 2. XEM FULL DANH SÁCH
            // =========================================
            else if (choice.equals("2")) {

                try {

                    List<UserDTO> users =
                            userDAO.getAllUsers();

                    System.out.println();
                    System.out.println(
                            "============== FULL USER LIST =============="
                    );

                    if (users.isEmpty()) {

                        System.out.println(
                                "Chưa có user nào."
                        );

                    } else {

                        for (UserDTO user : users) {

                            System.out.println(
                                    "ID: "
                                    + user.getUserId()
                                    + " | Họ tên: "
                                    + user.getFullName()
                                    + " | Email: "
                                    + user.getEmail()
                                    + " | Github: "
                                    + user.getGithubUsername()
                            );
                        }
                    }

                    System.out.println(
                            "============================================"
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Không thể lấy danh sách user!"
                    );

                    System.out.println(
                            "Lý do: " + e.getMessage()
                    );
                }
            }

            // =========================================
            // 3. SỬA USER
            // =========================================
            else if (choice.equals("3")) {

                try {

                    System.out.print(
                            "Nhập ID user muốn sửa: "
                    );

                    int userId = Integer.parseInt(
                            scanner.nextLine()
                    );

                    UserDTO user =
                            userDAO.getUserById(userId);

                    if (user == null) {

                        System.out.println(
                                "Không tìm thấy user!"
                        );

                        continue;
                    }

                    System.out.println();
                    System.out.println(
                            "=== THÔNG TIN HIỆN TẠI ==="
                    );

                    System.out.println(
                            "Họ tên: "
                            + user.getFullName()
                    );

                    System.out.println(
                            "Email: "
                            + user.getEmail()
                    );

                    System.out.println(
                            "Github: "
                            + user.getGithubUsername()
                    );

                    System.out.println();
                    System.out.println(
                            "=== NHẬP THÔNG TIN MỚI ==="
                    );

                    System.out.print(
                            "Nhập Họ và tên mới: "
                    );

                    String fullName =
                            scanner.nextLine();

                    System.out.print(
                            "Nhập Email mới: "
                    );

                    String email =
                            scanner.nextLine();

                    System.out.print(
                            "Nhập Github Username mới: "
                    );

                    String githubUsername =
                            scanner.nextLine();

                    boolean updated =
                            userDAO.updateUser(
                                    userId,
                                    fullName,
                                    email,
                                    githubUsername
                            );

                    if (updated) {

                        System.out.println();
                        System.out.println(
                                "Sửa user thành công!"
                        );

                    } else {

                        System.out.println(
                                "Không tìm thấy user để sửa!"
                        );
                    }

                } catch (NumberFormatException e) {

                    System.out.println(
                            "ID phải là số!"
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Sửa user thất bại!"
                    );

                    System.out.println(
                            "Lý do: " + e.getMessage()
                    );
                }
            }

            // =========================================
            // 4. XÓA USER
            // =========================================
            else if (choice.equals("4")) {

                try {

                    System.out.print(
                            "Nhập ID user muốn xóa: "
                    );

                    int userId = Integer.parseInt(
                            scanner.nextLine()
                    );

                    UserDTO user =
                            userDAO.getUserById(userId);

                    if (user == null) {

                        System.out.println(
                                "Không tìm thấy user!"
                        );

                        continue;
                    }

                    System.out.println();
                    System.out.println(
                            "User sẽ bị xóa:"
                    );

                    System.out.println(
                            "ID: "
                            + user.getUserId()
                    );

                    System.out.println(
                            "Họ tên: "
                            + user.getFullName()
                    );

                    System.out.println(
                            "Email: "
                            + user.getEmail()
                    );

                    System.out.print(
                            "Bạn có chắc muốn xóa? (Y/N): "
                    );

                    String confirm =
                            scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {

                        boolean deleted =
                                userDAO.deleteUser(userId);

                        if (deleted) {

                            System.out.println();
                            System.out.println(
                                    "Xóa user thành công!"
                            );

                        } else {

                            System.out.println(
                                    "Xóa user thất bại!"
                            );
                        }

                    } else {

                        System.out.println(
                                "Đã hủy xóa."
                        );
                    }

                } catch (NumberFormatException e) {

                    System.out.println(
                            "ID phải là số!"
                    );

                } catch (Exception e) {

                    System.out.println();
                    System.out.println(
                            "Xóa user thất bại!"
                    );

                    System.out.println(
                            "Lý do: " + e.getMessage()
                    );
                }
            }

            // =========================================
            // 5. THOÁT
            // =========================================
            else if (choice.equals("5")) {

                System.out.println();
                System.out.println(
                        "Đã thoát chương trình."
                );

                break;
            }

            // =========================================
            // LỰA CHỌN SAI
            // =========================================
            else {

                System.out.println();
                System.out.println(
                        "Lựa chọn không hợp lệ!"
                );
            }
        }

        scanner.close();
    }
}