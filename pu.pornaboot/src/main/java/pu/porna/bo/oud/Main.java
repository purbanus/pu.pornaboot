package pu.porna.bo.oud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import pu.porna.bo.impl.FileWalker;

public class Main
{
public static final String OLD_ALL_DIRECTORY = "~/Boeken/Strips/00-uitzoeken/Grote verzameling";
public static final String OLD_ALL_DIRECTORY_PATTERN = "~/Boeken/Strips/00-uitzoeken/Grote verzameling/strips**";


public static void main( String [] args ) throws IOException
{
	new Main().run();
}
public static String expandHome( String aPath )
{
	if ( aPath.startsWith( "~" + File.separator ) )
	{
		aPath = System.getProperty( "user.home" ) + aPath.substring( 1 );
	}
	else if ( aPath.startsWith( "~" ) )
	{
		// here you can implement reading homedir of other users if you care
		throw new UnsupportedOperationException( "Home dir expansion not implemented for explicit usernames" );
	}
	return aPath;
}
public Main()
{
	super();
}

private void run() throws IOException
{
    Path startingDir = Paths.get( expandHome( OLD_ALL_DIRECTORY ) );
    String pattern = expandHome( OLD_ALL_DIRECTORY_PATTERN );

    FileWalker walker = new FileWalker( pattern );
    Files.walkFileTree( startingDir, walker );
    System.out.println( "Klaar! Files verzameld: " + walker.getDirectories().size() );
}

}
