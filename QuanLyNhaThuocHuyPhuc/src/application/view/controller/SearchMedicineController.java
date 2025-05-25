package application.view.controller;

import application.database.ThuocDAO;
import entity.NhaCungCap;
import entity.Thuoc;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

public class SearchMedicineController {

    @FXML
    private TextField searchField; // Trường tìm kiếm
    @FXML
    private ComboBox<String> searchCriteriaCombo; // ComboBox cho tiêu chí tìm kiếm
    @FXML
    private TableView<Thuoc> medicineTable; // Bảng thuốc
    @FXML
    private TableColumn<Thuoc, String> codeColumn; // Cột mã thuốc
    @FXML
    private TableColumn<Thuoc, String> nameColumn; // Cột tên thuốc
    @FXML
    private TableColumn<Thuoc, String> thanhPhanColumn; // Cột thành phần
    @FXML
    private TableColumn<Thuoc, String> congDungColumn; // Cột công dụng
    @FXML
    private TableColumn<Thuoc, String> hanSuDungColumn; // Cột hạn sử dụng
    @FXML
    private TableColumn<Thuoc, String> giaBanColumn; // Cột giá bán
    @FXML
    private TableColumn<Thuoc, String> giaNhapColumn; // Cột giá nhập
    @FXML
    private TableColumn<Thuoc, String> soLuongTonColumn; // Cột số lượng tồn
    @FXML
    private TableColumn<Thuoc, String> maNhaCungCapColumn; // Cột mã nhà cung cấp
    @FXML
    private TableColumn<Thuoc, String> hinhAnhColumn; // Cột hình ảnh
    @FXML
    private ImageView imgPreview;

    private ThuocDAO thuocDAO = new ThuocDAO();

    // Phương thức gọi khi nhấn nút tìm kiếm
    @FXML
    private void handleSearchClick() {
        String keyword = searchField.getText();
        String selectedCriteria = searchCriteriaCombo.getValue();

        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Thuoc> thuocList = null;

            if ("Tên Thuốc".equals(selectedCriteria)) {
                thuocList = thuocDAO.searchThuocByTen(keyword);
            } else if ("Mã Thuốc".equals(selectedCriteria)) {
                thuocList = thuocDAO.searchThuocByMa(keyword);
            } else if ("Công Dụng".equals(selectedCriteria)) {
                thuocList = thuocDAO.searchThuocByCongDung(keyword);
            }
            updateTable(thuocList);
        }
    }

    // Phương thức gọi khi nhấn nút làm mới
    @FXML
    private void handleResetClick() {
        searchField.clear();
        searchCriteriaCombo.getSelectionModel().clearSelection();
        loadAllMedicines();
    }

    // Phương thức khởi tạo bảng thuốc
    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaThuoc()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenThuoc()));
        thanhPhanColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThanhPhan()));
        congDungColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCongDung()));
        hanSuDungColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHanSuDung()));
        giaBanColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getGiaBan())));
        giaNhapColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getGiaNhap())));
        soLuongTonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSoLuongTon())));
        maNhaCungCapColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNhaCungCap()));
        hinhAnhColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHinhAnh()));

        searchCriteriaCombo.setItems(FXCollections.observableArrayList("Tên Thuốc", "Mã Thuốc", "Công Dụng"));
        searchCriteriaCombo.getSelectionModel().select(0);

        loadAllMedicines();
        medicineTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadImagePreview(newSelection); 
            } else {
                imgPreview.setImage(null);
            }
        });
    }

    // Phương thức để hiển thị tất cả các thuốc trong bảng
    private void loadAllMedicines() {
        List<Thuoc> thuocList = thuocDAO.getAllThuoc();
        updateTable(thuocList);
    }

    // Phương thức cập nhật TableView với danh sách thuốc
    private void updateTable(List<Thuoc> thuocList) {
        ObservableList<Thuoc> observableList = FXCollections.observableArrayList(thuocList);
        medicineTable.setItems(observableList);
    }

    // Phương thức load ảnh vào imgPreview
    @FXML
    private void loadImagePreview(Thuoc thuoc) {

            // Hiển thị hình ảnh preview (nếu có)
            if (thuoc.getHinhAnh() != null && !thuoc.getHinhAnh().isEmpty()) {
                try {
                    Image image = new Image(getClass().getResourceAsStream(thuoc.getHinhAnh()));
                    imgPreview.setImage(image);
                } catch (Exception e) {
                    imgPreview.setImage(null);
                }
            } else {
                imgPreview.setImage(null);
            }
        }
}
