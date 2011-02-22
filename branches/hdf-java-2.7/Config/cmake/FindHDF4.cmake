# - Find HDF4, a library for reading and writing self describing array data.
#
# This module invokes the HDF4 wrapper compiler that should be installed
# alongside HDF4.  Depending upon the HDF4 Configuration, the wrapper compiler
# is called either h5cc or h5pcc.  If this succeeds, the module will then call
# the compiler with the -show argument to see what flags are used when compiling
# an HDF4 client application.
#
# The module will optionally accept the COMPONENTS argument.  If no COMPONENTS
# are specified, then the find module will default to finding only the HDF4 C
# library.  If one or more COMPONENTS are specified, the module will attempt to
# find the language bindings for the specified components.  Currently, the only
# valid components are C and CXX.  The module does not yet support finding the
# Fortran bindings.  If the COMPONENTS argument is not given, the module will
# attempt to find only the C bindings.
#
# On UNIX systems, this module will read the variable HDF4_USE_STATIC_LIBRARIES
# to determine whether or not to prefer a static link to a dynamic link for HDF4
# and all of it's dependencies.  To use this feature, make sure that the
# HDF4_USE_STATIC_LIBRARIES variable is set before the call to find_package.
#
# To provide the module with a hint about where to find your HDF4 installation,
# you can set the environment variable HDF4_ROOT.  The Find module will then
# look in this path when searching for HDF4 executables, paths, and libraries.
#
# In addition to finding the includes and libraries required to compile an HDF4
# client application, this module also makes an effort to find tools that come
# with the HDF4 distribution that may be useful for regression testing.
# 
# This module will define the following variables:
#  HDF4_INCLUDE_DIRS - Location of the HDF4 includes
#  HDF4_INCLUDE_DIR - Location of the HDF4 includes (deprecated)
#  HDF4_DEFINITIONS - Required compiler definitions for HDF4
#  HDF4_LIBRARIES - Required libraries for all requested bindings
#  HDF4_Fortran_LIBRARIES - Required fortran libraries for all requested bindings
#  HDF4_FOUND - true if HDF4 was found on the system
#  HDF4_LIBRARY_DIRS - the full set of library directories
#  HDF4_IS_PARALLEL - Whether or not HDF4 was found with parallel IO support
#  HDF4_C_COMPILER_EXECUTABLE - the path to the HDF4 C wrapper compiler
#  HDF4_Fortran_COMPILER_EXECUTABLE - the path to the HDF4 Fortran wrapper compiler
#  HDF4_DIFF_EXECUTABLE - the path to the HDF4 dataset comparison tool

#=============================================================================
# Copyright 2010 HDFGroup
#
# Distributed under the OSI-approved BSD License (the "License");
# see accompanying file Copyright.txt for details.
#
# This software is distributed WITHOUT ANY WARRANTY; without even the
# implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the License for more information.
#=============================================================================
# (To distributed this file outside of CMake, substitute the full
#  License text for the above reference.)

include(SelectLibraryConfigurations)
include(FindPackageHandleStandardArgs)

# The HINTS option should only be used for values computed from the system.
SET(_HDF4_HINTS
    $ENV{HOME}/.local
    $ENV{HDF4_ROOT}
)
# Hard-coded guesses should still go in PATHS. This ensures that the user
# environment can always override hard guesses.
SET(_HDF4_PATHS
    /usr/lib/hdf4
    /usr/share/hdf4
    /usr/local/hdf4
    /usr/local/hdf4/share
    $ENV{HOME}/.local
    $ENV{HDF4_ROOT}
)
    
# find the HDF4 include directories
find_path( HDF4_INCLUDE_DIRS hdf.h
    HINTS ${_HDF4_HINTS}
    PATHS ${_HDF4_PATHS}
    PATH_SUFFIXES
        include
        Include
)

# List of the valid HDF4 components
set( HDF4_VALID_COMPONENTS 
    C
)

# try to find the HDF4 wrapper compilers
find_program( HDF4_C_COMPILER_EXECUTABLE
    NAMES h4cc h4pcc
    HINTS ${_HDF4_HINTS}
    PATHS ${_HDF4_PATHS}
    PATH_SUFFIXES bin Bin
    DOC "HDF4 Wrapper compiler.  Used only to detect HDF4 compile flags." )
