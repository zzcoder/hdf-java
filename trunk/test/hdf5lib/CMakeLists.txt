cmake_minimum_required (VERSION 2.8.3)
PROJECT (HDFJAVA_TEST_HDF5LIB Java)

SET(CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES(
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

SET (CMAKE_JAVA_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH} ${HDFJAVA_LIB_DIR}/junit.jar ${HDFJAVA_JARS_TO_EXPORT})

SET (HDFJAVA_TEST_HDF5LIB_SRCS
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
    TestH5Lparams.java
    TestH5Lbasic.java
    TestH5Lcreate.java
    TestH5R.java
    TestH5P.java
    TestH5A.java
    TestH5Oparams.java
    TestH5Obasic.java
    TestH5Ocreate.java
    TestAll.java
)

ADD_JAR (${HDFJAVA_TEST_HDF5_LIB_TARGET} ${HDFJAVA_TEST_HDF5LIB_SRCS})
#INSTALL_JAR (${HDFJAVA_TEST_HDF5_LIB_TARGET} ${HJAVA_INSTALL_DATA_DIR}/tests tests)
ADD_DEPENDENCIES (${HDFJAVA_TEST_HDF5_LIB_TARGET} ${HDFJAVA_NCSA_HDF5_LIB_TARGET})

SET (HDF_JAVA_TEST_FILES
      h5ex_g_iterate.hdf
)

FOREACH (h5_file ${HDF_JAVA_TEST_FILES})
  SET (dest "${PROJECT_BINARY_DIR}/${h5_file}")
  #MESSAGE (STATUS " Copying ${h5_file}")
  ADD_CUSTOM_COMMAND (
      TARGET     ${HDFJAVA_TEST_HDF5_LIB_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${h5_file} ${dest}
  )
ENDFOREACH (h5_file ${HDF_JAVA_TEST_FILES})

SET (JUNIT_TEST_RUNNER ${PROJECT_BINARY_DIR}/JUnitRunner.cmake)
FILE (WRITE ${JUNIT_TEST_RUNNER} 
  "EXECUTE_PROCESS(COMMAND
  ${CMAKE_Java_RUNTIME} -Xmx1024M 
  -Djava.library.path=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}
  -cp \"${${HDFJAVA_TEST_HDF5_LIB_TARGET}_CLASSPATH}\"
  -ea org.junit.runner.JUnitCore test.hdf5lib.TestAll
  WORKING_DIRECTORY ${CMAKE_BINARY_DIR}
  RESULT_VARIABLE RESULT)
  IF(RESULT)
    MESSAGE(SEND_ERROR \"Error during JUnit HDF5LIB Tests\")
  ENDIF(RESULT)
  ")

ADD_TEST (JUnit-interface  ${CMAKE_COMMAND} -P ${JUNIT_TEST_RUNNER})
   