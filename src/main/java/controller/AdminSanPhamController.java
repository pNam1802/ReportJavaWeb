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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String admin = (String) session.getAttribute("admin");
        System.out.println("doPost - Session admin: " + admin);
        if (admin == null || !admin.equals("admin")) {
            String redirectUrl = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            LOGGER.warning("Unauthorized POST attempt to admin-san-pham, redirecting to login with URL: " + redirectUrl);
            response.sendRedirect(request.getContextPath() + "/login-admin?redirect=" + java.net.URLEncoder.encode(redirectUrl, "UTF-8"));
            return;
        }

        String action = request.getParameter("action");
        System.out.println("Action received: " + action);
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
                case "checkMaSanPham":
                    handleCheckMaSanPham(request, response);
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

    private void handleCheckMaSanPham(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maSanPhamParam = request.getParameter("maSanPham");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            int maSanPham = Integer.parseInt(maSanPhamParam);
            SanPham existingProduct = sanPhamDAO.getById(maSanPham);
            boolean exists = (existingProduct != null);
            LOGGER.info("Checking maSanPham: " + maSanPham + ", exists: " + exists); // Debug log
            response.getWriter().write("{\"exists\":" + exists + "}");
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid maSanPham format: " + maSanPhamParam);
            response.getWriter().write("{\"exists\":false}");
        }
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response)
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
                maDanhMucParam == null || giaGocParam == null || tinhTrang == null || 
                soLuongTonKhoParam == null) {
                LOGGER.warning("Missing required fields for adding product");
                response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
                return;
            }

            int maSanPham = Integer.parseInt(maSanPhamParam);
            // Kiểm tra mã sản phẩm trùng lặp
            SanPham existingProduct = sanPhamDAO.getById(maSanPham);
            if (existingProduct != null) {
                LOGGER.warning("Duplicate product ID: " + maSanPham);
                List<SanPham> sanPhams = sanPhamDAO.getSanPhams(0, Integer.MAX_VALUE);
                List<DanhMuc> danhMucs = danhMucDAO.getAll();
                request.setAttribute("sanPhams", sanPhams); // Giữ danh sách sản phẩm
                request.setAttribute("danhMucs", danhMucs); // Giữ danh sách danh mục
                request.setAttribute("formError", true); // Giữ form mở
                request.setAttribute("maSanPham", maSanPhamParam);
                request.setAttribute("tenSanPham", tenSanPham);
                request.setAttribute("maDanhMuc", maDanhMucParam);
                request.setAttribute("giaGoc", giaGocParam);
                request.setAttribute("giaKhuyenMai", giaKhuyenMaiParam);
                request.setAttribute("tinhTrang", tinhTrang);
                request.setAttribute("soLuongTonKho", soLuongTonKhoParam);
                request.setAttribute("chiTiet", chiTiet);
                Part filePart = request.getPart("hinhAnh");
                if (filePart != null && filePart.getSize() > 0) {
                    request.setAttribute("hinhAnh", filePart.getSubmittedFileName());
                }
                request.setAttribute("maSanPhamError", "Mã sản phẩm đã tồn tại, vui lòng nhập mã khác."); // Thông báo lỗi
                request.getRequestDispatcher("/views/AdminSanPham.jsp").forward(request, response);
                return;
            }

            int maDanhMuc = Integer.parseInt(maDanhMucParam);
            double giaGoc = Double.parseDouble(giaGocParam);
            double giaKhuyenMai = (giaKhuyenMaiParam != null && !giaKhuyenMaiParam.isEmpty())
                    ? Double.parseDouble(giaKhuyenMaiParam) : 0;
            int soLuongTonKho = Integer.parseInt(soLuongTonKhoParam);

            String hinhAnh = handleImageUpload(request);

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

            adminSanPhamDAO.add(sanPham);
            LOGGER.info("Added new product: " + tenSanPham + " with ID: " + maSanPham);
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

            SanPham existingProduct = sanPhamDAO.getById(maSanPham);
            if (existingProduct == null) {
                LOGGER.warning("Product not found with ID: " + maSanPham);
                response.sendRedirect(request.getContextPath() + "/admin-san-pham?action=list");
                return;
            }

            String hinhAnh = handleImageUpload(request);
            if (hinhAnh == null) {
                hinhAnh = existingProduct.getHinhAnh();
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