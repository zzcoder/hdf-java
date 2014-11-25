cmake_minimum_required (VERSION 2.8.10)
PROJECT (HDFJAVA_TEST_HDF5LIB Java)

set (CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES (
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

set (HDFJAVA_TEST_HDF5LIB_SRCS
    TestH5.java
    TestH5E.java
    TestH5Edefault.java
    TestH5Eregister.java
    TestH5Fparams.java
    TestH5Fbasic.java
    TestH5F.java
    TestH5Gbasic.java
    TestH5G.java
    TestH5Giterate.java
    TestH5Sbasic.java
    TestH5S.java
    TestH5Tparams.java
    TestH5Tbasic.java
    TestH5T.java
    TestH5Dparams.java
    TestH5D.java
    TestH5Dplist.java
    TestH5Lparams.java
    TestH5Lbasic.java
    TestH5Lcreate.java
    TestH5R.java
    TestH5P.java
    TestH5PData.java
    TestH5Pfapl.java
    TestH5A.java
    TestH5Oparams.java
    TestH5Obasic.java
    TestH5Ocreate.java
    TestH5Ocopy.java
    TestH5Z.java
    TestAll.java
)

file (WRITE ${PROJECT_BINARY_DIR}/Manifest.txt
"Main-Class: test.hdf5lib.TestAll
"
)

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_LIB_DIR}/junit.jar;${HDFJAVA_LIB_DIR}/hamcrest-core.jar;${HDFJAVA_HDF5_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

add_jar (${HDFJAVA_TEST_HDF5_LIB_TARGET} MANIFEST ${PROJECT_BINARY_DIR}/Manifest.txt ${HDFJAVA_TEST_HDF5LIB_SRCS})

get_target_property (${HDFJAVA_TEST_HDF5_LIB_TARGET}_JAR_FILE ${HDFJAVA_TEST_HDF5_LIB_TARGET} JAR_FILE)
#install_jar (${HDFJAVA_TEST_HDF5_LIB_TARGET} ${HJAVA_INSTALL_DATA_DIR}/tests tests)
#get_target_property (${HDFJAVA_TEST_HDF5_LIB_TARGET}_CLASSPATH ${HDFJAVA_TEST_HDF5_LIB_TARGET} CLASSDIR)

add_dependencies (${HDFJAVA_TEST_HDF5_LIB_TARGET} ${HDFJAVA_NCSA_HDF5_LIB_TARGET})

set (HDF_JAVA_TEST_FILES
      h5ex_g_iterate.hdf
      JUnit-interface.txt
)

foreach (h5_file ${HDF_JAVA_TEST_FILES})
  set (dest "${PROJECT_BINARY_DIR}/${h5_file}")
  #message (STATUS " Copying ${h5_file}")
  add_custom_command (
      TARGET     ${HDFJAVA_TEST_HDF5_LIB_TARGET}
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
set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${HDFJAVA_TEST_HDF5_LIB_TARGET}_JAR_FILE}")
set (testfilter "OK (598 tests)")

if (CMAKE_BUILD_TYPE MATCHES Debug)
  set (CMD_ARGS "-Dncsa.hdf.hdf5lib.H5.loadLibraryName=jhdf5_debug;")
endif(CMAKE_BUILD_TYPE MATCHES Debug)

add_test (
    NAME JUnit-interface
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=${CMD_ARGS}-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.hdf5lib.TestAll"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${HDFJAVA_BINARY_DIR}"
        -D "TEST_OUTPUT=test/hdf5lib/JUnit-interface.out"
#        -D "TEST_LOG_LEVEL=trace"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_ERROR=TRUE"
        -D "TEST_FILTER:STRING=${testfilter}"
        -D "TEST_REFERENCE=test/hdf5lib/JUnit-interface.txt"
        -P "${HDF_RESOURCES_DIR}/runTest.cmake"
)
