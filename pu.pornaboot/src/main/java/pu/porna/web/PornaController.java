package pu.porna.web;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import pu.porna.config.PornaConfig;
import pu.porna.dal.Directory;
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

@GetMapping(value = { "/pagina.html", "/pagina" } )
public String pagina( Model aodel )
{
	LOG.info( "Ze vragen een pagina op" );

	return "pagina";
}
@GetMapping(value = { "/directory.html", "/directory" })
public String directory( @ModelAttribute DirectoryRequest aDirectoryRequest, Model aModel ) throws IOException 
{
	LOG.info( "Directory request gestart" );
	StopWatch timer = new StopWatch();
	timer.start();
	
	// @@NOG rijen elimineren uit request parms
	Integer rijen = aDirectoryRequest.getRijen();
	Integer pageId = aDirectoryRequest.getPageId();
	String directoryString = aDirectoryRequest.getDirectory();
	if ( directoryString == null )
	{
		directoryString = getPornaConfig().getStartingDirectory();
	}
	else
	{
		directoryString = getPornaConfig().getStartingPrefix() + directoryString;
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
	aModel.addAttribute( "vorigeOrderBy", orderBy );
	LOG.info( "directory request klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
	return "directory";
}

}
