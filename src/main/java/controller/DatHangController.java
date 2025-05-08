package controller;

import dao.DatHangDAO;
import model.ChiTietDonHang;
import model.DonHang;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

@WebServlet("/dat-hang")
public class DatHangController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DatHangController.class.getName());
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

        if ("datHang".equals(action)) {
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
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin đặt hàng.");
                forwardToErrorPage(request, response);
                return;
            }

            // Kiểm tra định dạng
            if (!isValidEmail(email) || !isValidPhone(phone)) {
                request.setAttribute("errorMessage", "Email hoặc số điện thoại không hợp lệ.");
                forwardToErrorPage(request, response);
                return;
            }

            try {
                int maSanPham = Integer.parseInt(maSanPhamStr);
                int soLuong = Integer.parseInt(soLuongStr);
                double tongTien = parseDoubleSafe(tongTienStr, -1);

                if (soLuong <= 0 || tongTien <= 0) {
                    request.setAttribute("errorMessage", "Số lượng hoặc tổng tiền không hợp lệ.");
                    forwardToErrorPage(request, response);
                    return;
                }

                // Thêm hoặc lấy người dùng
                int maNguoiDung = datHangDAO.themHoacLayNguoiDung(fullName, phone, email, address);
                if (maNguoiDung == -1) {
                    request.setAttribute("errorMessage", "Không thể tạo hoặc lấy thông tin người dùng.");
                    forwardToErrorPage(request, response);
                    return;
                }

                // Tạo đơn hàng
                DonHang donHang = new DonHang();
                donHang.setNgayLap(new Date());
                donHang.setTrangThai("Chờ thanh toán");
                donHang.setTongTien(tongTien);
                donHang.setMaNguoiDung(maNguoiDung);

                // Chi tiết đơn hàng
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setMaSanPham(maSanPham);
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(tongTien / soLuong); // Tính đơn giá chính xác

                List<ChiTietDonHang> dsChiTiet = Collections.singletonList(chiTiet);

                // Đặt hàng
                boolean success = datHangDAO.placeOrder(donHang, dsChiTiet);
                if (!success) {
                    request.setAttribute("errorMessage", "Không thể đặt hàng. Vui lòng thử lại.");
                    forwardToErrorPage(request, response);
                    return;
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

                request.getRequestDispatcher("views/CamOn.jsp").forward(request, response);

            } catch (NumberFormatException | SQLException e) {
                logger.severe("Lỗi xử lý đặt hàng: " + e.getMessage());
                request.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình xử lý đặt hàng.");
                forwardToErrorPage(request, response);
            }
        } else if ("GioHangThanhToan".equals(action)) {
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String note = request.getParameter("note");

            // Kiểm tra dữ liệu đầu vào
            if (isEmpty(fullName, phone, email, address)) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin đặt hàng.");
                forwardToErrorPage(request, response);
                return;
            }

            // Kiểm tra định dạng
            if (!isValidEmail(email) || !isValidPhone(phone)) {
                request.setAttribute("errorMessage", "Email hoặc số điện thoại không hợp lệ.");
                forwardToErrorPage(request, response);
                return;
            }

            try {
                // Lấy danh sách sản phẩm từ form
                String[] maSanPhamStr = request.getParameterValues("maSanPham[]");
                String[] tenSanPham = request.getParameterValues("tenSanPham[]");
                String[] soLuongStr = request.getParameterValues("soLuong[]");
                String[] donGiaStr = request.getParameterValues("donGia[]");

                // Kiểm tra mảng rỗng
                if (maSanPhamStr == null || tenSanPham == null || soLuongStr == null || donGiaStr == null ||
                    maSanPhamStr.length == 0) {
                    request.setAttribute("errorMessage", "Giỏ hàng rỗng hoặc dữ liệu không hợp lệ.");
                    forwardToErrorPage(request, response);
                    return;
                }

                // Thêm hoặc lấy người dùng
                int maNguoiDung = datHangDAO.themHoacLayNguoiDung(fullName, phone, email, address);
                if (maNguoiDung == -1) {
                    request.setAttribute("errorMessage", "Không thể tạo hoặc lấy thông tin người dùng.");
                    forwardToErrorPage(request, response);
                    return;
                }

                List<ChiTietDonHang> dsChiTiet = new ArrayList<>();
                double tongTien = 0;
                List<Map<String, Object>> chiTietDonHangList = new ArrayList<>();

                for (int i = 0; i < maSanPhamStr.length; i++) {
                    int maSanPham = Integer.parseInt(maSanPhamStr[i]);
                    int soLuong = Integer.parseInt(soLuongStr[i]);
                    double donGia = parseDoubleSafe(donGiaStr[i], -1);

                    if (soLuong <= 0 || donGia <= 0) {
                        request.setAttribute("errorMessage", "Số lượng hoặc đơn giá không hợp lệ cho sản phẩm: " + tenSanPham[i]);
                        forwardToErrorPage(request, response);
                        return;
                    }

                    double thanhTien = donGia * soLuong;
                    tongTien += thanhTien;

                    ChiTietDonHang chiTiet = new ChiTietDonHang();
                    chiTiet.setMaSanPham(maSanPham);
                    chiTiet.setSoLuong(soLuong);
                    chiTiet.setDonGia(donGia);
                    dsChiTiet.add(chiTiet);

                    // Lưu thông tin chi tiết để truyền sang CamOn.jsp
                    Map<String, Object> chiTietInfo = new HashMap<>();
                    chiTietInfo.put("tenSanPham", tenSanPham[i]);
                    chiTietInfo.put("soLuong", soLuong);
                    chiTietInfo.put("donGia", donGia);
                    chiTietInfo.put("thanhTien", thanhTien);
                    chiTietDonHangList.add(chiTietInfo);
                }

                // Tạo đơn hàng
                DonHang donHang = new DonHang();
                donHang.setNgayLap(new Date());
                donHang.setTrangThai("Chờ thanh toán");
                donHang.setTongTien(tongTien);
                donHang.setMaNguoiDung(maNguoiDung);

                // Đặt hàng
                boolean success = datHangDAO.placeOrder(donHang, dsChiTiet);
                if (!success) {
                    request.setAttribute("errorMessage", "Không thể đặt hàng. Vui lòng thử lại.");
                    forwardToErrorPage(request, response);
                    return;
                }

                // Xóa giỏ hàng
                HttpSession session = request.getSession();
                session.removeAttribute("gioHang");

                // Truyền dữ liệu sang trang cảm ơn
                request.setAttribute("fullName", fullName);
                request.setAttribute("phone", phone);
                request.setAttribute("email", email);
                request.setAttribute("address", address);
                request.setAttribute("note", note);
                request.setAttribute("chiTietDonHangList", chiTietDonHangList);
                request.setAttribute("tongTien", tongTien);

                request.getRequestDispatcher("views/CamOn.jsp").forward(request, response);

            } catch (NumberFormatException | SQLException e) {
                logger.severe("Lỗi xử lý đặt hàng từ giỏ hàng: " + e.getMessage());
                request.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình xử lý đặt hàng.");
                forwardToErrorPage(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Hành động không được hỗ trợ.");
            forwardToErrorPage(request, response);
        }
    }

    private boolean isEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) return true;
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9]{10,11}$");
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("views/ErrorPage.jsp").forward(req, res);
    }

    private double parseDoubleSafe(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            logger.warning("Không thể parse giá trị double: " + value);
            return defaultValue;
        }
    }

    @Override
    public void destroy() {
        datHangDAO = null;
    }
}