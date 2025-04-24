package application.view.controller;

import application.database.NhanVienDAO;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ChiaCaLamViecController {

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<NhanVien> tableNhanVien;

    @FXML
    private TableColumn<NhanVien, String> colMaNhanVien;

    @FXML
    private TableColumn<NhanVien, String> colTenNhanVien;

    @FXML
    private TableColumn<NhanVien, String> colSDT;

    @FXML
    private TableColumn<NhanVien, Integer> colCaLam;

    @FXML
    private TableColumn<NhanVien, Boolean> colChon;

    @FXML
    private ComboBox<Integer> comboCaLam;

    @FXML
    private Button btnLuuCaLam;

    private ObservableList<NhanVien> nhanVienList;

    private NhanVienDAO nhanVienDAO;

    @FXML
    public void initialize() {
        nhanVienDAO = new NhanVienDAO();

        // Set up the CheckBox column
        colChon.setCellValueFactory(param -> param.getValue().selectedProperty());

        colChon.setCellFactory(column -> new TableCell<NhanVien, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                // Handle checkbox click without infinite loop
                checkBox.setOnAction(event -> {
                    NhanVien nhanVien = getTableView().getItems().get(getIndex());
                    if (nhanVien != null) {
                        nhanVien.setSelected(checkBox.isSelected());
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    NhanVien nhanVien = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(nhanVien.isSelected());
                    setGraphic(checkBox);
                }
            }
        });


        // Initialize other table columns
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        colTenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colCaLam.setCellValueFactory(new PropertyValueFactory<>("caLam"));

        // Initialize combo box for caLam
        comboCaLam.setItems(FXCollections.observableArrayList(1, 2));

        // Load data into the table
        loadNhanVienData();
    }

    private void loadNhanVienData() {
        nhanVienList = FXCollections.observableArrayList(nhanVienDAO.getAllNhanVien());
        tableNhanVien.setItems(nhanVienList);
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        ObservableList<NhanVien> searchResults;
        if (keyword.isEmpty()) {
            searchResults = FXCollections.observableArrayList(nhanVienDAO.getAllNhanVien());
        } else if (keyword.matches("NV-\\d{4}-\\d{3}")) {
            NhanVien nhanVien = nhanVienDAO.getNhanVienByMa(keyword);
            searchResults = nhanVien != null ? FXCollections.observableArrayList(nhanVien) : FXCollections.observableArrayList();
        } else {
            searchResults = FXCollections.observableArrayList(nhanVienDAO.searchNhanVienByName(keyword));
        }

        if (searchResults.isEmpty()) {
            showAlert("No Results", "No employees found matching the search criteria.", Alert.AlertType.INFORMATION);
        } else {
            tableNhanVien.setItems(searchResults);
        }
    }

    @FXML
    private void handleLuuCaLam() {
        Integer selectedCaLam = comboCaLam.getValue();
        if (selectedCaLam == null) {
            showAlert("Error", "Please select a shift (ca làm)!", Alert.AlertType.ERROR);
            return;
        }

        for (NhanVien nv : nhanVienList) {
            if (nv.isSelected()) {
                nv.setCaLam(selectedCaLam);
                boolean success = nhanVienDAO.setCaLam(nv.getMaNhanVien(), selectedCaLam);
                if (!success) {
                    showAlert("Error", "Failed to update ca làm for " + nv.getTenNhanVien(), Alert.AlertType.ERROR);
                    return;
                }
            }
        }

        showAlert("Success", "Shift (ca làm) updated successfully!", Alert.AlertType.INFORMATION);
        loadNhanVienData();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
