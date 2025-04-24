package application.view.controller;

import application.database.NhanVienDAO;
import application.database.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;

public class ThemNhanVienController {

    @FXML private TextField maNhanVienTextField;
    @FXML private TextField tenNhanVienTextField;
    @FXML private ComboBox<String> gioiTinhComboBox;
    @FXML private TextField namSinhTextField;
    @FXML private TextField soDienThoaiTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField heSoLuongTextField;
    @FXML private TextField luongCoBanTextField;
    @FXML private TextField maTaiKhoanTextField;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> vaiTroComboBox;
    @FXML private DatePicker ngayVaoLamDatePicker;
    @FXML private Button themNhanVienButton;
    @FXML private Button huyButton;

    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    // Xử lý sự kiện thêm nhân viên
    
    @FXML
    public void handleThemNhanVien(MouseEvent event) {
        // Kiểm tra xem các trường bắt buộc có bị bỏ trống không
        if (tenNhanVienTextField.getText().isEmpty() || gioiTinhComboBox.getValue() == null || 
            namSinhTextField.getText().isEmpty() || soDienThoaiTextField.getText().isEmpty() || 
            emailTextField.getText().isEmpty() || heSoLuongTextField.getText().isEmpty() || 
            luongCoBanTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || 
            passwordField.getText().isEmpty() || vaiTroComboBox.getValue() == null || 
            ngayVaoLamDatePicker.getValue() == null) {

            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin!", Alert.AlertType.ERROR);
            return; // Dừng lại nếu có trường dữ liệu không hợp lệ
        }

        // Kiểm tra năm sinh có hợp lệ không
        int namSinh = 0;
        try {
            namSinh = Integer.parseInt(namSinhTextField.getText());
            if (namSinh < 1900 || namSinh > LocalDate.now().getYear()) {
                showAlert("Lỗi", "Năm sinh không hợp lệ!", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Năm sinh phải là một số!", Alert.AlertType.ERROR);
            return;
        }

        // Kiểm tra số điện thoại có hợp lệ không (ví dụ: chỉ chứa số)
        String soDienThoai = soDienThoaiTextField.getText();
        if (!soDienThoai.matches("[0-9]+") || soDienThoai.length() != 10) {
            showAlert("Lỗi", "Số điện thoại không hợp lệ!", Alert.AlertType.ERROR);
            return;
        }

        // Kiểm tra email có hợp lệ không
        String email = emailTextField.getText();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Lỗi", "Email không hợp lệ!", Alert.AlertType.ERROR);
            return;
        }

        // Kiểm tra hệ số lương và lương cơ bản có phải là số không
        double heSoLuong = 0, luongCoBan = 0;
        try {
            heSoLuong = Double.parseDouble(heSoLuongTextField.getText());
            luongCoBan = Double.parseDouble(luongCoBanTextField.getText());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Hệ số lương và lương cơ bản phải là số!", Alert.AlertType.ERROR);
            return;
        }

        // Thông tin tài khoản
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String vaiTro = vaiTroComboBox.getValue();
        LocalDate ngayVaoLam = ngayVaoLamDatePicker.getValue();

        // Tạo đối tượng TaiKhoan
        TaiKhoan taiKhoan = new TaiKhoan(username, password, vaiTro, ngayVaoLam);

        // Tạo đối tượng NhanVien
        NhanVien nhanVien = new NhanVien(maNhanVienTextField.getText(),tenNhanVienTextField.getText(), 
                                         gioiTinhComboBox.getValue(), namSinh, 
                                         soDienThoai, email, heSoLuong, luongCoBan, taiKhoan, (Integer) null);

        // Thêm tài khoản vào cơ sở dữ liệu
        if (taiKhoanDAO.addTaiKhoan(taiKhoan)) {
            // Thêm nhân viên vào cơ sở dữ liệu 
            if (nhanVienDAO.addNhanVien(nhanVien)) {
                showAlert("Thành công", "Thêm nhân viên thành công!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Lỗi", "Thêm nhân viên không thành công!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Lỗi", "Thêm tài khoản không thành công!", Alert.AlertType.ERROR);
        }
    }


    // Xử lý sự kiện hủy
    @FXML
    public void handleHuy(MouseEvent event) {
        // Reset các trường nhập liệu
        maNhanVienTextField.clear();
        tenNhanVienTextField.clear();
        gioiTinhComboBox.getSelectionModel().clearSelection();
        namSinhTextField.clear();
        soDienThoaiTextField.clear();
        emailTextField.clear();
        heSoLuongTextField.clear();
        luongCoBanTextField.clear();
        usernameTextField.clear();
        passwordField.clear();
        vaiTroComboBox.getSelectionModel().clearSelection();
        ngayVaoLamDatePicker.setValue(null);
    }

    // Hàm hiển thị thông báo
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
