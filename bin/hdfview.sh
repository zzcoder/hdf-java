#!/bin/csh -f

# Set up default variable values if not supplied by the user.

# where the HDFView is installed
HDFVIEW_HOME=/afs/ncsa.uiuc.edu/projects/hdf/java/java10/mcgrath/verbena/Java
export HDFVIEW_HOME

# where Java is installed (requires jdk1.3 or above)
JAVAPATH=/opt/jdk1.4.1/bin
export JAVAPATH

###############################################################################
#            DO NOT MODIFY BELOW THIS LINE
###############################################################################

CPATH=$HDFVIEW_HOME"/lib/jhdf.jar:"$HDFVIEW_HOME"/lib/jhdf5.jar:"$HDFVIEW_HOME"/lib/jhdfobj.jar:"$HDFVIEW_HOME"/lib/netcdf.jar:"$HDFVIEW_HOME"/lib/fits.jar"

CPATH=$CPATH":"$HDFVIEW_HOME"/lib/jhdf4obj.jar:"$HDFVIEW_HOME"/lib/jhdf5obj.jar:"$HDFVIEW_HOME"/lib/jhdfview.jar"


if test -z "$CLASSPATH"; then
	CLASSPATH=""
fi
CLASSPATH=$CPATH":"$CLASSPATH
export CLASSPATH

if test -n "$JAVAPATH" ; then
	PATH=$JAVAPATH":"$PATH
	export PATH
fi


if test -e /bin/uname; then
   machtype=`/bin/uname -m`
   OS=`/bin/uname -s`
elif test -e /usr/bin/uname; then
   machtype=`/usr/bin/uname -m`
   OS=`/usr/bin/uname -s`
else
   machtype=unknown
   OS=unknown
fi

if test -z "$LD_LIBRARY_PATH" ; then
        LD_LIBRARY_PATH=""
fi

case  $machtype in:
    sun4*)
	LD_LIBRARY_PATH=$HDFVIEW_HOME/lib/solaris:$LD_LIBRARY_PATH
	;;
  i*86)
        LD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/linux:"$LD_LIBRARY_PATH
	;;
  IP*)
	OSREV=`/bin/uname -r`
	LD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/irix-"$OSREV":"$LD_LIBRARY_PATH 
	LD_LIBRARYN32_PATH=$HDFVIEW_HOME"/lib/irix-"$OSREV":"$LD_LIBRARY_PATH
	export LD_LIBRARYN32_PATH
	;;
    *)
	echo "Unknown machine type:  jhv may not work correctly"
        ;;
esac

export LD_LIBRARYN_PATH

$JAVAPATH/java -mx512m ncsa.hdf.view.HDFView -root $HDFVIEW_HOME
