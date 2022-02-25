package pu.porna.dal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
//@AllArgsConstructor

public class Directory implements FileSystemObject
{
private final String name;
private final LocalDateTime dateTimeLastModified;
private int totalNumberOfFiles;

private final Directory parent;

@Builder.Default
@ToString.Exclude
private List<Directory> subDirectories = new ArrayList<>();

@Builder.Default
@ToString.Exclude
private List<File> files = new ArrayList<>();

private final PornaFile pornaFile;

@Override
public long getSize()
{
	return 0;
}
}
