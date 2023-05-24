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
public static class TypeKwaliteit
{
private String type;
private String kwaliteit;
}

@Autowired private PornaConfig pornaConfig;
@Autowired private FilesContainerImpl filesContainer;

public void maakPornaFiles() throws IOException, URISyntaxException
{
	StopWatch timer = new StopWatch();
	for ( Directory directory : getFilesContainer().getDataHolder().getDirectories() )
	{
		TypeKwaliteit typeKwaliteit = bepaalTypeKwaliteitUitDirectory( directory.getDisplayName() );
		FileEntry fileEntry = maakFileEntry( typeKwaliteit );
		Map<String, FileEntry> fileEntries = new HashMap<>();
		for ( File file : directory.getFiles() )
		{
			fileEntries.put( file.getName(), fileEntry );
		}
		PornaFile pornaFile = new PornaFile( directory.getName(), pornaConfig, fileEntries );
		pornaFile.save();
	}
	log.info( "MaakPornaFiles klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
}

TypeKwaliteit bepaalTypeKwaliteitUitDirectory( String aDirectory )
{
	Path path = Paths.get( aDirectory );
	int index = path.getNameCount() - 1; //Laatste element
	String pathElement = path.getName( index ).toString();
	TypeKwaliteit typeKwaliteit = new TypeKwaliteit();
	if ( pathElement.equals( "top" ) || pathElement.equals( "goed" ) || pathElement.equals( "mwah" ) )
	{
		typeKwaliteit.setKwaliteit( pathElement );
		index--;
		pathElement = path.getName( index ).toString();
		if ( pathElement.equals( "gewoon" ) || pathElement.equals( "spanking" ) )
		{
			typeKwaliteit.setType( "bdsm-" + pathElement );
			return typeKwaliteit;
		}
	}
	if ( path.getNameCount() == 1 )
	{
		typeKwaliteit.setType( path.getName( 0 ).toString() );
	}
	else
	{
		typeKwaliteit.setType( path.getName( 1 ).toString() );
	}
	return typeKwaliteit;
}
FileEntry maakFileEntry( TypeKwaliteit typeKwaliteit )
{
	FileEntry fileEntry = new FileEntry();
	fileEntry.getProperties().put( "type", typeKwaliteit.getType() );
	if ( typeKwaliteit.getKwaliteit() != null )
	{
		fileEntry.getProperties().put( "kwaliteit", typeKwaliteit.getKwaliteit() );
	}
	return fileEntry;
}


}
