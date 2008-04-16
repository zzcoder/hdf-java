#!/bin/sh

export HDFVIEW_HOME=/home/srb/hdf-java/build
export JAVAPATH=/usr/java/jdk1.5.0_07/bin
export CLASSPATH=$HDFVIEW_HOME"/lib/jhdfobj.jar:"$HDFVIEW_HOME"/lib/h5srb.jar:."

TEST=/usr/bin/test
if [ ! -x /usr/bin/test ]
then
TEST=`which test`
fi


if $TEST -e /bin/uname; then
   os_name=`/bin/uname -s`
elif $TEST -e /usr/bin/uname; then
   os_name=`/usr/bin/uname -s`
else
   os_name=unknown
fi

case  $os_name in
    SunOS)
        LD_LIBRARY_PATH=$HDFVIEW_HOME/lib/solaris:$HDFVIEW_HOME/lib/ext:$LD_LIBRARY_PATH
        ;;
    Linux)
        LD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/linux:"$HDFVIEW_HOME"/lib/ext:"$LD_LIBRARY_PATH
        ;;
    IRIX*)
        OSREV=`/bin/uname -r`
        LD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/irix-6.5:"$HDFVIEW_HOME"/lib/ext:"$LD_LIBRARY_PATH 
        LD_LIBRARYN32_PATH=$HDFVIEW_HOME"/lib/irix-6.5:"$HDFVIEW_HOME"/lib/ext":$LD_LIBRARY_PATH
        export LD_LIBRARYN32_PATH
        ;;
    OSF1)
        LD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/alpha:"$HDFVIEW_HOME"/lib/ext:"$LD_LIBRARY_PATH
        ;;
    AIX)
        LD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/aix:"$HDFVIEW_HOME"/lib/ext:"$LD_LIBRARY_PATH
        ;;
    Darwin)
        DYLD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/macosx:"$HDFVIEW_HOME"/lib/ext:"$DYLD_LIBRARY_PATH
        export DYLD_LIBRARY_PATH
        ;;
    FreeBSD)
        LD_LIBRARY_PATH=$HDFVIEW_HOME"/lib/freebsd:"$HDFVIEW_HOME"/lib/ext:"$LD_LIBRARY_PATH
        ;;
    *)
        echo "Unknown Operating System:  HDFView may not work correctly"
        ;;
esac

export LD_LIBRARY_PATH="/home/srb/h5mod/native:"$LD_LIBRARY_PATH

$JAVAPATH/javac H5SRBTest.java 
$JAVAPATH/java -Djava.library.path=$LD_LIBRARY_PATH H5SRBTest /tempZone/home/rods/test.h5 $1



