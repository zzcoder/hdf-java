@ECHO OFF

@REM set up your jhv home directory, i.e. where you installed the JHV
SET JH45HOME=d:\data\rudbeckia-test\test

@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

@REM The windows directory, e.g. WINDIR=C:\WINNT\
SET WINDIR=C:\WINNT\

@REM set up your java (jdk1.3.x) home directory.
SET JAVAHOME=D:\Apps\jdk1.3
set LIBDIR=%JH45HOME%\lib


@REM Test if JH45 and java home directories are valid
rem IF not exist "%JH45HOME%\lib\jhdf.jar" SET JH45HOME=..
IF not exist "%JH45HOME%\lib\jhdf.jar" GOTO :NOJH45
IF not exist "%JH45HOME%\lib\jhdf5.jar" GOTO :NOJH45
IF not exist "%JH45HOME%\lib\jh4toh5.jar" GOTO :NOJH45
IF not exist "%JAVAHOME%\bin\java.exe" GOTO :NOJAVA
REM IF not exist "%JH45HOME%\lib\win32\jhdf.dll" GOTO :NOHDFDLL 
REM IF not exist "%JH45HOME%\lib\win32\jhdf5.dll" GOTO :NOHDFDLL 
IF not exist "%JH45HOME%\lib\win32\jh4toh5.dll" GOTO :NOHDFDLL 
GOTO :RUN

:NOJH45
@ECHO ON
@ECHO %JH45HOME%\lib\jhdf.jar not found,
@PAUSE
@GOTO :END

:NOJAVA
@ECHO ON
@ECHO %JAVAHOME%\bin\java.exe not found,
@PAUSE
@GOTO :END

:NOHDFDLL
@ECHO ON
@ECHO %JH45HOME%\lib\win32\jhdf.dll not found,
@PAUSE
@GOTO :END

:RUN

SET PATH=%JH45HOME%;"%JH45HOME%\lib\win32;%JAVAHOME%\bin;%WINDIR%\COMMAND"
IF "%OS%"=="Windows_NT" SET PATH=%JH45HOME%\lib\win32;%JAVAHOME%\bin;%JH45HOME%

SET CLASSPATH=%JH45HOME%\test\h4toh5;%JAVAHOME%\lib\rt.jar;%JAVAHOME%\lib\classes.zip;%JH45HOME%\lib\jhdf.jar;%JH45HOME%\lib\jhdf5.jar;%JH45HOME%\lib\jh4toh5.jar
%JAVAHOME%\bin\java -Djava.library.path="%LIBDIR%\win32" -Dncsa.hdf.h4toh5lib.h4toh5.h45lib="%LIBDIR%\win32\jh4toh5.dll" -Dncsa.hdf.hdflib.HDFLibrary.hdflib="%LIBDIR%\win32\jh4toh5.dll" -Dncsa.hdf.hdf5lib.h4toh5.hdf5lib="%LIBDIR%\win32\jh4toh5.dll" -classpath "%CLASSPATH%" imagetest
pause
:END

