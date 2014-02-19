cmake_minimum_required(VERSION 2.8.10)
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
SET (HDFJAVA_RESOURCES_DIR       ${HDFJAVA_SOURCE_DIR}/config/cmake)
SET (HDFJAVA_JNI_SRC_DIR         ${HDFJAVA_SOURCE_DIR}/native)
SET (HDFJAVA_JAVA_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/ncsa/hdf)
SET (HDFJAVA_JAVA_OBJECT_SRC_DIR ${HDFJAVA_SOURCE_DIR}/ncsa/hdf/object)
SET (HDFJAVA_TEST_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/test)
SET (HDFJAVA_EXAMPLES_SOURCE_DIR ${HDFJAVA_SOURCE_DIR}/examples)
SET (HDFJAVA_LIB_DIR             ${HDFJAVA_SOURCE_DIR}/lib)
SET (HDFJAVA_FITS_JAR            ${HDFJAVA_SOURCE_DIR}/lib/fits.jar)
SET (HDFJAVA_NETCDF_JAR          ${HDFJAVA_SOURCE_DIR}/lib/netcdf.jar)
SET (HDFJAVA_LOGGING_JAR         ${HDFJAVA_SOURCE_DIR}/lib/slf4j-api-1.7.5.jar)
SET (HDFJAVA_LOGGING_NOP_JAR     ${HDFJAVA_SOURCE_DIR}/lib/ext/slf4j-nop-1.7.5.jar)
SET (HDFJAVA_LOGGING_SIMPLE_JAR  ${HDFJAVA_SOURCE_DIR}/lib/ext/slf4j-simple-1.7.5.jar)

IF (NOT HJAVA_INSTALL_BIN_DIR)
  SET (HJAVA_INSTALL_BIN_DIR bin)
ENDIF (NOT HJAVA_INSTALL_BIN_DIR)
IF (NOT HJAVA_INSTALL_LIB_DIR)
  IF (APPLE)
    IF (HDFJAVA_PACK_MACOSX_BUNDLE)
      SET (HJAVA_INSTALL_JAR_DIR ../Java)
    ELSE (HDFJAVA_PACK_MACOSX_BUNDLE)
      SET (HJAVA_INSTALL_JAR_DIR lib)
    ENDIF (HDFJAVA_PACK_MACOSX_BUNDLE)
    SET (HJAVA_INSTALL_LIB_DIR lib)
  ELSE (APPLE)
    SET (HJAVA_INSTALL_JAR_DIR lib)
    SET (HJAVA_INSTALL_LIB_DIR lib)
  ENDIF (APPLE)
ENDIF (NOT HJAVA_INSTALL_LIB_DIR)
IF (NOT HJAVA_INSTALL_INCLUDE_DIR)
  SET (HJAVA_INSTALL_INCLUDE_DIR include)
ENDIF (NOT HJAVA_INSTALL_INCLUDE_DIR)
IF (NOT HJAVA_INSTALL_DATA_DIR)
  IF (NOT WIN32)
    SET (HJAVA_INSTALL_DATA_DIR share)
    SET (HJAVA_INSTALL_CMAKE_DIR share/cmake)
  ELSE (NOT WIN32)
    SET (HJAVA_INSTALL_DATA_DIR ".")
    SET (HJAVA_INSTALL_CMAKE_DIR cmake)
  ENDIF (NOT WIN32)
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
SET (HDFJAVA_PACKAGE_NAME "HDFView")
SET (HDFJAVA_PACKAGE_VERSION "${HJAVA_VERS_MAJOR}.${HJAVA_VERS_MINOR}.${HJAVA_VERS_RELEASE}")
SET (HDFJAVA_PACKAGE_VERSION_STRING "${HDFJAVA_PACKAGE_VERSION}")
SET (HDFJAVA_PACKAGE_VERSION_MAJOR "${HJAVA_VERS_MAJOR}")
SET (HDFJAVA_PACKAGE_VERSION_MINOR "${HJAVA_VERS_MINOR}")
SET (HDFJAVA_PACKAGE_STRING "${HDFJAVA_PACKAGE_NAME}")
SET (HDFJAVA_PACKAGE_TARNAME "hdf-java")
SET (HDFJAVA_PACKAGE_URL "http://www.hdfgroup.org/hdfjava")
SET (HDFJAVA_PACKAGE_BUGREPORT "help@hdfgroup.org")

