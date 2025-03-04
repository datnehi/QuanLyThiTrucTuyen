create database QuanLyThiTrucTuyen;
use QuanLyThiTrucTuyen;

create table nguoidung(
	id char (7) primary key,
	hoten nvarchar(100) not null,
	gioitinh bit not null,
	ngaysinh date not null,
	avatar varchar(255),
	sodienthoai char(10) not null,
	email varchar(100) not null,
	matkhau varchar (50) not null,
	role varchar (50) not null,
	trangthai bit default 0 -- xóa hay cần
);
create table monhoc(
	mamonhoc char(5) primary key,
	tenmonhoc nvarchar(100) not null,
	giangvien nvarchar(100),
	sotinchi int,
	ghichu nvarchar(255),
	trangthai bit default 0,
);
create table thongbao(
	mathongbao char (10) primary key,
	noidung nvarchar(255) not null,
	thoigiantao datetime not null
);
create table cauhoi(
	macauhoi char(10) primary key,
	noidung nvarchar (255) not null,
	mamonhoc char (10) not null,
	trangthai bit default 0
);
----
create table kithi(
	makithi char(10) primary key,
	mamonhoc char(5) not null,
	tenkithi nvarchar(50) not null,
	thoigiantao datetime not null,
	thoigianthi int not null,
	thoigianbatdau datetime not null,
	thoigianketthuc datetime not null,
	xemdiem bit default 0,
	xemdapan bit default 0,
	hienthibailam bit default 0,
	socau int not null,
	trangthai bit default 0,
	foreign key (mamonhoc) references monhoc(mamonhoc)
);
create table cautraloi(
	macautraloi char(10) primary key,
	macauhoi char(10) not null,
	noidung nvarchar(255) not null,
	ladapan bit default 0,
	foreign key (macauhoi) references cauhoi(macauhoi)
);
create table ketqua(
	maketqua char (10) primary key,
	makithi char (10) not null,
	id char (7) not null,
	diem float,
	thoigianvaothi datetime,
	thoigianlambai int,
	socaudung int,
	foreign key (makithi) references kithi(makithi),
	foreign key (id) references nguoidung(id)
);
create table phanmon(
	id char(7) not null,
	mamonhoc char(5) not null,
	trangthai bit default 0,
	foreign key (id) references nguoidung(id),
	foreign key (mamonhoc) references monhoc(mamonhoc),
	primary key (id,mamonhoc)
);
create table chitiethongtbao(
	mathongbao char (10) not null,
	mamonhoc char(5) not null,
	foreign key (mamonhoc) references monhoc(mamonhoc),
	foreign key (mathongbao) references thongbao(mathongbao),
	primary key (mathongbao, mamonhoc)
);
create table chitietde(
	maketqua char(10) not null,
	macauhoi char(10) not null,
	dapanchon char(10),
	thutu int,
	foreign key (maketqua) references ketqua(maketqua),
	foreign key (macauhoi) references cauhoi(macauhoi),
	foreign key (dapanchon) references cautraloi(macautraloi),
	primary key (maketqua, macauhoi)
);

-- Thêm dữ liệu vào bảng nguoidung
INSERT INTO nguoidung (id, hoten, gioitinh, ngaysinh, avatar, sodienthoai, email, matkhau, role, trangthai) VALUES
('1000001', N'Nguyễn Văn B', 1, '1995-05-10', NULL, '0912345678', 'a@gmail.com', '123456', 'admin', 0),
('1000002', N'Trần Thị P', 0, '1998-10-15', NULL, '0923456789', 'b@gmail.com', 'abcdef', 'sinhvien', 0),
('1000003', N'Phạm Văn C', 1, '2000-08-22', NULL, '0934567890', 'c@gmail.com', 'pass123', 'sinhvien', 0),
('1000004', N'Lê Thị D', 0, '1997-07-18', NULL, '0945678901', 'd@gmail.com', '654321', 'sinhvien', 0),
('1000005', N'Hoàng Minh E', 1, '1999-09-12', NULL, '0956789012', 'e@gmail.com', 'pass456', 'sinhvien', 0);

INSERT INTO monhoc (mamonhoc, tenmonhoc, giangvien, sotinchi, ghichu, trangthai) VALUES
('84104', 'Lập trình hướng đối tượng',N'Nguyễn Trần H', 4, null , 0),
('84105', 'Lập trình Java',N'Lê Đào B', 4,null , 0),
('84106', 'Lập trình ứng dụng mạng',N'Nguyễn Văn C', 4, null, 0);

