/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf5/COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

#ifndef _H5Git_H
#define _H5Git_H

#ifdef __cplusplus
extern "C" {
#endif 
#include <hdf5.h>

int H5Gn_members( hid_t file, char *group_name );

herr_t H5Gget_obj_info_idx( hid_t file, char *group_name, int idx, char **objname, int *type );

#ifdef __cplusplus
}
#endif 
#endif /*_H5Git_H*/
