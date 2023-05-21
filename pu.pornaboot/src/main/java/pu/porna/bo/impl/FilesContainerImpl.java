package pu.porna.bo.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pu.porna.bo.FilesContainer;
import pu.porna.bo.RowBounds;
import pu.porna.config.PornaConfig;
import pu.porna.dal.Directory;
import pu.porna.dal.File;
import pu.porna.dal.FileWalker;
import pu.porna.dal.PornaFile.FileEntry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Component
//@Data @@NOG Lombok klaagt over een IOException, maar ik zie niet waar dat op slaat
public class FilesContainerImpl implements FilesContainer
{
private static final Logger LOG = LoggerFactory.getLogger( FilesContainer.class );
@Autowired private PornaConfig pornaConfig; 

@Data
@Builder
@AllArgsConstructor
public static class DataHolder
{
private final List<Directory> directories;
private final Map<String, Directory> directoriesMap;
}
private DataHolder dataholder;

public PornaConfig getPornaConfig()
{
	return pornaConfig;
}

@Override
public void refresh() throws IOException
{
	FileWalker fileWalker = new FileWalker( getPornaConfig().getStartingDirectory(), getPornaConfig() );
	List<Directory> newDirectories = fileWalker.run();
	applyPropertiesAndSort( newDirectories );
	Map<String, Directory> newDirectoriesMap = createDirectoriesMap( newDirectories );

	dataholder = new DataHolder( newDirectories, newDirectoriesMap ); 
}

void applyPropertiesAndSort( List<Directory> aDirectories )
{
	for ( Directory directory : aDirectories )
	{
		directory.setTotalNumberOfFiles( directory.getFiles().size() );
		for ( File file : directory.getFiles() )
		{
			applyProperties( file, directory );
		}
		directory.getFiles().sort( ( a, b ) -> a.getName().compareToIgnoreCase( b.getName() ) );
		directory.getSubDirectories().sort( ( a, b ) -> a.getName().compareToIgnoreCase( b.getName() ) );
		applyPropertiesAndSort( directory.getSubDirectories() );
	}
}

void applyProperties( File aFile, Directory aDirectory )
{
	FileEntry fileEntry = aDirectory.getPornaFile().getFileEntries().get( aFile.getName() );
	if ( fileEntry != null )
	{
		aFile.setProperties( fileEntry.getProperties() );
	}
}

Map<String, Directory> createDirectoriesMap( List<Directory> aNewDirectories )
{
	Map<String, Directory> directoriesMap = new HashedMap<>();
	for ( Directory directory : aNewDirectories )
	{
		directoriesMap.put( directory.getName(), directory );
	}
	return directoriesMap;
}

DataHolder getDataHolder() throws IOException
{
	if ( dataholder == null )
	{
		refresh();
	}
	return dataholder;
}

@Override
public Directory getFilesPerDirectory( String aDirectoryName, String aFromFileName, RowBounds aRowBounds ) throws IOException
{
	if ( StringUtils.isEmpty( aDirectoryName ) )
	{
		aDirectoryName = FileWalker.expandHome( getPornaConfig().getStartingDirectory() );
	}
	while ( aDirectoryName.endsWith( "/" ) )
	{
		aDirectoryName = aDirectoryName.substring( 0, aDirectoryName.length() - 1 );
	}
	Directory directory = getDataHolder().getDirectoriesMap().get( aDirectoryName );
	
	directory = retainFilesStartingWith( aFromFileName, directory );
	directory = applyRowBounds( directory, aRowBounds );
	return directory;
}

// @@NOG Je kunt dit veralgemenen door ipv een Directory een List<File> op te geven
Directory retainFilesStartingWith( String aFromFileName, Directory aDirectory )
{
	if ( StringUtils.isEmpty( aFromFileName ) )
	{
		return aDirectory;
	}
	Directory directory = cloneDirectory( aDirectory );
	File fromFile = File.builder()
		.name( aFromFileName )
		.build();
	int index = Collections.binarySearch( directory.getFiles(), fromFile, ( a, b ) -> a.getName().compareToIgnoreCase( b.getName() ) );
	if ( index < 0 )
	{
		index = -index - 1;
	}
	directory.setFiles( directory.getFiles().subList( index, directory.getFiles().size() ) );
	return directory;
}
Directory applyRowBounds( Directory aDirectory, RowBounds aRowBounds )
{
	Directory directory = cloneDirectory( aDirectory );
	int offset = Math.min( aRowBounds.getOffset(), directory.getFiles().size() );
	int limit = Math.min( aRowBounds.getOffset() + aRowBounds.getLimit(), directory.getFiles().size() );
	LOG.debug( "Rowbounds for slice = " + offset + ", " + limit );
	directory.setFiles( directory.getFiles().subList( offset, limit ) );
	return directory;
}

Directory cloneDirectory( Directory directory )
{
	return Directory.builder()
		.name( directory.getName() )
		.dateTimeLastModified( directory.getDateTimeLastModified() )
		.totalNumberOfFiles( directory.getTotalNumberOfFiles() )
		.parent( directory.getParent() )
		.pornaFile( directory.getPornaFile() )
		.files( directory.getFiles() )
		.subDirectories( directory.getSubDirectories() )
		.build();
}
}
