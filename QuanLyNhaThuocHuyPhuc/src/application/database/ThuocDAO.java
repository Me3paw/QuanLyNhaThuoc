package application.database;

import application.model.Thuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public double getTotalCost() {
    String sql = "SELECT SUM(giaNhap * soLuongTon) AS totalCost FROM Thuoc";
    try (Connection conn = DatabaseConnector.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return rs.getDouble("totalCost");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}


    public void updateThuoc(Thuoc thuoc) {
        String sql = "UPDATE Thuoc SET soLuongTon = ? WHERE maThuoc = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thuoc.getSoLuongTon());
            ps.setString(2, thuoc.getMaThuoc());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public boolean addThuoc(Thuoc thuoc) {
        String sql = "INSERT INTO Thuoc (maThuoc, tenThuoc, thanhPhan, congDung, hanSuDung, giaBan, giaNhap, soLuongTon, maNhaCungCap, hinhAnh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (thuoc == null) {
            return false;
        }
        String generatedMaThuoc = generateNextMaThuoc(thuoc.getTenThuoc()); //tạo mã thuốc
        thuoc.setMaThuoc(generatedMaThuoc); 

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, thuoc.getMaThuoc());
            ps.setString(2, thuoc.getTenThuoc());
            ps.setString(3, thuoc.getThanhPhan());
            ps.setString(4, thuoc.getCongDung());
            ps.setString(5, thuoc.getHanSuDung()); 
            ps.setDouble(6, thuoc.getGiaBan());
            ps.setDouble(7, thuoc.getGiaNhap());
            ps.setInt(8, thuoc.getSoLuongTon());
            ps.setString(9, thuoc.getMaNhaCungCap());
            ps.setString(10, thuoc.getHinhAnh());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public String generateNextMaThuoc(String tenThuoc) {

        String namePart = tenThuoc.trim().toUpperCase();
        namePart = namePart.replaceAll("^[^A-Z]+", ""); 
        if (namePart.length() > 3) {
            namePart = namePart.substring(0, 3);
        } else if (namePart.isEmpty()) {
             namePart = "XXX"; 
        }

        String prefix = "TH-" + namePart + "-"; 

        String sql = "SELECT maThuoc FROM Thuoc WHERE maThuoc LIKE ? ORDER BY maThuoc DESC LIMIT 1";
        int nextSequence = 1; 

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prefix + "%"); 

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastMa = rs.getString("maThuoc");
                    Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "(\\d+)$");
                    Matcher matcher = pattern.matcher(lastMa);

                    if (matcher.find()) {
                        try {
                            int lastSequence = Integer.parseInt(matcher.group(1)); 
                            nextSequence = lastSequence + 1;
                        } catch (NumberFormatException e) {
                            System.err.println("Lỗi" + lastMa);
                        }
                    } else {
                         System.err.println("Không khớp định dạng");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR"; 
        }

        return prefix + String.format("%04d", nextSequence); 
    }
}
