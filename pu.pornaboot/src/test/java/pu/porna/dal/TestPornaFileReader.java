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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import pu.porna.dal.PornaFile.FileEntry;

@SpringBootTest
public class TestPornaFileReader
{

@Test
public void testExtractFileEntriesFromProperties()
{
	Properties properties = new Properties();
	properties.put( "FileA.type", "anal,busty" );
	properties.put( "FileA.hoegoed", "top" );
	properties.put( "FileB.type", "paartjes" );
	properties.put( "FileB.hoegoed", "goed" );
	
	Map<String, FileEntry> fileEntries = new PornaFileReader( "miepmiep" ).extractFileEntriesFromProperties( properties );
	checkProperties( fileEntries );
}
@Test
public void testExtractFileEntriesFromPropertiesWithInvalidProperty()
{
	Properties properties = new Properties();
	properties.put( "FileA.type", "anal,busty" );
	properties.put( "FileA.hoegoed", "top" );
	properties.put( "FileBtype", "paartjes" );
	properties.put( "FileB.hoegoed", "goed" );
	
	Map<String, FileEntry> fileEntries = new PornaFileReader( "pipo" ).extractFileEntriesFromProperties( properties );
	checkProperties( fileEntries );
}

@Test
public void testReadPornaFile() throws IOException
{
	URL url = getClass().getResource( ".porna" );
	String path = url.getPath();
	int lastSlashPos = path.lastIndexOf( "/" );
	String directory = path.substring( 0, lastSlashPos );

	PornaFile pornaFile = new PornaFileReader( directory ).readPornaFile();
	assertEquals( directory, pornaFile.getDirectory() );
	Map<String, FileEntry> fileEntries = pornaFile.getFileEntries();
	checkProperties( fileEntries );
	
}

private void checkProperties( Map<String, FileEntry> fileEntries )
{
	assertEquals( 2, fileEntries.size() );
	FileEntry fileEntry = fileEntries.get( "FileA" );
	assertNotNull( fileEntry );
	MultiValuedMap<String, String> propertiesMap = fileEntry.getProperties();
	Collection<String> values = propertiesMap.get( "type" );
	assertTrue( values.containsAll( List.of( "anal", "busty") ) );
}

}
