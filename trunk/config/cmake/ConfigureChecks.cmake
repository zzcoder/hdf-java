#-----------------------------------------------------------------------------
# Include all the necessary files for macros
#-----------------------------------------------------------------------------
INCLUDE (${CMAKE_ROOT}/Modules/CheckFunctionExists.cmake)
INCLUDE (${CMAKE_ROOT}/Modules/CheckIncludeFile.cmake)
INCLUDE (${CMAKE_ROOT}/Modules/CheckIncludeFiles.cmake)
INCLUDE (${CMAKE_ROOT}/Modules/CheckLibraryExists.cmake)
INCLUDE (${CMAKE_ROOT}/Modules/CheckSymbolExists.cmake)
INCLUDE (${CMAKE_ROOT}/Modules/CheckTypeSize.cmake)

#-----------------------------------------------------------------------------
# Always SET this for now IF we are on an OS X box
#-----------------------------------------------------------------------------
IF (APPLE)
  LIST(LENGTH CMAKE_OSX_ARCHITECTURES ARCH_LENGTH)
  IF(ARCH_LENGTH GREATER 1)
    set (CMAKE_OSX_ARCHITECTURES "" CACHE STRING "" FORCE)
    message(FATAL_ERROR "Building Universal Binaries on OS X is NOT supported by the HDF5 project. This is"
    "due to technical reasons. The best approach would be build each architecture in separate directories"
    "and use the 'lipo' tool to combine them into a single executable or library. The 'CMAKE_OSX_ARCHITECTURES'"
    "variable has been set to a blank value which will build the default architecture for this system.")
  ENDIF()
  SET (HJAVA_AC_APPLE_UNIVERSAL_BUILD 0)
ENDIF (APPLE)

#-----------------------------------------------------------------------------
# This MACRO checks IF the symbol exists in the library and IF it
# does, it appends library to the list.
#-----------------------------------------------------------------------------
SET (LINK_LIBS "")
MACRO (CHECK_LIBRARY_EXISTS_CONCAT LIBRARY SYMBOL VARIABLE)
  CHECK_LIBRARY_EXISTS ("${LIBRARY};${LINK_LIBS}" ${SYMBOL} "" ${VARIABLE})
  IF (${VARIABLE})
    SET (LINK_LIBS ${LINK_LIBS} ${LIBRARY})
  ENDIF (${VARIABLE})
ENDMACRO (CHECK_LIBRARY_EXISTS_CONCAT)

# ----------------------------------------------------------------------
# WINDOWS Hard code Values
# ----------------------------------------------------------------------

