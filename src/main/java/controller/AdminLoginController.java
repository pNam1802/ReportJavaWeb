package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.NguoiDungDAO;

@WebServlet("/login-admin")
public class AdminLoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminLoginController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/AdminLogin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        NguoiDungDAO dao = new NguoiDungDAO();
        boolean isValid = dao.validateAdminLogin(user, pass);

        if (isValid) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", user);
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");

        } else {
            request.setAttribute("errorMessage", "Đăng nhập thất bại! Sai tên hoặc mật khẩu.");
            request.getRequestDispatcher("views/AdminLogin.jsp").forward(request, response);
        }
    }

}