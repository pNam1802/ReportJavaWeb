drop database ban_ghe_sofa;
CREATE DATABASE IF NOT EXISTS ban_ghe_sofa;
USE ban_ghe_sofa;

CREATE TABLE danh_muc (
    maDanhMuc INT AUTO_INCREMENT PRIMARY KEY,
    tenDanhMuc VARCHAR(255),
    moTa TEXT
);

CREATE TABLE san_pham (
    maSanPham INT AUTO_INCREMENT PRIMARY KEY,
    tenSanPham VARCHAR(255),
    chiTiet TEXT,
    giaGoc DECIMAL(10,2),
    giaKhuyenMai DECIMAL(10,2),
    tinhTrang VARCHAR(50),
    idDanhMuc INT,
    soLuongTonKho INT,
    hinhAnh VARCHAR(255),
    FOREIGN KEY (idDanhMuc) REFERENCES danh_muc(maDanhMuc)
);

CREATE TABLE tin_tuc (
    maTinTuc INT AUTO_INCREMENT PRIMARY KEY,
    tieuDe VARCHAR(255),
    noiDung TEXT,
    ngayDang DATE,
    maSanPham INT,
    FOREIGN KEY (maSanPham) REFERENCES san_pham(maSanPham)
);

CREATE TABLE nguoi_dung (
    maNguoiDung INT AUTO_INCREMENT PRIMARY KEY,
    hoTen VARCHAR(255),
    sdt VARCHAR(20),
    email VARCHAR(100),
    diaChi TEXT,
    tenDangNhap VARCHAR(50),
    matKhau VARCHAR(255),
    vaiTro VARCHAR(50) DEFAULT 'user'
);

CREATE TABLE don_hang (
    maDonHang INT AUTO_INCREMENT PRIMARY KEY,
    ngayLap DATE,
    trangThai VARCHAR(50),
    tongTien DECIMAL(12,2),
    maNguoiDung INT,
    FOREIGN KEY (maNguoiDung) REFERENCES nguoi_dung(maNguoiDung)
);
CREATE TABLE chi_tiet_don_hang (
    maDonHang INT,
    maSanPham INT,
    soLuong INT,
    donGia DECIMAL(10,2),
    PRIMARY KEY (maDonHang, maSanPham),
    FOREIGN KEY (maDonHang) REFERENCES don_hang(maDonHang),
    FOREIGN KEY (maSanPham) REFERENCES san_pham(maSanPham)
);

CREATE TABLE thanh_toan (
    maThanhToan INT AUTO_INCREMENT PRIMARY KEY,
    maDonHang INT,
    hinhThucThanhToan VARCHAR(100),
    trangThai VARCHAR(50),
    ngayThanhToan DATE,
    FOREIGN KEY (maDonHang) REFERENCES don_hang(maDonHang)
);

CREATE TABLE khuyen_mai (
    maKhuyenMai INT AUTO_INCREMENT PRIMARY KEY,
    ngayBatDau DATE,
    ngayKetThuc DATE,
    giaKhuyenMai DECIMAL(10,2),
    maSanPham INT,
    FOREIGN KEY (maSanPham) REFERENCES san_pham(maSanPham)
);

CREATE TABLE danh_gia (
    maDanhGia INT AUTO_INCREMENT PRIMARY KEY,
    maSanPham INT,
    maNguoiDung INT,
    diemDanhGia INT CHECK (diemDanhGia BETWEEN 1 AND 5),
    noiDung TEXT,
    ngayDanhGia DATE,
    FOREIGN KEY (maSanPham) REFERENCES san_pham(maSanPham),
    FOREIGN KEY (maNguoiDung) REFERENCES nguoi_dung(maNguoiDung)
);

