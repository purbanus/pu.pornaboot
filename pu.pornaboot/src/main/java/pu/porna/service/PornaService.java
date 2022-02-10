package pu.porna.service;

import java.io.IOException;

import pu.porna.bo.FilesDocument;
import pu.porna.bo.RowBounds;
import pu.porna.dal.Directory;

public interface PornaService
{
public abstract Directory getFilesPerDirectory( String aDirectory, String aFromFile, RowBounds aRowBounds ) throws IOException;

}
