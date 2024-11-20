-- These are the queries that were run to create the database tables
CREATE TABLE player (
    playerID INT PRIMARY KEY IDENTITY(0,1),
    username VARCHAR(50) UNIQUE NOT NULL,
    password CHAR(60) NOT NULL
);

CREATE TABLE class (
    classID INT PRIMARY KEY IDENTITY(0,1),
    name VARCHAR(50) NOT NULL,
    health INT,
    stamina INT,
    speed INT
);

CREATE TABLE character (
    charID INT PRIMARY KEY IDENTITY(0,1),
    playerID INT NOT NULL REFERENCES player(playerID),
    charName VARCHAR(50) NOT NULL,
    classID INT NOT NULL REFERENCES class(classID),
    xp INT DEFAULT 0
);

CREATE TABLE item (
    itemID INT PRIMARY KEY IDENTITY(0,1),
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50),
    attack INT DEFAULT 0,
    defense INT DEFAULT 0,
    critChance DECIMAL(5,2) DEFAULT 0.0,
    minXp INT DEFAULT 0
);

CREATE TABLE quest (
    questID INT PRIMARY KEY IDENTITY(0,1),
    itemRewardID INT REFERENCES item(itemID),
    xpReward INT DEFAULT 0,
    objective TEXT,
    minXp INT DEFAULT 0
);

CREATE TABLE hasCompleted (
    questID INT REFERENCES quest(questID),
    charID INT REFERENCES character(charID),
    PRIMARY KEY (questID, charID)
);

CREATE TABLE guild (
    guildID INT PRIMARY KEY IDENTITY(0,1),
    dateFounded DATE,
    reputation INT DEFAULT 0,
    leaderCharID INT UNIQUE REFERENCES character(charID)
);

CREATE TABLE guildMember (
    guildID INT REFERENCES guild(guildID),
    charID INT REFERENCES character(charID),
	dateJoined DATE,
    PRIMARY KEY (guildID, charID),
    UNIQUE (charID)
);

CREATE TABLE characterHas (
    charID INT REFERENCES character(charID),
    itemID INT REFERENCES item(itemID),
    quantity INT DEFAULT 1,
    PRIMARY KEY (charID, itemID)
);
