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
    icons/animation.gif
    icons/apps.gif
    icons/audio.gif
    icons/autocontrast.gif
    icons/blank.gif
    icons/brightness.gif
    icons/chart.gif
    icons/copy.gif
    icons/cut.gif
    icons/dataset.gif
    icons/datasetA.gif
    icons/datatype.gif
    icons/datatypeA.gif
    icons/fileclose.gif
    icons/filenew.gif
    icons/fileopen.gif
    icons/filesave.gif
    icons/first.gif
    icons/folderclose.gif
    icons/foldercloseA.gif
    icons/folderopen.gif
    icons/folderopenA.gif
    icons/hdf_large.gif
    icons/hdf.gif
    icons/hdf32.gif
    icons/hdf32.icns
    icons/hdf32.png
    icons/hdf4.gif
    icons/hdf5.gif
    icons/help.gif
    icons/image.gif
    icons/imageA.gif
    icons/last.gif
    icons/link.gif
    icons/next.gif
    icons/palette.gif
    icons/paste.gif
    icons/pdf.gif
    icons/previous.gif
    icons/question.gif
    icons/table.gif
    icons/tableA.gif
    icons/text.gif
    icons/textA.gif
    icons/txt.gif
    icons/url.gif
    icons/video.gif
    icons/xls.gif
    icons/zoomin.gif
    icons/zoomout.gif
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

ADD_JAR (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HDFJAVA_NCSA_HDF_VIEW_SRCS} ${HDFJAVA_NCSA_HDF_VIEW_ICONS})
INSTALL_JAR (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${LIB_INSTALL_DIR}/${HDFJAVA_NCSA_VIEW_LIB_TARGET})
SET_GLOBAL_VARIABLE (HDFJAVA_JARS_TO_EXPORT "${HDFJAVA_JARS_TO_EXPORT};${${HDFJAVA_NCSA_VIEW_LIB_TARGET}_JAR_FILE}")
ADD_DEPENDENCIES (${HDFJAVA_NCSA_VIEW_LIB_TARGET} ${HDFJAVA_NCSA_HDF_LIB_TARGET})

#-----------------------------------------------------------------------------
# Add Target(s) to CMake Install for import into other projects
#-----------------------------------------------------------------------------
IF (HDFJAVA_EXPORTED_TARGETS)
  INSTALL (
      FILES 
          ${HDFJAVA_NCSA_VIEW_LIB_TARGET}
      EXPORT
          ${HDFJAVA_EXPORTED_TARGETS}
      DESTINATION ${HJAVA_INSTALL_BIN_DIR} COMPONENT applications
  )
ENDIF (HDFJAVA_EXPORTED_TARGETS)
