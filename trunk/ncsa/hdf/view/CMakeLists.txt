cmake_minimum_required (VERSION 2.8.3)
PROJECT (HDFJAVA_NCSA_HDF_VIEW)

SET (CMAKE_VERBOSE_MAKEFILE 1)

INCLUDE_DIRECTORIES (
    ${JAVA_NCSA_HDF_HDF4_BINARY_DIR}
    ${JAVA_NCSA_HDF_HDF5_BINARY_DIR}
    ${JAVA_NCSA_HDF_OBJECT_BINARY_DIR}
    ${HDFJAVA_LIB_DIR}
)

SET (HDFJAVA_NCSA_HDF_VIEW_SRCS
    Chart.java
    DataOptionDialog.java
    DataView.java
    DefaultFileFilter.java
    DefaultImageView.java
    DefaultMetaDataView.java
    DefaultPaletteView.java
    DefaultTableView.java
    DefaultTextView.java
    DefaultTreeView.java
    FileConversionDialog.java
    HDFView.java
    HelpView.java
    ImageView.java
    MathConversionDialog.java
    MetaDataView.java
    NewAttributeDialog.java
    NewDatasetDialog.java
    NewDatatypeDialog.java
    NewFileDialog.java
    NewGroupDialog.java
    NewImageDialog.java
    NewLinkDialog.java
    NewTableDataDialog.java
    PaletteView.java
    TableView.java
    TextView.java
    Tools.java
    TreeView.java
    UserOptionsDialog.java
    ViewManager.java
    ViewProperties.java
)
SET (HDFJAVA_NCSA_HDF_VIEW_ICONS
    ncsa/hdf/view/icons/animation.gif
    ncsa/hdf/view/icons/apps.gif
    ncsa/hdf/view/icons/audio.gif
    ncsa/hdf/view/icons/autocontrast.gif
    ncsa/hdf/view/icons/blank.gif
    ncsa/hdf/view/icons/brightness.gif
    ncsa/hdf/view/icons/chart.gif
    ncsa/hdf/view/icons/copy.gif
    ncsa/hdf/view/icons/cut.gif
    ncsa/hdf/view/icons/dataset.gif
    ncsa/hdf/view/icons/datasetA.gif
    ncsa/hdf/view/icons/datatype.gif
    ncsa/hdf/view/icons/datatypeA.gif
    ncsa/hdf/view/icons/fileclose.gif
    ncsa/hdf/view/icons/filenew.gif
    ncsa/hdf/view/icons/fileopen.gif
    ncsa/hdf/view/icons/filesave.gif
    ncsa/hdf/view/icons/first.gif
    ncsa/hdf/view/icons/folderclose.gif
    ncsa/hdf/view/icons/foldercloseA.gif
    ncsa/hdf/view/icons/folderopen.gif
    ncsa/hdf/view/icons/folderopenA.gif
    ncsa/hdf/view/icons/hdf_large.gif
    ncsa/hdf/view/icons/hdf.gif
    ncsa/hdf/view/icons/hdf32.gif
    ncsa/hdf/view/icons/hdf32.icns
    ncsa/hdf/view/icons/hdf32.png
    ncsa/hdf/view/icons/hdf4.gif
    ncsa/hdf/view/icons/hdf5.gif
    ncsa/hdf/view/icons/help.gif
    ncsa/hdf/view/icons/image.gif
    ncsa/hdf/view/icons/imageA.gif
    ncsa/hdf/view/icons/last.gif
    ncsa/hdf/view/icons/link.gif
    ncsa/hdf/view/icons/next.gif
    ncsa/hdf/view/icons/palette.gif
    ncsa/hdf/view/icons/paste.gif
    ncsa/hdf/view/icons/pdf.gif
    ncsa/hdf/view/icons/previous.gif
    ncsa/hdf/view/icons/question.gif
    ncsa/hdf/view/icons/table.gif
    ncsa/hdf/view/icons/tableA.gif
    ncsa/hdf/view/icons/text.gif
    ncsa/hdf/view/icons/textA.gif
    ncsa/hdf/view/icons/txt.gif
    ncsa/hdf/view/icons/url.gif
    ncsa/hdf/view/icons/video.gif
    ncsa/hdf/view/icons/xls.gif
    ncsa/hdf/view/icons/zoomin.gif
    ncsa/hdf/view/icons/zoomout.gif
)

