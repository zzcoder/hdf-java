
Unix Build Instructions

Prerequisites:

  1.  HDF4.1r5 binary distribution (source is not needed)
  2.  HDF5-1.4.3 binary distribution (source is not needed)
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

For more information:

  http://hdf.ncsa.uiuc.edu/hdf-java-html

  hdfhelp@ncsa.uiuc.edu


