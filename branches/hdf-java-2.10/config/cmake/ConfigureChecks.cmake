#-----------------------------------------------------------------------------
# Include all the necessary files for macros
#-----------------------------------------------------------------------------
include (${CMAKE_ROOT}/Modules/CheckFunctionExists.cmake)
include (${CMAKE_ROOT}/Modules/CheckIncludeFile.cmake)
include (${CMAKE_ROOT}/Modules/CheckIncludeFiles.cmake)
include (${CMAKE_ROOT}/Modules/CheckLibraryExists.cmake)
include (${CMAKE_ROOT}/Modules/CheckSymbolExists.cmake)
include (${CMAKE_ROOT}/Modules/CheckTypeSize.cmake)

#-----------------------------------------------------------------------------
# Always SET this for now IF we are on an OS X box
#-----------------------------------------------------------------------------
if (APPLE)
  list (LENGTH CMAKE_OSX_ARCHITECTURES ARCH_LENGTH)
  if (ARCH_LENGTH GREATER 1)
    set (CMAKE_OSX_ARCHITECTURES "" CACHE STRING "" FORCE)
    message(FATAL_ERROR "Building Universal Binaries on OS X is NOT supported by the HDF5 project. This is"
    "due to technical reasons. The best approach would be build each architecture in separate directories"
    "and use the 'lipo' tool to combine them into a single executable or library. The 'CMAKE_OSX_ARCHITECTURES'"
    "variable has been set to a blank value which will build the default architecture for this system.")
  endif ()
  set (HJAVA_AC_APPLE_UNIVERSAL_BUILD 0)
endif (APPLE)

#-----------------------------------------------------------------------------
# This MACRO checks IF the symbol exists in the library and IF it
# does, it appends library to the list.
#-----------------------------------------------------------------------------
set (LINK_LIBS "")
MACRO (CHECK_LIBRARY_EXISTS_CONCAT LIBRARY SYMBOL VARIABLE)
  CHECK_LIBRARY_EXISTS ("${LIBRARY};${LINK_LIBS}" ${SYMBOL} "" ${VARIABLE})
  if (${VARIABLE})
    set (LINK_LIBS ${LINK_LIBS} ${LIBRARY})
  endif (${VARIABLE})
ENDMACRO (CHECK_LIBRARY_EXISTS_CONCAT)

# ----------------------------------------------------------------------
# WINDOWS Hard code Values
# ----------------------------------------------------------------------

