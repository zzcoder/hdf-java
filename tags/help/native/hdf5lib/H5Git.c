/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

#ifdef __cplusplus
extern "C" {
#endif 
#include <hdf5.h>
#include <string.h>
#include "H5Git.h"
#define FALSE 0

#ifdef __cplusplus
herr_t count_elems(hid_t loc_id, const char *name, void *opdata);
herr_t obj_info(hid_t loc_id, const char *name, void *opdata);
#else
static herr_t count_elems(hid_t loc_id, const char *name, void *opdata);
static herr_t obj_info(hid_t loc_id, const char *name, void *opdata);
#endif

typedef struct retval {
	char * name;
	int type;
} retval_t;


/*-------------------------------------------------------------------------
 * Function:	H5Gn_members
 *
 * Purpose:	Return the number of members of a group.  The "members"
 *		are the datasets, groups, and named datatypes in the
 *		group.
 *
 *		This function wraps the H5Ginterate() function in
 *		a completely obvious way, uses the operator
 *		function 'count_members()' below;
 *
 * See also:	H5Giterate()
 *
 *		IN:  hid_t file:  the file id
 *		IN:  char *group_name: the name of the group
 *
 * Errors:
 *
 * Return:	Success:	The object number of members of
 *				the group.
 *
 *		Failure:	FAIL
 *
 * Programmer:	REMcG
 *		Monday, Aug 2, 1999
 *
 * Modifications:
 *
 *-------------------------------------------------------------------------
 */
int 
H5Gn_members( hid_t loc_id, char *group_name )
{
	int res;
	int nelems = 0;

	res = H5Giterate(loc_id, group_name, NULL, count_elems, (void *)&nelems);
	if (res < 0) {
		return res;
	} else {
		return( nelems );
	}
}



/*-------------------------------------------------------------------------
 * Function:	H5Gget_obj_info_idx
 *
 * Purpose:	Return the name and type of the member of the group
 *		at index 'idx', as defined by the H5Giterator()
 *		function.
 *
 *		This function wraps the H5Ginterate() function in
 *		a completely obvious way, uses the operator
 *		function 'get_objinfo()' below;
 *
 * See also:	H5Giterate()
 *
 *		IN:  hid_t file:  the file id
 *		IN:  char *group_name: the name of the group
 *		IN:  int idx:  the index of the member object (see
 *		               H5Giterate()
 * 		OUT:  char **objname:  the name of the member object 
 * 		OUT:  int *type:  the type of the object (dataset, 
 *			group, or named datatype)
 *
 * Errors:
 *
 * Return:	Success:	The object number of members of
 *				the group.
 *
 *		Failure:	FAIL
 *
 * Programmer:	REMcG
 *		Monday, Aug 2, 1999
 *
 * Modifications:
 *
 *-------------------------------------------------------------------------
 */
herr_t 
H5Gget_obj_info_idx( hid_t loc_id, char *group_name, int idx, char **objname, int *type )
{
	int res;
	retval_t retVal;

	res = H5Giterate(loc_id, group_name, &idx, obj_info, (void *)&retVal);
	if (res < 0) {
		return res;
	}
	*objname = retVal.name;
	*type = retVal.type;
	return 0;
}



/*-------------------------------------------------------------------------
 * Function:	count_elems
 *
 * Purpose:	this is the operator function called by H5Gn_members().
 *
 *		This function is passed to H5Ginterate().
 *
 * See also:	H5Giterate()
 *
 * 		OUT:  'opdata' is returned as an integer with the
 *			number of members in the group.
 *
 * Errors:
 *
 * Return:	Success:	The object number of members of
 *				the group.
 *
 *		Failure:	FAIL
 *
 * Programmer:	REMcG
 *		Monday, Aug 2, 1999
 *
 * Modifications:
 *
 *-------------------------------------------------------------------------
 */

#ifndef __cplusplus
static 
#endif
herr_t 
count_elems(hid_t loc_id, const char *name, void *opdata)
{
	herr_t res;
	H5G_stat_t statbuf;

	res = H5Gget_objinfo(loc_id, name, FALSE, &statbuf);
	if (res < 0) {
		return 1;
	} 
	switch (statbuf.type) {
	case H5G_GROUP: 
		(*(int *)opdata)++;
		 break;
	case H5G_DATASET: 
		(*(int *)opdata)++;
		 break;
	case H5G_TYPE: 
		(*(int *)opdata)++;
		 break;
	default:
		(*(int *)opdata)++; /* ???? count links or no? */
		 break;
	}
	return 0;
 }


/*-------------------------------------------------------------------------
 * Function:	obj_info
 *
 * Purpose:	this is the operator function called by H5Gn_members().
 *
 *		This function is passed to H5Ginterate().
 *
 * See also:	H5Giterate()
 *
 * 		OUT:  'opdata' is returned as a 'recvar_t', containing
 *			the object name and type.
 *
 * Errors:
 *
 * Return:	Success:	The object number of members of
 *				the group.
 *
 *		Failure:	FAIL
 *
 * Programmer:	REMcG
 *		Monday, Aug 2, 1999
 *
 * Modifications:
 *
 *-------------------------------------------------------------------------
 *			group, or named datatype)
 */
#ifndef __cplusplus
static 
#endif
herr_t 
obj_info(hid_t loc_id, const char *name, void *opdata)
{
	herr_t res;
	H5G_stat_t statbuf;

	res = H5Gget_objinfo(loc_id, name, FALSE, &statbuf);
	if (res < 0) {
		((retval_t *)opdata)->type = -1;
		((retval_t *)opdata)->name = NULL;
		return 1;
	} else {
		((retval_t *)opdata)->type = statbuf.type;
		((retval_t *)opdata)->name = (char *)strdup(name);
		return 1;
	}
 }
#ifdef __cplusplus
}
#endif 
