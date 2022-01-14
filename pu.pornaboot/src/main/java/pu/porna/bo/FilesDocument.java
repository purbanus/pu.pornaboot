package pu.porna.bo;

import java.util.List;

public class FilesDocument
{
private final String directory;
private final String parentDirectory;
private final List<String> subDirectories;
private final List<FileEntry> fileEntries;
private final int numberOfFiles;
public FilesDocument( String aDirectory, String aParentDirectory, List<String> aSubDirectories, List<FileEntry> aFileEntries, int aNumberOfFiles )
{
	super();
	directory = aDirectory;
	parentDirectory = aParentDirectory;
	subDirectories = aSubDirectories;
	fileEntries = aFileEntries;
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
public List<FileEntry> getFiles()
{
	return fileEntries;
}
public int getNumberOfFiles()
{
	return numberOfFiles;
}
}
