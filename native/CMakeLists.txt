cmake_minimum_required (VERSION 2.8.3)
PROJECT (HDFJAVA_JNI C)

#-----------------------------------------------------------------------------
# Option to Build Shared/Static libs, default is static
#-----------------------------------------------------------------------------
OPTION (BUILD_SHARED_LIBS "Build Shared Libraries" OFF)
SET (LIB_TYPE STATIC)
IF (BUILD_SHARED_LIBS)
  SET (LIB_TYPE SHARED)
  SET (HDFJAVA_BUILT_AS_DYNAMIC_LIB 1)
#  ADD_DEFINITIONS (-DHDFJAVA_BUILT_AS_DYNAMIC_LIB)
  IF (MSVC)
    SET (CMAKE_MFC_FLAG 0)
    FOREACH (flag_var
        CMAKE_C_FLAGS CMAKE_C_FLAGS_DEBUG CMAKE_C_FLAGS_RELEASE
        CMAKE_C_FLAGS_MINSIZEREL CMAKE_C_FLAGS_RELWITHDEBINFO
        CMAKE_CXX_FLAGS CMAKE_CXX_FLAGS_DEBUG CMAKE_CXX_FLAGS_RELEASE
        CMAKE_CXX_FLAGS_MINSIZEREL CMAKE_CXX_FLAGS_RELWITHDEBINFO)
      IF (${flag_var} MATCHES "/MT")
        STRING (REGEX REPLACE "/MT" "/MD" ${flag_var} "${${flag_var}}")
      ENDIF (${flag_var} MATCHES "/MT")
    ENDFOREACH (flag_var)
  ENDIF (MSVC)
ELSE (BUILD_SHARED_LIBS)
  IF (NOT WIN32)
    # should this be a user setting : Everyone uses it anyway ?
    ADD_DEFINITIONS (-DPIC)
  ENDIF (NOT WIN32)
  IF (MSVC)
    SET (CMAKE_MFC_FLAG 0)
    FOREACH (flag_var
        CMAKE_C_FLAGS CMAKE_C_FLAGS_DEBUG CMAKE_C_FLAGS_RELEASE
        CMAKE_C_FLAGS_MINSIZEREL CMAKE_C_FLAGS_RELWITHDEBINFO
        CMAKE_CXX_FLAGS CMAKE_CXX_FLAGS_DEBUG CMAKE_CXX_FLAGS_RELEASE
        CMAKE_CXX_FLAGS_MINSIZEREL CMAKE_CXX_FLAGS_RELWITHDEBINFO)
      IF (${flag_var} MATCHES "/MD")
        STRING (REGEX REPLACE "/MD" "/MT" ${flag_var} "${${flag_var}}")
      ENDIF (${flag_var} MATCHES "/MD")
    ENDFOREACH (flag_var)
  ENDIF (MSVC)
ENDIF (BUILD_SHARED_LIBS)

#-----------------------------------------------------------------------------
# When building utility executables that generate other (source) files :
# we make use of the following variables defined in the root CMakeLists.
# Certain systems may add /Debug or /Release to output paths
# and we need to call the executable from inside the CMake configuration
#-----------------------------------------------------------------------------
SET (EXE_EXT "")
IF (WIN32)
  SET (EXE_EXT ".exe")
  IF (NOT CYGWIN)
    ADD_DEFINITIONS (-DBIND_TO_CURRENT_VCLIBS_VERSION=1)
    ADD_DEFINITIONS (-D_CRT_SECURE_NO_WARNINGS)
  ENDIF (NOT CYGWIN)
ENDIF (WIN32)

#-----------------------------------------------------------------------------
# Add some definitions for Debug Builds
#-----------------------------------------------------------------------------
IF (CMAKE_BUILD_TYPE MATCHES Debug)
  ADD_DEFINITIONS(-DDEBUG)
  #-- NMake Makefiles will overwhelm the console with warnings if -Wall is used.
  IF (NOT WIN32)
    ADD_DEFINITIONS (-Wall)
  ENDIF (NOT WIN32)
ELSE (CMAKE_BUILD_TYPE MATCHES Debug)
  ADD_DEFINITIONS (-UH4_DEBUG_API -DNDEBUG)
ENDIF (CMAKE_BUILD_TYPE MATCHES Debug)

#-----------------------------------------------------------------------------
# Compiler specific flags : Shouldn't there be compiler tests for these
#-----------------------------------------------------------------------------
IF (CMAKE_COMPILER_IS_GNUCC)
  SET (CMAKE_C_FLAGS "${CMAKE_ANSI_CFLAGS} ${CMAKE_C_FLAGS} -std=c99 -fomit-frame-pointer -finline-functions -fno-common")
ENDIF (CMAKE_COMPILER_IS_GNUCC)

#-----------------------------------------------------------------------------
# Option to allow the user to disable compiler warnings
#-----------------------------------------------------------------------------
OPTION (HDFJAVA_DISABLE_COMPILER_WARNINGS "Disable compiler warnings" OFF)
IF (HDFJAVA_DISABLE_COMPILER_WARNINGS)
  # MSVC uses /w to suppress warnings.  It also complains if another
  # warning level is given, so remove it.
  IF (MSVC)
    SET (HDFJAVA_WARNINGS_BLOCKED 1)
    STRING (REGEX REPLACE "(^| )([/-])W[0-9]( |$)" " "
        CMAKE_C_FLAGS "${CMAKE_C_FLAGS}")
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} /w")
  ENDIF (MSVC)
  IF (WIN32 AND NOT CYGWIN)
    ADD_DEFINITIONS (-D_CRT_SECURE_NO_WARNINGS)
  ENDIF (WIN32 AND NOT CYGWIN)
  # Borland uses -w- to suppress warnings.
  IF (BORLAND)
    SET (HDFJAVA_WARNINGS_BLOCKED 1)
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -w-")
  ENDIF (BORLAND)

  # Most compilers use -w to suppress warnings.
  IF (NOT HDFJAVA_WARNINGS_BLOCKED)
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -w")
  ENDIF (NOT HDFJAVA_WARNINGS_BLOCKED)
ENDIF (HDFJAVA_DISABLE_COMPILER_WARNINGS)

#-----------------------------------------------------------------------------
# This is in here to help some of the GCC based IDES like Eclipse
# and code blocks parse the compiler errors and warnings better.
#-----------------------------------------------------------------------------
IF (CMAKE_COMPILER_IS_GNUCC)
  SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fmessage-length=0")
ENDIF (CMAKE_COMPILER_IS_GNUCC)
IF (CMAKE_COMPILER_IS_GNUCXX)
  SET (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fmessage-length=0")
ENDIF (CMAKE_COMPILER_IS_GNUCXX)

ADD_SUBDIRECTORY(${HDFJAVA_JNI_SOURCE_DIR}/hdflib      hdflib)
ADD_SUBDIRECTORY(${HDFJAVA_JNI_SOURCE_DIR}/hdf5lib     hdf5lib)
