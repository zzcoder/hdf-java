/*
 * For conditions of distribution and use, see the accompanying             *
 * hdf/COPYING file.                                                        *
*/

package glguerin.io;

import java.util.StringTokenizer;


// --- Revision History ---
// 10Mar99 GLG  created
// 11Mar99 GLG  rename some methods, refactor to give ensureCapacity()
// 11Mar99 GLG  add: constructor(int), asPath(), toString()
// 12Mar99 GLG  fix capacity-growing bug in append()
// 15Mar99 GLG  change some names
// 15Mar99 GLG  move setPath() and getPath() here
// 17Mar99 GLG  add basic escaping support, with non-altering implementations
// 17Mar99 GLG  refactor set()
// 27Mar99 GLG  change some names and edit doc-comments
// 31Mar99 GLG  make isAcceptablePart() also reject zero-length Strings
// 31Mar99 GLG  replace assemblePath() with buildPath()
// 01Apr99 GLG  add reverse()
// 01Apr99 GLG  add hash() and equals()


/**
** A Path is an ordered sequence of String parts, often representing a progressively
** more qualified name-sequence (such as a file-name), or an ordered series of elements
** (such as a search-path).
** The parts are randomly retrievable by index, 
** or retrievable as a String array,
** or can be assembled into a single String with embedded separators, 
** Parts are not randomly accessible for alteration.
** A Path's parts can only be altered altered at the tail end of the sequence, i.e. by appending
** or trimming the last part.  Eliminating random-access alteration, such as in java.util.Vector, 
** makes altering a Path substantially more efficient.
**<p>
** The order/sequence of the parts can be reversed by reverse().
** This is useful when you need to build a Path in reverse order from the eventual
** ordered sequence you want.  You can also use it to prepend a part, invoking
** the methods as: reverse(), append(newPart), reverse().
** I'm sure you can think of other uses.
**<p>
** The class Path will most likely used as a base-class for more specialized classes,
** such as one to specifically represent a file-name path (FilePathname).  One should also note
** that a Path sub-class can easily represent other ordered sequences, such as a class-path or
** a DNS-name.
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
** @see FilePathname
*/

public class Path
{
	/**
	** Split a String representing a path-like sequence into individual
	** parts where indicated by the given separator char.
	** Multiple adjacent occurances of the separator in the path are treated as
	** a single separator.  They do not yield multiple empty nor null parts.
	** Leading separators are treated the same as no leading separators,
	** so if your semantics require a distinction, you must determine it for
	** yourself, e.g. with String.startsWith().
	*/
	public static String[]
	split( String path, char sep )
	{
		char[] seps = { sep };
		StringTokenizer parser = new StringTokenizer( path, new String( seps ) );
		int count = parser.countTokens();
		String[] parts = new String[ count ];
		for ( int i = 0;  i < count; ++i )
		{  parts[ i ] = parser.nextToken();  }

		return ( parts );
	}


	/**
	** A zero-length array used to avoid having a null-reference internally.
	*/
	private static final String[] noParts = new String[ 0 ];

	/**
	** The internal array of parts, automatically resized as needed to grow.
	*/
	private String[] myParts;

	/**
	** The current part-count.
	*/
	private int myPartCount;

	/**
	** Create with no parts.
	*/
	public
	Path()
	{
		myPartCount = 0;
		myParts = noParts;
	}

	/**
	** Create with initial space for given part-count, but no actual parts.
	*/
	public
	Path( int partCount )
	{
		this();
		ensureCapacity( partCount );
	}

	/**
	** Create with initial parts.
	*/
	public
	Path( String[] parts )
	{
		this();
		set( parts );
	}



	/**
	** Return true if the given String is acceptable to be added as a part.
	** This implementation rejects null's and zero-length parts.
	** If you override in sub-classes, you must never accept null, as it makes the 
	** result of some methods ambiguous or prone to throwing NullPointerException's.
	*/
	protected boolean
	isAcceptablePart( String part )
	{  return ( part != null  &&  part.length() != 0 );  }


	/**
	** Transform the given String to an escaped form, whatever that may be.
	** Must accept null or zero-length Strings, and return them without error or exception.
	** Though principally for internal use, this method is public because the transformation
	** to and from escaped form is a useful feature of each instance.
	**<p>
	** This method must always perform a transformation, regardless of any internal
	** state indicating whether escapes are in use or not.
	** That is, this is strictly a transforming method, not a state-sensitive accessor method.
	**<p>
	** This implementation performs no transformation, always returning its argument.
	** Sub-classes may override to transform in ways they see fit.
	*/
	public String
	toEscapedForm( String toTransform )
	{  return ( toTransform );  }

