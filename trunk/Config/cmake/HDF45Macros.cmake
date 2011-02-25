#-------------------------------------------------------------------------------
MACRO (EXTERNAL_HDF4_LIBRARY compress_type libtype jpeg_pic)
  IF (${compress_type} MATCHES "SVN")
    EXTERNALPROJECT_ADD (HDF4
        SVN_REPOSITORY ${HDF4_URL}
        # [SVN_REVISION rev] 
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBLDTYPE:STRING=Release
            -DHDF_LEGACY_NAMING:BOOL=OFF
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF4_BUILD_TOOLS:BOOL=ON
            -DHDF4_BUILD_FORTRAN:BOOL=OFF
            -DHDF4_ALLOW_EXTERNAL_SUPPORT:STRING="SVN"
            -DHDF4_SVN_URL:STRING=${HDF4_SVN_URL}
            -DJPEG_SVN_URL:STRING=${JPEG_SVN_URL}
            -DZLIB_SVN_URL:STRING=${ZLIB_SVN_URL}
            -DSZIP_SVN_URL:STRING=${SZIP_SVN_URL}
            -DBUILD_JPEG_WITH_PIC:BOOL="${jpeg_pic}"
            -DHDF4_ENABLE_JPEG_SUPPORT:BOOL=${H4H5_ENABLE_JPEG_LIB_SUPPORT}
            -DHDF4_ENABLE_Z_LIB_SUPPORT:BOOL=${H4H5_ENABLE_Z_LIB_SUPPORT}
            -DHDF4_ENABLE_SZIP_SUPPORT:BOOL=${H4H5_ENABLE_SZIP_SUPPORT}
            -DHDF4_ENABLE_SZIP_ENCODING:BOOL=${H4H5_ENABLE_SZIP_ENCODING}
    ) 
  ENDIF (${compress_type} MATCHES "SVN")
  IF (${compress_type} MATCHES "TGZ")
    EXTERNALPROJECT_ADD (HDF4
        URL ${HDF4_URL}
        URL_MD5 ""
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBLDTYPE:STRING=Release
            -DHDF_LEGACY_NAMING:BOOL=OFF
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF4_BUILD_TOOLS:BOOL=ON
            -DHDF4_BUILD_FORTRAN:BOOL=OFF
            -DHDF4_ALLOW_EXTERNAL_SUPPORT:STRING="TGZ"
            -DTGZ_PATH:STRING=${TGZ_PATH}
            -DHDF4_TGZ_NAME:STRING=${HDF4_TGZ_NAME}
            -DJPEG_TGZ_NAME:STRING=${JPEG_TGZ_NAME}
            -DZLIB_TGZ_NAME:STRING=${ZLIB_TGZ_NAME}
            -DSZIP_TGZ_NAME:STRING=${SZIP_TGZ_NAME}
            -DBUILD_JPEG_WITH_PIC:BOOL="${jpeg_pic}"
            -DHDF4_ENABLE_JPEG_SUPPORT:BOOL=${H4H5_ENABLE_JPEG_LIB_SUPPORT}
            -DHDF4_ENABLE_Z_LIB_SUPPORT:BOOL=${H4H5_ENABLE_Z_LIB_SUPPORT}
            -DHDF4_ENABLE_SZIP_SUPPORT:BOOL=${H4H5_ENABLE_SZIP_SUPPORT}
            -DHDF4_ENABLE_SZIP_ENCODING:BOOL=${H4H5_ENABLE_SZIP_ENCODING}
    ) 
  ENDIF (${compress_type} MATCHES "TGZ")
  EXTERNALPROJECT_GET_PROPERTY (HDF4 BINARY_DIR SOURCE_DIR) 

  IF (${libtype} MATCHES "SHARED")
    IF (WIN32 AND NOT MINGW)
      SET (HDF4_LIBRARY
          "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/${CMAKE_IMPORT_LIBRARY_PREFIX}hdf${CMAKE_IMPORT_LIBRARY_SUFFIX}"
          "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/${CMAKE_IMPORT_LIBRARY_PREFIX}mfhdf${CMAKE_IMPORT_LIBRARY_SUFFIX}"
          "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/${CMAKE_IMPORT_LIBRARY_PREFIX}xdr${CMAKE_IMPORT_LIBRARY_SUFFIX}"
      )
    ELSE (WIN32 AND NOT MINGW)
      SET (HDF4_LIBRARY
          "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/${CMAKE_SHARED_LIBRARY_PREFIX}hdf${CMAKE_SHARED_LIBRARY_SUFFIX}"
          "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/${CMAKE_SHARED_LIBRARY_PREFIX}mfhdf${CMAKE_SHARED_LIBRARY_SUFFIX}"
      )
    ENDIF (WIN32 AND NOT MINGW)
  ELSE (${libtype} MATCHES "SHARED")
    SET (HDF4_LIBRARY
        "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/libhdf${CMAKE_STATIC_LIBRARY_SUFFIX}"
        "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/libmfhdf${CMAKE_STATIC_LIBRARY_SUFFIX}"
    )
    IF (WIN32 AND NOT MINGW)
      SET (HDF4_LIBRARY ${HDF4_LIBRARY} "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/libxdr${CMAKE_STATIC_LIBRARY_SUFFIX}")
    ENDIF (WIN32 AND NOT MINGW)
  ENDIF (${libtype} MATCHES "SHARED")
  SET (HDF4_INCLUDE_DIR_GEN "${BINARY_DIR}")
  SET (HDF4_HDF_INCLUDE_DIR "${SOURCE_DIR}/hdf/src")
  SET (HDF4_MFHDF_INCLUDE_DIR "${SOURCE_DIR}/mfhdf/libsrc")
  SET (HDF4_INCLUDE_DIR ${HDF4_HDF_INCLUDE_DIR} ${HDF4_MFHDF_INCLUDE_DIR})
  SET (HDF4_FOUND 1)
  SET (HDF4_LIBRARIES ${HDF4_LIBRARY})
  SET (HDF4_INCLUDE_DIRS ${HDF4_INCLUDE_DIR_GEN} ${HDF4_INCLUDE_DIR})
  ADD_EXECUTABLE (hdiff IMPORTED)
  IF (${libtype} MATCHES "SHARED" AND WIN32)
    SET_PROPERTY (TARGET hdiff PROPERTY IMPORTED_LOCATION "${BINARY_DIR}/bin/hdiffdll")
  ELSE (${libtype} MATCHES "SHARED" AND WIN32)
    SET_PROPERTY (TARGET hdiff PROPERTY IMPORTED_LOCATION "${BINARY_DIR}/bin/hdiff")
  ENDIF (${libtype} MATCHES "SHARED" AND WIN32)
  SET (HDF4_DIFF_EXECUTABLE $<TARGET_FILE:hdiff>)
