package controller;

import dao.DatHangDAO;
import model.ChiTietDonHang;
import model.DonHang;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

@WebServlet("/dat-hang")
public class DatHangController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DatHangController.class.getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String note = request.getParameter("note");

        String maSanPhamStr = request.getParameter("maSanPham");
        String tenSanPham = request.getParameter("tenSanPham");
        String hinhAnh = request.getParameter("hinhAnh");
        String soLuongStr = request.getParameter("soLuong");
        String tongTienStr = request.getParameter("tongTien");

        // Kiểm tra dữ liệu đầu vào
        if (isEmpty(fullName, phone, email, address, maSanPhamStr, soLuongStr, tongTienStr)) {
            request.setAttribute("errorMessage", "Thiếu thông tin đặt hàng.");
            forwardToErrorPage(request, response);
            return;
        }

        try {
            int maSanPham = Integer.parseInt(maSanPhamStr);
            int soLuong = Integer.parseInt(soLuongStr);
            double tongTien = parseDoubleSafe(tongTienStr, 0);

            DatHangDAO datHangDAO = new DatHangDAO();

            // Thêm hoặc lấy người dùng
            int maNguoiDung = datHangDAO.themHoacLayNguoiDung(fullName, phone, email, address);
            if (maNguoiDung == -1) {
                request.setAttribute("errorMessage", "Không thể tạo người dùng.");
                forwardToErrorPage(request, response);
                return;
            }

            // Tạo đơn hàng
            DonHang donHang = new DonHang();
            donHang.setNgayLap(new Date());
            donHang.setTrangThai("Đang xử lý");
            donHang.setTongTien(tongTien);
            donHang.setMaNguoiDung(maNguoiDung);

            // Chi tiết đơn hàng
            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setMaSanPham(maSanPham);
            chiTiet.setSoLuong(soLuong);
            chiTiet.setDonGia(tongTien); // hoặc tongTien / soLuong nếu chia đơn giá

            List<ChiTietDonHang> dsChiTiet = Collections.singletonList(chiTiet);

            // Đặt hàng
            boolean success = datHangDAO.placeOrder(donHang, dsChiTiet);
            if (!success) {
                request.setAttribute("errorMessage", "Không thể đặt hàng. Kiểm tra lại số lượng.");
                forwardToErrorPage(request, response);
                return;
            }

            // ✅ Truyền dữ liệu sang trang cảm ơn
            request.setAttribute("fullName", fullName);
            request.setAttribute("phone", phone);
            request.setAttribute("email", email);
            request.setAttribute("address", address);
            request.setAttribute("note", note);
            request.setAttribute("tenSanPham", tenSanPham);
            request.setAttribute("soLuong", soLuongStr);
            request.setAttribute("tongTien", tongTien);

            request.getRequestDispatcher("views/CamOn.jsp").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            logger.severe("Lỗi xử lý đặt hàng: " + e.getMessage());
            request.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình xử lý.");
            forwardToErrorPage(request, response);
        }
    }

    private boolean isEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) return true;
        }
        return false;
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("views/ErrorPage.jsp").forward(req, res);
    }

    private double parseDoubleSafe(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            logger.warning("Không thể parse giá trị double: " + value);
            return defaultValue;
        }
    }
}
