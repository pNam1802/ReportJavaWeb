package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.TinTuc;
import dao.TinTucDAO;

@WebServlet("/chi-tiet-tin-tuc")
public class ChiTietTinTucController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TinTucDAO tinTucDAO;

    @Override
    public void init() throws ServletException {
        tinTucDAO = new TinTucDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
        try {
            // Lấy maTinTuc từ tham số
            String maTinTucStr = request.getParameter("maTinTuc");
            if (maTinTucStr == null || maTinTucStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã tin tức không hợp lệ");
                return;
            }

            int maTinTuc = Integer.parseInt(maTinTucStr);
            // Lấy thông tin chi tiết tin tức
            TinTuc tinTuc = tinTucDAO.getTinTucById(maTinTuc);

            if (tinTuc == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy tin tức");
                return;
            }

            // Đặt tin tức vào request để gửi tới JSP
            request.setAttribute("tinTuc", tinTuc);
            // Chuyển hướng tới ChiTietTinTuc.jsp
            request.getRequestDispatcher("/views/ChiTietTinTuc.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã tin tức không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể tải chi tiết tin tức: " + e.getMessage());
            
        }
    }
}