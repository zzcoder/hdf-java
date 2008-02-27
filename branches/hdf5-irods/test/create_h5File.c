/*
 *   Creating and closing a dataset.
 */

#include "hdf5.h"

#define FILE "dset.h5"
#define RANK 2
#define DIM0 100
#define DIM1 1000

int main() {

   hid_t       file_id, did, sid;  /* identifiers */
   hsize_t     dims[] = {DIM0, DIM1};
   int         i, j, buf[DIM0][DIM1];
   herr_t      status;


   /* Initialize the dataset. */
   for (i = 0; i < DIM0; i++)
      for (j = 0; j < DIM1; j++)
         buf[i][j] = i * 6 + j + 1;

   /* Create a new file using default properties. */
   file_id = H5Fcreate(FILE, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);

   /* Create the data space for the dataset. */
   sid = H5Screate_simple(RANK, dims, NULL);

   /* Create the dataset. */
   did = H5Dcreate(file_id, "/dset", H5T_NATIVE_INT, sid, H5P_DEFAULT);

   /* Write the dataset. */
   status = H5Dwrite(did, H5T_NATIVE_INT, H5S_ALL, H5S_ALL, H5P_DEFAULT, buf);

   /* End access to the dataset and release resources used by it. */
   status = H5Dclose(did);

   /* Terminate access to the data space. */ 
   status = H5Sclose(sid);

   /* Close the file. */
   status = H5Fclose(file_id);
}

