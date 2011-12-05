cmake_minimum_required (VERSION 2.8.6)
PROJECT (HDFJAVA_EXAMPLES_DATATYPES Java)

SET(CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES(
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

SET (CMAKE_JAVA_INCLUDE_PATH ${HDFJAVA_JARS_TO_EXPORT})

SET (HDF_JAVA_EXAMPLES
    H5Ex_T_Array
    H5Ex_T_ArrayAttribute
    H5Ex_T_Bit
    H5Ex_T_BitAttribute
    H5Ex_T_Commit
    H5Ex_T_Compound
    H5Ex_T_CompoundAttribute
    H5Ex_T_Float
    H5Ex_T_FloatAttribute
    H5Ex_T_Integer
    H5Ex_T_IntegerAttribute
    H5Ex_T_ObjectReference
    H5Ex_T_ObjectReferenceAttribute
    H5Ex_T_Opaque
    H5Ex_T_OpaqueAttribute
    H5Ex_T_String
    H5Ex_T_StringAttribute
    H5Ex_T_VLString
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

MACRO (ADD_H5_TEST resultfile resultcode)
  ADD_TEST (
      NAME datatypes-${resultfile}
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
          -D "TEST_PROGRAM=examples.datatypes.${resultfile}"
          -D "TEST_ARGS:STRING=${ARGN}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${resultfile}_JAR_FILE}"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_RUNTIME_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_EXAMPLES_BINARY_DIR}"
          -D "TEST_OUTPUT=${HDFJAVA_EXAMPLES_DATATYPES_BINARY_DIR}/${resultfile}.out"
          -D "TEST_EXPECT=${resultcode}"
          -D "TEST_REFERENCE=${HDFJAVA_EXAMPLES_DATATYPES_BINARY_DIR}/${resultfile}.txt"
          -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
  )
  IF (NOT "${last_test}" STREQUAL "")
    SET_TESTS_PROPERTIES (datatypes-${resultfile} PROPERTIES DEPENDS ${last_test})
  ENDIF (NOT "${last_test}" STREQUAL "")
  SET (last_test "datatypes-${resultfile}")
ENDMACRO (ADD_H5_TEST file)

IF (BUILD_TESTING)

  FOREACH (example ${HDF_JAVA_EXAMPLES})
    ADD_TEST (
        NAME datatypes-${example}-clearall-objects
        COMMAND    ${CMAKE_COMMAND}
            -E remove
            ${example}.h5
            ${example}.out
            ${example}.out.err
    )
    IF (NOT "${last_test}" STREQUAL "")
      SET_TESTS_PROPERTIES (datatypes-${example}-clearall-objects PROPERTIES DEPENDS ${last_test})
    ENDIF (NOT "${last_test}" STREQUAL "")
    ADD_TEST (
        NAME datatypes-${example}-copy-objects
        COMMAND    ${CMAKE_COMMAND}
            -E copy_if_different 
            ${HDFJAVA_EXAMPLES_SOURCE_DIR}/testfiles/examples.datatypes.${example}.txt
            ${HDFJAVA_EXAMPLES_DATATYPES_BINARY_DIR}/${example}.txt  
    )
    SET_TESTS_PROPERTIES (datatypes-${example}-copy-objects PROPERTIES DEPENDS datatypes-${example}-clearall-objects)
    SET (last_test "datatypes-${example}-copy-objects")
    ADD_H5_TEST (${example} 0)
  ENDFOREACH (example ${HDF_JAVA_EXAMPLES})

ENDIF (BUILD_TESTING)
   