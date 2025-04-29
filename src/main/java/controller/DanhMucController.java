package controller;

import dao.DanhMucDAO;
import model.DanhMuc;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DanhMucController extends HttpServlet {
    private DanhMucDAO danhMucDAO;

    public void init() {
        danhMucDAO = new DanhMucDAO(); // Khởi tạo DAO
    }

    // Hiển thị danh sách DanhMuc
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DanhMuc> danhMucs = danhMucDAO.getAll(); // Lấy danh sách tất cả danh mục từ DB
        request.setAttribute("danhMucs", danhMucs);  // Đặt danh sách vào request
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/list.jsp"); // Chuyển đến trang list.jsp
        dispatcher.forward(request, response);  // Chuyển đến JSP để hiển thị
    }

    // Xử lý thêm DanhMuc
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tenDanhMuc = request.getParameter("tenDanhMuc");
        String moTa = request.getParameter("moTa");

        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setTenDanhMuc(tenDanhMuc);
        danhMuc.setMoTa(moTa);

        if (danhMucDAO.add(danhMuc)) {  // Nếu thêm thành công
            response.sendRedirect("danhmuc");  // Chuyển hướng lại trang danh sách
        } else {
            request.setAttribute("error", "Thêm danh mục không thành công.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/add.jsp"); // Nếu thất bại, quay lại trang thêm
            dispatcher.forward(request, response);
        }
    }
}
