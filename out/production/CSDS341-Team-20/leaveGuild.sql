-- Character leaving their guild
CREATE OR ALTER PROCEDURE leaveGuild
    @charID INT
AS
BEGIN
    -- Check if the character is in a guild
    IF NOT EXISTS (SELECT 1 FROM guildMember WHERE charID = @charID)
    BEGIN
        RAISERROR('Character is not in any guild.', 16, 1);
        RETURN;
    END

    -- Check if the character is the leader of the guild
    DECLARE @guildID INT;
    DECLARE @isLeader BIT;

    SELECT 
        gm.guildID,
        CASE 
            WHEN g.leaderCharID = @charID THEN 1 
            ELSE 0 
        END AS isLeader
    FROM guildMember gm
    JOIN guild g ON gm.guildID = g.guildID
    WHERE gm.charID = @charID;

    IF @isLeader = 1
    BEGIN
        RAISERROR('Character is the leader of the guild. Transfer leadership or dissolve the guild before leaving.', 16, 1);
        RETURN;
    END

    -- Remove the character from the guild
    DELETE FROM guildMember
    WHERE charID = @charID;

    PRINT 'Character successfully left the guild.';
END;
