@ECHO OFF

@REM =======================================================================
@REM                 JHV run batch file for Windows 95/98/NT
@REM =======================================================================


@REM To run this Java-based HDF Viewer (JHV), You need jdk1.1.x. 

@REM set up your jhv home directory, i.e. where you installed the JHV
@SET HDFVIEW_HOME=I:\ModularHDFView
@SET JAVAHOME=D:\Java\jdk1.4.1

@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH=%HDFVIEW_HOME%\lib\win;%PATH%

set CLASSPATH=%HDFVIEW_HOME%/lib/jhdf.jar;%HDFVIEW_HOME%/lib/jhdf5.jar;%HDFVIEW_HOME%/lib/jhdfobj.jar;%HDFVIEW_HOME%/lib/netcdf.jar;%HDFVIEW_HOME%//lib/fits.jar
set CLASSPATH=%CLASSPATH%;%HDFVIEW_HOME%/lib/jhdf4obj.jar;%HDFVIEW_HOME%/lib/jhdf5obj.jar;%HDFVIEW_HOME%/lib/jhdfview.jar

%JAVAHOME%\bin\java -mx512m -classpath %CLASSPATH% ncsa.hdf.view.HDFView -root %HDFVIEW_HOME%
pause
:END
