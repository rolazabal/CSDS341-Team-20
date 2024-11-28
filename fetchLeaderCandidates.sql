-- procedure to fetch the non-leader members  
-- of a given guild, along with some basic information about each member 
-- (name, class name, xp).
CREATE OR ALTER PROCEDURE fetchLeaderCandidates
	@charID INT,
	@guildID INT OUTPUT
AS
BEGIN
	-- make sure character is guild leader
	IF NOT EXISTS (SELECT 1 FROM guildMember INNER JOIN guild
	ON charID = leaderCharID AND charID = @charID)
	BEGIN
    	RAISERROR ('Character is not guild leader.', 1, 16);
    	RETURN;
	END
	ELSE
    BEGIN
		-- get member info
        SELECT m.charID as ID, charName as name, name as class, xp
        FROM guildMember as m INNER JOIN character as ch
        ON m.charID = ch.charID AND guildID = 0
        INNER JOIN class as c ON ch.classID = c.classID;
		-- register character's guild
		SET @guildID = (SELECT guildID from guildMember WHERE charID = @charID)
    END
END;