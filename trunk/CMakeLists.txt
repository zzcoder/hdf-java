cmake_minimum_required(VERSION 2.8.3)
PROJECT ( HDFJAVA C Java )

#-----------------------------------------------------------------------------
# Instructions for use : Normal Build
#
# For standard build of HDFJAVA libraries,tests and tools. 
# Run cmake using the HDFJAVA source tree to generate a build tree.
# Enable/Disable options according to requirements and
# set CMAKE_INSTALL_PREFIX to the required install path.
# Make install can be used to install all components for system-wide use.
#
#-----------------------------------------------------------------------------
# Instructions for use : Sub-Project Build
#
# To include HDFJAVA as a sub-project within another project. 
# Set HDFJAVA_EXTERNALLY_CONFIGURED to 1 in the parent project and
# supply values for the following variables...
#
# HDFJAVA_EXPORTED_TARGETS :
#   Set this to the name of the targets variable which controls exports
#   If unset (because parent project does not support/use the 
#   INSTALL (EXPORT target...) syntax), then targets are not configured 
#   for export during install.
#
# HDFJAVA_LIB_DEPENDENCIES :
#   If the build of HDFJAVA libs is being customized, then rules for the
#   dependencies of the HDFJAVA libs may be 'incomplete', add additional
#   dependencies to this variable so that external projects pick them up
#
# HDFJAVA_EXTERNAL_LIB_PREFIX :
#   If the parent project needs to install hdf libraries, but avoid 
#   name conflicts with system versions, then a prefix may be added
#   to ensure that the correct versions configured are used.
#
#-----------------------------------------------------------------------------
IF (BUILD_SHARED_LIBS)
  SET (BUILD_NAME_EXT "SHARED")
ELSE (BUILD_SHARED_LIBS)
  SET (BUILD_NAME_EXT "STATIC")
ENDIF (BUILD_SHARED_LIBS)

#-----------------------------------------------------------------------------
# Set the core names of all the libraries
#-----------------------------------------------------------------------------
SET (HDFJAVA_JNI_HDF_LIB_CORENAME          "jhdf")
SET (HDFJAVA_JNI_HDF5_LIB_CORENAME         "jhdf5")
SET (HDFJAVA_NCSA_HDF_LIB_CORENAME         "jarhdf")
SET (HDFJAVA_NCSA_HDF5_LIB_CORENAME        "jarhdf5")
SET (HDFJAVA_NCSA_OBJECT_LIB_CORENAME      "jarhdfobj")
SET (HDFJAVA_NCSA_FITS_LIB_CORENAME        "jarfitsobj")
SET (HDFJAVA_NCSA_H4_LIB_CORENAME          "jarh4obj")
SET (HDFJAVA_NCSA_H5_LIB_CORENAME          "jarh5obj")
SET (HDFJAVA_NCSA_NC2_LIB_CORENAME         "jarnc2obj")
SET (HDFJAVA_TEST_HDF5_LIB_CORENAME        "jartest5")
SET (HDFJAVA_TEST_OBJECT_LIB_CORENAME      "jartestobj")
SET (HDFJAVA_TEST_UITEST_CORENAME          "jaruitest")
SET (HDFJAVA_NCSA_VIEW_LIB_CORENAME        "jhdfview")

#-----------------------------------------------------------------------------
# Set the true names of all the libraries if customized by external project
#-----------------------------------------------------------------------------
SET (HDFJAVA_JNI_HDF_LIB_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_JNI_HDF_LIB_CORENAME}")
SET (HDFJAVA_JNI_HDF5_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_JNI_HDF5_LIB_CORENAME}")
SET (HDFJAVA_NCSA_HDF_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_HDF_LIB_CORENAME}")
SET (HDFJAVA_NCSA_HDF5_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_HDF5_LIB_CORENAME}")
SET (HDFJAVA_NCSA_OBJECT_LIB_NAME  "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_OBJECT_LIB_CORENAME}")
SET (HDFJAVA_NCSA_FITS_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_FITS_LIB_CORENAME}")
SET (HDFJAVA_NCSA_H4_LIB_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_H4_LIB_CORENAME}")
SET (HDFJAVA_NCSA_H5_LIB_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_H5_LIB_CORENAME}")
SET (HDFJAVA_NCSA_NC2_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_NC2_LIB_CORENAME}")
SET (HDFJAVA_TEST_HDF5_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_TEST_HDF5_LIB_CORENAME}")
SET (HDFJAVA_TEST_OBJECT_LIB_NAME  "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_TEST_OBJECT_LIB_CORENAME}")
SET (HDFJAVA_TEST_UITEST_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_TEST_UITEST_CORENAME}")
SET (HDFJAVA_NCSA_VIEW_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_VIEW_LIB_CORENAME}")

#-----------------------------------------------------------------------------
# Set the target names of all the libraries
#-----------------------------------------------------------------------------
SET (HDFJAVA_JNI_HDF_LIB_TARGET       ${HDFJAVA_JNI_HDF_LIB_CORENAME})
SET (HDFJAVA_JNI_HDF5_LIB_TARGET      ${HDFJAVA_JNI_HDF5_LIB_CORENAME})
SET (HDFJAVA_NCSA_HDF_LIB_TARGET      ${HDFJAVA_NCSA_HDF_LIB_CORENAME})
SET (HDFJAVA_NCSA_HDF5_LIB_TARGET     ${HDFJAVA_NCSA_HDF5_LIB_CORENAME})
SET (HDFJAVA_NCSA_OBJECT_LIB_TARGET   ${HDFJAVA_NCSA_OBJECT_LIB_CORENAME})
SET (HDFJAVA_NCSA_FITS_LIB_TARGET     ${HDFJAVA_NCSA_FITS_LIB_CORENAME})
SET (HDFJAVA_NCSA_H4_LIB_TARGET       ${HDFJAVA_NCSA_H4_LIB_CORENAME})
SET (HDFJAVA_NCSA_H5_LIB_TARGET       ${HDFJAVA_NCSA_H5_LIB_CORENAME})
SET (HDFJAVA_NCSA_NC2_LIB_TARGET      ${HDFJAVA_NCSA_NC2_LIB_CORENAME})
SET (HDFJAVA_TEST_HDF5_LIB_TARGET     ${HDFJAVA_TEST_HDF5_LIB_CORENAME})
SET (HDFJAVA_TEST_OBJECT_LIB_TARGET   ${HDFJAVA_TEST_OBJECT_LIB_CORENAME})
SET (HDFJAVA_TEST_UITEST_TARGET       ${HDFJAVA_TEST_UITEST_CORENAME})
SET (HDFJAVA_NCSA_VIEW_LIB_TARGET     ${HDFJAVA_NCSA_VIEW_LIB_CORENAME})

