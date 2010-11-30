cmake_minimum_required (VERSION 2.8.3)
PROJECT (HDFJAVA_NCSA_HDF_HDF4 Java)

SET (CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES (
    ${HDFJAVA_NCSA_HDF_HDF4_SOURCE_DIR}
    ${HDFJAVA_NCSA_HDF_HDF4_BINARY_DIR}
)

SET (HDFJAVA_NCSA_HDF_HDF4_SRCS
    HDFException.java
    HDFJavaException.java
    HDFArray.java
    HDFChunkInfo.java
    HDFCompInfo.java
    HDFConstants.java
    HDFDeflateCompInfo.java
    HDFDeprecated.java
    HDFIMCOMPCompInfo.java
    HDFJPEGCompInfo.java
    HDFLibrary.java
    HDFLibraryException.java
    HDFNativeData.java
    HDFNBITChunkInfo.java
    HDFNBITCompInfo.java
    HDFNotImplementedException.java
    HDFOldCompInfo.java
    HDFOldRLECompInfo.java
    HDFRLECompInfo.java
    HDFSKPHUFFCompInfo.java
    HDFSZIPCompInfo.java
    HDFTable.java
)

INCLUDE_DIRECTORIES (${CMAKE_CURRENT_BINARY_DIR}/CMakeFiles/${HDFJAVA_NCSA_HDF_LIB_CORENAME}.dir/ncsa/hdf/hdflib)

ADD_LIBRARY (${HDFJAVA_NCSA_HDF_LIB_TARGET} ${HDFJAVA_NCSA_HDF_HDF4_SRCS})
