@ECHO OFF

@REM ========================================================================
@ECHO                Batch file for compiling HDF object classes

@ECHO    This file is used to compile the Java source code of the HDF object 
@ECHO    package and pack the binary distribution for Wondows. It does not 
@ECHO    compile the native C source for the Java wrapper. Before you use
@ECHO    file to pack the binary distribution, you need to compile the 
@ECHO    native C source of the HDF4 wrapper and HDF5 wrapper. 

@ECHO    Before you run this file, you need
@ECHO        1) set the %JAVAHOME%, which is the home directory of the
@ECHO           Java virtual machine. The HDF object package reqires jdk1.3.x.
@ECHO        2) set the %HDFOBJECT%, which is the directory of the HDF object
@ECHO           source code.
@ECHO        1) compile the native C source of the HDF4 and HDF5 wrapper and
@ECHO           move jhdf.dll and jhdf5.dll to %HDFOBJECT%\lib\win\

@ECHO    After you run this file, the binary ditribution will be created and
@ECHO    located at %HDFOBJECT%\windows
@REM ========================================================================

PAUSE

@REM set up your java home directory, e.g. JAVAHOME=d:\java\jdk1.3.1
SET JAVAHOME=d:\java\jdk1.3.1

@REM set up HDFOBJECT home directory, e.g. HDFOBJECT=e:\work\HDFOBJECT
SET HDFOBJECT=e:\work\hdf-java

@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH=%JAVAHOME%\bin;%PATH%
IF not "%OS%"=="Windows_NT" SET PATH="%JAVAHOME%\bin;%PATH%"
SET CLASSPATH=%HDFOBJECT%\classes

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

REM DELETE OLD FILES
del %HDFOBJECT%\classes\ncsa\hdf\hdflib\*.*/Q
del %HDFOBJECT%\classes\ncsa\hdf\hdf5lib\*.*/Q
del %HDFOBJECT%\classes\ncsa\hdf\hdf5lib\exceptions\*.*/Q
del %HDFOBJECT%\classes\ncsa\hdf\object\*.*/Q

mkdir %HDFOBJECT%\classes
mkdir %HDFOBJECT%\lib

@ECHO compiling, please wait ....

SET CLASSPATH=%HDFOBJECT%;%HDFOBJECT%\classes

@ECHO compiling "glguerin"
javac -d %HDFOBJECT%\classes %HDFOBJECT%\glguerin\io\*.java
javac -d %HDFOBJECT%\classes %HDFOBJECT%\glguerin\mac\io\*.java
javac -d %HDFOBJECT%\classes %HDFOBJECT%\glguerin\util\*.java

@ECHO compiling "ncsa\hdf\hdflib"
javac -d %HDFOBJECT%\classes %HDFOBJECT%\ncsa\hdf\hdflib\*.java

@ECHO compiling "ncsa\hdf\hdf5lib"
javac -d %HDFOBJECT%\classes %HDFOBJECT%\ncsa\hdf\hdf5lib\*.java

@ECHO compiling "ncsa\hdf\hdf5lib\exceptions"
javac -d %HDFOBJECT%\classes %HDFOBJECT%\ncsa\hdf\hdf5lib\exceptions\*.java

@ECHO compiling "ncsa\hdf\object"
javac -d %HDFOBJECT%\classes %HDFOBJECT%\ncsa\hdf\object\*.java

@ECHO packing, please wait ....

REM delete old jar files
del %HDFOBJECT%\lib\jhdf.jar/Q
del %HDFOBJECT%\lib\jhdf5.jar/Q
del %HDFOBJECT%\lib\jhdfobj.jar/Q

cd %HDFOBJECT%\classes
jar -cf jhdf.jar ncsa\hdf\hdflib glguerin
jar -cf jhdf5.jar ncsa\hdf\hdf5lib
jar -cf jhdfobj.jar ncsa\hdf\object

move jhdf.jar %HDFOBJECT%\lib 
move jhdf5.jar %HDFOBJECT%\lib 
move jhdfobj.jar %HDFOBJECT%\lib 

cd %HDFOBJECT%

mkdir %HDFOBJECT%\windows
mkdir %HDFOBJECT%\windows\hdf-java
mkdir %HDFOBJECT%\windows\hdf-java\lib
mkdir %HDFOBJECT%\windows\hdf-java\docs

copy %HDFOBJECT%\Copying %HDFOBJECT%\windows\hdf-java
xcopy %HDFOBJECT%\lib %HDFOBJECT%\windows\hdf-java\lib/E
xcopy %HDFOBJECT%\docs %HDFOBJECT%\windows\hdf-java\docs/E

cd %HDFOBJECT%\windows\hdf-java
jar -cf hdf-java-windows.jar *.*
move hdf-java-windows.jar %HDFOBJECT%\windows

cd %HDFOBJECT%

pause

:END

