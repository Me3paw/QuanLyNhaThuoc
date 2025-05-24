package application.database;

import application.database.DatabaseConnector;
import entity.LoThuoc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoThuocDAO {

    // Thêm mới lô thuốc
    public boolean insert(LoThuoc loThuoc) {
        String sql = "INSERT INTO LoThuoc(maLoThuoc, maThuoc, soLuongNhap, ngayNhap, hanSuDung, giaNhap, ghiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loThuoc.getMaLoThuoc());
            ps.setString(2, loThuoc.getMaThuoc());
            ps.setInt(3, loThuoc.getSoLuongNhap());
            ps.setString(4, loThuoc.getNgayNhap().toString());
            ps.setString(5, loThuoc.getHanSuDung().toString());
            ps.setDouble(6, loThuoc.getGiaNhap());
            ps.setString(7, loThuoc.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật lô thuốc theo mã lô
    public boolean update(LoThuoc loThuoc) {
        String sql = "UPDATE LoThuoc SET maThuoc = ?, soLuongNhap = ?, ngayNhap = ?, hanSuDung = ?, giaNhap = ?, ghiChu = ? " +
                     "WHERE maLoThuoc = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loThuoc.getMaThuoc());
            ps.setInt(2, loThuoc.getSoLuongNhap());
            ps.setString(3, loThuoc.getNgayNhap().toString());
            ps.setString(4, loThuoc.getHanSuDung().toString());
            ps.setDouble(5, loThuoc.getGiaNhap());
            ps.setString(6, loThuoc.getGhiChu());
            ps.setString(7, loThuoc.getMaLoThuoc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xoá lô thuốc theo mã lô
    public boolean delete(String maLoThuoc) {
        String sql = "DELETE FROM LoThuoc WHERE maLoThuoc = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoThuoc);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả các lô thuốc
    public List<LoThuoc> getAll() {
        List<LoThuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM LoThuoc";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoThuoc loThuoc = new LoThuoc(
                    rs.getString("maLoThuoc"),
                    rs.getString("maThuoc"),
                    rs.getInt("soLuongNhap"),
                    LocalDate.parse(rs.getString("ngayNhap")),
                    LocalDate.parse(rs.getString("hanSuDung")),
                    rs.getDouble("giaNhap"),
                    rs.getString("ghiChu")
                );
                list.add(loThuoc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy lô thuốc theo mã lô
    public LoThuoc getById(String maLoThuoc) {
        String sql = "SELECT * FROM LoThuoc WHERE maLoThuoc = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoThuoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new LoThuoc(
                    rs.getString("maLoThuoc"),
                    rs.getString("maThuoc"),
                    rs.getInt("soLuongNhap"),
                    LocalDate.parse(rs.getString("ngayNhap")),
                    LocalDate.parse(rs.getString("hanSuDung")),
                    rs.getDouble("giaNhap"),
                    rs.getString("ghiChu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
