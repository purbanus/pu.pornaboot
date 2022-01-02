package pu.porna.web;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pu.porna.bo.FilesDocument;
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

@RequestMapping(value = { "/pagina.html", "/pagina" }, method = RequestMethod.GET)
// Spring 5 @GetMapping(value = { "/pagina.html", "/pagina" })
public String pagina( Model aodel )
{
	LOG.info( "Ze vragen een pagina op" );

	return "pagina";
}
@RequestMapping(value = { "/files.html", "/files" }, method = RequestMethod.GET)
//Spring 5 @GetMapping(value = { "/files.html", "/files" })
public String files( @ModelAttribute FilesRequestParameters aFilesRequestParameters, Model aModel ) throws IOException 
{
	LOG.info( "Files request gestart" );
	StopWatch timer = new StopWatch();
	timer.start();
	
	Integer rijen = aFilesRequestParameters.getRijen();
	Integer pageId = aFilesRequestParameters.getPageId();
	String directory = aFilesRequestParameters.getDirectory();
	String zoekenVanaf = aFilesRequestParameters.getZoekenVanaf() == null ? "" : aFilesRequestParameters.getZoekenVanaf();

	FilesDocument filesDocument;
	Paginator paginator;
	if ( rijen == null || pageId == null )
	{
		filesDocument = getPornaService().getFiles( directory, zoekenVanaf, Paginator.getStartingRowBounds() );
		paginator = new Paginator( filesDocument.getNumberOfFiles(), 1 );
	}
	else
	{
		paginator = new Paginator( rijen, pageId );
		filesDocument = getPornaService().getFiles( directory, zoekenVanaf, paginator.getHuidigeRowBounds() );
	}
	aModel.addAttribute( "paginator", paginator );
	aModel.addAttribute( "zoekenVanaf", zoekenVanaf );
	aModel.addAttribute( "filesDocument", filesDocument );
	LOG.info( "files request klaar in " + timer.getTime( TimeUnit.MILLISECONDS ) + "ms" );
	return "files";
}

//@RequestMapping(value = { "/fileusage.html", "/fileusage" }, method = RequestMethod.GET)
////Spring 5 @GetMapping(value = { "/fileusage.html", "/fileusage" })
//public String fileUsage( @ModelAttribute ZoekenVanafRequest aZoekenVanafRequest, Model aModel ) 
//{
//	logger.info( "FileUsage request gestart" );
//	Integer aRijen = aZoekenVanafRequest.getRijen();
//	Integer aPageId = aZoekenVanafRequest.getPageId();
//	String aZoekenVanaf = aZoekenVanafRequest.getZoekenVanaf();
//	aZoekenVanaf = aZoekenVanaf == null ? "" : aZoekenVanaf;
//	if ( aRijen == null || aPageId == null )
//	{
//		aRijen = getXrefService().getFileUsageCount( aZoekenVanaf );
//		aPageId = 1;
//	}
//	Paginator paginator = new Paginator( aRijen, aPageId );
//	aModel.addAttribute( "paginator", paginator );
//	aModel.addAttribute( "zoekenVanaf", aZoekenVanaf );
//	aModel.addAttribute( "fileUsageDocument", getXrefService().getFileUsage( aZoekenVanaf, paginator.getHuidigeRowBounds() ) );
//	return "fileusage";
//}


}
