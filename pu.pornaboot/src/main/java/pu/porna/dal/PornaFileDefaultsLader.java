package pu.porna.dal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.impl.FilesContainerImpl;
import pu.porna.config.PornaConfig;
import pu.porna.dal.PornaFile.FileEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Data
@Slf4j
public class PornaFileDefaultsLader
{
@Data
@AllArgsConstructor
@NoArgsConstructor
public static class KwaliteitProperty
{
private String kwaliteit;
private String property;
}

@Autowired private PornaConfig pornaConfig;
@Autowired private FilesContainerImpl filesContainer;

public void maakPornaFiles() throws IOException, URISyntaxException
{
	StopWatch timer = StopWatch.createStarted();
	for ( Directory directory : getFilesContainer().getDataHolder().getDirectories() )
	{
		KwaliteitProperty typeKwaliteit = bepaalKwaliteitPropertyUitDirectory( directory.getDisplayName() );
		FileEntry fileEntry = maakFileEntry( typeKwaliteit );
		Map<String, FileEntry> fileEntries = new HashMap<>();
		for ( File file : directory.getFiles() )
		{
			fileEntries.put( file.getName(), fileEntry );
		}
		PornaFile pornaFile = new PornaFile( directory.getName(), pornaConfig, fileEntries );
		pornaFile.save();
	}
	LOG.info( "MaakPornaFiles klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
}

public static KwaliteitProperty bepaalKwaliteitPropertyUitDirectory( String aDirectory )
{
	Path path = Paths.get( aDirectory );
	int index = path.getNameCount() - 1; //Laatste element
	String pathElement = path.getName( index ).toString();
	KwaliteitProperty kwaliteitProperty = new KwaliteitProperty();
	if ( pathElement.equals( "top" ) || pathElement.equals( "goed" ) || pathElement.equals( "mwah" ) )
	{
		kwaliteitProperty.setKwaliteit( pathElement );
		index--;
		pathElement = path.getName( index ).toString();
		if ( pathElement.equals( "gewoon" ) || pathElement.equals( "spanking" ) )
		{
			kwaliteitProperty.setProperty( "bdsm-" + pathElement );
			return kwaliteitProperty;
		}
	}
	if ( path.getNameCount() == 1 )
	{
		kwaliteitProperty.setProperty( path.getName( 0 ).toString() );
	}
	else
	{
		kwaliteitProperty.setProperty( path.getName( 1 ).toString() );
	}
	return kwaliteitProperty;
}
FileEntry maakFileEntry( KwaliteitProperty typeKwaliteit )
{
	FileEntry fileEntry = new FileEntry();
	fileEntry.getProperties().put( "property", typeKwaliteit.getProperty() );
	if ( typeKwaliteit.getKwaliteit() != null )
	{
		fileEntry.getProperties().put( "kwaliteit", typeKwaliteit.getKwaliteit() );
	}
	return fileEntry;
}


}
