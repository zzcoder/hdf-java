# runTest.cmake executes a command and captures the output in a file. File is then compared
# against a reference file. Exit status of command can also be compared.

# arguments checking
IF (NOT TEST_TESTER)
  MESSAGE (FATAL_ERROR "Require TEST_TESTER to be defined")
ENDIF (NOT TEST_TESTER)
IF (NOT TEST_PROGRAM)
  MESSAGE (FATAL_ERROR "Require TEST_PROGRAM to be defined")
ENDIF (NOT TEST_PROGRAM)
IF (NOT TEST_LIBRARY_DIRECTORY)
  MESSAGE (STATUS "Require TEST_LIBRARY_DIRECTORY to be defined")
ENDIF (NOT TEST_LIBRARY_DIRECTORY)
IF (NOT TEST_FOLDER)
  MESSAGE ( FATAL_ERROR "Require TEST_FOLDER to be defined")
ENDIF (NOT TEST_FOLDER)
IF (NOT TEST_OUTPUT)
  MESSAGE (FATAL_ERROR "Require TEST_OUTPUT to be defined")
ENDIF (NOT TEST_OUTPUT)
IF (NOT TEST_CLASSPATH)
  MESSAGE (STATUS "Require TEST_CLASSPATH to be defined")
ENDIF (NOT TEST_CLASSPATH)
IF (NOT TEST_REFERENCE)
  MESSAGE (FATAL_ERROR "Require TEST_REFERENCE to be defined")
ENDIF (NOT TEST_REFERENCE)

MESSAGE (STATUS "COMMAND: ${TEST_TESTER} -Xmx1024M -Djava.library.path=${TEST_LIBRARY_DIRECTORY} -cp ${TEST_CLASSPATH} ${TEST_PROGRAM}")

# run the test program, capture the stdout/stderr and the result var
EXECUTE_PROCESS (
    COMMAND ${TEST_TESTER} -Xmx1024M
    -Djava.library.path=${TEST_LIBRARY_DIRECTORY}
    -cp "${TEST_CLASSPATH}" ${TEST_PROGRAM}
    ${ARGN}
    WORKING_DIRECTORY ${TEST_FOLDER}
    RESULT_VARIABLE TEST_RESULT
    OUTPUT_FILE ${TEST_OUTPUT}
    ERROR_FILE ${TEST_OUTPUT}.err
    OUTPUT_VARIABLE TEST_ERROR
    ERROR_VARIABLE TEST_ERROR
)

MESSAGE (STATUS "COMMAND Result: ${TEST_RESULT}")

IF (TEST_APPEND)
  FILE (APPEND ${TEST_OUTPUT} "${TEST_APPEND} ${TEST_RESULT}\n") 
ENDIF (TEST_APPEND)

# if the return value is !=0 bail out
IF (NOT ${TEST_RESULT} STREQUAL ${TEST_EXPECT})
  MESSAGE ( FATAL_ERROR "Failed: Test program ${TEST_PROGRAM} exited != 0.\n${TEST_ERROR}")
ENDIF (NOT ${TEST_RESULT} STREQUAL ${TEST_EXPECT})

MESSAGE (STATUS "COMMAND Error: ${TEST_ERROR}")

IF (WIN32 AND NOT MINGW)
  FILE (READ ${TEST_REFERENCE} TEST_STREAM)
  FILE (WRITE ${TEST_REFERENCE} "${TEST_STREAM}")
ENDIF (WIN32 AND NOT MINGW)

# now compare the output with the reference
EXECUTE_PROCESS (
    COMMAND ${CMAKE_COMMAND} -E compare_files ${TEST_OUTPUT} ${TEST_REFERENCE}
    RESULT_VARIABLE TEST_RESULT
)

# again, if return value is !=0 scream and shout
IF (TEST_RESULT)
  MESSAGE (FATAL_ERROR "Failed: The output of ${TEST_PROGRAM} did not match ${TEST_REFERENCE}")
ENDIF (TEST_RESULT)

# everything went fine...
MESSAGE ("Passed: The output of ${TEST_PROGRAM} matches ${TEST_REFERENCE}")

