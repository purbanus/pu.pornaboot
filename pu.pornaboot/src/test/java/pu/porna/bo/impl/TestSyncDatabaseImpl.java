package pu.porna.bo.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.dal.DirectoryConverter;
import pu.porna.dal.DirectoryRepository;
import pu.porna.dal.FileRepository;

import lombok.Data;

@Data
@SpringBootTest
public class TestSyncDatabaseImpl
{
@Autowired DirectoryRepository directoryRepository;
@Autowired DirectoryConverter directoryConverter;
@Autowired FileRepository fileRepository;
@Autowired SyncDatabaseImpl syncDatabaseImpl;

@SuppressWarnings( "null" )
@Test
@Transactional
public void testDeleteDeletedFiles() throws IOException
{
	List<Directory> huidigeDirectories = getDirectoryConverter().getAllDirectories();
	huidigeDirectories = getSyncDatabaseImpl().unpackDirectories( huidigeDirectories );
	
	List<File> huidigeFiles = getFileRepository().findAll( Sort.by( "id" ) );
	List<File> filesToBeDeleted = new ArrayList<>();
	filesToBeDeleted.add( huidigeFiles.get( 0 ) );
	filesToBeDeleted.add( huidigeFiles.get( 6 ) );
	filesToBeDeleted.add( huidigeFiles.get( 7 ) );
	getSyncDatabaseImpl().deleteDeletedFiles( filesToBeDeleted, huidigeDirectories );

	// Check dat de files er niet meer zijn
	File file = null;
	try
	{
		file = getFileRepository().getById( filesToBeDeleted.get( 0 ).getId() );	
	}
	catch ( EntityNotFoundException | JpaObjectRetrievalFailureException e )
	{}
	assertNull( file );

	file = null;
	try
	{
		file = getFileRepository().getById( filesToBeDeleted.get( 1 ).getId() );	
	}
	catch ( EntityNotFoundException | JpaObjectRetrievalFailureException e )
	{}
	assertNull( file );

	file = null;
	try
	{
		file = getFileRepository().getById( filesToBeDeleted.get( 2 ).getId() );	
	}
	catch ( EntityNotFoundException | JpaObjectRetrievalFailureException e )
	{}
	assertNull( file );
}
@Test
@Transactional
public void testAddNieuweFiles() throws IOException
{
	List<Directory> huidigeDirectories = getDirectoryConverter().getAllDirectories();
	huidigeDirectories = getSyncDatabaseImpl().unpackDirectories( huidigeDirectories );
	int aantalHuidigeDirectories = huidigeDirectories.size();
	
	List<File> filesToBeAdded = new ArrayList<>();
	filesToBeAdded.add( File.builder()
		.name( "pipo nieuw" )
		.directory( huidigeDirectories.get( 0 ) )
		.build()
		);
	Directory nieuweDirectory = Directory.builder()
		.name( "dikke dir nieuw" )
		.parent( huidigeDirectories.get( 1 ) )
		.build()
		;
	File nieuweFile = File.builder()
		.name( "pipo nieuw" )
		.directory( nieuweDirectory )
		.build()
		;
	nieuweDirectory.getFiles().add( nieuweFile );
	filesToBeAdded.add( nieuweFile );

	getSyncDatabaseImpl().addNieuweFiles( filesToBeAdded, huidigeDirectories );

	// Check dat de nieuwe files er  zijn
	@SuppressWarnings( "unused" )
	File file = getFileRepository().getById( filesToBeAdded.get( 0 ).getId() );
	file = getFileRepository().getById( filesToBeAdded.get( 1 ).getId() );

	// Check dat de eerste file is toegevoegd aan directory 0
	@SuppressWarnings( "unused" )
	Directory directory = getDirectoryRepository().getById( huidigeDirectories.get( 0 ).getId() );
		
	// Check dat er een directory is toegevoegd 
	List<Directory> aangepasteDirectories = getDirectoryConverter().getAllDirectories();
	aangepasteDirectories = getSyncDatabaseImpl().unpackDirectories( huidigeDirectories );
	assertEquals( aantalHuidigeDirectories + 1, aangepasteDirectories.size() );
}
@Test
@Transactional
public void testDeleteEmptyDirectories() throws IOException
{
	List<Directory> directories = getDirectoryConverter().getAllDirectories();
	Directory emptyDirectory = Directory.builder()
		.name( "empty" )
		.dateTimeLastModified( LocalDateTime.of( 2022, 5, 14, 12, 34 ) )
		.totalNumberOfFiles( 0 )
		.parent( directories.get( 0 ) )
		.build();
	getDirectoryRepository().save( emptyDirectory );
	
	// Check dat de directory er is
	Directory directory = getDirectoryRepository().getById( emptyDirectory.getId() );	
	assertNotNull( directory );
	
	directories = getDirectoryConverter().getAllDirectories();
	directories = getSyncDatabaseImpl().unpackDirectories( directories );
	int deletedDirectories = getSyncDatabaseImpl().deleteEmptyDirectories( directories );
	assertEquals( 3, deletedDirectories );

	// Check dat de directory er niet meer is
	directory = null;
	try
	{
		directory = getDirectoryRepository().getById( emptyDirectory.getId() );	
	}
	catch ( EntityNotFoundException | JpaObjectRetrievalFailureException e )
	{}
	assertNull( directory );
}

@Test
public void testUnpackDirectories() throws IOException
{
	List<Directory> directories = getDirectoryConverter().getAllDirectories();
	directories = getSyncDatabaseImpl().unpackDirectories( directories );
	assertEquals( 7, directories.size() );
	assertEquals( "Dir A", directories.get( 0 ).getName() );
	assertEquals( "Dir A/Dir B", directories.get( 1 ).getName() );
	assertEquals( "Dir A/Dir B/Dir C", directories.get( 2 ).getName() );
	assertEquals( "Dir A/Dir B/Dir D", directories.get( 3 ).getName() );
	assertEquals( "Dir A/Dir E", directories.get( 4 ).getName() );
	assertEquals( "Dir A/Dir E/Dir F", directories.get( 5 ).getName() );
	assertEquals( "Dir A/Dir E/Dir G", directories.get( 6 ).getName() );
}
@SuppressWarnings( "unused" )
private List<File> createFileList()
{
	List<File> files = new ArrayList<>();
	for ( char x = 'a'; x <= 'z'; x++ )
	{
		files.add( File.builder().name( String.valueOf( x ) ).build() );
	}
	return files;
}
}
