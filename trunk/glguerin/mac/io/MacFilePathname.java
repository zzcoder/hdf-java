/*
 * For conditions of distribution and use, see the accompanying             *
 * hdf/COPYING file.                                                        *
*/

package glguerin.mac.io;

import java.io.*;

import glguerin.io.*;


// --- Revision History ---
// 12Mar99 GLG  created
// 16Mar99 GLG  name changes
// 17Mar99 GLG  edit doc-comments
// 17Mar99 GLG  refactor to use escaping basics from Path class
// 17Mar99 GLG  add constructor with MacFilePathname arg
// 26Mar99 GLG  edit doc-comments
// 31Mar99 GLG  add doc-comments
// 31Mar99 GLG  refactor to use buildPath()
// 01Apr99 GLG  cover class-name change to MacFiling
// 08Apr99 GLG  add escaping-sensitive getPart(int), getParts(), and getLast() methods
// 18Apr99 GLG  method name-changes
// 22Apr99 GLG  cover class-name change to MacEscaping
// 26Apr99 GLG  edit doc-comments
// 07May99 GLG  add setFrom(MacFilePathname) -- D'OH!!!
// 10May99 GLG  add appendFrom(MacFilePathname) -- DOUBLE D'OH!!!


/**
** A MacFilePathname extends the behavior and features of FilePathname to
** be aware of certain Mac-specific constructs.  These are:
**<ol>
**   <li> when converting between Files and MacFilePathnames, escaping is taken into account.
**   <li> the ability to produce Mac-native Paths, i.e. colon-separated and unescaped.
**</ol>
**<p>
** Just like a FilePathname, but unlike a File, a MacFilePathname only operates on
** parts that represent the elements of a name -- it makes no reference to the actual file-system.
** That is, you can't do MacFilePathname.exists() or MacFilePathname.length() to discover
** things about the targeted file-system object.  Of course, it's pretty easy to use
** getPath() in a File constructor to make a File of interest.
** And since MacFilePathname.getPath() is aware of escaping, things won't break on
** names with literal slashes or percents in them.
** You can also, of course, use a MacFileForker for many of the methods you
** might use a File for.
**<p>
** File-name escaping is handled using the MacEscaping class.
** Internally, parts are kept in unescaped form, so Path.append() is overridden here.
** Each instance of MacFilePathname has an internal flag that tells it whether to use
** escaping or not.  The initial state of that flag comes from the public static boolean variable
** <b>defaultEscaping</b>.  That static variable is initially
** set to <b>MacEscaping.platformUsesEscapes()</b>,
** but you can change it at any time.
**<p>
** Since each MacFilePathname has its own internal sense of escaping, you can
** invoke <b>setEscaping( boolean )</b> any time you need to change that state.
** You can examine the current state with <b>boolean getEscaping()</b>.
**<p>
** Mac-native path-name formats are returned from <b>getMacPath().</b>
** The <b>appendMacPath( String )</b> method parses a String in Mac-native form
** and appends the parts.  Mac-native name forms are never escaped, regardless
** of the state of the internal escaping flag.
**<p>
** No methods in this class are synchronized, even when a thread-safety issue
** is known to exist.
** If you need thread-safety you must provide it yourself.
** The most common uses of this class do not involve shared access
** by multiple threads, so synchronizing seemed like a time-wasting overkill.
** If you disagree, you have the source...
**
** @author Gregory Guerin
**
** @see glguerin.io.FilePathname
** @see glguerin.io.Path
** @see MacFileForker
** @see MacEscaping
*/

