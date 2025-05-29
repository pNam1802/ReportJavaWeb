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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
        :root {
            --primary-color: #f97316; /* Orange */
            --secondary-color: #1e40af; /* Dark Blue */
            --card-bg: rgba(255, 255, 255, 0.9); /* Giữ hiệu ứng trong suốt */
            --text-color: #1f2937; /* Dark Gray */
        }

        body {
            background-image: url('${pageContext.request.contextPath}/images/banan1.png');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            color: var(--text-color);
            font-family: 'Inter', sans-serif;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .container-overlay {
            background-color: rgba(0, 0, 0, 0.3);
            padding: 2rem;
            border-radius: 1rem;
        }

        .card {
            background-color: var(--card-bg);
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
    <div class="container mx-auto px-4 flex flex-col items-center container-overlay">
        <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo iSofa" class="h-20 mb-4">
        <div class="card rounded-lg shadow-lg p-6 max-w-lg mx-auto"> 
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
                <div class="mb-6 relative">
                    <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
                        <i class="fas fa-lock mr-2"></i>Mật khẩu
                    </label>
                    <input type="password" class="form-control w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none" id="password" name="password" required>
                    <button type="button" onclick="togglePassword()" class="absolute right-3 top-1/2 transform translate-y-1/2 text-gray-600 hover:text-gray-800">
                        <i class="fas fa-eye"></i>
                    </button>
                </div>
                <button type="submit" class="btn-primary w-full py-2 rounded-md text-white font-semibold">Đăng nhập</button>
                <p class="text-center text-sm text-gray-600 mt-4">
                    Để quay lại iSofa, kích vào 
                    <a href="${pageContext.request.contextPath}/san-pham" class="text-orange-500 font-medium hover:underline">Trang chủ</a>
                </p>
            </form>
        </div>
    </div>
    <script>
        function togglePassword() {
            try {
                const passwordField = document.getElementById('password');
                const icon = document.querySelector('.fa-eye') || document.querySelector('.fa-eye-slash');
                if (!passwordField || !icon) {
                    console.error('Không tìm thấy trường mật khẩu hoặc biểu tượng mắt.');
                    return;
                }
                if (passwordField.type === 'password') {
                    passwordField.type = 'text';
                    icon.classList.remove('fa-eye');
                    icon.classList.add('fa-eye-slash');
                } else {
                    passwordField.type = 'password';
                    icon.classList.remove('fa-eye-slash');
                    icon.classList.add('fa-eye');
                }
            } catch (error) {
                console.error('Lỗi trong hàm togglePassword:', error);
            }
        }
    </script>
</body>
</html>