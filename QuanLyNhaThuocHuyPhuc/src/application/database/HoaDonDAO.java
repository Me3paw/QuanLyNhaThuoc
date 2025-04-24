package application.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.HoaDon;
import entity.HoaDonView;

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
 // Lookup Hóa Đơn theo Mã Hóa Đơn hoặc SĐT
    public List<HoaDonView> lookupHoaDon(String maHoaDon, String sdt) {
        List<HoaDonView> list = new ArrayList<>();

        String sql = "SELECT hd.maHoaDon, hd.ngayLap, kh.tenKH, kh.soDienThoai " +
                     "FROM HoaDon hd JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "WHERE 1=1 ";

        if (!maHoaDon.isEmpty()) {
            sql += "AND hd.maHoaDon LIKE ? ";
        }

        if (!sdt.isEmpty()) {
            sql += "AND kh.soDienThoai LIKE ? ";
        }

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (!maHoaDon.isEmpty()) {
                ps.setString(idx++, "%" + maHoaDon + "%");
            }
            if (!sdt.isEmpty()) {
                ps.setString(idx++, "%" + sdt + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HoaDonView(
                        rs.getString("maHoaDon"),
                        rs.getString("ngayLap"),
                        rs.getString("tenKH"),
                        rs.getString("soDienThoai")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
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
