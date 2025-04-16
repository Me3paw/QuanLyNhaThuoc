package application.view.controller;

import java.io.IOException;

import application.database.TaiKhoanDAO;
import application.model.TaiKhoan;
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

    private TaiKhoanDAO accountDAO = new TaiKhoanDAO();

    // Phương thức xử lý khi người dùng nhấn "Đổi mật khẩu"
    @FXML
    private void handleChangePassword() {
        String username = txtUsername.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Kiểm tra nếu mật khẩu mới và mật khẩu xác nhận khớp
        if (newPassword.equals(confirmPassword)) {
            // Kiểm tra tài khoản có tồn tại trong cơ sở dữ liệu không
            TaiKhoan account = accountDAO.getTaiKhoanByTenDangNhap(username);  // Dùng phương thức getTaiKhoanByTenDangNhap
            if (account != null) {
                // Thực hiện cập nhật mật khẩu
                boolean success = accountDAO.updateTaiKhoanPassword(username, newPassword);  // Sử dụng phương thức cập nhật mật khẩu
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Mật khẩu của bạn đã được thay đổi.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thay đổi mật khẩu. Vui lòng thử lại.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên đăng nhập không tồn tại.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Mật khẩu và nhập lại mật khẩu không khớp.");
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
            Scene loginScene = new Scene(root);
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
