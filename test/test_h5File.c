#include "h5File.h"
#include "h5Handler.h"
#include "hdf5.h"
#if 0	/* XXXX rm for irods */
#include "hdf5PackDef.h"
#endif
#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <malloc.h>

#define NO_TEST_ATTRI	1
#define NO_TEST_PALETTE	1
#define TEST_SUBSET 1
#define HDF5_LOCAL 1

#define FILENAME "dset.h5"
#define DSET_NAME "dset"
#define RANK 2
#define DIM0 100
#define DIM1 1000

int print_group(rcComm_t *conn, const H5Group *pg);
int print_dataset(const H5Dataset *d);
int print_attribute(const H5Attribute *a);
int create_test_file(const char *filename);

int main(int argc, char* argv[])
{
    int ret_value=0, i=0;
    char fname[80];
    H5File *f=0;
    H5Dataset *d=0;
    rcComm_t *conn = NULL;

/******************************************************************************
 *    In real application, the filename should be obtained from the SRB server. 
 *    Since SRB stores files as resources, there must be a traslation between
 *    actual physical file and the resource identifier.
 ******************************************************************************/

#ifdef HDF5_LOCAL
    strcpy(fname, FILENAME);
    create_test_file(FILENAME);
    printf("\n..... test file: %s\n", fname);
    fflush(stdout);
#else
    if (argc <=1 )
    {
        printf("Enter file name: ");
        scanf("%s", fname);
    } else
        strcpy(fname, argv[1]);

    printf("\n..... test file: %s\n", fname);
    fflush(stdout);

    conn = rcConnect (NULL, NULL, NULL,
     NULL, NULL, NULL, NULL);
    if (clStatus(conn) != CLI_CONNECTION_OK) {
      fprintf(stderr,"Connection to rodsMaster failed.\n");
      fprintf(stderr,"%s",clErrorMessage(conn));
      rods_perror (2, clStatus(conn), "", SRB_RCMD_ACTION|SRB_LONG_MSG);
      clFinish(conn); exit(3);
    }
#endif

    f = (H5File*)malloc(sizeof(H5File));
    assert(f);
    H5File_ctor(f);

    f->filename = (char*)malloc(strlen(fname)+1);
    strcpy(f->filename, fname);

/******************************************************************************
 *  In real application, the client program should make this call andi
 *      a) pack message
 *      b) send it to the SRB server
 *      c)  wait for response from the SRB server
 ******************************************************************************/
    f->opID = H5FILE_OP_OPEN;
    ret_value = h5ObjRequest(conn, f, H5OBJECT_FILE);
    if (ret_value < 0) {
	fprintf (stderr, "H5FILE_OP_OPEN failed, status = %d\n", ret_value);
	exit (1);
    }

#ifdef foo
/* ...... f goes to the server */


/******************************************************************************
 * In real application, the server program make this call and
 *    a) unpack the message to for H5File structure
 *    b) process the request
 *    c) pack the message and send back to client
 *****************************************************************************/
    h5ObjProcess(f, H5OBJECT_FILE);
#endif

/* .... f goes to the client */

    if (f->fid < 0)
        goto exit;

    assert(f->root);

    /* client needs to set the byte order so that the sever can check 
     * if the byte order matches
     */
    for (i=0; i<f->root->ndatasets; i++)
    {
        d = (H5Dataset *) &f->root->datasets[i];
        d->type.order =get_machine_endian();
    } 

    ret_value = print_group(conn, f->root);


/******************************************************************************
 * suppose we want to read the data value  of the first dataset from the file
 ******************************************************************************/
    d = NULL;
    if (f->root->ndatasets>0)
	d = (H5Dataset *) &f->root->datasets[0];

    if (d)
    {
/******************************************************************************
 *  In real application, the client program should make this call andi
 *      a) pack message
 *      b) send it to the SRB server
 *      c)  wait for response from the SRB server
 ******************************************************************************/
        d->opID = H5DATASET_OP_READ;
        ret_value = h5ObjRequest(conn, d, H5OBJECT_DATASET);
        if (ret_value < 0) {
            fprintf (stderr, "H5DATASET_OP_READ failed, status = %d\n", 
	      ret_value);
            exit (1);
        }


#ifdef foo
/* ...... d goes to the server */

/******************************************************************************
 * In real application, the server program make this call and
 *    a) unpack the message to for H5File structure
 *    b) process the request
 *    c) pack the message and send back to client
 *****************************************************************************/
        h5ObjProcess(d, H5OBJECT_DATASET);
#endif

/* .... d goes to the client */
        print_dataset(d);
    }

/* close the file at the server */
/******************************************************************************
 *  In real application, the client program should make this call andi
 *      a) pack message
 *      b) send it to the SRB server
 *      c)  wait for response from the SRB server
 ******************************************************************************/

    f->opID = H5FILE_OP_CLOSE;
    ret_value = h5ObjRequest(conn, f, H5OBJECT_FILE);
    if (ret_value < 0) {
        fprintf (stderr, "H5FILE_OP_CLOSE failed, status = %d\n", ret_value);
        exit (1);
    }

#ifdef foo
/* ...... f goes to the server */

/******************************************************************************
 * In real application, the server program make this call and
 *    a) unpack the message to for H5File structure
 *    b) process the request
 *    c) pack the message and send back to client
 *****************************************************************************/
    h5ObjProcess(f, H5OBJECT_FILE);
#endif

exit:
    H5File_dtor(f);
    if (f) free(f);

    return ret_value;
}

