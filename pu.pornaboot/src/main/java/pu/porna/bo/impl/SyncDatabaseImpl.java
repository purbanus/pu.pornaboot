package pu.porna.bo.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.SyncDatabase;
import pu.porna.config.PornaConfig;
import pu.porna.dal.DirectoryConverter;
import pu.porna.dal.DirectoryRepository;
import pu.porna.dal.FileRepository;
import pu.porna.dal.FileWalker;
import pu.porna.dal.PropertyRepositoryHelper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Component
@Slf4j
public class SyncDatabaseImpl implements SyncDatabase
{
@Autowired private DirectoryRepository directoryRepository; 
@Autowired private DirectoryConverter directoryConverter; 
@Autowired private FileRepository fileRepository; 
@Autowired private PropertyRepositoryHelper propertyRepositoryHelper; 
@Autowired private PornaConfig pornaConfig; 

@Override
public void syncDatabase()
{
	syncDatabaseFiles();
	syncDirectories();
}

@Transactional
void syncDatabaseFiles()
{
	List<Directory> huidigeDirectories = unpackDirectories( getDirectoryConverter().getAllDirectories() );
	List<Directory> nieuweDirectories = unpackDirectories( leesFsDirectories() );
	List<File> changedFiles = getChangedFiles( huidigeDirectories, nieuweDirectories );
	List<File> remainingFiles = getRemainingFiles( huidigeDirectories, nieuweDirectories );
	List<File> originalRemainingFiles = new ArrayList<>( remainingFiles );
	List<File> movedFiles = lookForFileMoves( changedFiles, remainingFiles );
	List<File> deletedFiles = lookForFileDeletes( changedFiles, remainingFiles );
	updateMovedFiles( movedFiles, originalRemainingFiles, huidigeDirectories );
	deleteDeletedFiles( deletedFiles, huidigeDirectories );
	addNieuweFiles( remainingFiles, huidigeDirectories );
}
@Transactional
void syncDirectories()
{
	deleteEmptyDirectories( getDirectoryConverter().getAllDirectories() );
}
public List<Directory> leesFsDirectories()
{
	FileWalker fileWalker = new FileWalker( getPornaConfig().getStartingDirectory(), getPornaConfig() );
	List<Directory> nieuweDirectories;
	try
	{
		nieuweDirectories = fileWalker.run();
	}
	catch ( IOException e )
	{
		throw new RuntimeException( e );
	}
	return nieuweDirectories;
}
/**
 * @param aHuidigeDirectories
 * @param aNieuweDirectories
 * @return de files in aHuidigeDirectories die niet (meer) voorkomen in aNieuweDirectories. De check is op directory/file, dus dat ze 
 * niet meer voorkomen kan betekenen daat ze verplaatst zijn of verwijderd.
 */
public List<File> getChangedFiles( List<Directory> aHuidigeDirectories, List<Directory> aNieuweDirectories )
{
	List<File> huidigeFileList = createFileList( aHuidigeDirectories );
	List<File> nieuweFileList = createFileList( aNieuweDirectories );
	@SuppressWarnings( "rawtypes" )
	MultiKeyMap nieuweFilesLookup = createFileLookup( nieuweFileList );
	List<File> changedFiles = new ArrayList<>();
	for ( File file : huidigeFileList )
	{
		File nieuweFile = (File) nieuweFilesLookup.get( file.getDirectory().getName(), file.getName() );
		if ( nieuweFile == null )
		{
			changedFiles.add( nieuweFile );
		}
	}
	return changedFiles;
}
/**
 * @param aHuidigeDirectories
 * @param aNieuweDirectories
 * @return de files in aNieuweDirectories die niet (meer) voorkomen in aHuidigeDirectories, m.a.w. die zijn verplaatst of nieuw. 
 * Of anders gezegd, de files in anieuweDirectories die overblijven als je alle files uit aHuidigeDirectories die voorkomen in
 * aNieuweDirectories, wegstreept.
 */
public List<File> getRemainingFiles( List<Directory> aHuidigeDirectories, List<Directory> aNieuweDirectories )
{
	List<File> huidigeFileList = createFileList( aHuidigeDirectories );
	List<File> nieuweFileList = createFileList( aNieuweDirectories );
	@SuppressWarnings( "rawtypes" )
	MultiKeyMap nieuweFilesLookup = createFileLookup( nieuweFileList );
	for ( File file : huidigeFileList )
	{
		File nieuweFile = (File) nieuweFilesLookup.get( file.getDirectory().getName(), file.getName() );
		if ( nieuweFile != null )
		{
			// File komt (nog steeds) voor in FS. Check voor de zekerheid grootte en datum
			if ( ! file.getDateTimeLastModified().equals( nieuweFile.getDateTimeLastModified() ) )
			{
				LOG.warn( "File datum/tijd is gewijzigd! Huidige=" + file.getDateTimeLastModified() + " nieuwe=" + nieuweFile.getDateTimeLastModified() );
				file.setDateTimeLastModified( nieuweFile.getDateTimeLastModified() );
			}
			if ( file.getSize() != nieuweFile.getSize() )
			{
				LOG.warn( "File grootte is gewijzigd! Huidige=" + file.getSize() + " nieuwe=" + nieuweFile.getSize() );
				file.setDateTimeLastModified( nieuweFile.getDateTimeLastModified() );
			}
			// @@NOG File saven
			nieuweFilesLookup.remove( file.getDirectory().getName(), file.getName() );
		}
	}
	List<File> remainingList = new ArrayList<>();
	Collection<File> remainingCollectiom = nieuweFilesLookup.values();
	remainingList.addAll( remainingCollectiom );
	return remainingList;
}
/**
 * @param aChangedFiles de files in huidigeDirectories die niet meer voorkomen in nieuweDirectories, althans niet in de huidige directory.
 * @param aRemainingFiles de files in NieuweDirectories die niet meer voorkomen in HuidigeDirectories, m.a.w. ze zijn verplaatst of nieuw.
 * @return de files in aChangedFiles die voorkomen in aRemainingFiles, maw alleen de directory is gewijzigd, d.w.z. ze zijn verplaatst.
 */
List<File> lookForFileMoves( List<File> aChangedFiles, List<File> aRemainingFiles )
{
	List<File> movedFiles = new ArrayList<>();
	Map<String, File> remainingFileLookup = createFileNameLookup( aRemainingFiles );
	for ( File file : aChangedFiles )
	{
		File movedFile = remainingFileLookup.get( file.getName() );
		if ( movedFile != null )
		{
			movedFiles.add( movedFile );
			aRemainingFiles.remove( movedFile );
		}
	}
	return movedFiles;
}
/**
 * @param aChangedFiles de files in huidigeDirectories die niet meer voorkomen in nieuweDirectories, althans niet in de huidige directory.
 * @param aRemainingFiles de files in NieuweDirectories die niet meer voorkomen in HuidigeDirectories, m.a.w. ze zijn verplaatst of nieuw.
 * @return de files in aChangedFiles die NIET meer voorkomen in aRemainingFiles, maw ze zijn verwijderd..
 */
List<File> lookForFileDeletes( List<File> aChangedFiles, List<File> aRemainingFiles )
{
	List<File> deletedFiles = new ArrayList<>();
	Map<String, File> remainingFileLookup = createFileNameLookup( aRemainingFiles );
	for ( File file : aChangedFiles )
	{
		File deletedFile = remainingFileLookup.get( file.getName() );
		if ( deletedFile == null )
		{
			deletedFiles.add( deletedFile );
			aRemainingFiles.remove( deletedFile );
		}
	}
	return deletedFiles;
}
/**
 * Update de moved files naar huidige directories
 * @param aMovedFiles De files in huidigeDirectories die verplaatst zijn naar een andere directory
 * @param aOriginalRemaingFiles de files in aNieuweDirectories die niet (meer) voorkomen in aHuidigeDirectories, m.a.w. die zijn verplaatst of nieuw.
 * @param aHuidigeDirectories De huidigeDirectories
 */
void updateMovedFiles( List<File> aMovedFiles, List<File> aOriginalRemainingFiles, List<Directory> aHuidigeDirectories )
{
	Map<String, Directory> huidigeDirectoryLookup = createDirectoryLookup( aHuidigeDirectories );
	Map<String, File> remainingFileLookup = createFileNameLookup( aOriginalRemainingFiles );
	List<File> filesAlreadyMoved = new ArrayList<>();
	for ( File file : aMovedFiles )
	{
		if ( ! filesAlreadyMoved.contains( file ) )
		{
			File nieuweFile = remainingFileLookup.get( file.getName() );
			// Check of de nieuwe directory bestaat. Als die niet bestaat hoeven we alleen de nieuwe directory te saven, alle relaties
			// zijn dan goed en aanwezig. 
			if ( ! huidigeDirectoryLookup.containsKey( nieuweFile.getDirectory().getName() ) )
			{
				Directory directory = nieuweFile.getDirectory();
				getDirectoryRepository().save( directory );
				// Nu de files uit aMovedFiles weggooien want die bestaan nu allemaal
				filesAlreadyMoved.addAll( directory.getFiles() );
			}
			else
			{
				// aMovedFiles bestaat uit files in de huidige directory. Daar moet je de file weghalen uit de filesList
				Directory huidigeDirectory = file.getDirectory();
				huidigeDirectory.getFiles().remove( file );
				getDirectoryRepository().save( huidigeDirectory );
				// Nu toevoegen aan de nieuwe directory, die al bestaat in huidigeDirectories
				Directory nieuweDirectory = huidigeDirectoryLookup.get( nieuweFile.getDirectory().getName() );
				nieuweDirectory.getFiles().add( file );
				file.setDirectory( nieuweDirectory );
				getDirectoryRepository().save( nieuweDirectory );
				filesAlreadyMoved.add( file );
			}
		}
	}
}
/**
 * Verwijder de verwijderde files, d.w.z die niet meer voorkomen in nieuwe directories
 * @param aDeletedFiles de files in huidige directories die NIET meer voorkomen in nieuwe directories, maw ze zijn verwijderd.
 * @param aHuidigeDirectories De huidige directories
 */
void deleteDeletedFiles( List<File> aDeletedFiles, List<Directory> aHuidigeDirectories )
{
	@SuppressWarnings( "unused" )
	Map<String, Directory> huidigeDirectoryLookup = createDirectoryLookup( aHuidigeDirectories );
	for ( File file : aDeletedFiles )
	{
//		Directory huidigeDirectory = huidigeDirectoryLookup.get( file.getDirectory().getName() );
//		huidigeDirectory.getFiles().remove( file );
//		getDirectoryRepository().save( huidigeDirectory );
		getFileRepository().delete( file );
	}
}
void addNieuweFiles( List<File> aRemainingFiles, List<Directory> aHuidigeDirectories )
{
	Map<String, Directory> huidigeDirectoryLookup = createDirectoryLookup( aHuidigeDirectories );
	List<File> filesAlreadyAdded = new ArrayList<>();
	for ( File file : aRemainingFiles )
	{
		if ( ! filesAlreadyAdded.contains( file ) )
		{
			// Check of de nieuwe directory bestaat. Als die niet bestaat hoeven we alleen de nieuwe directory te saven, alle relaties
			// zijn dan goed en aanwezig. 
			if ( ! huidigeDirectoryLookup.containsKey( file.getDirectory().getName() ) )
			{
				Directory directory = file.getDirectory();
				// @@NOG Wat als de parent niet bestaat?
				getDirectoryRepository().save( directory );
				// Nu de files uit aRemainingFiles weggooien want die bestaan nu allemaal
				filesAlreadyAdded.addAll( directory.getFiles() );
			}
			else
			{
				Directory huidigeDirectory = huidigeDirectoryLookup.get( file.getDirectory().getName() );
				huidigeDirectory.getFiles().add( file );
				file.setDirectory( huidigeDirectory );
//				for ( File directoryFile : huidigeDirectory.getFiles() )
//				{
//					List<Property> newProperties = new ArrayList<>();
//					for ( Property property : directoryFile.getProperties() )
//					{
//						newProperties.add( getPropertyRepositoryHelper().createProperty( property.getName() ) );
//					}
//					directoryFile.setProperties( newProperties );
//				}
//				getDirectoryRepository().save( huidigeDirectory );
				getFileRepository().save( file );
				filesAlreadyAdded.add( file );
			}
		}
	}
}
int deleteEmptyDirectories( List<Directory> aDirectories )
{
	int directoriesDeleted = 0;
	for ( Directory directory : aDirectories )
	{
		if ( directory.getFiles().size() == 0 && directory.getSubDirectories().size() == 0 )
		{
			getDirectoryRepository().delete( directory );
			directoriesDeleted++;
		}
	}
	return directoriesDeleted;
}


//----------------------------------------------------------------------------------------------------------------------------------
// Utilities
//----------------------------------------------------------------------------------------------------------------------------------
List<Directory> unpackDirectories( List<Directory> aDirectories )
{
	List<Directory> unpackedDirectories = new ArrayList<>();
	for ( Directory directory : aDirectories )
	{
		unpackedDirectories.add( directory );
		unpackedDirectories.addAll( unpackDirectories( directory.getSubDirectories() ) );
	}
	return unpackedDirectories;
}
List<File> createFileList( List<Directory> aDirectories )
{
	List<File> fileList = new ArrayList<>();
	for ( Directory directory : aDirectories )
	{
		fileList.addAll( directory.getFiles() );
	}
	return fileList;
}
@SuppressWarnings( {
    "unchecked", "rawtypes"
} )
MultiKeyMap createFileLookup( List<File> aFiles )
{
	MultiKeyMap nieuweFilesLookup = new MultiKeyMap<>();
	for ( File file : aFiles )
	{
		nieuweFilesLookup.put( file.getDirectory().getName(), file.getName(), file );
	}
	return nieuweFilesLookup;
}
Map<String, File> createFileNameLookup( List<File> aFiles )
{
	Map<String, File> fileNameLookup = new HashMap<>();
	for ( File file : aFiles )
	{
		fileNameLookup.put( file.getDirectory().getName(), file );
	}
	return fileNameLookup;
}
Map<String, Directory> createDirectoryLookup( List<Directory> aNieuweDirectories )
{
	Map<String, Directory> directoryLookup = new HashMap<>();
	for ( Directory directory : aNieuweDirectories )
	{
		directoryLookup.put( directory.getName(), directory );
	}
	return directoryLookup;
}

}
