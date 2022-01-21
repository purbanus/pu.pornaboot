package pu.porna.bo.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import pu.porna.bo.FilesDocument;
import pu.porna.dal.Directory;
import pu.porna.dal.File;
import pu.porna.dal.FileWalker;
import pu.porna.dal.PornaFile.FileEntry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Component
public class AllFilesReader
{
public static String START_DIRECTORY = "~/Videos/vrouwen";

@Data
@Builder
@AllArgsConstructor
public static class DataHolder
{
private final List<Directory> directories;
private final Map<String, Directory> directoriesMap;
}
private DataHolder dataholder;

public void refreshAllFiles()
{
	FileWalker fileWalker = new FileWalker( START_DIRECTORY );
	List<Directory> newDirectories = fileWalker.getDirectories();
	applyProperties( newDirectories );
	Map<String, Directory> newDirectoriesMap = createDirectoriesMap( newDirectories );

	dataholder = new DataHolder( newDirectories, newDirectoriesMap ); 
}

void applyProperties( List<Directory> aDirectories )
{
	for ( Directory directory : aDirectories )
	{
		for ( File file : directory.getFiles() )
		{
			applyProperties( file, directory );
		}
		applyProperties( directory.getSubDirectories() );
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

private Map<String, Directory> createDirectoriesMap( List<Directory> aNewDirectories )
{
	Map<String, Directory> directoriesMap = new HashedMap<>();
	for ( Directory directory : aNewDirectories )
	{
		directoriesMap.put( directory.getName(), directory );
	}
	return directoriesMap;
}

DataHolder getDataHolder()
{
	if ( dataholder == null )
	{
		refreshAllFiles();
	}
	return dataholder;
}

public Directory getFilesPerDirectory( String aDirectoryName )
{
	return getDataHolder().getDirectoriesMap().get( aDirectoryName );
}
}
