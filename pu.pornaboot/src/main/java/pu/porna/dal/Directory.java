package pu.porna.dal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Directory implements FileSystemObject
{
private final String name;
private final LocalDate dateLastModified;

private final Directory parent;
private final List<Directory> subDirectories = new ArrayList<>();
private final List<File> files = new ArrayList<>();

@Override
public long getSize()
{
	return 0;
}
}