#-----------------------------------------------------------------------------
# Define some CMake variables for use later in the project
#-----------------------------------------------------------------------------
SET (HDFJAVA_RESOURCES_DIR       ${HDFJAVA_SOURCE_DIR}/Config/cmake)
SET (HDFJAVA_JNI_SRC_DIR         ${HDFJAVA_SOURCE_DIR}/native)
SET (HDFJAVA_JAVA_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/ncsa/hdf)
SET (HDFJAVA_JAVA_OBJECT_SRC_DIR ${HDFJAVA_SOURCE_DIR}/ncsa/hdf/object)
SET (HDFJAVA_TEST_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/test)
SET (HDFJAVA_EXAMPLES_SOURCE_DIR ${HDFJAVA_SOURCE_DIR}/examples)
SET (HDFJAVA_LIB_DIR             ${HDFJAVA_SOURCE_DIR}/lib)

IF (NOT HJAVA_INSTALL_BIN_DIR)
  SET (HJAVA_INSTALL_BIN_DIR bin)
ENDIF (NOT HJAVA_INSTALL_BIN_DIR)
IF (NOT HJAVA_INSTALL_LIB_DIR)
  SET (HJAVA_INSTALL_LIB_DIR lib)
ENDIF (NOT HJAVA_INSTALL_LIB_DIR)
IF (NOT HJAVA_INSTALL_INCLUDE_DIR)
  SET (HJAVA_INSTALL_INCLUDE_DIR include)
ENDIF (NOT HJAVA_INSTALL_INCLUDE_DIR)
IF (NOT HJAVA_INSTALL_DATA_DIR)
  SET (HJAVA_INSTALL_DATA_DIR share)
ENDIF (NOT HJAVA_INSTALL_DATA_DIR)

#-----------------------------------------------------------------------------
# parse the full version number from hjava.h and include in HJAVA_VERS_INFO
#-----------------------------------------------------------------------------
FILE (READ ${HDFJAVA_SOURCE_DIR}/VERSION _hjava_version_contents)
STRING (REGEX REPLACE ".*VERSION=hdf-java-([0-9]*.[0-9]*).*$"
    "\\1" HJAVA_FULL_VERSION ${_hjava_version_contents})
STRING (REGEX REPLACE ".*VERSION=hdf-java-([0-9]*).*$"
    "\\1" HJAVA_VERS_MAJOR ${_hjava_version_contents})
STRING (REGEX REPLACE ".*VERSION=hdf-java-[0-9]*.([0-9]*).*$"
    "\\1" HJAVA_VERS_MINOR ${_hjava_version_contents})
#STRING (REGEX REPLACE ".*#define[ \t]+HJAVA_LIBVER_RELEASE[ \t]+([0-9]*).*$"
#    "\\1" HJAVA_VERS_RELEASE ${_hjava_version_contents})
#MESSAGE (STATUS "VERSION: ${HJAVA_FULL_VERSION}")
#MESSAGE (STATUS "VERSION MAJOR: ${HJAVA_VERS_MAJOR}")
#MESSAGE (STATUS "VERSION MINOR: ${HJAVA_VERS_MINOR}")
#SET (HJAVA_VERS_MAJOR 2)
#SET (HJAVA_VERS_MAJOR 7)
SET (HJAVA_VERS_RELEASE 0)

#-----------------------------------------------------------------------------
# Basic HDFJAVA stuff here
#-----------------------------------------------------------------------------
SET (HDFJAVA_PACKAGE "hdf-java")
SET (HDFJAVA_PACKAGE_NAME "HDF-JAVA")
SET (HDFJAVA_PACKAGE_VERSION "${HJAVA_VERS_MAJOR}.${HJAVA_VERS_MINOR}.${HJAVA_VERS_RELEASE}")
SET (HDFJAVA_PACKAGE_VERSION_MAJOR "${HJAVA_VERS_MAJOR}")
SET (HDFJAVA_PACKAGE_VERSION_MINOR "${HJAVA_VERS_MINOR}")
SET (HDFJAVA_PACKAGE_STRING "${HDFJAVA_PACKAGE_NAME} ${HDFJAVA_PACKAGE_VERSION}")
SET (HDFJAVA_PACKAGE_TARNAME "hdf-java")
SET (HDFJAVA_PACKAGE_URL "http://www.hdfgroup.org/hdfjava")
SET (HDFJAVA_PACKAGE_BUGREPORT "help@hdfgroup.org")

#-----------------------------------------------------------------------------
# Include some macros for reusable code
#-----------------------------------------------------------------------------
SET (CMAKE_MODULE_PATH ${HDFJAVA_RESOURCES_DIR})
INCLUDE (${HDFJAVA_RESOURCES_DIR}/UseJava.cmake)
INCLUDE (${HDFJAVA_RESOURCES_DIR}/HDFMacros.cmake)
INCLUDE (${HDFJAVA_RESOURCES_DIR}/HDF45Macros.cmake)
INCLUDE (${HDFJAVA_RESOURCES_DIR}/HDFJavaMacros.cmake)

#-----------------------------------------------------------------------------
# Setup output Directories
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  SET (CMAKE_RUNTIME_OUTPUT_DIRECTORY
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Executables."
  )
  SET (CMAKE_LIBRARY_OUTPUT_DIRECTORY
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Libraries"
  )
  SET (CMAKE_ARCHIVE_OUTPUT_DIRECTORY
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all static libraries."
  )
