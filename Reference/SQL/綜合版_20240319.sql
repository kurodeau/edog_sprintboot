-- 如果不存在edog資料庫, 則創建並使用之 
CREATE DATABASE IF NOT EXISTS edog;
USE edog;

-- 賣家等級  創建table
CREATE TABLE IF NOT EXISTS sellerLv (
    sellerLvId INT AUTO_INCREMENT primary key,
    lvName VARCHAR(10),
    platformCommission DECIMAL(3,2),
    adAllowType INT,
    isExportGoldflow BOOLEAN,
    freightSub INT,
    returnSubPerMonth INT,
    isShowPriority BOOLEAN,
    shelvesNumber INT
);
-- 賣家  創建table
CREATE TABLE IF NOT EXISTS seller (
    sellerId INT AUTO_INCREMENT primary key,
    sellerLvId INT,
    sellerEmail VARCHAR(200),
    sellerCompany VARCHAR(200),
    sellerTaxId CHAR(8),
    sellerCapital INT,
    sellerContact VARCHAR(50),
    sellerCompanyPhone VARCHAR(10),
    sellerCompanyExtension VARCHAR(10),
    sellerMobile VARCHAR(10),

    sellerCounty VARCHAR(5),
    sellerDistrict VARCHAR(5),

    sellerAddress VARCHAR(100),
    sellerPassword VARCHAR(100),
    sellerBankAccount VARCHAR(100),
    sellerBankCode VARCHAR(3),
    sellerBankAccountNumber VARCHAR(100),
    sellerCreateTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    isConfirm BOOLEAN,
	CONSTRAINT sellerLvId FOREIGN KEY (sellerLvId) REFERENCES sellerLv(sellerLvId)
);
-- 會員資料  創建table-- 
CREATE TABLE IF NOT EXISTS buyer(
    memberId INT AUTO_INCREMENT PRIMARY KEY,
    memberEmail VARCHAR(200),
    thirdFrom VARCHAR(100),
    memberName VARCHAR(100),
    memberPhone VARCHAR(10),
    memberMobile VARCHAR(20),
    memberBirthday DATE,
    memberPassword VARCHAR(100),
    memberAddress VARCHAR(100),
    isMemberEmail BOOLEAN,
    memberRegistrationTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    petName VARCHAR(100),
    petImg LONGBLOB,
    petImgUploadTime DATETIME,
    petVaccName1 VARCHAR(100),
    petVaccTime1 DATETIME,
    petVaccName2 VARCHAR(100),
    petVaccTime2 DATETIME,
    isConfirm BOOLEAN DEFAULT TRUE,
    logitude DOUBLE,
    latitude DOUBLE
);
-- 檢舉類型  創建table-- 
create table IF NOT EXISTS reportType(
reportTypeId int primary key,
reportTypeSort varchar(100)
);
-- 優惠券  創建table-- 
CREATE TABLE IF NOT EXISTS coupon (
    couponId INT AUTO_INCREMENT primary key,
    couponName VARCHAR(100),
    couponCode VARCHAR(20),
    startTime DATETIME,
    endTime DATETIME,
    minSpendingAmount INT,
    couponQuantity INT,
    memberAllowQuantity INT,
    couponDiscount INT,
    couponCreateTime DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- 搜尋關鍵字  創建table-- 
Create Table IF NOT EXISTS searchKeyWords(
searchKeyWordsId int AUTO_INCREMENT primary key ,
keyword varchar(100) ,
sort int ,
startTime datetime, 
endTime datetime,
isDisplay boolean
);
-- 通知  創建table-- 
Create Table IF NOT EXISTS notify(
notifyId int AUTO_INCREMENT primary key ,
notifyType int ,
notifyContent varchar(100),
notifyTime datetime, 
notifyState varchar(10)
);
-- 後臺管理者  創建table-- 
Create Table IF NOT EXISTS manager(
managerId int AUTO_INCREMENT primary key ,
managerEmail varchar(100) ,
managerPassword varchar(100) ,
managerPer int ,
managerCreatetime datetime DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS ad (
adid int AUTO_INCREMENT primary KEY,
sellerId int,
constraint fk_seller_sellerID
foreign key(sellerID) references seller(sellerID),
adimg longblob,
adImgUploadTime datetime,
adName varchar(20),
adUrl varchar(100),
adStartTime date ,
adEndTime date  ,
adLv int,
adMemo varchar(100),
adstatus varchar(50),
adCreateTime datetime DEFAULT CURRENT_TIMESTAMP,
isEnabled boolean
);
-- 廣告審核  創建table-- 
Create Table IF NOT EXISTS adConfirm(
adConfirmId int AUTO_INCREMENT primary key ,
adid int ,
constraint fk_ad_adid
foreign key(adid) references ad(adid),
failReason varchar(100) ,
confirmTime datetime ,
reviewStatus boolean 
);
-- 跑馬燈  創建table-- 
Create Table IF NOT EXISTS newsTicker(
newsTickerId int AUTO_INCREMENT primary key ,
newsTickerContent varchar(100) ,
sort int ,
startTime datetime, 
endTime datetime,
isDisplay boolean,
createTime datetime DEFAULT CURRENT_TIMESTAMP
);
-- 匯款明細  創建table-- 
CREATE TABLE IF NOT EXISTS remittance (
    remittanceId INT AUTO_INCREMENT PRIMARY KEY,
    sellerId INT,
    remittanceEstimatedTime DATETIME,
    remittanceTime DATETIME,
    settlementTime DATETIME,
    turnover INT,
    handlingFee INT,
    remittanceAmount INT,
    remittanceStatus INT,
    explanation VARCHAR(100),
    FOREIGN KEY (sellerId) REFERENCES seller(sellerId) 
);
-- 寵物抽卡  創建table-- 
CREATE TABLE IF NOT EXISTS petdraw (
    petdrawId int PRIMARY KEY AUTO_INCREMENT,
    memberId INT,
    memberpairId INT,
    ismemberlike BOOLEAN,
    memberrestime DATETIME,
    memberpairrestime DATETIME,
    ismemberpairlike BOOLEAN,
    petdrawtime DATETIME DEFAULT CURRENT_TIMESTAMP,
    petdrawlog DOUBLE,
    petdrawlat DOUBLE,
    CONSTRAINT fk_member FOREIGN KEY (memberId) REFERENCES buyer(memberId),
    CONSTRAINT fk_member_pair FOREIGN KEY (memberpairId) REFERENCES buyer(memberId)

);
-- BS聊天室  創建table-- 
Create Table IF NOT EXISTS sellChatRoom(
sellChatRoomId int AUTO_INCREMENT primary key ,
buyerId int ,
sellerId int ,
createTime datetime DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_buyerId FOREIGN KEY (buyerId) REFERENCES buyer(memberId),
CONSTRAINT fk_sellerId FOREIGN KEY (buyerId) REFERENCES seller(sellerId)
);
-- BB聊天室  創建table-- 
Create Table IF NOT EXISTS chatRoom(
chatRoomId int AUTO_INCREMENT primary key ,
buyer1Id int ,
buyer2Id int ,
createTime datetime DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_buyer1Id FOREIGN KEY (buyer1Id) REFERENCES buyer(memberId),
CONSTRAINT fk_buyer2Id FOREIGN KEY (buyer2Id) REFERENCES buyer(memberId)
);
-- BB聊天  創建table-- 
create table IF NOT EXISTS chat(
chatId int AUTO_INCREMENT primary key,
chatRoomId int,
sendPart int,
message varchar(100),
image BLOB,
sendTime datetime DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_chatRoomId FOREIGN KEY (chatRoomId) REFERENCES chatRoom(chatRoomId)
);
-- BS聊天  創建table-- 
create table IF NOT EXISTS sellChat(
sellChatId int AUTO_INCREMENT primary key,
sellChatRoomId int,
sendPart int,
message varchar(100),
image BLOB,
sendTime datetime DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_sellChatRoomId FOREIGN KEY (sellChatRoomId) REFERENCES sellChatRoom(sellChatRoomId)
);
-- 黑名單  創建table-- 
create table IF NOT EXISTS blackList(
blackListId int primary key AUTO_INCREMENT,
memberId int,
foreign key (memberId) references buyer(memberId),
memberBlockId int,
foreign key (memberBlockId) references buyer(memberId),
blackListTime datetime DEFAULT CURRENT_TIMESTAMP
);

-- 文章檢舉類型  創建table-- 
CREATE TABLE IF NOT EXISTS articleType(
articleTypeId Integer primary key AUTO_INCREMENT,
articleTypeName varchar(100)
);
-- 文章  創建table-- 
CREATE TABLE IF NOT EXISTS article(
    articleId INT PRIMARY KEY AUTO_INCREMENT,
    memberId INT,
    articleTitle VARCHAR(255),
    articleContent VARCHAR(500),
    upFiles LONGBLOB,
    artCreateTime DATETIME,
    artUpdateTime DATETIME,
    articleLike INT,
    articleComment INT,
    articleSort INT,
    isEnabled BOOLEAN,
    FOREIGN KEY (memberId) REFERENCES buyer(memberId),
    FOREIGN KEY (articleSort) REFERENCES articleType(articleTypeId)
);

-- 文章喜歡  創建table-- 
CREATE TABLE IF NOT EXISTS articleLike(
articleLikeId Int primary key AUTO_INCREMENT,
memberId Int,
articleId Int,
articleLikeListTime datetime DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (memberId) REFERENCES buyer(memberId),
FOREIGN KEY (articleId) REFERENCES article(articleId)
);

-- 商品分類  創建table-- 
CREATE TABLE IF NOT EXISTS productSort (
    productSortId INT AUTO_INCREMENT PRIMARY KEY,
    productSortNo INT unique key,
    productSortName VARCHAR(10),
    productCategory VARCHAR(50),
    isEnabled BOOLEAN
);


-- 商品  創建table-- 
CREATE TABLE IF NOT EXISTS product (
    productId INT AUTO_INCREMENT PRIMARY KEY,
    sellerId INT,
    productCoverImg LONGBLOB,
    productName VARCHAR(100),
    price INT,
    productStockQuantity INT,
    productSoldQuantity INT DEFAULT 0,
    productDetails VARCHAR(500),
    productStatus VARCHAR(10),
    productCreationTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    animalType VARCHAR(10),
    ratings INT,
    totalReviews INT,
    productSortNo INT,
    isEnabled BOOLEAN,
    CONSTRAINT fk_seller FOREIGN KEY (sellerId) REFERENCES seller(sellerId),
    CONSTRAINT fk_productSort FOREIGN KEY (productSortNo) REFERENCES productSort(productSortNo)
);
-- 優惠券使用  創建table-- 
CREATE TABLE IF NOT EXISTS couponUsed (
    couponUseId INT AUTO_INCREMENT primary key,
    couponId INT,
    usedId INT,
    useTime DATETIME,
    CONSTRAINT fk_couponId FOREIGN KEY (couponId) REFERENCES coupon(couponId),
    CONSTRAINT fk_userid FOREIGN KEY (usedId) REFERENCES buyer(memberId)
);
-- 留言  創建table-- 
create table IF NOT EXISTS reply(
replyId int primary key AUTO_INCREMENT,
articleId int,
FOREIGN KEY (articleId) REFERENCES article(articleId),
memberId int,
FOREIGN KEY (memberId) REFERENCES buyer(memberId),
replyContent varchar(100),
replyTime Datetime DEFAULT CURRENT_TIMESTAMP,
replyLike int,
isEnabled boolean
 );
-- 留言喜歡  創建table-- 
create table IF NOT EXISTS replyLike(
replyLikeId int primary key AUTO_INCREMENT,
memberId int,
FOREIGN KEY (memberId) REFERENCES buyer(memberId),
replyId int,
FOREIGN KEY (replyId) REFERENCES reply(replyId),
replyLikeTime datetime DEFAULT CURRENT_TIMESTAMP
 );

-- 商品圖片  創建table-- 
CREATE TABLE IF NOT EXISTS productImage (
    productImgId INT AUTO_INCREMENT PRIMARY KEY,
    productId INT,
    productImg LONGBLOB,
    productImgTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    isCover BOOLEAN,
    isEnabled BOOLEAN,
    CONSTRAINT fk_product FOREIGN KEY (productId) REFERENCES product(productId)
);
-- 收藏  創建table-- 
create table IF NOT EXISTS collection (
collectionId int primary key AUTO_INCREMENT,
productId int,
constraint fk_product_productId
foreign key(productID) references product(productID),
memberId int,
constraint fk_buyer_memberId
foreign key(memberId) references buyer(memberId),
isEnabled boolean ,
collectionTime datetime DEFAULT CURRENT_TIMESTAMP
);
-- 訂單  創建table-- 
CREATE TABLE IF NOT EXISTS productOrder (
    orderId INT PRIMARY KEY AUTO_INCREMENT,
    sellerId INT,
    memberId INT,
    couponId int,
    memberPaysShipping INT,
    sellerPaysShipping INT,
    orderOrigPrice INT,
    actualPay INT,
    orderTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    orderStatus INT,
    invoiceNumber INT,
    receiver VARCHAR(100),
    receiveTime DATETIME,
    mobile VARCHAR(100),
    contactAddress VARCHAR(100),
    isDelivered BOOLEAN,
    shippingTime DATETIME,
    FOREIGN KEY (sellerId) REFERENCES seller(sellerId), 
    FOREIGN KEY (memberId) REFERENCES buyer(memberId), 
    FOREIGN KEY (couponId) REFERENCES coupon(couponId) 
);
-- 訂單明細  創建table -- 
CREATE TABLE IF NOT EXISTS orderDetails (

    orderDetailsId INT PRIMARY KEY AUTO_INCREMENT,
    orderId INT,
    productId INT,
    purchaseQuantity INT,
    isCommented BOOLEAN,
    stars INT,
    commentedTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    comments VARCHAR(200),
    attachments VARCHAR(200),
    isEnable BOOLEAN,
    FOREIGN KEY (orderId) REFERENCES productOrder (orderId), 
    FOREIGN KEY (productId) REFERENCES product (productId)
);
-- 檢舉  創建table-- 
CREATE TABLE IF NOT EXISTS report(
reportId Int primary key AUTO_INCREMENT,
reportMemberId Int,

reportTargetType INT,
replyId Int,
articleId Int,
reportTypeId Int,
reportTime datetime DEFAULT CURRENT_TIMESTAMP,
reportState Int,
reportDealTime datetime,
FOREIGN KEY (reportMemberId) REFERENCES buyer(memberId),
FOREIGN KEY (replyId) REFERENCES reply(replyId),
FOREIGN KEY (articleId) REFERENCES article(articleId),
FOREIGN KEY (reportTypeId) REFERENCES reportType(reportTypeId)
);

-- 通知分類  創建table-- 
create table IF NOT EXISTS msgType(
msgTypeId int primary key AUTO_INCREMENT,
msgTypeContent varchar(100)
);
-- 通知  創建table-- 
create table IF NOT EXISTS msg(
msgId int primary key AUTO_INCREMENT,
memberId int,
foreign key(memberId) references buyer(memberId),
articleId int,
foreign key(articleId) references article(articleId),
replyId int,
foreign key(replyId) references reply(replyId),
reportId int,
foreign key(reportId) references report(reportId),
petdrawId int,
foreign key(petdrawId) references petdraw(petdrawId),
msgTypeId int,
foreign key(msgTypeId) references msgType(msgTypeId),
msgTime datetime DEFAULT CURRENT_TIMESTAMP,
isRead boolean,
isEnabled boolean
);


-- 賣家等級  放入測試資料
INSERT INTO sellerLv (lvName, platformCommission, adAllowType, isExportGoldflow, freightSub, returnSubPerMonth, isShowPriority, shelvesNumber)
VALUES 
  ('免費', 0.05, 1, false, 10, 2, false, 50),
  ('專業', 0.08, 2, true, 20, 3, false, 100),
  ('企業', 0.1, 3, true, 30, 4, true, 150);
-- 賣家表
INSERT INTO seller (sellerLvId, sellerEmail, sellerCompany, sellerTaxId, sellerCapital, sellerContact, sellerCompanyPhone, sellerCompanyExtension, sellerMobile, sellerCounty, sellerDistrict, sellerAddress, sellerPassword, sellerBankAccount, sellerBankCode, sellerBankAccountNumber, isConfirm)
VALUES 
    (1, 'seller1@example.com', 'ABC Company', 'A1234567', 500000, 'John Doe', '0223456789', '123', '0912345678', '台北市', '中正區', 'XX巷 YY路 1號', 'Password1', '123-456-789', '012', '98765432', true),
    (2, 'seller2@example.com', '未審核通過id2', 'B2345678', 700000, 'Jane Smith', '0223456789', '456', '0923456789', '台北市', '大安區', 'XX巷 YY路 2號', 'Password2', '234-567-890', '345', '87654321', false),
    (3, 'seller3@example.com', '未審核通過id3', 'C3456789', 900000, 'Bob Johnson', '0223456789', '789', '0934567890', '台北市', '中正區', 'XX巷 YY路 3號', 'Password3', '345-678-901', '456', '76543210', false),
    (1, 'seller4@example.com', 'PQR Industries', 'D4567890', 1200000, 'Alice Lee', '0223456789', '012', '0945678901', '桃園市', '中壢區', 'XX巷 YY路 4號', 'Password4', '456-789-012', '567', '65432109', true),
    (2, 'seller5@example.com', 'JKL Enterprises', 'E5678901', 1500000, 'Charlie Chan', '0223456789', '345', '0956789012', '新竹市', '東區', 'XX巷 YY路 5號', 'Password5', '567-890-123', '678', '54321098', true),
    (2, 'TestSeller@gmail.com', 'JKL Enterprises', 'E5678901', 1500000, 'Charlie Chan', '0223456789', '345', '0956789012', '新竹市', '東區', 'XX巷 YY路 5號', '$2a$10$lZ/wmFXgzSJECGth9qaQ6OZXlofbd/GGRSGS0BRU/ifk282RKmcRS', '567-890-123', '678', '54321098', true);
-- 插入修改過的假資料到 seller 表(沒有時間戳記)
INSERT INTO seller (sellerLvId, sellerEmail, sellerCompany, sellerTaxId, sellerCapital, sellerContact, sellerCompanyPhone, sellerCompanyExtension, sellerMobile, sellerCounty, sellerDistrict, sellerAddress, sellerPassword, sellerBankAccount, sellerBankCode, sellerBankAccountNumber, isConfirm)
VALUES 
    (1, 'seller6@example.com', 'DEF Ltd.', 'F6789012', 1800000, 'David Wang', '0223456789', '678', '0967890123', '台南市', '中西區', 'XX巷 YY路 6號', 'Password6', '678901234', '789', '43210987', true),
    (2, 'seller7@example.com', 'GHI Corp.', 'G7890123', 2000000, 'Eva Chen', '0223456789', '901', '0978901234', '高雄市', '前鎮區', 'XX巷 YY路 7號', 'Password7', '789012345', '890', '32109876', true),
    (3, 'seller8@example.com', 'ABC Company', 'H8901234', 2200000, 'Frank Lin', '0223456789', '123', '0989012345', '台中市', '北區', 'XX巷 YY路 8號', 'Password8', '890123456', '901', '21098765', true),
    (2, 'seller9@example.com', 'JKL Enterprises', 'I9012345', 2500000, 'Grace Wu', '0223456789', '234', '0990123456', '台北市', '萬華區', 'XX巷 YY路 9號', 'Password9', '901234567', '012', '10987654', true),
    (1, 'seller10@example.com', 'PQR Industries', 'J0123456', 2800000, 'Henry Chang', '0223456789', '345', '0911234567', '新北市', '三峽區', 'XX巷 YY路 10號', 'Password10', '012345678', '123', '09876543', true);


-- 會員資料  放入測試資料-- 
INSERT INTO buyer (
    memberEmail, thirdFrom, memberName, memberPhone, memberMobile, memberBirthday,
    memberPassword, memberAddress, isMemberEmail, memberRegistrationTime, petName, petImg,
    petImgUploadTime, petVaccName1, petVaccTime1, petVaccName2, petVaccTime2, logitude, latitude
) VALUES 
    ('buyer1@example.com', NULL, 'Buyer1', '1234567890', '0912345678', '1990-01-01', 'password1', 'Address1', true, '2023-01-01 10:00:00', 'Pet1', NULL, NULL, 'Vaccine1', '2023-02-01 12:30:00', 'Vaccine2', '2023-03-15 15:45:00', 121.5654, 25.0330),
    ('buyer2@example.com', NULL, 'David', '0987654321', '0923456789', '1985-05-15', 'password2', 'Address2', true, '2023-02-01 11:30:00', '木木鴞', NULL, NULL, 'Vaccine3', '2023-04-05 09:15:00', 'Vaccine4', '2023-06-20 14:00:00', 121.5654, 25.0330),
    ('buyer3@example.com', NULL, 'Buyer3', '0912345678', '0934567890', '1995-08-20', 'password3', 'Address3', true, '2023-03-01 14:45:00', 'Pet3', NULL, NULL, 'Vaccine5', '2023-07-10 16:30:00', 'Vaccine6', '2023-09-25 11:45:00', 121.5654, 25.0330),
    ('buyer4@example.com', NULL, 'Buyer4', '0923456789', '0945678901', '1988-11-10', 'password4', 'Address4', true, '2023-04-01 09:15:00', 'Pet4', NULL, NULL, 'Vaccine7', '2023-11-01 08:00:00', 'Vaccine8', '2023-12-15 10:20:00', 120.6736, 24.1477),
    ('buyer5@example.com', NULL, 'Buyer5', '0934567890', '0956789012', '1992-03-25', 'password5', 'Address5', true, '2023-05-01 12:00:00', 'Pet5', NULL, NULL, 'Vaccine9', '2024-01-05 13:45:00', 'Vaccine10', '2024-03-20 17:00:00', 120.3014, 22.6273),
    ('buyer6@example.com', NULL, 'Buyer6', '0945678901', '0967890123', '1980-06-30', 'password6', 'Address6', true, '2023-06-01 15:30:00', 'Pet6', NULL, NULL, 'Vaccine11', '2024-04-10 09:30:00', 'Vaccine12', '2024-06-25 14:15:00', 120.6582, 24.1632), -- 假设台中
    ('buyer7@example.com', NULL, 'Buyer7', '0956789012', '0978901234', '1998-09-05', 'password7', 'Address7', true, '2023-07-01 18:45:00', 'Pet7', NULL, NULL, 'Vaccine13', '2024-07-20 11:00:00', 'Vaccine14', '2024-09-05 15:30:00', 120.6582, 24.1632), -- 假设台中
    ('buyer8@example.com', NULL, 'Buyer8', '0967890123', '0989012345', '1983-12-15', 'password8', 'Address8', true, '2023-08-01 21:00:00', 'Pet8', NULL, NULL, 'Vaccine15', '2024-10-01 14:45:00', 'Vaccine16', '2024-12-15 18:00:00', 120.3417, 22.6300), -- 假设高雄
    ('buyer9@example.com', NULL, 'Buyer9', '0978901234', '0990123456', '1991-02-20', 'password9', 'Address9', true, '2023-09-01 09:30:00', 'Pet9', NULL, NULL, 'Vaccine17', '2025-01-15 17:15:00', 'Vaccine18', '2025-03-30 20:30:00', 121.5402, 25.0478), -- 假设台北
    ('buyer10@example.com', NULL, 'Buyer10', '0989012345', '0910234567', '1987-05-10', 'password10', 'Address10', true, '2023-10-01 12:45:00', 'Pet10', NULL, NULL, 'Vaccine19', '2025-04-20 10:00:00', 'Vaccine20', '2025-06-05 13:15:00', 121.5402, 25.0478), -- 假设台北
	('TestBuyer@example.com', NULL, 'Buyer10', '0989012345', '0910234567', '1987-05-10', '$2a$10$XfhKuTNknEELCPZuildBneQ65VeXiFD8SK1NHsPFxm52MaOo5XcQm', 'Address10', true, '2023-10-01 12:45:00', 'Pet10', NULL, NULL, 'Vaccine19', '2025-04-20 10:00:00', 'Vaccine20', '2025-06-05 13:15:00', 121.5402, 25.0478);

-- 檢舉類型  放入測試資料-- (檢舉資料的測試資料只能新增一次, 有需要自己開起來新增)
-- INSERT INTO reportType (reportTypeId, reportTypeSort)
-- VALUES 
--     (1, '廣告內容不實'),
--     (2, '商品資訊不正確'),
--     (3, '交易詐欺'),
--     (4, '騷擾言論'),
--     (5, '侵犯智慧財產權'),
--     (6, '不當內容'),
--     (7, '不當連結'),
--     (8, '冒名詐騙'),
--     (9, '違規內容'),
--     (10, '其他');
-- 優惠券  放入測試資料-- 
INSERT INTO coupon (
    couponName, couponCode, startTime, endTime,
    minSpendingAmount, couponQuantity, memberAllowQuantity,
    couponDiscount, couponCreateTime
)
VALUES 
    ('優惠券1', 'CODE1', '2023-11-01 00:00:00', '2024-11-30 23:59:59', 1000, 50, 1, 10, '2023-10-01 10:00:00'),
    ('時間已過期id2', 'CODE2', '2023-12-01 00:00:00', '2023-12-31 23:59:59', 1500, 30, 2, 15, '2023-11-01 11:00:00'),
    ('時間不上架id3', 'CODE3', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 2000, 20, 3, 20, '2023-12-01 12:00:00'),
    ('優惠券4', 'CODE4', '2023-05-01 00:00:00', '2024-05-31 23:59:59', 2500, 10, 4, 25, '2023-04-01 13:00:00'),
    ('優惠券5', 'CODE5', '2023-10-01 00:00:00', '2024-10-31 23:59:59', 3000, 5, 5, 30, '2023-09-01 14:00:00');
-- 搜尋關鍵字  放入測試資料-- 
INSERT INTO searchKeyWords (keyword, sort, startTime, endTime, isDisplay)
VALUES 
  ('時間內正常顯示', 1, NOW(), DATE('2024-12-13 00:00:00'), true),
  ('時間內但不顯示', 2, NOW(), DATE('2024-12-13 00:00:00'), false),
  ('時間已經過了不會看到', 3, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), true),
  ('時間不會到,不會看到', 4, DATE('2024-12-13 00:00:00'), DATE_ADD(DATE('2024-12-13 00:00:00'), INTERVAL 30 DAY), true),
  ('時間內正常顯示,但id5排序10', 10, NOW(), DATE('2024-12-13 00:00:00'), true),
  ('Maintenance Alert: System Upgrade', 6, NOW(), DATE_ADD(NOW(), INTERVAL 10 DAY), true),
  ('Employee of the Month: John Doe', 7, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), true),
  ('Limited-time Offer: Free Shipping', 8, NOW(), DATE_ADD(NOW(), INTERVAL 20 DAY), true),
  ('Community Event: Charity Run', 9, NOW(), DATE_ADD(NOW(), INTERVAL 40 DAY), true),
  ('時間內正常顯示,但id10排序5', 5, NOW(), DATE('2024-12-13 00:00:00'), true),
  ('Tech Gadgets Sale', 11, NOW(), DATE_ADD(NOW(), INTERVAL 180 DAY), false), -- 不會上架的資料
  ('Flash Sale: Limited Time', 12, NOW(), DATE_ADD(NOW(), INTERVAL 180 DAY), true); -- 已經下架的資料
  -- 通知  放入測試資料-- 
