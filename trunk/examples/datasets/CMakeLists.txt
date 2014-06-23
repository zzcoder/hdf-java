cmake_minimum_required (VERSION 2.8.10)
PROJECT (HDFJAVA_EXAMPLES_DATASETS Java)

set (CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES (
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

set (HDF_JAVA_EXAMPLES
    H5Ex_D_Alloc
    H5Ex_D_Checksum
    H5Ex_D_Chunk
    H5Ex_D_Compact
    H5Ex_D_External
    H5Ex_D_FillValue
    H5Ex_D_Gzip
    H5Ex_D_Hyperslab
    H5Ex_D_ReadWrite
    H5Ex_D_Shuffle
    H5Ex_D_Szip
    H5Ex_D_UnlimitedAdd
    H5Ex_D_UnlimitedGzip
    H5Ex_D_UnlimitedMod
    H5Ex_D_Nbit
    H5Ex_D_Transform
    H5Ex_D_Sofloat
    H5Ex_D_Soint
)

set (HDF_JAVA_OBJECT_EXAMPLES
    H5ObjectEx_D_Alloc
    H5ObjectEx_D_Checksum
    H5ObjectEx_D_Chunk
    H5ObjectEx_D_Compact
    H5ObjectEx_D_External
    H5ObjectEx_D_FillValue
    H5ObjectEx_D_Gzip
    H5ObjectEx_D_Hyperslab
    H5ObjectEx_D_ReadWrite
    H5ObjectEx_D_Shuffle
    H5ObjectEx_D_Szip
    H5ObjectEx_D_UnlimitedAdd
    H5ObjectEx_D_UnlimitedGzip
    H5ObjectEx_D_UnlimitedMod
    H5ObjectEx_D_Nbit
    H5ObjectEx_D_Transform
    H5ObjectEx_D_Sofloat
    H5ObjectEx_D_Soint
)

if (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
else (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
endif (WIN32)

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_HDF5_JARS}")

set (CMAKE_JAVA_CLASSPATH ".")
foreach (HDFJAVA_JAR ${CMAKE_JAVA_INCLUDE_PATH})
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_JAR}")
endforeach (HDFJAVA_JAR)

foreach (example ${HDF_JAVA_EXAMPLES})
  file (WRITE ${PROJECT_BINARY_DIR}/${example}_Manifest.txt
  "Main-Class: examples.datasets.${example}
"
  )
  add_jar (${example} MANIFEST ${PROJECT_BINARY_DIR}/${example}_Manifest.txt ${example}.java)
  get_target_property (${example}_JAR_FILE ${example} JAR_FILE)
#  install_jar (${example} ${HJAVA_INSTALL_DATA_DIR}/examples examples)
  get_target_property (${example}_CLASSPATH ${example} CLASSDIR)
  add_dependencies (${example} ${HDFJAVA_NCSA_HDF5_LIB_TARGET})
endforeach (example ${HDF_JAVA_EXAMPLES})

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_HDF5_JARS};${HDFJAVA_HDF4_JARS};${HDFJAVA_OBJECT_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_NOP_JAR}")

set (CMAKE_JAVA_CLASSPATH ".")
foreach (HDFJAVA_JAR ${CMAKE_JAVA_INCLUDE_PATH})
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFJAVA_JAR}")
endforeach (HDFJAVA_JAR)

foreach (example ${HDF_JAVA_OBJECT_EXAMPLES})
  file (WRITE ${PROJECT_BINARY_DIR}/${example}_Manifest.txt
  "Main-Class: examples.datasets.${example}
"
  )
  add_jar (${example} MANIFEST ${PROJECT_BINARY_DIR}/${example}_Manifest.txt ${example}.java)
  get_target_property (${example}_JAR_FILE ${example} JAR_FILE)
#  install_jar (${example} ${HJAVA_INSTALL_DATA_DIR}/examples examples)
  get_target_property (${example}_CLASSPATH ${example} CLASSDIR)
  add_dependencies (${example} ${HDFJAVA_NCSA_H5_LIB_TARGET})
endforeach (example ${HDF_JAVA_OBJECT_EXAMPLES})

