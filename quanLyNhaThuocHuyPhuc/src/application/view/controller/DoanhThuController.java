//Controller để load fake data nhằm test UI như nào chứ không biết làm :[ 


package application.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.Locale;
import java.util.Random;

public class DoanhThuController {

    // --- KPI Cards ---
    @FXML private Label thuocKhoValueLabel;
    @FXML private Label khachHangValueLabel;
    @FXML private Label soDuQuyValueLabel;
    @FXML private Label nhanVienValueLabel;

    // --- Charts Lớn ---
    @FXML private ComboBox<String> doanhThuYearCombo;
    @FXML private BarChart<String, Number> doanhThuChart;
    @FXML private CategoryAxis doanhThuXAxis;
    @FXML private NumberAxis doanhThuYAxis;

    @FXML private ComboBox<String> loiNhuanYearCombo;
    @FXML private LineChart<String, Number> loiNhuanChart;
    @FXML private CategoryAxis loiNhuanXAxis;
    @FXML private NumberAxis loiNhuanYAxis;

    // --- Mini KPI Cards ---
    @FXML private Label nhanVienMoiValueLabel;
    @FXML private Label nhanVienMoiPercentageLabel;
    @FXML private Label doanhThuMiniValueLabel;
    @FXML private Label doanhThuMiniPercentageLabel;
    @FXML private Label chiPhiMiniValueLabel;
    @FXML private Label chiPhiMiniPercentageLabel;
    @FXML private Label loiNhuanMiniValueLabel;
    @FXML private Label loiNhuanMiniPercentageLabel;

    // --- Mini Bar Charts ---
    @FXML private BarChart<String, Number> nhanVienMoiChart;
    @FXML private BarChart<String, Number> doanhThuMiniChart;
    @FXML private BarChart<String, Number> chiPhiMiniChart;
    @FXML private BarChart<String, Number> loiNhuanMiniChart;


