cmake_minimum_required (VERSION 2.8.10)
PROJECT (HDFJAVA_TEST_UITEST Java)

set (CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES (
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

set (HDFJAVA_FEST_JARS
    ${HDFJAVA_LIB_DIR}/fest-swing.jar
    ${HDFJAVA_LIB_DIR}/fest-reflect.jar
    ${HDFJAVA_LIB_DIR}/fest-util.jar
    ${HDFJAVA_LIB_DIR}/fest-assert.jar
)

set (HDFJAVA_TEST_UITEST_SRCS
    TestHDFViewCLMultipleFiles.java
    TestHDFViewCLRootMultipleFiles.java
    TestHDFViewMenu.java
    TestTreeViewNewVLDatatypes.java
    TestTreeViewNewMenu.java
    TestTreeViewGroups.java
    TestTreeViewFiles.java
    TestTreeViewFilters.java
    TestTreeViewExport.java
    TestTableViewMenu.java
    TestHDFViewTAttr2.java
    TestAll.java
)

file (WRITE ${PROJECT_BINARY_DIR}/Manifest.txt
"Main-Class: test.uitest.TestAll
"
)

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_LIB_DIR}/junit.jar;${HDFJAVA_JARS_TO_EXPORT};${HDFJAVA_HDFVIEWJAR_TO_EXPORT};${HDFJAVA_FEST_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

add_jar (${HDFJAVA_TEST_UITEST_TARGET} MANIFEST ${PROJECT_BINARY_DIR}/Manifest.txt ${HDFJAVA_TEST_UITEST_SRCS})

get_target_property (${HDFJAVA_TEST_UITEST_TARGET}_JAR_FILE ${HDFJAVA_TEST_UITEST_TARGET} JAR_FILE)
#install_jar (${HDFJAVA_TEST_UITEST_TARGET} ${HJAVA_INSTALL_DATA_DIR}/tests tests)

add_dependencies (${HDFJAVA_TEST_UITEST_TARGET} ${HDFJAVA_NCSA_HDF5_LIB_TARGET} ${HDFJAVA_NCSA_HDF_LIB_TARGET})

set (HDF_JAVA_TEST_FILES
      tattrintsize.h5
      tintsattrs.h5
      tintsize.h5
      tcmpdattrintsize.h5
      tcmpdints.h5
      tcmpdintsize.h5
      tfilters.h5
      tscalarattrintsize.h5
      tscalarintsize.h5
      tscalarstring.h5
      tordergr.h5
      tattr2.h5
)

set (HDF_JAVA_TEST_REFERENCE
      JUnit-gui-CL-multi.txt
      JUnit-gui-CL-multi.err
      JUnit-gui-CL-root-multi.txt
      JUnit-gui-CL-root-multi.err
      JUnit-gui-menu.txt
      JUnit-gui-menu.err
      JUnit-gui-treeview-groups.txt
      JUnit-gui-treeview-groups.err
      JUnit-gui-treeview-files.txt
      JUnit-gui-treeview-files.err
      JUnit-gui-treeview-filters.txt
      JUnit-gui-treeview-filters.err
      JUnit-gui-treeview-export.txt
      JUnit-gui-treeview-export.err
      JUnit-gui-treeview-newmenu.txt
      JUnit-gui-treeview-newmenu.err
      JUnit-gui-tableview-menu.txt
      JUnit-gui-tableview-menu.err
      JUnit-gui-treeview-newvldt.txt
      JUnit-gui-treeview-newvldt.err
      JUnit-gui-tattr2.txt
      JUnit-gui-tattr2.err
      DU64BITS.xbin
)

set (HDF_JAVA_EXPORT_REFERENCE
      DS64BITS.xtxt
)

foreach (h5_file ${HDF_JAVA_TEST_FILES})
  set (dest "${PROJECT_BINARY_DIR}/${h5_file}")
  #message (STATUS " Copying ${h5_file}")
  add_custom_command (
      TARGET     ${HDFJAVA_TEST_UITEST_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${h5_file} ${dest}
  )
endforeach (h5_file ${HDF_JAVA_TEST_FILES})

foreach (ref_file ${HDF_JAVA_TEST_REFERENCE})
  set (dest "${PROJECT_BINARY_DIR}/${ref_file}")
  #message (STATUS " Copying ${ref_file}")
  add_custom_command (
      TARGET     ${HDFJAVA_TEST_UITEST_TARGET}
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${ref_file} ${dest}
  )
endforeach (ref_file ${HDF_JAVA_TEST_REFERENCE})

foreach (exp_file ${HDF_JAVA_EXPORT_REFERENCE})
  if (WIN32)
    file (READ ${PROJECT_SOURCE_DIR}/${exp_file} TEST_STREAM)
    file (WRITE ${PROJECT_BINARY_DIR}/${exp_file} "${TEST_STREAM}")
  else (WIN32)
    add_custom_command (
        TARGET     ${HDFJAVA_TEST_UITEST_TARGET}
        POST_BUILD
        COMMAND    ${CMAKE_COMMAND}
        ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${exp_file} ${PROJECT_BINARY_DIR}/${exp_file}
    )
  endif (WIN32)
endforeach (exp_file ${HDF_JAVA_EXPORT_REFERENCE})

if (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
else (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
endif (WIN32)

set (CMAKE_JAVA_CLASSPATH ".")
foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
endforeach (CMAKE_INCLUDE_PATH)
set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${HDFJAVA_TEST_UITEST_TARGET}_JAR_FILE}")

add_test (
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
        DS64BITS.txt
        DU64BITS.bin
        JUnit-gui-menu.out
        JUnit-gui-menu.out.err
        JUnit-gui-treeview-newmenu.out
        JUnit-gui-treeview-newmenu.out.err
        JUnit-gui-treeview-groups.out
        JUnit-gui-treeview-groups.out.err
        JUnit-gui-treeview-files.out
        JUnit-gui-treeview-files.out.err
        JUnit-gui-treeview-filters.out
        JUnit-gui-treeview-filters.out.err
        JUnit-gui-treeview-export.out
        JUnit-gui-treeview-export.out.err
        JUnit-gui-tableview-menu.out
        JUnit-gui-tableview-menu.out.err
        JUnit-gui-tattr2.out
        JUnit-gui-tattr2.out.err
        JUnit-gui-treeview-newvldt.out
        JUnit-gui-treeview-newvldt.out.err
        JUnit-gui-CL-multi.out
        JUnit-gui-CL-multi.out.err
        JUnit-gui-CL-root-multi.out
        JUnit-gui-CL-root-multi.out.err
)

add_test (
    NAME JUnit-gui-menu
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestHDFViewMenu"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-menu.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-menu.err"
        -D "TEST_REFERENCE=JUnit-gui-menu.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-menu PROPERTIES
    DEPENDS "JUnit-gui-clear-objects;JUnit-gui-modules"
)

add_test (
    NAME JUnit-gui-treeview-newmenu
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewNewMenu"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-newmenu.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-treeview-newmenu.err"
        -D "TEST_REFERENCE=JUnit-gui-treeview-newmenu.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-treeview-newmenu PROPERTIES
    DEPENDS JUnit-gui-menu
)

add_test (
    NAME JUnit-gui-treeview-groups
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewGroups"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-groups.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-treeview-groups.err"
        -D "TEST_REFERENCE=JUnit-gui-treeview-groups.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-treeview-groups PROPERTIES
    DEPENDS JUnit-gui-treeview-newmenu
)

add_test (
    NAME JUnit-gui-treeview-files-copy
    COMMAND    ${CMAKE_COMMAND}
        -E copy ${PROJECT_SOURCE_DIR}/tcmpdintsize.h5 ${PROJECT_BINARY_DIR}/temp_cmpdimport.h5
)

set_tests_properties (JUnit-gui-treeview-files-copy PROPERTIES
    DEPENDS JUnit-gui-treeview-groups
)

add_test (
    NAME JUnit-gui-treeview-files
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewFiles"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-files.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-treeview-files.err"
        -D "TEST_REFERENCE=JUnit-gui-treeview-files.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-treeview-files PROPERTIES
    DEPENDS JUnit-gui-treeview-files-copy
)

add_test (
    NAME JUnit-gui-treeview-export
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewExport"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-export.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-treeview-export.err"
        -D "TEST_REFERENCE=JUnit-gui-treeview-export.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-treeview-export PROPERTIES
    DEPENDS JUnit-gui-treeview-files
)

add_test (
    NAME JUnit-gui-treeview-export2
    COMMAND ${CMAKE_COMMAND}
        -E compare_files ${PROJECT_BINARY_DIR}/DS64BITS.xtxt ${PROJECT_BINARY_DIR}/DS64BITS.txt
)
set_tests_properties (JUnit-gui-treeview-export2 PROPERTIES
    DEPENDS JUnit-gui-treeview-export
)

add_test (
    NAME JUnit-gui-treeview-export3
    COMMAND ${CMAKE_COMMAND}
        -E compare_files ${PROJECT_BINARY_DIR}/DU64BITS.xbin ${PROJECT_BINARY_DIR}/DU64BITS.bin
)
set_tests_properties (JUnit-gui-treeview-export3 PROPERTIES
    DEPENDS JUnit-gui-treeview-export2
)
   
add_test (
    NAME JUnit-gui-tableview-menu
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTableViewMenu"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-tableview-menu.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-tableview-menu.err"
        -D "TEST_REFERENCE=JUnit-gui-tableview-menu.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-tableview-menu PROPERTIES
    DEPENDS JUnit-gui-treeview-export3
)

add_test (
    NAME JUnit-gui-treeview-newvldt
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewNewVLDatatypes"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-newvldt.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-treeview-newvldt.err"
        -D "TEST_REFERENCE=JUnit-gui-treeview-newvldt.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-treeview-newvldt PROPERTIES
    DEPENDS JUnit-gui-tableview-menu
)

add_test (
    NAME JUnit-gui-treeview-filters
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestTreeViewFilters"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-treeview-filters.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-treeview-filters.err"
        -D "TEST_REFERENCE=JUnit-gui-treeview-filters.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-treeview-filters PROPERTIES
    DEPENDS JUnit-gui-treeview-newvldt
)
   
add_test (
    NAME JUnit-gui-tattr2
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestHDFViewTAttr2"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-tattr2.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-tattr2.err"
        -D "TEST_REFERENCE=JUnit-gui-tattr2.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-tattr2 PROPERTIES
    DEPENDS JUnit-gui-treeview-filters
)
   
add_test (
    NAME JUnit-gui-CL-multi
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestHDFViewCLMultipleFiles"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-CL-multi.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-CL-multi.err"
        -D "TEST_REFERENCE=JUnit-gui-CL-multi.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-CL-multi PROPERTIES
    DEPENDS JUnit-gui-tattr2
)
   
add_test (
    NAME JUnit-gui-CL-root-multi
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING=-ea;org.junit.runner.JUnitCore"
        -D "TEST_PROGRAM=test.uitest.TestHDFViewCLRootMultipleFiles"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${PROJECT_BINARY_DIR}"
        -D "TEST_OUTPUT=JUnit-gui-CL-root-multi.out"
        -D "TEST_EXPECT=0"
        -D "TEST_MASK_FILE=TRUE"
        -D "TEST_ERRREF=JUnit-gui-CL-root-multi.err"
        -D "TEST_REFERENCE=JUnit-gui-CL-root-multi.txt"
        -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
)

set_tests_properties (JUnit-gui-CL-root-multi PROPERTIES
    DEPENDS JUnit-gui-CL-multi
)
   