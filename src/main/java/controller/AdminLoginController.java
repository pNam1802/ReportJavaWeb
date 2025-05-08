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
        // Chuyển hướng đến trang login JSP khi truy cập GET
        request.getRequestDispatcher("views/AdminLogin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        // Kiểm tra thông tin đăng nhập admin
        if ("admin".equals(user) && "admin123".equals(pass)) {
            // Thiết lập session cho admin
            HttpSession session = request.getSession();
            session.setAttribute("admin", user);
            response.sendRedirect(request.getContextPath() + "/views/AdminDashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "Đăng nhập thất bại! Sai tên hoặc mật khẩu.");
            request.getRequestDispatcher("views/AdminLogin.jsp").forward(request, response);
        }
    }
}
