#-------------------------------------------------------------------------------
MACRO (HDFJAVA_SET_LIB_OPTIONS libtarget libname libtype)
  HDF_SET_LIB_OPTIONS (${libtarget} ${libname} ${libtype})
  #message (STATUS "Target: ${libtarget} is ${libname} libtype: ${libtype}")
  if (${libtype} MATCHES "SHARED")
    if (WIN32)
      set (LIBHDF_VERSION ${HDFJAVA_PACKAGE_VERSION_MAJOR})
    else (WIN32)
      set (LIBHDF_VERSION ${HDFJAVA_PACKAGE_VERSION})
    endif (WIN32)
    #message (STATUS "Version: ${LIBHDF_VERSION}")
    set_target_properties (${libtarget} PROPERTIES VERSION ${LIBHDF_VERSION})
    set_target_properties (${libtarget} PROPERTIES SOVERSION ${LIBHDF_VERSION})
  endif (${libtype} MATCHES "SHARED")

  #-- Apple Specific install_name for libraries
  if (APPLE)
    option (HDF_BUILD_WITH_INSTALL_NAME "Build with library install_name set to the installation path" OFF)
    if (HDF_BUILD_WITH_INSTALL_NAME)
      set_target_properties (${libtarget} PROPERTIES
          LINK_FLAGS "-current_version ${HDFJAVA_PACKAGE_VERSION} -compatibility_version ${HDFJAVA_PACKAGE_VERSION}"
          INSTALL_NAME_DIR "${CMAKE_INSTALL_PREFIX}/lib"
          BUILD_WITH_INSTALL_RPATH ${HDF_BUILD_WITH_INSTALL_NAME}
      )
    endif (HDF_BUILD_WITH_INSTALL_NAME)
  endif (APPLE)

ENDMACRO (HDFJAVA_SET_LIB_OPTIONS)
