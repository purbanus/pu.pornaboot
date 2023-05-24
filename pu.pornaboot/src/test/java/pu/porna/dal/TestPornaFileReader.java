package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pu.porna.config.PornaConfig;
import pu.porna.dal.PornaFile.FileEntry;

import lombok.Data;

@Data
@SpringBootTest
public class TestPornaFileReader
{
@Autowired PornaConfig pornaConfig;

@Test
public void testExtractFileEntriesFromProperties()
{
	Map<String, FileEntry> fileEntries = TestPornaFile.createFileEntries();
	TestPornaFile.checkFileEntries( fileEntries );
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
	TestPornaFile.checkFileEntries( fileEntries, true );
}

@Test
public void testReadPornaProperties() throws IOException
{
	URL url = getClass().getResource( ".porna" );
	String path = url.getPath();
	int lastSlashPos = path.lastIndexOf( "/" );
	String directory = path.substring( 0, lastSlashPos );
	Properties properties = new PornaFileReader().readPornaProperties( directory + File.separator + getPornaConfig().getPornaFileName() );
	TestPornaFile.checkProperties( properties );
}
@Test
public void testReadPornaFile() throws IOException
{
	URL url = getClass().getResource( ".porna" );
	String path = url.getPath();
	int lastSlashPos = path.lastIndexOf( "/" );
	String directory = path.substring( 0, lastSlashPos );

	PornaFile pornaFile = new PornaFileReader().readPornaFile( directory, getPornaConfig() );
	assertEquals( directory, pornaFile.getDirectory() );
	Map<String, FileEntry> fileEntries = pornaFile.getFileEntries();
	TestPornaFile.checkFileEntries( fileEntries );
	
}


}
