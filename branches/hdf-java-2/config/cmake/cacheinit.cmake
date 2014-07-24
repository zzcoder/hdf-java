# This is the CMakeCache file.

########################
# EXTERNAL cache entries
########################

set (BUILD_SHARED_LIBS OFF CACHE BOOL "Build Shared Libraries" FORCE)

set (USE_SHARED_LIBS OFF CACHE BOOL "Use Shared Libraries" FORCE)

set (BUILD_TESTING ON CACHE BOOL "Build HDFJAVA Unit Testing" FORCE)

set (HDF_PACKAGE_EXT "" CACHE STRING "Name of HDF package extension" FORCE)

set (HDF_BUILD_EXAMPLES ON CACHE BOOL "Build HDFJAVA Library Examples" FORCE)

set (HDF_ENABLE_JPEG_LIB_SUPPORT ON CACHE BOOL "Use Jpeg library" FORCE)

set (HDF_ENABLE_Z_LIB_SUPPORT ON CACHE BOOL "Enable Zlib Filters" FORCE)

set (HDF_ENABLE_SZIP_SUPPORT ON CACHE BOOL "Use SZip Filter" FORCE)

set (HDF_ENABLE_SZIP_ENCODING ON CACHE BOOL "Use SZip Encoding" FORCE)

set (HDF_DISABLE_COMPILER_WARNINGS OFF CACHE BOOL "Disable compiler warnings" FORCE)

set (HDF_ENABLE_PARALLEL OFF CACHE BOOL "Enable parallel build (requires MPI)" FORCE)

set (HDF_ENABLE_COVERAGE OFF CACHE BOOL "Enable code coverage for Libraries and Programs" FORCE)

set (HDF_SKIP_GUI_TESTS OFF CACHE BOOL "Skip testing of GUI" FORCE)

set (HDF_PACKAGE_EXTLIBS OFF CACHE BOOL "(WINDOWS)CPACK - include external libraries" FORCE)

set (HDF_ALLOW_EXTERNAL_SUPPORT "NO" CACHE STRING "Allow External Library Building (NO SVN TGZ)" FORCE)
set_property (CACHE HDF_ALLOW_EXTERNAL_SUPPORT PROPERTY STRINGS NO SVN TGZ)

set (ZLIB_SVN_URL "http://svn.hdfgroup.uiuc.edu/zlib/trunk" CACHE STRING "Use ZLib from HDF repository" FORCE)

set (SZIP_SVN_URL "http://svn.hdfgroup.uiuc.edu/szip/trunk" CACHE STRING "Use SZip from HDF repository" FORCE)

set (JPEG_SVN_URL "http://svn.hdfgroup.uiuc.edu/jpeg/branches/jpeg8b" CACHE STRING "Use JPEG from HDF repository" FORCE)

set (HDF4_SVN_URL "http://svn.hdfgroup.uiuc.edu/hdf4/trunk" CACHE STRING "Use HDF4 from HDF repository" FORCE)

set (HDF5_SVN_URL "http://svn.hdfgroup.uiuc.edu/hdf5/branches/hdf5_1_8" CACHE STRING "Use HDF5 from HDF repository" FORCE)

set (ZLIB_TGZ_NAME "ZLib.tar.gz" CACHE STRING "Use ZLib from compressed file" FORCE)

set (SZIP_TGZ_NAME "SZip.tar.gz" CACHE STRING "Use SZip from compressed file" FORCE)

set (JPEG_TGZ_NAME "JPEG8b.tar.gz" CACHE STRING "Use JPEG from compressed file" FORCE)

set (HDF4_TGZ_NAME "HDF4.tar.gz" CACHE STRING "Use HDF4 from compressed file" FORCE)

set (HDF5_TGZ_NAME "HDF5.tar.gz" CACHE STRING "Use HDF5 from compressed file" FORCE)

set (HDF4_PACKAGE_NAME "hdf4" CACHE STRING "Name of HDF4 package" FORCE)

set (HDF5_PACKAGE_NAME "hdf5" CACHE STRING "Name of HDF5 package" FORCE)

set (ZLIB_PACKAGE_NAME "zlib" CACHE STRING "Name of ZLIB package" FORCE)

set (SZIP_PACKAGE_NAME "szip" CACHE STRING "Name of SZIP package" FORCE)

set (JPEG_PACKAGE_NAME "jpeg" CACHE STRING "Name of JPEG package" FORCE)
