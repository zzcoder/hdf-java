#
# Copyright by The HDF Group.
# Copyright by the Board of Trustees of the University of Illinois.
# All rights reserved.
#
# This file is part of HDF Java Products. The full HDF Java copyright       *
# notice, including terms governing use, modification, and redistribution,  *
# is contained in the file, COPYING.  COPYING can be found at the root of   *
# the source code distribution tree. You can also access it online  at      *
# http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
# access to the file, you may request a copy from help@hdfgroup.org.        *
##
## Makefile.am
## Run automake to generate a Makefile.in from this file.
##
# 
# This makefile mostly just reinvokes make in the various subdirectories
# but does so in the correct order.  You can alternatively invoke make from
# each subdirectory manually.
#

include $(top_srcdir)/config/commence.am

SUFFIXES: .java .class

CLASSPATH = $(srcdir)
all: h4 h5

h4_PATH = hdf/h4
h4_SOURCES = $(h4_PATH)/HDFException.java \
        $(h4_PATH)/HDFArray.java \
        $(h4_PATH)/HDFChunkInfo.java \
        $(h4_PATH)/HDFCompInfo.java \
        $(h4_PATH)/HDFConstants.java \
        $(h4_PATH)/HDFDeflateCompInfo.java \
        $(h4_PATH)/HDFDeprecated.java \
        $(h4_PATH)/HDFIMCOMPCompInfo.java \
        $(h4_PATH)/HDFJPEGCompInfo.java \
        $(h4_PATH)/HDFJavaException.java \
        $(h4_PATH)/HDFLibrary.java \
        $(h4_PATH)/HDFLibraryException.java \
        $(h4_PATH)/HDFNBITChunkInfo.java \
        $(h4_PATH)/HDFNBITCompInfo.java \
        $(h4_PATH)/HDFNativeData.java \
        $(h4_PATH)/HDFNewCompInfo.java \
        $(h4_PATH)/HDFNotImplementedException.java \
        $(h4_PATH)/HDFOldCompInfo.java \
        $(h4_PATH)/HDFOldRLECompInfo.java \
        $(h4_PATH)/HDFRLECompInfo.java \
        $(h4_PATH)/HDFSKPHUFFCompInfo.java \
        $(h4_PATH)/HDFSZIPCompInfo.java \
        $(h4_PATH)/HDFTable.java 

h4_CLASSES = $(h4_PATH)/HDFException.class \
        $(h4_PATH)/HDFArray.class \
        $(h4_PATH)/HDFChunkInfo.class \
        $(h4_PATH)/HDFCompInfo.class \
        $(h4_PATH)/HDFConstants.class \
        $(h4_PATH)/HDFDeflateCompInfo.class \
        $(h4_PATH)/HDFDeprecated.class \
        $(h4_PATH)/HDFIMCOMPCompInfo.class \
        $(h4_PATH)/HDFJPEGCompInfo.class \
        $(h4_PATH)/HDFJavaException.class \
        $(h4_PATH)/HDFLibrary.class \
        $(h4_PATH)/HDFLibraryException.class \
        $(h4_PATH)/HDFNBITChunkInfo.class \
        $(h4_PATH)/HDFNBITCompInfo.class \
        $(h4_PATH)/HDFNativeData.class \
        $(h4_PATH)/HDFNewCompInfo.class \
        $(h4_PATH)/HDFNotImplementedException.class \
        $(h4_PATH)/HDFOldCompInfo.class \
        $(h4_PATH)/HDFOldRLECompInfo.class \
        $(h4_PATH)/HDFRLECompInfo.class \
        $(h4_PATH)/HDFSKPHUFFCompInfo.class \
        $(h4_PATH)/HDFSZIPCompInfo.class \
        $(h4_PATH)/HDFTable.class 


h4_doc: $(h4_SOURCES)
	javadoc -author $(h4_SOURCES)
        
h4: $(h4_CLASSES)

h5_PATH = hdf/h5

h5_constant_SOURCES = $(h5_PATH)/constants/H5_define.java \
	$(h5_PATH)/constants/H5constant.java  \
	$(h5_PATH)/constants/H5Dconstant.java  \
	$(h5_PATH)/constants/H5Econstant.java \
	$(h5_PATH)/constants/H5F_define.java  \
	$(h5_PATH)/constants/H5Fconstant.java  \
	$(h5_PATH)/constants/H5Gconstant.java \
        $(h5_PATH)/constants/H5I_define.java  \
	$(h5_PATH)/constants/H5Iconstant.java  \
	$(h5_PATH)/constants/H5L_define.java  \
	$(h5_PATH)/constants/H5Pconstant.java \
        $(h5_PATH)/constants/H5Rconstant.java  \
	$(h5_PATH)/constants/H5S_define.java  \
	$(h5_PATH)/constants/H5Sconstant.java \
        $(h5_PATH)/constants/H5T_define.java  \
	$(h5_PATH)/constants/H5Tconstant.java  \
	$(h5_PATH)/constants/H5Z_define.java  \
	$(h5_PATH)/constants/H5Zconstant.java

