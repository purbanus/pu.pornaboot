package pu.porna.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateRequest
{
private String directory;
private String fileName;
private String kwaliteit;
private String type;
}