ELSE (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  # if we are externally configured, but the project uses old cmake scripts
  # this may not be set and utilities like Hdetect will fail
  IF (NOT CMAKE_RUNTIME_OUTPUT_DIRECTORY)
    SET (CMAKE_RUNTIME_OUTPUT_DIRECTORY ${EXECUTABLE_OUTPUT_PATH})
  ENDIF (NOT CMAKE_RUNTIME_OUTPUT_DIRECTORY)
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Targets built within this project are exported at Install time for use
# by other projects using FindHDFJAVA.
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXPORTED_TARGETS)
  SET (HDFJAVA_EXPORTED_TARGETS "HDFJAVA-targets")
ENDIF (NOT HDFJAVA_EXPORTED_TARGETS)

#-----------------------------------------------------------------------------
# To include a library in the list exported by the project AT BUILD TIME,
# add it to this variable. This is NOT used by Make Install, but for projects
# which include HDFJAVA as a sub-project within their build tree
#-----------------------------------------------------------------------------
SET_GLOBAL_VARIABLE (HDFJAVA_LIBRARIES_TO_EXPORT "")
SET_GLOBAL_VARIABLE (HDFJAVA_JARS_TO_EXPORT "")

#-----------------------------------------------------------------------------
# Run all the CMake configuration tests for our build environment
#-----------------------------------------------------------------------------
INCLUDE (${HDFJAVA_RESOURCES_DIR}/ConfigureChecks.cmake)

#-----------------------------------------------------------------------------
# Option to use code coverage
#-----------------------------------------------------------------------------
OPTION (HDFJAVA_ENABLE_COVERAGE "Enable code coverage for Libraries and Programs" OFF)
IF (HDFJAVA_ENABLE_COVERAGE)
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -g -O0 -fprofile-arcs -ftest-coverage")
    SET (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g -O0 -fprofile-arcs -ftest-coverage")
    SET (LDFLAGS "${LDFLAGS} -fprofile-arcs -ftest-coverage")   
ENDIF (HDFJAVA_ENABLE_COVERAGE)

#-----------------------------------------------------------------------------
# Include the main src and config directories
#-----------------------------------------------------------------------------
SET (HDFJAVA_INCLUDE_DIRECTORIES
    ${HDFJAVA_JNI_SRC_DIR}/hdflib 
    ${HDFJAVA_JNI_SRC_DIR}/hdf5lib
    ${HDFJAVA_BINARY_DIR}
    ${JAVA_INCLUDE_PATH}
    ${JAVA_INCLUDE_PATH2}
)
INCLUDE_DIRECTORIES (${HDFJAVA_INCLUDE_DIRECTORIES})
SET (CMAKE_JAVA_INCLUDE_PATH ${HDFJAVA_LIB_DIR}/fits.jar ${HDFJAVA_LIB_DIR}/netcdf.jar)

SET (MAKE_SYSTEM)
IF (CMAKE_BUILD_TOOL MATCHES "make")
  SET (MAKE_SYSTEM 1)
ENDIF (CMAKE_BUILD_TOOL MATCHES "make")

SET (CFG_INIT "/${CMAKE_CFG_INTDIR}")
IF (MAKE_SYSTEM)
  SET (CFG_INIT "")
ENDIF (MAKE_SYSTEM)

INCLUDE (ExternalProject)
OPTION (HDFJAVA_ALLOW_EXTERNAL_SUPPORT "Allow External Library Building" "NO")
OPTION (JPEG_USE_EXTERNAL "Use External Library Building for JPEG" 0)
OPTION (ZLIB_USE_EXTERNAL "Use External Library Building for ZLIB" 0)
OPTION (SZIP_USE_EXTERNAL "Use External Library Building for SZIP" 0)
OPTION (HDF4_USE_EXTERNAL "Use External Library Building for HDF4" 0)
OPTION (HDF5_USE_EXTERNAL "Use External Library Building for HDF5" 0)
IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  SET (JPEG_USE_EXTERNAL 1)
  SET (ZLIB_USE_EXTERNAL 1)
  SET (SZIP_USE_EXTERNAL 1)
  SET (HDF4_USE_EXTERNAL 1)
  SET (HDF5_USE_EXTERNAL 1)
  IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN")
    SET (JPEG_URL ${JPEG_SVN_URL})
    SET (ZLIB_URL ${ZLIB_SVN_URL})
    SET (SZIP_URL ${SZIP_SVN_URL})
    SET (HDF4_URL ${HDF4_SVN_URL})
    SET (HDF5_URL ${HDF5_SVN_URL})
  ELSEIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    IF (NOT TGZ_PATH)
      SET (TGZ_PATH ${HDFJAVA_SOURCE_DIR})
    ENDIF (NOT TGZ_PATH)
    SET (JPEG_URL ${TGZ_PATH}/${JPEG_TGZ_NAME})
    SET (ZLIB_URL ${TGZ_PATH}/${ZLIB_TGZ_NAME})
    SET (SZIP_URL ${TGZ_PATH}/${SZIP_TGZ_NAME})
    SET (HDF4_URL ${TGZ_PATH}/${HDF4_TGZ_NAME})
    SET (HDF5_URL ${TGZ_PATH}/${HDF5_TGZ_NAME})
  ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN")
ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")

SET (HDF4_LINK_LIBS ${LINK_LIBS})
SET (HDF5_LINK_LIBS ${LINK_LIBS})

#-----------------------------------------------------------------------------
# Option for LibJpeg support
#-----------------------------------------------------------------------------
OPTION (HDFJAVA_ENABLE_JPEG_LIB_SUPPORT "Enable libjpeg" ON)
IF (HDFJAVA_ENABLE_JPEG_LIB_SUPPORT)
  IF (NOT HJAVA_JPEGLIB_HEADER)
    IF (NOT JPEG_USE_EXTERNAL)
      FIND_PACKAGE (JPEG)
    ENDIF (NOT JPEG_USE_EXTERNAL)
    IF (JPEG_FOUND)
      SET (JPEG_INCLUDE_DIRS ${JPEG_INCLUDE_DIR})
      SET (HJAVA_HAVE_JPEGLIB_H 1)
      SET (HJAVA_HAVE_LIBJPEG 1)
      SET (HJAVA_JPEGLIB_HEADER "jpeglib.h")
      SET (JPEG_INCLUDE_DIR_GEN ${JPEG_INCLUDE_DIR})
    ELSE (JPEG_FOUND)
      IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        # May need to build JPEG with PIC on x64 machines with gcc
        # Need to use CMAKE_ANSI_CFLAGS define so that compiler test works
        IF (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS "-fPIC")
        ELSE (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS " ")
        ENDIF (BUILD_JPEG_WITH_PIC)
        
        EXTERNAL_JPEG_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${JPEG_CMAKE_C_FLAGS})
        SET (HJAVA_HAVE_JPEGLIB_H 1)
        SET (HJAVA_HAVE_LIBJPEG 1)
        SET (HJAVA_JPEGLIB_HEADER "jpeglib.h")
        MESSAGE (STATUS "JPEGLIB is built")
      ELSE (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " JPEGLib is Required for JPEGLib support in HDFJAVA")
      ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    ENDIF (JPEG_FOUND)
  ELSE (NOT HJAVA_JPEGLIB_HEADER)
    # This project is being called from within another and JPEGLib is already configured
    SET (HJAVA_HAVE_JPEGLIB_H 1)
  ENDIF (NOT HJAVA_JPEGLIB_HEADER)
  SET (LINK_LIBS ${LINK_LIBS} ${JPEG_LIBRARY})
  SET (HDF4_LINK_LIBS ${HDF4_LINK_LIBS} ${JPEG_LIBRARY})
  INCLUDE_DIRECTORIES (${JPEG_INCLUDE_DIRS})
  MESSAGE (STATUS "JPEGLIB is ON")
