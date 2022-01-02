package pu.porna.bo.impl;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pu.porna.bo.oud.DatabaseUpdater;
import pu.porna.bo.oud.Directory;
import pu.porna.bo.oud.File;

public class FileWalker extends SimpleFileVisitor<Path>
{
private static final Logger LOG = LoggerFactory.getLogger( DatabaseUpdater.class );
private static String [] FOUTE_DIRECTORIES = { "@eaDir" };
private static String [] FOUTE_FILES = { ".directory", "Thumbs.db" };
private final List<Directory> directories = new ArrayList<>();
private final Map<String, Directory> directoryLookup = new HashMap<>();
private final PathMatcher matcher;

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
public static List<Directory> walkDeFiles( String aStartingDirectory, String aSearchPattern ) throws IOException
{
	Path startingDir = Paths.get( expandHome( aStartingDirectory ) );
	String pattern = expandHome( aSearchPattern );
	
	FileWalker walker = new FileWalker( pattern );
	Files.walkFileTree( startingDir, walker );
	return walker.getDirectories();
}

@SuppressWarnings( "resource" )
public FileWalker( String aPattern )
{
	super();
    matcher = FileSystems.getDefault().getPathMatcher( "glob:" + expandHome( aPattern ) );
}
public List<Directory> getDirectories()
{
	return directories;
}
public Map<String, Directory> getDirectoryLookup()
{
	return directoryLookup;
}
public PathMatcher getMatcher()
{
	return matcher;
}
@Override
public FileVisitResult visitFile( Path aFile, BasicFileAttributes aAttributes )
{
	if ( isFileOk( aFile, aAttributes ) )
	{
		Path path = aFile.toAbsolutePath();
		Path directoryPath = path.getParent();
		String directoryString = directoryPath.toString();
		Directory directory = getDirectoryLookup().get( directoryString );
		if ( directory == null )
		{
			throw new RuntimeException( "Directory bestaat niet: " + directoryString );
		}
       	File file = new File( -31415, path.getFileName().toString(), directory );
       	directory.getFiles().add( file );
	}
	// Je kunt hier altijd CONTINUE retourneren want je wilt doorgaan met de volgende file, ongeacht
	// of je deze verwerkt hebt.
	return CONTINUE;
}

@Override
public FileVisitResult preVisitDirectory( Path aDir, BasicFileAttributes aAttributes )
{
	if ( ! isDirectoryOk( aDir ) )
	{
		return FileVisitResult.SKIP_SUBTREE;
	}
	//LOG.debug( "Directory gestart: %s%n", aDir );
	LOG.debug( "Directory gestart: {}", aDir );
	Path path = aDir.toAbsolutePath();
	Directory parentDirectory = getDirectoryLookup().get( path.getParent().toString() );
	Directory newDirectory = new Directory( 1, path.toString(), parentDirectory );
	getDirectories().add( newDirectory );
	getDirectoryLookup().put( path.toString(), newDirectory );
	if ( parentDirectory != null )
	{
		parentDirectory.getSubDirectories().add(  newDirectory );
	}
	return CONTINUE;
}
//@Override
//public FileVisitResult postVisitDirectory( Path dir, IOException exc )
//{
//	return CONTINUE;
//}

// If there is some error accessing the file, let the user know.
// If you don't override this method and an error occurs, an IOException is thrown.
@Override
public FileVisitResult visitFileFailed( Path file, IOException exc )
{
	LOG.error( "Fout in file " + file.toString(), exc );
	return CONTINUE;
}
public boolean isFileOk( Path aFile, BasicFileAttributes aAttributes )
{
	if ( ! aAttributes.isRegularFile() )
	{
		return false;
	}
	if ( ! isFileOrDirectoryOk( aFile ) )
	{
		return false;
	}
	for ( String fouteFile : FOUTE_FILES )
	{
	    if ( aFile.endsWith( fouteFile ) )
	    {
	    	return false;
	    }
	}
	return true;
}
public boolean isDirectoryOk( Path aDirectory )
{
	if ( ! isFileOrDirectoryOk( aDirectory ) )
	{
		return false;
	}
	for ( String fouteDirectory : FOUTE_DIRECTORIES )
	{
	    if ( aDirectory.endsWith( fouteDirectory ) )
	    {
	    	return false;
	    }
	}
    return true;
}
private boolean isFileOrDirectoryOk( Path aPath )
{
	Path path = aPath.toAbsolutePath();
    if ( path == null )
    {
    	return false;
    }
    if ( ! getMatcher().matches( path ) )
    {
    	return false;
    }
    return true;
}
}
