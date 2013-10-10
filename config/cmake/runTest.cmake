# runTest.cmake executes a command and captures the output in a file. File is then compared
# against a reference file. Exit status of command can also be compared.
cmake_policy(SET CMP0007 NEW)

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

IF (NOT TEST_ERRREF)
  SET (ERROR_APPEND 1)
ENDIF (NOT TEST_ERRREF)

IF (NOT TEST_LOG_LEVEL)
  SET (LOG_LEVEL "info")
ELSE (NOT TEST_LOG_LEVEL)
  SET (LOG_LEVEL "${TEST_LOG_LEVEL}")
ENDIF (NOT TEST_LOG_LEVEL)

MESSAGE (STATUS "COMMAND: ${TEST_TESTER} -Xmx1024M -Djava.library.path=\"${TEST_LIBRARY_DIRECTORY}\" -cp \"${TEST_CLASSPATH}\" ${TEST_ARGS} ${TEST_PROGRAM} ${ARGN}")

IF (WIN32 AND NOT MINGW)
  SET (ENV{PATH} "$ENV{PATH}\\;${TEST_LIBRARY_DIRECTORY}")
ENDIF (WIN32 AND NOT MINGW)

# run the test program, capture the stdout/stderr and the result var
EXECUTE_PROCESS (
    COMMAND ${TEST_TESTER} -Xmx1024M
    -Dorg.slf4j.simpleLogger.defaultLog=${LOG_LEVEL}
    -Djava.library.path=${TEST_LIBRARY_DIRECTORY}
    -cp "${TEST_CLASSPATH}" ${TEST_ARGS} ${TEST_PROGRAM}
    ${ARGN}
    WORKING_DIRECTORY ${TEST_FOLDER}
    RESULT_VARIABLE TEST_RESULT
    OUTPUT_FILE ${TEST_OUTPUT}
    ERROR_FILE ${TEST_OUTPUT}.err
    ERROR_VARIABLE TEST_ERROR
)

MESSAGE (STATUS "COMMAND Result: ${TEST_RESULT}")

IF (EXISTS ${TEST_FOLDER}/${TEST_OUTPUT}.err)
  FILE (READ ${TEST_FOLDER}/${TEST_OUTPUT}.err TEST_STREAM)
  IF (TEST_MASK_FILE)
    STRING(REGEX REPLACE "CurrentDir is [^\n]+\n" "CurrentDir is (dir name)\n" TEST_STREAM "${TEST_STREAM}") 
  ENDIF (TEST_MASK_FILE)

  IF (ERROR_APPEND)
    FILE (APPEND ${TEST_FOLDER}/${TEST_OUTPUT} "${TEST_STREAM}") 
  ELSE (ERROR_APPEND)
    FILE (WRITE ${TEST_FOLDER}/${TEST_OUTPUT}.err "${TEST_STREAM}")
  ENDIF (ERROR_APPEND)
ENDIF (EXISTS ${TEST_FOLDER}/${TEST_OUTPUT}.err)

# if the return value is !=0 bail out
IF (NOT ${TEST_RESULT} STREQUAL ${TEST_EXPECT})
  MESSAGE (STATUS "ERROR OUTPUT: ${TEST_STREAM}")
  MESSAGE (FATAL_ERROR "Failed: Test program ${TEST_PROGRAM} exited != 0.\n${TEST_ERROR}")
ENDIF (NOT ${TEST_RESULT} STREQUAL ${TEST_EXPECT})

MESSAGE (STATUS "COMMAND Error: ${TEST_ERROR}")

