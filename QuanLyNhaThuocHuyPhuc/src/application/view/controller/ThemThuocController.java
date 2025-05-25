package application.view.controller;

import application.database.NhaCungCapDAO;
import application.database.ThuocDAO;
import entity.NhaCungCap;
import entity.Thuoc;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    @FXML private Button btnThemThuoc;
    @FXML private Button btnHuy;
    @FXML private Button btnNhapFile;

    @FXML private TableView<Thuoc> danhSachThuoc;
    @FXML private TableColumn<Thuoc, String> colMaThuoc;
    @FXML private TableColumn<Thuoc, String> colTenThuoc;
    @FXML private TableColumn<Thuoc, String> colThanhPhan;
    @FXML private TableColumn<Thuoc, String> colCongDung;
    @FXML private TableColumn<Thuoc, String> colHanSuDung;
    @FXML private TableColumn<Thuoc, Double> colGiaBan;
    @FXML private TableColumn<Thuoc, Double> colGiaNhap;
    @FXML private TableColumn<Thuoc, Integer> colSoLuongTon;
    @FXML private TableColumn<Thuoc, String> colNhaCungCap;
    @FXML private TableColumn<Thuoc, String> colHinhAnh;

    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();

    private File selectedImageFile = null;

    private ObservableList<NhaCungCap> listNhaCungCap;
    private final ObservableList<Thuoc> dsThuoc = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load nhà cung cấp lên combobox
        loadNhaCungCapData();

        // Thiết lập cell value factory cho các cột
       
        colTenThuoc.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTenThuoc()));
        colThanhPhan.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getThanhPhan()));
        colCongDung.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCongDung()));
        colHanSuDung.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getHanSuDung()));
        colGiaBan.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getGiaBan()).asObject());
        colGiaNhap.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getGiaNhap()).asObject());
        colSoLuongTon.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getSoLuongTon()).asObject());

        // Hiển thị tên nhà cung cấp thay vì mã
        colNhaCungCap.setCellValueFactory(cell -> {
            String maNCC = cell.getValue().getMaNhaCungCap();
            NhaCungCap ncc = NhaCungCapDAO.getByMaNhaCungCap(maNCC);
            String tenNCC=ncc.getTenNhaCungCap();
            return new SimpleStringProperty(tenNCC != null ? tenNCC : "");
        });

        colHinhAnh.setCellFactory(new Callback<TableColumn<Thuoc, String>, TableCell<Thuoc, String>>() {
            @Override
            public TableCell<Thuoc, String> call(TableColumn<Thuoc, String> param) {
                return new TableCell<Thuoc, String>() {
                    private final ImageView imageView = new ImageView();

                    {
                        imageView.setFitWidth(150);   // chỉnh kích thước ảnh phù hợp
                        imageView.setFitHeight(80);
                        imageView.setPreserveRatio(false);
                    }

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);
                        if (empty || imagePath == null || imagePath.isEmpty()) {
                            setGraphic(null);
                        } else {
                            try {
                                // Tạo ảnh từ đường dẫn
                            	String resourcePath = "/application/assets/images/thuoc/" + imagePath;
                            	Image image = new Image(getClass().getResourceAsStream(resourcePath));
                            	imageView.setImage(image);
                            	setGraphic(imageView);
                            } catch (Exception e) {
                                setGraphic(null); // trường hợp lỗi đường dẫn ảnh
                            }
                        }
                    }
                };
            }
        });

        // Thiết lập CellValueFactory vẫn giữ dạng String đường dẫn
        colHinhAnh.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getHinhAnh()));
        danhSachThuoc.setItems(dsThuoc);
    }

    private void loadNhaCungCapData() {
        List<NhaCungCap> nccListFromDb = nccDAO.getAllTenNhaCungCap();
        listNhaCungCap = FXCollections.observableArrayList(nccListFromDb);
        cboNhaCungCap.setItems(listNhaCungCap);
    }

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
        } else {
            selectedImageFile = null;
            txtHinhAnhPath.clear();
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

        if (!validateInputs(tenThuoc, hanSuDungDate, giaBanStr, giaNhapStr, soLuongTonStr, selectedNCC)) return;

        double giaBan = Double.parseDouble(giaBanStr);
        double giaNhap = Double.parseDouble(giaNhapStr);
        int soLuongTon = Integer.parseInt(soLuongTonStr);

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

        if (selectedImageFile != null) {
            try {
                Path destinationFolder = Paths.get("src", "application", "assets", "images", "thuoc");
                if (!Files.exists(destinationFolder)) Files.createDirectories(destinationFolder);

                String newFileName = selectedImageFile.getName();
                Path destinationPath = destinationFolder.resolve(newFileName);
                Files.copy(selectedImageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                hinhAnhRelativePath = "/application/assets/images/thuoc/" + newFileName;
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi File", "Không thể sao chép file hình ảnh: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        } else {
            hinhAnhRelativePath = txtHinhAnhPath.getText().trim();
        }

        String maThuoc = thuocDAO.generateNextMaThuoc(tenThuoc);
        Thuoc newThuoc = new Thuoc(maThuoc, tenThuoc, thanhPhan, congDung, hanSuDungDate.toString(), giaBan, giaNhap, soLuongTon, selectedNCC.getMaNhaCungCap(), hinhAnhRelativePath);

        if (thuocDAO.addThuoc(newThuoc)) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm thuốc '" + tenThuoc + "' với mã '" + maThuoc + "' thành công!");
            dsThuoc.add(newThuoc); // thêm vào danh sách hiển thị
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Thêm thuốc thất bại.");
        }
    }

    private boolean validateInputs(String tenThuoc, LocalDate hanSuDungDate, String giaBanStr, String giaNhapStr, String soLuongTonStr, NhaCungCap selectedNCC) {
        if (tenThuoc.isEmpty()) return showAndFocus(txtTenThuoc, "Thiếu thông tin", "Vui lòng nhập Tên Thuốc.");
        if (hanSuDungDate == null) return showAndFocus(dpHanSuDung, "Thiếu thông tin", "Vui lòng chọn Hạn Sử Dụng.");
        if (giaBanStr.isEmpty()) return showAndFocus(txtGiaBan, "Thiếu thông tin", "Vui lòng nhập Giá Bán.");
        if (giaNhapStr.isEmpty()) return showAndFocus(txtGiaNhap, "Thiếu thông tin", "Vui lòng nhập Giá Nhập.");
        if (soLuongTonStr.isEmpty()) return showAndFocus(txtSoLuongTon, "Thiếu thông tin", "Vui lòng nhập Số Lượng Tồn.");
        if (selectedNCC == null) return showAndFocus(cboNhaCungCap, "Thiếu thông tin", "Vui lòng chọn Nhà Cung Cấp.");

        try {
            Double.parseDouble(giaBanStr);
        } catch (NumberFormatException e) {
            return showAndFocus(txtGiaBan, "Sai định dạng", "Giá bán phải là số hợp lệ.");
        }
        try {
            Double.parseDouble(giaNhapStr);
        } catch (NumberFormatException e) {
            return showAndFocus(txtGiaNhap, "Sai định dạng", "Giá nhập phải là số hợp lệ.");
        }
        try {
            Integer.parseInt(soLuongTonStr);
        } catch (NumberFormatException e) {
            return showAndFocus(txtSoLuongTon, "Sai định dạng", "Số lượng tồn phải là số nguyên.");
        }
        return true;
    }

    private boolean showAndFocus(Control control, String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
        control.requestFocus();
        return false;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        txtTenThuoc.clear();
        txtThanhPhan.clear();
        txtCongDung.clear();
        dpHanSuDung.setValue(null);
        txtGiaBan.clear();
        txtGiaNhap.clear();
        txtSoLuongTon.clear();
        cboNhaCungCap.setValue(null);
        txtHinhAnhPath.clear();
        selectedImageFile = null;
    }

    @FXML
    private void handleNhapFileAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file Excel chứa danh sách thuốc");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );

        Stage stage = (Stage) btnNhapFile.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) return;

        try (FileInputStream fis = new FileInputStream(selectedFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            dsThuoc.clear();

            // Bỏ qua hàng đầu tiên (header)
            if (rowIterator.hasNext()) rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String tenThuoc = getCellStringValue(row.getCell(0));
                String thanhPhan = getCellStringValue(row.getCell(1));
                String congDung = getCellStringValue(row.getCell(2));
                String hanSuDung = getCellStringValue(row.getCell(3));
                double giaBan = getCellDoubleValue(row.getCell(4));
                double giaNhap = getCellDoubleValue(row.getCell(5));
                int soLuongTon = (int) getCellDoubleValue(row.getCell(6));
                String maNCC = getCellStringValue(row.getCell(7));
                String hinhAnh = getCellStringValue(row.getCell(8));

                String maThuoc = thuocDAO.generateNextMaThuoc(tenThuoc);

                Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, thanhPhan, congDung, hanSuDung, giaBan, giaNhap, soLuongTon, maNCC, hinhAnh);
                dsThuoc.add(thuoc);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi đọc file", "Không thể đọc file Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "";
    }

    private double getCellDoubleValue(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    @FXML
    private void handleHuyAction() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleThemTatCaThuocAction() {
        if (dsThuoc.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Danh sách thuốc trống, vui lòng nhập file trước.");
            return;
        }
        
        int countSuccess = 0;
        for (Thuoc thuoc : dsThuoc) {
            // Có thể kiểm tra thuốc đã tồn tại hay chưa nếu muốn
            if (thuocDAO.addThuoc(thuoc)) {
                countSuccess++;
            }
        }
        
        showAlert(Alert.AlertType.INFORMATION, "Hoàn tất", "Đã thêm thành công " + countSuccess + " thuốc.");
        dsThuoc.clear(); // Xóa danh sách sau khi thêm xong
    }
}
