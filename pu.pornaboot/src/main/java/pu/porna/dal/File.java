package pu.porna.dal;

import java.time.LocalDateTime;

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
private final LocalDateTime dateTimeLastModified;

private final Directory directory;
@Builder.Default
private MultiValuedMap<String, String> properties = new ArrayListValuedHashMap<>();

}
