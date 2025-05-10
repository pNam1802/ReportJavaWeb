package controller;

import dao.SanPhamDAO;
import interfaces.ISanPham;
import model.SanPham;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/loc-danh-muc")
public class LocDanhMucController extends HttpServlet {
	private ISanPham sanPhamDAO;
	
	public void init() {
        sanPhamDAO = new SanPhamDAO();
    }
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	int maDanhMuc = Integer.parseInt(request.getParameter("maDanhMuc"));

        List<SanPham> dsSanPham = sanPhamDAO.getSanPhamTheoDanhMuc(maDanhMuc, 0, Integer.MAX_VALUE);

        request.setAttribute("sanPhams", dsSanPham);
        request.getRequestDispatcher("views/TrangChu.jsp").forward(request, response);
    }
}
