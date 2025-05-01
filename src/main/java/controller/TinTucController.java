package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.TinTuc;
import dao.TinTucDAO;

@WebServlet("/tin-tuc")
public class TinTucController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TinTucDAO tinTucDAO;

    @Override
    public void init() throws ServletException {
        tinTucDAO = new TinTucDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Số tin tức trên mỗi trang
        int pageSize = 6;
        // Lấy số trang hiện tại từ tham số, mặc định là 1
        String pageStr = request.getParameter("page");
        int currentPage = pageStr != null ? Integer.parseInt(pageStr) : 1;

        // Lấy danh sách tin tức theo trang
        List<TinTuc> tinTucs = tinTucDAO.getTinTucByPage(currentPage, pageSize);
        // Tính tổng số trang
        int totalTinTuc = tinTucDAO.getTotalTinTuc();
        int totalPages = (int) Math.ceil((double) totalTinTuc / pageSize);

        // Đặt các thuộc tính để gửi tới JSP
        request.setAttribute("tinTucs", tinTucs);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        // Chuyển hướng tới TinTuc.jsp
        request.getRequestDispatcher("/views/TinTuc.jsp").forward(request, response);
    }
}