@ECHO OFF

@REM =======================================================================
@REM
@REM          Batch file to compile the HDF Java source code
@REM
@REM =======================================================================

@REM set up your java home directory(requires jdk1.4.1 or above), for example
@REM SET JAVAHOME=d:\java\jdk1.4.1
SET JAVAHOME=

@REM set up "HDF JAVA Product" home directory, for example
@REM SET HDFJAVA=D:\work\hdf-java
SET HDFJAVA=


@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH=%JAVAHOME%\bin
IF not "%OS%"=="Windows_NT" SET PATH="%JAVAHOME%\bin"
SET CLASSPATH=%HDFJAVA%\lib\jhdfobj.jar;%HDFJAVA%\projects\java\classes

IF not exist "%JAVAHOME%\bin\javac.exe" GOTO :NOJAVA
GOTO :COMPILE

:NOJAVA
@ECHO ON
@ECHO %JAVAHOME%\bin\javac.exe not found,
@ECHO please check your java home directory.
@PAUSE
@GOTO :END

:COMPILE

@REM COMPILE SOURCE FILES

@REM DELETE OLD FILES
del %HDFJAVA%\classes\ncsa\hdf\hdflib\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\hdf5lib\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\hdf5lib\exceptions\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\object\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\object\h4\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\object\h5\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\object\nc2\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\object\fits\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\view\*.*/Q
del %HDFJAVA%\classes\ncsa\hdf\view\icons\*.*/Q
mkdir %HDFJAVA%\classes\

@ECHO compiling, please wait ....

SET CLASSPATH=%HDFJAVA%;%HDFJAVA%\classes;%HDFJAVA%\lib\netcdf.jar;%HDFJAVA%\lib\fits.jar

@ECHO compiling "ncsa\hdf\hdflib", please wait ....
javac -d %HDFJAVA%\classes %HDFJAVA%\glguerin\io\*.java
javac -d %HDFJAVA%\classes %HDFJAVA%\glguerin\mac\io\*.java
javac -d %HDFJAVA%\classes %HDFJAVA%\glguerin\util\*.java
javac -d %HDFJAVA%\classes %HDFJAVA%\ncsa\hdf\hdflib\*.java

@ECHO compiling "ncsa\hdf\hdf5lib", please wait ....
javac -d %HDFJAVA%\classes %HDFJAVA%\ncsa\hdf\hdf5lib\*.java
javac -d %HDFJAVA%\classes %HDFJAVA%\ncsa\hdf\hdf5lib\exceptions\*.java

@ECHO compiling "ncsa\hdf\object", please wait ....
javac -d %HDFJAVA%\classes\ %HDFJAVA%\ncsa\hdf\object\*.java

@ECHO compiling "ncsa\hdf\object\h4", please wait ....
javac -d %HDFJAVA%\classes\ %HDFJAVA%\ncsa\hdf\object\h4\*.java

@ECHO compiling "ncsa\hdf\object\h5", please wait ....
javac -d %HDFJAVA%\classes\ %HDFJAVA%\ncsa\hdf\object\h5\*.java

@ECHO compiling "ncsa\hdf\object\nc2", please wait ....
javac -d %HDFJAVA%\classes\ %HDFJAVA%\ncsa\hdf\object\nc2\*.java

@ECHO compiling "ncsa\hdf\object\fits", please wait ....
javac -d %HDFJAVA%\classes\ %HDFJAVA%\ncsa\hdf\object\fits\*.java

@ECHO compiling "ncsa\hdf\view", please wait ....
javac -d %HDFJAVA%\classes\ %HDFJAVA%\ncsa\hdf\view\*.java

@ECHO packing, please wait ....

REM delete old jar files
del %HDFJAVA%\lib\jhdf.jar/Q
del %HDFJAVA%\lib\jhdf5.jar/Q
del %HDFJAVA%\lib\jhdfobj.jar/Q
del %HDFJAVA%\lib\jhdf4obj.jar/Q
del %HDFJAVA%\lib\jhdf5obj.jar/Q
del %HDFJAVA%\lib\ext\nc2obj.jar/Q
del %HDFJAVA%\lib\ext\fitsobj.jar/Q
del %HDFJAVA%\lib\jhdfview.jar/Q

cd %HDFJAVA%\classes
jar -cf jhdf.jar ncsa\hdf\hdflib glguerin
jar -cf jhdf5.jar ncsa\hdf\hdf5lib
jar -cf jhdfobj.jar ncsa\hdf\object\*.class
jar -cf jhdf4obj.jar ncsa\hdf\object\h4\*.class
jar -cf jhdf5obj.jar ncsa\hdf\object\h5\*.class
jar -cf nc2obj.jar ncsa\hdf\object\nc2\*.class
jar -cf fitsobj.jar ncsa\hdf\object\fits\*.class
jar -cf jhdfview.jar ncsa\hdf\view\*.class

mkdir %HDFJAVA%\lib
mkdir %HDFJAVA%\lib\ext
move jhdf.jar %HDFJAVA%\lib
move jhdf5.jar %HDFJAVA%\lib
move jhdfobj.jar %HDFJAVA%\lib
move jhdf4obj.jar %HDFJAVA%\lib
move jhdf5obj.jar %HDFJAVA%\lib
move nc2obj.jar %HDFJAVA%\lib\ext
move fitsobj.jar %HDFJAVA%\lib\ext
move jhdfview.jar %HDFJAVA%

cd %HDFJAVA%
jar -uf jhdfview.jar ncsa\hdf\view\icons\*.gif
jar -uf jhdfview.jar ncsa\hdf\view\*.html
move jhdfview.jar %HDFJAVA%\lib 

cd %HDFJAVA%

:END

PAUSE

