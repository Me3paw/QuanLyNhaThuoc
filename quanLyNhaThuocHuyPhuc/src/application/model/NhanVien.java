package application.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private String gioiTinh;  // Sử dụng String thay vì Enum
    private int namSinh;
    private String soDienThoai;
    private String email;
    private double heSoLuong;
    private double luongCoBan;
    private TaiKhoan taiKhoan;
    private int caLam;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    // Constructor
    public NhanVien(String maNhanVien, String tenNhanVien, String gioiTinh, int namSinh, String soDienThoai,
            String email, double heSoLuong, double luongCoBan, TaiKhoan taiKhoan, int caLam) {
			this.maNhanVien = maNhanVien;
			this.tenNhanVien = tenNhanVien;
			this.gioiTinh = gioiTinh;
			this.namSinh = namSinh;
			this.soDienThoai = soDienThoai;
			this.email = email;
			this.heSoLuong = heSoLuong;
			this.luongCoBan = luongCoBan;
			this.taiKhoan = taiKhoan;
			this.caLam = caLam;
	}
    public NhanVien() {
		// TODO Auto-generated constructor stub
	}
	public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
    // Getters and Setters
    public int getCaLam() {
        return caLam;
    }

    public void setCaLam(int caLam) {
        this.caLam = caLam;
    }
    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getHeSoLuong() {
        return heSoLuong;
    }

    public void setHeSoLuong(double heSoLuong) {
        this.heSoLuong = heSoLuong;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    // Phương thức tính lương
    public double tinhLuong() {
        return this.luongCoBan * this.heSoLuong;
    }

    // To String method for easy printing
    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", namSinh=" + namSinh +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", heSoLuong=" + heSoLuong +
                ", luongCoBan=" + luongCoBan +
                ", taiKhoan=" + taiKhoan +
                ", caLam=" + caLam +
                '}';
    }
}
