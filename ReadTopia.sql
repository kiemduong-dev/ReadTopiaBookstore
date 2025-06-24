	USE [master]
	GO
	/*******************************************************************************
	   Drop database if it exists
	********************************************************************************/
	IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'ReadTopia')
	BEGIN
		ALTER DATABASE ReadTopia SET OFFLINE WITH ROLLBACK IMMEDIATE;
		ALTER DATABASE ReadTopia SET ONLINE;
		DROP DATABASE ReadTopia;
	END
	GO

	CREATE DATABASE ReadTopia
	GO
	USE ReadTopia
	GO

	/*******************************************************************************
	   Drop tables if exists
	*******************************************************************************/
	DECLARE @sql NVARCHAR(MAX) 
	SET @sql = N'' 

	SELECT @sql = @sql + N'ALTER TABLE ' + QUOTENAME(KCU1.TABLE_SCHEMA) 
		+ N'.' + QUOTENAME(KCU1.TABLE_NAME) 
		+ N' DROP CONSTRAINT ' 
		+ QUOTENAME(rc.CONSTRAINT_NAME) + N'; ' + CHAR(13) + CHAR(10) 
	FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS AS RC 

	INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU1 
		ON KCU1.CONSTRAINT_CATALOG = RC.CONSTRAINT_CATALOG  
		AND KCU1.CONSTRAINT_SCHEMA = RC.CONSTRAINT_SCHEMA 
		AND KCU1.CONSTRAINT_NAME = RC.CONSTRAINT_NAME 

	EXECUTE(@sql) 
	GO

	DECLARE @sql2 NVARCHAR(MAX) = ''
	SELECT @sql2 += ' DROP TABLE ' + QUOTENAME(TABLE_SCHEMA) + '.'+ QUOTENAME(TABLE_NAME) + '; '
	FROM INFORMATION_SCHEMA.TABLES
	WHERE TABLE_TYPE = 'BASE TABLE'

	EXEC sp_executesql @sql2 
	GO

	/*******************************************************************************
	   Create tables
	*******************************************************************************/
	CREATE TABLE Account(
		username VARCHAR(255),
		firstName NVARCHAR(255),
		lastName NVARCHAR(255),
		[password] VARCHAR(255),
		dob DATE,
		email VARCHAR(255),
		phone VARCHAR(15),
		[role] INT,
		[address] NVARCHAR(255),
		sex INT,
		accStatus INT,
		code CHAR(6),
		CONSTRAINT PK_Account PRIMARY KEY (username)
	)
	GO

	CREATE TABLE Staff(
		staffID INT IDENTITY(1,1),
		username VARCHAR(255),
		CONSTRAINT PK_Staff PRIMARY KEY (staffID),
		CONSTRAINT FK_Staff_Account FOREIGN KEY (username) REFERENCES Account(username)
	)
	GO

	CREATE TABLE Promotion(
		proID INT IDENTITY(1,1),
		proName NVARCHAR(255),
		proCode VARCHAR(255),
		discount FLOAT,
		startDate DATE,
		endDate DATE,
		quantity INT,
		proStatus INT,
		createdBy INT,
		approvedBy INT,
		CONSTRAINT PK_Promotion PRIMARY KEY (proID),
		CONSTRAINT FK_Promotion_Staff_staffID FOREIGN KEY (createdBy) REFERENCES Staff(staffID),
		CONSTRAINT FK_Promotion_Staff_managerID FOREIGN KEY (approvedBy) REFERENCES Staff(staffID)
	)
	GO

	CREATE TABLE Supplier(
		supID INT IDENTITY(1,1),
		supName NVARCHAR(255),
		supEmail VARCHAR(255),
		supPhone VARCHAR(15),
		supAddress NVARCHAR(255),
		supStatus INT,
		CONSTRAINT PK_Supplier PRIMARY KEY (supID)
	)
	GO

	CREATE TABLE Category(
		catID INT IDENTITY(1,1),
		catName NVARCHAR(255),
		catDescription NTEXT,
		catStatus INT,
		CONSTRAINT PK_Category PRIMARY KEY (catID)
	)
	GO

	CREATE TABLE Book(
		bookID INT IDENTITY(1,1),
		bookTitle NVARCHAR(255),
		author NVARCHAR(255),
		translator NVARCHAR(255),
		publisher NVARCHAR(255),
		publicationYear INT,
		isbn VARCHAR(255),
		[image] TEXT,
		bookDescription NTEXT,
		hardcover INT,
		dimension VARCHAR(255),
		[weight] FLOAT,
		bookPrice DECIMAL(10,2),
		bookQuantity INT,
		bookStatus INT,
		CONSTRAINT PK_Book PRIMARY KEY (bookID)
	)
	GO

	CREATE TABLE Book_Category(
		book_cate_id INT IDENTITY(1,1),
		book_id INT,
		cat_id INT,
		CONSTRAINT PK_Book_Category PRIMARY KEY (book_cate_id),
		CONSTRAINT FK_Book_Category_Category FOREIGN KEY (cat_id) REFERENCES Category(catID),
		CONSTRAINT FK_Book_Category_Book FOREIGN KEY (book_id) REFERENCES Book(bookID)
	)
	GO

	CREATE TABLE Cart(
		cartID INT IDENTITY(1,1),
		username VARCHAR(255),
		bookID INT,
		quantity INT,
		CONSTRAINT PK_Cart PRIMARY KEY (cartID),
		CONSTRAINT FK_Cart_Account FOREIGN KEY (username) REFERENCES Account(username),
		CONSTRAINT FK_Cart_Book FOREIGN KEY (bookID) REFERENCES Book(bookID)
	)
	GO

	CREATE TABLE [Notification](
		notID INT IDENTITY(1,1),
		staffID INT,
		notTitle NVARCHAR(255),
		receiver INT,
		notDescription NTEXT,
		notStatus INT,
		CONSTRAINT PK_Notification PRIMARY KEY (notID),
		CONSTRAINT FK_Notification_Staff FOREIGN KEY (staffID) REFERENCES Staff(staffID)
	)
	GO

	CREATE TABLE [Order](
		orderID INT IDENTITY(1,1),
		proID INT,
		username VARCHAR(255),
		staffID INT,
		orderDate DATE,
		[orderAddress] NVARCHAR(255),
		orderStatus INT,
		CONSTRAINT PK_Order PRIMARY KEY (orderID),
		CONSTRAINT FK_Order_Promotion FOREIGN KEY (proID) REFERENCES Promotion(proID),
		CONSTRAINT FK_Order_Account FOREIGN KEY (username) REFERENCES Account(username),
		CONSTRAINT FK_Order_Staff FOREIGN KEY (staffID) REFERENCES Staff(staffID)
	)
	GO

	CREATE TABLE OrderDetail(
		ODID INT IDENTITY(1,1),
		bookID INT,
		orderID INT,
		quantity INT,
		totalPrice DECIMAL(10,2),
		CONSTRAINT PK_OrderDetail PRIMARY KEY (ODID),
		CONSTRAINT FK_OrderDetail_Book FOREIGN KEY (bookID) REFERENCES Book(bookID),
		CONSTRAINT FK_OrderDetail_Order FOREIGN KEY (orderID) REFERENCES [Order](orderID)
	)
	GO

	CREATE TABLE ImportStock(
		ISID INT IDENTITY(1,1),
		supID INT,
		importDate DATE,
		ISStatus INT,
		staffID INT,
		CONSTRAINT PK_Stock PRIMARY KEY (ISID),
		CONSTRAINT FK_Stock_Supplier FOREIGN KEY (supID) REFERENCES Supplier(supID),
		CONSTRAINT FK_Stock_Staff FOREIGN KEY (staffID) REFERENCES Staff(staffID)
	)
	GO

	CREATE TABLE ImportStockDetail(
		ISDID INT IDENTITY(1,1),
		bookID INT,
		ISID INT,
		ISDQuantity INT,
		importPrice DECIMAL(10,2),
		CONSTRAINT PK_Batch PRIMARY KEY (ISDID),
		CONSTRAINT FK_Batch_Book FOREIGN KEY (bookID) REFERENCES Book(bookID),
		CONSTRAINT FK_Batch_Stock FOREIGN KEY (ISID) REFERENCES ImportStock(ISID)
	)
	GO

	CREATE TABLE InvalidatedToken(
		ITID VARCHAR(255),
		expiryTime DATE,
		username VARCHAR(255),
		CONSTRAINT PK_InvalidatedToken PRIMARY KEY (ITID),
		CONSTRAINT FK_InvalidatedToken_Account FOREIGN KEY (username) REFERENCES Account(username)
	)
	GO

	CREATE TABLE promotion_log(
		pro_log_id INT IDENTITY(1,1),
		pro_id INT,
		staff_id INT,
		pro_action INT,
		pro_log_date DATE,
		CONSTRAINT PK_promotion_log PRIMARY KEY (pro_log_id),
		CONSTRAINT FK_promotion_log_promotion FOREIGN KEY (pro_id) REFERENCES Promotion(proID),
		CONSTRAINT FK_promotion_log_staff FOREIGN KEY (staff_id) REFERENCES Staff(staffID)
	)
	GO

	/*******************************************************************************
	   Insert data into tables
	*******************************************************************************/
	-- Insert admin accounts
