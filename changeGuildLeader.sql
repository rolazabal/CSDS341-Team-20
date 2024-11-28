-- procedure to transfer
-- leadership of a guild to a given charID
CREATE OR ALTER PROCEDURE changeGuildLeader
   	@guildID INT,
   	@charID INT,
	@newLeadCharID INT OUTPUT
AS
BEGIN
   	-- make sure new leader is an existing member of the given guild
   	IF NOT EXISTS (SELECT 1 FROM guildMember
   	WHERE guildID = @guildID AND charID = @charID)
   	BEGIN
        RAISERROR ('Leadership must be transferred to another member of this guild.', 1, 16);
        RETURN;
   	END
   	-- make sure new leader is not already the leader
   	ELSE IF EXISTS (SELECT 1 FROM guild WHERE leaderCharID = @charID)
   	BEGIN
        RAISERROR ('Character is already the leader of this guild.', 1, 16);
        RETURN;
   	END
   	ELSE
   	BEGIN
        -- update guild table
        UPDATE guild
        SET leaderCharID = @charID
        WHERE guildID = @guildID;
		SELECT @newLeadCharID = @charID
   	END
END;