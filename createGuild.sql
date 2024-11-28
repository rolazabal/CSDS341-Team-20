-- procedure to add a new
-- guild given a leader character and dateFounded
-- returns the ID of the created guild
CREATE OR ALTER PROCEDURE createGuild
   	@charID INT,
   	@dateFounded DATE,
   	@guildID INT OUTPUT
AS
BEGIN
   	-- make sure character is not in a guild
   	IF EXISTS (SELECT 1 FROM guildMember WHERE charID = @charID)
   	BEGIN
        RAISERROR ('Character already belongs to a guild. Leave current guild in order to create a new one.', 16, 1);
        RETURN;
   	END
   	ELSE
   	BEGIN
        -- add new tuple to guild table, reputation for new guilds
        --starts at 0
        INSERT INTO guild (dateFounded, reputation, leaderCharID)
        VALUES (@dateFounded, 0, @charID)
        SELECT @guildID = SCOPE_IDENTITY();
        -- add new tuple to guildMember
        INSERT INTO guildMember (guildID, charID, dateJoined)
        VALUES (@guildID, @charID, @dateFounded)
   	END
END;
