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
        sanPhamDAO = new SanPhamDAO(); // Khởi tạo DAO mà không cần truyền tham số
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int page = 1; // Trang mặc định là 1
        int limit = 6; // Số sản phẩm mỗi trang

        // Kiểm tra tham số page từ request
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam); // Đặt giá trị cho page
            } catch (NumberFormatException e) {
                page = 1; // Nếu tham số không hợp lệ, mặc định là trang 1
            }
        }

        int offset = (page - 1) * limit; // Tính toán vị trí bắt đầu cho phân trang
        List<SanPham> sanPhams = sanPhamDAO.getSanPhams(offset, limit); // Lấy sản phẩm từ database
        int totalSanPhams = sanPhamDAO.getTotalSanPham(); // Lấy tổng số sản phẩm
        int totalPages = (int) Math.ceil((double) totalSanPhams / limit); // Tính tổng số trang

        // Đặt các thuộc tính cần thiết cho JSP
        request.setAttribute("sanPhams", sanPhams);
        request.setAttribute("currentPage", page); // Đặt trang hiện tại vào request
        request.setAttribute("totalPages", totalPages); // Đặt tổng số trang vào request

        // Chuyển tiếp đến trang JSP
        request.getRequestDispatcher("views/TrangChu.jsp").forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response); // POST xử lý như GET
    }

    @Override
    public void destroy() {
        // Đảm bảo đóng kết nối khi servlet bị hủy
        try {
            if (sanPhamDAO != null) {
                sanPhamDAO.closeConnection(); // Đóng kết nối sau khi servlet bị hủy
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
