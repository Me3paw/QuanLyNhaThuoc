package application.view.controller;

import application.database.KhachHangDAO;
import entity.KhachHang;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class DanhSachKhachHangController {

    @FXML private TextField txtSdtSearch;
    @FXML private TableView<KhachHang> tableCustomer;
    @FXML private TableColumn<KhachHang, String> colMaKH;
    @FXML private TableColumn<KhachHang, String> colTenKH;
    @FXML private TableColumn<KhachHang, String> colSdt;
    @FXML private TableColumn<KhachHang, String> colGioiTinh;

    private final KhachHangDAO khachHangDAO = new KhachHangDAO(); 

    @FXML
    public void initialize() {
        setupTableColumns();
        loadAllCustomers(); 
    }

    private void setupTableColumns() {
        colMaKH.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getMaKhachHang));
        colTenKH.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getTenKH));
        colSdt.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getSoDienThoai));
        colGioiTinh.setCellValueFactory(cellData -> Bindings.createStringBinding(cellData.getValue()::getGioiTinh));
    }

    private void loadAllCustomers() {
        ObservableList<KhachHang> danhSach = FXCollections.observableArrayList(khachHangDAO.getAllKhachHang());
        tableCustomer.setItems(danhSach);
    }

    @FXML
    private void handleSearchBySdt() {
        String sdtKeyword = txtSdtSearch.getText().trim();
        List<KhachHang> ketQuaTimKiem = new ArrayList<>(); 

        if (sdtKeyword.isEmpty()) {
            loadAllCustomers(); 
            return;
        }

        KhachHang foundCustomer = khachHangDAO.getKhachHangBySDT(sdtKeyword);

        if (foundCustomer != null) {
            ketQuaTimKiem.add(foundCustomer);
            updateTable(ketQuaTimKiem);
        } else {
            updateTable(ketQuaTimKiem); 
            showNoResultsFound();
        }
    }

    @FXML
    private void handleResetClick() {
        txtSdtSearch.clear();
        loadAllCustomers();
       // if (resultLabel != null) resultLabel.setText("");
    }
    
    private void updateTable(List<KhachHang> customerList) {
        if (customerList == null) {
          customerList = new ArrayList<>();
       }
       tableCustomer.setItems(FXCollections.observableArrayList(customerList));
   }

    private void showNoResultsFound() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kết quả tìm kiếm");
        alert.setHeaderText(null);
        alert.setContentText("Không tìm thấy khách hàng nào khớp với từ khóa.");
        alert.showAndWait();
    }

     public void refreshTable() {
         loadAllCustomers();
     }
}