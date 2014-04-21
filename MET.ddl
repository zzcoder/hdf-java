HDF5 "MET_TG_met0001_QL.h5" {
GROUP "/" {
   ATTRIBUTE "ADSS" {
      DATATYPE  H5T_STRING {
         STRSIZE H5T_VARIABLE;
         STRPAD H5T_STR_NULLTERM;
         CSET H5T_CSET_ASCII;
         CTYPE H5T_C_S1;
      }
      DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
      DATA {
      (0): "ALX04032013"
      }
   }
   ATTRIBUTE "Main" {
      DATATYPE  H5T_STRING {
         STRSIZE H5T_VARIABLE;
         STRPAD H5T_STR_NULLTERM;
         CSET H5T_CSET_ASCII;
         CTYPE H5T_C_S1;
      }
      DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
      DATA {
      (0): "MET"
      }
   }
   ATTRIBUTE "TrialID" {
      DATATYPE  H5T_STRING {
         STRSIZE H5T_VARIABLE;
         STRPAD H5T_STR_NULLTERM;
         CSET H5T_CSET_ASCII;
         CTYPE H5T_C_S1;
      }
      DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
      DATA {
      (0): "Trial245"
      }
   }
   ATTRIBUTE "TrialTitle" {
      DATATYPE  H5T_STRING {
         STRSIZE H5T_VARIABLE;
         STRPAD H5T_STR_NULLTERM;
         CSET H5T_CSET_ASCII;
         CTYPE H5T_C_S1;
      }
      DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
      DATA {
      (0): "CCTS Test"
      }
   }
   DATASET "Anemometer" {
      DATATYPE  H5T_COMPOUND {
         H5T_ENUM {
            H5T_STD_I32LE;
            "POWERED_OFF"      0;
            "INITIALIZING"     1;
            "OPERATIONAL"      2;
            "UNKNOWN"          3;
         } "status";
         H5T_IEEE_F32LE "x_wind";
         H5T_IEEE_F32LE "y_wind";
         H5T_IEEE_F32LE "upward_air_velocity";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            OPERATIONAL,
            -0.68,
            0.77,
            0.05
         },
      (1): {
            OPERATIONAL,
            -0.37,
            0.21,
            0.04
         },
      (2): {
            OPERATIONAL,
            -0.25,
            0,
            0.22
         },
      (3): {
            OPERATIONAL,
            -0.19,
            -0.07,
            0.1
         },
      (4): {
            OPERATIONAL,
            -0.1,
            -0.11,
            0.1
         },
      (5): {
            OPERATIONAL,
            -0.62,
            0.71,
            0.26
         },
      (6): {
            OPERATIONAL,
            -1.23,
            1.27,
            -0.09
         },
      (7): {
            OPERATIONAL,
            -0.34,
            0.21,
            -0.19
         },
      (8): {
            OPERATIONAL,
            -0.07,
            0.08,
            -0.23
         },
      (9): {
            OPERATIONAL,
            0.05,
            0.24,
            0.12
         },
      (10): {
            OPERATIONAL,
            -0.37,
            0.58,
            0.13
         },
      (11): {
            OPERATIONAL,
            -0.49,
            0.16,
            0.2
         },
      (12): {
            OPERATIONAL,
            -0.19,
            0,
            -0.01
         },
      (13): {
            OPERATIONAL,
            -0.16,
            0.03,
            0.06
         },
      (14): {
            OPERATIONAL,
            -0.04,
            -0.05,
            0.02
         },
      (15): {
            OPERATIONAL,
            -0.25,
            0.09,
            0.19
         },
      (16): {
            OPERATIONAL,
            -1.36,
            1.38,
            0.32
         },
      (17): {
            OPERATIONAL,
            -0.08,
            0.7,
            -0.19
         },
      (18): {
            OPERATIONAL,
            -0.02,
            0.18,
            -0.34
         },
      (19): {
            OPERATIONAL,
            -0.04,
            0.19,
            -0.13
         },
      (20): {
            OPERATIONAL,
            -0.46,
            0.45,
            0.12
         },
      (21): {
            OPERATIONAL,
            -0.72,
            0.5,
            0.26
         },
      (22): {
            OPERATIONAL,
            -0.34,
            0.17,
            0.22
         },
      (23): {
            OPERATIONAL,
            -0.29,
            0.08,
            -0.01
         },
      (24): {
            OPERATIONAL,
            -0.27,
            0,
            -0.01
         },
      (25): {
            OPERATIONAL,
            -0.04,
            0,
            0.06
         },
      (26): {
            OPERATIONAL,
            -0.95,
            0.78,
            0.24
         },
      (27): {
            OPERATIONAL,
            -0.61,
            0.63,
            -0.29
         },
      (28): {
            OPERATIONAL,
            -0.17,
            0.3,
            -0.24
         },
      (29): {
            OPERATIONAL,
            -0.06,
            0.16,
            0
         },
      (30): {
            OPERATIONAL,
            -0.06,
            0.18,
            0.08
         },
      (31): {
            OPERATIONAL,
            -0.48,
            0.53,
            0.18
         },
      (32): {
            OPERATIONAL,
            -0.32,
            0.32,
            0.19
         },
      (33): {
            OPERATIONAL,
            -0.23,
            0.07,
            0.08
         },
      (34): {
            OPERATIONAL,
            -0.19,
            0.03,
            0
         },
      (35): {
            OPERATIONAL,
            -0.07,
            -0.03,
            0
         },
      (36): {
            OPERATIONAL,
            -0.54,
            0.86,
            0.48
         },
      (37): {
            OPERATIONAL,
            -1.18,
            1.31,
            -0.14
         },
      (38): {
            OPERATIONAL,
            0.2,
            0.39,
            -0.24
         },
      (39): {
            OPERATIONAL,
            0,
            0.22,
            -0.08
         },
      (40): {
            OPERATIONAL,
            0.05,
            0.23,
            0.01
         },
      (41): {
            OPERATIONAL,
            -0.33,
            0.52,
            0.07
         },
      (42): {
            OPERATIONAL,
            -0.52,
            0.78,
            0.25
         },
      (43): {
            OPERATIONAL,
            -0.19,
            0.3,
            0.01
         },
      (44): {
            OPERATIONAL,
            -0.33,
            0.02,
            0.14
         },
      (45): {
            OPERATIONAL,
            -0.29,
            -0.17,
            0.11
         },
      (46): {
            OPERATIONAL,
            -0.07,
            0.03,
            0.11
         },
      (47): {
            OPERATIONAL,
            -1.32,
            1.58,
            0.41
         },
      (48): {
            OPERATIONAL,
            -0.9,
            1.09,
            -0.23
         },
      (49): {
            OPERATIONAL,
            -0.24,
            0.25,
            -0.22
         },
      (50): {
            OPERATIONAL,
            -0.25,
            0.02,
            -0.11
         },
      (51): {
            OPERATIONAL,
            -0.23,
            0.07,
            0.08
         },
      (52): {
            OPERATIONAL,
            -0.56,
            0.68,
            0
         },
      (53): {
            OPERATIONAL,
            -0.47,
            0.04,
            0.08
         },
      (54): {
            OPERATIONAL,
            -0.29,
            0.1,
            0.01
         },
      (55): {
            OPERATIONAL,
            -0.24,
            -0.01,
            -0.05
         },
      (56): {
            OPERATIONAL,
            -0.12,
            -0.04,
            0.01
         },
      (57): {
            OPERATIONAL,
            -0.44,
            0.37,
            -0.03
         },
      (58): {
            OPERATIONAL,
            -1.02,
            1.16,
            0.07
         },
      (59): {
            OPERATIONAL,
            -0.22,
            0.17,
            -0.5
         },
      (60): {
            OPERATIONAL,
            -0.06,
            0.25,
            -0.08
         },
      (61): {
            OPERATIONAL,
            -0.05,
            0.16,
            0.06
         },
      (62): {
            OPERATIONAL,
            -0.61,
            0.6,
            0.05
         },
      (63): {
            OPERATIONAL,
            -0.57,
            0.41,
            0.24
         },
      (64): {
            OPERATIONAL,
            -0.32,
            0.06,
            0.15
         },
      (65): {
            OPERATIONAL,
            -0.32,
            -0.09,
            0.15
         },
      (66): {
            OPERATIONAL,
            -0.23,
            0,
            0.01
         },
      (67): {
            OPERATIONAL,
            -0.09,
            0.27,
            0.1
         },
      (68): {
            OPERATIONAL,
            -1.28,
            1.48,
            0.28
         },
      (69): {
            OPERATIONAL,
            0.06,
            0.78,
            -0.21
         },
      (70): {
            OPERATIONAL,
            0,
            0.25,
            -0.12
         },
      (71): {
            OPERATIONAL,
            -0.03,
            0.23,
            0.01
         },
      (72): {
            OPERATIONAL,
            -0.06,
            0.2,
            0.14
         },
      (73): {
            OPERATIONAL,
            -0.6,
            0.52,
            0.18
         },
      (74): {
            OPERATIONAL,
            -0.19,
            0.12,
            0.03
         },
      (75): {
            OPERATIONAL,
            -0.16,
            0.1,
            0
         },
      (76): {
            OPERATIONAL,
            -0.2,
            0.08,
            -0.05
         },
      (77): {
            OPERATIONAL,
            -0.13,
            0.12,
            -0.06
         },
      (78): {
            OPERATIONAL,
            -1.13,
            1.24,
            0.41
         },
      (79): {
            OPERATIONAL,
            -0.9,
            1.08,
            -0.28
         },
      (80): {
            OPERATIONAL,
            -0.15,
            0.45,
            -0.01
         },
      (81): {
            OPERATIONAL,
            -0.18,
            0.17,
            -0.15
         },
      (82): {
            OPERATIONAL,
            -0.07,
            0.18,
            0.13
         },
      (83): {
            OPERATIONAL,
            -0.93,
            0.99,
            0.19
         },
      (84): {
            OPERATIONAL,
            -0.43,
            0.26,
            0.1
         },
      (85): {
            OPERATIONAL,
            -0.36,
            0.02,
            0.01
         },
      (86): {
            OPERATIONAL,
            -0.2,
            0,
            0.1
         },
      (87): {
            OPERATIONAL,
            -0.14,
            -0.09,
            0
         },
      (88): {
            OPERATIONAL,
            -0.54,
            0.61,
            0.32
         },
      (89): {
            OPERATIONAL,
            -1.03,
            1.06,
            -0.08
         },
      (90): {
            OPERATIONAL,
            -0.24,
            0.21,
            -0.45
         },
      (91): {
            OPERATIONAL,
            -0.14,
            0.26,
            -0.01
         },
      (92): {
            OPERATIONAL,
            -0.02,
            0.27,
            0.13
         },
      (93): {
            OPERATIONAL,
            -0.44,
            0.44,
            0.24
         },
      (94): {
            OPERATIONAL,
            -0.64,
            0.57,
            0.09
         },
      (95): {
            OPERATIONAL,
            -0.4,
            0.24,
            0.16
         },
      (96): {
            OPERATIONAL,
            -0.18,
            0.12,
            -0.08
         },
      (97): {
            OPERATIONAL,
            -0.11,
            0.07,
            0.02
         },
      (98): {
            OPERATIONAL,
            -0.07,
            0.23,
            0.08
         },
      (99): {
            OPERATIONAL,
            -1.27,
            1.47,
            0.38
         },
      (100): {
            OPERATIONAL,
            -0.09,
            0.66,
            -0.2
         },
      (101): {
            OPERATIONAL,
            -0.02,
            0.45,
            -0.15
         },
      (102): {
            OPERATIONAL,
            0.02,
            0.3,
            0.01
         },
      (103): {
            OPERATIONAL,
            0,
            0.26,
            0.16
         },
      (104): {
            OPERATIONAL,
            -0.5,
            0.37,
            -0.02
         },
      (105): {
            OPERATIONAL,
            -0.41,
            0.37,
            0.19
         }
      }
      ATTRIBUTE "ATEC_Level" {
         DATATYPE  H5T_STD_I32LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 1
         }
      }
      ATTRIBUTE "DIMENSION_LIST" {
         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): (DATASET 11512 /Anemometer_Time )
         }
      }
      ATTRIBUTE "Data_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_1.0"
         }
      }
      ATTRIBUTE "H&S" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_H&S"
         }
      }
      ATTRIBUTE "HumanVerified" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "false"
         }
      }
      ATTRIBUTE "LocHeight" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 2
         }
      }
      ATTRIBUTE "LocationID" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "ALX 3"
         }
      }
      ATTRIBUTE "LocationSource" {
         DATATYPE  H5T_ENUM {
            H5T_STD_I32LE;
            "APPROXIMATE"      0;
            "SURVEYED"         1;
            "GPS_FIXED"        2;
            "GPS_MOBILE"       3;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): SURVEYED
         }
      }
      ATTRIBUTE "LocationUTM" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
         DATA {
         (0): 4.43576e+06, 325215, 1327
         }
      }
      ATTRIBUTE "QA" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_QA"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
      ATTRIBUTE "UTM_Zone" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "12T"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 4 ) / ( 4 ) }
         DATA {
         (0): "enum", "m/s", "m/s", "m/s"
         }
      }
   }
   DATASET "Anemometer_Time" {
      DATATYPE  H5T_COMPOUND {
         H5T_STD_I64LE "time";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "timeString";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            635315457808450000,
            "3/27/2014 19:36:20.845"
         },
      (1): {
            635315457818490000,
            "3/27/2014 19:36:21.849"
         },
      (2): {
            635315457828520000,
            "3/27/2014 19:36:22.852"
         },
      (3): {
            635315457838560000,
            "3/27/2014 19:36:23.856"
         },
      (4): {
            635315457848590000,
            "3/27/2014 19:36:24.859"
         },
      (5): {
            635315457858640000,
            "3/27/2014 19:36:25.864"
         },
      (6): {
            635315457868680000,
            "3/27/2014 19:36:26.868"
         },
      (7): {
            635315457878710000,
            "3/27/2014 19:36:27.871"
         },
      (8): {
            635315457888740000,
            "3/27/2014 19:36:28.874"
         },
      (9): {
            635315457898780000,
            "3/27/2014 19:36:29.878"
         },
      (10): {
            635315457908830000,
            "3/27/2014 19:36:30.883"
         },
      (11): {
            635315457918860000,
            "3/27/2014 19:36:31.886"
         },
      (12): {
            635315457928900000,
            "3/27/2014 19:36:32.890"
         },
      (13): {
            635315457938950000,
            "3/27/2014 19:36:33.895"
         },
      (14): {
            635315457948970000,
            "3/27/2014 19:36:34.897"
         },
      (15): {
            635315457959010000,
            "3/27/2014 19:36:35.901"
         },
      (16): {
            635315457969060000,
            "3/27/2014 19:36:36.906"
         },
      (17): {
            635315457979090000,
            "3/27/2014 19:36:37.909"
         },
      (18): {
            635315457989120000,
            "3/27/2014 19:36:38.912"
         },
      (19): {
            635315457999150000,
            "3/27/2014 19:36:39.915"
         },
      (20): {
            635315458009200000,
            "3/27/2014 19:36:40.920"
         },
      (21): {
            635315458019240000,
            "3/27/2014 19:36:41.924"
         },
      (22): {
            635315458029270000,
            "3/27/2014 19:36:42.927"
         },
      (23): {
            635315458039310000,
            "3/27/2014 19:36:43.931"
         },
      (24): {
            635315458049340000,
            "3/27/2014 19:36:44.934"
         },
      (25): {
            635315458059380000,
            "3/27/2014 19:36:45.938"
         },
      (26): {
            635315458069430000,
            "3/27/2014 19:36:46.943"
         },
      (27): {
            635315458079460000,
            "3/27/2014 19:36:47.946"
         },
      (28): {
            635315458089490000,
            "3/27/2014 19:36:48.949"
         },
      (29): {
            635315458099530000,
            "3/27/2014 19:36:49.953"
         },
      (30): {
            635315458109580000,
            "3/27/2014 19:36:50.958"
         },
      (31): {
            635315458119610000,
            "3/27/2014 19:36:51.961"
         },
      (32): {
            635315458129640000,
            "3/27/2014 19:36:52.964"
         },
      (33): {
            635315458139690000,
            "3/27/2014 19:36:53.969"
         },
      (34): {
            635315458149720000,
            "3/27/2014 19:36:54.972"
         },
      (35): {
            635315458159760000,
            "3/27/2014 19:36:55.976"
         },
      (36): {
            635315458169800000,
            "3/27/2014 19:36:56.980"
         },
      (37): {
            635315458179840000,
            "3/27/2014 19:36:57.984"
         },
      (38): {
            635315458189860000,
            "3/27/2014 19:36:58.986"
         },
      (39): {
            635315458199900000,
            "3/27/2014 19:36:59.990"
         },
      (40): {
            635315458209950000,
            "3/27/2014 19:37:00.995"
         },
      (41): {
            635315458219990000,
            "3/27/2014 19:37:01.999"
         },
      (42): {
            635315458230020000,
            "3/27/2014 19:37:03.002"
         },
      (43): {
            635315458240060000,
            "3/27/2014 19:37:04.006"
         },
      (44): {
            635315458250090000,
            "3/27/2014 19:37:05.009"
         },
      (45): {
            635315458260140000,
            "3/27/2014 19:37:06.014"
         },
      (46): {
            635315458270170000,
            "3/27/2014 19:37:07.017"
         },
      (47): {
            635315458280210000,
            "3/27/2014 19:37:08.021"
         },
      (48): {
            635315458290250000,
            "3/27/2014 19:37:09.025"
         },
      (49): {
            635315458300280000,
            "3/27/2014 19:37:10.028"
         },
      (50): {
            635315458310320000,
            "3/27/2014 19:37:11.032"
         },
      (51): {
            635315458320350000,
            "3/27/2014 19:37:12.035"
         },
      (52): {
            635315458330400000,
            "3/27/2014 19:37:13.040"
         },
      (53): {
            635315458340430000,
            "3/27/2014 19:37:14.043"
         },
      (54): {
            635315458350470000,
            "3/27/2014 19:37:15.047"
         },
      (55): {
            635315458360510000,
            "3/27/2014 19:37:16.051"
         },
      (56): {
            635315458370540000,
            "3/27/2014 19:37:17.054"
         },
      (57): {
            635315458380590000,
            "3/27/2014 19:37:18.059"
         },
      (58): {
            635315458390630000,
            "3/27/2014 19:37:19.063"
         },
      (59): {
            635315458400650000,
            "3/27/2014 19:37:20.065"
         },
      (60): {
            635315458410690000,
            "3/27/2014 19:37:21.069"
         },
      (61): {
            635315458420740000,
            "3/27/2014 19:37:22.074"
         },
      (62): {
            635315458430770000,
            "3/27/2014 19:37:23.077"
         },
      (63): {
            635315458440810000,
            "3/27/2014 19:37:24.081"
         },
      (64): {
            635315458450840000,
            "3/27/2014 19:37:25.084"
         },
      (65): {
            635315458460870000,
            "3/27/2014 19:37:26.087"
         },
      (66): {
            635315458470920000,
            "3/27/2014 19:37:27.092"
         },
      (67): {
            635315458480960000,
            "3/27/2014 19:37:28.096"
         },
      (68): {
            635315458491000000,
            "3/27/2014 19:37:29.100"
         },
      (69): {
            635315458501040000,
            "3/27/2014 19:37:30.104"
         },
      (70): {
            635315458511060000,
            "3/27/2014 19:37:31.106"
         },
      (71): {
            635315458521110000,
            "3/27/2014 19:37:32.111"
         },
      (72): {
            635315458531140000,
            "3/27/2014 19:37:33.114"
         },
      (73): {
            635315458541180000,
            "3/27/2014 19:37:34.118"
         },
      (74): {
            635315458551220000,
            "3/27/2014 19:37:35.122"
         },
      (75): {
            635315458561260000,
            "3/27/2014 19:37:36.126"
         },
      (76): {
            635315458571290000,
            "3/27/2014 19:37:37.129"
         },
      (77): {
            635315458581340000,
            "3/27/2014 19:37:38.134"
         },
      (78): {
            635315458591370000,
            "3/27/2014 19:37:39.137"
         },
      (79): {
            635315458601410000,
            "3/27/2014 19:37:40.141"
         },
      (80): {
            635315458611450000,
            "3/27/2014 19:37:41.145"
         },
      (81): {
            635315458621470000,
            "3/27/2014 19:37:42.147"
         },
      (82): {
            635315458631510000,
            "3/27/2014 19:37:43.151"
         },
      (83): {
            635315458641560000,
            "3/27/2014 19:37:44.156"
         },
      (84): {
            635315458651590000,
            "3/27/2014 19:37:45.159"
         },
      (85): {
            635315458661630000,
            "3/27/2014 19:37:46.163"
         },
      (86): {
            635315458671660000,
            "3/27/2014 19:37:47.166"
         },
      (87): {
            635315458681710000,
            "3/27/2014 19:37:48.171"
         },
      (88): {
            635315458691740000,
            "3/27/2014 19:37:49.174"
         },
      (89): {
            635315458701790000,
            "3/27/2014 19:37:50.179"
         },
      (90): {
            635315458711810000,
            "3/27/2014 19:37:51.181"
         },
      (91): {
            635315458721850000,
            "3/27/2014 19:37:52.185"
         },
      (92): {
            635315458731890000,
            "3/27/2014 19:37:53.189"
         },
      (93): {
            635315458741920000,
            "3/27/2014 19:37:54.192"
         },
      (94): {
            635315458751970000,
            "3/27/2014 19:37:55.197"
         },
      (95): {
            635315458762010000,
            "3/27/2014 19:37:56.201"
         },
      (96): {
            635315458772030000,
            "3/27/2014 19:37:57.203"
         },
      (97): {
            635315458782080000,
            "3/27/2014 19:37:58.208"
         },
      (98): {
            635315458792120000,
            "3/27/2014 19:37:59.212"
         },
      (99): {
            635315458802160000,
            "3/27/2014 19:38:00.216"
         },
      (100): {
            635315458812190000,
            "3/27/2014 19:38:01.219"
         },
      (101): {
            635315458822230000,
            "3/27/2014 19:38:02.223"
         },
      (102): {
            635315458832260000,
            "3/27/2014 19:38:03.226"
         },
      (103): {
            635315458842300000,
            "3/27/2014 19:38:04.230"
         },
      (104): {
            635315458852340000,
            "3/27/2014 19:38:05.234"
         },
      (105): {
            635315458862380000,
            "3/27/2014 19:38:06.238"
         }
      }
      ATTRIBUTE "CLASS" {
         DATATYPE  H5T_STRING {
            STRSIZE 16;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SCALAR
         DATA {
         (0): "DIMENSION_SCALE"
         }
      }
      ATTRIBUTE "REFERENCE_LIST" {
         DATATYPE  H5T_COMPOUND {
            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
            H5T_STD_I32LE "dimension";
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): {
               DATASET 11024 /Anemometer ,
               0
            }
         }
      }
      ATTRIBUTE "Time_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "v1.0"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
         DATA {
         (0): ".Net DateTime.Ticks", "DateTime"
         }
      }
   }
   DATASET "Barometer" {
      DATATYPE  H5T_COMPOUND {
         H5T_ENUM {
            H5T_STD_I32LE;
            "POWERED_OFF"      0;
            "INITIALIZING"     1;
            "OPERATIONAL"      2;
            "UNKNOWN"          3;
         } "status";
         H5T_IEEE_F32LE "air_pressure";
      }
      DATASPACE  SIMPLE { ( 0 ) / ( H5S_UNLIMITED ) }
      DATA {
      }
      ATTRIBUTE "ATEC_Level" {
         DATATYPE  H5T_STD_I32LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 1
         }
      }
      ATTRIBUTE "DIMENSION_LIST" {
         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): (DATASET 14136 /Barometer_Time )
         }
      }
      ATTRIBUTE "Data_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_1.0"
         }
      }
      ATTRIBUTE "H&S" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_H&S"
         }
      }
      ATTRIBUTE "HumanVerified" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "false"
         }
      }
      ATTRIBUTE "LocHeight" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 2
         }
      }
      ATTRIBUTE "LocationID" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "ALX 3"
         }
      }
      ATTRIBUTE "LocationSource" {
         DATATYPE  H5T_ENUM {
            H5T_STD_I32LE;
            "APPROXIMATE"      0;
            "SURVEYED"         1;
            "GPS_FIXED"        2;
            "GPS_MOBILE"       3;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): SURVEYED
         }
      }
      ATTRIBUTE "LocationUTM" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
         DATA {
         (0): 4.43576e+06, 325215, 1327
         }
      }
      ATTRIBUTE "QA" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_QA"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
      ATTRIBUTE "UTM_Zone" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "12T"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
         DATA {
         (0): "enum", "hPa"
         }
      }
   }
   DATASET "Barometer_Time" {
      DATATYPE  H5T_COMPOUND {
         H5T_STD_I64LE "time";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "timeString";
      }
      DATASPACE  SIMPLE { ( 0 ) / ( H5S_UNLIMITED ) }
      DATA {
      }
      ATTRIBUTE "CLASS" {
         DATATYPE  H5T_STRING {
            STRSIZE 16;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SCALAR
         DATA {
         (0): "DIMENSION_SCALE"
         }
      }
      ATTRIBUTE "REFERENCE_LIST" {
         DATATYPE  H5T_COMPOUND {
            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
            H5T_STD_I32LE "dimension";
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): {
               DATASET 13600 /Barometer ,
               0
            }
         }
      }
      ATTRIBUTE "Time_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "v1.0"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
         DATA {
         (0): ".Net DateTime.Ticks", "DateTime"
         }
      }
   }
   DATASET "MET" {
      DATATYPE  H5T_COMPOUND {
         H5T_IEEE_F32LE "air_temperature";
         H5T_IEEE_F32LE "relative_humidity";
         H5T_IEEE_F32LE "x_wind";
         H5T_IEEE_F32LE "y_wind";
         H5T_IEEE_F32LE "upward_air_velocity";
         H5T_IEEE_F32LE "air_pressure";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            24,
            12.7,
            0.01,
            0.34,
            0.22,
            -9999
         },
      (1): {
            24,
            12.7,
            -0.37,
            0.21,
            0.04,
            -9999
         },
      (2): {
            24,
            12.7,
            -0.37,
            0.21,
            0.04,
            -9999
         },
      (3): {
            24,
            12.7,
            -0.25,
            0,
            0.22,
            -9999
         },
      (4): {
            24,
            12.7,
            -0.19,
            -0.07,
            0.1,
            -9999
         },
      (5): {
            24,
            12.7,
            -0.62,
            0.71,
            0.26,
            -9999
         },
      (6): {
            24,
            12.7,
            -0.62,
            0.71,
            0.26,
            -9999
         },
      (7): {
            24,
            12.7,
            -1.23,
            1.27,
            -0.09,
            -9999
         },
      (8): {
            24,
            12.7,
            -0.07,
            0.08,
            -0.23,
            -9999
         },
      (9): {
            24,
            12.7,
            -0.07,
            0.08,
            -0.23,
            -9999
         },
      (10): {
            24,
            12.7,
            -0.37,
            0.58,
            0.13,
            -9999
         },
      (11): {
            24,
            12.7,
            -0.49,
            0.16,
            0.2,
            -9999
         },
      (12): {
            24,
            12.7,
            -0.19,
            0,
            -0.01,
            -9999
         },
      (13): {
            24,
            12.7,
            -0.19,
            0,
            -0.01,
            -9999
         },
      (14): {
            24,
            12.7,
            -0.04,
            -0.05,
            0.02,
            -9999
         },
      (15): {
            24,
            12.7,
            -0.04,
            -0.05,
            0.02,
            -9999
         },
      (16): {
            24,
            12.7,
            -0.25,
            0.09,
            0.19,
            -9999
         },
      (17): {
            24,
            12.7,
            -1.36,
            1.38,
            0.32,
            -9999
         },
      (18): {
            24,
            12.7,
            -0.08,
            0.7,
            -0.19,
            -9999
         },
      (19): {
            24,
            12.7,
            -0.02,
            0.18,
            -0.34,
            -9999
         },
      (20): {
            24,
            12.7,
            -0.04,
            0.19,
            -0.13,
            -9999
         },
      (21): {
            23.9,
            12.7,
            -0.46,
            0.45,
            0.12,
            -9999
         },
      (22): {
            23.9,
            12.7,
            -0.72,
            0.5,
            0.26,
            -9999
         },
      (23): {
            23.9,
            12.7,
            -0.34,
            0.17,
            0.22,
            -9999
         },
      (24): {
            23.9,
            12.7,
            -0.29,
            0.08,
            -0.01,
            -9999
         },
      (25): {
            23.9,
            12.7,
            -0.04,
            0,
            0.06,
            -9999
         },
      (26): {
            23.9,
            12.7,
            -0.04,
            0,
            0.06,
            -9999
         },
      (27): {
            23.9,
            12.7,
            -0.61,
            0.63,
            -0.29,
            -9999
         },
      (28): {
            23.9,
            12.7,
            -0.61,
            0.63,
            -0.29,
            -9999
         },
      (29): {
            23.9,
            12.7,
            -0.17,
            0.3,
            -0.24,
            -9999
         },
      (30): {
            23.9,
            12.7,
            -0.06,
            0.16,
            0,
            -9999
         },
      (31): {
            23.9,
            12.7,
            -0.06,
            0.18,
            0.08,
            -9999
         },
      (32): {
            23.9,
            12.7,
            -0.48,
            0.53,
            0.18,
            -9999
         },
      (33): {
            23.9,
            12.7,
            -0.32,
            0.32,
            0.19,
            -9999
         },
      (34): {
            23.9,
            12.7,
            -0.23,
            0.07,
            0.08,
            -9999
         },
      (35): {
            23.9,
            12.7,
            -0.19,
            0.03,
            0,
            -9999
         },
      (36): {
            23.9,
            12.7,
            -0.07,
            -0.03,
            0,
            -9999
         },
      (37): {
            23.9,
            12.7,
            -0.54,
            0.86,
            0.48,
            -9999
         },
      (38): {
            23.9,
            12.7,
            0.2,
            0.39,
            -0.24,
            -9999
         },
      (39): {
            23.9,
            12.7,
            0.2,
            0.39,
            -0.24,
            -9999
         },
      (40): {
            23.9,
            12.7,
            0.05,
            0.23,
            0.01,
            -9999
         },
      (41): {
            23.9,
            12.7,
            0.05,
            0.23,
            0.01,
            -9999
         },
      (42): {
            23.9,
            12.7,
            -0.33,
            0.52,
            0.07,
            -9999
         },
      (43): {
            23.9,
            12.7,
            -0.52,
            0.78,
            0.25,
            -9999
         },
      (44): {
            23.9,
            12.7,
            -0.19,
            0.3,
            0.01,
            -9999
         },
      (45): {
            23.9,
            12.7,
            -0.33,
            0.02,
            0.14,
            -9999
         },
      (46): {
            23.9,
            12.7,
            -0.29,
            -0.17,
            0.11,
            -9999
         },
      (47): {
            23.9,
            12.7,
            -1.32,
            1.58,
            0.41,
            -9999
         },
      (48): {
            23.9,
            12.7,
            -1.32,
            1.58,
            0.41,
            -9999
         },
      (49): {
            23.9,
            12.7,
            -0.9,
            1.09,
            -0.23,
            -9999
         },
      (50): {
            23.9,
            12.7,
            -0.24,
            0.25,
            -0.22,
            -9999
         },
      (51): {
            23.9,
            12.7,
            -0.25,
            0.02,
            -0.11,
            -9999
         },
      (52): {
            23.9,
            12.7,
            -0.23,
            0.07,
            0.08,
            -9999
         },
      (53): {
            23.9,
            12.7,
            -0.56,
            0.68,
            0,
            -9999
         },
      (54): {
            23.9,
            12.7,
            -0.47,
            0.04,
            0.08,
            -9999
         },
      (55): {
            23.9,
            12.7,
            -0.29,
            0.1,
            0.01,
            -9999
         },
      (56): {
            23.9,
            12.7,
            -0.24,
            -0.01,
            -0.05,
            -9999
         },
      (57): {
            23.9,
            12.7,
            -0.12,
            -0.04,
            0.01,
            -9999
         },
      (58): {
            23.9,
            12.7,
            -0.44,
            0.37,
            -0.03,
            -9999
         },
      (59): {
            23.9,
            12.7,
            -1.02,
            1.16,
            0.07,
            -9999
         },
      (60): {
            23.9,
            12.7,
            -0.22,
            0.17,
            -0.5,
            -9999
         },
      (61): {
            23.9,
            12.7,
            -0.06,
            0.25,
            -0.08,
            -9999
         },
      (62): {
            23.9,
            12.7,
            -0.05,
            0.16,
            0.06,
            -9999
         },
      (63): {
            23.9,
            12.7,
            -0.61,
            0.6,
            0.05,
            -9999
         },
      (64): {
            23.9,
            12.7,
            -0.57,
            0.41,
            0.24,
            -9999
         },
      (65): {
            23.9,
            12.7,
            -0.32,
            0.06,
            0.15,
            -9999
         },
      (66): {
            23.9,
            12.7,
            -0.32,
            -0.09,
            0.15,
            -9999
         },
      (67): {
            23.9,
            12.7,
            -0.23,
            0,
            0.01,
            -9999
         },
      (68): {
            23.9,
            12.7,
            -0.09,
            0.27,
            0.1,
            -9999
         },
      (69): {
            23.9,
            12.7,
            -1.28,
            1.48,
            0.28,
            -9999
         },
      (70): {
            23.9,
            12.8,
            0.06,
            0.78,
            -0.21,
            -9999
         },
      (71): {
            23.9,
            12.8,
            0,
            0.25,
            -0.12,
            -9999
         },
      (72): {
            23.9,
            12.8,
            -0.03,
            0.23,
            0.01,
            -9999
         },
      (73): {
            23.9,
            12.8,
            -0.06,
            0.2,
            0.14,
            -9999
         },
      (74): {
            23.9,
            12.8,
            -0.6,
            0.52,
            0.18,
            -9999
         },
      (75): {
            23.9,
            12.8,
            -0.19,
            0.12,
            0.03,
            -9999
         },
      (76): {
            23.9,
            12.8,
            -0.16,
            0.1,
            0,
            -9999
         },
      (77): {
            23.9,
            12.8,
            -0.2,
            0.08,
            -0.05,
            -9999
         },
      (78): {
            23.9,
            12.8,
            -0.13,
            0.12,
            -0.06,
            -9999
         },
      (79): {
            23.9,
            12.8,
            -1.13,
            1.24,
            0.41,
            -9999
         },
      (80): {
            23.9,
            12.8,
            -0.9,
            1.08,
            -0.28,
            -9999
         },
      (81): {
            23.9,
            12.8,
            -0.15,
            0.45,
            -0.01,
            -9999
         },
      (82): {
            23.9,
            12.8,
            -0.18,
            0.17,
            -0.15,
            -9999
         },
      (83): {
            23.9,
            12.8,
            -0.07,
            0.18,
            0.13,
            -9999
         },
      (84): {
            23.9,
            12.8,
            -0.93,
            0.99,
            0.19,
            -9999
         },
      (85): {
            23.9,
            12.8,
            -0.43,
            0.26,
            0.1,
            -9999
         },
      (86): {
            23.9,
            12.8,
            -0.36,
            0.02,
            0.01,
            -9999
         },
      (87): {
            23.9,
            12.8,
            -0.2,
            0,
            0.1,
            -9999
         },
      (88): {
            23.9,
            12.8,
            -0.14,
            -0.09,
            0,
            -9999
         },
      (89): {
            23.9,
            12.8,
            -0.54,
            0.61,
            0.32,
            -9999
         },
      (90): {
            23.9,
            12.8,
            -1.03,
            1.06,
            -0.08,
            -9999
         },
      (91): {
            23.9,
            12.8,
            -0.24,
            0.21,
            -0.45,
            -9999
         },
      (92): {
            23.9,
            12.8,
            -0.14,
            0.26,
            -0.01,
            -9999
         },
      (93): {
            23.9,
            12.8,
            -0.02,
            0.27,
            0.13,
            -9999
         },
      (94): {
            23.9,
            12.8,
            -0.44,
            0.44,
            0.24,
            -9999
         },
      (95): {
            23.9,
            12.8,
            -0.64,
            0.57,
            0.09,
            -9999
         },
      (96): {
            23.9,
            12.8,
            -0.4,
            0.24,
            0.16,
            -9999
         },
      (97): {
            23.9,
            12.8,
            -0.18,
            0.12,
            -0.08,
            -9999
         },
      (98): {
            23.9,
            12.8,
            -0.11,
            0.07,
            0.02,
            -9999
         },
      (99): {
            23.9,
            12.8,
            -0.07,
            0.23,
            0.08,
            -9999
         },
      (100): {
            23.9,
            12.8,
            -1.27,
            1.47,
            0.38,
            -9999
         },
      (101): {
            23.9,
            12.8,
            -0.09,
            0.66,
            -0.2,
            -9999
         },
      (102): {
            23.9,
            12.8,
            -0.02,
            0.45,
            -0.15,
            -9999
         },
      (103): {
            23.9,
            12.8,
            0.02,
            0.3,
            0.01,
            -9999
         },
      (104): {
            23.9,
            12.8,
            0,
            0.26,
            0.16,
            -9999
         },
      (105): {
            23.9,
            12.8,
            -0.5,
            0.37,
            -0.02,
            -9999
         }
      }
      ATTRIBUTE "ATEC_Level" {
         DATATYPE  H5T_STD_I32LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 1
         }
      }
      ATTRIBUTE "Anemometer" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RMY-1004292"
         }
      }
      ATTRIBUTE "DIMENSION_LIST" {
         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): (DATASET 6200 /MET_Time )
         }
      }
      ATTRIBUTE "Data_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_1.0"
         }
      }
      ATTRIBUTE "H&S" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_H&S"
         }
      }
      ATTRIBUTE "HumanVerified" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "false"
         }
      }
      ATTRIBUTE "LocHeight" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 2
         }
      }
      ATTRIBUTE "LocationID" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "ALX 3"
         }
      }
      ATTRIBUTE "LocationSource" {
         DATATYPE  H5T_ENUM {
            H5T_STD_I32LE;
            "APPROXIMATE"      0;
            "SURVEYED"         1;
            "GPS_FIXED"        2;
            "GPS_MOBILE"       3;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): SURVEYED
         }
      }
      ATTRIBUTE "LocationUTM" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
         DATA {
         (0): 4.43576e+06, 325215, 1327
         }
      }
      ATTRIBUTE "QA" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_QA"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
      ATTRIBUTE "TempHumidity" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "HMP-1001717"
         }
      }
      ATTRIBUTE "UTM_Zone" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "12T"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 6 ) / ( 6 ) }
         DATA {
         (0): "degrees C", "%", "m/s", "m/s", "m/s", "hPa"
         }
      }
   }
   DATASET "MET_H&S" {
      DATATYPE  H5T_COMPOUND {
         H5T_ENUM {
            H5T_STD_I32LE;
            "POWERED_OFF"      0;
            "INITIALIZING"     1;
            "OPERATIONAL"      2;
            "UNKNOWN"          3;
         } "tempStatus";
         H5T_ENUM {
            H5T_STD_I32LE;
            "POWERED_OFF"      0;
            "INITIALIZING"     1;
            "OPERATIONAL"      2;
            "UNKNOWN"          3;
         } "anemometerStatus";
         H5T_ENUM {
            H5T_STD_I32LE;
            "POWERED_OFF"      0;
            "INITIALIZING"     1;
            "OPERATIONAL"      2;
            "UNKNOWN"          3;
         } "barometerStatus";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (1): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (2): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (3): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (4): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (5): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (6): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (7): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (8): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (9): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (10): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (11): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (12): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (13): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (14): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (15): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (16): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (17): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (18): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (19): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (20): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (21): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (22): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (23): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (24): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (25): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (26): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (27): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (28): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (29): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (30): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (31): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (32): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (33): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (34): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (35): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (36): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (37): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (38): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (39): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (40): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (41): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (42): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (43): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (44): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (45): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (46): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (47): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (48): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (49): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (50): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (51): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (52): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (53): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (54): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (55): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (56): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (57): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (58): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (59): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (60): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (61): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (62): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (63): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (64): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (65): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (66): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (67): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (68): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (69): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (70): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (71): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (72): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (73): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (74): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (75): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (76): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (77): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (78): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (79): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (80): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (81): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (82): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (83): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (84): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (85): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (86): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (87): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (88): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (89): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (90): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (91): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (92): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (93): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (94): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (95): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (96): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (97): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (98): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (99): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (100): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (101): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (102): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (103): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (104): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         },
      (105): {
            OPERATIONAL,
            OPERATIONAL,
            UNKNOWN
         }
      }
      ATTRIBUTE "DIMENSION_LIST" {
         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): (DATASET 17112 /MET_H&S_Time )
         }
      }
      ATTRIBUTE "Dataset" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET"
         }
      }
      ATTRIBUTE "HS_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_HS_1.0"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
         DATA {
         (0): "enum", "enum", "enum"
         }
      }
   }
   DATASET "MET_H&S_Time" {
      DATATYPE  H5T_COMPOUND {
         H5T_STD_I64LE "time";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "timeString";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            635315457807950000,
            "3/27/2014 19:36:20.795"
         },
      (1): {
            635315457820710000,
            "3/27/2014 19:36:22.071"
         },
      (2): {
            635315457827310000,
            "3/27/2014 19:36:22.731"
         },
      (3): {
            635315457835830000,
            "3/27/2014 19:36:23.583"
         },
      (4): {
            635315457846830000,
            "3/27/2014 19:36:24.683"
         },
      (5): {
            635315457859630000,
            "3/27/2014 19:36:25.963"
         },
      (6): {
            635315457866430000,
            "3/27/2014 19:36:26.643"
         },
      (7): {
            635315457879190000,
            "3/27/2014 19:36:27.919"
         },
      (8): {
            635315457891870000,
            "3/27/2014 19:36:29.187"
         },
      (9): {
            635315457898470000,
            "3/27/2014 19:36:29.847"
         },
      (10): {
            635315457911140000,
            "3/27/2014 19:36:31.114"
         },
      (11): {
            635315457918540000,
            "3/27/2014 19:36:31.854"
         },
      (12): {
            635315457931220000,
            "3/27/2014 19:36:33.122"
         },
      (13): {
            635315457937900000,
            "3/27/2014 19:36:33.790"
         },
      (14): {
            635315457950580000,
            "3/27/2014 19:36:35.058"
         },
      (15): {
            635315457957180000,
            "3/27/2014 19:36:35.718"
         },
      (16): {
            635315457965860000,
            "3/27/2014 19:36:36.586"
         },
      (17): {
            635315457976900000,
            "3/27/2014 19:36:37.690"
         },
      (18): {
            635315457985420000,
            "3/27/2014 19:36:38.542"
         },
      (19): {
            635315457996540000,
            "3/27/2014 19:36:39.654"
         },
      (20): {
            635315458009300000,
            "3/27/2014 19:36:40.930"
         },
      (21): {
            635315458015900000,
            "3/27/2014 19:36:41.590"
         },
      (22): {
            635315458025180000,
            "3/27/2014 19:36:42.518"
         },
      (23): {
            635315458037700000,
            "3/27/2014 19:36:43.770"
         },
      (24): {
            635315458048740000,
            "3/27/2014 19:36:44.874"
         },
      (25): {
            635315458061420000,
            "3/27/2014 19:36:46.142"
         },
      (26): {
            635315458068100000,
            "3/27/2014 19:36:46.810"
         },
      (27): {
            635315458080850000,
            "3/27/2014 19:36:48.085"
         },
      (28): {
            635315458087450000,
            "3/27/2014 19:36:48.745"
         },
      (29): {
            635315458100130000,
            "3/27/2014 19:36:50.013"
         },
      (30): {
            635315458106690000,
            "3/27/2014 19:36:50.669"
         },
      (31): {
            635315458115970000,
            "3/27/2014 19:36:51.597"
         },
      (32): {
            635315458126970000,
            "3/27/2014 19:36:52.697"
         },
      (33): {
            635315458139650000,
            "3/27/2014 19:36:53.965"
         },
      (34): {
            635315458146250000,
            "3/27/2014 19:36:54.625"
         },
      (35): {
            635315458158930000,
            "3/27/2014 19:36:55.893"
         },
      (36): {
            635315458167450000,
            "3/27/2014 19:36:56.745"
         },
      (37): {
            635315458178770000,
            "3/27/2014 19:36:57.877"
         },
      (38): {
            635315458191570000,
            "3/27/2014 19:36:59.157"
         },
      (39): {
            635315458198250000,
            "3/27/2014 19:36:59.825"
         },
      (40): {
            635315458210970000,
            "3/27/2014 19:37:01.097"
         },
      (41): {
            635315458217610000,
            "3/27/2014 19:37:01.761"
         },
      (42): {
            635315458230370000,
            "3/27/2014 19:37:03.037"
         },
      (43): {
            635315458237010000,
            "3/27/2014 19:37:03.701"
         },
      (44): {
            635315458249680000,
            "3/27/2014 19:37:04.968"
         },
      (45): {
            635315458256280000,
            "3/27/2014 19:37:05.628"
         },
      (46): {
            635315458269000000,
            "3/27/2014 19:37:06.900"
         },
      (47): {
            635315458281760000,
            "3/27/2014 19:37:08.176"
         },
      (48): {
            635315458288360000,
            "3/27/2014 19:37:08.836"
         },
      (49): {
            635315458301160000,
            "3/27/2014 19:37:10.116"
         },
      (50): {
            635315458307760000,
            "3/27/2014 19:37:10.776"
         },
      (51): {
            635315458320480000,
            "3/27/2014 19:37:12.048"
         },
      (52): {
            635315458327080000,
            "3/27/2014 19:37:12.708"
         },
      (53): {
            635315458339840000,
            "3/27/2014 19:37:13.984"
         },
      (54): {
            635315458346560000,
            "3/27/2014 19:37:14.656"
         },
      (55): {
            635315458359240000,
            "3/27/2014 19:37:15.924"
         },
      (56): {
            635315458365840000,
            "3/27/2014 19:37:16.584"
         },
      (57): {
            635315458378640000,
            "3/27/2014 19:37:17.864"
         },
      (58): {
            635315458391240000,
            "3/27/2014 19:37:19.124"
         },
      (59): {
            635315458397840000,
            "3/27/2014 19:37:19.784"
         },
      (60): {
            635315458410480000,
            "3/27/2014 19:37:21.048"
         },
      (61): {
            635315458417200000,
            "3/27/2014 19:37:21.720"
         },
      (62): {
            635315458429960000,
            "3/27/2014 19:37:22.996"
         },
      (63): {
            635315458436550000,
            "3/27/2014 19:37:23.655"
         },
      (64): {
            635315458449230000,
            "3/27/2014 19:37:24.923"
         },
      (65): {
            635315458455910000,
            "3/27/2014 19:37:25.591"
         },
      (66): {
            635315458468750000,
            "3/27/2014 19:37:26.875"
         },
      (67): {
            635315458481390000,
            "3/27/2014 19:37:28.139"
         },
      (68): {
            635315458487990000,
            "3/27/2014 19:37:28.799"
         },
      (69): {
            635315458500750000,
            "3/27/2014 19:37:30.075"
         },
      (70): {
            635315458507350000,
            "3/27/2014 19:37:30.735"
         },
      (71): {
            635315458520190000,
            "3/27/2014 19:37:32.019"
         },
      (72): {
            635315458526790000,
            "3/27/2014 19:37:32.679"
         },
      (73): {
            635315458536070000,
            "3/27/2014 19:37:33.607"
         },
      (74): {
            635315458548470000,
            "3/27/2014 19:37:34.847"
         },
      (75): {
            635315458561150000,
            "3/27/2014 19:37:36.115"
         },
      (76): {
            635315458567870000,
            "3/27/2014 19:37:36.787"
         },
      (77): {
            635315458580590000,
            "3/27/2014 19:37:38.059"
         },
      (78): {
            635315458587190000,
            "3/27/2014 19:37:38.719"
         },
      (79): {
            635315458595900000,
            "3/27/2014 19:37:39.590"
         },
      (80): {
            635315458606900000,
            "3/27/2014 19:37:40.690"
         },
      (81): {
            635315458619540000,
            "3/27/2014 19:37:41.954"
         },
      (82): {
            635315458626140000,
            "3/27/2014 19:37:42.614"
         },
      (83): {
            635315458638870000,
            "3/27/2014 19:37:43.887"
         },
      (84): {
            635315458647380000,
            "3/27/2014 19:37:44.738"
         },
      (85): {
            635315458659780000,
            "3/27/2014 19:37:45.978"
         },
      (86): {
            635315458666380000,
            "3/27/2014 19:37:46.638"
         },
      (87): {
            635315458679100000,
            "3/27/2014 19:37:47.910"
         },
      (88): {
            635315458691980000,
            "3/27/2014 19:37:49.198"
         },
      (89): {
            635315458698580000,
            "3/27/2014 19:37:49.858"
         },
      (90): {
            635315458707100000,
            "3/27/2014 19:37:50.710"
         },
      (91): {
            635315458719500000,
            "3/27/2014 19:37:51.950"
         },
      (92): {
            635315458726180000,
            "3/27/2014 19:37:52.618"
         },
      (93): {
            635315458735500000,
            "3/27/2014 19:37:53.550"
         },
      (94): {
            635315458746500000,
            "3/27/2014 19:37:54.650"
         },
      (95): {
            635315458759180000,
            "3/27/2014 19:37:55.918"
         },
      (96): {
            635315458771890000,
            "3/27/2014 19:37:57.189"
         },
      (97): {
            635315458778490000,
            "3/27/2014 19:37:57.849"
         },
      (98): {
            635315458787810000,
            "3/27/2014 19:37:58.781"
         },
      (99): {
            635315458798730000,
            "3/27/2014 19:37:59.873"
         },
      (100): {
            635315458811410000,
            "3/27/2014 19:38:01.141"
         },
      (101): {
            635315458819410000,
            "3/27/2014 19:38:01.941"
         },
      (102): {
            635315458826010000,
            "3/27/2014 19:38:02.601"
         },
      (103): {
            635315458838690000,
            "3/27/2014 19:38:03.869"
         },
      (104): {
            635315458851450000,
            "3/27/2014 19:38:05.145"
         },
      (105): {
            635315458858130000,
            "3/27/2014 19:38:05.813"
         }
      }
      ATTRIBUTE "CLASS" {
         DATATYPE  H5T_STRING {
            STRSIZE 16;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SCALAR
         DATA {
         (0): "DIMENSION_SCALE"
         }
      }
      ATTRIBUTE "REFERENCE_LIST" {
         DATATYPE  H5T_COMPOUND {
            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
            H5T_STD_I32LE "dimension";
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): {
               DATASET 16192 /MET_H&S ,
               0
            }
         }
      }
      ATTRIBUTE "Time_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "v1.0"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
         DATA {
         (0): ".Net DateTime.Ticks", "DateTime"
         }
      }
   }
   DATASET "MET_Parameters" {
      DATATYPE  H5T_COMPOUND {
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "name";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "type";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "value";
      }
      DATASPACE  SIMPLE { ( 0 ) / ( H5S_UNLIMITED ) }
      DATA {
      }
      ATTRIBUTE "Dataset" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET"
         }
      }
      ATTRIBUTE "Param_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "Param_1.0"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
   }
   DATASET "MET_Provenance" {
      DATATYPE  H5T_COMPOUND {
         H5T_STD_I64LE "time";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "description";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "actor";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "algorithm";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "version";
      }
      DATASPACE  SIMPLE { ( 1 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            635315459012010794,
            "This is a simple processor for the MET raw data ingestion.",
            "Data Processing Manager",
            "DPM_MET_Handler",
            "v1.0"
         }
      }
      ATTRIBUTE "Dataset" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET"
         }
      }
      ATTRIBUTE "Prov_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "Prov_1.0"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
   }
   DATASET "MET_QA" {
      DATATYPE  H5T_COMPOUND {
         H5T_ENUM {
            H5T_STD_I32LE;
            "Success"          0;
            "Information"      1;
            "Warning"          2;
            "Error"            3;
            "Command"          4;
            "QC"               5;
            "TO_Log"           6;
         } "category";
         H5T_ENUM {
            H5T_STD_I32LE;
            "Level_1"          0;
            "Level_2"          1;
            "Level_3"          2;
            "Level_4"          3;
            "Level_5"          4;
         } "level";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "reason";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "description";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "source";
         H5T_STD_I64LE "dateFlagged";
         H5T_STD_I8LE "humanVerified";
         H5T_ENUM {
            H5T_STD_I32LE;
            "NOT_PROCESSED"    0;
            "RAW_UNPROCESSED"  1;
            "NORMAL"           2;
            "OUT_OF_RANGE"     3;
            "EQUIPMENT_ERROR"  4;
         } "tempAQAStatus";
         H5T_ENUM {
            H5T_STD_I32LE;
            "NOT_PROCESSED"    0;
            "RAW_UNPROCESSED"  1;
            "NORMAL"           2;
            "OUT_OF_RANGE"     3;
            "EQUIPMENT_ERROR"  4;
         } "barometerAQAStatus";
         H5T_ENUM {
            H5T_STD_I32LE;
            "NOT_PROCESSED"    0;
            "RAW_UNPROCESSED"  1;
            "NORMAL"           2;
            "OUT_OF_RANGE"     3;
            "EQUIPMENT_ERROR"  4;
         } "anemometerAQAStatus";
      }
      DATASPACE  SIMPLE { ( 0 ) / ( H5S_UNLIMITED ) }
      DATA {
      }
      ATTRIBUTE "DIMENSION_LIST" {
         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): (DATASET 19616 /MET_QA_Time )
         }
      }
      ATTRIBUTE "Dataset" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET"
         }
      }
      ATTRIBUTE "QA_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_QA_1.0"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
   }
   DATASET "MET_QA_Time" {
      DATATYPE  H5T_COMPOUND {
         H5T_STD_I64LE "time";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "timeString";
      }
      DATASPACE  SIMPLE { ( 0 ) / ( H5S_UNLIMITED ) }
      DATA {
      }
      ATTRIBUTE "CLASS" {
         DATATYPE  H5T_STRING {
            STRSIZE 16;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SCALAR
         DATA {
         (0): "DIMENSION_SCALE"
         }
      }
      ATTRIBUTE "REFERENCE_LIST" {
         DATATYPE  H5T_COMPOUND {
            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
            H5T_STD_I32LE "dimension";
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): {
               DATASET 18368 /MET_QA ,
               0
            }
         }
      }
      ATTRIBUTE "Time_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "v1.0"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
         DATA {
         (0): ".Net DateTime.Ticks", "DateTime"
         }
      }
   }
   DATASET "MET_Time" {
      DATATYPE  H5T_COMPOUND {
         H5T_STD_I64LE "time";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "timeString";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            635315457807950000,
            "3/27/2014 19:36:20.795"
         },
      (1): {
            635315457820710000,
            "3/27/2014 19:36:22.071"
         },
      (2): {
            635315457827310000,
            "3/27/2014 19:36:22.731"
         },
      (3): {
            635315457835830000,
            "3/27/2014 19:36:23.583"
         },
      (4): {
            635315457846830000,
            "3/27/2014 19:36:24.683"
         },
      (5): {
            635315457859630000,
            "3/27/2014 19:36:25.963"
         },
      (6): {
            635315457866430000,
            "3/27/2014 19:36:26.643"
         },
      (7): {
            635315457879190000,
            "3/27/2014 19:36:27.919"
         },
      (8): {
            635315457891870000,
            "3/27/2014 19:36:29.187"
         },
      (9): {
            635315457898470000,
            "3/27/2014 19:36:29.847"
         },
      (10): {
            635315457911140000,
            "3/27/2014 19:36:31.114"
         },
      (11): {
            635315457918540000,
            "3/27/2014 19:36:31.854"
         },
      (12): {
            635315457931220000,
            "3/27/2014 19:36:33.122"
         },
      (13): {
            635315457937900000,
            "3/27/2014 19:36:33.790"
         },
      (14): {
            635315457950580000,
            "3/27/2014 19:36:35.058"
         },
      (15): {
            635315457957180000,
            "3/27/2014 19:36:35.718"
         },
      (16): {
            635315457965860000,
            "3/27/2014 19:36:36.586"
         },
      (17): {
            635315457976900000,
            "3/27/2014 19:36:37.690"
         },
      (18): {
            635315457985420000,
            "3/27/2014 19:36:38.542"
         },
      (19): {
            635315457996540000,
            "3/27/2014 19:36:39.654"
         },
      (20): {
            635315458009300000,
            "3/27/2014 19:36:40.930"
         },
      (21): {
            635315458015900000,
            "3/27/2014 19:36:41.590"
         },
      (22): {
            635315458025180000,
            "3/27/2014 19:36:42.518"
         },
      (23): {
            635315458037700000,
            "3/27/2014 19:36:43.770"
         },
      (24): {
            635315458048740000,
            "3/27/2014 19:36:44.874"
         },
      (25): {
            635315458061420000,
            "3/27/2014 19:36:46.142"
         },
      (26): {
            635315458068100000,
            "3/27/2014 19:36:46.810"
         },
      (27): {
            635315458080850000,
            "3/27/2014 19:36:48.085"
         },
      (28): {
            635315458087450000,
            "3/27/2014 19:36:48.745"
         },
      (29): {
            635315458100130000,
            "3/27/2014 19:36:50.013"
         },
      (30): {
            635315458106690000,
            "3/27/2014 19:36:50.669"
         },
      (31): {
            635315458115970000,
            "3/27/2014 19:36:51.597"
         },
      (32): {
            635315458126970000,
            "3/27/2014 19:36:52.697"
         },
      (33): {
            635315458139650000,
            "3/27/2014 19:36:53.965"
         },
      (34): {
            635315458146250000,
            "3/27/2014 19:36:54.625"
         },
      (35): {
            635315458158930000,
            "3/27/2014 19:36:55.893"
         },
      (36): {
            635315458167450000,
            "3/27/2014 19:36:56.745"
         },
      (37): {
            635315458178770000,
            "3/27/2014 19:36:57.877"
         },
      (38): {
            635315458191570000,
            "3/27/2014 19:36:59.157"
         },
      (39): {
            635315458198250000,
            "3/27/2014 19:36:59.825"
         },
      (40): {
            635315458210970000,
            "3/27/2014 19:37:01.097"
         },
      (41): {
            635315458217610000,
            "3/27/2014 19:37:01.761"
         },
      (42): {
            635315458230370000,
            "3/27/2014 19:37:03.037"
         },
      (43): {
            635315458237010000,
            "3/27/2014 19:37:03.701"
         },
      (44): {
            635315458249680000,
            "3/27/2014 19:37:04.968"
         },
      (45): {
            635315458256280000,
            "3/27/2014 19:37:05.628"
         },
      (46): {
            635315458269000000,
            "3/27/2014 19:37:06.900"
         },
      (47): {
            635315458281760000,
            "3/27/2014 19:37:08.176"
         },
      (48): {
            635315458288360000,
            "3/27/2014 19:37:08.836"
         },
      (49): {
            635315458301160000,
            "3/27/2014 19:37:10.116"
         },
      (50): {
            635315458307760000,
            "3/27/2014 19:37:10.776"
         },
      (51): {
            635315458320480000,
            "3/27/2014 19:37:12.048"
         },
      (52): {
            635315458327080000,
            "3/27/2014 19:37:12.708"
         },
      (53): {
            635315458339840000,
            "3/27/2014 19:37:13.984"
         },
      (54): {
            635315458346560000,
            "3/27/2014 19:37:14.656"
         },
      (55): {
            635315458359240000,
            "3/27/2014 19:37:15.924"
         },
      (56): {
            635315458365840000,
            "3/27/2014 19:37:16.584"
         },
      (57): {
            635315458378640000,
            "3/27/2014 19:37:17.864"
         },
      (58): {
            635315458391240000,
            "3/27/2014 19:37:19.124"
         },
      (59): {
            635315458397840000,
            "3/27/2014 19:37:19.784"
         },
      (60): {
            635315458410480000,
            "3/27/2014 19:37:21.048"
         },
      (61): {
            635315458417200000,
            "3/27/2014 19:37:21.720"
         },
      (62): {
            635315458429960000,
            "3/27/2014 19:37:22.996"
         },
      (63): {
            635315458436550000,
            "3/27/2014 19:37:23.655"
         },
      (64): {
            635315458449230000,
            "3/27/2014 19:37:24.923"
         },
      (65): {
            635315458455910000,
            "3/27/2014 19:37:25.591"
         },
      (66): {
            635315458468750000,
            "3/27/2014 19:37:26.875"
         },
      (67): {
            635315458481390000,
            "3/27/2014 19:37:28.139"
         },
      (68): {
            635315458487990000,
            "3/27/2014 19:37:28.799"
         },
      (69): {
            635315458500750000,
            "3/27/2014 19:37:30.075"
         },
      (70): {
            635315458507350000,
            "3/27/2014 19:37:30.735"
         },
      (71): {
            635315458520190000,
            "3/27/2014 19:37:32.019"
         },
      (72): {
            635315458526790000,
            "3/27/2014 19:37:32.679"
         },
      (73): {
            635315458536070000,
            "3/27/2014 19:37:33.607"
         },
      (74): {
            635315458548470000,
            "3/27/2014 19:37:34.847"
         },
      (75): {
            635315458561150000,
            "3/27/2014 19:37:36.115"
         },
      (76): {
            635315458567870000,
            "3/27/2014 19:37:36.787"
         },
      (77): {
            635315458580590000,
            "3/27/2014 19:37:38.059"
         },
      (78): {
            635315458587190000,
            "3/27/2014 19:37:38.719"
         },
      (79): {
            635315458595900000,
            "3/27/2014 19:37:39.590"
         },
      (80): {
            635315458606900000,
            "3/27/2014 19:37:40.690"
         },
      (81): {
            635315458619540000,
            "3/27/2014 19:37:41.954"
         },
      (82): {
            635315458626140000,
            "3/27/2014 19:37:42.614"
         },
      (83): {
            635315458638870000,
            "3/27/2014 19:37:43.887"
         },
      (84): {
            635315458647380000,
            "3/27/2014 19:37:44.738"
         },
      (85): {
            635315458659780000,
            "3/27/2014 19:37:45.978"
         },
      (86): {
            635315458666380000,
            "3/27/2014 19:37:46.638"
         },
      (87): {
            635315458679100000,
            "3/27/2014 19:37:47.910"
         },
      (88): {
            635315458691980000,
            "3/27/2014 19:37:49.198"
         },
      (89): {
            635315458698580000,
            "3/27/2014 19:37:49.858"
         },
      (90): {
            635315458707100000,
            "3/27/2014 19:37:50.710"
         },
      (91): {
            635315458719500000,
            "3/27/2014 19:37:51.950"
         },
      (92): {
            635315458726180000,
            "3/27/2014 19:37:52.618"
         },
      (93): {
            635315458735500000,
            "3/27/2014 19:37:53.550"
         },
      (94): {
            635315458746500000,
            "3/27/2014 19:37:54.650"
         },
      (95): {
            635315458759180000,
            "3/27/2014 19:37:55.918"
         },
      (96): {
            635315458771890000,
            "3/27/2014 19:37:57.189"
         },
      (97): {
            635315458778490000,
            "3/27/2014 19:37:57.849"
         },
      (98): {
            635315458787810000,
            "3/27/2014 19:37:58.781"
         },
      (99): {
            635315458798730000,
            "3/27/2014 19:37:59.873"
         },
      (100): {
            635315458811410000,
            "3/27/2014 19:38:01.141"
         },
      (101): {
            635315458819410000,
            "3/27/2014 19:38:01.941"
         },
      (102): {
            635315458826010000,
            "3/27/2014 19:38:02.601"
         },
      (103): {
            635315458838690000,
            "3/27/2014 19:38:03.869"
         },
      (104): {
            635315458851450000,
            "3/27/2014 19:38:05.145"
         },
      (105): {
            635315458858130000,
            "3/27/2014 19:38:05.813"
         }
      }
      ATTRIBUTE "CLASS" {
         DATATYPE  H5T_STRING {
            STRSIZE 16;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SCALAR
         DATA {
         (0): "DIMENSION_SCALE"
         }
      }
      ATTRIBUTE "REFERENCE_LIST" {
         DATATYPE  H5T_COMPOUND {
            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
            H5T_STD_I32LE "dimension";
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): {
               DATASET 5304 /MET ,
               0
            }
         }
      }
      ATTRIBUTE "Time_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "v1.02"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
         DATA {
         (0): ".Net DateTime.Ticks", "DateTime"
         }
      }
   }
   DATASET "TempRH" {
      DATATYPE  H5T_COMPOUND {
         H5T_ENUM {
            H5T_STD_I32LE;
            "POWERED_OFF"      0;
            "INITIALIZING"     1;
            "OPERATIONAL"      2;
            "UNKNOWN"          3;
         } "status";
         H5T_IEEE_F32LE "air_temperature";
         H5T_IEEE_F32LE "relative_humidity";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            OPERATIONAL,
            24,
            12.7
         },
      (1): {
            OPERATIONAL,
            24,
            12.7
         },
      (2): {
            OPERATIONAL,
            24,
            12.7
         },
      (3): {
            OPERATIONAL,
            24,
            12.7
         },
      (4): {
            OPERATIONAL,
            24,
            12.7
         },
      (5): {
            OPERATIONAL,
            24,
            12.7
         },
      (6): {
            OPERATIONAL,
            24,
            12.7
         },
      (7): {
            OPERATIONAL,
            24,
            12.7
         },
      (8): {
            OPERATIONAL,
            24,
            12.7
         },
      (9): {
            OPERATIONAL,
            24,
            12.7
         },
      (10): {
            OPERATIONAL,
            24,
            12.7
         },
      (11): {
            OPERATIONAL,
            24,
            12.7
         },
      (12): {
            OPERATIONAL,
            24,
            12.7
         },
      (13): {
            OPERATIONAL,
            24,
            12.7
         },
      (14): {
            OPERATIONAL,
            24,
            12.7
         },
      (15): {
            OPERATIONAL,
            24,
            12.7
         },
      (16): {
            OPERATIONAL,
            24,
            12.7
         },
      (17): {
            OPERATIONAL,
            24,
            12.7
         },
      (18): {
            OPERATIONAL,
            24,
            12.7
         },
      (19): {
            OPERATIONAL,
            24,
            12.7
         },
      (20): {
            OPERATIONAL,
            24,
            12.7
         },
      (21): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (22): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (23): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (24): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (25): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (26): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (27): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (28): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (29): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (30): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (31): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (32): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (33): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (34): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (35): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (36): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (37): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (38): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (39): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (40): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (41): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (42): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (43): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (44): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (45): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (46): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (47): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (48): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (49): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (50): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (51): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (52): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (53): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (54): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (55): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (56): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (57): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (58): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (59): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (60): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (61): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (62): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (63): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (64): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (65): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (66): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (67): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (68): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (69): {
            OPERATIONAL,
            23.9,
            12.7
         },
      (70): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (71): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (72): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (73): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (74): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (75): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (76): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (77): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (78): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (79): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (80): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (81): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (82): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (83): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (84): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (85): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (86): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (87): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (88): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (89): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (90): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (91): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (92): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (93): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (94): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (95): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (96): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (97): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (98): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (99): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (100): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (101): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (102): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (103): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (104): {
            OPERATIONAL,
            23.9,
            12.8
         },
      (105): {
            OPERATIONAL,
            23.9,
            12.8
         }
      }
      ATTRIBUTE "ATEC_Level" {
         DATATYPE  H5T_STD_I32LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 1
         }
      }
      ATTRIBUTE "DIMENSION_LIST" {
         DATATYPE  H5T_VLEN { H5T_REFERENCE { H5T_STD_REF_OBJECT }}
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): (DATASET 8952 /TempRH_Time )
         }
      }
      ATTRIBUTE "Data_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_1.0"
         }
      }
      ATTRIBUTE "H&S" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_H&S"
         }
      }
      ATTRIBUTE "HumanVerified" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "false"
         }
      }
      ATTRIBUTE "LocHeight" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): 2
         }
      }
      ATTRIBUTE "LocationID" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "ALX 3"
         }
      }
      ATTRIBUTE "LocationSource" {
         DATATYPE  H5T_ENUM {
            H5T_STD_I32LE;
            "APPROXIMATE"      0;
            "SURVEYED"         1;
            "GPS_FIXED"        2;
            "GPS_MOBILE"       3;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): SURVEYED
         }
      }
      ATTRIBUTE "LocationUTM" {
         DATATYPE  H5T_IEEE_F64LE
         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
         DATA {
         (0): 4.43576e+06, 325215, 1327
         }
      }
      ATTRIBUTE "QA" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "MET_QA"
         }
      }
      ATTRIBUTE "Source" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "RI MET TG_met0001"
         }
      }
      ATTRIBUTE "UTM_Zone" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "12T"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 3 ) / ( 3 ) }
         DATA {
         (0): "enum", "degrees C", "%"
         }
      }
   }
   DATASET "TempRH_Time" {
      DATATYPE  H5T_COMPOUND {
         H5T_STD_I64LE "time";
         H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         } "timeString";
      }
      DATASPACE  SIMPLE { ( 106 ) / ( H5S_UNLIMITED ) }
      DATA {
      (0): {
            635315457807950000,
            "3/27/2014 19:36:20.795"
         },
      (1): {
            635315457820710000,
            "3/27/2014 19:36:22.071"
         },
      (2): {
            635315457827310000,
            "3/27/2014 19:36:22.731"
         },
      (3): {
            635315457835830000,
            "3/27/2014 19:36:23.583"
         },
      (4): {
            635315457846830000,
            "3/27/2014 19:36:24.683"
         },
      (5): {
            635315457859630000,
            "3/27/2014 19:36:25.963"
         },
      (6): {
            635315457866430000,
            "3/27/2014 19:36:26.643"
         },
      (7): {
            635315457879190000,
            "3/27/2014 19:36:27.919"
         },
      (8): {
            635315457891870000,
            "3/27/2014 19:36:29.187"
         },
      (9): {
            635315457898470000,
            "3/27/2014 19:36:29.847"
         },
      (10): {
            635315457911140000,
            "3/27/2014 19:36:31.114"
         },
      (11): {
            635315457918540000,
            "3/27/2014 19:36:31.854"
         },
      (12): {
            635315457931220000,
            "3/27/2014 19:36:33.122"
         },
      (13): {
            635315457937900000,
            "3/27/2014 19:36:33.790"
         },
      (14): {
            635315457950580000,
            "3/27/2014 19:36:35.058"
         },
      (15): {
            635315457957180000,
            "3/27/2014 19:36:35.718"
         },
      (16): {
            635315457965860000,
            "3/27/2014 19:36:36.586"
         },
      (17): {
            635315457976900000,
            "3/27/2014 19:36:37.690"
         },
      (18): {
            635315457985420000,
            "3/27/2014 19:36:38.542"
         },
      (19): {
            635315457996540000,
            "3/27/2014 19:36:39.654"
         },
      (20): {
            635315458009300000,
            "3/27/2014 19:36:40.930"
         },
      (21): {
            635315458015900000,
            "3/27/2014 19:36:41.590"
         },
      (22): {
            635315458025180000,
            "3/27/2014 19:36:42.518"
         },
      (23): {
            635315458037700000,
            "3/27/2014 19:36:43.770"
         },
      (24): {
            635315458048740000,
            "3/27/2014 19:36:44.874"
         },
      (25): {
            635315458061420000,
            "3/27/2014 19:36:46.142"
         },
      (26): {
            635315458068100000,
            "3/27/2014 19:36:46.810"
         },
      (27): {
            635315458080850000,
            "3/27/2014 19:36:48.085"
         },
      (28): {
            635315458087450000,
            "3/27/2014 19:36:48.745"
         },
      (29): {
            635315458100130000,
            "3/27/2014 19:36:50.013"
         },
      (30): {
            635315458106690000,
            "3/27/2014 19:36:50.669"
         },
      (31): {
            635315458115970000,
            "3/27/2014 19:36:51.597"
         },
      (32): {
            635315458126970000,
            "3/27/2014 19:36:52.697"
         },
      (33): {
            635315458139650000,
            "3/27/2014 19:36:53.965"
         },
      (34): {
            635315458146250000,
            "3/27/2014 19:36:54.625"
         },
      (35): {
            635315458158930000,
            "3/27/2014 19:36:55.893"
         },
      (36): {
            635315458167450000,
            "3/27/2014 19:36:56.745"
         },
      (37): {
            635315458178770000,
            "3/27/2014 19:36:57.877"
         },
      (38): {
            635315458191570000,
            "3/27/2014 19:36:59.157"
         },
      (39): {
            635315458198250000,
            "3/27/2014 19:36:59.825"
         },
      (40): {
            635315458210970000,
            "3/27/2014 19:37:01.097"
         },
      (41): {
            635315458217610000,
            "3/27/2014 19:37:01.761"
         },
      (42): {
            635315458230370000,
            "3/27/2014 19:37:03.037"
         },
      (43): {
            635315458237010000,
            "3/27/2014 19:37:03.701"
         },
      (44): {
            635315458249680000,
            "3/27/2014 19:37:04.968"
         },
      (45): {
            635315458256280000,
            "3/27/2014 19:37:05.628"
         },
      (46): {
            635315458269000000,
            "3/27/2014 19:37:06.900"
         },
      (47): {
            635315458281760000,
            "3/27/2014 19:37:08.176"
         },
      (48): {
            635315458288360000,
            "3/27/2014 19:37:08.836"
         },
      (49): {
            635315458301160000,
            "3/27/2014 19:37:10.116"
         },
      (50): {
            635315458307760000,
            "3/27/2014 19:37:10.776"
         },
      (51): {
            635315458320480000,
            "3/27/2014 19:37:12.048"
         },
      (52): {
            635315458327080000,
            "3/27/2014 19:37:12.708"
         },
      (53): {
            635315458339840000,
            "3/27/2014 19:37:13.984"
         },
      (54): {
            635315458346560000,
            "3/27/2014 19:37:14.656"
         },
      (55): {
            635315458359240000,
            "3/27/2014 19:37:15.924"
         },
      (56): {
            635315458365840000,
            "3/27/2014 19:37:16.584"
         },
      (57): {
            635315458378640000,
            "3/27/2014 19:37:17.864"
         },
      (58): {
            635315458391240000,
            "3/27/2014 19:37:19.124"
         },
      (59): {
            635315458397840000,
            "3/27/2014 19:37:19.784"
         },
      (60): {
            635315458410480000,
            "3/27/2014 19:37:21.048"
         },
      (61): {
            635315458417200000,
            "3/27/2014 19:37:21.720"
         },
      (62): {
            635315458429960000,
            "3/27/2014 19:37:22.996"
         },
      (63): {
            635315458436550000,
            "3/27/2014 19:37:23.655"
         },
      (64): {
            635315458449230000,
            "3/27/2014 19:37:24.923"
         },
      (65): {
            635315458455910000,
            "3/27/2014 19:37:25.591"
         },
      (66): {
            635315458468750000,
            "3/27/2014 19:37:26.875"
         },
      (67): {
            635315458481390000,
            "3/27/2014 19:37:28.139"
         },
      (68): {
            635315458487990000,
            "3/27/2014 19:37:28.799"
         },
      (69): {
            635315458500750000,
            "3/27/2014 19:37:30.075"
         },
      (70): {
            635315458507350000,
            "3/27/2014 19:37:30.735"
         },
      (71): {
            635315458520190000,
            "3/27/2014 19:37:32.019"
         },
      (72): {
            635315458526790000,
            "3/27/2014 19:37:32.679"
         },
      (73): {
            635315458536070000,
            "3/27/2014 19:37:33.607"
         },
      (74): {
            635315458548470000,
            "3/27/2014 19:37:34.847"
         },
      (75): {
            635315458561150000,
            "3/27/2014 19:37:36.115"
         },
      (76): {
            635315458567870000,
            "3/27/2014 19:37:36.787"
         },
      (77): {
            635315458580590000,
            "3/27/2014 19:37:38.059"
         },
      (78): {
            635315458587190000,
            "3/27/2014 19:37:38.719"
         },
      (79): {
            635315458595900000,
            "3/27/2014 19:37:39.590"
         },
      (80): {
            635315458606900000,
            "3/27/2014 19:37:40.690"
         },
      (81): {
            635315458619540000,
            "3/27/2014 19:37:41.954"
         },
      (82): {
            635315458626140000,
            "3/27/2014 19:37:42.614"
         },
      (83): {
            635315458638870000,
            "3/27/2014 19:37:43.887"
         },
      (84): {
            635315458647380000,
            "3/27/2014 19:37:44.738"
         },
      (85): {
            635315458659780000,
            "3/27/2014 19:37:45.978"
         },
      (86): {
            635315458666380000,
            "3/27/2014 19:37:46.638"
         },
      (87): {
            635315458679100000,
            "3/27/2014 19:37:47.910"
         },
      (88): {
            635315458691980000,
            "3/27/2014 19:37:49.198"
         },
      (89): {
            635315458698580000,
            "3/27/2014 19:37:49.858"
         },
      (90): {
            635315458707100000,
            "3/27/2014 19:37:50.710"
         },
      (91): {
            635315458719500000,
            "3/27/2014 19:37:51.950"
         },
      (92): {
            635315458726180000,
            "3/27/2014 19:37:52.618"
         },
      (93): {
            635315458735500000,
            "3/27/2014 19:37:53.550"
         },
      (94): {
            635315458746500000,
            "3/27/2014 19:37:54.650"
         },
      (95): {
            635315458759180000,
            "3/27/2014 19:37:55.918"
         },
      (96): {
            635315458771890000,
            "3/27/2014 19:37:57.189"
         },
      (97): {
            635315458778490000,
            "3/27/2014 19:37:57.849"
         },
      (98): {
            635315458787810000,
            "3/27/2014 19:37:58.781"
         },
      (99): {
            635315458798730000,
            "3/27/2014 19:37:59.873"
         },
      (100): {
            635315458811410000,
            "3/27/2014 19:38:01.141"
         },
      (101): {
            635315458819410000,
            "3/27/2014 19:38:01.941"
         },
      (102): {
            635315458826010000,
            "3/27/2014 19:38:02.601"
         },
      (103): {
            635315458838690000,
            "3/27/2014 19:38:03.869"
         },
      (104): {
            635315458851450000,
            "3/27/2014 19:38:05.145"
         },
      (105): {
            635315458858130000,
            "3/27/2014 19:38:05.813"
         }
      }
      ATTRIBUTE "CLASS" {
         DATATYPE  H5T_STRING {
            STRSIZE 16;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SCALAR
         DATA {
         (0): "DIMENSION_SCALE"
         }
      }
      ATTRIBUTE "REFERENCE_LIST" {
         DATATYPE  H5T_COMPOUND {
            H5T_REFERENCE { H5T_STD_REF_OBJECT } "dataset";
            H5T_STD_I32LE "dimension";
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): {
               DATASET 8512 /TempRH ,
               0
            }
         }
      }
      ATTRIBUTE "Time_Version" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 1 ) / ( 1 ) }
         DATA {
         (0): "v1.0"
         }
      }
      ATTRIBUTE "Units" {
         DATATYPE  H5T_STRING {
            STRSIZE H5T_VARIABLE;
            STRPAD H5T_STR_NULLTERM;
            CSET H5T_CSET_ASCII;
            CTYPE H5T_C_S1;
         }
         DATASPACE  SIMPLE { ( 2 ) / ( 2 ) }
         DATA {
         (0): ".Net DateTime.Ticks", "DateTime"
         }
      }
   }
}
}
