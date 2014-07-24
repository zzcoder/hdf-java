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
#   install (EXPORT target...) syntax), then targets are not configured 
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
set (HDFJAVA_JNI_HDF_LIB_CORENAME          "jhdf")
set (HDFJAVA_JNI_HDF5_LIB_CORENAME         "jhdf5")
set (HDFJAVA_NCSA_HDF_LIB_CORENAME         "jarhdf")
set (HDFJAVA_NCSA_HDF5_LIB_CORENAME        "jarhdf5")
set (HDFJAVA_NCSA_OBJECT_LIB_CORENAME      "jarhdfobj")
set (HDFJAVA_NCSA_FITS_LIB_CORENAME        "jarfitsobj")
set (HDFJAVA_NCSA_H4_LIB_CORENAME          "jarh4obj")
set (HDFJAVA_NCSA_H5_LIB_CORENAME          "jarh5obj")
set (HDFJAVA_NCSA_NC2_LIB_CORENAME         "jarnc2obj")
set (HDFJAVA_TEST_HDF5_LIB_CORENAME        "jartest5")
set (HDFJAVA_TEST_OBJECT_LIB_CORENAME      "jartestobj")
set (HDFJAVA_TEST_UITEST_CORENAME          "jaruitest")
set (HDFJAVA_NCSA_VIEW_LIB_CORENAME        "jhdfview")

#-----------------------------------------------------------------------------
# Set the true names of all the libraries if customized by external project
#-----------------------------------------------------------------------------
set (HDFJAVA_JNI_HDF_LIB_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_JNI_HDF_LIB_CORENAME}")
set (HDFJAVA_JNI_HDF5_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_JNI_HDF5_LIB_CORENAME}")
set (HDFJAVA_NCSA_HDF_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_HDF_LIB_CORENAME}")
set (HDFJAVA_NCSA_HDF5_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_HDF5_LIB_CORENAME}")
set (HDFJAVA_NCSA_OBJECT_LIB_NAME  "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_OBJECT_LIB_CORENAME}")
set (HDFJAVA_NCSA_FITS_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_FITS_LIB_CORENAME}")
set (HDFJAVA_NCSA_H4_LIB_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_H4_LIB_CORENAME}")
set (HDFJAVA_NCSA_H5_LIB_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_H5_LIB_CORENAME}")
set (HDFJAVA_NCSA_NC2_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_NC2_LIB_CORENAME}")
set (HDFJAVA_TEST_HDF5_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_TEST_HDF5_LIB_CORENAME}")
set (HDFJAVA_TEST_OBJECT_LIB_NAME  "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_TEST_OBJECT_LIB_CORENAME}")
set (HDFJAVA_TEST_UITEST_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_TEST_UITEST_CORENAME}")
set (HDFJAVA_NCSA_VIEW_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_VIEW_LIB_CORENAME}")

#-----------------------------------------------------------------------------
# Set the target names of all the libraries
#-----------------------------------------------------------------------------
set (HDFJAVA_JNI_HDF_LIB_TARGET       ${HDFJAVA_JNI_HDF_LIB_CORENAME})
set (HDFJAVA_JNI_HDF5_LIB_TARGET      ${HDFJAVA_JNI_HDF5_LIB_CORENAME})
set (HDFJAVA_NCSA_HDF_LIB_TARGET      ${HDFJAVA_NCSA_HDF_LIB_CORENAME})
set (HDFJAVA_NCSA_HDF5_LIB_TARGET     ${HDFJAVA_NCSA_HDF5_LIB_CORENAME})
set (HDFJAVA_NCSA_OBJECT_LIB_TARGET   ${HDFJAVA_NCSA_OBJECT_LIB_CORENAME})
set (HDFJAVA_NCSA_FITS_LIB_TARGET     ${HDFJAVA_NCSA_FITS_LIB_CORENAME})
set (HDFJAVA_NCSA_H4_LIB_TARGET       ${HDFJAVA_NCSA_H4_LIB_CORENAME})
set (HDFJAVA_NCSA_H5_LIB_TARGET       ${HDFJAVA_NCSA_H5_LIB_CORENAME})
set (HDFJAVA_NCSA_NC2_LIB_TARGET      ${HDFJAVA_NCSA_NC2_LIB_CORENAME})
set (HDFJAVA_TEST_HDF5_LIB_TARGET     ${HDFJAVA_TEST_HDF5_LIB_CORENAME})
set (HDFJAVA_TEST_OBJECT_LIB_TARGET   ${HDFJAVA_TEST_OBJECT_LIB_CORENAME})
set (HDFJAVA_TEST_UITEST_TARGET       ${HDFJAVA_TEST_UITEST_CORENAME})
set (HDFJAVA_NCSA_VIEW_LIB_TARGET     ${HDFJAVA_NCSA_VIEW_LIB_CORENAME})

