-- Fetching all of a players characters by their username
-- And some general information about them for the user
CREATE OR ALTER PROCEDURE fetchCharacters
	@username varchar(50)
AS
BEGIN
	SELECT c.charID, c.charName, c.xp, cl.name, cl.health, cl.speed, cl.stamina FROM player AS p
	JOIN character AS c ON p.playerID = c.playerID
	JOIN class AS cl ON cl.classID = c.classID
	WHERE p.username = @username
END