IF (NOT TEST_SKIP_COMPARE)
  IF (WIN32 AND NOT MINGW)
    FILE (READ ${TEST_FOLDER}/${TEST_REFERENCE} TEST_STREAM)
    FILE (WRITE ${TEST_FOLDER}/${TEST_REFERENCE} "${TEST_STREAM}")
  ENDIF (WIN32 AND NOT MINGW)

  # now compare the output with the reference
  EXECUTE_PROCESS (
      COMMAND ${CMAKE_COMMAND} -E compare_files ${TEST_FOLDER}/${TEST_OUTPUT} ${TEST_FOLDER}/${TEST_REFERENCE}
      RESULT_VARIABLE TEST_RESULT
  )
  IF (NOT ${TEST_RESULT} STREQUAL 0)
  SET (TEST_RESULT 0)
  FILE (STRINGS ${TEST_FOLDER}/${TEST_OUTPUT} test_act)
  LIST (LENGTH test_act len_act)
  FILE (STRINGS ${TEST_FOLDER}/${TEST_REFERENCE} test_ref)
  LIST (LENGTH test_ref len_ref)
  IF (NOT ${len_act} STREQUAL "0")
    MATH (EXPR _FP_LEN "${len_ref} - 1")
    FOREACH (line RANGE 0 ${_FP_LEN})
      LIST (GET test_act ${line} str_act)
      LIST (GET test_ref ${line} str_ref)
      IF (NOT "${str_act}" STREQUAL "${str_ref}")
        IF (NOT "${str_act}" STREQUAL "")
          SET (TEST_RESULT 1)
          MESSAGE ("line = ${line}\n***ACTUAL: ${str_act}\n****REFER: ${str_ref}\n")
         ENDIF (NOT "${str_act}" STREQUAL "")
      ENDIF (NOT "${str_act}" STREQUAL "${str_ref}")
    ENDFOREACH (line RANGE 0 ${_FP_LEN})
  ENDIF (NOT ${len_act} STREQUAL "0")
  IF (NOT ${len_act} STREQUAL ${len_ref})
    SET (TEST_RESULT 1)
  ENDIF (NOT ${len_act} STREQUAL ${len_ref})
  ENDIF (NOT ${TEST_RESULT} STREQUAL 0)

  MESSAGE (STATUS "COMPARE Result: ${TEST_RESULT}")

  # again, if return value is !=0 scream and shout
  IF (NOT ${TEST_RESULT} STREQUAL 0)
    MESSAGE (FATAL_ERROR "Failed: The output of ${TEST_OUTPUT} did not match ${TEST_REFERENCE}")
  ENDIF (NOT ${TEST_RESULT} STREQUAL 0)
  
  IF (TEST_ERRREF)
    IF (WIN32 AND NOT MINGW)
      FILE (READ ${TEST_FOLDER}/${TEST_ERRREF} TEST_STREAM)
      FILE (WRITE ${TEST_FOLDER}/${TEST_ERRREF} "${TEST_STREAM}")
    ENDIF (WIN32 AND NOT MINGW)

    # now compare the error output with the error reference
    EXECUTE_PROCESS (
        COMMAND ${CMAKE_COMMAND} -E compare_files ${TEST_FOLDER}/${TEST_OUTPUT}.err ${TEST_FOLDER}/${TEST_ERRREF}
        RESULT_VARIABLE TEST_RESULT
    )
    IF (NOT ${TEST_RESULT} STREQUAL 0)
    SET (TEST_RESULT 0)
    FILE (STRINGS ${TEST_FOLDER}/${TEST_OUTPUT}.err test_act)
    LIST (LENGTH test_act len_act)
    FILE (STRINGS ${TEST_FOLDER}/${TEST_ERRREF} test_ref)
    LIST (LENGTH test_ref len_ref)
    MATH (EXPR _FP_LEN "${len_ref} - 1")
    IF (NOT ${len_act} STREQUAL "0")
      MATH (EXPR _FP_LEN "${len_ref} - 1")
      FOREACH (line RANGE 0 ${_FP_LEN})
        LIST (GET test_act ${line} str_act)
        LIST (GET test_ref ${line} str_ref)
        IF (NOT "${str_act}" STREQUAL "${str_ref}")
          IF (NOT "${str_act}" STREQUAL "")
            SET (TEST_RESULT 1)
            MESSAGE ("line = ${line}\n***ACTUAL: ${str_act}\n****REFER: ${str_ref}\n")
           ENDIF (NOT "${str_act}" STREQUAL "")
        ENDIF (NOT "${str_act}" STREQUAL "${str_ref}")
      ENDFOREACH (line RANGE 0 ${_FP_LEN})
    ENDIF (NOT ${len_act} STREQUAL "0")
    IF (NOT ${len_act} STREQUAL ${len_ref})
      SET (TEST_RESULT 1)
    ENDIF (NOT ${len_act} STREQUAL ${len_ref})
    ENDIF (NOT ${TEST_RESULT} STREQUAL 0)

    MESSAGE (STATUS "COMPARE Result: ${TEST_RESULT}")

    # again, if return value is !=0 scream and shout
    IF (NOT ${TEST_RESULT} STREQUAL 0)
      MESSAGE (FATAL_ERROR "Failed: The error output of ${TEST_OUTPUT}.err did not match ${TEST_ERRREF}")
    ENDIF (NOT ${TEST_RESULT} STREQUAL 0)
  ENDIF (TEST_ERRREF)
ENDIF (NOT TEST_SKIP_COMPARE)

IF (TEST_GREP_COMPARE)
  # now grep the output with the reference
  FILE (READ ${TEST_FOLDER}/${TEST_OUTPUT} TEST_STREAM)

  # TEST_REFERENCE should always be matched
  STRING(REGEX MATCH "${TEST_REFERENCE}" TEST_MATCH ${TEST_STREAM}) 
  STRING(COMPARE EQUAL "${TEST_REFERENCE}" "${TEST_MATCH}" TEST_RESULT) 
  IF (${TEST_RESULT} STREQUAL "0")
    MESSAGE (FATAL_ERROR "Failed: The output of ${TEST_PROGRAM} did not contain ${TEST_REFERENCE}")
  ENDIF (${TEST_RESULT} STREQUAL "0")

  STRING(REGEX MATCH "${TEST_FILTER}" TEST_MATCH ${TEST_STREAM}) 
  IF (${TEST_EXPECT} STREQUAL "1")
    # TEST_EXPECT (1) interperts TEST_FILTER as NOT to match
    STRING(LENGTH "${TEST_MATCH}" TEST_RESULT) 
    IF (NOT ${TEST_RESULT} STREQUAL "0")
      MESSAGE (FATAL_ERROR "Failed: The output of ${TEST_PROGRAM} did contain ${TEST_FILTER}")
    ENDIF (NOT ${TEST_RESULT} STREQUAL "0")
  ENDIF (${TEST_EXPECT} STREQUAL "0")
ENDIF (TEST_GREP_COMPARE)

# everything went fine...
MESSAGE ("${TEST_PROGRAM} Passed")

