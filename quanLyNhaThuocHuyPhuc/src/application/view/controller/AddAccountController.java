package application.view.controller;

import application.model.TaiKhoan;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;

public class AddAccountController {

//    @FXML
//    private AnchorPane accountForm;
//
//    @FXML
//    private TextField usernameField;
//
//    @FXML
//    private TextField passwordField;
//
//    @FXML
//    private ComboBox<String> roleComboBox;
//
//    @FXML
//    private DatePicker dateOfJoiningPicker;
//
//    @FXML
//    private Button saveButton;
//
//    @FXML
//    private Button cancelButton;
//
//    // Hành động khi nút "Lưu Tài Khoản" được nhấn
//    @FXML
//    private void handleSaveAction() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//        String role = roleComboBox.getValue();
//        LocalDate dateOfJoining = dateOfJoiningPicker.getValue();
//
//        // Kiểm tra nhập liệu
//        if (username.isEmpty() || password.isEmpty() || role == null || dateOfJoining == null) {
//            showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin tài khoản.");
//            return;
//        }
//
//        // Tạo đối tượng Account mới
//        TaiKhoan newAccount = new TaiKhoan(0, username, password, role, dateOfJoining);
//
//        // Gọi DAO để lưu tài khoản
//        boolean isSaved = new application.database.TaiKhoanDAO().addAccount(newAccount);
//
//        if (isSaved) {
//            showAlert("Thông báo", "Tài khoản đã được thêm thành công.");
//            clearForm(); // reset form sau khi lưu
//        } else {
//            showAlert("Lỗi", "Không thể thêm tài khoản. Vui lòng kiểm tra lại.");
//        }
//    }
//
//
//    // Hành động khi nút "Hủy" được nhấn
//    @FXML
//    private void handleCancelAction() {
//        // Xác nhận người dùng có muốn hủy không
//        Alert alert = new Alert(AlertType.CONFIRMATION);
//        alert.setTitle("Hủy thao tác");
//        alert.setHeaderText("Bạn chắc chắn muốn hủy không?");
//        alert.setContentText("Tất cả dữ liệu sẽ bị mất.");
//
//        if (alert.showAndWait().get() == ButtonType.OK) {
//            // Đóng cửa sổ hoặc làm mới form
//            clearForm();
//        }
//    }
//
//    // Hàm để hiển thị thông báo (Alert)
//    private void showAlert(String title, String content) {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    // Hàm làm mới form sau khi lưu hoặc hủy
//    private void clearForm() {
//        usernameField.clear();
//        passwordField.clear();
//        roleComboBox.setValue(null);
//        dateOfJoiningPicker.setValue(null);
//    }
}
