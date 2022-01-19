package pu.porna.dal;

import java.time.LocalDate;

public interface FileSystemObject
{
public abstract LocalDate getDateLastModified();
public abstract String getName();
public abstract long getSize();

}
