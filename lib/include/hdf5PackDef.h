#ifndef HDF5PACKDEF_H
#define HDF5PACKDEF_H
#include <stdio.h>

#include "srbClient.h"
#include "h5Object.h"
#include "h5Group.h"
#include "hdf5PackDefExtern.h"

FormatDef h5File_PF = "int fopID; str *filename; int ffid; struct *h5Group_PF; struct h5error_PF;int ftime;";
FormatDef h5error_PF = "str major[MAX_ERROR_SIZE]; str minor[MAX_ERROR_SIZE];";
FormatDef h5Group_PF = "int gopID; int gfid; int gobjID[OBJID_DIM]; str *gfullpath; str *dummyParent; int nGroupMembers; struct *h5Group_PF[nGroupMembers]; int nDatasetMembers; struct *h5Dataset_PF[nDatasetMembers]; int nattributes; struct *h5Attribute_PF[nattributes]; struct h5error_PF;int gtime;";
/*
FormatDef h5Attribute_PF = "int aopID; int afid; str *aname; str *aobj_path; int aobj_type; int aclass; struct h5Datatype_PF; struct h5Dataspace_PF; int nvalue; char *value[nvalue]; struct h5error_PF;";
*/
FormatDef h5Attribute_PF = "int aopID; int afid; str *aname; str *aobj_path; int aobj_type; int aclass; struct h5Datatype_PF; struct h5Dataspace_PF; int nvalue; ? aclass:3,6,9 = str *value[nvalue][NULL_TERM]:default= char *value[nvalue]; struct h5error_PF;";
/*
FormatDef h5Dataset_PF = "int dopID; int dfid; int dobjID[OBJID_DIM]; str *dfullpath; int nattributes; struct *h5Attribute_PF[nattributes]; struct h5Datatype_PF; struct h5Dataspace_PF; int nvalue; char *value[nvalue]; struct h5error_PF;";
*/
FormatDef h5Dataset_PF = "int dopID; int dfid; int dobjID[OBJID_DIM]; int dclass; str *dfullpath; int nattributes; struct *h5Attribute_PF[nattributes]; struct h5Datatype_PF; struct h5Dataspace_PF; int nvalue; ? dclass:3,6,9 = str *value[nvalue][NULL_TERM]:default= char %value[nvalue]; struct h5error_PF;int dtime;";

FormatDef h5Datatype_PF = "int tclass; int torder; int tsign; int tsize; int ntmenbers; int *mtypes[ntmenbers]; str *mnames[ntmenbers][NULL_TERM];";
FormatDef h5Dataspace_PF= "int rank; int dims[H5S_MAX_RANK]; int npoints; int start[H5DATASPACE_MAX_RANK]; int stride[H5DATASPACE_MAX_RANK]; int count[H5DATASPACE_MAX_RANK];";
FormatDef testStrArray_PF = "int numStr; str *myAtrArry[numStr][NULL_TERM];"; 
FormatDefConst Hdf5Def[] = {
	{"MAX_ERROR_SIZE", (char *) MAX_ERROR_SIZE},
	{"h5File_PF", (char *) &h5File_PF},
	{"h5error_PF", (char *) &h5error_PF},
	{"h5Group_PF", (char *) &h5Group_PF},
	{"h5Attribute_PF", (char *) &h5Attribute_PF},
	{"h5Dataset_PF", (char *) &h5Dataset_PF},
	{"h5Datatype_PF", (char *) &h5Datatype_PF},
	{"h5Dataspace_PF", (char *) &h5Dataspace_PF},
	{"OBJID_DIM", (char *) 2},
	{"H5S_MAX_RANK", (char *) 32},
	{"H5DATASPACE_MAX_RANK", (char *) 32},
	{"testStrArray_PF", (char *) &testStrArray_PF},
        {END_DEF_NAME, (char *) END_DEF_VAL},
};
 
#endif	/* HDF5PACKDEF_H */
