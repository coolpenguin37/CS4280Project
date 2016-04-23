DELETE FROM [User];
DBCC CHECKIDENT ([User], RESEED, 0)
INSERT INTO [User] VALUES ('useralice', 'pwalice', 'alice', 'alice@cityu.edu.hk', '12345678', '1', '1');
INSERT INTO [User] VALUES ('userbob', 'pwbob', 'bob', 'bob@cityu.edu.hk', '12345678', '0', '1');
INSERT INTO [User] VALUES ('usereve', 'pweve', 'eve', 'eve@cityu.edu.hk', '12345678', '1', '2');
SELECT * FROM [User];

DELETE FROM [HotelInfo];
DBCC CHECKIDENT ([HotelInfo], RESEED, 0)
INSERT INTO [HotelInfo] VALUES ('Smirnoff', 'Moscow', 0, 4);
INSERT INTO [HotelInfo] VALUES ('ICON', 'Hong Kong', 1, 5);
INSERT INTO [HotelInfo] VALUES ('Park Hyatt', 'Tokyo', 1, 5);
SELECT * FROM [HotelInfo];

SELECT [Order.OrderID], [Comment.CommentID], [Comment.Content], [Comment.Score]
FROM Order INNER JOIN [Comment]
ON [Order.OrderID] = [Comment.OrderID]
WHERE [Order.HotelID] IN 
(SELECT [HotelInfo.HotelID] FROM [HotelInfo] WHERE [HotelName] = ?)


CREATE TABLE [Manager] 
(RID int IDENTITY(1,1) NOT NULL, 
UserID int NOT NULL, 
HotelID int NOT NULL);

DELETE FROM [Manager];
DBCC CHECKIDENT ([Manager], RESEED, 0);
INSERT INTO [Manager] VALUES ('1', '2');
INSERT INTO [Manager] VALUES ('2', '1');
INSERT INTO [Manager] VALUES ('3', '2');
SELECT * FROM [Manager];

CREATE TABLE [MemberBenefits] 
(RID int IDENTITY(1,1) NOT NULL, 
HotelID int NOT NULL,
CommonUser int NOT NULL,
PreferredUser int NOT NULL,
GoldUser int NOT NULL,
PlantiumUser int NOT NULL,
WelcomeGift int NOT NULL,
LateCheckout int NOT NULL,
Breakfast int NOT NULL,
RoomUpgrade int NOT NULL,
);

DELETE FROM [MemberBenefits];
DBCC CHECKIDENT ([MemberBenefits], RESEED, 0);
INSERT INTO [MemberBenefits] VALUES (1, 100, 95, 90, 85, 2, 3, 4 ,4);
INSERT INTO [MemberBenefits] VALUES (2, 100, 100, 90, 90, 3, 3, 4, 4);
INSERT INTO [MemberBenefits] VALUES (3, 100, 100, 100, 100, 4, 4, 4, 4);
SELECT * FROM [MemberBenefits];

ALTER TABLE [Comment] ADD date [Date] NULL;
EXEC sp_RENAME 'MemberBenefits.PlantiumUser', 'PlatinumUser', 'COLUMN'
