cmake_minimum_required (VERSION 2.8.10)
PROJECT (HDFJAVA_TEST_MODULES Java)

set (CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES (
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${JAVA_NCSA_HDF_VIEW_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

set (HDFJAVA_FEST_JARS
    ${HDFJAVA_LIB_DIR}/fest-swing.jar
    ${HDFJAVA_LIB_DIR}/fest-reflect.jar
    ${HDFJAVA_LIB_DIR}/fest-util.jar
    ${HDFJAVA_LIB_DIR}/fest-assert.jar
)

set (HDFJAVA_TEST_MODULES_LIB_TARGET          "jarmodlib")

set (HDFJAVA_TEST_UIMOD_TARGET                "jarmodtest")

set (HDFJAVA_TEST_MODULES_SRCS
    GermanTableView.java
)

set (HDF_JAVA_TEST_FILES
      tintsize.h5
)

set (HDF_JAVA_TEST_REFERENCE
      JUnit-gui-modules.txt
      JUnit-gui-modules.err
)

INCLUDE_DIRECTORIES (${CMAKE_CURRENT_BINARY_DIR}/CMakeFiles/${HDFJAVA_NCSA_VIEW_LIB_CORENAME}.dir/ncsa/hdf/view)

foreach (HCP_JAR ${HDFJAVA_JARS_TO_EXPORT})
  get_filename_component (_HCP_FILE ${HCP_JAR} NAME)
  set (HDFVIEW_CLASSJARS "${_HCP_FILE} ${HDFVIEW_CLASSJARS}")
endforeach (HCP_JAR ${HDFJAVA_JARS_TO_EXPORT})
foreach (HCP_JAR ${HDFJAVA_HDFVIEWJAR_TO_EXPORT})
  get_filename_component (_HCP_FILE ${HCP_JAR} NAME)
  set (HDFVIEW_CLASSJARS "${_HCP_FILE} ${HDFVIEW_CLASSJARS}")
endforeach (HCP_JAR ${HDFJAVA_HDFVIEWJAR_TO_EXPORT})
get_filename_component (_HCP_FILE ${HDFJAVA_LOGGING_NOP_JAR} NAME)
set (HDFVIEW_CLASSJARS "${HDFVIEW_CLASSJARS} ${_HCP_FILE}")

file (WRITE ${PROJECT_BINARY_DIR}/Manifest.txt
"
"
)

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_LIB_DIR}/junit.jar;${HDFJAVA_LIB_DIR}/hamcrest-core.jar;${HDFJAVA_JARS_TO_EXPORT};${HDFJAVA_HDFVIEWJAR_TO_EXPORT};${HDFJAVA_FEST_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

add_jar (${HDFJAVA_TEST_MODULES_LIB_TARGET} MANIFEST ${HDFJAVA_TEST_MODULES_BINARY_DIR}/Manifest.txt SOURCES "${HDFJAVA_TEST_MODULES_SRCS}")
get_target_property (${HDFJAVA_TEST_MODULES_LIB_TARGET}_JAR_FILE ${HDFJAVA_TEST_MODULES_LIB_TARGET} JAR_FILE)

add_dependencies (${HDFJAVA_TEST_MODULES_LIB_TARGET} ${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HDFJAVA_NCSA_HDF5_LIB_TARGET} ${HDFJAVA_NCSA_HDF_LIB_TARGET})

foreach (h5_file ${HDF_JAVA_TEST_FILES})
  set (dest "${PROJECT_BINARY_DIR}/${h5_file}")
  #message (STATUS " Copying ${h5_file}")
  add_custom_command (
      TARGET     ${HDFJAVA_TEST_MODULES_LIB_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${h5_file} ${dest}
  )
endforeach (h5_file ${HDF_JAVA_TEST_FILES})

foreach (ref_file ${HDF_JAVA_TEST_REFERENCE})
  set (dest "${PROJECT_BINARY_DIR}/${ref_file}")
  #message (STATUS " Copying ${ref_file}")
  add_custom_command (
      TARGET     ${HDFJAVA_TEST_MODULES_LIB_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${ref_file} ${dest}
  )
endforeach (ref_file ${HDF_JAVA_TEST_REFERENCE})

# make test dir
file (MAKE_DIRECTORY "${PROJECT_BINARY_DIR}/lib")
file (MAKE_DIRECTORY "${PROJECT_BINARY_DIR}/lib/ext")

add_custom_command (
    TARGET     ${HDFJAVA_TEST_MODULES_LIB_TARGET}
    POST_BUILD
    COMMAND    ${CMAKE_COMMAND}
    ARGS       -E copy_if_different ${${HDFJAVA_TEST_MODULES_LIB_TARGET}_JAR_FILE} ${PROJECT_BINARY_DIR}/lib/ext
)

set (HDFJAVA_TEST_UIMOD_SRCS
    TestGermanViewMenu.java
)

file (WRITE ${PROJECT_BINARY_DIR}/TManifest.txt
"Main-Class: test.modules.TestGermanViewMenu
"
)

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_LIB_DIR}/junit.jar;${HDFJAVA_LIB_DIR}/hamcrest-core.jar;${HDFJAVA_JARS_TO_EXPORT};${HDFJAVA_HDFVIEWJAR_TO_EXPORT};${HDFJAVA_FEST_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

add_jar (${HDFJAVA_TEST_UIMOD_TARGET} MANIFEST ${PROJECT_BINARY_DIR}/TManifest.txt ${HDFJAVA_TEST_UIMOD_SRCS})

get_target_property (${HDFJAVA_TEST_UIMOD_TARGET}_JAR_FILE ${HDFJAVA_TEST_UIMOD_TARGET} JAR_FILE)
#install_jar (${HDFJAVA_TEST_UIMOD_TARGET} ${HJAVA_INSTALL_DATA_DIR}/tests tests)

add_dependencies (${HDFJAVA_TEST_UIMOD_TARGET} ${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HDFJAVA_NCSA_HDF5_LIB_TARGET} ${HDFJAVA_NCSA_HDF_LIB_TARGET})

if (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
else (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
endif (WIN32)

set (CMAKE_JAVA_CLASSPATH ".")
foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
endforeach (CMAKE_INCLUDE_PATH)
set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${HDFJAVA_TEST_UIMOD_TARGET}_JAR_FILE}")

set (CMD_ARGS "-Dhdfview.workdir=${PROJECT_BINARY_DIR};")
if (CMAKE_BUILD_TYPE MATCHES Debug)
  set (CMD_ARGS "${CMD_ARGS}-Dncsa.hdf.hdf5lib.H5.loadLibraryName=jhdf5_debug;")
endif(CMAKE_BUILD_TYPE MATCHES Debug)

add_test (
    NAME JUnit-gui-modules-clear-objects
    COMMAND    ${CMAKE_COMMAND}
        -E remove 
        JUnit-gui-modules.out
        JUnit-gui-modules.out.err
)

add_test (
    NAME JUnit-gui-modules
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=${CMD_ARGS}-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.modules.TestGermanViewMenu"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-modules.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-modules.err"
        -D "TEST_REFERENCE=JUnit-gui-modules.txt"
        -P "${HDF_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-modules PROPERTIES
    DEPENDS JUnit-gui-modules-clear-objects
)