set (WINDOWS)
if (WIN32)
  if (MINGW)
    set (HJAVA_HAVE_MINGW 1)
    set (WINDOWS 1) # MinGW tries to imitate Windows
    set (CMAKE_REQUIRED_FLAGS "-DWIN32_LEAN_AND_MEAN=1 -DNOGDI=1")
  endif (MINGW)
  set (HJAVA_HAVE_WIN32_API 1)
  set (CMAKE_REQUIRED_LIBRARIES "ws2_32.lib;wsock32.lib")
  if (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
    set (WINDOWS 1)
    set (CMAKE_REQUIRED_FLAGS "/DWIN32_LEAN_AND_MEAN=1 /DNOGDI=1")
    if (MSVC)
      set (HJAVA_HAVE_VISUAL_STUDIO 1)
    endif (MSVC)
  endif (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
endif (WIN32)

if (WINDOWS)
  set (HJAVA_HAVE_IO_H 1)
  set (HJAVA_HAVE_SETJMP_H 1)
  set (HJAVA_HAVE_STDDEF_H 1)
  set (HJAVA_HAVE_SYS_STAT_H 1)
  set (HJAVA_HAVE_SYS_TIMEB_H 1)
  set (HJAVA_HAVE_SYS_TYPES_H 1)
  set (HJAVA_HAVE_WINSOCK_H 1)
  set (HJAVA_HAVE_LIBM 1)
  set (HJAVA_HAVE_STRDUP 1)
  set (HJAVA_HAVE_SYSTEM 1)
  set (HJAVA_HAVE_DIFFTIME 1)
  set (HJAVA_HAVE_LONGJMP 1)
  set (HJAVA_STDC_HEADERS 1)
  if (NOT MINGW)
    set (HJAVA_HAVE_GETHOSTNAME 1)
  endif (NOT MINGW)
  if (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
    set (HJAVA_HAVE_GETCONSOLESCREENBUFFERINFO 1)
  endif (NOT UNIX AND NOT CYGWIN AND NOT MINGW)
  set (HJAVA_HAVE_FUNCTION 1)
  set (HJAVA_GETTIMEOFDAY_GIVES_TZ 1)
  set (HJAVA_HAVE_TIMEZONE 1)
  set (HJAVA_HAVE_GETTIMEOFDAY 1)
  if (MINGW)
    set (HJAVA_HAVE_WINSOCK2_H 1)
  endif (MINGW)
  set (HJAVA_HAVE_LIBWS2_32 1)
  set (HJAVA_HAVE_LIBWSOCK32 1)
endif (WINDOWS)

# ----------------------------------------------------------------------
# END of WINDOWS Hard code Values
# ----------------------------------------------------------------------

if (HJAVA_BUILD_TOOLS)
  set (HJAVA_INCLUDE_DIR_TOOLS ${HJAVA_INCLUDE_DIR} )
endif (HJAVA_BUILD_TOOLS)

if (CYGWIN)
  set (HJAVA_HAVE_LSEEK64 0)
endif (CYGWIN)

#-----------------------------------------------------------------------------
#  Check for the math library "m"
#-----------------------------------------------------------------------------
if (NOT WINDOWS)
  CHECK_LIBRARY_EXISTS_CONCAT ("m" ceil     HJAVA_HAVE_LIBM)
  #CHECK_LIBRARY_EXISTS_CONCAT ("dl"     dlopen       HJAVA_HAVE_LIBDL)
  CHECK_LIBRARY_EXISTS_CONCAT ("ws2_32" WSAStartup  HJAVA_HAVE_LIBWS2_32)
  CHECK_LIBRARY_EXISTS_CONCAT ("wsock32" gethostbyname HJAVA_HAVE_LIBWSOCK32)
endif (NOT WINDOWS)
CHECK_LIBRARY_EXISTS_CONCAT ("ucb"    gethostname  HJAVA_HAVE_LIBUCB)
CHECK_LIBRARY_EXISTS_CONCAT ("socket" connect      HJAVA_HAVE_LIBSOCKET)
CHECK_LIBRARY_EXISTS ("c" gethostbyname "" NOT_NEED_LIBNSL)


if (NOT NOT_NEED_LIBNSL)
  CHECK_LIBRARY_EXISTS_CONCAT ("nsl"    gethostbyname  HJAVA_HAVE_LIBNSL)
endif (NOT NOT_NEED_LIBNSL)

# For other tests to use the same libraries
set (CMAKE_REQUIRED_LIBRARIES ${CMAKE_REQUIRED_LIBRARIES} ${LINK_LIBS})

set (USE_INCLUDES "")
if (WINDOWS)
  set (USE_INCLUDES ${USE_INCLUDES} "windows.h")
endif (WINDOWS)

# For other specific tests, use this MACRO.
MACRO (HJAVA_FUNCTION_TEST OTHER_TEST)
  if ("HJAVA_${OTHER_TEST}" MATCHES "^HJAVA_${OTHER_TEST}$")
    set (MACRO_CHECK_FUNCTION_DEFINITIONS "-D${OTHER_TEST} ${CMAKE_REQUIRED_FLAGS}")
    set (OTHER_TEST_ADD_LIBRARIES)
    if (CMAKE_REQUIRED_LIBRARIES)
      set (OTHER_TEST_ADD_LIBRARIES "-DLINK_LIBRARIES:STRING=${CMAKE_REQUIRED_LIBRARIES}")
    endif (CMAKE_REQUIRED_LIBRARIES)

    foreach (def ${HDF_EXTRA_TEST_DEFINITIONS})
      set (MACRO_CHECK_FUNCTION_DEFINITIONS "${MACRO_CHECK_FUNCTION_DEFINITIONS} -D${def}=${${def}}")
    endforeach (def)

    foreach (def
        HAVE_SYS_TIME_H
        HAVE_UNISTD_H
        HAVE_SYS_TYPES_H
        HAVE_SYS_SOCKET_H
    )
      if ("${HJAVA_${def}}")
        set (MACRO_CHECK_FUNCTION_DEFINITIONS "${MACRO_CHECK_FUNCTION_DEFINITIONS} -D${def}")
      endif ("${HJAVA_${def}}")
    endforeach (def)

    if (LARGEFILE)
      set (MACRO_CHECK_FUNCTION_DEFINITIONS
          "${MACRO_CHECK_FUNCTION_DEFINITIONS} -D_FILE_OFFSET_BITS=64 -D_LARGEFILE64_SOURCE -D_LARGEFILE_SOURCE"
      )
    endif (LARGEFILE)

    #message (STATUS "Performing ${OTHER_TEST}")
    TRY_COMPILE (${OTHER_TEST}
        ${HDFJAVA_BINARY_DIR}/CMake
        ${HDFJAVA_RESOURCES_DIR}/HDFTests.c
        CMAKE_FLAGS -DCOMPILE_DEFINITIONS:STRING=${MACRO_CHECK_FUNCTION_DEFINITIONS}
        "${OTHER_TEST_ADD_LIBRARIES}"
        OUTPUT_VARIABLE OUTPUT
    )
    if (${OTHER_TEST})
      set (HJAVA_${OTHER_TEST} 1 CACHE INTERNAL "Other test ${FUNCTION}")
      message (STATUS "Performing Other Test ${OTHER_TEST} - Success")
    else (${OTHER_TEST})
      message (STATUS "Performing Other Test ${OTHER_TEST} - Failed")
      set (HJAVA_${OTHER_TEST} "" CACHE INTERNAL "Other test ${FUNCTION}")
      file (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Performing Other Test ${OTHER_TEST} failed with the following output:\n"
          "${OUTPUT}\n"
      )
    endif (${OTHER_TEST})
  endif ("HJAVA_${OTHER_TEST}" MATCHES "^HJAVA_${OTHER_TEST}$")
ENDMACRO (HJAVA_FUNCTION_TEST)


#-----------------------------------------------------------------------------
# Check IF header file exists and add it to the list.
#-----------------------------------------------------------------------------
MACRO (CHECK_INCLUDE_FILE_CONCAT FILE VARIABLE)
  CHECK_INCLUDE_FILES ("${USE_INCLUDES};${FILE}" ${VARIABLE})
  if (${VARIABLE})
    set (USE_INCLUDES ${USE_INCLUDES} ${FILE})
  endif (${VARIABLE})
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
if (HJAVA_HAVE_STDINT_H AND CMAKE_CXX_COMPILER_LOADED)
  CHECK_INCLUDE_FILE_CXX ("stdint.h" HJAVA_HAVE_STDINT_H_CXX)
  if (NOT HJAVA_HAVE_STDINT_H_CXX)
    set (HJAVA_HAVE_STDINT_H "" CACHE INTERNAL "Have includes HAVE_STDINT_H")
    set (USE_INCLUDES ${USE_INCLUDES} "stdint.h")
  endif (NOT HJAVA_HAVE_STDINT_H_CXX)
endif (HJAVA_HAVE_STDINT_H AND CMAKE_CXX_COMPILER_LOADED)

# Darwin
CHECK_INCLUDE_FILE_CONCAT ("mach/mach_time.h" HJAVA_HAVE_MACH_MACH_TIME_H)

# Windows
CHECK_INCLUDE_FILE_CONCAT ("io.h"            HJAVA_HAVE_IO_H)
if (NOT CYGWIN)
  CHECK_INCLUDE_FILE_CONCAT ("winsock2.h"      HJAVA_HAVE_WINSOCK_H)
endif (NOT CYGWIN)
CHECK_INCLUDE_FILE_CONCAT ("sys/timeb.h"     HJAVA_HAVE_SYS_TIMEB_H)

if (CMAKE_SYSTEM_NAME MATCHES "OSF")
  CHECK_INCLUDE_FILE_CONCAT ("sys/sysinfo.h" HJAVA_HAVE_SYS_SYSINFO_H)
  CHECK_INCLUDE_FILE_CONCAT ("sys/proc.h"    HJAVA_HAVE_SYS_PROC_H)
else (CMAKE_SYSTEM_NAME MATCHES "OSF")
  set (H5_HAVE_SYS_SYSINFO_H "" CACHE INTERNAL "" FORCE)
  set (H5_HAVE_SYS_PROC_H    "" CACHE INTERNAL "" FORCE)
endif (CMAKE_SYSTEM_NAME MATCHES "OSF")

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
set (LINUX_LFS 0)

set (HDF_EXTRA_FLAGS)
if (NOT WINDOWS)
  # Linux Specific flags
  if (CYGWIN)
    set (HDF_EXTRA_FLAGS -D_BSD_SOURCE)
  else (CYGWIN)
    set (HDF_EXTRA_FLAGS -D_POSIX_SOURCE -D_BSD_SOURCE)
  endif (CYGWIN)
  option (HDF_ENABLE_LARGE_FILE "Enable support for large (64-bit) files on Linux." ON)
  if (HDF_ENABLE_LARGE_FILE)
    set (msg "Performing TEST_LFS_WORKS")
    try_run (TEST_LFS_WORKS_RUN   TEST_LFS_WORKS_COMPILE
        ${HDFJAVA_BINARY_DIR}/CMake
        ${HDFJAVA_RESOURCES_DIR}/HDFTests.c
        CMAKE_FLAGS -DCOMPILE_DEFINITIONS:STRING=-DTEST_LFS_WORKS
        OUTPUT_VARIABLE OUTPUT
    )
    if (TEST_LFS_WORKS_COMPILE)
      if (TEST_LFS_WORKS_RUN  MATCHES 0)
        set (TEST_LFS_WORKS 1 CACHE INTERNAL ${msg})
        set (LARGEFILE 1)
        set (HDF_EXTRA_FLAGS ${HDF_EXTRA_FLAGS} -D_FILE_OFFSET_BITS=64 -D_LARGEFILE64_SOURCE -D_LARGEFILE_SOURCE)
        message (STATUS "${msg}... yes")
      else (TEST_LFS_WORKS_RUN  MATCHES 0)
        set (TEST_LFS_WORKS "" CACHE INTERNAL ${msg})
        message (STATUS "${msg}... no")
        file (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
              "Test TEST_LFS_WORKS Run failed with the following output and exit code:\n ${OUTPUT}\n"
        )
      endif (TEST_LFS_WORKS_RUN  MATCHES 0)
    else (TEST_LFS_WORKS_COMPILE )
      set (TEST_LFS_WORKS "" CACHE INTERNAL ${msg})
      message (STATUS "${msg}... no")
      file (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Test TEST_LFS_WORKS Compile failed with the following output:\n ${OUTPUT}\n"
      )
    endif (TEST_LFS_WORKS_COMPILE)
  endif (HDF_ENABLE_LARGE_FILE)
  set (CMAKE_REQUIRED_DEFINITIONS ${CMAKE_REQUIRED_DEFINITIONS} ${HDF_EXTRA_FLAGS})
endif (NOT WINDOWS)

ADD_DEFINITIONS (${HDF_EXTRA_FLAGS})

#-----------------------------------------------------------------------------
# Check for HAVE_OFF64_T functionality
#-----------------------------------------------------------------------------
if (NOT WINDOWS OR MINGW)
  HJAVA_FUNCTION_TEST (HAVE_OFF64_T)
  if (H5_HAVE_OFF64_T)
    CHECK_FUNCTION_EXISTS (lseek64            HJAVA_HAVE_LSEEK64)
    CHECK_FUNCTION_EXISTS (fseeko64           HJAVA_HAVE_FSEEKO64)
    CHECK_FUNCTION_EXISTS (ftello64           HJAVA_HAVE_FTELLO64)
    CHECK_FUNCTION_EXISTS (ftruncate64        HJAVA_HAVE_FTRUNCATE64)
  endif (H5_HAVE_OFF64_T)

  CHECK_FUNCTION_EXISTS (fseeko               HJAVA_HAVE_FSEEKO)
  CHECK_FUNCTION_EXISTS (ftello               HJAVA_HAVE_FTELLO)

  HJAVA_FUNCTION_TEST (HAVE_STAT64_STRUCT)
  if (HAVE_STAT64_STRUCT)
    CHECK_FUNCTION_EXISTS (fstat64            HJAVA_HAVE_FSTAT64)
    CHECK_FUNCTION_EXISTS (stat64             HJAVA_HAVE_STAT64)
  endif (HAVE_STAT64_STRUCT)
endif (NOT WINDOWS OR MINGW)

#-----------------------------------------------------------------------------
#  Check the size in bytes of all the int and float types
#-----------------------------------------------------------------------------
MACRO (HJAVA_CHECK_TYPE_SIZE type var)
  set (aType ${type})
  set (aVar  ${var})
#  message (STATUS "Checking size of ${aType} and storing into ${aVar}")
  CHECK_TYPE_SIZE (${aType}   ${aVar})
  if (NOT ${aVar})
    set (${aVar} 0 CACHE INTERNAL "SizeOf for ${aType}")
#    message (STATUS "Size of ${aType} was NOT Found")
  endif (NOT ${aVar})
ENDMACRO (HJAVA_CHECK_TYPE_SIZE)



HJAVA_CHECK_TYPE_SIZE (char           HJAVA_SIZEOF_CHAR)
HJAVA_CHECK_TYPE_SIZE (short          HJAVA_SIZEOF_SHORT)
HJAVA_CHECK_TYPE_SIZE (int            HJAVA_SIZEOF_INT)
HJAVA_CHECK_TYPE_SIZE (unsigned       HJAVA_SIZEOF_UNSIGNED)
if (NOT APPLE)
  HJAVA_CHECK_TYPE_SIZE (long       HJAVA_SIZEOF_LONG)
endif (NOT APPLE)
HJAVA_CHECK_TYPE_SIZE ("long long"    HJAVA_SIZEOF_LONG_LONG)
HJAVA_CHECK_TYPE_SIZE (__int64        HJAVA_SIZEOF___INT64)
if (NOT HJAVA_SIZEOF___INT64)
  set (HJAVA_SIZEOF___INT64 0)
endif (NOT HJAVA_SIZEOF___INT64)

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
if (NOT APPLE)
  HJAVA_CHECK_TYPE_SIZE (size_t       HJAVA_SIZEOF_SIZE_T)
  HJAVA_CHECK_TYPE_SIZE (ssize_t      HJAVA_SIZEOF_SSIZE_T)
  if (NOT HJAVA_SIZEOF_SSIZE_T)
    set (HJAVA_SIZEOF_SSIZE_T 0)
  endif (NOT HJAVA_SIZEOF_SSIZE_T)
endif (NOT APPLE)

HJAVA_CHECK_TYPE_SIZE (off_t          HJAVA_SIZEOF_OFF_T)
HJAVA_CHECK_TYPE_SIZE (off64_t        HJAVA_SIZEOF_OFF64_T)
if (NOT HJAVA_SIZEOF_OFF64_T)
  set (HJAVA_SIZEOF_OFF64_T 0)
endif (NOT HJAVA_SIZEOF_OFF64_T)

# ----------------------------------------------------------------------
# How do we figure out the width of a tty in characters?
#
CHECK_FUNCTION_EXISTS (_getvideoconfig   HJAVA_HAVE__GETVIDEOCONFIG)
CHECK_FUNCTION_EXISTS (gettextinfo       HJAVA_HAVE_GETTEXTINFO)
CHECK_FUNCTION_EXISTS (_scrsize          HJAVA_HAVE__SCRSIZE)
CHECK_FUNCTION_EXISTS (ioctl             HJAVA_HAVE_IOCTL)
HJAVA_FUNCTION_TEST (HAVE_STRUCT_VIDEOCONFIG)
HJAVA_FUNCTION_TEST (HAVE_STRUCT_TEXT_INFO)
if (NOT WINDOWS)
  if (NOT CYGWIN AND NOT MINGW)
    CHECK_FUNCTION_EXISTS (GetConsoleScreenBufferInfo    HJAVA_HAVE_GETCONSOLESCREENBUFFERINFO)
  endif (NOT CYGWIN AND NOT MINGW)
  CHECK_SYMBOL_EXISTS (TIOCGWINSZ "sys/ioctl.h" HJAVA_HAVE_TIOCGWINSZ)
  CHECK_SYMBOL_EXISTS (TIOCGETD   "sys/ioctl.h" HJAVA_HAVE_TIOCGETD)
endif (NOT WINDOWS)

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
set (CMAKE_REQUIRED_INCLUDES "${MPI_INCLUDE_PATH}/mpi.h" )
set (CMAKE_REQUIRED_LIBRARIES "${MPI_LIBRARY}" )
CHECK_FUNCTION_EXISTS (MPI_File_get_size HJAVA_HAVE_MPI_GET_SIZE)

#-----------------------------------------------------------------------------



#-----------------------------------------------------------------------------
# Check a bunch of other functions
#-----------------------------------------------------------------------------
if (NOT WINDOWS)
  foreach (test
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
  endforeach (test)
endif (NOT WINDOWS)

#-----------------------------------------------------------------------------
# Determine how 'inline' is used
#-----------------------------------------------------------------------------
set (HJAVA_EXTRA_TEST_DEFINITIONS INLINE_TEST_INLINE)
foreach (inline_test inline __inline__ __inline)
  set (INLINE_TEST_INLINE ${inline_test})
  HJAVA_FUNCTION_TEST (INLINE_TEST_${inline_test})
endforeach (inline_test)

set (HJAVA_EXTRA_TEST_DEFINITIONS)
if (INLINE_TEST___inline__)
  set (HJAVA_inline __inline__)
else (INLINE_TEST___inline__)
  if (INLINE_TEST___inline)
    set (HJAVA_inline __inline)
  else (INLINE_TEST___inline)
    if (INLINE_TEST_inline)
      set (HJAVA_inline inline)
    endif (INLINE_TEST_inline)
  endif (INLINE_TEST___inline)
endif (INLINE_TEST___inline__)

#-----------------------------------------------------------------------------
# Check how to print a Long Long integer
#-----------------------------------------------------------------------------
if (NOT HJAVA_PRINTF_LL_WIDTH OR HJAVA_PRINTF_LL_WIDTH MATCHES "unknown")
  set (PRINT_LL_FOUND 0)
  message (STATUS "Checking for appropriate format for 64 bit long:")
  foreach (HJAVA_PRINTF_LL l64 l L q I64 ll)
    set (CURRENT_TEST_DEFINITIONS "-DPRINTF_LL_WIDTH=${HJAVA_PRINTF_LL}")
    if (HJAVA_SIZEOF_LONG_LONG)
      set (CURRENT_TEST_DEFINITIONS "${CURRENT_TEST_DEFINITIONS} -DHAVE_LONG_LONG")
    endif (HJAVA_SIZEOF_LONG_LONG)
    try_run (HJAVA_PRINTF_LL_TEST_RUN   HJAVA_PRINTF_LL_TEST_COMPILE
        ${HDFJAVA_BINARY_DIR}/CMake
        ${HDFJAVA_RESOURCES_DIR}/HDFTests.c
        CMAKE_FLAGS -DCOMPILE_DEFINITIONS:STRING=${CURRENT_TEST_DEFINITIONS}
        OUTPUT_VARIABLE OUTPUT
    )
    if (HJAVA_PRINTF_LL_TEST_COMPILE)
      if (HJAVA_PRINTF_LL_TEST_RUN MATCHES 0)
        set (HJAVA_PRINTF_LL_WIDTH "\"${HJAVA_PRINTF_LL}\"" CACHE INTERNAL "Width for printf for type `long long' or `__int64', us. `ll")
        set (PRINT_LL_FOUND 1)
      else (HJAVA_PRINTF_LL_TEST_RUN MATCHES 0)
        message ("Width with ${HJAVA_PRINTF_LL} failed with result: ${HJAVA_PRINTF_LL_TEST_RUN}")
      endif (HJAVA_PRINTF_LL_TEST_RUN MATCHES 0)
    else (HJAVA_PRINTF_LL_TEST_COMPILE)
      file (APPEND ${CMAKE_BINARY_DIR}/CMakeFiles/CMakeError.log
          "Test HJAVA_PRINTF_LL_WIDTH for ${HJAVA_PRINTF_LL} failed with the following output:\n ${OUTPUT}\n"
      )
    endif (HJAVA_PRINTF_LL_TEST_COMPILE)
  endforeach (HJAVA_PRINTF_LL)
  
  if (PRINT_LL_FOUND)
    message (STATUS "Checking for apropriate format for 64 bit long: found ${HJAVA_PRINTF_LL_WIDTH}")
  else (PRINT_LL_FOUND)
    message (STATUS "Checking for apropriate format for 64 bit long: not found")
    set (HJAVA_PRINTF_LL_WIDTH "\"unknown\"" CACHE INTERNAL
        "Width for printf for type `long long' or `__int64', us. `ll"
    )
  endif (PRINT_LL_FOUND)
endif (NOT HJAVA_PRINTF_LL_WIDTH OR HJAVA_PRINTF_LL_WIDTH MATCHES "unknown")

#-----------------------------------------------------------------------------
# Set the flag to indicate that the machine can handle converting
# denormalized floating-point values.
# (This flag should be set for all machines, except for the Crays, where
# the cache value is set in it's config file)
#-----------------------------------------------------------------------------
set (HJAVA_CONVERT_DENORMAL_FLOAT 1)

#-----------------------------------------------------------------------------
#  Are we going to use HSIZE_T
#-----------------------------------------------------------------------------
if (HJAVA_ENABLE_HSIZET)
  set (HJAVA_HAVE_LARGE_HSIZET 1)
endif (HJAVA_ENABLE_HSIZET)
