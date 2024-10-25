package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pu.porna.bo.Directory;
import pu.porna.config.PornaConfig;
import pu.porna.util.MatrixFormatter;
import pu.porna.util.StringHelper;
import pu.services.StopWatch;

@SpringBootTest
//@SpringJUnitConfig
//@SpringJUnitWebConfig
public class TestFileWalkerOverVrouwen
{
public static final String OLD_ALL_DIRECTORY = "/media/purbanus/5TB Seagate/Videos/vrouwen";

@Autowired PornaConfig pornaConfig;

@Test
public void testFromDirectory() throws IOException
{
	FileWalker walker = new FileWalker( OLD_ALL_DIRECTORY, pornaConfig );
	StopWatch timer = new StopWatch();
	List<Directory> directories = walker.run();
	System.out.println( "Klaar! Files verzameld in " + timer.getElapsedMs() );
	assertTrue( directories.size() >= 130 );
}

@Test
public void testAndPrint() throws IOException
{
	FileWalker walker = new FileWalker( OLD_ALL_DIRECTORY, pornaConfig );
	StopWatch timer = new StopWatch();
	List<Directory> directories = walker.run();
	System.out.println( "Klaar! Files verzameld in " + timer.getElapsedMs() );
	MatrixFormatter matrixFormatter = new MatrixFormatter();
	matrixFormatter.addHeader( StringHelper.streep( 260 ) );
	matrixFormatter.addDetail( new String [] { "Directory Name", "# Files", "# Subdirectories", "Parent Directory", "Last Modified" } );
	matrixFormatter.addHeader( StringHelper.streep( 260 ) );
	for ( Directory directory : directories )
	{
		matrixFormatter.addDetail( new String [] { directory.getName(), String.valueOf( directory.getFiles().size() ), String.valueOf( directory.getSubDirectories().size() ), directory.getParent() == null ? "null" : directory.getParent().getName(), directory.getDateTimeLastModified().toString() } );
	}
	System.out.println( matrixFormatter.getOutput() );
}

}
