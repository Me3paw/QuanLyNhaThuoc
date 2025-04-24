package entity;

public class ChiTietHoaDon {
    private String maHoaDon;
    private String maThuoc;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon(String maHoaDon, String maThuoc, int soLuong, double donGia) {
        this.maHoaDon = maHoaDon;
        this.maThuoc = maThuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public ChiTietHoaDon() {
    }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getMaThuoc() { return maThuoc; }
    public void setMaThuoc(String maThuoc) { this.maThuoc = maThuoc; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
}
