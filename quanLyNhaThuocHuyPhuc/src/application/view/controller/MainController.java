package application.view.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MainController {
    @FXML
    private AnchorPane contentArea;

    @FXML
    public void initialize() {
        loadPOSPage();
    }

    public void loadPOSPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/POS.fxml"));
            AnchorPane view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
