# CS4280 Project
## Introduction
This is the project for CS4280.

## Workable Function
- Register Account
- Login
- Create New Hotel

## TODO
Write the TODO list.
- Add servlets/JSP for existing database interfaces [Login, Create Account, Manager site (check hotel room info)]
- Transfer data between pages (url-rewriting, hidden forms, cookie and session)
- Session control
- Synchronization

## Database

### User

|UserID|Username|Password|Name|Email|Tel|IsSubscribed|UserType|
|:----:|:------:|:------:|:--:|:---:|:-:|:----------:|:------:|
|int|varchar(255)|varchar(255)|varchar(255)|varchar(255)|varchar(255)|int|int|

### HotelRoom

|HotelID|RoomType|RoomName|StandardRate|NumOfRoom|RoomSize|
|:-----:|:------:|:------:|:----------:|:-------:|:------:|
|int|int|varchar(255)|int|int|int|

### Comments

|CommentID|OrderID|Content|Score|
|:-------:|:-----:|:-----:|:---:|
|int|int|varchar(6000)|int|

### HotelInfo

|HotelID|HotelName|Address|IsRecommended|StarRating|Label|
|:-----:|:-------:|:-----:|:-----------:|:--------:|:---:|
|int|varchar(255)|varchar(255)|int|int|varchar(255)|


### Manager

|RID|UserID|HotelID|
|:-:|:----:|:-----:|
|int|int|int|

### Order

|OrderID|Status|UserID|CIDate|CODate|HotelID|RoomType|NumOfRoom|
|:-----:|:----:|:----:|:----:|:----:|:-----:|:------:|:-------:|
|int|int|int|datetime|datetime|int|int|int|

### MemberBenefits

|RID|CommonUser|PreferredUser|GoldUser|PlantiumUser|WelcomeGift|LateCheckout|Breakfast|FreeWiFi|
|:-:|:--------:|:-----------:|:------:|:----------:|:---------:|:----------:|:-------:|:------:|
|int|int|int|int|int|int|int|int|int|
