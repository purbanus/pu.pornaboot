package pu.porna.bo;

import java.io.IOException;

public interface FilesMaker
{
public abstract FilesDocument getFiles( String aDirectory, String aFromFile, RowBounds aRowBounds ) throws IOException;
}