ENDIF (HDFJAVA_ENABLE_JPEG_LIB_SUPPORT)

#-----------------------------------------------------------------------------
# Option for ZLib support
#-----------------------------------------------------------------------------
OPTION (HDFJAVA_ENABLE_Z_LIB_SUPPORT "Enable Zlib Filters" ON)
IF (HDFJAVA_ENABLE_Z_LIB_SUPPORT)
  IF (NOT HJAVA_ZLIB_HEADER)
    IF (NOT ZLIB_USE_EXTERNAL)
      FIND_PACKAGE (ZLIB)
    ENDIF (NOT ZLIB_USE_EXTERNAL)
    IF (ZLIB_FOUND)
      SET (HJAVA_HAVE_FILTER_DEFLATE 1)
      SET (HJAVA_HAVE_ZLIB_H 1)
      SET (HJAVA_HAVE_LIBZ 1)
      SET (HJAVA_ZLIB_HEADER "zlib.h")
      SET (ZLIB_INCLUDE_DIR_GEN ${ZLIB_INCLUDE_DIR})
    ELSE (ZLIB_FOUND)
      IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        EXTERNAL_ZLIB_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
        SET (HJAVA_HAVE_FILTER_DEFLATE 1)
        SET (HJAVA_HAVE_ZLIB_H 1)
        SET (HJAVA_HAVE_LIBZ 1)
        MESSAGE (STATUS "Filter ZLIB is built")
      ELSE (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " ZLib is Required for ZLib support in HDFJAVA")
      ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    ENDIF (ZLIB_FOUND)
  ELSE (NOT HJAVA_ZLIB_HEADER)
    # This project is being called from within another and ZLib is already configured
    SET (HJAVA_HAVE_FILTER_DEFLATE 1)
    SET (HJAVA_HAVE_ZLIB_H 1)
    SET (HJAVA_HAVE_LIBZ 1)
  ENDIF (NOT HJAVA_ZLIB_HEADER)
  SET (LINK_LIBS ${LINK_LIBS} ${ZLIB_LIBRARIES})
  SET (HDF4_LINK_LIBS ${HDF4_LINK_LIBS} ${ZLIB_LIBRARIES})
  SET (HDF5_LINK_LIBS ${HDF5_LINK_LIBS} ${ZLIB_LIBRARIES})
  INCLUDE_DIRECTORIES (${ZLIB_INCLUDE_DIRS})
  MESSAGE (STATUS "Filter ZLIB is ON")
ENDIF (HDFJAVA_ENABLE_Z_LIB_SUPPORT)

