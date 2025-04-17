package application.view.controller;

import application.database.CaLamDAO;
import application.model.CaLamViec;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ChiaCaLamViecController {

    @FXML
    private TableView<CaLamViec> tableView;

    @FXML
    private Button btnChiaCa;

    private CaLamDAO caLamDAO;

    public ChiaCaLamViecController() {
        try {
            // Kết nối đến cơ sở dữ liệu (thay đổi URL, user, password theo hệ thống của bạn)
            Connection conn = DriverManager.getConnection("jdbc:sqlite:PharmacyManagement.db");

            // Khởi tạo CaLamDAO với connection
            this.caLamDAO = new CaLamDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Lỗi kết nối cơ sở dữ liệu", "Không thể kết nối đến cơ sở dữ liệu.");
        }
    }

    public void initialize() {
        try {
            // Lấy danh sách ca làm việc từ database và hiển thị lên TableView
            List<CaLamViec> danhSachCaLam = caLamDAO.layDanhSach();
            tableView.getItems().setAll(danhSachCaLam);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Lỗi truy vấn dữ liệu", "Không thể lấy danh sách ca làm việc.");
        }
    }

    @FXML
    private void chiaCaLam() {
        try {
            // Thực hiện chia ca làm việc hoặc các hành động khác liên quan
            // Ví dụ: Gọi phương thức để thêm hoặc cập nhật ca làm việc trong cơ sở dữ liệu
            // CaLamViec caMoi = new CaLamViec(...);
            // caLamDAO.themCa(caMoi);

            showSuccessAlert("Chia ca thành công", "Ca làm việc đã được thêm thành công.");
        } catch (Exception e) {
            // Sử dụng Exception thay vì SQLException vì có thể có các lỗi khác
            e.printStackTrace();
            showErrorAlert("Lỗi thao tác", "Có lỗi xảy ra khi chia ca làm việc.");
        }
    }

    // Hiển thị thông báo lỗi
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hiển thị thông báo thành công
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
