package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.RatingData.Rating;
import model.RevenueData.Revenue;
import model.TopProductData.Product;
import model.InventoryData.Inventory;
import model.OrderStatusData.Status;

public class RevenueDataDAO {
	   public static List<Revenue> getRevenueData() {
	        List<Revenue> data = new ArrayList<>();
	        try (Connection conn = DBConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT DATE_FORMAT(ngayLap, '%Y-%m') AS thang, SUM(tongTien) AS doanh_thu " +
	                     "FROM don_hang WHERE YEAR(ngayLap) = YEAR(CURDATE()) " +
	                     "GROUP BY DATE_FORMAT(ngayLap, '%Y-%m') ORDER BY thang")) {
	            while (rs.next()) {
	                data.add(new Revenue(rs.getString("thang"), rs.getDouble("doanh_thu")));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return data;
	    }	

	public static List<Product> getTopProducts() {
	    List<Product> data = new ArrayList<>();
	    try (Connection conn = DBConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT sp.tenSanPham, SUM(ctdh.soLuong) AS so_luong_ban " +
	                 "FROM chi_tiet_don_hang ctdh JOIN san_pham sp ON ctdh.maSanPham = sp.maSanPham " +
	                 "GROUP BY sp.maSanPham, sp.tenSanPham ORDER BY so_luong_ban DESC LIMIT 5")) {
	        while (rs.next()) {
	            data.add(new Product(rs.getString("tenSanPham"), rs.getInt("so_luong_ban")));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return data;
	}
	public static List<Status> getOrderStatusData() {
        List<Status> data = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT trangThai, ROUND((COUNT(*) * 100.0 / SUM(COUNT(*)) OVER ()), 2) AS ty_le " +
                     "FROM don_hang GROUP BY trangThai")) {
            while (rs.next()) {
                data.add(new Status(rs.getString("trangThai"), rs.getDouble("ty_le")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
	   public static List<Rating> getRatingData() {
	        List<Rating> data = new ArrayList<>();
	        try (Connection conn = DBConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT diemDanhGia, COUNT(*) AS so_luong " +
	                     "FROM danh_gia GROUP BY diemDanhGia ORDER BY diemDanhGia")) {
	            while (rs.next()) {
	                data.add(new Rating(rs.getInt("diemDanhGia") + " sao", rs.getInt("so_luong")));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return data;
	    }
	   public static List<Inventory> getInventoryData() {
	        List<Inventory> data = new ArrayList<>();
	        try (Connection conn = DBConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT dm.tenDanhMuc, SUM(sp.soLuongTonKho) AS ton_kho " +
	                     "FROM san_pham sp JOIN danh_muc dm ON sp.idDanhMuc = dm.maDanhMuc " +
	                     "GROUP BY dm.maDanhMuc, dm.tenDanhMuc")) {
	            while (rs.next()) {
	                data.add(new Inventory(rs.getString("tenDanhMuc"), rs.getInt("ton_kho")));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return data;
	    }
}
