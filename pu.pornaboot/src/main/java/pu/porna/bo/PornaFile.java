package pu.porna.bo;

import java.time.LocalDateTime;

public class PornaFile
{
private final String name;
private final long size;
private final LocalDateTime timeModified;
private final String remark;
public PornaFile( String aName, long aSize, LocalDateTime aTimeModified, String aRemark )
{
	super();
	name = aName;
	size = aSize;
	timeModified = aTimeModified;
	remark = aRemark;
}
public String getName()
{
	return name;
}
public long getSize()
{
	return size;
}
public LocalDateTime getTimeModified()
{
	return timeModified;
}
public String getRemark()
{
	return remark;
}
@Override
public String toString()
{
	return "File [name=" + name + "]";
}


}
