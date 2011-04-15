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
  SET (H4_BUILT_AS_DYNAMIC_LIB 1)
  SET (H5_BUILT_AS_DYNAMIC_LIB 1)
#  ADD_DEFINITIONS (-DHDFJAVA_BUILT_AS_DYNAMIC_LIB)
ELSE (BUILD_SHARED_LIBS)
  SET (HDFJAVA_BUILT_AS_STATIC_LIB 1)
  SET (H4_BUILT_AS_STATIC_LIB 1)
  SET (H5_BUILT_AS_STATIC_LIB 1)
  IF (NOT WIN32)
    # should this be a user setting : Everyone uses it anyway ?
    ADD_DEFINITIONS (-DPIC)
  ENDIF (NOT WIN32)
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

IF (MSVC)
  SET (CMAKE_MFC_FLAG 0)
ENDIF (MSVC)

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
OPTION (HDF_DISABLE_COMPILER_WARNINGS "Disable compiler warnings" OFF)
IF (HDF_DISABLE_COMPILER_WARNINGS)
  # MSVC uses /w to suppress warnings.  It also complains if another
  # warning level is given, so remove it.
  IF (MSVC)
    SET (HDF_WARNINGS_BLOCKED 1)
    STRING (REGEX REPLACE "(^| )([/-])W[0-9]( |$)" " "
        CMAKE_C_FLAGS "${CMAKE_C_FLAGS}")
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} /w")
  ENDIF (MSVC)
  IF (WIN32 AND NOT CYGWIN)
    ADD_DEFINITIONS (-D_CRT_SECURE_NO_WARNINGS)
  ENDIF (WIN32 AND NOT CYGWIN)
  # Borland uses -w- to suppress warnings.
  IF (BORLAND)
    SET (HDF_WARNINGS_BLOCKED 1)
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -w-")
  ENDIF (BORLAND)

  # Most compilers use -w to suppress warnings.
  IF (NOT HDF_WARNINGS_BLOCKED)
    SET (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -w")
  ENDIF (NOT HDF_WARNINGS_BLOCKED)
ENDIF (HDF_DISABLE_COMPILER_WARNINGS)

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

INCLUDE (ExternalProject)
OPTION (HDF_ALLOW_EXTERNAL_SUPPORT "Allow External Library Building" "NO")
OPTION (JPEG_USE_EXTERNAL "Use External Library Building for JPEG" 0)
OPTION (ZLIB_USE_EXTERNAL "Use External Library Building for ZLIB" 0)
OPTION (SZIP_USE_EXTERNAL "Use External Library Building for SZIP" 0)
OPTION (HDF4_USE_EXTERNAL "Use External Library Building for HDF4" 0)
OPTION (HDF5_USE_EXTERNAL "Use External Library Building for HDF5" 0)
IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  SET (JPEG_USE_EXTERNAL 1)
  SET (ZLIB_USE_EXTERNAL 1)
  SET (SZIP_USE_EXTERNAL 1)
  SET (HDF4_USE_EXTERNAL 1)
  SET (HDF5_USE_EXTERNAL 1)
  IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN")
    SET (JPEG_URL ${JPEG_SVN_URL})
    SET (ZLIB_URL ${ZLIB_SVN_URL})
    SET (SZIP_URL ${SZIP_SVN_URL})
    SET (HDF4_URL ${HDF4_SVN_URL})
    SET (HDF5_URL ${HDF5_SVN_URL})
  ELSEIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    IF (NOT TGZ_PATH)
      SET (TGZ_PATH ${HDFJAVA_SOURCE_DIR})
    ENDIF (NOT TGZ_PATH)
    SET (JPEG_URL ${TGZ_PATH}/${JPEG_TGZ_NAME})
    SET (ZLIB_URL ${TGZ_PATH}/${ZLIB_TGZ_NAME})
    SET (SZIP_URL ${TGZ_PATH}/${SZIP_TGZ_NAME})
    SET (HDF4_URL ${TGZ_PATH}/${HDF4_TGZ_NAME})
    SET (HDF5_URL ${TGZ_PATH}/${HDF5_TGZ_NAME})
  ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN")
ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")

SET (HDF4_LINK_LIBS ${LINK_LIBS})
SET (HDF5_LINK_LIBS ${LINK_LIBS})

#-----------------------------------------------------------------------------
# Option for LibJpeg support
#-----------------------------------------------------------------------------
OPTION (HDF_ENABLE_JPEG_LIB_SUPPORT "Enable libjpeg" ON)
IF (HDF_ENABLE_JPEG_LIB_SUPPORT)
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
      IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        # May need to build JPEG with PIC on x64 machines with gcc
        # Need to use CMAKE_ANSI_CFLAGS define so that compiler test works
        IF (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS "-fPIC")
        ELSE (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS " ")
        ENDIF (BUILD_JPEG_WITH_PIC)
        
        EXTERNAL_JPEG_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${JPEG_CMAKE_C_FLAGS})
        SET (HJAVA_HAVE_JPEGLIB_H 1)
        SET (HJAVA_HAVE_LIBJPEG 1)
        SET (HJAVA_JPEGLIB_HEADER "jpeglib.h")
        MESSAGE (STATUS "JPEGLIB is built")
      ELSE (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " JPEGLib is Required for JPEGLib support in HDFJAVA")
      ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
    ENDIF (JPEG_FOUND)
  ELSE (NOT HJAVA_JPEGLIB_HEADER)
    # This project is being called from within another and JPEGLib is already configured
    SET (HJAVA_HAVE_JPEGLIB_H 1)
  ENDIF (NOT HJAVA_JPEGLIB_HEADER)
  SET (LINK_LIBS ${LINK_LIBS} ${JPEG_LIBRARY})
  SET (HDF4_LINK_LIBS ${HDF4_LINK_LIBS} ${JPEG_LIBRARY})
  INCLUDE_DIRECTORIES (${JPEG_INCLUDE_DIRS})
  MESSAGE (STATUS "JPEGLIB is ON")
