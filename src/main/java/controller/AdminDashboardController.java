package controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import dao.RevenueDataDAO;
import model.InventoryData;
import model.OrderStatusData;
import model.RatingData;
import model.RevenueData;
import model.TopProductData;

@WebServlet("/admin-dashboard")
public class AdminDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        List<RevenueData.Revenue> revenueData = RevenueDataDAO.getRevenueData();
        List<TopProductData.Product> topProducts = RevenueDataDAO.getTopProducts();
        List<OrderStatusData.Status> orderStatusData = RevenueDataDAO.getOrderStatusData();
        List<RatingData.Rating> ratingData = RevenueDataDAO.getRatingData();
        List<InventoryData.Inventory> inventoryData = RevenueDataDAO.getInventoryData();

        request.setAttribute("revenueData", revenueData);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("orderStatusData", orderStatusData);
        request.setAttribute("ratingData", ratingData);
        request.setAttribute("inventoryData", inventoryData);

        String export = request.getParameter("export");
        if ("pdf".equals(export)) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=bao_cao_thong_ke.pdf");

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font font = new Font(baseFont, 12, Font.NORMAL);
                DecimalFormat df = new DecimalFormat("#,###");

                document.add(new Paragraph("BÁO CÁO THỐNG KÊ iSOFA\n", font));

                document.add(new Paragraph("\n1. Doanh thu theo tháng:", font));
                for (RevenueData.Revenue revenue : revenueData) {
                    document.add(new Paragraph(revenue.getMonth() + ": " + df.format(revenue.getAmount()) + " VND", font));
                }

                document.add(new Paragraph("\n2. Top 5 sản phẩm bán chạy:", font));
                for (TopProductData.Product product : topProducts) {
                    document.add(new Paragraph(product.getName() + ": " + product.getQuantity() + " sản phẩm", font));
                }

                document.add(new Paragraph("\n3. Tỷ lệ trạng thái đơn hàng:", font));
                for (OrderStatusData.Status status : orderStatusData) {
                    document.add(new Paragraph(status.getStatus() + ": " + status.getPercentage() + "%", font));
                }

                document.add(new Paragraph("\n4. Phân bố điểm đánh giá:", font));
                for (RatingData.Rating rating : ratingData) {
                    document.add(new Paragraph(rating.getRating() + ": " + rating.getCount() + " lượt", font));
                }

                document.add(new Paragraph("\n5. Tồn kho theo danh mục:", font));
                for (InventoryData.Inventory inventory : inventoryData) {
                    document.add(new Paragraph(inventory.getCategory() + ": " + inventory.getStock() + " sản phẩm", font));
                }

                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        request.getRequestDispatcher("/views/AdminDashboard.jsp").forward(request, response);
    }
}
