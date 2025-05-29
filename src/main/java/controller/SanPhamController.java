package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.SanPhamDAO;
import interfaces.ISanPham;
import model.DanhGiaSanPham;
import model.GioHang;
import model.GioHangItem;
import model.SanPham;

@WebServlet("/san-pham")
public class SanPhamController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ISanPham sanPhamDAO;

    @Override
    public void init() throws ServletException {

        sanPhamDAO = new SanPhamDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String action = request.getParameter("action");
        // xem chi tiết sản phẩm
        if ("chiTiet".equals(action)) {
            // Xử lý xem chi tiết sản phẩm
            String id = request.getParameter("id");
            if (id != null) {
                try {
                    SanPham sanPham = sanPhamDAO.getById(Integer.parseInt(id));
                    request.setAttribute("sanPham", sanPham);
                    request.getRequestDispatcher("views/ChiTietSanPham.jsp").forward(request, response);
                    return;
                } catch (NumberFormatException e) {
                    // Xử lý khi ID không hợp lệ
                }
            }
            response.sendRedirect(request.getContextPath() + "/san-pham");
            return;
        }
        if ("daGiao".equals(action)) {
            SanPhamDAO dao = new SanPhamDAO();
            List<DanhGiaSanPham> danhGiaList = dao.layDanhSachDanhGiaDaGiao();
            request.setAttribute("danhGiaList", danhGiaList);
            request.getRequestDispatcher("/views/SanPhamGiao.jsp").forward(request, response);
        }
     // Tìm kiếm sản phẩm
        if ("timKiem".equals(action)) {
            String keyword = request.getParameter("keyword");
            System.out.println("Keyword received: '" + keyword + "'");
            int page = 1;
            int limit = 6;

            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            int offset = (page - 1) * limit;
            List<SanPham> sanPhams;
            int totalSanPhams;

            if (keyword != null && !keyword.trim().isEmpty()) {
                System.out.println("Calling searchByName with keyword: " + keyword);
                sanPhams = sanPhamDAO.searchByName(keyword, offset, limit);
                totalSanPhams = sanPhamDAO.getTotalSanPhamByName(keyword);
                request.setAttribute("keyword", keyword);
            } else {
                System.out.println("Keyword empty, calling getSanPhams");
                sanPhams = sanPhamDAO.getSanPhams(offset, limit);
                totalSanPhams = sanPhamDAO.getTotalSanPham();
            }

            int totalPages = (int) Math.ceil((double) totalSanPhams / limit);
            request.setAttribute("sanPhams", sanPhams);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("views/TrangChu.jsp").forward(request, response);
            return;
        }


        // Xử lý phân trang (giữ nguyên như cũ)
        int page = 1;
        int limit = 6;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int offset = (page - 1) * limit;

        List<SanPham> sanPhams = sanPhamDAO.getSanPhams(offset, limit);
        int totalSanPhams = sanPhamDAO.getTotalSanPham();
        int totalPages = (int) Math.ceil((double) totalSanPhams / limit);

        request.setAttribute("sanPhams", sanPhams);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("views/TrangChu.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action");

        // đặt hàng
        if("datHang".equals(action)) {
        	String id = request.getParameter("id");
        	String soLuongStr = request.getParameter("soLuong");

        	if (id != null && soLuongStr != null) {
        	    try {
        	        int maSanPham = Integer.parseInt(id);
        	        int soLuong = Integer.parseInt(soLuongStr);

        	        // Lấy sản phẩm
        	        SanPham sanPham = sanPhamDAO.getById(maSanPham);

        	        // Đặt hàng hoặc chuyển đến trang xác nhận
        	        request.setAttribute("sanPham", sanPham);
        	        request.setAttribute("soLuong", soLuong);

        	        request.getRequestDispatcher("views/DatHang.jsp").forward(request, response);
        	        return;

        	    } catch (NumberFormatException e) {
        	        // xử lý lỗi parse nếu cần
        	    }
        	}

        }
     // xử lý action giỏ hàng
        if ("themVaoGioHang".equals(action)) {
            String id = request.getParameter("id");
            String soLuongStr = request.getParameter("soLuong");
            // nếu mã sản phẩm và số lượng tồn tại thì bắt đầu xử lý
            if (id != null && soLuongStr != null) {
                try {
                	// ép kiểu
                    int maSanPham = Integer.parseInt(id);
                    int soLuong = Integer.parseInt(soLuongStr);
                    // lấy đối tượng sản phẩm thông qua ID
                    SanPham sanPham = sanPhamDAO.getById(maSanPham);
                    // tạo session
                    HttpSession session = request.getSession();
                    // lấy đối tượng giỏ hàng đang trong session
                    GioHang gioHang = (GioHang) session.getAttribute("gioHang");
                    // nếu giỏ hàng null thì tạo giỏ hàng mới
                    if (gioHang == null) {
                        gioHang = new GioHang();
                    }
                    // tạo giỏ hàng item để lưu trữ thông tin từng sản phẩm
                    GioHangItem item = new GioHangItem(
                        sanPham.getMaSanPham(),
                        sanPham.getTenSanPham(),
                        sanPham.getGiaKhuyenMai(),
                        soLuong
                    );
                    // đưa giỏ hàn item vào gioHang
                    gioHang.themSanPham(item);

                    // Lưu giỏ hàng lại vào session
                    session.setAttribute("gioHang", gioHang);

                    // Chuyển sang trang giỏ hàng controller
                    response.sendRedirect("giohang");
                    return;

                } catch (NumberFormatException e) {
                    e.printStackTrace(); // hoặc log lỗi
                }
            }

            // Nếu dữ liệu lỗi thì quay về trang sản phẩm
            response.sendRedirect("san-pham");
            return;
        }


    }

    @Override
    public void destroy() {
        try {
            if (sanPhamDAO != null) {
                sanPhamDAO.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