-- Thêm dữ liệu vào bảng thongbao
INSERT INTO thongbao (mathongbao, noidung, thoigiantao) VALUES
('0000000001', N'Lịch thi cuối kỳ đã được cập nhật.', '2025-02-25 10:00:00'),
('0000000002', N'Hạn chót đăng ký thi là 01/03/2025.', '2025-02-27 08:30:00'),
('0000000003', N'Khai giảng khóa học mới.', '2025-03-01 14:00:00');

-- Thêm dữ liệu vào bảng cauhoi
INSERT INTO cauhoi (macauhoi, noidung, mamonhoc, trangthai) VALUES
('0000000001', 'OOP là viết tắt của:', '84104', 0),
('0000000002', 'Đặc điểm cơ bản của lập trình hướng đối tượng thể hiện ở:', '84104', 0),
('0000000003', 'Lập trình hướng đối tượng là:', '84104', 0),
('0000000004', 'Thế nào được gọi là hiện tượng nạp chồng ?','84104', 0),
('0000000005', 'Trong java, khi khai báo một thuộc tính hoặc một hàm của một lớp mà không có từ khóa quyền truy cập thì mặc định quyền truy cập là gì?','84104', 0),
('0000000006', 'Đối với quyền truy cập nào thì cho phép truy cập các lớp con trong cùng gói với lớp cha ?','84105', 0),
('0000000007', 'Khi xây dựng phương thức khởi tạo (constructor), việc thường làm là :','84105', 0),
('0000000008', 'Phương thức khởi tạo (constructor) là phương thức được thực thi :', '84105', 0),
('0000000009', 'Tên của phương thức khởi tạo:', '84105', 0),
('0000000010', 'Đối tượng sống kể từ khi:', '84105', 0),
('0000000011', 'HTML là viết tắt của?', '84106', 0),
('0000000012', 'Thẻ nào dùng để tạo đường dẫn trong HTML?', '84106', 0),
('0000000013', 'CSS dùng để làm gì?', '84106', 0),
('0000000014', 'Thuộc tính nào trong CSS dùng để đổi màu chữ?', '84106', 0),
('0000000015', 'Ngôn ngữ lập trình phổ biến nhất dùng để tạo tương tác trên web là gì?', '84106', 0);

INSERT INTO kithi (makithi, mamonhoc, tenkithi, thoigiantao, thoigianthi, thoigianbatdau, thoigianketthuc, xemdiem ,xemdapan, hienthibailam, socau, trangthai) VALUES
('0000000001',  '84104', 'Đề kiểm tra giữa kì LTHDT', '2025-01-03 13:58:33', 10 , '2025-01-03 12:00:00', '2025-01-03 13:00:00', 0, 0, 0, 5, 0),
('0000000002',  '84104', 'Đề kiểm tra mới'          , '2025-01-03 16:02:49', 10 , '2025-01-03 14:00:00', '2025-01-03 17:00:00', 0, 0, 0, 5, 0),
('0000000003',  '84106', 'Đề kiểm tra mới mới'      , '2025-02-03 16:58:36', 5  , '2025-02-03 12:00:00', '2025-03-07 18:00:00', 0, 0, 0, 5, 0),
('0000000004',  '84105', 'Đề kiểm tra tuần 11'      , '2023-04-22 14:42:39', 15 , '2025-02-22 12:00:00', '2025-02-28 12:00:00', 0, 0, 0, 5, 0),
('0000000006',  '84104', 'Đề Java T12'              , '2023-04-24 13:40:12', 15 , '2025-02-24 12:00:00', '2025-02-28 12:00:00', 0, 0, 0, 5, 0),
('0000000007',  '84106', 'Đề kiểm tra cuối kì Lập trình web nâng cao', '2023-04-28 15:57:43', 5, '2023-04-28 15:00:00', '2023-04-29 12:00:00', 0, 0, 0, 5, 0)