-- Tạo Stored Procedure để cập nhật giá khuyến mãi
DELIMITER //
DROP PROCEDURE IF EXISTS UpdatePromotionPrices //
CREATE PROCEDURE UpdatePromotionPrices()
BEGIN
    -- Cập nhật giá khuyến mãi cho các sản phẩm có khuyến mãi hợp lệ
    UPDATE san_pham s
    SET s.giaKhuyenMai = (
        SELECT MIN(km.giaKhuyenMai)
        FROM khuyen_mai km
        WHERE km.maSanPham = s.maSanPham
        AND km.ngayBatDau <= CURDATE()
        AND km.ngayKetThuc >= CURDATE()
    )
    WHERE EXISTS (
        SELECT 1
        FROM khuyen_mai km
        WHERE km.maSanPham = s.maSanPham
        AND km.ngayBatDau <= CURDATE()
        AND km.ngayKetThuc >= CURDATE()
    );

    -- Đặt lại giá khuyến mãi về giá gốc cho các sản phẩm không có khuyến mãi hợp lệ
    UPDATE san_pham s
    SET s.giaKhuyenMai = s.giaGoc
    WHERE NOT EXISTS (
        SELECT 1
        FROM khuyen_mai km
        WHERE km.maSanPham = s.maSanPham
        AND km.ngayBatDau <= CURDATE()
        AND km.ngayKetThuc >= CURDATE()
    );
END //
DELIMITER ;

-- Tạo trigger cập nhật giá khuyến mãi trong bảng sản phẩm từ bảng khuyến mãi
DELIMITER //
DROP TRIGGER IF EXISTS trg_km_after_insert //
CREATE TRIGGER trg_km_after_insert
AFTER INSERT ON khuyen_mai
FOR EACH ROW
BEGIN
    IF NEW.giaKhuyenMai IS NOT NULL 
       AND NEW.ngayBatDau <= CURDATE() 
       AND NEW.ngayKetThuc >= CURDATE() THEN
        UPDATE san_pham
        SET giaKhuyenMai = NEW.giaKhuyenMai
        WHERE maSanPham = NEW.maSanPham;
    END IF;
END //

DROP TRIGGER IF EXISTS trg_km_after_update //
CREATE TRIGGER trg_km_after_update
AFTER UPDATE ON khuyen_mai
FOR EACH ROW
BEGIN
    DECLARE active_promotion_count INT;
    DECLARE min_promotion_price DECIMAL(10,2);

    -- Kiểm tra số lượng khuyến mãi hợp lệ cho sản phẩm
    SELECT COUNT(*) INTO active_promotion_count
    FROM khuyen_mai
    WHERE maSanPham = NEW.maSanPham
    AND ngayBatDau <= CURDATE()
    AND ngayKetThuc >= CURDATE();

    -- Lấy giá khuyến mãi thấp nhất (nếu có)
    SELECT MIN(giaKhuyenMai) INTO min_promotion_price
    FROM khuyen_mai
    WHERE maSanPham = NEW.maSanPham
    AND ngayBatDau <= CURDATE()
    AND ngayKetThuc >= CURDATE();

    -- Nếu có khuyến mãi hợp lệ
    IF active_promotion_count > 0 AND min_promotion_price IS NOT NULL THEN
        UPDATE san_pham
        SET giaKhuyenMai = min_promotion_price
        WHERE maSanPham = NEW.maSanPham;
    -- Nếu không có khuyến mãi hợp lệ
    ELSE
        UPDATE san_pham
        SET giaKhuyenMai = giaGoc
        WHERE maSanPham = NEW.maSanPham;
    END IF;
END //

DROP TRIGGER IF EXISTS trg_km_after_delete //
CREATE TRIGGER trg_km_after_delete
AFTER DELETE ON khuyen_mai
FOR EACH ROW
BEGIN
    DECLARE active_promotion_count INT;
    DECLARE min_promotion_price DECIMAL(10,2);
    
    -- Kiểm tra số lượng khuyến mãi hợp lệ
    SELECT COUNT(*) INTO active_promotion_count
    FROM khuyen_mai
    WHERE maSanPham = OLD.maSanPham
    AND ngayBatDau <= CURDATE()
    AND ngayKetThuc >= CURDATE();
    
    -- Lấy giá khuyến mãi thấp nhất
    SELECT MIN(giaKhuyenMai) INTO min_promotion_price
    FROM khuyen_mai
    WHERE maSanPham = OLD.maSanPham
    AND ngayBatDau <= CURDATE()
    AND ngayKetThuc >= CURDATE();
    
    -- Nếu còn khuyến mãi hợp lệ
    IF active_promotion_count > 0 AND min_promotion_price IS NOT NULL THEN
        UPDATE san_pham
        SET giaKhuyenMai = min_promotion_price
        WHERE maSanPham = OLD.maSanPham;
    -- Nếu không còn khuyến mãi hợp lệ
    ELSE
        UPDATE san_pham
        SET giaKhuyenMai = giaGoc
        WHERE maSanPham = OLD.maSanPham;
    END IF;
