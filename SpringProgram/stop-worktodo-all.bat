@echo off
title Work To Do Stop

set PORT=8098
set SPRING_DIR=F:\DesktopWorkToDoShell\SpringProgram
set DESKTOP_DIR=F:\DesktopWorkToDoShell\Desktop
set SPRING_PID_FILE=%SPRING_DIR%\spring.pid
set ELECTRON_PID_FILE=%DESKTOP_DIR%\electron.pid

echo Stopping desktop shell ...
if exist "%ELECTRON_PID_FILE%" (
    set /p EPID=<"%ELECTRON_PID_FILE%"
    if not "%EPID%"=="" (
        taskkill /F /PID %EPID% >nul 2>nul
    )
    del /f /q "%ELECTRON_PID_FILE%" >nul 2>nul
)

taskkill /F /IM electron.exe >nul 2>nul

echo Stopping Spring Boot ...
if exist "%SPRING_PID_FILE%" (
    set /p SPID=<"%SPRING_PID_FILE%"
    if not "%SPID%"=="" (
        taskkill /F /PID %SPID% >nul 2>nul
    )
    del /f /q "%SPRING_PID_FILE%" >nul 2>nul
)

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%PORT% ^| findstr LISTENING') do (
    taskkill /F /PID %%a >nul 2>nul
)

echo Done.
exit
