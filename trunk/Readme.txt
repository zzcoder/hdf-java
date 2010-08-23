
                               HDF-Java Products
===============================================================================



Features and changes
=======================================

For major changes, please read "About This Release" at 
http://www.hdfgroup.org/hdf-java-html/


Files included in the release
=======================================

1) The "hdfview/" directory contains the installation programs for easy
   download and installation for HDFView. The installation programs are
   named by platforms. For example, "hdfview_install_linux32.bin" if for 
   32-bit linux machines. After you install HDFView, you have the pre-built 
   binaries for all the HDF-Java products: JNI wrapper, object package, 
   and HDFView.

2) The "bin/" directory includes the pre-built binaries for all of the
    HDF-Java product. You can either download the tarball, which has all 
    the necessary binaries, or individual part of pre-built binaries. 
    We strongly recommend you to download the tarball instead of individual
    files.

3) The "src/" directory has all of the source code for the HDF-Java product.
    You will only need it if you are building HDF-Java from the source.


Build HDF-Java from the source
=======================================

Prerequisites:
    1. HDF4 binary distribution (source is not needed)
    2. HDF5 binary distribution (source is not needed)
    3. Java Development Kit 1.5.x or above
    4. External libraries: szip, zlib, jpeg (for HDF4 only)

Build on unix:
===================

1. Configure

Use the example configure file at Config/config.sh

The configure step must specify the paths to the required software and
where to install.  

  ./configure --prefix=INSTDIR \
    --with-hdf5=INC,LIB \
    --with-hdf4=INC,LIB --with-jdk=INC,LIB \
    --with-libjpeg=$JPEG/include,$JPEG/lib \
    --with-libsz=$SZIP/include,$SZIP/lib \
    --with-libz=$ZLIB/include,$ZLIB/lib \

Where
    --with-hdf5=INC,LIB = path to where the HDF5 include and library are installed,
        e.g., /usr/local/include,/usr/local/lib

    --with-hdf4=INC,LIB = path to where the HDF4.1r5 include and library are installed, 
        e.g., /usr/local/include,/usr/local/lib

    --with-jdk=INC,LIB = path to where the JDK1.3.1 include and library are installed, 
        e.g., /usr/local/jdk1.3.1/include,/usr/local/jdk1.3.1/jre/lib

    --prefix=INSTDIR = where to install the HDF java products, 
        e.g., /work/hdf-java

For convenience, Config/config.sh is provided as an example script for configuration.

2. Build

Build with the command 'make'.

To install, use 'make install'

To generate javadocs, 'make javadocs'


Build on W1ndows:
===================

1) Build Java source code
    a) configure/modify the batch file, windows/Makefile_java.bat
        i) set the %JAVAHOME% in the batch file. 
           %JAVAHOME% is the home directory of the Java Development Kit, 
           such as SET JAVAHOME=d:\java\jdk1.5.0
        ii) set the %HDFJAVA% in the batch file. 
           %HDFJAVA% is the directory of this HDF object source code, 
           such as SET HDFJAVA=D:\work\hdf-java
    b) run batch file, windows/Makefile_java.bat

2) Build the HDF4 native C code with Microsfot Visual C++
    a) modify make file, windows/jhdf.mak
    b) nmake /f jhdf.mak

3) Build the HDF5 native C code with Microsfot Visual C++
    a) modify make file, windows/jhdf5.mak
    b) nmake /f jhdf5.mak


For more Information, read
=======================================

  http://www.hdfgroup.org/hdf-java-html


For questions, send email to
=======================================

  help@hdfgroup.org


