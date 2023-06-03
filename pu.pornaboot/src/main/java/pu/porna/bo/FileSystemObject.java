package pu.porna.bo;

import java.time.LocalDateTime;
import java.util.Comparator;

public interface FileSystemObject
{
public static class OpNaam implements Comparator<FileSystemObject>
{
	@Override
	public int compare( FileSystemObject aO1, FileSystemObject aO2 )
	{
		return aO1.getName().compareToIgnoreCase( aO2.getName() );
	}
}
public static class OpSize implements Comparator<FileSystemObject>
{
	@Override
	public int compare( FileSystemObject aO1, FileSystemObject aO2 )
	{
		return aO1.getSize() > aO2.getSize() ? 1 : aO1.getSize() == aO2.getSize() ? 0 : -1;
	}
}
public static class OpDatum implements Comparator<FileSystemObject>
{
	@Override
	public int compare( FileSystemObject aO1, FileSystemObject aO2 )
	{
		return aO1.getDateTimeLastModified().compareTo( aO2.getDateTimeLastModified() );
	}
}
public static final OpNaam OP_NAAM = new OpNaam();
public static final OpSize OP_SIZE = new OpSize();
public static final OpDatum OP_DATUM = new OpDatum();

public abstract LocalDateTime getDateTimeLastModified();
public abstract String getName();
public abstract long getSize();

}
