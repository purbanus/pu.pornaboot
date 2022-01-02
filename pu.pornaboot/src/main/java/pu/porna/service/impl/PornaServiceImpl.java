package pu.porna.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pu.porna.bo.FilesDocument;
import pu.porna.bo.FilesMaker;
import pu.porna.bo.RowBounds;
import pu.porna.service.PornaService;
import pu.porna.web.PornaController;


@Service
public class PornaServiceImpl implements PornaService
{
@SuppressWarnings( "unused" )
private static final Logger LOG = LoggerFactory.getLogger(PornaController.class);
@Autowired private FilesMaker filesMaker;

public FilesMaker getFilesMaker()
{
	return filesMaker;
}

@Override
public FilesDocument getFiles( String aDirectory, String aFromFile, RowBounds aRowBounds ) throws IOException
{
	return getFilesMaker().getFiles( aDirectory, aFromFile, aRowBounds );
}

}