MACRO (ADD_H5_TEST resultfile resultcode)
  add_test (
      NAME datasets-${resultfile}
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${${resultfile}_JAR_FILE}"
          -D "TEST_ARGS:STRING=${ARGN}"
          -D "TEST_PROGRAM=examples.datasets.${resultfile}"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_EXAMPLES_BINARY_DIR}"
          -D "TEST_OUTPUT=datasets/${resultfile}.out"
          -D "TEST_EXPECT=${resultcode}"
          -D "TEST_REFERENCE=datasets/${resultfile}.txt"
          -P "${HDF_RESOURCES_DIR}/runTest.cmake"
  )
  if (NOT "${last_test}" STREQUAL "")
    set_tests_properties (datasets-${resultfile} PROPERTIES DEPENDS ${last_test})
  endif (NOT "${last_test}" STREQUAL "")
  set (last_test "datasets-${resultfile}")
ENDMACRO (ADD_H5_TEST file)

if (BUILD_TESTING)

  foreach (example ${HDF_JAVA_EXAMPLES})
    if (${example} STREQUAL "H5Ex_D_External")
      add_test (
          NAME datasets-${example}-clearall-objects
          COMMAND    ${CMAKE_COMMAND}
              -E remove
              ${HDFJAVA_EXAMPLES_BINARY_DIR}/${example}.h5
              ${HDFJAVA_EXAMPLES_BINARY_DIR}/${example}.data
              ${example}.out
              ${example}.out.err
      )
    else (${example} STREQUAL "H5Ex_D_External")
      add_test (
          NAME datasets-${example}-clearall-objects
          COMMAND    ${CMAKE_COMMAND}
              -E remove
              ${HDFJAVA_EXAMPLES_BINARY_DIR}/${example}.h5
              ${example}.out
              ${example}.out.err
      )
    endif (${example} STREQUAL "H5Ex_D_External")
    if (NOT "${last_test}" STREQUAL "")
      set_tests_properties (datasets-${example}-clearall-objects PROPERTIES DEPENDS ${last_test})
    endif (NOT "${last_test}" STREQUAL "")
    add_test (
        NAME datasets-${example}-copy-objects
        COMMAND    ${CMAKE_COMMAND}
            -E copy_if_different 
            ${HDFJAVA_EXAMPLES_SOURCE_DIR}/testfiles/examples.datasets.${example}.txt
            ${HDFJAVA_EXAMPLES_DATASETS_BINARY_DIR}/${example}.txt  
    )
    set_tests_properties (datasets-${example}-copy-objects PROPERTIES DEPENDS datasets-${example}-clearall-objects)
    set (last_test "datasets-${example}-copy-objects")
    ADD_H5_TEST (${example} 0)
  endforeach (example ${HDF_JAVA_EXAMPLES})

  foreach (example ${HDF_JAVA_OBJECT_EXAMPLES})
    if (${example} STREQUAL "H5ObjectEx_D_External")
      add_test (
          NAME datasets-${example}-clearall-objects
          COMMAND    ${CMAKE_COMMAND}
              -E remove
              ${HDFJAVA_EXAMPLES_BINARY_DIR}/${example}.h5
              ${HDFJAVA_EXAMPLES_BINARY_DIR}/${example}.data
              ${example}.out
              ${example}.out.err
      )
    else (${example} STREQUAL "H5ObjectEx_D_External")
      add_test (
          NAME datasets-${example}-clearall-objects
          COMMAND    ${CMAKE_COMMAND}
              -E remove
              ${HDFJAVA_EXAMPLES_BINARY_DIR}/${example}.h5
              ${example}.out
              ${example}.out.err
      )
    endif (${example} STREQUAL "H5ObjectEx_D_External")
    if (NOT "${last_test}" STREQUAL "")
      set_tests_properties (datasets-${example}-clearall-objects PROPERTIES DEPENDS ${last_test})
    endif (NOT "${last_test}" STREQUAL "")
    add_test (
        NAME datasets-${example}-copy-objects
        COMMAND    ${CMAKE_COMMAND}
            -E copy_if_different 
            ${HDFJAVA_EXAMPLES_SOURCE_DIR}/testfiles/examples.datasets.${example}.txt
            ${HDFJAVA_EXAMPLES_DATASETS_BINARY_DIR}/${example}.txt  
    )
    set_tests_properties (datasets-${example}-copy-objects PROPERTIES DEPENDS datasets-${example}-clearall-objects)
    set (last_test "datasets-${example}-copy-objects")
    ADD_H5_TEST (${example} 0)
  endforeach (example ${HDF_OBJECT_JAVA_EXAMPLES})

endif (BUILD_TESTING)
   