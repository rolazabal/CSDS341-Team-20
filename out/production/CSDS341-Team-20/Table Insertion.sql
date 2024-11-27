-- Insert players
INSERT INTO player (username, password)
VALUES
('PlayerOne', 'pass1'),
('PlayerTwo', 'pass2'),
('PlayerThree', 'pass3'),
('PlayerFour', 'pass4'),
('PlayerFive', 'pass5'),
('PlayerSix', 'pass6'),
('PlayerSeven', 'pass7'),
('PlayerEight', 'pass8'),
('PlayerNine', 'pass9'),
('PlayerTen', 'pass10');

-- Insert classes
INSERT INTO class (name, health, stamina, speed)
VALUES
('Warrior', 150, 100, 50),
('Mage', 100, 150, 60),
('Rogue', 120, 120, 80),
('Cleric', 130, 140, 40);

-- Insert characters
-- Ensure valid playerID and classID references
INSERT INTO character (playerID, charName, classID, xp)
VALUES
(0, 'HeroOne', 1, 100),
(0, 'HeroTwo', 2, 200),
(1, 'ShadowOne', 3, 150),
(1, 'ShadowTwo', 1, 180),
(2, 'MageMaster', 2, 300),
(2, 'SwordKing', 1, 100),
(3, 'RogueLord', 3, 250),
(3, 'Healer', 3, 220),
(4, 'BraveKnight', 1, 400),
(4, 'MagicMissile', 2, 120),
(5, 'SwiftBlade', 3, 230),
(6, 'HolyLight', 1, 270),
(7, 'DarkAssassin', 3, 310),
(8, 'FireWizard', 2, 190),
(9, 'Protector', 1, 300);

-- Insert items
INSERT INTO item (name, type, attack, defense, critChance, minXp)
VALUES
('Sword of Power', 'Weapon', 50, 10, 5.5, 0),
('Shield of Valor', 'Armor', 0, 40, 0.0, 0),
('Dagger of Stealth', 'Weapon', 30, 5, 10.0, 50),
('Staff of Wisdom', 'Weapon', 20, 0, 0.0, 100),
('Health Potion', 'Consumable', 0, 0, 0.0, 0),
('Mana Potion', 'Consumable', 0, 0, 0.0, 0),
('Helmet of Insight', 'Armor', 0, 20, 0.0, 50),
('Boots of Speed', 'Accessory', 0, 10, 0.0, 0),
('Ring of Strength', 'Accessory', 10, 5, 0.0, 100),
('Cape of Shadows', 'Accessory', 0, 15, 2.5, 200);

-- Assign items to characters
-- Ensure charID and itemID references exist
INSERT INTO characterHas (charID, itemID, quantity)
VALUES
(0, 0, 1),
(0, 4, 3),
(1, 3, 1),
(1, 5, 2),
(2, 2, 1),
(3, 1, 1),
(4, 8, 1),
(5, 6, 1),
(5, 7, 1),
(6, 9, 1),
(7, 3, 1),
(8, 0, 1),
(9, 2, 1);

INSERT INTO player (username, password)
VALUES
('PlayerEleven', 'pass11'),
('PlayerTwelve', 'pass12'),
('PlayerThirteen', 'pass13'),
('PlayerFourteen', 'pass14'),
('PlayerFifteen', 'pass15'),
('PlayerSixteen', 'pass16'),
('PlayerSeventeen', 'pass17'),
('PlayerEighteen', 'pass18'),
('PlayerNineteen', 'pass19'),
('PlayerTwenty', 'pass20');

