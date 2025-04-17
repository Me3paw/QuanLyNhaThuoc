package application.view.controller;

import application.database.TaiKhoanDAO;
import application.model.TaiKhoan;

import java.io.IOException;


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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

   @FXML
private void handleLogin() {
    String user = txtUsername.getText();
    String pass = txtPassword.getText();

    if (user.isEmpty() || pass.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        return;
    }

    TaiKhoan taiKhoan = accountDAO.getTaiKhoanByTenDangNhap(user);

    if (taiKhoan == null) {
        showAlert(Alert.AlertType.ERROR, "Lỗi", "Tài khoản không tồn tại trong hệ thống.");
        return;
    }

    if (!taiKhoan.getMatKhau().equals(pass)) {
        showAlert(Alert.AlertType.ERROR, "Lỗi", "Sai mật khẩu. Vui lòng thử lại.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/MainLayout.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.updateUserInfo(
            TaiKhoanDAO.getTenNguoiDung(taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau()), 
            taiKhoan.getVaiTro()
        );

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

