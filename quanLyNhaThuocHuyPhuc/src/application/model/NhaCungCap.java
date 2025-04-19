package application.model;

public class NhaCungCap {
	private String maNhaCungCap;
	private String tenNhaCungCap;
	private String soDienThoai;
	private String diaChi;
	private String email;
	
	public NhaCungCap() {
	}
	public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String soDienThoai, String diaChi, String email) {
		this.maNhaCungCap = maNhaCungCap;
		this.tenNhaCungCap = tenNhaCungCap;
		this.soDienThoai = soDienThoai;
		this.diaChi = diaChi;
		this.email = email;
	}
	public String getMaNhaCungCap() {
		return maNhaCungCap;
	}
	public void setMaNhaCungCap(String maNhaCungCap) {
		this.maNhaCungCap = maNhaCungCap;
	}
	public String getTenNhaCungCap() {
		return tenNhaCungCap;
	}
	public void setTenNhaCungCap(String tenNhaCungCap) {
		this.tenNhaCungCap = tenNhaCungCap;
	}
	public String getSoDienThoai() {
		return soDienThoai;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return maNhaCungCap + "- " + tenNhaCungCap ;
	}
	
	
}
