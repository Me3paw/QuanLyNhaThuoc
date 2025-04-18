package application.view.controller;

import application.database.TaiKhoanDAO;
import application.model.TaiKhoan;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


import java.util.List;

public class PhanQuyenController {

    @FXML private ComboBox<String> comboUserList; // ComboBox cho người dùng
    @FXML private ComboBox<String> comboRole;     // ComboBox cho vai trò
    @FXML private Button updateRoleButton;
    @FXML private Button saveChangesButton;

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    // Hàm khởi tạo (sử dụng để load dữ liệu ban đầu cho các ComboBox)
    public void initialize() {
        // Load danh sách người dùng từ cơ sở dữ liệu vào comboUserList
        loadUsersToComboBox();
        
        // Load danh sách vai trò vào comboRole (ví dụ: Admin, Nhân viên)
        loadRolesToComboBox();
    }

    // Phương thức để load danh sách người dùng vào ComboBox
    private void loadUsersToComboBox() {
        List<TaiKhoan> taiKhoans = taiKhoanDAO.getAllTaiKhoan(); // Lấy tất cả tài khoản từ cơ sở dữ liệu

        for (TaiKhoan taiKhoan : taiKhoans) {
            // Thêm tên người dùng vào ComboBox (hoặc có thể là username)
            comboUserList.getItems().add(taiKhoan.getTenDangNhap());
        }
    }

    // Phương thức để load danh sách vai trò vào ComboBox
    private void loadRolesToComboBox() {
        comboRole.getItems().add("Admin");
        comboRole.getItems().add("Nhân viên");
        comboRole.getItems().add("Quản lý");
    }

    // Hàm xử lý sự kiện khi bấm nút "Cập nhật quyền"
    @FXML
    public void handleUpdateRole(MouseEvent event) {
        // Kiểm tra các trường bắt buộc không được bỏ trống
        if (comboUserList.getValue() == null || comboRole.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn người dùng và vai trò!", Alert.AlertType.ERROR);
            return;
        }

        String selectedUser = comboUserList.getValue();
        String selectedRole = comboRole.getValue();

        // Thực hiện cập nhật quyền người dùng (phương thức sẽ cần phải được định nghĩa trong DAO)
        // Giả sử bạn có phương thức updateRole trong TaiKhoanDAO
        if (taiKhoanDAO.updateVaiTro(selectedUser, selectedRole)) {
            showAlert("Thành công", "Cập nhật quyền người dùng thành công!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Lỗi", "Cập nhật quyền người dùng không thành công!", Alert.AlertType.ERROR);
        }
    }

    // Hàm hiển thị thông báo lỗi hoặc thông báo thành công
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
