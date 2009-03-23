package hdf.h5.structs;

import hdf.h5.enums.H5T.H5T_cset_t;

public class H5A_info_t {
  public int          corder_valid;   // Indicate if creation order is valid
  public int          corder;         // Creation order
  public H5T_cset_t   cset;           // Character set of attribute name
  public long         data_size;      // Size of raw data
}