INSERT INTO notify (notifyType, notifyContent, notifyTime, notifyState)
VALUES 
  (10, 'New Message Received', '2023-12-01 09:30:00', null),
  (20, 'Event Reminder: Meeting at 2 PM', '2023-12-02 14:00:00', null),
  (10, 'Important Announcement: System Maintenance', '2023-12-03 23:59:00', null),
  (10, 'Upcoming Deadline: Project Submission', '2023-12-05 18:00:00', null),
  (20, 'Happy Birthday! 🎉', '2023-12-10 00:00:00', null);
  -- 後臺管理者  放入測試資料-- 
INSERT INTO manager (managerEmail, managerPassword, managerPer, managerCreatetime)
VALUES 
  ('luisa@example.com', 'adminpass1', 10, CURRENT_TIMESTAMP),
  ('sakiko@example.com', 'adminpass2', 20, CURRENT_TIMESTAMP),
  ('soyo@example.com', 'adminpass3', 10, CURRENT_TIMESTAMP),
  ('dgeetia@example.com', 'adminpass4', 10, CURRENT_TIMESTAMP),
  ('grand@example.com', 'adminpass5', 20, CURRENT_TIMESTAMP),
  ('alen@example.com', 'adminpass6', 10, CURRENT_TIMESTAMP),
  ('red@example.com', 'adminpass7', 10, CURRENT_TIMESTAMP),
  ('timmiy@example.com', 'adminpass8', 20, CURRENT_TIMESTAMP),
  ('testmanager2@gmail.com', '$2a$10$VAHoLewjAHro12EGZmD1zu1TNOSiUGrJJ6FMLf1FzKAcp/kzqrMly', 10, CURRENT_TIMESTAMP),
  ('testmanager@gmail.com', '$2a$10$AMRbaSaciFN/8ry3CuZLq.8O7wKw.zSh4WKHU9Kup0riB/Etu6nPC', 10, CURRENT_TIMESTAMP);

  -- 廣告  放入假資料-- 

