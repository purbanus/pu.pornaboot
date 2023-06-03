package pu.porna.dal;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.Kwaliteit;
import pu.porna.bo.Property;
import pu.porna.config.PornaConfig;
import pu.porna.dal.PornaFileDefaultsLader.KwaliteitProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = false )
public class FileWalker extends SimpleFileVisitor<Path>
{
private static final Logger LOG = LoggerFactory.getLogger( FileWalker.class );

private static String [] FOUTE_DIRECTORIES = { "@eaDir" };
private static String [] FOUTE_FILES ={ ".directory", "Thumbs.db" };

private final Path startingDirectory;
private final List<Directory> directories = new ArrayList<>();

private final Map<String, Directory> directoryLookup = new HashMap<>();
private final PornaConfig pornaConfig;

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
public FileWalker( String aStartingDirectory, PornaConfig aPornaConfig )
{
	super();
	startingDirectory = Paths.get( expandHome( aStartingDirectory ) );
	pornaConfig = aPornaConfig;
}
public List<String> getFouteDirectories()
{
	return Arrays.asList( FOUTE_DIRECTORIES );
}
public List<String> getFouteFiles()
{
	List<String> fouteFiles = new ArrayList<>( Arrays.asList( FOUTE_FILES ) );
	fouteFiles.add( getPornaConfig().getPornaFileName() );
	return fouteFiles;
}
public List<Directory> run() throws IOException
{
	Files.walkFileTree( getStartingDirectory(), this );
	return getDirectories();
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
		KwaliteitProperty kwaliteitProperty = PornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( directoryString );
		Kwaliteit kwaliteit = kwaliteitProperty.getKwaliteit() == null ? null :Kwaliteit.fromString( kwaliteitProperty.getKwaliteit() );
		if ( directory == null )
		{
			throw new RuntimeException( "Directory bestaat niet: " + directoryString );
		}
		File file = File.builder()
			.name( path.getFileName().toString() )
			.size( aAttributes.size() )
			.dateTimeLastModified( LocalDateTime.ofInstant( aAttributes.lastModifiedTime().toInstant(), ZoneId.systemDefault() ) )
			.directory( directory )
			// review is er nog niet
			.kwaliteit( kwaliteit )
			.properties( propertyStringsToProperties( kwaliteitProperty.getProperty() ) )
			.pornaConfig( getPornaConfig() )
			.build();
		directory.getFiles().add( file );
	}
	// Je kunt hier altijd CONTINUE retourneren want je wilt doorgaan met de volgende file, ongeacht
	// of je deze verwerkt hebt.
	return CONTINUE;
}
// @@NOG zit ook al in FilesContainer
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

@Override
public FileVisitResult preVisitDirectory( Path aDir, BasicFileAttributes aAttributes ) throws IOException
{
	if ( !isDirectoryOk( aDir ) )
	{
		return FileVisitResult.SKIP_SUBTREE;
	}
	// LOG.debug( "Directory gestart: %s%n", aDir );
	//LOG.debug( "Directory gestart: {}", aDir );
	Path path = aDir.toAbsolutePath();
	//PornaFile pornafile = PornaFile.fromDirectory( path.toString(), getPornaConfig() );
	Directory parentDirectory = getDirectoryLookup().get( path.getParent().toString() );
	Directory newDirectory = Directory.builder()
		.name( path.toString() )
		.dateTimeLastModified( LocalDateTime.ofInstant( aAttributes.lastModifiedTime().toInstant(), ZoneId.systemDefault() ) )
		.pornaConfig( pornaConfig )
		.parent( parentDirectory )
		.build();
	getDirectories().add( newDirectory );
	getDirectoryLookup().put( path.toString(), newDirectory );
	if ( parentDirectory != null )
	{
		parentDirectory.getSubDirectories().add( newDirectory );
	}
	return CONTINUE;
}
// @Override
// public FileVisitResult postVisitDirectory( Path dir, IOException exc )
// {
// return CONTINUE;
// }

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
	if ( !aAttributes.isRegularFile() )
	{
		return false;
	}
	if ( !isFileOrDirectoryOk( aFile ) )
	{
		return false;
	}
	for ( String fouteFile : getFouteFiles() )
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
	if ( !isFileOrDirectoryOk( aDirectory ) )
	{
		return false;
	}
	for ( String fouteDirectory : getFouteDirectories() )
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
	return true;
}
public static LocalDate longToLocalDate( long aLastModified )
{
	return LocalDate.ofInstant( Instant.ofEpochMilli( aLastModified ), TimeZone.getDefault().toZoneId() );
}
}
