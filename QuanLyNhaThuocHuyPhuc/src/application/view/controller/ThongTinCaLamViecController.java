package application.view.controller;

import application.database.LichSuCaLamDAO;
import application.database.NhanVienDAO;
import application.util.SessionManager;
import entity.LichSuCaLam;
import entity.NhanVien;
import entity.NhanVienByCa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ThongTinCaLamViecController {

    @FXML
    private Button btnChamCong;
    @FXML
    private TableView<NhanVienByCa> tableNhanVienByCa;

    @FXML
    private TableColumn<NhanVienByCa, String> colCa1;

    @FXML
    private TableColumn<NhanVienByCa, String> colCa2;

    @FXML
    private TableView<LichSuCaLam> tableLichSuCaLam;

    @FXML
    private TableColumn<LichSuCaLam, String> colMaNhanVien;

    @FXML
    private TableColumn<LichSuCaLam, LocalDate> colNgayLam;

    @FXML
    private TableColumn<LichSuCaLam, Integer> colCaLam;

    private final LichSuCaLamDAO lichSuCaLamDAO = new LichSuCaLamDAO();

    public void initialize() {
        // Set up table columns
        colMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        colNgayLam.setCellValueFactory(new PropertyValueFactory<>("ngayLam"));
        colCaLam.setCellValueFactory(new PropertyValueFactory<>("caLam"));
        
        colCa1.setCellValueFactory(new PropertyValueFactory<>("ca1"));
        colCa2.setCellValueFactory(new PropertyValueFactory<>("ca2"));
        loadNhanVienByCa();
        // Load current user's LichSuCaLam
        loadLichSuCaLam();

        // Set up button action
        btnChamCong.setOnAction(event -> chamCong());
    }
   private void loadNhanVienByCa() {
    ObservableList<NhanVienByCa> data = FXCollections.observableArrayList();

    // Fetch all NhanVien and group by shifts
    var nhanVienDAO = new NhanVienDAO(); // Ensure this DAO exists
    List<NhanVien> nhanVienList = nhanVienDAO.getAllNhanVien(); // Fetch all NhanVien from the database

    List<String> ca1List = nhanVienList.stream()
            .filter(nv -> nv.getCaLam() == 1)
            .map(NhanVien::getTenNhanVien)
            .toList();
    List<String> ca2List = nhanVienList.stream()
            .filter(nv -> nv.getCaLam() == 2)
            .map(NhanVien::getTenNhanVien)
            .toList();

    // Create rows for the table
    int maxRows = Math.max(ca1List.size(), ca2List.size());
    for (int i = 0; i < maxRows; i++) {
        String ca1 = i < ca1List.size() ? ca1List.get(i) : "";
        String ca2 = i < ca2List.size() ? ca2List.get(i) : "";
        data.add(new NhanVienByCa(ca1, ca2));
    }

    tableNhanVienByCa.setItems(data);
   }

    private void loadLichSuCaLam() {
        var currentUser = SessionManager.getLoggedInNhanVien();
        if (currentUser != null) {
            ObservableList<LichSuCaLam> data = FXCollections.observableArrayList(
                    lichSuCaLamDAO.getLichSuCaLamByNhanVien(currentUser.getMaNhanVien())
            );
            tableLichSuCaLam.setItems(data);

            // Disable btnChamCong if there is an entry for today
            boolean hasTodayEntry = data.stream()
                    .anyMatch(entry -> entry.getNgayLam().equals(LocalDate.now()));
            btnChamCong.setDisable(hasTodayEntry);
        }
    }

    private void chamCong() {
        var currentUser = SessionManager.getLoggedInNhanVien();
        if (currentUser != null) {
            LocalDate today = LocalDate.now();
            int caLam = currentUser.getCaLam();

            LichSuCaLam newEntry = new LichSuCaLam(currentUser.getMaNhanVien(), today, caLam);
            if (lichSuCaLamDAO.addLichSuCaLam(newEntry)) {
                loadLichSuCaLam(); // Refresh table
            }
        }
    }
}
