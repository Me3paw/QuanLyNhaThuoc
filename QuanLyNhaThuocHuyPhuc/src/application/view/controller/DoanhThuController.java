package application.view.controller;

import application.database.ChiTietHoaDonDAO;
import application.database.KhachHangDAO;
import application.database.NhanVienDAO;
import application.database.ThuocDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class DoanhThuController {

    @FXML
    private Label thuocKhoValueLabel;

    @FXML
    private Label khachHangValueLabel;

    @FXML
    private Label nhanVienValueLabel;

    @FXML
    private BarChart<String, Number> doanhThuChart;

    @FXML
    private LineChart<String, Number> loiNhuanChart;

    @FXML
    private ComboBox<String> doanhThuYearCombo;

    @FXML
    private ComboBox<String> loiNhuanYearCombo;

    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

    @FXML
    public void initialize() {
        // Load KPI values
        loadKPIValues();

        // Load charts
        loadDoanhThuChart();
        loadLoiNhuanChart();

        // Set up year combo boxe
    }

    private void loadKPIValues() {
        // Count all Thuốc
        int thuocCount = thuocDAO.getAllThuoc().size();
        thuocKhoValueLabel.setText(String.valueOf(thuocCount));

        // Count all Khách Hàng
        int khachHangCount = khachHangDAO.getAllKhachHang().size();
        khachHangValueLabel.setText(String.valueOf(khachHangCount));

        // Count all Nhân Viên
        int nhanVienCount = nhanVienDAO.getAllNhanVien().size();
        nhanVienValueLabel.setText(String.valueOf(nhanVienCount));
    }

    private void loadDoanhThuChart() {
    // Calculate total revenue and cost
    double thuNhap = chiTietHoaDonDAO.getTotalRevenue();
    double chiPhi = thuocDAO.getTotalCost();

    // Update Bar Chart
    doanhThuChart.getData().clear();
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.getData().add(new XYChart.Data<>("Thu nhập", thuNhap));
    series.getData().add(new XYChart.Data<>("Chi phí", chiPhi));
    doanhThuChart.getData().add(series);
}

private void loadLoiNhuanChart() {
    // Calculate total revenue, cost, and profit
    double thuNhap = chiTietHoaDonDAO.getTotalRevenue();
    double chiPhi = thuocDAO.getTotalCost();
    double loiNhuan = thuNhap - chiPhi;

    // Update Line Chart
    loiNhuanChart.getData().clear();
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.getData().add(new XYChart.Data<>("Thu nhập", thuNhap));
    series.getData().add(new XYChart.Data<>("Lợi nhuận", loiNhuan));
    loiNhuanChart.getData().add(series);
}

}
