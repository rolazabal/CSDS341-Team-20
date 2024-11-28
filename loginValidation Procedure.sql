-- Validating a users login request
CREATE PROCEDURE validateLogin
    @username VARCHAR(50),
    @password CHAR(60)
AS
BEGIN
    -- Declare variables to hold the result
    DECLARE @playerID INT;
    DECLARE @resultMessage NVARCHAR(100);

    -- Check if the username and password match
    SELECT @playerID = playerID
    FROM player
    WHERE username = @username AND password = @password;

    IF @playerID IS NOT NULL
    BEGIN
        -- Successful login
        SELECT @playerID AS playerID, 'Login successful' AS resultMessage;
    END
    ELSE
    BEGIN
        -- Login failed
        SELECT -1 AS playerID, 'Invalid username or password' AS resultMessage;
    END
END;
