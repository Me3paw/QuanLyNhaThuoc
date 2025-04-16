package application.view.controller;

import application.database.KhachHangDAO;
import application.model.KhachHang;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CustomerController {

    @FXML private TextField txtHoTen;
    @FXML private TextField txtSoDienThoai;
    @FXML private TextField txtDiaChi;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cboGioiTinh;
    @FXML private TextArea txtGhiChu;
    @FXML private Button btnThemKhachHang;

    private KhachHangDAO khachHangDAO;

    @FXML
    public void initialize() {
        khachHangDAO = new KhachHangDAO();
        cboGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
    }

    @FXML
    private void handleThemKhachHang() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String diaChi = txtDiaChi.getText().trim(); 
        String email = txtEmail.getText().trim();  
        String gioiTinh = cboGioiTinh.getValue();
        String ghiChu = txtGhiChu.getText().trim(); 

        if (hoTen.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Họ và tên không được để trống.");
            txtHoTen.requestFocus();
            return;
        }
        if (sdt.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Số điện thoại không được để trống.");
            txtSoDienThoai.requestFocus();
            return;
        }
         if (gioiTinh == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng chọn giới tính.");
            cboGioiTinh.requestFocus();
            return;
        }

        String maKH = khachHangDAO.generateNextMaKhachHang();
        KhachHang newKH = new KhachHang();
        newKH.setMaKhachHang(maKH);
        newKH.setTenKH(hoTen);
        newKH.setSoDienThoai(sdt);
        newKH.setGioiTinh(gioiTinh);
        newKH.setDiaChi(diaChi);     
        newKH.setEmail(email);      
        newKH.setGhiChu(ghiChu);     

        boolean success = khachHangDAO.insertKhachHang(newKH); 

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm khách hàng mới thành công!");
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể thêm khách hàng. Vui lòng thử lại.");
        }
    }

    private void clearForm() {
        txtHoTen.clear();
        txtSoDienThoai.clear();
        txtDiaChi.clear();
        txtEmail.clear();
        cboGioiTinh.getSelectionModel().clearSelection();
        cboGioiTinh.setPromptText("Chọn giới tính"); 
        txtGhiChu.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); 
        alert.setContentText(content);
        alert.showAndWait();
    }
}