    private final ObservableList<String> monthNames = FXCollections.observableArrayList(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    );
    private final ObservableList<String> monthNumbers = FXCollections.observableArrayList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    );

    @FXML
    public void initialize() {
        // Thiết lập trục X cho charts lớn
        doanhThuXAxis.setCategories(monthNames);
        loiNhuanXAxis.setCategories(monthNames);

        //Thiết lập các năm trong ComboBox
        ObservableList<String> years = FXCollections.observableArrayList("2024", "2023", "2022");
        doanhThuYearCombo.setItems(years);
        loiNhuanYearCombo.setItems(years);
        doanhThuYearCombo.getSelectionModel().selectFirst();
        loiNhuanYearCombo.getSelectionModel().selectFirst();

        // Load dữ liệu giả lập ban đầu cho năm mặc định (2024)
        loadFakeDataForYear("2024");

        //Thêm listeners để load lại dữ liệu khi đổi năm
        doanhThuYearCombo.setOnAction(event -> loadFakeDataForYear(doanhThuYearCombo.getValue()));
        loiNhuanYearCombo.setOnAction(event -> loadFakeDataForYear(loiNhuanYearCombo.getValue()));
    }


    private void loadFakeDataForYear(String selectedYear) {
        if (selectedYear == null) return; 

        System.out.println("Loading fake data for year: " + selectedYear);


        Random random = new Random(); 
        int yearFactor = selectedYear.hashCode() % 100; 

        loadKpiData(random, yearFactor);

        loadMiniKpiData(random, yearFactor);

        loadLargeChartData(random, yearFactor);

        loadMiniChartData(nhanVienMoiChart, random, 1, 5 + yearFactor / 20);
        loadMiniChartData(doanhThuMiniChart, random, 40000 + yearFactor * 50, 80000 + yearFactor * 100);
        loadMiniChartData(chiPhiMiniChart, random, 5000 + yearFactor * 20, 15000 + yearFactor * 50);
        loadMiniChartData(loiNhuanMiniChart, random, 30000 + yearFactor * 30, 60000 + yearFactor * 80);
    }


    private void loadKpiData(Random random, int yearFactor) {
        thuocKhoValueLabel.setText(String.format("%,d", 4500 + random.nextInt(500) + yearFactor));
        khachHangValueLabel.setText(String.format("%,d", 1500 + random.nextInt(200) + yearFactor));
        soDuQuyValueLabel.setText(String.format("%,d", 280000 + random.nextInt(50000) + yearFactor * 100));
        nhanVienValueLabel.setText(String.valueOf(28 + random.nextInt(5) + yearFactor / 20));
    }


    private void loadMiniKpiData(Random random, int yearFactor) {
        int nvMoi = 2 + random.nextInt(3) + yearFactor / 30;
        nhanVienMoiValueLabel.setText(String.valueOf(nvMoi));
        setPercentageLabel(nhanVienMoiPercentageLabel, 5 + random.nextInt(10) - yearFactor / 15);

        long dtMini = 55000 + random.nextInt(10000) + yearFactor * 100;
        doanhThuMiniValueLabel.setText(formatCurrencyRough(dtMini));
        setPercentageLabel(doanhThuMiniPercentageLabel, 15 + random.nextInt(10) - yearFactor / 10);

        long cpMini = 8000 + random.nextInt(4000) + yearFactor * 50;
        chiPhiMiniValueLabel.setText(formatCurrencyRough(cpMini));
        setPercentageLabel(chiPhiMiniPercentageLabel, -2 - random.nextInt(8) + yearFactor / 12);

        long lnMini = dtMini - cpMini;
        loiNhuanMiniValueLabel.setText(formatCurrencyRough(lnMini));
        setPercentageLabel(loiNhuanMiniPercentageLabel, -10 - random.nextInt(10) + yearFactor / 8);
    }


    private void loadLargeChartData(Random random, int yearFactor) {
         ObservableList<XYChart.Data<String, Number>> thuNhapData = FXCollections.observableArrayList();
         ObservableList<XYChart.Data<String, Number>> chiPhiData = FXCollections.observableArrayList();
         ObservableList<XYChart.Data<String, Number>> tongDoanhThuData = FXCollections.observableArrayList();
         ObservableList<XYChart.Data<String, Number>> tongLoiNhuanData = FXCollections.observableArrayList();

         for (String month : monthNames) { 
             int monthFactor = month.hashCode() % 50;
             double baseThuNhap = Math.max(5, 40 + random.nextDouble() * 50 + yearFactor / 2.0 + monthFactor / 3.0);
             double baseChiPhi = Math.max(5, 20 + random.nextDouble() * 40 + yearFactor / 3.0 + monthFactor / 4.0);
             baseChiPhi = Math.min(baseChiPhi, baseThuNhap * 0.9); //

             thuNhapData.add(new XYChart.Data<>(month, baseThuNhap));
             chiPhiData.add(new XYChart.Data<>(month, baseChiPhi));

             double baseTongDT = Math.max(10, 50 + random.nextDouble() * 20 + yearFactor / 2.5 + monthFactor / 3.5);
             double baseTongLN = baseTongDT * (0.6 + random.nextDouble() * 0.3) - yearFactor / 4.0 - monthFactor / 5.0;
             baseTongLN = Math.max(baseTongLN, baseTongDT * 0.1); 

             tongDoanhThuData.add(new XYChart.Data<>(month, baseTongDT));
             tongLoiNhuanData.add(new XYChart.Data<>(month, baseTongLN));
         }

         XYChart.Series<String, Number> thuNhapSeries = new XYChart.Series<>(thuNhapData); thuNhapSeries.setName("Thu nhập");
         XYChart.Series<String, Number> chiPhiSeries = new XYChart.Series<>(chiPhiData); chiPhiSeries.setName("Chi Phí");
         doanhThuChart.getData().setAll(thuNhapSeries, chiPhiSeries);

         XYChart.Series<String, Number> tongDoanhThuSeries = new XYChart.Series<>(tongDoanhThuData); tongDoanhThuSeries.setName("Tổng Doanh Thu");
         XYChart.Series<String, Number> tongLoiNhuanSeries = new XYChart.Series<>(tongLoiNhuanData); tongLoiNhuanSeries.setName("Tổng Lợi Nhuận");
         loiNhuanChart.getData().setAll(tongDoanhThuSeries, tongLoiNhuanSeries);
    }



    private void loadMiniChartData(BarChart<String, Number> chart, Random random, double min, double max) {
         if (chart == null) return; 

         ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
         for (String monthNumber : monthNumbers) { 
             data.add(new XYChart.Data<>(monthNumber, Math.max(0, min + random.nextDouble() * (max - min)))); 
         }

         XYChart.Series<String, Number> series = new XYChart.Series<>(data);

         chart.getData().setAll(series); // Dùng setAll để cập nhật/ghi đè dữ liệu cũ

    }


     private String formatCurrencyRough(long value) {
         return String.format(Locale.GERMANY, "đ%,d", value);

     }



    private void setPercentageLabel(Label label, double percentage) {
        label.getStyleClass().removeAll("percentage-up", "percentage-down", "percentage-neutral");
        String prefix = (percentage >= 0) ? "+" : "";
        label.setText(String.format("%s%.0f%%", prefix, percentage));

        if (percentage > 0) {
            label.getStyleClass().add("percentage-up");
        } else if (percentage < 0) {
            label.getStyleClass().add("percentage-down");
        } else {
             label.getStyleClass().add("percentage-neutral"); 
        }
    }
}