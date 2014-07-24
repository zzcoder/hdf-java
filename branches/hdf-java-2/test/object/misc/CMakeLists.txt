cmake_minimum_required (VERSION 2.8.10)
PROJECT (HDFJAVA_TEST_OBJECT_MISC Java)

set (CMAKE_VERBOSE_MAKEFILE 1)

include_directories (
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

###########################################################################
# TestH5Object
###########################################################################

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_OBJECT_JARS};${HDFJAVA_HDF5_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

file (WRITE ${PROJECT_BINARY_DIR}/TestH5Object_Manifest.txt
"Main-Class: test.object.misc.TestH5Object
"
)
add_jar (TestH5Object MANIFEST ${PROJECT_BINARY_DIR}/TestH5Object_Manifest.txt TestH5Object.java)
get_target_property (TestH5Object_JAR_FILE TestH5Object JAR_FILE)
add_dependencies (TestH5Object ${HDFJAVA_NCSA_H5_LIB_TARGET})

if (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
else (WIN32)
  set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
endif (WIN32)

set (CMAKE_JAVA_CLASSPATH ".")
foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
endforeach (CMAKE_INCLUDE_PATH)
set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${TestH5Object_JAR_FILE}")

add_test (
    NAME TESTH5OBJECT-object-misc
    COMMAND "${CMAKE_COMMAND}"
        -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
        -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
        -D "TEST_ARGS:STRING="
        -D "TEST_PROGRAM=test.object.misc.TestH5Object"
        -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
        -D "TEST_FOLDER=${HDFJAVA_TEST_OBJECT_MISC_BINARY_DIR}"
        -D "TEST_OUTPUT=TestH5Object.out"
        -D "TEST_EXPECT=0"
        -D "TEST_REFERENCE=TestH5Object.txt"
        -D "TEST_SKIP_COMPARE=TRUE"
        -P "${HDF_RESOURCES_DIR}/runTest.cmake"
)

###########################################################################
# TestH5Table
###########################################################################

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_OBJECT_JARS};${HDFJAVA_HDF5_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

file (WRITE ${PROJECT_BINARY_DIR}/TestH5Table_Manifest.txt
"Main-Class: test.object.misc.TestH5Table
"
)
add_jar (TestH5Table MANIFEST ${PROJECT_BINARY_DIR}/TestH5Table_Manifest.txt TestH5Table.java)
get_target_property (TestH5Table_JAR_FILE TestH5Table JAR_FILE)
add_dependencies (TestH5Table ${HDFJAVA_NCSA_H5_LIB_TARGET})

if (HDFJAVA_TEST_H5TABLE)
  if (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
  else (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
  endif (WIN32)

  set (CMAKE_JAVA_CLASSPATH ".")
  foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
    set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
  endforeach (CMAKE_INCLUDE_PATH)
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${TestH5Table_JAR_FILE}")

  add_test (
      NAME TESTH5TABLE-object-misc
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
          -D "TEST_ARGS:STRING="
          -D "TEST_PROGRAM=test.object.misc.TestH5Table"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_TEST_OBJECT_MISC_BINARY_DIR}"
          -D "TEST_OUTPUT=TestH5Table.out"
          -D "TEST_EXPECT=0"
          -D "TEST_REFERENCE=TestH5Table.txt"
          -D "TEST_SKIP_COMPARE=TRUE"
          -P "${HDF_RESOURCES_DIR}/runTest.cmake"
  )
endif (HDFJAVA_TEST_H5TABLE)

###########################################################################
# TestDebugHDF
###########################################################################

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_OBJECT_JARS};${HDFJAVA_HDF4_JARS};${HDFJAVA_HDF5_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

file (WRITE ${PROJECT_BINARY_DIR}/TestDebugHDF_Manifest.txt
"Main-Class: test.object.misc.DebugHDF
"
)
add_jar (TestDebugHDF MANIFEST ${PROJECT_BINARY_DIR}/TestDebugHDF_Manifest.txt DebugHDF.java)
get_target_property (TestDebugHDF_JAR_FILE TestDebugHDF JAR_FILE)
add_dependencies (TestDebugHDF ${HDFJAVA_NCSA_H5_LIB_TARGET} ${HDFJAVA_NCSA_H4_LIB_TARGET})

if (HDFJAVA_TEST_DEBUGHDF)
  if (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
  else (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
  endif (WIN32)

  set (CMAKE_JAVA_CLASSPATH ".")
  foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
    set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
  endforeach (CMAKE_INCLUDE_PATH)
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${TestDebugHDF_JAR_FILE}")

  add_test (
      NAME TESTDEBUGHDF-object-misc
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
          -D "TEST_ARGS:STRING="
          -D "TEST_PROGRAM=test.object.misc.DebugHDF"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_TEST_OBJECT_MISC_BINARY_DIR}"
          -D "TEST_OUTPUT=DebugHDF.out"
          -D "TEST_EXPECT=0"
          -D "TEST_REFERENCE=DebugHDF.txt"
          -D "TEST_SKIP_COMPARE=TRUE"
          -P "${HDF_RESOURCES_DIR}/runTest.cmake"
  )
endif (HDFJAVA_TEST_DEBUGHDF)

###########################################################################
# TestH5File
###########################################################################

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_OBJECT_JARS};${HDFJAVA_HDF5_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

file (WRITE ${PROJECT_BINARY_DIR}/TestH5File_Manifest.txt
"Main-Class: test.object.misc.TestH5File
"
)
add_jar (TestH5File MANIFEST ${PROJECT_BINARY_DIR}/TestH5File_Manifest.txt TestH5File.java)
get_target_property (TestH5File_JAR_FILE TestH5File JAR_FILE)
add_dependencies (TestH5File ${HDFJAVA_NCSA_H5_LIB_TARGET})

if (HDFJAVA_TEST_H5FILE)
  if (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
  else (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
  endif (WIN32)

  set (CMAKE_JAVA_CLASSPATH ".")
  foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
    set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
  endforeach (CMAKE_INCLUDE_PATH)
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${TestH5File_JAR_FILE}")

  add_test (
      NAME TESTH5FILE-object-misc
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
          -D "TEST_ARGS:STRING="
          -D "TEST_PROGRAM=test.object.misc.TestH5File"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_TEST_OBJECT_MISC_BINARY_DIR}"
          -D "TEST_OUTPUT=TestH5File.out"
          -D "TEST_EXPECT=0"
          -D "TEST_REFERENCE=TestH5File.txt"
          -D "TEST_SKIP_COMPARE=TRUE"
          -P "${HDF_RESOURCES_DIR}/runTest.cmake"
  )
endif (HDFJAVA_TEST_H5FILE)

###########################################################################
# TestH5MemoryLeak
###########################################################################

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_OBJECT_JARS};${HDFJAVA_HDF5_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

file (WRITE ${PROJECT_BINARY_DIR}/TestH5MemoryLeak_Manifest.txt
"Main-Class: test.object.misc.TestH5MemoryLeak
"
)
add_jar (TestH5MemoryLeak MANIFEST ${PROJECT_BINARY_DIR}/TestH5MemoryLeak_Manifest.txt TestH5MemoryLeak.java)
get_target_property (TestH5MemoryLeak_JAR_FILE TestH5MemoryLeak JAR_FILE)
add_dependencies (TestH5MemoryLeak ${HDFJAVA_NCSA_H5_LIB_TARGET})

if (HDFJAVA_TEST_MEMORYLEAK)
  if (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
  else (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
  endif (WIN32)

  set (CMAKE_JAVA_CLASSPATH ".")
  foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
    set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
  endforeach (CMAKE_INCLUDE_PATH)
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${TestH5MemoryLeak_JAR_FILE}")

  add_test (
      NAME TESTMEMORYLEAK-object-misc
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
          -D "TEST_ARGS:STRING="
          -D "TEST_PROGRAM=test.object.misc.TestH5MemoryLeak"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_TEST_OBJECT_MISC_BINARY_DIR}"
          -D "TEST_OUTPUT=TestH5MemoryLeak.out"
          -D "TEST_EXPECT=0"
          -D "TEST_REFERENCE=TestH5MemoryLeak.txt"
          -D "TEST_SKIP_COMPARE=TRUE"
          -P "${HDF_RESOURCES_DIR}/runTest.cmake"
  )
endif (HDFJAVA_TEST_MEMORYLEAK)

###########################################################################
# TestH4File
###########################################################################

set (CMAKE_JAVA_INCLUDE_PATH "${HDFJAVA_OBJECT_JARS};${HDFJAVA_HDF4_JARS};${HDFJAVA_LOGGING_JAR};${HDFJAVA_LOGGING_SIMPLE_JAR}")

file (WRITE ${PROJECT_BINARY_DIR}/TestH4File_Manifest.txt
"Main-Class: test.object.misc.TestH4File
"
)
add_jar (TestH4File MANIFEST ${PROJECT_BINARY_DIR}/TestH4File_Manifest.txt TestH4File.java)
get_target_property (TestH4File_JAR_FILE TestH4File JAR_FILE)
add_dependencies (TestH4File ${HDFJAVA_NCSA_H4_LIB_TARGET})

if (HDFJAVA_TEST_H4FILE)
  if (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
  else (WIN32)
    set (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
  endif (WIN32)

  set (CMAKE_JAVA_CLASSPATH ".")
  foreach (CMAKE_INCLUDE_PATH ${CMAKE_JAVA_INCLUDE_PATH})
    set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${CMAKE_INCLUDE_PATH}")
  endforeach (CMAKE_INCLUDE_PATH)
  set (CMAKE_JAVA_CLASSPATH "${CMAKE_JAVA_CLASSPATH}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${TestH4File_JAR_FILE}")

  add_test (
      NAME TESTH4FILE-object-misc
      COMMAND "${CMAKE_COMMAND}"
          -D "TEST_TESTER=${CMAKE_Java_RUNTIME};${CMAKE_Java_RUNTIME_FLAGS}"
          -D "TEST_CLASSPATH:STRING=${CMAKE_JAVA_CLASSPATH}"
          -D "TEST_ARGS:STRING="
          -D "TEST_PROGRAM=test.object.misc.TestH4File"
          -D "TEST_LIBRARY_DIRECTORY=${CMAKE_TEST_OUTPUT_DIRECTORY}"
          -D "TEST_FOLDER=${HDFJAVA_TEST_OBJECT_MISC_BINARY_DIR}"
          -D "TEST_OUTPUT=TestH4File.out"
          -D "TEST_EXPECT=0"
          -D "TEST_REFERENCE=TestH4File.txt"
          -D "TEST_SKIP_COMPARE=TRUE"
          -P "${HDF_RESOURCES_DIR}/runTest.cmake"
  )
endif (HDFJAVA_TEST_H4FILE)
