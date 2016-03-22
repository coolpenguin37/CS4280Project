# CS4280 Project
## Introduction
This is the project for CS4280.
## TODO
Write the TODO list.
- Add servlets/JSP for existing database interfaces [Login, Create Account, Manager site (check hotel room info)]
- Transfer data between pages (url-rewriting, hidden forms, cookie and session)
- Session control
- Synchronization
## Database
- User
    * UserID, int
    * Username, varchar(255)
    * Password, varchar(255)
    * Name, varchar(255)
    * Email, varchar(255), allow nulls
    * Tel, varchar(255), allow nulls
    * IsSubscribed, int
    * UserType, int
- HotelRoom
    * HotelID, int
    * RoomType, int
    * RoomName, varchar(255)
    * StandardRate, int
    * NumOfRoom, int
    * RoomSize, int
- Comment
    * CommentID, int
    * OrderID, int
    * Content, varchar(6000)
    * Score, int
- HotelInfo
    * HotelID, int
    * Name, varchar(255)
    * Location, varchar(255)
    * IsRecommended, int
    * StarRating, int
- Manager
    * UserID, int
    * UserType, int
- Order
    * OrderID, int
    * Status, int
    * UserID, int
    * CIDate, datetime
    * CODate, datetime
    * HotelID, int
    * RoomType, int
    * NumOfRoom, int
- Location (Location, HotelID)