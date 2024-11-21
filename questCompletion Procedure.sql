-- Use Case 1: Maximilian Schulten, awarding players the rewards linked
-- with a completed quest
CREATE PROCEDURE questCompletion
    @charID INT,
    @questID INT
AS
BEGIN
    -- Fetch rewards from the quest
    DECLARE @xpReward INT, @itemRewardID INT, @minXP INT, @charXP INT;

    SELECT @xpReward = xpReward, @itemRewardID = itemRewardID, @minXP = minXp
    FROM quest
    WHERE questID = @questID;

	SELECT @charXP = xp FROM character

	IF @charXP >= @minXP
	BEGIN
		-- Update character XP
		UPDATE character
		SET xp = xp + @xpReward
		WHERE charID = @charID;

		-- Add item reward to character inventory
		IF @itemRewardID IS NOT NULL
		BEGIN
			IF EXISTS (SELECT 1 FROM characterHas WHERE charID = @charID AND itemID = @itemRewardID)
			BEGIN
				UPDATE characterHas
				SET quantity = quantity + 1
				WHERE charID = @charID AND itemID = @itemRewardID;
			END
			ELSE
			BEGIN
				INSERT INTO characterHas (charID, itemID, quantity)
				VALUES (@charID, @itemRewardID, 1);
			END
		END;

		-- Record quest completion
		INSERT INTO hasCompleted (questID, charID)
		VALUES (@questID, @charID);
	END
	ELSE
	BEGIN
		RAISERROR('Character does not meet minimum XP Requirement', 16,1)
	END
END;