INSERT INTO ad (sellerId,adimg, adImgUploadTime, adName, adUrl, adStartTime, adEndTime, adLv, adMemo, adStatus, adCreateTime, isEnabled)
VALUES 
  (1,NULL, NOW(), 'Test Ad 1', 'https://www.google.com/', NOW(), DATE('2099-12-31 00:00:00'), 1, 'This is a test ad.', "平台使用", NOW(), true),
  (2,NULL, NOW(), 'Test Ad 2', 'https://www.google.com/', NOW(), DATE('2099-12-31 00:00:00'), 1, 'Another test ad.', "平台使用", NOW(), true),
  (3,NULL, NOW(), 'Test Ad 3', 'https://www.google.com/', NOW(), DATE('2099-12-31 00:00:00'), 0, 'Yet another test ad.', "平台使用", NOW(), true),
  (1,NULL, NOW(), 'Test Ad 4', 'https://www.google.com/', NOW(), DATE('2099-12-31 00:00:00'), 0, 'Test ad number four.', "平台使用", NOW(), true),
  (2,NULL, NOW(), 'Test Ad 5', 'https://www.google.com/', NOW(), DATE('2024-12-31 00:00:00'), 1, 'The last test ad.', "已上架", NOW(), true),
  (3,NULL, NOW(), 'Test Ad 6', 'https://www.google.com/', NOW(), DATE('2024-12-31 00:00:00'), 1, 'Sample ad.', "已上架", NOW(), true),
  (1,NULL, NOW(), 'Test Ad 7', 'https://www.google.com/', NOW(), DATE('2024-12-31 00:00:00'), 1, 'Sample ad.', "已上架", NOW(), true),
  (2,NULL, NOW(), 'Test Ad 8', 'https://www.google.com/', NOW(), DATE('2024-12-31 00:00:00'), 0, 'Sample ad.', "已上架", NOW(), true),
  (3,NULL, NOW(), 'Test Ad 9', 'https://www.google.com/', NOW(), DATE('2024-12-31 00:00:00'), 0, 'Sample ad.', "已上架", NOW(), true),
  (1,NULL, NOW(), 'Test Ad 10', 'https://www.google.com/', NOW(), DATE('2024-12-31 00:00:00'), 0, 'Sample ad.', "已上架", NOW(), true),
  (2,NULL, NOW(), 'Test Ad 11', 'https://www.google.com/', NOW(), DATE('2024-12-31 00:00:00'), 1, 'Sample ad.', "已上架", NOW(), true);

