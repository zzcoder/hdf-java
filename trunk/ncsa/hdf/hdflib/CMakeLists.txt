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
    HDFNewCompInfo.java
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

SET (CMAKE_JNI_TARGET TRUE)
SET (CMAKE_JAVA_TARGET_VERSION ${HDFJAVA_PACKAGE_VERSION})

ADD_JAR (${HDFJAVA_NCSA_HDF_LIB_TARGET} ${HDFJAVA_NCSA_HDF_HDF4_SRCS})
INSTALL_JAR (${HDFJAVA_NCSA_HDF_LIB_TARGET} ${LIB_INSTALL_DIR}/${HDFJAVA_NCSA_HDF_LIB_TARGET})
INSTALL_JNI_SYMLINK (${HDFJAVA_NCSA_HDF_LIB_TARGET} ${CMAKE_JAVA_LIBRARY_OUTPUT_PATH})
SET_GLOBAL_VARIABLE (HDFJAVA_JARS_TO_EXPORT "${HDFJAVA_JARS_TO_EXPORT};${${HDFJAVA_NCSA_HDF_LIB_TARGET}_JAR_FILE}")
ADD_DEPENDENCIES (${HDFJAVA_NCSA_HDF_LIB_TARGET} ${HDFJAVA_JNI_HDF_LIB_TARGET})

#-----------------------------------------------------------------------------
# Add Target(s) to CMake Install for import into other projects
#-----------------------------------------------------------------------------
IF (HDFJAVA_EXPORTED_TARGETS)
  INSTALL (
      FILES 
          ${HDFJAVA_NCSA_HDF_LIB_TARGET}
      EXPORT
          ${HDFJAVA_EXPORTED_TARGETS}
      DESTINATION ${HJAVA_INSTALL_LIB_DIR} COMPONENT libraries
  )
ENDIF (HDFJAVA_EXPORTED_TARGETS)
