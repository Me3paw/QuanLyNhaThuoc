package application.database;

import application.model.NhanVien;
import application.model.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    // Lấy tất cả nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maTaiKhoan = rs.getString("taiKhoan");
                
                // Lấy thông tin tài khoản từ lớp TaiKhoanDAO
                TaiKhoan taiKhoan = new TaiKhoanDAO().getTaiKhoanByMa(maTaiKhoan);

                NhanVien nhanVien = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("tenNhanVien"),
                    rs.getString("gioiTinh"),
                    rs.getInt("namSinh"),
                    rs.getString("soDienThoai"),
                    rs.getString("email"),
                    rs.getDouble("heSoLuong"),
                    rs.getDouble("luongCoBan"),
                    taiKhoan
                );
                list.add(nhanVien);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Tìm nhân viên theo mã nhân viên
    public NhanVien getNhanVienByMa(String maNhanVien) {
        String sql = "SELECT * FROM nhanvien WHERE maNhanVien = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maTaiKhoan = rs.getString("taiKhoan");
                TaiKhoan taiKhoan = new TaiKhoanDAO().getTaiKhoanByMa(maTaiKhoan);

                NhanVien nhanVien = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("tenNhanVien"),
                    rs.getString("gioiTinh"),
                    rs.getInt("namSinh"),
                    rs.getString("soDienThoai"),
                    rs.getString("email"),
                    rs.getDouble("heSoLuong"),
                    rs.getDouble("luongCoBan"),
                    taiKhoan
                );
                return nhanVien;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm nhân viên
    public boolean addNhanVien(NhanVien nhanVien) {
        // Tạo mã nhân viên mới tự động
        String maNhanVienMoi = generateMaNhanVienTuDong();
        nhanVien.setMaNhanVien(maNhanVienMoi);

        String sql = "INSERT INTO nhanvien (maNhanVien, tenNhanVien, gioiTinh, namSinh, soDienThoai, email, heSoLuong, luongCoBan, taiKhoan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin nhân viên
    public boolean updateNhanVien(NhanVien nhanVien) {
        String sqlUpdateNhanVien = "UPDATE nhanvien SET tenNhanVien = ?, gioiTinh = ?, namSinh = ?, soDienThoai = ?, email = ?, heSoLuong = ?, luongCoBan = ? WHERE maNhanVien = ?";
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
                psNV.setString(8, nhanVien.getMaNhanVien());
                psNV.executeUpdate();
            }

            // Cập nhật tài khoản (ngoại trừ maTaiKhoan và ngayVaoLam)
            try (PreparedStatement psTK = conn.prepareStatement(sqlUpdateTaiKhoan)) {
                psTK.setString(1, nhanVien.getTaiKhoan().getTenDangNhap());
                psTK.setString(2, nhanVien.getTaiKhoan().getMatKhau());
                psTK.setString(3, nhanVien.getTaiKhoan().getVaiTro());
                psTK.setString(4, nhanVien.getTaiKhoan().getMaTaiKhoan());
                psTK.executeUpdate();
            }

            conn.commit(); // Nếu mọi thứ thành công
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseConnector.getConnection().rollback(); // Rollback nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }


    // Xóa nhân viên
    public boolean deleteNhanVien(String maNhanVien) {
        String sql = "DELETE FROM nhanvien WHERE maNhanVien = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
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
                TaiKhoan taiKhoan = new TaiKhoanDAO().getTaiKhoanByMa(maTaiKhoan);

                NhanVien nhanVien = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("tenNhanVien"),
                    rs.getString("gioiTinh"),
                    rs.getInt("namSinh"),
                    rs.getString("soDienThoai"),
                    rs.getString("email"),
                    rs.getDouble("heSoLuong"),
                    rs.getDouble("luongCoBan"),
                    taiKhoan
                );
                list.add(nhanVien);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public String generateMaNhanVienTuDong() {
        String maMoi = "";
        String yyMM = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMM"));
        String prefix = "NV-" + yyMM + "-";

        String sql = "SELECT maNhanVien FROM nhanvien WHERE maNhanVien LIKE ? ORDER BY maNhanVien DESC LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prefix + "%"); // VD: "NV-2504-%"

            ResultSet rs = ps.executeQuery();
            int stt = 1;

            if (rs.next()) {
                String lastMa = rs.getString("maNhanVien"); // VD: NV-2504-007
                String[] parts = lastMa.split("-");

                if (parts.length == 3) {
                    try {
                        stt = Integer.parseInt(parts[2]) + 1;
                    } catch (NumberFormatException e) {
                        stt = 1; // fallback nếu phần STT bị lỗi
                    }
                }
            }

            maMoi = prefix + String.format("%03d", stt); // VD: NV-2504-008

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maMoi;
    }


}
