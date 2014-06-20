cmake_minimum_required (VERSION 2.8.10)
PROJECT (HDFJAVA_JNI C)

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
set (LIB_TYPE ${LIB_TYPE} PARENT_SCOPE)

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
  ADD_DEFINITIONS(-DDEBUG)
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
    STRING (REGEX REPLACE "(^| )([/-])W[0-9]( |$)" " " CMAKE_C_FLAGS "${CMAKE_C_FLAGS}")
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} /w")
    STRING (REGEX REPLACE "(^| )([/-])W[0-9]( |$)" " " CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
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
    FIND_PACKAGE (HDF4 NAMES ${HDF4_PACKAGE_NAME}${HDF_PACKAGE_EXT})
    if (NOT HDF4_FOUND)
      FIND_PACKAGE (HDF4) # Legacy find
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
set (HDF4_FOUND 1 PARENT_SCOPE)
set (HDF4_INCLUDE_DIR_GEN ${HDF4_INCLUDE_DIR_GEN} PARENT_SCOPE)
set (HDF4_VERSION_STRING ${HDF4_VERSION_STRING} PARENT_SCOPE)

#-----------------------------------------------------------------------------
# HDF5 support
#-----------------------------------------------------------------------------
if (NOT BUILD_SHARED_LIBS)
  set (HDF5_USE_STATIC_LIBRARIES 1)
endif (NOT BUILD_SHARED_LIBS)

if (NOT HJAVA_HDF5_HEADER)
  if (NOT HDF5_USE_EXTERNAL)
    FIND_PACKAGE (HDF5 NAMES ${HDF5_PACKAGE_NAME}${HDF_PACKAGE_EXT})
    if (NOT HDF5_FOUND)
      FIND_PACKAGE (HDF5) # Legacy find
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
set (HDF5_FOUND 1 PARENT_SCOPE)
set (HDF5_INCLUDE_DIR_GEN ${HDF5_INCLUDE_DIR_GEN} PARENT_SCOPE)
set (HDF5_VERSION_STRING ${HDF5_VERSION_STRING} PARENT_SCOPE)

add_subdirectory (${HDFJAVA_JNI_SOURCE_DIR}/hdflib      hdflib)
add_subdirectory (${HDFJAVA_JNI_SOURCE_DIR}/hdf5lib     hdf5lib)

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