h5_constant_CLASSES = $(h5_PATH)/constants/H5_define.class \
	$(h5_PATH)/constants/H5constant.class \
	$(h5_PATH)/constants/H5Dconstant.class \
	$(h5_PATH)/constants/H5Econstant.class \
        $(h5_PATH)/constants/H5F_define.class \
        $(h5_PATH)/constants/H5Fconstant.class \
        $(h5_PATH)/constants/H5Gconstant.class \
        $(h5_PATH)/constants/H5I_define.class \
        $(h5_PATH)/constants/H5Iconstant.class \
        $(h5_PATH)/constants/H5L_define.class \
        $(h5_PATH)/constants/H5Pconstant.class \
        $(h5_PATH)/constants/H5Rconstant.class \
        $(h5_PATH)/constants/H5S_define.class \
        $(h5_PATH)/constants/H5Sconstant.class \
        $(h5_PATH)/constants/H5T_define.class \
        $(h5_PATH)/constants/H5Tconstant.class \
        $(h5_PATH)/constants/H5Z_define.class \
        $(h5_PATH)/constants/H5Zconstant.class

h5_enums_SOURCES = $(h5_PATH)/enums/H5_INDEX.java \
	$(h5_PATH)/enums/H5_ITER.java \
	$(h5_PATH)/enums/H5C_cache_decr_mode.java \
        $(h5_PATH)/enums/H5C_cache_incr_mode.java \
        $(h5_PATH)/enums/H5C_cache_flash_incr_mode.java \
        $(h5_PATH)/enums/H5D_ALLOC_TIME.java \
        $(h5_PATH)/enums/H5D_CHUNK.java \
        $(h5_PATH)/enums/H5D_FILL_TIME.java \
        $(h5_PATH)/enums/H5D_FILL_VALUE.java \
        $(h5_PATH)/enums/H5D_LAYOUT.java \
        $(h5_PATH)/enums/H5D_SPACE_STATUS.java \
        $(h5_PATH)/enums/H5E_DIRECTION.java \
        $(h5_PATH)/enums/H5E_TYPE.java \
        $(h5_PATH)/enums/H5F_CLOSE_DEGREE.java \
        $(h5_PATH)/enums/H5F_LIBVER.java \
        $(h5_PATH)/enums/H5F_SCOPE.java \
        $(h5_PATH)/enums/H5FD_MEM.java \
        $(h5_PATH)/enums/H5G_STORAGE_TYPE.java \
        $(h5_PATH)/enums/H5I_TYPE.java \
        $(h5_PATH)/enums/H5L_TYPE.java \
        $(h5_PATH)/enums/H5O_TYPE.java \
        $(h5_PATH)/enums/H5R_TYPE.java \
        $(h5_PATH)/enums/H5S_CLASS.java \
        $(h5_PATH)/enums/H5S_SEL.java \
        $(h5_PATH)/enums/H5S_SELECT_OPER.java \
        $(h5_PATH)/enums/H5T_BKG.java \
        $(h5_PATH)/enums/H5T_CLASS.java \
        $(h5_PATH)/enums/H5T_CMD.java \
        $(h5_PATH)/enums/H5T_CONV_EXCEPT.java \
        $(h5_PATH)/enums/H5T_CONV_RET.java \
        $(h5_PATH)/enums/H5T_CSET.java \
        $(h5_PATH)/enums/H5T_DIR.java \
        $(h5_PATH)/enums/H5T_NORM.java \
        $(h5_PATH)/enums/H5T_ORDER.java \
        $(h5_PATH)/enums/H5T_PAD.java \
        $(h5_PATH)/enums/H5T_PERS.java \
        $(h5_PATH)/enums/H5T_SGN.java \
        $(h5_PATH)/enums/H5T_STR.java \
        $(h5_PATH)/enums/H5Z_CB.java \
        $(h5_PATH)/enums/H5Z_EDC.java \
        $(h5_PATH)/enums/H5Z_FILTER.java \
        $(h5_PATH)/enums/H5Z_SO_SCALE.java

