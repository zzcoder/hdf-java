/*
 * For conditions of distribution and use, see the accompanying             *
 * hdf/COPYING file.                                                        *
*/

package glguerin.util;


// --- Revision History ---
// 12Apr99 GLG  create
// 14Apr99 GLG  add isMRJ() -- duh!
// 14Apr99 GLG  change package
// 22Apr99 GLG  add isMacOS() that uses mechanism different from isMRJ()
// 23Apr99 GLG  change package
// 02Jun99 GLG  change package yet again


/**
** MacPlatform contains static methods that return certain information
** about the platform the program is running on.
** Though this information is often Mac-specific, when the platform is not a Mac,
** safe values are returned indicating "not a Mac".  These may be zero or null.
**<p>
** This is pure Java and contains no Mac-platform dependencies,
** at least in the sense that it should run without crashing or dying on any platform.
** It may throw a SecurityException or other exception, but that's a different question.
**<p>
** This class could have been more accurately named Platform instead of MacPlatform.
** I didn't do that because the methods embodied here all distinguish Mac platforms from
** non-Mac platforms, or distinguish gradations amongst Mac OS platforms.
** Thus, it seemed appropriate to name this MacPlatform.
**
** @author Gregory Guerin
*/

public class MacPlatform
{

	/**
	** Determine whether the current platform is the Mac OS or not.
	** This method compares the "os.name" property's value to
	** the String "Mac OS" using a case-insensitive comparison.  
	** Upon matching, it returns true, else false.
	** Note that this uses a completely different mechanism than isMRJ().
	**<p>
	** I don't know what Mac OS X Server returns for "os.name" when 
	** running outside of the Blue Box.  Nor do I know what Mac OS X Consumer will return.
	**
	** @return  True if this platform is the Mac OS, false if not.
	*/
	public static boolean
	isMacOS()
	{  return ( "Mac OS".equalsIgnoreCase( System.getProperty( "os.name" ) ) );  }


	/**
	** Return true on MRJ platform, false on others, by determining
	** whether the platform supports JDirect or not.
	*/
	public static boolean
	isMRJ()
	{  return ( getJDirectVersion() != 0 );  }


	/**
	** Determine whether JDirect 1 or 2 is available, or not at all.
	** When JDirect 2 is available, you should use it.  JDirect 1 is currently available
	** whenever JDirect 2 is, but Apple may remove JDirect 1 in the future.
	** When only JDirect 1 is available, you should use it.
	** When JDirect is unavailable in any form, e.g. on all platforms other than MRJ,
	** zero is returned.
	** Note that the Classes looked for are not instantiated -- we only need to know
	** whether the Class itself is present and usable.
	**<p>
	** You can also use the returned value as an indication of MRJ-ness or not.
	** Any non-zero value means "MRJ", while zero means "not MRJ".
	** Indeed, isMRJ() does exactly that.
	**<p>
	** Arguably, <b>ANY</b> Throwable thrown when trying to load a Class
	** should be caught and result in moving to the next trial Class.
	** I chose not to do this because someone might want a SecurityException to
	** be caught at a higher level, and I didn't want to preclude that.
	**
	** @see #isMRJ()
	*/
	public static int
	getJDirectVersion()
	{
		// JDirect 2 is preferred over JDirect 1, when both are available.
		try
		{
			Class.forName( "com.apple.mrj.jdirect.JDirectLinker" );
			return ( 2 );
		}
		catch ( ClassNotFoundException why )
		{  /* FALL THROUGH */  }

		try
		{
			// A JDirect 1 class...
			Class.forName( "com.apple.memory.MemoryObject" );
			return ( 1 );
		}
		catch ( ClassNotFoundException why )
		{  /* FALL THROUGH */  }

		return ( 0 );
	}

}

