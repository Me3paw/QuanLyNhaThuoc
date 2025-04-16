package application.view.controller;

import application.database.ThuocDAO;
import application.model.Thuoc;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.util.List;

public class SearchMedicineController {

    @FXML
    private TextField searchField; // Trường tìm kiếm
    @FXML
    private ComboBox<String> searchCriteriaCombo; // ComboBox cho tiêu chí tìm kiếm
    @FXML
    private Label resultLabel; // Label hiển thị kết quả tìm kiếm
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

    private ThuocDAO thuocDAO = new ThuocDAO();

    // Phương thức gọi khi nhấn nút tìm kiếm
    @FXML
    private void handleSearchClick() {
        String keyword = searchField.getText();
        String selectedCriteria = searchCriteriaCombo.getValue();

        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Thuoc> thuocList = null;

            // Tìm kiếm theo tiêu chí đã chọn
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
        resultLabel.setText("Kết quả tìm kiếm: 0 thuốc tìm thấy");
        loadAllMedicines();
    }

    // Phương thức khởi tạo bảng thuốc
    @FXML
    public void initialize() {
        // Khởi tạo các cột trong bảng
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

        // Thiết lập các mục trong ComboBox
        searchCriteriaCombo.setItems(FXCollections.observableArrayList("Tên Thuốc", "Mã Thuốc", "Công Dụng"));
        searchCriteriaCombo.getSelectionModel().select(0);

        // Hiển thị tất cả các thuốc khi khởi động
        loadAllMedicines();
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
        resultLabel.setText("Kết quả tìm kiếm: " + thuocList.size() + " thuốc tìm thấy");
    }
}