h5_enums_CLASSES = $(h5_PATH)/enums/H5_INDEX.class \
	$(h5_PATH)/enums/H5_ITER.class \
	$(h5_PATH)/enums/H5C_cache_decr_mode.class \
        $(h5_PATH)/enums/H5C_cache_incr_mode.class \
        $(h5_PATH)/enums/H5C_cache_flash_incr_mode.class \
        $(h5_PATH)/enums/H5D_ALLOC_TIME.class \
        $(h5_PATH)/enums/H5D_CHUNK.class \
        $(h5_PATH)/enums/H5D_FILL_TIME.class \
        $(h5_PATH)/enums/H5D_FILL_VALUE.class \
        $(h5_PATH)/enums/H5D_LAYOUT.class \
        $(h5_PATH)/enums/H5D_SPACE_STATUS.class \
        $(h5_PATH)/enums/H5E_DIRECTION.class \
        $(h5_PATH)/enums/H5E_TYPE.class \
        $(h5_PATH)/enums/H5F_CLOSE_DEGREE.class \
        $(h5_PATH)/enums/H5F_LIBVER.class \
        $(h5_PATH)/enums/H5F_SCOPE.class \
        $(h5_PATH)/enums/H5FD_MEM.class \
        $(h5_PATH)/enums/H5G_STORAGE_TYPE.class \
        $(h5_PATH)/enums/H5I_TYPE.class \
        $(h5_PATH)/enums/H5L_TYPE.class \
        $(h5_PATH)/enums/H5O_TYPE.class \
        $(h5_PATH)/enums/H5R_TYPE.class \
        $(h5_PATH)/enums/H5S_CLASS.class \
        $(h5_PATH)/enums/H5S_SEL.class \
        $(h5_PATH)/enums/H5S_SELECT_OPER.class \
        $(h5_PATH)/enums/H5T_BKG.class \
        $(h5_PATH)/enums/H5T_CLASS.class \
        $(h5_PATH)/enums/H5T_CMD.class \
        $(h5_PATH)/enums/H5T_CONV_EXCEPT.class \
        $(h5_PATH)/enums/H5T_CONV_RET.class \
        $(h5_PATH)/enums/H5T_CSET.class \
        $(h5_PATH)/enums/H5T_DIR.class \
        $(h5_PATH)/enums/H5T_NORM.class \
        $(h5_PATH)/enums/H5T_ORDER.class \
        $(h5_PATH)/enums/H5T_PAD.class \
        $(h5_PATH)/enums/H5T_PERS.class \
        $(h5_PATH)/enums/H5T_SGN.class \
        $(h5_PATH)/enums/H5T_STR.class \
        $(h5_PATH)/enums/H5Z_CB.class \
        $(h5_PATH)/enums/H5Z_EDC.class \
        $(h5_PATH)/enums/H5Z_FILTER.class \
        $(h5_PATH)/enums/H5Z_SO_SCALE.class

h5_exceptions_SOURCES = $(h5_PATH)/exceptions/HDF5AtomException.java \
	$(h5_PATH)/exceptions/HDF5AttributeException.java \
        $(h5_PATH)/exceptions/HDF5BtreeException.java \
        $(h5_PATH)/exceptions/HDF5DataFiltersException.java \
        $(h5_PATH)/exceptions/HDF5DataStorageException.java \
        $(h5_PATH)/exceptions/HDF5DatasetInterfaceException.java \
        $(h5_PATH)/exceptions/HDF5DataspaceInterfaceException.java \
        $(h5_PATH)/exceptions/HDF5DatatypeInterfaceException.java \
        $(h5_PATH)/exceptions/HDF5Exception.java \
        $(h5_PATH)/exceptions/HDF5ExternalFileListException.java \
        $(h5_PATH)/exceptions/HDF5FileInterfaceException.java \
        $(h5_PATH)/exceptions/HDF5FunctionArgumentException.java \
        $(h5_PATH)/exceptions/HDF5FunctionEntryExitException.java \
        $(h5_PATH)/exceptions/HDF5HeapException.java \
        $(h5_PATH)/exceptions/HDF5InternalErrorException.java \
        $(h5_PATH)/exceptions/HDF5JavaException.java \
        $(h5_PATH)/exceptions/HDF5LibraryException.java \
        $(h5_PATH)/exceptions/HDF5LowLevelIOException.java \
        $(h5_PATH)/exceptions/HDF5MetaDataCacheException.java \
        $(h5_PATH)/exceptions/HDF5ObjectHeaderException.java \
        $(h5_PATH)/exceptions/HDF5PropertyListInterfaceException.java \
        $(h5_PATH)/exceptions/HDF5ReferenceException.java \
        $(h5_PATH)/exceptions/HDF5ResourceUnavailableException.java \
        $(h5_PATH)/exceptions/HDF5SymbolTableException.java 

