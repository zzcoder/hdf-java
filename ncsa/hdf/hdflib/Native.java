/*
 *  Contributed by Guy Brooker, with optimization suggested by
 *  Greg L. Guerin
 */
package ncsa.hdf.hdflib;

import java.io.File;

import glguerin.mac.io.MacFilePathname;
import glguerin.util.MacPlatform;


public class Native {


    private static boolean isMac = MacPlatform.isMRJ();
    private static boolean isWin = (System.getProperty("os.name")).toLowerCase().startsWith("win");

    private Native() {};

    public static String nativeFilePath(String javaFileName) {

        if (isWin) {
            return javaFileName.replace('\\', '/');
        } else if (isMac) {
            return nativeFilePath(new File(javaFileName));
        } else {
            return javaFileName;
        }

    }

    public static String nativeFilePath(File file) {

        String path = file.getAbsolutePath();

        if (isWin) {
            return path.replace('\\', '/');
        } else if (isMac) {

            MacFilePathname macPath = new MacFilePathname(file);

            return macPath.getMacPath();

        } else {
            return path;
        }
    }

    public static String javaFilePath(String nativeFileName) {

        if (isMac) {

            MacFilePathname macPath = new MacFilePathname();

            macPath.appendMacPath(nativeFileName);

            String javaPath = macPath.getPath();

            return javaPath;

        } else {
            return nativeFileName;
        }
    }

}