#-----------------------------------------------------------------------------
# Set Install folder value
#-----------------------------------------------------------------------------
IF (CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)
  IF(CMAKE_HOST_UNIX)
    SET(CMAKE_INSTALL_PREFIX "/HDF_Group/${HDFJAVA_PACKAGE_NAME}/${HDFJAVA_PACKAGE_VERSION}"
      CACHE PATH "Install path prefix, prepended onto install directories." FORCE)
  ELSE(CMAKE_HOST_UNIX)
    GetDefaultWindowsPrefixBase(CMAKE_GENERIC_PROGRAM_FILES)
    SET(CMAKE_INSTALL_PREFIX
      "${CMAKE_GENERIC_PROGRAM_FILES}/HDF_Group/${HDFJAVA_PACKAGE_NAME}/${HDFJAVA_PACKAGE_VERSION}"
      CACHE PATH "Install path prefix, prepended onto install directories." FORCE)
    SET(CMAKE_GENERIC_PROGRAM_FILES)
  ENDIF(CMAKE_HOST_UNIX)
ENDIF (CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)

FIND_PACKAGE(Java)
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
  SET (CMAKE_JAVA_TARGET_OUTPUT_DIR 
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all java jars."
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
SET_GLOBAL_VARIABLE (HDFJAVA_HDFVIEWJAR_TO_EXPORT "${HDFJAVA_LOGGING_JAR}")
SET_GLOBAL_VARIABLE (HDFJAVA_SOURCE_PACKAGES "")
SET_GLOBAL_VARIABLE (HDFJAVA_OBJECT_JARS "")

#-----------------------------------------------------------------------------
# Run all the CMake configuration tests for our build environment
#-----------------------------------------------------------------------------
INCLUDE (${HDFJAVA_RESOURCES_DIR}/ConfigureChecks.cmake)

#-----------------------------------------------------------------------------
# Option to use code coverage
#-----------------------------------------------------------------------------
OPTION (HDF_ENABLE_COVERAGE "Enable code coverage for Libraries and Programs" OFF)
IF (HDF_ENABLE_COVERAGE)
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -g -O0 -fprofile-arcs -ftest-coverage")
    SET (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g -O0 -fprofile-arcs -ftest-coverage")
    SET (LDFLAGS "${LDFLAGS} -fprofile-arcs -ftest-coverage")   
ENDIF (HDF_ENABLE_COVERAGE)

#-----------------------------------------------------------------------------
# Include user macros
#-----------------------------------------------------------------------------
INCLUDE (UserMacros.cmake)

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
SET (CMAKE_JAVA_INCLUDE_PATH "")
SET (HDFVIEW_OPTIONS_INCLUDE_PATH "")

SET (MAKE_SYSTEM)
IF (CMAKE_BUILD_TOOL MATCHES "make")
  SET (MAKE_SYSTEM 1)
ENDIF (CMAKE_BUILD_TOOL MATCHES "make")

SET (CFG_INIT "/${CMAKE_CFG_INTDIR}")
IF (MAKE_SYSTEM)
  SET (CFG_INIT "")
ENDIF (MAKE_SYSTEM)

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
  INCLUDE (${HDFJAVA_SOURCE_DIR}/CTestConfig.cmake)
  CONFIGURE_FILE (${HDFJAVA_RESOURCES_DIR}/CTestCustom.cmake ${HDFJAVA_BINARY_DIR}/CTestCustom.ctest @ONLY)
ENDIF (BUILD_TESTING)

#-----------------------------------------------------------------------------
# Add the HDFJAVA Library Target to the build
#-----------------------------------------------------------------------------
ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/native    native)
ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/ncsa      ncsa)
IF (BUILD_TESTING)
  IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
    ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/test ${PROJECT_BINARY_DIR}/test)
  ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
