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
    public List<Thuoc> searchThuoc(String keyword) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM Thuoc WHERE maThuoc LIKE ? OR tenThuoc LIKE ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

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
                t.setHinhAnh(rs.getString("hinhAnh"));
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
 // Tìm theo TÊN THUỐC
    public List<Thuoc> searchThuocByTen(String tenThuoc) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM Thuoc WHERE tenThuoc LIKE ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + tenThuoc + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = createThuocFromResultSet(rs);
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Tìm theo MÃ THUỐC
    public List<Thuoc> searchThuocByMa(String maThuoc) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM Thuoc WHERE maThuoc LIKE ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + maThuoc + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = createThuocFromResultSet(rs);
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Tìm theo CÔNG DỤNG
    public List<Thuoc> searchThuocByCongDung(String congDung) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM Thuoc WHERE congDung LIKE ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + congDung + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = createThuocFromResultSet(rs);
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    private Thuoc createThuocFromResultSet(ResultSet rs) throws Exception {
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


}
