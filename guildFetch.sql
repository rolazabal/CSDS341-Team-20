-- fetch the guild a player is in and its leader.
CREATE OR ALTER PROCEDURE fetchGuild
	@charID INT
AS
BEGIN
	SELECT c.charName as leader, dateFounded, reputation FROM guildMember gm
	JOIN GUILD g ON gm.guildID = g.guildID
	JOIN character c on c.charID = g.leaderCharID
	WHERE gm.charID = @charID
END