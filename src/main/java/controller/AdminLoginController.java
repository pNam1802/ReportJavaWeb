package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        if ("admin".equals(user) && "admin123".equals(pass)) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", user);
            System.out.println("Session after login - admin: " + session.getAttribute("admin"));
            response.sendRedirect(request.getContextPath() + "/views/AdminDashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "Đăng nhập thất bại! Sai tên hoặc mật khẩu.");
            request.getRequestDispatcher("views/AdminLogin.jsp").forward(request, response);
        }
    }
}