package pu.porna.dal;

import java.time.LocalDateTime;

public interface FileSystemObject
{
public abstract LocalDateTime getDateTimeLastModified();
public abstract String getName();
public abstract long getSize();

}
