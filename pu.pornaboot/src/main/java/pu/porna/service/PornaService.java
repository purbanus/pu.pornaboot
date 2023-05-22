package pu.porna.service;

import java.io.IOException;

import pu.porna.bo.RowBounds;
import pu.porna.dal.Directory;
import pu.porna.web.OrderBy;

public interface PornaService
{
public abstract Directory getFilesPerDirectory( String aDirectory, String aFromFile, RowBounds aRowBounds, OrderBy orderBy ) throws IOException;

}