ENDIF (HDF_ENABLE_JPEG_LIB_SUPPORT)

#-----------------------------------------------------------------------------
# Option for ZLib support
#-----------------------------------------------------------------------------
OPTION (HDF_ENABLE_Z_LIB_SUPPORT "Enable Zlib Filters" ON)
IF (HDF_ENABLE_Z_LIB_SUPPORT)
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
      IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        EXTERNAL_ZLIB_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
        SET (HJAVA_HAVE_FILTER_DEFLATE 1)
        SET (HJAVA_HAVE_ZLIB_H 1)
        SET (HJAVA_HAVE_LIBZ 1)
        MESSAGE (STATUS "Filter ZLIB is built")
      ELSE (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " ZLib is Required for ZLib support in HDFJAVA")
      ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
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
ENDIF (HDF_ENABLE_Z_LIB_SUPPORT)

#-----------------------------------------------------------------------------
# Option for SzLib support
#-----------------------------------------------------------------------------
#SET (CMAKE_MODULE_PATH ${HDFJAVA_RESOURCES_DIR} ${CMAKE_MODULE_PATH})
OPTION (HDF_ENABLE_SZIP_SUPPORT "Use SZip Filter" OFF)
IF (HDF_ENABLE_SZIP_SUPPORT)
  OPTION (HDF_ENABLE_SZIP_ENCODING "Use SZip Encoding" OFF)
  IF (NOT SZIP_USE_EXTERNAL)
    FIND_PACKAGE (SZIP)
  ENDIF (NOT SZIP_USE_EXTERNAL)
  IF (SZIP_FOUND)
    SET (HJAVA_HAVE_FILTER_SZIP 1)
    SET (HJAVA_HAVE_SZLIB_H 1)
    SET (HJAVA_HAVE_LIBSZ 1)
    SET (SZIP_INCLUDE_DIR_GEN ${SZIP_INCLUDE_DIR})
  ELSE (SZIP_FOUND)
    IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      EXTERNAL_SZIP_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE} ${HDF_ENABLE_SZIP_ENCODING})
      SET (HJAVA_HAVE_FILTER_SZIP 1)
      SET (HJAVA_HAVE_SZLIB_H 1)
      SET (HJAVA_HAVE_LIBSZ 1)
      MESSAGE (STATUS "Filter SZIP is built")
    ELSE (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
      MESSAGE (FATAL_ERROR "SZIP is Required for SZIP support in HDFJAVA")
    ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
  ENDIF (SZIP_FOUND)
  SET (LINK_LIBS ${LINK_LIBS} ${SZIP_LIBRARIES})
  SET (HDF4_LINK_LIBS ${HDF4_LINK_LIBS} ${SZIP_LIBRARIES})
  SET (HDF5_LINK_LIBS ${HDF5_LINK_LIBS} ${SZIP_LIBRARIES})
  INCLUDE_DIRECTORIES (${SZIP_INCLUDE_DIRS})
  MESSAGE (STATUS "Filter SZIP is ON")
  IF (HDF_ENABLE_SZIP_ENCODING)
    SET (HDF_HAVE_SZIP_ENCODER 1)
  ENDIF (HDF_ENABLE_SZIP_ENCODING)
ENDIF (HDF_ENABLE_SZIP_SUPPORT)

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
      IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        # May need to build JPEG with PIC on x64 machines with gcc
        # Need to use CMAKE_ANSI_CFLAGS define so that compiler test works
        IF (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS "-fPIC")
        ELSE (BUILD_JPEG_WITH_PIC)
          SET (JPEG_CMAKE_C_FLAGS " ")
        ENDIF (BUILD_JPEG_WITH_PIC)
        
        EXTERNAL_HDF4_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE} ${JPEG_CMAKE_C_FLAGS})
        SET (HJAVA_HAVE_HDF_H 1)
        SET (HJAVA_HAVE_HDF4 1)
        SET (HJAVA_HDF4_HEADER "hdf.h")
        MESSAGE (STATUS "HDF4 is built")
      ELSE (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " HDF4 is Required for  HDFJAVA")
      ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
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
      IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        EXTERNAL_HDF5_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
        SET (HJAVA_HAVE_H5PUBCONF_H 1)
        SET (HJAVA_HAVE_HDF5 1)
        SET (HJAVA_HDF5_HEADER "h5pubconf.h")
        MESSAGE (STATUS "HDF5 is built")
      ELSE (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
        MESSAGE (FATAL_ERROR " HDF5 is Required for  HDFJAVA")
      ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
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
OPTION (HDF_PACKAGE_EXTLIBS "CPACK - include external libraries" OFF)
IF (HDF_PACKAGE_EXTLIBS)
  SET (EXTERNAL_HEADER_LIST "")
  SET (EXTERNAL_LIBRARY_LIST "")
  SET (EXTERNAL_LIBRARYDLL_LIST "")
  FILE (MAKE_DIRECTORY ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${BLDTYPE})
    
  IF (HDF_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)
    PACKAGE_JPEG_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT})
  ENDIF (HDF_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)

  IF (HDF_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
    PACKAGE_ZLIB_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDF_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
    
  IF (HDF_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
    PACKAGE_SZIP_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDF_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
    
  IF (HDF4_FOUND)
    PACKAGE_HDF4_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDF4_FOUND)
    
  IF (HDF5_FOUND)
    PACKAGE_HDF5_LIBRARY (${HDF_ALLOW_EXTERNAL_SUPPORT} ${LIB_TYPE})
  ENDIF (HDF5_FOUND)
ENDIF (HDF_PACKAGE_EXTLIBS)

ADD_SUBDIRECTORY(${HDFJAVA_JNI_SOURCE_DIR}/hdflib      hdflib)
ADD_SUBDIRECTORY(${HDFJAVA_JNI_SOURCE_DIR}/hdf5lib     hdf5lib)

IF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")
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
    ADD_DEPENDENCIES (HDF4 JPEG)
    ADD_DEPENDENCIES (HDF4 ZLIB)
    ADD_DEPENDENCIES (HDF4 SZIP)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF_LIB_TARGET} HDF4)
  ENDIF (HDF4_FOUND AND HDF4_USE_EXTERNAL)
  IF (HDF5_FOUND AND HDF5_USE_EXTERNAL)
    ADD_DEPENDENCIES (HDF5 ZLIB)
    ADD_DEPENDENCIES (HDF5 SZIP)
    ADD_DEPENDENCIES (${HDFJAVA_JNI_HDF5_LIB_TARGET} HDF5)
  ENDIF (HDF5_FOUND AND HDF5_USE_EXTERNAL)
