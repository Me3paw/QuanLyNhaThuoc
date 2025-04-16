package application.database;

import application.model.HoaDon;
import application.model.ChiTietHoaDon;

import java.util.List;

public class TestHoaDon {
    public static void main(String[] args) {
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

        System.out.println("====== DANH SÁCH HÓA ĐƠN ======");

        List<HoaDon> listHoaDon = hoaDonDAO.getAllHoaDon();

        for (HoaDon hd : listHoaDon) {
            System.out.println("Mã Hóa Đơn: " + hd.getMaHoaDon());
            System.out.println("Ngày Lập: " + hd.getNgayLap());
            System.out.println("Khách Hàng: " + hd.getMaKhachHang());
            System.out.println("Nhân Viên: " + hd.getMaNhanVien());

            System.out.println("---- Chi Tiết Hóa Đơn ----");

            List<ChiTietHoaDon> listCTHD = chiTietHoaDonDAO.getChiTietHoaDonByMaHD(hd.getMaHoaDon());
            for (ChiTietHoaDon ct : listCTHD) {
                System.out.println("- Mã Thuốc: " + ct.getMaThuoc());
                System.out.println("- Số lượng: " + ct.getSoLuong());
                System.out.println("- Đơn giá: " + ct.getDonGia());
                System.out.println("-------------------------");
            }

            System.out.println("======================================");
        }
    }
}
