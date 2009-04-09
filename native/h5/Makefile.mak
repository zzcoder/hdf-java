# Makefile for building HDF5 JNI interface for Windows.
# 
# Author: Peter Cao
#           The HDF Group
#           Dec 30, 2008
#
# Notes: 
#   To comiple the source code, run 'nmake -f nt_makefile'
#   You may have add your VS studio bin path to your system path, e.g.
#   set path=%path";"C:\Program Files\Microsoft Visual Studio 8\VC\bin"
#

JAVAHOME=D:\Java\jdk1.6.0_12
HDF5INC=G:\HDF\lib\hdf5182\include
HDF5LIB=G:\HDF\lib\hdf5182\lib\hdf5.lib
SZLIB=G:\HDF\lib_external\szip20-win-xpnet-enc\lib\szlib.lib
ZLIB=G:\HDF\lib_external\zlib-121-windows\lib\zlib.lib

#####################################################################
#      Do not change below this line unless you know what you do    #
#####################################################################

CC=cl.exe
LINKER = link.exe

CFLAGS = \
/I $(HDF5INC) /I $(JAVAHOME)"\include" /I $(JAVAHOME)"\include\win32" \
/D "_MBCS" /U "__cplusplus" \
/D "_CRT_SECURE_NO_WARNINGS" /D "_LARGEFILE_SOURCE" \
/nologo /W3 /EHsc /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /FD /c /TP \

LINKFLAG=/nologo /dll /incremental:no /machine:i386 

OBJECTS=exceptionImp.obj h5util.obj H5constant.obj H5Dconstant.obj \
H5Econstant.obj H5Fconstant.obj H5Gconstant.obj H5Iconstant.obj \
H5Pconstant.obj H5Rconstant.obj H5Sconstant.obj H5Tconstant.obj H5Zconstant.obj \
H5.obj H5F.obj H5G.obj H5S.obj

MSDEV_LIBS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib \
advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib \
/NODEFAULTLIB:"MSVCRT" /NODEFAULTLIB:"LIBC.LIB"

all: $(OBJECTS)
	$(LINKER) /OUT:"jhdf5.dll" $(LINKFLAG) $(OBJECTS) $(HDF5LIB) $(SZLIB) $(ZLIB) $(MSDEV_LIBS)

exceptionImp.obj: exceptionImp.c
	$(CC) $(CFLAGS) /Fo$@ $?	

h5util.obj: h5util.c
	$(CC) $(CFLAGS) /Fo$@ $?	
        
H5constant.obj: H5constant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Dconstant.obj: H5Dconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Econstant.obj: H5Econstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Fconstant.obj: H5Fconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Gconstant.obj: H5Gconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Iconstant.obj: H5Iconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Pconstant.obj: H5Pconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Rconstant.obj: H5Rconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

	$(CC) $(CFLAGS) /Fo$@ $?

H5Sconstant.obj: H5Sconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Tconstant.obj: H5Tconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5Zconstant.obj: H5Zconstant.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5.obj: H5.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5F.obj: H5F.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5G.obj: H5G.c
	$(CC) $(CFLAGS) /Fo$@ $?

H5S.obj: H5S.c
	$(CC) $(CFLAGS) /Fo$@ $?

clean:
	@del *.dll
	@del *.lib
	@del *.obj
	@del *.idb
	@del *.pdb
	@del *.ilk
	@del *.pdb
	@del *.exp

