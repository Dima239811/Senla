@echo off
REM Настройки подключения к PostgreSQL
SET PGPASSWORD=postgres
SET PGUSER=postgres
SET PGDATABASE=bookstore
SET PGHOST=localhost
SET PGPORT=5432

REM Создание базы данных (если её нет)
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -c "CREATE DATABASE %PGDATABASE%;" 2>nul

REM Импорт схемы (CREATE TABLE)
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d %PGDATABASE% -f createTable.sql

REM Импорт тестовых данных (INSERT)
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d %PGDATABASE% -f insertData.sql

echo База данных и тестовые данные успешно созданы!
pause