-- Insert characters for the new 20 players
-- Ensure valid playerID and classID references
INSERT INTO character (playerID, charName, classID, xp)
VALUES
(10, 'KnightHunter', 1, 120), -- PlayerEleven
(10, 'ArcaneMage', 2, 250),
(11, 'SilentRogue', 3, 180), -- PlayerTwelve
(11, 'ClericOfLight', 0, 300),
(12, 'WarriorOfOld', 1, 220), -- PlayerThirteen
(12, 'MageFlame', 2, 310),
(13, 'StealthAssassin', 3, 200), -- PlayerFourteen
(13, 'Guardian', 0, 400),
(14, 'BattleMaster', 1, 150), -- PlayerFifteen
(14, 'WizardSupreme', 2, 270),
(15, 'QuickBlade', 3, 200), -- PlayerSixteen
(15, 'DivineHealer', 1, 340),
(16, 'SteelKnight', 1, 180), -- PlayerSeventeen
(16, 'FireSorcerer', 2, 320),
(17, 'ShadowThief', 3, 250), -- PlayerEighteen
(17, 'Priestess', 2, 280),
(18, 'DragonSlayer', 1, 300), -- PlayerNineteen
(18, 'FrostWizard', 2, 240),
(19, 'BladeDancer', 3, 230), -- PlayerTwenty
(19, 'HolyPaladin', 0, 290);

-- Assign items to characters for players 11 through 20
-- Ensure charID and itemID references exist
INSERT INTO characterHas (charID, itemID, quantity)
VALUES
(10, 0, 1), -- KnightHunter gets Sword of Power (itemID 0)
(10, 1, 1), -- KnightHunter gets Shield of Valor (itemID 1)
(11, 4, 1), -- ArcaneMage gets Staff of Wisdom (itemID 4)
(12, 3, 1), -- SilentRogue gets Dagger of Stealth (itemID 3)
(12, 7, 1), -- SilentRogue gets Helmet of Insight (itemID 7)
(13, 5, 3), -- ClericOfLight gets Health Potions (itemID 5)
(14, 8, 1), -- WarriorOfOld gets Ring of Strength (itemID 8)
(14, 6, 2), -- WarriorOfOld gets Mana Potions (itemID 6)
(15, 9, 1), -- MageFlame gets Boots of Speed (itemID 9)
(16, 2, 1), -- StealthAssassin gets Dagger of Stealth (itemID 2)
(17, 0, 1), -- Guardian gets Sword of Power (itemID 0)
(18, 1, 1), -- BattleMaster gets Shield of Valor (itemID 1)
(18, 4, 1), -- BattleMaster gets Staff of Wisdom (itemID 4)
(19, 7, 1), -- WizardSupreme gets Helmet of Insight (itemID 7)
(20, 8, 1), -- QuickBlade gets Ring of Strength (itemID 8)
(20, 5, 1), -- QuickBlade gets Health Potion (itemID 5)
(21, 9, 1), -- DivineHealer gets Boots of Speed (itemID 9)
(22, 2, 1), -- SteelKnight gets Dagger of Stealth (itemID 2)
(22, 1, 1); -- SteelKnight gets Shield of Valor (itemID 1)


-- Create guilds starting from guildID 0
INSERT INTO guild (dateFounded, reputation, leaderCharID)
VALUES
('2024-01-01', 50, 10), -- Guild 0 led by KnightHunter
('2024-02-15', 60, 14), -- Guild 1 led by WarriorOfOld
('2024-03-20', 80, 18), -- Guild 2 led by BattleMaster
('2024-04-10', 90, 22); -- Guild 3 led by SteelKnight

-- Assign characters to guilds
INSERT INTO guildMember (guildID, charID, dateJoined)
VALUES
(0, 11, CURRENT_TIMESTAMP), -- ArcaneMage joins Guild 0
(0, 12, CURRENT_TIMESTAMP), -- SilentRogue joins Guild 0
(1, 15, CURRENT_TIMESTAMP), -- MageFlame joins Guild 1
(1, 16, CURRENT_TIMESTAMP), -- StealthAssassin joins Guild 1
(2, 19, CURRENT_TIMESTAMP), -- WizardSupreme joins Guild 2
(2, 20, CURRENT_TIMESTAMP), -- QuickBlade joins Guild 2
(3, 23, CURRENT_TIMESTAMP), -- DivineHealer joins Guild 3
(3, 24, CURRENT_TIMESTAMP), -- SteelKnight joins Guild 3
(3, 22, CURRENT_TIMESTAMP); -- SteelKnight joins as leader

