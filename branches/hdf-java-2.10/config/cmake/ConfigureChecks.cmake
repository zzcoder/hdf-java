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
  IF (MINGW)
    SET (HJAVA_HAVE_MINGW 1)
    SET (WINDOWS 1) # MinGW tries to imitate Windows
    SET (CMAKE_REQUIRED_FLAGS "-DWIN32_LEAN_AND_MEAN=1 -DNOGDI=1")
  ENDIF (MINGW)
  SET (HJAVA_HAVE_WIN32_API 1)
  SET (CMAKE_REQUIRED_LIBRARIES "ws2_32.lib;wsock32.lib")
  IF (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
    SET (WINDOWS 1)
    SET (CMAKE_REQUIRED_FLAGS "/DWIN32_LEAN_AND_MEAN=1 /DNOGDI=1")
    IF (MSVC)
      SET (HJAVA_HAVE_VISUAL_STUDIO 1)
    ENDIF (MSVC)
  ENDIF (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
ENDIF (WIN32)

IF (WINDOWS)
  SET (HJAVA_HAVE_IO_H 1)
  SET (HJAVA_HAVE_SETJMP_H 1)
  SET (HJAVA_HAVE_STDDEF_H 1)
  SET (HJAVA_HAVE_SYS_STAT_H 1)
  SET (HJAVA_HAVE_SYS_TIMEB_H 1)
  SET (HJAVA_HAVE_SYS_TYPES_H 1)
  SET (HJAVA_HAVE_WINSOCK_H 1)
  SET (HJAVA_HAVE_LIBM 1)
  SET (HJAVA_HAVE_STRDUP 1)
  SET (HJAVA_HAVE_SYSTEM 1)
  SET (HJAVA_HAVE_DIFFTIME 1)
  SET (HJAVA_HAVE_LONGJMP 1)
  SET (HJAVA_STDC_HEADERS 1)
  IF (NOT MINGW)
    SET (HJAVA_HAVE_GETHOSTNAME 1)
  ENDIF (NOT MINGW)
  IF (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
    SET (HJAVA_HAVE_GETCONSOLESCREENBUFFERINFO 1)
  ENDIF (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
  SET (HJAVA_HAVE_FUNCTION 1)
  SET (HJAVA_GETTIMEOFDAY_GIVES_TZ 1)
  SET (HJAVA_HAVE_TIMEZONE 1)
  SET (HJAVA_HAVE_GETTIMEOFDAY 1)
  IF (MINGW)
    SET (HJAVA_HAVE_WINSOCK2_H 1)
  ENDIF (MINGW)
  SET (HJAVA_HAVE_LIBWS2_32 1)
  SET (HJAVA_HAVE_LIBWSOCK32 1)
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
  CHECK_LIBRARY_EXISTS_CONCAT ("m" ceil     HJAVA_HAVE_LIBM)
  CHECK_LIBRARY_EXISTS_CONCAT ("ws2_32" WSAStartup  HJAVA_HAVE_LIBWS2_32)
  CHECK_LIBRARY_EXISTS_CONCAT ("wsock32" gethostbyname HJAVA_HAVE_LIBWSOCK32)
ENDIF (NOT WINDOWS)
#CHECK_LIBRARY_EXISTS_CONCAT ("dl"     dlopen       HJAVA_HAVE_LIBDL)
CHECK_LIBRARY_EXISTS_CONCAT ("ucb"    gethostname  HJAVA_HAVE_LIBUCB)
CHECK_LIBRARY_EXISTS_CONCAT ("socket" connect      HJAVA_HAVE_LIBSOCKET)
CHECK_LIBRARY_EXISTS ("c" gethostbyname "" NOT_NEED_LIBNSL)


IF (NOT NOT_NEED_LIBNSL)
  CHECK_LIBRARY_EXISTS_CONCAT ("nsl"    gethostbyname  HJAVA_HAVE_LIBNSL)
ENDIF (NOT NOT_NEED_LIBNSL)

# For other tests to use the same libraries
SET (CMAKE_REQUIRED_LIBRARIES ${CMAKE_REQUIRED_LIBRARIES} ${LINK_LIBS})

SET (USE_INCLUDES "")
IF (WINDOWS)
  SET (USE_INCLUDES ${USE_INCLUDES} "windows.h")
ENDIF (WINDOWS)

# For other specific tests, use this MACRO.
MACRO (HJAVA_FUNCTION_TEST OTHER_TEST)
  IF ("HJAVA_${OTHER_TEST}" MATCHES "^HJAVA_${OTHER_TEST}$")
    SET (MACRO_CHECK_FUNCTION_DEFINITIONS "-D${OTHER_TEST} ${CMAKE_REQUIRED_FLAGS}")
    SET (OTHER_TEST_ADD_LIBRARIES)
    IF (CMAKE_REQUIRED_LIBRARIES)
      SET (OTHER_TEST_ADD_LIBRARIES "-DLINK_LIBRARIES:STRING=${CMAKE_REQUIRED_LIBRARIES}")
    ENDIF (CMAKE_REQUIRED_LIBRARIES)

    FOREACH (def ${HDF_EXTRA_TEST_DEFINITIONS})
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
        ${HDFJAVA_BINARY_DIR}/CMake
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
      FILE (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Performing Other Test ${OTHER_TEST} failed with the following output:\n"
          "${OUTPUT}\n"
      )
    ENDIF (${OTHER_TEST})
  ENDIF ("HJAVA_${OTHER_TEST}" MATCHES "^HJAVA_${OTHER_TEST}$")
ENDMACRO (HJAVA_FUNCTION_TEST)


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
CHECK_INCLUDE_FILE_CONCAT ("sys/resource.h"  HJAVA_HAVE_SYS_RESOURCE_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/time.h"      HJAVA_HAVE_SYS_TIME_H)
CHECK_INCLUDE_FILE_CONCAT ("unistd.h"        HJAVA_HAVE_UNISTD_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/ioctl.h"     HJAVA_HAVE_SYS_IOCTL_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/stat.h"      HJAVA_HAVE_SYS_STAT_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/socket.h"    HJAVA_HAVE_SYS_SOCKET_H)
CHECK_INCLUDE_FILE_CONCAT ("sys/types.h"     HJAVA_HAVE_SYS_TYPES_H)
CHECK_INCLUDE_FILE_CONCAT ("stddef.h"        HJAVA_HAVE_STDDEF_H)
CHECK_INCLUDE_FILE_CONCAT ("setjmp.h"        HJAVA_HAVE_SETJMP_H)
CHECK_INCLUDE_FILE_CONCAT ("features.h"      HJAVA_HAVE_FEATURES_H)
CHECK_INCLUDE_FILE_CONCAT ("stdint.h"        HJAVA_HAVE_STDINT_H)

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

# Darwin
CHECK_INCLUDE_FILE_CONCAT ("mach/mach_time.h" HJAVA_HAVE_MACH_MACH_TIME_H)

# Windows
CHECK_INCLUDE_FILE_CONCAT ("io.h"            HJAVA_HAVE_IO_H)
IF (NOT CYGWIN)
  CHECK_INCLUDE_FILE_CONCAT ("winsock2.h"      HJAVA_HAVE_WINSOCK_H)
ENDIF (NOT CYGWIN)
CHECK_INCLUDE_FILE_CONCAT ("sys/timeb.h"     HJAVA_HAVE_SYS_TIMEB_H)

IF (CMAKE_SYSTEM_NAME MATCHES "OSF")
  CHECK_INCLUDE_FILE_CONCAT ("sys/sysinfo.h" HJAVA_HAVE_SYS_SYSINFO_H)
  CHECK_INCLUDE_FILE_CONCAT ("sys/proc.h"    HJAVA_HAVE_SYS_PROC_H)
ELSE (CMAKE_SYSTEM_NAME MATCHES "OSF")
  SET (H5_HAVE_SYS_SYSINFO_H "" CACHE INTERNAL "" FORCE)
  SET (H5_HAVE_SYS_PROC_H    "" CACHE INTERNAL "" FORCE)
ENDIF (CMAKE_SYSTEM_NAME MATCHES "OSF")

CHECK_INCLUDE_FILE_CONCAT ("globus/common.h" HJAVA_HAVE_GLOBUS_COMMON_H)
CHECK_INCLUDE_FILE_CONCAT ("pdb.h"           HJAVA_HAVE_PDB_H)
CHECK_INCLUDE_FILE_CONCAT ("pthread.h"       HJAVA_HAVE_PTHREAD_H)
CHECK_INCLUDE_FILE_CONCAT ("srbclient.h"     HJAVA_HAVE_SRBCLIENT_H)
CHECK_INCLUDE_FILE_CONCAT ("string.h"        HJAVA_HAVE_STRING_H)
CHECK_INCLUDE_FILE_CONCAT ("strings.h"       HJAVA_HAVE_STRINGS_H)
CHECK_INCLUDE_FILE_CONCAT ("time.h"          HJAVA_HAVE_TIME_H)
CHECK_INCLUDE_FILE_CONCAT ("stdlib.h"        HJAVA_HAVE_STDLIB_H)
CHECK_INCLUDE_FILE_CONCAT ("memory.h"        HJAVA_HAVE_MEMORY_H)
CHECK_INCLUDE_FILE_CONCAT ("dlfcn.h"         HJAVA_HAVE_DLFCN_H)
CHECK_INCLUDE_FILE_CONCAT ("inttypes.h"      HJAVA_HAVE_INTTYPES_H)
CHECK_INCLUDE_FILE_CONCAT ("netinet/in.h"    HJAVA_HAVE_NETINET_IN_H)

#-----------------------------------------------------------------------------
#  Check for large file support
#-----------------------------------------------------------------------------

# The linux-lfs option is deprecated.
SET (LINUX_LFS 0)

SET (HDF_EXTRA_FLAGS)
IF (NOT WINDOWS)
  # Linux Specific flags
  IF (CYGWIN)
    SET (HDF_EXTRA_FLAGS -D_BSD_SOURCE)
  ELSE (CYGWIN)
    SET (HDF_EXTRA_FLAGS -D_POSIX_SOURCE -D_BSD_SOURCE)
  ENDIF (CYGWIN)
  OPTION (HDF_ENABLE_LARGE_FILE "Enable support for large (64-bit) files on Linux." ON)
  IF (HDF_ENABLE_LARGE_FILE)
    SET (msg "Performing TEST_LFS_WORKS")
    TRY_RUN (TEST_LFS_WORKS_RUN   TEST_LFS_WORKS_COMPILE
        ${HDFJAVA_BINARY_DIR}/CMake
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
# Check for HAVE_OFF64_T functionality
#-----------------------------------------------------------------------------
IF (NOT WINDOWS OR MINGW)
  HJAVA_FUNCTION_TEST (HAVE_OFF64_T)
  IF (H5_HAVE_OFF64_T)
    CHECK_FUNCTION_EXISTS (lseek64            HJAVA_HAVE_LSEEK64)
    CHECK_FUNCTION_EXISTS (fseeko64           HJAVA_HAVE_FSEEKO64)
    CHECK_FUNCTION_EXISTS (ftello64           HJAVA_HAVE_FTELLO64)
    CHECK_FUNCTION_EXISTS (ftruncate64        HJAVA_HAVE_FTRUNCATE64)
  ENDIF (H5_HAVE_OFF64_T)

  CHECK_FUNCTION_EXISTS (fseeko               HJAVA_HAVE_FSEEKO)
  CHECK_FUNCTION_EXISTS (ftello               HJAVA_HAVE_FTELLO)

  HJAVA_FUNCTION_TEST (HAVE_STAT64_STRUCT)
  IF (HAVE_STAT64_STRUCT)
    CHECK_FUNCTION_EXISTS (fstat64            HJAVA_HAVE_FSTAT64)
    CHECK_FUNCTION_EXISTS (stat64             HJAVA_HAVE_STAT64)
  ENDIF (HAVE_STAT64_STRUCT)
ENDIF (NOT WINDOWS OR MINGW)

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

# ----------------------------------------------------------------------
# How do we figure out the width of a tty in characters?
#
CHECK_FUNCTION_EXISTS (_getvideoconfig   HJAVA_HAVE__GETVIDEOCONFIG)
CHECK_FUNCTION_EXISTS (gettextinfo       HJAVA_HAVE_GETTEXTINFO)
CHECK_FUNCTION_EXISTS (_scrsize          HJAVA_HAVE__SCRSIZE)
CHECK_FUNCTION_EXISTS (ioctl             HJAVA_HAVE_IOCTL)
HJAVA_FUNCTION_TEST (HAVE_STRUCT_VIDEOCONFIG)
HJAVA_FUNCTION_TEST (HAVE_STRUCT_TEXT_INFO)
IF (NOT WINDOWS)
  IF (NOT CYGWIN AND NOT MINGW)
    CHECK_FUNCTION_EXISTS (GetConsoleScreenBufferInfo    HJAVA_HAVE_GETCONSOLESCREENBUFFERINFO)
  ENDIF (NOT CYGWIN AND NOT MINGW)
  CHECK_SYMBOL_EXISTS (TIOCGWINSZ "sys/ioctl.h" HJAVA_HAVE_TIOCGWINSZ)
  CHECK_SYMBOL_EXISTS (TIOCGETD   "sys/ioctl.h" HJAVA_HAVE_TIOCGETD)
ENDIF (NOT WINDOWS)

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

# MPI checks
SET (CMAKE_REQUIRED_INCLUDES "${MPI_INCLUDE_PATH}/mpi.h" )
SET (CMAKE_REQUIRED_LIBRARIES "${MPI_LIBRARY}" )
CHECK_FUNCTION_EXISTS (MPI_File_get_size HJAVA_HAVE_MPI_GET_SIZE)

#-----------------------------------------------------------------------------



#-----------------------------------------------------------------------------
# Check a bunch of other functions
#-----------------------------------------------------------------------------
IF (NOT WINDOWS)
  FOREACH (test
      LONE_COLON
      HAVE_ATTRIBUTE
      HAVE_C99_FUNC
      HAVE_FUNCTION
      HAVE_C99_DESIGNATED_INITIALIZER
      SYSTEM_SCOPE_THREADS
      HAVE_SOCKLEN_T
      CXX_HAVE_OFFSETOF
  )
    HJAVA_FUNCTION_TEST (${test})
  ENDFOREACH (test)
ENDIF (NOT WINDOWS)

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
        ${HDFJAVA_BINARY_DIR}/CMake
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
