
#hdf5 jni names: libjhdf5.jnilib
install_name_tool -change /afs/.ncsa.uiuc.edu/projects/hdf/java/java9/xcao/hdf5-1.6.4-build/pommier_ibm/lib/libhdf5.0.dylib libhdf5.0.dylib libjhdf5.jnilib
install_name_tool -change /afs/ncsa.uiuc.edu/projects/hdf/packages/szip/Darwin-7.7/lib/libsz.2.dylib libsz.2.dylib libjhdf5.jnilib
install_name_tool -change /usr/lib/libz.1.dylib libz.1.dylib libjhdf5.jnilib

# hdf5 lib names: libhdf5.dylib
install_name_tool -change /afs/ncsa.uiuc.edu/projects/hdf/packages/szip/Darwin-7.7/lib/libsz.2.dylib libsz.2.dylib libhdf5.dylib
install_name_tool -change /usr/lib/libz.1.dylib libz.1.dylib libhdf5.dylib

#hdf5 jni names: libjhdf.jnilib
install_name_tool -change /afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/hdf4_shared/lib/libmfhdf.0.dylib libmfhdf.0.dylib libjhdf.jnilib
install_name_tool -change /afs/.ncsa.uiuc.edu/projects/hdf/java/java8/xcao/hdf4_shared/lib/libdf.0.dylib libdf.0.dylib libjhdf.jnilib
install_name_tool -change /afs/ncsa.uiuc.edu/projects/hdf/packages/szip/Darwin-7.7/lib/libsz.2.dylib libsz.2.dylib libjhdf.jnilib
install_name_tool -change /sw/lib/libjpeg.62.dylib libjpeg.62.dylib libjhdf.jnilib
install_name_tool -change /usr/lib/libz.1.dylib libz.1.dylib libjhdf.jnilib

#hdf5 lib names: libdf.dylib
install_name_tool -change /sw/lib/libjpeg.62.dylib libjpeg.62.dylib libdf.dylib
install_name_tool -change /sw/lib/libjpeg.62.dylib libjpeg.62.dylib libmfhdf.dylib
install_name_tool -change /usr/lib/libz.1.dylib libz.1.dylib libdf.dylib
install_name_tool -change /usr/lib/libz.1.dylib libz.1.dylib libmfhdf.dylib