END //
DELIMITER ;

-- Tạo Event Scheduler để cập nhật giá khuyến mãi hàng ngày
DELIMITER //
DROP EVENT IF EXISTS update_promotion_prices_daily //
CREATE EVENT update_promotion_prices_daily
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_DATE + INTERVAL 1 DAY + INTERVAL 1 MINUTE
DO
BEGIN
    CALL UpdatePromotionPrices();
END //
DELIMITER ;

-- Kích hoạt Event Scheduler
SET GLOBAL event_scheduler = ON;



INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi, vaiTro, tenDangNhap, matKhau)
VALUES ('Admin', '0000000000', 'admin@shop.com', 'Trụ sở chính', 'admin', 'admin', 'admin123');
INSERT INTO danh_muc (tenDanhMuc, moTa)
VALUES 
('Sofa', 'Các loại ghế sofa cao cấp và hiện đại.'),
('Bàn trà', 'Bàn trà nhỏ gọn dùng cho phòng khách.'),
('Bàn ăn', 'Bàn ăn gia đình với nhiều kích thước.'),
('Ghế thư giãn', 'Ghế ngồi thoải mái dùng để thư giãn.'),
('Tủ giường', 'Tủ kết hợp giường giúp tiết kiệm không gian.');
INSERT INTO san_pham (tenSanPham, chiTiet, giaGoc, giaKhuyenMai, tinhTrang, idDanhMuc, soLuongTonKho, hinhAnh)
VALUES 
('Sofa Góc L', 'Sofa góc L chất liệu da cao cấp.', 12000000, 10000000, 'Còn hàng', 1, 5, 'sofa1.jpg'),
('Sofa Băng', 'Sofa băng vải bố, màu xám hiện đại.', 9500000, 8700000, 'Còn hàng', 1, 3, 'sofa2.jpg'),
('Bàn Trà Tròn', 'Bàn trà tròn chân gỗ sồi.', 2500000, 2200000, 'Còn hàng', 2, 10, 'sofa3.jpg'),
('Bàn Trà Vuông', 'Bàn trà vuông mặt kính đen.', 2700000, 2400000, 'Còn hàng', 2, 7, 'sofa4.jpg'),
('Bàn Ăn 6 Ghế', 'Bàn ăn gỗ tự nhiên, kèm 6 ghế.', 8500000, 7800000, 'Còn hàng', 3, 4, 'sofa5.jpg'),
('Bàn Ăn Tròn', 'Bàn ăn tròn xoay, tiết kiệm không gian.', 7900000, 7200000, 'Còn hàng', 3, 3, 'sofa6.jpg'),
('Ghế Thư Giãn Gỗ', 'Ghế gỗ cong kiểu Nhật.', 4200000, 3900000, 'Còn hàng', 4, 6, 'sofa7.jpg'),
('Ghế Thư Giãn Da', 'Ghế bọc da ngả lưng thư giãn.', 4800000, 4500000, 'Còn hàng', 4, 2, 'sofa8.jpg'),
('Tủ Giường Gấp', 'Giường gấp kết hợp tủ treo.', 15000000, 13500000, 'Còn hàng', 5, 3, 'sofa9.png'),
('Tủ Giường Kéo', 'Giường kéo với ngăn chứa đồ.', 11000000, 9800000, 'Còn hàng', 5, 4, 'sofa10.png');
INSERT INTO nguoi_dung (hoTen, sdt, email, diaChi, vaiTro, tenDangNhap, matKhau)
VALUES 
('Nguyễn Văn A', '0912345678', 'nguyenvana@gmail.com', 'Hà Nội', 'user', NULL, NULL),
('Trần Thị B', '0987654321', 'tranthib@yahoo.com', 'TP. Hồ Chí Minh', 'user', NULL, NULL),
('Lê Văn C', '0901122334', 'levanc@gmail.com', 'Đà Nẵng', 'user', NULL, NULL),
('Phạm Thị D', '0933445566', 'phamthid@hotmail.com', 'Cần Thơ', 'user', NULL, NULL),
('Đỗ Văn E', '0977554433', 'dovane@outlook.com', 'Hải Phòng', 'user', NULL, NULL),
('Nguyễn Thị F', '0911223344', 'nguyenf@gmail.com', 'Bắc Ninh', 'user', NULL, NULL),
('Trần Văn G', '0988776655', 'tranvg@gmail.com', 'Quảng Ninh', 'user', NULL, NULL),
('Lê Thị H', '0908765432', 'lethih@gmail.com', 'Thái Nguyên', 'user', NULL, NULL),
('Phạm Văn I', '0932112233', 'phamvi@gmail.com', 'Nam Định', 'user', NULL, NULL),
('Đỗ Thị K', '0977123456', 'dothik@yahoo.com', 'Vĩnh Phúc', 'user', NULL, NULL),
('Nguyễn Văn L', '0912345679', 'nguyenl@gmail.com', 'Hà Nội', 'user', NULL, NULL),
('Trần Thị M', '0987654322', 'tranthim@gmail.com', 'TP. Hồ Chí Minh', 'user', NULL, NULL),
('Lê Văn N', '0901122335', 'levann@gmail.com', 'Đà Nẵng', 'user', NULL, NULL),
('Phạm Thị O', '0933445567', 'phamtho@gmail.com', 'Cần Thơ', 'user', NULL, NULL),
('Đỗ Văn P', '0977554434', 'dovanp@gmail.com', 'Hải Phòng', 'user', NULL, NULL),
('Nguyễn Thị Q', '0911223345', 'nguyenq@gmail.com', 'Bắc Ninh', 'user', NULL, NULL),
('Trần Thị R', '0988776656', 'trantr@gmail.com', 'Quảng Ninh', 'user', NULL, NULL),
('Lê Văn S', '0909876543', 'levans@gmail.com', 'Thái Nguyên', 'user', NULL, NULL),
('Phạm Thị T', '0933445568', 'phamthit@gmail.com', 'Nam Định', 'user', NULL, NULL),
('Đỗ Văn U', '0977554435', 'dovanu@gmail.com', 'Vĩnh Phúc', 'user', NULL, NULL);

