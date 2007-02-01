@ECHO OFF

@REM =======================================================================
@REM
@REM          Batch file to run the HDFView
@REM
@REM =======================================================================

@REM set up your java home directory(requires jdk1.4.1 or above), for example
SET JAVAHOME=C:\Program Files\ncsa\hdfview 2.4\jre

@REM set up "HDF JAVA Product" home directory, for example
@REM SET HDFJAVA=D:\work\hdf-java
SET HDFJAVA=C:\Program Files\ncsa\hdfview 2.4

@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH="%HDFJAVA%"\lib\win;"%HDFJAVA%"\lib\ext

@REM set the JNI classpath
set JNI_CLASSPATH="%HDFJAVA%"\lib\jhdf.jar;"%HDFJAVA%"\lib\jhdf5.jar

@REM set the object package classpath
set OBJ_CLASSPATH="%HDFJAVA%"\lib\jhdfobj.jar;"%HDFJAVA%"\lib\jhdf4obj.jar;"%HDFJAVA%"\lib\jhdf5obj.jar;"%HDFJAVA%"\lib\netcdf.jar;"%HDFJAVA%"\lib\fits.jar

@REM set the CLASSPATH
set CLASSPATH=%JNI_CLASSPATH%;%OBJ_CLASSPATH%;"%HDFJAVA%"\lib\jhdfview.jar

"%JAVAHOME%\bin\java" -mx999m -Djava.library.path=%PATH% -Dhdfview.root="%HDFJAVA%" -classpath %CLASSPATH% ncsa.hdf.view.HDFView -root "%HDFJAVA%"
