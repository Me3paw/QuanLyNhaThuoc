package application.view.controller;

import application.database.NhanVienDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class QuanLyNhanVienController {

    @FXML private TextField tenNhanVienTextField;
    @FXML private ComboBox<String> gioiTinhComboBox;
    @FXML private TextField namSinhTextField;
    @FXML private TextField soDienThoaiTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField heSoLuongTextField;
    @FXML private TextField luongCoBanTextField;
    @FXML private DatePicker ngayVaoLamDatePicker;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> vaiTroComboBox;

    @FXML private TableView<NhanVien> nhanVienTable;
    @FXML private TableColumn<NhanVien, String> maNhanVienCol;
    @FXML private TableColumn<NhanVien, String> tenNhanVienCol;
    @FXML private TableColumn<NhanVien, String> gioiTinhCol;
    @FXML private TableColumn<NhanVien, Integer> namSinhCol;
    @FXML private TableColumn<NhanVien, String> soDienThoaiCol;
    @FXML private TableColumn<NhanVien, String> emailCol;
    @FXML private TableColumn<NhanVien, Double> heSoLuongCol;
    @FXML private TableColumn<NhanVien, Double> luongCoBanCol;
    @FXML private TableColumn<NhanVien, String> usernameCol;
    @FXML private TableColumn<NhanVien, String> passwordCol;
    @FXML private TableColumn<NhanVien, String> vaiTroCol;
    @FXML private TableColumn<NhanVien, LocalDate> ngayVaoLamCol;

    private final ObservableList<NhanVien> nhanVienList = FXCollections.observableArrayList();
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    @FXML
    private void initialize() {
        gioiTinhComboBox.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        vaiTroComboBox.setItems(FXCollections.observableArrayList("Admin", "Nhân viên"));

        // Set cell value factories
        maNhanVienCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaNhanVien()));
        tenNhanVienCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTenNhanVien()));
        gioiTinhCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGioiTinh()));
        namSinhCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNamSinh()).asObject());
        soDienThoaiCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSoDienThoai()));
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        heSoLuongCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getHeSoLuong()).asObject());
        luongCoBanCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getLuongCoBan()).asObject());
        usernameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTaiKhoan().getTenDangNhap()));
        passwordCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTaiKhoan().getMatKhau()));
        vaiTroCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTaiKhoan().getVaiTro()));
        ngayVaoLamCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTaiKhoan().getNgayVaoLam()));

        nhanVienTable.setItems(nhanVienList);

        loadNhanVienFromDB();
        
        nhanVienTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showNhanVienDetails(newSelection);
            }
        });
    }

    private void loadNhanVienFromDB() {
        nhanVienList.clear();
        List<NhanVien> ds = nhanVienDAO.getAllNhanVien();
        if (ds != null) {
            nhanVienList.addAll(ds);
        }
        passwordCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);
                if (empty || password == null) {
                    setText(null);
                } else {
                    // Hiển thị dấu chấm cho mỗi ký tự
                    setText("•".repeat(password.length()));
                }
            }
        });
    }

    @FXML
    private void handleThemNhanVien() {
        try {
        	// Kiểm tra dữ liệu trước
            if (!checkTen(tenNhanVienTextField.getText())
                    || !checkNamSinh(namSinhTextField.getText())
                    || !checkSoDienThoai(soDienThoaiTextField.getText())
                    || !checkEmail(emailTextField.getText())
                    || !checkHeSoLuong(heSoLuongTextField.getText())
                    || !checkLuongCoBan(luongCoBanTextField.getText())
                    || !checkTenDangNhap(usernameTextField.getText())
                    || !checkMatKhau(passwordField.getText())
                    || !checkVaiTro(vaiTroComboBox.getValue())
                    || !checkNgayVaoLam(ngayVaoLamDatePicker.getValue())) {
                return; // Dừng lại nếu có lỗi
            }

            NhanVien nv = new NhanVien();
            // Mã nhân viên sẽ được sinh tự động trong DAO
            nv.setTenNhanVien(tenNhanVienTextField.getText());
            nv.setGioiTinh(gioiTinhComboBox.getValue());
            nv.setNamSinh(Integer.parseInt(namSinhTextField.getText()));
            nv.setSoDienThoai(soDienThoaiTextField.getText());
            nv.setEmail(emailTextField.getText());
            nv.setHeSoLuong(Double.parseDouble(heSoLuongTextField.getText()));
            nv.setLuongCoBan(Double.parseDouble(luongCoBanTextField.getText()));

            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(usernameTextField.getText());
            tk.setMatKhau(passwordField.getText());
            tk.setVaiTro(vaiTroComboBox.getValue());
            tk.setNgayVaoLam(ngayVaoLamDatePicker.getValue());
            tk.setNhanVien(nv);
            nv.setTaiKhoan(tk);

            boolean added = nhanVienDAO.addNhanVien(nv);
            if (added) {
                loadNhanVienFromDB();
                clearFields();
                showAlert("Thành công", "Thêm nhân viên thành công.");
            } else {
                showAlert("Lỗi", "Thêm nhân viên thất bại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi nhập dữ liệu", "Vui lòng kiểm tra lại các trường thông tin.");
        }
    }

    @FXML
    private void handleLamMoi() {
    	clearFields();
    	loadNhanVienFromDB();
    }

    @FXML
    private void handleSuaNhanVien() {
        NhanVien selected = nhanVienTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
            	if (!checkTen(tenNhanVienTextField.getText())
                        || !checkNamSinh(namSinhTextField.getText())
                        || !checkSoDienThoai(soDienThoaiTextField.getText())
                        || !checkEmail(emailTextField.getText())
                        || !checkHeSoLuong(heSoLuongTextField.getText())
                        || !checkLuongCoBan(luongCoBanTextField.getText())
                        || !checkTenDangNhap(usernameTextField.getText())
                        || !checkMatKhau(passwordField.getText())
                        || !checkVaiTro(vaiTroComboBox.getValue())
                        || !checkNgayVaoLam(ngayVaoLamDatePicker.getValue())) {
                    return; // Dừng lại nếu có lỗi
                }
                selected.setTenNhanVien(tenNhanVienTextField.getText());
                selected.setGioiTinh(gioiTinhComboBox.getValue());
                selected.setNamSinh(Integer.parseInt(namSinhTextField.getText()));
                selected.setSoDienThoai(soDienThoaiTextField.getText());
                selected.setEmail(emailTextField.getText());
                selected.setHeSoLuong(Double.parseDouble(heSoLuongTextField.getText()));
                selected.setLuongCoBan(Double.parseDouble(luongCoBanTextField.getText()));

                TaiKhoan tk = selected.getTaiKhoan();
                tk.setTenDangNhap(usernameTextField.getText());
                tk.setMatKhau(passwordField.getText());
                tk.setVaiTro(vaiTroComboBox.getValue());
                tk.setNgayVaoLam(ngayVaoLamDatePicker.getValue());

                boolean updated = nhanVienDAO.updateNhanVien(selected);
                if (updated) {
                    loadNhanVienFromDB();
                    clearFields();
                    showAlert("Thành công", "Cập nhật nhân viên thành công.");
                } else {
                    showAlert("Lỗi", "Cập nhật nhân viên thất bại.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi nhập dữ liệu", "Vui lòng kiểm tra lại các trường thông tin.");
            }
        } else {
            showAlert("Chưa chọn nhân viên", "Vui lòng chọn nhân viên để sửa.");
        }
    }

    @FXML
    private void handleTimNhanVien() {
        String keyword = tenNhanVienTextField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            showAlert("Tìm kiếm", "Vui lòng nhập tên nhân viên cần tìm.");
            return;
        }

        List<NhanVien> ds = nhanVienDAO.searchNhanVienByName(keyword);
        if (ds != null && !ds.isEmpty()) {
            nhanVienList.clear();
            nhanVienList.addAll(ds);
            nhanVienTable.getSelectionModel().select(0);
            nhanVienTable.scrollTo(0);
        } else {
            showAlert("Không tìm thấy", "Không có nhân viên nào trùng khớp.");
        }
    }

    private void clearFields() {
        tenNhanVienTextField.clear();
        gioiTinhComboBox.getSelectionModel().clearSelection();
        namSinhTextField.clear();
        soDienThoaiTextField.clear();
        emailTextField.clear();
        heSoLuongTextField.clear();
        luongCoBanTextField.clear();
        ngayVaoLamDatePicker.setValue(null);
        usernameTextField.clear();
        passwordField.clear();
        vaiTroComboBox.getSelectionModel().clearSelection();
        tenNhanVienTextField.requestFocus();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showNhanVienDetails(NhanVien nv) {
        tenNhanVienTextField.setText(nv.getTenNhanVien());
        gioiTinhComboBox.setValue(nv.getGioiTinh());
        namSinhTextField.setText(String.valueOf(nv.getNamSinh()));
        soDienThoaiTextField.setText(nv.getSoDienThoai());
        emailTextField.setText(nv.getEmail());
        heSoLuongTextField.setText(String.valueOf(nv.getHeSoLuong()));
        luongCoBanTextField.setText(String.valueOf(nv.getLuongCoBan()));
        if (nv.getTaiKhoan() != null) {
            usernameTextField.setText(nv.getTaiKhoan().getTenDangNhap());
            passwordField.setText(nv.getTaiKhoan().getMatKhau());
            vaiTroComboBox.setValue(nv.getTaiKhoan().getVaiTro());
            ngayVaoLamDatePicker.setValue(nv.getTaiKhoan().getNgayVaoLam());
        } else {
            usernameTextField.clear();
            passwordField.clear();
            vaiTroComboBox.getSelectionModel().clearSelection();
            ngayVaoLamDatePicker.setValue(null);
        }
    }
 // Check tên nhân viên
    public boolean checkTen(String ten) {
        if (ten == null || !ten.matches("^([\\p{Lu}][\\p{Ll}]+)(\\s[\\p{Lu}][\\p{Ll}]*)*$")) {
            showAlert("Lỗi tên", "Tên nhân viên không hợp lệ. Vui lòng viết hoa chữ cái đầu mỗi từ.");
            return false;
        }
        return true;
    }

    // Check năm sinh (phải là số, tuổi lớn hơn hoặc bằng 18)
    public boolean checkNamSinh(String namSinhStr) {
        try {
            int namSinh = Integer.parseInt(namSinhStr);
            int currentYear = LocalDate.now().getYear();
            int age = currentYear - namSinh;

            if (age < 18) {
                showAlert("Lỗi tuổi", "Nhân viên phải từ 18 tuổi trở lên.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Lỗi năm sinh", "Năm sinh phải là số.");
            return false;
        }
    }

    // Check số điện thoại (10 chữ số, bắt đầu bằng 0)
    public boolean checkSoDienThoai(String sdt) {
        if (sdt == null || !sdt.matches("^0\\d{9}$")) {
            showAlert("Lỗi số điện thoại", "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0.");
            return false;
        }
        return true;
    }

    // Check email
    public boolean checkEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Lỗi email", "Email không hợp lệ.");
            return false;
        }
        return true;
    }

    // Check hệ số lương (phải là số dương)
    public boolean checkHeSoLuong(String heSoStr) {
        try {
            double heSo = Double.parseDouble(heSoStr);
            if (heSo <= 0) {
                showAlert("Lỗi hệ số lương", "Hệ số lương phải lớn hơn 0.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Lỗi hệ số lương", "Hệ số lương phải là số.");
            return false;
        }
    }

    // Check lương cơ bản (phải là số dương)
    public boolean checkLuongCoBan(String luongStr) {
        try {
            double luong = Double.parseDouble(luongStr);
            if (luong <= 0) {
                showAlert("Lỗi lương cơ bản", "Lương cơ bản phải lớn hơn 0.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Lỗi lương cơ bản", "Lương cơ bản phải là số.");
            return false;
        }
    }

    // Check tên đăng nhập (không rỗng, ít nhất 4 ký tự)
    public boolean checkTenDangNhap(String username) {
        if (username == null || username.trim().length() < 4) {
            showAlert("Lỗi tên đăng nhập", "Tên đăng nhập phải có ít nhất 4 ký tự.");
            return false;
        }
        return true;
    }

    // Check mật khẩu (ít nhất 6 ký tự, có chữ và số)
    public boolean checkMatKhau(String password) {
        if (password == null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) {
            showAlert("Lỗi mật khẩu", "Mật khẩu phải có ít nhất 6 ký tự bao gồm chữ và số.");
            return false;
        }
        return true;
    }

    // Check vai trò (phải được chọn)
    public boolean checkVaiTro(String vaiTro) {
        if (vaiTro == null || vaiTro.isEmpty()) {
            showAlert("Lỗi vai trò", "Vui lòng chọn vai trò.");
            return false;
        }
        return true;
    }

    // Check ngày vào làm (phải được chọn, không vượt quá hôm nay)
    public boolean checkNgayVaoLam(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            showAlert("Lỗi ngày vào làm", "Ngày vào làm không được để trống hoặc sau hôm nay.");
            return false;
        }
        return true;
    }

    
    
}
