cmake_minimum_required(VERSION 3.1.0)
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
set (HDFJAVA_TEST_HDF5_LIB_CORENAME        "jartest5")

#-----------------------------------------------------------------------------
# Set the true names of all the libraries if customized by external project
#-----------------------------------------------------------------------------
set (HDFJAVA_JNI_HDF_LIB_NAME      "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_JNI_HDF_LIB_CORENAME}")
set (HDFJAVA_JNI_HDF5_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_JNI_HDF5_LIB_CORENAME}")
set (HDFJAVA_NCSA_HDF_LIB_NAME     "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_HDF_LIB_CORENAME}")
set (HDFJAVA_NCSA_HDF5_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_NCSA_HDF5_LIB_CORENAME}")
set (HDFJAVA_TEST_HDF5_LIB_NAME    "${HDFJAVA_EXTERNAL_LIB_PREFIX}${HDFJAVA_TEST_HDF5_LIB_CORENAME}")

#-----------------------------------------------------------------------------
# Set the target names of all the libraries
#-----------------------------------------------------------------------------
set (HDFJAVA_JNI_HDF_LIB_TARGET       ${HDFJAVA_JNI_HDF_LIB_CORENAME})
set (HDFJAVA_JNI_HDF5_LIB_TARGET      ${HDFJAVA_JNI_HDF5_LIB_CORENAME})
set (HDFJAVA_NCSA_HDF_LIB_TARGET      ${HDFJAVA_NCSA_HDF_LIB_CORENAME})
set (HDFJAVA_NCSA_HDF5_LIB_TARGET     ${HDFJAVA_NCSA_HDF5_LIB_CORENAME})
set (HDFJAVA_TEST_HDF5_LIB_TARGET     ${HDFJAVA_TEST_HDF5_LIB_CORENAME})

#-----------------------------------------------------------------------------
# Define some CMake variables for use later in the project
#-----------------------------------------------------------------------------
set (HDF_RESOURCES_DIR           ${HDFJAVA_SOURCE_DIR}/config/cmake)
set (HDF_RESOURCES_EXT_DIR       ${HDFJAVA_SOURCE_DIR}/config/cmake_ext_mod)
set (HDFJAVA_JAVA_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/ncsa/hdf)
set (HDFJAVA_TEST_SRC_DIR        ${HDFJAVA_SOURCE_DIR}/test)
set (HDFJAVA_LIB_DIR             ${HDFJAVA_SOURCE_DIR}/lib)
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
set (HDFJAVA_PACKAGE_NAME "HDFJAVA")
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
    hdflib 
    hdf5lib
    ${HDFJAVA_BINARY_DIR}
    ${JAVA_INCLUDE_PATH}
    ${JAVA_INCLUDE_PATH2}
)
INCLUDE_DIRECTORIES (${HDFJAVA_INCLUDE_DIRECTORIES})
set (CMAKE_JAVA_INCLUDE_PATH "")

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

message (STATUS "JAVA: JAVA_HOME=$ENV{JAVA_HOME} JAVA_ROOT=$ENV{JAVA_ROOT}")
find_package (JNI)

INCLUDE_DIRECTORIES ( ${JNI_INCLUDE_DIRS} )

#-----------------------------------------------------------------------------
# Option to Build Shared/Static libs, default is static
#-----------------------------------------------------------------------------
option (BUILD_SHARED_LIBS "Build Shared Libraries" OFF)
set (LIB_TYPE STATIC)
if (BUILD_SHARED_LIBS)
  set (LIB_TYPE SHARED)
  add_definitions (-DHDFJAVA_BUILT_AS_DYNAMIC_LIB)
else (BUILD_SHARED_LIBS)
  add_definitions (-DHDFJAVA_BUILT_AS_STATIC_LIB)
  set (CMAKE_POSITION_INDEPENDENT_CODE ON)
endif (BUILD_SHARED_LIBS)
set (LIB_TYPE ${LIB_TYPE})

