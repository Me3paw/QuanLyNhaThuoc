package application.view.controller;

import application.database.NhaCungCapDAO;
import application.database.ThuocDAO;
import entity.NhaCungCap;
import entity.Thuoc;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

public class ThemThuocController {


    @FXML private TextField txtTenThuoc;
    @FXML private TextArea txtThanhPhan;
    @FXML private TextArea txtCongDung;
    @FXML private DatePicker dpHanSuDung;
    @FXML private TextField txtGiaBan;
    @FXML private TextField txtGiaNhap;
    @FXML private TextField txtSoLuongTon;
    @FXML private ComboBox<NhaCungCap> cboNhaCungCap;
    @FXML private TextField txtHinhAnhPath;
    @FXML private Button btnBrowseImage;
    @FXML private ImageView imgPreview;
    @FXML private Button btnThemThuoc;
    @FXML private Button btnHuy;
    
    @FXML private TableView<Thuoc> medicineTable;
    @FXML private TableColumn<Thuoc, String> codeColumn;
    @FXML private TableColumn<Thuoc, String> nameColumn;
    @FXML private TableColumn<Thuoc, String> thanhPhanColumn;
    @FXML private TableColumn<Thuoc, String> congDungColumn;
    @FXML private TableColumn<Thuoc, String> hanSuDungColumn;
    @FXML private TableColumn<Thuoc, Double> giaBanColumn;
    @FXML private TableColumn<Thuoc, Double> giaNhapColumn;
    @FXML private TableColumn<Thuoc, Integer> soLuongTonColumn;
    @FXML private TableColumn<Thuoc, String> maNhaCungCapColumn;
    @FXML private TableColumn<Thuoc, String> hinhAnhColumn;

    private ThuocDAO thuocDAO = new ThuocDAO();
    private NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private File selectedImageFile = null;
    private ObservableList<NhaCungCap> listNhaCungCap;

    @FXML
    public void initialize() {
    	imgPreview.setImage(new Image(getClass().getResourceAsStream("/application/assets/images/thuocmacdinh.png")));
    	loadDataToTable();
        loadNhaCungCapData();
        
        medicineTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithThuoc(newSelection);
            }
        });
    }


    private void loadNhaCungCapData() {
        List<NhaCungCap> nccListFromDb = nccDAO.getAllTenNhaCungCap();
        listNhaCungCap = FXCollections.observableArrayList(nccListFromDb);
        cboNhaCungCap.setItems(listNhaCungCap);
    }

    //Xử lý sự kiện chọn ảnh và hiển thị preview.
    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn Hình Ảnh Thuốc");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        Stage stage = (Stage) btnBrowseImage.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImageFile = file;
            txtHinhAnhPath.setText(selectedImageFile.getName());
            try {
                Image image = new Image(selectedImageFile.toURI().toString());
                if (imgPreview != null) {
                    imgPreview.setImage(image);
                }
            } catch (Exception e) {
                 showAlert(Alert.AlertType.ERROR, "Lỗi Hiển Thị Ảnh", "Không thể hiển thị ảnh xem trước: " + e.getMessage());
                 if (imgPreview != null) { imgPreview.setImage(null); }
                 selectedImageFile = null;
                 txtHinhAnhPath.clear();
            }
        } else {
            selectedImageFile = null;
            txtHinhAnhPath.clear();
            if (imgPreview != null) { imgPreview.setImage(null); }
        }
    }


    @FXML