-- 廣告審核  放入測試資料-- 
INSERT INTO adConfirm (adid, failReason, confirmTime, reviewStatus)
VALUES 
  (1, 'Not suitable for our audience.', NOW(), true),
  (2, 'Image quality issue.', NOW(), false),
  (3, 'Ad content violation.', NOW(), true),
  (4, 'Ad violated content guidelines.', NOW(), false),
  (5, 'Inappropriate content.', NOW(), true),
  (6, 'Not suitable for our platform.', NOW(), false),
  (7, 'Ad content quality issue.', NOW(), true),
  (8, 'Violated ad policies.', NOW(), false),
  (9, 'Not aligned with our brand.', NOW(), true),
  (10, 'Ad did not pass review.', NOW(), false);
  -- 跑馬燈  放入測試資料-- 
INSERT INTO newsTicker (newsTickerContent, sort, startTime, endTime, isDisplay)
VALUES 
  ('歡迎來到EDOG,毛小孩的快樂天堂', 1, NOW(), DATE('2024-12-13 00:00:00'), true),
  ('這個不應該顯示', 2, NOW(), DATE('2024-12-13 00:00:00'), false),
  ('最齊全的寵物百貨讓你逛到手軟', 3, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), false),
  ('80年代主題風貓窩熱賣中', 3, DATE('2023-12-13 00:00:00'), DATE('2023-12-14 00:00:00'), true),
  ('未來戰狗潔牙骨上架', 3, DATE('2025-12-13 00:00:00'), DATE('2025-12-14 00:00:00'), true),
  ('罐罐坐飛機!!百種進口罐罐讓你花式養刁貓主子', 4, DATE('2023-12-13 00:00:00'), DATE_ADD(DATE('2024-12-13 00:00:00'), INTERVAL 30 DAY), true);
  -- 匯款明細  放入測試資料-- 
INSERT INTO remittance (
    sellerId,remittanceEstimatedTime,remittanceTime,settlementTime,
    turnover,handlingFee,remittanceAmount,remittanceStatus,explanation
)
VALUES
    (1, '2023-01-01 10:00:00', null, '2023-01-10 20:45:00', 10000, 500, 9500, 1, '還沒匯款'),
    (2, '2023-02-01 12:00:00', null, '2023-02-15 18:30:00', 15000, 750, 14250, 1, '還沒匯款'),
    (3, '2023-03-05 08:30:00', '2023-03-10 10:15:00', '2023-03-15 12:00:00', 12000, 600, 11400, 0, 'Failed remittance'),
    (1, '2023-04-10 15:45:00', '2023-04-15 18:00:00', '2023-04-20 22:30:00', 18000, 900, 17100, 1, 'Third remittance'),
    (2, '2023-05-15 09:15:00', '2023-05-20 12:30:00', '2023-05-25 15:45:00', 20000, 1000, 19000, 1, 'Fourth remittance'),
    (3, '2023-06-20 11:30:00', '2023-06-25 14:45:00', '2023-06-30 17:00:00', 15000, 750, 14250, 1, 'Fifth remittance'),
    (1, '2023-07-25 13:45:00', '2023-07-30 16:00:00', '2023-08-05 20:15:00', 16000, 800, 15200, 1, 'Sixth remittance'),
    (2, '2023-08-30 16:30:00', '2023-09-05 18:45:00', '2023-09-10 21:00:00', 22000, 1100, 20900, 1, 'Seventh remittance'),
    (3, '2023-09-05 10:00:00', '2023-09-10 12:15:00', '2023-09-15 14:30:00', 13000, 650, 12350, 1, 'Eighth remittance'),
    (1, '2023-10-10 14:15:00', '2023-10-15 16:30:00', '2023-10-20 18:45:00', 19000, 950, 18050, 1, 'Ninth remittance');