ENDIF (BUILD_TESTING)

#-----------------------------------------------------------------------------
# Option to build HDFJAVA Examples
#-----------------------------------------------------------------------------
IF (HDF_BUILD_EXAMPLES)
  ADD_SUBDIRECTORY(${HDFJAVA_SOURCE_DIR}/examples    examples)
ENDIF (HDF_BUILD_EXAMPLES)

#-----------------------------------------------------------------------------
# Add Required Jar(s)
#-----------------------------------------------------------------------------
INSTALL (
    FILES
        ${HDFJAVA_FITS_JAR}
        ${HDFJAVA_NETCDF_JAR}
        ${HDFJAVA_LOGGING_JAR}
        ${HDFJAVA_LOGGING_NOP_JAR}
    DESTINATION ${HJAVA_INSTALL_JAR_DIR}
    COMPONENT libraries
)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA_Examples.cmake file and the examples
#-----------------------------------------------------------------------------
OPTION (HDFJAVA_PACK_EXAMPLES  "Package the HDFJAVA Library Examples Compressed File" OFF)
IF (HDFJAVA_PACK_EXAMPLES)
  CONFIGURE_FILE (
      ${HDFJAVA_RESOURCES_DIR}/HDFJAVA_Examples.cmake.in 
      ${HDFJAVA_BINARY_DIR}/HDFJAVA_Examples.cmake @ONLY
  )
  INSTALL (
      FILES ${HDFJAVA_BINARY_DIR}/HDFJAVA_Examples.cmake
      DESTINATION ${HJAVA_INSTALL_DATA_DIR}
      COMPONENT docs
  )
  IF (EXISTS "${HDFJAVA_EXAMPLES_COMPRESSED_DIR}/${HDFJAVA_EXAMPLES_COMPRESSED}")
    INSTALL (
        FILES
            ${HDFJAVA_EXAMPLES_COMPRESSED_DIR}/${HDFJAVA_EXAMPLES_COMPRESSED}
            ${HDFJAVA_RESOURCES_DIR}/USING_CMake_Examples.txt
        DESTINATION ${HJAVA_INSTALL_DATA_DIR}
        COMPONENT docs
    )
  ENDIF (EXISTS "${HDFJAVA_EXAMPLES_COMPRESSED_DIR}/${HDFJAVA_EXAMPLES_COMPRESSED}")
ENDIF (HDFJAVA_PACK_EXAMPLES)

#SET (CREATE_JAVADOCS "TRUE")
#-----------------------------------------------------------------------------
# Add Required JavaDocs
#-----------------------------------------------------------------------------
IF (CREATE_JAVADOCS)
  SET (HDFVIEW_TEST_CLASSPATH "${${HDFJAVA_NCSA_VIEW_LIB_TARGET}_JAR_FILE}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_LOGGING_SIMPLE_JAR}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_LOGGING_JAR}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_LIB_DIR}")
  CREATE_JAVADOC (hdf_java_doc
      PACKAGES ${HDFJAVA_SOURCE_PACKAGES}
      SOURCEPATH "${CMAKE_CURRENT_SOURCE_DIR}"
      CLASSPATH ${HDFVIEW_TEST_CLASSPATH}
      INSTALLPATH ${HJAVA_INSTALL_DATA_DIR}
      WINDOWTITLE "HDFJava 2.10.1"
      DOCTITLE "<h1>HDFJava 2.10.1</h1>"
      AUTHOR TRUE
      USE TRUE
      VERSION TRUE
  )
ELSE (CREATE_JAVADOCS)
  INSTALL (
      DIRECTORY ${HDFJAVA_SOURCE_DIR}/docs/javadocs
      DESTINATION ${HJAVA_INSTALL_DATA_DIR}
      USE_SOURCE_PERMISSIONS 
      COMPONENT docs
      PATTERN ".svn" EXCLUDE
  )
