package pu.porna.bo.impl;

import java.io.File;
import java.io.IOException;
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
import pu.porna.bo.FileEntry;

@Component
public class FilesMakerImpl implements FilesMaker
{
private static final Logger LOG = LoggerFactory.getLogger(FilesMakerImpl.class);

private static final String [] FOUTE_DIRECTORIES = { "@eaDir" };
private static final String [] FOUTE_FILES = { ".directory", "Thumbs.db" };

@Value( "${porna.starting-directory}" )  private String startingDirectory;

public static String expandHome( String aPath )
{
	if ( aPath.startsWith( "~" + File.separator ) )
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
	String directory = aDirectory == null ? "" : aDirectory;
	Map<String, String> propertiesLookup = getProperties( aDirectory );
	
	List<String> subDirectories = new ArrayList<>();
	List<FileEntry> files = new ArrayList<>();
	File fileSystemDirectory = new File( getStartingDirectory() + ( aDirectory == null ? "" : File.separator + aDirectory ) );
	String fromFile = aFromFile == null ? "" : aFromFile;
	String parentDirectory = getParentDirectoryOf( fileSystemDirectory );
	File [] rawFiles = fileSystemDirectory.listFiles();
    for ( File file : rawFiles )
    {
    	if ( file.isDirectory() )
        {
    		if ( isDirectoryOk( file ) )
        	{
    			subDirectories.add( StringUtils.isEmpty( directory ) ? file.getName() : directory + File.separator + file.getName() );
        	}
        }
    	else
    	{
    		if ( isFileOk( file ) )
    		{
	    		if ( file.getName().compareToIgnoreCase( fromFile ) >= 0 )
	    		{
		    		LocalDateTime modifiedTime = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		    		String remark = propertiesLookup.get( file.getName() ) == null ? "" : propertiesLookup.get( file.getName() );
		    		FileEntry fileEntry = new FileEntry( file.getName(), file.length(), modifiedTime, remark );
		    		files.add( fileEntry );
	    		}
    		}
        }
    }
    int numberOfFiles = files.size();
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
	return new FilesDocument( directory, parentDirectory, subDirectories, files, numberOfFiles );
}

/**
 *  Restrict directories to our subsystem of directories; don't go to the parent of /home/.../Videos
 * @param aDirectory
 * @return the parentdirectory of aDirectory, relative to our startingDirectory
 * @throws IOException 
 */
String getParentDirectoryOf( File aDirectory ) throws IOException
{
	String directory = aDirectory.getCanonicalPath();
	if ( directory.equals( getStartingDirectory() ) )
	{
		return null;
	}
	String parentDirectory = new File( aDirectory.getParent() ).getCanonicalPath();
	if ( parentDirectory.startsWith( getStartingDirectory() ) )
	{
		parentDirectory = parentDirectory.substring( getStartingDirectory().length() );
		if ( parentDirectory.startsWith( File.separator ) )
		{
			parentDirectory = parentDirectory.substring( 1 );
		}
	}
	return parentDirectory;
}

@SuppressWarnings( "static-method" )
private Map<String, String> getProperties( String aDirectory )
{
	// @@NOG Auto-generated method stub
	return new HashMap<>();
}

public boolean isFileOk( File aFile )
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
public boolean isDirectoryOk( File aDirectory )
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
private boolean isFileOrDirectoryOk( File aFile )
{
	return aFile.getAbsolutePath() != null;
}
}
