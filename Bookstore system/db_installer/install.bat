chcp 1251
@echo off
REM install_db.bat

set DB_NAME=bookstore
set DB_USER=postgres
set PGPASSWORD=9534218

echo Создание БД %DB_NAME%...

psql -U %DB_USER% -c "DROP DATABASE IF EXISTS %DB_NAME%;"
psql -U %DB_USER% -c "CREATE DATABASE %DB_NAME%;"

echo Применение схемы...

psql -U %DB_USER% -d %DB_NAME% -f DDL_scripts.sql

echo Заполнение данными...
psql -U %DB_USER% -d %DB_NAME% -f DML_scripts.sql

echo БД установлена!
pause