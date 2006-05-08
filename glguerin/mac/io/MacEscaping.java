/*
 * For conditions of distribution and use, see the accompanying             *
 * hdf/COPYING file.                                                        *
*/

package glguerin.mac.io;

import java.io.UnsupportedEncodingException;

import glguerin.util.MacPlatform;


// --- Revision History ---
// 26Feb99 GLG  create
// 02Mar99 GLG  method name changes
// 02Mar99 GLG  add containsNonliterals()
// 09Mar99 GLG  add platformUsesEscapes()
// 09Mar99 GLG  rename method to containsMacEscapees()
// 12Mar99 GLG  add code to methods so null String works (returns null)
// 26Mar99 GLG  move getOSTypeString() here
// 18Apr99 GLG  add fromMacRoman(), and change how toLiteralName() decodes escapes
// 22Apr99 GLG  change name, add doc-comments
// 22Apr99 GLG  change match-string in platformUsesEscapes()
// 26Apr99 GLG  doc-comment cleanup
// 02Jun99 GLG  cover package changes


/**
** MacEscaping contains static utility methods for working with escaped
** and unescaped Mac OS file-names.
** It currently provides translation between the escaped (i.e. Java-friendly)
** and unescaped (i.e. raw) forms of names.  It does not translate from
** the Java/MRJ form where "/" is a separator, to the native-Mac form
** where ":" is a separator (and different semantics also exist) -- use a MacFilePathname for that.
** Escaping is used under MRJ because the Mac OS allows /'s in its file-names,
** and at the same time uses "/" as the File.separator to represent path-names.
** Indeed, Mac OS allows any character in a file-name except the native ":" separator.
**<p>
** The above situation with "/" arises out of Java's rather limited and Unix-centered view
** towards file-systems.
** Though java.io.File is intended to hide these, it doesn't entirely succeed.
** At the very least, the programmers using File were very Unix-centric in their use of File,
** which leads to the same result -- things that should be abstract and hidden aren't:
**<ol>
**   <li>The semantics of relative vs. absolute paths is Unix-centric, and
**     exactly opposite to the Mac OS's native semantics.  That is, under Unix an absolute
**     path starts with File.separator, but under Mac OS that syntax means a relative path.
**   <li>The meaning of repeated adjacent File.separator's is presumed to follow
**     the Unix-centered approach, i.e. repeats are ignored.  This is not true of Mac OS.
**   <li>Named references to "this directory" and "parent directory" are presumed
**     to be "." and "..".  This is not true of Mac OS, where the native separator ":"
**     is used for this purpose.
**   <li>File-system names are presumed to be legal if they start with ".".
**     Mac OS used to forbid this, and many existing programs are still unable to
**     handle this correctly.  Since Mac OS 7, it's not illegal, though this has not fixed
**     the programs (or programmers) who still do things poorly.  Not an issue for new
**     code, until you throw an old broken program into the work-flow.  Oops.
**</ol>
** All these eminently foreseeable defects in File made it a real, um, <i>challenge </i>to
** implement Java under Mac OS.  Even if File had been defined to use URL semantics to
** represent file-names, it would have been better.  Alas, now we're stuck with it --
** a stone-axe in a laser-scalpel world.
**<p>
**<i>[And this doesn't even cover the case where Mac OS allows
** multiple volumes of the same name to be mounted at the same time, without ambiguity.
** With path-names alone, Java programs on the Mac are still susceptible to this ambiguity.
** If you can't think of why or how that might happen, imagine a CD-ROM back-up of a volume,
** or two CD-ROM's mounted at the same time, containing different revisions of a
** commercial product.  You want to do a differential compare, yet you can't because
** the names alone are ambiguous.  If you actually need to disambiguate something like this,
** JConfig may help you:
**<br>&nbsp;&nbsp;
** <b>http://www.tolstoy.com/samizdat/jconfig.html</b>
** ]</i>
**<p>
** Thus, to avoid difficulties in working with Java programs
** (and programmers) that already operated under Unix semantics when
** working with Files, MRJ's creators chose to just provide Unix-like semantics as much as possible.
** This includes the use of "/" as separator, the use of "." and ".." as directory-names,
** and various other aspects.
** One difficulty with this scheme is that a literal "/" is not uncommon in native Mac OS file-names.
** So MRJ uses an escaping scheme similar to the HTTP URL scheme, i.e. the character '%'
** introduces a two-hex-digit escape encoding the character-value.
** In MRJ 2.0 and earlier, some ASCII characters were escaped in addition to "/" and "%" themselves.
** Also, MacRoman characters were escaped as their MacRoman byte-values rather than
** converted to UniCode chars,
** e.g. %83 represents <b>capital-E acute</b>, not a UniCode control-char.
** Under MRJ 2.1, only "/" and "%" are escaped, and MacRoman characters are
** translated to UniCode chars.
**<p>
** Note that these escapes are all Java-only constructs, and appear only to Java programs running
** under MRJ.  Calls to native Mac OS code still require the Mac-format path-name without escapes.
** Users also expect to see a file named "4/99 Budget" as exactly that, not "4%2f99 Budget",
** so hanlding escapes is an important user-visible feature, too.
**
** @author Gregory Guerin
**
** @see MacCatalogInfo
** @see MacFilePathname
*/

