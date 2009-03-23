package hdf.h5.structs;

import hdf.h5.enums.H5G.H5G_storage_type_t;

//Information struct for group (for H5Gget_info/H5Gget_info_by_name/H5Gget_info_by_idx)
public class H5G_info_t {
  public H5G_storage_type_t storage_type; // Type of storage for links in group
  public long nlinks; // Number of links in group
  public long max_corder; // Current max. creation order value for group
  public int mounted; // Whether group has a file mounted on it
}
