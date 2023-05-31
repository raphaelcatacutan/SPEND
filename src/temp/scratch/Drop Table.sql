DECLARE @sql VARCHAR(100)

DECLARE db_cursor CURSOR FOR
SELECT name FROM sys.databases WHERE name LIKE '%spendbtest%'

OPEN db_cursor
FETCH NEXT FROM db_cursor INTO @name

WHILE @@FETCH_STATUS = 0
BEGIN
    SET @sql = 'DROP DATABASE ' + QUOTENAME(@name)
    EXEC (@sql)
    FETCH NEXT FROM db_cursor INTO @name
END

CLOSE db_cursor
DEALLOCATE db_cursor
