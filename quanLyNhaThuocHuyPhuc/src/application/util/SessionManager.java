package application.util;

import application.database.NhanVienDAO;
import application.model.TaiKhoan;
import application.model.NhanVien;

public class SessionManager {
    private static TaiKhoan loggedInUser;
    private static NhanVien loggedInNhanVien;

    public static void setLoggedInUser(TaiKhoan user) {
        loggedInUser = user;

        // Debug: Log the TaiKhoan object
        System.out.println("SessionManager: Setting loggedInUser: " + (user != null ? user.getTenDangNhap() : "null"));
        System.out.println("DEBUG MaTaiKhoan: " + user.getMaTaiKhoan());
        // Load corresponding NhanVien data based on TaiKhoan
        NhanVienDAO nhanVienDAO = new NhanVienDAO();

        loggedInNhanVien = nhanVienDAO.getNhanVienByTaiKhoan(user.getMaTaiKhoan());

        // Debug: Log the NhanVien object
        System.out.println("SessionManager: Retrieved loggedInNhanVien: " + 
            (loggedInNhanVien != null ? loggedInNhanVien.getTenNhanVien() : "null"));
    }

    public static TaiKhoan getLoggedInUser() {
        return loggedInUser;
    }

    public static NhanVien getLoggedInNhanVien() {
        return loggedInNhanVien;
    }

    public static void clearSession() {
        loggedInUser = null;
        loggedInNhanVien = null;
    }
}
