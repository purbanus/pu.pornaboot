package pu.porna.dal;

import java.time.LocalDate;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File implements FileSystemObject
{
private final String name;
private final long size;
private final LocalDate dateLastModified;

private final Directory directory;
private final MultiValuedMap<String, String> properties = new ArrayListValuedHashMap<>();

}
