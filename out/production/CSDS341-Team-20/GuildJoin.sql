-- Join guild Procedure
CREATE OR ALTER PROCEDURE joinGuild
	@charID int,
	@guildID int
AS
BEGIN
	insert into guildMember(charID, guildID)
	VALUES
	(@charID, @guildID)
END