INSERT INTO don_hang (ngayLap, trangThai, tongTien, maNguoiDung)
VALUES 
('2025-05-01', 'Chờ xử lý', 5000000.00, 1),
('2025-05-02', 'Đang giao', 7000000.00, 2),
('2025-05-03', 'Hoàn thành', 12000000.00, 3),
('2025-05-04', 'Hủy', 15000000.00, 4),
('2025-05-05', 'Chờ xử lý', 4500000.00, 5),
('2025-05-06', 'Đang giao', 9000000.00, 6),
('2025-05-07', 'Hoàn thành', 2500000.00, 7),
('2025-05-08', 'Hủy', 13000000.00, 8),
('2025-05-09', 'Chờ xử lý', 1000000.00, 9),
('2025-05-10', 'Đang giao', 8000000.00, 10),
('2025-05-11', 'Hoàn thành', 15000000.00, 11),
('2025-05-12', 'Hủy', 6000000.00, 12),
('2025-05-13', 'Chờ xử lý', 11000000.00, 13),
('2025-05-14', 'Đang giao', 7000000.00, 14),
('2025-05-15', 'Hoàn thành', 2000000.00, 15),
('2025-05-16', 'Hủy', 12000000.00, 16),
('2025-05-17', 'Chờ xử lý', 4000000.00, 17),
('2025-05-18', 'Đang giao', 15000000.00, 18),
('2025-05-19', 'Hoàn thành', 8500000.00, 19),
('2025-05-20', 'Hủy', 3000000.00, 20);
INSERT INTO chi_tiet_don_hang (maDonHang, maSanPham, soLuong, donGia)
VALUES 
(1, 1, 2, 2500000.00),
(1, 2, 1, 2000000.00),
(2, 3, 3, 3000000.00),
(2, 4, 2, 3500000.00),
(3, 5, 4, 2100000.00),
(3, 6, 2, 3800000.00),
(4, 7, 1, 2500000.00),
(4, 8, 3, 4000000.00),
(5, 9, 2, 2500000.00),
(5, 10, 1, 2000000.00),
(6, 1, 1, 2500000.00),
(6, 2, 2, 2000000.00),
(7, 3, 4, 3000000.00),
(7, 4, 3, 3500000.00),
(8, 5, 2, 2100000.00),
(8, 6, 2, 3800000.00),
(9, 7, 1, 2500000.00),
(9, 8, 1, 3000000.00),
(10, 9, 3, 2500000.00),
(10, 10, 2, 2000000.00),
(11, 1, 2, 2500000.00);

