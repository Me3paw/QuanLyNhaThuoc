
package application;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage splashStage) throws Exception {
        // 1. Tạo hình ảnh splash
        Image image = new Image(getClass().getResource("/application/assets/images/splash2.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(350);

        // 2. Tạo thanh tiến trình đẹp
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(500);
        progressBar.setPrefWidth(500);
       
        progressBar.setLayoutY(335);  // Gần đáy ảnh

        progressBar.setStyle( 
            "-fx-accent: #ED6214;" +
            "-fx-control-inner-background: transparent;" +
            "-fx-background-color: rgba(255,255,255,0.3);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" 
        );

        // 3. Gộp vào Pane
        Pane splashRoot = new Pane(imageView, progressBar);

        // 4. Fade in splash
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), splashRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // 5. Tạo scene splash
        Scene splashScene = new Scene(splashRoot, 500, 350);
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(splashScene);
        splashStage.show();

        // 6. Tạo và lưu timeline riêng
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(60), e -> {
            // Cập nhật thanh tiến trình trên UI thread
            javafx.application.Platform.runLater(() -> {
                double progress = progressBar.getProgress() + 0.04;
                if (progress <= 1.0) {
                    progressBar.setProgress(progress);
                } else {
                    timeline.stop(); // Dừng timeline khi thanh tiến trình đầy

                    try {
                        // Load màn hình login
                        Parent loginRoot = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
                        Scene loginScene = new Scene(loginRoot,800, 600);
                        loginScene.getStylesheets().add(getClass().getResource("assets/css/Login.css").toExternalForm());

                        Stage loginStage = new Stage(); // Mở stage mới cho login
                        loginStage.setTitle("Màn Hình Đăng Nhập");
                        loginStage.setScene(loginScene);
                        loginStage.show();

                        splashStage.close(); // Đóng splash
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
