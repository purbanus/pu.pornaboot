package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;

import javax.persistence.Tuple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.Kwaliteit;
import pu.porna.bo.Property;

@SpringBootTest
public class TestDirectoryQueryRepository
{
@Autowired private DirectoryQueryRepository repository;
@Autowired private DirectoryConverter converter;

@Test
public void testGetDirectoriesAsTuples()
{
	List<Tuple> tuples = repository.getDirectoryTuplesById( 1 );
	assertEquals( 14, tuples.size() );
	Tuple tuple = tuples.get( 0 );
	assertNotNull( tuple );
}
@Test
public void testConvertTuplesToFlatDocuments()
{
	List<Tuple> tuples = repository.getDirectoryTuplesById( 1 );
	List<FlatDocument> documents = converter.convertTuplesToFlatDocuments( tuples );
	assertEquals( 14, documents.size() );
}
@Test
public void testGetDirectoryById()
{
	Directory directory = converter.getDirectory( 1 );

	checkDirectory( directory );

}
//@Test
//public void testGetIssuesAsFlatDocuments()
//{
//	List<Tuple> tuples = repository.getIssueTuplesByYear( 1977 );
//	List<FlatDocument> issues = converter.convertTuplesToFlatDocuments( tuples );
//	assertEquals( 7, issues.size() );
//	
//	FlatDocument issue;
//	
//	/* We hebben hier nisschien een probleem. Er zijn hier 6 issues met id 2. Dat issue heeft namelijk 
//	 * twee paginas en 3 auteurs. Echter, de volgorde van de auteurs is niet hetzelfde: 
//	 * - Bij de eerste 3 is het Mamaloe, Pipo en Dikke (id's 2, 3, 1)
//	 * - Bij de laatste 3 is het Pipo, Dikke en Mamaloe (id's 1, 3, 2)
//	 * - In H2 is de volgorde weer anders
//	 * ==> Opgelost door ze in de query te sorteren
//	 */
//	issue = issues.get( 0 );
//	assertEquals( 2, issue.getIssueId() );
//	assertEquals( false, issue.isSpecialIssue() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( "March", issue.getIssueTitel() );
//	assertEquals( "Pipo cover", issue.getFrontCover() );
//	assertEquals( "Koeie back cover", issue.getBackCover() );
//	assertEquals( "", issue.getIssueCommentaar() );
//	assertEquals( "Titeltje 2", issue.getStripTitel() );
//	assertEquals( 58, issue.getPaginaStart() );
//	assertEquals( null, issue.getPaginaFinish() );
//	assertEquals( "De dikke deur", issue.getAuteur() );
//
//	issue = issues.get( 1 );
//	assertEquals( 2, issue.getIssueId() );
//	assertEquals( false, issue.isSpecialIssue() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( "March", issue.getIssueTitel() );
//	assertEquals( "Pipo cover", issue.getFrontCover() );
//	assertEquals( "Koeie back cover", issue.getBackCover() );
//	assertEquals( "", issue.getIssueCommentaar() );
//	assertEquals( "Titeltje 2", issue.getStripTitel() );
//	assertEquals( 58, issue.getPaginaStart() );
//	assertEquals( null, issue.getPaginaFinish() );
//	assertEquals( "Mamaloe de koe", issue.getAuteur() );
//	
//	issue = issues.get( 2 );
//	assertEquals( 2, issue.getIssueId() );
//	assertEquals( false, issue.isSpecialIssue() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( "March", issue.getIssueTitel() );
//	assertEquals( "Pipo cover", issue.getFrontCover() );
//	assertEquals( "Koeie back cover", issue.getBackCover() );
//	assertEquals( "", issue.getIssueCommentaar() );
//	assertEquals( "Titeltje 2", issue.getStripTitel() );
//	assertEquals( 58, issue.getPaginaStart() );
//	assertEquals( null, issue.getPaginaFinish() );
//	assertEquals( "Pipo de Clown", issue.getAuteur() );
//	
//	issue = issues.get( 3 );
//	assertEquals( 2, issue.getIssueId() );
//	assertEquals( false, issue.isSpecialIssue() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( "March", issue.getIssueTitel() );
//	assertEquals( "Pipo cover", issue.getFrontCover() );
//	assertEquals( "Koeie back cover", issue.getBackCover() );
//	assertEquals( "", issue.getIssueCommentaar() );
//	assertEquals( "Titeltje 2", issue.getStripTitel() );
//	assertEquals( 69, issue.getPaginaStart() );
//	assertEquals( 70, issue.getPaginaFinish() );
//	assertEquals( "De dikke deur", issue.getAuteur() );
//
//	issue = issues.get( 4 );
//	assertEquals( 2, issue.getIssueId() );
//	assertEquals( false, issue.isSpecialIssue() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( "March", issue.getIssueTitel() );
//	assertEquals( "Pipo cover", issue.getFrontCover() );
//	assertEquals( "Koeie back cover", issue.getBackCover() );
//	assertEquals( "", issue.getIssueCommentaar() );
//	assertEquals( "Titeltje 2", issue.getStripTitel() );
//	assertEquals( 69, issue.getPaginaStart() );
//	assertEquals( 70, issue.getPaginaFinish() );
//	assertEquals( "Mamaloe de koe", issue.getAuteur() );
//	
//	issue = issues.get( 5 );
//	assertEquals( 2, issue.getIssueId() );
//	assertEquals( false, issue.isSpecialIssue() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( "March", issue.getIssueTitel() );
//	assertEquals( "Pipo cover", issue.getFrontCover() );
//	assertEquals( "Koeie back cover", issue.getBackCover() );
//	assertEquals( "", issue.getIssueCommentaar() );
//	assertEquals( "Titeltje 2", issue.getStripTitel() );
//	assertEquals( 69, issue.getPaginaStart() );
//	assertEquals( 70, issue.getPaginaFinish() );
//	assertEquals( "Pipo de Clown", issue.getAuteur() );
//	
//	issue = issues.get( 6 );
//	assertEquals( 1, issue.getIssueId() );
//	assertEquals( true, issue.isSpecialIssue() );
//	assertEquals( 1977, issue.getJaar() );
//	assertEquals( "Winter", issue.getIssueTitel() );
//	assertEquals( "Olivia de Bernardis", issue.getFrontCover() );
//	assertEquals( "Luis Royo", issue.getBackCover() );
//	assertEquals( "prachtig commentaar", issue.getIssueCommentaar() );
//	assertEquals( "Titeltje 1", issue.getStripTitel() );
//	assertEquals( 55, issue.getPaginaStart() );
//	assertEquals( 57, issue.getPaginaFinish() );
//	assertEquals( "Pipo de Clown", issue.getAuteur() );
//	
//}
//@Test
//public void testGetAuteurAsFlatDocuments()
//{
//	List<Tuple> tuples = repository.getAuteurTuplesById( 1 );
//	List<FlatDocument> auteurList = converter.convertTuplesToFlatDocuments( tuples );
//	assertEquals( 3, auteurList.size() );
//	
//	FlatDocument auteurDocument;
//	
//	/// Auteur 1 komt drie keer voor: issue 2, pagina 2, issue 2, pagina 3 en issue 1, pagina 1
//	assertEquals( 3, auteurList.size() );
//	
//	auteurDocument = auteurList.get( 0 );
//	assertEquals( 2, auteurDocument.getIssueId() );
//	assertEquals( false, auteurDocument.isSpecialIssue() );
//	assertEquals( 1977, auteurDocument.getJaar() );
//	assertEquals( "March", auteurDocument.getIssueTitel() );
//	assertEquals( "Pipo cover", auteurDocument.getFrontCover() );
//	assertEquals( "Koeie back cover", auteurDocument.getBackCover() );
//	assertEquals( "Titeltje 2", auteurDocument.getStripTitel() );
//	assertEquals( "Dit is wat commentaar", auteurDocument.getStripCommentaar() );
//	assertEquals( null, auteurDocument.getStripReview() );
//	assertEquals( 58, auteurDocument.getPaginaStart() );
//	assertEquals( null, auteurDocument.getPaginaFinish() );
//	assertEquals( 1, auteurDocument.getAuteurId() );
//	assertEquals( "Pipo de Clown", auteurDocument.getAuteur() );
//
//	auteurDocument = auteurList.get( 1 );
//	assertEquals( 2, auteurDocument.getIssueId() );
//	assertEquals( false, auteurDocument.isSpecialIssue() );
//	assertEquals( 1977, auteurDocument.getJaar() );
//	assertEquals( "March", auteurDocument.getIssueTitel() );
//	assertEquals( "Pipo cover", auteurDocument.getFrontCover() );
//	assertEquals( "Koeie back cover", auteurDocument.getBackCover() );
//	assertEquals( "Titeltje 2", auteurDocument.getStripTitel() );
//	assertEquals( "Dit is wat commentaar", auteurDocument.getStripCommentaar() );
//	assertEquals( null, auteurDocument.getStripReview() );
//	assertEquals( 69, auteurDocument.getPaginaStart() );
//	assertEquals( 70, auteurDocument.getPaginaFinish() );
//	assertEquals( 1, auteurDocument.getAuteurId() );
//	assertEquals( "Pipo de Clown", auteurDocument.getAuteur() );
//	
//	auteurDocument = auteurList.get( 2 );
//	assertEquals( 1, auteurDocument.getIssueId() );
//	assertEquals( true, auteurDocument.isSpecialIssue() );
//	assertEquals( 1977, auteurDocument.getJaar() );
//	assertEquals( "Winter", auteurDocument.getIssueTitel() );
//	assertEquals( "Olivia de Bernardis", auteurDocument.getFrontCover() );
//	assertEquals( "Luis Royo", auteurDocument.getBackCover() );
//	assertEquals( "Titeltje 1", auteurDocument.getStripTitel() );
//	assertEquals( null, auteurDocument.getStripCommentaar() );
//	assertEquals( "En hier is een review", auteurDocument.getStripReview() );
//	assertEquals( 55, auteurDocument.getPaginaStart() );
//	assertEquals( 57, auteurDocument.getPaginaFinish() );
//	assertEquals( 1, auteurDocument.getAuteurId() );
//	assertEquals( "Pipo de Clown", auteurDocument.getAuteur() );
//	
//}
//
//@Test
//public void testGetIssuesByYear()
//{
//	List<Issue> issues = converter.getIssuesByYear( 1977 );
//	assertEquals( 2, issues.size() );
//
//	Issue issue = issues.get( 0 );
//	checkIssue( issue );
//
//}
public static void checkDirectory( Directory aDirectory )
{
	assertEquals( 1, aDirectory.getId() );
	assertEquals( "Dir A", aDirectory.getName() );
	assertEquals( "2023-05-14T02:03:04", aDirectory.getDateTimeLastModified().toString() );
	assertEquals( null, aDirectory.getParent() );
	
	List<File> files = aDirectory.getFiles();
	assertEquals( 3, files.size() );
	File file = files.get( 0 );
	assertEquals( "Dir A File A", file.getName() );
	assertEquals( "2023-05-14T02:03:04", aDirectory.getDateTimeLastModified().toString() );
	assertEquals( 123456, file.getSize() );
	assertEquals( Kwaliteit.TOP, file.getKwaliteit() );
	assertEquals( "pipo review", file.getReview() );
	assertEquals( aDirectory, file.getDirectory() );
	
	List<Directory> subdirectories = aDirectory.getSubDirectories();
	assertEquals( 2, subdirectories.size() );
	
	Directory subdirectory1 = subdirectories.get( 0 );
	assertEquals( 2, subdirectory1.getId() );
	assertEquals( "Dir A/Dir B", subdirectory1.getName() );
	assertEquals( "2023-05-15T02:03:04", subdirectory1.getDateTimeLastModified().toString() );
	assertEquals( aDirectory, subdirectory1.getParent() );
	
	Directory subdirectory2 = subdirectories.get( 1 );
	assertEquals( 5, subdirectory2.getId() );
	assertEquals( "Dir A/Dir E", subdirectory2.getName() );
	assertEquals( "2023-05-18T02:03:04", subdirectory2.getDateTimeLastModified().toString() );
	assertEquals( aDirectory, subdirectory2.getParent() );
	
	List<Property> properties = file.getProperties();
	assertEquals( 3, properties.size() );

	// De volgorde van de auteurs is raadselachtig. Effe sortere dus.
	// Ook een expliciete OrderBy helpt niet
	properties.sort( Comparator.comparing( Property::getId ) );
	assertEquals( "anal", properties.get( 0 ).getName() );
	assertEquals( "asian", properties.get( 1 ).getName() );
	assertEquals( "bdsm", properties.get( 2 ).getName() );

}

}