SET (WINDOWS)
IF (WIN32)
  IF (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
    SET (WINDOWS 1)
  ENDIF (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
ENDIF (WIN32)

IF (WINDOWS)
  SET (HJAVA_HAVE_LIBM 1)
  SET (HJAVA_HAVE_IO_H 1)
  SET (HJAVA_HAVE_SETJMP_H 1)
  SET (HJAVA_HAVE_STDDEF_H 1)
  SET (HJAVA_HAVE_SYS_STAT_H 1)
  SET (HJAVA_HAVE_SYS_TIMEB_H 1)
  SET (HJAVA_HAVE_SYS_TYPES_H 1)
  SET (HJAVA_HAVE_WINSOCK_H 1)
  SET (HJAVA_HAVE_STRDUP 1)
  SET (HJAVA_HAVE_SYSTEM 1)
  SET (HJAVA_HAVE_DIFFTIME 1)
  SET (HJAVA_HAVE_LONGJMP 1)
  SET (HJAVA_STDC_HEADERS 1)
  SET (HJAVA_HAVE_GETHOSTNAME 1)
  SET (HJAVA_HAVE_TIMEZONE 1)
  SET (HJAVA_HAVE_FUNCTION 1)
ENDIF (WINDOWS)

#-----------------------------------------------------------------------------
# These tests need to be manually SET for windows since there is currently
# something not quite correct with the actual test implementation. This affects
# the 'dt_arith' test and most likely lots of other code
# ----------------------------------------------------------------------------
IF (WINDOWS)
  SET (H4_FP_TO_ULLONG_RIGHT_MAXIMUM "" CACHE INTERNAL "")
ENDIF (WINDOWS)

# ----------------------------------------------------------------------
# END of WINDOWS Hard code Values
# ----------------------------------------------------------------------

IF (HJAVA_BUILD_TOOLS)
  SET (HJAVA_INCLUDE_DIR_TOOLS ${HJAVA_INCLUDE_DIR} )
ENDIF (HJAVA_BUILD_TOOLS)

IF (CYGWIN)
  SET (HJAVA_HAVE_LSEEK64 0)
ENDIF (CYGWIN)

#-----------------------------------------------------------------------------
#  Check for the math library "m"
#-----------------------------------------------------------------------------
IF (NOT WINDOWS)
  CHECK_LIBRARY_EXISTS_CONCAT ("m" random     HJAVA_HAVE_LIBM)
ENDIF (NOT WINDOWS)
CHECK_LIBRARY_EXISTS_CONCAT ("ws2_32" WSAStartup  HJAVA_HAVE_LIBWS2_32)
CHECK_LIBRARY_EXISTS_CONCAT ("wsock32" gethostbyname HJAVA_HAVE_LIBWSOCK32)
#CHECK_LIBRARY_EXISTS_CONCAT ("dl"     dlopen       HJAVA_HAVE_LIBDL)
CHECK_LIBRARY_EXISTS_CONCAT ("ucb"    gethostname  HJAVA_HAVE_LIBUCB)
CHECK_LIBRARY_EXISTS_CONCAT ("socket" connect      HJAVA_HAVE_LIBSOCKET)
CHECK_LIBRARY_EXISTS ("c" gethostbyname "" NOT_NEED_LIBNSL)


IF (NOT NOT_NEED_LIBNSL)
  CHECK_LIBRARY_EXISTS_CONCAT ("nsl"    gethostbyname  HJAVA_HAVE_LIBNSL)
ENDIF (NOT NOT_NEED_LIBNSL)


SET (USE_INCLUDES "")
IF (WINDOWS)
  SET (USE_INCLUDES ${USE_INCLUDES} "windows.h")
ENDIF (WINDOWS)
#-----------------------------------------------------------------------------
# Check IF header file exists and add it to the list.
#-----------------------------------------------------------------------------
MACRO (CHECK_INCLUDE_FILE_CONCAT FILE VARIABLE)
  CHECK_INCLUDE_FILES ("${USE_INCLUDES};${FILE}" ${VARIABLE})
  IF (${VARIABLE})
    SET (USE_INCLUDES ${USE_INCLUDES} ${FILE})
  ENDIF (${VARIABLE})
ENDMACRO (CHECK_INCLUDE_FILE_CONCAT)

#-----------------------------------------------------------------------------
#  Check for the existence of certain header files
#-----------------------------------------------------------------------------
CHECK_INCLUDE_FILE_CONCAT ("globus/common.h" HJAVA_HAVE_GLOBUS_COMMON_H)
CHECK_INCLUDE_FILE_CONCAT ("io.h"            HJAVA_HAVE_IO_H)
CHECK_INCLUDE_FILE_CONCAT ("mfhdf.h"         HJAVA_HAVE_MFHDF_H)
CHECK_INCLUDE_FILE_CONCAT ("pdb.h"           HJAVA_HAVE_PDB_H)
CHECK_INCLUDE_FILE_CONCAT ("pthread.h"       HJAVA_HAVE_PTHREAD_H)
CHECK_INCLUDE_FILE_CONCAT ("setjmp.h"        HJAVA_HAVE_SETJMP_H)
CHECK_INCLUDE_FILE_CONCAT ("srbclient.h"     HJAVA_HAVE_SRBCLIENT_H)
CHECK_INCLUDE_FILE_CONCAT ("stddef.h"        HJAVA_HAVE_STDDEF_H)
CHECK_INCLUDE_FILE_CONCAT ("stdint.h"        HJAVA_HAVE_STDINT_H)
CHECK_INCLUDE_FILE_CONCAT ("string.h"        HJAVA_HAVE_STRING_H)
CHECK_INCLUDE_FILE_CONCAT ("strings.h"       HJAVA_HAVE_STRINGS_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/ioctl.h"     HJAVA_HAVE_SYS_IOCTL_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/proc.h"      HJAVA_HAVE_SYS_PROC_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/resource.h"  HJAVA_HAVE_SYS_RESOURCE_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/socket.h"    HJAVA_HAVE_SYS_SOCKET_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/stat.h"      HJAVA_HAVE_SYS_STAT_H)
IF (CMAKE_SYSTEM_NAME MATCHES "OSF")
  CHECK_INCLUDE_FILE_CONCAT ("sys/sysinfo.h" HJAVA_HAVE_SYS_SYSINFO_H)
ELSE (CMAKE_SYSTEM_NAME MATCHES "OSF")
  SET (HJAVA_HAVE_SYS_SYSINFO_H "" CACHE INTERNAL "" FORCE)
ENDIF (CMAKE_SYSTEM_NAME MATCHES "OSF")
CHECK_INCLUDE_FILE_CONCAT ("sys/time.h"      HJAVA_HAVE_SYS_TIME_H)
CHECK_INCLUDE_FILE_CONCAT ("time.h"          HJAVA_HAVE_TIME_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/timeb.h"     HJAVA_HAVE_SYS_TIMEB_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/types.h"     HJAVA_HAVE_SYS_TYPES_H)
CHECK_INCLUDE_FILE_CONCAT ("unistd.h"        HJAVA_HAVE_UNISTD_H)
CHECK_INCLUDE_FILE_CONCAT ("stdlib.h"        HJAVA_HAVE_STDLIB_H)
CHECK_INCLUDE_FILE_CONCAT ("memory.h"        HJAVA_HAVE_MEMORY_H)
CHECK_INCLUDE_FILE_CONCAT ("dlfcn.h"         HJAVA_HAVE_DLFCN_H)
CHECK_INCLUDE_FILE_CONCAT ("features.h"      HJAVA_HAVE_FEATURES_H)
CHECK_INCLUDE_FILE_CONCAT ("inttypes.h"      HJAVA_HAVE_INTTYPES_H)
CHECK_INCLUDE_FILE_CONCAT ("netinet/in.h"    HJAVA_HAVE_NETINET_IN_H)

IF (NOT CYGWIN)
  CHECK_INCLUDE_FILE_CONCAT ("winsock2.h"      HJAVA_HAVE_WINSOCK_H)
ENDIF (NOT CYGWIN)

# IF the c compiler found stdint, check the C++ as well. On some systems this
# file will be found by C but not C++, only do this test IF the C++ compiler
# has been initialized (e.g. the project also includes some c++)
IF (HJAVA_HAVE_STDINT_H AND CMAKE_CXX_COMPILER_LOADED)
  CHECK_INCLUDE_FILE_CXX ("stdint.h" HJAVA_HAVE_STDINT_H_CXX)
  IF (NOT HJAVA_HAVE_STDINT_H_CXX)
    SET (HJAVA_HAVE_STDINT_H "" CACHE INTERNAL "Have includes HAVE_STDINT_H")
    SET (USE_INCLUDES ${USE_INCLUDES} "stdint.h")
  ENDIF (NOT HJAVA_HAVE_STDINT_H_CXX)
ENDIF (HJAVA_HAVE_STDINT_H AND CMAKE_CXX_COMPILER_LOADED)

#-----------------------------------------------------------------------------
#  Check for large file support
#-----------------------------------------------------------------------------

# The linux-lfs option is deprecated.
SET (LINUX_LFS 0)

SET (HDF_EXTRA_FLAGS)
IF (NOT WINDOWS)
  # Linux Specific flags
  SET (HDF_EXTRA_FLAGS -D_POSIX_SOURCE -D_BSD_SOURCE)
  OPTION (HDF_ENABLE_LARGE_FILE "Enable support for large (64-bit) files on Linux." ON)
  IF (HDF_ENABLE_LARGE_FILE)
    SET (msg "Performing TEST_LFS_WORKS")
    TRY_RUN (TEST_LFS_WORKS_RUN   TEST_LFS_WORKS_COMPILE
        ${CMAKE_BINARY_DIR}
        ${HDFJAVA_RESOURCES_DIR}/HDFTests.c
        CMAKE_FLAGS -DCOMPILE_DEFINITIONS:STRING=-DTEST_LFS_WORKS
        OUTPUT_VARIABLE OUTPUT
    )
    IF (TEST_LFS_WORKS_COMPILE)
      IF (TEST_LFS_WORKS_RUN  MATCHES 0)
        SET (TEST_LFS_WORKS 1 CACHE INTERNAL ${msg})
    SET (LARGEFILE 1)
        SET (HDF_EXTRA_FLAGS ${HDF_EXTRA_FLAGS} -D_FILE_OFFSET_BITS=64 -D_LARGEFILE64_SOURCE -D_LARGEFILE_SOURCE)
        MESSAGE (STATUS "${msg}... yes")
      ELSE (TEST_LFS_WORKS_RUN  MATCHES 0)
        SET (TEST_LFS_WORKS "" CACHE INTERNAL ${msg})
        MESSAGE (STATUS "${msg}... no")
        FILE (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
              "Test TEST_LFS_WORKS Run failed with the following output and exit code:\n ${OUTPUT}\n"
        )
      ENDIF (TEST_LFS_WORKS_RUN  MATCHES 0)
    ELSE (TEST_LFS_WORKS_COMPILE )
      SET (TEST_LFS_WORKS "" CACHE INTERNAL ${msg})
      MESSAGE (STATUS "${msg}... no")
      FILE (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Test TEST_LFS_WORKS Compile failed with the following output:\n ${OUTPUT}\n"
      )
    ENDIF (TEST_LFS_WORKS_COMPILE)
  ENDIF (HDF_ENABLE_LARGE_FILE)
  SET (CMAKE_REQUIRED_DEFINITIONS ${CMAKE_REQUIRED_DEFINITIONS} ${HDF_EXTRA_FLAGS})
ENDIF (NOT WINDOWS)

ADD_DEFINITIONS (${HDF_EXTRA_FLAGS})


#-----------------------------------------------------------------------------
#  Check the size in bytes of all the int and float types
#-----------------------------------------------------------------------------
MACRO (HJAVA_CHECK_TYPE_SIZE type var)
  SET (aType ${type})
  SET (aVar  ${var})
#  MESSAGE (STATUS "Checking size of ${aType} and storing into ${aVar}")
  CHECK_TYPE_SIZE (${aType}   ${aVar})
  IF (NOT ${aVar})
    SET (${aVar} 0 CACHE INTERNAL "SizeOf for ${aType}")
#    MESSAGE (STATUS "Size of ${aType} was NOT Found")
  ENDIF (NOT ${aVar})
ENDMACRO (HJAVA_CHECK_TYPE_SIZE)



HJAVA_CHECK_TYPE_SIZE (char           HJAVA_SIZEOF_CHAR)
HJAVA_CHECK_TYPE_SIZE (short          HJAVA_SIZEOF_SHORT)
HJAVA_CHECK_TYPE_SIZE (int            HJAVA_SIZEOF_INT)
HJAVA_CHECK_TYPE_SIZE (unsigned       HJAVA_SIZEOF_UNSIGNED)
IF (NOT APPLE)
  HJAVA_CHECK_TYPE_SIZE (long       HJAVA_SIZEOF_LONG)
ENDIF (NOT APPLE)
HJAVA_CHECK_TYPE_SIZE ("long long"    HJAVA_SIZEOF_LONG_LONG)
HJAVA_CHECK_TYPE_SIZE (__int64        HJAVA_SIZEOF___INT64)
IF (NOT HJAVA_SIZEOF___INT64)
  SET (HJAVA_SIZEOF___INT64 0)
ENDIF (NOT HJAVA_SIZEOF___INT64)

HJAVA_CHECK_TYPE_SIZE (float          HJAVA_SIZEOF_FLOAT)
HJAVA_CHECK_TYPE_SIZE (double         HJAVA_SIZEOF_DOUBLE)
HJAVA_CHECK_TYPE_SIZE ("long double"  HJAVA_SIZEOF_LONG_DOUBLE)
HJAVA_CHECK_TYPE_SIZE (int8_t         HJAVA_SIZEOF_INT8_T)
HJAVA_CHECK_TYPE_SIZE (uint8_t        HJAVA_SIZEOF_UINT8_T)
HJAVA_CHECK_TYPE_SIZE (int_least8_t   HJAVA_SIZEOF_INT_LEAST8_T)
HJAVA_CHECK_TYPE_SIZE (uint_least8_t  HJAVA_SIZEOF_UINT_LEAST8_T)
HJAVA_CHECK_TYPE_SIZE (int_fast8_t    HJAVA_SIZEOF_INT_FAST8_T)
HJAVA_CHECK_TYPE_SIZE (uint_fast8_t   HJAVA_SIZEOF_UINT_FAST8_T)
HJAVA_CHECK_TYPE_SIZE (int16_t        HJAVA_SIZEOF_INT16_T)
HJAVA_CHECK_TYPE_SIZE (uint16_t       HJAVA_SIZEOF_UINT16_T)
HJAVA_CHECK_TYPE_SIZE (int_least16_t  HJAVA_SIZEOF_INT_LEAST16_T)
HJAVA_CHECK_TYPE_SIZE (uint_least16_t HJAVA_SIZEOF_UINT_LEAST16_T)
HJAVA_CHECK_TYPE_SIZE (int_fast16_t   HJAVA_SIZEOF_INT_FAST16_T)
HJAVA_CHECK_TYPE_SIZE (uint_fast16_t  HJAVA_SIZEOF_UINT_FAST16_T)
HJAVA_CHECK_TYPE_SIZE (int32_t        HJAVA_SIZEOF_INT32_T)
HJAVA_CHECK_TYPE_SIZE (uint32_t       HJAVA_SIZEOF_UINT32_T)
HJAVA_CHECK_TYPE_SIZE (int_least32_t  HJAVA_SIZEOF_INT_LEAST32_T)
HJAVA_CHECK_TYPE_SIZE (uint_least32_t HJAVA_SIZEOF_UINT_LEAST32_T)
HJAVA_CHECK_TYPE_SIZE (int_fast32_t   HJAVA_SIZEOF_INT_FAST32_T)
HJAVA_CHECK_TYPE_SIZE (uint_fast32_t  HJAVA_SIZEOF_UINT_FAST32_T)
HJAVA_CHECK_TYPE_SIZE (int64_t        HJAVA_SIZEOF_INT64_T)
HJAVA_CHECK_TYPE_SIZE (uint64_t       HJAVA_SIZEOF_UINT64_T)
HJAVA_CHECK_TYPE_SIZE (int_least64_t  HJAVA_SIZEOF_INT_LEAST64_T)
HJAVA_CHECK_TYPE_SIZE (uint_least64_t HJAVA_SIZEOF_UINT_LEAST64_T)
HJAVA_CHECK_TYPE_SIZE (int_fast64_t   HJAVA_SIZEOF_INT_FAST64_T)
HJAVA_CHECK_TYPE_SIZE (uint_fast64_t  HJAVA_SIZEOF_UINT_FAST64_T)
IF (NOT APPLE)
  HJAVA_CHECK_TYPE_SIZE (size_t       HJAVA_SIZEOF_SIZE_T)
  HJAVA_CHECK_TYPE_SIZE (ssize_t      HJAVA_SIZEOF_SSIZE_T)
  IF (NOT HJAVA_SIZEOF_SSIZE_T)
    SET (HJAVA_SIZEOF_SSIZE_T 0)
  ENDIF (NOT HJAVA_SIZEOF_SSIZE_T)
ENDIF (NOT APPLE)
HJAVA_CHECK_TYPE_SIZE (off_t          HJAVA_SIZEOF_OFF_T)
HJAVA_CHECK_TYPE_SIZE (off64_t        HJAVA_SIZEOF_OFF64_T)
IF (NOT HJAVA_SIZEOF_OFF64_T)
  SET (HJAVA_SIZEOF_OFF64_T 0)
ENDIF (NOT HJAVA_SIZEOF_OFF64_T)


# For other tests to use the same libraries
SET (CMAKE_REQUIRED_LIBRARIES ${LINK_LIBS})

#-----------------------------------------------------------------------------
# Check for some functions that are used
#
CHECK_FUNCTION_EXISTS (alarm             HJAVA_HAVE_ALARM)
CHECK_FUNCTION_EXISTS (fork              HJAVA_HAVE_FORK)
CHECK_FUNCTION_EXISTS (frexpf            HJAVA_HAVE_FREXPF)
CHECK_FUNCTION_EXISTS (frexpl            HJAVA_HAVE_FREXPL)

CHECK_FUNCTION_EXISTS (gethostname       HJAVA_HAVE_GETHOSTNAME)
CHECK_FUNCTION_EXISTS (getpwuid          HJAVA_HAVE_GETPWUID)
CHECK_FUNCTION_EXISTS (getrusage         HJAVA_HAVE_GETRUSAGE)
CHECK_FUNCTION_EXISTS (lstat             HJAVA_HAVE_LSTAT)

CHECK_FUNCTION_EXISTS (rand_r            HJAVA_HAVE_RAND_R)
CHECK_FUNCTION_EXISTS (random            HJAVA_HAVE_RANDOM)
CHECK_FUNCTION_EXISTS (setsysinfo        HJAVA_HAVE_SETSYSINFO)

CHECK_FUNCTION_EXISTS (signal            HJAVA_HAVE_SIGNAL)
CHECK_FUNCTION_EXISTS (longjmp           HJAVA_HAVE_LONGJMP)
CHECK_FUNCTION_EXISTS (setjmp            HJAVA_HAVE_SETJMP)
CHECK_FUNCTION_EXISTS (siglongjmp        HJAVA_HAVE_SIGLONGJMP)
CHECK_FUNCTION_EXISTS (sigsetjmp         HJAVA_HAVE_SIGSETJMP)
CHECK_FUNCTION_EXISTS (sigaction         HJAVA_HAVE_SIGACTION)
CHECK_FUNCTION_EXISTS (sigprocmask       HJAVA_HAVE_SIGPROCMASK)

CHECK_FUNCTION_EXISTS (snprintf          HJAVA_HAVE_SNPRINTF)
CHECK_FUNCTION_EXISTS (srandom           HJAVA_HAVE_SRANDOM)
CHECK_FUNCTION_EXISTS (strdup            HJAVA_HAVE_STRDUP)
CHECK_FUNCTION_EXISTS (symlink           HJAVA_HAVE_SYMLINK)
CHECK_FUNCTION_EXISTS (system            HJAVA_HAVE_SYSTEM)

CHECK_FUNCTION_EXISTS (tmpfile           HJAVA_HAVE_TMPFILE)
CHECK_FUNCTION_EXISTS (vasprintf         HJAVA_HAVE_VASPRINTF)
CHECK_FUNCTION_EXISTS (waitpid           HJAVA_HAVE_WAITPID)

CHECK_FUNCTION_EXISTS (vsnprintf         HJAVA_HAVE_VSNPRINTF)
CHECK_FUNCTION_EXISTS (ioctl             HJAVA_HAVE_IOCTL)
#CHECK_FUNCTION_EXISTS (gettimeofday      HJAVA_HAVE_GETTIMEOFDAY)
CHECK_FUNCTION_EXISTS (difftime          HJAVA_HAVE_DIFFTIME)
CHECK_FUNCTION_EXISTS (fseeko            HJAVA_HAVE_FSEEKO)
CHECK_FUNCTION_EXISTS (ftello            HJAVA_HAVE_FTELLO)
CHECK_FUNCTION_EXISTS (fseeko64          HJAVA_HAVE_FSEEKO64)
CHECK_FUNCTION_EXISTS (ftello64          HJAVA_HAVE_FTELLO64)
CHECK_FUNCTION_EXISTS (fstat64           HJAVA_HAVE_FSTAT64)
CHECK_FUNCTION_EXISTS (stat64            HJAVA_HAVE_STAT64)

# MPI checks
SET (CMAKE_REQUIRED_INCLUDES "${MPI_INCLUDE_PATH}/mpi.h" )
SET (CMAKE_REQUIRED_LIBRARIES "${MPI_LIBRARY}" )
CHECK_FUNCTION_EXISTS (MPI_File_get_size HJAVA_HAVE_MPI_GET_SIZE)

#-----------------------------------------------------------------------------

#-----------------------------------------------------------------------------
#  Since gettimeofday is not defined any where standard, lets look in all the
#  usual places. On MSVC we are just going to use ::clock()
#-----------------------------------------------------------------------------
IF (NOT MSVC)
  IF ("HJAVA_HAVE_TIME_GETTIMEOFDAY" MATCHES "^HJAVA_HAVE_TIME_GETTIMEOFDAY$")
    TRY_COMPILE (HAVE_TIME_GETTIMEOFDAY
        ${CMAKE_BINARY_DIR}
        ${HDFJAVA_RESOURCES_DIR}/GetTimeOfDayTest.c
        COMPILE_DEFINITIONS -DTRY_TIME_H
        OUTPUT_VARIABLE OUTPUT
    )
    IF (HAVE_TIME_GETTIMEOFDAY STREQUAL "TRUE")
      SET (HJAVA_HAVE_TIME_GETTIMEOFDAY "1" CACHE INTERNAL "HJAVA_HAVE_TIME_GETTIMEOFDAY")
      SET (HJAVA_HAVE_GETTIMEOFDAY "1" CACHE INTERNAL "HJAVA_HAVE_GETTIMEOFDAY")
    ENDIF (HAVE_TIME_GETTIMEOFDAY STREQUAL "TRUE")
  ENDIF ("HJAVA_HAVE_TIME_GETTIMEOFDAY" MATCHES "^HJAVA_HAVE_TIME_GETTIMEOFDAY$")

  IF ("HJAVA_HAVE_SYS_TIME_GETTIMEOFDAY" MATCHES "^HJAVA_HAVE_SYS_TIME_GETTIMEOFDAY$")
    TRY_COMPILE (HAVE_SYS_TIME_GETTIMEOFDAY
        ${CMAKE_BINARY_DIR}
        ${HDFJAVA_RESOURCES_DIR}/GetTimeOfDayTest.c
        COMPILE_DEFINITIONS -DTRY_SYS_TIME_H
        OUTPUT_VARIABLE OUTPUT
    )
    IF (HAVE_SYS_TIME_GETTIMEOFDAY STREQUAL "TRUE")
      SET (HJAVA_HAVE_SYS_TIME_GETTIMEOFDAY "1" CACHE INTERNAL "HJAVA_HAVE_SYS_TIME_GETTIMEOFDAY")
      SET (HJAVA_HAVE_GETTIMEOFDAY "1" CACHE INTERNAL "HJAVA_HAVE_GETTIMEOFDAY")
    ENDIF (HAVE_SYS_TIME_GETTIMEOFDAY STREQUAL "TRUE")
  ENDIF ("HJAVA_HAVE_SYS_TIME_GETTIMEOFDAY" MATCHES "^HJAVA_HAVE_SYS_TIME_GETTIMEOFDAY$")
ENDIF (NOT MSVC)

IF (NOT HAVE_SYS_TIME_GETTIMEOFDAY AND NOT HJAVA_HAVE_GETTIMEOFDAY AND NOT MSVC)
  MESSAGE (STATUS "---------------------------------------------------------------")
  MESSAGE (STATUS "Function 'gettimeofday()' was not found. HJAVA will use its")
  MESSAGE (STATUS "  own implementation.. This can happen on older versions of")
  MESSAGE (STATUS "  MinGW on Windows. Consider upgrading your MinGW installation")
  MESSAGE (STATUS "  to a newer version such as MinGW 3.12")
  MESSAGE (STATUS "---------------------------------------------------------------")
ENDIF (NOT HAVE_SYS_TIME_GETTIMEOFDAY AND NOT HJAVA_HAVE_GETTIMEOFDAY AND NOT MSVC)


# Check for Symbols
CHECK_SYMBOL_EXISTS (tzname "time.h" HJAVA_HAVE_DECL_TZNAME)

#-----------------------------------------------------------------------------
#
#-----------------------------------------------------------------------------
IF (NOT WINDOWS)
  CHECK_SYMBOL_EXISTS (TIOCGWINSZ "sys/ioctl.h" HJAVA_HAVE_TIOCGWINSZ)
  CHECK_SYMBOL_EXISTS (TIOCGETD   "sys/ioctl.h" HJAVA_HAVE_TIOCGETD)
ENDIF (NOT WINDOWS)


# For other other specific tests, use this MACRO.
MACRO (HJAVA_FUNCTION_TEST OTHER_TEST)
  IF ("HJAVA_${OTHER_TEST}" MATCHES "^HJAVA_${OTHER_TEST}$")
    SET (MACRO_CHECK_FUNCTION_DEFINITIONS "-D${OTHER_TEST} ${CMAKE_REQUIRED_FLAGS}")
    SET (OTHER_TEST_ADD_LIBRARIES)
    IF (CMAKE_REQUIRED_LIBRARIES)
      SET (OTHER_TEST_ADD_LIBRARIES "-DLINK_LIBRARIES:STRING=${CMAKE_REQUIRED_LIBRARIES}")
    ENDIF (CMAKE_REQUIRED_LIBRARIES)
    
    FOREACH (def ${HJAVA_EXTRA_TEST_DEFINITIONS})
      SET (MACRO_CHECK_FUNCTION_DEFINITIONS "${MACRO_CHECK_FUNCTION_DEFINITIONS} -D${def}=${${def}}")
    ENDFOREACH (def)

    FOREACH (def
        HAVE_SYS_TIME_H
        HAVE_UNISTD_H
        HAVE_SYS_TYPES_H
        HAVE_SYS_SOCKET_H
    )
      IF ("${HJAVA_${def}}")
        SET (MACRO_CHECK_FUNCTION_DEFINITIONS "${MACRO_CHECK_FUNCTION_DEFINITIONS} -D${def}")
      ENDIF ("${HJAVA_${def}}")
    ENDFOREACH (def)
    
    IF (LARGEFILE)
      SET (MACRO_CHECK_FUNCTION_DEFINITIONS
          "${MACRO_CHECK_FUNCTION_DEFINITIONS} -D_FILE_OFFSET_BITS=64 -D_LARGEFILE64_SOURCE -D_LARGEFILE_SOURCE"
      )
    ENDIF (LARGEFILE)

    #MESSAGE (STATUS "Performing ${OTHER_TEST}")
    TRY_COMPILE (${OTHER_TEST}
        ${CMAKE_BINARY_DIR}
        ${HDFJAVA_RESOURCES_DIR}/HDFTests.c
        CMAKE_FLAGS -DCOMPILE_DEFINITIONS:STRING=${MACRO_CHECK_FUNCTION_DEFINITIONS}
        "${OTHER_TEST_ADD_LIBRARIES}"
        OUTPUT_VARIABLE OUTPUT
    )
    IF (${OTHER_TEST})
      SET (HJAVA_${OTHER_TEST} 1 CACHE INTERNAL "Other test ${FUNCTION}")
      MESSAGE (STATUS "Performing Other Test ${OTHER_TEST} - Success")
    ELSE (${OTHER_TEST})
      MESSAGE (STATUS "Performing Other Test ${OTHER_TEST} - Failed")
      SET (HJAVA_${OTHER_TEST} "" CACHE INTERNAL "Other test ${FUNCTION}")
      FILE (APPEND
          ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Performing Other Test ${OTHER_TEST} failed with the following output:\n"
          "${OUTPUT}\n"
      )
    ENDIF (${OTHER_TEST})
  ENDIF ("HJAVA_${OTHER_TEST}" MATCHES "^HJAVA_${OTHER_TEST}$")
ENDMACRO (HJAVA_FUNCTION_TEST)

#-----------------------------------------------------------------------------
# Check a bunch of other functions
#-----------------------------------------------------------------------------
IF (NOT WINDOWS)
  FOREACH (test
      TIME_WITH_SYS_TIME
      STDC_HEADERS
      HAVE_TM_ZONE
      HAVE_STRUCT_TM_TM_ZONE
      HAVE_ATTRIBUTE
      HAVE_FUNCTION
      HAVE_TM_GMTOFF
#      HAVE_TIMEZONE
      HAVE_STRUCT_TIMEZONE
      HAVE_STAT_ST_BLOCKS
      HAVE_FUNCTION
      SYSTEM_SCOPE_THREADS
      HAVE_SOCKLEN_T
      DEV_T_IS_SCALAR
      HAVE_OFF64_T
      GETTIMEOFDAY_GIVES_TZ
      VSNPRINTF_WORKS
      HAVE_C99_FUNC
      HAVE_C99_DESIGNATED_INITIALIZER
      CXX_HAVE_OFFSETOF
  )
    HJAVA_FUNCTION_TEST (${test})
  ENDFOREACH (test)
  IF (NOT CYGWIN AND NOT MINGW)
    HJAVA_FUNCTION_TEST (HAVE_TIMEZONE)
#      HJAVA_FUNCTION_TEST (HAVE_STAT_ST_BLOCKS)
  ENDIF (NOT CYGWIN AND NOT MINGW)
ENDIF (NOT WINDOWS)

#-----------------------------------------------------------------------------
# Look for 64 bit file stream capability
#-----------------------------------------------------------------------------
IF (HAVE_OFF64_T)
  CHECK_FUNCTION_EXISTS (lseek64           HJAVA_HAVE_LSEEK64)
  CHECK_FUNCTION_EXISTS (fseek64           HJAVA_HAVE_FSEEK64)
ENDIF (HAVE_OFF64_T)

#-----------------------------------------------------------------------------
# Determine how 'inline' is used
#-----------------------------------------------------------------------------
SET (HJAVA_EXTRA_TEST_DEFINITIONS INLINE_TEST_INLINE)
FOREACH (inline_test inline __inline__ __inline)
  SET (INLINE_TEST_INLINE ${inline_test})
  HJAVA_FUNCTION_TEST (INLINE_TEST_${inline_test})
ENDFOREACH (inline_test)

SET (HJAVA_EXTRA_TEST_DEFINITIONS)
IF (INLINE_TEST___inline__)
  SET (HJAVA_inline __inline__)
ELSE (INLINE_TEST___inline__)
  IF (INLINE_TEST___inline)
    SET (HJAVA_inline __inline)
  ELSE (INLINE_TEST___inline)
    IF (INLINE_TEST_inline)
      SET (HJAVA_inline inline)
    ENDIF (INLINE_TEST_inline)
  ENDIF (INLINE_TEST___inline)
ENDIF (INLINE_TEST___inline__)

#-----------------------------------------------------------------------------
# Check how to print a Long Long integer
#-----------------------------------------------------------------------------
IF (NOT HJAVA_PRINTF_LL_WIDTH OR HJAVA_PRINTF_LL_WIDTH MATCHES "unknown")
  SET (PRINT_LL_FOUND 0)
  MESSAGE (STATUS "Checking for appropriate format for 64 bit long:")
  FOREACH (HJAVA_PRINTF_LL l64 l L q I64 ll)
    SET (CURRENT_TEST_DEFINITIONS "-DPRINTF_LL_WIDTH=${HJAVA_PRINTF_LL}")
    IF (HJAVA_SIZEOF_LONG_LONG)
      SET (CURRENT_TEST_DEFINITIONS "${CURRENT_TEST_DEFINITIONS} -DHAVE_LONG_LONG")
    ENDIF (HJAVA_SIZEOF_LONG_LONG)
    TRY_RUN (HJAVA_PRINTF_LL_TEST_RUN   HJAVA_PRINTF_LL_TEST_COMPILE
        ${CMAKE_BINARY_DIR}
        ${HDFJAVA_RESOURCES_DIR}/HDFTests.c
        CMAKE_FLAGS -DCOMPILE_DEFINITIONS:STRING=${CURRENT_TEST_DEFINITIONS}
        OUTPUT_VARIABLE OUTPUT
    )
    IF (HJAVA_PRINTF_LL_TEST_COMPILE)
      IF (HJAVA_PRINTF_LL_TEST_RUN MATCHES 0)
        SET (HJAVA_PRINTF_LL_WIDTH "\"${HJAVA_PRINTF_LL}\"" CACHE INTERNAL "Width for printf for type `long long' or `__int64', us. `ll")
        SET (PRINT_LL_FOUND 1)
      ELSE (HJAVA_PRINTF_LL_TEST_RUN MATCHES 0)
        MESSAGE ("Width with ${HJAVA_PRINTF_LL} failed with result: ${HJAVA_PRINTF_LL_TEST_RUN}")
      ENDIF (HJAVA_PRINTF_LL_TEST_RUN MATCHES 0)
    ELSE (HJAVA_PRINTF_LL_TEST_COMPILE)
      FILE (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Test HJAVA_PRINTF_LL_WIDTH for ${HJAVA_PRINTF_LL} failed with the following output:\n ${OUTPUT}\n"
      )
    ENDIF (HJAVA_PRINTF_LL_TEST_COMPILE)
  ENDFOREACH (HJAVA_PRINTF_LL)
  
  IF (PRINT_LL_FOUND)
    MESSAGE (STATUS "Checking for apropriate format for 64 bit long: found ${HJAVA_PRINTF_LL_WIDTH}")
  ELSE (PRINT_LL_FOUND)
    MESSAGE (STATUS "Checking for apropriate format for 64 bit long: not found")
    SET (HJAVA_PRINTF_LL_WIDTH "\"unknown\"" CACHE INTERNAL
        "Width for printf for type `long long' or `__int64', us. `ll"
    )
  ENDIF (PRINT_LL_FOUND)
ENDIF (NOT HJAVA_PRINTF_LL_WIDTH OR HJAVA_PRINTF_LL_WIDTH MATCHES "unknown")

#-----------------------------------------------------------------------------
# Set the flag to indicate that the machine can handle converting
# denormalized floating-point values.
# (This flag should be set for all machines, except for the Crays, where
# the cache value is set in it's config file)
#-----------------------------------------------------------------------------
SET (HJAVA_CONVERT_DENORMAL_FLOAT 1)

#-----------------------------------------------------------------------------
#  Are we going to use HSIZE_T
#-----------------------------------------------------------------------------
IF (HJAVA_ENABLE_HSIZET)
  SET (HJAVA_HAVE_LARGE_HSIZET 1)
ENDIF (HJAVA_ENABLE_HSIZET)

#-----------------------------------------------------------------------------
# Macro to determine the various conversion capabilities
#-----------------------------------------------------------------------------
MACRO (HDFConversionTests TEST msg)
  IF ("${TEST}" MATCHES "^${TEST}$")
    #MESSAGE (STATUS "===> ${TEST}")
    TRY_RUN (${TEST}_RUN   ${TEST}_COMPILE
        ${CMAKE_BINARY_DIR}
        ${HDFJAVA_RESOURCES_DIR}/ConversionTests.c
        CMAKE_FLAGS -DCOMPILE_DEFINITIONS:STRING=-D${TEST}_TEST
        OUTPUT_VARIABLE OUTPUT
    )
    IF (${TEST}_COMPILE)
      IF (${TEST}_RUN  MATCHES 0)
        SET (${TEST} 1 CACHE INTERNAL ${msg})
        MESSAGE (STATUS "${msg}... yes")
      ELSE (${TEST}_RUN  MATCHES 0)
        SET (${TEST} "" CACHE INTERNAL ${msg})
        MESSAGE (STATUS "${msg}... no")
        FILE (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
            "Test ${TEST} Run failed with the following output and exit code:\n ${OUTPUT}\n"
        )
      ENDIF (${TEST}_RUN  MATCHES 0)
    ELSE (${TEST}_COMPILE )
      SET (${TEST} "" CACHE INTERNAL ${msg})
      MESSAGE (STATUS "${msg}... no")
      FILE (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Test ${TEST} Compile failed with the following output:\n ${OUTPUT}\n"
      )
    ENDIF (${TEST}_COMPILE)

  ENDIF ("${TEST}" MATCHES "^${TEST}$")
ENDMACRO (HDFConversionTests)

#-----------------------------------------------------------------------------
# Macro to make some of the conversion tests easier to write/read
#-----------------------------------------------------------------------------
MACRO (HDFMiscConversionTest VAR TEST msg)
  IF ("${TEST}" MATCHES "^${TEST}$")
    IF (${VAR})
      SET (${TEST} 1 CACHE INTERNAL ${msg})
      MESSAGE (STATUS "${msg}... yes")
    ELSE (${VAR})
      SET (${TEST} "" CACHE INTERNAL ${msg})
      MESSAGE (STATUS "${msg}... no")
    ENDIF (${VAR})
  ENDIF ("${TEST}" MATCHES "^${TEST}$")
ENDMACRO (HDFMiscConversionTest)

#-----------------------------------------------------------------------------
# Check various conversion capabilities
#-----------------------------------------------------------------------------

# -----------------------------------------------------------------------
# Set flag to indicate that the machine can handle conversion from
# long double to integers accurately.  This flag should be set "yes" for
# all machines except all SGIs.  For SGIs, some conversions are
# incorrect and its cache value is set "no" in its config/irix6.x and
# irix5.x.
#
HDFMiscConversionTest (HJAVA_SIZEOF_LONG_DOUBLE HJAVA_LDOUBLE_TO_INTEGER_ACCURATE "checking IF converting from long double to integers is accurate")
# -----------------------------------------------------------------------
# Set flag to indicate that the machine can do conversion from
# long double to integers regardless of accuracy.  This flag should be
# set "yes" for all machines except HP-UX 11.00.  For HP-UX 11.00, the
# compiler has 'floating exception' when converting 'long double' to all
# integers except 'unsigned long long'.  Other HP-UX systems are unknown
# yet. (1/8/05 - SLU)
#
HDFConversionTests (HJAVA_LDOUBLE_TO_INTEGER_WORKS "Checking IF converting from long double to integers works")
# -----------------------------------------------------------------------
# Set flag to indicate that the machine can handle conversion from
# integers to long double.  (This flag should be set "yes" for all
# machines except all SGIs, where some conversions are
# incorrect and its cache value is set "no" in its config/irix6.x and
# irix5.x)
#
HDFMiscConversionTest (HJAVA_SIZEOF_LONG_DOUBLE HJAVA_INTEGER_TO_LDOUBLE_ACCURATE "checking IF accurately converting from integers to long double")
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can accurately convert
# 'unsigned long' to 'float' values.
# (This flag should be set for all machines, except for Pathscale compiler
# on Sandia's Linux machine where the compiler interprets 'unsigned long'
# values as negative when the first bit of 'unsigned long' is on during
# the conversion to float.)
HDFConversionTests (HJAVA_ULONG_TO_FLOAT_ACCURATE "Checking IF accurately converting unsigned long to float values")
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can accurately convert
# 'unsigned (long) long' values to 'float' and 'double' values.
# (This flag should be set for all machines, except for the SGIs, where
# the cache value is set in the config/irix6.x config file) and Solaris
# 64-bit machines, where the short program below tests if round-up is
# correctly handled.
#
HDFConversionTests (HJAVA_ULONG_TO_FP_BOTTOM_BIT_ACCURATE "Checking IF accurately converting unsigned long long to floating-point values")
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can accurately convert
# 'float' or 'double' to 'unsigned long long' values.
# (This flag should be set for all machines, except for PGI compiler
# where round-up happens when the fraction of float-point value is greater
# than 0.5.
#
HDFConversionTests (HJAVA_FP_TO_ULLONG_ACCURATE "Checking IF accurately roundup converting floating-point to unsigned long long values" )
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can accurately convert
# 'float', 'double' or 'long double' to 'unsigned long long' values.
# (This flag should be set for all machines, except for HP-UX machines
# where the maximal number for unsigned long long is 0x7fffffffffffffff
# during conversion.
#
HDFConversionTests (HJAVA_FP_TO_ULLONG_RIGHT_MAXIMUM "Checking IF right maximum converting floating-point to unsigned long long values" )
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can accurately convert
# 'long double' to 'unsigned int' values.  (This flag should be set for
# all machines, except for some Intel compilers on some Linux.)
#
HDFConversionTests (HJAVA_LDOUBLE_TO_UINT_ACCURATE "Checking IF correctly converting long double to unsigned int values")
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can _compile_
# 'unsigned long long' to 'float' and 'double' typecasts.
# (This flag should be set for all machines.)
#
IF (HJAVA_ULLONG_TO_FP_CAST_WORKS MATCHES ^HJAVA_ULLONG_TO_FP_CAST_WORKS$)
  SET (HJAVA_ULLONG_TO_FP_CAST_WORKS 1 CACHE INTERNAL "Checking IF compiling unsigned long long to floating-point typecasts work")
  MESSAGE (STATUS "Checking IF compiling unsigned long long to floating-point typecasts work... yes")
ENDIF (HJAVA_ULLONG_TO_FP_CAST_WORKS MATCHES ^HJAVA_ULLONG_TO_FP_CAST_WORKS$)
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can _compile_
# 'long long' to 'float' and 'double' typecasts.
# (This flag should be set for all machines.)
#
IF (HJAVA_LLONG_TO_FP_CAST_WORKS MATCHES ^HJAVA_LLONG_TO_FP_CAST_WORKS$)
  SET (HJAVA_LLONG_TO_FP_CAST_WORKS 1 CACHE INTERNAL "Checking IF compiling long long to floating-point typecasts work")
  MESSAGE (STATUS "Checking IF compiling long long to floating-point typecasts work... yes")
ENDIF (HJAVA_LLONG_TO_FP_CAST_WORKS MATCHES ^HJAVA_LLONG_TO_FP_CAST_WORKS$)
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can convert from
# 'unsigned long long' to 'long double' without precision loss.
# (This flag should be set for all machines, except for FreeBSD(sleipnir)
# where the last 2 bytes of mantissa are lost when compiler tries to do
# the conversion, and Cygwin where compiler doesn't do rounding correctly.)
#
HDFConversionTests (HJAVA_ULLONG_TO_LDOUBLE_PRECISION "Checking IF converting unsigned long long to long double with precision")
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can handle overflow converting
# all floating-point to all integer types.
# (This flag should be set for all machines, except for Cray X1 where
# floating exception is generated when the floating-point value is greater
# than the maximal integer value).
#
HDFConversionTests (HJAVA_FP_TO_INTEGER_OVERFLOW_WORKS  "Checking IF overflows normally converting floating-point to integer values")
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can accurately convert
# 'long double' to '(unsigned) long long' values.  (This flag should be set for
# all machines, except for Mac OS 10.4 and SGI IRIX64 6.5.  When the bit sequence
# of long double is 0x4351ccf385ebc8a0bfcc2a3c..., the values of (unsigned)long long
# start to go wrong on these two machines.  Adjusting it higher to
# 0x4351ccf385ebc8a0dfcc... or 0x4351ccf385ebc8a0ffcc... will make the converted
# values wildly wrong.  This test detects this wrong behavior and disable the test.
#
HDFConversionTests (HJAVA_LDOUBLE_TO_LLONG_ACCURATE "Checking IF correctly converting long double to (unsigned) long long values")
# ----------------------------------------------------------------------
# Set the flag to indicate that the machine can accurately convert
# '(unsigned) long long' to 'long double' values.  (This flag should be set for
# all machines, except for Mac OS 10.4, when the bit sequences are 003fff...,
# 007fff..., 00ffff..., 01ffff..., ..., 7fffff..., the converted values are twice
# as big as they should be.
#
HDFConversionTests (HJAVA_LLONG_TO_LDOUBLE_CORRECT "Checking IF correctly converting (unsigned) long long to long double values")
HDFConversionTests (HJAVA_NO_ALIGNMENT_RESTRICTIONS "Checking IF alignment restrictions are strictly enforced")

# Define a macro for Cygwin (on XP only) where the compiler has rounding
#   problem converting from unsigned long long to long double */
IF (CYGWIN)
  SET (HJAVA_CYGWIN_ULLONG_TO_LDOUBLE_ROUND_PROBLEM 1)
ENDIF (CYGWIN)