#-----------------------------------------------------------------------------
# When building utility executables that generate other (source) files :
# we make use of the following variables defined in the root CMakeLists.
# Certain systems may add /Debug or /Release to output paths
# and we need to call the executable from inside the CMake configuration
#-----------------------------------------------------------------------------
set (EXE_EXT "")
if (WIN32)
  set (EXE_EXT ".exe")
  if (NOT CYGWIN)
    add_definitions (-D_BIND_TO_CURRENT_VCLIBS_VERSION=1)
    add_definitions (-D_CRT_SECURE_NO_WARNINGS)
  endif (NOT CYGWIN)
endif (WIN32)

if (MSVC)
  set (CMAKE_MFC_FLAG 0)
endif (MSVC)

#-----------------------------------------------------------------------------
# Add some definitions for Debug Builds
#-----------------------------------------------------------------------------
if (CMAKE_BUILD_TYPE MATCHES Debug)
  add_definitions (-DDEBUG)
  #-- NMake Makefiles will overwhelm the console with warnings if -Wall is used.
  if (NOT WIN32)
    add_definitions (-Wall)
  endif (NOT WIN32)
else (CMAKE_BUILD_TYPE MATCHES Debug)
  add_definitions (-UH4_DEBUG_API -DNDEBUG)
endif (CMAKE_BUILD_TYPE MATCHES Debug)

#-----------------------------------------------------------------------------
# Compiler specific flags : Shouldn't there be compiler tests for these
#-----------------------------------------------------------------------------
if (CMAKE_COMPILER_IS_GNUCC)
  set (CMAKE_C_FLAGS "${CMAKE_ANSI_CFLAGS} ${CMAKE_C_FLAGS} -std=c99 -fomit-frame-pointer -finline-functions -fno-common")
endif (CMAKE_COMPILER_IS_GNUCC)
if (CMAKE_COMPILER_IS_GNUCXX)
  set (CMAKE_CXX_FLAGS "${CMAKE_ANSI_CFLAGS} ${CMAKE_CXX_FLAGS} -fomit-frame-pointer -finline-functions -fno-common")
endif (CMAKE_COMPILER_IS_GNUCXX)

