-- Fetch a characters completed quest based off of their ID.
CREATE OR ALTER PROCEDURE completedQuests
	@charID INT
AS
BEGIN
	SELECT q.objective, q.minXp, q.xpReward, i.name, i.attack, i.defense FROM quest q
	JOIN hasCompleted hc ON hc.questID = q.questID
	JOIN item i on i.itemID = q.itemRewardID
	WHERE hc.charID = @charID
END