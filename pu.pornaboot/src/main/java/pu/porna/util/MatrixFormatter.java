package pu.porna.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MatrixFormatter
{
private final List<Line> lines = new ArrayList<>();
private final List<MatrixFormatterColumnDef> columnDef = new ArrayList<>();
private int columnSpacing = 2;
private Alignment defaultAlignment = ALIGN_LEFT;

public static final class Alignment
{
private final String name;
private Alignment( String aName )
{
	super();
	name = aName;
}
@Override
public String toString()
{
	return name;
}
}
public static final Alignment ALIGN_LEFT    = new Alignment( "LEFT"    );
public static final Alignment ALIGN_CENTER  = new Alignment( "CENTER"  );
public static final Alignment ALIGN_RIGHT   = new Alignment( "RIGHT"   );
//	public static final Alignment ALIGN_UNKNOWN = new Alignment( "UNKNOWN" );

public static final int WIDTH_UNKNOWN = -1;

//	private Vector tabs = new Vector();
//	private Vector alignments = new Vector();

private abstract class Line
{
public abstract String getOutput();
public abstract int getColumnCount();
protected int getColumnWidth( int aColumn )
{
	if ( aColumn > getColumnCount() - 1 )
	{
		return 0;
	}
	return getColumnWidthImpl( aColumn );
}
protected abstract int getColumnWidthImpl( int aColumn );
}

private class HeaderLine extends Line
{
private final String s;
public HeaderLine( String as )
{
	s = as;
}
@Override
public int getColumnCount()
{
	return 1;
}
@Override
protected int getColumnWidthImpl( int aColumn )
{
	return 0;
}
@Override
public String getOutput()
{
	return s;
}
@Override
public String toString()
{
	return s;
}
}
private class DetailLine extends Line
{
private final String [] columns;
public DetailLine( String [] aColumns )
{
	columns = aColumns;
}
public DetailLine( String aColumns )
{
	this( aColumns, "\t" );
}
public DetailLine( String aColumns, String aSeparator )
{
	columns = StringHelper.explode2( aColumns, "\t" );
}
@Override
public int getColumnCount()
{
	return columns.length;
}
@Override
protected int getColumnWidthImpl( int aColumn )
{
	return columns[aColumn] == null ? 0 : columns[aColumn].length();
}
@Override
public String getOutput()
{
	StringBuffer sb = new StringBuffer();
	for ( int x = 0; x < columns.length; x++ )
	{
		sb.append( formatColumn( x, columns[x] ) );
		sb.append( StringHelper.spaties( getColumnSpacing() ) );
	}
	return sb.toString();
}
@Override
public String toString()
{
	//			return StringHelper.implode( columns, " " );
	return super.toString();
}
}
public MatrixFormatter() {
	super();
}
public void addDetail( String [] aColumnValues )
{
	getLines().add( new DetailLine( aColumnValues ) );
}
public void addDetail( String aColumnValues )
{
	getLines().add( new DetailLine( aColumnValues ) );
}
public void addDetail( Collection<?> aColumnValues )
{
	addDetail( createTabbedString( aColumnValues ) );
}
public void addHeader(String aHeaderLine)
{
	getLines().add( new HeaderLine( aHeaderLine ) );
}
private void adjustColumnWidths()
{
	int maxColumnCount = getMaxColumnCount();
	int [] w = new int[maxColumnCount];
	for ( int x = 0; x < maxColumnCount; x++ )
	{
		w[x] = 0;
	}
	for ( Line L : getLines() )
	{
		for ( int c = 0; c < L.getColumnCount(); c++ )
		{
			if ( L.getColumnWidth( c ) > w[c] )
			{
				w[c] = L.getColumnWidth( c );
			}
		}
	}
	for ( int x = 0; x < w.length; x++ )
	{
		getColumnDef( x ).setWidth( w[x] );
	}
}
private MatrixFormatterColumnDef createDefaultColumnDef()
{
	return new MatrixFormatterColumnDef( this, getDefaultAlignment() );
}
private static String createTabbedString( Collection<?> aCollection )
{
	String [] strings = new String [aCollection.size()];
	int x = 0;
	for ( Object object : aCollection )
	{
		strings[x] = String.valueOf( object );
	}
	return StringHelper.implode( strings, "\t" );
}
private String formatColumn( int aColumn, String aContents )
{
	String c = aContents == null ? "" : StringHelper.trimTrailing( aContents );
	MatrixFormatterColumnDef def = getColumnDef( aColumn );

	if ( def.getAlignment() == ALIGN_LEFT )
	{
		return c + StringHelper.spaties( def.getWidth() - c.length() );
	}

	if ( def.getAlignment() == ALIGN_RIGHT )
	{
		return  StringHelper.spaties( def.getWidth() - c.length() ) + c;
	}

	int startSpace = ( def.getWidth() - c.length() ) / 2;
	return  StringHelper.spaties( startSpace ) + c + StringHelper.spaties( def.getWidth() - c.length() - startSpace );
}
private List<MatrixFormatterColumnDef> getColumnDef()
{
	int maxCol = getMaxColumnCount();
	if ( maxCol > columnDef.size() )
	{
		for ( int x = columnDef.size(); x < maxCol; x++ )
		{
			columnDef.add( createDefaultColumnDef() );
		}
	}
	return columnDef;
}
public MatrixFormatterColumnDef getColumnDef( int aColumn )
{
	return getColumnDef().get( aColumn );
}
//private MatrixFormatterColumnDef [] getColumnDefArray()
//{
//	MatrixFormatterColumnDef [] cdef = new MatrixFormatterColumnDef[getColumnDef().size()];
//	getColumnDef().toArray( cdef );
//	return cdef;
//}
public int getColumnSpacing() {
	return columnSpacing;
}
public Alignment getDefaultAlignment()
{
	return defaultAlignment;
}
private Line [] getLineArray()
{
	//	Line [] line = (Line []) getLines().toArray(); // Cast gaat mis, zie TestCast

	return getLines().toArray( new Line [getLines().size()] );
}
private List<Line> getLines()
{
	return lines;
}
public int getMaxColumnCount()
{
	int numColumns = 0;
	Line [] line = getLineArray();
	for ( Line element : line )
	{
		if ( element.getColumnCount() > numColumns )
		{
			numColumns = element.getColumnCount();
		}
	}
	return numColumns;
}
/**
 * Returns the output of this MatrixFormatter as a single String.
 */
public String getOutput()
{
	StringBuffer sb = new StringBuffer();
	for ( String outputLine : getOutputAsList() )
	{
		sb.append( outputLine ).append( System.getProperty( "line.separator" ) );
	}
	return sb.toString();
}

/**
 * Returns the output of this MatrixFormatter as a List of Strings.
 */
public List<String> getOutputAsList()
{
	Line [] line = getLineArray();

	adjustColumnWidths();


	List<String> ret = new ArrayList<>();
	for ( Line element : line )
	{
		ret.add( element.getOutput() );
	}
	return ret;
}

public void setAlignment( int aColumn, MatrixFormatter.Alignment aAlignment )
{
	// Add extra columns if necessary
	int maxColCount = getMaxColumnCount();
	for ( int x = maxColCount; x <= aColumn; x++ )
	{
		getColumnDef().add( createDefaultColumnDef() );
	}

	// Set the alignment
	getColumnDef().get( aColumn ).setAlignment( aAlignment );
}
public void setColumnSpacing(int newColumnSpacing)
{
	columnSpacing = newColumnSpacing;
}
public void setDefaultAlignment( Alignment newDefaultAlignment )
{
	defaultAlignment = newDefaultAlignment;
}
@Override
public String toString()
{
	return getOutput();
}
}