-- 寵物抽卡  放入測試資料-- 

INSERT INTO petdraw (memberId, memberpairId, ismemberlike, memberrestime, memberpairrestime, ismemberpairlike, petdrawtime, petdrawlog, petdrawlat)
VALUES
    (1, 2, true, '2023-01-05 12:00:00', '2023-01-05 12:30:00', false, '2023-01-05 13:00:00', 25.123, 121.456),
    (1, 3, false, '2023-02-10 15:30:00', '2023-02-10 16:00:00', true, '2023-02-10 16:30:00', 25.456, 121.789),
    (1, 4, true, '2023-03-15 18:00:00', '2023-03-15 18:30:00', false, '2023-03-15 19:00:00', 25.789, 121.012);
-- BS聊天室  放入測試資料-- 
INSERT INTO sellChatRoom (buyerId, sellerId, createTime)
VALUES
    (2, 2, '2023-01-01 10:00:00'),
    (2, 4, '2023-02-15 14:30:00'),
    (2, 6, '2023-03-20 18:45:00');
-- BB聊天室  放入測試資料-- 
INSERT INTO chatRoom (buyer1Id, buyer2Id, createTime)
VALUES
    (1, 2, '2023-01-01 10:00:00'),
    (1, 4, '2023-02-15 14:30:00'),
    (1, 6, '2023-03-20 18:45:00');
-- BB聊天  放入測試資料-- 
INSERT INTO chat (chatRoomId, sendPart, message, image, sendTime)
VALUES
    (1, 1, '你好，這是第一則訊息', NULL, '2023-01-01 10:15:00'),
    (1, 2, '收到，請問有什麼需要幫助的嗎？', NULL, '2023-01-01 10:30:00'),
    (2, 1, '這是第二個聊天室的訊息', NULL, '2023-02-15 15:00:00'),
    (2, 2, '好的，有任何問題歡迎問我', NULL, '2023-02-15 15:15:00'),
    (3, 1, '第三個聊天室的測試訊息', NULL, '2023-03-20 19:00:00');
-- BS聊天  放入測試資料-- 
INSERT INTO sellChat (sellChatRoomId, sendPart, message, image, sendTime)
VALUES
    (1, 1, '你好，這是第一則訊息', NULL, '2023-01-01 10:15:00'),
    (1, 2, '收到，請問有什麼需要幫助的嗎？', NULL, '2023-01-01 10:30:00'),
    (2, 1, '這是第二個聊天室的訊息', NULL, '2023-02-15 15:00:00'),
    (2, 2, '好的，有任何問題歡迎問我', NULL, '2023-02-15 15:15:00'),
    (3, 1, '第三個聊天室的測試訊息', NULL, '2023-03-20 19:00:00');
-- 黑名單  放入測試資料-- 
INSERT INTO blackList (memberId, memberBlockId, blackListTime)
VALUES
    (1, 2, '2023-01-05 12:00:00'),
    (3, 4, '2023-02-10 17:30:00'),
    (5, 6, '2023-03-15 14:45:00');
-- 文章檢舉類型  放入測試資料-- 
INSERT INTO articleType (articleTypeName) VALUES
('寵物生活'),
('寵物美容'),
('寵物訓練'),
('寵物飲食'),
('寵物保健'),
('寵物旅遊');

-- 文章  放入測試資料-- 

INSERT INTO article (articleId, memberId, articleTitle, articleContent, artCreateTime, artUpdateTime, articleLike, articleComment, articleSort, isEnabled) VALUES
(1, 1, '如何養護你的狗的皮膚', '狗狗的皮膚健康對牠們的幸福至關重要。很多因素可以影響狗狗的皮膚健康，包括環境、飲食和基因。在本文中，我們將介紹一些簡單的方法來保持你的狗狗的皮膚健康，讓牠們活得更舒適。', '2023-01-15 08:00:00', NULL, 15, 5, 5, true),
(2, 2, '遊山玩水：和狗一起享受大自然', '帶著你的狗狗一同探索大自然，盡情奔跑！無論是在山上遠足、在河邊戲水，還是在草地上奔跑，這都是與你的狗狗一起度過美好時光的好方法。在這篇文章中，我們將分享一些遊山玩水的技巧，讓你和你的狗狗可以安全且快樂地享受大自然。', '2023-02-20 10:30:00', '2023-02-20 10:30:00', 20, 10, 6, true),
(3, 3, '教你的貓咪使用貓砂盆的技巧', '貓砂盆是貓咪的重要生活用品之一，但有些貓咪可能需要一些時間才能適應使用它。在這篇文章中，我們將分享一些教你的貓咪使用貓砂盆的技巧，幫助你的貓咪順利適應這個新環境。', '2023-03-10 14:45:00', '2023-03-10 14:45:00', 25, 8, 3, true),
(4, 4, '狗狗的飲食攻略：選擇最適合的狗糧', '狗狗的飲食是牠們健康和幸福的重要組成部分。在市場上有各種各樣的狗糧可供選擇，但如何找到最適合你的狗狗的狗糧呢？在這篇文章中，我們將分享一些關於選擇最適合的狗糧的攻略，讓你的狗狗擁有健康的飲食習慣。', '2023-04-05 16:20:00', '2023-04-05 16:20:00', 30, 12, 4, true),
(5, 5, '美容秘訣：如何給你的貓洗澡', '給貓咪洗澡可能是一件挑戰，但遵循這些技巧可以讓事情變得更容易。在這篇文章中，我們將分享一些給你的貓洗澡的秘訣，幫助你讓這個過程更加順利和輕鬆。', '2023-05-12 11:10:00', NULL, 18, 7, 2, true),
(6, 1, '如何保持你的寵物狗健康並開心', '一些簡單的方法可以讓你的狗狗保持健康並快樂。在這篇文章中，我們將分享一些保持你的寵物狗健康並開心的技巧，讓你的寵物與你共度愉快時光。', '2023-06-25 09:30:00', '2023-06-25 09:30:00', 22, 9, 5, true),
(7, 2, '貓咪養成好習慣的秘訣', '教導你的貓咪一些良好的習慣，讓牠們更容易相處。在這篇文章中，我們將分享一些培養貓咪良好習慣的秘訣，幫助你的貓咪成為更好的伴侶。', '2023-07-18 13:55:00', '2023-07-18 13:55:00', 27, 11, 3, true),
(8, 3, '寵物旅遊安全注意事項', '帶著你的寵物旅行前應該知道的一些重要注意事項。無論是自駕遊還是乘坐飛機，安全是首要考慮的因素。在這篇文章中，我們將分享一些寵物旅遊安全注意事項，讓你和你的寵物可以安全又愉快地旅行。', '2023-08-07 15:40:00', NULL, 21, 6, 6, true),
(9, 4, '如何照顧年老的狗狗', '年老的狗狗需要特別的照顧和關注，讓牠們舒適度過晚年。在這篇文章中，我們將分享一些照顧年老的狗狗的技巧，幫助你的狗狗度過舒適的晚年時光。', '2023-09-14 17:25:00', NULL, 24, 10, 5, true),
(10, 5, '狗狗的食譜：健康自製狗零食', '製作一些營養豐富的自製狗零食，讓你的狗狗愛不釋手。在這篇文章中，我們將分享一些健康的狗狗食譜，幫助你製作美味又營養的狗狗零食。', '2023-10-29 12:15:00', NULL, 28, 13, 4, false);

-- 文章喜歡  放入測試資料-- 
INSERT INTO articleLike (memberId, articleId, articleLikeListTime)
VALUES
    (1, 1, '2023-01-05 11:00:00'),
    (2, 2, '2023-02-10 15:30:00'),
    (3, 3, '2023-03-15 20:00:00'),
    (4, 4, '2023-04-20 09:00:00'),
    (5, 5, '2023-05-25 13:30:00'),
    (1, 2, '2023-01-06 11:15:00'),
    (2, 3, '2023-02-11 16:00:00'),
    (3, 4, '2023-03-16 20:30:00'),
    (4, 5, '2023-04-21 09:30:00'),
    (5, 1, '2023-05-26 14:00:00');
    
INSERT INTO productSort(productSortNo , productSortName , productCategory,isEnabled)
VALUES

(0, '食品','food',1),
(1, '玩具','toys',1),
(2, '寢具','bedding',1),
(3, '美容清潔','grooming',1),
(4, '攜帶袋','carriers',1),
(5, '服飾','apparel',1),
(6, '健康保健','health_care',1),
(7, '配件','accessories',1);  
    
    
    
-- 商品  放入測試資料-- 
INSERT INTO product (sellerId, productCoverImg, productName, price, productStockQuantity, productDetails, productStatus, productCreationTime, animalType, ratings, totalReviews, productSortNo, isEnabled)
VALUES