public class MacEscaping
{
    /**  The MRJ escaping character, '%'.*/
    public static final char ESCAPE = '%';

    /**  MRJ's file-separator as Java-code sees it, '/'. */
    public static final char MRJ_SEP = '/';

    /**
    ** Mac OS's native file-separator, ':'.
    ** This character has a special meaning under Microsoft's OS'es, too,
    ** so you may encounter problems if you blindly convert something like
    ** "C:foo\bar\barf.dat" to what may seem to be a platform-neutral form.
    */
    public static final char NATIVE_SEP = ':';

    /**
    ** What you should substitute for literal NATIVE_SEP characters
    ** that might appear in file-names you deal with, '-'.
    ** Don't try this on a path-name from a Microsoft OS, though, or
    ** you'll nuke the "drive-letter" part of the name.
    */
    public static final char NATIVE_SUB = '-';


    /** String to replace instances of ESCAPE with. */
    private static String ESCAPE_SUB;

    /** String to replace instances of MRJ_SEP with. */
    private static String MRJ_SUB;

    /** A MacRoman-to-UniCode translation table, used for unescaping. */
    private static char[] charsMacRoman;

    static
    {
        StringBuffer build = new StringBuffer( 3 );
        build.append( ESCAPE ).append( Integer.toHexString( ESCAPE ) );
        ESCAPE_SUB = build.toString().intern();

        build.setLength( 0 );
        build.append( ESCAPE ).append( Integer.toHexString( MRJ_SEP ) );
        MRJ_SUB = build.toString().intern();

        // Generate a MacRoman mapping in charsMacRoman array...
        try
        {
            byte[] bytes = new byte[ 256 ];
            for ( int i = 0;  i < bytes.length;  ++i )
            {  bytes[ i ] = (byte) i;  }

            charsMacRoman = new String( bytes, "MacRoman" ).toCharArray();
        }
        catch ( UnsupportedEncodingException why )
        {
            // If not an available encoding, the mapping is isomorphic (i.e. no change).
            char[] chars = new char[ 256 ];
            for ( int i = 0;  i < chars.length;  ++i )
            {  chars[ i ] = (char) i;  }

            charsMacRoman = chars;
        }
    }

    /**
    ** Translate the low 8-bits of the value into a UniCode char using the MacRoman encoding.
    ** That is, the low 8-bits of value are presumed to be a MacRoman-encoded value,
    ** so convert it to its UniCode representation.
    ** Though generally useful when dealing with MacRoman bytes, this method
    ** is specifically used in toLiteralName() to translate escaped byte-values to chars.
    **
    ** @return  The UniCode representation of the low 8-bits of value, interpreted as a MacRoman byte.
    */
    public static char
    fromMacRoman( int value )
    {  return ( charsMacRoman[ 0x0FF & value ] );  }




    /**
    ** Determine whether the current platform uses escape-sequences in filenames
    ** or not.  This method invokes MacPlatform.isMacOS() and assumes the returned
    ** boolean is identical to the escape-using characteristic.
    ** This means that all JVM's running under any version of Mac OS are
    ** presumed to use escaping, which may not actually be the case.
    ** In particular, I don't know what Mac OS X Server returns when
    ** running outside of the Blue Box.  Nor do I know what Mac OS X Consumer will return.
    **
    ** @return  True if this platform uses escape-sequences in java.io.File name-parts, false if not.
    **
    ** @see glguerin.util.MacPlatform#isMacOS
    */
    public static boolean
    platformUsesEscapes()
    {  return ( MacPlatform.isMacOS() );  }


    /**
    ** Determine whether the given String contains any characters that will need
    ** escaping or conversion in order to be used as a Mac file-name part.
    ** Returns true if if the given String contains characters which need to be
    ** escaped on the Mac, false if it contains no such characters.
    ** If the given String is null, then false is returned without throwing an exception.
    **<p>
    ** This method does not identify the Mac's native separator ":" as being an escapee.
    ** This is intentional.
    */
    public static boolean
    containsMacEscapees( String given )
    {
        if ( given == null  ||  given.length() == 0 )
            return ( false );

        if ( given.indexOf( ESCAPE ) >= 0 )
            return ( true );

        if ( given.indexOf( MRJ_SEP ) >= 0 )
            return ( true );

        return ( false );
    }

