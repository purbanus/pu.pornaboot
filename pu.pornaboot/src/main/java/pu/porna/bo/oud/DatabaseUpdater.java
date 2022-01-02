package pu.porna.bo.oud;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pu.porna.bo.impl.FileWalker;

@Service
public class DatabaseUpdater
{
@SuppressWarnings( "unused" )
private static final Logger LOG = LoggerFactory.getLogger( DatabaseUpdater.class );

@Value( "${porna.starting-directory}" ) private String startingDirectory;
/*OUD @Value( "${search-pattern}" )*/ private String searchPattern;

public DatabaseUpdater()
{
	// TODO Auto-generated constructor stub
}

public String getStartingDirectory()
{
	return startingDirectory;
}
public String getSearchPattern()
{
	return searchPattern;
}

public void run() throws IOException
{
	List<Directory> directories = FileWalker.walkDeFiles( startingDirectory, searchPattern );
	System.out.println( "Klaar! Directories verzameld: " + directories.size() );
}

}
