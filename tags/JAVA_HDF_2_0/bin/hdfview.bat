@ECHO OFF

@REM =======================================================================
@REM
@REM          Batch file to run the HDFView
@REM
@REM =======================================================================

@REM set up your java home directory(requires jdk1.4.1 or above), for example
@REM SET JAVAHOME=d:\java\jdk1.4.1
SET JAVAHOME=d:\java\jdk1.4.1

@REM set up "HDF JAVA Product" home directory, for example
@REM SET HDFJAVA=D:\work\hdf-java
SET HDFJAVA=D:\work\hdf-java


@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH=%HDFJAVA%\lib\win;%PATH%

@REM set the JNI classpath
set JNI_CLASSPATH=%HDFJAVA%/lib/jhdf.jar;%HDFJAVA%/lib/jhdf5.jar

@REM set the object package classpath
set OBJ_CLASSPATH=%HDFJAVA%/lib/jhdfobj.jar;%HDFJAVA%/lib/jhdf4obj.jar;%HDFJAVA%/lib/jhdf5obj.jar;%HDFJAVA%\lib\netcdf.jar;%HDFJAVA%\lib\fits.jar

@REM set the CLASSPATH
set CLASSPATH=%JNI_CLASSPATH%;%OBJ_CLASSPATH%;%HDFJAVA%/lib/jhdfview.jar

%JAVAHOME%\bin\java -mx512m -classpath %CLASSPATH% ncsa.hdf.view.HDFView -root %HDFJAVA%
pause
:END
