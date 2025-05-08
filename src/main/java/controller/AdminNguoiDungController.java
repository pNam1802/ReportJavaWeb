
package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.NguoiDungDAO;
import model.NguoiDung;

@WebServlet("/admin/nguoi-dung")
public class AdminNguoiDungController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NguoiDungDAO nguoiDungDAO;

    @Override
    public void init() throws ServletException {
        nguoiDungDAO = new NguoiDungDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login-admin");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("edit".equals(action)) {
                int maNguoiDung = Integer.parseInt(request.getParameter("maNguoiDung"));
                NguoiDung nguoiDung = nguoiDungDAO.getNguoiDungById(maNguoiDung);
                request.setAttribute("nguoiDung", nguoiDung);
                request.getRequestDispatcher("/views/AdminNguoiDung.jsp").forward(request, response);
            } else {
                int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
                int pageSize = 10;
                List<NguoiDung> nguoiDungs = nguoiDungDAO.getNguoiDungsByPage(page, pageSize);
                int totalNguoiDungs = nguoiDungDAO.getTotalNguoiDungs();
                int totalPages = (int) Math.ceil((double) totalNguoiDungs / pageSize);

                request.setAttribute("nguoiDungs", nguoiDungs);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/views/AdminNguoiDung.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/views/AdminNguoiDung.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login-admin");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                NguoiDung nguoiDung = new NguoiDung();
                nguoiDung.setHoTen(request.getParameter("hoTen"));
                nguoiDung.setSdt(request.getParameter("sdt"));
                nguoiDung.setEmail(request.getParameter("email"));
                nguoiDung.setDiaChi(request.getParameter("diaChi"));
				/*
				 * nguoiDung.setTenDangNhap(request.getParameter("tenDangNhap"));
				 * nguoiDung.setMatKhau(request.getParameter("matKhau"));
				 * nguoiDung.setVaiTro(request.getParameter("vaiTro"));
				 */
                nguoiDungDAO.addNguoiDung(nguoiDung);
            } else if ("update".equals(action)) {
                NguoiDung nguoiDung = new NguoiDung();
                nguoiDung.setMaNguoiDung(Integer.parseInt(request.getParameter("maNguoiDung")));
                nguoiDung.setHoTen(request.getParameter("hoTen"));
                nguoiDung.setSdt(request.getParameter("sdt"));
                nguoiDung.setEmail(request.getParameter("email"));
                nguoiDung.setDiaChi(request.getParameter("diaChi"));
				/*
				 * nguoiDung.setTenDangNhap(request.getParameter("tenDangNhap"));
				 * nguoiDung.setMatKhau(request.getParameter("matKhau"));
				 * nguoiDung.setVaiTro(request.getParameter("vaiTro"));
				 */
                nguoiDungDAO.updateNguoiDung(nguoiDung);
            } else if ("delete".equals(action)) {
                int maNguoiDung = Integer.parseInt(request.getParameter("maNguoiDung"));
                nguoiDungDAO.deleteNguoiDung(maNguoiDung);
            }
            response.sendRedirect(request.getContextPath() + "/admin/nguoi-dung");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý: " + e.getMessage());
            doGet(request, response);
        }
    }
}