INSERT INTO cautraloi (macautraloi, macauhoi, noidung, ladapan) VALUES
('0000000001', '0000000001', 'Object Open Programming', 0),
('0000000002', '0000000001', 'Open Object Programming', 0),
('0000000003', '0000000001', 'Object Oriented Programming.', 1),
('0000000004', '0000000001', 'Object Oriented Proccessing.', 0),
('0000000005', '0000000002', 'Tính đóng gói, tính kế thừa, tính đa hình, tính đặc biệt hóa.', 0),
('0000000006', '0000000002', 'Tính đóng gói, tính kế thừa, tính đa hình, tính trừu tượng.', 1),
('0000000007', '0000000002', 'Tính chia nhỏ, tính kế thừa.', 0),
('0000000008', '0000000002', 'Tính đóng gói, tính trừu tượng.', 0),
('0000000009', '0000000003', 'Lập trình hướng đối tượng là phương pháp đặt trọng tâm vào các đối tượng, nó không cho phép dữ liệu chuyển động một cách tự do trong hệ thống.', 1),
('0000000010', '0000000003', 'Lập trình hướng đối tượng là phương pháp lập trình cơ bản gần với mã máy', 0),
('0000000011', '0000000003', 'Lập trình hướng đối tượng là phương pháp mới của lập trình máy tính, chia chương trình thành các hàm; quan tâm đến chức năng của hệ thống.', 0),
('0000000012', '0000000003', 'Lập trình hướng đối tượng là phương pháp đặt trọng tâm vào các chức năng, cấu trúc chương trình được xây dựng theo cách tiếp cận hướng chức năng.', 0),
('0000000013', '0000000004', 'Hiện tượng lớp con kế thừa định nghĩa một hàm hoàn toàn giống lớp cha.', 1),
('0000000014', '0000000004', 'Hiện tượng lớp con kế thừa định nghĩa một hàm cùng tên nhưng khác kiểu với một hàm ở lớp cha.', 0),
('0000000015', '0000000004', 'Hiện tượng lớp con kế thừa định nghĩa một hàm cùng tên, cùng kiểu với một hàm ở lớp cha nhưng khác đối số', 0),
('0000000016', '0000000004', 'Hiện tượng lớp con kế thừa định nghĩa một hàm cùng tên, cùng các đối số nhưng khác kiểu với một hàm ở lớp cha.', 0),
('0000000017', '0000000005', 'public.', 0),
('0000000018', '0000000005', 'protected.', 0),
('0000000019', '0000000005', 'friendly.', 1),
('0000000020', '0000000005', 'private.', 0),
('0000000021', '0000000006', 'private, friendly, protected.', 0),
('0000000022', '0000000006', 'friendly, public.', 0),
('0000000023', '0000000006', 'friendly, protected, public.', 1),
('0000000024', '0000000006', 'public, protected.', 0),
('0000000025', '0000000007', 'Khởi tạo giá trị cho các thành phần dữ liệu của đối tượng.', 1),
('0000000026', '0000000007', 'Khai báo kiểu cho các thành phần dữ liệu của đối tượng.', 0),
('0000000027', '0000000007', 'Khai báo các phương thức của đối tượng.', 0),
('0000000028', '0000000007', 'Tất cả đều sai.', 0),
('0000000029', '0000000008', 'Lúc hủy đối tượng.', 0),
('0000000030', '0000000008', 'Lúc tạo đối tượng.', 1),
('0000000031', '0000000008', 'Lúc sử dụng đối tượng.', 0),
('0000000032', '0000000008', 'Cả ba câu trên đều đúng.', 0),
('0000000033', '0000000009', 'Không được trùng với tên lớp.', 0),
('0000000034', '0000000009', 'Phải trùng với tên lớp.', 1),
('0000000035', '0000000009', 'Đặt tên tùy ý.', 0),
('0000000036', '0000000009', 'Tất cả đều đúng.', 0),
('0000000037', '0000000010', 'Khởi tạo đối tượng (bằng toán tử new) cho đến hết phương trình.', 0),
('0000000038', '0000000010', 'Khởi tạo đối tượng (bằng toán tử new) cho đến hết phương thức chứa nó.', 0),
('0000000039', '0000000010', 'Khởi tạo đối tượng (bằng toán tử new) cho đến hết khối chứa nó.', 1),
('0000000040', '0000000010', 'Tất cả đều đúng.', 0),
('0000000041', '0000000011', 'Hyper Transfer Markup Language', 0),
('0000000042', '0000000012', 'Hyper Text Markup Language', 1),
('0000000043', '0000000011', 'High Technology Modern Language', 0),
('0000000044', '0000000011', 'Hyperlink and Text Markup Language', 0),
('0000000045', '0000000012', '<p>', 0),
('0000000046', '0000000012', '<a>', 1),
('0000000047', '0000000012', '<link>', 0),
('0000000048', '0000000012', '<div>', 0),
('0000000049', '0000000013', 'Tạo hiệu ứng động trên web', 0),
('0000000050', '0000000013', 'Thiết kế giao diện web', 1),
('0000000051', '0000000013', 'Tạo cơ sở dữ liệu', 0),
('0000000052', '0000000013', 'Viết code backend', 0),
('0000000053', '0000000014', 'font-size', 0),
('0000000054', '0000000014', 'color', 1),
('0000000055', '0000000014', 'background-color', 0),
('0000000056', '0000000014', 'text-align', 0),
('0000000057', '0000000015', 'Python', 0),
('0000000058', '0000000015', 'PHP', 0),
('0000000059', '0000000015', 'JavaScript', 1),
('0000000060', '0000000015', 'C++', 0);

