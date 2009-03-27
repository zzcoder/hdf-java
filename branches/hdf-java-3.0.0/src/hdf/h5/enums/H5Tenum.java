/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package hdf.h5.enums;

public enum H5Tenum {
//How is the `bkg' buffer used by the conversion function?
// H5T_bkg_t 
  H5T_BKG_NO,             //background buffer is not needed, send NULL
  H5T_BKG_TEMP,           //bkg buffer used as temp storage only
  H5T_BKG_YES,            //init bkg buf with data before conversion 

//These are the various classes of datatypes 
//If this goes over 16 types (0-15), the file format will need to change)
// H5T_class_t 
  H5T_NO_CLASS,           //error   
  H5T_INTEGER,            //integer types   
  H5T_FLOAT,              //floating-point types 
  H5T_TIME,               //date and time types    
  H5T_STRING,             //character string types 
  H5T_BITFIELD,           //bit field types        
  H5T_OPAQUE,             //opaque types           
  H5T_COMPOUND,           //compound types         
  H5T_REFERENCE,          //reference types        
  H5T_ENUM,               //enumeration types      
  H5T_VLEN,               //Variable-Length types  
  H5T_ARRAY,              //Array types            
  H5T_NCLASSES,           //this must be last      

//Commands sent to conversion functions
// H5T_cmd_t
  H5T_CONV_INIT,          //query and/or initialize private data
  H5T_CONV_CONV,          //convert data from source to dest datatype 
  H5T_CONV_FREE,          //function is being removed from path    

//The exception type passed into the conversion callback function
// H5T_conv_except_t 
  H5T_CONV_EXCEPT_RANGE_HI,   //source value is greater than destination's range
  H5T_CONV_EXCEPT_RANGE_LOW,  //source value is less than destination's range 
  H5T_CONV_EXCEPT_PRECISION,  //source value loses precision in destination 
  H5T_CONV_EXCEPT_TRUNCATE,   //source value is truncated in destination 
  H5T_CONV_EXCEPT_PINF,       //source value is positive infinity(floating number)
  H5T_CONV_EXCEPT_NINF,       //source value is negative infinity(floating number)
  H5T_CONV_EXCEPT_NAN,        //source value is NaN(floating number) 

//The return value from conversion callback function H5T_conv_except_func_t
// H5T_conv_ret_t 
  H5T_CONV_ABORT,         //abort conversion
  H5T_CONV_UNHANDLED,     //callback function failed to handle the exception
  H5T_CONV_HANDLED,       //callback function handled the exception successfully 

// H5T_cset_t 
  H5T_CSET_ERROR,         //error
  H5T_CSET_ASCII,         //US ASCII
  H5T_CSET_UTF8,          //UTF-8 Unicode encoding
  H5T_CSET_RESERVED_2,    //reserved for later use
  H5T_CSET_RESERVED_3,    //reserved for later use
  H5T_CSET_RESERVED_4,    //reserved for later use
  H5T_CSET_RESERVED_5,    //reserved for later use
  H5T_CSET_RESERVED_6,    //reserved for later use
  H5T_CSET_RESERVED_7,    //reserved for later use
  H5T_CSET_RESERVED_8,    //reserved for later use
  H5T_CSET_RESERVED_9,    //reserved for later use
  H5T_CSET_RESERVED_10,   //reserved for later use
  H5T_CSET_RESERVED_11,   //reserved for later use
  H5T_CSET_RESERVED_12,   //reserved for later use
  H5T_CSET_RESERVED_13,   //reserved for later use
  H5T_CSET_RESERVED_14,   //reserved for later use
  H5T_CSET_RESERVED_15,   //reserved for later use

//The order to retrieve atomic native datatype
// H5T_direction_t 
  H5T_DIR_DEFAULT,        //default direction is inscendent
  H5T_DIR_ASCEND,         //in inscendent order
  H5T_DIR_DESCEND,        //in descendent order

//Floating-point normalization schemes
// H5T_norm_t 
  H5T_NORM_ERROR,         //error
  H5T_NORM_IMPLIED,       //msb of mantissa isn't stored, always 1
  H5T_NORM_MSBSET,        //msb of mantissa is always 1
  H5T_NORM_NONE,          //not normalized 

//Byte orders
// H5T_order_t 
  H5T_ORDER_ERROR,        //error 
  H5T_ORDER_LE,           //little endian
  H5T_ORDER_BE,           //bit endian   
  H5T_ORDER_VAX,          //VAX mixed endian
  H5T_ORDER_NONE,         //no particular order (strings, bits,..)

//Type of padding to use in other atomic types
// H5T_pad_t 
  H5T_PAD_ERROR,          //error 
  H5T_PAD_ZERO,           //always set to zero 
  H5T_PAD_ONE,            //always set to one 
  H5T_PAD_BACKGROUND,     //set to background value  
  H5T_NPAD,               //THIS MUST BE LAST PAD

//Conversion function persistence
// H5T_pers_t
  H5T_PERS_DONTCARE,      //wild card
  H5T_PERS_HARD,          //hard conversion function
  H5T_PERS_SOFT,          //soft conversion function

//Types of integer sign schemes
// H5T_sign_t 
  H5T_SGN_ERROR,          //error
  H5T_SGN_NONE,           //this is an unsigned type
  H5T_SGN_2,              //two's complement
  H5T_NSGN,               //this must be last SGN!

//Type of padding to use in character strings.
//public enum H5T_str_t 
  H5T_STR_ERROR,          //error 
  H5T_STR_NULLTERM,       //null terminate like in C
  H5T_STR_NULLPAD,        //pad with nulls
  H5T_STR_SPACEPAD,       //pad with spaces like in Fortran 
  H5T_STR_RESERVED_3,     //reserved for later use
  H5T_STR_RESERVED_4,     //reserved for later use
  H5T_STR_RESERVED_5,     //reserved for later use
  H5T_STR_RESERVED_6,     //reserved for later use
  H5T_STR_RESERVED_7,     //reserved for later use
  H5T_STR_RESERVED_8,     //reserved for later use
  H5T_STR_RESERVED_9,     //reserved for later use
  H5T_STR_RESERVED_10,    //reserved for later use
  H5T_STR_RESERVED_11,    //reserved for later use 
  H5T_STR_RESERVED_12,    //reserved for later use 
  H5T_STR_RESERVED_13,    //reserved for later use
  H5T_STR_RESERVED_14,    //reserved for later use
  H5T_STR_RESERVED_15;    //reserved for later use
}
