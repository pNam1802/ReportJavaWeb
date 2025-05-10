package controller;

import interfaces.IQuanLyTinTuc;
import dao.QuanLyTinTucDAO;
import model.TinTuc;
import model.SanPham;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/QuanLyTinTuc")
public class QuanLyTinTucController extends HttpServlet {
    private IQuanLyTinTuc tinTucDAO;

    @Override
    public void init() {
        tinTucDAO = new QuanLyTinTucDAO();
        System.out.println("Controller: Initialized QuanLyTinTucDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    showAddForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteTinTuc(req, resp);
                    break;
                case "list":
                default:
                    listTinTuc(req, resp);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error processing request: " + e.getMessage());
            try {
                req.getRequestDispatcher("/views/ErrorPage.jsp").forward(req, resp);
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error page not found: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    addTinTuc(req, resp);
                    break;
                case "edit":
                    updateTinTuc(req, resp);
                    break;
                default:
                    listTinTuc(req, resp);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error processing request: " + e.getMessage());
            try {
                req.getRequestDispatcher("/views/ErrorPage.jsp").forward(req, resp);
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error page not found: " + ex.getMessage());
            }
        }
    }

    private void listTinTuc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Controller: Entering listTinTuc");
        int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        int pageSize = 10;
        List<TinTuc> tinTucs = tinTucDAO.getTinTucByPage(page, pageSize);
        int totalTinTuc = tinTucDAO.getTotalTinTuc();
        int totalPages = (int) Math.ceil((double) totalTinTuc / pageSize);
        // Load products for display in table
        List<SanPham> products = tinTucDAO.getAllProducts();
        System.out.println("Controller: Loaded " + (products != null ? products.size() : "null") + " products in listTinTuc");

        req.setAttribute("tinTucs", tinTucs);
        req.setAttribute("products", products); // Add products to request
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/views/TinTucHandle.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Controller: Entering showAddForm");
        List<SanPham> products = tinTucDAO.getAllProducts();
        System.out.println("Controller: Loaded " + (products != null ? products.size() : "null") + " products in showAddForm");
        req.setAttribute("products", products);
        req.setAttribute("action", "add");
        req.getRequestDispatcher("/views/TinTucHandle.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Controller: Entering showEditForm");
        int maTinTuc = Integer.parseInt(req.getParameter("maTinTuc"));
        TinTuc tinTuc = tinTucDAO.getTinTucById(maTinTuc);
        List<SanPham> products = tinTucDAO.getAllProducts();
        System.out.println("Controller: Loaded " + (products != null ? products.size() : "null") + " products in showEditForm");
        req.setAttribute("tinTuc", tinTuc);
        req.setAttribute("products", products);
        req.setAttribute("action", "edit");
        req.getRequestDispatcher("/views/TinTucHandle.jsp").forward(req, resp);
    }

    private void addTinTuc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {
        System.out.println("Controller: Entering addTinTuc");
        String tieuDe = req.getParameter("tieuDe");
        String noiDung = req.getParameter("noiDung");
        String ngayDangStr = req.getParameter("ngayDang");
        int maSanPham = Integer.parseInt(req.getParameter("maSanPham"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date ngayDang = dateFormat.parse(ngayDangStr);

        TinTuc news = new TinTuc();
        news.setTieuDe(tieuDe);
        news.setNoiDung(noiDung);
        news.setNgayDang(ngayDang);
        news.setMaSanPham(maSanPham);

        tinTucDAO.addTinTuc(news);
        resp.sendRedirect(req.getContextPath() + "/QuanLyTinTuc");
    }

    private void updateTinTuc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {
        System.out.println("Controller: Entering updateTinTuc");
        int maTinTuc = Integer.parseInt(req.getParameter("maTinTuc"));
        String tieuDe = req.getParameter("tieuDe");
        String noiDung = req.getParameter("noiDung");
        String ngayDangStr = req.getParameter("ngayDang");
        int maSanPham = Integer.parseInt(req.getParameter("maSanPham"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date ngayDang = dateFormat.parse(ngayDangStr);

        TinTuc news = new TinTuc();
        news.setMaTinTuc(maTinTuc);
        news.setTieuDe(tieuDe);
        news.setNoiDung(noiDung);
        news.setNgayDang(ngayDang);
        news.setMaSanPham(maSanPham);

        tinTucDAO.updateTinTuc(news);
        resp.sendRedirect(req.getContextPath() + "/QuanLyTinTuc");
    }

    private void deleteTinTuc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Controller: Entering deleteTinTuc");
        int maTinTuc = Integer.parseInt(req.getParameter("maTinTuc"));
        tinTucDAO.deleteTinTuc(maTinTuc);
        resp.sendRedirect(req.getContextPath() + "/QuanLyTinTuc");
    }
}