-- 食品-- 
    (6, NULL, '狗麻吉', 300, 999, '均衡營養, 寵愛狗狗', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 0, TRUE),
    (6, NULL, 'Hero 狗', 150, 999, '專為狗狗設計 , 健康零負擔', '已上架', '2023-02-15 14:30:00', 'dog', 5, 3, 0, TRUE),
    (6, NULL, 'Nature 狗飼料', 80, 999, '純天然食品 , 照顧你的小狗', '已上架', '2023-03-20 18:45:00', 'dog', 3, 1, 0, TRUE),
    (6, NULL, 'Good dog', 120, 999, 'S30(鮭魚+甜薯)低過敏配方', '已上架', '2023-04-10 08:00:00', 'dog', 4, 2, 0, TRUE),
    (6, NULL, '狗狗小舖', 200, 999, '在地食材國產非基因改造玉米', '已上架', '2023-05-25 12:00:00', 'dog', 5, 3, 0, TRUE),
    (6, NULL, '七彩鳥 鳥可樂', 200, 999, '特選上等原料，品質純正，含有蛋黃、蛋白質、酵母粉、多種高單位維他命及磷、鈣等礦物質。', '已上架', '2023-05-25 12:00:00', 'bird', 5, 3, 0, TRUE),
    (6, NULL, '雜食鳥營養飼料鳥飼料 八哥/九官鳥專用', 200, 999, '添加天然酵母及複合維他命、鈣、鐵、磷等礦物質，每百公克可產生熱量3860卡，極適合各種雜食性鳥類長期飼用，可促進生長、確保羽毛光澤、活潑健康。', '已上架', '2023-05-25 12:00:00', 'bird', 5, 3, 0, TRUE),
    (6, NULL, '合美健NO.14-B 寵物鳥、鸚鵡用', 200, 999, '精選各類種子，營養配合最均衡。經洗選及乾燥處理，安全無菌，確保衛生健康', '已上架', '2023-05-25 12:00:00', 'bird', 5, 3, 0, TRUE),
    (6, NULL, '凡賽爾 寵物鳥、鸚鵡用 滋養丸', 200, 999, '以大量新鮮水果和穀物為基礎並由科學精算成份比例所組成的顆粒飼料，提供雜食性鳥類所需的營養，完全食用沒有浪費。', '已上架', '2023-05-25 12:00:00', 'bird', 5, 3, 0, TRUE),
    (6, NULL, '愛鳥樂8個蛋黃NO.10/蛋黃栗營養飼料', 200, 999, '精選上等原料，品質新鮮。', '已上架', '2023-05-25 12:00:00', 'bird', 5, 3, 0, TRUE),
    (6, NULL, '博士巧思貓食', 450, 999, '主要使用原料：新鮮海洋魚粉(鮭魚、鯷魚)、雞肉粉、胚芽米', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 0, TRUE),
    (6, NULL, '博朗氏貓飼料', 1000, 999, '高適口性平衡糧 產品期限：18個月', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 0, TRUE),
    (6, NULL, '保羅叔叔貓飼料', 1300, 999, '運費120元一單限購 2包', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 0, TRUE),
    (6, NULL, '博朗氏 貓飼料', 1400, 999, ' LCB藍帶廚坊 -貓飼料 海陸雙拼 (化毛潔牙) 15LB(6.8kg)s', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 0, TRUE),
    (6, NULL, '樂倍貓飼料', 1500, 999, ' 樂倍黑酵母無穀保健糧挑嘴貓4.5kg', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 0, TRUE) ,

-- 玩具--
  (6, NULL, '超大衝鋒鴨寵物咬咬狗狗玩具', 900, 999, '材質柔軟 不傷牙齒🦷解悶 安心陪伴♥️', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 1, TRUE),
  (6, NULL, '繩結 狗玩具', 800, 999, '適合大型犬、大型幼犬、破壞力強', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 1, TRUE),
  (6, NULL, '天使法杖×惡魔長鞭 市售最強逗貓棒', 700, 999, '模擬獵物超高速移動🔹 適合愛抓小獵物的貓咪', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 1, TRUE),
  (6, NULL, '貓咪玩具 逗貓 自動逗貓球', 650, 999, '使用說明 充電時長約為2小時，充滿電之後可持續使用4~5小時。長按2s，拍拍開啟運動', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 1, TRUE),
  (6, NULL, '貓跳台 貓抓柱 貓爬架 實木貓跳臺', 2000, 999, '因商品製作成本高和運輸成本高，出貨後，非質量問題不退不換歐，介意請勿下單', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 1, TRUE),


-- 寢具--
(6, NULL, ' 寵物床 狗窩 寵物涼感床 狗床 貓床 寵物睡窩 寵物涼墊 寵物睡墊 寵物涼感墊', 2500, 999, ' 主子們是沒有毛細孔的 太熱的話容易中暑喔💦炎熱的春夏請幫主子們準備好降溫產品 ', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 2, TRUE),
    (6, NULL, ' 小骨頭寵物窩 🔥兩件折15元⭐ 寵物窩 寵物睡窩 狗狗睡窩 寵物床 狗窩 狗床 寵物毯 狗睡窩 狗睡床', 3000, 999, ' 面料柔軟親膚，厚實高純度PP棉填充讓睡窩不會軟趴趴 ', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 2, TRUE),
    (6, NULL, ' 貓窩 寵物窩 四季通用的貓咪窩 貓屋 寵物睡窩 半封閉可拆洗貓床 ', 3500, 999, ' 💞💞可愛的動物耳朵設計，是一張好看的床💞💞用絨球，給你的貓帶來歡樂 ', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 2, TRUE),
    (6, NULL, ' 保暖貝殼窩 貓窩 貝殼窩 寵物貝殼窩 寵物窩 貓咪貝殼窩 ', 5000, 999, '🔶觸感舒適柔軟，不易皺摺變形🔶優質PP棉填充，回彈性強不塌陷 ', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 2, TRUE),

  -- 美容清潔--
    (6, NULL, '寵物潔足杯 洗腳杯 洗腳器 洗爪器 寵物洗腳 狗狗清潔 ', 1100, 999, '✅柔軟矽膠，不傷腳✅可拆裝設計，方便清洗 ', '已上架', '2023-01-01 10:00:00', 'dog', 5, 2, 3, TRUE),
    (6, NULL, ' 寵物除蚤沐浴乳/敏感肌適用/現貨/你他的招財❤️寵物除蟲清潔液 寵物沐浴 寵物洗澡 寵物用品 狗洗澡  ', 700, 999, '有效去除寵物體外寄生蟲、塵蟎、蟎蟲、跳蚤、蝨子，防止皮膚癢、掉毛、皮膚病變 ', '已上架', '2023-01-01 10:00:00', 'dog', 5, 2, 3, TRUE),
    (6, NULL, ' 寵物沐浴乳 寵物洗毛精 寵物沐浴精 寵物洗澡 狗狗沐浴乳  ', 750, 999, '愛寵是家庭的一員，因此給他們提供最好的護理是非常重要的。全新的寵物沐浴露，適用於狗狗，四季都能使用🧴🫧！', '已上架', '2023-01-01 10:00:00', 'dog', 5, 2, 3, TRUE),
    (6, NULL, ' 送寵物洗毛刷 | 呆丸喵 天然寵物沐浴乳 貓沐浴乳 ', 950, 999, '呆丸喵專為寵物打造的高效除臭的寵物沐浴乳/洗毛精，結合天然植物成分和溫和滋潤配方，有效潔淨並呵護您愛寵的皮膚與毛髮。', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 3, TRUE),
    (6, NULL, ' 鳥用體外清潔噴霧 鸚鵡除臭 鸚鵡環境清潔 鳥類通用 除臭噴霧 ', 2300, 999, '未販售應施檢疫物，圖片僅供示意⚠️本產品無任何醫療行為 如有需要請諮詢獸醫師協助', '已上架', '2023-01-01 10:00:00', 'bird', 3, 2, 3, TRUE),
    (6, NULL, ' 潔羽寶 鸚鵡除臭劑 環境清潔 防蟲噴霧 鳥類通用', 450, 999, '適用於: 鸚鵡, 文鳥, 繡眼, 鴿子和其他鳥類日常可以針對環境 鳥籠 巢箱 地面 牆角等定期噴灑', '已上架', '2023-01-01 10:00:00', 'bird', 3, 2, 3, TRUE),


  --  攜帶袋  --
