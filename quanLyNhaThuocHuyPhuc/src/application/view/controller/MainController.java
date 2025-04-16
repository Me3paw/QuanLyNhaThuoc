package application.view.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane contentArea;

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
    private HBox quanLyNhanVienItem;
    @FXML
    private VBox quanLyNhanVienSubMenu;

    // Ca làm việc
    @FXML
    private HBox caLamViecItem;
    @FXML
    private VBox caLamViecSubMenu;

    @FXML
    public void initialize() {
        loadPage("POS.fxml"); // Load default page
    }

    private void loadPage(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/" + fxmlFileName));
            AnchorPane view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetSidebarStyle() {
        hoaDonItem.getStyleClass().remove("sidebar-item-selected");
        //thongKeItem.getStyleClass().remove("sidebar-item-selected");
       // khachHangItem.getStyleClass().remove("sidebar-item-selected");
        ///thuocItem.getStyleClass().remove("sidebar-item-selected");
       // quanLyNhanVienItem.getStyleClass().remove("sidebar-item-selected");
        //caLamViecItem.getStyleClass().remove("sidebar-item-selected");
    }

    @FXML
    private void handleHoaDonClick() {
        resetSidebarStyle();
        hoaDonItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(hoaDonSubMenu);
    }

    @FXML
    private void handleLapHoaDonClick() {
        loadPage("POS.fxml"); // Load Lập hóa đơn view
    }

    @FXML
    private void handleTimKiemHoaDonClick() {
    	loadPage("PurchaseLookup.fxml");
    }
    @FXML
    private void handleKhachHangClick() { 	
        resetSidebarStyle();
        khachHangItem.getStyleClass().add("sidebar-item-selected");
        toggleSubMenu(khachHangSubMenu);
    }

    @FXML
    private void handleThemKhachHangClick() { 
        loadPage("AddCustomer.fxml"); 
    }
    @FXML
    private void handleTimKiemKhachHangClick() { 
        loadPage("CustomerLookup.fxml");
    }

    // Repeat handleClick for each menu item...

    private void toggleSubMenu(VBox subMenu) {
        boolean isVisible = subMenu.isVisible();
        subMenu.setVisible(!isVisible);
        subMenu.setManaged(!isVisible);
    }
}
