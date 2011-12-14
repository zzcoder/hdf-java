#-------------------------------------------------------------------------------
MACRO (HDFJAVA_SET_LIB_OPTIONS libtarget libname libtype)
  HDF_SET_LIB_OPTIONS (${libtarget} ${libname} ${libtype})
  #message (STATUS "Target: ${libtarget} is ${libname} libtype: ${libtype}")
  IF (${libtype} MATCHES "SHARED")
    IF (WIN32)
      SET (LIBHDF_VERSION ${HDFJAVA_PACKAGE_VERSION_MAJOR})
    ELSE (WIN32)
      SET (LIBHDF_VERSION ${HDFJAVA_PACKAGE_VERSION})
    ENDIF (WIN32)
    #message (STATUS "Version: ${LIBHDF_VERSION}")
    SET_TARGET_PROPERTIES (${libtarget} PROPERTIES VERSION ${LIBHDF_VERSION})
    SET_TARGET_PROPERTIES (${libtarget} PROPERTIES SOVERSION ${LIBHDF_VERSION})
  ENDIF (${libtype} MATCHES "SHARED")

  #-- Apple Specific install_name for libraries
  IF (APPLE)
    OPTION (HDF_BUILD_WITH_INSTALL_NAME "Build with library install_name set to the installation path" OFF)
    IF (HDF_BUILD_WITH_INSTALL_NAME)
      SET_TARGET_PROPERTIES (${libtarget} PROPERTIES
          LINK_FLAGS "-current_version ${HDFJAVA_PACKAGE_VERSION} -compatibility_version ${HDFJAVA_PACKAGE_VERSION}"
          INSTALL_NAME_DIR "${CMAKE_INSTALL_PREFIX}/lib"
          BUILD_WITH_INSTALL_RPATH ${HDF_BUILD_WITH_INSTALL_NAME}
      )
    ENDIF (HDF_BUILD_WITH_INSTALL_NAME)
  ENDIF (APPLE)

ENDMACRO (HDFJAVA_SET_LIB_OPTIONS)