(6, NULL, ' 現貨 貓包外出雙肩包 太空艙寵物包 裝貓背包', 1000, 999, '商品名稱：寵物太空艙背包請注意！！！！首次使用之前請將背包放在空氣流通處、進行通風因為背包是剛出廠、所以可能有味道、非質量問題請進行清理或通風使用背包適用12斤以內寵物台灣現貨、當天下單、一般當天寄出、 ', '已上架', '2023-01-01 10:00:00', 'cat', 5, 2, 4, TRUE),
(6, NULL, ' 大狗背包 寵物背包 寵物包 寵物外出包 寵物後背包 狗狗外出包 狗背包 老狗背帶 狗狗背包', 1100, 999, '🔶LDLC超級工廠出品，適合6公斤內貓、狗、寵物🔶拉鍊升級，更耐用🔶附有同色系肩背帶，可單肩揹、斜肩揹、手提🔶規格 : 約46*26*28cm 適用6公斤以內寵物🔶材質 : 亞麻布+網布+PE板🔶附有行李固定帶🔶全手提包65%為網格設計，透氣不悶熱🔶目前進貨的新出廠手提包都只有單拉鍊頭設計 ', '已上架', '2023-01-01 10:00:00', 'dog', 5, 2, 4, TRUE),
(6, NULL, '貓咪外出包包 貓咪外出雙肩包 貓咪外出背包大容量 貓咪外出袋 寵愛喵包外出便攜包 透氣出行攜帶咪大容量', 1000, 999, '包裹抵達門店會有手機簡訊通知取貨,到貨後請盡快前往便利店完成取貨。 到貨日起算,7天內未完成取貨,您訂購的產品會被退回。 ', '已上架', '2023-01-01 10:00:00', 'cat', 5, 2, 4, TRUE),
(6, NULL, ' 寵物包外出便攜背包大型犬狗狗露頭背包通風透氣可水洗', 1600, 999, '◾ 寵爸寵媽一人外出時，也方便攜帶愛寵◽ 全包露頭露前腳，尊重愛寵隱私，細心呵護寶貝◾ 不管是騎摩托車、重機、腳踏車，可愛寶貝都可以背著陪你出門◽ 耐磨舒適透氣，四季通用，高密度輕微抗水', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 4, TRUE),
(6, NULL, ' 批發鳥籠子塑膠組裝寵物籠展示籠25圓形玄鳳牡丹鸚鵡繡眼外出鳥籠', 1500, 999, '材質：高強度工程塑膠 規格：標配：籠體1套+吊鉤1套+底盤1件+站槓1件 顏色：籠體，掛鉤顏色竹黃色', '已上架', '2023-01-01 10:00:00', 'bird', 2, 2, 4, TRUE),
(6, NULL, '  寵尚天鸚鵡外出籠 手提便攜鸚鵡籠子 文鳥虎皮圓形鳥籠觀賞繁殖籠子', 1300, 999, '❤️💛本店所售商品均有品質保證，請放心購買喲 收到貨品後有任何問題請第一時間聯繫我呢 保證給您一個滿意的回復 非常榮幸在成長路上能得到您的支持和鼓勵 喜歡請給我點讚收藏並關注唷 萬分感謝啦💕💕', '已上架', '2023-01-01 10:00:00', 'bird', 2, 2, 4, TRUE),


  --  服飾  --
  (6, NULL, ' 哈利波特寵物聖誕魔法袍斗篷寵物衣服變身裝 眼鏡 領巾領帶貓咪', 500, 999, '您家毛孩子是哪一個學院的呢?哆咔星球tips：領帶S碼脖圍：20-30cm可調節   M碼脖圍：30-40cm可調節   【手工測量誤差1-3cm  請根據您家寶貝的脖圍來選擇哦】   沒有L碼數', '已上架', '2023-01-01 10:00:00', 'cat', 5, 2, 5, TRUE),
 (6, NULL, ' 【貓咪服飾】貓咪狗狗衣服薄款英短藍貓小奶貓防掉毛可愛網紅抖音同款寵物服飾 貓咪衣服', 600, 999, '每一件商品出貨前都會仔細檢查後包裝，希望大家收到商品時都能滿心喜悅的微笑👉產品質量：本店只用上乘 產品質量可靠，放心購買，用的開心', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 5, TRUE),
 (6, NULL, ' 狗衣服 秋冬季打底衫 小狗狗衣服', 800, 999, '註意：手工測量，存在2-3厘米誤差，屬於正常，批次不同衣領顏色輕微色差 &nbsp; 商品做升級，下擺位置和袖口不是縮口是圓形下擺', '已上架', '2023-01-01 10:00:00', 'dog', 3, 2, 5, TRUE),
 (6, NULL, ' 夏日防曬狗狗四腳衣/大狗涼感衣服/薄款背心/寵物衣服/狗衣/狗毛衣/', 750, 999, '尺碼偏大一號，請謹慎下單注意：四腳衣先看背長，請購買前一定要測量，不能確定時請購買大一號·，謝謝。【物流資訊】※本商店所有商品出貨時間2-3天，運輸時間5-7天左右到貨，週末、法定節假日物流會有延遲！', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 5, TRUE),
(6, NULL, ' 鸚鵡鳥用尿不濕 鸚鵡衣服 鸚鵡尿布 鳥尿布 鳥衣服', 550, 999, '🌟鳥寶尿布衣🌟🌟多款現貨隨時更新🌟以魔鬼氈黏貼，穿的時候可墊衛生紙，或者化妝棉，髒了換掉就好，不用一直洗尿布。🌟鳥寶剛穿一定會不調節，幾次之後就會習慣了', '已上架', '2023-01-01 10:00:00', 'bird', 5, 2, 5, TRUE),

  --  保健食品  --

(6, NULL, ' INPLUS狗保健品 犬用保健 犬用保健食品 益生菌 卵磷脂 關節保健 腸益菌 腸胃保健 關節保健', 1250, 999, '★本產品無任何醫療行為 如有需要請諮詢獸醫師協助★未使用時放置在陰涼處及孩童碰觸不到的地方。', '已上架', '2023-01-01 10:00:00', 'dog', 4, 2, 6, TRUE),
(6, NULL, ' 美國 IN-Plus "贏" 高效能活化益生菌、骨力補-關節心臟強化配方 腸益菌 酵素 整腸 狗狗保健 關節保健', 1150, 999, '【主要成分】：美國專利益生菌PA-5051、活化乳酸菌、果寡糖、甘露聚醣、綜合酵素、綜合維生素、麥芽糊精。【營養成分】：維生素A 300 IU、維生素B1 10.24mg、維生素B2 0.32mg、維生素B3 1.74mg、維生素B5 0.97mg、維生素B6 15.59mg、生物素 6.00μg、葉酸 19.88μg、維生素B12 4.97μg、維生素D3 50IU、維生素E 0.80mg、活性益生菌5x108 cfu、果寡糖10mg、甘露聚糖20mg、綜合消化酵素9mg。', '已上架', '2023-01-01 10:00:00', 'dog', 5, 2, 6, TRUE),
(6, NULL, ' 貓用保健食品系列 貓保健品 貓營養品 營養保健 IN-PLUS贏 皮毛保健 關節保健 眼睛保健 腸胃保', 1450, 999, '🌟發育整腸酵素維護腸胃健康  幫助消化  營養補給/好吸收【主要成份】綜合消化酵素、綜合腸益菌、菊苣根、複合維生素（A、B1、B2、B3、B6、B12、D3、E、生物素、葉酸）、礦物質（鐵、鋅、錳、銅）', '已上架', '2023-01-01 10:00:00', 'cat', 4, 2, 6, TRUE),
(6, NULL, ' 美國 IN-Plus"贏" 貓咪保健專用系列-離胺酸、蔓越莓泌尿安、PA-5051 貓用益生菌 牛磺酸、排毛粉', 1650, 999, '可混和在飼料或罐頭中給予。為保證產品效果，開封後請盡早食用完畢。年齡 6月齡以下 6月齡以上疾病期:一天2次，每次半包 一天2次，每次1包保健期:一天1次，每次半包 一天1次，每次1包', '已上架', '2023-01-01 10:00:00', 'cat', 3, 2, 6, TRUE),

--  配件  --
(6, NULL, ' 優選好物 狗狗自背包大型犬德牧金毛戰術背包外齣自背包寵物牽引狗背包', 600, 999, '本店所售商品均有品質保證，請放心購買，收到貨品後有任何問題請第壹時間聯系我們呢，保證給您壹個滿意的答復！全場商品購滿三件9折~ ', '已上架', '2023-01-01 10:00:00', 'dog', 5, 2, 7, TRUE);

-- 優惠券使用  放入測試資料-- 
INSERT INTO couponUsed (
    couponId, usedId, useTime
)
VALUES 
    (1, 2, '2023-11-05 14:30:00'),
    (2, 2, '2023-12-10 08:45:00'),
    (3, 2, '2024-01-15 16:20:00'),
    (4, 5, '2023-05-20 12:00:00'),
    (5, 5, '2023-10-25 19:30:00');
