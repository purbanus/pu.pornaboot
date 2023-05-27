package pu.porna.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.RedirectView;

import pu.porna.config.PornaConfig;
import pu.porna.dal.Directory;
import pu.porna.dal.File;
import pu.porna.service.PornaService;

import lombok.Data;

/**
 * Handles requests for porna pages.
 */
@Controller
@Data
// Je kunt hier ook een @RequestMappging("fileusage") opgeven, dan krijgen de requestmappings bij de methodes dat als voorvoegsel
public class PornaController
{
private static final Logger LOG = LoggerFactory.getLogger(PornaController.class);

@Autowired private PornaService pornaService;
@Autowired private PornaConfig pornaConfig;

@GetMapping(value = { "/directory.html", "/directory" })
public String directory( @ModelAttribute DirectoryRequest aDirectoryRequest, Model aModel ) throws IOException 
{
	LOG.info( "Directory request gestart" );
	StopWatch timer = StopWatch.createStarted();
	
	Integer rijen = aDirectoryRequest.getRijen();
	Integer pageId = aDirectoryRequest.getPageId();
	String directoryString = aDirectoryRequest.getDirectory();
	if ( directoryString == null )
	{
		directoryString = getPornaConfig().getStartingDirectory();
	}
	else
	{
		directoryString = toRealDirectory( directoryString );
	}
	String zoekenVanaf = aDirectoryRequest.getZoekenVanaf() == null ? "" : aDirectoryRequest.getZoekenVanaf();
	String orderByString = aDirectoryRequest.getOrderBy();
	LOG.info( "OrderBy = " + orderByString );
	OrderBy orderBy = orderByString == null ? OrderBy.OP_NAAM : OrderBy.fromString( orderByString );

	Directory directory;
	Paginator paginator;
	if ( rijen == null || pageId == null )
	{
		directory = getPornaService().getFilesPerDirectory( directoryString, zoekenVanaf, Paginator.getStartingRowBounds(), orderBy );
		paginator = new Paginator( directory.getTotalNumberOfFiles(), 1 );
	}
	else
	{
		paginator = new Paginator( rijen, pageId );
		directory = getPornaService().getFilesPerDirectory( directoryString, zoekenVanaf, paginator.getHuidigeRowBounds(), orderBy );
	}
	aModel.addAttribute( "paginator", paginator );
	aModel.addAttribute( "zoekenVanaf", zoekenVanaf );
	aModel.addAttribute( "directory", directory );
	aModel.addAttribute( "orderBy", orderBy );
	LOG.info( "directory request klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
	return "directory";
}
@GetMapping(value = { "/laaddefaults" })
public String laadDefaults() throws IOException, URISyntaxException
{
	LOG.info( "LaadDefaults request gestart" );
	StopWatch timer = StopWatch.createStarted();
	getPornaService().laadDefaults();
	LOG.info( "LaadDefaults request klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
	return "redirect:/directory";
}
@GetMapping(value = { "/file.html" })
public String file( @ModelAttribute FileRequest aFileRequest, Model aModel ) throws IOException 
{
	LOG.info( "File request gestart" );
	StopWatch timer = StopWatch.createStarted();

	String directoryString = aFileRequest.getDirectory();
	if ( directoryString == null )
	{
		throw new RuntimeException( "Directory naam is verplicht" );
	}
	else
	{
		directoryString = toRealDirectory( directoryString );
	}
	
	String fileString = aFileRequest.getName();
	if ( fileString == null )
	{
		throw new RuntimeException( "File naam is verplicht" );
	}
	File file = getPornaService().getFile( directoryString, fileString );
	
	aModel.addAttribute( "file", file );
	aModel.addAttribute( "kwaliteiten", getPornaService().getKwaliteiten() ); 
	aModel.addAttribute( "deKwaliteit", file.getKwaliteit() );
	aModel.addAttribute( "types", getPornaService().getTypes() ); 
	aModel.addAttribute( "deType", file.getType() );

	LOG.info( "File request klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
	return "file";
}
public String toRealDirectory( String displayDirectory )
{
	return getPornaConfig().getStartingPrefix() + displayDirectory;
}
@GetMapping(value = { "/file-update.html" })
public RedirectView fileUpdate( @ModelAttribute FileUpdateRequest aFileUpdateRequest, Model aModel ) throws IOException 
{
	LOG.info( "File-update request gestart" );
	StopWatch timer = StopWatch.createStarted();
	
	String directory = aFileUpdateRequest.getDirectory();
	String realDirectory = toRealDirectory( directory );
	String fileName = aFileUpdateRequest.getFileName();
	String kwaliteit = aFileUpdateRequest.getKwaliteit();
	String type = aFileUpdateRequest.getType();
	
	getPornaService().saveFile( realDirectory, fileName, kwaliteit, type );
	
	return new RedirectView( "/file.html?directory=" + directory + "&name=" + fileName );
}
}
