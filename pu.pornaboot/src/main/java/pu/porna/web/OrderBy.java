package pu.porna.web;

import java.util.Comparator;

import pu.porna.dal.FileSystemObject;

public enum OrderBy
{

OP_NAAM( "opNaam", FileSystemObject.OP_NAAM ),
OP_SIZE( "opSize", FileSystemObject.OP_SIZE ),
OP_DATUM( "opDatum", FileSystemObject.OP_DATUM);
	
private final String name;
private final Comparator<FileSystemObject> comparator;

public static OrderBy fromString( String aOrderBy )
{
	switch ( aOrderBy )
	{
		case "opNaam": return OP_NAAM;
		case "opSize": return OP_SIZE;
		case "opDatum": return OP_DATUM;
		default: throw new RuntimeException( "Ongeldige waarde voor OrderBy: " + aOrderBy );
	}
}
private OrderBy( String aName, Comparator<FileSystemObject> aComparator )
{
	name = aName;
	comparator = aComparator;
}
public String getName()
{
	return name;
}
public Comparator<FileSystemObject> getComparator()
{
	return comparator;
}
@Override
public String toString()
{
	return getName();
}

}
