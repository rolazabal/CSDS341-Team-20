-- New player creation (signup)
CREATE OR ALTER PROCEDURE newPlayer
	@uname varchar(50),
	@pw char(60)
AS
BEGIN
	INSERT INTO player(username, password)
	VALUES
	(@uname, @pw)
END