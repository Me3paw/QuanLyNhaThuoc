package application.database;

import application.model.CaLamViec;
import application.model.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaLamDAO {
    private Connection conn;

    public CaLamDAO(Connection conn) {
        this.conn = conn;
    }

    // Tạo bảng CaLam nếu chưa tồn tại
    public void taoBangCaLam() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS CaLam (
                maCa TEXT PRIMARY KEY,
                ngayLam DATE NOT NULL,
                gioBatDau TEXT NOT NULL,
                gioKetThuc TEXT NOT NULL,
                nhanVien TEXT NOT NULL,
                ghiChu TEXT
            );
        """;
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    // Thêm một ca làm việc mới vào cơ sở dữ liệu
    public void themCa(CaLamViec ca) throws SQLException {
        String sql = "INSERT INTO CaLam(maCa, ngayLam, gioBatDau, gioKetThuc, nhanVien, ghiChu) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ca.getMaCa());
        ps.setDate(2, Date.valueOf(ca.getNgayLam())); // Chuyển từ LocalDate sang Date
        ps.setString(3, ca.getGioBatDau());
        ps.setString(4, ca.getGioKetThuc());
        ps.setString(5, ca.getNhanVien());
        ps.setString(6, ca.getGhiChu());
        ps.executeUpdate();
    }

    // Lấy danh sách tất cả các ca làm việc
    public List<CaLamViec> layDanhSach() throws SQLException {
        List<CaLamViec> ds = new ArrayList<>();
        String sql = "SELECT * FROM CaLam";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            ds.add(new CaLamViec(
                rs.getString("maCa"),
                rs.getDate("ngayLam").toLocalDate(), // Chuyển từ Date sang LocalDate
                rs.getString("gioBatDau"),
                rs.getString("gioKetThuc"),
                rs.getString("nhanVien"),
                rs.getString("ghiChu")
            ));
        }
        return ds;
    }

    // Cập nhật thông tin ca làm việc
    public void capNhatCa(CaLamViec ca) throws SQLException {
        String sql = "UPDATE CaLam SET ngayLam=?, gioBatDau=?, gioKetThuc=?, nhanVien=?, ghiChu=? WHERE maCa=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDate(1, Date.valueOf(ca.getNgayLam())); // Chuyển từ LocalDate sang Date
        ps.setString(2, ca.getGioBatDau());
        ps.setString(3, ca.getGioKetThuc());
        ps.setString(4, ca.getNhanVien());
        ps.setString(5, ca.getGhiChu());
        ps.setString(6, ca.getMaCa());
        ps.executeUpdate();
    }

    // Xóa ca làm việc theo mã ca
    public void xoaCa(String maCa) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM CaLam WHERE maCa=?");
        ps.setString(1, maCa);
        ps.executeUpdate();
    }
}
