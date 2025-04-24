package application.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.KhachHang;

public class KhachHangDAO {

    // Lookup KhachHang theo SDT
    public KhachHang getKhachHangBySDT(String sdt) {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKH"),
                        rs.getString("gioiTinh"),
                        rs.getString("soDienThoai")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Insert New KhachHang
    public boolean insertKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKhachHang, tenKH, gioiTinh, soDienThoai) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSoDienThoai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Generate Next maKhachHang tự động
    public String generateNextMaKhachHang() {
        String sql = "SELECT MAX(maKhachHang) AS maxMa FROM KhachHang";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String maxMa = rs.getString("maxMa");
                if (maxMa == null) {
                    return "KH001";
                }

                int nextId = Integer.parseInt(maxMa.substring(2)) + 1;
                return String.format("KH%03d", nextId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "KH001"; // fallback
    }
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> customerList = new ArrayList<>();
        String sql = "SELECT maKhachHang, tenKH, gioiTinh, soDienThoai FROM KhachHang ORDER BY maKhachHang";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maKH = rs.getString("maKhachHang");
                String tenKH = rs.getString("tenKH");
                String gioiTinh = rs.getString("gioiTinh");
                String sdt = rs.getString("soDienThoai");

                if (maKH != null && !maKH.trim().isEmpty() && tenKH != null && !tenKH.trim().isEmpty()) {
                    KhachHang kh = new KhachHang(maKH, tenKH, gioiTinh, sdt);
                    customerList.add(kh);
                } else {
                     System.err.println("DAO cảnh báo: Bỏ qua dòng KhachHang không hợp lệ trong DB (MaKH: " + maKH + ", TenKH: " + tenKH + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); 
        }
        return customerList;
    }
}