	/**
	** Transform the given String to a literal (unescaped) form, whatever that may be.
	** Must accept null or zero-length Strings, and return them without error or exception.
	** Though principally for internal use, this method is public because the transformation
	** to and from escaped form is a useful feature of each instance.
	**<p>
	** This method must always perform a transformation, regardless of any internal
	** state indicating whether escapes are in use or not.
	** That is, this is strictly a transforming method, not a state-sensitive accessor method.
	**<p>
	** This implementation performs no transformation, always returning its argument.
	** Sub-classes may override to transform in ways they see fit.
	*/
	public String
	toLiteralForm( String toTransform )
	{  return ( toTransform );  }


	/**
	** Create a StringBuffer holding all the parts, placing the given char between the parts,
	** and also placing it initially if the flag is true.  The StringBuffer contains the escaped or 
	** unescaped (literal) parts according to the given useEscapes flag.
	** That flag is passed to getPart(int,boolean) to retrieve each part assembled.
	** When this instance has no parts, the returned String is zero-length when initially is false,
	** or the returned String contains a single between char when initially is true.
	** This method is a building-block commonly used to define overrides of getPath()
	** or more specialized but similar methods.
	*/
	public StringBuffer
	buildPath( char between, boolean initially, boolean useEscapes )
	{
		StringBuffer build = new StringBuffer();
		if ( initially )
			build.append( between );

		for ( int i = 0;  true;  ++i )
		{
			String part = getPart( i, useEscapes );
			if ( part == null )
				break;

			build.append( part );

			if ( part( i + 1 ) != null )
				build.append( between );
		}
		return ( build );
	}



	/**
	** Ensure the given capacity, growing the array as needed while preserving current contents.
	** This has no effect on part-count, only on available array capacity.
	*/
	public void
	ensureCapacity( int partCount )
	{
		if ( myParts.length < partCount )
		{
			String[] wasParts = myParts;
			myParts = new String[ partCount ];		// new array is created cleared
			for ( int i = 0, end = myPartCount;  i < end;  ++i )
			{
				myParts[ i ] = wasParts[ i ];
				wasParts[ i ] = null;		// remove old reference
			}
		}
	}


	/**
	** Return the current part-count.
	*/
	public int
	partCount()
	{  return ( myPartCount );  }


	/**
	** Clear all the parts, but don't reduce the current capacity.
	*/
	public void
	clear()
	{
		myPartCount = 0;
		if ( myParts != null )
		{
			for ( int i = 0;  i < myParts.length; ++i )
			{  myParts[ i ] = null;  }
		}
	}


	/**
	** Clear all the parts, then append the given Strings by calling appendAll( String[] ).
	** If toCopy is null, this is cleared and nothing is appended.
	**
	** @see #appendAll
	*/
	public void
	set( String[] toCopy )
	{
		clear();
		appendAll( toCopy );
	}


	/**
	** Split the given String into parts where indicated by the given separator char,
	** assigning them as the parts of this object.
	** If path is null, this object is just cleared.
	** Multiple adjacent occurances of the separator in the path are treated as
	** a single separator.  They do not yield multiple empty nor null components.
	** Leading separators are treated the same as no leading separators,
	** so if your semantics require a distinction, you must determine this for
	** yourself, e.g. with String.startsWith().
	*/
	public void
	setFrom( String path, char sep )
	{
		if ( path != null )
			set( split( path, sep ) );
		else
			clear();
	}



	/**
	** Reverse the order of the parts.
	** Does not reverse the individual part-Strings, only the order.
	** Does not invoke any other Path methods.
	*/
	public void
	reverse()
	{
		String[] parts = myParts;
		for ( int a = 0, z = myPartCount - 1;  a < z;  ++a, --z )
		{
			String temp = parts[ a ];
			parts[ a ] = parts[ z ];
			parts[ z ] = temp;
		}
	}



	/**
	** Append the given part String, growing the internal storage as needed.  
	** If toAppend is unacceptable, it's quietly ignored.
	**<p>
	** Acceptance is determined by the protected method isAcceptablePart().
	**
	** @see #isAcceptablePart
	*/
	public void
	append( String toAppend )
	{
		if ( isAcceptablePart( toAppend ) )
		{
			if ( myPartCount == myParts.length )
				ensureCapacity( myPartCount + myPartCount + 1 );

			myParts[ myPartCount++ ] = toAppend;
		}
	}

	/**
	** Append all the items in toAppend using the append(String) method.
	** If toAppend is null or zero-length, nothing is appended.
	**
	** @see #append
	*/
	public void
	appendAll( String[] toAppend )
	{
		if ( toAppend != null  &&  toAppend.length > 0   )
		{
			// To avoid multiple enlargements, ensure capacity once initially.
			ensureCapacity( partCount() + toAppend.length );

			for ( int i = 0;  i < toAppend.length;  ++i )
			{  append( toAppend[ i ] );  }
		}
	}


