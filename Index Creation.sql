-- Index for player table: Optimize lookups by username
CREATE INDEX idx_player_username ON player (username);

-- Index for character table: Optimize lookups by playerID and classID
CREATE INDEX idx_character_playerID ON character (playerID);
CREATE INDEX idx_character_classID ON character (classID);

-- Index for class table: Optimize lookups by name
CREATE INDEX idx_class_name ON class (name);

-- Index for item table: Optimize lookups by type and name
CREATE INDEX idx_item_type ON item (type);
CREATE INDEX idx_item_name ON item (name);

-- Index for quest table: Optimize lookups by itemRewardID and minXp
CREATE INDEX idx_quest_itemRewardID ON quest (itemRewardID);
CREATE INDEX idx_quest_minXp ON quest (minXp);

-- Index for hasCompleted table: Optimize lookups by questID and charID
CREATE INDEX idx_hasCompleted_questID ON hasCompleted (questID);
CREATE INDEX idx_hasCompleted_charID ON hasCompleted (charID);

-- Index for guild table: Optimize lookups by leaderCharID
CREATE INDEX idx_guild_leaderCharID ON guild (leaderCharID);

-- Index for guildMember table: Optimize lookups by guildID and charID
CREATE INDEX idx_guildMember_guildID ON guildMember (guildID);
CREATE INDEX idx_guildMember_charID ON guildMember (charID);

-- Index for characterHas table: Optimize lookups by charID and itemID
CREATE INDEX idx_characterHas_charID ON characterHas (charID);
CREATE INDEX idx_characterHas_itemID ON characterHas (itemID);