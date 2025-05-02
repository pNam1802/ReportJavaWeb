package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.SanPhamDAO;
import model.SanPham;

@WebServlet("/san-pham")
public class SanPhamController extends HttpServlet {
    private static final long serialVersionUID = 1L;    
    private SanPhamDAO sanPhamDAO;

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
        
        doGet(request, response);
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