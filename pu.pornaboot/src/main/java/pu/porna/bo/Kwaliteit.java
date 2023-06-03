package pu.porna.bo;

public enum Kwaliteit
{
TOP( "top" ), GOED( "goed" ), MWAH( "mwah" );
private final String name;
public static Kwaliteit fromString( String kwaliteitString)
{
	if ( kwaliteitString == null )
	{
		return null;
	}
	switch ( kwaliteitString.toLowerCase() )
	{
		case "top": return TOP;
		case "goed": return GOED;
		case "mwah": return MWAH;
		default: throw new RuntimeException( "Ongeldige kwaliteit: " + kwaliteitString );
	}
}
private Kwaliteit( String aName )
{
	name = aName;
}
public String getName()
{
	return name;
}
@Override
public String toString()
{
	return getName();
}
}