INSERT INTO thanh_toan (maDonHang, hinhThucThanhToan, trangThai, ngayThanhToan)
VALUES 
(1, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-01'),
(2, 'Tiền mặt', 'Đã thanh toán', '2025-05-02'),
(3, 'Chuyển khoản', 'Đã thanh toán', '2025-05-03'),
(4, 'Tiền mặt', 'Chưa thanh toán', '2025-05-04'),
(5, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-05'),
(6, 'Tiền mặt', 'Đã thanh toán', '2025-05-06'),
(7, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-07'),
(8, 'Tiền mặt', 'Đã thanh toán', '2025-05-08'),
(9, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-09'),
(10, 'Tiền mặt', 'Đã thanh toán', '2025-05-10'),
(11, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-11'),
(12, 'Tiền mặt', 'Đã thanh toán', '2025-05-12'),
(13, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-13'),
(14, 'Tiền mặt', 'Đã thanh toán', '2025-05-14'),
(15, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-15'),
(16, 'Tiền mặt', 'Đã thanh toán', '2025-05-16'),
(17, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-17'),
(18, 'Tiền mặt', 'Đã thanh toán', '2025-05-18'),
(19, 'Chuyển khoản', 'Chưa thanh toán', '2025-05-19'),
(20, 'Tiền mặt', 'Đã thanh toán', '2025-05-20');
INSERT INTO khuyen_mai (ngayBatDau, ngayKetThuc, giaKhuyenMai, maSanPham)
VALUES 
('2025-05-01', '2025-05-10', 1000000.00, 1),
('2025-05-05', '2025-05-15', 1500000.00, 2),
('2025-05-10', '2025-05-20', 500000.00, 3),
('2025-05-01', '2025-05-12', 2000000.00, 4),
('2025-05-02', '2025-05-10', 1200000.00, 5),
('2025-05-05', '2025-05-18', 800000.00, 6),
('2025-05-07', '2025-05-17', 1500000.00, 7),
('2025-05-01', '2025-05-20', 1000000.00, 8),
('2025-05-02', '2025-05-12', 1100000.00, 9),
('2025-05-06', '2025-05-15', 1300000.00, 10),
('2025-05-03', '2025-05-13', 1400000.00, 1),
('2025-05-04', '2025-05-14', 1700000.00, 2),
('2025-05-06', '2025-05-16', 1100000.00, 3),
('2025-05-01', '2025-05-11', 2000000.00, 4),
('2025-05-02', '2025-05-12', 1900000.00, 5),
('2025-05-04', '2025-05-14', 1600000.00, 6),
('2025-05-03', '2025-05-13', 1200000.00, 7),
('2025-05-05', '2025-05-15', 1500000.00, 8),
('2025-05-07', '2025-05-17', 1300000.00, 9),
('2025-05-01', '2025-05-10', 1100000.00, 10);

INSERT INTO danh_gia (maSanPham, maNguoiDung, diemDanhGia, noiDung, ngayDanhGia)
VALUES
(1, 1, 5, 'Sofa rất đẹp và thoải mái!', '2025-05-01'),
(2, 2, 4, 'Sofa khá tốt nhưng giá hơi cao.', '2025-05-02'),
(3, 3, 5, 'Bàn trà rất chắc chắn, phù hợp với không gian nhỏ.', '2025-05-03'),
(4, 4, 3, 'Bàn trà mặt kính khá mỏng.', '2025-05-04'),
(5, 5, 4, 'Bàn ăn gỗ đẹp, chất lượng ổn.', '2025-05-05'),
(6, 6, 4, 'Bàn ăn tròn tiết kiệm diện tích, nhưng hơi nhỏ.', '2025-05-06'),
(7, 7, 5, 'Ghế thư giãn rất thoải mái, kiểu dáng đẹp.', '2025-05-07'),
(8, 8, 4, 'Ghế thư giãn da tốt, nhưng cần cải thiện đệm ngồi.', '2025-05-08'),
(9, 9, 5, 'Giường gấp kết hợp tủ rất tiện lợi cho không gian nhỏ.', '2025-05-09'),
(10, 10, 3, 'Giường kéo hơi khó sử dụng, cần cải thiện.', '2025-05-10');


