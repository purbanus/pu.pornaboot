package pu.porna.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pu.porna.config.PornaConfig;
import pu.porna.dal.PornaFile.FileEntry;

import freemarker.template.utility.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public class PornaFileReader
{
//private static final Logger LOG = LoggerFactory.getLogger(PornaFileReader.class);

public PornaFile readPornaFile( String aDirectory, PornaConfig aPornaConfig  ) throws IOException
{
	Properties properties = readPornaProperties( aDirectory + File.separator + aPornaConfig.getPornaFileName() );
	Map<String, FileEntry> fileEntryMap = extractFileEntriesFromProperties( properties );
	return PornaFile.builder()
		// .directory( getDirectory() )
		.fileEntries( fileEntryMap )
		.build();
}
Properties readPornaProperties( String aPath ) throws FileNotFoundException, IOException
{
	File file = new File( aPath );
	if ( ! file.exists() )
	{
		return new Properties();
	}
	try ( Reader reader = new FileReader( aPath ); )
	{
		Properties pornaProperties = new Properties();
		pornaProperties.load( reader );
		return pornaProperties;
	}
}
Map<String, FileEntry> extractFileEntriesFromProperties( Properties aProperties )
{
	Map<String, FileEntry> fileEntryMap = new HashMap<>();
	for ( Map.Entry<Object, Object> entry : aProperties.entrySet() )
	{
		String key = (String) entry.getKey();
		int pointPos = key.lastIndexOf( "." );
		if ( pointPos < 0 )
		{
			handleInvalidPornaProperty( key, entry.getValue() );
		}
		else
		{
			String fileName = key.substring( 0, pointPos );
			String property = key.substring( pointPos + 1 );
			List<String> values = getValues( (String) entry.getValue() );
			FileEntry fileEntry = fileEntryMap.get( fileName );
			if ( fileEntry == null )
			{
				fileEntry = new FileEntry();
				fileEntryMap.put( fileName, fileEntry );
			}
			fileEntry.getProperties().putAll( property, values );
		}
	}
	return fileEntryMap;
}
private List<String> getValues( String aValuesString )
{
	return List.of( StringUtil.split( aValuesString, ',' ) );
}
private void handleInvalidPornaProperty( String aKey, Object aValue )
{
	// @@NOG Mailen?
	log.error( "Error interpreting pornafile entry with key=" + aKey + " and values=" + aValue );
}

}
