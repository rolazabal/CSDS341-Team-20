-- Fetch a characters info by their ID
CREATE OR ALTER PROCEDURE fetchInventory
	@charID INT
AS
BEGIN
	SELECT i.name, i.attack, i.defense, i.critChance, i.type, ch.quantity FROM character c
	JOIN characterHas ch ON ch.charID = c.charID
	JOIN item i ON i.itemID = ch.itemID
	WHERE ch.charID = @charID
END