#-----------------------------------------------------------------------------
# Option to allow the user to disable compiler warnings
#-----------------------------------------------------------------------------
option (HDF_DISABLE_COMPILER_WARNINGS "Disable compiler warnings" OFF)
if (HDF_DISABLE_COMPILER_WARNINGS)
  # MSVC uses /w to suppress warnings.  It also complains if another
  # warning level is given, so remove it.
  if (MSVC)
    set (HDF_WARNINGS_BLOCKED 1)
    string (REGEX REPLACE "(^| )([/-])W[0-9]( |$)" " " CMAKE_C_FLAGS "${CMAKE_C_FLAGS}")
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} /w")
    string (REGEX REPLACE "(^| )([/-])W[0-9]( |$)" " " CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
    set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /w")
  endif (MSVC)
  if (WIN32)
    add_definitions (-D_CRT_SECURE_NO_WARNINGS)
  endif (WIN32)
  # Borland uses -w- to suppress warnings.
  if (BORLAND)
    set (HDF_WARNINGS_BLOCKED 1)
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -w-")
  endif (BORLAND)

  # Most compilers use -w to suppress warnings.
  if (NOT HDF_WARNINGS_BLOCKED)
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -w")
    set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -w")
  endif (NOT HDF_WARNINGS_BLOCKED)
endif (HDF_DISABLE_COMPILER_WARNINGS)

set (EXTERNAL_HEADER_LIST "")
set (EXTERNAL_LIBRARY_LIST "")
set (EXTERNAL_LIBRARYDLL_LIST "")

#-----------------------------------------------------------------------------
# This is in here to help some of the GCC based IDES like Eclipse
# and code blocks parse the compiler errors and warnings better.
#-----------------------------------------------------------------------------
if (CMAKE_COMPILER_IS_GNUCC)
  set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fmessage-length=0")
endif (CMAKE_COMPILER_IS_GNUCC)
if (CMAKE_COMPILER_IS_GNUCXX)
  set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fmessage-length=0")
endif (CMAKE_COMPILER_IS_GNUCXX)

include (ExternalProject)
#option (HDF_ALLOW_EXTERNAL_SUPPORT "Allow External Library Building" "NO")
set (HDF_ALLOW_EXTERNAL_SUPPORT "NO" CACHE STRING "Allow External Library Building (NO SVN TGZ)")
set_property (CACHE HDF_ALLOW_EXTERNAL_SUPPORT PROPERTY STRINGS NO SVN TGZ)
option (HDF4_USE_EXTERNAL "Use External Library Building for HDF4" 0)
option (HDF5_USE_EXTERNAL "Use External Library Building for HDF5" 0)
if (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  set (HDF4_USE_EXTERNAL 1 CACHE BOOL "Use External Library Building for HDF4" FORCE)
  set (HDF5_USE_EXTERNAL 1 CACHE BOOL "Use External Library Building for HDF5" FORCE)
  if (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN")
    set (HDF4_URL ${HDF4_SVN_URL})
    set (HDF5_URL ${HDF5_SVN_URL})
  elseif (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    if (NOT TGZPATH)
      set (TGZPATH ${HDFJAVA_SOURCE_DIR})
    endif (NOT TGZPATH)
    set (HDF4_URL ${TGZPATH}/${HDF4_TGZ_NAME})
    set (HDF5_URL ${TGZPATH}/${HDF5_TGZ_NAME})
  endif (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN")
endif (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")

set (HDF4_LINK_LIBS ${LINK_LIBS})
set (HDF5_LINK_LIBS ${LINK_LIBS})

#-----------------------------------------------------------------------------
# HDF4 support
#-----------------------------------------------------------------------------
if (NOT BUILD_SHARED_LIBS)
  set (HDF4_USE_STATIC_LIBRARIES 1)
endif (NOT BUILD_SHARED_LIBS)

if (NOT HJAVA_HDF4_HEADER)
  if (NOT HDF4_USE_EXTERNAL)
    find_package (HDF4 NAMES ${HDF4_PACKAGE_NAME}${HDF_PACKAGE_EXT})
    if (NOT HDF4_FOUND)
      find_package (HDF4) # Legacy find
    endif (NOT HDF4_FOUND)
  endif (NOT HDF4_USE_EXTERNAL)
  if (HDF4_FOUND)
    message (STATUS "HDF4 found: INC=${HDF4_INCLUDE_DIR} TOOLS=${HDF4_TOOLS_DIR}")
    if ((NOT USE_SHARED_LIBS AND HDF4_BUILD_SHARED_LIBS) OR (USE_SHARED_LIBS AND NOT HDF4_BUILD_SHARED_LIBS))
      message (STATUS "HDF4 build type does match requested type")
    endif ((NOT USE_SHARED_LIBS AND HDF4_BUILD_SHARED_LIBS) OR (USE_SHARED_LIBS AND NOT HDF4_BUILD_SHARED_LIBS))
    if (HDF4_BUILD_SHARED_LIBS)
      add_definitions (-DH4_BUILT_AS_DYNAMIC_LIB)
    else (HDF4_BUILD_SHARED_LIBS)
      add_definitions (-DH4_BUILT_AS_STATIC_LIB)
    endif (HDF4_BUILD_SHARED_LIBS)
    set (HJAVA_HAVE_HDF_H 1)
    set (HJAVA_HAVE_HDF4 1)
    set (HJAVA_HDF4_HEADER "hdf.h")
    set (HDF4_INCLUDE_DIR_GEN ${HDF4_INCLUDE_DIR})
    set (HDF4_INCLUDE_DIRS ${HDF4_INCLUDE_DIR})
  else (HDF4_FOUND)
    if (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      # May need to build JPEG with PIC on x64 machines with gcc
      # Need to use CMAKE_ANSI_CFLAGS define so that compiler test works
      if (NOT CMAKE_ANSI_CFLAGS)
        set (CMAKE_ANSI_CFLAGS " ")
      endif (NOT CMAKE_ANSI_CFLAGS)
      if (BUILD_JPEG_WITH_PIC)
        set (CMAKE_ANSI_CFLAGS "${CMAKE_ANSI_CFLAGS} -fPIC")
      endif (BUILD_JPEG_WITH_PIC)
        
      EXTERNAL_HDF4_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
      set (HJAVA_HAVE_HDF_H 1)
      set (HJAVA_HAVE_HDF4 1)
      set (HJAVA_HDF4_HEADER "hdf.h")
      message (STATUS "HDF4 is built")
      if (BUILD_SHARED_LIBS)
        add_definitions (-DH4_BUILT_AS_DYNAMIC_LIB)
      else (BUILD_SHARED_LIBS)
        add_definitions (-DH4_BUILT_AS_STATIC_LIB)
      endif (BUILD_SHARED_LIBS)
    else (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      message (FATAL_ERROR " HDF4 is Required for  HDFJAVA")
    endif (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  endif (HDF4_FOUND)
else (NOT HJAVA_HDF4_HEADER)
  # This project is being called from within another and HDF4 is already configured
  set (HJAVA_HAVE_HDF_H 1)
  set (HJAVA_HAVE_HDF4 1)
endif (NOT HJAVA_HDF4_HEADER)
if (NOT HDF4_LIBRARY)
  set (HDF4_LIBRARY ${HDF4_LIBRARIES})
endif (NOT HDF4_LIBRARY)
set (HDF4_LINK_LIBS ${HDF4_LINK_LIBS} ${HDF4_LIBRARIES})
INCLUDE_DIRECTORIES (${HDF4_INCLUDE_DIRS})
set (HDF4_FOUND 1)
set (HDF4_INCLUDE_DIR_GEN ${HDF4_INCLUDE_DIR_GEN})
set (HDF4_VERSION_STRING ${HDF4_VERSION_STRING})

#-----------------------------------------------------------------------------
# HDF5 support
#-----------------------------------------------------------------------------
if (NOT BUILD_SHARED_LIBS)
  set (HDF5_USE_STATIC_LIBRARIES 1)
endif (NOT BUILD_SHARED_LIBS)

if (NOT HJAVA_HDF5_HEADER)
  if (NOT HDF5_USE_EXTERNAL)
    find_package (HDF5 NAMES ${HDF5_PACKAGE_NAME}${HDF_PACKAGE_EXT})
    if (NOT HDF5_FOUND)
      find_package (HDF5) # Legacy find
    endif (NOT HDF5_FOUND)
  endif (NOT HDF5_USE_EXTERNAL)
  if (HDF5_FOUND)
    message (STATUS "HDF5 found: INC=${HDF5_INCLUDE_DIR} TOOLS=${HDF5_TOOLS_DIR}")
    if ((NOT USE_SHARED_LIBS AND HDF5_BUILD_SHARED_LIBS) OR (USE_SHARED_LIBS AND NOT HDF5_BUILD_SHARED_LIBS))
      message (STATUS "HDF5 build type does match requested type")
    endif ((NOT USE_SHARED_LIBS AND HDF5_BUILD_SHARED_LIBS) OR (USE_SHARED_LIBS AND NOT HDF5_BUILD_SHARED_LIBS))
    if (HDF5_BUILD_SHARED_LIBS)
      add_definitions (-DH5_BUILT_AS_DYNAMIC_LIB)
    else (HDF5_BUILD_SHARED_LIBS)
      add_definitions (-DH5_BUILT_AS_STATIC_LIB)
    endif (HDF5_BUILD_SHARED_LIBS)
    set (HJAVA_HAVE_H5PUBCONF_H 1)
    set (HJAVA_HAVE_HDF5 1)
    set (HJAVA_HDF5_HEADER "h5pubconf.h")
    set (HDF5_INCLUDE_DIR_GEN ${HDF5_INCLUDE_DIR})
    set (HDF5_INCLUDE_DIRS ${HDF5_INCLUDE_DIR})
  else (HDF5_FOUND)
    if (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      EXTERNAL_HDF5_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
      set (HJAVA_HAVE_H5PUBCONF_H 1)
      set (HJAVA_HAVE_HDF5 1)
      set (HJAVA_HDF5_HEADER "h5pubconf.h")
      message (STATUS "HDF5 is built")
      if (BUILD_SHARED_LIBS)
        add_definitions (-DH5_BUILT_AS_DYNAMIC_LIB)
      else (BUILD_SHARED_LIBS)
        add_definitions (-DH5_BUILT_AS_STATIC_LIB)
      endif (BUILD_SHARED_LIBS)
    else (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      message (FATAL_ERROR " HDF5 is Required for  HDFJAVA")
    endif (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  endif (HDF5_FOUND)
else (NOT HJAVA_HDF5_HEADER)
  # This project is being called from within another and HDF5 is already configured
  set (HJAVA_HAVE_H5PUBCONF_H 1)
  set (HJAVA_HAVE_HDF5 1)
endif (NOT HJAVA_HDF5_HEADER)
if (NOT HDF5_LIBRARY)
  set (HDF5_LIBRARY ${HDF5_LIBRARIES})
endif (NOT HDF5_LIBRARY)
set (HDF5_LINK_LIBS ${HDF5_LINK_LIBS} ${HDF5_LIBRARIES})
INCLUDE_DIRECTORIES (${HDF5_INCLUDE_DIRS})
set (HDF5_FOUND 1)
set (HDF5_INCLUDE_DIR_GEN ${HDF5_INCLUDE_DIR_GEN})
set (HDF5_VERSION_STRING ${HDF5_VERSION_STRING})

add_subdirectory (${HDFJAVA_SOURCE_DIR}/hdflib      hdflib)
add_subdirectory (${HDFJAVA_SOURCE_DIR}/hdf5lib     hdf5lib)

if (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  if (JPEG_FOUND)
    add_dependencies (${HDFJAVA_JNI_HDF_LIB_TARGET} JPEG)
    if (HDF4_FOUND AND HDF4_USE_EXTERNAL)
      add_dependencies (HDF4 JPEG)
    endif (HDF4_FOUND AND HDF4_USE_EXTERNAL)
  endif (JPEG_FOUND)
  if (ZLIB_FOUND)
    add_dependencies (${HDFJAVA_JNI_HDF_LIB_TARGET} ZLIB)
    if (HDF4_FOUND AND HDF4_USE_EXTERNAL)
      add_dependencies (HDF4 ZLIB)
    endif (HDF4_FOUND AND HDF4_USE_EXTERNAL)
    if (HDF5_FOUND AND HDF5_USE_EXTERNAL)
      add_dependencies (HDF5 ZLIB)
    endif (HDF5_FOUND AND HDF5_USE_EXTERNAL)
  endif (ZLIB_FOUND)
  if (SZIP_FOUND)
    add_dependencies (${HDFJAVA_JNI_HDF_LIB_TARGET} SZIP)
    if (HDF4_FOUND AND HDF4_USE_EXTERNAL)
      add_dependencies (HDF4 SZIP)
    endif (HDF4_FOUND AND HDF4_USE_EXTERNAL)
    if (HDF5_FOUND AND HDF5_USE_EXTERNAL)
      add_dependencies (HDF5 SZIP)
    endif (HDF5_FOUND AND HDF5_USE_EXTERNAL)
  endif (SZIP_FOUND)
  if (HDF4_FOUND AND HDF4_USE_EXTERNAL)
    add_dependencies (${HDFJAVA_JNI_HDF_LIB_TARGET} HDF4)
  endif (HDF4_FOUND AND HDF4_USE_EXTERNAL)
  if (HDF5_FOUND AND HDF5_USE_EXTERNAL)
    add_dependencies (${HDFJAVA_JNI_HDF5_LIB_TARGET} HDF5)
  endif (HDF5_FOUND AND HDF5_USE_EXTERNAL)
endif (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")

#-----------------------------------------------------------------------------
# Add the HDFJAVA Library Target to the build
#-----------------------------------------------------------------------------
add_subdirectory (${HDFJAVA_SOURCE_DIR}/ncsa      ncsa)
if (BUILD_TESTING)
  if (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
    add_subdirectory (${HDFJAVA_SOURCE_DIR}/test ${PROJECT_BINARY_DIR}/test)
  endif (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
endif (BUILD_TESTING)

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
  
  set (CPACK_STRIP_FILES "${HDFJAVA_LIBRARIES_TO_EXPORT};${EXTERNAL_LIBRARY_LIST}")
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
