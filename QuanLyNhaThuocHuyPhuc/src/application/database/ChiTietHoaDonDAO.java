package application.database;

import application.model.ChiTietHoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {

    // Insert single line of ChiTietHoaDon
    public boolean insertChiTietHoaDon(ChiTietHoaDon ct) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, soLuong, donGia) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaHoaDon());
            ps.setString(2, ct.getMaThuoc());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public double getTotalRevenue() {
    String sql = "SELECT SUM(ct.soLuong * ct.donGia) AS totalRevenue FROM ChiTietHoaDon ct";
    try (Connection conn = DatabaseConnector.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return rs.getDouble("totalRevenue");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}




    public List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHoaDon) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setMaHoaDon(rs.getString("maHoaDon"));
                cthd.setMaThuoc(rs.getString("maThuoc"));
                cthd.setSoLuong(rs.getInt("soLuong"));
                cthd.setDonGia(rs.getDouble("donGia"));

                list.add(cthd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
