package pu.porna.dal;

import java.time.LocalDateTime;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import pu.porna.config.PornaConfig;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File implements FileSystemObject
{
private final PornaConfig pornaConfig;
private final String name;
private final long size;
private final LocalDateTime dateTimeLastModified;

private final Directory directory;
@Builder.Default
private MultiValuedMap<String, String> properties = new ArrayListValuedHashMap<>();

public String getDirectoryDisplayName()
{
	return getDirectory().getName().substring( getPornaConfig().getStartingPrefix().length() );
}
public String getKwaliteit()
{
	return String.join( ",", properties.get( "kwaliteit" ) );
}
public String getType()
{
	return String.join( ",", properties.get( "type" ) );
}
public String getVideoLocation()
{
	return "/videos/" + getDirectoryDisplayName() + "/" + getName();
}
}