h5_exceptions_CLASSES = $(h5_PATH)/exceptions/HDF5AtomException.class \
	$(h5_PATH)/exceptions/HDF5AttributeException.class \
        $(h5_PATH)/exceptions/HDF5BtreeException.class \
        $(h5_PATH)/exceptions/HDF5DataFiltersException.class \
        $(h5_PATH)/exceptions/HDF5DataStorageException.class \
        $(h5_PATH)/exceptions/HDF5DatasetInterfaceException.class \
        $(h5_PATH)/exceptions/HDF5DataspaceInterfaceException.class \
        $(h5_PATH)/exceptions/HDF5DatatypeInterfaceException.class \
        $(h5_PATH)/exceptions/HDF5Exception.class \
        $(h5_PATH)/exceptions/HDF5ExternalFileListException.class \
        $(h5_PATH)/exceptions/HDF5FileInterfaceException.class \
        $(h5_PATH)/exceptions/HDF5FunctionArgumentException.class \
        $(h5_PATH)/exceptions/HDF5FunctionEntryExitException.class \
        $(h5_PATH)/exceptions/HDF5HeapException.class \
        $(h5_PATH)/exceptions/HDF5InternalErrorException.class \
        $(h5_PATH)/exceptions/HDF5JavaException.class \
        $(h5_PATH)/exceptions/HDF5LibraryException.class \
        $(h5_PATH)/exceptions/HDF5LowLevelIOException.class \
        $(h5_PATH)/exceptions/HDF5MetaDataCacheException.class \
        $(h5_PATH)/exceptions/HDF5ObjectHeaderException.class \
        $(h5_PATH)/exceptions/HDF5PropertyListInterfaceException.class \
        $(h5_PATH)/exceptions/HDF5ReferenceException.class \
        $(h5_PATH)/exceptions/HDF5ResourceUnavailableException.class \
        $(h5_PATH)/exceptions/HDF5SymbolTableException.class 

h5_structs_SOURCES = $(h5_PATH)/structs/H5A_info_t.java \
	$(h5_PATH)/structs/H5G_info_t.java

h5_structs_CLASSES = $(h5_PATH)/structs/H5A_info_t.class \
	$(h5_PATH)/structs/H5G_info_t.class

h5_SOURCES = $(h5_constant_SOURCES) \
	$(h5_enums_SOURCES) \
	$(h5_structs_SOURCES) \
	$(h5_exceptions_SOURCES) \
	$(h5_PATH)/H5.java \
	$(h5_PATH)/H5A.java  \
	$(h5_PATH)/H5D.java  \
	$(h5_PATH)/H5E.java  \
	$(h5_PATH)/H5F.java  \
	$(h5_PATH)/H5G.java \
	$(h5_PATH)/H5I.java  \
	$(h5_PATH)/H5L.java  \
	$(h5_PATH)/H5O.java  \
	$(h5_PATH)/H5P.java  \
	$(h5_PATH)/H5R.java  \
	$(h5_PATH)/H5S.java \
	$(h5_PATH)/H5T.java  \
	$(h5_PATH)/H5Z.java  \
	$(h5_PATH)/HDFArray.java  \
	$(h5_PATH)/HDFNativeData.java

h5_CLASSES = $(h5_constant_CLASSES) \
	$(h5_enums_CLASSES) \
	$(h5_structs_CLASSES) \
	$(h5_exceptions_CLASSES) \
	$(h5_PATH)/H5.class  \
	$(h5_PATH)/H5A.class  \
	$(h5_PATH)/H5D.class  \
	$(h5_PATH)/H5E.class  \
	$(h5_PATH)/H5F.class  \
	$(h5_PATH)/H5G.class \
	$(h5_PATH)/H5I.class  \
	$(h5_PATH)/H5L.class  \
	$(h5_PATH)/H5O.class  \
	$(h5_PATH)/H5P.class  \
	$(h5_PATH)/H5R.class  \
	$(h5_PATH)/H5S.class \
	$(h5_PATH)/H5T.class  \
	$(h5_PATH)/H5Z.class  \
	$(h5_PATH)/HDFArray.class  \
	$(h5_PATH)/HDFNativeData.class

SUFFIXES: .java .class

h5_doc: $(h5_SOURCES)
	javadoc -author $(h5_SOURCES)
        
h5: $(h5_CLASSES)

.java.class:
	$(JAVAC) -classpath $(CLASSPATH) $<



include $(top_srcdir)/config/conclude.am
