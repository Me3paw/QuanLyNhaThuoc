package application.model;

public class Thuoc {
    private String maThuoc;
    private String tenThuoc;
    private String thanhPhan;
    private String congDung;
    private String hanSuDung;
    private double giaBan;
    private double giaNhap;
    private int soLuongTon;
    private String maNhaCungCap;
    private String hinhAnh;
    
    public Thuoc() {
		this("", "", "", "", "", 0.0, 0.0, 0, "", "");
	}
    
    
    public Thuoc(String maThuoc, String tenThuoc, String thanhPhan, String congDung, String hanSuDung, double giaBan,
			double giaNhap, int soLuongTon, String maNhaCungCap, String hinhAnh) {
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
		this.thanhPhan = thanhPhan;
		this.congDung = congDung;
		this.hanSuDung = hanSuDung;
		this.giaBan = giaBan;
		this.giaNhap = giaNhap;
		this.soLuongTon = soLuongTon;
		this.maNhaCungCap = maNhaCungCap;
		this.hinhAnh = hinhAnh;
	}

	// Getter + Setter

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }

    public String getCongDung() {
        return congDung;
    }

    public void setCongDung(String congDung) {
        this.congDung = congDung;
    }

    public String getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(String hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}