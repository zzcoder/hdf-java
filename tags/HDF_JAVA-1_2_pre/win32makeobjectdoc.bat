@ECHO OFF

@REM =======================================================================
@REM                  batch file for compiling HDFView classes
@REM =======================================================================

@REM set up your java home directory, e.g. JAVAHOME=d:\java\jdk1.2.2 
SET JAVAHOME=d:\java\jdk1.3.1

@REM set up your JHI5 home directory, e.g. HDFVIEW=d:\work\JavaHDF5\java-hdf5.
SET HDFVIEW=e:\work\hdfview

@REM Do not make changes under this line unless you know what you are doing.
@REM =======================================================================

SET PATH=%JAVAHOME%\bin
IF not "%OS%"=="Windows_NT" SET PATH="%JAVAHOME%\bin"
SET CLASSPATH=%HDFVIEW%\classes

IF not exist "%JAVAHOME%\bin\javac.exe" GOTO :NOJAVA

GOTO :JAVADOC
IF "%1"=="doc" GOTO :JAVADOC

GOTO :COMPILE

:NOJAVA
@ECHO ON
@ECHO %JAVAHOME%\bin\javac.exe not found,
@ECHO please check your java home directory.
@PAUSE
@GOTO :END

@REM RUN JAVA DOCS

:JAVADOC

echo %HDFVIEW%\classes
javadoc -sourcepath %HDFVIEW%\src -d %HDFVIEW%\javadocs ncsa.hdf.object

@GOTO :END

@REM COMPILE SOURCE FILES
:COMPILE

REM DELETE OLD FILES
del %HDFVIEW%\classes\ncsa\hdf\lib\*.*/Q;
del %HDFVIEW%\classes\ncsa\hdf\lib\exceptions\*.*/Q;
del %HDFVIEW%\classes\ncsa\hdf\object\*.*/Q;
del %HDFVIEW%\classes\ncsa\hdf\io\*.*/Q;
del %HDFVIEW%\classes\ncsa\hdf\view\*.*/Q;
mkdir %HDFVIEW%\classes

@ECHO compiling, please wait ....

@ECHO compiling "ncsa\hdf\hdf5lib"
javac -d %HDFVIEW%\classes %HDFVIEW%\SRC\ncsa\hdf\lib\*.java
javac -d %HDFVIEW%\classes %HDFVIEW%\SRC\ncsa\hdf\lib\exceptions\*.java

@ECHO compiling "ncsa\hdf\hdf5objects"
javac -d %HDFVIEW%\classes %HDFVIEW%\SRC\ncsa\hdf\object\*.java

@ECHO compiling "ncsa\hdf\io"
javac -d %HDFVIEW%\classes %HDFVIEW%\SRC\ncsa\hdf\io\*.java

@ECHO compiling "ncsa\hdf\view"
javac -d %HDFVIEW%\classes %HDFVIEW%\SRC\ncsa\hdf\h5view\*.java

pause

:END
