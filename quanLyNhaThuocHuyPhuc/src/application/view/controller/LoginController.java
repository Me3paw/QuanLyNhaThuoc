package application.view.controller;

import application.database.TaiKhoanDAO;
import application.model.TaiKhoan;
import application.util.SessionManager;

import java.io.IOException;

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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogin() {
        System.out.println("handleLogin() called");

        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        System.out.println("Username entered: " + user);
        System.out.println("Password entered: " + pass);

        if (user.isEmpty() || pass.isEmpty()) {
            System.out.println("Username or password is empty");
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            return;
        }

        TaiKhoan taiKhoan = accountDAO.getTaiKhoanByTenDangNhap(user);
        System.out.println("TaiKhoan retrieved: " + (taiKhoan != null ? taiKhoan.getTenDangNhap() : "null"));

        if (taiKhoan == null) {
            System.out.println("Account not found in the system");
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Tài khoản không tồn tại trong hệ thống.");
            return;
        }

        if (!taiKhoan.getMatKhau().equals(pass)) {
            System.out.println("Incorrect password");
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Sai mật khẩu. Vui lòng thử lại.");
            return;
        }

        System.out.println("Login successful, saving user to SessionManager");
        SessionManager.setLoggedInUser(taiKhoan);

        if (SessionManager.getLoggedInNhanVien() == null) {
            System.out.println("NhanVien data not found for the logged-in user");
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy thông tin nhân viên.");
            return;
        }

        try {
            System.out.println("Loading MainLayout.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/MainLayout.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/assets/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Trang chính");
            stage.setScene(scene);
            stage.show();

            System.out.println("Closing current login window");
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            System.out.println("Error loading MainLayout.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("handleForgotPassword() called");

        try {
            System.out.println("Hiding login window");
            Stage loginStage = (Stage) forgotPasswordLink.getScene().getWindow();
            loginStage.hide();

            System.out.println("Loading ForgotPasswordLayout.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/ForgotPasswordLayout.fxml"));
            Parent root = loader.load();

            Scene forgotPasswordScene = new Scene(root);
            forgotPasswordScene.getStylesheets().add(getClass().getResource("/application/assets/css/ForgotPassword.css").toExternalForm());

            Stage forgotPasswordStage = new Stage();
            forgotPasswordStage.setWidth(800);
            forgotPasswordStage.setHeight(600);
            forgotPasswordStage.setScene(forgotPasswordScene);
            forgotPasswordStage.setTitle("Quên Mật Khẩu");
            forgotPasswordStage.show();

        } catch (IOException e) {
            System.out.println("Error loading ForgotPasswordLayout.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
