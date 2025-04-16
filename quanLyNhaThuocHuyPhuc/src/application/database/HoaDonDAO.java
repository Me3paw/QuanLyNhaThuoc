package application.database;

import application.model.HoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // Insert new Hóa Đơn
    public boolean insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (maHoaDon, ngayLap, maKhachHang, maNhanVien) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, String.valueOf(hd.getNgayLap()));
            ps.setString(3, hd.getMaKhachHang());
            ps.setString(4, hd.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get all Hóa Đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                hd.setNgayLap(LocalDate.parse(rs.getString("ngayLap")));
                hd.setMaKhachHang(rs.getString("maKhachHang"));
                hd.setMaNhanVien(rs.getString("maNhanVien"));

                list.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Auto Generate next maHoaDon
    public String generateNextMaHoaDon() {
        String sql = "SELECT MAX(maHoaDon) AS maxMa FROM HoaDon";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String maxMa = rs.getString("maxMa");
                if (maxMa == null) {
                    return "HD00001";
                }

                int nextId = Integer.parseInt(maxMa.substring(2)) + 1;
                return String.format("HD%05d", nextId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "HD00001"; // fallback
    }
}
