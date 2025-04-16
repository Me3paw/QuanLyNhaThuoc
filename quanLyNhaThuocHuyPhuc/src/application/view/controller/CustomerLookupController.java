package application.view.controller;

import application.database.KhachHangDAO;
import application.model.KhachHang;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button; 
import javafx.scene.control.cell.ComboBoxTableCell; 
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList; 
import java.util.List;

public class CustomerLookupController {

    @FXML private TextField searchField; 
    @FXML private ComboBox<String> searchCriteriaCombo; 
    @FXML private Label resultLabel; 
    @FXML private TableView<KhachHang> customerTable; 

    @FXML private TableColumn<KhachHang, String> maKhachHangColumn;
    @FXML private TableColumn<KhachHang, String> tenKhachHangColumn;
    @FXML private TableColumn<KhachHang, String> sdtColumn;
    @FXML private TableColumn<KhachHang, String> gioiTinhColumn;
    @FXML private TableColumn<KhachHang, String> diaChiColumn; 
    @FXML private TableColumn<KhachHang, String> emailColumn;  
    @FXML private TableColumn<KhachHang, String> ghiChuColumn; 

    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private ObservableList<String> gioiTinhOptions = FXCollections.observableArrayList("Nam", "Nữ", "Khác");


    @FXML
    public void initialize() {
        customerTable.setEditable(true);

        maKhachHangColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKhachHang()));
        tenKhachHangColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenKH()));
        sdtColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSoDienThoai()));
        gioiTinhColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGioiTinh()));
        diaChiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDiaChi()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        ghiChuColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGhiChu()));

        tenKhachHangColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tenKhachHangColumn.setOnEditCommit(event -> handleEditCommit(event, (kh, value) -> kh.setTenKH(value)));

        sdtColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sdtColumn.setOnEditCommit(event -> handleEditCommit(event, (kh, value) -> kh.setSoDienThoai(value)));

        gioiTinhColumn.setCellFactory(ComboBoxTableCell.forTableColumn(gioiTinhOptions));
        gioiTinhColumn.setOnEditCommit(event -> handleEditCommit(event, (kh, value) -> kh.setGioiTinh(value)));

        diaChiColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        diaChiColumn.setOnEditCommit(event -> handleEditCommit(event, (kh, value) -> kh.setDiaChi(value)));

        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> handleEditCommit(event, (kh, value) -> kh.setEmail(value)));

        ghiChuColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ghiChuColumn.setOnEditCommit(event -> handleEditCommit(event, (kh, value) -> kh.setGhiChu(value)));

        searchCriteriaCombo.setItems(FXCollections.observableArrayList("Số Điện Thoại"));
        searchCriteriaCombo.getSelectionModel().selectFirst();

        loadAllCustomers(); 
    }
    @FXML
    private void handleSearchClick() {
        String keyword = searchField.getText().trim(); 
        List<KhachHang> customerList = new ArrayList<>(); 

        if (keyword != null && !keyword.isEmpty()) {
        	KhachHang kh = khachHangDAO.getKhachHangBySDT(keyword); 
        	if (kh != null) {
        		customerList.add(kh);
        	}
        	else {
                showAlert(Alert.AlertType.ERROR, "Chưa nhập trường tìm kiếm", "Vui lòng thử lại.");
               
            }
        } else if (keyword == null || keyword.isEmpty()){
             loadAllCustomers(); 
             return; 
        }

        updateTable(customerList);
    }

    @FXML
    private void handleResetClick() {
        searchField.clear();
        searchCriteriaCombo.getSelectionModel().selectFirst(); 
        resultLabel.setText(""); 
        loadAllCustomers(); 
    }


    private void loadAllCustomers() {
        List<KhachHang> customerList = khachHangDAO.getAllKhachHang();
        updateTable(customerList);
    }

    private void updateTable(List<KhachHang> customerList) {
        if (customerList == null) {
           customerList = new ArrayList<>(); 
        }
        ObservableList<KhachHang> observableList = FXCollections.observableArrayList(customerList);
        customerTable.setItems(observableList);
        resultLabel.setText("Tìm thấy: " + customerList.size() + " khách hàng");
        customerTable.refresh();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); 
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FunctionalInterface
    interface KhachHangUpdater {
        void update(KhachHang kh, String value);
    }
    
    private void handleEditCommit(TableColumn.CellEditEvent<KhachHang, String> event, KhachHangUpdater updater) {
        KhachHang editedCustomer = event.getRowValue();
        String newValue = event.getNewValue();         

        if (editedCustomer != null && newValue != null) {
             String oldValue = event.getOldValue();
            try {
                updater.update(editedCustomer, newValue);
                boolean success = khachHangDAO.updateKhachHang(editedCustomer);
                if (!success) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi Cập Nhật", "Không thể cập nhật khách hàng trong cơ sở dữ liệu.");
                    updater.update(editedCustomer, oldValue);
                    event.getTableView().getItems().set(event.getTablePosition().getRow(), editedCustomer);
                    event.getTableView().refresh(); 
                }

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
                updater.update(editedCustomer, oldValue);
                event.getTableView().getItems().set(event.getTablePosition().getRow(), editedCustomer);
                event.getTableView().refresh();
                e.printStackTrace();
            }
        }
    }
}