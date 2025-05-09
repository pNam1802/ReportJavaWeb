<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập Admin - iSofa</title>

    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        :root {
            --primary-color: #f97316; /* Orange */
            --secondary-color: #1e40af; /* Dark Blue */
            --bg-color: #f3f4f6; /* Light Gray */
            --card-bg: #ffffff; /* White */
            --text-color: #1f2937; /* Dark Gray */
        }

        body {
            background-color: var(--bg-color);
            color: var(--text-color);
            font-family: 'Inter', sans-serif;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .card {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }

        .btn-primary {
            background-color: var(--primary-color);
            color: white;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #ea580c;
        }

        .form-control {
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(249, 115, 22, 0.25);
        }
    </style>
</head>
<body>
    <div class="container mx-auto px-4">
        <div class="card bg-white rounded-lg shadow-lg p-6 max-w-md mx-auto">
            <div class="text-center mb-6">
                <h3 class="text-2xl font-bold text-gray-800">Đăng nhập Admin</h3>
                <p class="text-gray-600">Hệ thống quản trị <span class="text-orange-500 font-semibold">iSofa</span></p>
            </div>
            <form action="${pageContext.request.contextPath}/login-admin" method="post">
                <div class="mb-4">
                    <label for="username" class="block text-sm font-medium text-gray-700 mb-1">
                        <i class="fas fa-user mr-2"></i>Tên đăng nhập
                    </label>
                    <input type="text" class="form-control w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none" id="username" name="username" required>
                </div>
                <div class="mb-6">
                    <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
                        <i class="fas fa-lock mr-2"></i>Mật khẩu
                    </label>
                    <input type="password" class="form-control w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none" id="password" name="password" required>
                </div>
                <button type="submit" class="btn-primary w-full py-2 rounded-md text-white font-semibold">Đăng nhập</button>
            </form>
        </div>
    </div>
</body>
</html>