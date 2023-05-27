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
public class TestPornaFile
{
@Autowired PornaConfig pornaConfig;

public static Properties createProperties()
{
	Properties properties = new Properties();
	properties.put( "FileA.type", "anal,busty" );
	properties.put( "FileA.kwaliteit", "top" );
	properties.put( "FileB.type", "paartjes" );
	properties.put( "FileB.kwaliteit", "goed" );
	return properties;
}
public static Map<String, FileEntry> createFileEntries()
{
	Properties properties = createProperties();
	
	Map<String, FileEntry> fileEntries = new PornaFileReader().extractFileEntriesFromProperties( properties );
	return fileEntries;
}

public static void checkProperties( Properties properties )
{
	assertEquals( "anal,busty", properties.get( "FileA.type" ) );
	assertEquals( "paartjes", properties.get( "FileB.type" ) );
	assertEquals( "top", properties.get( "FileA.kwaliteit" ) );
	assertEquals( "goed", properties.get( "FileB.kwaliteit" ) );
}
public static void checkFileEntries( Map<String, FileEntry> fileEntries )
{
	checkFileEntries( fileEntries, false );
}
public static void checkFileEntries( Map<String, FileEntry> fileEntries, boolean aInvalidProperty )
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

@Test
public void testFromDirectory() throws IOException
{
	URL url = getClass().getResource( ".porna" );
	String path = url.getPath();
	int lastSlashPos = path.lastIndexOf( "/" );
	String directory = path.substring( 0, lastSlashPos );

	PornaFile pornaFile = PornaFile.fromDirectory( directory, pornaConfig );
//	assertEquals( directory, pornaFile.getDirectory() );
	Map<String, FileEntry> fileEntries = pornaFile.getFileEntries();
	checkFileEntries( fileEntries );
	
}

@Test
public void testCreateProperties()
{
	Map<String, FileEntry> fileEntries = createFileEntries();
	PornaFile pornaFile = new PornaFile();
	pornaFile.setFileEntries( fileEntries );
	Properties properties = pornaFile.createProperties();
	checkProperties( properties );
}

@Test
public void testGetDistinctProperties()
{
	Map<String, FileEntry> fileEntries = createFileEntries();
	PornaFile pornaFile = new PornaFile();
	pornaFile.setFileEntries( fileEntries );
	MultiValuedMap<String, String> distinctProperties = pornaFile.getDistinctProperties();
	assertTrue( distinctProperties.containsKey( "kwaliteit" ) );
	Collection<String> kwaliteiten = distinctProperties.get( "kwaliteit" );
	assertTrue( kwaliteiten.contains( "top" ) );
	assertTrue( kwaliteiten.contains( "goed" ) );
	assertTrue( distinctProperties.containsKey( "kwaliteit" ) );
	Collection<String> types = distinctProperties.get( "type" );
	assertTrue( types.contains( "anal" ) );
	assertTrue( types.contains( "busty" ) );
	assertTrue( types.contains( "paartjes" ) );
}
}
