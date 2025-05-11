package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import interfaces.IAdminSanPham;
import interfaces.ISanPham;
import interfaces.IDanhMuc;
import dao.AdminSanPhamDAO;
import dao.SanPhamDAO;
import dao.DanhMucDAO;
import model.SanPham;
import model.DanhMuc;

@WebServlet("/admin-san-pham")
@MultipartConfig
public class AdminSanPhamController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ISanPham sanPhamDAO;
    private IAdminSanPham adminSanPhamDAO;
    private IDanhMuc danhMucDAO;

    @Override
    public void init() throws ServletException {
        sanPhamDAO = new SanPhamDAO();
        adminSanPhamDAO = new AdminSanPhamDAO();
        danhMucDAO = new DanhMucDAO();
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
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            int pageSize = 10;
            int offset = (page - 1) * pageSize;
            List<SanPham> sanPhams = sanPhamDAO.getSanPhams(offset, pageSize);
            List<DanhMuc> danhMucs = danhMucDAO.getAll();
            int totalSanPhams = sanPhamDAO.getTotalSanPham();
            int totalPages = (int) Math.ceil((double) totalSanPhams / pageSize);

            if ("edit".equals(action)) {
                int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
                SanPham sanPham = sanPhamDAO.getById(maSanPham);
                if (sanPham == null) {
                    request.setAttribute("errorMessage", "Không tìm thấy sản phẩm với mã " + maSanPham);
                }
                request.setAttribute("sanPham", sanPham);
            } else if ("checkMaSanPham".equals(action)) {
                String maSanPhamParam = request.getParameter("maSanPham");
                int maSanPham = Integer.parseInt(maSanPhamParam);
                SanPham existingProduct = sanPhamDAO.getById(maSanPham);
                response.setContentType("text/plain");
                response.getWriter().write(existingProduct != null ? "exists" : "not_exists");
                return;
            }

            request.setAttribute("sanPhams", sanPhams);
            request.setAttribute("danhMucs", danhMucs);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/views/AdminSanPham.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage());
            request.getRequestDispatcher("/views/AdminSanPham.jsp").forward(request, response);
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
                addProduct(request, response);
            } else if ("update".equals(action)) {
                updateProduct(request, response);
            } else if ("delete".equals(action)) {
                deleteProduct(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin-san-pham");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Lỗi xử lý cơ sở dữ liệu: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NumberFormatException, SQLException {
        String maSanPhamParam = request.getParameter("maSanPham");
        int maSanPham = Integer.parseInt(maSanPhamParam);
        String soLuongTonKhoParam = request.getParameter("soLuongTonKho");
        int soLuongTonKho = Integer.parseInt(soLuongTonKhoParam);
        String giaGocParam = request.getParameter("giaGoc");
        double giaGoc = Double.parseDouble(giaGocParam);

        // Set số âm thành 0
        if (soLuongTonKho < 0) {
            soLuongTonKho = 0;
        }
        if (giaGoc < 0) {
            giaGoc = 0;
        }

        // Kiểm tra mã sản phẩm trùng lặp
        SanPham existingProduct = sanPhamDAO.getById(maSanPham);
        if (existingProduct != null) {
            List<SanPham> sanPhams = sanPhamDAO.getSanPhams(0, 10);
            List<DanhMuc> danhMucs = danhMucDAO.getAll();
            request.setAttribute("sanPhams", sanPhams);
            request.setAttribute("danhMucs", danhMucs);
            request.setAttribute("formError", true);
            request.setAttribute("maSanPham", maSanPhamParam);
            request.setAttribute("tenSanPham", request.getParameter("tenSanPham"));
            request.setAttribute("maDanhMuc", request.getParameter("maDanhMuc"));
            request.setAttribute("giaGoc", String.valueOf(giaGoc));
            request.setAttribute("tinhTrang", request.getParameter("tinhTrang"));
            request.setAttribute("soLuongTonKho", String.valueOf(soLuongTonKho));
            request.setAttribute("chiTiet", request.getParameter("chiTiet"));
            request.setAttribute("maSanPhamError", "Đã có mã sản phẩm này.");
            request.getRequestDispatcher("/views/AdminSanPham.jsp").forward(request, response);
            return;
        }

        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(maSanPham);
        sanPham.setTenSanPham(request.getParameter("tenSanPham"));
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setMaDanhMuc(Integer.parseInt(request.getParameter("maDanhMuc")));
        sanPham.setDanhMuc(danhMuc);
        sanPham.setGiaGoc(giaGoc);
        sanPham.setGiaKhuyenMai(giaGoc);
        sanPham.setTinhTrang(request.getParameter("tinhTrang"));
        sanPham.setSoLuongTonKho(soLuongTonKho);
        sanPham.setChiTiet(request.getParameter("chiTiet"));
        String hinhAnh = imageUpload(request);
        sanPham.setHinhAnh(hinhAnh);

        adminSanPhamDAO.add(sanPham);
        response.sendRedirect(request.getContextPath() + "/admin-san-pham");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NumberFormatException, SQLException {
        int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
        String soLuongTonKhoParam = request.getParameter("soLuongTonKho");
        int soLuongTonKho = Integer.parseInt(soLuongTonKhoParam);
        String giaGocParam = request.getParameter("giaGoc");
        double giaGoc = Double.parseDouble(giaGocParam);
        String giaKhuyenMaiParam = request.getParameter("giaKhuyenMai");
        double giaKhuyenMai = Double.parseDouble(giaKhuyenMaiParam);

        if (soLuongTonKho < 0) {
            soLuongTonKho = 0;
        }
        if (giaGoc < 0) {
            giaGoc = 0;
        }
        if (giaKhuyenMai < 0) {
            giaKhuyenMai = 0;
        }

        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(maSanPham);
        sanPham.setTenSanPham(request.getParameter("tenSanPham"));
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setMaDanhMuc(Integer.parseInt(request.getParameter("maDanhMuc")));
        sanPham.setDanhMuc(danhMuc);
        sanPham.setGiaGoc(giaGoc);
        sanPham.setGiaKhuyenMai(giaKhuyenMai);
        sanPham.setTinhTrang(request.getParameter("tinhTrang"));
        sanPham.setSoLuongTonKho(soLuongTonKho);
        sanPham.setChiTiet(request.getParameter("chiTiet"));
        String hinhAnh = imageUpload(request);
        SanPham existingProduct = sanPhamDAO.getById(maSanPham);
        sanPham.setHinhAnh(hinhAnh != null ? hinhAnh : (existingProduct != null ? existingProduct.getHinhAnh() : null));

        adminSanPhamDAO.update(sanPham);
        response.sendRedirect(request.getContextPath() + "/admin-san-pham");
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NumberFormatException, SQLException {
        int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));
        adminSanPhamDAO.delete(maSanPham);
        response.sendRedirect(request.getContextPath() + "/admin-san-pham");
    }

    private String imageUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("hinhAnh");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("") + "images/";
            java.io.File uploadDir = new java.io.File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            filePart.write(uploadPath + fileName);
            return fileName;
        }
        return null;
    }
}