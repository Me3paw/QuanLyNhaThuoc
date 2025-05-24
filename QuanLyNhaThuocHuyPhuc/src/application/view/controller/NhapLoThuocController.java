package application.view.controller;

import entity.LoThuoc;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.database.LoThuocDAO;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

public class NhapLoThuocController {

    @FXML private TextField txtMaLoThuoc;
    @FXML private TextField txtMaThuoc;
    @FXML private TextField txtSoLuongNhap;
    @FXML private DatePicker dpNgayNhap;
    @FXML private DatePicker dpHanSuDung;
    @FXML private TextField txtGiaNhap;
    @FXML private TextField txtGhiChu;

    @FXML private Button btnThemMoi;
    @FXML private Button btnCapNhat;
    @FXML private Button btnXoa;
    @FXML private Button btnTaiExcel;

    @FXML private TableView<LoThuoc> tableLoThuoc;

    @FXML private TableColumn<LoThuoc, String> colMaLoThuoc;
    @FXML private TableColumn<LoThuoc, String> colMaThuoc;
    @FXML private TableColumn<LoThuoc, Integer> colSoLuongNhap;
    @FXML private TableColumn<LoThuoc, LocalDate> colNgayNhap;
    @FXML private TableColumn<LoThuoc, LocalDate> colHanSuDung;
    @FXML private TableColumn<LoThuoc, Double> colGiaNhap;
    @FXML private TableColumn<LoThuoc, String> colGhiChu;

    private LoThuocDAO loThuocDAO;
    private ObservableList<LoThuoc> loThuocList;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        loThuocDAO = new LoThuocDAO();
        loThuocList = FXCollections.observableArrayList();

