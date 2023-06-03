package pu.porna.dal;

import java.time.LocalDateTime;

import lombok.Value;

@Value
public class FlatDocument
{
private final int directoryId;
private final String directoryName;
private final LocalDateTime directoryDateTimeLastModified;
private final Integer directoryParentId;
private final Integer subDirectoryId;
private final String subDirectoryName;
private final LocalDateTime subDirectoryDateTimeLastModified;
private final Integer subDirectoryParentId;
private final Integer fileId;
private final String fileName;
private final Long fileSize;
private final LocalDateTime fileDateTimeLastModified;
private final String fileReview;
private final String fileKwaliteit;
private final Integer propertyId;
private final String propertyName;
}
