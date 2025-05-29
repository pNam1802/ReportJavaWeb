package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DatHangDAO;
import model.ChiTietDonHang;
import model.DonHang;


@WebServlet("/dat-hang")
public class DatHangController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DatHangController.class.getName());
    private static final String ERROR_PAGE = "views/ErrorPage.jsp";
    private static final String THANK_YOU_PAGE = "views/CamOn.jsp";


    private static class Action {
        static final String DAT_HANG = "datHang";
        static final String GIO_HANG_THANH_TOAN = "GioHangThanhToan";
    }


    private static class TrangThai {
        static final String CHO_XU_LY = "Chờ xử lý";
    }

    private DatHangDAO datHangDAO;

    @Override
    public void init() throws ServletException {
        datHangDAO = new DatHangDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        try {
            if (Action.DAT_HANG.equals(action)) {
                processSingleProductOrder(request, response);
            } else if (Action.GIO_HANG_THANH_TOAN.equals(action)) {
                processCartOrder(request, response);
            } else {
                throw new ServletException("Hành động không được hỗ trợ: " + action);
            }
        } catch (ServletException | IOException e) {
            LOGGER.severe("Lỗi xử lý yêu cầu: " + e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            forwardToErrorPage(request, response);
        }
    }


    private void processSingleProductOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String note = request.getParameter("note");
        String maSanPhamStr = request.getParameter("maSanPham");
        String tenSanPham = request.getParameter("tenSanPham");
        String hinhAnh = request.getParameter("hinhAnh");
        String soLuongStr = request.getParameter("soLuong");
        String tongTienStr = request.getParameter("tongTien");

        // Kiểm tra dữ liệu đầu vào
        if (isEmpty(fullName, phone, email, address, maSanPhamStr, soLuongStr, tongTienStr)) {
            throw new ServletException("Vui lòng điền đầy đủ thông tin đặt hàng.");
        }

        // Kiểm tra định dạng email và số điện thoại
        if (!isValidEmail(email)) {
            throw new ServletException("Email không hợp lệ.");
        }
        if (!isValidPhone(phone)) {
            throw new ServletException("Số điện thoại không hợp lệ.");
        }

        try {
            // Chuyển đổi và kiểm tra dữ liệu số
            int maSanPham = Integer.parseInt(maSanPhamStr);
            int soLuong = Integer.parseInt(soLuongStr);
            double tongTien = parseDoubleSafe(tongTienStr, -1);

            if (soLuong <= 0) {
                throw new ServletException("Số lượng sản phẩm phải lớn hơn 0.");
            }
            if (tongTien <= 0) {
                throw new ServletException("Tổng tiền không hợp lệ.");
            }

            // Thêm hoặc lấy người dùng
            int maNguoiDung = datHangDAO.themHoacLayNguoiDung(fullName, phone, email, address);
            if (maNguoiDung <= 0) {
                throw new ServletException("Không thể tạo hoặc lấy thông tin người dùng.");
            }

            // Tạo đơn hàng
            DonHang donHang = new DonHang();
            donHang.setNgayLap(new Date());
            donHang.setTrangThai(TrangThai.CHO_XU_LY); // Thanh toán khi giao hàng (COD)
            donHang.setTongTien(tongTien);
            donHang.setMaNguoiDung(maNguoiDung);

            // Tạo chi tiết đơn hàng
            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setMaSanPham(maSanPham);
            chiTiet.setSoLuong(soLuong);
            chiTiet.setDonGia(tongTien / soLuong);

            List<ChiTietDonHang> dsChiTiet = Collections.singletonList(chiTiet);

            // Đặt hàng
            boolean success = datHangDAO.placeOrder(donHang, dsChiTiet);
            if (!success) {
                throw new ServletException("Không thể đặt hàng. Vui lòng thử lại.");
            }

            // Truyền dữ liệu sang trang cảm ơn
            request.setAttribute("fullName", fullName);
            request.setAttribute("phone", phone);
            request.setAttribute("email", email);
            request.setAttribute("address", address);
            request.setAttribute("note", note);
            request.setAttribute("tenSanPham", tenSanPham);
            request.setAttribute("hinhAnh", hinhAnh);
            request.setAttribute("soLuong", soLuong);
            request.setAttribute("tongTien", tongTien);

            request.getRequestDispatcher(THANK_YOU_PAGE).forward(request, response);

        } catch (SQLException e) {
            LOGGER.severe("Lỗi SQL khi xử lý đặt hàng đơn lẻ: " + e.getMessage());
            throw new ServletException("Lỗi cơ sở dữ liệu: " + e.getMessage());
        } catch (NumberFormatException e) {
            LOGGER.severe("Lỗi định dạng số: " + e.getMessage());
            throw new ServletException("Dữ liệu số không hợp lệ: " + e.getMessage());
        }
    }

    private void processCartOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin từ form
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String note = request.getParameter("note");

        // Kiểm tra thông tin bắt buộc
        if (isEmpty(fullName, phone, email, address)) {
            throw new ServletException("Vui lòng điền đầy đủ thông tin đặt hàng.");
        }

        // Kiểm tra định dạng email và số điện thoại
        if (!isValidEmail(email)) {
            throw new ServletException("Email không hợp lệ.");
        }
        if (!isValidPhone(phone)) {
            throw new ServletException("Số điện thoại không hợp lệ.");
        }

        // Lấy danh sách sản phẩm từ form
        String[] maSanPhamStr = request.getParameterValues("maSanPham[]");
        String[] tenSanPham = request.getParameterValues("tenSanPham[]");
        String[] soLuongStr = request.getParameterValues("soLuong[]");
        String[] donGiaStr = request.getParameterValues("donGia[]");

        if (maSanPhamStr == null || tenSanPham == null || soLuongStr == null || donGiaStr == null
                || maSanPhamStr.length == 0) {
            throw new ServletException("Giỏ hàng rỗng hoặc dữ liệu không hợp lệ.");
        }

        try {
            int maNguoiDung = datHangDAO.themHoacLayNguoiDung(fullName, phone, email, address);
            if (maNguoiDung <= 0) {
                throw new ServletException("Không thể tạo hoặc lấy thông tin người dùng.");
            }

            List<ChiTietDonHang> dsChiTiet = new ArrayList<>();
            double tongTien = 0;

            for (int i = 0; i < maSanPhamStr.length; i++) {
                int maSanPham = Integer.parseInt(maSanPhamStr[i]);
                int soLuong = Integer.parseInt(soLuongStr[i]);
                double donGia = parseDoubleSafe(donGiaStr[i], -1);

                if (soLuong <= 0) {
                    throw new ServletException("Số lượng không hợp lệ cho sản phẩm: " + tenSanPham[i]);
                }
                if (donGia <= 0) {
                    throw new ServletException("Đơn giá không hợp lệ cho sản phẩm: " + tenSanPham[i]);
                }

                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setMaSanPham(maSanPham);
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(donGia);

                dsChiTiet.add(chiTiet);
                tongTien += donGia * soLuong;
            }

            // Tạo đơn hàng
            DonHang donHang = new DonHang();
            donHang.setNgayLap(new Date());
            donHang.setTrangThai(TrangThai.CHO_XU_LY);
            donHang.setTongTien(tongTien);
            donHang.setMaNguoiDung(maNguoiDung);

            // Gửi đơn hàng
            boolean datHangThanhCong = datHangDAO.placeOrder(donHang, dsChiTiet);
            if (!datHangThanhCong) {
                throw new ServletException("Không thể đặt hàng. Vui lòng thử lại.");
            }
            HttpSession session = request.getSession();
            session.removeAttribute("gioHang"); // hoặc tên giỏ hàng bạn đang dùng

            // Chuyển đến trang cảm ơn
            request.setAttribute("fullName", fullName);
            request.setAttribute("phone", phone);
            request.setAttribute("email", email);
            request.setAttribute("address", address);
            request.setAttribute("note", note);
            request.setAttribute("tongTien", tongTien);

            request.getRequestDispatcher(THANK_YOU_PAGE).forward(request, response);

        } catch (SQLException e) {
            LOGGER.severe("Lỗi SQL khi xử lý đơn hàng: " + e.getMessage());
            throw new ServletException("Lỗi khi truy cập cơ sở dữ liệu.");
        } catch (NumberFormatException e) {
            LOGGER.severe("Lỗi định dạng số: " + e.getMessage());
            throw new ServletException("Dữ liệu số không hợp lệ.");
        }
    }

// kiểm tra các thông tin chuẩn dạng hay chưa
    private boolean isEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\+?[0-9]{10,13}$");
    }

    private double parseDoubleSafe(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
    }
}
