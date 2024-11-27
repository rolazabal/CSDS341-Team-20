DECLARE @newCharacterID INT;

EXEC newCharacter
    @playerID = 1,        -- Replace with a valid playerID
    @classID = 2,         -- Replace with a valid classID
    @charName = 'My New Character', -- Replace with the desired character name
    @newID = @newCharacterID OUTPUT;

-- Display the new character ID
SELECT * FROM character WHERE charID = @newCharacterID;