int print_group(rcComm_t *conn, const H5Group *pg)
{
    int ret_value=0, i=0, j=0;
    H5Group *g=0;
    H5Dataset *d=0;

    assert(pg);

    for (i=0; i<pg->ngroups; i++)
    {
        g = (H5Group *) &pg->groups[i];
        printf("%s\n", g->fullpath);

#ifdef TEST_ATTRI
        g->opID = H5GROUP_OP_READ_ATTRIBUTE;
        ret_value = h5ObjRequest(conn, g, H5OBJECT_GROUP);
        if (ret_value < 0) {
            fprintf (stderr, 
	      "H5GROUP_OP_READ_ATTRIBUTE failed, status = %d\n", ret_value);
            exit (1);
        }

        printf("\nTotal number of attributes = %d\n", g->nattributes);
    if (g->attributes)
    {
        printf("Attributes: \n");

        for (i=0; i<g->nattributes; i++)
            print_attribute(&(g->attributes[i]));
    }
#endif

        ret_value = print_group(conn, g);
    }

    for (i=0; i<pg->ndatasets; i++)
    {
        d = (H5Dataset *) &pg->datasets[i];
        printf("%s\n", d->fullpath);

#ifdef TEST_PALETTE
        if (d->nattributes > 0)
        {
            int i=0;
            unsigned char *pv;
            H5Attribute a = (d->attributes)[0];
            if ( strcmp(a.name, PALETTE_VALUE) == 0)
            {
                printf("\n#################### PALETTE #####################\n");
                pv = (unsigned char *)a.value;
                for (i=0; i<20; i++) printf("%u\t", *((unsigned char*)(pv+i)));    
            }
        }
#endif

        d->opID = H5DATASET_OP_READ;
        d->nattributes = 0;

#ifdef TEST_SUBSET
        for (j=0; j<d->space.rank; j++)
        {
            d->space.count[j] = d->space.dims[j]/10;
            if (d->space.count[j] == 0) d->space.count[j] =1;
        }
#endif
        ret_value = h5ObjRequest(conn, d, H5OBJECT_DATASET);
        if (ret_value < 0) {
            fprintf (stderr, "H5DATASET_OP_READ failed, status = %d\n", 
	      ret_value);
            exit (1);
        }

#ifdef TEST_ATTRI
        d->opID = H5DATASET_OP_READ_ATTRIBUTE;
        ret_value = h5ObjRequest(conn, d, H5OBJECT_DATASET);
        if (ret_value < 0) {
            fprintf (stderr, 
	      "H5DATASET_OP_READ_ATTRIBUTE failed, status = %d\n", ret_value);
            exit (1);
        }
#endif

        ret_value = print_dataset(d);
    } 
  
    return ret_value;
}

int print_dataset(const H5Dataset *d)
{
    int ret_value=0, size=0, i=0;
    char* pv;
    char **strs;

    assert(d);

    printf("\nThe total size of the value buffer = %d\n", d->nvalue);
    printf("\nPrinting the first 10 values of %s\n", d->fullpath);
    if (d->value)
    {
        size = 1;
        pv = (char*)d->value;
        for (i=0; i<d->space.rank; i++) size *= d->space.count[i];
        if (size > 10 ) size = 10; /* print only the first 10 values */
        if (d->type.class == H5DATATYPE_VLEN 
            || d->type.class == H5DATATYPE_COMPOUND
            || d->type.class == H5DATATYPE_STRING)
            strs = (char **)d->value;

        for (i=0; i<size; i++)
        {
            if (d->type.class == H5DATATYPE_INTEGER)
            {
                if (d->type.sign == H5DATATYPE_SGN_NONE)
                {
                    if (d->type.size == 1)
                        printf("%u\t", *((unsigned char*)(pv+i)));
                    else if (d->type.size == 2)
                        printf("%u\t", *((unsigned short*)(pv+i*2)));
                    else if (d->type.size == 4)
                        printf("%u\t", *((unsigned int*)(pv+i*4)));
                    else if (d->type.size == 8)
                        printf("%u\t", *((unsigned long long*)(pv+i*8)));
                }
                else 
                {
                    if (d->type.size == 1)
                        printf("%d\t", *((char*)(pv+i)));
                    else if (d->type.size == 2)
                        printf("%d\t", *((short*)(pv+i*2)));
                    else if (d->type.size == 4)
                        printf("%d\t", *((int*)(pv+i*4)));
                    else if (d->type.size == 8)
                        printf("%d\t", *((long long*)(pv+i*8)));
                }
            } else if (d->type.class == H5DATATYPE_FLOAT)
            {
                if (d->type.size == 4)
                    printf("%f\t", *((float *)(pv+i*4)));
                else if (d->type.size == 8)
                    printf("%f\t", *((double *)(pv+i*8)));
            } else if (d->type.class == H5DATATYPE_VLEN 
                    || d->type.class == H5DATATYPE_COMPOUND)
            {
                if (strs[i])
                    printf("%s\t", strs[i]);
            } else if (d->type.class == H5DATATYPE_STRING)
            {
                if (strs[i])
                    printf("%s\t", strs[i]);
            }
        }
        printf("\n\n");
    }

    if (d->attributes)
    {
        printf("\nTotal number of attributes = %d\n", d->nattributes);
        printf("Attributes: \n");
  
        for (i=0; i<d->nattributes; i++)
            print_attribute(&(d->attributes[i]));
    }


    return ret_value;
}