#-----------------------------------------------------------------------------
# Option for SzLib support
#-----------------------------------------------------------------------------
#SET (CMAKE_MODULE_PATH ${HDFJAVA_RESOURCES_DIR} ${CMAKE_MODULE_PATH})
OPTION (HDFJAVA_ENABLE_SZIP_SUPPORT "Use SZip Filter" OFF)
IF (HDFJAVA_ENABLE_SZIP_SUPPORT)
  OPTION (HDFJAVA_ENABLE_SZIP_ENCODING "Use SZip Encoding" OFF)
  IF (NOT SZIP_USE_EXTERNAL)
    FIND_PACKAGE (SZIP)
  ENDIF (NOT SZIP_USE_EXTERNAL)
  IF (SZIP_FOUND)
    SET (HJAVA_HAVE_FILTER_SZIP 1)
    SET (HJAVA_HAVE_SZLIB_H 1)
    SET (HJAVA_HAVE_LIBSZ 1)
    SET (SZIP_INCLUDE_DIR_GEN ${SZIP_INCLUDE_DIR})
  ELSE (SZIP_FOUND)
    IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      EXTERNAL_SZIP_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE} ${HDFJAVA_ENABLE_SZIP_ENCODING})
      SET (HJAVA_HAVE_FILTER_SZIP 1)
      SET (HJAVA_HAVE_SHDFJAVA_LIBRARIES_TO_EXPORTZLIB_H 1)
      SET (HJAVA_HAVE_LIBSZ 1)
      MESSAGE (STATUS "Filter SZIP is built")
    ELSE (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      MESSAGE (FATAL_ERROR "SZIP is Required for SZIP support in HDFJAVA")
    ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  ENDIF (SZIP_FOUND)
  SET (LINK_LIBS ${LINK_LIBS} ${SZIP_LIBRARIES})
  SET (HDF4_LINK_LIBS ${HDF4_LINK_LIBS} ${SZIP_LIBRARIES})
  SET (HDF5_LINK_LIBS ${HDF5_LINK_LIBS} ${SZIP_LIBRARIES})
  INCLUDE_DIRECTORIES (${SZIP_INCLUDE_DIRS})
  MESSAGE (STATUS "Filter SZIP is ON")
  IF (HDFJAVA_ENABLE_SZIP_ENCODING)
    SET (HDFJAVA_HAVE_SZIP_ENCODER 1)
  ENDIF (HDFJAVA_ENABLE_SZIP_ENCODING)
ENDIF (HDFJAVA_ENABLE_SZIP_SUPPORT)

#-----------------------------------------------------------------------------
# HDF4 support
#-----------------------------------------------------------------------------
IF (NOT BUILD_SHARED_LIBS)
  SET (HDF4_USE_STATIC_LIBRARIES 1)
ENDIF (NOT BUILD_SHARED_LIBS)
  IF (NOT HJAVA_HDF4_HEADER)
    IF (NOT HDF4_USE_EXTERNAL)
      FIND_PACKAGE (HDF4)
    ENDIF (NOT HDF4_USE_EXTERNAL)
    IF (HDF4_FOUND)
      SET (HJAVA_HAVE_HDF_H 1)
      SET (HJAVA_HAVE_HDF4 1)
      SET (HJAVA_HDF4_HEADER "hdf.h")
      SET (HDF4_INCLUDE_DIR_GEN ${HDF4_INCLUDE_DIR})
      MESSAGE (STATUS "HDF4 found")
    ELSE (HDF4_FOUND)
      IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        # May need to build JPEG with PIC on x64 machines with gcc
        # Need to use CMAKE_ANSI_CFLAGS define so that compiler test works
        IF (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS "-fPIC")
        ELSE (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS " ")
        ENDIF (BUILD_JPEG_WITH_PIC)
        
        EXTERNAL_HDF4_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE} ${JPEG_CMAKE_C_FLAGS})
        SET (HJAVA_HAVE_HDF_H 1)
        SET (HJAVA_HAVE_HDF4 1)
        SET (HJAVA_HDF4_HEADER "hdf.h")
        MESSAGE (STATUS "HDF4 is built")
      ELSE (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " HDF4 is Required for  HDFJAVA")
      ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    ENDIF (HDF4_FOUND)
  ELSE (NOT HJAVA_HDF4_HEADER)
    # This project is being called from within another and HDF4 is already configured
    SET (HJAVA_HAVE_HDF_H 1)
    SET (HJAVA_HAVE_HDF4 1)
  ENDIF (NOT HJAVA_HDF4_HEADER)
  IF (NOT HDF4_LIBRARY)
    SET (HDF4_LIBRARY ${HDF4_LIBRARIES})
  ENDIF (NOT HDF4_LIBRARY)
  SET (LINK_LIBS ${LINK_LIBS} ${HDF4_LIBRARIES})
  SET (HDF4_LINK_LIBS ${HDF4_LINK_LIBS} ${HDF4_LIBRARIES})
  INCLUDE_DIRECTORIES (${HDF4_INCLUDE_DIRS})

#-----------------------------------------------------------------------------
# HDF5 support
#-----------------------------------------------------------------------------
IF (NOT BUILD_SHARED_LIBS)
  SET (HDF5_USE_STATIC_LIBRARIES 1)
ENDIF (NOT BUILD_SHARED_LIBS)
  IF (NOT HJAVA_HDF5_HEADER)
    IF (NOT HDF5_USE_EXTERNAL)
      FIND_PACKAGE (HDF5)
    ENDIF (NOT HDF5_USE_EXTERNAL)
    IF (HDF5_FOUND)
      SET (HJAVA_HAVE_H5PUBCONF_H 1)
      SET (HJAVA_HAVE_HDF5 1)
      SET (HJAVA_HDF5_HEADER "h5pubconf.h")
      SET (HDF5_INCLUDE_DIR_GEN ${HDF5_INCLUDE_DIR})
      MESSAGE (STATUS "HDF5 found")
    ELSE (HDF5_FOUND)
      IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        EXTERNAL_HDF5_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
        SET (HJAVA_HAVE_H5PUBCONF_H 1)
        SET (HJAVA_HAVE_HDF5 1)
        SET (HJAVA_HDF5_HEADER "h5pubconf.h")
        ADD_EXECUTABLE (h5diff IMPORTED)
        MESSAGE (STATUS "HDF5 is built")
      ELSE (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " HDF5 is Required for  HDFJAVA")
      ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    ENDIF (HDF5_FOUND)
  ELSE (NOT HJAVA_HDF5_HEADER)
    # This project is being called from within another and HDF5 is already configured
    SET (HJAVA_HAVE_H5PUBCONF_H 1)
    SET (HJAVA_HAVE_HDF5 1)
  ENDIF (NOT HJAVA_HDF5_HEADER)
  IF (NOT HDF5_LIBRARY)
    SET (HDF5_LIBRARY ${HDF5_LIBRARIES})
  ENDIF (NOT HDF5_LIBRARY)
  SET (LINK_LIBS ${LINK_LIBS} ${HDF5_LIBRARIES})
  SET (HDF5_LINK_LIBS ${HDF5_LINK_LIBS} ${HDF5_LIBRARIES})
  INCLUDE_DIRECTORIES (${HDF5_INCLUDE_DIRS})

