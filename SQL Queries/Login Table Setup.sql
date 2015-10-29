Create Table Biometrix.dbo.LoginTable
(UserID	INT	IDENTITY PRIMARY KEY,
 Username NVARCHAR(20) UNIQUE,
 Password NVARCHAR(30) )