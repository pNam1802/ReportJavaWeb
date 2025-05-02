package controller;

import dao.SanPhamDAO;
import model.SanPham;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        if ("chiTiet".equals(action)) {
            String id = request.getParameter("id");
            if (id != null) {
                try {
                    SanPham sanPham = sanPhamDAO.getById(Integer.parseInt(id));
                    request.setAttribute("sanPham", sanPham);
                    request.getRequestDispatcher("views/ChiTietSanPham.jsp").forward(request, response);
                    return;
                } catch (NumberFormatException e) {
                    // Xử lý lỗi
                }
            }
            response.sendRedirect(request.getContextPath() + "/san-pham");
            return;
        }

        // Xử lý phân trang
        int page = 1;
        int limit = 10;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int offset = (page - 1) * limit;

        String danhMuc = request.getParameter("danhMuc");
        List<SanPham> sanPhams;
        int totalSanPhams;
        int totalPages;

        if (danhMuc != null && !danhMuc.isEmpty()) {
            // Ánh xạ danhMuc sang maDanhMuc (số)
            int maDanhMuc;
            switch (danhMuc.toLowerCase()) {
                case "sofa": maDanhMuc = 1; break;
                case "ban-tra": maDanhMuc = 2; break;
                case "tu-giuong": maDanhMuc = 3; break;
                case "ban-an": maDanhMuc = 4; break;
                case "ghe-thu-gian": maDanhMuc = 5; break;
                default: maDanhMuc = Integer.parseInt(danhMuc); // Nếu không khớp, giữ nguyên (có thể gây lỗi)
            }
            sanPhams = sanPhamDAO.getSanPhamTheoDanhMuc(maDanhMuc, offset, limit);
            totalSanPhams = sanPhamDAO.getTotalSanPhamTheoDanhMuc(maDanhMuc);
            totalPages = (int) Math.ceil((double) totalSanPhams / limit);
        } else {
            sanPhams = sanPhamDAO.getSanPhams(offset, limit);
            totalSanPhams = sanPhamDAO.getTotalSanPham();
            totalPages = (int) Math.ceil((double) totalSanPhams / limit);
        }

        request.setAttribute("sanPhams", sanPhams);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("danhMuc", danhMuc); // Giữ danhMuc gốc để hiển thị trong phân trang
        request.getRequestDispatcher("views/TrangChu.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("datHang".equals(action)) {
            String id = request.getParameter("id");
            String soLuongStr = request.getParameter("soLuong");
            if (id != null && soLuongStr != null) {
                try {
                    int maSanPham = Integer.parseInt(id);
                    int soLuong = Integer.parseInt(soLuongStr);
                    SanPham sanPham = sanPhamDAO.getById(maSanPham);
                    request.setAttribute("sanPham", sanPham);
                    request.setAttribute("soLuong", soLuong);
                    request.getRequestDispatcher("views/DatHang.jsp").forward(request, response);
                    return;
                } catch (NumberFormatException e) {
                    // Xử lý lỗi
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

//package controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import dao.SanPhamDAO;
//import model.SanPham;
//
//@WebServlet("/san-pham")
//public class SanPhamController extends HttpServlet {
//    private static final long serialVersionUID = 1L;    
//    private SanPhamDAO sanPhamDAO;
//
//    @Override
//    public void init() throws ServletException {
//
//        sanPhamDAO = new SanPhamDAO();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        String action = request.getParameter("action");
//        // xem chi tiết sản phẩm
//        if ("chiTiet".equals(action)) {
//            // Xử lý xem chi tiết sản phẩm
//            String id = request.getParameter("id");
//            if (id != null) {
//                try {
//                    SanPham sanPham = sanPhamDAO.getById(Integer.parseInt(id));
//                    request.setAttribute("sanPham", sanPham);
//                    request.getRequestDispatcher("views/ChiTietSanPham.jsp").forward(request, response);
//                    return;
//                } catch (NumberFormatException e) {
//                    // Xử lý khi ID không hợp lệ
//                }
//            }
//            response.sendRedirect(request.getContextPath() + "/san-pham");
//            return;
//        }
//  
//        // Xử lý phân trang (giữ nguyên như cũ)
//        int page = 1;
//        int limit = 6;
//
//        String pageParam = request.getParameter("page");
//        if (pageParam != null) {
//            try {
//                page = Integer.parseInt(pageParam);
//            } catch (NumberFormatException e) {
//                page = 1;
//            }
//        }
//
//        String danhMuc = request.getParameter("danhMuc");
//        List<SanPham> sanPhams;
//
//        if (danhMuc != null && !danhMuc.isEmpty()) {
//            int offset = (page - 1) * limit;
//            sanPhams = sanPhamDAO.getSanPhamTheoDanhMuc(danhMuc, offset, limit);
//            int totalSanPhams = sanPhamDAO.getTotalSanPhamTheoDanhMuc(danhMuc);
//            int totalPages = (int) Math.ceil((double) totalSanPhams / limit);
//
//            request.setAttribute("sanPhams", sanPhams);
//            request.setAttribute("currentPage", page);
//            request.setAttribute("totalPages", totalPages);
//            request.setAttribute("danhMuc", danhMuc); // Gửi lại để tạo link phân trang đúng
//        }else {
//            int offset = (page - 1) * limit;
//            sanPhams = sanPhamDAO.getSanPhams(offset, limit);
//            int totalSanPhams = sanPhamDAO.getTotalSanPham();
//            int totalPages = (int) Math.ceil((double) totalSanPhams / limit);
//
//            request.setAttribute("sanPhams", sanPhams);
//            request.setAttribute("currentPage", page);
//            request.setAttribute("totalPages", totalPages);
//        }
//
////        int offset = (page - 1) * limit;
////        List<SanPham> sanPhams = sanPhamDAO.getSanPhams(offset, limit);
////        int totalSanPhams = sanPhamDAO.getTotalSanPham();
////        int totalPages = (int) Math.ceil((double) totalSanPhams / limit);
////
////        request.setAttribute("sanPhams", sanPhams);
////        request.setAttribute("currentPage", page);
////        request.setAttribute("totalPages", totalPages);
//
//        request.getRequestDispatcher("views/TrangChu.jsp").forward(request, response);
//        
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//        String action = request.getParameter("action");
//        
//        // đặt hàng
//        if("datHang".equals(action)) {
//        	String id = request.getParameter("id");
//        	String soLuongStr = request.getParameter("soLuong");
//
//        	if (id != null && soLuongStr != null) {
//        	    try {
//        	        int maSanPham = Integer.parseInt(id);
//        	        int soLuong = Integer.parseInt(soLuongStr);
//
//        	        // Lấy sản phẩm
//        	        SanPham sanPham = sanPhamDAO.getById(maSanPham);
//
//        	        // Đặt hàng hoặc chuyển đến trang xác nhận
//        	        request.setAttribute("sanPham", sanPham);
//        	        request.setAttribute("soLuong", soLuong);
//
//        	        request.getRequestDispatcher("views/DatHang.jsp").forward(request, response);
//        	        return;
//
//        	    } catch (NumberFormatException e) {
//        	        // xử lý lỗi parse nếu cần
//        	    }
//        	}
//
//        }
//        
//        doGet(request, response);
//    }
//
//    @Override
//    public void destroy() {
//        try {
//            if (sanPhamDAO != null) {
//                sanPhamDAO.closeConnection();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
