#!/bin/csh -f

# Set up default variable values if not supplied by the user.

# where the HDFView is installed
set HDFVIEW_HOME=/afs/ncsa.uiuc.edu/projects/hdf/java/java4/mcgrath/verbena/Java

# where Java is installed (requires jdk1.3 or above)
set JAVAPATH=/opt/jdk1.4.1/bin

###############################################################################
#            DO NOT MODIFY BELOW THIS LINE
###############################################################################

set CPATH=$HDFVIEW_HOME"/lib/jhdf.jar:"$HDFVIEW_HOME"/lib/jhdf5.jar:"$HDFVIEW_HOME"/lib/jhdfobj.jar:"$HDFVIEW_HOME"/lib/netcdf.jar"

set CPATH=$CPATH":"$HDFVIEW_HOME"/lib/jhdf4obj.jar:"$HDFVIEW_HOME"/lib/jhdf5obj.jar:"$HDFVIEW_HOME"/lib/jhdfview.jar"


if (${?CLASSPATH} == 0) then
	setenv CLASSPATH ""
endif
setenv CLASSPATH $CPATH":"$CLASSPATH

if (${?JAVAPATH} != 0) then
	setenv PATH $JAVAPATH":"$PATH
endif


if (-e /bin/uname) then
   set machtype=`/bin/uname -m`
   set OS=`/bin/uname -s`
else if (-e /usr/bin/uname) then
   set machtype=`/usr/bin/uname -m`
   set OS=`/usr/bin/uname -s`
else
   set machtype=unknown
   set OS=unknown
endif

if (${?LD_LIBRARY_PATH} == 0) then
        setenv LD_LIBRARY_PATH ""
endif

switch ( $machtype )
    case sun4*:
	setenv LD_LIBRARY_PATH $HDFVIEW_HOME/lib/solaris:$LD_LIBRARY_PATH
	breaksw
  case i*86:
        setenv LD_LIBRARY_PATH $HDFVIEW_HOME"/lib/linux:"$LD_LIBRARY_PATH
	breaksw
  case IP*:
	set OSREV=`/bin/uname -r`
	setenv LD_LIBRARY_PATH $HDFVIEW_HOME"/lib/irix-"$OSREV":"$LD_LIBRARY_PATH 
	setenv LD_LIBRARYN32_PATH $HDFVIEW_HOME"/lib/irix-"$OSREV":"$LD_LIBRARY_PATH
	breaksw
    default:
	echo "Unknown machine type:  jhv may not work correctly"
        breaksw
endsw

$JAVAPATH/java -mx512m ncsa.hdf.view.HDFView -root $HDFVIEW_HOME