ENDMACRO (EXTERNAL_HDF4_LIBRARY)

#-------------------------------------------------------------------------------
MACRO (PACKAGE_HDF4_LIBRARY compress_type libtype)
  FOREACH (HDF4_LIB ${HDF4_LIBRARY})
    GET_FILENAME_COMPONENT(HDF4_LIB_NAME ${HDF4_LIB} NAME)
    ADD_CUSTOM_TARGET (${HDF4_LIB_NAME}-Library-Copy ALL
        COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_LIB} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
        COMMENT "Copying ${HDF4_LIB} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
    )
    SET (EXTERNAL_LIBRARY_LIST ${EXTERNAL_LIBRARY_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${HDF4_LIB_NAME})
    IF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
      ADD_DEPENDENCIES (${HDF4_LIB_NAME}-Library-Copy HDF4)
    ENDIF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
  ENDFOREACH (HDF4_LIB {HDF4_LIBRARY})
  ADD_CUSTOM_TARGET (HDF4-GenHeader-Copy ALL
      COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_INCLUDE_DIR_GEN}/h4config.h ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
      COMMENT "Copying ${HDF4_INCLUDE_DIR_GEN}/h4config.h to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
  )
  SET (EXTERNAL_HEADER_LIST ${EXTERNAL_HEADER_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/h4config.h)
  IF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    ADD_DEPENDENCIES (HDF4-GenHeader-Copy HDF4)
  ENDIF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
  IF (WIN32 AND NOT CYGWIN)
    IF (${libtype} MATCHES "SHARED")
      SET (EXTERNAL_LIBRARYDLL_LIST ${EXTERNAL_LIBRARYDLL_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${HDF4_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX})
      ADD_CUSTOM_TARGET (${HDF4_DLL_NAME}-Dll-Copy ALL
          COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF4_BIN_PATH}/${HDF4_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
          COMMENT "Copying ${HDF4_BIN_PATH}/${HDF4_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
      )
      IF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
        ADD_DEPENDENCIES (${HDF4_DLL_NAME}-Dll-Copy HDF4)
      ENDIF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    ENDIF (${libtype} MATCHES "SHARED")
  ENDIF (WIN32 AND NOT CYGWIN)
ENDMACRO (PACKAGE_HDF4_LIBRARY)

#-------------------------------------------------------------------------------
MACRO (EXTERNAL_HDF5_LIBRARY compress_type libtype)
  IF (${compress_type} MATCHES "SVN")
    EXTERNALPROJECT_ADD (HDF5
        SVN_REPOSITORY ${HDF5_URL}
        # [SVN_REVISION rev] 
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBLDTYPE:STRING=Release
            -DHDF_LEGACY_NAMING:BOOL=OFF
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF5_BUILD_TOOLS:BOOL=ON
            -DHDF5_ALLOW_EXTERNAL_SUPPORT:STRING="SVN"
            -DHDF5_SVN_URL:STRING=${HDF5_SVN_URL}
            -DZLIB_SVN_URL:STRING=${ZLIB_SVN_URL}
            -DSZIP_SVN_URL:STRING=${SZIP_SVN_URL}
            -DHDF5_ENABLE_Z_LIB_SUPPORT:BOOL=${H4H5_ENABLE_Z_LIB_SUPPORT}
            -DHDF5_ENABLE_SZIP_SUPPORT:BOOL=${H4H5_ENABLE_SZIP_SUPPORT}
            -DHDF5_ENABLE_SZIP_ENCODING:BOOL=${H4H5_ENABLE_SZIP_ENCODING}
    ) 
  ENDIF (${compress_type} MATCHES "SVN")
  IF (${compress_type} MATCHES "TGZ")
    EXTERNALPROJECT_ADD (HDF5
        URL ${HDF5_URL}
        URL_MD5 ""
        INSTALL_COMMAND ""
        CMAKE_ARGS
            -DBLDTYPE:STRING=Release
            -DHDF_LEGACY_NAMING:BOOL=OFF
            -DBUILD_SHARED_LIBS:BOOL=${BUILD_SHARED_LIBS}
            -DHDF5_BUILD_TOOLS:BOOL=ON
            -DHDF5_ALLOW_EXTERNAL_SUPPORT:STRING="TGZ"
            -DTGZ_PATH:STRING=${TGZ_PATH}
            -DHDF5_TGZ_NAME:STRING=${HDF5_TGZ_NAME}
            -DZLIB_TGZ_NAME:STRING=${ZLIB_TGZ_NAME}
            -DSZIP_TGZ_NAME:STRING=${SZIP_TGZ_NAME}
            -DHDF5_ENABLE_Z_LIB_SUPPORT:BOOL=${H4H5_ENABLE_Z_LIB_SUPPORT}
            -DHDF5_ENABLE_SZIP_SUPPORT:BOOL=${H4H5_ENABLE_SZIP_SUPPORT}
            -DHDF5_ENABLE_SZIP_ENCODING:BOOL=${H4H5_ENABLE_SZIP_ENCODING}
    ) 
  ENDIF (${compress_type} MATCHES "TGZ")
  EXTERNALPROJECT_GET_PROPERTY (HDF5 BINARY_DIR SOURCE_DIR) 

  IF (${libtype} MATCHES "SHARED")
    IF (WIN32 AND NOT MINGW)
      SET (HDF5_LIBRARY "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/${CMAKE_IMPORT_LIBRARY_PREFIX}hdf5${CMAKE_IMPORT_LIBRARY_SUFFIX}")
    ELSE (WIN32 AND NOT MINGW)
      SET (HDF5_LIBRARY "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/${CMAKE_SHARED_LIBRARY_PREFIX}hdf5${CMAKE_SHARED_LIBRARY_SUFFIX}")
    ENDIF (WIN32 AND NOT MINGW)
  ELSE (${libtype} MATCHES "SHARED")
    SET (HDF5_LIBRARY "${BINARY_DIR}/bin/${CMAKE_CFG_INTDIR}/libhdf5${CMAKE_STATIC_LIBRARY_SUFFIX}")
  ENDIF (${libtype} MATCHES "SHARED")
  SET (HDF5_INCLUDE_DIR_GEN "${BINARY_DIR}")
  SET (HDF5_INCLUDE_DIR "${SOURCE_DIR}/src")
  SET (HDF5_FOUND 1)
  SET (HDF5_LIBRARIES ${HDF5_LIBRARY})
  SET (HDF5_INCLUDE_DIRS ${HDF5_INCLUDE_DIR_GEN} ${HDF5_INCLUDE_DIR})
  ADD_EXECUTABLE (h5diff IMPORTED)
  IF (${libtype} MATCHES "SHARED" AND WIN32)
    SET_PROPERTY (TARGET h5diff PROPERTY IMPORTED_LOCATION "${BINARY_DIR}/bin/h5diffdll")
  ELSE (${libtype} MATCHES "SHARED" AND WIN32)
    SET_PROPERTY (TARGET h5diff PROPERTY IMPORTED_LOCATION "${BINARY_DIR}/bin/h5diff")
  ENDIF (${libtype} MATCHES "SHARED" AND WIN32)
  SET (HDF5_DIFF_EXECUTABLE $<TARGET_FILE:h5diff>)
ENDMACRO (EXTERNAL_HDF5_LIBRARY)

#-------------------------------------------------------------------------------
MACRO (PACKAGE_HDF5_LIBRARY compress_type libtype)
  ADD_CUSTOM_TARGET (HDF5-GenHeader-Copy ALL
      COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_INCLUDE_DIR_GEN}/H5pubconf.h ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
      COMMENT "Copying ${HDF5_INCLUDE_DIR_GEN}/H5pubconf.h to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
  )
  SET (EXTERNAL_HEADER_LIST ${EXTERNAL_HEADER_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/H5pubconf.h)
  ADD_CUSTOM_TARGET (HDF5-Library-Copy ALL
      COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_LIBRARY} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
      COMMENT "Copying ${HDF5_LIBRARY} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
  )
  GET_FILENAME_COMPONENT(HDF5_LIB_NAME ${HDF5_LIBRARY} NAME)
  SET (EXTERNAL_LIBRARY_LIST ${EXTERNAL_LIBRARY_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${HDF5_LIB_NAME})
  IF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    ADD_DEPENDENCIES (HDF5-GenHeader-Copy HDF5)
    ADD_DEPENDENCIES (HDF5-Library-Copy HDF5)
  ENDIF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
  IF (WIN32 AND NOT CYGWIN)
    IF (${libtype} MATCHES "SHARED")
      SET (EXTERNAL_LIBRARYDLL_LIST ${EXTERNAL_LIBRARYDLL_LIST} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/${HDF5_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX})
      ADD_CUSTOM_TARGET (HDF5-Dll-Copy ALL
          COMMAND ${CMAKE_COMMAND} -E copy_if_different ${HDF5_BIN_PATH}/${HDF5_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/
          COMMENT "Copying ${HDF5_BIN_PATH}/${HDF5_DLL_NAME}${CMAKE_SHARED_LIBRARY_SUFFIX} to ${CMAKE_RUNTIME_OUTPUT_DIRECTORY}/"
      )
      IF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
        ADD_DEPENDENCIES (HDF5-Dll-Copy HDF5)
      ENDIF (${compress_type} MATCHES "SVN" OR ${compress_type} MATCHES "TGZ")
    ENDIF (${libtype} MATCHES "SHARED")
  ENDIF (WIN32 AND NOT CYGWIN)
ENDMACRO (PACKAGE_HDF5_LIBRARY)
