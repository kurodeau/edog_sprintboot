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
create table IF NOT EXISTS buyer(
	memberId int AUTO_INCREMENT primary key,
	memberEmail VARCHAR(200),
    thirdFrom VARCHAR(100),
    memberName VARCHAR(100),
    memberPhone VARCHAR(10),
    memberMobile varchar(20),
    memberBirthday DATE,
    memberPassword varchar(100),
    memberAddress VARCHAR(100),
    isMemberEmail boolean,
    memberRegistrationTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    petName VARCHAR(100),
    petImg LONGBLOB,
    petImgUploadTime datetime,
    petVaccName1 VARCHAR(100),
    petVaccTime1 DATETIME,
    petVaccName2 VARCHAR(100),
    petVaccTime2 DATETIME,
    isConfirm boolean DEFAULT TRUE
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
Create Table IF NOT EXISTS managerUser(
managerUserId int AUTO_INCREMENT primary key ,
managerUserName varchar(100) ,
managerPassword varchar(100) ,
managerPer int ,
createtime datetime DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS ad (
adid int AUTO_INCREMENT primary KEY,
-- constraint fk_seller_sellerID
-- foreign key(sellerID) references seller(sellerID),
adimg longblob,
adImgUploadTime datetime,
adName varchar(20),
adUrl varchar(100),
adStartTime datetime ,
adEndTime datetime  ,
adLv int,
adMemo varchar(100),
isAdConfirm boolean,
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
    petdrawid int PRIMARY KEY AUTO_INCREMENT,
    memberid INT,
    memberpairid INT,
    ismemberlike BOOLEAN,
    memberrestime DATETIME,
    memberpairrestime DATETIME,
    ismemberpairlike BOOLEAN,
    petdrawtime DATETIME DEFAULT CURRENT_TIMESTAMP,
    petdrawlog DOUBLE,
    petdrawlat DOUBLE,
    CONSTRAINT fk_member FOREIGN KEY (memberid) REFERENCES buyer(memberid),
    CONSTRAINT fk_member_pair FOREIGN KEY (memberpairid) REFERENCES buyer(memberid)
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
foreign key (memberId) references buyer(memberid),
memberBlockId int,
foreign key (memberBlockId) references buyer(memberid),
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
    artUpdateTime DATETIME,
    articleLike INT,
    articleComment INT,
    articleShare INT,
    articleSort INT,
    isEnabled BOOLEAN,
    FOREIGN KEY (memberId) REFERENCES buyer(memberId),
    FOREIGN KEY (articleSort) REFERENCES articleType(articleTypeId)
);
CREATE TABLE IF NOT EXISTS articlePic(
articlePicId int primary key AUTO_INCREMENT,
articleId int,
articlePicBlob LONGBLOB,
articlePicTime datetime DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (articleId) REFERENCES article(articleId)
);
-- 文章影片  創建table-- 
CREATE TABLE IF NOT EXISTS articleVid(
articleVidId Int primary key AUTO_INCREMENT,
articleId Int,
articleVidBlob LONGBLOB,
articleVidTime datetime DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (articleId) REFERENCES article(articleId)
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
-- 商品  創建table-- 
CREATE TABLE IF NOT EXISTS product (
    productId INT AUTO_INCREMENT PRIMARY KEY,
    sellerId INT,
    productCoverImg LONGBLOB,
    productName VARCHAR(100),
    productPrice INT,
    productStockQuantity INT,
    productDetails VARCHAR(100),
    productStatus VARCHAR(10),
    productCreationTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    totalStars INT,
    totalReviews INT,
    productSort INT,
    isEnabled BOOLEAN,
    CONSTRAINT fk_seller FOREIGN KEY (sellerId) REFERENCES seller(sellerId)
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
-- 通知  創建table-- 
create table IF NOT EXISTS msg(
msgId int primary key AUTO_INCREMENT,
memberId int,
foreign key(memberId) references buyer(memberId),
msgType int,
msgContent varchar(100),
msgTime datetime DEFAULT CURRENT_TIMESTAMP,
isEnabled boolean
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
    orderDetailsId INT,
    productId INT,
    purchaseQuantity INT,
    isCommented BOOLEAN,
    stars INT,
    commentedTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    comments VARCHAR(200),
    attachments VARCHAR(200),
    isEnable BOOLEAN,
    PRIMARY KEY (orderDetailsId, productId),
    FOREIGN KEY (orderDetailsId) REFERENCES productOrder (orderId), 
    FOREIGN KEY (productId) REFERENCES product (productId)
);
-- 檢舉  創建table-- 
CREATE TABLE IF NOT EXISTS report(
reportId Int primary key AUTO_INCREMENT,
reportMemberId Int,
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




-- 賣家等級  放入測試資料
INSERT INTO sellerLv (lvName, platformCommission, adAllowType, isExportGoldflow, freightSub, returnSubPerMonth, isShowPriority, shelvesNumber)
VALUES 
  ('Bronze', 0.05, 1, true, 10, 2, false, 50),
  ('Silver', 0.08, 2, true, 20, 3, true, 100),
  ('Gold', 0.1, 3, true, 30, 4, true, 150),
  ('Platinum', 0.12, 4, true, 40, 5, true, 200),
  ('Diamond', 0.15, 5, true, 50, 6, true, 250);
  -- 賣家  放入測試資料
INSERT INTO seller (sellerLvId, sellerEmail, sellerCompany, sellerTaxId, sellerCapital, sellerContact, sellerCompanyPhone, sellerCompanyExtension, sellerMobile, sellerAddress, sellerPassword, sellerBankAccount, sellerBankCode, sellerBankAccountNumber, isConfirm)
VALUES 
    (1, 'seller1@example.com', 'ABC Company', 'A1234567', 500000, 'John Doe', '0223456789', '123', '0912345678', '台北市中正區1號', 'Password1', '123-456-789', '012', '98765432', true),
    (2, 'seller2@example.com', '未審核通過id2', 'B2345678', 700000, 'Jane Smith', '0223456789', '456', '0923456789', '台北市中正區2號', 'Password2', '234-567-890', '345', '87654321', false),
    (3, 'seller3@example.com', '未審核通過id3', 'C3456789', 900000, 'Bob Johnson', '0223456789', '789', '0934567890', '台北市中正區3號', 'Password3', '345-678-901', '456', '76543210', false),
    (4, 'seller4@example.com', 'PQR Industries', 'D4567890', 1200000, 'Alice Lee', '0223456789', '012', '0945678901', '台北市中正區4號', 'Password4', '456-789-012', '567', '65432109', true),
    (5, 'seller5@example.com', 'JKL Enterprises', 'E5678901', 1500000, 'Charlie Chan', '0223456789', '345', '0956789012', '台北市中正區5號', 'Password5', '567-890-123', '678', '54321098', true);

   
   -- 插入修改过的假数据到 seller 表(沒有時間戳記)
INSERT INTO seller (sellerLvId, sellerEmail, sellerCompany, sellerTaxId, sellerCapital, sellerContact, sellerCompanyPhone, sellerCompanyExtension, sellerMobile, sellerAddress, sellerPassword, sellerBankAccount, sellerBankCode, sellerBankAccountNumber, isConfirm)
VALUES 
    (1, 'seller6@example.com', 'DEF Ltd.', 'F6789012', 1800000, 'David Wang', '0223456789', '678', '0967890123', '台北市中正區6號', 'Password6', '678901234', '789', '43210987', true),
    (2, 'seller7@example.com', 'GHI Corp.', 'G7890123', 2000000, 'Eva Chen', '0223456789', '901', '0978901234', '台北市中正區7號', 'Password7', '789012345', '890', '32109876', true),
    (3, 'seller8@example.com', 'ABC Company', 'H8901234', 2200000, 'Frank Lin', '0223456789', '123', '0989012345', '台北市中正區8號', 'Password8', '890123456', '901', '21098765', true),
    (4, 'seller9@example.com', 'JKL Enterprises', 'I9012345', 2500000, 'Grace Wu', '0223456789', '234', '0990123456', '台北市中正區9號', 'Password9', '901234567', '012', '10987654', true),
    (5, 'seller10@example.com', 'PQR Industries', 'J0123456', 2800000, 'Henry Chang', '0223456789', '345', '0911234567', '台北市中正區10號', 'Password10', '012345678', '123', '09876543', true);

-- 會員資料  放入測試資料-- 
INSERT INTO buyer (
    memberEmail, thirdFrom, memberName,
    memberPhone, memberMobile, memberBirthday,
    memberPassword, memberAddress, isMemberEmail,
    memberRegistrationTime, petName, petImg,
    petImgUploadTime, petVaccName1, petVaccTime1,
    petVaccName2, petVaccTime2
)
VALUES 
    ('buyer1@example.com', NULL, 'Buyer1', '1234567890', '0912345678', '1990-01-01', 'password1', 'Address1', true, '2023-01-01 10:00:00', 'Pet1', NULL, NULL, 'Vaccine1', '2023-02-01 12:30:00', 'Vaccine2', '2023-03-15 15:45:00'),
    ('buyer2@example.com', NULL, 'David', '0987654321', '0923456789', '1985-05-15', 'password2', 'Address2', true, '2023-02-01 11:30:00', '木木鴞', NULL, NULL, 'Vaccine3', '2023-04-05 09:15:00', 'Vaccine4', '2023-06-20 14:00:00'),
    ('buyer3@example.com', NULL, 'Buyer3', '0912345678', '0934567890', '1995-08-20', 'password3', 'Address3', true, '2023-03-01 14:45:00', 'Pet3', NULL, NULL, 'Vaccine5', '2023-07-10 16:30:00', 'Vaccine6', '2023-09-25 11:45:00'),
    ('buyer4@example.com', NULL, 'Buyer4', '0923456789', '0945678901', '1988-11-10', 'password4', 'Address4', true, '2023-04-01 09:15:00', 'Pet4', NULL, NULL, 'Vaccine7', '2023-11-01 08:00:00', 'Vaccine8', '2023-12-15 10:20:00'),
    ('buyer5@example.com', NULL, 'Buyer5', '0934567890', '0956789012', '1992-03-25', 'password5', 'Address5', true, '2023-05-01 12:00:00', 'Pet5', NULL, NULL, 'Vaccine9', '2024-01-05 13:45:00', 'Vaccine10', '2024-03-20 17:00:00'),
    ('buyer6@example.com', NULL, 'Buyer6', '0945678901', '0967890123', '1980-06-30', 'password6', 'Address6', true, '2023-06-01 15:30:00', 'Pet6', NULL, NULL, 'Vaccine11', '2024-04-10 09:30:00', 'Vaccine12', '2024-06-25 14:15:00'),
    ('buyer7@example.com', NULL, 'Buyer7', '0956789012', '0978901234', '1998-09-05', 'password7', 'Address7', true, '2023-07-01 18:45:00', 'Pet7', NULL, NULL, 'Vaccine13', '2024-07-20 11:00:00', 'Vaccine14', '2024-09-05 15:30:00'),
    ('buyer8@example.com', NULL, 'Buyer8', '0967890123', '0989012345', '1983-12-15', 'password8', 'Address8', true, '2023-08-01 21:00:00', 'Pet8', NULL, NULL, 'Vaccine15', '2024-10-01 14:45:00', 'Vaccine16', '2024-12-15 18:00:00'),
    ('buyer9@example.com', NULL, 'Buyer9', '0978901234', '0990123456', '1991-02-20', 'password9', 'Address9', true, '2023-09-01 09:30:00', 'Pet9', NULL, NULL, 'Vaccine17', '2025-01-15 17:15:00', 'Vaccine18', '2025-03-30 20:30:00'),
    ('buyer10@example.com', NULL, 'Buyer10', '0989012345', '0910234567', '1987-05-10', 'password10', 'Address10', true, '2023-10-01 12:45:00', 'Pet10', NULL, NULL, 'Vaccine19', '2025-04-20 10:00:00', 'Vaccine20', '2025-06-05 13:15:00');
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
INSERT INTO managerUser (managerUserName, managerPassword, managerPer, createtime)
VALUES 
  ('Luisa', 'adminpass1', 10, NOW()),
  ('Sakiko', 'adminpass2', 20, NOW()),
  ('Soyo', 'adminpass3', 10, NOW()),
  ('Dgeetia', 'adminpass4', 10, NOW()),
  ('Grand', 'adminpass5', 20, NOW()),
  ('Alen', 'adminpass6', 10, NOW()),
  ('Red', 'adminpass7', 10, NOW()),
  ('Timmiy', 'adminpass8', 20, NOW()),
  ('Fren', 'adminpass9', 10, NOW()),
  ('Lulia', 'adminpass10', 10, NOW());
-- 廣告  放入假資料-- 
INSERT INTO ad (adimg, adImgUploadTime, adName, adUrl, adStartTime, adEndTime, adLv, adMemo, isAdConfirm, adCreateTime, isEnabled)
VALUES 
  (NULL, NOW(), 'Test Ad 1', 'https://www.google.com/', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 'This is a test ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 2', 'https://www.google.com/', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 2, 'Another test ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 3', 'https://www.google.com/', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 3, 'Yet another test ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 4', 'https://www.google.com/', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 4, 'Test ad number four.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 5', 'https://www.google.com/', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 5, 'The last test ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 6', 'https://www.google.com/', NOW(), DATE('2023-12-13 00:00:00'), 6, 'Sample ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 7', 'https://www.google.com/', NOW(), DATE('2023-12-13 00:00:00'), 6, 'Sample ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 8', 'https://www.google.com/', NOW(), DATE('2023-12-13 00:00:00'), 6, 'Sample ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 9', 'https://www.google.com/', NOW(), DATE('2023-12-13 00:00:00'), 6, 'Sample ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 10', 'https://www.google.com/', NOW(), DATE('2023-12-13 00:00:00'), 6, 'Sample ad.', true, NOW(), true),
  (NULL, NOW(), 'Test Ad 11', 'https://www.google.com/', NOW(), DATE('2023-12-13 00:00:00'), 6, 'Sample ad.', true, NOW(), true);


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
  ('時間內正常顯示', 1, NOW(), DATE('2024-12-13 00:00:00'), true),
  ('時間內但不顯示', 2, NOW(), DATE('2024-12-13 00:00:00'), false),
  ('時間已經過了不會看到', 3, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), true),
  ('時間不會到,不會看到', 4, DATE('2024-12-13 00:00:00'), DATE_ADD(DATE('2024-12-13 00:00:00'), INTERVAL 30 DAY), true),
  ('時間內正常顯示,但id5排序10', 10, NOW(), DATE('2024-12-13 00:00:00'), true),
  ('Maintenance Alert: System Upgrade', 6, NOW(), DATE_ADD(NOW(), INTERVAL 10 DAY), true),
  ('Employee of the Month: John Doe', 7, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), true),
  ('Limited-time Offer: Free Shipping', 8, NOW(), DATE_ADD(NOW(), INTERVAL 20 DAY), true),
  ('Community Event: Charity Run', 9, NOW(), DATE_ADD(NOW(), INTERVAL 40 DAY), true),
  ('時間內正常顯示,但id10排序5', 5, NOW(), DATE('2024-12-13 00:00:00'), true);
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
INSERT INTO petdraw (memberid, memberpairid, ismemberlike, memberrestime, memberpairrestime, ismemberpairlike, petdrawtime, petdrawlog, petdrawlat)
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
  ('文章分類1'),
  ('文章分類2'),
  ('文章分類3'),
  ('文章分類4'),
  ('文章分類5');

-- 文章  放入測試資料-- 
INSERT INTO article (memberId, articleTitle, articleContent, artUpdateTime, articleLike, articleComment, articleShare, articleSort, isEnabled)
VALUES
    (1, '標題1', '這是文章1的內容。', '2023-01-05 10:20:00', 15, 8, 5, 1, TRUE),
    (2, '標題2', '這是文章2的內容。', '2023-02-10 14:45:00', 20, 12, 8, 2, FALSE),
    (3, '標題3', '這是文章3的內容。', '2023-03-15 18:30:00', 10, 5, 3, 3, TRUE),
    (4, '標題4', '這是文章4的內容。', '2023-04-20 08:10:00', 25, 15, 10, 4, FALSE),
    (5, '標題5', '這是文章5的內容。', '2023-05-25 12:50:00', 18, 10, 7, 5, TRUE);
-- 文章圖片  放入測試資料-- 
INSERT INTO articlePic (articleId, articlePicBlob, articlePicTime)
VALUES
    (1, NULL, '2023-01-05 10:30:00'),
    (2, NULL, '2023-02-10 15:00:00'),
    (3, NULL, '2023-03-15 19:00:00'),
    (4, NULL, '2023-04-20 08:30:00'),
    (5, NULL, '2023-05-25 13:00:00');
-- 文章影片  放入測試資料-- 
INSERT INTO articleVid (articleId, articleVidBlob, articleVidTime)
VALUES
    (1, NULL, '2023-01-05 10:45:00'),
    (2, NULL, '2023-02-10 15:15:00'),
    (3, NULL, '2023-03-15 19:30:00');
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
-- 商品  放入測試資料-- 
INSERT INTO product (sellerId, productCoverImg, productName, productPrice, productStockQuantity, productDetails, productStatus, productCreationTime, totalStars, totalReviews, productSort, isEnabled)
VALUES
    (1, NULL, '商品A', 100, 50, '這是商品A的描述', '上架', '2023-01-01 10:00:00', 4, 2, 1, TRUE),
    (1, NULL, '商品B', 150, 30, '這是商品B的描述', '上架', '2023-02-15 14:30:00', 5, 3, 2, FALSE),
    (2, NULL, '商品C', 80, 20, '這是商品C的描述', '下架', '2023-03-20 18:45:00', 3, 1, 3, FALSE),
    (2, NULL, '商品D', 120, 40, '這是商品D的描述', '上架', '2023-04-10 08:00:00', 4, 2, 4, TRUE),
    (3, NULL, '商品E', 200, 15, '這是商品E的描述', '上架', '2023-05-25 12:00:00', 5, 3, 5, TRUE);
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
INSERT INTO reply (articleId, memberId, replyContent, replyTime, replyLike, isEnabled)
VALUES
    (1, 1, '回覆1', '2023-01-05 11:30:00', 3, true),
    (1, 2, '回覆2', '2023-01-06 12:00:00', 1, False),
    (2, 3, '回覆3', '2023-02-10 16:30:00', 2, False),
    (2, 4, '回覆4', '2023-02-11 17:00:00', 0, true),
    (2, 5, '回覆5', '2023-02-12 10:00:00', 5, true);
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
-- 通知  放入測試資料-- 
INSERT INTO msg (memberId, msgType, msgContent, msgTime, isEnabled)
VALUES
    (1, 1, '您的訂單已經發貨，請注意查收', '2023-01-05 12:00:00', true),
    (2, 2, '您的會員資料已經更新', '2023-02-10 17:30:00', true),
    (3, 1, '新的促銷活動即將開始，請留意通知', '2023-03-15 14:45:00', true);
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
INSERT INTO orderDetails (orderDetailsId, productId, purchaseQuantity, isCommented, stars, commentedTime, comments, attachments, isEnable)
VALUES
    (1, 1, 2, TRUE, 4, '2023-01-01 12:30:00', 'Great orderDetailsproductOrderproduct!', NULL, TRUE),
    (1, 2, 1, FALSE, NULL, NULL, NULL, NULL, TRUE),
    (2, 1, 3, FALSE, NULL, NULL, NULL, NULL, TRUE),
    (2, 2, 1, TRUE, 5, '2023-02-15 14:45:00', 'Amazing!', NULL, TRUE),
    (3, 3, 1, FALSE, NULL, NULL, NULL, NULL, TRUE);


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
INSERT INTO report (reportMemberId, replyId, articleId, reportTypeId, reportTime, reportState, reportDealTime)
VALUES
    (1, 1, NULL, 3, '2023-01-05 12:00:00', 1, '2023-01-06 14:30:00'),
    (2, NULL, 2, 8, '2023-02-10 17:30:00', 0, NULL),
    (3, 3, NULL, 5, '2023-03-15 14:45:00', 1, '2023-03-16 10:00:00'),
    (4, NULL, 1, 10, '2023-04-20 09:15:00', 0, NULL),
    (5, 2, NULL, 6, '2023-05-25 18:30:00', 1, '2023-05-26 12:45:00');