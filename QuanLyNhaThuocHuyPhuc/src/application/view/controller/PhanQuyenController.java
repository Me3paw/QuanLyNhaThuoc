package application.view.controller;

import application.database.TaiKhoanDAO;
import application.util.SessionManager;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; 
import javafx.collections.transformation.SortedList;   
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate; 

public class PhanQuyenController {

    @FXML private TextField txtSearch;
    @FXML private TableView<TaiKhoan> tableTaiKhoan; 
    @FXML private TableColumn<TaiKhoan, Boolean> colChon;
    @FXML private TableColumn<TaiKhoan, String> colMaNhanVien; 
    @FXML private TableColumn<TaiKhoan, String> colTenNhanVien;
    @FXML private TableColumn<TaiKhoan, String> colSdtNV;       
    @FXML private TableColumn<TaiKhoan, String> colVaiTro;      
    @FXML private ComboBox<String> cboNewRole; 
    @FXML private Button btnApplyRole; 
    @FXML private Button btnChonTatCa; 

    private ObservableList<TaiKhoan> masterData = FXCollections.observableArrayList(); 
    private FilteredList<TaiKhoan> filteredData;
    private SortedList<TaiKhoan> sortedData;
    private TaiKhoanDAO taiKhoanDAO;
    private final ObservableList<String> vaiTroOptions = FXCollections.observableArrayList("Admin", "Nhân viên");
    private boolean isAllSelected = false;

    @FXML
    public void initialize() {
        taiKhoanDAO = new TaiKhoanDAO(); 

        setupTableColumns(); 
        cboNewRole.setItems(vaiTroOptions); 
        loadTaiKhoanData(); 
        setupSearchFilter(); 
    }