#-----------------------------------------------------------------------------
# Option for external libraries on windows
#-----------------------------------------------------------------------------
OPTION (HDFJAVA_PACKAGE_EXTLIBS "CPACK - include external libraries" OFF)
IF (HDFJAVA_PACKAGE_EXTLIBS)
  SET (EXTERNAL_HEADER_LIST "")
  SET (EXTERNAL_LIBRARY_LIST "")
  SET (EXTERNAL_LIBRARYDLL_LIST "")
  FILE (MAKE_DIRECTORY ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${BLDTYPE})
    
  IF (HDFJAVA_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)
    PACKAGE_JPEG_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT})
  ENDIF (HDFJAVA_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)

  IF (HDFJAVA_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
    PACKAGE_ZLIB_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDFJAVA_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
    
  IF (HDFJAVA_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
    PACKAGE_SZIP_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDFJAVA_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
    
  IF (HDF4_FOUND)
    PACKAGE_HDF4_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDF4_FOUND)
    
  IF (HDF5_FOUND)
    PACKAGE_HDF5_LIBRARY (${HDFJAVA_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDF5_FOUND)
ENDIF (HDFJAVA_PACKAGE_EXTLIBS)

IF (WIN32 AND NOT CYGWIN)
  IF (BUILD_SHARED_LIBS)
    FILE (MAKE_DIRECTORY ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${BLDTYPE})
    IF (HDFJAVA_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
      GET_FILENAME_COMPONENT(ZLIB_DLL_NAME ${ZLIB_LIBRARY} NAME_WE)
      # MESSAGE (STATUS "ZLIB_DLL_NAME: ${ZLIB_DLL_NAME}")
      GET_FILENAME_COMPONENT(ZLIB_BIN_PATH ${ZLIB_LIBRARY} PATH)
      # MESSAGE (STATUS "ZLIB_BIN_PATH: ${ZLIB_BIN_PATH}")
      ADD_CUSTOM_TARGET (ZLIB-Release-Copy ALL
          COMMAND ${CMAKE_COMMAND} -E copy_if_different ${ZLIB_BIN_PATH}/${ZLIB_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/
          COMMENT "Copying ${ZLIB_BIN_PATH}/${ZLIB_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/"
      )
      IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        ADD_DEPENDENCIES (ZLIB-Release-Copy ZLIB)
      ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      IF (BUILD_TESTING)
        ADD_CUSTOM_TARGET (ZLIB-Test-Copy ALL
            COMMAND ${CMAKE_COMMAND} -E copy_if_different ${ZLIB_BIN_PATH}/${ZLIB_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
            COMMENT "Copying ${ZLIB_BIN_PATH}/${ZLIB_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
        )
        IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
          ADD_DEPENDENCIES (ZLIB-Test-Copy ZLIB)
        ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      ENDIF (BUILD_TESTING)
    ENDIF (HDFJAVA_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
    
    IF (HDFJAVA_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
      GET_FILENAME_COMPONENT(SZIP_DLL_NAME ${SZIP_LIBRARY} NAME_WE)
      # MESSAGE (STATUS "SZIP_DLL_NAME: ${SZIP_DLL_NAME}")
      GET_FILENAME_COMPONENT(SZIP_BIN_PATH ${SZIP_LIBRARY} PATH)
      # MESSAGE (STATUS "SZIP_BIN_PATH: ${SZIP_BIN_PATH}")
      ADD_CUSTOM_TARGET (SZIP-Release-Copy ALL
          COMMAND ${CMAKE_COMMAND} -E copy_if_different ${SZIP_BIN_PATH}/${SZIP_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/
          COMMENT "Copying ${SZIP_BIN_PATH}/${SZIP_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/"
      )
      IF (BUILD_TESTING)
        ADD_CUSTOM_TARGET (SZIP-Test-Copy ALL
            COMMAND ${CMAKE_COMMAND} -E copy_if_different ${SZIP_BIN_PATH}/${SZIP_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
            COMMENT "Copying ${SZIP_BIN_PATH}/${SZIP_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
        )
        IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
          ADD_DEPENDENCIES (SZIP-Test-Copy SZIP)
        ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      ENDIF (BUILD_TESTING)
      IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        ADD_DEPENDENCIES (SZIP-Release-Copy SZIP)
      ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    ENDIF (HDFJAVA_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
    
    IF (HDF4_FOUND)
      FOREACH (HDF4_LIB ${HDF4_LIBRARY})
        GET_FILENAME_COMPONENT(HDF4_DLL_NAME ${HDF4_LIB} NAME_WE)
        # MESSAGE (STATUS "HDF4_DLL_NAME: ${HDF4_DLL_NAME}")
        GET_FILENAME_COMPONENT(HDF4_BIN_PATH ${HDF4_LIB} PATH)
        # MESSAGE (STATUS "HDF4_BIN_PATH: ${HDF4_BIN_PATH}")
        ADD_CUSTOM_TARGET (${HDF4_DLL_NAME}-Release-Copy ALL
            COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_BIN_PATH}/${HDF4_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/
            COMMENT "Copying ${HDF4_BIN_PATH}/${HDF4_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/"
        )
        IF (BUILD_TESTING)
          ADD_CUSTOM_TARGET (${HDF4_DLL_NAME}-Test-Copy ALL
              COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_BIN_PATH}/${HDF4_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
              COMMENT "Copying ${HDF4_BIN_PATH}/${HDF4_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
          )
          IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
            ADD_DEPENDENCIES (${HDF4_DLL_NAME}-Test-Copy HDF4)
          ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        ENDIF (BUILD_TESTING)
        IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
          ADD_DEPENDENCIES (${HDF4_DLL_NAME}-Release-Copy HDF4)
        ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      ENDFOREACH (HDF4_LIB {HDF4_LIBRARY})
    ENDIF (HDF4_FOUND)
    
    IF (HDF5_FOUND)
      GET_FILENAME_COMPONENT(HDF5_DLL_NAME ${HDF5_LIBRARY} NAME_WE)
      # MESSAGE (STATUS "HDF5_DLL_NAME: ${HDF5_DLL_NAME}")
      GET_FILENAME_COMPONENT(HDF5_BIN_PATH ${HDF5_LIBRARY} PATH)
      # MESSAGE (STATUS "HDF5_BIN_PATH: ${HDF5_BIN_PATH}")
      ADD_CUSTOM_TARGET (HDF5-Release-Copy ALL
          COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_BIN_PATH}/${HDF5_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/
          COMMENT "Copying ${HDF5_BIN_PATH}/${HDF5_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_CFG_INTDIR}/"
      )
      IF (BUILD_TESTING)
        ADD_CUSTOM_TARGET (HDF5-Test-Copy ALL
            COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_BIN_PATH}/${HDF5_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
            COMMENT "Copying ${HDF5_BIN_PATH}/${HDF5_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
        )
        IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
          ADD_DEPENDENCIES (HDF5-Test-Copy HDF5)
        ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      ENDIF (BUILD_TESTING)
      IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        ADD_DEPENDENCIES (HDF5-Release-Copy HDF5)
      ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    ENDIF (HDF5_FOUND)
    
  ENDIF (BUILD_SHARED_LIBS)
ENDIF (WIN32 AND NOT CYGWIN)

#-----------------------------------------------------------------------------
# Build utility to copy and strip X lines of file
#-----------------------------------------------------------------------------
IF (HDFJAVA_BUILD_EXAMPLES AND BUILD_TESTING)
  SET (XLATE_UTILITY "xlatefile")
  ADD_EXECUTABLE(${XLATE_UTILITY} ${HDFJAVA_RESOURCES_DIR}/xlatefile.c)
ENDIF (HDFJAVA_BUILD_EXAMPLES AND BUILD_TESTING)

FIND_PACKAGE(JNI)

#-----------------------------------------------------------------------------
# Add the HDFJAVA Library Target to the build
#-----------------------------------------------------------------------------
ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/native    native)
ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/ncsa      ncsa)

IF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  IF (JPEG_FOUND AND JPEG_USE_EXTERNAL)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF_LIB_TARGET} JPEG)
  ENDIF (JPEG_FOUND AND JPEG_USE_EXTERNAL)
  IF (ZLIB_FOUND AND ZLIB_USE_EXTERNAL)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF_LIB_TARGET} ZLIB)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF5_LIB_TARGET} ZLIB)
  ENDIF (ZLIB_FOUND AND ZLIB_USE_EXTERNAL)
  IF (SZIP_FOUND AND SZIP_USE_EXTERNAL)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF_LIB_TARGET} SZIP)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF5_LIB_TARGET} SZIP)
  ENDIF (SZIP_FOUND AND SZIP_USE_EXTERNAL)
  IF (HDF4_FOUND AND HDF4_USE_EXTERNAL)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF_LIB_TARGET} HDF4)
  ENDIF (HDF4_FOUND AND HDF4_USE_EXTERNAL)
  IF (HDF5_FOUND AND HDF5_USE_EXTERNAL)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF5_LIB_TARGET} HDF5)
  ENDIF (HDF5_FOUND AND HDF5_USE_EXTERNAL)
