package application.view.controller;

import application.database.ChiTietHoaDonDAO;
import application.database.HoaDonDAO;
import application.model.ChiTietHoaDon;
import application.model.HoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DoanhThuController {

    @FXML
    private ComboBox<String> comboKieuThongKe;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<DoanhThuRow> tableDoanhThu;

    @FXML
    private TableColumn<DoanhThuRow, String> colNgay;

    @FXML
    private TableColumn<DoanhThuRow, Integer> colSoLuong;

    @FXML
    private TableColumn<DoanhThuRow, Double> colTongTien;

    @FXML
    private Label lblTongDoanhThu;

    @FXML
    public void initialize() {
        comboKieuThongKe.setItems(FXCollections.observableArrayList("Theo ngày", "Theo tháng", "Theo năm"));
    }

    @FXML
    private void handleThongKe() {
        String kieuThongKe = comboKieuThongKe.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (kieuThongKe == null || selectedDate == null) {
            showAlert("Vui lòng chọn kiểu thống kê và ngày.");
            return;
        }

        // Lấy danh sách chi tiết hóa đơn và hóa đơn từ cơ sở dữ liệu
        List<ChiTietHoaDon> chiTietList = new ChiTietHoaDonDAO().layTatCaChiTiet();
        List<HoaDon> hoaDonList = new HoaDonDAO().getAllHoaDon();

        // Chuyển danh sách hóa đơn thành map để tìm nhanh theo mã hóa đơn
        Map<String, HoaDon> hoaDonMap = hoaDonList.stream()
                .collect(Collectors.toMap(HoaDon::getMaHoaDon, hd -> hd));

        // Lọc và tính toán doanh thu
        ObservableList<DoanhThuRow> rows = FXCollections.observableArrayList();
        double tongDoanhThu = 0;
        int tongSoLuong = 0;

        for (ChiTietHoaDon cthd : chiTietList) {
            HoaDon hoaDon = hoaDonMap.get(cthd.getMaHoaDon());
            if (hoaDon == null || hoaDon.getNgayLap() == null) continue;

            LocalDate ngayLap = hoaDon.getNgayLap();
            boolean match = false;
            String nhanDang = "";

            switch (kieuThongKe) {
                case "Theo ngày":
                    if (ngayLap.isEqual(selectedDate)) {
                        nhanDang = ngayLap.toString();
                        match = true;
                    }
                    break;
                case "Theo tháng":
                    if (ngayLap.getMonthValue() == selectedDate.getMonthValue() &&
                        ngayLap.getYear() == selectedDate.getYear()) {
                        nhanDang = "Tháng " + selectedDate.getMonthValue() + "/" + selectedDate.getYear();
                        match = true;
                    }
                    break;
                case "Theo năm":
                    if (ngayLap.getYear() == selectedDate.getYear()) {
                        nhanDang = "Năm " + selectedDate.getYear();
                        match = true;
                    }
                    break;
            }

            if (match) {
                double tien = cthd.getSoLuong() * cthd.getDonGia();
                tongDoanhThu += tien;
                tongSoLuong += cthd.getSoLuong();
            }
        }

        // Thêm kết quả vào bảng
        rows.add(new DoanhThuRow(
                kieuThongKe + " " + formatNgay(selectedDate, kieuThongKe),
                tongSoLuong,
                tongDoanhThu
        ));

        tableDoanhThu.setItems(rows);
        lblTongDoanhThu.setText(String.format("%.0f VNĐ", tongDoanhThu));
    }

    private String formatNgay(LocalDate date, String kieu) {
        return switch (kieu) {
            case "Theo ngày" -> date.toString();
            case "Theo tháng" -> date.getMonthValue() + "/" + date.getYear();
            case "Theo năm" -> String.valueOf(date.getYear());
            default -> "";
        };
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Model bảng doanh thu
    public static class DoanhThuRow {
        private final String ngay;
        private final Integer tongSoLuong;
        private final Double tongTien;

        public DoanhThuRow(String ngay, Integer tongSoLuong, Double tongTien) {
            this.ngay = ngay;
            this.tongSoLuong = tongSoLuong;
            this.tongTien = tongTien;
        }

        public String getNgay() {
            return ngay;
        }

        public Integer getTongSoLuong() {
            return tongSoLuong;
        }

        public Double getTongTien() {
            return tongTien;
        }
    }
}
