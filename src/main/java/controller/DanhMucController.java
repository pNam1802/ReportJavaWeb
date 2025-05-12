package controller;

import dao.DanhMucDAO;
import dao.SanPhamDAO;
import interfaces.IDanhMuc;
import interfaces.ISanPham;
import model.DanhMuc;
import model.SanPham;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/danh-muc")
public class DanhMucController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private IDanhMuc danhMucDAO;
    private ISanPham sanPhamDAO;

    public void init() {
        danhMucDAO = new DanhMucDAO();
        sanPhamDAO = new SanPhamDAO();
    }

    // Hiển thị danh sách DanhMuc
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tenDanhMuc = request.getParameter("tenDanhMuc");

        // Nếu có tenDanhMuc, lọc sản phẩm theo danh mục
        if (tenDanhMuc != null && !tenDanhMuc.isEmpty()) {
        	int page = 1;
            int limit = 6;

            // Xử lý phân trang
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
            int totalPages;

            // Lấy mã danh mục từ tên danh mục
            DanhMuc danhMuc = danhMucDAO.getTenDanhMuc(tenDanhMuc);
            if (danhMuc != null) {
                int maDanhMuc = danhMuc.getMaDanhMuc();
                sanPhams = sanPhamDAO.getSanPhamTheoDanhMuc(maDanhMuc, offset, limit);
                totalSanPhams = sanPhamDAO.getTotalSanPhamTheoDanhMuc(maDanhMuc);
                totalPages = (int) Math.ceil((double) totalSanPhams / limit);
            } else {
                // Nếu danh mục không tồn tại, trả về danh sách rỗng
                sanPhams = new ArrayList<>();
                totalSanPhams = 0;
                totalPages = 1;
            }

            request.setAttribute("sanPhams", sanPhams);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("tenDanhMuc", tenDanhMuc);
            request.getRequestDispatcher("/views/DanhMuc.jsp").forward(request, response);
            return;
        }

        // Nếu không có tenDanhMuc, hiển thị danh sách danh mục
        List<DanhMuc> danhMucs = danhMucDAO.getAll();
        request.setAttribute("danhMucs", danhMucs);
        request.getRequestDispatcher("/views/DanhMuc.jsp").forward(request, response);
    }

    // Xử lý thêm DanhMuc
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tenDanhMuc = request.getParameter("tenDanhMuc");
        String moTa = request.getParameter("moTa");

        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setTenDanhMuc(tenDanhMuc);
        danhMuc.setMoTa(moTa);

        if (danhMucDAO.add(danhMuc)) {  // Nếu thêm thành công
            response.sendRedirect("danhmuc");  // Chuyển hướng lại trang danh sách
        } else {
            request.setAttribute("error", "Thêm danh mục không thành công.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/add.jsp"); // Nếu thất bại, quay lại trang thêm
            dispatcher.forward(request, response);
        }
    }
}
