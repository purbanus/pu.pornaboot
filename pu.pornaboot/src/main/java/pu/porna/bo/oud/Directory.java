package pu.porna.bo.oud;

import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystemObject
{
private final int id;
private final String name;
private final Directory parent;
private final List<Directory> subDirectories = new ArrayList<>();
private final List<File> files = new ArrayList<>();

public Directory( int aId, String aName, Directory aParent )
{
	super();
	id = aId;
	name = aName;
	parent = aParent;
}

@Override
public int getId()
{
	return id;
}
@Override
public String getName()
{
	return name;
}
public Directory getParent()
{
	return parent;
}
public List<Directory> getSubDirectories()
{
	return subDirectories;
}
public List<File> getFiles()
{
	return files;
}

@Override
public String toString()
{
	return "D " + getName() + " " + files;
}

}
