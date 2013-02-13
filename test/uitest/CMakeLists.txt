cmake_minimum_required (VERSION 2.8.6)
PROJECT (HDFJAVA_TEST_UITEST Java)

SET(CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES(
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

SET (HDFJAVA_FEST_JARS
    ${HDFJAVA_LIB_DIR}/fest-swing.jar
    ${HDFJAVA_LIB_DIR}/fest-reflect.jar
    ${HDFJAVA_LIB_DIR}/fest-util.jar
    ${HDFJAVA_LIB_DIR}/fest-assert.jar
)

SET (HDFJAVA_TEST_UITEST_SRCS
#    TestHDFViewCLGeometry.java
    TestHDFViewCLMultipleFiles.java
    TestHDFViewCLRootMultipleFiles.java
    TestHDFViewMenu.java
    TestTreeViewNewMenu.java
    TestTreeViewFiles.java
    TestAll.java
)

FILE (WRITE ${PROJECT_BINARY_DIR}/Manifest.txt
"Main-Class: test.uitest.TestAll
"
)

SET (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_LIB_DIR}/junit.jar;${HDFJAVA_JARS_TO_EXPORT};${HDFJAVA_HDFVIEWJAR_TO_EXPORT};${HDFJAVA_FEST_JARS}")

ADD_JAR (${HDFJAVA_TEST_UITEST_TARGET} ${PROJECT_BINARY_DIR}/Manifest.txt ${HDFJAVA_TEST_UITEST_SRCS})

GET_TARGET_PROPERTY(${HDFJAVA_TEST_UITEST_TARGET}_JAR_FILE ${HDFJAVA_TEST_UITEST_TARGET} JAR_FILE)
#INSTALL_JAR (${HDFJAVA_TEST_UITEST_TARGET} ${HJAVA_INSTALL_DATA_DIR}/tests tests)

ADD_DEPENDENCIES (${HDFJAVA_TEST_UITEST_TARGET} ${HDFJAVA_NCSA_HDF5_LIB_TARGET} ${HDFJAVA_NCSA_HDF_LIB_TARGET})

SET (HDF_JAVA_TEST_FILES
      tattrintsize.h5
      tintsize.h5
      tcmpdattrintsize.h5
      tcmpdintsize.h5
      tscalarattrintsize.h5
      tscalarintsize.h5
      tscalarstring.h5
      tordergr.h5
)

SET (HDF_JAVA_TEST_REFERENCE
#      JUnit-gui-CL-geom.txt
      JUnit-gui-CL-multi.txt
      JUnit-gui-CL-root-multi.txt
      JUnit-gui-menu.txt
      JUnit-gui-treeview-files.txt
      JUnit-gui-treeview-newmenu.txt
)

FOREACH (h5_file ${HDF_JAVA_TEST_FILES})
  SET (dest "${PROJECT_BINARY_DIR}/${h5_file}")
  #MESSAGE (STATUS " Copying ${h5_file}")
  ADD_CUSTOM_COMMAND (
      TARGET     ${HDFJAVA_TEST_UITEST_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${h5_file} ${dest}
  )
ENDFOREACH (h5_file ${HDF_JAVA_TEST_FILES})

FOREACH (ref_file ${HDF_JAVA_TEST_REFERENCE})
  SET (dest "${PROJECT_BINARY_DIR}/${ref_file}")
  #MESSAGE (STATUS " Copying ${ref_file}")
  ADD_CUSTOM_COMMAND (
      TARGET     ${HDFJAVA_TEST_UITEST_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${ref_file} ${dest}
  )
ENDFOREACH (ref_file ${HDF_JAVA_TEST_REFERENCE})

IF (WIN32 AND NOT CYGWIN)
  SET(CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
ELSE (WIN32 AND NOT CYGWIN)
  SET(CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
ENDIF(WIN32 AND NOT CYGWIN)

SET (CMAKE_JAVA_CLASSPATH ".")
FOREACH (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
  SET(CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
ENDFOREACH(CMAKE_INCLUDE_PATH)
SET (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${HDFJAVA_TEST_UITEST_TARGET}_JAR_FILE}")

ADD_TEST (
    NAME JUnit-gui-clear-objects
    COMMAND    ${CMAKE_COMMAND}
        -E remove 
        closebutton.hdf
        testopenbutton.hdf
        testopenfile.hdf
        testopenrofile.h5
        testfile.hdf
        testfile.h5
        closefile.hdf
        closeallfile.hdf
        closeallfile.h5
        testsavefile.h5
        testsaveasfile.h5
        testsaveasfile2.h5
        testgrp.h5
        testds.h5
)

ADD_TEST (
    NAME JUnit-gui-menu
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestHDFViewMenu"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-menu.out"
        -D "TEST_EXPECT=0"
        -D "TEST_APPEND=TRUE"
        -D "TEST_REFERENCE=JUnit-gui-menu.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

SET_TESTS_PROPERTIES (JUnit-gui-menu PROPERTIES
    DEPENDS JUnit-gui-clear-objects
)

ADD_TEST (
    NAME JUnit-gui-treeview-newmenu
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewNewMenu"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-newmenu.out"
        -D "TEST_EXPECT=0"
        -D "TEST_APPEND=TRUE"
        -D "TEST_REFERENCE=JUnit-gui-treeview-newmenu.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

SET_TESTS_PROPERTIES (JUnit-gui-treeview-newmenu PROPERTIES
    DEPENDS JUnit-gui-menu
)

ADD_TEST (
    NAME JUnit-gui-treeview-files-copy
    COMMAND    ${CMAKE_COMMAND}
        -E copy ${PROJECT_SOURCE_DIR}/tcmpdintsize.h5 ${PROJECT_BINARY_DIR}/temp_cmpdimport.h5
)

SET_TESTS_PROPERTIES (JUnit-gui-treeview-files-copy PROPERTIES
    DEPENDS JUnit-gui-treeview-newmenu
)

ADD_TEST (
    NAME JUnit-gui-treeview-files
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewFiles"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-files.out"
        -D "TEST_EXPECT=0"
        -D "TEST_APPEND=TRUE"
        -D "TEST_REFERENCE=JUnit-gui-treeview-files.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

SET_TESTS_PROPERTIES (JUnit-gui-treeview-files PROPERTIES
    DEPENDS JUnit-gui-treeview-files-copy
)
   
ADD_TEST (
    NAME JUnit-gui-CL-multi
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestHDFViewCLMultipleFiles"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-CL-multi.out"
        -D "TEST_EXPECT=0"
        -D "TEST_APPEND=TRUE"
        -D "TEST_REFERENCE=JUnit-gui-CL-multi.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

SET_TESTS_PROPERTIES (JUnit-gui-CL-multi PROPERTIES
    DEPENDS JUnit-gui-treeview-files
)
   
ADD_TEST (
    NAME JUnit-gui-CL-root-multi
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestHDFViewCLRootMultipleFiles"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-CL-root-multi.out"
        -D "TEST_EXPECT=0"
        -D "TEST_APPEND=TRUE"
        -D "TEST_REFERENCE=JUnit-gui-CL-root-multi.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

SET_TESTS_PROPERTIES (JUnit-gui-CL-root-multi PROPERTIES
    DEPENDS JUnit-gui-CL-multi
)
   
#ADD_TEST (
#    NAME JUnit-gui-CL-geom
#    COMMAND "${CMAKE_COMMAND}"
#        -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
#        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
#        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
#        -D "TEST_PROGRAM=test.uitest.TestHDFViewCLGeometry"
#        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
#        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
#        -D "TEST_OUTPUT=JUnit-gui-CL-geom.out"
#        -D "TEST_EXPECT=0"
#        -D "TEST_APPEND=TRUE"
#        -D "TEST_REFERENCE=JUnit-gui-CL-geom.txt"
#        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
#)
#
#SET_TESTS_PROPERTIES (JUnit-gui-CL-geom PROPERTIES
#    DEPENDS JUnit-gui-CL-root-multi
#)
   