<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chọn Vai Trò - Bàn Ghế Sofa</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow mx-auto" style="max-width: 500px;">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0">Chọn Vai Trò</h3>
        </div>
        <div class="card-body">
            <form action="vai-tro" method="post">
                <div class="d-grid gap-2">
                    <input type="submit" name="role" value="User" class="btn btn-outline-primary">
                    <input type="submit" name="role" value="Admin" class="btn btn-outline-success">
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>