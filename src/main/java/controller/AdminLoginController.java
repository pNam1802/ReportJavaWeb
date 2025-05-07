package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

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
            response.sendRedirect("views/AdminDashboard.jsp");
        } else {
            // Nếu đăng nhập sai, lưu thông báo lỗi và chuyển hướng lại trang đăng nhập
            request.setAttribute("errorMessage", "Đăng nhập thất bại! Sai tên hoặc mật khẩu.");
            request.getRequestDispatcher("views/AdminLogin.jsp").forward(request, response);
        }
    }
}
