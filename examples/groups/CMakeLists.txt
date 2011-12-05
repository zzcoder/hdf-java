cmake_minimum_required (VERSION 2.8.6)
PROJECT (HDFJAVA_EXAMPLES_GROUPS Java)

SET(CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES(
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

SET (CMAKE_JAVA_INCLUDE_PATH ${HDFJAVA_JARS_TO_EXPORT})

SET (HDF_JAVA_EXAMPLES
    H5Ex_G_Create
    H5Ex_G_Iterate
    H5Ex_G_Compact
    H5Ex_G_Corder
    H5Ex_G_Intermediate
    H5Ex_G_Phase
    H5Ex_G_Visit
)

IF (WIN32 AND NOT CYGWIN)
  SET(CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
ELSE (WIN32 AND NOT CYGWIN)
  SET(CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
ENDIF(WIN32 AND NOT CYGWIN)

SET (CMAKE_JAVA_CLASSPATH ".")
FOREACH (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
  SET(CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
ENDFOREACH(CMAKE_INCLUDE_PATH)

FOREACH (example ${HDF_JAVA_EXAMPLES})
  ADD_JAR (${example} ${example}.java)
  GET_TARGET_PROPERTY(${example}_JAR_FILE ${example} JAR_FILE)
#  INSTALL_JAR (${example} ${HJAVA_INSTALL_DATA_DIR}/examples examples)
  GET_TARGET_PROPERTY(${example}_CLASSPATH ${example} CLASS_DIR)
  ADD_DEPENDENCIES (${example} ${HDFJAVA_NCSA_HDF5_LIB_TARGET})
ENDFOREACH (example ${HDF_JAVA_EXAMPLES})

SET (HDF_JAVA_TEST_FILES
    h5ex_g_iterate.h5
    h5ex_g_visit.h5
)

FOREACH (h5_file ${HDF_JAVA_TEST_FILES})
  SET (dest "${PROJECT_BINARY_DIR}/${h5_file}")
  #MESSAGE (STATUS " Copying ${h5_file}")
  ADD_CUSTOM_COMMAND (
      TARGET     H5Ex_G_Visit
      POST_BUILD
      COMMAND    ${CMAKE_COMMAND}
      ARGS       -E copy_if_different ${PROJECT_SOURCE_DIR}/${h5_file} ${dest}
  )
ENDFOREACH (h5_file ${HDF_JAVA_TEST_FILES})

MACRO (ADD_H5_TEST resultfile resultcode)
  ADD_TEST (
      NAME groups-${resultfile}
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
          -D "TEST_PROGRAM=examples.groups.${resultfile}"
          -D "TEST_ARGS:STRING=${ARGN}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${resultfile}_JAR_FILE}"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_EXAMPLES_BINARY_DIR}"
          -D "TEST_OUTPUT=${HDFJAVA_EXAMPLES_GROUPS_BINARY_DIR}/${resultfile}.out"
          -D "TEST_EXPECT=${resultcode}"
          -D "TEST_REFERENCE=${HDFJAVA_EXAMPLES_GROUPS_BINARY_DIR}/${resultfile}.txt"
          -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
  )
  IF (NOT "${last_test}" STREQUAL "")
    SET_TESTS_PROPERTIES (groups-${resultfile} PROPERTIES DEPENDS ${last_test})
  ENDIF (NOT "${last_test}" STREQUAL "")
  SET (last_test "groups-${resultfile}")
ENDMACRO (ADD_H5_TEST file)

IF (BUILD_TESTING)

  FOREACH (example ${HDF_JAVA_EXAMPLES})
    ADD_TEST (
        NAME groups-${example}-clearall-objects
        COMMAND    ${CMAKE_COMMAND}
            -E remove
            ${example}.out
            ${example}.out.err
    )
    IF (NOT "${last_test}" STREQUAL "")
      SET_TESTS_PROPERTIES (groups-${example}-clearall-objects PROPERTIES DEPENDS ${last_test})
    ENDIF (NOT "${last_test}" STREQUAL "")
    SET (last_test "groups-${example}-clearall-objects")
    IF (NOT ${example} STREQUAL "H5Ex_G_Iterate" AND NOT ${example} STREQUAL "H5Ex_G_Visit")
      ADD_TEST (
          NAME groups-${example}-clearall-h5s
          COMMAND    ${CMAKE_COMMAND}
              -E remove
              ${example}.h5
      )
      IF (NOT "${last_test}" STREQUAL "")
        SET_TESTS_PROPERTIES (groups-${example}-clearall-h5s PROPERTIES DEPENDS ${last_test})
      ENDIF (NOT "${last_test}" STREQUAL "")
      SET (last_test "groups-${example}-clearall-h5s")
    ENDIF (NOT ${example} STREQUAL "H5Ex_G_Iterate" AND NOT ${example} STREQUAL "H5Ex_G_Visit")
    ADD_TEST (
        NAME groups-${example}-copy-objects
        COMMAND    ${CMAKE_COMMAND}
            -E copy_if_different 
            ${HDFJAVA_EXAMPLES_SOURCE_DIR}/testfiles/examples.groups.${example}.txt
            ${HDFJAVA_EXAMPLES_GROUPS_BINARY_DIR}/${example}.txt  
    )
    IF (NOT "${last_test}" STREQUAL "")
      SET_TESTS_PROPERTIES (groups-${example}-copy-objects PROPERTIES DEPENDS ${last_test})
    ENDIF (NOT "${last_test}" STREQUAL "")
    SET (last_test "groups-${example}-copy-objects")
    ADD_H5_TEST (${example} 0)
  ENDFOREACH (example ${HDF_JAVA_EXAMPLES})

ENDIF (BUILD_TESTING)
   