
/*
 * For conditions of distribution and use, see the accompanying             *
 * hdf/COPYING file.                                                        *
*/

package glguerin.io;

import java.io.*;


// --- Revision History ---
// 11Mar99 GLG  created
// 15Mar99 GLG  change some names
// 17Mar99 GLG  minor refactoring
// 27Mar99 GLG  remove junk; edit doc-comments
// 31Mar99 GLG  use new method buildPath()
// 01Apr99 GLG  change class name
// 02Apr99 GLG  expand doc-comments


/**
** A FilePathname is a sub-class of Path with additional methods for working with Files.
** Unlike a File, a FilePathname only operates on the parts that make up the name.
** That is, you can't do FilePathname.exists() or FilePathname.length() to discover
** things about the targeted file-system object.  Of course, it's pretty easy to use
** FilePathname.getPath() in a File constructor to make a File of interest.
**<p>
** A FilePathname always represents the absolute path, since Path has no
** facility for dealing with the concept of "relative" paths.
** The parts of the File's path are used as-is to assemble the Path, without regard
** to any possible escaping that they may contain.  That is, this class has no knowledge
** of escapes within the File-path parts, and always operates on them literally.
** This is as it should be for a simple, basic class.
**<p>
** No methods in this class are synchronized, even when a thread-safety issue
** is known to exist.
** If you need thread-safety you must provide it yourself.
** The most common uses of this class do not involve shared access
** by multiple threads, so synchronizing seemed like a time-wasting overkill.
** If you disagree, you have the source...
**
** @author Gregory Guerin
*/

public class FilePathname
  extends Path
{

    /**
    ** Create with no parts.
    */
    public
    FilePathname()
    {  super();  }

    /**
    ** Create by copying parts from a File.
    */
    public
    FilePathname( File theFile )
    {  this();  setFrom( theFile );  }

    /**
    ** Create by parsing a String as if it were a File's absolute or relative path-name.
    ** Don't assume that this constructor is more efficient than new FilePathname(File).
    ** It invokes setPath(String), which internally creates a temporary File object,
    ** so the FilePathname(File) constructor is actually more efficient since it uses
    ** an existing File.
    */
    public
    FilePathname( String path )
    {  this();  setPath( path );  }


    /**
    ** Set this object's parts from the separated pieces of the File's absolute path.
    ** If the File is null, this is cleared without causing an error.
    */
    public void
    setFrom( File theFile )
    {
        if ( theFile != null )
            setFrom( theFile.getAbsolutePath(), File.separatorChar );
        else
            clear();
    }



    /**
    ** Return a String consisting of the Path parts separated by File.separatorChar,
    ** with a leading separator to indicate an absolute path-name,
    ** and without any escaping applied.
    */
    public String
    getPath()
    {  return ( buildPath( File.separatorChar, true, false ).toString() );  }

    /**
    ** Set the parts from the given String, interpreted as a relative or absolute path,
    ** effectively mirroring the handling of path-names in File.
    ** This object is set to the resulting absolute-path, even if the path String
    ** indicates a relative path.
    ** If path is a zero-length String, this object is set to the "user.dir" directory.
    ** If path is null, this object is simply cleared, with no other objects being temporarily created.
    **<p>
    ** Internally we use a temporary new File to perform all
    ** the File-specific aspects of this transformation.
    ** Thus, this method is less efficient than setFrom(File), which works
    ** with an existing File object.
    */
    public void
    setPath( String path )
    {
        if ( path != null )
            setFrom( new File( path ) );
        else
            clear();
    }


}

