-- Fetch a characters info by their ID
CREATE OR ALTER PROCEDURE fetchCharacterInfo
	@charID INT
AS
BEGIN
	SELECT c.charName, cl.name as className, cl.health, cl.stamina, cl.speed FROM character c
	JOIN class cl ON c.classID = cl.classID
	WHERE c.charID = @charID
END