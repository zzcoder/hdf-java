@ECHO OFF

@REM =======================================================================
@REM
@REM          Batch file to run the HDFView
@REM
@REM =======================================================================

@REM set up Java home directory (requires jdk1.5.0 or above), e.g. D:\java\jdk1.5.0
SET JAVAHOME=

@REM set up "HDF JAVA Product" home directory, e.g. D:\hdf-java
SET HDFJAVA=

@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH="%HDFJAVA%"\lib\win;"%HDFJAVA%"\lib\ext

@REM set the JNI classpath
set JNI_CLASSPATH="%HDFJAVA%"\lib\jhdf.jar;"%HDFJAVA%"\lib\jhdf5.jar

@REM set the object package classpath
set OBJ_CLASSPATH="%HDFJAVA%"\lib\jhdfobj.jar;"%HDFJAVA%"\lib\jhdf4obj.jar;"%HDFJAVA%"\lib\jhdf5obj.jar;"%HDFJAVA%"\lib\netcdf.jar;"%HDFJAVA%"\lib\fits.jar

@REM set the CLASSPATH
set CLASSPATH=%JNI_CLASSPATH%;%OBJ_CLASSPATH%;"%HDFJAVA%"\lib\jhdfview.jar

"%JAVAHOME%\bin\java" -mx1024m -Djava.library.path=%PATH% -Dhdfview.root="%HDFJAVA%" -classpath %CLASSPATH% ncsa.hdf.view.HDFView -root "%HDFJAVA%"
