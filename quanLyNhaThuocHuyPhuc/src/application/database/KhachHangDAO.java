package application.database;

import application.model.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getString("diaChi"),
                        rs.getString("ghiChu")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Insert New KhachHang
    public boolean insertKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKhachHang, tenKH, gioiTinh, soDienThoai, email, diaChi, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getDiaChi());
            ps.setString(7, kh.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH = ?, gioiTinh = ?, soDienThoai = ?, email = ?, diaChi = ?, ghiChu = ? WHERE maKhachHang = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getGioiTinh());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getDiaChi());
            ps.setString(6, kh.getGhiChu());
            ps.setString(7, kh.getMaKhachHang()); 

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; 

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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

        return "KH001"; 
    }
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> customerList = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang"; 

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("maKhachHang"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                kh.setEmail(rs.getString("email"));
                kh.setDiaChi(rs.getString("diaChi"));
                kh.setGhiChu(rs.getString("ghiChu"));
                customerList.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

}
