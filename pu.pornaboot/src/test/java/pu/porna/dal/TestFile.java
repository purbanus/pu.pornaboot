package pu.porna.dal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TestFile
{

@Test
public void testBuildFile() throws IOException
{
	File file = File.builder()
		.name( "pipo" )
		.dateTimeLastModified( LocalDateTime.of( 2022, 5, 14, 12, 34 ) )
		.size( 35500 )
		.build();
	assertEquals( "pipo", file.getName() );
	assertEquals( 35500, file.getSize() );
	assertEquals( null, file.getDirectory() );
	assertNotNull( file.getProperties() );
	assertTrue( file.getProperties().isEmpty() );
	
}

}
