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
    animation.gif
    apps.gif
    audio.gif
    autocontrast.gif
    blank.gif
    brightness.gif
    chart.gif
    copy.gif
    cut.gif
    dataset.gif
    datasetA.gif
    datatype.gif
    datatypeA.gif
    fileclose.gif
    filenew.gif
    fileopen.gif
    filesave.gif
    first.gif
    folderclose.gif
    foldercloseA.gif
    folderopen.gif
    folderopenA.gif
    hdf_large.gif
    hdf.gif
    hdf32.gif
    hdf32.icns
    hdf32.png
    hdf4.gif
    hdf5.gif
    help.gif
    image.gif
    imageA.gif
    last.gif
    link.gif
    next.gif
    palette.gif
    paste.gif
    pdf.gif
    previous.gif
    question.gif
    table.gif
    tableA.gif
    text.gif
    textA.gif
    txt.gif
    url.gif
    video.gif
    xls.gif
    zoomin.gif
    zoomout.gif
)

SET (CMAKE_JAVA_INCLUDE_PATH 
    ${CMAKE_JAVA_INCLUDE_PATH} 
    ${HDFJAVA_JARS_TO_EXPORT}
)

INCLUDE_DIRECTORIES (${CMAKE_CURRENT_BINARY_DIR}/CMakeFiles/${HDFJAVA_NCSA_VIEW_LIB_CORENAME}.dir/ncsa/hdf/object)

ADD_JAR (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HDFJAVA_NCSA_HDF_VIEW_SRCS})
INSTALL_JAR (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${LIB_INSTALL_DIR}/${HDFJAVA_NCSA_VIEW_LIB_TARGET})
SET_GLOBAL_VARIABLE (HDFJAVA_JARS_TO_EXPORT "${HDFJAVA_JARS_TO_EXPORT};${${HDFJAVA_NCSA_VIEW_LIB_TARGET}_JAR_FILE}")
ADD_DEPENDENCIES (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HDFJAVA_JNI_HDF5_LIB_TARGET} ${HDFJAVA_JNI_HDF_LIB_TARGET})