#-----------------------------------------------------------------------------
# Define some CMake variables for use later in the project
#-----------------------------------------------------------------------------
set (HDF_RESOURCES_DIR       ${HDFJAVA_SOURCE_DIR}/config/cmake)
set (HDF_RESOURCES_EXT_DIR           ${HDFJAVA_SOURCE_DIR}/config/cmake_ext_mod)
set (HDFJAVA_JNI_SRC_DIR         ${HDFJAVA_SOURCE_DIR}/native)
set (HDFJAVA_JAVA_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/ncsa/hdf)
set (HDFJAVA_JAVA_OBJECT_SRC_DIR ${HDFJAVA_SOURCE_DIR}/ncsa/hdf/object)
set (HDFJAVA_TEST_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/test)
set (HDFJAVA_EXAMPLES_SOURCE_DIR ${HDFJAVA_SOURCE_DIR}/examples)
set (HDFJAVA_LIB_DIR             ${HDFJAVA_SOURCE_DIR}/lib)
set (HDFJAVA_FITS_JAR            ${HDFJAVA_SOURCE_DIR}/lib/fits.jar)
set (HDFJAVA_NETCDF_JAR          ${HDFJAVA_SOURCE_DIR}/lib/netcdf.jar)
set (HDFJAVA_LOGGING_JAR         ${HDFJAVA_SOURCE_DIR}/lib/slf4j-api-1.7.5.jar)
set (HDFJAVA_LOGGING_NOP_JAR     ${HDFJAVA_SOURCE_DIR}/lib/ext/slf4j-nop-1.7.5.jar)
set (HDFJAVA_LOGGING_SIMPLE_JAR  ${HDFJAVA_SOURCE_DIR}/lib/ext/slf4j-simple-1.7.5.jar)

if (NOT HJAVA_INSTALL_BIN_DIR)
  set (HJAVA_INSTALL_BIN_DIR bin)
endif (NOT HJAVA_INSTALL_BIN_DIR)
if (NOT HJAVA_INSTALL_LIB_DIR)
  if (APPLE)
    option (HDFJAVA_PACK_MACOSX_BUNDLE "Create a Mac OSX Application Bundle" OFF)
    if (HDFJAVA_PACK_MACOSX_BUNDLE)
      option (HDFJAVA_PACK_MACOSX_16_BUNDLE "Create a Mac OSX Application Bundle with Apple Java 1.6" OFF)
      if (HDFJAVA_PACK_MACOSX_16_BUNDLE)
        set (HJAVA_INSTALL_JAR_DIR Java)
      else (HDFJAVA_PACK_MACOSX_16_BUNDLE)
        set (HJAVA_INSTALL_JAR_DIR ../Java)
      endif (HDFJAVA_PACK_MACOSX_16_BUNDLE)
    else (HDFJAVA_PACK_MACOSX_BUNDLE)
      set (HJAVA_INSTALL_JAR_DIR lib)
    endif (HDFJAVA_PACK_MACOSX_BUNDLE)
    set (HJAVA_INSTALL_LIB_DIR lib)
  else (APPLE)
    set (HJAVA_INSTALL_JAR_DIR lib)
    set (HJAVA_INSTALL_LIB_DIR lib)
  endif (APPLE)
endif (NOT HJAVA_INSTALL_LIB_DIR)
if (NOT HJAVA_INSTALL_INCLUDE_DIR)
  set (HJAVA_INSTALL_INCLUDE_DIR include)
endif (NOT HJAVA_INSTALL_INCLUDE_DIR)
if (NOT HJAVA_INSTALL_DATA_DIR)
  if (NOT WIN32)
    set (HJAVA_INSTALL_DATA_DIR share)
    set (HJAVA_INSTALL_CMAKE_DIR share/cmake)
    if (APPLE)
      if (HDFJAVA_PACK_MACOSX_BUNDLE)
        set (HJAVA_INSTALL_EXTRA_DIR ../SharedSupport)
      else (HDFJAVA_PACK_MACOSX_BUNDLE)
        set (HJAVA_INSTALL_EXTRA_DIR share)
      endif (HDFJAVA_PACK_MACOSX_BUNDLE)
    else (APPLE)
      set (HJAVA_INSTALL_EXTRA_DIR share)
    endif (APPLE)
  else (NOT WIN32)
    set (HJAVA_INSTALL_DATA_DIR ".")
    set (HJAVA_INSTALL_CMAKE_DIR cmake)
    set (HJAVA_INSTALL_EXTRA_DIR ".")
  endif (NOT WIN32)
endif (NOT HJAVA_INSTALL_DATA_DIR)

#-----------------------------------------------------------------------------
# parse the full version number from hjava.h and include in HJAVA_VERS_INFO
#-----------------------------------------------------------------------------
file (READ ${HDFJAVA_SOURCE_DIR}/VERSION _hjava_version_contents)
string (REGEX REPLACE ".*VERSION=hdf-java-([0-9]*.[0-9]*).*$"
    "\\1" HJAVA_FULL_VERSION ${_hjava_version_contents})
string (REGEX REPLACE ".*VERSION=hdf-java-([0-9]*).*$"
    "\\1" HJAVA_VERS_MAJOR ${_hjava_version_contents})
string (REGEX REPLACE ".*VERSION=hdf-java-[0-9]*.([0-9]*).*$"
    "\\1" HJAVA_VERS_MINOR ${_hjava_version_contents})
string (REGEX REPLACE ".*VERSION=hdf-java-[0-9]*.[0-9]*.([0-9]*).*$"
    "\\1" HJAVA_VERS_RELEASE ${_hjava_version_contents})
