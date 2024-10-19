package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.Property;

@SpringBootTest

// Dit is eigenlijk verboden. Je moet je queries zo maken dat ze alles meteen ophalen
//@Transactional

// Waarvoor was dit?
//@Rollback( false )

// Dit doet het niet. Ik krijg een fout: "No qualifying bean of type 'pu.heavymetal.dal.AuteurRepositoryHelper' available"
// Snap er niks van maar het werkt gewoon niet.
//@DataJpaTest

public class TestDirectoryRepository
{
@Autowired private DirectoryRepository directoryRepository;
@Autowired private FileRepository fileRepository;
@Autowired private PropertyRepositoryHelper propertyRepositoryHelper;

@Test
// Dit is nodig omdat we refereren aan attributen van Directory die nog niet opgehaald zijn. Door eraan te refereren (bijv files)
// moeten ze alsnog worden opgehaald. Standaard krijg je dan een LazyInitializationException. Dit voorkom je door de transactie
// hier naar toe te halen met @Transactional. Echter, je krijgt dan een extra query per onopgehaald attribuut, in dit geval 
// files, subdirectories en properties. In dit geval is dit niet zo erg omdat we maar één issue ophalen, maar bij 100 issues heb je al snel
// 300 extra queries en dat is te veel.
@Transactional
public void testRootDirectory()
{
	List<Directory> directories = directoryRepository.findAll( Sort.by( "id" ));
	assertEquals( 7, directories.size() );
	Directory directory = directories.get( 0 );
	TestDirectoryQueryRepository.checkDirectory( directory );
//	assertEquals( 1, directory.getId() );
//	assertEquals( "Dir A", directory.getName() );
//	assertEquals( "2023-05-14T02:03:04", directory.getDateTimeLastModified().toString() );
//	assertEquals( null, directory.getParent() );
//	
//	List<File> files = directory.getFiles();
//	assertEquals( 3, files.size() );
//	File file = files.get( 0 );
//	assertEquals( 10, file.getId() );
//	assertEquals( "Dir A File A", file.getName() );
//	assertEquals( "2023-05-14T12:05:54", file.getDateTimeLastModified().toString() );
//	assertEquals( 123456, file.getSize() );
//	assertEquals( Kwaliteit.TOP, file.getKwaliteit() );
//	assertEquals( "pipo review", file.getReview() );
//	assertEquals( directory, file.getDirectory() );
//	
//	List<Directory> subdirectories = directory.getSubDirectories();
//	assertEquals( 2, subdirectories.size() );
//	
//	Directory subdirectory1 = subdirectories.get( 0 );
//	assertEquals( 2, subdirectory1.getId() );
//	assertEquals( "Dir A/Dir B", subdirectory1.getName() );
//	assertEquals( "2023-05-15T02:03:04", subdirectory1.getDateTimeLastModified().toString() );
//	assertEquals( directory, subdirectory1.getParent() );
//	
//	Directory subdirectory2 = subdirectories.get( 1 );
//	assertEquals( 5, subdirectory2.getId() );
//	assertEquals( "Dir A/Dir E", subdirectory2.getName() );
//	assertEquals( "2023-05-18T02:03:04", subdirectory2.getDateTimeLastModified().toString() );
//	assertEquals( directory, subdirectory2.getParent() );
//	
//	List<Property> properties = file.getProperties();
//	assertEquals( 3, properties.size() );
//	// De volgorde van de properties is raadselachtig. Effe sortere dus.
//	properties.sort( Comparator.comparing( Property::getId ) );
//	assertEquals( "anal", properties.get( 0 ).getName() );
//	assertEquals( "asian", properties.get( 1 ).getName() );
//	assertEquals( "bdsm", properties.get( 2 ).getName() );
}
//@Test
//@Transactional // Zie hierboven
//public void testIssueWinter1977()
//{
//	List<Issue> issues = directoryRepository.findAll( Sort.by( "jaar", "maand" ));
//	assertEquals( 2, issues.size() );
//	Issue issue = issues.get( 1 );
//	assertEquals( true, issue.isSpecialIssue() );
//	assertEquals( "Winter", issue.getTitel() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( 12, issue.getMaand() );
//	assertEquals( "Olivia de Bernardis", issue.getFrontCover() );
//	assertEquals( "Luis Royo", issue.getBackCover() );
//	assertEquals( "prachtig commentaar", issue.getCommentaar() );
//	
//	List<Strip> strips = issue.getStrips();
//	assertEquals( 1, strips.size() );
//	Strip strip = strips.get( 0 );
//	assertEquals( "Titeltje 1", strip.getTitel() );
//	assertEquals( null, strip.getCommentaar() );
//	assertEquals( "En hier is een review", strip.getReview() );
//	assertEquals( issue, strip.getIssue() );
//	
//	List<Pagina> paginas = strip.getPaginas();
//	assertEquals( 1, paginas.size() );
//	Pagina pagina1 = paginas.get( 0 );
//	assertEquals( 55, pagina1.getStart() );
//	assertEquals( 57, pagina1.getFinish() );
//	assertEquals( strip, pagina1.getStrip() );
//	
//	List<Auteur> auteurs = strip.getAuteurs();
//	assertEquals( 1, auteurs.size() );
//	assertEquals( "Pipo de Clown", auteurs.get( 0 ).getNaam() );
//}
@Test
@Transactional
public void testDeleteDirectory()
{
	Property property1 = propertyRepositoryHelper.createProperty( "Pipo de Cloooooown" );
	Property property2 = propertyRepositoryHelper.createProperty( "Zwiep Zwap" );
	Property property3 = propertyRepositoryHelper.createProperty( "Grasmaaier" );
	
	File file1 = File.builder()
		.name( "Namularis" )
		.review( "Een reviewtje" )
		.properties( List.of( property1, property2, property3 ) )
		.build(); 
	Directory directory = Directory.builder()
		.name( "Kalfje" )
		.files( List.of( file1 ) )
		.build();
	// Hier dus lusjes om het issue in de strips en de strip in de paginas te zetten
	for ( File file : directory.getFiles() )
	{
		file.setDirectory( directory );
	}
	directory = directoryRepository.save( directory );
	
	// Check dat het issue er is
	Directory newDirectory = directoryRepository.getById( directory.getId() );
	assertNotNull( newDirectory );
	assertEquals( directory, newDirectory );
	
	directoryRepository.delete( directory );

	// Check dat her er niet meer is
	Directory deletedDirectory = null;
	try
	{
		deletedDirectory = directoryRepository.getById( directory.getId() );
	}
	catch ( EntityNotFoundException | JpaObjectRetrievalFailureException e )
	{}
	assertNull( deletedDirectory );
}
@Test
@Transactional // Zie boven
public void testFiles()
{
	List<File> files = fileRepository.getAllFilesOrderedByName();
	assertEquals( 15, files.size() );
	assertEquals( "Dir A File A", files.get( 0 ).getName() );
	assertEquals( "Dir A", files.get( 0 ).getDirectory().getName() );
	assertEquals( "Dir A File B", files.get( 1 ).getName() );
	assertEquals( "Dir A", files.get( 1 ).getDirectory().getName() );
}

@Test
public void testGetDirectoryCount()
{
	int directoryCount = directoryRepository.getDirectoryCount();
	assertEquals( 7, directoryCount );
}


}