	/**
	** Trim the most recently added part, and return it.
	** Returns null if there are no parts left.
	*/
	public String
	trim()
	{
		String trimmed = null;
		if ( myPartCount > 0 )
		{
			// Ensure internal array retains no reference to trimmed part.
			trimmed = myParts[ --myPartCount ];
			myParts[ myPartCount ] = null;
		}
		return ( trimmed );
	}

	/**
	** Swap the given String with the last part, using trim() and append().  
	** If toSwap is null, the result is equivalent to trim(), since you can't have null parts.
	** If there are no parts to begin with, the result is equivalent to append(),
	** and null is returned.
	*/
	public String
	swap( String toSwap )
	{
		String old = trim();
		append( toSwap );
		return ( old );
	}

	/**
	** Return the last part, or null if no parts.
	** This is operationally equivalent to 
	** <b>part( partCount() - 1 )</b> but is more concise and efficient.
	** As with part(int), escaping is not applied.
	** This implementation calls no other methods.
	*/
	public String
	last()
	{
		if ( myPartCount > 0 )
			return ( myParts[ myPartCount - 1 ] );
		else
			return ( null );
	}

	/**
	** Return the indexed part without escaping it, or null if out of range.
	*/
	public String
	part( int index )
	{
		if ( index >= 0  &&  index < myPartCount )
			return ( myParts[ index ] );
		else
			return ( null );
	}

	/**
	** Return the indexed part with the given escaping applied, or null if out of range.
	** Invokes the methods part(int) and toEscapedForm(String).
	*/
	public String
	getPart( int index, boolean useEscapes )
	{
		String part = part( index );
		if ( useEscapes )
			part = toEscapedForm( part );
		return ( part );
	}

	/**
	** Return a new array holding all the parts, with escapes applied according
	** to the given flag.  
	** Invokes the methods part(int) and toEscapedForm(String)
	** to fill the array, but not getPart(int).
	** If there are no parts, the returned array is zero-length, but never null.
	*/
	public String[]
	getParts( boolean useEscapes )
	{
		int len = partCount();
		if ( len == 0 )
			return ( noParts );

		String[] result = new String[ len ];
		if ( useEscapes )
		{
			for ( int i = 0;  i < len;  ++i )
			{  result[ i ] = toEscapedForm( part( i ) );  }
		}
		else
		{
			// Having a second actual loop should be faster than deciding about escaping
			// at each iteration.  Or maybe it makes no practical difference.
			for ( int i = 0;  i < len;  ++i )
			{  result[ i ] = part( i );  }
		}

		return ( result );
	}


	/**
	** Return a String consisting of the Path parts separated by an internally determined
	** separator.
	** This implementation uses a comma separator, without a leading separator, and without escapes.
	** Sub-classes should override this method and setPath() to act as desired.
	**
	** @see #setPath
	*/
	public String
	getPath()
	{  return ( buildPath( ',', false, false ).toString() );  }


	/**
	** Set the parts from the given String, separated by some internally determined
	** separator.
	** This implementation uses a comma separator, just like getPath() does.
	** Sub-classes should override this method and getPath() to act as desired.
	**
	** @see #getPath
	*/
	public void
	setPath( String path )
	{
		if ( path != null )
			setFrom( path, ',' );
		else
			clear();
	}


	/**
	** Override Object.toString(), returning the getPath() value.
	*/
	public String
	toString()
	{  return ( getPath() );  }


	/**
	** Override Object.hashCode(), returning the partCount().
	** I'm sure one could devise a hash-code that gave fewer collisions,
	** but that's probably more appropriate for sub-classes.
	** Just ensure that two Path's for which equals(Object) yields true
	** return identical hash-codes.
	*/
	public int
	hashCode()
	{  return ( partCount() );  }

	/**
	** Override Object.equals(Object), returning true if every unescaped
	** part in this instance String.equals() every unescaped part in other.
	** Sub-classes can override, e.g. performing String.equalsIgnoreCase() instead
	** of String.equals().
	*/
	public boolean
	equals( Object other )
	{
		// Doesn't need a null-check of other, because null is never
		// an instance of any type.
		if ( other instanceof Path )
		{
			Path that = (Path) other;
			int count = partCount();
			if ( count == that.partCount() )
			{
				// We don't need to check for null-part in loop, since we're
				// certain to never go out of range.
				for ( int i = 0;  i < count;  ++i )
				{
					if ( ! part( i ).equals( that.part( i ) ) )
						return ( false );
				}
				return ( true );
			}
		}
		return ( false );
	}


}

