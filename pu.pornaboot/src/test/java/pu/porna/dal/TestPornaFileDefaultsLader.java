package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pu.porna.bo.Directory;
import pu.porna.bo.impl.FilesContainerImpl;
import pu.porna.dal.PornaFile.FileEntry;
import pu.porna.dal.PornaFileDefaultsLader.KwaliteitProperty;

import lombok.Data;

@Data
@SpringBootTest
public class TestPornaFileDefaultsLader
{
@Autowired private FilesContainerImpl filesContainer;

@Test
public void testPath() throws IOException
{
	Directory directory = getFilesContainer().getDataHolder().getDirectories().get( 0 );
	Path path = Paths.get( directory.getName() );
	Path lastPathElement = path.getName( path.getNameCount() - 1 );
	String lastElement = lastPathElement.toString();
	assertEquals( "Dir A", lastElement );
}

@Test
public void testBepaalTypeKwaliteitUitDirectory()
{
	PornaFileDefaultsLader pornaFileDefaultsLader = new PornaFileDefaultsLader();
	KwaliteitProperty typeKwaliteit;
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( "/vrouwen/paartjes/top" );
	assertEquals( "paartjes", typeKwaliteit.getProperty() );
	assertEquals( "top", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( "/vrouwen/paartjes/goed" );
	assertEquals( "paartjes", typeKwaliteit.getProperty() );
	assertEquals( "goed", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( "/vrouwen/paartjes/mwah" );
	assertEquals( "paartjes", typeKwaliteit.getProperty() );
	assertEquals( "mwah", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( "/vrouwen/enema" );
	assertEquals( "enema", typeKwaliteit.getProperty() );
	assertEquals( null, typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( "/vrouwen/bdsm/spanking/top" );
	assertEquals( "bdsm-spanking", typeKwaliteit.getProperty() );
	assertEquals( "top", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( "/vrouwen/bdsm/gewoon/goed" );
	assertEquals( "bdsm-gewoon", typeKwaliteit.getProperty() );
	assertEquals( "goed", typeKwaliteit.getKwaliteit() );

	typeKwaliteit = pornaFileDefaultsLader.bepaalKwaliteitPropertyUitDirectory( "/vrouwen/asian/vivian-hsu" );
	assertEquals( "asian", typeKwaliteit.getProperty() );
	assertEquals( null, typeKwaliteit.getKwaliteit() );
}
@Test
public void testMaakFileEntry()
{
	PornaFileDefaultsLader pornaFileDefaultsLader = new PornaFileDefaultsLader();
	KwaliteitProperty kwaliteitProperty;
	
	kwaliteitProperty = new KwaliteitProperty( "top", "paartjes" );
	FileEntry expected = new FileEntry();
	expected.getProperties().put( "property", "paartjes" );
	expected.getProperties().put( "kwaliteit", "top" );
	assertEquals( expected, pornaFileDefaultsLader.maakFileEntry( kwaliteitProperty ) );
}
}
