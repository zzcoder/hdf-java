


			     NCSA HDFView 1.0
                            ------------------
			
	1) What it is
	2) Features
	3) Limitations
	4) System requirements
	5) Installation
        6) The files you should get
        7) For further information
______________________________________________________________________


1) What it is

The HDFView is a Java-based tool for browsing and editing NCSA HDF4 and 
HDF5 files. HDFView allows users to browse through any HDF4 and HDF5 file; 
starting with a tree view of all top-level objects in an HDF file's hierarchy. 
HDFView allows a user to descend through the hierarchy and navigate among 
the file's data objects. The content of a data object is loaded only when 
the object is selected, providing interactive and efficient access to HDF4 
and HDF5 files. HDFView editing features allow a user to create, delete, and 
modify the value of HDF objects and attributes.

2) Features

HDFView Version 1.1 provides basic browsing features for HDF4 and HDF5 files. 
The browsing features are listed below. Details of the features are discussed 
in their related chapters of the HDFView User's Guide.

  -> Open multiple HDF4 or HDF5 files. 
  -> Display file hierarchy in a tree and allow users to navigate among the file's data objects. 
  -> Show dataset as an image, a table or text. 
  -> Allow users to select a subset of dataset to display. 
  -> Show attributes and other general information of data objects. 
  -> Display line plot for selected table data, and histogram for selected image area. 
  -> Display simple compound dataset (non-nested compound datatype) in a table. 
  -> Support simple image manipulation: zooming in/out image, flipping image horizontally or vertically, and showing palette for index RGB image. 
  -> Allow to display any of three dimensions of a multiple dimension data set. 
  -> Allow users to specify stride of dataset selection. 
  -> Allow users to selection select fields/members of VData/compound dataet to display. 
  -> Display file information, such as file size, total number of datasets. 
  -> Change and save user options such as recent files, delimiter, and font size. 
  -> Save current file into the same type (HDF4 to HDF4, HDF5 to HDF5). 
  -> Add/delete object in the file (no deletion for HDF4). 
  -> Copy/paste object in the same file. 
  -> Copy/paste object between files but the same file format (HDF4 to HDF4, HDF5 to HDF5). 
  -> Add/delete a group and all its decendents (no deletion for HDF4). 
  -> Modify and save data values in spreadsheet. 
  -> Create new dataset with simple datatype such as SDS, atomic data array. 
  -> Copy/paste data values of dataset. 
  -> Save single dataset into ASCII text file. 
  -> Save a single image to JPEG file. 
  -> Add/delete attribute (no deletion for HDF4). 
  -> Modify and save attibute value. 

3) Limitations

Version 1.1 is the first release of the HDFView. It has the following limitations.

  -> HDFView does not support undo operation, all the changes will go to the physical file, and the editing results cannot be recovered. 
  -> HDFView cannot create/modify Vdata or compound dataset. 
  -> Deleting object/attribute in HDF4 is not supported. 
  -> HDFView does not support conversion between HDF4 and HDf5. 
  -> HDFView does not display dataset of nested compound datatypes. 
  -> HDFView does not display named datatypes. 
  -> HDFView supports only indexed image with RGB color model or 24-bit true color image. It does not support other color model such as CMYK and HSV. 
  -> Opening a large image or dataset may cause "out of memory" error due to the limitations of the Java virtual machine. 
  -> HDFView is implemented in JavaTM 2 Platform. It runs only at JDK 1.3.x. It does not work with JDK 1.1.x or JDK 1.2.x. 

4) System requirements

The Java 2 Runtime Environment, Sun JRE1.3.1, is bundled in this product.
No aditional software is required. 

Since the Java 2 Runtime Environment is bundled in the HDFView, download and 
installation the HDFView requires acceptance of the Java 2 Runtime Environment 
license agreement. Please read the Java Binary Code License Agreement at

http://java.sun.com/j2se/1.3/jre/j2re-1_3_1_02-license.html

HDFView 1.1 has been built and tested on Solaris, SGI IRIX 6.5, Linux,
and Windows 95/98/2000/NT

5) Installation

Install on Unix

After downloading installation file and,  cd to the directory where you 
downloaded the installer. At the prompt type:  

sh ./hdfview-xxx.bin 

where xxx is the Unix OS name such as solaris, linux or irix. The installer 
will guide you to select the Java Virtual Machine and HDFView home directory 
(where you want to install the HDFView). 

Install on Windows
After downloading, double-click 

hdfview-windows.exe
 
The installer will guide you to select the Java Virtual Machine and 
HDFView home directory (where you want to install the HDFView). After 
installing, you can lanuch the HDFView from 
Start -> Program Files -> NCSA HDFView

6) The files you should get

For Windows
    Readme.txt          - This readme file.
    Copyright           - The copyright information.
    hdfview.exe         - The excutable program to start the HDFView
    docs/UsersGuide     - The HDFView User's Guide
    jre/                - The Java 2 Runtime Environment: Sun JRE 1.3.1
    lib/
        jhdf.jar        - The jar file containing all the classes of the Java
                          wrapper of the HDF 4 library.
        jhdf5.jar       - The jar file containing all the classes of the Java
                          wrapper of the HDF 5 library.
        jhdfobj.jar     - The jar file containing all the Java classes of the 
                           HDF data objects.
        jhdfview.jar    - The jar file containing allthe Java classes of the GUI
                          components of the HDFView.
    lib/win
        jhdf.dll        - The dynamic link library of HDF 4.
        jhdf5.dll       - The dynamic link library of HDF 5.
    samples/            - Sample HDF 4 and HDF 5 files.
    UninstallerData/    - Files for uninstalling the HDfView.

For Unix
    Readme.txt          - This readme file.
    Copyright           - The copyright information.
    hdfview             - The shell script to start the HDFView
    docs/UsersGuide     - The HDFView User's Guide
    jre/                - The Java 2 Runtime Environment: Sun JRE 1.3.1
    lib/
        jhdf.jar        - The jar file containing all the classes of the Java
                          wrapper of the HDF 4 library.
        jhdf5.jar       - The jar file containing all the classes of the Java
                          wrapper of the HDF 5 library.
        jhdfobj.jar     - The jar file containing all the Java classes of the 
                           HDF data objects.
        jhdfview.jar    - The jar file containing allthe Java classes of the GUI
                          components of the HDFView.
    lib/(solaris, linux, or irix-6.5)
        jhdf.so         - The dynamic link library of HDF 4.
        jhdf5.so        - The dynamic link library of HDF 5.
    samples/            - Sample HDF 4 and HDF 5 files.
    UninstallerData/    - Files for uninstalling the HDfView.

7) For further information

General information about HDF and HDF4 and HDF5 are available at:
http://hdf.ncsa.uiuc.edu

Information about the HDFView tool and related products is available
http://hdf.ncsa.uiuc.edu/hdf-java-html

Questions and feedback may be sent to:
hdfhelp@ncsa.uiuc.edu

