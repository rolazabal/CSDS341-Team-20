-- Use Case 2: Maximilian Schulten, Creating a new character
-- for a specific player based on their ID
CREATE OR ALTER PROCEDURE newCharacter
	@playerID INT,
	@classID INT,
	@charName varchar(50),
	@newID INT OUTPUT
AS
BEGIN
	-- Ensure playerID is valid
	IF NOT EXISTS (SELECT 1 FROM player WHERE playerID = @playerID) OR NOT EXISTS (SELECT 1 FROM class WHERE classID = @classID)
	BEGIN
		RAISERROR ('Invalid playerID/classID. Please check the values.', 16, 1);
		RETURN;
	END
	ELSE
	BEGIN	
		INSERT INTO character (playerID, charName, classID) 
		VALUES
		(@playerID, @charName, @classID)
		SELECT @newID = SCOPE_IDENTITY()
	END
END