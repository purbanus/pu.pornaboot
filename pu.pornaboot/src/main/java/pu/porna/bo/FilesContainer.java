package pu.porna.bo;

import java.io.IOException;

import pu.porna.dal.Directory;

public interface FilesContainer
{
public abstract void refresh() throws IOException;
public abstract Directory getFilesPerDirectory( String aDirectoryName, String aFromFile, RowBounds aRowBounds ) throws IOException;
}
