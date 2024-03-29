package pu.porna.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import pu.porna.bo.Directory;
import pu.porna.bo.File;
import pu.porna.bo.RowBounds;
import pu.porna.web.OrderBy;

public interface PornaService
{
public abstract Directory getFilesPerDirectory( String aDirectory, String aFromFile, RowBounds aRowBounds, OrderBy orderBy ) throws IOException;
public abstract File getFile( String aDirectory, String aFileNaam ) throws IOException;
public abstract void laadDefaults() throws IOException, URISyntaxException;
public abstract Set<String> getKwaliteiten() throws IOException;
public abstract Set<String> getProperties() throws IOException;
public abstract void saveFile( String aDirectory, String aFileName, String aKwaliteit, String aType, String aReview ) throws IOException;
}
