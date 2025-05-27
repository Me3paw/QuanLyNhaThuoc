package application.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.NhaCungCap;


public class NhaCungCapDAO {
	
	
	public List<NhaCungCap> getAllTenNhaCungCap() {
        List<NhaCungCap> listNCC = new ArrayList<>();
        String sql = "SELECT maNhaCungCap, tenNhaCungCap FROM NhaCungCap"; 

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maNCC = rs.getString("maNhaCungCap");
                String tenNCC = rs.getString("tenNhaCungCap");
                NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC,"","","");
                listNCC.add(ncc);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNCC;
    }
	public static NhaCungCap getByMaNhaCungCap(String maNCC) {
		NhaCungCap ncc = null;
		String sql = "SELECT * FROM NhaCungCap WHERE maNhaCungCap = ?";

		try (Connection conn = DatabaseConnector.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maNCC);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String tenNCC = rs.getString("tenNhaCungCap");
					String diaChi = rs.getString("diaChi");
					String soDienThoai = rs.getString("soDienThoai");
					String email = rs.getString("email");
					ncc = new NhaCungCap(maNCC, tenNCC, diaChi, soDienThoai, email);
				}
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ncc;
	}
}