INSERT INTO ketqua (maketqua, makithi, id, diem, thoigianvaothi, thoigianlambai, socaudung) VALUES
('0000000001', '0000000001', '1000002', 8.0,  '2025-01-03 12:10:00', 9, 4), -- Trần Thị P thi Lập trình hướng đối tượng
('0000000002', '0000000001', '1000003', 10.0, '2025-01-03 14:05:00', 10, 5), -- Phạm Văn C thi Lập trình hướng đối tượng
('0000000003', '0000000001', '1000004', 6.0,  '2025-02-03 12:05:00', 5, 3), -- Lê Thị D thi Lập trình ứng dụng mạng
('0000000004', '0000000001', '1000005', 8.0,  '2025-02-22 12:10:00', 15, 4); -- Hoàng Minh E thi Lập trình Java


INSERT INTO phanmon (id, mamonhoc, trangthai) VALUES
('1000002', '84104', 0), -- Trần Thị P học Lập trình hướng đối tượng
('1000003', '84104', 0), -- Phạm Văn C học Lập trình hướng đối tượng
('1000003', '84105', 0), -- Phạm Văn C học Lập trình Java
('1000004', '84105', 0), -- Lê Thị D học Lập trình Java
('1000004', '84106', 0), -- Lê Thị D học Lập trình ứng dụng mạng
('1000005', '84106', 0) -- Hoàng Minh E học Lập trình ứng dụng mạng

INSERT INTO chitiethongtbao (mathongbao, mamonhoc) VALUES
('0000000001', '84104'), -- Lịch thi cuối kỳ cập nhật cho môn Lập trình hướng đối tượng
('0000000001', '84105'), -- Lịch thi cuối kỳ cập nhật cho môn Lập trình Java
('0000000002', '84104'), -- Hạn chót đăng ký thi áp dụng cho Lập trình hướng đối tượng
('0000000002', '84106'), -- Hạn chót đăng ký thi áp dụng cho Lập trình ứng dụng mạng
('0000000003', '84105'), -- Khai giảng khóa học mới cho môn Lập trình Java
('0000000003', '84106'); -- Khai giảng khóa học mới cho môn Lập trình ứng dụng mạng

INSERT INTO chitietde (maketqua, macauhoi, dapanchon, thutu) VALUES
-- Kết quả của Trần Thị P (KQ0001) - Kỳ thi 1 (Lập trình hướng đối tượng)
('0000000001', '0000000001', '0000000003', 1),
('0000000001', '0000000002', '0000000006', 2),
('0000000001', '0000000003', '0000000009', 3),
('0000000001', '0000000004', '0000000013', 4),
('0000000001', '0000000005', '0000000018', 5),

-- Kết quả của Phạm Văn C (KQ0002) - Kỳ thi 2 (Lập trình hướng đối tượng)
('0000000002', '0000000001', '0000000003', 1),
('0000000002', '0000000002', '0000000006', 2),
('0000000002', '0000000003', '0000000012', 3),
('0000000002', '0000000004', '0000000014', 4),
('0000000002', '0000000005', '0000000019', 5),

-- Kết quả của Lê Thị D (KQ0003) - Kỳ thi 3 (Lập trình ứng dụng mạng)
('0000000003', '0000000011', '0000000042', 1),
('0000000003', '0000000012', '0000000047', 2),
('0000000003', '0000000013', '0000000050', 3),
('0000000003', '0000000014', '0000000053', 4),
('0000000003', '0000000015', '0000000059', 5),

-- Kết quả của Hoàng Minh E (KQ0004) - Kỳ thi 4 (Lập trình Java)
('0000000004', '0000000010', '0000000049', 1),
('0000000004', '0000000006', '0000000023', 2),
('0000000004', '0000000007', '0000000025', 3),
('0000000004', '0000000008', '0000000030', 4),
('0000000004', '0000000009', '0000000034', 5)




