package pu.porna.bo.oud;

public class File implements FileSystemObject
{
private final int id;
private final String name;
private final Directory directory;
public File( int aId, String aName, Directory aDirectory )
{
	super();
	id = aId;
	name = aName;
	directory = aDirectory;
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
public Directory getDirectory()
{
	return directory;
}

@Override
public String toString()
{
	return "F " + directory.getName() + "/" + getName();
}
}