int print_attribute(const H5Attribute *a)
{
    int ret_value=0, size=0, i=0;
    char* pv;
    char **strs;

    assert(a);

    printf("\n\tThe total size of the attribute value buffer = %d\n", a->nvalue);
    printf("\n\tPrinting the first 10 values of %s\n", a->name);
    if (a->value)
    {
        size = 1;
        pv = (char*)a->value;

        for (i=0; i<a->space.rank; i++) size *= a->space.count[i];
        if (size > 10 ) size = 10; /* print only the first 10 values */
        if (a->type.class == H5DATATYPE_VLEN
            || a->type.class == H5DATATYPE_COMPOUND
            || a->type.class == H5DATATYPE_STRING)
            strs = (char **)a->value;

        for (i=0; i<size; i++)
        {
            if (a->type.class == H5DATATYPE_INTEGER)
            {
                if (a->type.sign == H5DATATYPE_SGN_NONE)
                {
                    if (a->type.size == 1)
                        printf("%u\t", *((unsigned char*)(pv+i)));
                    else if (a->type.size == 2)
                        printf("%u\t", *((unsigned short*)(pv+i*2)));
                    else if (a->type.size == 4)
                        printf("%u\t", *((unsigned int*)(pv+i*4)));
                    else if (a->type.size == 8)
                        printf("%u\t", *((unsigned long long*)(pv+i*8)));
                }
                else
                {
                    if (a->type.size == 1)
                        printf("%d\t", *((char*)(pv+i)));
                    else if (a->type.size == 2)
                        printf("%d\t", *((short*)(pv+i*2)));
                    else if (a->type.size == 4)
                        printf("%d\t", *((int*)(pv+i*4)));
                    else if (a->type.size == 8)
                        printf("%d\t", *((long long*)(pv+i*8)));
                }
            } else if (a->type.class == H5DATATYPE_FLOAT)
            {
                if (a->type.size == 4)
                    printf("%f\t", *((float *)(pv+i*4)));
                else if (a->type.size == 8)
                    printf("%f\t", *((double *)(pv+i*8)));
            } else if (a->type.class == H5DATATYPE_VLEN
                    || a->type.class == H5DATATYPE_COMPOUND)
            {
                if (strs[i])
                    printf("%s\t", strs[i]);
            } else if (a->type.class == H5DATATYPE_STRING)
            {
                if (strs[i]) printf("%s\t", strs[i]);
            }
        }
        printf("\n\n");
    }


    return ret_value;
}

int create_test_file(const char *filename)
{
   hid_t       file_id, did, sid;  /* identifiers */
   hsize_t     dims[] = {DIM0, DIM1};
   int         i, j, buf[DIM0][DIM1];
   int      status;


   /* Initialize the dataset. */
   for (i = 0; i < DIM0; i++) {
      for (j = 0; j < DIM1; j++)
         buf[i][j] = i * 6 + j + 1;
   }

   /* Create a new file using default properties. */
   file_id = H5Fcreate(filename, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);

   /* Create the data space for the dataset. */
   sid = H5Screate_simple(RANK, dims, NULL);

   /* Create the dataset. */
   did = H5Dcreate(file_id, DSET_NAME, H5T_NATIVE_INT, sid, H5P_DEFAULT);

   /* Write the dataset. */
   status = H5Dwrite(did, H5T_NATIVE_INT, H5S_ALL, H5S_ALL, H5P_DEFAULT, buf);

   /* End access to the dataset and release resources used by it. */
   status = H5Dclose(did);

   /* Terminate access to the data space. */
   status = H5Sclose(sid);

   /* Close the file. */
   status = H5Fclose(file_id);

   return status; 
}


