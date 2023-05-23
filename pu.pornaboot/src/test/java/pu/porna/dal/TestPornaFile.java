package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	values = propertiesMap.get( "kwaliteit" );
	assertTrue( values.containsAll( List.of( "top") ) );
}

}
