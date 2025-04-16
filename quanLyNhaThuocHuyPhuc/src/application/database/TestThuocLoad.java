package application.database;

import application.model.Thuoc;

import java.util.List;

public class TestThuocLoad {
    public static void main(String[] args) {
        ThuocDAO dao = new ThuocDAO();
        List<Thuoc> list = dao.getAllThuoc();

        System.out.println("====== DANH SÁCH THUỐC ======");

        for (Thuoc t : list) {
            System.out.println("Mã Thuốc: " + t.getMaThuoc());
            System.out.println("Tên Thuốc: " + t.getTenThuoc());
            System.out.println("Thành Phần: " + t.getThanhPhan());
            System.out.println("Công Dụng: " + t.getCongDung());
            System.out.println("Hạn Sử Dụng: " + t.getHanSuDung());
            System.out.println("Giá Nhập: " + t.getGiaNhap());
            System.out.println("Giá Bán: " + t.getGiaBan());
            System.out.println("Số Lượng Tồn: " + t.getSoLuongTon());
            System.out.println("Nhà Cung Cấp: " + t.getMaNhaCungCap());
            System.out.println("Hình Ảnh: " + t.getHinhAnh());
            System.out.println("---------------------------------");
        }
    }
}
