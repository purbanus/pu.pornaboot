package pu.porna.bo.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.FilesContainer;
import pu.porna.bo.Kwaliteit;
import pu.porna.bo.Property;
import pu.porna.bo.RowBounds;
import pu.porna.config.PornaConfig;
import pu.porna.dal.DirectoryConverter;
import pu.porna.dal.FileRepository;
import pu.porna.dal.FileWalker;
import pu.porna.web.OrderBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Component
@Slf4j
//@Data @@NOG Lombok klaagt over een IOException, maar ik zie niet waar dat op slaat
public class FilesContainerImpl implements FilesContainer
{
@Data
@Builder
@AllArgsConstructor
public static class DataHolder
{
private final List<Directory> directories;
private final Map<String, Directory> directoriesMap;
@Builder.Default
private Set<String> distinctProperties = null;
private Set<String> getDistinctProperties()
{
	if ( distinctProperties == null )
	{
		Set<String> newDistinctProperties = new TreeSet<>();
		for ( Directory directory : getDirectories() )
		{
			for ( File file : directory.getFiles() )
			{
				newDistinctProperties.addAll( file.getPropertiesAsStrings() );
			}
		}
		distinctProperties = newDistinctProperties;
	}
	return distinctProperties;
}
// @@NOG Overbodig
public Set<String> getProperties()
{
	return getDistinctProperties();
}
public Set<String> getKwaliteiten()
{
	Set<String> kwaliteiten = new TreeSet<>(); 
	for ( Kwaliteit kwaliteit : Kwaliteit.values() )
	{
		kwaliteiten.add( kwaliteit.getName() );
	}
	return kwaliteiten;
}
}
//@Autowired private DirectoryRepository directoryRepository;
@Autowired private DirectoryConverter directoryConverter;
@Autowired private FileRepository fileRepository;
@Autowired private PornaConfig pornaConfig; 

@ToString.Exclude
@EqualsAndHashCode.Exclude
private DataHolder dataholder;

public PornaConfig getPornaConfig()
{
	return pornaConfig;
}
public Directory getDirectory( String aDirectoryName)
{
	return getDataHolder().getDirectoriesMap().get( aDirectoryName );
}

//@Override
//public void refresh()
//{
//	StopWatch timer = StopWatch.createStarted();
//	FileWalker fileWalker = new FileWalker( getPornaConfig().getStartingDirectory(), getPornaConfig() );
//	List<Directory> newDirectories = fileWalker.run();
//	applyPropertiesAndSort( newDirectories );
//	Map<String, Directory> newDirectoriesMap = createDirectoriesMap( newDirectories );
//
//	dataholder = DataHolder.builder()
//		.directories( newDirectories )
//		.directoriesMap( newDirectoriesMap )
//		.build(); 
//	log.info( "Refresh klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
//}
@Override
public void refresh()
{
	StopWatch timer = StopWatch.createStarted();

	List<Directory> newDirectories = getDirectoryConverter().getAllDirectories();
	applyPropertiesAndSort( newDirectories );
	Map<String, Directory> newDirectoriesMap = createDirectoriesMap( newDirectories );

	dataholder = DataHolder.builder()
		.directories( newDirectories )
		.directoriesMap( newDirectoriesMap )
		.build(); 
	LOG.info( "Refresh klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
}

void applyPropertiesAndSort( List<Directory> aDirectories )
{
	for ( Directory directory : aDirectories )
	{
		directory.setTotalNumberOfFiles( directory.getFiles().size() );
		directory.getFiles().sort( ( a, b ) -> a.getName().compareToIgnoreCase( b.getName() ) );
		// @@NOG Moet dit eigenlijk wel? Alle diectories zitten toch al in aDirectories?
		// @@CHECK
		// directory.getSubDirectories().sort( ( a, b ) -> a.getName().compareToIgnoreCase( b.getName() ) );
		// applyPropertiesAndSort( directory.getSubDirectories() );
	}
}

//void applyProperties( File aFile, Directory aDirectory )
//{
//	FileEntry fileEntry = aDirectory.getPornaFile().getFileEntries().get( aFile.getName() );
//	if ( fileEntry != null )
//	{
//		aFile.setProperties( fileEntry.getProperties() );
//	}
//}

Map<String, Directory> createDirectoriesMap( List<Directory> aNewDirectories )
{
	Map<String, Directory> directoriesMap = new HashedMap<>();
	for ( Directory directory : aNewDirectories )
	{
		directoriesMap.put( directory.getName(), directory );
	}
	return directoriesMap;
}

public DataHolder getDataHolder()
{
	if ( dataholder == null )
	{
		refresh();
	}
	return dataholder;
}
@Override
public List<Directory> getAllDirectories()
{
	return getDataHolder().getDirectories();
}

@Override
public Directory getFilesPerDirectory( String aDirectoryName, String aFromFileName, RowBounds aRowBounds, OrderBy aOrderBy )
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
// @@NOG Waarvoor? YAGNI!
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
	LOG.debug( "Rowbounds for slice = " + offset + ", " + limit );
	aDirectory.setFiles( aDirectory.getFiles().subList( offset, limit ) );
	return aDirectory;
}

Directory cloneDirectory( Directory directory )
{
	return Directory.builder()
		.id( directory.getId() )
		.name( directory.getName() )
		.dateTimeLastModified( directory.getDateTimeLastModified() )
		.pornaConfig( pornaConfig )
		.totalNumberOfFiles( directory.getTotalNumberOfFiles() )
		.parent( directory.getParent() )
		.subDirectories( directory.getSubDirectories() )
		.files( directory.getFiles() )
		.build();
}

@Override
public File getFile( String aDirectoryName, String aFileName ) throws IOException
{
	Directory directory = getDataHolder().getDirectoriesMap().get( aDirectoryName );
	return directory.getFile( aFileName );
}

@Override
public Set<String> getKwaliteiten() throws IOException
{
	return getDataHolder().getKwaliteiten();
}

@Override
public Set<String> getProperties()
{
	return getDataHolder().getProperties();
}

@Override
public void saveFile( String aDirectory, String aFileName, String aKwaliteit, String aProperty, String aReview ) throws IOException
{
	File file = getFile( aDirectory, aFileName );
	file.setKwaliteit( Kwaliteit.fromString( aKwaliteit ) );
	file.setReview( aReview );
	file.setProperties( propertyStringsToProperties( aProperty ) );
	getFileRepository().save( file );
}
List<Property> propertyStringsToProperties( String aPropertyValues )
{
	List<String> propertyStrings = List.of( StringUtils.split( aPropertyValues, ',' ) );
	List<Property> properties = new ArrayList<>();
	for ( String propertyString : propertyStrings )
	{
		properties.add( Property.builder().name( propertyString ).build() );
	}
	return properties;
}

}
