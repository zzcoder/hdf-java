#-------------------------------------------------------------------------------
MACRO (EXTERNAL_HDF4_LIBRARY compress_type libtype)
  set (HDF45_BUILD_XDR_LIB OFF)
  if (WIN32)
    set (HDF45_BUILD_XDR_LIB ON)
  endif (WIN32)
  if (${compress_type} MATCHES "SVN")
    EXTERNALPROJECT_ADD (HDF4
        SVN_REPOSITORY ${HDF4_URL}
        # [SVN_REVISION rev] 
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF_PACKAGE_EXT:STRING=${HDF_PACKAGE_EXT}
            -DCMAKE_BUILD_TYPE:STRING=${CMAKE_BUILD_TYPE}
            -DCMAKE_INSTALL_PREFIX:PATH=${CMAKE_INSTALL_PREFIX}
            -DCMAKE_RUNTIME_OUTPUT_DIRECTORY:PATH=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}
            -DCMAKE_LIBRARY_OUTPUT_DIRECTORY:PATH=${CMAKE_LIBRARY_OUTPUT_DIRECTORY}
            -DCMAKE_ARCHIVE_OUTPUT_DIRECTORY:PATH=${CMAKE_ARCHIVE_OUTPUT_DIRECTORY}
            -DCMAKE_ANSI_CFLAGS:STRING=${CMAKE_ANSI_CFLAGS}
            -DHDF4_BUILD_TOOLS:BOOL=OFF
            -DHDF4_BUILD_FORTRAN:BOOL=OFF
            -DHDF4_BUILD_XDR_LIB:BOOL=${HDF45_BUILD_XDR_LIB}
            -DHDF4_EXTERNALLY_CONFIGURED:BOOL=OFF
            -DHDF4_PACKAGE_EXTLIBS:BOOL=${HDF_PACKAGE_EXTLIBS}
            -DHDF4_ALLOW_EXTERNAL_SUPPORT:STRING="SVN"
            -DJPEG_SVN_URL:STRING=${JPEG_SVN_URL}
            -DZLIB_SVN_URL:STRING=${ZLIB_SVN_URL}
            -DSZIP_SVN_URL:STRING=${SZIP_SVN_URL}
            -DHDF4_ENABLE_JPEG_LIB_SUPPORT:BOOL=${HDF_ENABLE_JPEG_LIB_SUPPORT}
            -DHDF4_ENABLE_Z_LIB_SUPPORT:BOOL=${HDF_ENABLE_Z_LIB_SUPPORT}
            -DHDF4_ENABLE_SZIP_SUPPORT:BOOL=${HDF_ENABLE_SZIP_SUPPORT}
            -DHDF4_ENABLE_SZIP_ENCODING:BOOL=${HDF_ENABLE_SZIP_ENCODING}
    ) 
  endif (${compress_type} MATCHES "SVN")
  if (${compress_type} MATCHES "TGZ")
    EXTERNALPROJECT_ADD (HDF4
        URL ${HDF4_URL}
        URL_MD5 ""
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF_PACKAGE_EXT:STRING=${HDF_PACKAGE_EXT}
            -DCMAKE_BUILD_TYPE:STRING=${CMAKE_BUILD_TYPE}
            -DCMAKE_INSTALL_PREFIX:PATH=${CMAKE_INSTALL_PREFIX}
            -DCMAKE_RUNTIME_OUTPUT_DIRECTORY:PATH=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}
            -DCMAKE_LIBRARY_OUTPUT_DIRECTORY:PATH=${CMAKE_LIBRARY_OUTPUT_DIRECTORY}
            -DCMAKE_ARCHIVE_OUTPUT_DIRECTORY:PATH=${CMAKE_ARCHIVE_OUTPUT_DIRECTORY}
            -DCMAKE_ANSI_CFLAGS:STRING=${CMAKE_ANSI_CFLAGS}
            -DHDF4_BUILD_TOOLS:BOOL=OFF
            -DHDF4_BUILD_FORTRAN:BOOL=OFF
            -DHDF4_BUILD_XDR_LIB:BOOL=${HDF45_BUILD_XDR_LIB}
            -DHDF4_EXTERNALLY_CONFIGURED:BOOL=OFF
            -DHDF4_PACKAGE_EXTLIBS:BOOL=${HDF_PACKAGE_EXTLIBS}
            -DHDF4_ALLOW_EXTERNAL_SUPPORT:STRING="TGZ"
            -DTGZPATH:STRING=${TGZPATH}
            -DJPEG_TGZ_NAME:STRING=${JPEG_TGZ_NAME}
            -DZLIB_TGZ_NAME:STRING=${ZLIB_TGZ_NAME}
            -DSZIP_TGZ_NAME:STRING=${SZIP_TGZ_NAME}
            -DHDF4_ENABLE_JPEG_LIB_SUPPORT:BOOL=${HDF_ENABLE_JPEG_LIB_SUPPORT}
            -DHDF4_ENABLE_Z_LIB_SUPPORT:BOOL=${HDF_ENABLE_Z_LIB_SUPPORT}
            -DHDF4_ENABLE_SZIP_SUPPORT:BOOL=${HDF_ENABLE_SZIP_SUPPORT}
            -DHDF4_ENABLE_SZIP_ENCODING:BOOL=${HDF_ENABLE_SZIP_ENCODING}
    ) 
  endif (${compress_type} MATCHES "TGZ")
  EXTERNALPROJECT_GET_PROPERTY (HDF4 BINARY_DIR SOURCE_DIR) 

  if (${CMAKE_BUILD_TYPE} MATCHES "DEBUG")
    if (WIN32)
      set (HDF_LIB_NAME "hdf_D")
      set (MFHDF_LIB_NAME "hdf_D")
      set (XDR_LIB_NAME "xdr_D")
      if (HDF_ENABLE_JPEG_LIB_SUPPORT)
        set (JPEG_LIB_NAME "jpeg_D")
      endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
      if (HDF_ENABLE_Z_LIB_SUPPORT)
        set (ZLIB_LIB_NAME "zlib_D")
      endif (HDF_ENABLE_Z_LIB_SUPPORT)
      if (HDF_ENABLE_SZIP_SUPPORT)
        set (SZIP_LIB_NAME "szip_D")
      endif (HDF_ENABLE_SZIP_SUPPORT)
    else (WIN32)
      set (HDF_LIB_NAME "hdf_debug")
      set (MFHDF_LIB_NAME "mfhdf_debug")
      set (XDR_LIB_NAME "xdr_debug")
      if (HDF_ENABLE_JPEG_LIB_SUPPORT)
        set (JPEG_LIB_NAME "jpeg_debug")
      endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
      if (HDF_ENABLE_Z_LIB_SUPPORT)
        set (ZLIB_LIB_NAME "z_debug")
      endif (HDF_ENABLE_Z_LIB_SUPPORT)
      if (HDF_ENABLE_SZIP_SUPPORT)
        set (SZIP_LIB_NAME "szip_debug")
      endif (HDF_ENABLE_SZIP_SUPPORT)
    endif (WIN32)
  else (${CMAKE_BUILD_TYPE} MATCHES "DEBUG")
    set (HDF_LIB_NAME "hdf")
    set (MFHDF_LIB_NAME "mfhdf")
    set (XDR_LIB_NAME "xdr")
    if (HDF_ENABLE_JPEG_LIB_SUPPORT)
      set (JPEG_LIB_NAME "jpeg")
    endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
    if (HDF_ENABLE_Z_LIB_SUPPORT)
      if (WIN32)
        set (ZLIB_LIB_NAME "zlib")
      else (WIN32)
        set (ZLIB_LIB_NAME "z")
      endif (WIN32)
    endif (HDF_ENABLE_Z_LIB_SUPPORT)
    if (HDF_ENABLE_SZIP_SUPPORT)
      set (SZIP_LIB_NAME "szip")
    endif (HDF_ENABLE_SZIP_SUPPORT)
  endif (${CMAKE_BUILD_TYPE} MATCHES "DEBUG")

  # Create imported target hdf4
  ADD_LIBRARY(hdf ${libtype} IMPORTED)
  add_dependencies (hdf HDF4)
  ADD_LIBRARY(mfhdf ${libtype} IMPORTED)
  add_dependencies (mfhdf HDF4)
  set (MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES "hdf")
  if (HDF45_BUILD_XDR_LIB)
    ADD_LIBRARY(xdr ${libtype} IMPORTED)
    add_dependencies (xdr HDF4)
    set (MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES "xdr;${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}")
  endif (HDF45_BUILD_XDR_LIB)
  if (HDF_ENABLE_JPEG_LIB_SUPPORT)
    ADD_LIBRARY(jpeg ${libtype} IMPORTED)
    add_dependencies (jpeg HDF4)
  endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
  if (HDF_ENABLE_Z_LIB_SUPPORT)
    ADD_LIBRARY(zlib ${libtype} IMPORTED)
    add_dependencies (zlib HDF4)
  endif (HDF_ENABLE_Z_LIB_SUPPORT)
  if (HDF_ENABLE_SZIP_SUPPORT)
    ADD_LIBRARY(szip ${libtype} IMPORTED)
    add_dependencies (szip HDF4)
  endif (HDF_ENABLE_SZIP_SUPPORT)

  if (${libtype} MATCHES "SHARED")
    if (WIN32)
      if (MINGW)
        SET_TARGET_PROPERTIES(hdf PROPERTIES
            IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${HDF_LIB_NAME}.lib"
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${HDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LIBRARIES "jpeg;zlib;szip"
          )
        SET_TARGET_PROPERTIES(mfhdf PROPERTIES
            IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${MFHDF_LIB_NAME}.lib"
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${MFHDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LIBRARIES "${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}"
          )
        if (HDF45_BUILD_XDR_LIB)
          SET_TARGET_PROPERTIES(xdr PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${XDR_LIB_NAME}.lib"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${XDR_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF45_BUILD_XDR_LIB)
        if (HDF_ENABLE_JPEG_LIB_SUPPORT)
          SET_TARGET_PROPERTIES(jpeg PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${JPEG_LIB_NAME}.lib"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${JPEG_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
        if (HDF_ENABLE_Z_LIB_SUPPORT)
         SET_TARGET_PROPERTIES(zlib PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${ZLIB_LIB_NAME}.lib"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${ZLIB_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF_ENABLE_Z_LIB_SUPPORT)
        if (HDF_ENABLE_SZIP_SUPPORT)
          SET_TARGET_PROPERTIES(szip PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${SZIP_LIB_NAME}.lib"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${SZIP_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF_ENABLE_SZIP_SUPPORT)
      else (MINGW)
        SET_TARGET_PROPERTIES(hdf PROPERTIES
            IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${HDF_LIB_NAME}${CMAKE_IMPORT_LIBRARY_SUFFIX}"
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${HDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LIBRARIES "jpeg;zlib;szip"
          )
        SET_TARGET_PROPERTIES(mfhdf PROPERTIES
            IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${MFHDF_LIB_NAME}${CMAKE_IMPORT_LIBRARY_SUFFIX}"
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${MFHDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LIBRARIES "${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}"
          )
        if (HDF45_BUILD_XDR_LIB)
          SET_TARGET_PROPERTIES(xdr PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${XDR_LIB_NAME}${CMAKE_IMPORT_LIBRARY_SUFFIX}"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${XDR_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF45_BUILD_XDR_LIB)
        if (HDF_ENABLE_JPEG_LIB_SUPPORT)
          SET_TARGET_PROPERTIES(jpeg PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${JPEG_LIB_NAME}${CMAKE_IMPORT_LIBRARY_SUFFIX}"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${JPEG_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
        if (HDF_ENABLE_Z_LIB_SUPPORT)
         SET_TARGET_PROPERTIES(zlib PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${ZLIB_LIB_NAME}${CMAKE_IMPORT_LIBRARY_SUFFIX}"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${ZLIB_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF_ENABLE_Z_LIB_SUPPORT)
        if (HDF_ENABLE_SZIP_SUPPORT)
          SET_TARGET_PROPERTIES(szip PROPERTIES
              IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${SZIP_LIB_NAME}${CMAKE_IMPORT_LIBRARY_SUFFIX}"
              IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${SZIP_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          )
        endif (HDF_ENABLE_SZIP_SUPPORT)
      endif (MINGW)
    else (WIN32)
      SET_TARGET_PROPERTIES(hdf PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${HDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "jpeg;zlib;szip"
          IMPORTED_SONAME "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${HDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}.${HDF4_VERSION_STRING}"
          SOVERSION "${HDF4_VERSION_STRING}"
      )
      SET_TARGET_PROPERTIES(mfhdf PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${MFHDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}"
          IMPORTED_SONAME "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${MFHDF_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}.${HDF4_VERSION_STRING}"
          SOVERSION "${HDF4_VERSION_STRING}"
      )
      if (HDF45_BUILD_XDR_LIB)
        SET_TARGET_PROPERTIES(xdr PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${XDR_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_SONAME "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${XDR_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}.${HDF4_VERSION_STRING}"
            SOVERSION "${HDF4_VERSION_STRING}"
        )
      endif (HDF45_BUILD_XDR_LIB)
      if (HDF_ENABLE_JPEG_LIB_SUPPORT)
        SET_TARGET_PROPERTIES(jpeg PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${JPEG_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_SONAME "${CMAKE_SHARED_LIBRARY_PREFIX}${JPEG_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}.${JPEG_VERSION_STRING}"
            SOVERSION "${JPEG_VERSION_STRING}"
        )
      endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
      if (HDF_ENABLE_Z_LIB_SUPPORT)
        SET_TARGET_PROPERTIES(zlib PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${ZLIB_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_SONAME "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${ZLIB_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}.${ZLIB_VERSION_STRING}"
            SOVERSION "${ZLIB_VERSION_STRING}"
        )
      endif (HDF_ENABLE_Z_LIB_SUPPORT)
      if (HDF_ENABLE_SZIP_SUPPORT)
        SET_TARGET_PROPERTIES(szip PROPERTIES
           IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${SZIP_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_SONAME "${CMAKE_SHARED_LIBRARY_PREFIX}${SZIP_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}.${SZIP_VERSION_STRING}"
            SOVERSION "${SZIP_VERSION_STRING}"
        )
      endif (HDF_ENABLE_SZIP_SUPPORT)
    endif (WIN32)
  else (${libtype} MATCHES "SHARED")
    if (WIN32 AND NOT MINGW)
      SET_TARGET_PROPERTIES(hdf PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/lib${HDF_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "jpeg;zlib;szip"
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
      SET_TARGET_PROPERTIES(mfhdf PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/lib${MFHDF_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}"
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
      if (HDF45_BUILD_XDR_LIB)
        SET_TARGET_PROPERTIES(xdr PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/lib${XDR_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
      endif (HDF45_BUILD_XDR_LIB)
      if (HDF_ENABLE_JPEG_LIB_SUPPORT)
        SET_TARGET_PROPERTIES(jpeg PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/lib${JPEG_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
        )
      endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
      if (HDF_ENABLE_Z_LIB_SUPPORT)
        SET_TARGET_PROPERTIES(zlib PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/lib${ZLIB_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
        )
      endif (HDF_ENABLE_Z_LIB_SUPPORT)
      if (HDF_ENABLE_SZIP_SUPPORT)
        SET_TARGET_PROPERTIES(szip PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/lib${SZIP_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
        )
      endif (HDF_ENABLE_SZIP_SUPPORT)
    else (WIN32 AND NOT MINGW)
      SET_TARGET_PROPERTIES(hdf PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/lib${HDF_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "jpeg;zlib;szip"
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
      SET_TARGET_PROPERTIES(mfhdf PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/lib${MFHDF_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}"
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
      if (HDF45_BUILD_XDR_LIB)
        SET_TARGET_PROPERTIES(xdr PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/lib${XDR_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
      endif (HDF45_BUILD_XDR_LIB)
      if (HDF_ENABLE_JPEG_LIB_SUPPORT)
        SET_TARGET_PROPERTIES(jpeg PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/lib${JPEG_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
        )
      endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
      if (HDF_ENABLE_Z_LIB_SUPPORT)
        SET_TARGET_PROPERTIES(zlib PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/lib${ZLIB_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
        )
      endif (HDF_ENABLE_Z_LIB_SUPPORT)
      if (HDF_ENABLE_SZIP_SUPPORT)
        SET_TARGET_PROPERTIES(szip PROPERTIES
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/lib${SZIP_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LANGUAGES "C"
        )
      endif (HDF_ENABLE_SZIP_SUPPORT)
    endif (WIN32 AND NOT MINGW)
  endif (${libtype} MATCHES "SHARED")
  set (HDF4_LIBRARY "hdf;mfhdf")
  if (HDF45_BUILD_XDR_LIB)
    set (HDF4_LIBRARY "${HDF4_LIBRARY};xdr")
  endif (HDF45_BUILD_XDR_LIB)
  if (HDF_ENABLE_JPEG_LIB_SUPPORT)
    set (HDF4_LIBRARY "${HDF4_LIBRARY};jpeg")
    set (JPEG_LIBRARY "jpeg")
    set (JPEG_INCLUDE_DIR "${BINARY_DIR}/JPEG-prefix/src/JPEG/src")
    set (JPEG_INCLUDE_DIR_GEN "${BINARY_DIR}/JPEG-prefix/src/JPEG-build" PARENT_SCOPE)
    set (H425_HAVE_JPEGLIB_H 1)
    set (H425_HAVE_LIBJPEG 1)
    set (H425_JPEGLIB_HEADER "jpeglib.h")
    set (JPEG_LIBRARIES ${JPEG_LIBRARY})
    set (JPEG_INCLUDE_DIRS ${JPEG_INCLUDE_DIR_GEN} ${JPEG_INCLUDE_DIR})
    set (JPEG_FOUND 1 PARENT_SCOPE)
  endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
  if (HDF_ENABLE_Z_LIB_SUPPORT)
    set (HDF4_LIBRARY "${HDF4_LIBRARY};zlib")
    set (ZLIB_LIBRARY "zlib")
    set (ZLIB_INCLUDE_DIR "${BINARY_DIR}/ZLIB-prefix/src/ZLIB/src")
    set (ZLIB_INCLUDE_DIR_GEN "${BINARY_DIR}/ZLIB-prefix/src/ZLIB-build" PARENT_SCOPE)
    set (H425_HAVE_FILTER_DEFLATE 1)
    set (H425_HAVE_ZLIB_H 1)
    set (H425_HAVE_LIBZ 1)
    set (H425_ZLIB_HEADER "zlib.h")
    set (ZLIB_LIBRARIES ${ZLIB_LIBRARY})
    set (ZLIB_INCLUDE_DIRS ${ZLIB_INCLUDE_DIR_GEN} ${ZLIB_INCLUDE_DIR})
    set (ZLIB_FOUND 1 PARENT_SCOPE)
  endif (HDF_ENABLE_Z_LIB_SUPPORT)
  if (HDF_ENABLE_SZIP_SUPPORT)
    set (HDF4_LIBRARY "${HDF4_LIBRARY};szip")
    set (SZIP_LIBRARY "szip")
    set (SZIP_INCLUDE_DIR "${BINARY_DIR}/SZIP-prefix/src/SZIP/src")
    set (SZIP_INCLUDE_DIR_GEN "${BINARY_DIR}/SZIP-prefix/src/SZIP-build" PARENT_SCOPE)
    set (H425_HAVE_FILTER_SZIP 1)
    set (H425_HAVE_SZLIB_H 1)
    set (H425_HAVE_LIBSZ 1)
    set (SZIP_LIBRARIES ${SZIP_LIBRARY})
    set (SZIP_INCLUDE_DIRS ${SZIP_INCLUDE_DIR_GEN} ${SZIP_INCLUDE_DIR})
    set (SZIP_FOUND 1 PARENT_SCOPE)
  endif (HDF_ENABLE_SZIP_SUPPORT)
#  file (READ ${BINARY_DIR}/h4config.h _h4config_h_contents)
#  STRING (REGEX REPLACE ".*#define[ \t]+H4_VERSION[ \t]+\"([0-9A-Za-z.]+)\".*" "\\1" HDF4_VERSION_STRING ${_h4config_h_contents})
  
  set (HDF4_INCLUDE_DIR_GEN "${BINARY_DIR}")
  set (HDF4_HDF_INCLUDE_DIR "${SOURCE_DIR}/hdf/src")
  set (HDF4_MFHDF_INCLUDE_DIR "${SOURCE_DIR}/mfhdf/libsrc")
  set (HDF4_INCLUDE_DIR ${HDF4_HDF_INCLUDE_DIR} ${HDF4_MFHDF_INCLUDE_DIR})
  set (HDF4_FOUND 1)
  set (HDF4_LIBRARIES ${HDF4_LIBRARY})
  set (HDF4_INCLUDE_DIRS ${HDF4_INCLUDE_DIR_GEN} ${HDF4_INCLUDE_DIR})
ENDMACRO (EXTERNAL_HDF4_LIBRARY)

#-------------------------------------------------------------------------------
MACRO (PACKAGE_HDF4_LIBRARY compress_type libtype)
  ADD_CUSTOM_TARGET (HDF4-GenHeader-Copy ALL
      COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_INCLUDE_DIR_GEN}/h4config.h ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
      COMMENT "Copying ${HDF4_INCLUDE_DIR_GEN}/h4config.h to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
  )
  set (EXTERNAL_HEADER_LIST ${EXTERNAL_HEADER_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/h4config.h)
  if (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    add_dependencies (HDF4-GenHeader-Copy HDF4)
  endif (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
  if (WIN32)
    if (${libtype} MATCHES "SHARED")
      foreach (HDF4_LIB ${HDF4_LIBRARY})
        GET_PROPERTY (HDF4_DLL TARGET ${HDF4_LIB} PROPERTY LOCATION_${CMAKE_BUILD_TYPE})
        GET_FILENAME_COMPONENT(HDF4_DLL_NAME ${HDF4_DLL} NAME)
        if (BUILD_TESTING)
          ADD_CUSTOM_TARGET (${HDF4_DLL_NAME}-Test-Copy ALL
              COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_DLL} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${HDF4_DLL_NAME}
              COMMENT "Copying ${HDF4_DLL} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
          )
          add_dependencies (${HDF4_DLL_NAME}-Test-Copy HDF4)
        endif (BUILD_TESTING)
      endforeach (HDF4_LIB {HDF4_LIBRARY})
    endif (${libtype} MATCHES "SHARED")
  endif (WIN32)
ENDMACRO (PACKAGE_HDF4_LIBRARY)

#-------------------------------------------------------------------------------
MACRO (EXTERNAL_HDF5_LIBRARY compress_type libtype)
  if (${compress_type} MATCHES "SVN")
    EXTERNALPROJECT_ADD (HDF5
        SVN_REPOSITORY ${HDF5_URL}
        # [SVN_REVISION rev] 
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF_PACKAGE_EXT:STRING=${HDF_PACKAGE_EXT}
            -DCMAKE_BUILD_TYPE:STRING=${CMAKE_BUILD_TYPE}
            -DCMAKE_INSTALL_PREFIX:PATH=${CMAKE_INSTALL_PREFIX}
            -DCMAKE_RUNTIME_OUTPUT_DIRECTORY:PATH=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}
            -DCMAKE_LIBRARY_OUTPUT_DIRECTORY:PATH=${CMAKE_LIBRARY_OUTPUT_DIRECTORY}
            -DCMAKE_ARCHIVE_OUTPUT_DIRECTORY:PATH=${CMAKE_ARCHIVE_OUTPUT_DIRECTORY}
            -DCMAKE_ANSI_CFLAGS:STRING=${CMAKE_ANSI_CFLAGS}
            -DHDF5_BUILD_TOOLS:BOOL=OFF
            -DHDF5_EXTERNALLY_CONFIGURED:BOOL=OFF
            -DHDF5_PACKAGE_EXTLIBS:BOOL=${HDF_PACKAGE_EXTLIBS}
            -DHDF5_ALLOW_EXTERNAL_SUPPORT:STRING="SVN"
            -DZLIB_SVN_URL:STRING=${ZLIB_SVN_URL}
            -DSZIP_SVN_URL:STRING=${SZIP_SVN_URL}
            -DHDF5_ENABLE_Z_LIB_SUPPORT:BOOL=${HDF_ENABLE_Z_LIB_SUPPORT}
            -DHDF5_ENABLE_SZIP_SUPPORT:BOOL=${HDF_ENABLE_SZIP_SUPPORT}
            -DHDF5_ENABLE_SZIP_ENCODING:BOOL=${HDF_ENABLE_SZIP_ENCODING}
    ) 
  endif (${compress_type} MATCHES "SVN")
  if (${compress_type} MATCHES "TGZ")
    EXTERNALPROJECT_ADD (HDF5
        URL ${HDF5_URL}
        URL_MD5 ""
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF_PACKAGE_EXT:STRING=${HDF_PACKAGE_EXT}
            -DCMAKE_BUILD_TYPE:STRING=${CMAKE_BUILD_TYPE}
            -DCMAKE_INSTALL_PREFIX:PATH=${CMAKE_INSTALL_PREFIX}
            -DCMAKE_RUNTIME_OUTPUT_DIRECTORY:PATH=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}
            -DCMAKE_LIBRARY_OUTPUT_DIRECTORY:PATH=${CMAKE_LIBRARY_OUTPUT_DIRECTORY}
            -DCMAKE_ARCHIVE_OUTPUT_DIRECTORY:PATH=${CMAKE_ARCHIVE_OUTPUT_DIRECTORY}
            -DCMAKE_ANSI_CFLAGS:STRING=${CMAKE_ANSI_CFLAGS}
            -DHDF5_BUILD_TOOLS:BOOL=OFF
            -DHDF5_EXTERNALLY_CONFIGURED:BOOL=OFF
            -DHDF5_PACKAGE_EXTLIBS:BOOL=${HDF_PACKAGE_EXTLIBS}
            -DHDF5_ALLOW_EXTERNAL_SUPPORT:STRING="TGZ"
            -DTGZPATH:STRING=${TGZPATH}
            -DZLIB_TGZ_NAME:STRING=${ZLIB_TGZ_NAME}
            -DSZIP_TGZ_NAME:STRING=${SZIP_TGZ_NAME}
            -DHDF5_ENABLE_Z_LIB_SUPPORT:BOOL=${HDF_ENABLE_Z_LIB_SUPPORT}
            -DHDF5_ENABLE_SZIP_SUPPORT:BOOL=${HDF_ENABLE_SZIP_SUPPORT}
            -DHDF5_ENABLE_SZIP_ENCODING:BOOL=${HDF_ENABLE_SZIP_ENCODING}
    ) 
  endif (${compress_type} MATCHES "TGZ")
  EXTERNALPROJECT_GET_PROPERTY (HDF5 BINARY_DIR SOURCE_DIR) 

  if (${CMAKE_BUILD_TYPE} MATCHES "DEBUG")
    if (WIN32)
      set (HDF5_LIB_NAME "hdf5_D")
    else (WIN32)
      set (HDF5_LIB_NAME "hdf5_debug")
    endif (WIN32)
  else (${CMAKE_BUILD_TYPE} MATCHES "DEBUG")
    set (HDF5_LIB_NAME "hdf5")
  endif (${CMAKE_BUILD_TYPE} MATCHES "DEBUG")

  # Create imported target hdf5
  ADD_LIBRARY(hdf5 ${libtype} IMPORTED)
  add_dependencies (hdf5 HDF5)

  if (${libtype} MATCHES "SHARED")
    if (WIN32)
      if (MINGW)
        SET_TARGET_PROPERTIES(hdf5 PROPERTIES
            IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${HDF5_LIB_NAME}.lib"
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${HDF5_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LIBRARIES "zlib;szip"
        )
      else (MINGW)
        SET_TARGET_PROPERTIES(hdf5 PROPERTIES
            IMPORTED_IMPLIB "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${HDF5_LIB_NAME}${CMAKE_IMPORT_LIBRARY_SUFFIX}"
            IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/${CMAKE_IMPORT_LIBRARY_PREFIX}${HDF5_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
            IMPORTED_LINK_INTERFACE_LIBRARIES "zlib;szip"
        )
      endif (MINGW)
    else (WIN32)
      SET_TARGET_PROPERTIES(hdf5 PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${HDF5_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "dl;zlib;szip"
          IMPORTED_SONAME "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_SHARED_LIBRARY_PREFIX}${HDF5_LIB_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX}.${HDF5_VERSION_STRING}"
          SOVERSION "${HDF5_VERSION_STRING}"
      )
    endif (WIN32)
  else (${libtype} MATCHES "SHARED")
    if (WIN32 AND NOT MINGW)
      SET_TARGET_PROPERTIES(hdf5 PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/${CMAKE_BUILD_TYPE}/lib${HDF5_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "zlib;szip"
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
    else (WIN32 AND NOT MINGW)
      SET_TARGET_PROPERTIES(hdf5 PROPERTIES
          IMPORTED_LOCATION "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/lib${HDF5_LIB_NAME}${CMAKE_STATIC_LIBRARY_SUFFIX}"
          IMPORTED_LINK_INTERFACE_LIBRARIES "zlib;szip"
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
    endif (WIN32 AND NOT MINGW)
  endif (${libtype} MATCHES "SHARED")
  set (HDF5_LIBRARY "hdf5")
#  file (READ ${BINARY_DIR}/H5pubconf.h _h5pubconf_h_contents)
#  STRING (REGEX REPLACE ".*#define[ \t]+H5_VERSION[ \t]+\"([0-9A-Za-z.]+)\".*" "\\1" HDF5_VERSION_STRING ${_h5pubconf_h_contents})

  set (HDF5_INCLUDE_DIR_GEN "${BINARY_DIR}")
  set (HDF5_INCLUDE_DIR "${SOURCE_DIR}/src")
  set (HDF5_FOUND 1)
  set (HDF5_LIBRARIES ${HDF5_LIBRARY})
  set (HDF5_INCLUDE_DIRS ${HDF5_INCLUDE_DIR_GEN} ${HDF5_INCLUDE_DIR})
ENDMACRO (EXTERNAL_HDF5_LIBRARY)

#-------------------------------------------------------------------------------
MACRO (PACKAGE_HDF5_LIBRARY compress_type libtype)
  ADD_CUSTOM_TARGET (HDF5-GenHeader-Copy ALL
      COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_INCLUDE_DIR_GEN}/H5pubconf.h ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
      COMMENT "Copying ${HDF5_INCLUDE_DIR_GEN}/H5pubconf.h to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
  )
  set (EXTERNAL_HEADER_LIST ${EXTERNAL_HEADER_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/H5pubconf.h)
  if (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    add_dependencies (HDF5-GenHeader-Copy HDF5)
  endif (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
  if (WIN32)
    if (${libtype} MATCHES "SHARED")
      foreach (HDF5_LIB ${HDF5_LIBRARY})
        GET_PROPERTY (HDF5_DLL TARGET ${HDF5_LIB} PROPERTY LOCATION_${CMAKE_BUILD_TYPE})
        GET_FILENAME_COMPONENT(HDF5_DLL_NAME ${HDF5_DLL} NAME)
        if (BUILD_TESTING)
          ADD_CUSTOM_TARGET (${HDF5_DLL_NAME}-Test-Copy ALL
              COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_DLL} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${HDF5_DLL_NAME}
              COMMENT "Copying ${HDF5_DLL} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
          )
          add_dependencies (${HDF5_DLL_NAME}-Test-Copy HDF5)
        endif (BUILD_TESTING)
      endforeach (HDF5_LIB {HDF5_LIBRARY})
    endif (${libtype} MATCHES "SHARED")
  endif (WIN32)
ENDMACRO (PACKAGE_HDF5_LIBRARY)
