package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/application/view/MainLayout.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/application/assets/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Pharma POS - Huy Phuc");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
