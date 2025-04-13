package application.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertData {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("INSERTING DATA...");

            stmt.execute("PRAGMA foreign_keys = ON;");
            
            stmt.executeUpdate("DELETE FROM Thuoc");

            stmt.executeUpdate("INSERT INTO Thuoc VALUES " +
            		"('TH-PAR-0001', 'Paracetamol 500mg', 'Paracetamol', 'Giảm đau, hạ sốt', '2026-12-31', 1500, 1000, 500, 'NCC001', '/application/assets/images/thuoc/paracetamol.jpg')," +
            		"('TH-AMO-0002', 'Amoxicillin 500mg', 'Amoxicillin', 'Kháng sinh điều trị nhiễm khuẩn', '2026-12-31', 3000, 2000, 300, 'NCC002', '/application/assets/images/thuoc/amoxicillin.jpg')," +
            		"('TH-LOR-0003', 'Loratadine 10mg', 'Loratadine', 'Chống dị ứng, giảm ngứa', '2026-12-31', 2500, 1500, 200, 'NCC003', '/application/assets/images/thuoc/loratadine.jpg')");



            System.out.println("INSERT DATA SUCCESS!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
