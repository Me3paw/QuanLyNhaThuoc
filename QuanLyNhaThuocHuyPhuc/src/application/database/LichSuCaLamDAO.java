package application.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.LichSuCaLam;

public class LichSuCaLamDAO {

    public List<LichSuCaLam> getLichSuCaLamByNhanVien(String maNhanVien) {
        List<LichSuCaLam> list = new ArrayList<>();
        String sql = "SELECT * FROM LichSuCaLam WHERE maNhanVien = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LichSuCaLam lichSuCaLam = new LichSuCaLam(
                        rs.getString("maNhanVien"),
                        rs.getDate("ngayLam").toLocalDate(),
                        rs.getInt("caLam")
                );
                list.add(lichSuCaLam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<LichSuCaLam> getAllLichSuCaLam() {
        List<LichSuCaLam> list = new ArrayList<>();
        String sql = "SELECT * FROM lichsucalam";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LichSuCaLam lichSu = new LichSuCaLam(
                    rs.getString("maNhanVien"),
                    rs.getDate("ngayLam").toLocalDate(),
                    rs.getInt("caLam")
                );
                list.add(lichSu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addLichSuCaLam(LichSuCaLam lichSuCaLam) {
        String sql = "INSERT INTO LichSuCaLam (maNhanVien, ngayLam, caLam) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lichSuCaLam.getMaNhanVien());
            ps.setDate(2, Date.valueOf(lichSuCaLam.getNgayLam()));
            ps.setInt(3, lichSuCaLam.getCaLam());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
