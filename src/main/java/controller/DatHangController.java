package controller;

import dao.DatHangDAO;
import model.DonHang;
import model.ChiTietDonHang;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/dat-hang")
public class DatHangController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Lấy dữ liệu người dùng từ form
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String note = request.getParameter("note");

        // Lấy danh sách sản phẩm từ form
        String[] tenSanPham = request.getParameterValues("tenSanPham");
        String[] soLuongSanPham = request.getParameterValues("soLuong");
        String[] donGiaSanPham = request.getParameterValues("donGia");

        // Gọi DAO
        DatHangDAO datHangDAO = new DatHangDAO();

        // Lấy hoặc thêm người dùng
        int maNguoiDung = datHangDAO.themHoacLayNguoiDung(fullName, phone, email, address);

        // Tạo đối tượng đơn hàng
        DonHang donHang = new DonHang();
        donHang.setNgayLap(new Date());
        donHang.setTrangThai("Chưa xử lý");

        // Lấy tổng tiền từ form, mặc định là 0 nếu null
        String tongTienStr = request.getParameter("tongTien");
        double tongTien = 0;
        if (tongTienStr != null && !tongTienStr.isEmpty()) {
            try {
                tongTien = Double.parseDouble(tongTienStr);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Hoặc log lỗi
            }
        }
        donHang.setTongTien(tongTien);
        donHang.setMaNguoiDung(maNguoiDung);

        // Thêm đơn hàng vào DB
        int maDonHang = datHangDAO.themDonHang(donHang);

        // Tạo danh sách chi tiết đơn hàng
        List<ChiTietDonHang> chiTietDonHangList = new ArrayList<>();
        for (int i = 0; i < tenSanPham.length; i++) {
            ChiTietDonHang chiTiet = new ChiTietDonHang();

            // Lấy mã sản phẩm từ parameter có dạng "maSanPham0", "maSanPham1",...
            String maSanPhamStr = request.getParameter("maSanPham" + i);
            if (maSanPhamStr != null && !maSanPhamStr.isEmpty()) {
                try {
                    int maSanPham = Integer.parseInt(maSanPhamStr);
                    chiTiet.setMaSanPham(maSanPham);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue; // Bỏ qua sản phẩm này nếu mã lỗi
                }
            }

            try {
                int soLuong = Integer.parseInt(soLuongSanPham[i]);
                double donGia = Double.parseDouble(donGiaSanPham[i]);
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(donGia);

                // Cập nhật số lượng kho và thêm vào danh sách
                datHangDAO.capNhatSoLuongSanPham(chiTiet.getMaSanPham(), soLuong);
                chiTietDonHangList.add(chiTiet);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Bỏ qua sản phẩm có dữ liệu không hợp lệ
            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        // Lưu chi tiết đơn hàng
        try {
			datHangDAO.themChiTietDonHang(maDonHang, chiTietDonHangList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Truyền dữ liệu qua trang cảm ơn
        request.setAttribute("fullName", fullName);
        request.setAttribute("phone", phone);
        request.setAttribute("email", email);
        request.setAttribute("address", address);
        request.setAttribute("note", note);
        request.setAttribute("tenSanPham", tenSanPham);
        request.setAttribute("soLuong", soLuongSanPham);
        request.setAttribute("tongTien", tongTien);

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/CamOn.jsp");
        dispatcher.forward(request, response);
    }
}
