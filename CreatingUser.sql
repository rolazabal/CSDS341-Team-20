
-- Create a SQL Server login for the user
CREATE LOGIN proj
WITH PASSWORD = 'ProjPass1234!', CHECK_POLICY = OFF;
GO

CREATE USER proj
FOR LOGIN proj;
GO

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON SCHEMA::dbo TO proj;
GO
