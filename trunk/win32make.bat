@ECHO OFF

@REM =======================================================================
@REM                  batch file for compiling HDFView classes
@REM =======================================================================

@REM set up your java home directory, e.g. JAVAHOME=d:\java\jdk1.3.1
SET JAVAHOME=d:\java\jdk1.3.1

@REM set up HDFView home directory, e.g. HDFVIEW=e:\work\hdfview
SET HDFVIEW=e:\work\hdfview

@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH=%JAVAHOME%\bin
IF not "%OS%"=="Windows_NT" SET PATH="%JAVAHOME%\bin"
SET CLASSPATH=%HDFVIEW%\classes

IF not exist "%JAVAHOME%\bin\javac.exe" GOTO :NOJAVA
GOTO :COMPILE

:NOJAVA
@ECHO ON
@ECHO %JAVAHOME%\bin\javac.exe not found,
@ECHO please check your java home directory.
@PAUSE
@GOTO :END

:COMPILE

REM IF "%1"=="doc" GOTO :JAVADOC

GOTO :SOURCE

:JAVADOC

echo %HDFVIEW%\classes
javadoc -sourcepath %HDFVIEW%\src -d %HDFVIEW%\javadocs ncsa.hdf.object ncsa.hdf.view

@GOTO :END

:SOURCE
@REM COMPILE SOURCE FILES

REM DELETE OLD FILES
del %HDFVIEW%\classes\ncsa\hdf\object\*.*/Q
del %HDFVIEW%\classes\ncsa\hdf\view\*.*/Q
del %HDFVIEW%\classes\ncsa\hdf\view\icons\*.*/Q
mkdir %HDFVIEW%\classes

@ECHO compiling, please wait ....

SET CLASSPATH=%HDFVIEW%\classes;%HDFVIEW%\lib\jhdf.jar;%HDFVIEW%\lib\jhdf5.jar

@ECHO compiling "ncsa\hdf\object"
javac -d %HDFVIEW%\classes %HDFVIEW%\SRC\ncsa\hdf\object\*.java

@ECHO compiling "ncsa\hdf\view"
javac -d %HDFVIEW%\classes %HDFVIEW%\SRC\ncsa\hdf\view\*.java

@ECHO packing, please wait ....

REM delete old jar files
del %HDFVIEW%\lib\jhdfobj.jar/Q
del %HDFVIEW%\lib\jhdfview.jar/Q

cd %HDFVIEW%\classes
jar -cf jhdfobj.jar ncsa\hdf\object
move jhdfobj.jar %HDFVIEW%\lib 

jar -cf jhdfview.jar ncsa\hdf\view
move jhdfview.jar %HDFVIEW%\src
cd %HDFVIEW%\src
jar -uf jhdfview.jar ncsa\hdf\view\icons\*.gif
jar -uf jhdfview.jar ncsa\hdf\view\*.html
move jhdfview.jar %HDFVIEW%\lib 

cd %HDFVIEW%

:END

