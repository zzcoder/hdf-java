########################################################
#  Include file for user options
########################################################

#-----------------------------------------------------------------------------
# Option to Build with User Defined Values
#-----------------------------------------------------------------------------
MACRO (MACRO_USER_DEFINED_LIBS)
  set (USER_DEFINED_VALUE "FALSE")
ENDMACRO (MACRO_USER_DEFINED_LIBS)

#-------------------------------------------------------------------------------
option (BUILD_USER_DEFINED_LIBS "Build With User Defined Values" OFF)
if (BUILD_USER_DEFINED_LIBS)
  MACRO_USER_DEFINED_LIBS ()
endif (BUILD_USER_DEFINED_LIBS)
 