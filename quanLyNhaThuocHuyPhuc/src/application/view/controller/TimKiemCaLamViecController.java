package application.view.controller;

import application.database.CaLamDAO;
import application.model.CaLamViec;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TimKiemCaLamViecController {

    @FXML
    private TableView<CaLamViec> tableView;

    private CaLamDAO caLamDAO;

    public TimKiemCaLamViecController() {
        try {
            // Kết nối đến cơ sở dữ liệu (thay đổi URL, user, password theo hệ thống của bạn)
            Connection conn = DriverManager.getConnection("jdbc:sqlite:PharmacyManagement.db");

            // Khởi tạo CaLamDAO với connection
            this.caLamDAO = new CaLamDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {
            // Lấy danh sách ca làm việc từ database
            List<CaLamViec> danhSachCaLam = caLamDAO.layDanhSach();

            // Hiển thị danh sách lên TableView
            tableView.getItems().setAll(danhSachCaLam);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
