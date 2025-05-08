package controller;

import dao.QuanLyTinTucDAO;
import model.TinTuc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/QuanLyTinTuc")
public class QuanLyTinTucController extends HttpServlet {
    private QuanLyTinTucDAO tinTucDAO;

    @Override
    public void init() {
        tinTucDAO = new QuanLyTinTucDAO();
        System.out.println("Controller: Initialized QuanLyTinTucDAO");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Controller: Entering doGet for QuanLyTinTuc");
        String action = request.getParameter("action");
        int pageSize = 6;
        String pageStr = request.getParameter("page");
        int currentPage = 1;
        List<TinTuc> tinTucs = new ArrayList<>();
        int totalTinTuc = 0;
        int totalPages = 1;

        // Xử lý tham số page
        System.out.println("Controller: Received page parameter: " + pageStr);
        try {
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1) currentPage = 1;
            }
            System.out.println("Controller: Parsed page number: " + currentPage);
        } catch (NumberFormatException e) {
            System.err.println("Controller: Invalid page number: " + pageStr + ", defaulting to page 1");
            request.setAttribute("message", "Số trang không hợp lệ!");
            request.setAttribute("messageType", "danger");
            currentPage = 1;
        }

        try {
            // Lấy danh sách tin tức
            System.out.println("Controller: Calling getTinTucByPage for page " + currentPage);
            tinTucs = tinTucDAO.getTinTucByPage(currentPage, pageSize);
            System.out.println("Controller: getTinTucByPage returned " + tinTucs.size() + " items");
            System.out.println("Controller: Calling getTotalTinTuc");
            totalTinTuc = tinTucDAO.getTotalTinTuc();
            totalPages = (int) Math.ceil((double) totalTinTuc / pageSize);
            System.out.println("Controller: Fetched " + tinTucs.size() + " news items, total news: " + totalTinTuc + ", total pages: " + totalPages);

            // Lấy danh sách sản phẩm
            System.out.println("Controller: Calling getAllProducts");
            List<QuanLyTinTucDAO.Product> products = tinTucDAO.getAllProducts();
            System.out.println("Controller: getAllProducts returned " + products.size() + " items");

            // Đặt các thuộc tính vào request
            request.setAttribute("newsList", tinTucs);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("products", products);
            System.out.println("Controller: Set newsList with size " + tinTucs.size());
            System.out.println("Controller: Set products with size " + products.size());

            if (action != null && action.equals("edit")) {
                // Hiển thị form sửa
                String idStr = request.getParameter("id");
                System.out.println("Controller: Action=edit, received id: " + idStr);
                if (idStr == null || idStr.isEmpty()) {
                    request.setAttribute("message", "ID tin tức không hợp lệ!");
                    request.setAttribute("messageType", "danger");
                } else {
                    try {
                        int maTinTuc = Integer.parseInt(idStr);
                        System.out.println("Controller: Calling getTinTucById for ID " + maTinTuc);
                        TinTuc news = tinTucDAO.getTinTucById(maTinTuc);
                        if (news == null) {
                            request.setAttribute("message", "Tin tức không tồn tại!");
                            request.setAttribute("messageType", "danger");
                        } else {
                            request.setAttribute("news", news);
                            System.out.println("Controller: Loaded news for edit, ID: " + maTinTuc);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Controller: Invalid news ID: " + idStr);
                        request.setAttribute("message", "ID tin tức không hợp lệ!");
                        request.setAttribute("messageType", "danger");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Controller: Error in doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            tinTucs = new ArrayList<>();
            request.setAttribute("newsList", tinTucs);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", 1);
            // Lấy products trong trường hợp lỗi
            List<QuanLyTinTucDAO.Product> products = tinTucDAO.getAllProducts();
            request.setAttribute("products", products);
            System.out.println("Controller: Exception occurred, set empty newsList with size " + tinTucs.size());
            System.out.println("Controller: Set products with size " + products.size());
        }

        System.out.println("Controller: Forwarding to TinTucHandle.jsp, newsList size: " + (request.getAttribute("newsList") != null ? ((List<?>) request.getAttribute("newsList")).size() : "null"));
        request.getRequestDispatcher("/views/TinTucHandle.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Controller: Entering doPost for QuanLyTinTuc");
        String action = request.getParameter("action");
        System.out.println("Controller: Received action: " + action);

        try {
            if (action == null || action.isEmpty()) {
                throw new IllegalArgumentException("Hành động không được chỉ định!");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            TinTuc news = new TinTuc();

            if (action.equals("add") || action.equals("update")) {
                String tieuDe = request.getParameter("tieuDe");
                String noiDung = request.getParameter("noiDung");
                String ngayDangStr = request.getParameter("ngayDang");
                String maSanPhamStr = request.getParameter("maSanPham");
                System.out.println("Controller: Received form data - tieuDe: " + tieuDe + ", noiDung: " + noiDung + ", ngayDang: " + ngayDangStr + ", maSanPham: " + maSanPhamStr);

                if (tieuDe == null || noiDung == null || ngayDangStr == null || maSanPhamStr == null ||
                    tieuDe.trim().isEmpty() || noiDung.trim().isEmpty() || ngayDangStr.trim().isEmpty() || maSanPhamStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng điền đầy đủ thông tin!");
                }

                news.setTieuDe(tieuDe);
                news.setNoiDung(noiDung);
                news.setNgayDang(dateFormat.parse(ngayDangStr));
                news.setMaSanPham(Integer.parseInt(maSanPhamStr));
            }

            if (action.equals("add")) {
                System.out.println("Controller: Adding news with title: " + news.getTieuDe());
                tinTucDAO.addTinTuc(news);
                request.setAttribute("message", "Thêm tin tức thành công!");
                request.setAttribute("messageType", "success");
            } else if (action.equals("update")) {
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.isEmpty()) {
                    throw new IllegalArgumentException("ID tin tức không hợp lệ!");
                }
                news.setMaTinTuc(Integer.parseInt(idStr));
                System.out.println("Controller: Updating news with ID: " + news.getMaTinTuc());
                tinTucDAO.updateTinTuc(news);
                request.setAttribute("message", "Cập nhật tin tức thành công!");
                request.setAttribute("messageType", "success");
            } else if (action.equals("delete")) {
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.isEmpty()) {
                    throw new IllegalArgumentException("ID tin tức không hợp lệ!");
                }
                int maTinTuc = Integer.parseInt(idStr);
                System.out.println("Controller: Deleting news with ID: " + maTinTuc);
                tinTucDAO.deleteTinTuc(maTinTuc);
                request.setAttribute("message", "Xóa tin tức thành công!");
                request.setAttribute("messageType", "success");
            } else {
                throw new IllegalArgumentException("Hành động không hợp lệ!");
            }
        } catch (Exception e) {
            System.err.println("Controller: Error in doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Thao tác thất bại: " + e.getMessage());
            request.setAttribute("messageType", "danger");
        }

        doGet(request, response); // Tải lại danh sách
    }
}