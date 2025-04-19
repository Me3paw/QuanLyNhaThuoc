package application.view.controller;

import application.database.LichSuCaLamDAO;
import application.model.LichSuCaLam;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class LichSuCaLamViecController {

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<LichSuCaLam> tableLichSuCaLam;

    @FXML
    private TableColumn<LichSuCaLam, String> colMaNhanVien;

    @FXML
    private TableColumn<LichSuCaLam, String> colNgayLam;

    @FXML
    private TableColumn<LichSuCaLam, Integer> colCaLam;

    private ObservableList<LichSuCaLam> lichSuCaLamList = FXCollections.observableArrayList();
    private LichSuCaLamDAO lichSuCaLamDAO = new LichSuCaLamDAO();

    @FXML
    public void initialize() {
        // Set up table columns
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        colNgayLam.setCellValueFactory(new PropertyValueFactory<>("ngayLam"));
        colCaLam.setCellValueFactory(new PropertyValueFactory<>("caLam"));

        // Load initial data
        hienThiTatCa();
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            hienThiTatCa();
        } else {
            ObservableList<LichSuCaLam> ketQua = FXCollections.observableArrayList();

            if (keyword.matches("NV-\\d{4}-\\d{3}")) {
                // Search by maNhanVien
                ketQua.addAll(lichSuCaLamDAO.getLichSuCaLamByNhanVien(keyword));
            } else {
                // Search by name (if applicable, adjust logic as needed)
                for (LichSuCaLam item : lichSuCaLamList) {
                    if (item.getMaNhanVien().toLowerCase().contains(keyword)) {
                        ketQua.add(item);
                    }
                }
            }

            if (ketQua.isEmpty()) {
                showNoResultsFound();
            } else {
                tableLichSuCaLam.setItems(ketQua);
            }
        }
    }

    private void hienThiTatCa() {
        lichSuCaLamList.setAll(lichSuCaLamDAO.getAllLichSuCaLam());
        tableLichSuCaLam.setItems(lichSuCaLamList);
    }


    private void showNoResultsFound() {
        tableLichSuCaLam.setItems(FXCollections.observableArrayList());
        System.out.println("No results found.");
    }
}
