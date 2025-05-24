package application.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.NhanVien;
import entity.TaiKhoan;

public class NhanVienDAO {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    // Lấy tất cả nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "SELECT * FROM nhanvien";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maTaiKhoan = rs.getString("taiKhoan");
                TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoanByMa(maTaiKhoan);
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("gioiTinh"),
                        rs.getInt("namSinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getDouble("heSoLuong"),
                        rs.getDouble("luongCoBan"),
                        taiKhoan,
                        rs.getInt("caLam")
                );
                list.add(nhanVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy nhân viên theo mã tài khoản
    public NhanVien getNhanVienByTaiKhoan(String maTaiKhoan) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "SELECT * FROM nhanvien WHERE taiKhoan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maTaiKhoan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoanByMa(maTaiKhoan);
                return new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("gioiTinh"),
                        rs.getInt("namSinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getDouble("heSoLuong"),
                        rs.getDouble("luongCoBan"),
                        taiKhoan,
                        rs.getInt("caLam")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy nhân viên theo mã nhân viên
    public NhanVien getNhanVienByMa(String maNhanVien) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "SELECT * FROM nhanvien WHERE maNhanVien = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maTaiKhoan = rs.getString("taiKhoan");
                TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoanByMa(maTaiKhoan);
                return new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("gioiTinh"),
                        rs.getInt("namSinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getDouble("heSoLuong"),
                        rs.getDouble("luongCoBan"),
                        taiKhoan,
                        rs.getInt("caLam")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm nhân viên (tự động tạo maNhanVien và maTaiKhoan)
    public boolean addNhanVien(NhanVien nhanVien) {
        // Tạo mã tài khoản mới tự động
        String maTaiKhoanMoi = taiKhoanDAO.generateMaTaiKhoanTuDong();
        nhanVien.getTaiKhoan().setMaTaiKhoan(maTaiKhoanMoi);

        // Thêm tài khoản trước
        boolean addedTaiKhoan = taiKhoanDAO.addTaiKhoan(nhanVien.getTaiKhoan());
        if (!addedTaiKhoan) {
            System.out.println("Thêm tài khoản thất bại, không thể thêm nhân viên.");
            return false;
        }

        // Tạo mã nhân viên mới tự động
        String maNhanVienMoi = generateMaNhanVienTuDong();
        nhanVien.setMaNhanVien(maNhanVienMoi);

        String sql = "INSERT INTO nhanvien (maNhanVien, tenNhanVien, gioiTinh, namSinh, soDienThoai, email, heSoLuong, luongCoBan, taiKhoan, caLam) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nhanVien.getMaNhanVien());
            ps.setString(2, nhanVien.getTenNhanVien());
            ps.setString(3, nhanVien.getGioiTinh());
            ps.setInt(4, nhanVien.getNamSinh());
            ps.setString(5, nhanVien.getSoDienThoai());
            ps.setString(6, nhanVien.getEmail());
            ps.setDouble(7, nhanVien.getHeSoLuong());
            ps.setDouble(8, nhanVien.getLuongCoBan());
            ps.setString(9, nhanVien.getTaiKhoan().getMaTaiKhoan());
            ps.setInt(10, nhanVien.getCaLam());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật nhân viên và tài khoản cùng lúc
    public boolean updateNhanVien(NhanVien nhanVien) {
        String sqlUpdateNhanVien = "UPDATE nhanvien SET tenNhanVien = ?, gioiTinh = ?, namSinh = ?, soDienThoai = ?, email = ?, heSoLuong = ?, luongCoBan = ?, caLam = ? WHERE maNhanVien = ?";
        String sqlUpdateTaiKhoan = "UPDATE taikhoan SET tenDangNhap = ?, matKhau = ?, vaiTro = ? WHERE maTaiKhoan = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Cập nhật nhân viên
            try (PreparedStatement psNV = conn.prepareStatement(sqlUpdateNhanVien)) {
                psNV.setString(1, nhanVien.getTenNhanVien());
                psNV.setString(2, nhanVien.getGioiTinh());
                psNV.setInt(3, nhanVien.getNamSinh());
                psNV.setString(4, nhanVien.getSoDienThoai());
                psNV.setString(5, nhanVien.getEmail());
                psNV.setDouble(6, nhanVien.getHeSoLuong());
                psNV.setDouble(7, nhanVien.getLuongCoBan());
                psNV.setInt(8, nhanVien.getCaLam());
                psNV.setString(9, nhanVien.getMaNhanVien());
                psNV.executeUpdate();
            }

            // Cập nhật tài khoản
            try (PreparedStatement psTK = conn.prepareStatement(sqlUpdateTaiKhoan)) {
                psTK.setString(1, nhanVien.getTaiKhoan().getTenDangNhap());
                psTK.setString(2, nhanVien.getTaiKhoan().getMatKhau());
                psTK.setString(3, nhanVien.getTaiKhoan().getVaiTro());
                psTK.setString(4, nhanVien.getTaiKhoan().getMaTaiKhoan());
                psTK.executeUpdate();
            }

            conn.commit(); // Commit nếu thành công
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseConnector.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    



    // Tìm nhân viên theo tên
    public List<NhanVien> searchNhanVienByName(String name) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE tenNhanVien LIKE ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maTaiKhoan = rs.getString("taiKhoan");
                TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoanByMa(maTaiKhoan);

                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("gioiTinh"),
                        rs.getInt("namSinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getDouble("heSoLuong"),
                        rs.getDouble("luongCoBan"),
                        taiKhoan,
                        rs.getInt("caLam")
                );
                list.add(nhanVien);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Cập nhật ca làm việc
    public boolean setCaLam(String maNhanVien, int caLam) {
        String sql = "UPDATE nhanvien SET caLam = ? WHERE maNhanVien = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, caLam);
            ps.setString(2, maNhanVien);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Sinh mã nhân viên tự động
    public String generateMaNhanVienTuDong() {
        String maMoi = "";
        String yyMM = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMM"));
        String prefix = "NV-" + yyMM + "-";

        String sql = "SELECT maNhanVien FROM nhanvien WHERE maNhanVien LIKE ? ORDER BY maNhanVien DESC LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prefix + "%");

            ResultSet rs = ps.executeQuery();
            int stt = 1;

            if (rs.next()) {
                String lastMa = rs.getString("maNhanVien");
                String[] parts = lastMa.split("-");
                if (parts.length == 3) {
                    try {
                        stt = Integer.parseInt(parts[2]) + 1;
                    } catch (NumberFormatException e) {
                        stt = 1;
                    }
                }
            }

            maMoi = prefix + String.format("%03d", stt);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maMoi;
    }
    

}
