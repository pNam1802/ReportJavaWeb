package controller;

import dao.DonHangDAO;
import dao.SanPhamDAO;
import model.DonHang;
import model.DonHangWithUser;
import model.SanPham;
import model.SanPhamInDonHang;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/don-hang")
public class DonHangController extends HttpServlet {

    private DonHangDAO donHangDAO;

    @Override
    public void init() throws ServletException {
        donHangDAO = new DonHangDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "chitiet":
                    showDonHangDetail(request, response);
                    break;
                default: // mặc định là hiển thị danh sách
                    showDonHangs(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi xử lý yêu cầu");
        }
    }




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "updateInfo":
                    updateDonHangInfo(request, response);
                    break;
                case "updateStatus":
                    updateDonHangStatus(request, response);
                    break;
                case "paymentStatus":
                    updatePaymentStatus(request, response);
                    break;
                case "huy":
                    huyDonHang(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không được hỗ trợ");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi xử lý yêu cầu");
        }
    }
    private void huyDonHang(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int maDonHang = Integer.parseInt(request.getParameter("maDonHang"));
            DonHangDAO dao = new DonHangDAO(); // nếu bạn dùng constructor, hoặc gọi từ singleton
            dao.huyDonHang(maDonHang);
            response.sendRedirect("don-hang"); // quay lại trang danh sách
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đơn hàng không hợp lệ");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi hủy đơn hàng");
        }
    }

    private void showDonHangs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String trangThai = request.getParameter("trangThai");
        List<DonHang> donHangs;

        // Kiểm tra nếu trangThai là null hoặc rỗng thì gọi getAllDonHang
        if (trangThai == null || trangThai.trim().isEmpty()) {
            donHangs = donHangDAO.getAllDonHang();
        } else if ("Đã thanh toán".equalsIgnoreCase(trangThai) || "Chưa thanh toán".equalsIgnoreCase(trangThai)) {
            // Lọc theo trạng thái thanh toán
            donHangs = donHangDAO.getDonHangsByTrangThaiThanhToan(trangThai);
        } else {
            // Bao gồm luôn cả trường hợp "Tất cả" đã xử lý trong DAO
            donHangs = donHangDAO.getDonHangsByTrangThai(trangThai);
        }

        request.setAttribute("donHangs", donHangs);
        request.setAttribute("trangThaiDaChon", trangThai);

        // Trả về trang views/QuanLyDonHang.jsp (sử dụng Bootstrap)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/QuanLyDonHang.jsp");
        dispatcher.forward(request, response);
    }

    private void showDonHangDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int maDonHang = Integer.parseInt(request.getParameter("maDonHang"));

            // Lấy danh sách sản phẩm và thông tin đơn hàng
            List<SanPhamInDonHang> sanPhamList = donHangDAO.getSanPhamInDonHang(maDonHang);
            List<DonHangWithUser> donHangWithUsers = donHangDAO.getDonHangWithUser(maDonHang);

            // Log để kiểm tra
            System.out.println("maDonHang = " + maDonHang);
            System.out.println("sanPhamList = " + (sanPhamList != null ? sanPhamList.size() : "null"));
            System.out.println("donHangWithUsers = " + (donHangWithUsers != null ? donHangWithUsers.size() : "null"));

            if (donHangWithUsers == null || donHangWithUsers.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng.");
                return;
            }

            // Gắn danh sách sản phẩm nếu có
            for (DonHangWithUser donHang : donHangWithUsers) {
                donHang.setSanPhamList(sanPhamList); // Có thể null, JSP xử lý điều kiện
            }

            // Chuẩn bị dữ liệu và chuyển tiếp
            SanPhamDAO sanPhamDAO = new SanPhamDAO();
            List<SanPham> danhSachSanPham = sanPhamDAO.getAll();

            request.setAttribute("danhSachSanPham", danhSachSanPham);
            request.setAttribute("donHangWithUsers", donHangWithUsers);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/ChiTietDonHang.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đơn hàng không hợp lệ");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi truy vấn cơ sở dữ liệu");
        }
    }


    private void updateDonHangStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {

        int maDonHang = Integer.parseInt(request.getParameter("maDonHang"));
        String trangThaiMoi = request.getParameter("trangThaiMoi");

        donHangDAO.updateTrangThaiDonHang(maDonHang, trangThaiMoi);
        response.sendRedirect(request.getContextPath() + "/don-hang?action=list");
    }

    private void updatePaymentStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {

        int maDonHang = Integer.parseInt(request.getParameter("maDonHang"));
        String thanhToanTrangThai = request.getParameter("thanhToanTrangThai");

        donHangDAO.updatePaymentStatus(maDonHang, thanhToanTrangThai);
        response.sendRedirect(request.getContextPath() + "/don-hang?action=list");
    }

    private void updateDonHangInfo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            // Lấy dữ liệu từ form
            int maDonHang = Integer.parseInt(request.getParameter("maDonHang"));
            String trangThai = request.getParameter("trangThai");
            String trangThaiThanhToan = request.getParameter("trangThaiThanhToan");
            String diaChi = request.getParameter("diaChi");

            // Kiểm tra dữ liệu đầu vào
            if (trangThai == null || trangThai.trim().isEmpty() ||
                trangThaiThanhToan == null || trangThaiThanhToan.trim().isEmpty() ||
                diaChi == null || diaChi.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thông tin không hợp lệ");
                return;
            }

            // Danh sách trạng thái hợp lệ
            List<String> validTrangThai = Arrays.asList("Chờ xử lý", "Đang giao", "Hoàn thành", "Hủy");
            List<String> validTrangThaiThanhToan = Arrays.asList("Chưa thanh toán", "Đã thanh toán");

            if (!validTrangThai.contains(trangThai) || !validTrangThaiThanhToan.contains(trangThaiThanhToan)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trạng thái không hợp lệ");
                return;
            }

            // Cập nhật thông tin trong cơ sở dữ liệu
            donHangDAO.updateTrangThaiDonHang(maDonHang, trangThai);
            donHangDAO.updatePaymentStatus(maDonHang, trangThaiThanhToan);
            donHangDAO.updateDiaChiNguoiDungTheoMaDonHang(maDonHang, diaChi);

            // Chuyển hướng về trang chi tiết đơn hàng với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/don-hang?action=chitiet&maDonHang=" + maDonHang + "&success=updated");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đơn hàng không hợp lệ");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi cập nhật thông tin đơn hàng");
        }
    }
    

    @Override
    public void destroy() {
        donHangDAO = null;
    }
    



}