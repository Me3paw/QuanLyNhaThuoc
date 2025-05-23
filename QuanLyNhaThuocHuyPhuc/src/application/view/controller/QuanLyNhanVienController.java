package application.view.controller;

import application.database.NhanVienDAO;
import application.database.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.List;

public class QuanLyNhanVienController {

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

    @FXML private Button themNhanVienButton;
    @FXML private Button xoaNhanVienButton;
    @FXML private Button suaNhanVienButton;
    @FXML private Button timNhanVienButton;

    @FXML private TableView<NhanVien> nhanVienTable;
    @FXML private TableColumn<NhanVien, String> maNhanVienCol;
    @FXML private TableColumn<NhanVien, String> tenNhanVienCol;
    @FXML private TableColumn<NhanVien, String> gioiTinhCol;
    @FXML private TableColumn<NhanVien, Integer> namSinhCol;
    @FXML private TableColumn<NhanVien, String> soDienThoaiCol;
    @FXML private TableColumn<NhanVien, String> emailCol;
    @FXML private TableColumn<NhanVien, Double> heSoLuongCol;
    @FXML private TableColumn<NhanVien, Double> luongCoBanCol;
    @FXML private TableColumn<NhanVien, LocalDate> ngayVaoLamCol;
    @FXML private TableColumn<NhanVien, String> usernameCol;
    @FXML private TableColumn<NhanVien, String> passwordCol;
    @FXML private TableColumn<NhanVien, String> vaiTroCol;

    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    private ObservableList<NhanVien> nhanVienList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Khởi tạo ComboBox (nếu cần) - thường đã set sẵn trong FXML

        // Thiết lập các cột cho TableView
        maNhanVienCol.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        tenNhanVienCol.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        gioiTinhCol.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        namSinhCol.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
        soDienThoaiCol.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        heSoLuongCol.setCellValueFactory(new PropertyValueFactory<>("heSoLuong"));
        luongCoBanCol.setCellValueFactory(new PropertyValueFactory<>("luongCoBan"));
        ngayVaoLamCol.setCellValueFactory(cellData -> {
            LocalDate ngayVaoLam = null;
            if(cellData.getValue().getTaiKhoan() != null) {
                ngayVaoLam = cellData.getValue().getTaiKhoan().getNgayVaoLam();
            }
            return new javafx.beans.property.SimpleObjectProperty<>(ngayVaoLam);

        });
        usernameCol.setCellValueFactory(cellData -> {
            String username = "";
            if(cellData.getValue().getTaiKhoan() != null) {
                username = cellData.getValue().getTaiKhoan().getTenDangNhap();
            }
            return javafx.beans.property.SimpleStringProperty.stringExpression(javafx.beans.binding.Bindings.createStringBinding(() -> username));
        });
        passwordCol.setCellValueFactory(cellData -> {
            String password = "";
            if(cellData.getValue().getTaiKhoan() != null) {
                password = cellData.getValue().getTaiKhoan().getMatKhau();
            }
            return javafx.beans.property.SimpleStringProperty.stringExpression(javafx.beans.binding.Bindings.createStringBinding(() -> password));
        });
        vaiTroCol.setCellValueFactory(cellData -> {
            String vaiTro = "";
            if(cellData.getValue().getTaiKhoan() != null) {
                vaiTro = cellData.getValue().getTaiKhoan().getVaiTro();
            }
            return javafx.beans.property.SimpleStringProperty.stringExpression(javafx.beans.binding.Bindings.createStringBinding(() -> vaiTro));
        });

        // Load dữ liệu ban đầu
        loadNhanVien();