-- Insert admin accounts
INSERT INTO Account(username, [password], firstName, lastName, dob, email, [address], phone, sex, [role], accStatus)
VALUES
('admin1', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 1', 'AdminLast1', GETDATE(), 'duongankiem.dev@gmail.com', 'FPT University, Can Tho Campus', '0343634608', 1, 0, 1),
('admin2', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 2', 'AdminLast2', GETDATE(), 'DuyQHCE180889@fpt.edu.vn', 'FPT University, Can Tho Campus', '0343634609', 0, 0, 1),
('admin3', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 3', 'AdminLast3', GETDATE(), 'AnhNTCE181048@fpt.edu.vn', 'FPT University, Can Tho Campus', '0343634610', 1, 0, 1),
('admin4', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 4', 'AdminLast4', GETDATE(), 'AnhNTCE181953@gmail.com', 'FPT University, Can Tho Campus', '0343634611', 0, 0, 1),
('admin5', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 5', 'AdminLast5', GETDATE(), 'BaoVCCE182018@fpt.edu.vn', 'FPT University, Can Tho Campus', '0343634612', 1, 0, 1),
('admin6', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 6', 'AdminLast6', GETDATE(), 'ReadTopia@gmail.com', 'FPT University, Can Tho Campus', '0343634613', 0, 0, 1),
('admin7', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 7', 'AdminLast7', GETDATE(), 'ReadTopia@gmail.com', 'FPT University, Can Tho Campus', '0343634614', 1, 0, 1),
('admin8', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', 'Administrator 8', 'AdminLast8', GETDATE(), 'ReadTopia@gmail.com', 'FPT University, Can Tho Campus', '0343634615', 0, 0, 1),
('YenQNPCE181445', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Yến', N'Quang Nhật Phi', '1998-04-16', 'YenQNPCE181445@fpt.edu.vn', N'1 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh', '0377777778', 1, 0, 1),
('ThienNHNCE180035', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Thiện', N'Nguyễn Huỳnh Nhất', '1996-06-11', 'ThienNHNCE180035@fpt.edu.vn', N'600 Nguyễn Văn Cừ, Q.1, TP. Hồ Chí Minh', '0387654321', 0, 0, 1),
('HanNNCE180049', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Hân', N'Nguyễn Ngọc', '1998-01-25', 'HanNNCE180049@fpt.edu.vn', N'Đại học FPT, Quận Cái Răng, TP. Cần Thơ', '0967123456', 1, 0, 1),
('ThoHPCE181027', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Thọ', N'Huỳnh Phúc', '1996-05-10', 'ThoHPCE181027@fpt.edu.vn', N'Khu Công nghệ cao, Quận 9, TP. Hồ Chí Minh', '0909123456', 0, 0, 1),
('ThuanPVHCE181377', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Thuận', N'Phan Văn Hoà', '1999-06-08', 'ThuanPVHCE181377@fpt.edu.vn', N'Trung tâm CNTT, Đà Nẵng', '0912345678', 0, 0, 1),
('ToanLTCE190713', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Toàn', N'La Thiện', '1992-11-24', 'toanlt.ce190713@gmail.com', N'Hòa Lạc, Thạch Thất, Hà Nội', '0377777777', 0, 0, 1);


-- ==================== INSERT STAFF ====================
INSERT INTO Staff(username)
VALUES
('admin1'), ('admin2'), ('admin3'), ('admin4'),
('admin5'), ('admin6'), ('admin7'), ('admin8'),
('YenQNPCE181445'), ('ThienNHNCE180035'), ('HanNNCE180049'),
('ThoHPCE181027'), ('ThuanPVHCE181377'), ('ToanLTCE190713');
	-- Insert customer accounts
INSERT INTO Account (username, [password], firstName, lastName, dob, email, [address], phone, sex, [role], accStatus)
VALUES
('PhatNTCE171269', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Phát', N'Nguyễn Tấn', '2004-01-23', 'PhatNTCE171269@fpt.edu.vn', N'1 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh', '0976696064', 0, 1, 1),
('LongNHCE171974', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Long', N'Nguyễn Hoàng', '2004-08-01', 'LongNHCE171974@fpt.edu.vn', N'600 Nguyễn Văn Cừ, Q.1, TP. Hồ Chí Minh', '0987543172', 0, 1, 1),
('AnNCHCE180413', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'An', N'Nguyễn Cao Hoài', '2004-06-10', 'AnNCHCE180413@fpt.edu.vn', N'Đại học FPT, Quận Cái Răng, TP. Cần Thơ', '0924256985', 1, 1, 1),
('NghiLTTCE182357', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Nghi', N'Lý Trần Tường', '2004-02-09', 'NghiLTTCE182357@fpt.edu.vn', N'Khu Công nghệ cao, Quận 9, TP. Hồ Chí Minh', '0909550321', 1, 1, 1),
('NguyenHTCE190356', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Nguyễn', N'Huỳnh Trọng', '2004-10-12', 'nguyenht.ce190356@gmail.com', N'Trung tâm CNTT, Đà Nẵng', '0964663064', 0, 1, 1),
('KhangTDCE181439', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Khang', N'Trần Dũy', '2004-11-16', 'KhangTDCE181439@fpt.edu.vn', N'Hòa Lạc, Thạch Thất, Hà Nội', '0955589643', 1, 1, 1),
('KhangTNCE180189', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Khang', N'Trần Nhất', '2004-09-21', 'KhangTNCE180189@fpt.edu.vn', N'1 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh', '0950092514', 1, 1, 1),
('MinhLKBCE180860', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Minh', N'Lê Khắc Bảo', '2004-09-08', 'MinhLKBCE180860@fpt.edu.vn', N'600 Nguyễn Văn Cừ, Q.1, TP. Hồ Chí Minh', '0972784975', 0, 1, 1),
('MinhNCCE181128', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Minh', N'Nguyễn Công', '2004-09-02', 'MinhNCCE181128@fpt.edu.vn', N'Đại học FPT, Quận Cái Răng, TP. Cần Thơ', '0937611814', 0, 1, 1),
('PhuongDVHCE181438', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Phương', N'Đào Văn Hà', '2004-07-18', 'PhuongDVHCE181438@fpt.edu.vn', N'Khu Công nghệ cao, Quận 9, TP. Hồ Chí Minh', '0947956273', 1, 1, 1),
('KhangLNHCE191583', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Khang', N'Lê Nguyễn Hoàng', '2004-08-19', 'KhangLNH.CE191583@gmail.com', N'Trung tâm CNTT, Đà Nẵng', '0998929410', 1, 1, 1),
('TanhNTCE182341', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Tánh', N'Nguyễn Thiện', '2004-08-13', 'TanhNTCE182341@fpt.edu.vn', N'Hòa Lạc, Thạch Thất, Hà Nội', '0910374672', 0, 1, 1),
('TranVTHCE180112', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Trân', N'Võ Thị Huế', '2004-02-28', 'TranVTHCE180112@fpt.edu.vn', N'1 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh', '0938562437', 1, 1, 1),
('ThangNDCE180608', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Thắng', N'Nguyễn Đức', '2004-09-18', 'ThangNDCE180608@fpt.edu.vn', N'600 Nguyễn Văn Cừ, Q.1, TP. Hồ Chí Minh', '0904350319', 0, 1, 1),
('HuyBTCE180610', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Huy', N'Bùi Tuấn', '2004-10-04', 'HuyBTCE180610@fpt.edu.vn', N'Đại học FPT, Quận Cái Răng, TP. Cần Thơ', '0929178751', 0, 1, 1),
('HungNTCE180765', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Hưng', N'Nguyễn Tín', '2004-10-08', 'HungNTCE180765@fpt.edu.vn', N'Khu Công nghệ cao, Quận 9, TP. Hồ Chí Minh', '0939647052', 0, 1, 1),
('ThangDQCE182036', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Thắng', N'Diệp Quốc', '2004-12-16', 'ThangDQCE182036@fpt.edu.vn', N'Trung tâm CNTT, Đà Nẵng', '0953634068', 0, 1, 1),
('DatVTCE180184', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Đạt', N'Võ Tiến', '2004-06-11', 'DatVTCE180184@fpt.edu.vn', N'Hòa Lạc, Thạch Thất, Hà Nội', '0968839206', 0, 1, 1),
('HungPTCE180074', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Hưng', N'Phạm Tấn', '2004-06-22', 'HungPTCE180074@fpt.edu.vn', N'1 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh', '0929331004', 0, 1, 1),
('DiemLTCE180621', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Diễm', N'Lương Thị', '2004-03-16', 'DiemLTCE180621@fpt.edu.vn', N'600 Nguyễn Văn Cừ, Q.1, TP. Hồ Chí Minh', '0980828479', 1, 1, 1),
('LinhLTTCE181262', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Linh', N'Lê Thị Trúc', '2004-02-03', 'LinhLTTCE181262@fpt.edu.vn', N'Đại học FPT, Quận Cái Răng, TP. Cần Thơ', '0912196241', 1, 1, 1),
('DucGTCE182376', '$2a$10$lVJbfkuTptencFw.FBKAueCuPskC2mpFgUmn5nNiSN43UtU9hg2YO', N'Đức', N'Giang Tú', '2004-07-10', 'DucGTCE182376@fpt.edu.vn', N'Khu Công nghệ cao, Quận 9, TP. Hồ Chí Minh', '0962372983', 0, 1, 1);



	--INSERT INTO Book (bookTitle, author, translator, publisher, publicationYear, bookPrice, [image], isbn, bookDescription, hardcover, dimension, weight, bookQuantity, bookStatus)
	--VALUES
	--(N'Chuyện Hay Sử Việt - Nhà Nguyễn - Quốc Gia Thống Nhất', N'Nguyễn Như Mai, Nguyễn Quốc Tín, Nguyễn Huy Thắng', NULL, N'Nhà Xuất Bản Kim Đồng', 2024, 51000, 'https://cdn0.fahasa.com/media/catalog/product/c/h/chuyen-hay-su-viet-9_nha-nguyen.jpg', '8935244897364', N'Lịch sử - truyền thống', 136, N'20.5 x 14.5 x 0.6 cm', 180, 1, 1),
	--(N'Hào Kiệt Đất Phương Nam - Nguyễn Hữu Cảnh - Người Khai Sinh Sài Gòn', N'Hoài Anh, Nguyễn Đông Hải', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 23800, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244887426.jpg', '8935244887426', N'Lịch sử - truyền thống', 32, N'20.5 x 18.5 x 0.3 cm', 100, 1, 1),
	--(N'Tranh Truyện Lịch Sử Việt Nam - Lương Thế Vinh (Tái Bản 2023)', N'Lê Minh Hải, Anh Chi', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_luong-the-vinh_tb-2023.jpg', '8935244892956', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	--(N'Tranh Truyện Lịch Sử Việt Nam - Trần Nhân Tông (Tái Bản 2023)', N'Tạ Huy Long, Lê Phương Liên', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_tran-nhan-tong_tb-2023.jpg', '8935244892932', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	--(N'Tranh Truyện Lịch Sử Việt Nam - Đinh Bộ Lĩnh (Tái Bản 2023)', N'Tạ Huy Long, Nam Việt', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_dinh-bo-linh_tb-2023.jpg', '8935244892901', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	--(N'Tranh Truyện Lịch Sử Việt Nam - Phùng Hưng (Tái Bản 2023)', N'Lê Minh Hải, Anh Chi', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_phung-hung_tb-2023.jpg', '8935244892987', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	--(N'Những Người Thầy Trong Sử Việt - Tập 2 (Tái Bản 2023)', N'Nguyễn Như Mai, Nguyễn Quốc Tín, Nguyễn Huy Thắng', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 76000, 'https://cdn0.fahasa.com/media/catalog/product/n/h/nhung-nguoi-thay-trong-su-viet-2_tb-2023.jpg', '8935244899207', N'Lịch sử - truyền thống', 356, N'20.5 x 14.5 x 1.7 cm', 370, 1, 1),
	--(N'Tranh Truyện Lịch Sử Việt Nam - Ngô Sĩ Liên (Tái Bản 2023)', N'Hiếu Minh, Lê Minh Hải', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244886412.jpg', '8935244886412', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.5 cm', 100, 1, 1),
	--(N'Chuyện Hay Sử Việt - Nhà Trần - Hào Khí Đông A', N'Nguyễn Như Mai, Nguyễn Quốc Tín, Nguyễn Huy Thắng', NULL, N'Nhà Xuất Bản Kim Đồng', 2024, 51000, 'https://cdn0.fahasa.com/media/catalog/product/c/h/chuyen-hay-su-viet-5_nha-tran.jpg', '8935244897326', N'Lịch sử - truyền thống', 140, N'20.5 x 14.5 x 0.7 cm', 185, 1, 1),
	--(N'Hào Kiệt Đất Phương Nam - Trương Định - Bình Tây Đại Nguyên Soái', N'Hoài Anh, Nguyễn Đông Hải', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 23000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244887488.jpg', '8935244887488', N'Lịch sử - truyền thống', 32, N'20.5 x 18.5 x 0.2 cm', 83, 1, 1),
	--(N'Tranh Truyện Lịch Sử Việt Nam - Phạm Ngũ Lão (Tái Bản 2023)', N'Lê Minh Hải, Anh Chi', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_pham-ngu-lao_tb-2023_1.jpg', '8935244892963', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	--(N'Tranh Truyện Lịch Sử Việt Nam - Lý Nam Đế (Tái Bản 2023)', N'Tạ Huy Long', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_ly-nam-de_tb-2023.jpg', '8935244892888', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1);

	INSERT INTO Book (bookTitle, author, translator, publisher, publicationYear, bookPrice, [image], isbn, bookDescription, hardcover, dimension, weight, bookQuantity, bookStatus)
	VALUES
	(N'Tranh Truyện Lịch Sử Việt Nam - Lý Công Uẩn (Tái Bản 2023)', N'Nam Việt, Tạ Huy Long', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244886399.jpg', '8935244886399', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.5 cm', 100, 1, 1),
	(N'Tranh Truyện Lịch Sử Việt Nam - Trần Quốc Toản (Tái Bản 2023)', N'Nguyễn Huy Thắng, Lê Minh Hải', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_tran-quoc-toan_tb-2023.jpg', '8935244893090', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	(N'Hào Kiệt Đất Phương Nam - Nguyễn Trung Trực - Gươm Tuốt Kiên Giang', N'Hoài Anh, Nguyễn Đông Hải', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 23000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244887501.jpg', '8935244887501', N'Lịch sử - truyền thống', 32, N'20.5 x 18.5 x 0.2 cm', 83, 1, 1),
	(N'Hào Kiệt Đất Phương Nam - Thiên Hộ Dương - Chiến Lũy Tháp Mười', N'Hoài Anh, Nguyễn Đông Hải', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 23000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244887525.jpg', '8935244887525', N'Lịch sử - truyền thống', 32, N'20.5 x 18.5 x 0.3 cm', 100, 1, 1),
	(N'Tranh Truyện Lịch Sử Việt Nam - Lê Chân (Tái Bản 2023)', N'Vương Trọng, Nhóm Oopsy', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_le-chan_tb-2023.jpg', '8935244893083', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	(N'Tranh Truyện Lịch Sử Việt Nam - Lý Thái Tông', N'Lê Minh Hải, Hiếu Minh', NULL, N'Nhà Xuất Bản Kim Đồng', 2023, 15000, 'https://cdn0.fahasa.com/media/catalog/product/t/r/tranh-truyen-lich-su-viet-nam_bia_ly-thai-tong.jpg', '8935244891751', N'Lịch sử - truyền thống', 32, N'20.5 x 14.5 x 0.2 cm', 100, 1, 1),
	(N'Hiểu Về Tài Chính, Vững Bước Tương Lai - Cân Đối Ngân Sách', N'Cecilia Minden', N'Hoàng Thi', N'Nhà Xuất Bản Kim Đồng', 2022, 21000, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244376.jpg', '8935244859409', N'Kiến thức - Kỹ năng', 32, N'20.5 x 14.5 x 0.3 cm', 54, 1, 1),
	(N'Hiểu Về Tài Chính, Vững Bước Tương Lai - Đầu Tư Thông Minh', N'Katie Marsico', N'Phạm Nguyên Trường', N'Nhà Xuất Bản Kim Đồng', 2021, 21000, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244378.jpg', '8935244859348', N'Kiến thức - Kỹ năng', 32, N'20.5 x 14.5 x 0.3 cm', 56, 1, 1),
	(N'Tớ Là CEO Nhí 2 - Tập 7 - Giới Hạn Của Tiền - Cùng Bé Khám Phá Kĩ Năng Quản Lí Tài Chính', N'True Fruit', N'Trần Thiên Tú', N'Nhà Xuất Bản Kim Đồng', 2022, 21000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244873498.jpg', '8935244873498', N'Kiến thức - Kỹ năng', 32, N'20.5 x 19 x 0.3 cm', 70, 1, 1),
	(N'Tớ Là CEO Nhí 2 - Tập 8 - Trả Tiền Thế Nào Nhỉ? - Cùng Bé Khám Phá Kĩ Năng Quản Lí Tài Chính', N'True Fruit', N'Trần Thiên Tú', N'Nhà Xuất Bản Kim Đồng', 2022, 21000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244873504.jpg', '8935244873504', N'Kiến thức - Kỹ năng', 32, N'20.5 x 19 x 0.3 cm', 70, 1, 1),
	(N'Hiểu Về Tài Chính, Vững Bước Tương Lai - Hiểu Về Thuế', N'Linda Crotta Brennan', N'Phạm Nguyên Trường', N'Nhà Xuất Bản Kim Đồng', 2021, 21000, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244379.jpg', '8935244859416', N'Kiến thức - Kỹ năng', 32, N'20.5 x 14.5 x 0.3 cm', 55, 1, 1),
	(N'Hiểu Về Tài Chính, Vững Bước Tương Lai - Tập Tành Kinh Doanh', N'Cecilia Minden', N'Phạm Nguyên Trường', N'Nhà Xuất Bản Kim Đồng', 2022, 21000, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244380.jpg', '8935244859355', N'Kiến thức - Kỹ năng', 32, N'20.5 x 14.5 x 0.3 cm', 56, 1, 1),
	(N'Hiểu Về Tài Chính, Vững Bước Tương Lai - Tiêu Dùng Thông Minh', N'Cecilia Minden', N'Phạm Nguyên Trường', N'Nhà Xuất Bản Kim Đồng', 2021, 21000, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244375.jpg', '8935244859379', N'Kiến thức - Kỹ năng', 32, N'20.5 x 14.5 cm', 100, 1, 1),
	(N'Tớ Là CEO Nhí 2 - Tập 4 - Học Cách Tính Toán Chi Tiêu - Cùng Bé Khám Phá Kĩ Năng Quản Lí Tài Chính', N'True Fruit', N'Trần Thiên Tú', N'Nhà Xuất Bản Kim Đồng', 2022, 27000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244873467.jpg', '8935244873467', N'Kiến thức - Kỹ năng', 32, N'20.5 x 19 x 0.3 cm', 70, 1, 1),
	(N'Hiểu Về Tài Chính, Vững Bước Tương Lai - Học Cách Cho Đi', N'Cecilia Minden', N'Phạm Nguyên Trường', N'Nhà Xuất Bản Kim Đồng', 2021, 21000, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244374.jpg', '8935244859362', N'Kiến thức - Kỹ năng', 32, N'20.5 x 14.5 x 0.3 cm', 56, 1, 1),
	(N'Tớ Là CEO Nhí 2 - Tập 2 - Làm Thế Nào Để Kiếm Tiền Nhỉ? - Cùng Bé Khám Phá Kĩ Năng Quản Lí Tài Chính', N'True Fruit', N'Trần Thiên Tú', N'Nhà Xuất Bản Kim Đồng', 2022, 27000, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244873443.jpg', '8935244873443', N'Kiến thức - Kỹ năng', 32, N'20.5 x 19 x 0.3 cm', 70, 1, 1);



	-- Insert random categories
	INSERT INTO Category(catName, catDescription, catStatus)
	VALUES
	('Light Novel', 'Light novels from various authors', 1),
	('History', 'Books about history and traditions', 1),
	('Science', 'Scientific books for enthusiasts', 1),
	('Self-help', 'Books on self-improvement', 1),
	('Kids', 'Books for children', 1),
	('Fantasy', 'Fantasy stories', 1),
	('Romance', 'Romantic stories and novels', 1),
	('Mystery', 'Mystery and thriller books', 1),
	('Technology', 'Books on technology and innovation', 1),
	('Education', 'Educational books for students', 1);

	-- Randomly assign categories to books
	DECLARE @BookCount INT
	DECLARE @CategoryCount INT
	DECLARE @BookID INT
	DECLARE @CategoryID INT

	-- Get the count of books and categories
	SET @BookCount = (SELECT COUNT(*) FROM Book)
	SET @CategoryCount = (SELECT COUNT(*) FROM Category)

	-- Loop through each book and assign random categories
	WHILE @BookCount > 0
	BEGIN
		-- Randomly select a book and category
		SET @BookID = FLOOR(RAND() * @BookCount) + 1
		SET @CategoryID = FLOOR(RAND() * @CategoryCount) + 1

		-- Insert the mapping into Book_Category
		INSERT INTO Book_Category (book_id, cat_id)
		VALUES (@BookID, @CategoryID)

		-- Decrement the book count to prevent infinite loop
		SET @BookCount = @BookCount - 1
	END

	/*******************************************************************************
	   Create procedure to edit book info
	*******************************************************************************/
	GO
	CREATE PROCEDURE editBookInfo
	AS
	BEGIN
		DECLARE @BookID INT
		DECLARE @RandomPrice DECIMAL(10, 2)
		DECLARE @RandomQuantity INT

		-- Cursor để duyệt qua từng sách
		DECLARE book_cursor CURSOR FOR 
		SELECT bookID FROM Book

		OPEN book_cursor
		FETCH NEXT FROM book_cursor INTO @BookID

		WHILE @@FETCH_STATUS = 0
		BEGIN
			-- Tạo giá ngẫu nhiên từ 20,000 đến 1,000,000
			SET @RandomPrice = CAST((2000000 + ABS(CHECKSUM(NEWID())) % 980000) / 100.0 AS DECIMAL(10))
        
			-- Tạo số lượng ngẫu nhiên từ 1 đến 100
			SET @RandomQuantity = ABS(CHECKSUM(NEWID())) % 100 + 1

			-- Cập nhật thông tin sách
			UPDATE Book
			SET bookPrice = @RandomPrice,
				bookQuantity = @RandomQuantity
			WHERE bookID = @BookID

			FETCH NEXT FROM book_cursor INTO @BookID
		END

		CLOSE book_cursor
		DEALLOCATE book_cursor
	END
	GO

	EXEC editBookInfo;

	/******************************************************************************* 
	   Insert Import Stock and Import Stock Details
	*******************************************************************************/
	-- Tạo nhà cung cấp mẫu (nếu chưa có)
	DECLARE @SupplierCount INT = 5;

	WHILE @SupplierCount > 0
	BEGIN
		IF NOT EXISTS (SELECT 1 FROM Supplier WHERE supName = 'Supplier ' + CAST(@SupplierCount AS VARCHAR(10)))
		BEGIN
			INSERT INTO Supplier (supName, supEmail, supPhone, supAddress, supStatus)
			VALUES (
				'Supplier ' + CAST(@SupplierCount AS VARCHAR(10)), 
				'supplier' + CAST(@SupplierCount AS VARCHAR(10)) + '@example.com', 
				'012345678' + CAST(@SupplierCount AS VARCHAR(1)), 
				'Address ' + CAST(@SupplierCount AS VARCHAR(10)), 
				1
			);
		END;
		SET @SupplierCount = @SupplierCount - 1;
	END;

	-- Tạo 5 nhập kho
	DECLARE @SupplierID INT;
	DECLARE @StaffID INT;
	DECLARE @ImportDate DATE;
	DECLARE @NewISID INT;

	SET @StaffID = (SELECT TOP 1 staffID FROM Staff ORDER BY NEWID()); -- Nhân viên ngẫu nhiên

	DECLARE @BookIDs_Import TABLE (BookID INT); -- Bảng tạm cho sách

	-- Lấy ID của 5 sách ngẫu nhiên
	INSERT INTO @BookIDs_Import (BookID)
	SELECT TOP 5 bookID FROM Book ORDER BY NEWID();

	DECLARE @BookID_Import INT; -- Biến cho nhập kho

	-- Duyệt qua từng sách và tạo nhập kho
	DECLARE book_cursor_import CURSOR FOR SELECT BookID FROM @BookIDs_Import;
	OPEN book_cursor_import;

	FETCH NEXT FROM book_cursor_import INTO @BookID_Import;
	WHILE @@FETCH_STATUS = 0
	BEGIN
		SET @SupplierID = (SELECT TOP 1 supID FROM Supplier ORDER BY NEWID()); -- Nhà cung cấp ngẫu nhiên
		SET @ImportDate = DATEADD(DAY, -ABS(CHECKSUM(NEWID()) % 30), GETDATE()); -- Ngày nhập ngẫu nhiên

		-- Tạo nhập kho
		INSERT INTO ImportStock (supID, importDate, ISStatus, staffID)
		VALUES (@SupplierID, @ImportDate, 1, @StaffID);

		SET @NewISID = SCOPE_IDENTITY();

		-- Thêm chi tiết nhập kho
		INSERT INTO ImportStockDetail (bookID, ISID, ISDQuantity, importPrice)
		VALUES (
			@BookID_Import, 
			@NewISID, 
			ABS(CHECKSUM(NEWID()) % 50) + 1, -- Số lượng ngẫu nhiên từ 1 đến 50
			CAST((10000 + ABS(CHECKSUM(NEWID()) % 500000)) AS DECIMAL(10, 2)) -- Giá ngẫu nhiên từ 10,000 đến 500,000
		);

		FETCH NEXT FROM book_cursor_import INTO @BookID_Import;
	END;

	CLOSE book_cursor_import;
	DEALLOCATE book_cursor_import;

	-- Tạo đơn hàng ngẫu nhiên cho tất cả các khách hàng
	DECLARE @CustomerID NVARCHAR(255);
	DECLARE @OrderID INT;
	DECLARE @BookID_Order INT;
	DECLARE @OrderDate DATE;
	DECLARE @Quantity INT;
	DECLARE @BookPrice DECIMAL(10, 2);
	DECLARE @TotalPrice DECIMAL(10, 2);
	DECLARE @OrderStatus INT;

	-- Lấy danh sách khách hàng
	DECLARE customer_cursor CURSOR FOR
	SELECT username FROM Account WHERE [role] = 1;

	OPEN customer_cursor;
	FETCH NEXT FROM customer_cursor INTO @CustomerID;

	WHILE @@FETCH_STATUS = 0
	BEGIN
		-- Tạo từ 2 đến 3 đơn hàng cho mỗi khách hàng
		DECLARE @OrderCount INT = ABS(CHECKSUM(NEWID())) % 2 + 2; -- 2 đến 3 đơn hàng

		WHILE @OrderCount > 0
		BEGIN
			-- Tạo ngày đặt hàng ngẫu nhiên trong 30 ngày qua
			SET @OrderDate = DATEADD(DAY, -ABS(CHECKSUM(NEWID()) % 30), GETDATE());

			-- Chọn trạng thái đơn hàng ngẫu nhiên
			SET @OrderStatus = ABS(CHECKSUM(NEWID())) % 5; -- Từ 0 đến 4

			-- Thêm đơn hàng
			INSERT INTO [Order] (proID, username, staffID, orderDate, orderAddress, orderStatus)
			VALUES (
				NULL, 
				@CustomerID, 
				(SELECT TOP 1 staffID FROM Staff ORDER BY NEWID()), 
				@OrderDate, 
				'Random Address', 
				@OrderStatus
			);

			SET @OrderID = SCOPE_IDENTITY();

			-- Tạo từ 1 đến 3 chi tiết đơn hàng cho mỗi đơn hàng
			DECLARE @OrderDetailCount INT = ABS(CHECKSUM(NEWID())) % 3 + 1; -- 1 đến 3 chi tiết đơn hàng

			WHILE @OrderDetailCount > 0
			BEGIN
				-- Lấy ngẫu nhiên một sách
				SET @BookID_Order = (SELECT TOP 1 bookID FROM Book ORDER BY NEWID());

				-- Lấy giá sách
				SET @BookPrice = (SELECT bookPrice FROM Book WHERE bookID = @BookID_Order);

				-- Tạo số lượng ngẫu nhiên từ 1 đến 5
				SET @Quantity = ABS(CHECKSUM(NEWID())) % 5 + 1;

				-- Tính tổng giá
				SET @TotalPrice = @Quantity * @BookPrice;

				-- Thêm chi tiết đơn hàng
				INSERT INTO OrderDetail (bookID, orderID, quantity, totalPrice)
				VALUES (@BookID_Order, @OrderID, @Quantity, @TotalPrice);

				SET @OrderDetailCount = @OrderDetailCount - 1;
			END

			SET @OrderCount = @OrderCount - 1;
		END

		FETCH NEXT FROM customer_cursor INTO @CustomerID;
	END

	CLOSE customer_cursor;
	DEALLOCATE customer_cursor;
	go 


	-- Tạo stored procedure để cập nhật ISBN ngẫu nhiên cho sách

	CREATE OR ALTER PROCEDURE UpdateRandomISBN
	AS
	BEGIN
		-- Biến để lưu trữ thông tin
		DECLARE @BookID INT;
		DECLARE @RandomISBN VARCHAR(10);

		-- Cursor để duyệt qua từng sách có ISBN là NULL hoặc rỗng
		DECLARE book_cursor CURSOR FOR
		SELECT bookID
		FROM Book
		WHERE LEN(isbn) != 10 OR isbn IS NULL OR isbn = '';

		OPEN book_cursor;
		FETCH NEXT FROM book_cursor INTO @BookID;

		WHILE @@FETCH_STATUS = 0
		BEGIN
			-- Sinh ISBN ngẫu nhiên và đảm bảo không trùng lặp
			WHILE 1 = 1
			BEGIN
				SET @RandomISBN = CAST(ABS(CHECKSUM(NEWID())) % 10000000000 AS VARCHAR(10));
				-- Đảm bảo độ dài là 10 chữ số
				SET @RandomISBN = RIGHT('0000000000' + @RandomISBN, 10);
				IF NOT EXISTS (SELECT 1 FROM Book WHERE isbn = @RandomISBN)
				BEGIN
					BREAK;
				END
			END

			-- Cập nhật ISBN cho sách
			UPDATE Book
			SET isbn = @RandomISBN
			WHERE bookID = @BookID;

			-- Tiếp tục với sách tiếp theo
			FETCH NEXT FROM book_cursor INTO @BookID;
		END

		CLOSE book_cursor;
		DEALLOCATE book_cursor;
	END;
	GO

	-- Gọi stored procedure sau khi chèn sách
	EXEC UpdateRandomISBN;


