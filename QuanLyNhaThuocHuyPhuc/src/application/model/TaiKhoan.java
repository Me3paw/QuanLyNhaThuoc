package application.model;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TaiKhoan {
    private String maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;
    private LocalDate ngayVaoLam;
    private NhanVien nhanVien;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    
    public TaiKhoan() {
		this("", "", "", "", LocalDate.now());
	}

    // Constructor
    public TaiKhoan(String maTaiKhoan, String tenDangNhap, String matKhau, String vaiTro, LocalDate ngayVaoLam) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.ngayVaoLam = ngayVaoLam;
    }
    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    public TaiKhoan( String tenDangNhap, String matKhau, String vaiTro, LocalDate ngayVaoLam) {
    	
    	this.tenDangNhap = tenDangNhap;
    	this.matKhau = matKhau;
    	this.vaiTro = vaiTro;
    	this.ngayVaoLam = ngayVaoLam;
    }

    // Getters and Setters
    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public boolean isSelected() {
        return selected.get();
    }    
    public BooleanProperty selectedProperty() {
        return selected;
    } 
    
    public final void setSelected(boolean selected) {
        this.selected.set(selected);
    }
    
    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTaiKhoan='" + maTaiKhoan + '\'' +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", vaiTro='" + vaiTro + '\'' +
                ", ngayVaoLam=" + ngayVaoLam +
                '}';
    }
}
