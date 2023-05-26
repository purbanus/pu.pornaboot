package pu.porna.bo.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pu.porna.bo.FilesContainer;
import pu.porna.bo.RowBounds;
import pu.porna.config.PornaConfig;
import pu.porna.dal.Directory;
import pu.porna.dal.File;
import pu.porna.dal.FileWalker;
import pu.porna.dal.PornaFile.FileEntry;
import pu.porna.web.OrderBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
//@Data @@NOG Lombok klaagt over een IOException, maar ik zie niet waar dat op slaat
public class FilesContainerImpl implements FilesContainer
{
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
	StopWatch timer = StopWatch.createStarted();
	FileWalker fileWalker = new FileWalker( getPornaConfig().getStartingDirectory(), getPornaConfig() );
	List<Directory> newDirectories = fileWalker.run();
	applyPropertiesAndSort( newDirectories );
	Map<String, Directory> newDirectoriesMap = createDirectoriesMap( newDirectories );

	dataholder = new DataHolder( newDirectories, newDirectoriesMap ); 
	log.info( "Refresh klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
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

public DataHolder getDataHolder() throws IOException
{
	if ( dataholder == null )
	{
		refresh();
	}
	return dataholder;
}

@Override
public Directory getFilesPerDirectory( String aDirectoryName, String aFromFileName, RowBounds aRowBounds, OrderBy aOrderBy ) throws IOException
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
	directory = cloneDirectory( directory );
	directory = retainFilesStartingWith( aFromFileName, directory );
	directory.getFiles().sort( aOrderBy.getComparator() );
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
	File fromFile = File.builder()
		.name( aFromFileName )
		.build();
	int index = Collections.binarySearch( aDirectory.getFiles(), fromFile, ( a, b ) -> a.getName().compareToIgnoreCase( b.getName() ) );
	if ( index < 0 )
	{
		index = -index - 1;
	}
	aDirectory.setFiles( aDirectory.getFiles().subList( index, aDirectory.getFiles().size() ) );
	return aDirectory;
}

Directory applyRowBounds( Directory aDirectory, RowBounds aRowBounds )
{
	int offset = Math.min( aRowBounds.getOffset(), aDirectory.getFiles().size() );
	int limit = Math.min( aRowBounds.getOffset() + aRowBounds.getLimit(), aDirectory.getFiles().size() );
	log.debug( "Rowbounds for slice = " + offset + ", " + limit );
	aDirectory.setFiles( aDirectory.getFiles().subList( offset, limit ) );
	return aDirectory;
}

Directory cloneDirectory( Directory directory )
{
	return Directory.builder()
		.name( directory.getName() )
		.dateTimeLastModified( directory.getDateTimeLastModified() )
		.totalNumberOfFiles( directory.getTotalNumberOfFiles() )
		.parent( directory.getParent() )
		.pornaFile( directory.getPornaFile() )
		.pornaConfig( pornaConfig )
		.files( directory.getFiles() )
		.subDirectories( directory.getSubDirectories() )
		.build();
}

@Override
public File getFile( String aDirectoryName, String aFileName ) throws IOException
{
	Directory directory = getDataHolder().getDirectoriesMap().get( aDirectoryName );
	return directory.getFile( aFileName );
}
}
