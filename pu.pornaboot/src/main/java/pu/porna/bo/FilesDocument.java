package pu.porna.bo;

import java.util.List;

public class FilesDocument
{
private final String directory;
private final String parentDirectory;
private final List<String> subDirectories;
private final List<PornaFile> pornaFiles;
private final int numberOfFiles;
public FilesDocument( String aDirectory, String aParentDirectory, List<String> aSubDirectories, List<PornaFile> aPornaFiles, int aNumberOfFiles )
{
	super();
	directory = aDirectory;
	parentDirectory = aParentDirectory;
	subDirectories = aSubDirectories;
	pornaFiles = aPornaFiles;
	numberOfFiles = aNumberOfFiles;
}
public String getDirectory()
{
	return directory;
}
public String getParentDidrectory()
{
	return parentDirectory;
}
public List<String> getSubDirectories()
{
	return subDirectories;
}
public List<PornaFile> getFiles()
{
	return pornaFiles;
}
public int getNumberOfFiles()
{
	return numberOfFiles;
}
}
