package application.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoanDAO {

    // Lấy tất cả tài khoản
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                taiKhoan.setNgayVaoLam(rs.getDate("ngayVaoLam").toLocalDate());
                list.add(taiKhoan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Tìm tài khoản theo mã tài khoản
    public TaiKhoan getTaiKhoanByMa(String maTaiKhoan) {
        String sql = "SELECT * FROM taikhoan WHERE maTaiKhoan = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTaiKhoan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                taiKhoan.setNgayVaoLam(rs.getDate("ngayVaoLam").toLocalDate());
                return taiKhoan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm tài khoản
    public boolean addTaiKhoan(TaiKhoan taiKhoan) {
        // Gọi hàm tự sinh mã trước khi thêm
        String maTuDong = generateMaTaiKhoanTuDong();
        taiKhoan.setMaTaiKhoan(maTuDong); // Gán vào đối tượng trước khi insert

        String sql = "INSERT INTO taikhoan (maTaiKhoan, tenDangNhap, matKhau, vaiTro, ngayVaoLam) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taiKhoan.getMaTaiKhoan());
            ps.setString(2, taiKhoan.getTenDangNhap());
            ps.setString(3, taiKhoan.getMatKhau());
            ps.setString(4, taiKhoan.getVaiTro());
            ps.setDate(5, Date.valueOf(taiKhoan.getNgayVaoLam()));

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // Cập nhật tài khoản
    public boolean updateTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "UPDATE taikhoan SET tenDangNhap = ?, matKhau = ?, vaiTro = ?, ngayVaoLam = ? WHERE maTaiKhoan = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, taiKhoan.getTenDangNhap());
            ps.setString(2, taiKhoan.getMatKhau());
            ps.setString(3, taiKhoan.getVaiTro());
            ps.setDate(4, Date.valueOf(taiKhoan.getNgayVaoLam()));
            ps.setString(5, taiKhoan.getMaTaiKhoan());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tài khoản
    public boolean deleteTaiKhoan(String maTaiKhoan) {
        String sql = "DELETE FROM taikhoan WHERE maTaiKhoan = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTaiKhoan);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm tài khoản theo tên đăng nhập
    public TaiKhoan getTaiKhoanByTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM taikhoan WHERE tenDangNhap = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                taiKhoan.setNgayVaoLam(rs.getDate("ngayVaoLam").toLocalDate());
                return taiKhoan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updateTaiKhoanPassword(String username, String newPassword) {
        String sql = "UPDATE taikhoan SET matKhau = ? WHERE tenDangNhap = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, username);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String generateMaTaiKhoanTuDong() {
        String maMoi = "";
        String yyMM = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMM"));
        String prefix = "TK-" + yyMM + "-";

        String sql = "SELECT maTaiKhoan FROM taikhoan WHERE maTaiKhoan LIKE ? ORDER BY maTaiKhoan DESC LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prefix + "%"); // VD: "TK-2504-%"

            ResultSet rs = ps.executeQuery();
            int stt = 1;

            if (rs.next()) {
                String lastMa = rs.getString("maTaiKhoan"); // VD: TK-2504-004
                String[] parts = lastMa.split("-");

                if (parts.length == 3) {
                    try {
                        stt = Integer.parseInt(parts[2]) + 1;
                    } catch (NumberFormatException e) {
                        stt = 1; // fallback nếu STT lỗi
                    }
                }
            }

            maMoi = prefix + String.format("%03d", stt); // VD: TK-2504-005

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maMoi;
    }
    // Phương thức lấy tên người dùng từ bảng nhanvien dựa trên maTaiKhoan
    public static String getTenNguoiDung(String tenDangNhap, String matKhau) {
        String tenNguoiDung = null;
        String sql = "SELECT nv.tenNhanVien " +
                     "FROM taikhoan tk " +
                     "JOIN NhanVien nv ON tk.maTaiKhoan = nv.taiKhoan " +
                     "WHERE tk.tenDangNhap = ? AND tk.matKhau = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tenNguoiDung = rs.getString("tenNhanVien");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tenNguoiDung;
    }
    
    public List<TaiKhoan> getAllTaiKhoanJoinNhanVien() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT tk.maTaiKhoan, tk.tenDangNhap, tk.matKhau, tk.vaiTro, tk.ngayVaoLam, " +
                     "nv.maNhanVien, nv.tenNhanVien, nv.soDienThoai " + 
                     "FROM taikhoan tk LEFT JOIN NhanVien nv ON tk.maTaiKhoan = nv.taiKhoan " +
                     "ORDER BY tk.maTaiKhoan";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                Date ngayVaoLamDb = rs.getDate("ngayVaoLam");
                taiKhoan.setNgayVaoLam(ngayVaoLamDb != null ? ngayVaoLamDb.toLocalDate() : null);

                String maNV = rs.getString("maNhanVien");
                if (maNV != null) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNhanVien(maNV);
                    nv.setTenNhanVien(rs.getString("tenNhanVien"));
                    nv.setSoDienThoai(rs.getString("soDienThoai"));
                    taiKhoan.setNhanVien(nv); 
                } else {
                    taiKhoan.setNhanVien(null); 
                }

                list.add(taiKhoan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateTaiKhoanRole(String maTaiKhoan, String newRole) {
        String sql = "UPDATE taikhoan SET vaiTro = ? WHERE maTaiKhoan = ?";
        if (maTaiKhoan == null || newRole == null) { return false; }
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setString(2, maTaiKhoan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    
}
