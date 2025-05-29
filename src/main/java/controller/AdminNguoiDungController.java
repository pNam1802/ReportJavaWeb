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
import interfaces.IAdminNguoiDung;
import model.NguoiDung;

@WebServlet("/admin/nguoi-dung")
public class AdminNguoiDungController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IAdminNguoiDung nguoiDungDAO;

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
        String role = request.getParameter("role");
        try {
            if ("edit".equals(action)) {
                int maNguoiDung = Integer.parseInt(request.getParameter("maNguoiDung"));
                NguoiDung nguoiDung = nguoiDungDAO.getNguoiDungById(maNguoiDung);
                request.setAttribute("nguoiDung", nguoiDung);
                request.setAttribute("role", role);
                request.getRequestDispatcher("/views/AdminNguoiDung.jsp").forward(request, response);
            } else {
                int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
                int pageSize = 10;
                List<NguoiDung> nguoiDungs;
                int totalNguoiDungs;
                int totalPages;

                if (role != null && (role.equals("admin") || role.equals("user"))) {
                    nguoiDungs = nguoiDungDAO.getNguoiDungsByRoleAndPage(role, page, pageSize);
                    totalNguoiDungs = nguoiDungDAO.getTotalNguoiDungsByRole(role);
                } else {
                    nguoiDungs = nguoiDungDAO.getNguoiDungsByPage(page, pageSize);
                    totalNguoiDungs = nguoiDungDAO.getTotalNguoiDungs();
                }
                totalPages = (int) Math.ceil((double) totalNguoiDungs / pageSize);

                request.setAttribute("nguoiDungs", nguoiDungs);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("role", role);
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
                addNguoiDung(request, response);
            } else if ("update".equals(action)) {
                updateNguoiDung(request, response);
            } else if ("delete".equals(action)) {
                String[] maNguoiDungs = request.getParameterValues("maNguoiDungs");
                String maNguoiDung = request.getParameter("maNguoiDung");
                if (maNguoiDungs != null && maNguoiDungs.length > 0) {
                    deleteMultipleNguoiDung(request, response);
                } else if (maNguoiDung != null) {
                    deleteNguoiDung(request, response);
                }
            }
            String role = request.getParameter("role");
            String redirectUrl = request.getContextPath() + "/admin/nguoi-dung";
            if (role != null && !role.isEmpty()) {
                redirectUrl += "?role=" + role;
            }
            response.sendRedirect(redirectUrl);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void addNguoiDung(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setHoTen(request.getParameter("hoTen"));
        nguoiDung.setSdt(request.getParameter("sdt"));
        nguoiDung.setEmail(request.getParameter("email"));
        nguoiDung.setDiaChi(request.getParameter("diaChi"));
        nguoiDung.setTenDangNhap(request.getParameter("tenDangNhap"));
        nguoiDung.setMatKhau(request.getParameter("matKhau"));
        nguoiDung.setVaiTro(request.getParameter("vaiTro"));

        nguoiDungDAO.addNguoiDung(nguoiDung);
    }

    private void updateNguoiDung(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setMaNguoiDung(Integer.parseInt(request.getParameter("maNguoiDung")));
        nguoiDung.setHoTen(request.getParameter("hoTen"));
        nguoiDung.setSdt(request.getParameter("sdt"));
        nguoiDung.setEmail(request.getParameter("email"));
        nguoiDung.setDiaChi(request.getParameter("diaChi"));
        nguoiDung.setTenDangNhap(request.getParameter("tenDangNhap"));
        nguoiDung.setMatKhau(request.getParameter("matKhau"));
        nguoiDung.setVaiTro(request.getParameter("vaiTro"));

        nguoiDungDAO.updateNguoiDung(nguoiDung);
    }

    private void deleteNguoiDung(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int maNguoiDung = Integer.parseInt(request.getParameter("maNguoiDung"));
        nguoiDungDAO.deleteNguoiDung(maNguoiDung);
    }

    private void deleteMultipleNguoiDung(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String[] maNguoiDungs = request.getParameterValues("maNguoiDungs");
        for (String maNguoiDung : maNguoiDungs) {
            nguoiDungDAO.deleteNguoiDung(Integer.parseInt(maNguoiDung));
        }
    }
}