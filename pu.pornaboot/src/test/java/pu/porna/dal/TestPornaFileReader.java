package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pu.porna.config.PornaConfig;
import pu.porna.dal.PornaFile.FileEntry;

@SpringBootTest
public class TestPornaFileReader
{
@Autowired PornaConfig pornaConfig;

@Test
public void testExtractFileEntriesFromProperties()
{
	Properties properties = new Properties();
	properties.put( "FileA.type", "anal,busty" );
	properties.put( "FileA.kwaliteit", "top" );
	properties.put( "FileB.type", "paartjes" );
	properties.put( "FileB.kwaliteit", "goed" );
	
	Map<String, FileEntry> fileEntries = new PornaFileReader().extractFileEntriesFromProperties( properties );
	checkProperties( fileEntries );
}
@Test
public void testExtractFileEntriesFromPropertiesWithInvalidProperty()
{
	Properties properties = new Properties();
	properties.put( "FileA.type", "anal,busty" );
	properties.put( "FileA.kwaliteit", "top" );
	properties.put( "FileBtype", "paartjes" );
	properties.put( "FileB.kwaliteit", "goed" );
	
	Map<String, FileEntry> fileEntries = new PornaFileReader().extractFileEntriesFromProperties( properties );
	checkProperties( fileEntries, true );
}

@Test
public void testReadPornaFile() throws IOException
{
	URL url = getClass().getResource( ".porna" );
	String path = url.getPath();
	int lastSlashPos = path.lastIndexOf( "/" );
	String directory = path.substring( 0, lastSlashPos );

	PornaFile pornaFile = new PornaFileReader().readPornaFile( directory, pornaConfig );
//	assertEquals( directory, pornaFile.getDirectory() );
	Map<String, FileEntry> fileEntries = pornaFile.getFileEntries();
	checkProperties( fileEntries );
	
}

private void checkProperties( Map<String, FileEntry> fileEntries )
{
	checkProperties( fileEntries, false );
}
private void checkProperties( Map<String, FileEntry> fileEntries, boolean aInvalidProperty )
{
	assertEquals( 2, fileEntries.size() );
	{
		FileEntry fileEntry = fileEntries.get( "FileA" );
		assertNotNull( fileEntry );
		MultiValuedMap<String, String> propertiesMap = fileEntry.getProperties();
		Collection<String> values = propertiesMap.get( "type" );
		assertTrue( values.containsAll( List.of( "anal", "busty") ) );
		values = propertiesMap.get( "kwaliteit" );
		assertTrue( values.containsAll( List.of( "top") ) );
	}
	{
		FileEntry fileEntry = fileEntries.get( "FileB" );
		assertNotNull( fileEntry );
		MultiValuedMap<String, String> propertiesMap = fileEntry.getProperties();
		if ( ! aInvalidProperty )
		{
			Collection<String> values = propertiesMap.get( "type" );
			assertTrue( values.containsAll( List.of( "paartjes") ) );
		}
		Collection<String> values = propertiesMap.get( "kwaliteit" );
		assertTrue( values.containsAll( List.of( "goed") ) );
	}
}

}
