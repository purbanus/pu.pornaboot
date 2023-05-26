package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pu.porna.bo.impl.FilesContainerImpl;
import pu.porna.dal.PornaFile.FileEntry;
import pu.porna.dal.PornaFileDefaultsLader.TypeKwaliteit;

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
	assertEquals( "vrouwen", lastElement );
}

@Test
public void testBepaalTypeKwaliteitUitDirectory()
{
	PornaFileDefaultsLader pornaFileDefaultsLader = new PornaFileDefaultsLader();
	TypeKwaliteit typeKwaliteit;
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalTypeKwaliteitUitDirectory( "/vrouwen/paartjes/top" );
	assertEquals( "paartjes", typeKwaliteit.getType() );
	assertEquals( "top", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalTypeKwaliteitUitDirectory( "/vrouwen/paartjes/goed" );
	assertEquals( "paartjes", typeKwaliteit.getType() );
	assertEquals( "goed", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalTypeKwaliteitUitDirectory( "/vrouwen/paartjes/mwah" );
	assertEquals( "paartjes", typeKwaliteit.getType() );
	assertEquals( "mwah", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalTypeKwaliteitUitDirectory( "/vrouwen/enema" );
	assertEquals( "enema", typeKwaliteit.getType() );
	assertEquals( null, typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalTypeKwaliteitUitDirectory( "/vrouwen/bdsm/spanking/top" );
	assertEquals( "bdsm-spanking", typeKwaliteit.getType() );
	assertEquals( "top", typeKwaliteit.getKwaliteit() );
	
	typeKwaliteit = pornaFileDefaultsLader.bepaalTypeKwaliteitUitDirectory( "/vrouwen/bdsm/gewoon/goed" );
	assertEquals( "bdsm-gewoon", typeKwaliteit.getType() );
	assertEquals( "goed", typeKwaliteit.getKwaliteit() );

	typeKwaliteit = pornaFileDefaultsLader.bepaalTypeKwaliteitUitDirectory( "/vrouwen/asian/vivian-hsu" );
	assertEquals( "asian", typeKwaliteit.getType() );
	assertEquals( null, typeKwaliteit.getKwaliteit() );
}
@Test
public void testMaakFileEntry()
{
	PornaFileDefaultsLader pornaFileDefaultsLader = new PornaFileDefaultsLader();
	TypeKwaliteit typeKwaliteit;
	
	typeKwaliteit = new TypeKwaliteit( "paartjes", "top" );
	FileEntry expected = new FileEntry();
	expected.getProperties().put( "type", "paartjes" );
	expected.getProperties().put( "kwaliteit", "top" );
	assertEquals( expected, pornaFileDefaultsLader.maakFileEntry( typeKwaliteit ) );
}
}
