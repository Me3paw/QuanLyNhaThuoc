package application.view.controller;

import application.database.ThuocDAO;
import application.database.ChiTietHoaDonDAO;
import application.database.HoaDonDAO;
import application.database.KhachHangDAO;
import application.util.SessionManager;
import entity.CartItem;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.Thuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class POSController {

    @FXML private TextField txtMaThuocInput;
    @FXML private ImageView imgDrug;
    @FXML private Label lblDrugName;
    @FXML private Label lblDrugID;
    @FXML private TextArea txtThanhPhan;
    @FXML private TextField txtGiaTien;
    @FXML private TextField txtQuantity;
    @FXML private Button btnDecrease;
    @FXML private Button btnIncrease;
    @FXML private TableView<CartItem> cartTable;
    @FXML private TextField txtTongTien;
    @FXML private TextField txtTienDua;
    @FXML private TextField txtTienThua;
    @FXML private TextField txtMaHoaDon;
    @FXML private TextField txtSDT;
    @FXML private TextField txtTenKH;
    @FXML private ComboBox<String> cboGioiTinh;


    private Thuoc currentThuoc;
    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ObservableList<CartItem> cartData = FXCollections.observableArrayList();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private int quantity = 1;
    private String maHoaDon;
    @FXML
    public void initialize() {
        cartTable.setItems(cartData);
        setSoLuong();
        maHoaDon = hoaDonDAO.generateNextMaHoaDon();
        txtMaHoaDon.setText(maHoaDon);
        txtTienDua.textProperty().addListener((observable, oldValue, newValue) -> {
            updateTienThua();
        });
    }

    @FXML
    private void handleHuyDon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/MainLayout.fxml"));
            Parent root = loader.load();
            txtMaHoaDon.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleLookupKH() {
        String sdt = txtSDT.getText().trim();

        if (sdt.isEmpty()) return;

        KhachHang kh = khachHangDAO.getKhachHangBySDT(sdt);

        if (kh == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Khách Hàng");
            alert.setHeaderText("Không tìm thấy khách hàng này!");
            alert.setContentText("Bạn có muốn nhập mới không?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Allow input
                txtTenKH.setEditable(true);
                cboGioiTinh.setDisable(false);
                txtTenKH.clear();
                cboGioiTinh.getSelectionModel().selectFirst();
            } else {
                // Do nothing, stay here to change số điện thoại
                return;
            }
            return;
        }

        // Auto fill if found
        txtTenKH.setText(kh.getTenKH());
        cboGioiTinh.getSelectionModel().select(kh.getGioiTinh());

        txtTenKH.setEditable(false);
        cboGioiTinh.setDisable(true);
    }

 @FXML
private void handleCheckout() {
    if (cartData.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Giỏ hàng đang trống!");
        alert.showAndWait();
        return;
    }

    double tienThua;
    try {
        tienThua = Double.parseDouble(txtTienThua.getText());
    } catch (NumberFormatException e) {
        tienThua = 0; // Default to 0 if parsing fails
    }

    if (tienThua < 0) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Số tiền khách đưa không đủ để thanh toán!");
        alert.showAndWait();
        return;
    }

    String sdt = txtSDT.getText().trim();
    String tenKH = txtTenKH.getText().trim();
    KhachHang kh = (sdt.isEmpty()) ? null : khachHangDAO.getKhachHangBySDT(sdt);
    String maKH;

    if (kh == null) {
        if (!sdt.isEmpty()) {
            // Insert new Khách Hàng
            maKH = khachHangDAO.generateNextMaKhachHang();
            KhachHang newKH = new KhachHang(maKH, tenKH, cboGioiTinh.getValue(), sdt);
            khachHangDAO.insertKhachHang(newKH);
        } else {
            maKH = null; // No customer information
        }
    } else {
        maKH = kh.getMaKhachHang();
    }

    // Retrieve logged-in NhanVien from SessionManager
    String maNhanVien = SessionManager.getLoggedInNhanVien() != null
            ? SessionManager.getLoggedInNhanVien().getMaNhanVien()
            : "Unknown";

    // Create and insert HoaDon
    HoaDon hd = new HoaDon(maHoaDon, LocalDate.now(), maKH, maNhanVien);
    hoaDonDAO.insertHoaDon(hd);

    // Insert ChiTietHoaDon
    ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    for (CartItem item : cartData) {
        ChiTietHoaDon cthd = new ChiTietHoaDon(
                hd.getMaHoaDon(),
                item.getId(),
                item.getQuantity(),
                item.getPrice()
        );
        chiTietHoaDonDAO.insertChiTietHoaDon(cthd);

        // Update soLuongTon in Thuoc
        Thuoc thuoc = thuocDAO.getThuocByID(item.getId());
        if (thuoc != null) {
            int updatedSoLuongTon = thuoc.getSoLuongTon() - item.getQuantity();
            thuoc.setSoLuongTon(updatedSoLuongTon);
            thuocDAO.updateThuoc(thuoc);
        }
    }

    // Show success alert with "In Hóa Đơn" button
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Thông báo");
    alert.setHeaderText("Thanh toán thành công!");
    alert.setContentText("Bạn có muốn in hóa đơn không?");

    ButtonType btnInHoaDon = new ButtonType("In Hóa Đơn");
    ButtonType btnClose = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);
    alert.getButtonTypes().setAll(btnInHoaDon, btnClose);

    alert.showAndWait().ifPresent(response -> {
        if (response == btnInHoaDon) {
            // Generate and display invoice details
            StringBuilder content = new StringBuilder();
            content.append("Mã Hóa Đơn: ").append(hd.getMaHoaDon()).append("\n");
            content.append("Tên KH: ").append(tenKH).append("\n");
            content.append("SĐT: ").append(sdt).append("\n");
            content.append("Ngày Lập: ").append(hd.getNgayLap()).append("\n");
            content.append("-------------------------\n");

            double tongTien = 0;

            for (CartItem item : cartData) {
                double thanhTien = item.getQuantity() * item.getPrice();
                tongTien += thanhTien;

                content.append("Thuốc: ").append(item.getName())
                       .append(" | SL: ").append(item.getQuantity())
                       .append(" | Đơn giá: ").append(item.getPrice())
                       .append(" | Thành tiền: ").append(thanhTien)
                       .append("\n");
            }

            content.append("-------------------------\n");
            content.append("Tổng tiền: ").append(tongTien).append(" VND");

            Alert printAlert = new Alert(Alert.AlertType.INFORMATION);
            printAlert.setTitle("Hóa Đơn");
            printAlert.setHeaderText("Chi Tiết Hóa Đơn");
            printAlert.setContentText(content.toString());
            printAlert.showAndWait();
        }
    });

    // Reload POS Page
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/MainLayout.fxml"));
        Parent root = loader.load();
        txtMaHoaDon.getScene().setRoot(root);
    } catch (Exception e) {
        e.printStackTrace();
    }
}





    @FXML
    private void handleThongTinThuoc() {
        String maThuoc = txtMaThuocInput.getText().trim();
        if (maThuoc.isEmpty()) return;

        Thuoc t = thuocDAO.getThuocByID(maThuoc);
        if (t == null) {
            System.out.println("Không tìm thấy thuốc");
            return;
        }

        currentThuoc = t;

        lblDrugName.setText(t.getTenThuoc());
        lblDrugID.setText("ID: " + t.getMaThuoc());
        txtThanhPhan.setText(t.getThanhPhan());
        txtGiaTien.setText(String.valueOf(t.getGiaBan()));

        Image img = new Image(getClass().getResource(t.getHinhAnh()).toExternalForm());
        imgDrug.setImage(img);

        quantity = 1;
        txtQuantity.setText(String.valueOf(quantity));
    }

    @FXML
    private void handleXemChiTietThuoc() {
        if (currentThuoc == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông tin công dụng thuốc");
        alert.setHeaderText(currentThuoc.getTenThuoc());
        alert.setContentText(currentThuoc.getCongDung());
        alert.showAndWait();
    }

    @FXML
    private void handleThemThuocVaoGio() {
        if (currentThuoc == null) return;

        int soLuong;
        try {
            soLuong = Integer.parseInt(txtQuantity.getText());
        } catch (NumberFormatException e) {
            soLuong = 1;
        }

        for (CartItem item : cartData) {
            if (item.getId().equals(currentThuoc.getMaThuoc())) {
                item.setQuantity(item.getQuantity() + soLuong);
                cartTable.refresh();
                updateTongTien();
                return;
            }
        }

        CartItem newItem = new CartItem(
                currentThuoc.getMaThuoc(),
                currentThuoc.getTenThuoc(),
                soLuong,
                currentThuoc.getGiaBan()
        );

        cartData.add(newItem);
        updateTongTien();
    }

    @FXML
    private void handleRemoveFromCart() {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Chưa chọn sản phẩm cần xóa!");
            alert.showAndWait();
            return;
        }

        cartData.remove(selectedItem);
        updateTongTien();
    }

    private void updateTongTien() {
        double tongTien = 0;

        for (CartItem item : cartData) {
            tongTien += item.getPrice() * item.getQuantity();
        }

        txtTongTien.setText(String.valueOf(tongTien));
        updateTienThua();
    }

    private void updateTienThua() {
        double tongTien = 0;
        try {
            tongTien = Double.parseDouble(txtTongTien.getText());
        } catch (NumberFormatException ignored) {}

        double tienDua = 0;
        try {
            tienDua = Double.parseDouble(txtTienDua.getText());
        } catch (NumberFormatException ignored) {}

        double tienThua = tienDua - tongTien;

        txtTienThua.setText(String.valueOf(tienThua));
    }

    private void setSoLuong() {
    txtQuantity.setText(String.valueOf(quantity));

    btnIncrease.setOnAction(event -> {
        if (currentThuoc != null && quantity < currentThuoc.getSoLuongTon()) {
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Không đủ thuốc!");
            alert.showAndWait();
        }
    });

    btnDecrease.setOnAction(event -> {
        if (quantity > 1) {
            quantity--;
            txtQuantity.setText(String.valueOf(quantity));
        }
    });

    txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
        try {
            int num = Integer.parseInt(newValue);
            if (num > 0) {
                if (currentThuoc != null && num > currentThuoc.getSoLuongTon()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Không đủ thuốc!");
                    alert.showAndWait();
                    txtQuantity.setText(String.valueOf(currentThuoc.getSoLuongTon()));
                } else {
                    quantity = num;
                }
            } else {
                txtQuantity.setText(String.valueOf(quantity));
            }
        } catch (NumberFormatException e) {
            txtQuantity.setText(String.valueOf(quantity));
        }
    });
}

}
