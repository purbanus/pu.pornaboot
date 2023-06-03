package pu.porna.dal;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.Property;
import pu.porna.config.PornaConfig;

import lombok.Data;

@Data
@Service
public class DirectoryConverter
{
@Autowired PornaConfig pornaConfig;
@Autowired DirectoryQueryRepository directoryQueryRepository;

public DirectoryConverter()
{
	super();
}
// Om het LazyInitializationException probleem op te lossen. Zie https://stackoverflow.com/questions/64762080/how-to-map-sql-native-query-result-into-dto-in-spring-jpa-repository?rq=1
// - In de repository definieer je de ophaalmethode als List<Tuple> getBla
// - Die Tuples converteer je naar een FlatDocument, zie convertTuplesToFlatDocuments. Het enige lastige is dat je dat hier op index moet doen, 
//   niet op veldnaam, en dat je de class moet opgeven
// - Converteer de FlatDocuments naar Issues met hun Strips, Paginas en Auteurs
List<FlatDocument> convertTuplesToFlatDocuments( List<Tuple> aTuples )
{
	return aTuples.stream()
		.map( t -> new FlatDocument( 
			  t.get(  0, Integer.class )       // directoryId
			, t.get(  1, String.class  )       // directoryName
			, toLocalDateTime( t, 2 )          // directoryDateTimeLastModified
			, t.get(  3, Integer.class )       // directoryParentId
			, t.get(  4, Integer.class )       // subDirectoryId
			, t.get(  5, String.class  )       // subDirectoryName
			, toLocalDateTime( t, 6 )          // subDirectoryDateTimeLastModified
			, t.get(  7, Integer.class )       // subDirectoryParentId
			, t.get(  8, Integer.class )       // fileId
			, t.get(  9, String.class )        // fileName
			, toLong( t, 10 )                   // fileSize
			, toLocalDateTime( t, 11 )          // fileDateTimeLastModified
			, t.get( 12, String.class )        // fileReview
			, t.get( 13, String.class )        // fileKwaliteit
			, t.get( 14, Integer.class )       // propertyId
			, t.get( 15, String.class )        // propertyName
		) )
		.collect( Collectors.toList() );
}
LocalDateTime toLocalDateTime( Tuple aTuple, int aIndex )
{
	Timestamp timestamp = aTuple.get( aIndex, Timestamp.class );
	if ( timestamp == null )
	{
		return null;
	}
	return timestamp.toLocalDateTime();
}
Long toLong( Tuple aTuple, int aIndex )
{
	BigInteger bigInteger = aTuple.get( aIndex, BigInteger.class );
	if ( bigInteger == null )
	{
		return null;
	}
	return bigInteger.longValue();
}

@SuppressWarnings( {
    "unchecked", "rawtypes"
} )
List<Directory> convertFlatDocumentsToDirectories( List<FlatDocument> aFlatDocuments )
{
	Map<Integer, Directory> directoryMap = new HashMap<>();
	MultiKeyMap fileMap = new MultiKeyMap<>();
	MultiKeyMap propertyMap = new MultiKeyMap<>();
	List<Directory> directories = new ArrayList<>();
	List<Directory> subDirectories = new ArrayList<>();
	for ( FlatDocument flatDocument : aFlatDocuments )
	{
		Directory directory = directoryMap.get( flatDocument.getDirectoryId() );
		if ( directory == null )
		{
			directory = Directory.fromFlatDocument( flatDocument );
			directories.add( directory );
			directoryMap.put( directory.getId(), directory );
			Directory parentDirectory = directoryMap.get( flatDocument.getDirectoryParentId() );
			directory.setParent( parentDirectory );
			if ( parentDirectory != null )
			{
				parentDirectory.getSubDirectories().add( directory );
			}
			directory.setPornaConfig( pornaConfig );
		}
		
		if ( flatDocument.getSubDirectoryId() != null )
		{
			Directory subDirectory = directoryMap.get( flatDocument.getSubDirectoryId() );
			if ( subDirectory == null )
			{
				subDirectory = Directory.subDirectoryFromFlatDocument( flatDocument );
				subDirectories.add( subDirectory );
				directoryMap.put( subDirectory.getId(), subDirectory );
				subDirectory.setParent( directory );
				directory.getSubDirectories().add( subDirectory );
				directory.setPornaConfig( pornaConfig );
			}
		}
		if ( flatDocument.getFileId() != null )
		{
			File file = (File) fileMap.get( flatDocument.getDirectoryId(), flatDocument.getFileId() );
			if ( file == null )
			{
				file = File.fromFlatDocument( flatDocument );
				file.setDirectory( directory );
				file.setPornaConfig( pornaConfig );
				directory.getFiles().add( file );
				fileMap.put( directory.getId(), file.getId(), file );
			}
		}
		
		if ( flatDocument.getPropertyId() != null )
		{
			File file = (File) fileMap.get( flatDocument.getDirectoryId(), flatDocument.getFileId() );
			Property property = (Property) propertyMap.get( flatDocument.getDirectoryId(), flatDocument.getFileId(), flatDocument.getPropertyId() );
			if ( property == null )
			{
				property = Property.fromFlatDocument( flatDocument );
				file.getProperties().add( property );
				propertyMap.put( directory.getId(), file.getId(), property.getId(), property );
			}
		}
	}
	for ( Directory directory : directories )
	{
		directory.setTotalNumberOfFiles( directory.getFiles().size() );
	}
	return directories;
}
/**
 * Een AuteurDocument bestaat uit een auteur en de verzameling strips waar die in voorkomt. Het is handig als je ook meteen het issue
 * bij de strip hebt, dus het is een verzameling StripIssues
 * @param aFlatDocuments
 * @return
 */
//@SuppressWarnings( {
//    "unchecked", "rawtypes"
//} )
//AuteurDocument convertFlatDocumentToAuteur( List<FlatDocument> aFlatDocuments )
//{
//	Auteur auteur = null;
//	List<StripIssueDocument> stripIssueDocuments = new ArrayList<>();
//	Map<Integer, StripIssueDocument> stripIssueMap = new HashMap<>();
//	MultiKeyMap paginaMap = new MultiKeyMap<>();
//	for ( FlatDocument flatDocument : aFlatDocuments )
//	{
//		if ( auteur == null )
//		{
//			auteur = Auteur.fromFlatDocument( flatDocument );
//		}
//		
//		StripIssueDocument stripIssueDocument = stripIssueMap.get( flatDocument.getStripId() );
//		if ( stripIssueDocument == null )
//		{
//			stripIssueDocument = StripIssueDocument.fromFlatDocument( flatDocument );
//			stripIssueDocuments.add( stripIssueDocument );
//			stripIssueMap.put( flatDocument.getStripId(), stripIssueDocument );
//		}
//		
//		Strip strip = stripIssueDocument.getStrip();
//		List<Pagina> paginas = strip.getPaginas();
//		Pagina pagina = (Pagina) paginaMap.get( flatDocument.getStripId(), flatDocument.getPaginaId() );
//		if ( pagina == null )
//		{
//			pagina = Pagina.fromFlatDocument( flatDocument );
//			paginas.add( pagina );
//			paginaMap.put( strip.getId(), pagina.getId(), pagina );
//		}
//	}
//	return new AuteurDocument( auteur, stripIssueDocuments );
//}

private List<Directory> convertTuplesToDirectories( List<Tuple> tuples )
{
	List<FlatDocument> flatDocuments = convertTuplesToFlatDocuments( tuples );
	return convertFlatDocumentsToDirectories( flatDocuments );
}
public List<Directory> getAllDirectories()
{
	List<Tuple> tuples = getDirectoryQueryRepository().getAllDirectoryTuples();
	List<Directory> directories = convertTuplesToDirectories( tuples );
	if ( directories.size() == 0 )
	{
		throw new RuntimeException( "Er zijn geen directories" );
	}
	return directories;
}
public Directory getDirectory( int aId )
{
	List<Tuple> tuples = getDirectoryQueryRepository().getDirectoryTuplesById( aId );
	List<Directory> directories = convertTuplesToDirectories( tuples );
	if ( directories.size() == 0 )
	{
		throw new RuntimeException( "Dit directory-id bestaat niet: " + aId );
	}
	else if ( directories.size() > 1 )
	{
		throw new RuntimeException( "Dit directory-id is niet uniek: " + aId );
	}
	return directories.get( 0 );
}
//public AuteurDocument getAuteurById( int aId )
//{
//	List<Tuple> tuples = getIssueQueryRepository().getAuteurTuplesById( aId );
//	List<FlatDocument> flatDocuments = convertTuplesToFlatDocuments( tuples );
//	return convertFlatDocumentToAuteur( flatDocuments );
//}


}