private void handleThemThuocAction() {
    String tenThuoc = txtTenThuoc.getText().trim();
    String thanhPhan = txtThanhPhan.getText().trim();
    String congDung = txtCongDung.getText().trim();
    LocalDate hanSuDungDate = dpHanSuDung.getValue();
    String giaBanStr = txtGiaBan.getText().trim();
    String giaNhapStr = txtGiaNhap.getText().trim();
    String soLuongTonStr = txtSoLuongTon.getText().trim();
    NhaCungCap selectedNCC = cboNhaCungCap.getValue();
    String hinhAnhRelativePath = "";

    // Validate dữ liệu
    if (!validateInputs(tenThuoc, hanSuDungDate, giaBanStr, giaNhapStr, soLuongTonStr, selectedNCC)) {
        return;
    }

    double giaBan = Double.parseDouble(giaBanStr);
    double giaNhap = Double.parseDouble(giaNhapStr);
    int soLuongTon = Integer.parseInt(soLuongTonStr);

    // Additional validation
    if (hanSuDungDate.isBefore(LocalDate.now())) {
        showAlert(Alert.AlertType.WARNING, "Sai thông tin", "Hạn sử dụng phải lớn hơn ngày hôm nay.");
        dpHanSuDung.requestFocus();
        return;
    }
    if (giaBan <= giaNhap) {
        showAlert(Alert.AlertType.WARNING, "Sai thông tin", "Giá bán phải lớn hơn giá nhập.");
        txtGiaBan.requestFocus();
        return;
    }
    if (soLuongTon <= 0) {
        showAlert(Alert.AlertType.WARNING, "Sai thông tin", "Số lượng tồn phải lớn hơn 0.");
        txtSoLuongTon.requestFocus();
        return;
    }

    // Xử lý hình ảnh
    if (selectedImageFile != null) {
        try {
            Path destinationFolder = Paths.get("src", "application", "assets", "images", "thuoc");
            if (!Files.exists(destinationFolder)) {
                Files.createDirectories(destinationFolder);
            }
            String newFileName = selectedImageFile.getName(); // Dễ bị trùng tên
            Path destinationPath = destinationFolder.resolve(newFileName);
            Files.copy(selectedImageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            hinhAnhRelativePath = "/application/assets/images/thuoc" + newFileName;
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi File", "Không thể sao chép file hình ảnh: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    } else {
        hinhAnhRelativePath = txtHinhAnhPath.getText().trim();
    }

    // Tạo mã thuốc tự động
    String maThuoc = thuocDAO.generateNextMaThuoc(tenThuoc);

    Thuoc newThuoc = new Thuoc();
    newThuoc.setMaThuoc(maThuoc);
    newThuoc.setTenThuoc(tenThuoc);
    newThuoc.setThanhPhan(thanhPhan);
    newThuoc.setCongDung(congDung);
    newThuoc.setHanSuDung(hanSuDungDate.toString());
    newThuoc.setGiaBan(giaBan);
    newThuoc.setGiaNhap(giaNhap);
    newThuoc.setSoLuongTon(soLuongTon);
    newThuoc.setMaNhaCungCap(selectedNCC.getMaNhaCungCap());
    newThuoc.setHinhAnh(hinhAnhRelativePath);

    if (thuocDAO.addThuoc(newThuoc)) {
        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm thuốc '" + tenThuoc + "' với mã '" + maThuoc + "' thành công!");
        clearForm();
    } else {
        showAlert(Alert.AlertType.ERROR, "Lỗi", "Thêm thuốc thất bại.");
    }
}


    private boolean validateInputs(String tenThuoc, LocalDate hanSuDungDate, String giaBanStr, String giaNhapStr, String soLuongTonStr, NhaCungCap selectedNCC) {
        if (tenThuoc.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập Tên Thuốc."); txtTenThuoc.requestFocus(); return false; }
        if (hanSuDungDate == null) { showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Hạn Sử Dụng."); dpHanSuDung.requestFocus(); return false; }
        if (giaBanStr.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập Giá Bán."); txtGiaBan.requestFocus(); return false; }
        if (giaNhapStr.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập Giá Nhập."); txtGiaNhap.requestFocus(); return false; }
        if (soLuongTonStr.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập Số Lượng Tồn."); txtSoLuongTon.requestFocus(); return false; }
        if (selectedNCC == null) { showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Nhà Cung Cấp."); cboNhaCungCap.requestFocus(); return false; }
        try { if (Double.parseDouble(giaBanStr) < 0) throw new NumberFormatException(); } catch (NumberFormatException e) { showAlert(Alert.AlertType.WARNING, "Sai định dạng", "Giá Bán phải là số không âm."); txtGiaBan.requestFocus(); return false; }
        try { if (Double.parseDouble(giaNhapStr) < 0) throw new NumberFormatException(); } catch (NumberFormatException e) { showAlert(Alert.AlertType.WARNING, "Sai định dạng", "Giá Nhập phải là số không âm."); txtGiaNhap.requestFocus(); return false; }
        try { if (Integer.parseInt(soLuongTonStr) < 0) throw new NumberFormatException(); } catch (NumberFormatException e) { showAlert(Alert.AlertType.WARNING, "Sai định dạng", "Số Lượng Tồn phải là số nguyên không âm."); txtSoLuongTon.requestFocus(); return false; }
        return true;
    }


    @FXML
    private void handleHuyAction() {
        clearForm();
    }


    private void clearForm() {
        txtTenThuoc.clear();
        txtThanhPhan.clear();
        txtCongDung.clear();
        dpHanSuDung.setValue(null);
        txtGiaBan.clear();
        txtGiaNhap.clear();
        txtSoLuongTon.clear();
        cboNhaCungCap.getSelectionModel().clearSelection();
        cboNhaCungCap.setPromptText("Chọn nhà cung cấp");
        txtHinhAnhPath.clear();
        selectedImageFile = null;
        if (imgPreview != null) {
            imgPreview.setImage(null);
        }
        txtTenThuoc.requestFocus();
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

     /**
     * Hàm tiện ích lấy đuôi file.
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    public void loadDataToTable() {
        List<Thuoc> listThuoc = thuocDAO.getAllThuoc();
        ObservableList<Thuoc> data = FXCollections.observableArrayList(listThuoc);

        codeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaThuoc()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenThuoc()));
        thanhPhanColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThanhPhan()));
        congDungColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCongDung()));
        hanSuDungColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHanSuDung()));
        giaBanColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGiaBan()).asObject());
        giaNhapColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGiaNhap()).asObject());
        soLuongTonColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSoLuongTon()).asObject());
        maNhaCungCapColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaNhaCungCap()));
        hinhAnhColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHinhAnh()));

        medicineTable.setItems(data);
    }
    
    private void fillFormWithThuoc(Thuoc thuoc) {
        txtTenThuoc.setText(thuoc.getTenThuoc());
        txtThanhPhan.setText(thuoc.getThanhPhan());
        txtCongDung.setText(thuoc.getCongDung());
        
        // Đổi String sang LocalDate cho dpHanSuDung
        if (thuoc.getHanSuDung() != null && !thuoc.getHanSuDung().isEmpty()) {
            dpHanSuDung.setValue(LocalDate.parse(thuoc.getHanSuDung()));
        } else {
            dpHanSuDung.setValue(null);
        }
        
        txtGiaBan.setText(String.valueOf(thuoc.getGiaBan()));
        txtGiaNhap.setText(String.valueOf(thuoc.getGiaNhap()));
        txtSoLuongTon.setText(String.valueOf(thuoc.getSoLuongTon()));

        // Chọn NhaCungCap trong ComboBox dựa vào mã nhà cung cấp
        for (NhaCungCap ncc : cboNhaCungCap.getItems()) {
            if (ncc.getMaNhaCungCap().equals(thuoc.getMaNhaCungCap())) {
                cboNhaCungCap.getSelectionModel().select(ncc);
                break;
            }
        }

        txtHinhAnhPath.setText(thuoc.getHinhAnh());

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