package pu.porna.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JLabel;
public class StringHelper implements Serializable
{
	// Effe lache: Label heeft andere getallen voor LEFT, CENTER en RIGHT dan JLabel :-(
	public static final int LEFT   = JLabel.LEFT;
	public static final int CENTER = JLabel.CENTER;
	public static final int RIGHT  = JLabel.RIGHT;
	
	public static final String STRING_ENKELE_QUOTE  = "'";
	
	private static class Match
	{
		int startPos = -1;
		int length = -1;
	}
/**
 * StringHelper constructor comment.
 */
private StringHelper()
{
	super();
}

// Deze methode bestaat omdat het een stuk gemakkelijker testen is. Maar normaliter
// roep je align( String, ViewColumn ) aan.
public static String align( String aValue, int aMaxLen, int aAlignment )
{
	if ( aMaxLen <= 0 )
	{
		return "";
	}
		
	final int valueLen  = aValue.length();
	final int diff      = aMaxLen - valueLen;
	
	if ( diff <= 0 )
	{
		if ( aAlignment == RIGHT )
		{
			return aValue.substring( -diff, valueLen );
		}
		else
		{
			return aValue.substring( 0, aMaxLen );
		}
	}

	switch( aAlignment )
	{
		case LEFT:
			return aValue + StringHelper.repChar( ' ', diff );
		case RIGHT:
			return StringHelper.repChar( ' ', diff ) + aValue;
		case CENTER:
			return StringHelper.repChar( ' ', diff / 2 ) + aValue + StringHelper.repChar( ' ', diff - diff / 2 );
		default:
			Assert.assertTrue( false, "Invalid alignment: " + aAlignment );
			return null;
	}
}
/**
 * Retourneert of een String een bepaald teken bevat.
 * Creation date: (7-1-2001 23:17:00)
 * @param s java.lang.String
 * @param c char
 */
public static boolean bevat( String s, char c )
{
	char [] chars = new char[s.length()];
	s.getChars( 0, chars.length, chars, 0 );
	for ( int x = 0; x < chars.length; x++ )
	{
		if ( chars[x] == c )
		{
			return true;
		}
	}
	return false;
}
/**
 * Maakt een String van een bepaalde lengte; als ze kleiner is dan de gewenste lengte wordt ze uitgevuld met het opgegeven teken,
 * als ze langer is dan wordt ze afgekapt.
 * @return java.lang.String
 * @param s De String om uit te vullen
 * @param uitvulChar Het teken dat gebruikt wordt om de String uit te vullen
 * @param lengte De gewenste lengte
 */
public static String brengOpLengte( String s, char uitvulChar, int lengte)
{
	if ( s.length() > lengte )
	{
		return s.substring( 0, lengte );
	}
	else
	{
		return vulUit( s, uitvulChar, lengte );
	}
}
/**
 * Maakt een String van een bepaalde lengte; als ze kleiner is dan de gewenste lengte wordt ze uitgevuld met spaties,
 * als ze langer is dan wordt ze afgekapt.
 * @return java.lang.String
 * @param s De String om uit te vullen
 * @param uitvulChar Het teken dat gebruikt wordt om de String uit te vullen
 * @param lengte De gewenste lengte
 */
public static String brengOpLengteMetSpaties( String s, int lengte)
{
	if ( s.length() > lengte )
	{
		return s.substring( 0, lengte );
	}
	else
	{
		return vulUit( s, ' ', lengte );
	}
}

/**
 * Returns the path to a class (its package) with path separators instead of dots.
 * Note: this method always uses the forward slahs as the path separator
 * @param claxx
 * @return
 */
public static String classToPath( Class<?> claxx )
{
	String path = claxx.getName();
	path = path.substring( 0, path.lastIndexOf( '.' ) );
	path = StringHelper.replaceAll( path, ".", "/" );
	return path;
}

public static List<String> classPathAsList()
{
	String classPath = System.getProperty( "java.class.path" );
	List<String> classPathList = StringHelper.explode2ToList( classPath, System.getProperty( "path.separator" ) );
	Collections.sort( classPathList );
	return classPathList;
}
public static void printClassPath()
{
	List<String> classPathList = classPathAsList();
	for ( String classPathEntry : classPathList )
	{
		System.out.println( classPathEntry );
	}
}

/**
 * @param s The String to be investigated
 * @return Whether a String contains a digit
 */
public static boolean containsDigit ( String s )
{
	if ( s == null )
	{
		return false;
	}
	for ( int x = 0; x < s.length(); x++ )
	{
		if ( Character.isDigit( s.charAt( x ) ) )
		{
			return true;
		}
	}
	return false;
}

/**
 * @param s The String to be investigated
 * @return  Whether the string contains both lowercase and uppercase characeters.
 */
public static boolean containsMixedCase( String s )
{
	if ( s == null )
	{
		return false;
	}
	boolean hasLower = false;
	boolean hasUpper = false;
	for ( int x = 0; x < s.length(); x++ )
	{
		char c = s.charAt( x );
		if ( java.lang.Character.isLowerCase( c ) )
		{
			hasLower = true;
		}
		if ( java.lang.Character.isUpperCase( c ) )
		{
			hasUpper = true;
		}
	}
	return hasLower && hasUpper;
}


/**
 * Verdeelt een String in regels van maximaal <code>regelLengte</code> lang, netjes
 * afbrekend op wit en Enters. Dat wil zeggen dat iedere Enter sowieso een nieuwe regel begint,
 * en dat te lange regels op "whitespace" tekens afbreken (zie Character.isWhiteSpace()).
 * <p>
 * Nog een paar bijzonderheden:
 * <ul>
 * <li> Vooralsnog laten we de whitespace op het afbreekpunt gewoon staan, dus de string "123 456 789" met
 *      een regellengte van 10 wordt afgebroken in "123 456 ", "789". Je kunt het er desgewenst gewoon
 *      aftrimmen als je het weg wilt hebben.
 * <li> Enters worden WEL uit de string verwijderd nadat we er op afgebroken hebben.
 * <li> Alle whitespace aan het einde van de string wordt WEL verwijderd.
 *      HIGH Moet je hier niet alleen spaties weghalen en enters laten staan?
 * </ul>
 * @param s De te converteren tekst
 * @param aRegelLengte De maximale lengte van een regel.
 * @return Een List met de regels
 */
public static List<String> converteerTekst( String s, int aRegelLengte )
{
	String ss = trimTrailing( s );
	
	// We exploderen de tekst eerst op de enters. Daarna verdelen we de brokken verder in regels met
	// de maximale lengte. Je kunt niet explode() gebruiken want die beschouwt twee opeenvolgende enters
	// als EEn enter. Wat wij doen is alle mogelijke combinatie van entertekens vervangen door een speciale string
	// en dan kunnen we explode2 gebruiken.
	final String ENTER = "!@#";
	ss = replaceAll( ss, "\r\n", ENTER );
	ss = replaceAll( ss, "\n\r", ENTER );
	ss = replaceAll( ss, "\r"  , ENTER );
	ss = replaceAll( ss, "\n"  , ENTER );

	List<String> res = new ArrayList<>();
	for ( String regel : explode2ToList( ss, ENTER ) )
	{
		res.addAll( verdeelInRegelsMetVasteKantlijn( regel, aRegelLengte ) ); 
	}
	return res;
}
/**
 * Returns whether two strings are equal after being trimmed.
 * If one of the strings is <code>null</code> this method returns true
 * only if the other one is also <code>null</code> 
 * @param a One String
 * @param b Another String
 * @return whether two strings are equal after being trimmed.
 */
public static boolean equals( String a, String b )
{
	return a == null || b == null ? a == b : a.trim().equals( b.trim() );
}
/**
 * Returns whether two strings are equal after being trimmed.
 * If either string is <code>null</code> it is treated as the empty string
 * @param a One String
 * @param b Another String
 * @return whether two strings are equal after being trimmed.
 */
public static boolean equalsIgnoreNull( String a, String b )
{
	if ( a == null )
	{
		a = "";
	}
	if ( b == null )
	{
		b = "";
	}
	return a.trim().equals( b.trim() );
}

/**
 * Explodes a String into parts. The parts are separated bij delimiters.
 * @@NOG explode() considers a sequence of delimiters as one delimiter. This
 * is nice if tokens are separated by whitespace, but not what we want in explode().
 * For instance, explode( "aa;;bb", ";" ) should return an array of three strings: aa,
 * an empty string, and bb. SEE explode2.
 * @return java.lang.String [] The exploded parts
 * @param s The String to be exploded
 * @param delim The set of delimiters
 * @exception IllegalArgumentException if the string or the delimiter is <code>null</code>
 */
public static String[] explode( String s, String delim )
{
	List<String> list = explodeToList( s, delim );
	String [] sa = new String[list.size()];
	return list.toArray( sa );
}
/**
 * Explodes a String into parts. The parts are separated by a separator string.
 * <p> 
 * This method works rather differently than explode. It uses one string to separate
 * the parts. If two separator strings occur consecutively, it assumes an empty part
 * between them.
 * @return java.lang.String [] The exploded parts
 * @param s The string to be exploded
 * @param aSeparator The separator string
 * @exception IllegalArgumentException if the string or the delimiter is <code>null</code>
 */
public static String[] explode2( String s, String aSeparator )
{
	List<String> list = explode2ToList( s, aSeparator );
	String [] sa = new String[list.size()];
	return list.toArray( sa );
}
/**
 * Explodes a String into parts. The parts are separated by a separator string.
 * <p> 
 * This method works rather differently than explode. It uses one string to separate
 * the parts. If two separator strings occur consecutively, it assumes an empty part
 * between them.
 * @return a List with the exploded parts
 * @param s The string to be exploded
 * @param aSeparator The separator string
 * @exception IllegalArgumentException if the string or the delimiter is <code>null</code>
 */
public static List<String> explode2ToList( String s, String aSeparator )
{
	if ( s == null || aSeparator == null )
	{
		throw new IllegalArgumentException( "Neither string nor separator may be null" );
	}
		
	List<String> list = new ArrayList<>();
	int pos = 0;
	int sepPos = s.indexOf( aSeparator, pos );
	while ( sepPos >= 0 )
	{
		list.add( s.substring( pos, sepPos ) );
		pos = sepPos + aSeparator.length();
		sepPos = s.indexOf( aSeparator, pos );
	}
	list.add( s.substring( pos ) );
	return list;
}
/**
 * Explodes a String into parts. The parts are separated bij delimiters.
 * @@NOG explode() considers a sequence of delimiters as one delimiter. This
 * is nice if tokens are separated by whitespace, but not what we want in explode().
 * For instance, explode( "aa;;bb", ";" ) should return an array of three strings: aa,
 * an empty string, and bb. SEE explode2.
 * @return java.lang.String [] The exploded parts
 * @param s The String to be exploded
 * @param delim The set of delimiters
 * @exception IllegalArgumentException if the string or the delimiter is <code>null</code>
 */
public static List<String> explodeToList( String s, String delim )
{
	if ( s == null || delim == null )
	{
		throw new IllegalArgumentException( "String and delimiter may not be null" );
	}
		
	StringTokenizer tok = new StringTokenizer( s, delim );
	List<String> list = new ArrayList<>( tok.countTokens() );
	while ( tok.hasMoreTokens() )
	{
		list.add( tok.nextToken() );
	}
	return list;
}

public static String getShortClassName( Class<?> c )
{
	String s = c.getName();
	int pos = s.lastIndexOf( '.' );
	return s.substring( pos + 1, s.length() );
}
public static String getShortClassName( Object o )
{
	if ( o instanceof Class )
	{
		return getShortClassName( (Class<?>) o );
	}
	return getShortClassName( o.getClass() );
}
/**
 * Implodes String parts into one string, using a separator String between the parts.
 * Here is an example program.
 * <pre>
 *	Class ImplodeExample
 *	{
 *		public static void main( int [] args )
 *		{
 *			String s = new String [] { "Aap", "Noot", "Mies" };
 *			System.out.println( StringHelper.implode( s, "|" ) );
 *		}
 *	}
 * </pre>
 * The output of this program will be
 * <pre>	Aap|Noot|Mies</pre>
 * As you can see in this example, the separator string is only inserted between the elements
 * of the string array, not at the front or at the end.<p>
 * If one of the elements of the string array is <code>null</code> or the empty string, nothing
 * is inserted into the output string. This will have the effect of two consecutive delimiter
 * strings appearing in the output, as in
 * <pre>
 *	Class ImplodeExample2
 *	{
 *		public static void main( int [] args )
 *		{
 *			String s = new String [] { "Aap", null, "Mies" };
 *			System.out.println( StringHelper.implode( s, "|" ) );
 *		}
 *	}
 * </pre>
 * The output of this program will be
 * <pre>	Aap||Mies</pre>
 * In the most extreme case where all the input elements are <code>null</code> or empty,
 * only separators will be output, as in
 * <pre>
 *	Class ImplodeExample3
 *	{
 *		public static void main( int [] args )
 *		{
 *			String s = new String [] { "", null, null };
 *			System.out.println( StringHelper.implode( s, "|" ) );
 *		}
 *	}
 * </pre>
 * The output of this program will be
 * <pre>	||</pre>
 * If the array of input strings is <code>null</code>, an empty string will be
 * returned. However, if the separator is <code>null</code>, an <code>IllegalArgumentException</code> is thrown.
 * @return The constructed string
 * @param s An array of strings to be concatenated with the separator inbetween
 * @param separator The string to be inserted between the elements
 */
public static String implode( String [] aString, String aSeparator )
{
	if ( aSeparator == null )
	{
		throw new IllegalArgumentException( "Separator may not be null" );
	}
	if ( aString == null )
	{
		return "";
	}
	StringBuffer sb = new StringBuffer();
	for ( int x = 0; x < aString.length; x++ )
	{
		if ( x > 0 )
		{
			sb.append( aSeparator );
		}
		sb.append( aString[x] == null ? "" : aString[x] );
	}
	return sb.toString();
}
/**
 * Implodes String parts into one string, using a separator String between the parts.
 * Here is an example program.
 * <pre>
 *	Class ImplodeExample
 *	{
 *		public static void main( int [] args )
 *		{
 *			String s = new String [] { "Aap", "Noot", "Mies" };
 *			System.out.println( StringHelper.implode( Arrays.asList( s ), "|" ) );
 *		}
 *	}
 * </pre>
 * The output of this program will be
 * <pre>	Aap|Noot|Mies</pre>
 * As you can see in this example, the separator string is only inserted between the elements
 * of the string collection, not at the front or at the end.<p>
 * If one of the elements of the string collection is <code>null</code> or the empty string, nothing
 * is inserted into the output string. This will have the effect of two consecutive delimiter
 * strings appearing in the output, as in
 * <pre>
 *	Class ImplodeExample2
 *	{
 *		public static void main( int [] args )
 *		{
 *			String s = new String [] { "Aap", null, "Mies" };
 *			System.out.println( StringHelper.implode( Arrays.asList( s ), "|" ) );
 *		}
 *	}
 * </pre>
 * The output of this program will be
 * <pre>	Aap||Mies</pre>
 * In the most extreme case where all the input elements are <code>null</code> or empty,
 * only separators will be output, as in
 * <pre>
 *	Class ImplodeExample3
 *	{
 *		public static void main( int [] args )
 *		{
 *			String s = new String [] { "", null, null };
 *			System.out.println( StringHelper.implode( Arrays.asList( s ), "|" ) );
 *		}
 *	}
 * </pre>
 * The output of this program will be
 * <pre>	||</pre>
 * If the collection of input strings is <code>null</code>, an empty string will be
 * returned. However, if the separator is <code>null</code>, an <code>IllegalArgumentException</code> is thrown.
 * @return The constructed string
 * @param aCollection A collection of strings to be concatenated with the separator inbetween
 * @param separator The string to be inserted between the elements
 */
public static String implode( Collection<?> aCollection, String aSeparator )
{
	if ( aCollection == null )
	{
		return implode( new String [] {}, aSeparator );
	}
	String [] s = new String [aCollection.size()];
	int index = 0;
	for ( Object element : aCollection )
	{
		s[index++] = element == null ? null : String.valueOf( element );
	}
	return implode( s, aSeparator );
}

/*
 * Capitalizes the first character of each String in the array. If one of the
 * Strings is <code>null</code> it throws a NullPointerException.
 * @param s The array with the strings to be capitalized.
 * @return The array with the capitalized strings
 */
public static String [] initCap( String[] s )
{
	String [] newS = new String[s.length];
	for (int x = 0; x < newS.length; x++)
	{
		newS[x] = initCap( s[x] );
	}
	return newS;
}
/*
 * Capitalizes the first character of a string. If the
 * string is <code>null</code> it throws a NullPointerException.
 * @param s The string to be capitalized.
 * @return The capitalized string
 */
public static String initCap( String s )
{
	return Character.toUpperCase( s.charAt( 0 ) ) + s.substring( 1 );
}
/*
 * Lowercases the first character of each String in the array. If one of the
 * Strings is <code>null</code> it throws a NullPointerException.
 * @param s The array with the strings to be lowecased.
 * @return The array with the lowercased strings
 */
public static String [] initLow( String[] s )
{
	String [] newS = new String[s.length];
	for (int x = 0; x < newS.length; x++)
	{
		newS[x] = initLow( s[x] );
	}
	return newS;
}
/*
 * Lowercases the first character of a string. If the
 * string is <code>null</code> it throws a NullPointerException.
 * @param s The string to be lowercased.
 * @return The lowercased string
 */
public static String initLow( String s )
{
	return Character.toLowerCase( s.charAt( 0 ) ) + s.substring( 1 );
}
/**
 * Inserts one space between the characters of a string.
 */
public static String interSpace(String s)
{
	final StringBuffer sb = new StringBuffer();
	final int len = s.length();
	for ( int x = 0; x < len; x++ )
	{
		sb.append( s.charAt( x ) ).append( ' ' );
	}
	return sb.toString();
}
/**
 * Returns whether a String is empty or <code>null</code>
 * @param s
 * @return
 */
public static boolean isEmpty( String s )
{
	return s == null || s.trim().length() == 0;
}

public static String left( String s, int len )
{
	final int sLen = s.length();
	final int trueLen = len > sLen ? sLen : len; 
	return s.substring( 0, trueLen );
}
public static String mid( String s, int start, int len )
{
	final int sLen = s.length();
	if ( start > sLen )
	{
		return "";
	}
	final int trueLen = len > sLen - start ? sLen - start : len; 
	return s.substring( start, start + trueLen );
}
/**
 * Maakt een String bestaande uit <code>length</code> herhalingen van het teken <code>c</code>.
 * @return java.lang.String
 * @param c char
 * @param length int
 */
public static String repChar( char c, int lengte )
{
	if ( lengte <= 0 )
	{
		return "";
	}
	char [] rep = new char [lengte];
	for ( int x = 0; x < lengte; x++ )
	{
		rep[x] = c;
	}
	return new String( rep );
}
public static String replaceAll( String str, String oldString, String newString )
{
	StringBuffer sb = new StringBuffer();
	int index = str.indexOf( oldString, 0 );
	if ( index < 0 )
	{
		return str;
	}
	while (index >= 0)
	{
		sb.append( str.substring( 0, index ) );
		sb.append( newString );
		str = str.substring( index + oldString.length() );
		index = str.indexOf( oldString, 0 );
	}
	sb.append( str.substring( 0, str.length() ) );
	return sb.toString();
}
/**
 * Vervangt het eerste voorkomen van oldString in s door newString
 * @return java.lang.String
 * @param s java.lang.String
 * @param oldString java.lang.String
 * @param newString java.lang.String
 */
public static String replaceFirst( String s, String oldString, String newString ) 
{
	StringBuffer sb = new StringBuffer();
	int index = s.indexOf( oldString, 0 );
	if ( index < 0 )
	{
		return s;
	}
	if ( index > 0 )
	{
		sb.append( s.substring( 0, index ) );
	}
	sb.append( newString );
	sb.append( s.substring( index + oldString.length() ) );
	return sb.toString();
}
/**
 * Replaces the XML characters &amp;, &lt; and &gt; by their standard representation
 * &amp;amp, &amp;lt; and &amp;gt; respectively.
 * To replace the truly forbidden XML-chars see mc.base.java15; we use a regex so it must be at least java 1.4
 * @param s
 * @return
 */
public static String quoteXmlChars( String s )
{
	if ( s == null )
	{
		return null;
	}
	// Volgorde is essentieel anders wordt de & weer vervangen door &amp;
    String retS = s;
    retS = replaceAll( retS, "&", "&amp;" );
    retS = replaceAll( retS, "<", "&lt;" );
    retS = replaceAll( retS, ">", "&gt;" );
    return retS;
}

/**
 * Replaces the XML characters &amp;, &lt; and &gt; by their standard representation
 * &amp;amp, &amp;lt; and &amp;gt; respectively, for a whole array of strings.
 * To replace the truly forbidden XML-chars see mc.base.java15; we use a regex so it must be at least java 1.4
 * @param s
 * @return
 */
public static String[] quoteXmlChars( String [] s )
{
	if ( s == null )
	{
		return null;
	}
    String [] retS = s;
    for ( int x = 0; x < retS.length; x++ )
    {
        retS[x] = quoteXmlChars( retS[x] );
    }
    return retS;
}

/**
 * Maakt een String bestaande uit <code>length</code> herhalingen van de String <code>s</code>.
 * @return java.lang.String
 * @param s De te herhalen String
 * @param length int
 */
public static String repString( String s, int lengte )
{
	if ( lengte <= 0 )
	{
		return "";
	}
	StringBuffer sb = new StringBuffer();
	for ( int x = 0; x < lengte; x++ )
	{
		sb.append( s );
	}
	return sb.toString();
}
public static String right( String s, int len )
{
	final int sLen = s.length();
	final int trueLen = len > sLen ? sLen : len; 
	return s.substring( sLen - trueLen, sLen );
}
/**
 * Shows the differences between two strings. The algorithm is still pretty dumb;
 * when it sees a mismatch, it does the following:
 * <ul>
 * <li>It looks ahead in the right string for the character in the left string;
 * <li>Then it counts how many characters from the left string match the characters in the right string from that point
 * <li>Then it does the same with left and right reversed.
 * <li>The longest match wins. The characters from the string with the shorted match are considered missing.
 * </ul>
 * I don't believe this is a good algorithm, but it is good enough for now.
 * <p>
 * The output format is a series of lines separated by the default
 * line-separator. Each line shows the position, the character of the
 * left string, its numeric value, whether it is considered missing, and the the same 4 fields for the right string
 * @param The left string
 * @param The right string
 * @return The list of differences
 */
public static String showDiff( String lString, String rString )
{
	StringBuffer sb = new StringBuffer();
	
	if ( lString.length() != rString.length() )
	{
		sb.append( "Lengths differ: left=" + lString.length() + " right=" + rString.length() );
		sb.append( System.getProperty( "line.separator" ) );
	}

	int lPos = 0;
	int rPos = 0;
	int lLen = lString.length();
	int rLen = lString.length();
	while ( lPos < lLen && rPos < rLen )
	{
		if ( lString.charAt( lPos ) == rString.charAt( rPos ) )
		{
			lPos++;
			rPos++;
		}
		else
		{
			// lookAhead looks ahead for a string in the first string that matches
			// the second string as best as possible.
			Match lMatch = showDiff_LookAhead( lString, lPos, rString, rPos );
			Match rMatch = showDiff_LookAhead( rString, rPos, lString, lPos );
			sb.append( System.getProperty( "line.separator" ) );

			// Show previous line
			if ( lPos > 0 && rPos > 0 )
			{
				sb.append( showDiff_ReportLine( lString, lPos-1, false, rString, rPos-1, false ) );
			}
			
			// Show differences	
			if ( lMatch.startPos == -1 && rMatch.startPos == -1 )
			{
				// No match. Report current line and go to next position in both strings
				sb.append( showDiff_ReportLine( lString, lPos, true, rString, rPos, true ) );
				lPos++;
				rPos++;
			}
			else if ( lMatch.length >= rMatch.length )
			{
				while ( lPos < lMatch.startPos )
				{
					sb.append( showDiff_ReportLine( lString, lPos, false, rString, rPos, true ) );
					lPos++;
				}
			}
			else
			{
				while ( rPos < rMatch.startPos )
				{
					sb.append( showDiff_ReportLine( lString, lPos, true, rString, rPos, false ) );
					rPos++;
				}
			}
			// Show following line
			if ( lPos < lLen && rPos < rLen )
			{
				sb.append( showDiff_ReportLine( lString, lPos, false, rString, rPos, false ) );
			}
			if ( lPos+1 < lLen && rPos+1 < rLen )
			{
				sb.append( showDiff_ReportLine( lString, lPos+1, false, rString, rPos+1, false ) );
			}
		}
	}
	return sb.toString();
}
private static Match showDiff_LookAhead( String lString, int lPos, String rString, int rPos )
{
	final int MAX_LOOKAHEAD = 1000;
	final int lLength = lString.length();
	final int rLength = rString.length();
	final int maxLook = Math.min( lPos + MAX_LOOKAHEAD, lLength );
	int lLook = lPos + 1;
	int rLook = rPos;
	final char rChar = rString.charAt( rLook );

	for ( ;lLook < maxLook; lLook++ )
	{
		if ( lString.charAt( lLook ) == rChar )
		{
			break;
		}
	}

	Match match = new Match();
	if ( lLook < maxLook )
	{	
		match.startPos = lLook;
		match.length = 0;
		do
		{
			lLook++;
			rLook++;
			match.length++;
		}
		while ( lLook < lLength && rLook < rLength && lString.charAt( lLook ) == rString.charAt( rLook ) );
	}
	return match;
}
private static String showDiff_ReportLine( String lString, int lPos, boolean lMiss, String rString, int rPos, boolean rMiss )
{
	StringBuffer sb = new StringBuffer();
	sb.append( lPos );
	sb.append( "\t" );
	if ( ! Character.isWhitespace( lString.charAt( lPos ) ) )
	{
		sb.append( lString.charAt( lPos ) );
	}
	sb.append( "\t(" );
	sb.append( (int) lString.charAt( lPos ) );
	sb.append( ")\t" );
	if ( lMiss )
	{
		sb.append( "mis" );
	}
	sb.append( "\t" );
	sb.append( rPos );
	sb.append( "\t" );
	if ( ! Character.isWhitespace( rString.charAt( rPos ) ) )
	{
		sb.append( rString.charAt( rPos ) );
	}
	sb.append( "\t(" );
	sb.append( (int) rString.charAt( rPos ) );
	sb.append( ")\t" );
	if ( rMiss )
	{
		sb.append( "mis" );
	}
	sb.append( System.getProperty( "line.separator" ) );
	return sb.toString();
}
/**
 * Retourneert een serie spaties van de gewenste lengte
 * @return java.lang.String
 * @param lengte De gewenste lengte
 */
public static String spaties( int lengte )
{
	return repChar ( ' ', lengte );
}
/**
 * Verdeelt een String in regels van een bepaalde <code>regelLengte</code> lang.
 * @return java.lang.Vector
 * @param s java.lang.String
 * @param regelLengte int
 */
public static List<String> splitsVasteLengte( String s, int regelLengte )
{
	int beginPos = 0;
	int endPos = regelLengte;
	List<String> schermRegels = new ArrayList<>();
	while ( endPos < s.length() )
	{
		// create an string object of the specified length and add it to the vector
		schermRegels.add( s.substring( beginPos, endPos ) );
//		System.out.println("Text van " + beginPos + " - " + endPos + ": " + s.substring( beginPos, endPos ) ); 
		
		//  Zet klaar voor de volgende ronde
		beginPos = endPos;
		if (endPos + regelLengte > s.length() )
		{
			endPos = s.length();
		}
		else
		{
			endPos = endPos + regelLengte;
		}
	}

	schermRegels.add( s.substring( beginPos ) );
	//System.out.println("Text van " + beginPos + " - " + s.length() + ": " + s.substring( beginPos ) ); 

	return schermRegels;
}
public static String [] splitOnce( String s, String aSeparator )
{
	// HIGH Is splitten met een empty separator fout of niet?
	if ( aSeparator == null )
	{
		throw new RuntimeException( "Cannot split with a null separator" );
	}
	if ( s == null )
	{
		return new String [0];
	}
	int pos = s.indexOf( aSeparator );
	if ( pos == -1 )
	{
		return new String[] { s };
	}
	return new String [] { s.substring( 0, pos ), s.substring( pos + aSeparator.length() ) };
}
public static String [] split( String s, String [] aSeparators )
{
	if ( aSeparators == null )
	{
		throw new RuntimeException( "Cannot split with null separators" );
	}
	if ( s == null )
	{
		return new String [0];
	}

	List<String> res = new ArrayList<>();
	String rest = s;
	for ( String aSeparator : aSeparators )
	{
		String [] split = splitOnce( rest, aSeparator );
		res.add( split[0] );
		rest=split[1];
	}
	res.add( rest );
	return res.toArray( new String[res.size()] );
}

/**
 * Retourneert een streep van 80 lang
 * @return java.lang.String
 */
public static String streep()
{
	return repChar ( '-', 80 );
}
/**
 * Retourneert een streep van de gewenste lengte
 * @return java.lang.String
 * @param lengte De gewenste lengte
 */
public static String streep( int lengte )
{
	return repChar ( '-', lengte );
}
public static String streep2()
{
	return repChar ( '=', 80 );
}
public static String streep2(int lengte)
{
	return repChar ( '=', lengte );
}
/**
 * Lowercases an array of strings. If one of the
 * Strings is <code>null</code> it throws a NullPointerException.
 * @param s The array with the strings to be lowercased
 * @return The array with the lowercased strings
 */
public static String [] toLowerCase( String[] s )
{
	String [] newS = new String[s.length];
	for (int x = 0; x < newS.length; x++)
	{
		newS[x] = s[x].toLowerCase();
	}
	return newS;
}
/**
 * Uppercases an array of strings. If one of the
 * Strings is <code>null</code> it throws a NullPointerException.
 * @param s The array with the strings to be uppercased
 * @return The array with the uppercased strings
 */
public static String [] toUpperCase( String[] s )
{
	String [] newS = new String[s.length];
	for (int x = 0; x < newS.length; x++)
	{
		newS[x] = s[x].toUpperCase();
	}
	return newS;
}
/**
 * Removes white space from beginning this string.
 * <p>
 * All characters that have codes less than or equal to
 * <code>'&#92;u0020'</code> (the space character) are considered to be
 * white space.
 *
 * @return  this string, with leading white space removed
 */
public static String trimLeading(String s)
{
	if ( s == null )
	{
		return null;
	}
	int len = s.length();
	int st = 0;
	while ( ( st < len ) && ( s.charAt( st ) <= ' ' && s.charAt( len - 1 ) != '\u001A') )
	{
		st++;
	}
	return( st > 0 ) ? s.substring( st, len ) : s;
}
/**
  * Removes white space from end this string.
  * <p>
  * All characters that have codes less than or equal to
  * <code>'&#92;u0020'</code> (the space character) are considered to be
  * white space.
  *
  * @return  this string, with trailing white space removed
  */
public static String trimTrailing(String s)
{
	if ( s == null )
	{
		return null;
	}
	int len = s.length();
	int origLen = len;
	while ( ( len > 0) && (s.charAt(len - 1) <= ' ') && s.charAt( len - 1 ) != '\u001A' )
	{
		len--;
	}
	return(len != origLen) ? s.substring(0, len) : s;
}

public static String trimCharacters(String strIn, String trimChar)
{
	return trimFirstCharacters( trimLastCharacters( strIn, trimChar ), trimChar );
}
public static String trimFirstCharacters(String strIn, String trimChar)
{
	if ( strIn == null )
	{
		return null;
	}
	while (  strIn.startsWith( trimChar ) )
	{
		strIn = strIn.substring( trimChar.length() );
	}
	return strIn;
}

public static String trimLastCharacters(String strIn, String trimChar)
{
	if ( strIn == null )
	{
		return null;
	}
	while (  strIn.endsWith( trimChar ) )
	{
		strIn = strIn.substring( 0, strIn.length() - trimChar.length() );
	}
	return strIn;
}

/**
 * Verdeelt een String in regels van ongeveer <code>regelLengte</code> lang, netjes
 * afbrekend op wit. Er wordt GEEN rekening gehouden met Enters.
 * @return java.lang.String
 * @param s java.lang.String
 * @param regelLengte int
 */
public static String verdeelInRegels( String s, int regelLengte )
{
	// @@NOG Als het maar een regel is, slaat hij die while over en is afbreekpos onjuist.
	//       Lus nalopen waar de denkfout zit; voor nu even apart geval checken
	//       ==> Zie de code in verdeelInRegelsMetVasteKantlijn
	if ( s.length() <= regelLengte )
	{
		return s;
	}
		
	int oudePos = 0;
	int nieuwePos = regelLengte;
	int afbreekPos = 0;
	int regels = 0; // Debug
	int totLengte = 0; // Debug
	boolean debug = false;
	StringBuffer sb = new StringBuffer();
	while ( nieuwePos < s.length() )
	{
		// Zoek terug, maar niet verder dan de regellengte
		int linksPos = nieuwePos - 1;
		while ( linksPos > nieuwePos - regelLengte && ! Character.isWhitespace( s.charAt( linksPos ) ) )
		{
			linksPos--;
		}

		// Zoek vooruit
		int rechtsPos = nieuwePos;
		while ( rechtsPos < s.length() && ! Character.isWhitespace( s.charAt( rechtsPos ) ) )
			rechtsPos++;

		// We nemen de kleinste afstand
		if ( ( linksPos > nieuwePos - regelLengte ) && ( ( nieuwePos - linksPos ) < ( rechtsPos - nieuwePos ) ) )
		{
			afbreekPos = linksPos;
		}
		else
		{
			afbreekPos = rechtsPos;
		}

		if ( debug )
		{
			regels++;
			totLengte += afbreekPos - oudePos;
			sb.append( String.valueOf( regels ) + " " + String.valueOf( afbreekPos - oudePos ) + " " );
		}
		// Copieer naar de stringbuffer
		sb.append( s.substring( oudePos, afbreekPos ) ); // Rarigheidje bij substring: de substring is inclusief oudePos en EXCLUSIEF afbreekPos
		sb.append( "\n" );

		//  Zet klaar voor de volgende ronde
		oudePos = afbreekPos + 1;
		nieuwePos = oudePos + regelLengte;
	}

	// Laatste stukje
	if ( afbreekPos + 1 < s.length() )
	{
		sb.append( s.substring( afbreekPos + 1 ) );
		if ( debug )
		{
			regels++;
			totLengte += s.length() - afbreekPos + 1;
		}
	}
	if ( debug )
	{
		System.out.println( "aantal regels: " + regels + " gem. lengte: " + (double)totLengte / (double)regels );
	}

	return sb.toString();
}
/**
 * Als verdeelInRegels(), maar returns een List met losse Strings
 * @param s java.lang.String
 * @param regelLengte int
 * @return Een List met de tekstregels
 */
public static List<String> verdeelInRegelsList( String s, int regelLengte )
{
	// Eerst effe shoppen bij de buren
	String jattenDieHandel = verdeelInRegels( s, regelLengte );

	// Daarna bij de andere buren
	return explode2ToList( jattenDieHandel, "\n" );
}
/**
 * Verdeelt een String in regels van maximaal <code>regelLengte</code> lang, netjes
 * afbrekend op wit. Er wordt GEEN rekening gehouden met Enters.
 * <p>
 * N.B. Vooralsnog laten we de whitespace op het afbreekpunt gewoon staan, dus de string "123 456 789" met
 *      een regellengte van 10 wordt afgebroken in "123 456 ", "789". Je kunt her er desgewenst gewoon
 *      aftrimmen als je het weg wilt hebben.
 * @param s De te verdelen regels
 * @param regelLengte int
 * @return Een List met de regels
 */
public static List<String> verdeelInRegelsMetVasteKantlijn( String s, int regelLengte )
{
	List<String> res = new ArrayList<>();

	// @@NOG Als het maar een regel is, slaat hij die while over en is afbreekpos onjuist.
	//       Lus nalopen waar de denkfout zit; voor nu even apart geval checken
	if ( s.length() <= regelLengte )
	{
		res.add( s );
		return res;
	}

	// Voor het gemak even een char-array van de string maken
	char [] chars = new char[s.length()];
	s.getChars( 0, chars.length, chars, 0 );

	int oudePos   = 0;
	int nieuwePos = Math.min( regelLengte, chars.length );
	while ( nieuwePos < chars.length )
	{
		// We gaan nu naar links zoeken naar een spatie. We moeten alleen oppassen voor het geval dat er geen
		// spatie is. m.a.w. dat er 1 lang woord op de regel staat. In dat geval breken we het superlange
		// woord gewoon af op de regellengte.

		// AfbreekPos zetten we nu VOOR het laatste teken op de regel
		int afbreekPos = nieuwePos - 1;
		while ( afbreekPos > oudePos && ! Character.isWhitespace( chars[afbreekPos] ) )
		{
			afbreekPos--;
		}

		// Nu zetten we afbreekPos eentje voorbij het laatste teken op de regel, zoals gebruikelijk is in Java
		// (zie String.substring ). Tevens passen we een correctie toe als er geen spatie gevonden is
		if ( afbreekPos == oudePos )
		{
			afbreekPos = nieuwePos;
		}
		else
		{
			afbreekPos++;
		}

		res.add( s.substring( oudePos, afbreekPos ) );

		//  Zet klaar voor de volgende ronde
		oudePos   = afbreekPos;
		nieuwePos = Math.min( oudePos + regelLengte, chars.length );
	}
	// De laatste regel moet apart, want daar hoef je niet meer af te breken
	Assert.assertTrue( chars.length - oudePos <= regelLengte, "Laatste regel groter dan regellengte" );
	if ( oudePos < nieuwePos )
	{
		res.add( s.substring( oudePos, nieuwePos ) );
	}

	return res;
}
/**
 * Verdeelt een String in regels van maximaal <code>regelLengte</code> lang, netjes
 * afbrekend op wit. Het verschil met verdeelInRegels zit in het woordje "maximum", en in het feit
 * dat we ook op Enters afbreken.
 * <p>
 * N.B. Vooralsnog laten we de whitespace op het afbreekpunt gewoon staan, dus de string "123 456 789" met
 *      een regellengte van 10 wordt afgebroken in "123 456 ", "789". Je kunt her er desgewenst gewoon
 *      aftrimmen als je het weg wilt hebben.
 * @param s De te verdelen regels
 * @param regelLengte int
 * @return Een List met de regels
 */
public static List<String> XXXverdeelTekstInRegels( String s, int regelLengte )
{
	List<String> res = new ArrayList<>();

	// @@NOG Als het maar een regel is, slaat hij die while over en is afbreekpos onjuist.
	//       Lus nalopen waar de denkfout zit; voor nu even apart geval checken
	if ( s.length() <= regelLengte )
	{
		res.add( s );
		return res;
	}

	// Voor het gemak even een char-array van de string maken
	char [] chars = new char[s.length()];
	s.getChars( 0, chars.length, chars, 0 );

	int oudePos   = 0;
	int nieuwePos = Math.min( regelLengte, chars.length );
	while ( nieuwePos < chars.length )
	{
		// We gaan nu naar links zoeken naar een spatie. We moeten alleen oppassen voor het geval dat er geen
		// spatie is. m.a.w. dat er 1 lang woord op de regel staat. In dat geval breken we het superlange
		// woord gewoon af op de regellengte.

		// AfbreekPos zetten we nu VOOR het laatste teken op de regel
		int afbreekPos = nieuwePos - 1;
		while ( afbreekPos > oudePos && ! Character.isWhitespace( chars[afbreekPos] ) )
		{
			afbreekPos--;
		}

		// Nu zetten we afbreekPos eentje voorbij het laatste teken op de regel, zoals gebruikelijk is in Java
		// (zie String.substring ). Tevens passen we een correctie toe als er geen spatie gevonden is
		if ( afbreekPos == oudePos )
		{
			afbreekPos = nieuwePos;
		}
		else
		{
			afbreekPos++;
		}

		res.add( s.substring( oudePos, afbreekPos ) );

		//  Zet klaar voor de volgende ronde
		oudePos   = afbreekPos;
		nieuwePos = Math.min( oudePos + regelLengte, chars.length );
	}
	// De laatste regel moet apart, want daar hoef je niet meer af te breken
	Assert.assertTrue( chars.length - oudePos <= regelLengte, "Laatste regel groter dan regellengte" );
	if ( oudePos < nieuwePos )
	{
		res.add( s.substring( oudePos, nieuwePos ) );
	}

	return res;
}
public static String verdubbelEnkeleQuotes( String inputString )
{
	if( inputString == null || inputString.length() == 0 )
	{
		return inputString;
	}
	return replaceAll( inputString, "'", "''" );
}
/**
 * Als een String kleiner is dan een bepaalde lengte wordt ze uitgevuld met een opgegeven teken
 * @return java.lang.String
 * @param s De String om uit te vullen
 * @param uitvulChar Het teken dat gebruikt wordt om de String uit te vullen
 * @param lengte De gewenste lengte
 */
public static String vulUit(String s, char uitvulChar, int lengte)
{
	if ( s.length() < lengte )
	{
		return new String( s + StringHelper.repChar( uitvulChar, lengte - s.length() ) );
	}
	else
	{
		return s;
	}
}
/**
 * Als een String kleiner is dan een bepaalde lengte wordt ze uitgevuld met spaties
 * @return java.lang.String
 * @param s De String om uit te vullen
 * @param uitvulChar Het teken dat gebruikt wordt om de String uit te vullen
 * @param lengte De gewenste lengte
 */
public static String vulUitMetSpaties(String s, int lengte)
{
	return vulUit( s, ' ', lengte );
}

public static String XXXverdubbelEnkeleQuotes( String inputString )
{
	// !!!!! Alleen enkele quotes worden verdubbeld en meestal wil je ALLE quotes 
	// verdubbelen, zodat twee quotes vier quotes worden, etceterea.
	String result = inputString;		
	int indexVanaf = 0;
		
	while( indexVanaf <= result.length() )
	{
		int quoteIndex = result.indexOf( STRING_ENKELE_QUOTE, indexVanaf );
		
		// niet gevonden? dan zijn we klaar
		if( quoteIndex < 0 )
		{
			break;
		}
		// stom, zeg. indexOf retrouneert de positie VANAF de fromIndex
		quoteIndex = quoteIndex + indexVanaf;
		
		// is het de laatste positie?
		if( quoteIndex == result.length() - 1 )
		{
			result = result + STRING_ENKELE_QUOTE; 
		}
		// staat er nog geen quote achter?								 
		else if( ! STRING_ENKELE_QUOTE.equals( String.valueOf( result.charAt( quoteIndex + 1 ) ) ) )
		{				
			String strLinks  = result.substring( 0, quoteIndex + 1 );
			String strRechts = result.substring( quoteIndex + 1, result.length() );
			// frummel er een quote tussen
			result = strLinks + STRING_ENKELE_QUOTE + strRechts;
		}
		
		// en bij het volgende rondje vanaf twee posities voorbij de eerste quote zoeken				
		indexVanaf = quoteIndex + 2;	
	}		
	return result;
}
public static List<String> addSingleQuotes( Collection<String> aToBeQuoted )
{
	List<String> ret = new ArrayList<>();
	for ( String string : aToBeQuoted )
    {
	    ret.add( "'" + string + "'" );
    }
	return ret;
}

}
