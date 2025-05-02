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
        HttpSession session = request.getSession();

        // Lấy giỏ hàng từ session, nếu chưa có thì tạo mới
        GioHang gioHang = (GioHang) session.getAttribute("gioHang");
        if (gioHang == null) {
            gioHang = new GioHang();
            session.setAttribute("gioHang", gioHang);
        }

        // Đặt giỏ hàng làm attribute để hiển thị lên trang JSP
        request.setAttribute("gioHang", gioHang);

        // Chuyển đến trang hiển thị giỏ hàng
        request.getRequestDispatcher("views/GioHang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        GioHang gioHang = (GioHang) session.getAttribute("gioHang");
        if (gioHang == null) {
            gioHang = new GioHang();
            session.setAttribute("gioHang", gioHang);
        }

        String action = request.getParameter("action");

        if ("them".equals(action)) {
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
            String tenSanPham = request.getParameter("tenSanPham");
            double gia = Double.parseDouble(request.getParameter("gia"));
            int soLuong = Integer.parseInt(request.getParameter("soLuong"));

            GioHangItem item = new GioHangItem(maSanPham, tenSanPham, gia, soLuong);
            gioHang.themSanPham(item);

        } else if ("xoa".equals(action)) {
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
            gioHang.xoaSanPham(maSanPham);

        } else if ("capNhat".equals(action)) {
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
            int soLuongMoi = Integer.parseInt(request.getParameter("soLuongMoi"));
            gioHang.capNhatSoLuong(maSanPham, soLuongMoi);
        }

        // Sau khi xử lý, chuyển hướng lại về trang giỏ hàng để cập nhật giao diện
        response.sendRedirect("giohang");
    }
}