        // Khi chọn 1 nhân viên trên bảng thì hiển thị dữ liệu lên form
        nhanVienTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                hienThiChiTietNhanVien(newSelection);
            }
        });
    }

    // Load dữ liệu nhân viên từ DB
    private void loadNhanVien() {
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        nhanVienList.setAll(list);
        nhanVienTable.setItems(nhanVienList);
    }

    // Hiển thị chi tiết nhân viên lên form
    private void hienThiChiTietNhanVien(NhanVien nv) {
        tenNhanVienTextField.setText(nv.getTenNhanVien());
        gioiTinhComboBox.setValue(nv.getGioiTinh());
        namSinhTextField.setText(String.valueOf(nv.getNamSinh()));
        soDienThoaiTextField.setText(nv.getSoDienThoai());
        emailTextField.setText(nv.getEmail());
        heSoLuongTextField.setText(String.valueOf(nv.getHeSoLuong()));
        luongCoBanTextField.setText(String.valueOf(nv.getLuongCoBan()));
        if(nv.getTaiKhoan() != null) {
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

    // Xử lý sự kiện thêm nhân viên
    @FXML
    public void handleThemNhanVien(MouseEvent event) {
        // Tương tự như ThemNhanVienController
        if (tenNhanVienTextField.getText().isEmpty() || gioiTinhComboBox.getValue() == null ||
            namSinhTextField.getText().isEmpty() || soDienThoaiTextField.getText().isEmpty() ||
            emailTextField.getText().isEmpty() || heSoLuongTextField.getText().isEmpty() ||
            luongCoBanTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() ||
            passwordField.getText().isEmpty() || vaiTroComboBox.getValue() == null ||
            ngayVaoLamDatePicker.getValue() == null) {

            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin!", Alert.AlertType.ERROR);
            return;
        }

        int namSinh;
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

        String soDienThoai = soDienThoaiTextField.getText();
        if (!soDienThoai.matches("\\d{10}")) {
            showAlert("Lỗi", "Số điện thoại không hợp lệ! (Phải đủ 10 chữ số)", Alert.AlertType.ERROR);
            return;
        }

        String email = emailTextField.getText();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Lỗi", "Email không hợp lệ!", Alert.AlertType.ERROR);
            return;
        }

        double heSoLuong, luongCoBan;
        try {
            heSoLuong = Double.parseDouble(heSoLuongTextField.getText());
            luongCoBan = Double.parseDouble(luongCoBanTextField.getText());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Hệ số lương và lương cơ bản phải là số!", Alert.AlertType.ERROR);
            return;
        }

        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String vaiTro = vaiTroComboBox.getValue();
        LocalDate ngayVaoLam = ngayVaoLamDatePicker.getValue();

        TaiKhoan taiKhoan = new TaiKhoan(username, password, vaiTro, ngayVaoLam);

        String maNhanVien = nhanVienDAO.generateMaNhanVienTuDong();

        NhanVien nhanVien = new NhanVien(maNhanVien, tenNhanVienTextField.getText(),
                gioiTinhComboBox.getValue(), namSinh,
                soDienThoai, email, heSoLuong, luongCoBan, taiKhoan, 0);

        if (taiKhoanDAO.addTaiKhoan(taiKhoan)) {
            if (nhanVienDAO.addNhanVien(nhanVien)) {
                showAlert("Thành công", "Thêm nhân viên thành công!", Alert.AlertType.INFORMATION);
                loadNhanVien();
                clearForm();
            } else {
                showAlert("Lỗi", "Thêm nhân viên không thành công!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Lỗi", "Thêm tài khoản không thành công!", Alert.AlertType.ERROR);
        }
    }

    // Xử lý sự kiện xóa nhân viên
    @FXML
    public void handleXoaNhanVien(MouseEvent event) {
        NhanVien selected = nhanVienTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Vui lòng chọn nhân viên để xóa!", Alert.AlertType.ERROR);
            return;
        }

        boolean success = nhanVienDAO.deleteNhanVien(selected.getMaNhanVien());
        if (success) {
            if (selected.getTaiKhoan() != null) {
                taiKhoanDAO.deleteTaiKhoan(selected.getTaiKhoan().getMaTaiKhoan());
            }
            showAlert("Thành công", "Xóa nhân viên thành công!", Alert.AlertType.INFORMATION);
            loadNhanVien();
            clearForm();
        } else {
            showAlert("Lỗi", "Xóa nhân viên không thành công!", Alert.AlertType.ERROR);
        }
    }

    // Xử lý sự kiện sửa nhân viên
    @FXML
    public void handleSuaNhanVien(MouseEvent event) {
        NhanVien selected = nhanVienTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Vui lòng chọn nhân viên để sửa!", Alert.AlertType.ERROR);
            return;
        }

        // Kiểm tra dữ liệu tương tự thêm nhân viên
        if (tenNhanVienTextField.getText().isEmpty() || gioiTinhComboBox.getValue() == null ||
            namSinhTextField.getText().isEmpty() || soDienThoaiTextField.getText().isEmpty() ||
            emailTextField.getText().isEmpty() || heSoLuongTextField.getText().isEmpty() ||
            luongCoBanTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() ||
            passwordField.getText().isEmpty() || vaiTroComboBox.getValue() == null ||
            ngayVaoLamDatePicker.getValue() == null) {

            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin!", Alert.AlertType.ERROR);
            return;
        }

        int namSinh;
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

        String soDienThoai = soDienThoaiTextField.getText();
        if (!soDienThoai.matches("\\d{10}")) {
            showAlert("Lỗi", "Số điện thoại không hợp lệ! (Phải đủ 10 chữ số)", Alert.AlertType.ERROR);
            return;
        }

        String email = emailTextField.getText();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Lỗi", "Email không hợp lệ!", Alert.AlertType.ERROR);
            return;
        }

        double heSoLuong, luongCoBan;
        try {
            heSoLuong = Double.parseDouble(heSoLuongTextField.getText());
            luongCoBan = Double.parseDouble(luongCoBanTextField.getText());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Hệ số lương và lương cơ bản phải là số!", Alert.AlertType.ERROR);
            return;
        }

        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String vaiTro = vaiTroComboBox.getValue();
        LocalDate ngayVaoLam = ngayVaoLamDatePicker.getValue();

        TaiKhoan taiKhoan = new TaiKhoan(username, password, vaiTro, ngayVaoLam);

        NhanVien nhanVien = new NhanVien(selected.getMaNhanVien(), tenNhanVienTextField.getText(),
                gioiTinhComboBox.getValue(), namSinh,
                soDienThoai, email, heSoLuong, luongCoBan, taiKhoan, 0);

        // Cập nhật tài khoản trước
        boolean taiKhoanUpdated = taiKhoanDAO.updateTaiKhoan(taiKhoan);

        // Cập nhật nhân viên
        boolean nhanVienUpdated = nhanVienDAO.updateNhanVien(nhanVien);

        if (taiKhoanUpdated && nhanVienUpdated) {
            showAlert("Thành công", "Cập nhật nhân viên thành công!", Alert.AlertType.INFORMATION);
            loadNhanVien();
            clearForm();
        } else {
            showAlert("Lỗi", "Cập nhật nhân viên không thành công!", Alert.AlertType.ERROR);
        }
    }

    // Xử lý sự kiện tìm nhân viên (tìm theo tên)
    @FXML
    public void handleTimNhanVien(MouseEvent event) {
        String keyword = tenNhanVienTextField.getText().trim();
        if (keyword.isEmpty()) {
            loadNhanVien();
            return;
        }
        List<NhanVien> found = nhanVienDAO.searchNhanVienByName(keyword);
        nhanVienList.setAll(found);
        nhanVienTable.setItems(nhanVienList);
    }

    // Xóa trắng form
    private void clearForm() {
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

    // Hiển thị cảnh báo
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
