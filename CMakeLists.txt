cmake_minimum_required(VERSION 2.8.6)
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
SET_GLOBAL_VARIABLE (HDFJAVA_HDFVIEWJAR_TO_EXPORT "")

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
SET (HDFVIEW_OPTIONS_INCLUDE_PATH ${HDFJAVA_LIB_DIR}/fits.jar ${HDFJAVA_LIB_DIR}/netcdf.jar)

SET (MAKE_SYSTEM)
IF (CMAKE_BUILD_TOOL MATCHES "make")
  SET (MAKE_SYSTEM 1)
ENDIF (CMAKE_BUILD_TOOL MATCHES "make")

SET (CFG_INIT "/${CMAKE_CFG_INTDIR}")
IF (MAKE_SYSTEM)
  SET (CFG_INIT "")
ENDIF (MAKE_SYSTEM)

#-----------------------------------------------------------------------------
# Build utility to copy and strip X lines of file
#-----------------------------------------------------------------------------
IF (HDF_BUILD_EXAMPLES AND BUILD_TESTING)
  SET (XLATE_UTILITY "xlatefile")
  ADD_EXECUTABLE(${XLATE_UTILITY} ${HDFJAVA_RESOURCES_DIR}/xlatefile.c)
ENDIF (HDF_BUILD_EXAMPLES AND BUILD_TESTING)

FIND_PACKAGE(JNI)

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
      DESTINATION ${HJAVA_INSTALL_DATA_DIR}/cmake/HDFJAVA
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
      DESTINATION ${HJAVA_INSTALL_DATA_DIR}/cmake/HDFJAVA
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
      DESTINATION ${HJAVA_INSTALL_DATA_DIR}/cmake/HDFJAVA
  )
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Set the cpack variables
#-----------------------------------------------------------------------------
SET (CPACK_PACKAGE_VENDOR "HDF Group")
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

IF (CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)
  IF(CMAKE_HOST_UNIX)
    SET(CMAKE_INSTALL_PREFIX "/usr/local/${CPACK_PACKAGE_VENDOR}/${HDFJAVA_PACKAGE_NAME}/${HDFJAVA_PACKAGE_VERSION}"
      CACHE PATH "Install path prefix, prepended onto install directories." FORCE)
  ELSE(CMAKE_HOST_UNIX)
    GetDefaultWindowsPrefixBase(CMAKE_GENERIC_PROGRAM_FILES)
    SET(CMAKE_INSTALL_PREFIX
      "${CMAKE_GENERIC_PROGRAM_FILES}/${CPACK_PACKAGE_VENDOR}/${HDFJAVA_PACKAGE_NAME}/${HDFJAVA_PACKAGE_VERSION}"
      CACHE PATH "Install path prefix, prepended onto install directories." FORCE)
    SET(CMAKE_GENERIC_PROGRAM_FILES)
  ENDIF(CMAKE_HOST_UNIX)
ENDIF (CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)

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
  SET (CPACK_STRIP_FILES "${HDFJAVA_LIBRARIES_TO_EXPORT};${HDFJAVA_JARS_TO_EXPORT};${HDFJAVA_HDFVIEWJAR_TO_EXPORT};${EXTERNAL_LIBRARY_LIST}")
ENDIF (WIN32)
SET (CPACK_MONOLITHIC_INSTALL ON)

SET (CPACK_NSIS_MUI_FINISHPAGE_RUN ${HDFVIEW_SCRIPT})

INCLUDE(InstallRequiredSystemLibraries)

  SET (CPACK_INSTALL_CMAKE_PROJECTS "${HDFJAVA_BINARY_DIR};HDFJAVA;ALL;/")
  
  IF (HDF_PACKAGE_EXTLIBS)
    IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      IF (JPEG_FOUND AND JPEG_USE_EXTERNAL)
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${JPEG_INCLUDE_DIR_GEN};JPEG;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${JPEG_INCLUDE_DIR_GEN};JPEG;configinstall;/")
      ENDIF (JPEG_FOUND AND JPEG_USE_EXTERNAL)
      IF (ZLIB_FOUND AND ZLIB_USE_EXTERNAL)
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${ZLIB_INCLUDE_DIR_GEN};ZLIB;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${ZLIB_INCLUDE_DIR_GEN};ZLIB;configinstall;/")
      ENDIF (ZLIB_FOUND AND ZLIB_USE_EXTERNAL)
      IF (SZIP_FOUND AND SZIP_USE_EXTERNAL)
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${SZIP_INCLUDE_DIR_GEN};SZIP;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${SZIP_INCLUDE_DIR_GEN};SZIP;configinstall;/")
      ENDIF (SZIP_FOUND AND SZIP_USE_EXTERNAL)
      IF (HDF4_FOUND AND HDF4_USE_EXTERNAL)
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF4_INCLUDE_DIR_GEN};HDF4;libraries;/")
        SET (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF4_INCLUDE_DIR_GEN};HDF4;configinstall;/")
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
