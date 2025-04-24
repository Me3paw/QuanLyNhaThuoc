package application.view.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import application.database.*;
import entity.NhanVien;

import java.time.format.DateTimeFormatter;

public class TimNhanVienController {

    @FXML private TextField txtSearch;
    @FXML private TableView<NhanVien> tableNhanVien;
    @FXML private TableColumn<NhanVien, String> colMaNV;
    @FXML private TableColumn<NhanVien, String> colTenNV;
    @FXML private TableColumn<NhanVien, String> colGioiTinh;
    @FXML private TableColumn<NhanVien, Integer> colNamSinh;
    @FXML private TableColumn<NhanVien, String> colSoDienThoai;
    @FXML private TableColumn<NhanVien, String> colEmail;
    @FXML private TableColumn<NhanVien, Double> colHeSoLuong;
    @FXML private TableColumn<NhanVien, Double> colLuongCoBan;
    @FXML private TableColumn<NhanVien, String> colTenDangNhap;
    @FXML private TableColumn<NhanVien, String> colVaiTro;
    @FXML private TableColumn<NhanVien, String> colNgayVaoLam;
    @FXML private TableColumn<NhanVien, Integer> colCaLam;

    private final NhanVienDAO nhanVienDAO = new NhanVienDAO(); // DAO cho Nhân viên

    @FXML
    public void initialize() {
        // Khởi tạo các cột trong TableView
        setupTableColumns();
        
        // Hiển thị tất cả nhân viên khi bắt đầu
        hienThiTatCa();
    }

    private void setupTableColumns() {
        colMaNV.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getMaNhanVien));
        colTenNV.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getTenNhanVien));
        colGioiTinh.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getGioiTinh));
        colNamSinh.setCellValueFactory(cellData -> Bindings.createObjectBinding(cellData.getValue()::getNamSinh));
        colSoDienThoai.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getSoDienThoai));
        colEmail.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getEmail));
        colHeSoLuong.setCellValueFactory(cellData -> Bindings.createObjectBinding(cellData.getValue()::getHeSoLuong));
        colLuongCoBan.setCellValueFactory(cellData -> Bindings.createObjectBinding(cellData.getValue()::getLuongCoBan));

        colTenDangNhap.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getTaiKhoan().getTenDangNhap()));
        colVaiTro.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getTaiKhoan().getVaiTro()));

        colNgayVaoLam.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> 
                cellData.getValue().getTaiKhoan().getNgayVaoLam().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));
        colCaLam.setCellValueFactory(cellData -> Bindings.createObjectBinding(cellData.getValue()::getCaLam)); // Bind caLam
    }
    private void hienThiTatCa() {
        // Lấy tất cả nhân viên từ database và cập nhật bảng
        ObservableList<NhanVien> danhSach = FXCollections.observableArrayList(nhanVienDAO.getAllNhanVien());
        tableNhanVien.setItems(danhSach);
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm, hiển thị tất cả nhân viên
            hienThiTatCa();
        } else if (keyword.matches("NV-\\d{4}-\\d{3}")) {
            // Nếu từ khóa là mã nhân viên (có dạng NV-2504-008), tìm theo mã nhân viên
            NhanVien nhanVien = nhanVienDAO.getNhanVienByMa(keyword);
            if (nhanVien != null) {
                ObservableList<NhanVien> ketQua = FXCollections.observableArrayList(nhanVien);
                tableNhanVien.setItems(ketQua);
            } else {
                // Nếu không tìm thấy nhân viên theo mã, hiển thị thông báo
                showNoResultsFound();
            }
        } else {
            // Nếu từ khóa là tên nhân viên, tìm kiếm theo tên
            ObservableList<NhanVien> ketQua = FXCollections.observableArrayList(nhanVienDAO.searchNhanVienByName(keyword));
            if (ketQua.isEmpty()) {
                // Nếu không tìm thấy kết quả, hiển thị thông báo
                showNoResultsFound();
            } else {
                tableNhanVien.setItems(ketQua);
            }
        }
    }

    private void showNoResultsFound() {
        // Hiển thị thông báo nếu không tìm thấy kết quả tìm kiếm
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kết quả tìm kiếm");
        alert.setHeaderText(null);
        alert.setContentText("Không tìm thấy nhân viên khớp với từ khóa tìm kiếm.");
        alert.showAndWait();
    }
}
