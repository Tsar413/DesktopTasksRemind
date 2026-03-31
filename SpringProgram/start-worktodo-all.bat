@echo off
title Work To Do Start

set PORT=8098
set SPRING_DIR=F:\DesktopWorkToDoShell\SpringProgram
set DESKTOP_DIR=F:\DesktopWorkToDoShell\Desktop
set JAR=study-workToDo-0.0.1-SNAPSHOT.jar
set SPRING_PID_FILE=%SPRING_DIR%\spring.pid
set ELECTRON_PID_FILE=%DESKTOP_DIR%\electron.pid

cd /d %SPRING_DIR%

echo Checking port %PORT% ...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%PORT% ^| findstr LISTENING') do (
    echo Killing process on port %PORT% : PID=%%a
    taskkill /F /PID %%a >nul 2>nul
)

echo Closing existing Electron windows ...
taskkill /F /IM electron.exe >nul 2>nul

timeout /t 2 /nobreak >nul

if not exist "%SPRING_DIR%\%JAR%" (
    echo Jar not found: %SPRING_DIR%\%JAR%
    pause
    exit /b
)

echo Starting Spring Boot ...
powershell -NoProfile -Command "$p = Start-Process -FilePath 'javaw' -ArgumentList '-jar','%SPRING_DIR%\%JAR%' -WorkingDirectory '%SPRING_DIR%' -PassThru; $p.Id | Out-File -Encoding ascii '%SPRING_PID_FILE%'"

echo Waiting for Spring Boot ...
timeout /t 10 /nobreak >nul

netstat -ano | findstr :%PORT% | findstr LISTENING >nul
if errorlevel 1 (
    echo Spring Boot did not start successfully on port %PORT%.
    pause
    exit /b
)

echo Starting desktop shell ...
cd /d %DESKTOP_DIR%
powershell -NoProfile -Command "$p = Start-Process -FilePath 'cmd.exe' -ArgumentList '/c','npm start' -WorkingDirectory '%DESKTOP_DIR%' -WindowStyle Hidden -PassThru; $p.Id | Out-File -Encoding ascii '%ELECTRON_PID_FILE%'"

timeout /t 4 /nobreak >nul
start "" http://localhost:%PORT%/work

echo Started.
exit
