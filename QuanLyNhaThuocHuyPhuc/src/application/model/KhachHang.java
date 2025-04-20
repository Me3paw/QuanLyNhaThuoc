package application.model;

public class KhachHang {
    private String maKhachHang;
    private String tenKH;
    private String gioiTinh;
    private String soDienThoai;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String tenKH, String gioiTinh, String soDienThoai) {
        this.maKhachHang = maKhachHang;
        this.tenKH = tenKH;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
    }

    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

}