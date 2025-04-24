package entity;

public class HoaDonView {
    private String maHoaDon;
    private String ngayLap;
    private String tenKH;
    private String soDienThoai;

    public HoaDonView(String maHoaDon, String ngayLap, String tenKH, String soDienThoai) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.tenKH = tenKH;
        this.soDienThoai = soDienThoai;
    }

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public String getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(String ngayLap) {
		this.ngayLap = ngayLap;
	}

	public String getTenKH() {
		return tenKH;
	}

	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
    
    
}
