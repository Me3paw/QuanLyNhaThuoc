package application.view.controller;

import java.io.IOException;

import application.database.NhanVienDAO;
import application.database.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController {

    @FXML
    private TextField txtUsername;  // Ô nhập tài khoản
    @FXML
    private PasswordField txtNewPassword;  // Ô nhập mật khẩu mới
    @FXML
    private PasswordField txtConfirmPassword;  // Ô nhập lại mật khẩu
    @FXML
    private TextField txtSDT;


    // Phương thức xử lý khi người dùng nhấn "Đổi mật khẩu"
    @FXML
    private void handleChangePassword() {
	    String username = txtUsername.getText();
	    String newPassword = txtNewPassword.getText();
	    String confirmPassword = txtConfirmPassword.getText();
	    String sdt = txtSDT.getText(); // Get the SDT input from the user
	
	    // Validate if the username is provided
	    if (username == null || username.trim().isEmpty()) {
	        showAlert(Alert.AlertType.ERROR, "Error", "Vui lòng nhập tài khoản.");
	        return;
	    }
	
	    // Validate if the new password and confirm password match
	    if (!newPassword.equals(confirmPassword)) {
	        showAlert(Alert.AlertType.ERROR, "Error", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
	        return;
	    }
	
	    // Retrieve the NhanVien associated with the username
	    NhanVienDAO nhanVienDAO = new NhanVienDAO();
	    TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
	    TaiKhoan account = taiKhoanDAO.getTaiKhoanByTenDangNhap(username);
	
	    if (account == null) {
	        showAlert(Alert.AlertType.ERROR, "Error", "Tài khoản không tồn tại.");
	        return;
	    }
	
	    NhanVien nhanVien = nhanVienDAO.getNhanVienByTaiKhoan(account.getMaTaiKhoan());
	
	    // Validate if the phone number matches
	    if (nhanVien == null || !nhanVien.getSoDienThoai().equals(sdt)) {
	        showAlert(Alert.AlertType.ERROR, "Error", "Số điện thoại không khớp với tài khoản.");
	        return;
	    }
	
	    // Update the password if validation passes
	    boolean success = taiKhoanDAO.updateTaiKhoanPassword(username, newPassword);
	    if (success) {
	        showAlert(Alert.AlertType.INFORMATION, "Success", "Mật khẩu đã được đổi thành công.");
	        txtUsername.clear();
	        txtNewPassword.clear();
	        txtConfirmPassword.clear();
	        txtSDT.clear();
	        txtUsername.requestFocus();
	    } else {
	        showAlert(Alert.AlertType.ERROR, "Error", "Không thể đổi mật khẩu. Vui lòng thử lại sau.");
	    }
	}


    
    // Hiển thị thông báo cho người dùng
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Quay lại màn hình đăng nhập khi người dùng nhấn nút "Quay lại Đăng nhập"
     */
    @FXML
    private void handleBackToLogin() {
        // Lấy stage hiện tại (màn hình quên mật khẩu)
        Stage currentStage = (Stage) txtUsername.getScene().getWindow();

        // Tải lại màn hình đăng nhập (Login.fxml)
        try {
            // Tạo FXMLLoader để tải FXML cho màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Login.fxml"));
            Parent root = loader.load();
            
            // Tạo một cảnh mới với màn hình đăng nhập
            Scene loginScene = new Scene(root,800,600);
            loginScene.getStylesheets().add(getClass().getResource("/application/assets/css/Login.css").toExternalForm());
            
            // Thiết lập và hiển thị cửa sổ đăng nhập
            currentStage.setScene(loginScene);
            currentStage.setTitle("Đăng nhập");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở màn hình đăng nhập.");
        }
    }
}
