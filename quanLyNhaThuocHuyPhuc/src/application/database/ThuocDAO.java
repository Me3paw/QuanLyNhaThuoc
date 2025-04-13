package application.database;

import application.model.Thuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThuocDAO {

    public List<Thuoc> getAllThuoc() {
        List<Thuoc> list = new ArrayList<>();

        String sql = "SELECT * FROM Thuoc";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setMaThuoc(rs.getString("maThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setThanhPhan(rs.getString("thanhPhan"));
                t.setCongDung(rs.getString("congDung"));
                t.setHanSuDung(rs.getString("hanSuDung"));
                t.setGiaBan(rs.getDouble("giaBan"));
                t.setGiaNhap(rs.getDouble("giaNhap"));
                t.setSoLuongTon(rs.getInt("soLuongTon"));
                t.setMaNhaCungCap(rs.getString("maNhaCungCap"));
                t.setHinhAnh(rs.getString("hinhAnh")); // Path to image

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public Thuoc getThuocByID(String maThuoc) {
        String sql = "SELECT * FROM Thuoc WHERE maThuoc = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maThuoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Thuoc t = new Thuoc();
                t.setMaThuoc(rs.getString("maThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setThanhPhan(rs.getString("thanhPhan"));
                t.setCongDung(rs.getString("congDung"));
                t.setHanSuDung(rs.getString("hanSuDung"));
                t.setGiaBan(rs.getDouble("giaBan"));
                t.setGiaNhap(rs.getDouble("giaNhap"));
                t.setSoLuongTon(rs.getInt("soLuongTon"));
                t.setMaNhaCungCap(rs.getString("maNhaCungCap"));
                t.setHinhAnh(rs.getString("hinhAnh"));
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
