package pu.porna.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pu.porna.bo.FilesContainer;
import pu.porna.bo.RowBounds;
import pu.porna.dal.Directory;
import pu.porna.service.PornaService;
import pu.porna.web.PornaController;

import lombok.Data;


@Service
@Data
public class PornaServiceImpl implements PornaService
{
@SuppressWarnings( "unused" )
private static final Logger LOG = LoggerFactory.getLogger(PornaController.class);
@Autowired private FilesContainer filesContainer;

@Override
public Directory getFilesPerDirectory( String aDirectory, String aFromFile, RowBounds aRowBounds ) throws IOException
{
	return getFilesContainer().getFilesPerDirectory( aDirectory, aFromFile, aRowBounds );
}

}
