@echo off
set /p version=请输入版本号：
cd..
call mvn version:set -DnewVersion=%version%

if "%errorlevel%"=="1" goto :initfailed
goto initsuccess

:initfailed
echo 更新版本号失败！
pause
exit

:initsuccess
echo 更新版本号成功！
pause