package hdf.h5.constants;

public class H5Z_define {
  // General
  public static final int H5Z_FILTER_ALL   = 0;   // Symbol to remove all filters in H5Premove_filter
  public static final int H5Z_MAX_NFILTERS = 32;  // Maximum number of filters allowed in a pipeline
                                                  // (should probably be allowed to be an
                                                  // unlimited amount, but currently each
                                                  // filter uses a bit in a 32-bit field,
                                                  // so the format would have to be
                                                  // changed to accomodate that)

  // Flags for filter definition (stored)
  public static final int H5Z_FLAG_DEFMASK   = 0x00ff;  // definition flag mask
  public static final int H5Z_FLAG_MANDATORY = 0x0000;  // filter is mandatory
  public static final int H5Z_FLAG_OPTIONAL  = 0x0001;  // filter is optional

  // Additional flags for filter invocation (not stored)
  public static final int H5Z_FLAG_INVMASK  = 0xff00;  // invocation flag mask
  public static final int H5Z_FLAG_REVERSE  = 0x0100;  // reverse direction; read
  public static final int H5Z_FLAG_SKIP_EDC = 0x0200;  // skip EDC filters for read

  // Current version of the H5Z_class_t struct
  public static final int H5Z_CLASS_T_VERS = (1);

  // Bit flags for H5Zget_filter_info
  public static final int H5Z_FILTER_CONFIG_ENCODE_ENABLED = (0x0001);
  public static final int H5Z_FILTER_CONFIG_DECODE_ENABLED = (0x0002);

}
