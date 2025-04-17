package application.view.controller;

import application.database.DatabaseConnector;
import application.database.TaiKhoanDAO;
import application.model.TaiKhoan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Hyperlink forgotPasswordLink;

    private final TaiKhoanDAO accountDAO = new TaiKhoanDAO();

    
    @FXML
    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        TaiKhoan taiKhoan = null;

        // Truy vấn thông tin tài khoản từ bảng taikhoan
        String sql = "SELECT * FROM taikhoan WHERE tenDangNhap = ? AND matKhau = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                taiKhoan = new TaiKhoan();
                taiKhoan.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                taiKhoan.setNgayVaoLam(rs.getDate("ngayVaoLam").toLocalDate());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (taiKhoan != null) {
            // Lấy tên người dùng từ bảng nhanvien thông qua maTaiKhoan
            String tenNguoiDung = accountDAO.getTenNguoiDung(taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/MainLayout.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.updateUserInfo(tenNguoiDung, taiKhoan.getVaiTro());  // Truyền tên người dùng vào đây

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/application/assets/css/style.css").toExternalForm());

                Stage stage = new Stage();
                stage.setTitle("Trang chính");
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) txtUsername.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Sai thông tin đăng nhập!", "Vui lòng kiểm tra lại tên đăng nhập và mật khẩu.");
        }
    }



    private boolean isValidCredentials(String username, String password) {
        TaiKhoan taiKhoan = accountDAO.getTaiKhoanByTenDangNhap(username);
        return taiKhoan != null && taiKhoan.getMatKhau().equals(password);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            // Ẩn màn hình đăng nhập
            Stage loginStage = (Stage) forgotPasswordLink.getScene().getWindow();
            loginStage.hide();  // Ẩn cửa sổ đăng nhập

            // Tải màn hình quên mật khẩu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/ForgotPasswordLayout.fxml"));
            Parent root = loader.load();

            // Tạo cảnh cho màn hình quên mật khẩu
            Scene forgotPasswordScene = new Scene(root);
            
            // Thêm CSS vào cảnh
            forgotPasswordScene.getStylesheets().add(getClass().getResource("/application/assets/css/ForgotPassword.css").toExternalForm());

            // Hiển thị màn hình quên mật khẩu
            Stage forgotPasswordStage = new Stage();
            forgotPasswordStage.setWidth(800);
            forgotPasswordStage.setHeight(600);
            forgotPasswordStage.setScene(forgotPasswordScene);
            forgotPasswordStage.setTitle("Quên Mật Khẩu");
            forgotPasswordStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
}

