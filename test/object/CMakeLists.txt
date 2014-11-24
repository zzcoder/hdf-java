cmake_minimum_required (VERSION 2.8.10)
PROJECT (HDFJAVA_TEST_OBJECT Java)

set (CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES (
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

set (HDFJAVA_TEST_OBJECT_SRCS
    AllH5ObjectTests.java
    AttributeTest.java
    CompoundDSTest.java
    DatasetTest.java
    DatatypeTest.java
    DataFormatTest.java
    FileFormatTest.java
    GroupTest.java
    H5BugFixTest.java
    H5CompoundDSTest.java
    H5DatatypeTest.java
    H5FileTest.java
    H5GroupTest.java
    H5ScalarDSTest.java
    H5TestFile.java
    HObjectTest.java
    MetadataTest.java
    ScalarDSTest.java
)

file (WRITE ${PROJECT_BINARY_DIR}/Manifest.txt
"Main-Class: test.object.AllH5ObjectTests

"
)

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_LIB_DIR}/junit.jar;${HDFJAVA_LIB_DIR}/hamcrest-core.jar;${HDFJAVA_OBJECT_JARS};${HDFJAVA_HDF5_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

add_jar (${HDFJAVA_TEST_OBJECT_LIB_TARGET} MANIFEST ${PROJECT_BINARY_DIR}/Manifest.txt ${HDFJAVA_TEST_OBJECT_SRCS})

get_target_property (${HDFJAVA_TEST_OBJECT_LIB_TARGET}_JAR_FILE ${HDFJAVA_TEST_OBJECT_LIB_TARGET} JAR_FILE)
#install_jar (${HDFJAVA_TEST_OBJECT_LIB_TARGET} ${HJAVA_INSTALL_DATA_DIR}/tests tests)

add_dependencies (${HDFJAVA_TEST_OBJECT_LIB_TARGET} ${HDFJAVA_NCSA_H5_LIB_TARGET})

set (HDF_JAVA_TEST_FILES
      h5ex_d_extern.data
      h5ex_d_extern.hdf5
      JUnit-object-interface.txt
)

foreach (h5_file ${HDF_JAVA_TEST_FILES})
  set (dest "${PROJECT_BINARY_DIR}/${h5_file}")
  #message (STATUS " Copying ${h5_file}")
  add_custom_command (
      TARGET     ${HDFJAVA_TEST_OBJECT_LIB_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${h5_file} ${dest}
  )
endforeach (h5_file ${HDF_JAVA_TEST_FILES})

if (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
else (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
endif (WIN32)

set (CMAKE_JAVA_CLASSPATH ".")
foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
endforeach (CMAKE_INCLUDE_PATH)
set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${HDFJAVA_TEST_OBJECT_LIB_TARGET}_JAR_FILE}")
set (testfilter "OK (151 tests)")

if (CMAKE_BUILD_TYPE MATCHES Debug)
  set (CMD_ARGS "-Dncsa.hdf.hdf5lib.H5.loadLibraryName=jhdf5_debug;")
endif(CMAKE_BUILD_TYPE MATCHES Debug)

add_test (
    NAME JUnit-object-interface
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING="
        -D "TEST_PROGRAM=${CMD_ARGS}-ea;org.junit.runner.JUnitCore;test.object.AllH5ObjectTests"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${HDFJAVA_BINARY_DIR}"
        -D "TEST_OUTPUT=test/object/JUnit-object-interface.out"
        -D "TEST_EXPECT=0"
#        -D "TEST_LOG_LEVEL=trace"
        -D "TEST_FILTER:STRING=${testfilter}"
        -D "TEST_REFERENCE=test/object/JUnit-object-interface.txt"
        -D "TEST_MASK_ERROR=TRUE"
        -P "${HDF_RESOURCES_DIR}/runTest.cmake"
)
 