-- 留言  放入測試資料-- 
INSERT INTO reply (articleId, memberId, replyContent, replyTime, replyLike, isEnabled) VALUES
(1, 2, '很有用的信息，謝謝分享！', '2023-01-15 09:30:00', 5, true),
(1, 3, '我也覺得很重要，謝謝你的分享！', '2023-01-15 10:15:00', 8, true),
(2, 4, '這真是一個美好的體驗，我也想帶我的狗去山上玩！', '2023-02-20 11:20:00', 10, true),
(2, 5, '我之前就有試過，非常有趣！', '2023-02-20 12:00:00', 6, true),
(3, 1, '謝謝分享，我會試試這些方法！', '2023-03-10 15:20:00', 4, true),
(3, 2, '這些技巧真的有用，我的貓現在已經習慣使用貓砂盆了！', '2023-03-10 16:00:00', 7, true),
(4, 3, '謝謝分享這些飲食攻略，我會注意給我的狗選擇最適合的狗糧！', '2023-04-05 17:10:00', 9, true),
(4, 4, '我也正在尋找適合我的狗的狗糧，這將對我有所幫助，謝謝！', '2023-04-05 17:45:00', 5, true),
(5, 5, '這些秘訣真的很有用，我現在更容易給我的貓洗澡了！', '2023-05-12 12:30:00', 6, true),
(5, 1, '我也遇到過洗澡的問題，謝謝你分享你的經驗！', '2023-05-12 13:00:00', 3, true);

-- 留言喜歡  放入測試資料-- 
INSERT INTO replyLike (memberId, replyId, replyLikeTime)
VALUES
    (1, 1, '2023-01-05 12:00:00'),
    (2, 1, '2023-01-06 13:00:00'),
    (3, 1, '2023-02-10 17:30:00'),
    (4, 1, '2023-02-11 18:00:00'),
    (5, 1, '2023-02-12 11:00:00'),
    (1, 2, '2023-01-05 12:30:00'),
    (2, 2, '2023-01-06 13:30:00'),
    (3, 2, '2023-02-10 18:00:00'),
    (4, 2, '2023-02-11 19:00:00'),
    (5, 2, '2023-02-12 12:00:00');

-- 商品圖片  放入測試資料-- 
INSERT INTO productImage (productId, productImg, productImgTime, isCover, isEnabled)
VALUES
    (1, NULL, '2023-01-02 14:00:00', TRUE, TRUE),
    (1, NULL, '2023-01-03 15:30:00', FALSE, FALSE),
    (2, NULL, '2023-01-03 15:30:00', TRUE, FALSE),
    (2, NULL, '2023-01-03 15:30:00', FALSE, FALSE),
    (3, NULL, '2023-01-03 15:30:00', FALSE, TRUE),
    (4, NULL, '2023-01-03 15:30:00', FALSE, TRUE),
    (5, NULL, '2023-02-16 08:45:00', TRUE, TRUE);
-- 收藏  放入測試資料-- 
INSERT INTO collection (productId, memberId, isEnabled, collectionTime)
VALUES
    (1, 1, true, '2023-01-01 10:00:00'),
    (2, 2, true, '2023-02-05 15:30:00'),
    (3, 3, false, '2023-03-10 20:45:00'),
    (4, 1, true, '2023-04-15 08:20:00'),
    (5, 3, true, '2023-05-20 12:15:00');
-- 訂單  放入測試資料-- 
INSERT INTO productOrder (sellerId, memberId, couponId, memberPaysShipping, sellerPaysShipping, orderOrigPrice, actualPay, orderTime, orderStatus, invoiceNumber, receiver, receiveTime, mobile, contactAddress, isDelivered, shippingTime)
VALUES
(1, 1, 1, 10, 5, 500, 495, '2023-01-01 10:00:00', 1, 12345, 'John Doe', '2023-01-05 12:00:00', '1234567890', '123 Main St', true, '2023-01-06 14:30:00'),
(1, 2, 2, 8, 6, 700, 692, '2023-02-01 11:30:00', 2, 23456, 'Jane Smith', '2023-02-05 15:30:00', '9876543210', '456 Oak St', false, null),
(2, 2, NULL, 12, 4, 600, 588, '2023-03-01 14:45:00', 3, 34567, 'Sam Johnson', null, '3456789012', '789 Pine St', false, null),
(2, 3, NULL, 15, 3, 800, 785, '2023-04-01 16:00:00', 1, 45678, 'Emily Davis', '2023-04-05 18:00:00', '5678901234', '678 Maple St', true, '2023-04-07 10:00:00'),
(3, 4, 5, 10, 5, 900, 890, '2023-05-01 09:30:00', 2, 56789, 'Robert Wilson', '2023-05-05 11:45:00', '0123456789', '789 Elm St', false, null);
-- 訂單明細  放入測試資料-- 

-- 訂單1的明細 --
INSERT INTO orderDetails (orderId, productId, purchaseQuantity, isCommented, stars, commentedTime, comments, attachments, isEnable) 
VALUES (1, 1, 2, true, 4, '2024-03-04 12:30:00', '很滿意', 'attachment1.jpg', true);

INSERT INTO orderDetails (orderId, productId, purchaseQuantity, isCommented, stars, commentedTime, comments, attachments, isEnable) 
VALUES (1, 1, 1, false, NULL, NULL, NULL, NULL, true);

-- 訂單2的明細 --
INSERT INTO orderDetails (orderId, productId, purchaseQuantity, isCommented, stars, commentedTime, comments, attachments, isEnable) 
VALUES (2, 2, 3, true, 5, '2024-03-05 14:45:00', '非常好', 'attachment2.jpg', true);

INSERT INTO orderDetails (orderId, productId, purchaseQuantity, isCommented, stars, commentedTime, comments, attachments, isEnable) 
VALUES (2, 2, 2, true, 4, '2024-03-05 15:00:00', '不錯', 'attachment3.jpg', true);

-- 訂單3的明細 --
INSERT INTO orderDetails (orderId, productId, purchaseQuantity, isCommented, stars, commentedTime, comments, attachments, isEnable) 
VALUES (3, 3, 1, false, NULL, NULL, NULL, NULL, true);

INSERT INTO orderDetails (orderId, productId, purchaseQuantity, isCommented, stars, commentedTime, comments, attachments, isEnable) 
VALUES (3, 2, 4, true, 3, '2024-03-06 10:20:00', '一般般', 'attachment4.jpg', true);


-- 檢舉Type  放入測試資料-- 
INSERT INTO reportType (reportTypeId, reportTypeSort) VALUES
    (1, '垃圾訊息'),
    (2, '侵犯隱私'),
    (3, '仇恨言論'),
    (4, '虛假資訊'),
    (5, '侵權投訴'),
    (6, '不當內容'),
    (7, '垃圾廣告'),
    (8, '不實消息'),
    (9, '冒充身份'),
    (10, '其他');

-- 檢舉  放入測試資料-- 
INSERT INTO report (reportMemberId, reportTargetType, replyId, articleId, reportTypeId, reportTime, reportState, reportDealTime) VALUES
(3, 0, NULL, 1, 1, '2023-01-16 10:00:00', 1, '2023-01-16 11:00:00'),
(4, 0, NULL, 2, 3, '2023-02-21 11:30:00', 1, '2023-02-21 12:30:00'),
(5, 1, 1, NULL, 4, '2023-03-12 14:20:00', 1, '2023-03-12 15:20:00'), 
(1, 1, 2, NULL, 6, '2023-04-06 16:45:00', 1, '2023-04-06 17:45:00'),
(2, 0, NULL, 3, 9, '2023-05-13 09:55:00', 1, '2023-05-13 10:55:00'),
(1, 1, 3, NULL, 2, '2023-06-20 14:30:00', 0, NULL), 
(2, 1, 4, NULL, 7, '2023-07-08 17:10:00', 0, NULL), 
(3, 0, NULL, 4, 8, '2023-08-15 09:45:00', 0, NULL), 
(4, 0, NULL, 5, 5, '2023-09-02 11:20:00', 0, NULL), 
(5, 1, 5, NULL, 10, '2023-10-10 13:55:00', 0, NULL);

-- 通知分類  放入測試資料-- 
INSERT INTO msgType (msgTypeContent) VALUES 
    ('文章喜歡'),
    ('文章留言'),
    ('留言喜歡'),
    ('被抽到'),
    ('文章檢舉'),
    ('留言檢舉');
    
-- 通知  放入測試資料-- 
INSERT INTO msg (memberId, articleId, replyId, reportId, petdrawId, msgTypeId, msgTime, isRead, isEnabled) VALUES
    (1, 1, null, null, null, 1, '2024-03-14 08:00:00', FALSE, TRUE),
    (2, null, 2, null, null, 2, '2024-03-14 09:30:00', TRUE, TRUE),
    (3, null, 4, null, null, 3, '2024-03-14 10:45:00', FALSE, TRUE),
    (4, null, null, null, null, 4, '2024-03-14 11:20:00', TRUE, TRUE),
    (5, null, null, 1, null, 5, '2024-03-14 13:00:00', TRUE, FALSE),
    (1, null, null, 3, null, 6, '2024-03-14 14:15:00', FALSE, TRUE),
    (2, 2, null, null, null, 1, '2024-03-14 15:45:00', TRUE, TRUE),
    (3, null, 3, null, null, 2, '2024-03-14 16:30:00', FALSE, TRUE),
    (4, null, 4, null, null, 3, '2024-03-14 17:20:00', TRUE, TRUE),
    (5, null, null, null, null, 4, '2024-03-14 18:00:00', TRUE, FALSE);

