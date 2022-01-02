package pu.porna.service;

import java.io.IOException;

import pu.porna.bo.FilesDocument;
import pu.porna.bo.RowBounds;

public interface PornaService
{
public abstract FilesDocument getFiles(String aDirectory, String aFromFile, RowBounds aRowBounds ) throws IOException;

}