    private void setupTableColumns() {
        colChon.setCellValueFactory(param -> param.getValue().selectedProperty());
        colChon.setCellFactory(column -> new TableCell<TaiKhoan, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            {
                checkBox.setOnAction(event -> {
                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                        TaiKhoan taiKhoan = getTableView().getItems().get(getIndex());
                        if (taiKhoan != null) {
                            taiKhoan.setSelected(checkBox.isSelected());
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    if(tk != null) {
                        checkBox.setSelected(tk.isSelected());
                        setGraphic(checkBox);
                        setAlignment(Pos.CENTER);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        colChon.setSortable(false);

        // Cột Mã Nhân Viên (lấy từ đối tượng NhanVien trong TaiKhoan)
        colMaNhanVien.setCellValueFactory(cellData -> {
            NhanVien nv = cellData.getValue().getNhanVien();
            return new SimpleStringProperty(nv != null ? nv.getMaNhanVien() : "N/A");
        });

        // Cột Tên Nhân Viên
        colTenNhanVien.setCellValueFactory(cellData -> {
            NhanVien nv = cellData.getValue().getNhanVien();
            return new SimpleStringProperty(nv != null ? nv.getTenNhanVien() : "Chưa liên kết NV");
        });

        // Cột Số Điện Thoại Nhân Viên
        colSdtNV.setCellValueFactory(cellData -> {
            NhanVien nv = cellData.getValue().getNhanVien();
            return new SimpleStringProperty(nv != null ? nv.getSoDienThoai() : "N/A");
        });

        // Cột Vai Trò (lấy trực tiếp từ TaiKhoan)
        colVaiTro.setCellValueFactory(new PropertyValueFactory<>("vaiTro"));
    }


    private void loadTaiKhoanData() {
        List<TaiKhoan> accountList = taiKhoanDAO.getAllTaiKhoanJoinNhanVien(); 
        if (accountList != null) {
            accountList.forEach(tk -> tk.setSelected(false)); 
            masterData.setAll(accountList); // Cập nhật danh sách gốc
        } else {
            masterData.clear(); // Xóa dữ liệu nếu DAO trả về null
            showAlert("Lỗi", "Không thể tải dữ liệu tài khoản.", Alert.AlertType.ERROR);
        }
         tableTaiKhoan.refresh(); 
    }

    private void setupSearchFilter() {
        filteredData = new FilteredList<>(masterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(createSearchPredicate(newValue));
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableTaiKhoan.comparatorProperty());
        tableTaiKhoan.setItems(sortedData);
    }


    private Predicate<TaiKhoan> createSearchPredicate(String searchText) {
        return taiKhoan -> {
            if (searchText == null || searchText.isEmpty()) {
                return true; 
            }
            String lowerCaseFilter = searchText.toLowerCase();
            NhanVien nv = taiKhoan.getNhanVien();

            if (nv != null && nv.getMaNhanVien() != null && nv.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            if (nv != null && nv.getTenNhanVien() != null && nv.getTenNhanVien().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            if (nv != null && nv.getSoDienThoai() != null && nv.getSoDienThoai().contains(lowerCaseFilter)) { // SĐT không cần lowercase
                return true;
            }
            
            return false; 
        };
    }


    @FXML
    private void handleSearch() {
        filteredData.setPredicate(createSearchPredicate(txtSearch.getText()));
        btnChonTatCa.setText("Chọn tất cả");
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleResetSearch() {
        txtSearch.clear();
        
    }


	@FXML
	private void handleApplyRole() {
	    String selectedRole = cboNewRole.getValue();
	    if (selectedRole == null) {
	        showAlert("Chưa chọn vai trò", "Vui lòng chọn vai trò mới để áp dụng!", Alert.AlertType.WARNING);
	        return;
	    }
	
	    List<TaiKhoan> accountsToUpdate = new ArrayList<>();
	    for (TaiKhoan tk : masterData) {
	        if (tk.isSelected()) {
	            accountsToUpdate.add(tk);
	        }
	    }
	
	    if (accountsToUpdate.isEmpty()) {
	        showAlert("Chưa chọn tài khoản", "Vui lòng chọn ít nhất một tài khoản bằng cách tích vào ô 'Chọn'.", Alert.AlertType.WARNING);
	        return;
	    }
	
	    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Xác nhận cập nhật");
	    confirmAlert.setHeaderText("Áp dụng vai trò '" + selectedRole + "' cho " + accountsToUpdate.size() + " tài khoản?");
	    confirmAlert.setContentText("Bạn có chắc chắn muốn thực hiện thay đổi này?");
	    if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
	        return;
	    }
	
	    int successCount = 0;
	    int failCount = 0;
	    StringBuilder failedInfo = new StringBuilder("Các tài khoản cập nhật thất bại:\n");
	
	    for (TaiKhoan tk : accountsToUpdate) {
	        if (!selectedRole.equals(tk.getVaiTro())) {
	            boolean success = taiKhoanDAO.updateTaiKhoanRole(tk.getMaTaiKhoan(), selectedRole);
	            if (success) {
	                tk.setVaiTro(selectedRole);
	                successCount++;
	
	                // Update session if the logged-in user is updated
	                TaiKhoan loggedInUser = SessionManager.getLoggedInUser();
	                if (loggedInUser != null && loggedInUser.getMaTaiKhoan().equals(tk.getMaTaiKhoan())) {
	                    SessionManager.setLoggedInUser(tk);
	
	                    // Refresh MainController UI
	                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/MainLayout.fxml"));
	                    try {
	                        Parent root = loader.load();
	                        MainController mainController = loader.getController();
	                        mainController.updateUserInfo(tk.getNhanVien().getTenNhanVien(), tk.getVaiTro());
	                        tableTaiKhoan.getScene().setRoot(root); 
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            } else {
	                failCount++;
	                String identifier = tk.getTenDangNhap() != null ? tk.getTenDangNhap() : tk.getMaTaiKhoan();
	                failedInfo.append("- ").append(identifier).append("\n");
	            }
	        }
	        tk.setSelected(false);
	    }
	
	    tableTaiKhoan.refresh();
	
	    String message = "";
	    if (successCount > 0) {
	    	message += "Đã cập nhật thành công vai trò cho " + successCount + " tài khoản.\n";
	    	btnChonTatCa.setText("Chọn tất cả");
	    }
	    if (failCount > 0) message += "Cập nhật thất bại cho " + failCount + " tài khoản.\n" + failedInfo.toString();
	    if (message.isEmpty() && !accountsToUpdate.isEmpty()) message = "Không có tài khoản nào cần cập nhật (vai trò đã giống nhau).";
	    else if (message.isEmpty()) message = "Không có tài khoản nào được chọn.";
	
	    showAlert("Kết quả", message, failCount > 0 ? Alert.AlertType.WARNING : Alert.AlertType.INFORMATION);
	
	    cboNewRole.getSelectionModel().clearSelection();
	    cboNewRole.setPromptText("Chọn vai trò");
	    tableTaiKhoan.getSelectionModel().clearSelection();
	}
	@FXML private  void handleChonTatCa() {
		isAllSelected = !isAllSelected;

	    for (TaiKhoan tk : filteredData) {
	        tk.setSelected(isAllSelected);
	    }

	    tableTaiKhoan.refresh();

	    // Cập nhật lại văn bản trên nút
	    btnChonTatCa.setText(isAllSelected ? "Bỏ chọn tất cả" : "Chọn tất cả");
	}
}