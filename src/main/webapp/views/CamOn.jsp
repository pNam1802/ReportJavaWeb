<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.text.NumberFormat, java.util.Locale" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt hàng thành công</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #f25a29; /* Màu chủ đạo */
            --secondary-color: #3d3e42; /* Màu phụ */
        }
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .thank-you-container {
            margin-top: 80px;
            background-color: #ffffff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .thank-you-title {
            color: var(--primary-color);
            font-size: 36px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .thank-you-message {
            font-size: 18px;
            color: #333;
            margin-bottom: 10px;
        }
        .btn-home {
            background-color: var(--primary-color);
            border: none;
            font-weight: bold;
        }
        .btn-home:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="thank-you-container mx-auto col-md-8 col-lg-6">
        <%
            String hoTen = request.getParameter("fullName");
        Object tongTienObj = request.getAttribute("tongTien");
        String email = request.getParameter("email");

        double tongTien = 0;
        if (tongTienObj != null && tongTienObj instanceof Double) {
            tongTien = (Double) tongTienObj;
        }

            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            String tongTienFormatted = nf.format(tongTien);
        %>

        <h1 class="thank-you-title">Cảm ơn quý khách!</h1>
        <p class="thank-you-message">
            Cảm ơn <strong><%= (hoTen != null && !hoTen.isEmpty()) ? hoTen : "bạn" %></strong> đã đặt hàng.
        </p>
        <p class="thank-you-message">
            Tổng số tiền: <strong class="text-primary"><%= tongTienFormatted %> đ</strong>
        </p>
        <p class="thank-you-message">
            Chúng tôi sẽ liên hệ với bạn qua email <strong><%= (email != null && !email.isEmpty()) ? email : "(chưa cung cấp email)" %></strong> trong thời gian sớm nhất.
        </p>

        <a href="<%=request.getContextPath() %>/san-pham" class="btn btn-home text-white mt-4 px-4 py-2">Quay về trang chủ</a>
    </div>
</div>

<!-- Bootstrap JS (tùy chọn) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
