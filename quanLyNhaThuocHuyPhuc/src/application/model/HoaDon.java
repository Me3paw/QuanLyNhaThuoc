package application.model;

import java.time.LocalDate;

public class HoaDon {
    private String maHoaDon;
    private LocalDate ngayLap;
    private String maKhachHang;
    private String maNhanVien;
    
    public HoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }
    public HoaDon() {}

    public HoaDon(String maHoaDon, LocalDate ngayLap, String maKhachHang, String maNhanVien) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.maKhachHang = maKhachHang;
        this.maNhanVien = maNhanVien;
    }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }

    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }
}
