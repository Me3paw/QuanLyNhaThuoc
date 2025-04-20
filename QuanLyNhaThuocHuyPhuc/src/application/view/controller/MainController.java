package application.view.controller;
import application.model.NhanVien;
import application.model.TaiKhoan;
import application.util.SessionManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController {
 
    @FXML
    private StackPane contentArea;
    
    // Hóa đơn
    @FXML
    private HBox hoaDonItem;
    @FXML
    private VBox hoaDonSubMenu;
    
     // Thống kê
    @FXML
    private HBox thongKeItem;
    @FXML
    private VBox thongKeSubMenu;
   
    // Khách hàng
    @FXML
    private HBox khachHangItem;
    @FXML
    private VBox khachHangSubMenu;

    // Thuốc
    @FXML
    private HBox thuocItem;
    @FXML
    private VBox thuocSubMenu;
    
    // Quản lí nhân viên
    @FXML
    private HBox nhanVienItem;
    @FXML
    private VBox nhanVienSubMenu;
   
    
    // Ca làm việc
    @FXML
    private HBox caLamViecItem;
    @FXML
    private VBox caLamViecSubMenu;
    
    // Account
    @FXML
    private HBox accountItem;

    
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;

    // Sidebar
    @FXML
    private VBox subMenu;  // VBox chứa các mục submenu

    // Nút bật/tắt submenu
    @FXML
    private Button toggleButton;
    
    @FXML
    private HBox chiaCaButton; // Button for "Chia ca"
    @FXML
    private HBox thongKeCaButton;

   @FXML
	public void initialize() {
	    loadPage("POS.fxml"); // Load the default page
	    TaiKhoan loggedInUser = SessionManager.getLoggedInUser();
	    NhanVien loggedInNhanVien = SessionManager.getLoggedInNhanVien();
	
	    if (loggedInUser != null && loggedInNhanVien != null) {
	        // Set the name and role using the retrieved data
	        nameLabel.setText(loggedInNhanVien.getTenNhanVien());
	        roleLabel.setText(loggedInUser.getVaiTro());
	
	        // Check if the user is not an admin
	        if (!"admin".equalsIgnoreCase(loggedInUser.getVaiTro())) {
	            // Disable "Quản lí nhân viên" and its submenu
	            nhanVienItem.setDisable(true);
	            nhanVienSubMenu.setDisable(true);
	            chiaCaButton.setDisable(true);
	            thongKeCaButton.setDisable(true);

	        }
	    } else {
	        // Fallback values if no session data is available
	        nameLabel.setText("Unknown User");
	        roleLabel.setText("Unknown Role");
	    }
	}





    private void loadPage(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/" + fxmlFileName));
            StackPane view = loader.load();  // Dùng StackPane thay vì AnchorPane
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void resetSidebarStyle() {
        hoaDonItem.getStyleClass().remove("sidebar-item-selected");
        accountItem.getStyleClass().remove("sidebar-item-selected");
        thuocItem.getStyleClass().remove("sidebar-item-selected");
        khachHangItem.getStyleClass().remove("sidebar-item-selected");
        nhanVienItem.getStyleClass().remove("sidebar-item-selected");
        caLamViecItem.getStyleClass().remove("sidebar-item-selected");
    }

    // Toggle submenus (ẩn hiện các submenu)
    private void toggleSubMenu(VBox subMenu) {
        boolean isVisible = subMenu.isVisible();
        subMenu.setVisible(!isVisible);
        subMenu.setManaged(!isVisible);
    }

    // Toggle sidebar visibility
    @FXML
    private void toggleSidebar() {
        boolean isVisible = subMenu.isVisible();
        subMenu.setVisible(!isVisible);
        subMenu.setManaged(!isVisible);
    }
    
    //HoaDon
    @FXML
    private void handleHoaDonClick() {
        resetSidebarStyle();
        hoaDonItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(hoaDonSubMenu);
    }

    @FXML
    private void handleLapHoaDonClick() {
        loadPage("POS.fxml");
        // Load "Lập Hóa Đơn" view
    }

    @FXML
    private void handleTimKiemHoaDonClick() {
        loadPage("PurchaseLookup.fxml"); // Load "Tìm Kiếm Hóa Đơn" view
    }


    @FXML
    private void handleDoiDangNhap() {
        loadPage("DoiDangNhap.fxml"); // Load "Phân quyền Tài Khoản" view
    }
    @FXML
    private void handlePhanQuyenClick() {
        loadPage("PhanQuyen.fxml"); // Load "Phân quyền Tài Khoản" view
    }

   
    //Thuoc
    @FXML
    private void handleThuocClick() {
        resetSidebarStyle();
        thuocItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(thuocSubMenu);
    }

    @FXML
    private void handleDanhSachThuocClick() {
        loadPage("danhsachthuoc.fxml"); // Load "Danh Sách Thuốc" view
    }

    @FXML
    private void handleTimThuocClick() {
        loadPage("SearchMedicine.fxml"); // Load "Tìm Thuốc" view
    }

    @FXML
    private void handleThemThuocClick() {
        loadPage("ThemThuoc.fxml"); // Load "Thêm Thuốc" view
    }
    //ThongKe
    @FXML
    private void handleThongKeClick() {
        resetSidebarStyle();
        thongKeItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(thongKeSubMenu);
    }
    @FXML
    private void handleDoanhThuClick() {
        loadPage("DoanhThu.fxml"); 
    }
    //CaLamViec
    @FXML
    private void handleCaLamViecClick() {
        resetSidebarStyle();
        caLamViecItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(caLamViecSubMenu);
    }
    @FXML
    private void handleSignOut() {
        // Clear the session
        SessionManager.clearSession();

        try {
            // Load the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/login.fxml"));
            Parent loginRoot = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) nameLabel.getScene().getWindow();

            // Set the login scene
            Scene loginScene = new Scene(loginRoot);
            loginScene.getStylesheets().add(getClass().getResource("/application/assets/css/Login.css").toExternalForm());
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleThongTinCaLamViecClick() {
        loadPage("ThongTinCaLamViec.fxml"); 
    }

    @FXML
    private void handleChiaCaLamViecClick() {
        loadPage("ShiftManaging.fxml");
    }
    @FXML
    private void handleLichSuCaLamViecClick() {
        loadPage("LichSuCaLamViec.fxml");
    }

    @FXML
    private void handleNhanVienClick() {
        resetSidebarStyle();
        nhanVienItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(nhanVienSubMenu);
    }

    @FXML
    private void handleThemNhanVienClick() {
        loadPage("ThemNhanVien.fxml"); 
    }
    @FXML
    private void handleCapNhatNhanVienClick() {
    	loadPage("CapNhatNhanVien.fxml"); 
    }
    @FXML
    private void handleTimNhanVienClick() {
    	loadPage("TimNhanVien.fxml"); 
    }
    
    
    
    //KhachHang
    @FXML
    private void handleKhachHangClick() {
        resetSidebarStyle();
        khachHangItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(khachHangSubMenu);
    }

    @FXML
    private void handleDanhSachKhachHangClick() {
        loadPage("DanhSachKhachHang.fxml"); // Load "Danh Sách Khách Hàng" view
    }

 
    
 // Phương thức này sẽ được gọi sau khi người dùng đăng nhập thành công
    public void updateUserInfo(String tenNhanVien, String vaiTro) {
        nameLabel.setText(tenNhanVien);  // Cập nhật tên nhân viên
        roleLabel.setText(vaiTro);  // Cập nhật vai trò của nhân viên
    }

    
}