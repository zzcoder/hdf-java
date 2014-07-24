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
  externalproject_get_property (HDF4 BINARY_DIR SOURCE_DIR) 

  # Create imported target hdf4
  add_library (hdf ${libtype} IMPORTED)
  HDF_IMPORT_SET_LIB_OPTIONS (hdf "hdf" ${libtype} "")
  add_library (mfhdf ${libtype} IMPORTED)
  HDF_IMPORT_SET_LIB_OPTIONS (mfhdf "mfhdf" ${libtype} "")
  add_dependencies (HDF4 hdf mfhdf)
  set (MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES "hdf")
  if (HDF45_BUILD_XDR_LIB)
    add_library (xdr ${libtype} IMPORTED)
    HDF_IMPORT_SET_LIB_OPTIONS (xdr "xdr" ${libtype} "")
    add_dependencies (HDF4 xdr)
    set (MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES "xdr;${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}")
  endif (HDF45_BUILD_XDR_LIB)
  if (HDF_ENABLE_JPEG_LIB_SUPPORT)
    add_library (jpeg ${libtype} IMPORTED)
    HDF_IMPORT_SET_LIB_OPTIONS (jpeg "jpeg" ${libtype} "")
    add_dependencies (HDF4 jpeg)
  endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
  if (HDF_ENABLE_Z_LIB_SUPPORT)
    if (NOT ZLIB_LIBRARY)
      add_library (zlib ${libtype} IMPORTED)
      if (WIN32)
        set (ZLIB_LIB_NAME "zlib")
      else (WIN32)
        set (ZLIB_LIB_NAME "z")
      endif (WIN32)
      HDF_IMPORT_SET_LIB_OPTIONS (zlib ${ZLIB_LIB_NAME} ${libtype} "")
      set (HDF4_ZLIB "TRUE")
    endif (NOT ZLIB_LIBRARY)
    add_dependencies (HDF4 zlib)
  endif (HDF_ENABLE_Z_LIB_SUPPORT)
  if (HDF_ENABLE_SZIP_SUPPORT)
    if (NOT SZIP_LIBRARY)
      add_library (szip ${libtype} IMPORTED)
      HDF_IMPORT_SET_LIB_OPTIONS (szip "szip" ${libtype} "")
      set (HDF4_SZIP "TRUE")
    endif (NOT SZIP_LIBRARY)
    add_dependencies (HDF4 szip)
  endif (HDF_ENABLE_SZIP_SUPPORT)

  if (${libtype} MATCHES "SHARED")
    set_target_properties (hdf PROPERTIES
        IMPORTED_LINK_INTERFACE_LIBRARIES "jpeg;zlib;szip"
    )
    set_target_properties (mfhdf PROPERTIES
        IMPORTED_LINK_INTERFACE_LIBRARIES "${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}"
    )
  else (${libtype} MATCHES "SHARED")
    set_target_properties (hdf PROPERTIES
        IMPORTED_LINK_INTERFACE_LIBRARIES "jpeg;zlib;szip"
        IMPORTED_LINK_INTERFACE_LANGUAGES "C"
    )
    set_target_properties (mfhdf PROPERTIES
        IMPORTED_LINK_INTERFACE_LIBRARIES "${MFHDF_IMPORTED_LINK_INTERFACE_LIBRARIES}"
        IMPORTED_LINK_INTERFACE_LANGUAGES "C"
    )
    if (HDF45_BUILD_XDR_LIB)
      set_target_properties (xdr PROPERTIES
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
    )
    endif (HDF45_BUILD_XDR_LIB)
    if (HDF_ENABLE_JPEG_LIB_SUPPORT)
      set_target_properties (jpeg PROPERTIES
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
    endif (HDF_ENABLE_JPEG_LIB_SUPPORT)
    if (HDF_ENABLE_Z_LIB_SUPPORT)
      set_target_properties (zlib PROPERTIES
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
    endif (HDF_ENABLE_Z_LIB_SUPPORT)
    if (HDF_ENABLE_SZIP_SUPPORT)
      set_target_properties (szip PROPERTIES
          IMPORTED_LINK_INTERFACE_LANGUAGES "C"
      )
    endif (HDF_ENABLE_SZIP_SUPPORT)
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
  if (HDF_ENABLE_Z_LIB_SUPPORT AND HDF4_ZLIB)
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
  endif (HDF_ENABLE_Z_LIB_SUPPORT AND HDF4_ZLIB)
  if (HDF_ENABLE_SZIP_SUPPORT AND HDF4_SZIP)
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
  endif (HDF_ENABLE_SZIP_SUPPORT AND HDF4_SZIP)
#  file (READ ${BINARY_DIR}/h4config.h _h4config_h_contents)
#  string (REGEX REPLACE ".*#define[ \t]+H4_VERSION[ \t]+\"([0-9A-Za-z.]+)\".*" "\\1" HDF4_VERSION_STRING ${_h4config_h_contents})
  
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
  add_custom_target (HDF4-GenHeader-Copy ALL
      COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_INCLUDE_DIR_GEN}/h4config.h ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
      COMMENT "Copying ${HDF4_INCLUDE_DIR_GEN}/h4config.h to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
  )
  set (EXTERNAL_HEADER_LIST ${EXTERNAL_HEADER_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/h4config.h)
  if (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    add_dependencies (HDF4-GenHeader-Copy HDF4)
  endif (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
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
  externalproject_get_property (HDF5 BINARY_DIR SOURCE_DIR) 

  # Create imported target hdf5
  add_library (hdf5 ${libtype} IMPORTED)
  HDF_IMPORT_SET_LIB_OPTIONS (hdf5 "hdf5" ${libtype} "")
  add_dependencies (HDF5 hdf5)
  if (HDF_ENABLE_Z_LIB_SUPPORT)
    if (NOT ZLIB_LIBRARY)
      add_library (zlib ${libtype} IMPORTED)
  HDF_IMPORT_SET_LIB_OPTIONS (zlib "zlib" ${libtype} "")
      set (HDF5_ZLIB "TRUE")
    endif (NOT ZLIB_LIBRARY)
    add_dependencies (HDF5 zlib)
  endif (HDF_ENABLE_Z_LIB_SUPPORT)
  if (HDF_ENABLE_SZIP_SUPPORT)
    if (NOT SZIP_LIBRARY)
      add_library (szip ${libtype} IMPORTED)
      HDF_IMPORT_SET_LIB_OPTIONS (szip "szip" ${libtype} "")
      set (HDF5_SZIP "TRUE")
    endif (NOT SZIP_LIBRARY)
    add_dependencies (HDF5 szip)
  endif (HDF_ENABLE_SZIP_SUPPORT)

  if (${libtype} MATCHES "SHARED")
    if (WIN32)
      set_target_properties (hdf5 PROPERTIES
          IMPORTED_LINK_INTERFACE_LIBRARIES "zlib;szip"
      )
    else (WIN32)
      set_target_properties (hdf5 PROPERTIES
          IMPORTED_LINK_INTERFACE_LIBRARIES "dl;zlib;szip"
      )
    endif (WIN32)
  else (${libtype} MATCHES "SHARED")
    set_target_properties (hdf5 PROPERTIES
        IMPORTED_LINK_INTERFACE_LIBRARIES "zlib;szip"
        IMPORTED_LINK_INTERFACE_LANGUAGES "C"
    )
  endif (${libtype} MATCHES "SHARED")
  set (HDF5_LIBRARY "hdf5")
  if (HDF_ENABLE_Z_LIB_SUPPORT AND HDF5_ZLIB)
    set (HDF5_LIBRARY "${HDF5_LIBRARY};zlib")
    set (ZLIB_LIBRARY "zlib")
    set (ZLIB_INCLUDE_DIR "${BINARY_DIR}/ZLIB-prefix/src/ZLIB/src")
    set (ZLIB_INCLUDE_DIR_GEN "${BINARY_DIR}/ZLIB-prefix/src/ZLIB-build")
    set (H425_HAVE_FILTER_DEFLATE 1)
    set (H425_HAVE_ZLIB_H 1)
    set (H425_HAVE_LIBZ 1)
    set (H425_ZLIB_HEADER "zlib.h")
    set (ZLIB_LIBRARIES ${ZLIB_LIBRARY})
    set (ZLIB_INCLUDE_DIRS ${ZLIB_INCLUDE_DIR_GEN} ${ZLIB_INCLUDE_DIR})
   endif (HDF_ENABLE_Z_LIB_SUPPORT AND HDF5_ZLIB)
  if (HDF_ENABLE_SZIP_SUPPORT AND HDF5_SZIP)
    set (HDF5_LIBRARY "${HDF5_LIBRARY};szip")
    set (SZIP_LIBRARY "szip")
    set (SZIP_INCLUDE_DIR "${BINARY_DIR}/SZIP-prefix/src/SZIP/src")
    set (SZIP_INCLUDE_DIR_GEN "${BINARY_DIR}/SZIP-prefix/src/SZIP-build")
    set (H425_HAVE_FILTER_SZIP 1)
    set (H425_HAVE_SZLIB_H 1)
    set (H425_HAVE_LIBSZ 1)
    set (SZIP_LIBRARIES ${SZIP_LIBRARY})
    set (SZIP_INCLUDE_DIRS ${SZIP_INCLUDE_DIR_GEN} ${SZIP_INCLUDE_DIR})
  endif (HDF_ENABLE_SZIP_SUPPORT AND HDF5_SZIP)
#  file (READ ${BINARY_DIR}/H5pubconf.h _h5pubconf_h_contents)
#  string (REGEX REPLACE ".*#define[ \t]+H5_VERSION[ \t]+\"([0-9A-Za-z.]+)\".*" "\\1" HDF5_VERSION_STRING ${_h5pubconf_h_contents})

  set (HDF5_INCLUDE_DIR_GEN "${BINARY_DIR}")
  set (HDF5_INCLUDE_DIR "${SOURCE_DIR}/src")
  set (HDF5_FOUND 1)
  message ("HDF5_LIBRARY is ${HDF5_LIBRARY}\n")
  set (HDF5_LIBRARIES ${HDF5_LIBRARY})
  set (HDF5_INCLUDE_DIRS ${HDF5_INCLUDE_DIR_GEN} ${HDF5_INCLUDE_DIR})
ENDMACRO (EXTERNAL_HDF5_LIBRARY)

#-------------------------------------------------------------------------------
MACRO (PACKAGE_HDF5_LIBRARY compress_type libtype)
  add_custom_target (HDF5-GenHeader-Copy ALL
      COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_INCLUDE_DIR_GEN}/H5pubconf.h ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
      COMMENT "Copying ${HDF5_INCLUDE_DIR_GEN}/H5pubconf.h to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
  )
  set (EXTERNAL_HEADER_LIST ${EXTERNAL_HEADER_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/H5pubconf.h)
  if (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    add_dependencies (HDF5-GenHeader-Copy HDF5)
  endif (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
ENDMACRO (PACKAGE_HDF5_LIBRARY)