SET (HDFJAVA_NCSA_HDF_VIEW_HTML
    NewAttrHelp.html
    NewDatsetHelp.html
)

SET (CMAKE_JAVA_INCLUDE_PATH 
    ${CMAKE_JAVA_INCLUDE_PATH} 
    ${HDFJAVA_JARS_TO_EXPORT}
)

INCLUDE_DIRECTORIES (${CMAKE_CURRENT_BINARY_DIR}/CMakeFiles/${HDFJAVA_NCSA_VIEW_LIB_CORENAME}.dir/ncsa/hdf/object)

ADD_JAR (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${CMAKE_SOURCE_DIR} ${HDFJAVA_NCSA_HDF_VIEW_SRCS} ${HDFJAVA_NCSA_HDF_VIEW_ICONS})
INSTALL_JAR (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HJAVA_INSTALL_LIB_DIR} apps)
SET_GLOBAL_VARIABLE (HDFJAVA_JARS_TO_EXPORT "${HDFJAVA_JARS_TO_EXPORT};${${HDFJAVA_NCSA_VIEW_LIB_TARGET}_JAR_FILE}")
ADD_DEPENDENCIES (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HDFJAVA_NCSA_HDF_LIB_TARGET})

IF (UNIX)
  SET (HDFVIEW_SCRIPT "hdfview.sh")
ELSE (UNIX)
  SET (HDFVIEW_SCRIPT "hdfview.bat")
ENDIF (UNIX)


IF (WIN32 AND NOT CYGWIN)
  SET (CMAKE_JAVA_INCLUDE_FLAG_SEP ";")
  SET (CMAKE_JAVA_PATH_EXPORT "set PATH=@CMAKE_INSTALL_PREFIX@/lib;%PATH%")
ELSE (WIN32 AND NOT CYGWIN)
  SET (CMAKE_JAVA_INCLUDE_FLAG_SEP ":")
  SET (CMAKE_JAVA_PATH_EXPORT "export LD_LIBRARY_PATH=@CMAKE_INSTALL_PREFIX@/lib:$LD_LIBRARY_PATH")
ENDIF (WIN32 AND NOT CYGWIN)

SET (HDFVIEW_CLASSPATH "${HDFVIEW_CLASSPATH}@CMAKE_INSTALL_PREFIX@/lib")
FOREACH (HCP_JAR ${HDFJAVA_JARS_TO_EXPORT})
  GET_FILENAME_COMPONENT (_HCP_FILE ${HCP_JAR} NAME)
  SET (HDFVIEW_CLASSPATH "@CMAKE_INSTALL_PREFIX@/lib/${_HCP_FILE}${CMAKE_JAVA_INCLUDE_FLAG_SEP}${HDFVIEW_CLASSPATH}")
ENDFOREACH (HCP_JAR ${HDFJAVA_JARS_TO_EXPORT})

FILE (WRITE ${HDFJAVA_NCSA_HDF_VIEW_BINARY_DIR}/${HDFVIEW_SCRIPT}.cmake.in
  "${CMAKE_JAVA_PATH_EXPORT}
  ${CMAKE_Java_RUNTIME} -Xmx1024M -Djava.library.path=\"@CMAKE_INSTALL_PREFIX@/lib\" -Dhdfview.root=\"@CMAKE_INSTALL_PREFIX@/lib\" -cp \"${HDFVIEW_CLASSPATH}\" ncsa.hdf.view.HDFView -root \"@CMAKE_INSTALL_PREFIX@/share\"
  "
)


CONFIGURE_FILE (
    ${HDFJAVA_NCSA_HDF_VIEW_BINARY_DIR}/${HDFVIEW_SCRIPT}.cmake.in
    ${HDFJAVA_NCSA_HDF_VIEW_BINARY_DIR}/${HDFVIEW_SCRIPT} @ONLY
)
  
INSTALL (
    FILES ${HDFJAVA_NCSA_HDF_VIEW_BINARY_DIR}/${HDFVIEW_SCRIPT}
    DESTINATION ${HJAVA_INSTALL_BIN_DIR}
    COMPONENT apps
    PERMISSIONS OWNER_WRITE OWNER_READ OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_EXECUTE
)