        // Thiết lập TableView columns
        colMaLoThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaLoThuoc()));
        colMaThuoc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaThuoc()));
        colSoLuongNhap.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSoLuongNhap()).asObject());
        colNgayNhap.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNgayNhap()));
        colHanSuDung.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHanSuDung()));
        colGiaNhap.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGiaNhap()).asObject());
        colGhiChu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGhiChu()));

        loadData();

        // Chọn dòng trong TableView để điền vào form
        tableLoThuoc.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> fillForm(newValue));
    }

    private void loadData() {
        loThuocList.clear();
        loThuocList.addAll(loThuocDAO.getAll());
        tableLoThuoc.setItems(loThuocList);
    }

    private void fillForm(LoThuoc lt) {
        if (lt == null) {
            clearForm();
            return;
        }
        txtMaLoThuoc.setText(lt.getMaLoThuoc());
        txtMaThuoc.setText(lt.getMaThuoc());
        txtSoLuongNhap.setText(String.valueOf(lt.getSoLuongNhap()));
        dpNgayNhap.setValue(lt.getNgayNhap());
        dpHanSuDung.setValue(lt.getHanSuDung());
        txtGiaNhap.setText(String.valueOf(lt.getGiaNhap()));
        txtGhiChu.setText(lt.getGhiChu());
    }

    @FXML
    private void handleThemMoi(ActionEvent event) {
        try {
            LoThuoc lt = getFormData();
            if (loThuocDAO.insert(lt)) {
                showAlert(Alert.AlertType.INFORMATION, "Thêm lô thuốc thành công!");
                loadData();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Thêm lô thuốc thất bại! Mã lô thuốc có thể đã tồn tại.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Số lượng nhập hoặc giá nhập không hợp lệ.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleCapNhat(ActionEvent event) {
        try {
            LoThuoc lt = getFormData();
            if (loThuocDAO.update(lt)) {
                showAlert(Alert.AlertType.INFORMATION, "Cập nhật lô thuốc thành công!");
                loadData();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Cập nhật lô thuốc thất bại! Kiểm tra mã lô thuốc.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Số lượng nhập hoặc giá nhập không hợp lệ.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleXoa(ActionEvent event) {
        String maLoThuoc = txtMaLoThuoc.getText();
        if (maLoThuoc.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn lô thuốc để xoá!");
            return;
        }
        boolean confirm = confirmDialog("Bạn có chắc chắn muốn xoá lô thuốc này?");
        if (!confirm) return;

        if (loThuocDAO.delete(maLoThuoc)) {
            showAlert(Alert.AlertType.INFORMATION, "Xoá lô thuốc thành công!");
            loadData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Xoá lô thuốc thất bại!");
        }
    }

    @FXML
    private void handleTaiExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file Excel (.xlsx)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        Stage stage = (Stage) btnTaiExcel.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            importExcel(file);
        }
    }

    private void importExcel(File file) {
        int countInserted = 0;
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) continue; // Bỏ qua header

                try {
                    String maLoThuoc = getCellString(row.getCell(0));
                    String maThuoc = getCellString(row.getCell(1));

                    int soLuongNhap = parseCellToInt(row.getCell(2));
                    LocalDate ngayNhap = parseCellToDate(row.getCell(3));
                    LocalDate hanSuDung = parseCellToDate(row.getCell(4));
                    double giaNhap = parseCellToDouble(row.getCell(5));
                    String ghiChu = getCellString(row.getCell(6));

                    if (maLoThuoc.isEmpty() || maThuoc.isEmpty() || ngayNhap == null || hanSuDung == null) {
                        continue; // Bỏ qua dòng không hợp lệ
                    }

                    LoThuoc lt = new LoThuoc(maLoThuoc, maThuoc, soLuongNhap, ngayNhap, hanSuDung, giaNhap, ghiChu);
                    if (loThuocDAO.insert(lt)) {
                        countInserted++;
                    }
                } catch (Exception e) {
                    // Bỏ qua dòng lỗi và tiếp tục
                    System.err.println("Lỗi dòng " + (row.getRowNum()+1) + ": " + e.getMessage());
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Nhập file Excel thành công! Đã thêm " + countInserted + " lô thuốc.");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi khi nhập file Excel: " + e.getMessage());
        }
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            // Nếu là số kiểu ngày thì format sang chuỗi yyyy-MM-dd
            if (DateUtil.isCellDateFormatted(cell)) {
                LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
                return date.format(formatter);
            }
            double val = cell.getNumericCellValue();
            if (val == Math.floor(val)) {
                return String.valueOf((long) val);
            } else {
                return String.valueOf(val);
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }
        return "";
    }

    private int parseCellToInt(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            return Integer.parseInt(cell.getStringCellValue().trim());
        }
        throw new NumberFormatException("Giá trị không phải số nguyên.");
    }

    private double parseCellToDouble(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            return Double.parseDouble(cell.getStringCellValue().trim());
        }
        throw new NumberFormatException("Giá trị không phải số thực.");
    }

    private LocalDate parseCellToDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                String val = cell.getStringCellValue().trim();
                return LocalDate.parse(val, formatter);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày không hợp lệ: " + e.getParsedString());
        }
        return null;
    }

    private LoThuoc getFormData() {
        String maLoThuoc = txtMaLoThuoc.getText().trim();
        String maThuoc = txtMaThuoc.getText().trim();

        if (maLoThuoc.isEmpty() || maThuoc.isEmpty()) {
            throw new IllegalArgumentException("Mã lô thuốc và mã thuốc không được để trống.");
        }

        int soLuongNhap;
        double giaNhap;
        try {
            soLuongNhap = Integer.parseInt(txtSoLuongNhap.getText().trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Số lượng nhập phải là số nguyên.");
        }
        try {
            giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Giá nhập phải là số thực.");
        }

        LocalDate ngayNhap = dpNgayNhap.getValue();
        LocalDate hanSuDung = dpHanSuDung.getValue();

        if (ngayNhap == null || hanSuDung == null) {
            throw new IllegalArgumentException("Ngày nhập và hạn sử dụng không được để trống.");
        }

        String ghiChu = txtGhiChu.getText().trim();

        return new LoThuoc(maLoThuoc, maThuoc, soLuongNhap, ngayNhap, hanSuDung, giaNhap, ghiChu);
    }

    private void clearForm() {
        txtMaLoThuoc.clear();
        txtMaThuoc.clear();
        txtSoLuongNhap.clear();
        dpNgayNhap.setValue(null);
        dpHanSuDung.setValue(null);
        txtGiaNhap.clear();
        txtGhiChu.clear();
    }

    private void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean confirmDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
