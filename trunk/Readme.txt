The hdf-java product has been built and tested on:

   Windows NT/2000
   Solaris2.7
   Linux 2.2.10
   Irix6.5

This code will likely work on most platforms that support HDF and Java,
although the configure and Makefile may need to be adjusted.

This code does not compile on HP_UX 11, due to limitations in the HP
Java Native Interface.



Unix Build Instructions
=======================

Prerequisites:

  1.  HDF4.1r5 binary distribution (source is not needed)
  2.  HDF5-1.4.4 binary distribution (source is not needed)
  3.  Java Development Kit 1.3.1 or equivalent.  (JDK 1.2 and above will
      work on most systems.)

Build:

1. Configure

The configure step must specify the paths to the required software and
where to install.  

  ./configure --with-hdf5=INC,LIB --with-hdf4=INC,LIB \
	--with-jdk=INC,LIB --prefix=INSTDIR

--with-hdf5=INC,LIB = path to where the HDF5 include and library are installed,
        e.g., /usr/local/include,/usr/local/lib

--with-hdf4=INC,LIB = path to where the HDF4.1r5 include and library are 
        installed, e.g., /usr/local/include,/usr/local/lib

--with-jdk=INC,LIB = path to where the JDK1.3.1 include and library are 
        installed, e.g., /usr/local/jdk1.3.1/include,/usr/local/jdk1.3.1/jre/lib

--prefix=INSTDIR = where to install the HDF java products, e.g., /work/hdf-java

2. Build

Build with the command 'make'.

To install, use 'make install'

To generate javadocs, 'make javadocs'


Windows Build Instructions
=======================

Prerequisites:

  1.  HDF4.1r5 binary distribution (source is not needed)
  2.  HDF5-1.4.4 binary distribution (source is not needed)
  3.  Java Development Kit 1.3.1 or equivalent.  (JDK 1.2 and above will
      work on most systems.)

Build:

1. Configure

  a) set the %JAVAHOME% in the batch file, Makefile.bat. %JAVAHOME% is the home 
     directory of the Java Development Kit 1.3.1 or equivalent, such as
     d:\apps\java\jdk1.3.1
  b) set the %HDFOBJECT% in the batch file, Makefile.bat. %HDFOBJECT% is the 
     directory of this HDF object source code, such as
     d:\hdf-java

2. Build the HDF4 native C code with Microsfot Visual C++
  a) Start Visula C++ and create a new project of Win32 Dynamic-Link Library by
     File -> New -> Projects -> select Win32 Dynamic-Link Library and choose
     your project path, such as d:\hdf-java\projects\jni, and project name, jhdf
  b) After you create an empty project, add the HDF4 JNI source code into the
     project by
     Project -> Add To Project -> Files -> add all the HDF4 JNI source such as
     d:\hdf-java\native\hdflib\*.c
  c) Set the release project to active by
     Build -> Set Active Configuration.. -> Select Win32 Release
  d) Set include directories by
     Project -> C/C++ -> Preprocessor -> under Additional include directories, type
     i)  the HDF4 include directories, 
         such as d:\MyHDFStuff\hdf41r5\release\include
     ii) the Java jni include files, 
         such as D:\java\jdk1.3.1\include,D:\java\jdk1.3.1\include\win32
  e) Disable Multithread option by 
     Project -> C/C++ -> Preprocessor -> under Project Options:
     Change MT to ML
  f) Set the HDF4 static link library by
     Project -> Link -> General -> under Object/library modules:
      i) make sure the output file name is Release/jhdf.dll
     ii) delete the default setting and add the HDF4 static library,
     such as d:\MyHDFStuff\HDF41r5\lib\hd415.lib d:\MyHDFStuff\HDF41r5\lib\hm415.lib ws2_32.lib 
  g) Build the project
     After you you build the jhdf.dll, copy Release\jhdf.dll to
     %HDFOBJECT%\lib\win\jhdf.dll

3. Build the HDF5 native C code with Microsfot Visual C++
  a) Start Visula C++ and create a new project of Win32 Dynamic-Link Library by
     File -> New -> Projects -> select Win32 Dynamic-Link Library and choose
     your project path, such as d:\hdf-java\projects\jni5, and project name, jhdf5
  b) After you create an empty project, add the HDF5 JNI source code into the
     project by
     Project -> Add To Project -> Files -> add all the HDF4 JNI source such as
     d:\hdf-java\native\hdf5lib\*.c
  c) Set the release project to active by
     Build -> Set Active Configuration.. -> Select Win32 Release
  d) Set include directories by
     Project -> C/C++ -> Preprocessor -> under Additional include directories, type
     i)  the HDF5 include directories, 
         such as d:\MyHDFStuff\hdf514r3\release\include
     ii) the Java jni include files, 
         such as D:\java\jdk1.3.1\include,D:\java\jdk1.3.1\include\win32
  e) Disable Multithread option by 
     Project -> C/C++ -> Preprocessor -> under Project Options:
     Change MT to ML
  f) Set the HDF5 static link library by
     Project -> Link -> General -> under Object/library modules:
      i) make sure the output file name is Release/jhdf.dll
     ii) delete the default setting and add the HDF5 static library,
     such as d:\MyHDFstuff\HDF5143\c\release\lib\hdf5.lib  
  g) Build the project
     After you you build the jhdf.dll, copy Release\jhdf5.dll to
     %HDFOBJECT%\lib\win\jhdf5.dll    

4. Build

Run the batch file, Makefile.bat.

The binary distribution for Windows will be created at 
%HDFOBJECT%\windows



For More Information:
====================

  http://hdf.ncsa.uiuc.edu/hdf-java-html

  hdfhelp@ncsa.uiuc.edu


at 
%HDFOBJECT%\windows



For More Information:
====================

  http://hdf.ncsa.uiuc.edu/hdf-java-html

  hdfhelp@ncsa.uiuc.edu


