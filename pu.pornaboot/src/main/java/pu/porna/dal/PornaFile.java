package pu.porna.dal;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

// @@NOG Hoe haal je dit uit een propertyfile?
public static String PORNA_FILE_NAME = ".porna";

@Data
@NoArgsConstructor
@AllArgsConstructor
public static class FileEntry
{
	private MultiValuedMap<String, String> properties = new ArrayListValuedHashMap<>();
}
// private String directory; // @@NOG NODIG?
private Map<String, FileEntry> fileEntries;

public static PornaFile fromDirectory( String aDirectory ) throws IOException
{
	return new PornaFileReader( aDirectory ).readPornaFile();
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
}
