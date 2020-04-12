@echo off
if "%1"=="" goto nopar
if not exist disk1.dat goto nofile
cls
echo **** Installing Aladdin PC ****
lha x /c disk1.dat %1
if errorlevel 1 goto unpackerr

echo. 
echo DISK 1 installed correctly
echo Please insert DISK 2 then
:checkdisk2
pause
if exist disk2.dat goto unpack2

:notdisk2
echo.
echo ERROR - Could not find DISK 2.
echo Please make sure DISK 2 is
echo inserted correctly and then 
goto checkdisk2

:unpack2
lha x /c disk2.dat %1
if errorlevel 1 goto unpackerr

cls
echo The installation process is complete.
echo.
echo After changing to the correct drive and directory, type
echo SETSND        to set up sound options or type
echo ALADDIN       to play the game.
echo.
goto end

:unpackerr
echo.
echo There was an error during installation.
echo Are you sure you specified the drive and path
echo correctly including the final \
echo.
echo e.g.  INSTALL C:\ALADDIN\
echo.
goto end

:nopar
echo.
echo You must type the drive & directory where you wish to install Aladdin
echo.
echo e.g.  INSTALL C:\ALADDIN\
echo.
echo Remember to include the '\' at the end.
echo.
goto end

:nofile
echo.
echo You must run the installation program
echo from the installation disk drive.
echo Change to the correct drive and try again.
echo.
goto end:

:end