ENDIF (CREATE_JAVADOCS)

#-----------------------------------------------------------------------------
# Add Required Document(s)
#-----------------------------------------------------------------------------
IF (HDFJAVA_PACK_SAMPLES)
  INSTALL (
      DIRECTORY ${HDFJAVA_SOURCE_DIR}/docs/samples
      DESTINATION ${HJAVA_INSTALL_DATA_DIR}
      USE_SOURCE_PERMISSIONS 
      COMPONENT docs
      PATTERN ".svn" EXCLUDE
  )
ENDIF (HDFJAVA_PACK_SAMPLES)

INSTALL (
    DIRECTORY ${HDFJAVA_SOURCE_DIR}/docs/hdfview/UsersGuide
    DESTINATION ${HJAVA_INSTALL_DATA_DIR}
    USE_SOURCE_PERMISSIONS 
    COMPONENT docs
    PATTERN ".svn" EXCLUDE
)

#-----------------------------------------------------------------------------
# Add Target(s) to CMake Install for import into other projects
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  INSTALL (
      EXPORT ${HDFJAVA_EXPORTED_TARGETS}
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
      FILE ${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-targets.cmake
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Export all exported targets to the build tree for use by parent project
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  EXPORT (
      TARGETS ${HDFJAVA_LIBRARIES_TO_EXPORT} ${HDFJAVA_LIB_DEPENDENCIES}
      FILE ${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-targets.cmake
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

CONFIGURE_FILE (
    ${HDFJAVA_RESOURCES_DIR}/HDFJAVA-config.cmake.build.in 
    ${HDFJAVA_BINARY_DIR}/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config.cmake @ONLY
)

#-----------------------------------------------------------------------------
# Configure the FindJAVA.cmake file for the install directory
#-----------------------------------------------------------------------------
IF (NOT H4H5_EXTERNALLY_CONFIGURED)
  CONFIGURE_FILE (
      ${HDFJAVA_RESOURCES_DIR}/FindHDFJAVA.cmake.in 
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/FindHDFJAVA${HDF_PACKAGE_EXT}.cmake @ONLY
  )
  INSTALL (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/FindHDFJAVA${HDF_PACKAGE_EXT}.cmake
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
  )
ENDIF (NOT H4H5_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config.cmake file for the install directory
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  CONFIGURE_FILE (
      ${HDFJAVA_RESOURCES_DIR}/HDFJAVA-config.cmake.install.in
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config.cmake @ONLY
  )
  INSTALL (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config.cmake
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config-version .cmake file for the install directory
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  CONFIGURE_FILE (
      ${HDFJAVA_RESOURCES_DIR}/HDFJAVA-config-version.cmake.in
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config-version.cmake @ONLY
  )
  INSTALL (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config-version.cmake
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Option to include jre
#-----------------------------------------------------------------------------
IF (HDFJAVA_PACK_JREFILE)
  INSTALL (
      FILES ${HDFJAVA_PACK_JREPATH}/${HDFJAVA_PACK_JREFILE}
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}/JRE
  )
  IF (WIN32)
    SET (CPACK_NSIS_EXTRA_INSTALL_COMMANDS "
        SetOutPath \\\"$INSTDIR\\\\bin\\\\JRE\\\"
        ExecWait '\\\"$INSTDIR\\\\${HJAVA_INSTALL_CMAKE_DIR}\\\\${HDFJAVA_PACKAGE}\\\\JRE\\\\${HDFJAVA_PACK_JREFILE}\\\" /s /v\\\"INSTALLDIR=\\\\\\\"$INSTDIR\\\\bin\\\\JRE\\\\\\\" JAVAUPDATE=0\\\" /qn'
    ")
    IF (CMAKE_CL_64)
      SET (CPACK_NSIS_EXTRA_UNINSTALL_COMMANDS "
          ExecWait '\\\"$SYSDIR\\\\MsiExec.exe\\\" /x \\\"{26A24AE4-039D-4CA4-87B4-2F86417045FF}\\\" /qn'
          RMDir \\\"/r\\\" \\\"$INSTDIR\\\\bin\\\\JRE\\\"
      ")
    ELSE (CMAKE_CL_64)
      SET (CPACK_NSIS_EXTRA_UNINSTALL_COMMANDS "
          ExecWait '\\\"$SYSDIR\\\\MsiExec.exe\\\" /x \\\"{26A24AE4-039D-4CA4-87B4-2F83217045FF}\\\" /qn'
          RMDir \\\"/r\\\" \\\"$INSTDIR\\\\bin\\\\JRE\\\"
      ")
    ENDIF (CMAKE_CL_64)
  ENDIF (WIN32)
ENDIF (HDFJAVA_PACK_JREFILE)

#-----------------------------------------------------------------------------
# Configure the README.txt file for the binary package
#-----------------------------------------------------------------------------
SET (BINARY_SYSTEM_NAME ${CMAKE_SYSTEM_NAME})
SET (BINARY_PLATFORM "${CMAKE_SYSTEM_NAME}")
IF (WIN32)
  SET (BINARY_EXAMPLE_ENDING "zip")
  SET (BINARY_INSTALL_ENDING "exe")
  IF (CMAKE_CL_64)
    SET (BINARY_SYSTEM_NAME "win64")
  ELSE (CMAKE_CL_64)
    SET (BINARY_SYSTEM_NAME "win32")
  ENDIF (CMAKE_CL_64)
  IF (${CMAKE_SYSTEM_VERSION} MATCHES "6.1")
    SET (BINARY_PLATFORM "${BINARY_PLATFORM} 7")
  ELSEIF (${CMAKE_SYSTEM_VERSION} MATCHES "6.2")
    SET (BINARY_PLATFORM "${BINARY_PLATFORM} 8")
  ENDIF (${CMAKE_SYSTEM_VERSION} MATCHES "6.1")
  SET (BINARY_PLATFORM "${BINARY_PLATFORM} ${MSVC_C_ARCHITECTURE_ID}")
  IF (${CMAKE_C_COMPILER_VERSION} MATCHES "16.*")
    SET (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO 2010")
  ELSEIF (${CMAKE_C_COMPILER_VERSION} MATCHES "15.*")
    SET (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO 2008")
  ELSEIF (${CMAKE_C_COMPILER_VERSION} MATCHES "17.*")
    SET (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO 2012")
  ELSE (${CMAKE_C_COMPILER_VERSION} MATCHES "16.*")
    SET (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO ${CMAKE_C_COMPILER_VERSION}")
  ENDIF (${CMAKE_C_COMPILER_VERSION} MATCHES "16.*")
ELSEIF (APPLE)
  SET (BINARY_EXAMPLE_ENDING "tar.gz")
  SET (BINARY_INSTALL_ENDING "dmg")
  SET (BINARY_PLATFORM "${BINARY_PLATFORM} ${CMAKE_SYSTEM_VERSION} ${CMAKE_SYSTEM_PROCESSOR}")
  SET (BINARY_PLATFORM "${BINARY_PLATFORM}, using ${CMAKE_C_COMPILER_ID} C ${CMAKE_C_COMPILER_VERSION}")
ELSE (WIN32)
  SET (BINARY_EXAMPLE_ENDING "tar.gz")
  SET (BINARY_INSTALL_ENDING "sh")
  SET (BINARY_PLATFORM "${BINARY_PLATFORM} ${CMAKE_SYSTEM_VERSION} ${CMAKE_SYSTEM_PROCESSOR}")
  SET (BINARY_PLATFORM "${BINARY_PLATFORM}, using ${CMAKE_C_COMPILER_ID} C ${CMAKE_C_COMPILER_VERSION}")
ENDIF (WIN32)
SET (BINARY_PLATFORM "${BINARY_PLATFORM} / Java ${Java_VERSION_MAJOR}.${Java_VERSION_MINOR}")
IF (HDFJAVA_PACK_JREFILE)
  SET (BINARY_JRE_EMBEDDED "${BINARY_PLATFORM}. An embedded JRE will be installed.")
ENDIF (HDFJAVA_PACK_JREFILE)

CONFIGURE_FILE (
    ${HDFJAVA_RESOURCES_DIR}/README.txt.cmake.in 
    ${HDFJAVA_BINARY_DIR}/README.txt @ONLY
)

#-----------------------------------------------------------------------------
# Set the cpack variables
#-----------------------------------------------------------------------------
SET (CPACK_PACKAGE_VENDOR "HDF_Group")
SET (CPACK_PACKAGE_DESCRIPTION_SUMMARY "HDF-JAVA Installation")
SET (CPACK_PACKAGE_DESCRIPTION_FILE "${CMAKE_CURRENT_SOURCE_DIR}/Readme.txt")
SET (CPACK_PACKAGE_NAME "${HDFJAVA_PACKAGE_NAME}")
SET (CPACK_PACKAGE_VERSION "${HDFJAVA_PACKAGE_VERSION}")
SET (CPACK_PACKAGE_VERSION_MAJOR "${HDFJAVA_PACKAGE_VERSION_MAJOR}")
SET (CPACK_PACKAGE_VERSION_MINOR "${HDFJAVA_PACKAGE_VERSION_MINOR}")
SET (CPACK_PACKAGE_VERSION_PATCH "")
SET (CPACK_RESOURCE_FILE_LICENSE "${CMAKE_CURRENT_SOURCE_DIR}/COPYING")
SET (CPACK_RESOURCE_FILE_README "${CMAKE_CURRENT_SOURCE_DIR}/Readme.txt")
SET (CPACK_PACKAGE_RELOCATABLE TRUE)
SET (CPACK_PACKAGE_INSTALL_DIRECTORY "{CPACK_PACKAGE_VENDOR}/${CPACK_PACKAGE_NAME}/${CPACK_PACKAGE_VERSION}")
SET (CPACK_PACKAGE_ICON "${HDFJAVA_RESOURCES_DIR}/hdf.bmp")

SET (CPACK_GENERATOR "TGZ") 
IF (WIN32)
  LIST (APPEND CPACK_GENERATOR "NSIS") 
  # Installers for 32- vs. 64-bit CMake:
  #  - Root install directory (displayed to end user at installer-run time)
  #  - "NSIS package/display name" (text used in the installer GUI)
  #  - Registry key used to store info about the installation
  IF (CMAKE_CL_64)
    SET (CPACK_NSIS_INSTALL_ROOT "$PROGRAMFILES64")
    SET (CPACK_NSIS_PACKAGE_NAME "${HDFJAVA_PACKAGE_STRING}-${CPACK_PACKAGE_VERSION}")
    SET (CPACK_PACKAGE_INSTALL_REGISTRY_KEY "${CPACK_PACKAGE_NAME}-${CPACK_PACKAGE_VERSION} (Win64)")
  ELSE (CMAKE_CL_64)
    SET (CPACK_NSIS_INSTALL_ROOT "$PROGRAMFILES")
    SET (CPACK_NSIS_PACKAGE_NAME "${HDFJAVA_PACKAGE_STRING}-${CPACK_PACKAGE_VERSION}")
    SET (CPACK_PACKAGE_INSTALL_REGISTRY_KEY "${CPACK_PACKAGE_NAME}-${CPACK_PACKAGE_VERSION}")
  ENDIF (CMAKE_CL_64)
  # set the install/unistall icon used for the installer itself
  # There is a bug in NSI that does not handle full unix paths properly.
  SET (CPACK_NSIS_MUI_ICON "${HDFJAVA_RESOURCES_DIR}\\hdf.ico")
  SET (CPACK_NSIS_MUI_UNIICON "${HDFJAVA_RESOURCES_DIR}\\hdf.ico")
  # set the package header icon for MUI
  SET (CPACK_PACKAGE_ICON "${HDFJAVA_RESOURCES_DIR}\\hdf.bmp")
  SET (CPACK_NSIS_DISPLAY_NAME "@CPACK_NSIS_PACKAGE_NAME@, a visual tool for browsing and editing HDF files")
  SET (CPACK_PACKAGE_INSTALL_DIRECTORY "${CPACK_PACKAGE_VENDOR}\\\\${CPACK_PACKAGE_NAME}\\\\${CPACK_PACKAGE_VERSION}")
  SET (CPACK_MONOLITHIC_INSTALL ON)
  SET (CPACK_NSIS_CONTACT "${HDFJAVA_PACKAGE_BUGREPORT}")
  SET (CPACK_NSIS_MODIFY_PATH ON)
ELSEIF (APPLE)
#  LIST (APPEND CPACK_GENERATOR "DragNDrop") 
  SET (CPACK_COMPONENTS_ALL_IN_ONE_PACKAGE ON)
  SET (CPACK_PACKAGE_DEFAULT_LOCATION "/opt/${CPACK_PACKAGE_NAME}")
  SET (CPACK_PACKAGING_INSTALL_PREFIX "/")
  SET (CPACK_PACKAGE_ICON "${HDFJAVA_RESOURCES_DIR}/HDFView.icns")
  SET (CPACK_SET_DESTDIR TRUE) # Required when packaging, and set CMAKE_INSTALL_PREFIX to "/".
    
  IF (HDFJAVA_PACK_MACOSX_BUNDLE)
    LIST (APPEND CPACK_GENERATOR "Bundle")
    SET (CPACK_BUNDLE_NAME "${HDFJAVA_PACKAGE_STRING}")
    SET (CPACK_BUNDLE_LOCATION "/")    # make sure CMAKE_INSTALL_PREFIX ends in "/"
    SET (CMAKE_INSTALL_PREFIX "/${CPACK_BUNDLE_NAME}.app/Contents/Resources")
    SET (CPACK_BUNDLE_ICON "${HDFJAVA_RESOURCES_DIR}/HDFView.icns")
    SET (CPACK_BUNDLE_PLIST "${HDFJAVA_BINARY_DIR}/CMakeFiles/Info.plist")
#    SET (CPACK_BUNDLE_STARTUP_COMMAND "/System/Library/Frameworks/JavaVM.framework/Resources/MacOS/JavaApplicationStub")
    SET (CPACK_BUNDLE_STARTUP_COMMAND "${HDFJAVA_RESOURCES_DIR}/JavaAppLauncher")
    SET (CPACK_APPLE_GUI_BUNDLE_NAME "HDFView")
    SET (CPACK_APPLE_GUI_COPYRIGHT "Copyright © 2006-2014 by The HDF Group. All rights reserved.")
    SET (CPACK_APPLE_GUI_VERSION_STRING "${CPACK_PACKAGE_VERSION}")
    SET (CPACK_APPLE_GUI_SHORT_VERSION_STRING "${CPACK_PACKAGE_VERSION_MAJOR}.${CPACK_PACKAGE_VERSION_MINOR}")
    #-----------------------------------------------------------------------------
    # Configure the HDFJAVA-startup.sh and Info.plist file for the install bundle
    #-----------------------------------------------------------------------------
    CONFIGURE_FILE (
        ${HDFJAVA_RESOURCES_DIR}/CPack.Info.plist.in
        ${HDFJAVA_BINARY_DIR}/CMakeFiles/Info.plist @ONLY
    )
    CONFIGURE_FILE (
        ${HDFJAVA_RESOURCES_DIR}/PkgInfo.in
        ${HDFJAVA_BINARY_DIR}/CMakeFiles/PkgInfo @ONLY
    )
    CONFIGURE_FILE (
        ${HDFJAVA_RESOURCES_DIR}/version.plist.in
        ${HDFJAVA_BINARY_DIR}/CMakeFiles/version.plist @ONLY
    )
    INSTALL (
        FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/PkgInfo
              ${HDFJAVA_BINARY_DIR}/CMakeFiles/version.plist
        DESTINATION ..
    )
  ENDIF (HDFJAVA_PACK_MACOSX_BUNDLE)
ELSE (WIN32)
  LIST (APPEND CPACK_GENERATOR "STGZ") 
  SET (CPACK_PACKAGING_INSTALL_PREFIX "/${CPACK_PACKAGE_VENDOR}/${CPACK_PACKAGE_NAME}/${CPACK_PACKAGE_VERSION}")
  SET (CPACK_COMPONENTS_ALL_IN_ONE_PACKAGE ON)

  SET (CPACK_DEBIAN_PACKAGE_SECTION "Science")
  SET (CPACK_DEBIAN_PACKAGE_MAINTAINER "${HDFJAVA_PACKAGE_BUGREPORT}")
  
  SET (CPACK_RPM_COMPONENT_INSTALL ON)
  SET (CPACK_RPM_PACKAGE_RELOCATABLE ON)
  SET (CPACK_RPM_PACKAGE_GROUP "Development/Tools")
  SET (CPACK_RPM_PACKAGE_LICENSE "BSD-style")
  SET (CPACK_RPM_PACKAGE_URL "${HDFJAVA_PACKAGE_URL}")
  
  SET (CPACK_STRIP_FILES "${HDFJAVA_LIBRARIES_TO_EXPORT};${HDFJAVA_JARS_TO_EXPORT};${HDFJAVA_HDFVIEWJAR_TO_EXPORT};${HDFJAVA_LOGGING_NOP_JAR};${EXTERNAL_LIBRARY_LIST}")
ENDIF (WIN32)

SET (CPACK_NSIS_MUI_FINISHPAGE_RUN ${HDFVIEW_SCRIPT})

## By default, do not warn when built on machines using only VS Express:
#IF (NOT DEFINED CMAKE_INSTALL_SYSTEM_RUNTIME_LIBS_NO_WARNINGS)
#  SET (CMAKE_INSTALL_SYSTEM_RUNTIME_LIBS_NO_WARNINGS ON)
#ENDIF(NOT DEFINED CMAKE_INSTALL_SYSTEM_RUNTIME_LIBS_NO_WARNINGS)
#INCLUDE(InstallRequiredSystemLibraries)

SET (CPACK_INSTALL_CMAKE_PROJECTS "${HDFJAVA_BINARY_DIR};HDFJAVA;ALL;/")
  
IF (HDF_PACKAGE_EXTLIBS)
  IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      IF (HDF4_FOUND AND HDF4_USE_EXTERNAL)
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF4_INCLUDE_DIR_GEN};HDF4;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF4_INCLUDE_DIR_GEN};HDF4;configinstall;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${JPEG_INCLUDE_DIR_GEN};jpeg;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${JPEG_INCLUDE_DIR_GEN};jpeg;configinstall;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${ZLIB_INCLUDE_DIR_GEN};zlib;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${ZLIB_INCLUDE_DIR_GEN};zlib;configinstall;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${SZIP_INCLUDE_DIR_GEN};szip;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${SZIP_INCLUDE_DIR_GEN};szip;configinstall;/")
      ENDIF (HDF4_FOUND AND HDF4_USE_EXTERNAL)
      IF (HDF5_FOUND AND HDF5_USE_EXTERNAL)
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF5_INCLUDE_DIR_GEN};HDF5;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF5_INCLUDE_DIR_GEN};HDF5;configinstall;/")
      ENDIF (HDF5_FOUND AND HDF5_USE_EXTERNAL)
  ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
ENDIF (HDF_PACKAGE_EXTLIBS)

INCLUDE (CPack)

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
