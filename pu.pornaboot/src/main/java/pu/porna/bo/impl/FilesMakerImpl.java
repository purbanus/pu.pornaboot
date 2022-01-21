package pu.porna.bo.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pu.porna.bo.FilesDocument;
import pu.porna.bo.FilesMaker;
import pu.porna.bo.RowBounds;
import pu.porna.dal.Directory;
import pu.porna.dal.File;

@Component
public class FilesMakerImpl implements FilesMaker
{
private static final Logger LOG = LoggerFactory.getLogger(FilesMakerImpl.class);

private static final String [] FOUTE_DIRECTORIES = { "@eaDir" };
private static final String [] FOUTE_FILES = { ".directory", "Thumbs.db" };

@Value( "${porna.starting-directory}" )  private String startingDirectory;

public static String expandHome( String aPath )
{
	if ( aPath.startsWith( "~" + java.io.File.separator ) )
	{
		aPath = System.getProperty( "user.home" ) + aPath.substring( 1 );
	}
	else if ( aPath.startsWith( "~" ) )
	{
		// here you can implement reading homedir of other users if you care
		throw new UnsupportedOperationException( "Home dir expansion not implemented for explicit usernames" );
	}
	return aPath;
}

public String getStartingDirectory()
{
	return startingDirectory;
}

@Override
public FilesDocument getFiles( String aDirectory, String aFromFile, RowBounds aRowBounds ) throws IOException
{
	Directory directory = getDirectory( aDirectory );
	
	List<String> subDirectories = new ArrayList<>();
	List<File> files = new ArrayList<>();
	String fromFile = aFromFile == null ? "" : aFromFile;
	java.io.File [] rawFiles = fileSystemDirectory.listFiles();
    for ( java.io.File file : rawFiles )
    {
    	if ( file.isDirectory() )
        {
    		if ( isDirectoryOk( file ) )
        	{
    			subDirectories.add( StringUtils.isEmpty( directory ) ? file.getName() : directory + java.io.File.separator + file.getName() );
        	}
        }
    	else
    	{
    		if ( isFileOk( file ) )
    		{
	    		if ( file.getName().compareToIgnoreCase( fromFile ) >= 0 )
	    		{
		    		LocalDateTime modifiedTime = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		    		File fileEntry = File.builder()
		    			.name( file.getName() )
		    			.size( file.length() )
		    			.dateTimeLastModified( modifiedTime )
		    			.build();
		    		files.add( fileEntry );
	    		}
    		}
        }
    }
    subDirectories.sort( String.CASE_INSENSITIVE_ORDER );
    files.sort( (f1, f2) -> f1.getName().compareToIgnoreCase( f2.getName() ) );
//    files.sort( Comparator.comparing( PornaFile::getName, String.CASE_INSENSITIVE_ORDER ) );
//    files = files.stream()
//    	.sorted( (f1, f2) -> f1.getName().compareToIgnoreCase( f2.getName() ) )
//    	.collect( Collectors.toList() );
//    files = files.stream()
//    	.sorted( Comparator.comparing( PornaFile::getName, String.CASE_INSENSITIVE_ORDER ) )
//    	.collect( Collectors.toList() );
    int topIndex = Math.min( aRowBounds.getOffset() + aRowBounds.getLimit(), files.size() );
    LOG.debug( "Rowbounds for slice = " + aRowBounds.getOffset() + ", " + topIndex );
    files = files.subList( aRowBounds.getOffset(), topIndex );
	return new FilesDocument( directory, subDirectories, files );
}

Directory getDirectory( String aDirectory )
{
	if ( aDirectory == null )
	{
		return null;
	}
	String directoryName = getStartingDirectory() + ( aDirectory == null ? "" : java.io.File.separator + aDirectory );
	java.io.File fileSystemDirectory = new java.io.File( directoryName );
	Path directoryPath = Paths.get( aDirectory );
	LocalDateTime modifiedTime = Instant.ofEpochMilli( fileSystemDirectory.lastModified() ).atZone( ZoneId.systemDefault() ).toLocalDateTime();
	return Directory.builder()
		.name( directoryName )
		.dateTimeLastModified( modifiedTime )
		.parent( getParentDirectoryOf( directoryPath ) )
		.pornaFile( null )
		.build();
}

/**
 *  Restrict directories to our subsystem of directories; don't go to the parent of /home/.../Videos
 * @param aDirectory
 * @return the parentdirectory of aDirectory, relative to our startingDirectory
 * @throws IOException 
 */
Directory getParentDirectoryOf( Path aDirectoryPath )
{
	Path directoryPath = aDirectoryPath.getParent();
	String directoryName = directoryPath.toString();
	if ( directoryName.equals( getStartingDirectory() ) )
	{
		return null;
	}
//	if ( parentDirectory.startsWith( getStartingDirectory() ) )
//	{
//		parentDirectory = parentDirectory.substring( getStartingDirectory().length() );
//		if ( parentDirectory.startsWith( java.io.File.separator ) )
//		{
//			parentDirectory = parentDirectory.substring( 1 );
//		}
//	}
//	return parentDirectory;
	java.io.File fileSystemDirectory = new java.io.File( directoryName );
	LocalDateTime modifiedTime = Instant.ofEpochMilli( fileSystemDirectory.lastModified() ).atZone( ZoneId.systemDefault() ).toLocalDateTime();
	return Directory.builder()
		.name( directoryName )
		.dateTimeLastModified( modifiedTime )
		.parent( null ) // Dit gaat maar over 1 directory
		.pornaFile( null )
		.build();
}

@SuppressWarnings( "static-method" )
private Map<String, String> getProperties( String aDirectory )
{
	// @@NOG Auto-generated method stub
	return new HashMap<>();
}

public boolean isFileOk( java.io.File aFile )
{
	if ( ! isFileOrDirectoryOk( aFile ) )
	{
		return false;
	}
	if ( ! aFile.isFile() )
	{
		return false;
	}
	for ( String fouteFile : FOUTE_FILES )
	{
	    if ( aFile.getName().equals( fouteFile ) )
	    {
	    	return false;
	    }
	}
	return true;
}
public boolean isDirectoryOk( java.io.File aDirectory )
{
	if ( ! isFileOrDirectoryOk( aDirectory ) )
	{
		return false;
	}
	for ( String fouteDirectory : FOUTE_DIRECTORIES )
	{
	    if ( aDirectory.getName().equals( fouteDirectory ) )
	    {
	    	return false;
	    }
	}
    return true;
}
@SuppressWarnings( "static-method" )
private boolean isFileOrDirectoryOk( java.io.File aFile )
{
	return aFile.getAbsolutePath() != null;
}
}