message (STATUS "VERSION: ${HJAVA_FULL_VERSION}")
message (STATUS "VERSION MAJOR: ${HJAVA_VERS_MAJOR}")
message (STATUS "VERSION MINOR: ${HJAVA_VERS_MINOR}")
if (NOT HJAVA_VERS_RELEASE)
  set (HJAVA_VERS_RELEASE 0)
endif (NOT HJAVA_VERS_RELEASE)
message (STATUS "VERSION RELEASE: ${HJAVA_VERS_RELEASE}")

#-----------------------------------------------------------------------------
# Basic HDFJAVA stuff here
#-----------------------------------------------------------------------------
set (HDFJAVA_PACKAGE "hdf-java")
set (HDFJAVA_PACKAGE_NAME "HDFView")
set (HDFJAVA_PACKAGE_VERSION "${HJAVA_VERS_MAJOR}.${HJAVA_VERS_MINOR}.${HJAVA_VERS_RELEASE}")
set (HDFJAVA_PACKAGE_VERSION_STRING "${HDFJAVA_PACKAGE_VERSION}")
set (HDFJAVA_PACKAGE_VERSION_MAJOR "${HJAVA_VERS_MAJOR}")
set (HDFJAVA_PACKAGE_VERSION_MINOR "${HJAVA_VERS_MINOR}")
set (HDFJAVA_PACKAGE_STRING "${HDFJAVA_PACKAGE_NAME}")
set (HDFJAVA_PACKAGE_TARNAME "hdf-java")
set (HDFJAVA_PACKAGE_URL "http://www.hdfgroup.org/hdfjava")
set (HDFJAVA_PACKAGE_BUGREPORT "help@hdfgroup.org")

set (CMAKE_MODULE_PATH "${HDF_RESOURCES_DIR};${HDF_RESOURCES_EXT_DIR};${CMAKE_ROOT}/Modules")
find_package (Java)
#-----------------------------------------------------------------------------
# Include some macros for reusable code
#-----------------------------------------------------------------------------
include (${HDF_RESOURCES_DIR}/UseJava.cmake)
include (${HDF_RESOURCES_EXT_DIR}/HDFMacros.cmake)
include (${HDF_RESOURCES_EXT_DIR}/HDFLibMacros.cmake)
include (${HDF_RESOURCES_DIR}/HDF45Macros.cmake)
include (${HDF_RESOURCES_DIR}/HDFJavaMacros.cmake)

#-----------------------------------------------------------------------------
# Setup output Directories
#-----------------------------------------------------------------------------
if (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  set (CMAKE_RUNTIME_OUTPUT_DIRECTORY
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Executables."
  )
  set (CMAKE_LIBRARY_OUTPUT_DIRECTORY
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Libraries"
  )
  set (CMAKE_ARCHIVE_OUTPUT_DIRECTORY
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all static libraries."
  )
  set (CMAKE_JAVA_TARGET_OUTPUT_DIR 
      ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all java jars."
  )
  if (WIN32)
    set (CMAKE_TEST_OUTPUT_DIRECTORY ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE})
  else (WIN32)
    set (CMAKE_TEST_OUTPUT_DIRECTORY ${CMAKE_RUNTIME_OUTPUT_DIRECTORY})
  endif (WIN32)
