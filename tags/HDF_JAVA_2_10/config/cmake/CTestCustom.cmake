SET (CTEST_CUSTOM_MAXIMUM_NUMBER_OF_WARNINGS 1500)

SET (CTEST_CUSTOM_WARNING_EXCEPTION
    ${CTEST_CUSTOM_WARNING_EXCEPTION}
    "note.*expected.*void.*but argument is of type.*volatile"
    "SZIP.src.*:[ \t]*warning"
    "jpeg.src.*:[ \t]*warning"
    "POSIX name for this item is deprecated"
    "disabling jobserver mode"
    "config.cmake.xlatefile.c"
    "warning.*implicit declaration of function"
)
 
