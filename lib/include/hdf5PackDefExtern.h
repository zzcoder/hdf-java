#ifndef HDF5PACKDEF_EXTERN_H
#define HDF5PACKDEF_EXTERN_H
#include <stdio.h>

#include "srbClient.h"
#include "genProxyFunctExtern.h"
#include "h5Object.h"
#include "h5Group.h"

extern FormatDef h5File_PF;
extern FormatDef h5error_PF;
extern FormatDef h5Group_PF;
extern FormatDef h5Attribute_PF;

extern FormatDef h5Dataset_PF;

extern FormatDef h5Datatype_PF;
extern FormatDef h5Dataspace_PF;
extern FormatDef testStrArray_PF;
extern FormatDefConst Hdf5Def[];
 
#endif	/* HDF5PACKDEF_EXTERN_H */
