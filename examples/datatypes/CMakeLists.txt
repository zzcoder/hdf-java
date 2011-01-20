cmake_minimum_required (VERSION 2.8.3)
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

FOREACH (example ${HDF_JAVA_EXAMPLES})
  ADD_JAR (${example} ${example}.java)
#  INSTALL_JAR (${example} ${HJAVA_INSTALL_DATA_DIR}/examples examples)
  ADD_DEPENDENCIES (${example} ${HDFJAVA_NCSA_HDF5_LIB_TARGET})
ENDFOREACH (example ${HDF_JAVA_EXAMPLES})

MACRO (ADD_H5_TEST resultfile resultcode)
  ADD_TEST (
      NAME datatypes-${resultfile}
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME}"
          -D "TEST_PROGRAM=examples.datatypes.${resultfile}"
          -D "TEST_ARGS:STRING=${ARGN}"
          -D "TEST_CLASSPATH:STRING=${${resultfile}_CLASSPATH}"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_JAVA_LIBRARY_OUTPUT_PATH}"
          -D "TEST_FOLDER=${HDFJAVA_EXAMPLES_BINARY_DIR}"
          -D "TEST_OUTPUT=${HDFJAVA_EXAMPLES_DATATYPES_BINARY_DIR}/${resultfile}.out"
          -D "TEST_EXPECT=${resultcode}"
          -D "TEST_REFERENCE=${HDFJAVA_EXAMPLES_DATATYPES_BINARY_DIR}/${resultfile}.txt"
          -P "${HDFJAVA_RESOURCES_DIR}/runTest.cmake"
  )
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
    ADD_TEST (
        NAME datatypes-${example}-copy-objects
        COMMAND    ${CMAKE_COMMAND}
            -E copy_if_different 
            ${HDFJAVA_EXAMPLES_SOURCE_DIR}/testfiles/examples.datatypes.${example}.txt
            ${HDFJAVA_EXAMPLES_DATATYPES_BINARY_DIR}/${example}.txt  
    )
    ADD_H5_TEST (${example} 0)
  ENDFOREACH (example ${HDF_JAVA_EXAMPLES})

ENDIF (BUILD_TESTING)
   