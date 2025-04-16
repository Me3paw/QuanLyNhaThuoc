package application.database;

import application.model.NhanVien;

import java.util.List;

public class TestNhanVienLoad {

    public static void main(String[] args) {
        // Tạo đối tượng DAO để truy vấn bảng nhanvien
        NhanVienDAO dao = new NhanVienDAO();
        
        // Lấy danh sách nhân viên từ cơ sở dữ liệu
        List<NhanVien> list = dao.getAllNhanVien();

        // In danh sách nhân viên và tài khoản ra console
        System.out.println("====== DANH SÁCH NHÂN VIÊN ======");

        // Duyệt qua từng đối tượng NhanVien và in thông tin
        for (NhanVien nv : list) {
            System.out.println("Mã Nhân Viên: " + nv.getMaNhanVien());
            System.out.println("Tên Nhân Viên: " + nv.getTenNhanVien());
            System.out.println("Giới Tính: " + nv.getGioiTinh());
            System.out.println("Năm Sinh: " + nv.getNamSinh());
            System.out.println("Số Điện Thoại: " + nv.getSoDienThoai());
            System.out.println("Email: " + nv.getEmail());
            System.out.println("Hệ Số Lương: " + nv.getHeSoLuong());
            System.out.println("Lương Cơ Bản: " + nv.getLuongCoBan());
            
            // In thông tin tài khoản
            System.out.println("------ THÔNG TIN TÀI KHOẢN ------");
            System.out.println("Mã Tài Khoản: " + nv.getTaiKhoan().getMaTaiKhoan());
            System.out.println("Tên Đăng Nhập: " + nv.getTaiKhoan().getTenDangNhap());
            System.out.println("Mật Khẩu: " + nv.getTaiKhoan().getMatKhau());
            System.out.println("Vai Trò: " + nv.getTaiKhoan().getVaiTro());
            System.out.println("Ngày Vào Làm: " + nv.getTaiKhoan().getNgayVaoLam());
            System.out.println("---------------------------------");
        }
    }
}