-- Insert 10 new items into the item table
INSERT INTO item (name, type, attack, defense, critChance, minXp)
VALUES
('Axe of Fury', 'Weapon', 60, 5, 6.0, 100),
('Dragon Shield', 'Armor', 0, 50, 0.0, 150),
('Bow of Precision', 'Weapon', 40, 0, 8.0, 80),
('Staff of Thunder', 'Weapon', 35, 0, 0.0, 120),
('Elixir of Strength', 'Consumable', 20, 0, 0.0, 0),
('Elixir of Defense', 'Consumable', 0, 20, 0.0, 0),
('Amulet of Vitality', 'Accessory', 0, 10, 2.0, 200),
('Boots of Agility', 'Accessory', 0, 5, 0.0, 50),
('Helmet of Brilliance', 'Armor', 0, 30, 0.0, 100),
('Cloak of Invisibility', 'Accessory', 0, 15, 5.0, 250);

-- Assign new items to characters
INSERT INTO characterHas (charID, itemID, quantity)
VALUES
(10, 10, 1), -- KnightHunter gets Axe of Fury
(11, 11, 1), -- ArcaneMage gets Dragon Shield
(12, 12, 1), -- SilentRogue gets Bow of Precision
(13, 13, 1), -- ClericOfLight gets Staff of Thunder
(14, 14, 2), -- WarriorOfOld gets Elixir of Strength
(15, 15, 3), -- MageFlame gets Elixir of Defense
(16, 16, 1), -- StealthAssassin gets Amulet of Vitality
(17, 17, 1), -- Guardian gets Boots of Agility
(18, 18, 1), -- BattleMaster gets Helmet of Brilliance
(19, 19, 1); -- WizardSupreme gets Cloak of Invisibility

INSERT INTO quest (itemRewardID, xpReward, objective, minXp)
VALUES
(0, 200, 'Defeat the Goblin King in the Forest of Shadows.', 50), -- Quest 1
(1, 300, 'Retrieve the Dragon Egg from the Volcanic Cavern.', 100), -- Quest 2
(2, 150, 'Steal the Rogue’s Dagger from the Black Market.', 70), -- Quest 3
(3, 250, 'Conquer the Storm Elemental in the Thunder Plains.', 120), -- Quest 4
(4, 100, 'Deliver the Elixir of Strength to the Village Elder.', 30), -- Quest 5
(5, 400, 'Protect the Castle from the Undead Siege.', 200), -- Quest 6
(6, 350, 'Retrieve the Amulet of Vitality from the Ancient Tomb.', 150), -- Quest 7
(7, 220, 'Find the Boots of Agility hidden in the Misty Swamp.', 80), -- Quest 8
(8, 180, 'Recover the Helmet of Brilliance from the Crystal Mines.', 90), -- Quest 9
(9, 500, 'Uncover the Cloak of Invisibility in the Forbidden Forest.', 250); -- Quest 10
(0, 40, 'Tutorial', 0) -- Quest 11

-- Assign quest completions to characters
INSERT INTO hasCompleted (questID, charID)
VALUES
(0, 10), -- KnightHunter completes Quest 1
(1, 11), -- ArcaneMage completes Quest 2
(2, 12), -- SilentRogue completes Quest 3
(3, 13), -- ClericOfLight completes Quest 4
(4, 14), -- WarriorOfOld completes Quest 5
(5, 15), -- MageFlame completes Quest 6
(6, 16), -- StealthAssassin completes Quest 7
(7, 17), -- Guardian completes Quest 8
(8, 18), -- BattleMaster completes Quest 9
(9, 19), -- WizardSupreme completes Quest 10
(0, 11), -- ArcaneMage also completes Quest 1
(2, 13), -- ClericOfLight also completes Quest 3
(4, 17), -- Guardian also completes Quest 5
(6, 19), -- WizardSupreme also completes Quest 7
(8, 10); -- KnightHunter also completes Quest 9