ENDIF (HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "SVN" OR HDF_ALLOW_EXTERNAL_SUPPORT MATCHES "TGZ")

#-----------------------------------------------------------------------------
# Option for external libraries
#-----------------------------------------------------------------------------
IF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)
  IF (HDF_PACKAGE_EXTLIBS)
    IF (HDF_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)
      INSTALL (
          FILES ${JPEG_INCLUDE_DIR}/jerror.h ${JPEG_INCLUDE_DIR}/jmorecfg.h ${JPEG_INCLUDE_DIR}/jpeglib.h
          DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
          COMPONENT headers
      )
    ENDIF (HDF_ENABLE_JPEG_LIB_SUPPORT AND JPEG_FOUND)
    IF (HDF_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
      INSTALL (
          FILES ${ZLIB_INCLUDE_DIR}/zlib.h
          DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
          COMPONENT headers
      )
    ENDIF (HDF_ENABLE_Z_LIB_SUPPORT AND ZLIB_FOUND)
    IF (HDF_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
      INSTALL (
          FILES ${SZIP_INCLUDE_DIR}/szlib.h ${SZIP_INCLUDE_DIR}/szip_adpt.h ${SZIP_INCLUDE_DIR}/ricehdf.h
          DESTINATION ${HJAVA_INSTALL_INCLUDE_DIR}
          COMPONENT headers
      )
    ENDIF (HDF_ENABLE_SZIP_SUPPORT AND SZIP_FOUND)
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
  ENDIF (HDF_PACKAGE_EXTLIBS)
ENDIF (NOT HDFJAVA_EXTERNALLY_CONFIGURED)