package pu.porna.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryRequest
{
private Integer pageId;
private Integer rijen;
private String zoekenVanaf;
private String directory;
}
