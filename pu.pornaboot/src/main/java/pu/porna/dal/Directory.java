package pu.porna.dal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pu.porna.config.PornaConfig;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
//@AllArgsConstructor

public class Directory implements FileSystemObject
{
private final String name;
private final LocalDateTime dateTimeLastModified;
private final PornaConfig pornaConfig;

private int totalNumberOfFiles;

private final Directory parent;

@Builder.Default
@ToString.Exclude
private List<Directory> subDirectories = new ArrayList<>();

@Builder.Default
@ToString.Exclude
private List<File> files = new ArrayList<>();

@Builder.Default
@ToString.Exclude
@Getter( AccessLevel.NONE )
@Setter( AccessLevel.NONE )
private Map<String, File> filesMap = null;

private final PornaFile pornaFile;

@Override
public long getSize()
{
	return 0;
}

public String getDisplayName()
{
	return getName().substring( getPornaConfig().getStartingPrefix().length() );
}
private Map<String, File> getFilesMap()
{
	if ( filesMap == null )
	{
		Map<String, File> newMap = new HashMap<>();
		for ( File file : getFiles() )
		{
			newMap.put( file.getName(), file );
		}
		filesMap = newMap;
	}
	return filesMap;
}
public File getFile( String aFileName )
{
	return getFilesMap().get( aFileName );
}
}
