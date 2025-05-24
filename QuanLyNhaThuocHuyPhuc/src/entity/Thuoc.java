package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Thuoc {
    private String maThuoc;
    private String tenThuoc;
    private String thanhPhan;
    private String congDung;
    private double giaBan;
    private String hinhAnh;

    public Thuoc() {
        this("", "", "", "", 0.0, "");
    }

    public Thuoc(String maThuoc, String tenThuoc, String thanhPhan, String congDung, double giaBan, String hinhAnh) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.thanhPhan = thanhPhan;
        this.congDung = congDung;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
    }

    // Getter & Setter

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

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "Thuoc{" +
                "maThuoc='" + maThuoc + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", giaBan=" + giaBan +
                '}';
    }
}
