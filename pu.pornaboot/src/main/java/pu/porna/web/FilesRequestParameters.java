package pu.porna.web;

public class FilesRequestParameters
{
private Integer pageId;
private Integer rijen;
private String zoekenVanaf;
private String directory;
public Integer getPageId()
{
	return pageId;
}
public Integer getRijen()
{
	return rijen;
}
public String getZoekenVanaf()
{
	return zoekenVanaf;
}
public String getDirectory()
{
	return directory;
}
public void setPageId( Integer aPageId )
{
	pageId = aPageId;
}
public void setRijen( Integer aRijen )
{
	rijen = aRijen;
}
public void setZoekenVanaf( String aZoekenVanaf )
{
	zoekenVanaf = aZoekenVanaf;
}
public void setDirectory( String aDirectory )
{
	directory = aDirectory;
}


}