public class MacFilePathname
  extends FilePathname
{
    /**
    ** The value of this static field is copied to the initial state of
    ** a MacFilePathname's escaping-state at instantiation.
    ** Change this value if you want new MacFilePathname's to have different
    ** default escaping behavior.
    ** This field is initialized from <b>MacEscaping.platformUsesEscapes()</b>,
    ** which is usually the right thing to do.
    */
    public static boolean defaultEscaping = MacEscaping.platformUsesEscapes();


    /**  Internal state of instance's escaping.  */
    private boolean myEscaping;



    /**
    ** Create with no parts.
    ** Initially set my escaping state from current state of static field defaultEscaping.
    */
    public
    MacFilePathname()
    {
        super();
        myEscaping = defaultEscaping;
    }

    /**
    ** Create by copying parts from a File, with this instance set to default escaping state.
    */
    public
    MacFilePathname( File theFile )
    {
        this();
        setFrom( theFile );
    }

    /**
    ** Create by copying initial escaping state and parts from the other MacFilePathname.
    */
    public
    MacFilePathname( MacFilePathname other )
    {
        this();
        setFrom( other );
    }


    // ###  A D D I T I O N A L   M E T H O D S  ###

    /**
    ** Set from the given MacFilePathname, copying its parts and escaping.
    */
    public void
    setFrom( MacFilePathname other )
    {
        // This is sequenced so that escaping occurs properly for this and for other.
        // There are other acceptable sequences, too.
        boolean escaped = other.getEscaping();
        String[] parts = other.getParts( escaped );
        setEscaping( escaped );
        set( parts );
    }


    /**
    ** Append from the given MacFilePathname, copying its parts but NOT its escaping.
    ** The other's escaping is only used temporarily, while appending the other's parts,
    ** then this's original escaping is restored.
    */
    public void
    appendFrom( MacFilePathname other )
    {
        boolean escaped = other.getEscaping();
        String[] parts = other.getParts( escaped );
        boolean was = setEscaping( escaped );
        appendAll( parts );
        setEscaping( was );
    }


    /**
    ** Return true if this instance is escaping its parts, false if not.
    **
    ** @see #setEscaping
    */
    public boolean
    getEscaping()
    {  return ( myEscaping );  }

    /**
    ** Set the state of escaping to the given flag, returning the prior state.
    ** If the escaping-state is false when some parts are appended that contain escapes,
    ** setting the state to true will have no effect on the existing parts.
    ** These time-sensitive semantics also affect how getPath() and toString()
    ** convert the internal parts into returned values.
    **<p>
    ** Returning the prior state is a convenience in some cases where that prior
    ** state must be restored after escaping is temporarily disabled.
    ** To be thread-safe, this method should operate atomically,
    ** though it takes no such precautions.  That is, this method isn't thread-safe.
    **
    ** @see #getEscaping
    ** @see #getPath
    ** @see #toString
    */
    public boolean
    setEscaping( boolean useEscapes )
    {
        boolean was = myEscaping;
        myEscaping = useEscapes;
        return ( was );
    }


    /**
    ** Return the last part, escaped according to the current internal state.
    **
    ** @see #getEscaping
    ** @see #last
    */
    public String
    getLast()
    {
        String part = last();
        if ( getEscaping() )
            part = toEscapedForm( part );
        return ( part );
    }

    /**
    ** Return the indexed part, escaped according to the current internal state.
    **
    ** @see #getEscaping
    ** @see #getPart( int, boolean )
    */
    public String
    getPart( int index )
    {  return ( getPart( index, getEscaping() ) );  }

    /**
    ** Return a new array holding all the parts, escaped according to the current internal state.
    **
    ** @see #getEscaping
    ** @see #getParts( boolean )
    */
    public String[]
    getParts()
    {  return ( getParts( getEscaping() ) );  }


    /**
    ** Return a String consisting of the Path parts separated by colons,
    ** in a Mac-native absolute-path form, and without escaping any of the parts.
    ** Since escaping is only needed for Java's representation of file-names,
    ** a Mac-native name should never be escaped.
    **<p>
    ** An absolute Mac-native path normally contains a volume-name and
    ** an item-name (file or folder), so there must normally be at least two parts in
    ** this MacFilePathname in order to produce a well-formed Mac-native path name.
    ** If this method sees that there are not two parts to work with, it
    ** returns either an empty string (no parts) or a single name with a trailing colon
    ** (1 part).  The latter representation refers to the root directory of a named volume.
    ** The no-part representation refers to nothing, and is not a valid Mac-native path name.
    */
    public String
    getMacPath()
    {
        // Note that escaping is never used here.
        // The initial buildPath() won't put a trailing ':' on the StringBuffer,
        // but otherwise works fine for the no-part and one-part cases.
        StringBuffer built = buildPath( MacEscaping.NATIVE_SEP, false, false );

        // Handle the one-part case by appending a trailing ':'.
        if ( partCount() == 1 )
            built.append( MacEscaping.NATIVE_SEP );

        return ( built.toString() );
    }

    /**
    ** Append the parts parsed from the Mac-native path-name,
    ** ensuring that escaping is not applied.
    ** Since escaping is only needed for Java's representation of file-names,
    ** a Mac-native name is never escaped, and never converted from escaped form.
    **<p>
    ** The semantic construct "::" meaning "parent-directory-of" is not covered
    ** by this method.  This method uses a simple parsing of macPathName by the ":" separator.
    **<p>
    ** This method only appends, so if you want to set the entire path
    ** you should clear() this first.
    */
    public void
    appendMacPath( String macPathName )
    {
        // We have to use an escape-insensitive append().
        // We could use super.append() in a loop here, or
        // save and restore the current escaping flag.
        // I chose the latter because it seems simpler.
        // To be thread-safe, this should all occur atomically.
        boolean wasEscapes = setEscaping( false );
        appendAll( split( macPathName, MacEscaping.NATIVE_SEP ) );
        setEscaping( wasEscapes );
    }


    // ###  O V E R R I D E S  ###

    /**
    ** Transform the given String to an escaped form, whatever that may be.
    ** Must accept null or zero-length Strings, and return them without error or exception.
    **<p>
    ** This method must always perform a transformation, regardless of any internal
    ** state indicating whether escapes are in use or not.
    ** That is, this is strictly a transforming method, not a state-sensitive accessor method.
    **<p>
    ** This implementation use MacEscaping.toEscapedName() to transform the argument.
    */
    public String
    toEscapedForm( String toTransform )
    {  return ( MacEscaping.toEscapedName( toTransform ) );  }

    /**
    ** Transform the given String to a literal (unescaped) form, whatever that may be.
    ** Must accept null or zero-length Strings, and return them without error or exception.
    **<p>
    ** This method must always perform a transformation, regardless of any internal
    ** state indicating whether escapes are in use or not.
    ** That is, this is strictly a transforming method, not a state-sensitive accessor method.
    **<p>
    ** This implementation use MacEscaping.toLiteralName() to transform the argument.
    */
    public String
    toLiteralForm( String toTransform )
    {  return ( MacEscaping.toLiteralName( toTransform ) );  }



    /**
    ** Override Path.append() to add the given part after converting it from
    ** escaped to literal form, as indicated by this instance's current escaping state.
    ** That is, this implementation is sensitive to the current escaping state.
    **<p>
    ** If you wanted to forcibly truncate each part to the 31-byte limit imposed by
    ** the Mac OS, this would be the place to do it.  You'd have to apply this on the
    ** byte-array representation, encoded appropriately, since a 31-UniCode-character
    ** String may encode as more or less than 31 bytes, depending on what character
    ** values it contains.  For example, 31 Katakana chars will encode to be longer than 31 ASCII chars.
    **<p>
    ** An alternative to quiet truncation is to throw an IllegalArgumentException,
    ** which need not be declared as thrown by this method, since it's a RuntimeException.
    ** I don't do that since I wanted to be more flexible.
    ** If you disagree, you have the source...
    **
    ** @see glguerin.io.Path#append
    ** @see toLiteralForm
    */
    public void
    append( String toAppend )
    {
        if ( myEscaping )
            toAppend = toLiteralForm( toAppend );
        super.append( toAppend);
    }


    /**
    ** Return a String consisting of the Path parts separated by File.separatorChar,
    ** with a leading separator to indicate an absolute path-name, and with parts
    ** escaped or not according to the instance's current state.
    */
    public String
    getPath()
    {  return ( buildPath( File.separatorChar, true, getEscaping() ).toString() );  }


}

