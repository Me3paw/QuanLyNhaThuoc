package application.controller;

import application.database.TaiKhoanDAO;
import application.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ChangeAccountController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Label statusLabel;

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    @FXML
    public void initialize() {
        // Pre-fill the username field with the logged-in user's username
        if (SessionManager.getLoggedInUser() != null) {
            usernameField.setText(SessionManager.getLoggedInUser().getTenDangNhap());
        }
    }

	  @FXML
	private void handleChangePassword() {
	    String username = usernameField.getText();
	    String currentPassword = currentPasswordField.getText();
	    String newPassword = newPasswordField.getText();
	
	    if (username.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
	        statusLabel.setText("Tất cả các trường đều bắt buộc.");
	        return;
	    }
	
	    // Verify current password
	    if (!taiKhoanDAO.getTaiKhoanByTenDangNhap(username).getMatKhau().equals(currentPassword)) {
	        statusLabel.setText("Mật khẩu hiện tại không chính xác.");
	        return;
	    }
	
	    // Update the password
	    boolean success = taiKhoanDAO.updateTaiKhoanPassword(username, newPassword);
	    if (success) {
	        statusLabel.setText("Cập nhật mật khẩu thành công.");
	        statusLabel.setStyle("-fx-text-fill: green;");
	    } else {
	        statusLabel.setText("Cập nhật mật khẩu thất bại.");
	    }
	}

}
