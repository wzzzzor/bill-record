@echo off
set /p version=������汾�ţ�
cd..
call mvn version:set -DnewVersion=%version%

if "%errorlevel%"=="1" goto :initfailed
goto initsuccess

:initfailed
echo ���°汾��ʧ�ܣ�
pause
exit

:initsuccess
echo ���°汾�ųɹ���
pause