mark_as_advanced( HDF4_C_COMPILER_EXECUTABLE )

find_program( HDF4_DIFF_EXECUTABLE 
    NAMES hdiff
    HINTS ${_HDF4_HINTS}
    PATHS ${_HDF4_PATHS}
    PATH_SUFFIXES bin Bin 
    DOC "HDF4 file differencing tool." )
mark_as_advanced( HDF4_DIFF_EXECUTABLE )

# Invoke the HDF4 wrapper compiler.  The compiler return value is stored to the
# return_value argument, the text output is stored to the output variable.
macro( _HDF4_invoke_compiler language output return_value )
    if( HDF4_${language}_COMPILER_EXECUTABLE )
        exec_program( ${HDF4_${language}_COMPILER_EXECUTABLE} 
            ARGS -show
            OUTPUT_VARIABLE ${output}
            RETURN_VALUE ${return_value}
        )
        if( ${${return_value}} EQUAL 0 )
            # do nothing
        else()
            message( STATUS 
              "Unable to determine HDF4 ${language} flags from HDF4 wrapper." )
        endif()
    endif()
endmacro()

# Parse a compile line for definitions, includes, library paths, and libraries.
macro( _HDF4_parse_compile_line 
    compile_line_var
    include_paths
    definitions
    library_paths
    libraries )

    # Match the include paths
    string( REGEX MATCHALL "-I([^\" ]+)" include_path_flags 
        "${${compile_line_var}}"
    )
    foreach( IPATH ${include_path_flags} )
        string( REGEX REPLACE "^-I" "" IPATH ${IPATH} )
        string( REGEX REPLACE "//" "/" IPATH ${IPATH} )
        list( APPEND ${include_paths} ${IPATH} )
    endforeach()

    # Match the definitions
    string( REGEX MATCHALL "-D[^ ]*" definition_flags "${${compile_line_var}}" )
    foreach( DEF ${definition_flags} )
        list( APPEND ${definitions} ${DEF} )
    endforeach()

    # Match the library paths
    string( REGEX MATCHALL "-L([^\" ]+|\"[^\"]+\")" library_path_flags
        "${${compile_line_var}}"
    )
    
    foreach( LPATH ${library_path_flags} )
        string( REGEX REPLACE "^-L" "" LPATH ${LPATH} )
        string( REGEX REPLACE "//" "/" LPATH ${LPATH} )
        list( APPEND ${library_paths} ${LPATH} )
    endforeach()

    # now search for the library names specified in the compile line (match -l...)
    # match only -l's preceded by a space or comma
    # this is to exclude directory names like xxx-linux/
    string( REGEX MATCHALL "[, ]-l([^\", ]+)" library_name_flags
        "${${compile_line_var}}" )
    # strip the -l from all of the library flags and add to the search list
    foreach( LIB ${library_name_flags} )
        string( REGEX REPLACE "^[, ]-l" "" LIB ${LIB} )
        list( APPEND ${libraries} ${LIB} )
    endforeach()
endmacro()

if( HDF4_INCLUDE_DIRS AND HDF4_LIBRARIES )
    # Do nothing: we already have HDF4_INCLUDE_PATH and HDF4_LIBRARIES in the
    # cache, it would be a shame to override them
else()
    _HDF4_invoke_compiler( C HDF4_C_COMPILE_LINE HDF4_C_RETURN_VALUE )
#    _HDF4_invoke_compiler( Fortran HDF4_Fortran_COMPILE_LINE HDF4_Fortran_RETURN_VALUE )

    if( NOT HDF4_FIND_COMPONENTS )
        set( HDF4_LANGUAGE_BINDINGS "C" )
    else()
        # add the extra specified components, ensuring that they are valid.
        foreach( component ${HDF4_FIND_COMPONENTS} )
            list( FIND HDF4_VALID_COMPONENTS ${component} component_location )
            if( ${component_location} EQUAL -1 )
                message( FATAL_ERROR  
                    "\"${component}\" is not a valid HDF4 component." )
            else()
                list( APPEND HDF4_LANGUAGE_BINDINGS ${component} )
            endif()
        endforeach()
    endif()
    
    # seed the initial lists of libraries to find with items we know we need
    set( HDF4_C_LIBRARY_NAMES_INIT hdf mfhdf xdr)
