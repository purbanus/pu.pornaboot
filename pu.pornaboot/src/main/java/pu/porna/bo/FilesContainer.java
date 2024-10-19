package pu.porna.bo;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import pu.porna.web.OrderBy;

public interface FilesContainer
{
public abstract void refresh() throws IOException;
public abstract Directory getFilesPerDirectory( String aDirectoryName, String aFromFile, RowBounds aRowBounds, OrderBy aOrderBy ) throws IOException;
public abstract File getFile( String aFileName, String aDirectoryName ) throws IOException;
public abstract Set<String> getKwaliteiten() throws IOException;
public abstract Set<String> getProperties() throws IOException;
public abstract void saveFile( String aDirectory, String aFileName, String aKwaliteit, String aProperty, String aReview ) throws IOException;
public abstract List<Directory> getAllDirectories();
}
