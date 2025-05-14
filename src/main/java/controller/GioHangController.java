package controller;

import model.GioHang;
import model.GioHangItem;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
@WebServlet("/giohang")
public class GioHangController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	// tạo session
        HttpSession session = request.getSession();

        // Lấy giỏ hàng từ session
        GioHang gioHang = (GioHang) session.getAttribute("gioHang");
        if (gioHang == null) {
            gioHang = new GioHang(); // Nếu giỏ hàng không tồn tại, tạo mới
            session.setAttribute("gioHang", gioHang);
        }

        // Đặt giỏ hàng làm attribute để hiển thị lên trang JSP
        request.setAttribute("gioHang", gioHang);

        // Chuyển đến trang hiển thị giỏ hàng jsp
        request.getRequestDispatcher("views/GioHang.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        // Lấy giỏ hàng từ session
        GioHang gioHang = (GioHang) session.getAttribute("gioHang");
        if (gioHang == null && action != null) {
            gioHang = new GioHang(); // Nếu giỏ hàng không tồn tại, tạo mới
            session.setAttribute("gioHang", gioHang);
        }

       

        if ("them".equals(action)) {
            // Thêm sản phẩm vào giỏ
        	
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));// lấy mã sản phẩm
            String tenSanPham = request.getParameter("tenSanPham");// lấy tên sản phẩm
            double gia = Double.parseDouble(request.getParameter("gia"));// lấy giá
            int soLuong = Integer.parseInt(request.getParameter("soLuong"));// lấy số lượng từ form
            // đưa thông tin vào GioHangItem
            GioHangItem item = new GioHangItem(maSanPham, tenSanPham, gia, soLuong);
            // cho vào Gio Hang
            gioHang.themSanPham(item);

        } else if ("xoa".equals(action)) {
            // Xóa sản phẩm khỏi giỏ
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
            gioHang.xoaSanPham(maSanPham);

        } else if ("capNhat".equals(action)) {
            // Cập nhật số lượng sản phẩm
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
            int soLuongMoi = Integer.parseInt(request.getParameter("soLuongMoi"));
            gioHang.capNhatSoLuong(maSanPham, soLuongMoi);

        } else if ("ThanhToan".equals(action)) {
            // Kiểm tra giỏ hàng có sản phẩm không
            if (gioHang != null && !gioHang.getDanhSach().isEmpty()) {
                // Tính tổng giá trị giỏ hàng
                double tongTien = gioHang.getTongTien();

                // Lưu thông tin giỏ hàng và tổng tiền vào request để hiển thị trên trang thanh toán
                request.setAttribute("gioHang", gioHang);
                request.setAttribute("tongTien", tongTien);

                // Chuyển đến trang xác nhận thanh toán
                request.getRequestDispatcher("views/ThanhToanGioHang.jsp").forward(request, response);
            } else {
                // Giỏ hàng trống, chuyển hướng về trang giỏ hàng
                response.sendRedirect("giohang");
            }
            return;
        }
        if (gioHang != null) {
            session.setAttribute("gioHang", gioHang); // chỉ lưu lại nếu có thay đổi
        }


        // Lưu lại giỏ hàng vào session sau khi thay đổi
        session.setAttribute("gioHang", gioHang);

        // Sau khi xử lý, chuyển hướng lại về trang giỏ hàng để cập nhật giao diện
        response.sendRedirect("giohang");
    }


}