    /**
    ** If the given String, a file-name part, contains characters which must
    ** be escaped on the Mac, return the escaped form, otherwise return the original String.
    ** The given literalName <b>IS NOT</b> a full path-name, only a single part thereof.
    ** Separators appearing in literalName are treated as literal characters, not separators,
    ** and will be escaped.  This is the whole reason for having escaped names anyway.
    **<p>
    ** This method only performs escaping, not encoding from MacRoman or other schemes.
    ** This method won't translate ':' (NATIVE_SEP) to '-' (NATIVE_SUB), either.
    ** If you want either of these, you'll have to do them yourself.
    **<p>
    ** If the given String is null, then null is returned without throwing an exception.
    */
    public static String
    toEscapedName( String literalName )
    {
        if ( ! containsMacEscapees( literalName ) )
            return ( literalName );

        char[] chars = literalName.toCharArray();
        StringBuffer build = new StringBuffer( literalName.length() );

        for ( int i = 0;  i < chars.length;  ++i )
        {
            int it = chars[ i ];
            if ( it == ESCAPE )
                build.append( ESCAPE_SUB );
            else if ( it == MRJ_SEP )
                build.append( MRJ_SUB );
            else if ( it == NATIVE_SEP )
                build.append( NATIVE_SUB );
            else
                build.append( (char) it );
        }
        return ( build.toString() );
    }


    /**
    ** Return a String with Mac-escaping removed.
    ** If there are no escape-sequences (no '%' chars) at all,
    ** the original String is immediately returned.
    ** This method only performs unescaping, not encoding.
    ** If the given String is null, then null is returned without throwing an exception.
    **<p>
    ** Escapes in the range %80-%FF will be converted as if they represent MacRoman bytes.
    ** For example, %83 is converted to UniCode \u00C9 (capital-E acute), rather than to
    ** the direct UniCode representation of \u0083 (a control-char).
    ** The reason for this intepretation of escapes is that MRJ 2.0 and earlier represent
    ** file-name characters in the range 0x80-0xFF as escapes, not as UniCode chars.
    ** Since these escapes often represent a literal MacRoman byte, it would be
    ** improper to interpret them as UniCode-encoded escapes.
    **<p>
    ** If an apparent escape-sequence can't be decoded, it's kept as-is in the literal form.
    ** For example, "%fr" is not valid, because "fr" is not a hex value.
    ** The result is that "%fr" is copied verbatim to the result.
    ** If there aren't 2 chars after a'%', the sequence is also copied verbatim to the result.
    */
    public static String
    toLiteralName( String escapedName )
    {
        // If no escapes present, return original.
        if ( escapedName == null  ||  escapedName.indexOf( ESCAPE ) < 0 )
            return ( escapedName );

        char[] chars = escapedName.toCharArray();
        StringBuffer build = new StringBuffer( escapedName.length() );
        for ( int i = 0;  i < chars.length;  ++i )
        {
            int it = chars[ i ];
            if ( it == ESCAPE  &&  i + 2 < chars.length )
            {
                // Only two-hex-digit escapes are supported.
                // Always interpret the escaped 8-bit value as MacRoman.
                int hi = Character.digit( chars[ i + 1 ], 16 );
                int lo = Character.digit( chars[ i + 2 ], 16 );
                if ( hi >= 0  &&  lo >= 0 )
                {  it = fromMacRoman( (hi << 4) + lo );  i += 2;  }
            }
            // At this point, it is a char to append.
            // It's either the original literal char, or the decoded escape-sequence.
            // If it's been decoded, then i has also been moved along as needed.
            build.append( (char) it );
        }

        return ( build.toString() );
    }


    // ###  O S T Y P E S  ###

    /**
    ** Returns a UniCode String of 4 chars length, holding the expression
    ** of the macOSType as converted from MacRoman-bytes to UniCode.
    */
    public static String getOSTypeString( int macOSType )
    {
        byte[] bytes = new byte[ 4 ];
        bytes[ 3 ] = (byte) macOSType;
        bytes[ 2 ] = (byte) (macOSType >> 8);
        bytes[ 1 ] = (byte) (macOSType >> 16);
        bytes[ 0 ] = (byte) (macOSType >> 24);

        try {  return ( new String( bytes, "MacRoman" ) );  }
        catch ( UnsupportedEncodingException why ) {  /* FALL-THRU */ }

        return ( new String( bytes) );
    }
}
