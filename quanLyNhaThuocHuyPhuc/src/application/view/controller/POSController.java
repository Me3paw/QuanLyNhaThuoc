package application.view.controller;

import application.database.ThuocDAO;
import application.model.Thuoc;
import application.model.CartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    private Thuoc currentThuoc;
    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final ObservableList<CartItem> cartData = FXCollections.observableArrayList();
    private int quantity = 1;

    @FXML
    public void initialize() {
        cartTable.setItems(cartData);
        setSoLuong();

        txtTienDua.textProperty().addListener((observable, oldValue, newValue) -> {
            updateTienThua();
        });
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

        if (tienThua < 0) tienThua = 0;

        txtTienThua.setText(String.valueOf(tienThua));
    }

    private void setSoLuong() {
        txtQuantity.setText(String.valueOf(quantity));

        btnIncrease.setOnAction(event -> {
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
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
                    quantity = num;
                } else {
                    txtQuantity.setText(String.valueOf(quantity));
                }
            } catch (NumberFormatException e) {
                txtQuantity.setText(String.valueOf(quantity));
            }
        });
    }
}