ENDIF (HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDFJAVA_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")

#-----------------------------------------------------------------------------
# Dashboard and Testing Settings
#-----------------------------------------------------------------------------
OPTION (BUILD_TESTING "Build HDFJAVA Unit Testing" OFF)
IF (BUILD_TESTING)
  SET (DART_TESTING_TIMEOUT 1200
      CACHE INTEGER
      "Timeout in seconds for each test (default 1200=20minutes)"
  )
  ENABLE_TESTING ()
  INCLUDE (CTest)
  IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
    ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/test ${PROJECT_BINARY_DIR}/test)
  ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  INCLUDE (${HDFJAVA_SOURCE_DIR}/CTestConfig.cmake)
  CONFIGURE_FILE (${HDFJAVA_RESOURCES_DIR}/CTestCustom.cmake ${HDFJAVA_BINARY_DIR}/CTestCustom.ctest @ONLY)
ENDIF (BUILD_TESTING)

#-----------------------------------------------------------------------------
# Option to build HDFJAVA Examples
#-----------------------------------------------------------------------------
IF (HDFJAVA_BUILD_EXAMPLES)
  ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/examples    examples)
ENDIF (HDFJAVA_BUILD_EXAMPLES)

#-----------------------------------------------------------------------------
# Option for external libraries
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  IF (HDFJAVA_PACKAGE_EXTLIBS)
    IF (HDFJAVA_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)
      INSTALL (
          FILES ${JPEG_INCLUDE_DIR}/jerror.h ${JPEG_INCLUDE_DIR}/jmorecfg.h ${JPEG_INCLUDE_DIR}/jpeglib.h
          DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
          COMPONENT headers
      )
    ENDIF (HDFJAVA_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)
    IF (HDFJAVA_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
      INSTALL (
          FILES ${ZLIB_INCLUDE_DIR}/zlib.h
          DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
          COMPONENT headers
      )
    ENDIF (HDFJAVA_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
    IF (HDFJAVA_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
      INSTALL (
          FILES ${SZIP_INCLUDE_DIR}/szlib.h ${SZIP_INCLUDE_DIR}/szip_adpt.h ${SZIP_INCLUDE_DIR}/ricehdf.h
          DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
          COMPONENT headers
      )
    ENDIF (HDFJAVA_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
    IF (HDF4_FOUND)
      IF (HDF4_USE_EXTERNAL)
        INSTALL (
            FILES ${HDF4_HDF_INCLUDE_DIR}/hdf.h ${HDF4_MFHDF_INCLUDE_DIR}/mfhdf.h
            DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
            COMPONENT headers
        )
      ELSE (HDF4_USE_EXTERNAL)
        INSTALL (
            FILES ${HDF4_INCLUDE_DIR_GEN}/h4config.h ${HDF4_INCLUDE_DIR}/hdf.h ${HDF4_INCLUDE_DIR}/mfhdf.h
            DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
            COMPONENT headers
        )
      ENDIF (HDF4_USE_EXTERNAL)
    ENDIF (HDF4_FOUND)
    IF (HDF5_FOUND)
      INSTALL (
          FILES ${HDF5_INCLUDE_DIR}/H5public.h
          DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
          COMPONENT headers
      )
    ENDIF (HDF5_FOUND)
    INSTALL (
        FILES ${EXTERNAL_HEADER_LIST}
        DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
        COMPONENT headers
    )
    INSTALL(
        FILES ${EXTERNAL_LIBRARY_LIST}
        DESTINATION ${HJAVA_INSTALL_LIB_DIR}
        COMPONENT libraries
    )
    IF (WIN32 AND BUILD_SHARED_LIBS)
      INSTALL(
          FILES ${EXTERNAL_LIBRARYDLL_LIST}
          DESTINATION ${HJAVA_INSTALL_BIN_DIR}
          COMPONENT libraries
      )
    ENDIF (WIN32 AND BUILD_SHARED_LIBS)
  ENDIF (HDFJAVA_PACKAGE_EXTLIBS)
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Add Required Jar(s)
#-----------------------------------------------------------------------------
INSTALL (
    FILES ${HDFJAVA_LIB_DIR}/fits.jar ${HDFJAVA_LIB_DIR}/netcdf.jar
    DESTINATION ${HJAVA_INSTALL_LIB_DIR}
    COMPONENT libraries
)

#-----------------------------------------------------------------------------
# Add Required Document(s)
#-----------------------------------------------------------------------------
INSTALL (
    DIRECTORY ${HDFJAVA_SOURCE_DIR}/docs
    DESTINATION ${HJAVA_INSTALL_DATA_DIR}
    USE_SOURCE_PERMISSIONS 
    COMPONENT docs
)

#-----------------------------------------------------------------------------
# Add Target(s) to CMake Install for import into other projects
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  INSTALL (
      EXPORT ${HDFJAVA_EXPORTED_TARGETS}
      DESTINATION ${HJAVA_INSTALL_LIB_DIR}/cmake/HDFJAVA-${HDFJAVA_PACKAGE_VERSION}
      FILE HDFJAVA-targets.cmake
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Export all exported targets to the build tree for use by parent project
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  EXPORT (
      TARGETS ${HDFJAVA_LIBRARIES_TO_EXPORT} ${HDFJAVA_LIB_DEPENDENCIES}
      FILE HDFJAVA-targets.cmake
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config.cmake file for the build directory
#-----------------------------------------------------------------------------
SET (HDFJAVA_INCLUDES_BUILD_TIME
    ${HDFJAVA_JNI_SRC_DIR}
    ${HDFJAVA_JAVA_SRC_DIR}
    ${HDFJAVA_BINARY_DIR}
)
SET (HDFJAVA_VERSION_STRING @HDFJAVA_PACKAGE_VERSION@)
SET (HDFJAVA_VERSION_MAJOR  @HDFJAVA_PACKAGE_VERSION_MAJOR@)
SET (HDFJAVA_VERSION_MINOR  @HDFJAVA_PACKAGE_VERSION_MINOR@)

CONFIGURE_FILE (
    ${HDFJAVA_RESOURCES_DIR}/HDFJAVA-config.cmake.build.in 
    ${HDFJAVA_BINARY_DIR}/HDFJAVA-config.cmake @ONLY
)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config.cmake file for the install directory
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  CONFIGURE_FILE (
      ${HDFJAVA_RESOURCES_DIR}/HDFJAVA-config.cmake.install.in
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/HDFJAVA-config.cmake @ONLY
  )
  INSTALL (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/HDFJAVA-config.cmake
      DESTINATION ${HJAVA_INSTALL_LIB_DIR}/cmake/HDFJAVA-${HDFJAVA_PACKAGE_VERSION}
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config-version .cmake file for the install directory
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  CONFIGURE_FILE (
      ${HDFJAVA_RESOURCES_DIR}/HDFJAVA-config-version.cmake.in
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/HDFJAVA-config-version.cmake @ONLY
  )
  INSTALL (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/HDFJAVA-config-version.cmake
      DESTINATION ${HJAVA_INSTALL_LIB_DIR}/cmake/HDFJAVA-${HDFJAVA_PACKAGE_VERSION}
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Set the cpack variables
#-----------------------------------------------------------------------------
SET (CPACK_PACKAGE_VENDOR "The HDF Group")
SET (CPACK_PACKAGE_DESCRIPTION_SUMMARY "HDF-JAVA Installation")
SET (CPACK_PACKAGE_DESCRIPTION_FILE "${CMAKE_CURRENT_SOURCE_DIR}/Readme.txt")
SET (CPACK_PACKAGE_NAME "${HDFJAVA_PACKAGE_NAME}")
SET (CPACK_PACKAGE_INSTALL_DIRECTORY "${HDFJAVA_PACKAGE_NAME}")
SET (CPACK_PACKAGE_VERSION "${HDFJAVA_PACKAGE_VERSION}")
SET (CPACK_PACKAGE_VERSION_MAJOR "${HDFJAVA_PACKAGE_VERSION_MAJOR}")
SET (CPACK_PACKAGE_VERSION_MINOR "${HDFJAVA_PACKAGE_VERSION_MINOR}")
SET (CPACK_PACKAGE_VERSION_PATCH "")
SET (CPACK_RESOURCE_FILE_LICENSE "${CMAKE_CURRENT_SOURCE_DIR}/COPYING")
SET (CPACK_RESOURCE_FILE_README "${CMAKE_CURRENT_SOURCE_DIR}/Readme.txt")
SET (CPACK_PACKAGE_RELOCATABLE TRUE)

IF (WIN32)
  SET (CPACK_NSIS_CONTACT "help@hdfgroup.org")
  SET (CPACK_NSIS_MODIFY_PATH ON)
  SET (CPACK_NSIS_PACKAGE_NAME "HDFJAVA ${HDFJAVA_PACKAGE_VERSION}")
ELSE (WIN32)
  SET (CPACK_RPM_COMPONENT_INSTALL ON)
  SET (CPACK_RPM_PRE_INSTALL_SCRIPT_FILE "/sbin/ldconfig")
  SET (CPACK_RPM_PRE_UNINSTALL_SCRIPT_FILE "/sbin/ldconfig")
  SET (CPACK_RPM_POST_INSTALL_SCRIPT_FILE "/sbin/ldconfig")
  SET (CPACK_RPM_POST_UNINSTALL_SCRIPT_FILE "/sbin/ldconfig")
  SET (CPACK_RPM_PACKAGE_GROUP "Development/Tools")
  SET (CPACK_RPM_PACKAGE_LICENSE "BSD-style")
  SET (CPACK_RPM_PACKAGE_URL "http://www.hdfgroup.org/")
  SET (CPACK_STRIP_FILES "${HDFJAVA_LIBRARIES_TO_EXPORT};${HDFJAVA_JARS_TO_EXPORT};${EXTERNAL_LIBRARY_LIST}")
ENDIF (WIN32)

INCLUDE (CPack)
INCLUDE(InstallRequiredSystemLibraries)

#-----------------------------------------------------------------------------
# Now list the cpack commands
#-----------------------------------------------------------------------------
CPACK_ADD_COMPONENT (apps 
    DISPLAY_NAME "HDFJAVA Applications" 
    DEPENDS libraries
    GROUP Applications
)
CPACK_ADD_COMPONENT (libraries 
    DISPLAY_NAME "HDFJAVA Libraries"
    GROUP Runtime
)
CPACK_ADD_COMPONENT (headers 
    DISPLAY_NAME "HDFJAVA Headers" 
    DEPENDS libraries
    GROUP Development
)
CPACK_ADD_COMPONENT (docs 
    DISPLAY_NAME "HDFJAVA Documents" 
    DEPENDS libraries
    GROUP Documents
)
