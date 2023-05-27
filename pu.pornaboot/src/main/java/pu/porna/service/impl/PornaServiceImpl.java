package pu.porna.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pu.porna.bo.FilesContainer;
import pu.porna.bo.RowBounds;
import pu.porna.dal.Directory;
import pu.porna.dal.File;
import pu.porna.dal.PornaFileDefaultsLader;
import pu.porna.service.PornaService;
import pu.porna.web.OrderBy;

import lombok.Data;


@Service
@Data
public class PornaServiceImpl implements PornaService
{
@SuppressWarnings( "unused" )
private static final Logger LOG = LoggerFactory.getLogger(PornaServiceImpl.class);
@Autowired private FilesContainer filesContainer;
@Autowired private PornaFileDefaultsLader pornaFileDefaultsLader;

@Override
public Directory getFilesPerDirectory( String aDirectory, String aFromFile, RowBounds aRowBounds, OrderBy aOrderBy ) throws IOException
{
	return getFilesContainer().getFilesPerDirectory( aDirectory, aFromFile, aRowBounds, aOrderBy );
}

@Override
public void laadDefaults() throws IOException, URISyntaxException
{
	getPornaFileDefaultsLader().maakPornaFiles();
}

@Override
public File getFile( String aDirectory, String aFileName ) throws IOException
{
	return getFilesContainer().getFile( aDirectory, aFileName );
}

@Override
public Set<String> getKwaliteiten() throws IOException
{
	return getFilesContainer().getKwaliteiten();
}

@Override
public Set<String> getTypes() throws IOException
{
	return getFilesContainer().getTypes();
}

@Override
public void saveFile( String aDirectory, String aFileName, String aKwaliteit, String aType ) throws IOException
{
	getFilesContainer().saveFile( aDirectory, aFileName, aKwaliteit, aType );
}

}
