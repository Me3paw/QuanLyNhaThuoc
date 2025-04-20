package application.model;

public class ChiTietHoaDon extends HoaDon {
    private String maThuoc;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon(String maHoaDon, String maKhachHang, String maNhanVien, String maThuoc, int soLuong, double donGia) {
        super(maHoaDon, null, maKhachHang, maNhanVien);
        this.maThuoc = maThuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }
    public ChiTietHoaDon() {
    }
	public ChiTietHoaDon(String maHoaDon, String maThuoc, int soLuong, double donGia) {
		super(maHoaDon);
		this.maThuoc = maThuoc;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}

    public String getMaThuoc() { return maThuoc; }
    public void setMaThuoc(String maThuoc) { this.maThuoc = maThuoc; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
}