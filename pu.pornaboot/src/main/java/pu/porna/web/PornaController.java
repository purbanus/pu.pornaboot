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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pu.porna.bo.FilesDocument;
import pu.porna.dal.Directory;
import pu.porna.service.PornaService;

/**
 * Handles requests for porna pages.
 */
@Controller
// Je kunt hier ook een @RequestMappging("fileusage") opgeven, dan krijgen de requestmappings bij de methodes dat als voorvoegsel
public class PornaController
{
private static final Logger LOG = LoggerFactory.getLogger(PornaController.class);
@Autowired private PornaService pornaService;	

public PornaService getPornaService()
{
	return pornaService;
}

@GetMapping(value = { "/pagina.html", "/pagina" } )
public String pagina( Model aodel )
{
	LOG.info( "Ze vragen een pagina op" );

	return "pagina";
}
@GetMapping(value = { "/directory.html", "/directory" })
public String directory( @ModelAttribute DirectoryRequestParameters aDirectoryRequestParameters, Model aModel ) throws IOException 
{
	LOG.info( "Files request gestart" );
	StopWatch timer = new StopWatch();
	timer.start();
	
	// @@NOG rijen elimineren uit request parms
	Integer rijen = aDirectoryRequestParameters.getRijen();
	Integer pageId = aDirectoryRequestParameters.getPageId();
	String directoryString = aDirectoryRequestParameters.getDirectory();
	String zoekenVanaf = aDirectoryRequestParameters.getZoekenVanaf() == null ? "" : aDirectoryRequestParameters.getZoekenVanaf();

	Directory directory;
	Paginator paginator;
	if ( rijen == null || pageId == null )
	{
		directory = getPornaService().getFilesPerDirectory( directoryString, zoekenVanaf, Paginator.getStartingRowBounds() );
		paginator = new Paginator( directory.getFiles().size(), 1 );
	}
	else
	{
		paginator = new Paginator( rijen, pageId );
		directory = getPornaService().getFilesPerDirectory( directoryString, zoekenVanaf, paginator.getHuidigeRowBounds() );
	}
	aModel.addAttribute( "paginator", paginator );
	aModel.addAttribute( "zoekenVanaf", zoekenVanaf );
	aModel.addAttribute( "directory", directory );
	LOG.info( "directory request klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
	return "directory";
}

}