#    set( HDF4_Fortran_LIBRARY_NAMES_INIT hdf_fortran mfhdf_fortran )
    
    foreach( LANGUAGE ${HDF4_LANGUAGE_BINDINGS} )
        if( HDF4_${LANGUAGE}_COMPILE_LINE )
            _HDF4_parse_compile_line( HDF4_${LANGUAGE}_COMPILE_LINE
                HDF4_${LANGUAGE}_INCLUDE_FLAGS
                HDF4_${LANGUAGE}_DEFINITIONS
                HDF4_${LANGUAGE}_LIBRARY_DIRS
                HDF4_${LANGUAGE}_LIBRARY_NAMES
            )
        
            # take a guess that the includes may be in the 'include' sibling directory
            # of a library directory.
            foreach( dir ${HDF4_${LANGUAGE}_LIBRARY_DIRS} )
                list( APPEND HDF4_${LANGUAGE}_INCLUDE_FLAGS ${dir}/../include )
            endforeach()
        endif()

        # set the definitions for the language bindings.
        list( APPEND HDF4_DEFINITIONS ${HDF4_${LANGUAGE}_DEFINITIONS} )
    
        # find the HDF4 include directories
        find_path( HDF4_${LANGUAGE}_INCLUDE_DIR hdf.h
            HINTS
                ${HDF4_${LANGUAGE}_INCLUDE_FLAGS}
                ${_HDF4_HINTS}
            PATHS 
                ${_HDF4_PATHS}
            PATH_SUFFIXES
                include
                Include
        )
        mark_as_advanced( HDF4_${LANGUAGE}_INCLUDE_DIR )
        list( APPEND HDF4_INCLUDE_DIRS ${HDF4_${LANGUAGE}_INCLUDE_DIR} )
        
        set( HDF4_${LANGUAGE}_LIBRARY_NAMES 
            ${HDF4_${LANGUAGE}_LIBRARY_NAMES_INIT} 
            ${HDF4_${LANGUAGE}_LIBRARY_NAMES} )
        
        # find the HDF4 libraries
        foreach( LIB ${HDF4_${LANGUAGE}_LIBRARY_NAMES} )
            if( UNIX AND HDF4_USE_STATIC_LIBRARIES )
                # According to bug 1643 on the CMake bug tracker, this is the
                # preferred method for searching for a static library.
                # See http://www.cmake.org/Bug/view.php?id=1643.  We search
                # first for the full static library name, but fall back to a
                # generic search on the name if the static search fails.
                set( THIS_LIBRARY_SEARCH_DEBUG lib${LIB}d.a ${LIB}d )
                set( THIS_LIBRARY_SEARCH_RELEASE lib${LIB}.a ${LIB} )
            else()
                set( THIS_LIBRARY_SEARCH_DEBUG ${LIB}d )
                set( THIS_LIBRARY_SEARCH_RELEASE ${LIB} )
            endif()
            find_library( HDF4_${LIB}_LIBRARY_DEBUG 
                NAMES ${THIS_LIBRARY_SEARCH_DEBUG} 
                HINTS 
                    ${HDF4_${LANGUAGE}_LIBRARY_DIRS} 
                    ${_HDF4_HINTS} 
                PATHS 
                    ${_HDF4_PATHS}
                PATH_SUFFIXES lib Lib 
            )
            find_library( HDF4_${LIB}_LIBRARY_RELEASE
                NAMES ${THIS_LIBRARY_SEARCH_RELEASE} 
                HINTS 
                    ${HDF4_${LANGUAGE}_LIBRARY_DIRS} 
                    ${_HDF4_HINTS} 
                PATHS 
                    ${_HDF4_PATHS}
                PATH_SUFFIXES lib Lib 
             )
            select_library_configurations( HDF4_${LIB} )
            # even though we adjusted the individual library names in
            # select_library_configurations, we still need to distinguish
            # between debug and release variants because HDF4_LIBRARIES will
            # need to specify different lists for debug and optimized builds.
            # We can't just use the HDF4_${LIB}_LIBRARY variable (which was set
            # up by the selection macro above) because it may specify debug and
            # optimized variants for a particular library, but a list of
            # libraries is allowed to specify debug and optimized only once.
            list( APPEND HDF4_${LANGUAGE}_LIBRARIES_DEBUG 
                ${HDF4_${LIB}_LIBRARY_DEBUG} )
            list( APPEND HDF4_${LANGUAGE}_LIBRARIES_RELEASE 
                ${HDF4_${LIB}_LIBRARY_RELEASE} )
        endforeach()
        list( APPEND HDF4_LIBRARY_DIRS ${HDF4_${LANGUAGE}_LIBRARY_DIRS} )
        
        # Append the libraries for this language binding to the list of all
        # required libraries.
        list( APPEND HDF4_LIBRARIES_DEBUG 
            ${HDF4_${LANGUAGE}_LIBRARIES_DEBUG} )
        list( APPEND HDF4_LIBRARIES_RELEASE
            ${HDF4_${LANGUAGE}_LIBRARIES_RELEASE} )
    endforeach()

    # We may have picked up some duplicates in various lists during the above
    # process for the language bindings (both the C and C++ bindings depend on
    # libz for example).  Remove the duplicates.
    if( HDF4_INCLUDE_DIRS )
        list( REMOVE_DUPLICATES HDF4_INCLUDE_DIRS )
    endif()
    if( HDF4_LIBRARIES_DEBUG )
        list( REMOVE_DUPLICATES HDF4_LIBRARIES_DEBUG )
    endif()
    if( HDF4_LIBRARIES_RELEASE )
        list( REMOVE_DUPLICATES HDF4_LIBRARIES_RELEASE )
    endif()
    if( HDF4_LIBRARY_DIRS )
        list( REMOVE_DUPLICATES HDF4_LIBRARY_DIRS )
    endif()

    # Construct the complete list of HDF4 libraries with debug and optimized
    # variants when the generator supports them.
    if( CMAKE_CONFIGURATION_TYPES OR CMAKE_BUILD_TYPE )
        set( HDF4_LIBRARIES
            debug ${HDF4_LIBRARIES_DEBUG}
            optimized ${HDF4_LIBRARIES_RELEASE} )
    else()
        set( HDF4_LIBRARIES ${HDF4_LIBRARIES_RELEASE} )
    endif()

    # If the HDF4 include directory was found, open H4config.h to determine if
    # HDF4 was compiled with parallel IO support
    set( HDF4_IS_PARALLEL FALSE )
    foreach( _dir HDF4_INCLUDE_DIRS )
        if( EXISTS "${_dir}/h4config.h" )
            file( STRINGS "${_dir}/h4config.h" 
                HDF4_HAVE_PARALLEL_DEFINE
                REGEX "HAVE_PARALLEL 1" )
            if( HDF4_HAVE_PARALLEL_DEFINE )
                set( HDF4_IS_PARALLEL TRUE )
            endif()
        endif()
    endforeach()
    set( HDF4_IS_PARALLEL ${HDF4_IS_PARALLEL} CACHE BOOL
        "HDF4 library compiled with parallel IO support" )
    mark_as_advanced( HDF4_IS_PARALLEL )

endif()

find_package_handle_standard_args( HDF4 DEFAULT_MSG 
    HDF4_LIBRARIES 
    HDF4_INCLUDE_DIRS
)

mark_as_advanced( 
    HDF4_INCLUDE_DIRS 
    HDF4_LIBRARIES 
    HDF4_DEFINTIONS
    HDF4_LIBRARY_DIRS
    HDF4_C_COMPILER_EXECUTABLE
#    HDF4_Fortran_COMPILER_EXECUTABLE 
)

# For backwards compatibility we set HDF4_INCLUDE_DIR to the value of
# HDF4_INCLUDE_DIRS
set( HDF4_INCLUDE_DIR "${HDF4_INCLUDE_DIRS}" )

