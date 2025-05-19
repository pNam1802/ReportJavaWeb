package controller;

import dao.ChiTietDonHangDAO;
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
    private ChiTietDonHangDAO chiTietDonHangDAO;

    @Override
    public void init() throws ServletException {
        donHangDAO = new DonHangDAO();
        chiTietDonHangDAO = new ChiTietDonHangDAO();
    }
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
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
                case "removeProduct":
                    removeProductFromOrder(request, response);
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
        // Thiết lập mã hóa
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Lấy tham số trạng thái và trang
        String trangThai = request.getParameter("trangThai");
        int currentPage = 1;
        int itemsPerPage = 10; // Số đơn hàng mỗi trang

        // Lấy trang hiện tại từ tham số
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1; // Mặc định là trang 1 nếu tham số không hợp lệ
            }
        }

        // Tính toán OFFSET
        int offset = (currentPage - 1) * itemsPerPage;

        // Lấy danh sách đơn hàng và tổng số đơn hàng
        List<DonHang> donHangs;
        int totalItems;
        if (trangThai == null || trangThai.trim().isEmpty()) {
            donHangs = donHangDAO.getAllDonHang(offset, itemsPerPage);
            totalItems = donHangDAO.getTotalDonHangs();
        } else {
            donHangs = donHangDAO.getDonHangsByTrangThai(trangThai, offset, itemsPerPage);
            totalItems = donHangDAO.getTotalDonHangsByTrangThai(trangThai);
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // Đặt các thuộc tính để sử dụng trong JSP
        request.setAttribute("donHangs", donHangs);
        request.setAttribute("trangThaiDaChon", trangThai);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        // Chuyển hướng đến trang JSP
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
            System.out.print("trang thai duoc lay ra: " + trangThai);
            System.out.print("trang thai thanh toan: " + trangThaiThanhToan);
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
            response.sendRedirect(request.getContextPath() + "/don-hang?action=chitiet&maDonHang=" + maDonHang + "&updateStatus=success");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đơn hàng không hợp lệ");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi cập nhật thông tin đơn hàng");
        }
    }
    private void removeProductFromOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int maDonHang = Integer.parseInt(request.getParameter("maDonHang"));
            int maSanPham = Integer.parseInt(request.getParameter("maSanPham"));

            chiTietDonHangDAO.xoaSanPhamKhoiDonHang(maDonHang, maSanPham);

            // Sau khi xóa sản phẩm, chuyển hướng lại trang chi tiết đơn hàng
            response.sendRedirect("don-hang?action=chitiet&maDonHang=" + maDonHang + "&removeStatus=success");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xóa sản phẩm khỏi đơn hàng");
        }
    }


    @Override
    public void destroy() {
        donHangDAO = null;
    }
    



}