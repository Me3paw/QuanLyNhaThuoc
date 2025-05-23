package application.view.controller;

import application.database.NhanVienDAO;
import application.database.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CapNhatNhanVienController {

    @FXML private TextField maNhanVienTextField;
    @FXML private TextField tenNhanVienTextField;
    @FXML private ComboBox<String> gioiTinhComboBox;
    @FXML private TextField namSinhTextField;
    @FXML private TextField soDienThoaiTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField heSoLuongTextField;
    @FXML private TextField luongCoBanTextField;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> vaiTroComboBox;
    @FXML private DatePicker ngayVaoLamDatePicker;

    private TaiKhoan currentTaiKhoan;

    @FXML
    private void onMaNhanVienEntered() {
        String maNhanVien = maNhanVienTextField.getText();
        if (maNhanVien.isEmpty()) {
            // Nếu không nhập mã nhân viên, thông báo lỗi
            showAlert("Vui lòng nhập mã nhân viên để tìm kiếm.");
            return;
        }
        NhanVien nhanVien = new NhanVienDAO().getNhanVienByMa(maNhanVien);

        if (nhanVien != null) {
            // Đổ dữ liệu vào form
            tenNhanVienTextField.setText(nhanVien.getTenNhanVien());
            gioiTinhComboBox.setValue(nhanVien.getGioiTinh());
            namSinhTextField.setText(String.valueOf(nhanVien.getNamSinh()));
            soDienThoaiTextField.setText(nhanVien.getSoDienThoai());
            emailTextField.setText(nhanVien.getEmail());
            heSoLuongTextField.setText(String.valueOf(nhanVien.getHeSoLuong()));
            luongCoBanTextField.setText(String.valueOf(nhanVien.getLuongCoBan()));

            // Dữ liệu tài khoản
            currentTaiKhoan = nhanVien.getTaiKhoan();
            usernameTextField.setText(currentTaiKhoan.getTenDangNhap());
            passwordField.setText(currentTaiKhoan.getMatKhau());
            vaiTroComboBox.setValue(currentTaiKhoan.getVaiTro());
            ngayVaoLamDatePicker.setValue(currentTaiKhoan.getNgayVaoLam());

            // Không cho sửa mã nhân viên & ngày vào làm
            maNhanVienTextField.setEditable(false);
            ngayVaoLamDatePicker.setDisable(true);
        } else {
            showAlert("Không tìm thấy nhân viên với mã: " + maNhanVien);
        }
    }

   @FXML
private void handleCapNhatNhanVien() {
    try {
        // Validate fields
        if (maNhanVienTextField.getText().isEmpty() ||
            tenNhanVienTextField.getText().isEmpty() ||
            gioiTinhComboBox.getValue() == null ||
            namSinhTextField.getText().isEmpty() ||
            soDienThoaiTextField.getText().isEmpty() ||
            emailTextField.getText().isEmpty() ||
            heSoLuongTextField.getText().isEmpty() ||
            luongCoBanTextField.getText().isEmpty() ||
            usernameTextField.getText().isEmpty() ||
            passwordField.getText().isEmpty() ||
            vaiTroComboBox.getValue() == null) {
            showAlert("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Validate age
        int namSinh = Integer.parseInt(namSinhTextField.getText());
        int currentYear = java.time.Year.now().getValue();
        if (currentYear - namSinh < 18) {
            showAlert("Nhân viên phải đủ 18 tuổi trở lên.");
            return;
        }

        // Lấy thông tin từ form
        String maNhanVien = maNhanVienTextField.getText();
        String tenNhanVien = tenNhanVienTextField.getText();
        String gioiTinh = gioiTinhComboBox.getValue();
        String soDienThoai = soDienThoaiTextField.getText();
        String email = emailTextField.getText();
        double heSoLuong = Double.parseDouble(heSoLuongTextField.getText());
        double luongCoBan = Double.parseDouble(luongCoBanTextField.getText());
        String tenDangNhap = usernameTextField.getText();
        String matKhau = passwordField.getText();
        String vaiTro = vaiTroComboBox.getValue();

        // Cập nhật nhân viên
        NhanVien nhanVien = new NhanVien(
            maNhanVien,
            tenNhanVien,
            gioiTinh,
            namSinh,
            soDienThoai,
            email,
            heSoLuong,
            luongCoBan,
            currentTaiKhoan
        );
        boolean successNV = new NhanVienDAO().updateNhanVien(nhanVien);

        // Cập nhật tài khoản
        currentTaiKhoan.setTenDangNhap(tenDangNhap);
        currentTaiKhoan.setMatKhau(matKhau);
        currentTaiKhoan.setVaiTro(vaiTro);
        boolean successTK = new TaiKhoanDAO().updateTaiKhoan(currentTaiKhoan);

        // Clear fields after update
        maNhanVienTextField.clear();
        tenNhanVienTextField.clear();
        gioiTinhComboBox.setValue(null);
        namSinhTextField.clear();
        soDienThoaiTextField.clear();
        emailTextField.clear();
        heSoLuongTextField.clear();
        luongCoBanTextField.clear();
        usernameTextField.clear();
        passwordField.clear();
        vaiTroComboBox.setValue(null);
        ngayVaoLamDatePicker.setValue(null);
        maNhanVienTextField.setEditable(true);

        if (successNV && successTK) {
            showAlert("Cập nhật nhân viên thành công!");
        } else {
            showAlert("Cập nhật thất bại. Vui lòng kiểm tra dữ liệu.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Đã xảy ra lỗi: " + e.getMessage());
    }
}


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
private void handleLookupNhanVien() {
    onMaNhanVienEntered();
}

}
