package pu.porna.bo.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.RowBounds;

public class TestFilesContainerImpl
{
private FilesContainerImpl filesContainer = new FilesContainerImpl();
@Test
public void testCloneDirectory() throws IOException
{
	List<File> files = createFileList();
	Directory subc = Directory.builder().name( "c" ).build();
	Directory subd = Directory.builder().name( "d" ).build();
	Directory expected = Directory.builder()
		.id( 31415 )
		.name( "pipo" )
		.dateTimeLastModified( LocalDateTime.of( 2022, 5, 14, 12, 34 ) )
		.totalNumberOfFiles( files.size() )
		.parent( Directory.builder()
			.name( "parent" )
			.build() )
		.files( files )
		.subDirectories( List.of( subc, subd ) )
		.build();
	Directory actual = filesContainer.cloneDirectory( expected );
	assertEquals( expected.getId(), actual.getId() );
	assertEquals( expected.getName(), actual.getName() );
	assertEquals( expected.getDateTimeLastModified(), actual.getDateTimeLastModified() );
	assertEquals( expected.getTotalNumberOfFiles(), actual.getTotalNumberOfFiles() );
	assertEquals( expected.getParent(), actual.getParent() );
	assertEquals( expected.getFiles(), actual.getFiles() );
	assertEquals( expected.getSubDirectories(), actual.getSubDirectories() );

	// Check of equality getest wordt
	List<File> fouteFiles = new ArrayList<>( files );
	fouteFiles.set( 0, File.builder().name( "pipo" ).build() );
	assertNotEquals( fouteFiles, actual.getFiles() );
}
@Test
public void testRetainFilesStartingWithP()
{
	List<File> files = createFileList();
	Directory directory = Directory.builder().files( files ).build();
	Directory subSetDirectory = filesContainer.retainFilesStartingWith( "p", directory );
	List<File> subsetFiles = files.subList( 15, 26 );
	assertEquals( subsetFiles, subSetDirectory.getFiles() );
}
@Test
public void testRetainFilesStartingWithPipo()
{
	List<File> files = createFileList();
	Directory directory = Directory.builder().files( files ).build();
	Directory subSetDirectory = filesContainer.retainFilesStartingWith( "pipo", directory );
	List<File> subsetFiles = files.subList( 16, 26 );
	assertEquals( subsetFiles, subSetDirectory.getFiles() );
}
@Test
public void testRetainFilesStartingWithExclamation()
{
	// Exclamation mark is smaller than any letter
	List<File> files = createFileList();
	Directory directory = Directory.builder().files( files ).build();
	Directory subSetDirectory = filesContainer.retainFilesStartingWith( "!", directory );
	List<File> subsetFiles = files.subList( 0, 26 );
	assertEquals( subsetFiles, subSetDirectory.getFiles() );
}

@Test
public void testApplyRowBoundsOffset0Limit10()
{
	List<File> files = createFileList();
	Directory directory = Directory.builder().files( files ).build();
	Directory subSetDirectory = filesContainer.applyRowBounds( directory, new RowBounds( 0, 10 ) );
	List<File> subsetFiles = files.subList( 0, 10 );
	assertEquals( subsetFiles, subSetDirectory.getFiles() );
}
@Test
public void testApplyRowBoundsOffset5Limit2()
{
	List<File> files = createFileList();
	Directory directory = Directory.builder().files( files ).build();
	Directory subSetDirectory = filesContainer.applyRowBounds( directory, new RowBounds( 5, 2 ) );
	List<File> subsetFiles = files.subList( 5, 7 );
	assertEquals( subsetFiles, subSetDirectory.getFiles() );
}
@Test
public void testApplyRowBoundsOffset20Limit100()
{
	List<File> files = createFileList();
	Directory directory = Directory.builder().files( files ).build();
	Directory subSetDirectory = filesContainer.applyRowBounds( directory, new RowBounds( 20, 100 ) );
	List<File> subsetFiles = files.subList( 20, 26 );
	assertEquals( subsetFiles, subSetDirectory.getFiles() );
}

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
