package controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import dao.AdminSanPhamDAO;
import dao.SanPhamDAO;
import dao.DanhMucDAO;
import model.SanPham;
import model.DanhMuc;

@WebServlet("/admin-san-pham")
@MultipartConfig
public class AdminSanPhamController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminSanPhamController.class.getName());
    private SanPhamDAO sanPhamDAO;
    private AdminSanPhamDAO adminSanPhamDAO;
    private DanhMucDAO danhMucDAO;

    @Override
    public void init() throws ServletException {
        sanPhamDAO = new SanPhamDAO();
        adminSanPhamDAO = new AdminSanPhamDAO();
        danhMucDAO = new DanhMucDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String admin = (String) session.getAttribute("admin");
        if (admin == null || !admin.equals("admin")) {
            String redirectUrl = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            LOGGER.warning("Unauthorized access attempt to admin-san-pham, redirecting to login with URL: " + redirectUrl);
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
                    handleListProducts(request, response);
                    break;
                case "edit":
                    handleEditProduct(request, response);
                    break;
                case "delete":
                    handleDeleteProduct(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
            }
        } catch (Exception e) {
            LOGGER.severe("Error processing GET request: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("/views/ErrorPage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Đảm bảo mã hóa UTF-8
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String admin = (String) session.getAttribute("admin"); // Thay "username" bằng "admin"
        System.out.println("doPost - Session admin: " + admin); // Log để debug
        if (admin == null || !admin.equals("admin")) {
            String redirectUrl = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            LOGGER.warning("Unauthorized POST attempt to admin-san-pham, redirecting to login with URL: " + redirectUrl);
            response.sendRedirect(request.getContextPath() + "/login-admin?redirect=" + java.net.URLEncoder.encode(redirectUrl, "UTF-8"));
            return;
        }

        String action = request.getParameter("action");
        System.out.println("Action received: " + action); // Log để kiểm tra action
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
            return;
        }

        try {
            switch (action) {
                case "add":
                    handleAddProduct(request, response);
                    break;
                case "update":
                    handleUpdateProduct(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
            }
        } catch (Exception e) {
            LOGGER.severe("Error processing POST request: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("/views/ErrorPage.jsp").forward(request, response);
        }
    }

    private void handleListProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<SanPham> sanPhams = sanPhamDAO.getSanPhams(0, Integer.MAX_VALUE);
            List<DanhMuc> danhMucs = danhMucDAO.getAll();
            if (sanPhams == null || danhMucs == null) {
                LOGGER.warning("Failed to retrieve products or categories");
                throw new ServletException("Failed to retrieve data");
            }
            request.setAttribute("sanPhams", sanPhams);
            request.setAttribute("danhMucs", danhMucs);
            request.getRequestDispatcher("/views/AdminSanPham.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.severe("Error in handleListProducts: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/views/ErrorPage.jsp").forward(request, response);
        }
    }

    private void handleEditProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                SanPham sanPham = sanPhamDAO.getById(id);
                if (sanPham != null) {
                    List<DanhMuc> danhMucs = danhMucDAO.getAll();
                    request.setAttribute("sanPham", sanPham);
                    request.setAttribute("danhMucs", danhMucs);
                    request.getRequestDispatcher("/views/AdminSanPham.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid product ID format: " + idParam);
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
    }

    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                adminSanPhamDAO.delete(id);
                LOGGER.info("Deleted product with ID: " + id);
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid product ID format for deletion: " + idParam);
                request.setAttribute("error", "ID sản phẩm không hợp lệ: " + e.getMessage());
                request.getRequestDispatcher("/views/ErrorPage.jsp").forward(request, response);
                return;
            } catch (RuntimeException e) {
                LOGGER.severe("Error deleting product: " + e.getMessage());
                request.setAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
                request.getRequestDispatcher("/views/ErrorPage.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String tenSanPham = request.getParameter("tenSanPham");
            String maDanhMucParam = request.getParameter("maDanhMuc");
            String giaGocParam = request.getParameter("giaGoc");
            String giaKhuyenMaiParam = request.getParameter("giaKhuyenMai");
            String tinhTrang = request.getParameter("tinhTrang");
            String soLuongTonKhoParam = request.getParameter("soLuongTonKho");
            String chiTiet = request.getParameter("chiTiet");

            if (tenSanPham == null || tenSanPham.trim().isEmpty() ||
                maDanhMucParam == null || giaGocParam == null ||
                tinhTrang == null || soLuongTonKhoParam == null) {
                LOGGER.warning("Missing required fields for adding product");
                response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
                return;
            }

            int maDanhMuc = Integer.parseInt(maDanhMucParam);
            double giaGoc = Double.parseDouble(giaGocParam);
            double giaKhuyenMai = (giaKhuyenMaiParam != null && !giaKhuyenMaiParam.isEmpty())
                    ? Double.parseDouble(giaKhuyenMaiParam) : 0;
            int soLuongTonKho = Integer.parseInt(soLuongTonKhoParam);

            String hinhAnh = handleImageUpload(request);

            SanPham sanPham = new SanPham();
            sanPham.setTenSanPham(tenSanPham);
            sanPham.setGiaGoc(giaGoc);
            sanPham.setGiaKhuyenMai(giaKhuyenMai);
            sanPham.setTinhTrang(tinhTrang);
            sanPham.setSoLuongTonKho(soLuongTonKho);
            sanPham.setHinhAnh(hinhAnh);
            sanPham.setChiTiet(chiTiet != null ? chiTiet : "");
            DanhMuc danhMuc = new DanhMuc();
            danhMuc.setMaDanhMuc(maDanhMuc);
            sanPham.setDanhMuc(danhMuc);

            adminSanPhamDAO.add(sanPham);
            LOGGER.info("Added new product: " + tenSanPham);
            response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid number format in add product: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String maSanPhamParam = request.getParameter("maSanPham");
            String tenSanPham = request.getParameter("tenSanPham");
            String maDanhMucParam = request.getParameter("maDanhMuc");
            String giaGocParam = request.getParameter("giaGoc");
            String giaKhuyenMaiParam = request.getParameter("giaKhuyenMai");
            String tinhTrang = request.getParameter("tinhTrang");
            String soLuongTonKhoParam = request.getParameter("soLuongTonKho");
            String chiTiet = request.getParameter("chiTiet");

            if (maSanPhamParam == null || tenSanPham == null || tenSanPham.trim().isEmpty() ||
                maDanhMucParam == null || giaGocParam == null ||
                tinhTrang == null || soLuongTonKhoParam == null) {
                LOGGER.warning("Missing required fields for updating product");
                response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
                return;
            }

            int maSanPham = Integer.parseInt(maSanPhamParam);
            int maDanhMuc = Integer.parseInt(maDanhMucParam);
            double giaGoc = Double.parseDouble(giaGocParam);
            double giaKhuyenMai = (giaKhuyenMaiParam != null && !giaKhuyenMaiParam.isEmpty())
                    ? Double.parseDouble(giaKhuyenMaiParam) : 0;
            int soLuongTonKho = Integer.parseInt(soLuongTonKhoParam);

            String hinhAnh = handleImageUpload(request);
            if (hinhAnh == null) {
                SanPham existingProduct = sanPhamDAO.getById(maSanPham);
                hinhAnh = (existingProduct != null) ? existingProduct.getHinhAnh() : null;
            }

            SanPham sanPham = new SanPham();
            sanPham.setMaSanPham(maSanPham);
            sanPham.setTenSanPham(tenSanPham);
            sanPham.setGiaGoc(giaGoc);
            sanPham.setGiaKhuyenMai(giaKhuyenMai);
            sanPham.setTinhTrang(tinhTrang);
            sanPham.setSoLuongTonKho(soLuongTonKho);
            sanPham.setHinhAnh(hinhAnh);
            sanPham.setChiTiet(chiTiet != null ? chiTiet : "");
            DanhMuc danhMuc = new DanhMuc();
            danhMuc.setMaDanhMuc(maDanhMuc);
            sanPham.setDanhMuc(danhMuc);

            adminSanPhamDAO.update(sanPham);
            LOGGER.info("Updated product with ID: " + maSanPham);
            response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid number format in update product: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
        }
    }

    private String handleImageUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("hinhAnh");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("");
            filePart.write(uploadPath + fileName);
            return fileName;
        }
        return null;
    }
}