else (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  # if we are externally configured, but the project uses old cmake scripts
  # this may not be set and utilities like Hdetect will fail
  if (NOT CMAKE_RUNTIME_OUTPUT_DIRECTORY)
    set (CMAKE_RUNTIME_OUTPUT_DIRECTORY ${EXECUTABLE_OUTPUT_PATH})
  endif (NOT CMAKE_RUNTIME_OUTPUT_DIRECTORY)
endif (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Targets built within this project are exported at Install time for use
# by other projects using FindHDFJAVA.
#-----------------------------------------------------------------------------
if (NOT HDFJAVA_EXPORTED_TARGETS)
  set (HDFJAVA_EXPORTED_TARGETS "HDFJAVA-targets")
endif (NOT HDFJAVA_EXPORTED_TARGETS)

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
# Option to use code coverage
#-----------------------------------------------------------------------------
option (HDF_ENABLE_COVERAGE "Enable code coverage for Libraries and Programs" OFF)
if (HDF_ENABLE_COVERAGE)
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -g -O0 -fprofile-arcs -ftest-coverage")
    set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g -O0 -fprofile-arcs -ftest-coverage")
    set (LDFLAGS "${LDFLAGS} -fprofile-arcs -ftest-coverage")   
endif (HDF_ENABLE_COVERAGE)

#-----------------------------------------------------------------------------
# Include user macros
#-----------------------------------------------------------------------------
include (UserMacros.cmake)

#-----------------------------------------------------------------------------
# Include the main src and config directories
#-----------------------------------------------------------------------------
set (HDFJAVA_INCLUDE_DIRECTORIES
    ${HDFJAVA_JNI_SRC_DIR}/hdflib 
    ${HDFJAVA_JNI_SRC_DIR}/hdf5lib
    ${HDFJAVA_BINARY_DIR}
    ${JAVA_INCLUDE_PATH}
    ${JAVA_INCLUDE_PATH2}
)
INCLUDE_DIRECTORIES (${HDFJAVA_INCLUDE_DIRECTORIES})
set (CMAKE_JAVA_INCLUDE_PATH "")
set (HDFVIEW_OPTIONS_INCLUDE_PATH "")

set (MAKE_SYSTEM)
if (CMAKE_BUILD_TOOL MATCHES "make")
  set (MAKE_SYSTEM 1)
endif (CMAKE_BUILD_TOOL MATCHES "make")

set (CFG_INIT "/${CMAKE_CFG_INTDIR}")
if (MAKE_SYSTEM)
  set (CFG_INIT "")
endif (MAKE_SYSTEM)

#-----------------------------------------------------------------------------
# Dashboard and Testing Settings
#-----------------------------------------------------------------------------
option (BUILD_TESTING "Build HDFJAVA Unit Testing" OFF)
if (BUILD_TESTING)
  set (DART_TESTING_TIMEOUT 1200
      CACHE INTEGER
      "Timeout in seconds for each test (default 1200=20minutes)"
  )
  enable_testing ()
  include (CTest)
  include (${HDFJAVA_SOURCE_DIR}/CTestConfig.cmake)
  configure_file (${HDF_RESOURCES_EXT_DIR}/CTestCustom.cmake ${HDFJAVA_BINARY_DIR}/CTestCustom.ctest @ONLY)
endif (BUILD_TESTING)

#-----------------------------------------------------------------------------
# Add the HDFJAVA Library Target to the build
#-----------------------------------------------------------------------------
add_subdirectory (${HDFJAVA_SOURCE_DIR}/native    native)
add_subdirectory (${HDFJAVA_SOURCE_DIR}/ncsa      ncsa)
if (BUILD_TESTING)
  if (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
    add_subdirectory (${HDFJAVA_SOURCE_DIR}/test ${PROJECT_BINARY_DIR}/test)
  endif (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
endif (BUILD_TESTING)

#-----------------------------------------------------------------------------
# Option to build HDFJAVA Examples
#-----------------------------------------------------------------------------
if (HDF_BUILD_EXAMPLES)
  add_subdirectory (${HDFJAVA_SOURCE_DIR}/examples    examples)
endif (HDF_BUILD_EXAMPLES)

#-----------------------------------------------------------------------------
# Option for external libraries
#-----------------------------------------------------------------------------
option (HDF_PACKAGE_EXTLIBS "CPACK - include external libraries" OFF)
if (HDF_PACKAGE_EXTLIBS)
  if (HDF4_FOUND)
    PACKAGE_HDF4_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  endif (HDF4_FOUND)
    
  if (HDF5_FOUND)
    PACKAGE_HDF5_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  endif (HDF5_FOUND)
endif (HDF_PACKAGE_EXTLIBS)

#-----------------------------------------------------------------------------
# Add Required Jar(s)
#-----------------------------------------------------------------------------
install (
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
option (HDFJAVA_PACK_EXAMPLES  "Package the HDFJAVA Library Examples Compressed File" OFF)
if (HDFJAVA_PACK_EXAMPLES)
  configure_file (
      ${HDF_RESOURCES_DIR}/HDFJAVA_Examples.cmake.in 
      ${HDFJAVA_BINARY_DIR}/HDFJAVA_Examples.cmake @ONLY
  )
  install (
      FILES ${HDFJAVA_BINARY_DIR}/HDFJAVA_Examples.cmake
      DESTINATION ${HJAVA_INSTALL_EXTRA_DIR}
      COMPONENT docs
  )
  if (NOT ${HDFJAVA_EXAMPLES_COMPRESSED} STREQUAL "")
    if (EXISTS "${HDFJAVA_EXAMPLES_COMPRESSED_DIR}/${HDFJAVA_EXAMPLES_COMPRESSED}")
      install (
          FILES
              ${HDFJAVA_EXAMPLES_COMPRESSED_DIR}/${HDFJAVA_EXAMPLES_COMPRESSED}
              ${HDF_RESOURCES_DIR}/USING_CMake_Examples.txt
          DESTINATION ${HJAVA_INSTALL_EXTRA_DIR}
          COMPONENT docs
      )
    endif (EXISTS "${HDFJAVA_EXAMPLES_COMPRESSED_DIR}/${HDFJAVA_EXAMPLES_COMPRESSED}")
  endif (NOT ${HDFJAVA_EXAMPLES_COMPRESSED} STREQUAL "")
endif (HDFJAVA_PACK_EXAMPLES)

#set (CREATE_JAVADOCS "TRUE")
#-----------------------------------------------------------------------------
# Add Required JavaDocs
#-----------------------------------------------------------------------------
if (CREATE_JAVADOCS)
  set (HDFVIEW_TEST_CLASSPATH "${${HDFJAVA_NCSA_VIEW_LIB_TARGET}_JAR_FILE}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_LOGGING_SIMPLE_JAR}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_LOGGING_JAR}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_LIB_DIR}")
  CREATE_JAVADOC (hdf_java_doc
      PACKAGES ${HDFJAVA_SOURCE_PACKAGES}
      SOURCEPATH "${CMAKE_CURRENT_SOURCE_DIR}"
      CLASSPATH ${HDFVIEW_TEST_CLASSPATH}
      INSTALLPATH ${HJAVA_INSTALL_DATA_DIR}
      WINDOWTITLE "HDFJava 2.99"
      DOCTITLE "<h1>HDFJava 2.99</h1>"
      AUTHOR TRUE
      USE TRUE
      VERSION TRUE
  )
else (CREATE_JAVADOCS)
  install (
      DIRECTORY ${HDFJAVA_SOURCE_DIR}/docs/javadocs
      DESTINATION ${HJAVA_INSTALL_DATA_DIR}
      USE_SOURCE_PERMISSIONS 
      COMPONENT docs
      PATTERN ".svn" EXCLUDE
  )
endif (CREATE_JAVADOCS)

#-----------------------------------------------------------------------------
# Add Required Document(s)
#-----------------------------------------------------------------------------
option (HDFJAVA_PACK_SAMPLES  "Package the HDFJAVA File Samples" OFF)
if (HDFJAVA_PACK_SAMPLES)
  install (
      DIRECTORY ${HDFJAVA_SOURCE_DIR}/docs/samples
      DESTINATION ${HJAVA_INSTALL_EXTRA_DIR}
      USE_SOURCE_PERMISSIONS 
      COMPONENT docs
      PATTERN ".svn" EXCLUDE
  )
endif (HDFJAVA_PACK_SAMPLES)

if (NOT ${HDFJAVA_USERSGUIDE_COMPRESSED} STREQUAL "")
  if (EXISTS "${HDFJAVA_USERSGUIDE_COMPRESSED_DIR}/${HDFJAVA_USERSGUIDE_COMPRESSED}")
    install (
        FILES
            ${HDFJAVA_USERSGUIDE_COMPRESSED_DIR}/${HDFJAVA_USERSGUIDE_COMPRESSED}
        DESTINATION ${HJAVA_INSTALL_DATA_DIR}
        COMPONENT docs
    )
  endif (EXISTS "${HDFJAVA_USERSGUIDE_COMPRESSED_DIR}/${HDFJAVA_USERSGUIDE_COMPRESSED}")
endif (NOT ${HDFJAVA_USERSGUIDE_COMPRESSED} STREQUAL "")

#-----------------------------------------------------------------------------
# Add Target(s) to CMake Install for import into other projects
#-----------------------------------------------------------------------------
if (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  install (
      EXPORT ${HDFJAVA_EXPORTED_TARGETS}
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
      FILE ${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-targets.cmake
  )
endif (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Export all exported targets to the build tree for use by parent project
#-----------------------------------------------------------------------------
if (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  export (
      TARGETS ${HDFJAVA_LIBRARIES_TO_EXPORT} ${HDFJAVA_LIB_DEPENDENCIES}
      FILE ${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-targets.cmake
  )
endif (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config.cmake file for the build directory
#-----------------------------------------------------------------------------
set (HDFJAVA_INCLUDES_BUILD_TIME
    ${HDFJAVA_JNI_SRC_DIR}
    ${HDFJAVA_JAVA_SRC_DIR}
    ${HDFJAVA_BINARY_DIR}
)

configure_file (
    ${HDF_RESOURCES_DIR}/HDFJAVA-config.cmake.build.in 
    ${HDFJAVA_BINARY_DIR}/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config.cmake @ONLY
)

#-----------------------------------------------------------------------------
# Configure the FindJAVA.cmake file for the install directory
#-----------------------------------------------------------------------------
if (NOT H4H5_EXTERNALLY_CONFIGURED)
  configure_file (
      ${HDF_RESOURCES_DIR}/FindHDFJAVA.cmake.in 
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/FindHDFJAVA${HDF_PACKAGE_EXT}.cmake @ONLY
  )
  install (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/FindHDFJAVA${HDF_PACKAGE_EXT}.cmake
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
  )
endif (NOT H4H5_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config.cmake file for the install directory
#-----------------------------------------------------------------------------
if (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  configure_file (
      ${HDF_RESOURCES_DIR}/HDFJAVA-config.cmake.install.in
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config.cmake @ONLY
  )
  install (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config.cmake
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
  )
endif (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Configure the HDFJAVA-config-version .cmake file for the install directory
#-----------------------------------------------------------------------------
if (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  configure_file (
      ${HDF_RESOURCES_DIR}/HDFJAVA-config-version.cmake.in
      ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config-version.cmake @ONLY
  )
  install (
      FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/${HDFJAVA_PACKAGE}${HDF_PACKAGE_EXT}-config-version.cmake
      DESTINATION ${HJAVA_INSTALL_CMAKE_DIR}/${HDFJAVA_PACKAGE}
  )
endif (NOT HDFJAVA_EXTERNALLY_CONFIGURED)

#-----------------------------------------------------------------------------
# Option to include jre
#-----------------------------------------------------------------------------
option (HDFJAVA_PACK_JRE  "Package a JRE installer file" OFF)
if (HDFJAVA_PACK_JRE)
  if (WIN32)
    install (
        DIRECTORY "C:/Program Files/Java/jre7"
        DESTINATION ${HJAVA_INSTALL_BIN_DIR}
        USE_SOURCE_PERMISSIONS
    )
  else (WIN32)
    install (
        DIRECTORY "C:/Program Files/Java/jre7"
        DESTINATION ${HJAVA_INSTALL_BIN_DIR}
        USE_SOURCE_PERMISSIONS
    )
  endif (WIN32)
endif (HDFJAVA_PACK_JRE)

#-----------------------------------------------------------------------------
# Configure the README.txt file for the binary package
#-----------------------------------------------------------------------------
set (BINARY_SYSTEM_NAME ${CMAKE_SYSTEM_NAME})
set (BINARY_PLATFORM "${CMAKE_SYSTEM_NAME}")
if (WIN32)
  set (BINARY_EXAMPLE_ENDING "zip")
  set (BINARY_INSTALL_ENDING "exe")
  if (CMAKE_CL_64)
    set (BINARY_SYSTEM_NAME "win64")
  else (CMAKE_CL_64)
    set (BINARY_SYSTEM_NAME "win32")
  endif (CMAKE_CL_64)
  if (${CMAKE_SYSTEM_VERSION} MATCHES "6.1")
    set (BINARY_PLATFORM "${BINARY_PLATFORM} 7")
  elseif (${CMAKE_SYSTEM_VERSION} MATCHES "6.2")
    set (BINARY_PLATFORM "${BINARY_PLATFORM} 8")
  endif (${CMAKE_SYSTEM_VERSION} MATCHES "6.1")
  set (BINARY_PLATFORM "${BINARY_PLATFORM} ${MSVC_C_ARCHITECTURE_ID}")
  if (${CMAKE_C_COMPILER_VERSION} MATCHES "16.*")
    set (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO 2010")
  elseif (${CMAKE_C_COMPILER_VERSION} MATCHES "15.*")
    set (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO 2008")
  elseif (${CMAKE_C_COMPILER_VERSION} MATCHES "17.*")
    set (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO 2012")
  elseif (${CMAKE_C_COMPILER_VERSION} MATCHES "18.*")
    set (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO 2013")
  else (${CMAKE_C_COMPILER_VERSION} MATCHES "16.*")
    set (BINARY_PLATFORM "${BINARY_PLATFORM}, using VISUAL STUDIO ${CMAKE_C_COMPILER_VERSION}")
  endif (${CMAKE_C_COMPILER_VERSION} MATCHES "16.*")
elseif (APPLE)
  set (BINARY_EXAMPLE_ENDING "tar.gz")
  set (BINARY_INSTALL_ENDING "dmg")
  set (BINARY_PLATFORM "${BINARY_PLATFORM} ${CMAKE_SYSTEM_VERSION} ${CMAKE_SYSTEM_PROCESSOR}")
  set (BINARY_PLATFORM "${BINARY_PLATFORM}, using ${CMAKE_C_COMPILER_ID} C ${CMAKE_C_COMPILER_VERSION}")
else (WIN32)
  set (BINARY_EXAMPLE_ENDING "tar.gz")
  set (BINARY_INSTALL_ENDING "sh")
  set (BINARY_PLATFORM "${BINARY_PLATFORM} ${CMAKE_SYSTEM_VERSION} ${CMAKE_SYSTEM_PROCESSOR}")
  set (BINARY_PLATFORM "${BINARY_PLATFORM}, using ${CMAKE_C_COMPILER_ID} C ${CMAKE_C_COMPILER_VERSION}")
endif (WIN32)
set (BINARY_PLATFORM "${BINARY_PLATFORM} / Java ${Java_VERSION_MAJOR}.${Java_VERSION_MINOR}")
if (HDFJAVA_PACK_JRE)
  set (BINARY_PLATFORM "${BINARY_PLATFORM}. An embedded JRE will be installed.")
endif (HDFJAVA_PACK_JRE)

configure_file (
    ${HDF_RESOURCES_DIR}/README.txt.cmake.in 
    ${HDFJAVA_BINARY_DIR}/README.txt @ONLY
)

#-----------------------------------------------------------------------------
# Set the cpack variables
#-----------------------------------------------------------------------------
set (CPACK_PACKAGE_VENDOR "HDF_Group")
set (CPACK_PACKAGE_DESCRIPTION_SUMMARY "HDF-JAVA Installation")
set (CPACK_PACKAGE_DESCRIPTION_FILE "${CMAKE_CURRENT_SOURCE_DIR}/Readme.txt")
set (CPACK_PACKAGE_NAME "${HDFJAVA_PACKAGE_NAME}")
if (CDASH_LOCAL)
  set (CPACK_PACKAGE_VERSION "${HDFJAVA_PACKAGE_VERSION}")
else (CDASH_LOCAL)
  set (CPACK_PACKAGE_VERSION "${HDFJAVA_PACKAGE_VERSION_STRING}")
endif (CDASH_LOCAL)
set (CPACK_PACKAGE_VERSION_MAJOR "${HDFJAVA_PACKAGE_VERSION_MAJOR}")
set (CPACK_PACKAGE_VERSION_MINOR "${HDFJAVA_PACKAGE_VERSION_MINOR}")
set (CPACK_PACKAGE_VERSION_PATCH "")
set (CPACK_RESOURCE_FILE_LICENSE "${CMAKE_CURRENT_SOURCE_DIR}/COPYING")
set (CPACK_RESOURCE_FILE_README "${CMAKE_CURRENT_SOURCE_DIR}/Readme.txt")
set (CPACK_PACKAGE_RELOCATABLE TRUE)
set (CPACK_PACKAGE_INSTALL_DIRECTORY "{CPACK_PACKAGE_VENDOR}/${CPACK_PACKAGE_NAME}/${CPACK_PACKAGE_VERSION}")
set (CPACK_PACKAGE_ICON "${HDF_RESOURCES_EXT_DIR}/hdf.bmp")

set (CPACK_GENERATOR "TGZ") 
if (WIN32)
  list (APPEND CPACK_GENERATOR "NSIS") 
  # Installers for 32- vs. 64-bit CMake:
  #  - Root install directory (displayed to end user at installer-run time)
  #  - "NSIS package/display name" (text used in the installer GUI)
  #  - Registry key used to store info about the installation
  set (CPACK_NSIS_PACKAGE_NAME "${HDFJAVA_PACKAGE_STRING}-${CPACK_PACKAGE_VERSION}")
  if (CMAKE_CL_64)
    set (CPACK_NSIS_INSTALL_ROOT "$PROGRAMFILES64")
    set (CPACK_PACKAGE_INSTALL_REGISTRY_KEY "${CPACK_PACKAGE_NAME}-${CPACK_PACKAGE_VERSION} (Win64)")
  else (CMAKE_CL_64)
    set (CPACK_NSIS_INSTALL_ROOT "$PROGRAMFILES")
    set (CPACK_PACKAGE_INSTALL_REGISTRY_KEY "${CPACK_PACKAGE_NAME}-${CPACK_PACKAGE_VERSION}")
  endif (CMAKE_CL_64)
  # set the install/unistall icon used for the installer itself
  # There is a bug in NSI that does not handle full unix paths properly.
  set (CPACK_NSIS_MUI_ICON "${HDF_RESOURCES_EXT_DIR}\\\\hdf.ico")
  set (CPACK_NSIS_MUI_UNIICON "${HDF_RESOURCES_EXT_DIR}\\\\hdf.ico")
  # set the package header icon for MUI
  set (CPACK_PACKAGE_ICON "${HDF_RESOURCES_EXT_DIR}\\\\hdf.bmp")
  set (CPACK_NSIS_DISPLAY_NAME "${CPACK_NSIS_PACKAGE_NAME}, a visual tool for browsing and editing HDF files")
  set (CPACK_PACKAGE_INSTALL_DIRECTORY "${CPACK_PACKAGE_VENDOR}\\\\${CPACK_PACKAGE_NAME}\\\\${CPACK_PACKAGE_VERSION}")
  set (CPACK_MONOLITHIC_INSTALL ON)
  set (CPACK_NSIS_CONTACT "${HDFJAVA_PACKAGE_BUGREPORT}")
  set (CPACK_NSIS_MODIFY_PATH ON)
elseif (APPLE)
#  list (APPEND CPACK_GENERATOR "DragNDrop") 
  set (CPACK_COMPONENTS_ALL_IN_ONE_PACKAGE ON)
  set (CPACK_PACKAGING_INSTALL_PREFIX "/${CPACK_PACKAGE_INSTALL_DIRECTORY}")
  set (CPACK_PACKAGE_ICON "${HDF_RESOURCES_EXT_DIR}/hdf.icns")
    
  if (HDFJAVA_PACK_MACOSX_BUNDLE)
    list (APPEND CPACK_GENERATOR "Bundle")
    set (CPACK_BUNDLE_NAME "${HDFJAVA_PACKAGE_STRING}")
    set (CPACK_BUNDLE_LOCATION "/")    # make sure CMAKE_INSTALL_PREFIX ends in "/"
    set (CMAKE_INSTALL_PREFIX "/${CPACK_BUNDLE_NAME}.app/Contents/Resources")
    set (CPACK_BUNDLE_ICON "${HDF_RESOURCES_EXT_DIR}/hdf.icns")
    set (CPACK_BUNDLE_PLIST "${HDFJAVA_BINARY_DIR}/CMakeFiles/Info.plist")
    if (HDFJAVA_PACK_MACOSX_16_BUNDLE)
      set (CPACK_BUNDLE_STARTUP_COMMAND "/System/Library/Frameworks/JavaVM.framework/Resources/MacOS/JavaApplicationStub")
    else (HDFJAVA_PACK_MACOSX_16_BUNDLE)
      set (CPACK_BUNDLE_STARTUP_COMMAND "${HDF_RESOURCES_DIR}/JavaAppLauncher")
    endif (HDFJAVA_PACK_MACOSX_16_BUNDLE)
    set (CPACK_APPLE_GUI_BUNDLE_NAME "HDFView")
    set (CPACK_APPLE_GUI_COPYRIGHT "Copyright © 2006-2014 by The HDF Group. All rights reserved.")
    set (CPACK_APPLE_GUI_VERSION_STRING "${CPACK_PACKAGE_VERSION}")
    set (CPACK_APPLE_GUI_SHORT_VERSION_STRING "${CPACK_PACKAGE_VERSION_MAJOR}.${CPACK_PACKAGE_VERSION_MINOR}")
    #-----------------------------------------------------------------------------
    # Configure the HDFJAVA-startup.sh and Info.plist file for the install bundle
    #-----------------------------------------------------------------------------
    configure_file (
        ${HDF_RESOURCES_DIR}/CPack.Info.plist.in
        ${HDFJAVA_BINARY_DIR}/CMakeFiles/Info.plist @ONLY
    )
    configure_file (
        ${HDF_RESOURCES_DIR}/PkgInfo.in
        ${HDFJAVA_BINARY_DIR}/CMakeFiles/PkgInfo @ONLY
    )
    configure_file (
        ${HDF_RESOURCES_EXT_DIR}/version.plist.in
        ${HDFJAVA_BINARY_DIR}/CMakeFiles/version.plist @ONLY
    )
    install (
        FILES ${HDFJAVA_BINARY_DIR}/CMakeFiles/PkgInfo
              ${HDFJAVA_BINARY_DIR}/CMakeFiles/version.plist
        DESTINATION ..
    )
  endif (HDFJAVA_PACK_MACOSX_BUNDLE)
else (WIN32)
  list (APPEND CPACK_GENERATOR "STGZ") 
  set (CPACK_PACKAGING_INSTALL_PREFIX "/${CPACK_PACKAGE_VENDOR}/${CPACK_PACKAGE_NAME}/${CPACK_PACKAGE_VERSION}")
  set (CPACK_COMPONENTS_ALL_IN_ONE_PACKAGE ON)

  set (CPACK_DEBIAN_PACKAGE_SECTION "Science")
  set (CPACK_DEBIAN_PACKAGE_MAINTAINER "${HDFJAVA_PACKAGE_BUGREPORT}")
  
  set (CPACK_RPM_COMPONENT_INSTALL ON)
  set (CPACK_RPM_PACKAGE_RELOCATABLE ON)
  set (CPACK_RPM_PACKAGE_GROUP "Development/Tools")
  set (CPACK_RPM_PACKAGE_LICENSE "BSD-style")
  set (CPACK_RPM_PACKAGE_URL "${HDFJAVA_PACKAGE_URL}")
  
  set (CPACK_STRIP_FILES "${HDFJAVA_LIBRARIES_TO_EXPORT};${HDFJAVA_JARS_TO_EXPORT};${HDFJAVA_HDFVIEWJAR_TO_EXPORT};${HDFJAVA_LOGGING_NOP_JAR};${EXTERNAL_LIBRARY_LIST}")
endif (WIN32)

set (CPACK_NSIS_MUI_FINISHPAGE_RUN ${HDFVIEW_SCRIPT})

# By default, do not warn when built on machines using only VS Express:
if (NOT DEFINED CMAKE_INSTALL_SYSTEM_RUNTIME_LIBS_NO_WARNINGS)
  set (CMAKE_INSTALL_SYSTEM_RUNTIME_LIBS_NO_WARNINGS ON)
endif( NOT DEFINED CMAKE_INSTALL_SYSTEM_RUNTIME_LIBS_NO_WARNINGS)
include (InstallRequiredSystemLibraries)

set (CPACK_INSTALL_CMAKE_PROJECTS "${HDFJAVA_BINARY_DIR};HDFJAVA;ALL;/")
  
if (HDF_PACKAGE_EXTLIBS)
  if (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      if (HDF4_FOUND AND HDF4_USE_EXTERNAL)
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF4_INCLUDE_DIR_GEN};HDF4;libraries;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF4_INCLUDE_DIR_GEN};HDF4;configinstall;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${JPEG_INCLUDE_DIR_GEN};jpeg;libraries;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${JPEG_INCLUDE_DIR_GEN};jpeg;configinstall;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${ZLIB_INCLUDE_DIR_GEN};zlib;libraries;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${ZLIB_INCLUDE_DIR_GEN};zlib;configinstall;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${SZIP_INCLUDE_DIR_GEN};szip;libraries;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${SZIP_INCLUDE_DIR_GEN};szip;configinstall;/")
      endif (HDF4_FOUND AND HDF4_USE_EXTERNAL)
      if (HDF5_FOUND AND HDF5_USE_EXTERNAL)
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF5_INCLUDE_DIR_GEN};HDF5;libraries;/")
        set (CPACK_INSTALL_CMAKE_PROJECTS "${CPACK_INSTALL_CMAKE_PROJECTS};${HDF5_INCLUDE_DIR_GEN};HDF5;configinstall;/")
      endif (HDF5_FOUND AND HDF5_USE_EXTERNAL)
  endif (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
endif (HDF_PACKAGE_EXTLIBS)

include (CPack)

#-----------------------------------------------------------------------------
# Now list the cpack commands
#-----------------------------------------------------------------------------
cpack_add_component (apps 
    DISPLAY_NAME "HDFJAVA Applications" 
    DEPENDS libraries
    GROUP Applications
)
cpack_add_component (libraries 
    DISPLAY_NAME "HDFJAVA Libraries"
    GROUP Runtime
)
cpack_add_component (headers 
    DISPLAY_NAME "HDFJAVA Headers" 
    DEPENDS libraries
    GROUP Development
)
cpack_add_component (docs 
    DISPLAY_NAME "HDFJAVA Documents" 
    DEPENDS libraries
    GROUP Documents
)
