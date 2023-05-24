package pu.porna.dal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pu.porna.config.PornaConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PornaFile
{
@SuppressWarnings( "unused" )
private static final Logger LOG = LoggerFactory.getLogger(PornaFile.class);

@Data
@NoArgsConstructor
@AllArgsConstructor
// @@NOG Overbodig. Je kunt net zo goed de MultiValueMap rechtstreeks in de fileEntries zetten
public static class FileEntry
{
	private MultiValuedMap<String, String> properties = new ArrayListValuedHashMap<>();
}
private String directory;
private PornaConfig pornaConfig;
private Map<String, FileEntry> fileEntries;

public static PornaFile fromDirectory( String aDirectory, PornaConfig aPornaConfig ) throws IOException
{
	return new PornaFileReader().readPornaFile( aDirectory, aPornaConfig );
}
public void addProperty( String aFileName, String aProperty, String aValue )
{
	FileEntry fileEntry = getFileEntries().get( aFileName );
	if ( fileEntry == null )
	{
		fileEntry = new FileEntry();
		getFileEntries().put( aFileName, fileEntry );
	}
	fileEntry.getProperties().put( aProperty, aValue );
}
public void save() throws IOException
{
	Properties properties = createProperties();
	String pathName = getDirectory() + "/" + getPornaConfig().getPornaFileName();
	Writer writer = new FileWriter( pathName );
	properties.store( writer, "Porna files" );
}
Properties createProperties()
{
	Properties properties = new Properties();
	for ( String fileName : getFileEntries().keySet() )
	{
		FileEntry fileEntry = fileEntries.get( fileName );
		for ( String propertyName : fileEntry.getProperties().keySet() )
		{
			Collection<String> propertyValueList = fileEntry.getProperties().get( propertyName );
			String propertyValue = String.join( ",", propertyValueList );
			properties.put( fileName + "." + propertyName, propertyValue );
		}
	}
	return properties;
}

}
