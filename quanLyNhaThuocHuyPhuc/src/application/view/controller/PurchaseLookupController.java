package application.view.controller;

import application.database.ChiTietHoaDonDAO;
import application.database.HoaDonDAO;
import application.model.ChiTietHoaDon;
import application.model.HoaDonView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;


import java.util.List;

public class PurchaseLookupController {

    @FXML
    private TextField txtMaHoaDon;

    @FXML
    private TextField txtSDT;

    @FXML
    private TableView<HoaDonView> purchaseTable;

    @FXML
    private TableColumn<HoaDonView, Integer> colSTT;

    @FXML
    private TableColumn<HoaDonView, String> colMaHoaDon;

    @FXML
    private TableColumn<HoaDonView, String> colNgayLap;

    @FXML
    private TableColumn<HoaDonView, String> colTenKH;

    @FXML
    private TableColumn<HoaDonView, String> colSoDienThoai;
    
    @FXML
    private TableColumn<HoaDonView, Void> colAction;

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    @FXML
    public void initialize() {
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
        colNgayLap.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSTT.setCellFactory(col -> new TableCell<HoaDonView, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });


        colAction.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button btn = new Button("Xem chi tiết");
                    btn.setOnAction(event -> {
                        HoaDonView hd = getTableView().getItems().get(getIndex());
                        handleXemChiTiet(hd);
                    });
                    setGraphic(btn);
                }
            }
        });


    }
    private void handleXemChiTiet(HoaDonView hd) {
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        List<ChiTietHoaDon> list = chiTietHoaDonDAO.getChiTietHoaDonByMaHD(hd.getMaHoaDon());

        StringBuilder content = new StringBuilder();
        content.append("Mã Hóa Đơn: ").append(hd.getMaHoaDon()).append("\n");
        content.append("Tên KH: ").append(hd.getTenKH()).append("\n");
        content.append("SĐT: ").append(hd.getSoDienThoai()).append("\n");
        content.append("Ngày Lập: ").append(hd.getNgayLap()).append("\n");
        content.append("-------------------------\n");

        double tongTien = 0;

        for (ChiTietHoaDon ct : list) {
            double thanhTien = ct.getSoLuong() * ct.getDonGia();
            tongTien += thanhTien;

            content.append("Thuốc: ").append(ct.getMaThuoc())
                   .append(" | SL: ").append(ct.getSoLuong())
                   .append(" | Đơn giá: ").append(ct.getDonGia())
                   .append(" | Thành tiền: ").append(thanhTien)
                   .append("\n");
        }

        content.append("-------------------------\n");
        content.append("Tổng tiền: ").append(tongTien).append(" VND");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi Tiết Hóa Đơn");
        alert.setHeaderText("Thông Tin Chi Tiết");
        alert.setContentText(content.toString());
        alert.showAndWait();
    }


    @FXML
    private void handleLookup() {
        String maHoaDon = txtMaHoaDon.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (maHoaDon.isEmpty() && sdt.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Vui lòng nhập Mã Hóa Đơn hoặc SĐT để tìm kiếm!");
            alert.showAndWait();
            return;
        }

        List<HoaDonView> list = hoaDonDAO.lookupHoaDon(maHoaDon, sdt);

        ObservableList<HoaDonView> data = FXCollections.observableArrayList(list);
        purchaseTable.setItems(data);
        colSTT.setCellFactory(col -> new TableCell<HoaDonView, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
    }


}
