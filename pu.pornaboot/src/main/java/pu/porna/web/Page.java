package pu.porna.web;

public class Page
{
private final String id;
private final int offset;
private final int limit;
public Page( String aId, int aOffset, int aLimit )
{
	super();
	id = aId;
	offset = aOffset;
	limit = aLimit;
}
public String getId()
{
	return id;
}
public int getOffset()
{
	return offset;
}
public int getLimit()
{
	return limit;
}
@Override
public String toString()
{
	return "Page [id=" + id + ", offset=" + offset + ", limit=" + limit + "]";
}
}
