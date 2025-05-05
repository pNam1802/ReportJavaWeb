package controller;

import dao.SanPhamDAO;
import model.SanPham;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/loc-danh-muc")
public class LocDanhMucController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	int maDanhMuc = Integer.parseInt(request.getParameter("maDanhMuc"));

        SanPhamDAO spDAO = new SanPhamDAO();
        List<SanPham> dsSanPham = spDAO.getSanPhamTheoDanhMuc(maDanhMuc, 0, Integer.MAX_VALUE);

        request.setAttribute("sanPhams", dsSanPham);
        request.getRequestDispatcher("views/TrangChu.jsp").forward(request, response);
    }
}
