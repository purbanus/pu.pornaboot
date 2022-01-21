package pu.porna.dal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@AllArgsConstructor
public class Directory implements FileSystemObject
{
private final String name;
private final LocalDateTime dateTimeLastModified;

private final Directory parent;
private final List<Directory> subDirectories = new ArrayList<>();
private final List<File> files = new ArrayList<>();
private final PornaFile pornaFile;

@Override
public long getSize()
{
	return 0;
}
}
