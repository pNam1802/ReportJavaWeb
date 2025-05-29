package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminKhuyenMaiDAO;
import dao.SanPhamDAO;
import interfaces.IAdminKhuyenMai;
import interfaces.ISanPham;
import model.KhuyenMai;
import model.SanPham;

@WebServlet("/admin-khuyen-mai")
public class AdminKhuyenMaiController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IAdminKhuyenMai adminKhuyenMaiDAO;
    private ISanPham sanPhamDAO;
    private static final int PROMOTIONS_PER_PAGE = 10;

    @Override
    public void init() throws ServletException {
    	adminKhuyenMaiDAO = new AdminKhuyenMaiDAO();
    	sanPhamDAO = new SanPhamDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("admin")) {
            String redirectUrl = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            response.sendRedirect(request.getContextPath() + "/login-admin?redirect=" + java.net.URLEncoder.encode(redirectUrl, "UTF-8"));
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    handleListPromotions(request, response);
                    break;
                case "edit":
                    handleEditPromotion(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin-khuyen-mai?action=list");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("/views/ErrorPage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("admin")) {
            String redirectUrl = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            response.sendRedirect(request.getContextPath() + "/login-admin?redirect=" + java.net.URLEncoder.encode(redirectUrl, "UTF-8"));
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin-khuyen-mai?action=list");
            return;
        }

        try {
            switch (action) {
                case "add":
                    handleAddPromotion(request, response);
                    break;
                case "update":
                    handleUpdatePromotion(request, response);
                    break;
                case "delete":
                    handleDeletePromotion(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin-khuyen-mai?action=list");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            handleListPromotions(request, response);
        }
    }

    private void handleListPromotions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Update expired promotions
        	adminKhuyenMaiDAO.updateExpiredPromotions();

            String pageParam = request.getParameter("page");
            int page = pageParam != null ? Integer.parseInt(pageParam) : 1;
            if (page < 1) {
				page = 1;
			}

            int offset = (page - 1) * PROMOTIONS_PER_PAGE;
            int totalPromotions = adminKhuyenMaiDAO.getTotalPromotions();
            int totalPages = (int) Math.ceil((double) totalPromotions / PROMOTIONS_PER_PAGE);

            List<KhuyenMai> khuyenMais = adminKhuyenMaiDAO.getPromotions(offset, PROMOTIONS_PER_PAGE);
            List<SanPham> sanPhams = sanPhamDAO.getSanPhams(0, Integer.MAX_VALUE);

            request.setAttribute("khuyenMais", khuyenMais);
            request.setAttribute("sanPhams", sanPhams);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/views/AdminKhuyenMai.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi khi lấy danh sách khuyến mãi: " + e.getMessage());
            request.getRequestDispatcher("/views/ErrorPage.jsp").forward(request, response);
        }
    }

    private void handleEditPromotion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maKhuyenMaiParam = request.getParameter("id");
        if (maKhuyenMaiParam != null && !maKhuyenMaiParam.isEmpty()) {
            try {
                int id = Integer.parseInt(maKhuyenMaiParam);
                KhuyenMai khuyenMai = adminKhuyenMaiDAO.getById(id);
                if (khuyenMai != null) {
                    List<SanPham> sanPhams = sanPhamDAO.getSanPhams(0, Integer.MAX_VALUE);
                    request.setAttribute("khuyenMai", khuyenMai);
                    request.setAttribute("sanPhams", sanPhams);
                    request.getRequestDispatcher("/views/AdminKhuyenMai.jsp").forward(request, response);
                    return;
                }
                request.setAttribute("errorMessage", "Không tìm thấy khuyến mãi với ID: " + id);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID khuyến mãi không hợp lệ: " + e.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", "ID khuyến mãi không được cung cấp.");
        }
        handleListPromotions(request, response);
    }

    private void handleDeletePromotion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String maKhuyenMaiParam = request.getParameter("maKhuyenMai");
        if (maKhuyenMaiParam != null && !maKhuyenMaiParam.isEmpty()) {
            try {
                int id = Integer.parseInt(maKhuyenMaiParam);
                KhuyenMai khuyenMai = adminKhuyenMaiDAO.getById(id);
                if (khuyenMai == null) {
                    request.setAttribute("errorMessage", "Không tìm thấy khuyến mãi với ID: " + id);
                    handleListPromotions(request, response);
                    return;
                }
                adminKhuyenMaiDAO.delete(id);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID khuyến mãi không hợp lệ: " + e.getMessage());
                handleListPromotions(request, response);
                return;
            } catch (RuntimeException e) {
                request.setAttribute("errorMessage", "Lỗi khi xóa khuyến mãi: " + e.getMessage());
                handleListPromotions(request, response);
                return;
            }
        } else {
            request.setAttribute("errorMessage", "ID khuyến mãi không được cung cấp.");
            handleListPromotions(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin-khuyen-mai?action=list");
    }

    private void handleAddPromotion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String maSanPhamParam = request.getParameter("maSanPham");
            String ngayBatDau = request.getParameter("ngayBatDau");
            String ngayKetThuc = request.getParameter("ngayKetThuc");
            String giaKhuyenMaiParam = request.getParameter("giaKhuyenMai");

            if (maSanPhamParam == null || ngayBatDau == null || ngayKetThuc == null || giaKhuyenMaiParam == null) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ các trường bắt buộc.");
                handleListPromotions(request, response);
                return;
            }

            int maSanPham = Integer.parseInt(maSanPhamParam);
            double giaKhuyenMai = Double.parseDouble(giaKhuyenMaiParam);
            Date startDate = Date.valueOf(ngayBatDau);
            Date endDate = Date.valueOf(ngayKetThuc);

            if (endDate.before(startDate)) {
                request.setAttribute("errorMessage", "Ngày kết thúc phải sau ngày bắt đầu.");
                handleListPromotions(request, response);
                return;
            }

            KhuyenMai khuyenMai = new KhuyenMai();
            khuyenMai.setMaSanPham(maSanPham);
            khuyenMai.setNgayBatDau(startDate);
            khuyenMai.setNgayKetThuc(endDate);
            khuyenMai.setGiaKhuyenMai(giaKhuyenMai);

            adminKhuyenMaiDAO.add(khuyenMai);
            response.sendRedirect(request.getContextPath() + "/admin-khuyen-mai?action=list");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập không hợp lệ: " + e.getMessage());
            handleListPromotions(request, response);
        }
    }

    private void handleUpdatePromotion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String maKhuyenMaiParam = request.getParameter("maKhuyenMai");
            String maSanPhamParam = request.getParameter("maSanPham");
            String ngayBatDau = request.getParameter("ngayBatDau");
            String ngayKetThuc = request.getParameter("ngayKetThuc");
            String giaKhuyenMaiParam = request.getParameter("giaKhuyenMai");

            if (maKhuyenMaiParam == null || maSanPhamParam == null || ngayBatDau == null || ngayKetThuc == null || giaKhuyenMaiParam == null) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ các trường bắt buộc.");
                handleListPromotions(request, response);
                return;
            }

            int maKhuyenMai = Integer.parseInt(maKhuyenMaiParam);
            int maSanPham = Integer.parseInt(maSanPhamParam);
            double giaKhuyenMai = Double.parseDouble(giaKhuyenMaiParam);
            Date startDate = Date.valueOf(ngayBatDau);
            Date endDate = Date.valueOf(ngayKetThuc);

            if (endDate.before(startDate)) {
                request.setAttribute("errorMessage", "Ngày kết thúc phải sau ngày bắt đầu.");
                handleListPromotions(request, response);
                return;
            }

            KhuyenMai khuyenMai = new KhuyenMai();
            khuyenMai.setMaKhuyenMai(maKhuyenMai);
            khuyenMai.setMaSanPham(maSanPham);
            khuyenMai.setNgayBatDau(startDate);
            khuyenMai.setNgayKetThuc(endDate);
            khuyenMai.setGiaKhuyenMai(giaKhuyenMai);

            adminKhuyenMaiDAO.update(khuyenMai);
            response.sendRedirect(request.getContextPath() + "/admin-khuyen-mai?action=list");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Dữ liệu nhập không hợp lệ: " + e.getMessage());
            handleListPromotions(request, response);
        }
    }
}